package academy.javaengineering.senior.concurrency;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;

public class CompletableFuturePatterns {

    public static void main(String[] args) throws Exception {
        parallelApiCallsWithTimeout();
        fallbackStrategies();
        retryWithBackoff();
        chainingAsyncOperations();
        errorHandlingPatterns();
    }

    private static void parallelApiCallsWithTimeout() throws Exception {
        System.out.println("=== Parallel API Calls with Timeout ===");
        long start = System.currentTimeMillis();

        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
            sleep(200);
            return "User:John Doe";
        });

        CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> {
            sleep(300);
            return "Order:12345";
        });

        CompletableFuture<String> productFuture = CompletableFuture.supplyAsync(() -> {
            sleep(150);
            return "Product:Laptop";
        });

        CompletableFuture<Void> allDone = CompletableFuture.allOf(userFuture, orderFuture, productFuture);
        CompletableFuture<List<String>> combined = allDone.thenApply(v ->
            List.of(userFuture.join(), orderFuture.join(), productFuture.join())
        );

        try {
            List<String> results = combined.get(1, TimeUnit.SECONDS);
            System.out.println("All API results: " + results);
        } catch (TimeoutException e) {
            System.out.println("Timeout! Cancelling all...");
            combined.cancel(true);
            userFuture.cancel(true);
            orderFuture.cancel(true);
            productFuture.cancel(true);
        }

        System.out.println("Completed in " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void fallbackStrategies() throws Exception {
        System.out.println("\n=== Fallback Strategies ===");

        CompletableFuture<String> primary = CompletableFuture.supplyAsync(() -> {
            sleep(100);
            if (Math.random() > 0.3) throw new RuntimeException("Primary service failed");
            return "Primary result";
        });

        CompletableFuture<String> fallback1 = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            if (Math.random() > 0.5) throw new RuntimeException("Fallback1 failed");
            return "Fallback1 result";
        });

        CompletableFuture<String> fallback2 = CompletableFuture.supplyAsync(() -> "Fallback2 result");

        CompletableFuture<String> withFallback = primary
            .exceptionallyCompose(ex -> {
                System.out.println("Primary failed: " + ex.getMessage());
                return fallback1;
            })
            .exceptionallyCompose(ex -> {
                System.out.println("Fallback1 failed: " + ex.getMessage());
                return fallback2;
            });

        System.out.println("Final result: " + withFallback.get());
    }

    private static void retryWithBackoff() throws Exception {
        System.out.println("\n=== Retry with Backoff ===");
        AtomicInteger attempts = new AtomicInteger(0);
        int maxRetries = 3;

        CompletableFuture<String> result = retryWithBackoff(() ->
            CompletableFuture.supplyAsync(() -> {
                int attempt = attempts.incrementAndGet();
                System.out.println("Attempt " + attempt);
                if (attempt < 3) throw new RuntimeException("Not ready yet");
                return "Success on attempt " + attempt;
            }), maxRetries, 200
        );

        System.out.println("Retry result: " + result.get());
    }

    private static <T> CompletableFuture<T> retryWithBackoff(
            Supplier<CompletableFuture<T>> taskSupplier, int maxRetries, long initialDelayMs) {
        return taskSupplier.get()
            .thenApply(CompletableFuture::completedFuture)
            .exceptionallyCompose(ex -> {
                if (maxRetries <= 0) {
                    CompletableFuture<T> failed = new CompletableFuture<>();
                    failed.completeExceptionally(ex);
                    return failed;
                }
                System.out.println("Retrying in " + initialDelayMs + "ms... (" + maxRetries + " left)");
                return CompletableFuture.delayedExecutor(initialDelayMs, TimeUnit.MILLISECONDS)
                    .thenCompose(v -> retryWithBackoff(taskSupplier, maxRetries - 1, initialDelayMs * 2));
            })
            .thenCompose(f -> f);
    }

    private static void chainingAsyncOperations() throws Exception {
        System.out.println("\n=== Chaining Multiple Async Operations ===");

        CompletableFuture<String> pipeline = CompletableFuture
            .supplyAsync(() -> {
                System.out.println("Step 1: Fetch user data");
                sleep(100);
                return "userId=123";
            })
            .thenApplyAsync(userData -> {
                System.out.println("Step 2: Validate " + userData);
                sleep(50);
                return userData + ",valid=true";
            })
            .thenComposeAsync(validatedData -> {
                System.out.println("Step 3: Fetch orders for " + validatedData);
                return CompletableFuture.supplyAsync(() -> {
                    sleep(150);
                    return validatedData + ",orders=[1,2,3]";
                });
            })
            .thenCombineAsync(
                CompletableFuture.supplyAsync(() -> {
                    sleep(100);
                    return "rewards=500";
                }),
                (orders, rewards) -> orders + "," + rewards
            )
            .thenApplyAsync(allData -> {
                System.out.println("Step 5: Format final response");
                return "Response{" + allData + "}";
            });

        System.out.println("Pipeline result: " + pipeline.get(5, TimeUnit.SECONDS));
    }

    private static void errorHandlingPatterns() throws Exception {
        System.out.println("\n=== Error Handling Patterns ===");

        CompletableFuture<String> task = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            throw new RuntimeException("Simulated failure");
        });

        String result1 = task
            .exceptionally(ex -> {
                System.out.println("Recovered with default: " + ex.getMessage());
                return "default value";
            }).join();
        System.out.println("With exceptionally: " + result1);

        String result2 = task
            .handle((result, ex) -> {
                if (ex != null) {
                    System.out.println("Handled error in handle(): " + ex.getMessage());
                    return "handled value";
                }
                return result;
            }).join();
        System.out.println("With handle: " + result2);

        CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            throw new RuntimeException("Another failure");
        });

        String result3 = task2
            .exceptionallyCompose(ex -> {
                System.out.println("Composing fallback after error");
                return CompletableFuture.completedFuture("composed fallback");
            }).join();
        System.out.println("With exceptionallyCompose: " + result3);

        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            sleep(50);
            throw new RuntimeException("Timeout simulation");
        });

        try {
            String result4 = task3.orTimeout(1, TimeUnit.MILLISECONDS).join();
        } catch (CompletionException e) {
            System.out.println("Timeout caught: " + e.getCause().getClass().getSimpleName());
        }

        System.out.println("All error handling patterns demonstrated");
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @FunctionalInterface
    interface Supplier<T> {
        T get();
    }
}
