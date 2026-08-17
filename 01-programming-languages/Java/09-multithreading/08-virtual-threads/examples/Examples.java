package academy.javaengineering.concurrency.virtualthreads.examples;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * Virtual threads examples covering:
 * - Basic creation
 * - Executor service
 * - Pinning and fixes
 * - Structured concurrency
 * - Performance comparison
 */
public class Examples {

    public static void main(String[] args) throws Exception {
        basicCreationExample();
        virtualThreadExecutorExample();
        pinningExample();
        structuredConcurrencyExample();
        performanceComparisonExample();
    }

    private static void basicCreationExample() throws InterruptedException {
        System.out.println("=== Basic Virtual Thread Creation ===\n");

        Thread vt1 = Thread.ofVirtual().name("my-vt").start(() -> {
            System.out.println("  Running on: " + Thread.currentThread().getName());
            System.out.println("  Is virtual: " + Thread.currentThread().isVirtual());
        });

        Thread vt2 = Thread.startVirtualThread(() -> {
            System.out.println("  Started via startVirtualThread()");
        });

        vt1.join();
        vt2.join();
        System.out.println();
    }

    private static void virtualThreadExecutorExample() throws Exception {
        System.out.println("=== Virtual Thread Executor ===\n");

        Instant start = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("  1000 virtual threads completed in: " + elapsed.toMillis() + "ms\n");
    }

    private static void pinningExample() throws Exception {
        System.out.println("=== Pinning Example ===\n");

        Object syncLock = new Object();
        java.util.concurrent.locks.ReentrantLock reLock = new java.util.concurrent.locks.ReentrantLock();
        int taskCount = 50;
        long sleepMs = 20;

        // Pinned with synchronized
        Instant start1 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    synchronized (syncLock) {
                        try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Duration pinned = Duration.between(start1, Instant.now());

        // Not pinned with ReentrantLock
        Instant start2 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    reLock.lock();
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    finally { reLock.unlock(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Duration unpinned = Duration.between(start2, Instant.now());

        System.out.println("  synchronized (pinned): " + pinned.toMillis() + "ms");
        System.out.println("  ReentrantLock (unpinned): " + unpinned.toMillis() + "ms");
        System.out.println("  Pinning overhead: ~" + (pinned.toMillis() / Math.max(unpinned.toMillis(), 1)) + "x\n");
    }

    private static void structuredConcurrencyExample() throws Exception {
        System.out.println("=== Structured Concurrency (via ExecutorService) ===\n");

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<String> futureA = executor.submit(() -> {
                Thread.sleep(200);
                return "Hello";
            });

            Future<String> futureB = executor.submit(() -> {
                Thread.sleep(150);
                return "World";
            });

            String resultA = futureA.get();
            String resultB = futureB.get();

            System.out.println("  Result A: " + resultA);
            System.out.println("  Result B: " + resultB);
            System.out.println("  Combined: " + resultA + " " + resultB);
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println();
    }

    private static void performanceComparisonExample() throws Exception {
        System.out.println("=== Platform vs Virtual Thread Performance ===\n");

        int taskCount = 5000;
        long sleepMs = 10;

        Instant start1 = Instant.now();
        try (ExecutorService executor = Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Duration platformTime = Duration.between(start1, Instant.now());

        Instant start2 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        Duration virtualTime = Duration.between(start2, Instant.now());

        System.out.println("  Tasks: " + taskCount + " (each sleeps " + sleepMs + "ms)");
        System.out.println("  Platform threads (pool=200): " + platformTime.toMillis() + "ms");
        System.out.println("  Virtual threads: " + virtualTime.toMillis() + "ms");
        System.out.println("  Speedup: ~" + (platformTime.toMillis() / Math.max(virtualTime.toMillis(), 1)) + "x\n");
    }
}
