package academy.javaengineering.concurrency.threadpools.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solutions for thread pool configuration practice exercises.
 */
public class Solutions {

    // Solution 1: CPU-bound pool (cores + 1)
    public ThreadPoolExecutor createCpuBoundPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
            cores + 1, cores + 1, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>()
        );
    }

    // Solution 2: I/O-bound pool (cores * wait/compute ratio)
    public ThreadPoolExecutor createIoBoundPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        int poolSize = cores * 5; // 80% wait → 1 + 0.8/0.2 = 5
        return new ThreadPoolExecutor(
            poolSize, poolSize, 30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    // Solution 3: Bounded pool with CallerRunsPolicy
    public ThreadPoolExecutor createBoundedPool() {
        return new ThreadPoolExecutor(
            2, 4, 30L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    // Solution 4: Pool metrics
    public String getPoolMetrics(ThreadPoolExecutor pool) {
        return String.format("Active: %d, Pool: %d, Queue: %d, Completed: %d",
            pool.getActiveCount(),
            pool.getPoolSize(),
            pool.getQueue().size(),
            pool.getCompletedTaskCount());
    }

    // Solution 5: Graceful shutdown
    public boolean gracefulShutdown(ThreadPoolExecutor pool, long timeoutMs) throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
            pool.shutdownNow();
            return false;
        }
        return true;
    }

    // Solution 6: Submit with per-task timeout
    public <T> List<T> submitWithPerTaskTimeout(List<Callable<T>> tasks, long timeoutMs) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(tasks.size());
        try {
            List<Future<T>> futures = new ArrayList<>();
            for (Callable<T> task : tasks) {
                futures.add(pool.submit(task));
            }

            List<T> results = new ArrayList<>();
            for (Future<T> future : futures) {
                try {
                    results.add(future.get(timeoutMs, TimeUnit.MILLISECONDS));
                } catch (TimeoutException e) {
                    future.cancel(true);
                    results.add(null);
                } catch (ExecutionException e) {
                    results.add(null);
                }
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        Solutions solutions = new Solutions();
        int passed = 0;
        int total = 0;

        System.out.println("=== Thread Pool Configuration Solutions Tests ===\n");

        // Test 1
        total++;
        try {
            ThreadPoolExecutor pool = solutions.createCpuBoundPool();
            int cores = Runtime.getRuntime().availableProcessors();
            if (pool != null && pool.getCorePoolSize() == cores + 1) {
                System.out.println("Test 1 PASSED: createCpuBoundPool");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: expected " + (cores + 1) + ", got " + (pool == null ? "null" : pool.getCorePoolSize()));
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            ThreadPoolExecutor pool = solutions.createIoBoundPool();
            int cores = Runtime.getRuntime().availableProcessors();
            int expected = cores * 5;
            if (pool != null && pool.getCorePoolSize() == expected) {
                System.out.println("Test 2 PASSED: createIoBoundPool");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: expected " + expected + ", got " + (pool == null ? "null" : pool.getCorePoolSize()));
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            ThreadPoolExecutor pool = solutions.createBoundedPool();
            if (pool != null && pool.getCorePoolSize() == 2 && pool.getMaximumPoolSize() == 4) {
                System.out.println("Test 3 PASSED: createBoundedPool");
                passed++;
            } else {
                System.out.println("Test 3 FAILED");
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
            String metrics = solutions.getPoolMetrics(pool);
            pool.shutdown();
            pool.awaitTermination(3, TimeUnit.SECONDS);
            if (metrics.contains("Active") && metrics.contains("Pool")) {
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
            pool.execute(() -> { try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } });
            boolean result = solutions.gracefulShutdown(pool, 2000);
            if (result) {
                System.out.println("Test 5 PASSED: gracefulShutdown");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: " + result);
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            List<Callable<String>> tasks = List.of(
                () -> "fast",
                () -> { Thread.sleep(50); return "slow"; }
            );
            List<String> results = solutions.submitWithPerTaskTimeout(tasks, 1000);
            if (results.size() == 2 && "fast".equals(results.get(0))) {
                System.out.println("Test 6 PASSED: submitWithPerTaskTimeout");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 6 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
