import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates internal mechanics of the Java Concurrency Framework:
 * ExecutorService task lifecycle, work queue behavior, and rejection policies.
 */
public class ConcurrencyFrameworkInternals {

    public static void main(String[] args) throws Exception {
        demonstrateThreadPoolInternals();
        demonstrateWorkQueueBehavior();
        demonstrateRejectionPolicies();
    }

    static void demonstrateThreadPoolInternals() throws InterruptedException {
        System.out.println("=== ThreadPoolExecutor Internals ===");
        AtomicInteger threadCounter = new AtomicInteger(0);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 4, 60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(3),
            r -> new Thread(r, "pool-thread-" + threadCounter.incrementAndGet()),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        System.out.println("Core pool size: " + executor.getCorePoolSize());
        System.out.println("Max pool size: " + executor.getMaximumPoolSize());
        System.out.println("Queue capacity: " + executor.getQueue().remainingCapacity());

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("  Task " + taskId + " running on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    static void demonstrateWorkQueueBehavior() throws InterruptedException {
        System.out.println("=== Work Queue Behavior ===");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2)
        );

        System.out.println("Before submitting - active count: " + executor.getActiveCount());
        System.out.println("Before submitting - queue size: " + executor.getQueue().size());

        executor.submit(() -> { sleep(200); });
        executor.submit(() -> { sleep(200); });
        executor.submit(() -> { sleep(200); }); // queued since core=1, queue not full

        System.out.println("After submitting 3 - active count: " + executor.getActiveCount());
        System.out.println("After submitting 3 - queue size: " + executor.getQueue().size());

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println();
    }

    static void demonstrateRejectionPolicies() {
        System.out.println("=== Rejection Policies ===");
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.AbortPolicy()
        );

        executor.submit(() -> sleep(500));
        executor.submit(() -> sleep(500)); // queued (queue has space)

        try {
            executor.submit(() -> sleep(500)); // pool=1 busy, queue=1 full → rejected
            System.out.println("  Task accepted");
        } catch (RejectedExecutionException e) {
            System.out.println("  Task rejected: " + e.getMessage());
        }

        executor.shutdown();
    }

    static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
