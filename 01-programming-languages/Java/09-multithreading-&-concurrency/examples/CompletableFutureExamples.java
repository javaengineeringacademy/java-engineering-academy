package academy.javaengineering.concurrency.examples;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExamples {

    public static void main(String[] args) throws Exception {
        example1_BasicCreation();
        example2_ThenApplyThenCompose();
        example3_CombineAndAllOf();
        example4_ExceptionHandling();
        example5_AnyOfAndRealWorld();
    }

    // Example 1: Basic CompletableFuture creation
    static void example1_BasicCreation() throws Exception {
        System.out.println("=== Example 1: Basic Creation ===");

        // supplyAsync - returns a value
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            return "Hello from " + Thread.currentThread().getName();
        });

        System.out.println("Result: " + future1.get());

        // runAsync - no return value
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            System.out.println("Running async task on " + Thread.currentThread().getName());
        });

        future2.join(); // get() with unchecked exception

        // completedFuture - already completed
        CompletableFuture<String> completed = CompletableFuture.completedFuture("Already done");
        System.out.println("Completed: " + completed.get());

        // failedFuture (Java 9+)
        CompletableFuture<String> failed = CompletableFuture.failedFuture(
                new RuntimeException("Something went wrong"));
        System.out.println("Failed isCompletedExceptionally: " + failed.isCompletedExceptionally());

        System.out.println();
    }

    // Example 2: thenApply and thenCompose
    static void example2_ThenApplyThenCompose() throws Exception {
        System.out.println("=== Example 2: thenApply & thenCompose ===");

        // thenApply - transform the result (like map)
        CompletableFuture<Integer> lengthFuture = CompletableFuture
                .supplyAsync(() -> "Hello, CompletableFuture!")
                .thenApply(String::length);

        System.out.println("String length: " + lengthFuture.get());

        // thenApply chaining
        CompletableFuture<String> chained = CompletableFuture
                .supplyAsync(() -> 10)
                .thenApply(n -> n * 2)          // 20
                .thenApply(n -> n + 5)          // 25
                .thenApply(n -> "Result: " + n); // "Result: 25"

        System.out.println("Chained result: " + chained.get());

        // thenCompose - flatMap equivalent (avoids nested CompletableFuture)
        CompletableFuture<String> composed = CompletableFuture
                .supplyAsync(() -> "Java")
                .thenCompose(lang -> CompletableFuture.supplyAsync(() -> lang + " is great!"));

        System.out.println("Composed: " + composed.get());

        // thenApplyAsync - apply on a different thread
        CompletableFuture<String> asyncApply = CompletableFuture
                .supplyAsync(() -> "Original")
                .thenApplyAsync(s -> s + " - transformed on " + Thread.currentThread().getName());

        System.out.println("Async apply: " + asyncApply.get());

        System.out.println();
    }

    // Example 3: thenCombine and allOf
    static void example3_CombineAndAllOf() throws Exception {
        System.out.println("=== Example 3: thenCombine & allOf ===");

        CompletableFuture<String> firstName = CompletableFuture.supplyAsync(() -> "John");
        CompletableFuture<String> lastName = CompletableFuture.supplyAsync(() -> "Doe");

        // thenCombine - combine two futures
        CompletableFuture<String> fullName = firstName.thenCombine(lastName,
                (first, last) -> first + " " + last);

        System.out.println("Full name: " + fullName.get());

        // thenAccept - consume result without returning
        firstName.thenAccept(name -> System.out.println("First name consumed: " + name));

        // thenRun - run after completion, no access to result
        CompletableFuture<Void> afterAll = fullName.thenRun(() ->
                System.out.println("Name computation complete!"));

        // allOf - wait for all futures
        CompletableFuture<Integer> num1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> num2 = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> num3 = CompletableFuture.supplyAsync(() -> 30);

        CompletableFuture<Void> allDone = CompletableFuture.allOf(num1, num2, num3);
        allDone.join();

        int sum = num1.get() + num2.get() + num3.get();
        System.out.println("Sum of all: " + sum);

        // allOf with result aggregation
        CompletableFuture<String> s1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> s2 = CompletableFuture.supplyAsync(() -> " ");
        CompletableFuture<String> s3 = CompletableFuture.supplyAsync(() -> "World");

        CompletableFuture<String> combined = CompletableFuture.allOf(s1, s2, s3)
                .thenApply(v -> s1.join() + s2.join() + s3.join());

        System.out.println("Combined: " + combined.get());

        System.out.println();
    }

    // Example 4: Exception handling
    static void example4_ExceptionHandling() throws Exception {
        System.out.println("=== Example 4: Exception Handling ===");

        // exceptionally - handle exceptions
        CompletableFuture<String> withException = CompletableFuture
                .supplyAsync(() -> {
                    if (true) throw new RuntimeException("Oops!");
                    return "Should not reach here";
                })
                .exceptionally(ex -> "Recovered from: " + ex.getMessage());

        System.out.println("Exceptionally: " + withException.get());

        // handle - handle both success and exception
        CompletableFuture<String> handled = CompletableFuture
                .supplyAsync(() -> {
                    if (Math.random() > 0.5) throw new RuntimeException("Random failure");
                    return "Success!";
                })
                .handle((result, ex) -> {
                    if (ex != null) {
                        return "Handled error: " + ex.getMessage();
                    }
                    return "Handled success: " + result;
                });

        System.out.println("Handled: " + handled.get());

        // completeExceptionally
        CompletableFuture<String> failed = new CompletableFuture<>();
        failed.completeExceptionally(new RuntimeException("Manual failure"));
        System.out.println("Manually failed exceptionally: " + failed.isCompletedExceptionally());

        // whenComplete - side effect without transformation
        CompletableFuture.supplyAsync(() -> "data")
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        System.out.println("Error: " + ex.getMessage());
                    } else {
                        System.out.println("Success: " + result);
                    }
                })
                .join();

        System.out.println();
    }

    // Example 5: anyOf and practical example
    static void example5_AnyOfAndRealWorld() throws Exception {
        System.out.println("=== Example 5: anyOf & Practical Example ===");

        // anyOf - returns when first future completes
        CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Fast result";
        });

        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Slow result";
        });

        CompletableFuture<Object> first = CompletableFuture.anyOf(fast, slow);
        System.out.println("First completed: " + first.get());

        // Practical: Multiple API calls with timeout simulation
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CompletableFuture<String> api1 = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "API1 response";
        }, executor);

        CompletableFuture<String> api2 = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.MILLISECONDS.sleep(150); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "API2 response";
        }, executor);

        CompletableFuture<String> api3 = CompletableFuture.supplyAsync(() -> {
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "API3 response";
        }, executor);

        // Get fastest response
        CompletableFuture<Object> fastest = CompletableFuture.anyOf(api1, api2, api3);
        System.out.println("Fastest API: " + fastest.get());

        // Aggregate all results
        CompletableFuture<Void> allApis = CompletableFuture.allOf(api1, api2, api3);
        allApis.join();
        System.out.println("All APIs done: " + api1.get() + ", " + api2.get() + ", " + api3.get());

        executor.shutdown();

        System.out.println();
    }
}
