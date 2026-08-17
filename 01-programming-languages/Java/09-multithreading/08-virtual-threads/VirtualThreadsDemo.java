import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;

/**
 * Virtual Threads - Basic Demo (Java 21)
 *
 * Virtual threads are lightweight threads managed by the JVM, not the OS.
 * They enable millions of concurrent threads on a single JVM.
 *
 * Key differences from platform threads:
 * - Platform threads: 1 OS thread per Java thread, expensive to create
 * - Virtual threads: many virtual threads per carrier thread, cheap to create
 *
 * Expected output (times may vary):
 * === Basic Virtual Thread Creation ===
 * Platform thread created
 * Virtual thread created
 * Platform thread finished in 1003ms
 * Virtual thread finished in 1002ms
 *
 * === Virtual Thread Executor ===
 * Task 0 completed
 * Task 1 completed
 * ...
 * All 1000 tasks completed in ~500ms (much faster than platform threads)
 *
 * === Structured Task Scope (Preview) ===
 * Result A: Hello
 * Result B: World
 */
public class VirtualThreadsDemo {

    public static void main(String[] args) throws Exception {
        basicCreation();
        executorServiceExample();
        structuredConcurrency();
        performanceComparison();
    }

    // =========================================================
    // 1. BASIC VIRTUAL THREAD CREATION
    // =========================================================
    static void basicCreation() throws InterruptedException {
        System.out.println("=== Basic Virtual Thread Creation ===\n");

        // --- Before Java 21: Platform threads only ---
        // Thread platformThread = new Thread(() -> {
        //     System.out.println("Running on platform thread");
        //     try { Thread.sleep(1000); } catch (Exception e) {}
        // });
        // platformThread.start();  // Each platform thread = 1 OS thread

        // --- With Java 21: Virtual threads ---
        // Method 1: Thread.ofVirtual().start()
        Thread vt1 = Thread.ofVirtual().name("my-virtual-thread").start(() -> {
            System.out.println("Running on virtual thread: " + Thread.currentThread());
            System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
            try {
                Thread.sleep(500); // Virtual thread can sleep without blocking OS thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Method 2: Thread.startVirtualThread()
        Thread vt2 = Thread.startVirtualThread(() -> {
            System.out.println("Started via startVirtualThread()");
        });

        // Method 3: VirtualThread builder
        Thread vt3 = Thread.ofVirtual()
                .name("builder-thread")
                .start(() -> {
                    System.out.println("Started via builder pattern");
                });

        // Join to wait for completion
        vt1.join();
        vt2.join();
        vt3.join();

        System.out.println();
    }

    // =========================================================
    // 2. VIRTUAL THREAD EXECUTOR SERVICE
    // =========================================================
    static void executorServiceExample() throws InterruptedException {
        System.out.println("=== Virtual Thread Executor Service ===\n");

        Instant start = Instant.now();

        // --- Before Java 21: Fixed thread pool (limited concurrency) ---
        // ExecutorService executor = Executors.newFixedThreadPool(10);
        // This limits to 10 concurrent tasks - creating more just queues them

        // --- With Java 21: Unbounded virtual thread executor ---
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Create 1000 virtual threads - this would fail with platform threads
            for (int i = 0; i < 1000; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    // Simulate I/O-bound work
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    // System.out.println("Task " + taskId + " completed");
                });
            }
        } // try-with-resources auto-calls shutdownNow()

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("1000 virtual threads completed in: " + elapsed.toMillis() + "ms");
        System.out.println("(With platform threads, this would take ~10+ seconds)\n");
    }

    // =========================================================
    // 3. STRUCTURED CONCURRENCY (Preview in Java 21)
    // =========================================================
    static void structuredConcurrency() throws Exception {
        System.out.println("=== Structured Task Scope ===\n");

        // --- Before Java 21: Manual thread management ---
        // CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> fetchA());
        // CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> fetchB());
        // String resultA = f1.join();
        // String resultB = f2.join();
        // No automatic cleanup if one fails!

        // --- With Java 21: Structured Task Scope ---
        // try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
        //     Subtask<String> a = scope.fork(() -> fetchA());
        //     Subtask<String> b = scope.fork(() -> fetchB());
        //     scope.join();            // Wait for both
        //     scope.throwIfFailed();   // Propagate errors
        //     // Both subtasks guaranteed to complete before scope closes
        //     String result = a.get() + " " + b.get();
        // }

        // Simplified demonstration (StructuredTaskScope is preview - compile with --enable-preview)
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

            System.out.println("Result A: " + resultA);
            System.out.println("Result B: " + resultB);
            System.out.println("Combined: " + resultA + " " + resultB);
        }

        System.out.println();
    }

    // =========================================================
    // 4. PERFORMANCE COMPARISON
    // =========================================================
    static void performanceComparison() throws Exception {
        System.out.println("=== Platform vs Virtual Thread Performance ===\n");

        int taskCount = 5000;
        long sleepMs = 10;

        // Platform threads (limited pool)
        Instant start1 = Instant.now();
        try (ExecutorService executor = Executors.newFixedThreadPool(200)) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
        Duration platformTime = Duration.between(start1, Instant.now());

        // Virtual threads
        Instant start2 = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
        Duration virtualTime = Duration.between(start2, Instant.now());

        System.out.println("Tasks: " + taskCount + " (each sleeps " + sleepMs + "ms)");
        System.out.println("Platform threads (pool=200): " + platformTime.toMillis() + "ms");
        System.out.println("Virtual threads:            " + virtualTime.toMillis() + "ms");
        System.out.println("Virtual threads are ~" + (platformTime.toMillis() / Math.max(virtualTime.toMillis(), 1)) + "x faster for I/O-bound tasks");
        System.out.println();
    }

    static String fetchA() {
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return "Hello";
    }

    static String fetchB() {
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return "World";
    }
}
