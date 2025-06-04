package org.pofay.jcip_example.chapter1and2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;

public class CacheTest {

    @Test
    public void getAfterPutShouldReturnCorrectValue() {
        final var cache = new SingleThreadCache<String, String>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void containsKeyReturnsTrueForExistingKey() {
        final var cache = new SingleThreadCache<String, String>();
        cache.put("key1", "value1");
        assertEquals(true, cache.containsKey("key1"));
    }

    @Test
    public void containsKeyReturnsFalseFOrMissingKey() {
        final var cache = new SingleThreadCache<String, String>();
        cache.put("key45", "SomeValue");

        assertFalse(cache.containsKey("bogus"));
    }

    @Test
    @Disabled("Showing the problem of this class in a multi-threaded scenario.")
    public void concurrentPutShouldNotGuaranteeAllValuesAreStored() throws InterruptedException {
        final var cache = new SingleThreadCache<String, String>();
        final var threadCount = 100;
        final var threads = new Thread[threadCount];

        for (var i = 0; i < threadCount; i++) {
            final var value = String.valueOf(i);
            threads[i] = new Thread(() -> {
                cache.put(value, value);
            });
        }

        for (final var thread : threads)
            thread.start();
        for (final var thread : threads)
            thread.join();

        var successCount = 0;
        for (var i = 0; i < threadCount; i++) {
            final var key = String.valueOf(i);
            if (cache.get(key) != null) {
                successCount++;
            }
        }

        assertNotEquals(threadCount, successCount, "Race condition expected: This will fail from time to time");
    }

    @Test
    public void getAfterPutShouldReturnCorrectValueForLockedCache() {
        final var cache = new LockedCache<String, String>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void containsKeyReturnsTrueForExistingKeyForLockedCache() {
        final var cache = new LockedCache<>();
        cache.put("key1", "value1");
        assertEquals(true, cache.containsKey("key1"));
    }

    @Test
    public void containsKeyReturnsFalseFOrMissingKeyForLockedCache() {
        final var cache = new LockedCache<>();
        cache.put("key45", "SomeValue");

        assertFalse(cache.containsKey("bogus"));
    }

    @Test
    public void clearWorksInSingleThreadedMannerForLockedCache() {
        final var cache = new LockedCache<>();
        cache.put("key222", "something");

        cache.clear();
        final var actual = cache.values();

        assertTrue(actual.isEmpty());
        assertFalse(cache.containsKey("key222"));
    }

    @Test
    public void concurrentPutAndGetShouldBehaveConsistently() {
        final LockedCache<String, String> cache = new LockedCache<>();
        final int threadCount = 100;
        final var threads = new Thread[threadCount];
    }

}
