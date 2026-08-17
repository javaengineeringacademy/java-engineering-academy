package academy.javaengineering.concurrency.threadpools.examples;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread pool configuration examples:
 * - Pool sizing for different workloads
 * - Custom ThreadPoolExecutor configuration
 * - Monitoring and metrics
 * - Rejection handling
 */
public class Examples {

    private static final AtomicInteger taskCounter = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        cpuBoundPool();
        ioBoundPool();
        customThreadPool();
        monitorPoolMetrics();
        rejectionHandlingExample();
    }

    private static void cpuBoundPool() {
        System.out.println("=== CPU-Bound Pool (cores + 1) ===\n");

        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            cores + 1, cores + 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );

        long start = System.currentTimeMillis();
        for (int i = 0; i < cores * 2; i++) {
            pool.execute(() -> {
                long sum = 0;
                for (int j = 0; j < 1_000_000; j++) sum += j;
            });
        }

        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("  " + cores + " cores → pool size: " + (cores + 1));
        System.out.println("  Completed in " + (System.currentTimeMillis() - start) + "ms\n");
    }

    private static void ioBoundPool() {
        System.out.println("=== I/O-Bound Pool (cores * wait/compute ratio) ===\n");

        int cores = Runtime.getRuntime().availableProcessors();
        int waitRatio = 4; // 80% wait, 20% compute
        int poolSize = cores * waitRatio;

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            poolSize, poolSize, 30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            pool.execute(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        pool.shutdown();
        try { pool.awaitTermination(10, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println("  " + cores + " cores, 80% wait → pool size: " + poolSize);
        System.out.println("  100 I/O tasks completed in " + (System.currentTimeMillis() - start) + "ms\n");
    }

    private static void customThreadPool() {
        System.out.println("=== Custom ThreadPoolExecutor ===\n");

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2,  // core
            4,  // max
            30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        pool.allowCoreThreadTimeOut(true);

        for (int i = 0; i < 12; i++) {
            final int id = taskCounter.incrementAndGet();
            pool.execute(() -> {
                System.out.println("  Task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        System.out.println("\n  Pool size: " + pool.getPoolSize());
        System.out.println("  Queue size: " + pool.getQueue().size());
        System.out.println("  Completed: " + pool.getCompletedTaskCount());

        pool.shutdown();
        try { pool.awaitTermination(5, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }

    private static void monitorPoolMetrics() {
        System.out.println("=== Pool Monitoring ===\n");

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            2, 4, 30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10)
        );

        for (int i = 0; i < 8; i++) {
            final int id = taskCounter.incrementAndGet();
            pool.execute(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });

            if (i % 2 == 0) {
                System.out.println("  [Metrics] Active: " + pool.getActiveCount()
                    + ", Pool: " + pool.getPoolSize()
                    + ", Queue: " + pool.getQueue().size()
                    + ", Completed: " + pool.getCompletedTaskCount());
            }
        }

        pool.shutdown();
        try { pool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        System.out.println("\n  Final:");
        System.out.println("  Pool size: " + pool.getPoolSize());
        System.out.println("  Largest pool size: " + pool.getLargestPoolSize());
        System.out.println("  Completed tasks: " + pool.getCompletedTaskCount());
        System.out.println();
    }

    private static void rejectionHandlingExample() {
        System.out.println("=== Rejection Handling ===\n");

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2),
            (r, executor) -> {
                System.out.println("  REJECTED: " + ((Runnable)r).toString() + " - caller will run it");
            }
        );

        for (int i = 0; i < 6; i++) {
            final int id = taskCounter.incrementAndGet();
            pool.execute(() -> {
                System.out.println("  Executing task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        pool.shutdown();
        try { pool.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }
}
