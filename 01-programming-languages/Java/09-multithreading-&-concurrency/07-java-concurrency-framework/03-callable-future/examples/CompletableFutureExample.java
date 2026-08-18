package academy.javaengineering.concurrency.framework.callable;

import java.util.concurrent.*;

public class CompletableFutureExample {

    public static void main(String[] args) throws Exception {
        // --- 1. thenApply() - Transform result ---
        System.out.println("=== 1. thenApply() - Transform ===");
        CompletableFuture<String> cf1 = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenApply(s -> s + " World");
        System.out.println("thenApply result: " + cf1.get());

        // Chain multiple transforms
        CompletableFuture<Integer> cf1b = CompletableFuture
            .supplyAsync(() -> "  Hello World  ")
            .thenApply(String::trim)
            .thenApply(String::length);
        System.out.println("Chained length: " + cf1b.get());

        // --- 2. thenCompose() - FlatMap equivalent ---
        System.out.println("\n=== 2. thenCompose() - FlatMap ===");
        CompletableFuture<String> cf2 = CompletableFuture
            .supplyAsync(() -> "user-123")
            .thenCompose(userId -> CompletableFuture.supplyAsync(() -> "User(" + userId + ")"));
        System.out.println("thenCompose result: " + cf2.get());

        // --- 3. thenCombine() - Combine two futures ---
        System.out.println("\n=== 3. thenCombine() - Combine ===");
        CompletableFuture<String> firstName = CompletableFuture.supplyAsync(() -> "John");
        CompletableFuture<String> lastName = CompletableFuture.supplyAsync(() -> "Doe");

        CompletableFuture<String> fullName = firstName.thenCombine(lastName,
            (first, last) -> first + " " + last);
        System.out.println("thenCombine result: " + fullName.get());

        // --- 4. allOf() - Wait for all ---
        System.out.println("\n=== 4. allOf() - Wait for All ===");
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return "Task1";
        });
        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "Task2";
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "Task3";
        });

        CompletableFuture<Void> allDone = CompletableFuture.allOf(task1, task2, task3);
        allDone.join(); // blocks until all complete
        System.out.println("All completed: " + task1.get() + ", " + task2.get() + ", " + task3.get());

        // --- 5. anyOf() - Wait for first ---
        System.out.println("\n=== 5. anyOf() - Wait for First ===");
        CompletableFuture<String> slow1 = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return "slow1";
        });
        CompletableFuture<String> fast1 = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            return "fast1";
        });
        CompletableFuture<String> medium1 = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "medium1";
        });

        CompletableFuture<Object> firstDone = CompletableFuture.anyOf(slow1, fast1, medium1);
        System.out.println("First completed: " + firstDone.get());

        // --- 6. exceptionally() - Error handling ---
        System.out.println("\n=== 6. exceptionally() - Error Fallback ===");
        CompletableFuture<String> cf6 = CompletableFuture
            .supplyAsync(() -> {
                throw new RuntimeException("Oops!");
            })
            .exceptionally(ex -> "Fallback: " + ex.getMessage());
        System.out.println("exceptionally result: " + cf6.get());

        // --- 7. handle() - Both success and error ---
        System.out.println("\n=== 7. handle() - Success + Error ===");
        CompletableFuture<String> successCf = CompletableFuture
            .supplyAsync(() -> "Success!")
            .handle((result, ex) -> {
                if (ex != null) return "Error: " + ex.getMessage();
                return "Handled: " + result;
            });
        System.out.println("handle (success): " + successCf.get());

        CompletableFuture<String> errorCf = CompletableFuture
            .supplyAsync(() -> { throw new RuntimeException("Fail!"); })
            .handle((result, ex) -> {
                if (ex != null) return "Error handled: " + ex.getMessage();
                return "Handled: " + result;
            });
        System.out.println("handle (error): " + errorCf.get());

        // --- 8. Complete pipeline ---
        System.out.println("\n=== 8. Complete Pipeline ===");
        CompletableFuture<String> pipeline = CompletableFuture
            .supplyAsync(() -> "  Hello World  ")
            .thenApply(String::trim)
            .thenApply(s -> s.toUpperCase())
            .thenCombine(
                CompletableFuture.supplyAsync(() -> "!"),
                (s, suffix) -> s + suffix
            )
            .exceptionally(ex -> "Pipeline error: " + ex.getMessage());
        System.out.println("Pipeline result: " + pipeline.get());

        Thread.sleep(2000);
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
