import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Advanced concurrency examples demonstrating CompletableFuture,
 * Virtual Threads, and structured concurrency patterns.
 */
public class AdvancedConcurrency {

    // ============================================================
    // CompletableFuture Examples
    // ============================================================

    /**
     * Basic CompletableFuture chaining.
     */
    public static CompletableFuture<String> fetchAndProcessAsync() {
        return CompletableFuture
            .supplyAsync(() -> {
                simulateDelay(100);
                return "user-123";
            })
            .thenApplyAsync(userId -> {
                simulateDelay(100);
                return "John Doe";
            })
            .thenApplyAsync(name -> {
                simulateDelay(50);
                return "Hello, " + name + "!";
            });
    }

    /**
     * Combining multiple futures.
     */
    public static CompletableFuture<String> combineUserData() {
        CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(200);
            return "John Doe";
        });

        CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(300);
            return "Order-456";
        });

        CompletableFuture<String> balanceFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(150);
            return "$1,234.56";
        });

        return CompletableFuture.allOf(userFuture, orderFuture, balanceFuture)
            .thenApply(v -> String.format("User: %s, Order: %s, Balance: %s",
                userFuture.join(),
                orderFuture.join(),
                balanceFuture.join()));
    }

    /**
     * Error handling in CompletableFuture.
     */
    public static CompletableFuture<String> withErrorHandling() {
        return CompletableFuture
            .supplyAsync(() -> {
                if (Math.random() > 0.5) {
                    throw new RuntimeException("Service unavailable");
                }
                return "Success";
            })
            .exceptionally(ex -> {
                System.err.println("Error: " + ex.getMessage());
                return "Fallback value";
            })
            .thenApply(result -> "Result: " + result);
    }

    /**
     * Timeout handling.
     */
    public static CompletableFuture<String> withTimeout() {
        return CompletableFuture
            .supplyAsync(() -> {
                simulateDelay(5000);  // Slow operation
                return "Done";
            })
            .completeOnTimeout("Timeout!", 1, TimeUnit.SECONDS);
    }

    // ============================================================
    // Virtual Threads Examples
    // ============================================================

    /**
     * Demonstrates massive concurrency with virtual threads.
     */
    public static void virtualThreadsDemo() throws Exception {
        long start = System.currentTimeMillis();
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();
            
            for (int i = 0; i < 100_000; i++) {
                final int taskId = i;
                futures.add(executor.submit(() -> {
                    simulateDelay(100);
                    return "Task " + taskId + " completed";
                }));
            }
            
            // Wait for all tasks
            for (Future<String> future : futures) {
                future.get();
            }
        }
        
        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("100,000 tasks completed in %d ms%n", elapsed);
    }

    /**
     * Virtual threads with structured concurrency (simplified demo).
     */
    public static String structuredConcurrencyDemo() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // Launch independent tasks
            Future<String> userFuture = executor.submit(() -> {
                simulateDelay(200);
                return "User-123";
            });
            
            Future<String> orderFuture = executor.submit(() -> {
                simulateDelay(300);
                return "Order-456";
            });
            
            // Both must complete before proceeding
            String user = userFuture.get();
            String order = orderFuture.get();
            
            return String.format("User: %s, Order: %s", user, order);
        }
    }

    /**
     * Virtual threads avoiding pinning.
     */
    public static void pinningDemo() {
        // BAD: Using synchronized (will pin virtual thread)
        synchronized (AdvancedConcurrency.class) {
            simulateDelay(100);
        }
        
        // GOOD: Using ReentrantLock (won't pin)
        var lock = new java.util.concurrent.locks.ReentrantLock();
        lock.lock();
        try {
            simulateDelay(100);
        } finally {
            lock.unlock();
        }
    }

    // ============================================================
    // Fork/Join Example
    // ============================================================

    /**
     * Parallel sum using Fork/Join framework.
     */
    public static class ParallelSum extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start;
        private final int end;

        public ParallelSum(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            int mid = (start + end) / 2;
            ParallelSum left = new ParallelSum(array, start, mid);
            ParallelSum right = new ParallelSum(array, mid, end);

            left.fork();
            long rightResult = right.compute();
            long leftResult = left.join();

            return leftResult + rightResult;
        }
    }

    // ============================================================
    // Helper Methods
    // ============================================================

    private static void simulateDelay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== CompletableFuture Demo ===");
        
        // Basic chaining
        String result1 = fetchAndProcessAsync().join();
        System.out.println("Chained: " + result1);
        
        // Combining futures
        String result2 = combineUserData().join();
        System.out.println("Combined: " + result2);
        
        // Error handling
        String result3 = withErrorHandling().join();
        System.out.println("Error handling: " + result3);
        
        // Timeout
        String result4 = withTimeout().join();
        System.out.println("Timeout: " + result4);
        
        System.out.println("\n=== Virtual Threads Demo ===");
        
        virtualThreadsDemo();
        
        String result5 = structuredConcurrencyDemo();
        System.out.println("Structured: " + result5);
        
        System.out.println("\n=== Fork/Join Demo ===");
        
        long[] array = new long[100_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        
        ForkJoinPool pool = new ForkJoinPool();
        try {
            long sum = pool.invoke(new ParallelSum(array, 0, array.length));
            System.out.println("Parallel sum: " + sum);
        } finally {
            pool.shutdown();
        }
    }
}
