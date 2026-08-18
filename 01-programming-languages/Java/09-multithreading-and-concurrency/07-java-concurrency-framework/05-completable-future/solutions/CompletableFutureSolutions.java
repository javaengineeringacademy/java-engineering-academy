package academy.javaengineering.concurrency.framework.completable.solutions;

import java.util.concurrent.*;

public class CompletableFutureSolutions {
    public static void main(String[] args) throws Exception {
        // Solution 1: Chained operations
        String result = CompletableFuture
            .supplyAsync(() -> "user-123")
            .thenCompose(user -> CompletableFuture.supplyAsync(() -> "orders-" + user))
            .thenApply(orders -> "Total for " + orders)
            .get();
        System.out.println("Chained: " + result);

        // Solution 2: Error handling
        String recovered = CompletableFuture
            .supplyAsync(() -> { throw new RuntimeException("DB down"); })
            .exceptionally(ex -> "Fallback: " + ex.getMessage())
            .get();
        System.out.println("Recovered: " + recovered);

        // Solution 3: Combine
        CompletableFuture<String> name = CompletableFuture.supplyAsync(() -> "Alice");
        CompletableFuture<Integer> age = CompletableFuture.supplyAsync(() -> 30);
        String combined = name.thenCombine(age, (n, a) -> n + " is " + a + " years old").get();
        System.out.println("Combined: " + combined);

        // Solution 4: allOf
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");
        CompletableFuture.allOf(f1, f2, f3).join();
        System.out.println("All: " + f1.get() + f2.get() + f3.get());
    }
}
