package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates internal mechanics of CompletableFuture.
 */
public class CompletableFutureInternals {

    public static void main(String[] args) throws Exception {
        demonstrateDependencyChain();
        demonstrateCompletionPropagation();
        demonstrateExceptionHandling();
    }

    static void demonstrateDependencyChain() throws Exception {
        System.out.println("=== Dependency Chain ===");

        CompletableFuture<String> stage1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("Stage 1 on " + Thread.currentThread().getName());
            return "Hello";
        });

        CompletableFuture<Integer> stage2 = stage1.thenApply(s -> {
            System.out.println("Stage 2 on " + Thread.currentThread().getName());
            return s.length();
        });

        CompletableFuture<String> stage3 = stage2.thenApply(len -> {
            System.out.println("Stage 3 on " + Thread.currentThread().getName());
            return "Length: " + len;
        });

        System.out.println("Final: " + stage3.get());
    }

    static void demonstrateCompletionPropagation() throws Exception {
        System.out.println("\n=== Completion Propagation ===");

        CompletableFuture<Void> root = CompletableFuture.runAsync(() -> {
            System.out.println("Root completes");
        });

        // Each dependent stage fires when its predecessor completes
        root.thenRun(() -> System.out.println("Child 1 fires"))
            .thenRun(() -> System.out.println("Child 2 fires"))
            .thenRun(() -> System.out.println("Child 3 fires"));

        root.join();
        Thread.sleep(100);
    }

    static void demonstrateExceptionHandling() throws Exception {
        System.out.println("\n=== Exception Handling ===");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Boom!");
        }).exceptionally(ex -> {
            System.out.println("Caught: " + ex.getMessage());
            return "Recovered";
        });

        System.out.println("Result: " + future.get());
    }
}
