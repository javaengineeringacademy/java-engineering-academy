package academy.javaengineering.senior.concurrency;

import java.util.List;
import java.util.concurrent.*;

public class CompletableFutureDeepDive {

    // ============================================================
    // 1. CompletableFuture Creation
    // ============================================================

    public static CompletableFuture<String> createWithCompletedFuture() {
        return CompletableFuture.completedFuture("Immediate value");
    }

    public static CompletableFuture<String> createWithSupplyAsync() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(100);
            return "Async result";
        });
    }

    public static CompletableFuture<Void> createWithRunAsync() {
        return CompletableFuture.runAsync(() -> {
            simulateDelay(50);
            System.out.println("Side-effect only, no return value");
        });
    }

    // ============================================================
    // 2. Chaining Operations
    // ============================================================

    public static CompletableFuture<String> chainingWithThenApply() {
        return CompletableFuture
            .supplyAsync(() -> "user-42")
            .thenApply(id -> "User " + id)
            .thenApply(name -> name.toUpperCase());
    }

    public static CompletableFuture<String> chainingWithThenCompose() {
        return CompletableFuture
            .supplyAsync(() -> 42)
            .thenCompose(id -> CompletableFuture.supplyAsync(() -> "Lookup-" + id));
    }

    public static CompletableFuture<String> chainingWithThenCombine() {
        CompletableFuture<String> nameFuture = CompletableFuture.supplyAsync(() -> "Alice");
        CompletableFuture<Integer> ageFuture = CompletableFuture.supplyAsync(() -> 30);

        return nameFuture.thenCombine(ageFuture, (name, age) -> name + " is " + age);
    }

    public static CompletableFuture<Void> chainingWithThenAcceptAll() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Data-1");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Data-2");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "Data-3");

        return CompletableFuture.allOf(f1, f2, f3)
            .thenAccept(v -> {
                System.out.println(f1.join());
                System.out.println(f2.join());
                System.out.println(f3.join());
            });
    }

    // ============================================================
    // 3. Exception Handling
    // ============================================================

    public static CompletableFuture<String> handleWithExceptionally() {
        return CompletableFuture
            .supplyAsync(() -> {
                if (true) throw new RuntimeException("Service down");
                return "OK";
            })
            .exceptionally(ex -> "Fallback: " + ex.getMessage());
    }

    public static CompletableFuture<String> handleWithHandle() {
        return CompletableFuture
            .supplyAsync(() -> {
                throw new RuntimeException("Oops");
            })
            .handle((result, ex) -> ex != null ? "Error handled" : "Result: " + result);
    }

    public static CompletableFuture<String> handleWithWhenComplete() {
        return CompletableFuture
            .supplyAsync(() -> "Success")
            .whenComplete((result, ex) -> {
                if (ex != null) System.err.println("Failed: " + ex.getMessage());
                else System.out.println("Completed with: " + result);
            });
    }

    // ============================================================
    // 4. Composition (allOf / anyOf)
    // ============================================================

    public static List<String> composeWithAllOf() throws Exception {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");

        CompletableFuture<Void> allDone = CompletableFuture.allOf(f1, f2, f3);
        allDone.join();

        return List.of(f1.join(), f2.join(), f3.join());
    }

    public static String composeWithAnyOf() throws Exception {
        CompletableFuture<String> slow = CompletableFuture.supplyAsync(() -> {
            simulateDelay(2000);
            return "Slow";
        });
        CompletableFuture<String> fast = CompletableFuture.supplyAsync(() -> {
            simulateDelay(50);
            return "Fast";
        });

        Object winner = CompletableFuture.anyOf(slow, fast).join();
        return (String) winner;
    }

    // ============================================================
    // 5. Timeout Handling
    // ============================================================

    public static CompletableFuture<String> handleWithTimeout() {
        return CompletableFuture
            .supplyAsync(() -> {
                simulateDelay(3000);
                return "Done";
            })
            .orTimeout(1, TimeUnit.SECONDS);
    }

    public static CompletableFuture<String> handleWithCompleteOnTimeout() {
        return CompletableFuture
            .supplyAsync(() -> {
                simulateDelay(3000);
                return "Done";
            })
            .completeOnTimeout("Default value", 1, TimeUnit.SECONDS);
    }

    // ============================================================
    // 6. Real-World: Parallel API Calls with Fallback
    // ============================================================

    public static String fetchUserDataWithFallback(String userId) {
        CompletableFuture<String> userFuture = CompletableFuture
            .supplyAsync(() -> callExternalService("user-" + userId))
            .exceptionally(ex -> "Anonymous");

        CompletableFuture<Double> balanceFuture = CompletableFuture
            .supplyAsync(() -> callExternalService("balance-" + userId))
            .handle((result, ex) -> ex != null ? 0.0 : Double.parseDouble(result));

        CompletableFuture<List<String>> ordersFuture = CompletableFuture
            .supplyAsync(() -> callExternalService("orders-" + userId))
            .exceptionally(ex -> List.of());

        CompletableFuture<String> response = userFuture
            .thenCombine(balanceFuture, (user, balance) -> user + " | $" + balance)
            .thenCombine(ordersFuture, (info, orders) -> info + " | orders=" + orders.size());

        return response.completeOnTimeout("Timed out", 2, TimeUnit.SECONDS).join();
    }

    // ============================================================
    // Helpers
    // ============================================================

    private static void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static String callExternalService(String key) {
        simulateDelay(100);
        if (key.startsWith("orders-")) return "1,2,3";
        return key.replace("-", "_");
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Creation ===");
        System.out.println("completedFuture: " + createWithCompletedFuture().join());
        System.out.println("supplyAsync: " + createWithSupplyAsync().join());
        createWithRunAsync().join();

        System.out.println("\n=== Chaining ===");
        System.out.println("thenApply: " + chainingWithThenApply().join());
        System.out.println("thenCompose: " + chainingWithThenCompose().join());
        System.out.println("thenCombine: " + chainingWithThenCombine().join());
        chainingWithThenAcceptAll().join();

        System.out.println("\n=== Exception Handling ===");
        System.out.println("exceptionally: " + handleWithExceptionally().join());
        System.out.println("handle: " + handleWithHandle().join());
        System.out.println("whenComplete: " + handleWithWhenComplete().join());

        System.out.println("\n=== Composition ===");
        System.out.println("allOf: " + composeWithAllOf());
        System.out.println("anyOf: " + composeWithAnyOf());

        System.out.println("\n=== Timeout ===");
        System.out.println("orTimeout: " + handleWithTimeout().join());
        System.out.println("completeOnTimeout: " + handleWithCompleteOnTimeout().join());

        System.out.println("\n=== Real-World ===");
        System.out.println("API response: " + fetchUserDataWithFallback("123"));
    }
}
