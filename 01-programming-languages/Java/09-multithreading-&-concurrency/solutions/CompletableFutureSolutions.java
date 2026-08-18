package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureSolutions {

    public static void main(String[] args) throws Exception {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: thenApply()
     */
    static void exercise1() throws Exception {
        System.out.println("=== Exercise 1: thenApply() ===");
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> "Hello")
                .thenApply(s -> s.toUpperCase());
        System.out.println("Result: " + future.get());
    }

    /**
     * Exercise 2: thenCompose()
     */
    static void exercise2() throws Exception {
        System.out.println("=== Exercise 2: thenCompose() ===");
        CompletableFuture<Integer> future = CompletableFuture
                .supplyAsync(() -> 21)
                .thenCompose(value -> CompletableFuture.supplyAsync(() -> value * 2));
        System.out.println("Result: " + future.get());
    }

    /**
     * Exercise 3: allOf()
     */
    static void exercise3() throws Exception {
        System.out.println("=== Exercise 3: allOf() ===");
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            return "Result 1";
        });
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(1500); } catch (InterruptedException e) {}
            return "Result 2";
        });
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(800); } catch (InterruptedException e) {}
            return "Result 3";
        });

        CompletableFuture.allOf(f1, f2, f3).join();
        System.out.println(f1.get() + ", " + f2.get() + ", " + f3.get());
    }

    /**
     * Exercise 4: Exception handling
     */
    static void exercise4() throws Exception {
        System.out.println("=== Exercise 4: Exception Handling ===");
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> {
                    if (true) throw new RuntimeException("Something went wrong");
                    return "Success";
                })
                .exceptionally(ex -> "Recovered from: " + ex.getMessage());

        System.out.println("Result: " + future.get());

        CompletableFuture<String> future2 = CompletableFuture
                .supplyAsync(() -> "Hello")
                .handle((result, ex) -> {
                    if (ex != null) return "Error: " + ex.getMessage();
                    return result.toUpperCase();
                });
        System.out.println("Handle result: " + future2.get());
    }

    /**
     * Exercise 5: Combine two futures
     */
    static void exercise5() throws Exception {
        System.out.println("=== Exercise 5: thenCombine() ===");
        CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> "World");
        CompletableFuture<String> greetingFuture = CompletableFuture.supplyAsync(() -> "Hello");

        CompletableFuture<String> combined = greetingFuture.thenCombine(nameFuture,
                (greeting, name) -> greeting + ", " + name + "!");
        System.out.println("Result: " + combined.get());
    }
}
