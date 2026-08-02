package academy.javaengineering.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates Future, Callable, and CompletableFuture usage.
 * Shows asynchronous programming patterns.
 */
public class FutureExamples {

    public static void main(String[] args) throws Exception {
        demonstrateCallable();
        demonstrateFuture();
        demonstrateCompletableFuture();
    }

    /**
     * Demonstrates Callable interface for returning results.
     */
    public static void demonstrateCallable() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> callable = () -> {
            Thread.sleep(100);
            return "Callable result from " + Thread.currentThread().getName();
        };

        Future<String> future = executor.submit(callable);

        System.out.println("Is done: " + future.isDone());
        String result = future.get();
        System.out.println("Result: " + result);
        System.out.println("Is done after get: " + future.isDone());

        executor.shutdown();
        // Expected output:
        // Is done: false (or true if very fast)
        // Result: Callable result from pool-1-thread-1
        // Is done after get: true
    }

    /**
     * Demonstrates Future methods for checking status.
     */
    public static void demonstrateFuture() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(200);
            return 42;
        });

        System.out.println("Future is cancelled: " + future.isCancelled());
        System.out.println("Future is done: " + future.isDone());

        Integer result = future.get(1, TimeUnit.SECONDS);
        System.out.println("Future result: " + result);

        executor.shutdown();
        // Expected output:
        // Future is cancelled: false
        // Future is done: false (or true)
        // Future result: 42
    }

    /**
     * Demonstrates CompletableFuture for chaining operations.
     */
    public static void demonstrateCompletableFuture() throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        }).thenApply(s -> s + " World")
          .thenApply(s -> s + "!")
          .thenApply(String::toUpperCase);

        System.out.println("CompletableFuture result: " + future.get());

        // Demonstrating thenCompose
        CompletableFuture<Integer> composedFuture = CompletableFuture
                .supplyAsync(() -> "10")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> Integer.parseInt(s) * 2));

        System.out.println("Composed future result: " + composedFuture.get());

        // Demonstrating allOf
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "C");

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);
        allFutures.join();

        System.out.println("All futures completed: " + allFutures.isDone());
        // Expected output:
        // CompletableFuture result: HELLO WORLD!
        // Composed future result: 20
        // All futures completed: true
    }
}
