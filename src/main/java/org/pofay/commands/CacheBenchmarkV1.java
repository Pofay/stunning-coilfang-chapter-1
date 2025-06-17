package org.pofay.commands;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.pofay.jcip_example.chapter1and2.ReentrantLockedCache;
import org.pofay.jcip_example.chapter1and2.SimpleCache;
import org.pofay.jcip_example.chapter1and2.SimpleSynchronizedCache;
import org.pofay.jcip_example.chapter1and2.SynchronizedWithObjectCache;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "cache-benchmark", description = "Runs cache concurrency benchmarks")
public class CacheBenchmarkV1 implements Runnable {

    @Option(names = { "-t", "--threads" }, description = "Number of threads to use")
    int threads = 8;

    @Option(names = { "-o", "--operations" }, description = "Operations per thread")
    int operationsPerThread = 100_000;

    @Override
    public void run() {
        try {
            runBenchmarkFor("SimpleSynchronizedCache", new SimpleSynchronizedCache<>());
            runBenchmarkFor("SynchronizedWithObjectCache", new SynchronizedWithObjectCache<>());
            runBenchmarkFor("ReentrantLockedCache", new ReentrantLockedCache<>());
        } catch (InterruptedException e) {
            System.err.println("Benchmark interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    private void runBenchmarkFor(String cacheName, SimpleCache<Integer, Integer> cache) throws InterruptedException {
        System.out.println("Running benchmark for " + cacheName + " with " + threads + " threads, "
                + operationsPerThread + " ops/thread");

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threads);

        Runnable task = () -> {
            try {
                startLatch.await();
                for (int i = 0; i < operationsPerThread; i++) {
                    int key = i;
                    cache.put(key, key);
                    cache.get(key);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown();
            }
        };

        for (int i = 0; i < threads; i++) {
            new Thread(task).start();
        }

        long startTime = System.nanoTime();
        startLatch.countDown();
        doneLatch.await();

        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
        System.out.printf("%s took %d ms%n%n", cacheName, elapsedMs);
    }
}