package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates memory layout in CompletableFuture.
 */
public class CompletableFutureMemory {

    public static void main(String[] args) throws Exception {
        demonstrateResultStorage();
        demonstrateDependencyChainMemory();
        demonstrateCancellationMemory();
    }

    static void demonstrateResultStorage() throws Exception {
        System.out.println("=== Result Storage ===");

        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            long sum = 0;
            for (int i = 0; i < 1000000; i++) sum += i;
            return sum; // stored in CompletableFuture.result field
        });

        // volatile read via get()
        System.out.println("Result: " + future.get());

        // After completion, the result is immutable
        System.out.println("Join returns same: " + future.join());
    }

    static void demonstrateDependencyChainMemory() throws Exception {
        System.out.println("\n=== Dependency Chain Memory ===");

        // Each thenApply creates a UniCompletion node on the heap
        CompletableFuture<String> chain = CompletableFuture.supplyAsync(() -> "start")
            .thenApply(s -> s + "-a")   // UniApply node 1
            .thenApply(s -> s + "-b")   // UniApply node 2
            .thenApply(s -> s + "-c");  // UniApply node 3

        // After chain completes, intermediate nodes are eligible for GC
        System.out.println("Chain result: " + chain.get());

        // thenCompose for flat composition (reduces object count)
        CompletableFuture<String> flat = CompletableFuture.supplyAsync(() -> "start")
            .thenCompose(s -> CompletableFuture.completedFuture(s + "-flat"));
        System.out.println("Flat result: " + flat.get());
    }

    static void demonstrateCancellationMemory() throws Exception {
        System.out.println("\n=== Cancellation Memory ===");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(5000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Done";
        });

        Thread.sleep(50);
        boolean cancelled = future.cancel(false);
        System.out.println("Cancelled: " + cancelled);

        try {
            future.get();
        } catch (CancellationException e) {
            System.out.println("get() threw CancellationException");
            System.out.println("Exception stored in result field prevents further use");
        }
    }
}
