package org.pofay.jcip_example.chapter3.assignment;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

public class AppConfigHolderTest {

    @Test
    public void multiThreadedTest() throws InterruptedException {
        var oldConfig = new AppConfig("jdbc://old", 10, false);
        var newConfig = new AppConfig("jdbc://new", 20, true);
        var holder = new NonVolatileConfigHolder(oldConfig);
        var readerThreadCount = 30;
        var latch = new CountDownLatch(1);

        var seenUpdatedConfig = new AtomicBoolean(false);
        var errors = new ConcurrentLinkedQueue<String>();

        var readers = new ArrayList<Thread>();
        for (var t = 0; t < readerThreadCount; t++) {
            var readerThread = new Thread(() -> {
                var sawInitial = false;
                var sawUpdated = false;

                try {
                    while (!sawUpdated) {
                        final var currentConfig = holder.get();
                        if (currentConfig == null) {
                            errors.add("Reader saw null config");
                            break;
                        }

                        if (currentConfig.getDbUrl().equals("jdbc://old")) {
                            sawInitial = true;
                        }

                        if (currentConfig.getDbUrl().equals("jdbc://new")) {
                            sawUpdated = true;
                            seenUpdatedConfig.set(true);
                            break;
                        }
                        Thread.sleep(1000);
                    }
                    if (!sawInitial) {
                        errors.add("Reader never saw initial config");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            });
            readers.add(readerThread);
        }

        final var writerThread = new Thread(() -> {
            try {
                Thread.sleep(5000);
                holder.updateConfiguration(newConfig);
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        writerThread.start();
        for (var readerThread : readers) {
            readerThread.start();
        }

        writerThread.join();
        for (var readerThread : readers) {
            readerThread.join();
        }

        System.out.println("Seen updated config: " + seenUpdatedConfig.get());

        if (!errors.isEmpty()) {
            for (String err : errors) {
                System.out.println("❌ " + err);
            }
            fail("Some reader threads failed visibility assertions");
        }

        if (!seenUpdatedConfig.get()) {
            System.out.println("⚠️ Readers never saw updated config. This is expected with Non-Volatile");
        }

        assertTrue(seenUpdatedConfig.get(), "At least one reader should see the updated config");
        assertTrue(errors.isEmpty(), "Readers should not see nulls or skip initial config");
    }
}