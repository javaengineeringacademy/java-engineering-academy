package academy.javaengineering.concurrency.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExecutorServiceExamples {

    public static void main(String[] args) throws Exception {
        example1_FixedThreadPool();
        example2_CachedAndSingleThreadPool();
        example3_CallableAndFuture();
        example4_ScheduledExecutor();
        example5_ShutdownAndTimeout();
    }

    // Example 1: Fixed Thread Pool
    static void example1_FixedThreadPool() throws InterruptedException {
        System.out.println("=== Example 1: Fixed Thread Pool ===");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task-" + taskId + " running on " +
                        Thread.currentThread().getName());
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Fixed pool completed all tasks");
        System.out.println();
    }

    // Example 2: Cached and Single Thread Pool
    static void example2_CachedAndSingleThreadPool() throws InterruptedException {
        System.out.println("=== Example 2: Cached & Single Thread Pool ===");

        // CachedThreadPool - creates threads as needed, reuses idle threads
        ExecutorService cachedPool = Executors.newCachedThreadPool();

        for (int i = 0; i < 4; i++) {
            final int taskId = i;
            cachedPool.submit(() -> {
                System.out.println("Cached-" + taskId + " on " + Thread.currentThread().getName());
                try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }

        cachedPool.shutdown();
        cachedPool.awaitTermination(3, TimeUnit.SECONDS);

        // SingleThreadExecutor - single worker thread
        ExecutorService singlePool = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 4; i++) {
            final int taskId = i;
            singlePool.submit(() -> {
                System.out.println("Single-" + taskId + " on " + Thread.currentThread().getName());
            });
        }

        singlePool.shutdown();
        singlePool.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }

    // Example 3: Callable and Future
    static void example3_CallableAndFuture() throws ExecutionException, InterruptedException, TimeoutException {
        System.out.println("=== Example 3: Callable and Future ===");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Callable returns a value
        Callable<Integer> factorial = () -> {
            int n = 10;
            int result = 1;
            for (int i = 2; i <= n; i++) {
                result *= i;
            }
            System.out.println("Factorial computed by " + Thread.currentThread().getName());
            return result;
        };

        Callable<String> greeting = () -> {
            TimeUnit.MILLISECONDS.sleep(200);
            return "Hello from " + Thread.currentThread().getName();
        };

        Future<Integer> factorialFuture = executor.submit(factorial);
        Future<String> greetingFuture = executor.submit(greeting);

        // get() blocks until result is available
        System.out.println("10! = " + factorialFuture.get());
        System.out.println("Greeting: " + greetingFuture.get());

        // get(timeout) with timeout
        Callable<Long> slowTask = () -> {
            TimeUnit.SECONDS.sleep(2);
            return 42L;
        };

        Future<Long> slowFuture = executor.submit(slowTask);
        try {
            Long result = slowFuture.get(500, TimeUnit.MILLISECONDS);
            System.out.println("Slow result: " + result);
        } catch (TimeoutException e) {
            System.out.println("Slow task timed out! isDone=" + slowFuture.isDone());
            // Wait for actual completion
            System.out.println("Actual result: " + slowFuture.get());
        }

        // isDone(), isCancelled()
        System.out.println("Factorial future isDone: " + factorialFuture.isDone());
        System.out.println("Factorial future isCancelled: " + factorialFuture.isCancelled());

        // Cancel a future
        Future<Long> cancellableFuture = executor.submit(slowTask);
        TimeUnit.MILLISECONDS.sleep(100);
        boolean cancelled = cancellableFuture.cancel(true); // true = interrupt if running
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Is cancelled: " + cancellableFuture.isCancelled());

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println();
    }

    // Example 4: Scheduled Executor Service
    static void example4_ScheduledExecutor() throws InterruptedException {
        System.out.println("=== Example 4: Scheduled Executor ===");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // schedule() - run once after delay
        ScheduledFuture<String> scheduled = scheduler.schedule(() -> {
            return "Scheduled task completed on " + Thread.currentThread().getName();
        }, 500, TimeUnit.MILLISECONDS);

        System.out.println("Scheduled result: " + scheduled.get());

        // scheduleAtFixedRate() - run at fixed rate
        final int[] counter = {0};
        ScheduledFuture<?> fixedRate = scheduler.scheduleAtFixedRate(() -> {
            counter[0]++;
            System.out.println("FixedRate-" + counter[0] + " on " + Thread.currentThread().getName());
        }, 0, 200, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(1100); // Let it run a few times
        fixedRate.cancel(false); // Cancel but let current execution finish

        // scheduleWithFixedDelay() - run with fixed delay between end and start
        final int[] delayCounter = {0};
        ScheduledFuture<?> fixedDelay = scheduler.scheduleWithFixedDelay(() -> {
            delayCounter[0]++;
            System.out.println("FixedDelay-" + delayCounter[0] + " on " + Thread.currentThread().getName());
        }, 0, 300, TimeUnit.MILLISECONDS);

        TimeUnit.MILLISECONDS.sleep(1500);
        fixedDelay.cancel(false);

        scheduler.shutdown();
        scheduler.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println();
    }

    // Example 5: Shutdown and graceful termination
    static void example5_ShutdownAndTimeout() throws InterruptedException {
        System.out.println("=== Example 5: Shutdown Strategies ===");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Submit long-running tasks
        for (int i = 0; i < 4; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Long task-" + taskId + " started");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    System.out.println("Task-" + taskId + " interrupted during shutdown");
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println("Long task-" + taskId + " completed");
            });
        }

        // shutdown() - no new tasks, wait for existing to complete
        System.out.println("Calling shutdown()...");
        executor.shutdown();

        // awaitTermination() - block until all tasks complete or timeout
        boolean terminated = executor.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("Terminated within timeout? " + terminated);

        if (!terminated) {
            System.out.println("Tasks still running, calling shutdownNow()...");
            List<Runnable> droppedTasks = executor.shutdownNow();
            System.out.println("Dropped " + droppedTasks.size() + " tasks");
        }

        // isShutdown() and isTerminated()
        System.out.println("isShutdown: " + executor.isShutdown());
        System.out.println("isTerminated: " + executor.isTerminated());

        System.out.println();
    }
}
