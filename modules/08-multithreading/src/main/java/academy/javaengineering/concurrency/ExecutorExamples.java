package academy.javaengineering.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * Demonstrates ExecutorService and thread pool usage.
 * Shows various thread pool types and task submission.
 */
public class ExecutorExamples {

    public static void main(String[] args) throws InterruptedException {
        demonstrateFixedThreadPool();
        demonstrateCachedThreadPool();
        demonstrateSingleThreadExecutor();
        demonstrateScheduledExecutor();
    }

    /**
     * Demonstrates fixed thread pool.
     */
    public static void demonstrateFixedThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Fixed Pool Task " + taskId + " running on "
                        + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Fixed thread pool completed");
        // Expected output: Tasks executed on pool-1-thread-1, pool-1-thread-2, pool-1-thread-3
    }

    /**
     * Demonstrates cached thread pool.
     */
    public static void demonstrateCachedThreadPool() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Cached Pool Task " + taskId + " running on "
                        + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Cached thread pool completed");
        // Expected output: Tasks executed on potentially different threads
    }

    /**
     * Demonstrates single thread executor.
     */
    public static void demonstrateSingleThreadExecutor() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Single Pool Task " + taskId + " running on "
                        + Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Single thread pool completed");
        // Expected output: All tasks executed on same thread (pool-2-thread-1)
    }

    /**
     * Demonstrates scheduled executor service.
     */
    public static void demonstrateScheduledExecutor() throws InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

        ScheduledFuture<?> future1 = executor.schedule(() -> {
            System.out.println("Scheduled task 1 executed");
        }, 100, TimeUnit.MILLISECONDS);

        ScheduledFuture<?> future2 = executor.scheduleAtFixedRate(() -> {
            System.out.println("Periodic task executed");
        }, 0, 50, TimeUnit.MILLISECONDS);

        Thread.sleep(200);
        future2.cancel(false);

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        System.out.println("Scheduled executor completed");
        // Expected output: Periodic task executed multiple times, then Scheduled task 1
    }
}
