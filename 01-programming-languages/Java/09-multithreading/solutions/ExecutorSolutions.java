package academy.javaengineering.concurrency.solutions;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Solutions: Executor Service and CompletableFuture
 */
public class ExecutorSolutions {

    // Solution 1: Execute tasks and collect results in order
    public java.util.List<Integer> executeTasks(int n) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(n);
        try {
            java.util.List<Future<Integer>> futures = new java.util.ArrayList<>();
            for (int i = 0; i < n; i++) {
                final int idx = i;
                futures.add(pool.submit(() -> idx * 2));
            }
            java.util.List<Integer> results = new java.util.ArrayList<>();
            for (Future<Integer> f : futures) {
                results.add(f.get());
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // Solution 2: Parallel map
    public <T, R> java.util.List<R> parallelMap(java.util.List<T> items, java.util.function.Function<T, R> mapper) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(items.size());
        try {
            java.util.List<Future<R>> futures = new java.util.ArrayList<>();
            for (T item : items) {
                futures.add(pool.submit(() -> mapper.apply(item)));
            }
            java.util.List<R> results = new java.util.ArrayList<>();
            for (Future<R> f : futures) {
                results.add(f.get());
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // Solution 3: Execute with timeout
    public <T> T executeWithTimeout(Callable<T> task, long timeoutMs) throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<T> future = pool.submit(task);
            try {
                return future.get(timeoutMs, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                return null;
            }
        } finally {
            pool.shutdown();
        }
    }

    // Solution 4: CompletableFuture pipeline
    public CompletableFuture<Double> fetchAndCalculateTotal(String userId) {
        return fetchUserInfo(userId)
            .thenApply(this::fetchOrders)
            .thenApply(future -> {
                try {
                    java.util.List<Double> orders = future.get();
                    return orders.stream().mapToDouble(Double::doubleValue).sum();
                } catch (Exception e) {
                    return 0.0;
                }
            });
    }

    private CompletableFuture<String> fetchUserInfo(String userId) {
        return CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            if ("U001".equals(userId)) return "Alice:ENGINEERING";
            if ("U002".equals(userId)) return "Bob:MARKETING";
            return null;
        });
    }

    private CompletableFuture<java.util.List<Double>> fetchOrders(String userInfo) {
        return CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            if (userInfo == null) return java.util.List.of();
            if (userInfo.contains("ENGINEERING")) return java.util.List.of(100.0, 200.0, 150.0);
            return java.util.List.of(50.0, 75.0);
        });
    }

    // Solution 5: Submit and wait all
    public <T> java.util.List<T> submitAndWaitAll(java.util.List<Callable<T>> tasks) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(tasks.size());
        try {
            java.util.List<Future<T>> futures = pool.invokeAll(tasks);
            java.util.List<T> results = new java.util.ArrayList<>();
            for (Future<T> f : futures) {
                results.add(f.get());
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // Solution 6: Retry on failure
    public <T> T retryOnFailure(Callable<T> callable, int maxRetries) throws Exception {
        Exception lastException = null;
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return callable.call();
            } catch (Exception e) {
                lastException = e;
            }
        }
        throw new ExecutionException("Failed after " + (maxRetries + 1) + " attempts", lastException);
    }

    public static void main(String[] args) throws Exception {
        ExecutorSolutions sol = new ExecutorSolutions();
        System.out.println("=== ExecutorService Solutions ===\n");

        System.out.println("1. executeTasks(5): " + sol.executeTasks(5));
        System.out.println("2. parallelMap([1,2,3,4,5], x*2): " + sol.parallelMap(java.util.List.of(1, 2, 3, 4, 5), x -> x * 2));
        System.out.println("3. executeWithTimeout(fast, 1000): " + sol.executeWithTimeout(() -> "done", 1000));
        System.out.println("4. fetchAndCalculateTotal(U001): " + sol.fetchAndCalculateTotal("U001").get(5, TimeUnit.SECONDS));
        System.out.println("5. submitAndWaitAll: " + sol.submitAndWaitAll(java.util.List.of(() -> "A", () -> "B")));
        System.out.println("6. retryOnFailure: " + sol.retryOnFailure(() -> "success", 3));
    }
}
