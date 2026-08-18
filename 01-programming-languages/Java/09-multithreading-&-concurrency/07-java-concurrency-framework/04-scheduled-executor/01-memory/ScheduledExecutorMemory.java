package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates memory layout in ScheduledThreadPoolExecutor.
 */
public class ScheduledExecutorMemory {

    public static void main(String[] args) throws Exception {
        demonstrateDelayedWorkQueue();
        demonstratePeriodicTaskRetention();
        demonstrateCancelledTaskCleanup();
    }

    static void demonstrateDelayedWorkQueue() throws Exception {
        System.out.println("=== DelayedWorkQueue Memory ===");

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        // Queue grows dynamically (initial capacity 128)
        for (int i = 0; i < 5; i++) {
            executor.schedule(
                () -> System.out.println("Scheduled task"),
                10, TimeUnit.SECONDS
            );
        }

        System.out.println("Queue size: " + executor.getQueue().size());
        System.out.println("Queue is priority-based (min-heap by trigger time)");

        executor.shutdown();
    }

    static void demonstratePeriodicTaskRetention() throws Exception {
        System.out.println("\n=== Periodic Task Retention ===");

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

        // Periodic task re-offers itself after each execution
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(
            () -> System.out.println("Periodic: " + System.currentTimeMillis()),
            0, 100, TimeUnit.MILLISECONDS
        );

        Thread.sleep(350);
        future.cancel(false);

        // Task remains in queue until purge() or get()
        System.out.println("After cancel, queue size: " + executor.getQueue().size());
        executor.purge();
        System.out.println("After purge, queue size: " + executor.getQueue().size());

        executor.shutdown();
    }

    static void demonstrateCancelledTaskCleanup() throws Exception {
        System.out.println("\n=== SetRemoveOnCancelPolicy ===");

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRemoveOnCancelPolicy(true); // eagerly remove cancelled tasks

        ScheduledFuture<?> future1 = executor.schedule(
            () -> System.out.println("Task 1"),
            5, TimeUnit.SECONDS
        );
        ScheduledFuture<?> future2 = executor.schedule(
            () -> System.out.println("Task 2"),
            5, TimeUnit.SECONDS
        );

        Thread.sleep(50);
        future1.cancel(false);
        future2.cancel(false);
        Thread.sleep(50);

        System.out.println("With setRemoveOnCancelPolicy(true), queue size: " + executor.getQueue().size());

        executor.shutdown();
    }
}
