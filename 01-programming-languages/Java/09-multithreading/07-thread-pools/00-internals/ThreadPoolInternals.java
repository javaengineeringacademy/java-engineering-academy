package academy.javaengineering.concurrency.threadpools.internals;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates ThreadPoolExecutor internal mechanisms:
 * - Core parameters and state
 * - Task execution flow
 * - Worker lifecycle
 * - Queue interactions
 */
public class ThreadPoolInternals {

    public static void main(String[] args) throws Exception {
        inspectCoreParameters();
        demonstrateWorkerLifecycle();
        demonstrateQueueBehavior();
        demonstrateKeepAliveTime();
    }

    private static void inspectCoreParameters() throws InterruptedException {
        System.out.println("=== Core Parameters ===\n");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5)
        );

        System.out.println("Configuration:");
        System.out.println("  corePoolSize: " + executor.getCorePoolSize());
        System.out.println("  maximumPoolSize: " + executor.getMaximumPoolSize());
        System.out.println("  keepAliveTime: " + executor.getKeepAliveTime(TimeUnit.SECONDS) + "s");
        System.out.println("  poolSize: " + executor.getPoolSize());
        System.out.println("  activeCount: " + executor.getActiveCount());
        System.out.println("  completedTaskCount: " + executor.getCompletedTaskCount());
        System.out.println("  taskCount: " + executor.getTaskCount());

        // Submit tasks to fill core pool
        for (int i = 0; i < 4; i++) {
            final int id = i;
            executor.execute(() -> {
                System.out.println("  Task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
            Thread.sleep(50);
        }

        System.out.println("\nAfter submitting 4 tasks:");
        System.out.println("  poolSize: " + executor.getPoolSize());
        System.out.println("  activeCount: " + executor.getActiveCount());
        System.out.println("  queue size: " + executor.getQueue().size());

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    private static void demonstrateWorkerLifecycle() throws InterruptedException {
        System.out.println("=== Worker Lifecycle ===\n");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 3, 2L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        System.out.println("Phase 1: Core thread created on first task");
        executor.execute(() -> {
            System.out.println("  Core thread active: " + Thread.currentThread().getName());
            try { Thread.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread.sleep(100);
        System.out.println("  Pool size: " + executor.getPoolSize());

        System.out.println("\nPhase 2: Additional threads created when queue full");
        for (int i = 0; i < 6; i++) {
            final int id = i;
            executor.execute(() -> {
                System.out.println("  Task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        Thread.sleep(100);
        System.out.println("  Pool size: " + executor.getPoolSize());

        System.out.println("\nPhase 3: Excess threads terminated after keepAliveTime");
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    private static void demonstrateQueueBehavior() throws InterruptedException {
        System.out.println("=== Queue Behavior ===\n");

        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(3);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 2, 0L, TimeUnit.MILLISECONDS, queue
        );

        System.out.println("Queue capacity: " + queue.remainingCapacity());

        for (int i = 0; i < 5; i++) {
            final int id = i;
            try {
                executor.execute(() -> {
                    try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
                System.out.println("  Task " + id + " accepted - queue remaining: " + queue.remainingCapacity());
            } catch (RejectedExecutionException e) {
                System.out.println("  Task " + id + " REJECTED - queue full");
            }
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    private static void demonstrateKeepAliveTime() throws InterruptedException {
        System.out.println("=== Keep Alive Time ===\n");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 4, 1L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10)
        );

        // Create temporary threads
        for (int i = 0; i < 4; i++) {
            final int id = i;
            executor.execute(() -> {
                System.out.println("  Task " + id + " on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        Thread.sleep(200);
        System.out.println("  Pool size (active): " + executor.getPoolSize());

        System.out.println("  Waiting 2s for excess threads to terminate...");
        Thread.sleep(2000);
        System.out.println("  Pool size (after idle): " + executor.getPoolSize());

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println();
    }
}
