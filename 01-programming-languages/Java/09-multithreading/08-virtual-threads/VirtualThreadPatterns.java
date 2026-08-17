import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * Virtual Thread Patterns (Java 21)
 *
 * Common patterns and best practices for using virtual threads:
 * - Fan-out / Fan-in
 * - Thread confinement
 * - Scoped values (instead of ThreadLocal)
 * - Semaphore for rate limiting
 *
 * Expected output:
 * === Fan-Out / Fan-In Pattern ===
 * Fan-out: dispatched 100 tasks
 * Fan-in: collected 100 results in 50ms
 *
 * === Thread Confinement Pattern ===
 * Thread-local counter: 1000
 *
 * === Scoped Value Pattern ===
 * Request ID: req-123
 * Nested scope: req-123 (visible to child)
 */
public class VirtualThreadPatterns {

    // Scoped value (Java 21) - replacement for ThreadLocal with virtual threads
    // private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        fanOutFanIn();
        threadConfinement();
        scopedValuePattern();
        rateLimitingPattern();
    }

    // =========================================================
    // 1. FAN-OUT / FAN-IN PATTERN
    // =========================================================
    static void fanOutFanIn() throws Exception {
        System.out.println("=== Fan-Out / Fan-In Pattern ===\n");

        int taskCount = 100;

        // --- Before Java 21: CompletableFuture with fixed pool ---
        // ExecutorService pool = Executors.newFixedThreadPool(10);
        // List<CompletableFuture<String>> futures = new ArrayList<>();
        // for (int i = 0; i < taskCount; i++) {
        //     futures.add(CompletableFuture.supplyAsync(() -> process(i), pool));
        // }
        // List<String> results = futures.stream()
        //     .map(CompletableFuture::join)
        //     .toList();

        // --- With Java 21: Virtual threads with structured concurrency ---
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Fan-out: submit all tasks to virtual threads
            java.util.List<Future<String>> futures = new java.util.ArrayList<>();
            for (int i = 0; i < taskCount; i++) {
                final int taskId = i;
                futures.add(executor.submit(() -> processTask(taskId)));
            }

            // Fan-in: collect all results
            java.util.List<String> results = futures.stream()
                    .map(f -> {
                        try { return f.get(); }
                        catch (Exception e) { return "error"; }
                    })
                    .toList();

            Duration elapsed = Duration.between(start, Instant.now());
            System.out.println("Fan-out: dispatched " + taskCount + " tasks");
            System.out.println("Fan-in: collected " + results.size() + " results in " + elapsed.toMillis() + "ms");
        }

        System.out.println();
    }

    // =========================================================
    // 2. THREAD CONFINEMENT PATTERN
    // =========================================================
    static void threadConfinement() throws Exception {
        System.out.println("=== Thread Confinement Pattern ===\n");

        // --- Before Java 21: ThreadLocal (problematic with virtual threads) ---
        // private static ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);
        // ThreadLocal creates a copy per thread; with millions of virtual threads,
        // this wastes massive memory.

        // --- With Java 21: Use ScopedValue or pass data explicitly ---
        AtomicInteger globalCounter = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1000; i++) {
                executor.submit(() -> {
                    // Each virtual thread increments independently
                    globalCounter.incrementAndGet();
                });
            }
        }

        System.out.println("Thread-local counter: " + globalCounter.get());

        // ScopedValue pattern (Java 21 - preview)
        // ScopedValue.where(REQUEST_ID, "req-" + id).run(() -> {
        //     // REQUEST_ID.get() returns "req-" + id within this scope
        //     // Automatically cleaned up when scope exits
        //     processRequest();
        // });

        System.out.println("ScopedValue replaces ThreadLocal for virtual threads");
        System.out.println("  - No memory leak risk");
        System.out.println("  - Automatically scoped to task execution");
        System.out.println("  - Immutable - cannot be modified from child scope\n");
    }

    // =========================================================
    // 3. SCOPED VALUE PATTERN
    // =========================================================
    static void scopedValuePattern() throws Exception {
        System.out.println("=== Scoped Value Pattern ===\n");

        // Simulating ScopedValue behavior (preview feature)
        String requestId = "req-123";

        // In real code with ScopedValue:
        // ScopedValue.where(REQUEST_ID, requestId).run(() -> {
        //     System.out.println("Request ID: " + REQUEST_ID.get());
        //     handleNestedScope();
        // });

        // Demonstrating the concept with virtual threads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            Future<Void> future = executor.submit(() -> {
                // This scope has access to requestId
                System.out.println("Request ID: " + requestId);

                // Nested scope also has access
                Future<Void> nested = executor.submit(() -> {
                    System.out.println("Nested scope: " + requestId + " (visible to child)");
                    return null;
                });

                try { nested.get(); } catch (Exception e) { /* ignore */ }
                return null;
            });

            try { future.get(); } catch (Exception e) { /* ignore */ }
        }

        System.out.println();
    }

    // =========================================================
    // 4. RATE LIMITING WITH SEMAPHORE
    // =========================================================
    static void rateLimitingPattern() throws Exception {
        System.out.println("=== Rate Limiting Pattern ===\n");

        // --- Before Java 21: Rate limiting with thread pool size ---
        // ExecutorService pool = Executors.newFixedThreadPool(10); // max 10 concurrent

        // --- With Java 21: Semaphore + virtual threads ---
        // Virtual threads can be created in millions, so use Semaphore to limit concurrency
        Semaphore semaphore = new Semaphore(10); // Allow 10 concurrent
        AtomicInteger concurrent = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 100; i++) {
                final int taskId = i;
                executor.submit(() -> {
                    try {
                        semaphore.acquire(); // Limit to 10 concurrent
                        int current = concurrent.incrementAndGet();
                        maxConcurrent.updateAndGet(prev -> Math.max(prev, current));

                        Thread.sleep(10); // Simulate work

                        concurrent.decrementAndGet();
                        semaphore.release();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }

        System.out.println("Semaphore limited concurrent access to 10");
        System.out.println("Max concurrent threads observed: " + maxConcurrent.get());
        System.out.println("(Without semaphore, all 100 would run concurrently)\n");
    }

    static String processTask(int taskId) {
        try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return "result-" + taskId;
    }
}
