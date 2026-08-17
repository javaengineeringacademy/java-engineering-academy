package academy.javaengineering.concurrency.executor.memory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates ExecutorService memory considerations:
 * - Thread stack memory impact
 * - Queue memory behavior
 * - Memory monitoring
 */
public class ExecutorServiceMemory {

    private static final AtomicInteger taskCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        threadStackMemoryImpact();
        queueMemoryBehavior();
        memoryMonitoring();
    }

    private static void threadStackMemoryImpact() {
        System.out.println("=== Thread Stack Memory Impact ===\n");

        Runtime runtime = Runtime.getRuntime();
        long beforeUsed = runtime.totalMemory() - runtime.freeMemory();

        // Small pool
        ExecutorService smallPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            smallPool.execute(() -> {
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        smallPool.shutdown();
        try { smallPool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        long afterSmall = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Small pool (2 threads): ~" + ((afterSmall - beforeUsed) / 1024) + "KB used");

        // Large pool
        beforeUsed = runtime.totalMemory() - runtime.freeMemory();
        ExecutorService largePool = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 200; i++) {
            largePool.execute(() -> {
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        largePool.shutdown();
        try { largePool.awaitTermination(10, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        long afterLarge = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Large pool (50 threads): ~" + ((afterLarge - beforeUsed) / 1024) + "KB used");
        System.out.println("Note: Each thread allocates ~1MB stack space by default\n");
    }

    private static void queueMemoryBehavior() throws InterruptedException {
        System.out.println("=== Queue Memory Behavior ===\n");

        // Unbounded queue
        ExecutorService unbounded = Executors.newFixedThreadPool(1);
        AtomicInteger unboundedCount = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            final int taskId = taskCounter.incrementAndGet();
            unbounded.execute(() -> {
                unboundedCount.incrementAndGet();
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        unbounded.shutdown();
        unbounded.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Unbounded queue: processed " + unboundedCount.get() + " tasks");
        System.out.println("Risk: If tasks submitted faster than processed, queue grows unbounded → OOM\n");

        // Bounded queue
        ThreadPoolExecutor bounded = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        AtomicInteger boundedCount = new AtomicInteger(0);
        for (int i = 0; i < 100; i++) {
            final int taskId = taskCounter.incrementAndGet();
            bounded.execute(() -> {
                boundedCount.incrementAndGet();
                try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        bounded.shutdown();
        bounded.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Bounded queue (capacity=10): processed " + boundedCount.get() + " tasks");
        System.out.println("Safe: CallerRunsPolicy applies backpressure, queue never exceeds 10\n");
    }

    private static void memoryMonitoring() {
        System.out.println("=== Memory Monitoring ===\n");

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            4, 8, 30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100)
        );

        Runtime runtime = Runtime.getRuntime();
        System.out.println("JVM Memory:");
        System.out.println("  Max memory: " + (runtime.maxMemory() / 1024 / 1024) + "MB");
        System.out.println("  Total memory: " + (runtime.totalMemory() / 1024 / 1024) + "MB");
        System.out.println("  Free memory: " + (runtime.freeMemory() / 1024 / 1024) + "MB");

        System.out.println("\nPool Metrics:");
        System.out.println("  Pool size: " + pool.getPoolSize());
        System.out.println("  Active count: " + pool.getActiveCount());
        System.out.println("  Queue size: " + pool.getQueue().size());
        System.out.println("  Queue remaining capacity: " + ((ArrayBlockingQueue<?>) pool.getQueue()).remainingCapacity());

        pool.shutdown();
        System.out.println();
    }
}
