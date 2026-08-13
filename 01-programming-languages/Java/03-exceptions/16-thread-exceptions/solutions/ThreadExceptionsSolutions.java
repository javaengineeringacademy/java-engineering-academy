package academy.javaengineering.exceptions.thread;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solutions for thread exception handling exercises.
 */
public class ThreadExceptionsSolutions {

    /**
     * Exercise 1: Set up a UncaughtExceptionHandler that logs exceptions
     * to System.err with format: "ERROR [thread-name]: exception-message"
     */
    static void exercise1() throws InterruptedException {
        System.out.println("--- Exercise 1: UncaughtExceptionHandler ---");

        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.err.printf("ERROR [%s]: %s%n",
                thread.getName(), exception.getMessage());
        });

        Thread thread = new Thread(() -> {
            throw new RuntimeException("Test exception");
        }, "test-thread-1");

        thread.start();
        thread.join();
        System.out.println();
    }

    /**
     * Exercise 2: Demonstrate the difference between execute() and submit()
     * by catching exceptions from both approaches.
     */
    static void exercise2() throws Exception {
        System.out.println("--- Exercise 2: execute() vs submit() ---");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // execute() — exception propagates to UncaughtExceptionHandler
        executor.execute(() -> {
            throw new RuntimeException("execute() task failed");
        });

        // submit() — exception captured in Future
        Future<?> future = executor.submit(() -> {
            throw new RuntimeException("submit() task failed");
        });

        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            System.out.println("Captured from submit(): " + e.getCause().getMessage());
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println();
    }

    /**
     * Exercise 3: Build a CompletableFuture chain that handles exceptions
     * at each stage and recovers with fallback values.
     */
    static void exercise3() {
        System.out.println("--- Exercise 3: CompletableFuture Chain ---");

        CompletableFuture<String> chain = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Stage 1 failed");
            }
            return "initial";
        })
        .thenApply(result -> result.toUpperCase())
        .thenApply(result -> result + " processed")
        .exceptionally(ex -> {
            System.out.println("Chain exception: " + ex.getMessage());
            return "fallback";
        });

        System.out.println("Chain result: " + chain.join());
        System.out.println();
    }

    /**
     * Exercise 4: Create a thread that throws exception in finally block
     * and observe what happens to the original exception.
     */
    static void exercise4() throws InterruptedException {
        System.out.println("--- Exercise 4: Exception in finally ---");

        Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
            System.err.println("Reported: " + exception.getClass().getSimpleName()
                + " - " + exception.getMessage());
        });

        Thread thread = new Thread(() -> {
            try {
                throw new RuntimeException("Original exception");
            } finally {
                throw new RuntimeException("Finally exception");
            }
        });

        thread.start();
        thread.join();
        System.out.println();
    }

    /**
     * Exercise 5: Create a simple thread pool exception monitoring pattern
     * that tracks task failures.
     */
    static void exercise5() throws Exception {
        System.out.println("--- Exercise 5: Thread Pool Monitoring ---");

        ExecutorService executor = Executors.newFixedThreadPool(3);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                try {
                    if (taskId % 2 == 0) {
                        throw new RuntimeException("Task " + taskId + " failed");
                    }
                    System.out.println("Task " + taskId + " succeeded");
                } finally {
                    if (taskId % 2 == 0) {
                        failureCount.incrementAndGet();
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Total failures: " + failureCount.get());
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Thread Exception Solutions ===\n");

        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }
}
