package academy.javaengineering.concurrency.threadpools.practices;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread pool configuration practice exercises.
 * Complete the TODO sections in each method.
 */
public class Practices {

    // TODO 1: Create a properly sized CPU-bound thread pool
    // Use the formula: cores + 1
    public ThreadPoolExecutor createCpuBoundPool() {
        // TODO: Create ThreadPoolExecutor with correct sizing
        return null;
    }

    // TODO 2: Create a properly sized I/O-bound thread pool
    // Use the formula: cores * (1 + waitTime / computeTime)
    // Assume 80% wait time
    public ThreadPoolExecutor createIoBoundPool() {
        // TODO: Create ThreadPoolExecutor with correct sizing
        return null;
    }

    // TODO 3: Create a ThreadPoolExecutor with bounded queue and CallerRunsPolicy
    // core=2, max=4, queue capacity=10
    public ThreadPoolExecutor createBoundedPool() {
        // TODO: Create ThreadPoolExecutor with ArrayBlockingQueue
        return null;
    }

    // TODO 4: Monitor pool metrics while processing tasks
    // Return a string with: active count, pool size, queue size, completed count
    public String getPoolMetrics(ThreadPoolExecutor pool) {
        // TODO: Return formatted metrics string
        return "";
    }

    // TODO 5: Implement graceful shutdown with timeout
    // shutdown(), awaitTermination(), then shutdownNow() if needed
    public boolean gracefulShutdown(ThreadPoolExecutor pool, long timeoutMs) throws InterruptedException {
        // TODO: Implement shutdown sequence
        return false;
    }

    // TODO 6: Submit tasks and collect results with timeout per task
    // If a task doesn't complete within timeoutMs, cancel it
    public <T> java.util.List<T> submitWithPerTaskTimeout(
            java.util.List<Callable<T>> tasks, long timeoutMs) throws Exception {
        // TODO: Submit all tasks, collect results with per-task timeout
        return new java.util.ArrayList<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        Practices practices = new Practices();
        int passed = 0;
        int total = 0;

        System.out.println("=== Thread Pool Configuration Practices ===\n");

        // Test 1
        total++;
        try {
            ThreadPoolExecutor pool = practices.createCpuBoundPool();
            int cores = Runtime.getRuntime().availableProcessors();
            if (pool != null && pool.getCorePoolSize() == cores + 1) {
                System.out.println("Test 1 PASSED: createCpuBoundPool");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: expected core=" + (cores + 1) + ", got=" + (pool == null ? "null" : pool.getCorePoolSize()));
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            ThreadPoolExecutor pool = practices.createIoBoundPool();
            int cores = Runtime.getRuntime().availableProcessors();
            int expected = cores * 5; // 80% wait → 1 + 0.8/0.2 = 5
            if (pool != null && pool.getCorePoolSize() == expected) {
                System.out.println("Test 2 PASSED: createIoBoundPool");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: expected core=" + expected + ", got=" + (pool == null ? "null" : pool.getCorePoolSize()));
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            ThreadPoolExecutor pool = practices.createBoundedPool();
            if (pool != null && pool.getCorePoolSize() == 2 && pool.getMaximumPoolSize() == 4) {
                System.out.println("Test 3 PASSED: createBoundedPool");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: core=" + (pool == null ? "null" : pool.getCorePoolSize()) + ", max=" + (pool == null ? "null" : pool.getMaximumPoolSize()));
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 4, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10));
            pool.execute(() -> { try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
            Thread.sleep(50);
            String metrics = practices.getPoolMetrics(pool);
            pool.shutdown();
            pool.awaitTermination(3, TimeUnit.SECONDS);
            if (metrics.contains("Active") && metrics.contains("Pool") && metrics.contains("Queue")) {
                System.out.println("Test 4 PASSED: getPoolMetrics");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: " + metrics);
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
            pool.execute(() -> { try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
            boolean result = practices.gracefulShutdown(pool, 1000);
            if (result || pool.isTerminated()) {
                System.out.println("Test 5 PASSED: gracefulShutdown");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: pool not terminated");
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            java.util.List<Callable<String>> tasks = java.util.List.of(
                () -> "fast",
                () -> { Thread.sleep(50); return "slow"; }
            );
            java.util.List<String> results = practices.submitWithPerTaskTimeout(tasks, 1000);
            if (results.size() == 2) {
                System.out.println("Test 6 PASSED: submitWithPerTaskTimeout");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: got " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 6 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
