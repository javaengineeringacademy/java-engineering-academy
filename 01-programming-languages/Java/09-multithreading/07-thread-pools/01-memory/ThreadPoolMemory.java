package academy.javaengineering.concurrency.threadpools.memory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates thread pool memory considerations:
 * - Per-thread memory overhead
 * - Queue memory behavior
 * - Memory monitoring
 */
public class ThreadPoolMemory {

    public static void main(String[] args) throws Exception {
        perThreadMemory();
        queueMemoryBehavior();
        memoryMonitoring();
    }

    private static void perThreadMemory() {
        System.out.println("=== Per-Thread Memory Overhead ===\n");

        Runtime runtime = Runtime.getRuntime();
        long beforeGC = runtime.totalMemory() - runtime.freeMemory();
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        long before = runtime.totalMemory() - runtime.freeMemory();

        // Create many threads
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            10, 10, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );

        AtomicInteger counter = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                counter.incrementAndGet();
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        long after = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("10 threads x 100 tasks:");
        System.out.println("  Memory before: " + (before / 1024) + "KB");
        System.out.println("  Memory after: " + (after / 1024) + "KB");
        System.out.println("  Approx per thread: ~" + ((after - before) / 10 / 1024) + "KB (stack + worker)");

        pool.shutdown();
        System.out.println();
    }

    private static void queueMemoryBehavior() throws InterruptedException {
        System.out.println("=== Queue Memory Behavior ===\n");

        // Unbounded queue - tasks accumulate
        ThreadPoolExecutor unbounded = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );

        for (int i = 0; i < 50; i++) {
            final int id = i;
            unbounded.execute(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        System.out.println("Unbounded queue (1 worker, 50 tasks):");
        System.out.println("  Queue size: " + unbounded.getQueue().size());
        System.out.println("  Pool size: " + unbounded.getPoolSize());
        System.out.println("  Risk: Queue holds all pending tasks in memory");

        unbounded.shutdown();
        unbounded.awaitTermination(10, TimeUnit.SECONDS);

        // Bounded queue - backpressure
        ThreadPoolExecutor bounded = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(5),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        for (int i = 0; i < 50; i++) {
            final int id = i;
            bounded.execute(() -> {
                try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        System.out.println("\nBounded queue (capacity=5, CallerRuns):");
        System.out.println("  Queue size: " + bounded.getQueue().size());
        System.out.println("  Queue capacity: " + ((ArrayBlockingQueue<?>) bounded.getQueue()).remainingCapacity());
        System.out.println("  Safe: Queue never exceeds capacity, caller applies backpressure");

        bounded.shutdown();
        bounded.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    private static void memoryMonitoring() {
        System.out.println("=== Memory Monitoring ===\n");

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            4, 8, 30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100)
        );

        Runtime runtime = Runtime.getRuntime();
        System.out.println("JVM Memory:");
        System.out.println("  Max: " + (runtime.maxMemory() / 1024 / 1024) + "MB");
        System.out.println("  Total: " + (runtime.totalMemory() / 1024 / 1024) + "MB");
        System.out.println("  Free: " + (runtime.freeMemory() / 1024 / 1024) + "MB");

        System.out.println("\nPool Metrics:");
        System.out.println("  Pool size: " + pool.getPoolSize());
        System.out.println("  Queue size: " + pool.getQueue().size());
        System.out.println("  Completed tasks: " + pool.getCompletedTaskCount());

        pool.shutdown();
        System.out.println();
    }
}
