package academy.javaengineering.concurrency.executor.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Solutions for ExecutorService practice exercises.
 */
public class Solutions {

    // Solution 1: Execute tasks and collect results in order
    public List<Integer> executeTasks(int n) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(n);
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                final int idx = i;
                futures.add(pool.submit(() -> idx * 2));
            }

            List<Integer> results = new ArrayList<>();
            for (Future<Integer> f : futures) {
                results.add(f.get());
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // Solution 2: Parallel map with ordered results
    public <T, R> List<R> parallelMap(List<T> items, java.util.function.Function<T, R> mapper) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(items.size());
        try {
            List<Future<R>> futures = new ArrayList<>();
            for (T item : items) {
                futures.add(pool.submit(() -> mapper.apply(item)));
            }

            List<R> results = new ArrayList<>();
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

    // Solution 4: Retry on failure
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

    // Solution 5: Submit and wait for all
    public <T> List<T> submitAndWaitAll(List<Callable<T>> tasks) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(tasks.size());
        try {
            List<Future<T>> futures = pool.invokeAll(tasks);
            List<T> results = new ArrayList<>();
            for (Future<T> f : futures) {
                results.add(f.get());
            }
            return results;
        } finally {
            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    // Solution 6: Async pipeline
    public CompletableFuture<Double> fetchAndCalculateTotal(String userId) {
        return fetchUserInfo(userId)
            .thenApply(this::fetchOrders)
            .thenApply(future -> {
                try {
                    List<Double> orders = future.get();
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

    private CompletableFuture<List<Double>> fetchOrders(String userInfo) {
        return CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            if (userInfo == null) return List.of();
            if (userInfo.contains("ENGINEERING")) return List.of(100.0, 200.0, 150.0);
            return List.of(50.0, 75.0);
        });
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        Solutions solutions = new Solutions();
        int passed = 0;
        int total = 0;

        System.out.println("=== ExecutorService Solutions Tests ===\n");

        // Test 1
        total++;
        try {
            List<Integer> results = solutions.executeTasks(5);
            if (results.size() == 5 && results.get(0) == 0 && results.get(4) == 8) {
                System.out.println("Test 1 PASSED: executeTasks");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: got " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            List<Integer> doubled = solutions.parallelMap(List.of(1, 2, 3, 4, 5), x -> x * 2);
            if (doubled.equals(List.of(2, 4, 6, 8, 10))) {
                System.out.println("Test 2 PASSED: parallelMap");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: got " + doubled);
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            String fast = solutions.executeWithTimeout(() -> "done", 1000);
            String slow = solutions.executeWithTimeout(() -> {
                Thread.sleep(500);
                return "slow";
            }, 50);
            if ("done".equals(fast) && slow == null) {
                System.out.println("Test 3 PASSED: executeWithTimeout");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: fast=" + fast + ", slow=" + slow);
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            final int[] attempts = {0};
            String result = solutions.retryOnFailure(() -> {
                attempts[0]++;
                if (attempts[0] < 3) throw new RuntimeException("Not yet");
                return "success";
            }, 5);
            if ("success".equals(result) && attempts[0] == 3) {
                System.out.println("Test 4 PASSED: retryOnFailure");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: result=" + result + ", attempts=" + attempts[0]);
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            List<Callable<String>> tasks = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                final int idx = i;
                tasks.add(() -> "Task-" + idx);
            }
            List<String> results = solutions.submitAndWaitAll(tasks);
            if (results.size() == 5 && "Task-0".equals(results.get(0))) {
                System.out.println("Test 5 PASSED: submitAndWaitAll");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: got " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            CompletableFuture<Double> future = solutions.fetchAndCalculateTotal("U001");
            Double totalAmount = future.get(5, TimeUnit.SECONDS);
            if (Math.abs(totalAmount - 450.0) < 0.01) {
                System.out.println("Test 6 PASSED: fetchAndCalculateTotal");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: got " + totalAmount);
            }
        } catch (Exception e) {
            System.out.println("Test 6 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
