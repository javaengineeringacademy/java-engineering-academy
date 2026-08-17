package academy.javaengineering.concurrency.executor.internals;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates ExecutorService internal mechanisms:
 * - ThreadPoolExecutor core components
 * - Task execution flow
 * - Worker thread lifecycle
 * - Rejection handlers
 */
public class ExecutorServiceInternals {

    public static void main(String[] args) throws Exception {
        inspectThreadPoolExecutor();
        demonstrateTaskFlow();
        demonstrateRejectionHandlers();
        demonstrateThreadFactory();
    }

    private static void inspectThreadPoolExecutor() throws InterruptedException {
        System.out.println("=== ThreadPoolExecutor Internals ===\n");

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,  // corePoolSize
            4,  // maximumPoolSize
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        System.out.println("Initial state:");
        System.out.println("  Core pool size: " + executor.getCorePoolSize());
        System.out.println("  Max pool size: " + executor.getMaximumPoolSize());
        System.out.println("  Pool size: " + executor.getPoolSize());
        System.out.println("  Active count: " + executor.getActiveCount());
        System.out.println("  Queue size: " + executor.getQueue().size());
        System.out.println("  Keep alive time: " + executor.getKeepAliveTime(TimeUnit.SECONDS) + "s");

        // Submit tasks to observe thread creation
        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("  Task " + taskId + " running on " + Thread.currentThread().getName());
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
            Thread.sleep(50);
        }

        System.out.println("\nAfter submitting 6 tasks:");
        System.out.println("  Pool size: " + executor.getPoolSize());
        System.out.println("  Active count: " + executor.getActiveCount());
        System.out.println("  Queue size: " + executor.getQueue().size());
        System.out.println("  Completed task count: " + executor.getCompletedTaskCount());

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    private static void demonstrateTaskFlow() throws InterruptedException {
        System.out.println("=== Task Execution Flow ===\n");

        // Queue capacity of 2, core pool 1, max pool 2
        // This creates backpressure quickly
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 2, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(2),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        System.out.println("Pool config: core=1, max=2, queue=2, rejection=CallerRuns");
        System.out.println("Submitting 6 tasks...\n");

        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("  Task " + taskId + " executing on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
            System.out.println("  After task " + taskId + " submit - pool=" + executor.getPoolSize()
                + ", queue=" + executor.getQueue().size());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    private static void demonstrateRejectionHandlers() {
        System.out.println("=== Rejection Handlers ===\n");

        // AbortPolicy (default)
        ThreadPoolExecutor abortPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.AbortPolicy()
        );
        System.out.println("AbortPolicy:");
        submitAndCatch(abortPool, "Abort-1");
        submitAndCatch(abortPool, "Abort-2");
        submitAndCatch(abortPool, "Abort-3");
        abortPool.shutdown();

        // CallerRunsPolicy
        ThreadPoolExecutor callerPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        System.out.println("\nCallerRunsPolicy:");
        submitAndCatch(callerPool, "Caller-1");
        submitAndCatch(callerPool, "Caller-2");
        submitAndCatch(callerPool, "Caller-3");
        callerPool.shutdown();

        // DiscardOldestPolicy
        ThreadPoolExecutor discardOldestPool = new ThreadPoolExecutor(
            1, 1, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1),
            new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        System.out.println("\nDiscardOldestPolicy:");
        submitAndCatch(discardOldestPool, "DiscardOldest-1");
        submitAndCatch(discardOldestPool, "DiscardOldest-2");
        submitAndCatch(discardOldestPool, "DiscardOldest-3");
        discardOldestPool.shutdown();

        System.out.println();
    }

    private static void submitAndCatch(ThreadPoolExecutor executor, String name) {
        try {
            executor.execute(() -> {
                try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
            System.out.println("  " + name + " accepted");
        } catch (RejectedExecutionException e) {
            System.out.println("  " + name + " rejected: " + e.getMessage());
        }
    }

    private static void demonstrateThreadFactory() {
        System.out.println("=== Custom Thread Factory ===\n");

        ThreadFactory factory = r -> {
            Thread t = new Thread(r);
            t.setName("custom-worker-" + t.threadId());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            System.out.println("  Factory created thread: " + t.getName());
            return t;
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, 2, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), factory
        );

        for (int i = 0; i < 4; i++) {
            executor.execute(() -> {
                System.out.println("  Running on " + Thread.currentThread().getName());
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        executor.shutdown();
        try { executor.awaitTermination(3, TimeUnit.SECONDS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        System.out.println();
    }
}
