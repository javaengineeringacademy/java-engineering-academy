package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ExecutorServiceSolutions {

    public static void main(String[] args) throws Exception {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: Fixed thread pool
     */
    static void exercise1() throws InterruptedException {
        System.out.println("=== Exercise 1: Fixed Thread Pool ===");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("Task " + taskNum + " running in " + Thread.currentThread().getName());
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("All tasks completed");
    }

    /**
     * Exercise 2: Cached thread pool
     */
    static void exercise2() throws InterruptedException {
        System.out.println("=== Exercise 2: Cached Thread Pool ===");
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("Task " + taskNum + " in " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Exercise 3: Scheduled thread pool
     */
    static void exercise3() throws InterruptedException {
        System.out.println("=== Exercise 3: Scheduled Thread Pool ===");
        var scheduler = Executors.newScheduledThreadPool(2);

        scheduler.schedule(() -> {
            System.out.println("Delayed task executed after 2 seconds");
        }, 2, TimeUnit.SECONDS);

        var periodicTask = scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Periodic task: " + System.currentTimeMillis());
        }, 0, 1, TimeUnit.SECONDS);

        Thread.sleep(3500);
        periodicTask.cancel(false);
        scheduler.shutdown();
        scheduler.awaitTermination(5, TimeUnit.SECONDS);
    }

    /**
     * Exercise 4: Callable and Future
     */
    static void exercise4() throws Exception {
        System.out.println("=== Exercise 4: Callable and Future ===");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Callable<Integer> callable = () -> {
            System.out.println("Computing in " + Thread.currentThread().getName());
            Thread.sleep(1000);
            return 42;
        };

        Future<Integer> future = executor.submit(callable);
        System.out.println("Future is done: " + future.isDone());
        System.out.println("Getting result (blocking)...");
        int result = future.get();
        System.out.println("Result: " + result);
        System.out.println("Future is done: " + future.isDone());

        executor.shutdown();
    }

    /**
     * Exercise 5: Graceful shutdown
     */
    static void exercise5() throws InterruptedException {
        System.out.println("=== Exercise 5: Graceful Shutdown ===");
        ExecutorService executor = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 5; i++) {
            final int taskNum = i;
            executor.submit(() -> {
                System.out.println("Task " + taskNum + " started");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Task " + taskNum + " interrupted");
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskNum + " completed");
            });
        }

        System.out.println("Calling shutdown()...");
        executor.shutdown();

        boolean terminated = executor.awaitTermination(3, TimeUnit.SECONDS);
        if (!terminated) {
            System.out.println("Tasks not finished, calling shutdownNow()...");
            executor.shutdownNow();
        }
        System.out.println("Executor shut down: " + executor.isShutdown());
    }
}
