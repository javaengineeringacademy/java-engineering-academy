package academy.javaengineering.concurrency.executor.practices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * ExecutorService practice exercises.
 * Complete the TODO sections in each method.
 */
public class Practices {

    // TODO 1: Create a fixed thread pool, submit n tasks, collect results in order
    // Each task should return its index * 2
    public List<Integer> executeTasks(int n) throws Exception {
        // TODO: Create ExecutorService, submit tasks, collect results
        return new ArrayList<>();
    }

    // TODO 2: Implement parallel map — apply function to each element concurrently
    // Return results in the same order as input
    public <T, R> List<R> parallelMap(List<T> items, java.util.function.Function<T, R> mapper) throws Exception {
        // TODO: Use ExecutorService to apply mapper in parallel
        return new ArrayList<>();
    }

    // TODO 3: Submit a callable with timeout — return null if it doesn't complete in time
    public <T> T executeWithTimeout(Callable<T> task, long timeoutMs) throws Exception {
        // TODO: Submit task, use Future.get(timeout), cancel on timeout
        return null;
    }

    // TODO 4: Implement retry logic — retry callable up to maxRetries on failure
    public <T> T retryOnFailure(Callable<T> callable, int maxRetries) throws Exception {
        // TODO: Loop with try-catch, return first success or throw last exception
        return null;
    }

    // TODO 5: Submit multiple tasks with invokeAll, return all results
    public <T> List<T> submitAndWaitAll(List<Callable<T>> tasks) throws Exception {
        // TODO: Use invokeAll(), collect results from futures
        return new ArrayList<>();
    }

    // TODO 6: Build an async pipeline — fetch user, then orders, then total
    public CompletableFuture<Double> fetchAndCalculateTotal(String userId) {
        // TODO: Chain supplyAsync/thenApply/thenApply/thenApply
        return CompletableFuture.completedFuture(0.0);
    }

    // Simulated async operations
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
        Practices practices = new Practices();
        int passed = 0;
        int total = 0;

        System.out.println("=== ExecutorService Practices ===\n");

        // Test 1
        total++;
        try {
            List<Integer> results = practices.executeTasks(5);
            if (results.size() == 5 && results.get(0) == 0 && results.get(4) == 8) {
                System.out.println("Test 1 PASSED: executeTasks");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: executeTasks - got " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            List<Integer> items = List.of(1, 2, 3, 4, 5);
            List<Integer> doubled = practices.parallelMap(items, x -> x * 2);
            if (doubled.equals(List.of(2, 4, 6, 8, 10))) {
                System.out.println("Test 2 PASSED: parallelMap");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: parallelMap - got " + doubled);
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            String fast = practices.executeWithTimeout(() -> "done", 1000);
            String slow = practices.executeWithTimeout(() -> {
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
            String result = practices.retryOnFailure(() -> {
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
            List<String> results = practices.submitAndWaitAll(tasks);
            if (results.size() == 5 && "Task-0".equals(results.get(0)) && "Task-4".equals(results.get(4))) {
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
            CompletableFuture<Double> future = practices.fetchAndCalculateTotal("U001");
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
