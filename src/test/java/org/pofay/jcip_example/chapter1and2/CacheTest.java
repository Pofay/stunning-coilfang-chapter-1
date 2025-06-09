package org.pofay.jcip_example.chapter1and2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class CacheTest {

    @Test
    public void getAfterPutShouldReturnCorrectValue() {
        final var cache = new NotThreadSafeCache<String, String>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void containsKeyReturnsTrueForExistingKey() {
        final var cache = new NotThreadSafeCache<String, String>();
        cache.put("key1", "value1");
        assertEquals(true, cache.containsKey("key1"));
    }

    @Test
    public void containsKeyReturnsFalseFOrMissingKey() {
        final var cache = new NotThreadSafeCache<String, String>();
        cache.put("key45", "SomeValue");

        assertFalse(cache.containsKey("bogus"));
    }

    @Test
    @Disabled("Showing the problem of this class in a multi-threaded scenario.")
    public void concurrentPutShouldNotGuaranteeAllValuesAreStored() throws InterruptedException {
        final var cache = new NotThreadSafeCache<String, String>();
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
        final var cache = new ReentrantLockedCache<String, String>();
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void containsKeyReturnsTrueForExistingKeyForLockedCache() {
        final var cache = new ReentrantLockedCache<>();
        cache.put("key1", "value1");
        assertEquals(true, cache.containsKey("key1"));
    }

    @Test
    public void containsKeyReturnsFalseFOrMissingKeyForLockedCache() {
        final var cache = new ReentrantLockedCache<>();
        cache.put("key45", "SomeValue");

        assertFalse(cache.containsKey("bogus"));
    }

    @Test
    public void clearWorksInSingleThreadedMannerForLockedCache() {
        final var cache = new ReentrantLockedCache<>();
        cache.put("key222", "something");

        cache.clear();
        final var actual = cache.values();

        assertTrue(actual.isEmpty());
        assertFalse(cache.containsKey("key222"));
    }

    @Test
    public void concurrentPutAndGetShouldBehaveConsistently() throws InterruptedException {
        final ReentrantLockedCache<String, String> cache = new ReentrantLockedCache<>();
        final int threadCount = 10;
        final var entriesPerThread = 100;
        final var threads = new Thread[threadCount];

        for (var i = 0; i < threadCount; i++) {
            final var threadId = i;
            threads[i] = new Thread(() -> {
                for (var j = 0; j < entriesPerThread; j++) {
                    final var key = String.valueOf(threadId * 1000 + j);
                    cache.put(key, "val" + key);
                }
            });
        }

        for (var t : threads)
            t.start();

        for (var t : threads)
            t.join();

        final var expectedSize = threadCount * entriesPerThread;
        assertEquals(expectedSize, cache.size());

        assertEquals("val0", cache.get("0"));
        assertEquals("val9009", cache.get("9009"));
    }

    @Test
    public void usingNotThreadSafeTestWithReentrantCache() throws InterruptedException {
        final var cache = new ReentrantLockedCache<String, String>();
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

        assertEquals(threadCount, successCount);
    }

    @Test
    public void stressTestConcurrentAccess() throws InterruptedException {
        final ReentrantLockedCache<Integer, String> cache = new ReentrantLockedCache<>();
        final int opsPerThread = 1000;
        final int threadCount = 5;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    cache.put(j, "val" + j);
                    cache.get(j);
                    cache.containsKey(j);
                }
            });
        }

        for (Thread t : threads)
            t.start();
        for (Thread t : threads)
            t.join();

        assertTrue(cache.size() <= opsPerThread); // Upper bound (some keys get overwritten)
    }

    @Test
    public void concurrentPutAndGetShouldBehaveConsistentlyForSynchronizedWithObjectCache()
            throws InterruptedException {
        final var cache = new SynchronizedWithObjectCache<String, String>();
        final int threadCount = 10;
        final var entriesPerThread = 100;
        final var threads = new Thread[threadCount];

        for (var i = 0; i < threadCount; i++) {
            final var threadId = i;
            threads[i] = new Thread(() -> {
                for (var j = 0; j < entriesPerThread; j++) {
                    final var key = String.valueOf(threadId * 1000 + j);
                    cache.put(key, "val" + key);
                }
            });
        }

        for (var t : threads)
            t.start();

        for (var t : threads)
            t.join();

        final var expectedSize = threadCount * entriesPerThread;
        assertEquals(expectedSize, cache.size());

        assertEquals("val0", cache.get("0"));
        assertEquals("val9009", cache.get("9009"));
    }

    @Test
    public void usingNotThreadSafeTestWithSynchronizedObjectCache() throws InterruptedException {
        final var cache = new SynchronizedWithObjectCache<String, String>();
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

        assertEquals(threadCount, successCount);
    }

    @Test
    public void stressTestConcurrentAccessForSynchronizedWithObjectCache() throws InterruptedException {
        final var cache = new SynchronizedWithObjectCache<Integer, String>();
        final int opsPerThread = 1000;
        final int threadCount = 5;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    cache.put(j, "val" + j);
                    cache.get(j);
                    cache.containsKey(j);
                }
            });
        }

        for (Thread t : threads)
            t.start();
        for (Thread t : threads)
            t.join();

        assertTrue(cache.size() <= opsPerThread); // Upper bound (some keys get overwritten)
    }

    @Test
    public void concurrentPutAndGetShouldBehaveConsistentlyForSynchronizedCache() throws InterruptedException {
        final var cache = new SimpleSynchronizedCache<String, String>();
        final int threadCount = 10;
        final var entriesPerThread = 100;
        final var threads = new Thread[threadCount];

        for (var i = 0; i < threadCount; i++) {
            final var threadId = i;
            threads[i] = new Thread(() -> {
                for (var j = 0; j < entriesPerThread; j++) {
                    final var key = String.valueOf(threadId * 1000 + j);
                    cache.put(key, "val" + key);
                }
            });
        }

        for (var t : threads)
            t.start();

        for (var t : threads)
            t.join();

        final var expectedSize = threadCount * entriesPerThread;
        assertEquals(expectedSize, cache.size());

        assertEquals("val0", cache.get("0"));
        assertEquals("val9009", cache.get("9009"));
    }

    @Test
    public void usingNotThreadSafeTestWithSynchronizedCache() throws InterruptedException {
        final var cache = new SimpleSynchronizedCache<String, String>();
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

        assertEquals(threadCount, successCount);
    }


    @Test
    public void stressTestConcurrentAccessForSynchronizedCache() throws InterruptedException {
        final var cache = new SimpleSynchronizedCache<Integer, String>();
        final int opsPerThread = 1000;
        final int threadCount = 5;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < opsPerThread; j++) {
                    cache.put(j, "val" + j);
                    cache.get(j);
                    cache.containsKey(j);
                }
            });
        }

        for (Thread t : threads)
            t.start();
        for (Thread t : threads)
            t.join();

        assertTrue(cache.size() <= opsPerThread); // Upper bound (some keys get overwritten)
    }
}
