package academy.javaengineering.concurrency.practices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Exercises: Executor Service and CompletableFuture
 *
 * Complete the TODO sections below.
 */
public class ExecutorExercises {

    // TODO 1: Create a fixed thread pool and submit tasks
    // Execute n tasks, each returning its index * 2
    // Collect all results into a list, maintaining order
    public List<Integer> executeTasks(int n) throws Exception {
        // TODO: implement this using ExecutorService
        return new ArrayList<>();
    }

    // TODO 2: Implement parallel map operation
    // Apply a function to each element in parallel using thread pool
    // Return results in the same order as input
    public <T, R> List<R> parallelMap(List<T> items, java.util.function.Function<T, R> mapper) throws Exception {
        // TODO: implement this
        return new ArrayList<>();
    }

    // TODO 3: Implement a timeout for a task
    // Submit a callable and return the result
    // If the task doesn't complete within timeoutMs, return null
    public <T> T executeWithTimeout(Callable<T> task, long timeoutMs) {
        // TODO: implement this using ExecutorService with timeout
        return null;
    }

    // TODO 4: Chain CompletableFuture operations
    // Given a userId, perform these steps asynchronously:
    // 1. Fetch user info (simulate with delay)
    // 2. Based on user info, fetch their orders
    // 3. Calculate total from orders
    // Return the final total
    public CompletableFuture<Double> fetchAndCalculateTotal(String userId) {
        // TODO: implement this using CompletableFuture
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

    // TODO 5: Implement allOf with result collection
    // Submit multiple tasks and wait for all to complete
    // Return list of all results in submission order
    public <T> List<T> submitAndWaitAll(List<Callable<T>> tasks) throws Exception {
        // TODO: implement this using ExecutorService and invokeAll
        return new ArrayList<>();
    }

    // TODO 6: Implement retry logic with CompletableFuture
    // Retry a callable up to maxRetries times if it throws an exception
    // Return the first successful result
    public <T> T retryOnFailure(Callable<T> callable, int maxRetries) {
        // TODO: implement this using CompletableFuture
        return null;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        ExecutorExercises exercises = new ExecutorExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ExecutorExercises Tests ===\n");

        // Test 1
        total++;
        try {
            List<Integer> results = exercises.executeTasks(5);
            if (results.size() == 5 && results.get(0) == 0 && results.get(4) == 8) {
                System.out.println("Test 1 PASSED: executeTasks");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: executeTasks - got " + results);
            }
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.out.println("Test 1 FAILED: executeTasks - " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            List<Integer> items = List.of(1, 2, 3, 4, 5);
            List<Integer> doubled = exercises.parallelMap(items, x -> x * 2);
            if (doubled.equals(List.of(2, 4, 6, 8, 10))) {
                System.out.println("Test 2 PASSED: parallelMap");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: parallelMap - got " + doubled);
            }
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.out.println("Test 2 FAILED: parallelMap - " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            // Fast task
            String fast = exercises.executeWithTimeout(() -> "done", 1000);
            // Slow task with short timeout
            String slow = exercises.executeWithTimeout(() -> {
                Thread.sleep(500);
                return "slow";
            }, 50);
            if ("done".equals(fast) && slow == null) {
                System.out.println("Test 3 PASSED: executeWithTimeout");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: executeWithTimeout - fast=" + fast + ", slow=" + slow);
            }
        } catch (InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
            System.out.println("Test 3 FAILED: executeWithTimeout - " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            CompletableFuture<Double> future = exercises.fetchAndCalculateTotal("U001");
            Double totalAmount = future.get(5, TimeUnit.SECONDS);
            if (Math.abs(totalAmount - 450.0) < 0.01) {
                System.out.println("Test 4 PASSED: fetchAndCalculateTotal");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: fetchAndCalculateTotal - got " + totalAmount);
            }
        } catch (InterruptedException | java.util.concurrent.ExecutionException | java.util.concurrent.TimeoutException e) {
            System.out.println("Test 4 FAILED: fetchAndCalculateTotal - " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            List<Callable<String>> tasks = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                final int idx = i;
                tasks.add(() -> "Task-" + idx);
            }
            List<String> results = exercises.submitAndWaitAll(tasks);
            if (results.size() == 5 && "Task-0".equals(results.get(0)) && "Task-4".equals(results.get(4))) {
                System.out.println("Test 5 PASSED: submitAndWaitAll");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: submitAndWaitAll - got " + results);
            }
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.out.println("Test 5 FAILED: submitAndWaitAll - " + e.getMessage());
        }

        // Test 6
        total++;
        try {
            final int[] attempts = {0};
            String result = exercises.retryOnFailure(() -> {
                attempts[0]++;
                if (attempts[0] < 3) throw new RuntimeException("Not yet");
                return "success";
            }, 5);
            if ("success".equals(result) && attempts[0] == 3) {
                System.out.println("Test 6 PASSED: retryOnFailure");
                passed++;
            } else {
                System.out.println("Test 6 FAILED: retryOnFailure - result=" + result + ", attempts=" + attempts[0]);
            }
        } catch (InterruptedException | java.util.concurrent.ExecutionException e) {
            System.out.println("Test 6 FAILED: retryOnFailure - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
