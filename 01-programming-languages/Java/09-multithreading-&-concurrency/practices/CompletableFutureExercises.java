package academy.javaengineering.concurrency.practices;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExercises {

    public static void main(String[] args) throws Exception {
        exercise1();
        exercise2();
        exercise3();
        exercise4();
        exercise5();
    }

    /**
     * Exercise 1: thenApply()
     * TODO: Use thenApply() to transform the result of a CompletableFuture.
     *       Start with a CompletableFuture that returns "Hello",
     *       then apply a transformation to convert it to uppercase.
     */
    static void exercise1() throws Exception {
        System.out.println("=== Exercise 1: thenApply() ===");
        // TODO: Implement here
        // Hint: CompletableFuture.supplyAsync(() -> "Hello").thenApply(s -> s.toUpperCase())
    }

    /**
     * Exercise 2: thenCompose()
     * TODO: Use thenCompose() to chain two dependent CompletableFutures.
     *       First future returns a number, second future doubles it.
     */
    static void exercise2() throws Exception {
        System.out.println("=== Exercise 2: thenCompose() ===");
        // TODO: Implement here
        // Hint: thenCompose() is used when the transformation returns a CompletableFuture
    }

    /**
     * Exercise 3: allOf()
     * TODO: Use allOf() to wait for multiple CompletableFutures to complete.
     *       Create 3 futures that each sleep for different durations.
     *       Print results when all are complete.
     */
    static void exercise3() throws Exception {
        System.out.println("=== Exercise 3: allOf() ===");
        // TODO: Implement here
        // Hint: CompletableFuture.allOf(future1, future2, future3).join();
    }

    /**
     * Exercise 4: Exception handling
     * TODO: Use exceptionally() or handle() to handle exceptions in CompletableFuture.
     *       Create a future that completes exceptionally, and handle the error.
     */
    static void exercise4() throws Exception {
        System.out.println("=== Exercise 4: Exception Handling ===");
        // TODO: Implement here
        // Hint: future.exceptionally(ex -> "Default value")
    }

    /**
     * Exercise 5: Combine two futures
     * TODO: Use thenCombine() to combine results of two independent futures.
     *       First future returns a name, second returns a greeting.
     *       Combine them into "Hello, Name!".
     */
    static void exercise5() throws Exception {
        System.out.println("=== Exercise 5: thenCombine() ===");
        // TODO: Implement here
        // Hint: future1.thenCombine(future2, (name, greeting) -> greeting + ", " + name + "!")
    }
}
