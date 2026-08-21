package academy.javaengineering.senior.examples;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

public class AdvancedConcurrencyDemo {

    // CompletableFuture Composition
    static CompletableFuture<String> fetchUserId() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(100);
            return "user-123";
        });
    }

    static CompletableFuture<String> fetchUserName(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(80);
            return "Alice Johnson";
        });
    }

    static CompletableFuture<Double> fetchAccountBalance(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(120);
            return 15420.75;
        });
    }

    // Virtual Threads - structured parallel execution
    static String fetchProductDetails(String productId) {
        simulateDelay(50);
        return "Product: " + productId + " - Premium Widget";
    }

    static double fetchProductPrice(String productId) {
        simulateDelay(40);
        return 29.99;
    }

    // CompletableFuture with exception handling
    static CompletableFuture<String> unreliableService() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(50);
            if (Math.random() > 0.5) {
                throw new RuntimeException("Service unavailable");
            }
            return "Success";
        });
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Advanced Concurrency Demo ===\n");

        // 1. CompletableFuture composition
        System.out.println("--- CompletableFuture Composition ---");
        long start = System.currentTimeMillis();

        CompletableFuture<String> userIdFuture = fetchUserId();
        CompletableFuture<String> userNameFuture = userIdFuture.thenComposeAsync(
            AdvancedConcurrencyDemo::fetchUserName
        );
        CompletableFuture<Double> balanceFuture = userIdFuture.thenComposeAsync(
            AdvancedConcurrencyDemo::fetchAccountBalance
        );

        CompletableFuture<String> combined = userNameFuture
            .thenCombine(balanceFuture, (name, balance) ->
                String.format("User: %s, Balance: $%.2f", name, balance)
            );

        System.out.println(combined.get());
        System.out.printf("Completed in %dms%n%n", System.currentTimeMillis() - start);

        // 2. AllOf - parallel independent operations
        System.out.println("--- Parallel Independent Operations (allOf) ---");
        start = System.currentTimeMillis();

        List<String> productIds = List.of("P001", "P002", "P003", "P004", "P005");
        CompletableFuture<?>[] futures = productIds.stream()
            .map(id -> CompletableFuture.runAsync(() -> {
                String details = fetchProductDetails(id);
                double price = fetchProductPrice(id);
                System.out.printf("  %s - $%.2f%n", details, price);
            }))
            .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
        System.out.printf("All fetched in %dms%n%n", System.currentTimeMillis() - start);

        // 3. Exception handling with recovery
        System.out.println("--- Exception Handling with Recovery ---");
        CompletableFuture<String> withRecovery = unreliableService()
            .exceptionally(ex -> {
                System.out.println("  Primary failed: " + ex.getMessage());
                return "Fallback value";
            });

        CompletableFuture<String> withRetry = unreliableService()
            .handle((result, ex) -> {
                if (ex != null) {
                    System.out.println("  Retrying after failure...");
                    return retryOperation();
                }
                return result;
            });

        System.out.println("  With recovery: " + withRecovery.get());
        System.out.println("  With retry: " + withRetry.get());

        // 4. Virtual Threads demo
        System.out.println("\n--- Virtual Threads ---");
        start = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> results = executor.invokeAll(
                productIds.stream()
                    .map(id -> (Callable<String>) () -> {
                        long threadId = Thread.currentThread().threadId();
                        String details = fetchProductDetails(id);
                        double price = fetchProductPrice(id);
                        return String.format("  Thread %d: %s $%.2f", threadId, details, price);
                    })
                    .toList()
            );

            for (Future<String> f : results) {
                System.out.println(f.get());
            }
        }

        System.out.printf("Virtual threads completed in %dms%n", System.currentTimeMillis() - start);

        // 5. Thread pool deadlock example
        System.out.println("\n--- Thread Pool Deadlock Avoidance ---");
        ExecutorService smallPool = Executors.newFixedThreadPool(2);

        try {
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                simulateDelay(50);
                return "Result 1";
            }, smallPool);

            CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
                simulateDelay(50);
                return "Result 2";
            }, smallPool);

            // Safe: thenApply runs on calling thread by default
            String result = future1.thenApply(s -> s + " processed").get();
            System.out.println("  Safe result: " + result);
        } finally {
            smallPool.shutdown();
        }

        System.out.println("\n=== Demo Complete ===");
    }

    static String retryOperation() {
        simulateDelay(30);
        return "Retry succeeded";
    }

    static void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
