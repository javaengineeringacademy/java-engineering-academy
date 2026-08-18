package academy.javaengineering.concurrency.examples;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VirtualThreadExamples {

    public static void main(String[] args) throws Exception {
        example1_BasicVirtualThreads();
        example2_VirtualThreadFactory();
        example3_VirtualThreadPerTask();
        example4_PlatformVsVirtual();
        example5_VirtualThreadWithLock();
    }

    // Example 1: Basic virtual thread creation
    static void example1_BasicVirtualThreads() throws Exception {
        System.out.println("=== Example 1: Basic Virtual Threads ===");

        // Java 21+ virtual threads
        Thread vThread = Thread.ofVirtual().name("my-virtual-thread").start(() -> {
            System.out.println("Running on: " + Thread.currentThread().getName());
            System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
            System.out.println("Is daemon: " + Thread.currentThread().isDaemon());
        });

        vThread.join();

        // Virtual thread with Runnable
        Runnable task = () -> {
            System.out.println("\nVirtual thread task on: " + Thread.currentThread().getName());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task completed");
        };

        Thread vt = Thread.ofVirtual().name("VT-1").start(task);
        vt.join();

        System.out.println();
    }

    // Example 2: Virtual thread factory
    static void example2_VirtualThreadFactory() throws Exception {
        System.out.println("=== Example 2: Virtual Thread Factory ===");

        // Create virtual thread factory
        var factory = Thread.ofVirtual().name("vt-", 0).factory();

        // Use with ExecutorService
        ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);

        List<java.util.concurrent.Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final int id = i;
            futures.add(executor.submit(() -> {
                System.out.println("Task-" + id + " on " + Thread.currentThread().getName());
                TimeUnit.MILLISECONDS.sleep(50);
                return "Result-" + id;
            }));
        }

        for (var future : futures) {
            System.out.println("Got: " + future.get());
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }

    // Example 3: Virtual thread per task (structured style)
    static void example3_VirtualThreadPerTask() throws Exception {
        System.out.println("=== Example 3: Virtual Thread Per Task ===");

        // Modern approach - virtual thread per task executor
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Instant start = Instant.now();

            List<java.util.concurrent.Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                final int id = i;
                futures.add(executor.submit(() -> {
                    // Simulate I/O-bound work
                    TimeUnit.MILLISECONDS.sleep(10);
                    return "Task-" + id + " done";
                }));
            }

            int completed = 0;
            for (var future : futures) {
                future.get();
                completed++;
            }

            Duration elapsed = Duration.between(start, Instant.now());
            System.out.println("Completed " + completed + " tasks in " + elapsed.toMillis() + "ms");
            System.out.println("Virtual threads excel at I/O-bound workloads");
        }

        System.out.println();
    }

    // Example 4: Platform vs Virtual threads comparison
    static void example4_PlatformVsVirtual() throws Exception {
        System.out.println("=== Example 4: Platform vs Virtual Threads ===");

        int taskCount = 1000;
        long sleepMs = 10;

        // Platform threads (limited by OS)
        System.out.println("Testing with " + taskCount + " tasks, " + sleepMs + "ms sleep each:");
        System.out.println("Platform threads - creates OS thread per task (expensive)");

        Instant start = Instant.now();
        ExecutorService platformExecutor = Executors.newFixedThreadPool(100);
        List<java.util.concurrent.Future<Void>> platformFutures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            platformFutures.add(platformExecutor.submit(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return null;
            }));
        }

        for (var f : platformFutures) f.get();
        platformExecutor.shutdown();
        Duration platformTime = Duration.between(start, Instant.now());
        System.out.println("Platform threads: " + platformTime.toMillis() + "ms");

        // Virtual threads (lightweight)
        System.out.println("\nVirtual threads - lightweight, JVM managed");

        start = Instant.now();
        try (var virtualExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<java.util.concurrent.Future<Void>> virtualFutures = new ArrayList<>();

            for (int i = 0; i < taskCount; i++) {
                virtualFutures.add(virtualExecutor.submit(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return null;
                }));
            }

            for (var f : virtualFutures) f.get();
        }
        Duration virtualTime = Duration.between(start, Instant.now());
        System.out.println("Virtual threads: " + virtualTime.toMillis() + "ms");

        System.out.println("\nVirtual threads are ideal for:");
        System.out.println("- I/O-bound tasks (HTTP calls, file I/O, database queries)");
        System.out.println("- High concurrency with many threads");
        System.out.println("- tasks that block/sleep frequently");

        System.out.println();
    }

    // Example 5: Virtual threads and synchronized (pinning)
    static void example5_VirtualThreadWithLock() throws Exception {
        System.out.println("=== Example 5: Virtual Threads & Locks ===");

        // Virtual threads CAN be pinned to carrier threads by:
        // 1. synchronized blocks (Java < 22)
        // 2. native methods

        Object monitor = new Object();

        // synchronized blocks can pin virtual threads (avoid if possible)
        Thread pinnedThread = Thread.ofVirtual().name("pinned-vt").start(() -> {
            synchronized (monitor) {
                System.out.println("Virtual thread in synchronized block on " +
                        Thread.currentThread().getName());
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        pinnedThread.join();

        // ReentrantLock is preferred for virtual threads (no pinning)
        java.util.concurrent.locks.ReentrantLock lock = new java.util.concurrent.locks.ReentrantLock();

        Thread unpinnedThread = Thread.ofVirtual().name("unpinned-vt").start(() -> {
            lock.lock();
            try {
                System.out.println("Virtual thread with ReentrantLock on " +
                        Thread.currentThread().getName());
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } finally {
                lock.unlock();
            }
        });

        unpinnedThread.join();

        // Best practice: use java.util.concurrent utilities
        java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(3);

        List<Thread> virtualThreads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int id = i;
            Thread vt = Thread.ofVirtual().name("sem-vt-" + id).start(() -> {
                try {
                    semaphore.acquire();
                    System.out.println("Task-" + id + " acquired permit");
                    TimeUnit.MILLISECONDS.sleep(50);
                    semaphore.release();
                    System.out.println("Task-" + id + " released permit");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            virtualThreads.add(vt);
        }

        for (Thread t : virtualThreads) t.join();

        System.out.println("\nRecommendations for virtual threads:");
        System.out.println("- Prefer ReentrantLock over synchronized");
        System.out.println("- Use java.util.concurrent utilities");
        System.out.println("- Avoid long-running CPU-bound tasks");
        System.out.println("- Use jdk.tracePinnedThreads for debugging");

        System.out.println();
    }
}
