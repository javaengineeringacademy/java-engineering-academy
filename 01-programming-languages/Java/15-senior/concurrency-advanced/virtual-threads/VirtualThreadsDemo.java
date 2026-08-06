package academy.javaengineering.senior.concurrency;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class VirtualThreadsDemo {

    // ============================================================
    // 1. Thread.ofVirtual() Creation
    // ============================================================

    public static void basicVirtualThreadCreation() throws Exception {
        Thread vt = Thread.ofVirtual().name("my-virtual-thread").start(() -> {
            System.out.println("Running in " + Thread.currentThread().getName());
            System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
        });
        vt.join();
    }

    public static void virtualThreadWithExecutor() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 5; i++) {
                final int id = i;
                executor.submit(() -> System.out.println("Task-" + id + " on " + Thread.currentThread().getName()));
            }
        }
    }

    // ============================================================
    // 2. Structured Concurrency (StructuredTaskScope)
    // ============================================================

    public static void structuredConcurrencyDemo() throws Exception {
        System.out.println("\n=== Structured Concurrency ===");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> userTask = scope.fork(() -> {
                Thread.sleep(Duration.ofMillis(100));
                return "User-42";
            });

            Subtask<String> orderTask = scope.fork(() -> {
                Thread.sleep(Duration.ofMillis(150));
                return "Order-99";
            });

            scope.join();

            System.out.println("User: " + userTask.get());
            System.out.println("Order: " + orderTask.get());
        }
    }

    public static void structuredConcurrencyWithFailure() throws Exception {
        System.out.println("\n=== Structured Concurrency (ShutdownOnFailure) ===");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> task1 = scope.fork(() -> {
                Thread.sleep(Duration.ofMillis(50));
                return "OK";
            });

            Subtask<String> task2 = scope.fork(() -> {
                Thread.sleep(Duration.ofMillis(10));
                throw new RuntimeException("Task 2 failed");
            });

            try {
                scope.join();
            } catch (Exception e) {
                System.out.println("One task failed, shutting down scope");
            }
        } catch (Exception e) {
            System.out.println("Scope closed due to failure: " + e.getMessage());
        }
    }

    // ============================================================
    // 3. Pinning Detection
    // ============================================================

    private static final ReentrantLock lock = new ReentrantLock();
    private static final AtomicInteger pinCount = new AtomicInteger(0);

    public static void pinningDemo() throws Exception {
        System.out.println("\n=== Pinning Detection ===");

        long start = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
                executor.submit(() -> {
                    synchronized (VirtualThreadsDemo.class) {
                        pinCount.incrementAndGet();
                        Thread.sleep(Duration.ofMillis(10));
                    }
                });
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Pinning count: " + pinCount.get());
        System.out.println("Elapsed (with pinning): " + elapsed + "ms");
        System.out.println("Hint: use -Djdk.tracePinnedThreads=full to detect pinning");
    }

    public static void avoidPinningDemo() throws Exception {
        System.out.println("\n=== Avoiding Pinning with ReentrantLock ===");

        long start = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
                executor.submit(() -> {
                    lock.lock();
                    try {
                        Thread.sleep(Duration.ofMillis(10));
                    } finally {
                        lock.unlock();
                    }
                });
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Elapsed (no pinning): " + elapsed + "ms");
    }

    // ============================================================
    // 4. ThreadLocal with Virtual Threads
    // ============================================================

    private static final ThreadLocal<String> userContext = new ThreadLocal<>();

    public static void threadLocalDemo() throws Exception {
        System.out.println("\n=== ThreadLocal with Virtual Threads ===");

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 5; i++) {
                final int id = i;
                executor.submit(() -> {
                    userContext.set("user-" + id);
                    Thread.sleep(Duration.ofMillis(20));
                    System.out.println("Thread " + Thread.currentThread().getName()
                        + " sees: " + userContext.get());
                    userContext.remove();
                });
            }
        }
    }

    // ============================================================
    // 5. Performance Comparison: Platform vs Virtual Threads
    // ============================================================

    public static void performanceComparison() throws Exception {
        System.out.println("\n=== Performance: Platform vs Virtual Threads ===");

        int taskCount = 10_000;
        Duration sleepDuration = Duration.ofMillis(50);

        // Platform threads
        long startPlatform = System.currentTimeMillis();
        try (var executor = Executors.newFixedThreadPool(200)) {
            List<Future<Long>> futures = new java.util.ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                futures.add(executor.submit(() -> {
                    Thread.sleep(sleepDuration);
                    return Thread.currentThread().getId();
                }));
            }
            for (Future<Long> f : futures) f.get();
        }
        long platformTime = System.currentTimeMillis() - startPlatform;

        // Virtual threads
        long startVirtual = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Long>> futures = new java.util.ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                futures.add(executor.submit(() -> {
                    Thread.sleep(sleepDuration);
                    return Thread.currentThread().getId();
                }));
            }
            for (Future<Long> f : futures) f.get();
        }
        long virtualTime = System.currentTimeMillis() - startVirtual;

        System.out.println("Platform threads (" + taskCount + " tasks): " + platformTime + "ms");
        System.out.println("Virtual threads  (" + taskCount + " tasks): " + virtualTime + "ms");
        System.out.println("Speedup: " + String.format("%.1fx", (double) platformTime / virtualTime));
    }

    // ============================================================
    // 6. When to Use Virtual Threads
    // ============================================================

    public static void useCasesDemo() throws Exception {
        System.out.println("\n=== Virtual Thread Use Cases ===");

        // I/O-bound workloads (databases, HTTP calls, file I/O)
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            AtomicLong totalTime = new AtomicLong();

            var start = System.currentTimeMillis();
            List<Future<Void>> futures = new java.util.ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                futures.add(executor.submit(() -> {
                    Thread.sleep(Duration.ofMillis(10));
                    totalTime.addAndGet(10);
                    return null;
                }));
            }

            for (Future<Void> f : futures) f.get();
            long elapsed = System.currentTimeMillis() - start;

            System.out.println("1000 I/O tasks completed in " + elapsed + "ms");
            System.out.println("Suitable for: DB queries, HTTP calls, file reads");
            System.out.println("Not suitable for: CPU-bound computation");
        }
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== 1. Virtual Thread Creation ===");
        basicVirtualThreadCreation();
        virtualThreadWithExecutor();

        structuredConcurrencyDemo();
        structuredConcurrencyWithFailure();

        pinningDemo();
        avoidPinningDemo();

        threadLocalDemo();

        performanceComparison();

        useCasesDemo();
    }
}
