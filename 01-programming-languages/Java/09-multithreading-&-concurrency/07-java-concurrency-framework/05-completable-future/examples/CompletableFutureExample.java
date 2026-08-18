package academy.javaengineering.concurrency.framework.completable;

import java.util.concurrent.*;

public class CompletableFutureExample {
    public static void main(String[] args) throws Exception {
        // Basic chaining
        CompletableFuture<String> future = CompletableFuture
            .supplyAsync(() -> "Hello")
            .thenApply(s -> s + " World")
            .thenApply(String::toUpperCase);
        System.out.println("Chained: " + future.get());

        // Composition
        CompletableFuture<Integer> price = CompletableFuture.supplyAsync(() -> 100);
        CompletableFuture<Double> tax = CompletableFuture.supplyAsync(() -> 0.08);
        CompletableFuture<String> total = price.thenCombine(tax, (p, t) -> {
            double result = p * (1 + t);
            return String.format("Price: $%.2f", result);
        });
        System.out.println("Combined: " + total.get());

        // Error handling
        CompletableFuture<String> errorFuture = CompletableFuture
            .supplyAsync(() -> { throw new RuntimeException("Failed!"); })
            .exceptionally(ex -> "Recovered: " + ex.getMessage());
        System.out.println("Error handling: " + errorFuture.get());

        // allOf
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");
        CompletableFuture.allOf(f1, f2, f3).join();
        System.out.println("All: " + f1.get() + f2.get() + f3.get());

        // anyOf
        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "Slow";
        });
        CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> "Fast");
        Object first = CompletableFuture.anyOf(slow, fast).get();
        System.out.println("First: " + first);
    }
}
