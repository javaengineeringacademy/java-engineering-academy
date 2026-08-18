package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates internal mechanics of ScheduledThreadPoolExecutor.
 */
public class ScheduledExecutorInternals {

    public static void main(String[] args) throws Exception {
        demonstrateDelayedExecution();
        demonstratePeriodicExecution();
        demonstrateFixedRateVsDelay();
    }

    static void demonstrateDelayedExecution() throws Exception {
        System.out.println("=== Delayed Execution ===");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        long start = System.currentTimeMillis();
        ScheduledFuture<?> future = scheduler.schedule(
            () -> System.out.println("Delayed task at " + (System.currentTimeMillis() - start) + "ms"),
            500, TimeUnit.MILLISECONDS
        );

        System.out.println("Task scheduled, getDelay: " + future.getDelay(TimeUnit.MILLISECONDS) + "ms");
        future.get();

        scheduler.shutdown();
    }

    static void demonstratePeriodicExecution() throws Exception {
        System.out.println("\n=== Periodic Execution ===");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicInteger count = new AtomicInteger(0);

        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(
            () -> System.out.println("Tick " + count.incrementAndGet()),
            0, 200, TimeUnit.MILLISECONDS
        );

        Thread.sleep(700);
        future.cancel(false);

        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
    }

    static void demonstrateFixedRateVsDelay() throws Exception {
        System.out.println("\n=== Fixed Rate vs Fixed Delay ===");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        System.out.println("Fixed Rate (starts immediately after previous completes):");
        scheduler.scheduleAtFixedRate(
            () -> {
                System.out.println("  Rate: " + System.currentTimeMillis() % 10000);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            },
            0, 200, TimeUnit.MILLISECONDS
        );

        System.out.println("Fixed Delay (waits delay after previous completes):");
        scheduler.scheduleWithFixedDelay(
            () -> {
                System.out.println("  Delay: " + System.currentTimeMillis() % 10000);
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            },
            0, 200, TimeUnit.MILLISECONDS
        );

        Thread.sleep(1100);
        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
    }

    static class AtomicInteger {
        private int value = 0;
        int incrementAndGet() { return ++value; }
    }
}
