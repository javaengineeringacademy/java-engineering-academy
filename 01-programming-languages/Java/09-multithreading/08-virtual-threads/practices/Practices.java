package academy.javaengineering.concurrency.virtualthreads.practices;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Virtual threads practice exercises.
 * Complete the TODO sections in each method.
 */
public class Practices {

    // TODO 1: Create 1000 virtual threads that each sleep for 10ms
    // Return the total execution time
    public long executeVirtualThreads(int count, long sleepMs) throws Exception {
        // TODO: Use Executors.newVirtualThreadPerTaskExecutor()
        return 0;
    }

    // TODO 2: Implement fan-out/fan-in pattern
    // Submit count tasks in parallel, collect all results
    public <T> java.util.List<T> fanOutFanIn(java.util.List<java.util.concurrent.Callable<T>> tasks) throws Exception {
        // TODO: Use virtual thread executor, submit all, collect results
        return new java.util.ArrayList<>();
    }

    // TODO 3: Implement rate limiting with Semaphore
    // Allow maxConcurrency virtual threads to run simultaneously
    public int executeWithRateLimit(int taskCount, int maxConcurrency) throws Exception {
        // TODO: Use Semaphore + virtual thread executor
        return 0;
    }

    // TODO 4: Fix pinning — replace synchronized with ReentrantLock
    // Measure performance difference
    public long executeWithoutPinning(int taskCount, long sleepMs) throws Exception {
        // TODO: Use ReentrantLock instead of synchronized
        return 0;
    }

    // TODO 5: Implement timeout for virtual thread tasks
    // Submit tasks, cancel any that don't complete within timeoutMs
    public <T> java.util.List<T> executeWithTimeout(java.util.List<Callable<T>> tasks, long timeoutMs) throws Exception {
        // TODO: Submit to virtual thread executor, get with timeout, cancel on timeout
        return new java.util.ArrayList<>();
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        Practices practices = new Practices();
        int passed = 0;
        int total = 0;

        System.out.println("=== Virtual Threads Practices ===\n");

        // Test 1
        total++;
        try {
            long time = practices.executeVirtualThreads(100, 10);
            if (time > 0 && time < 5000) {
                System.out.println("Test 1 PASSED: executeVirtualThreads (" + time + "ms)");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: time=" + time);
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: " + e.getMessage());
        }

        // Test 2
        total++;
        try {
            java.util.List<Callable<String>> tasks = java.util.List.of(
                () -> "A", () -> "B", () -> "C"
            );
            java.util.List<String> results = practices.fanOutFanIn(tasks);
            if (results.size() == 3 && results.contains("A") && results.contains("B")) {
                System.out.println("Test 2 PASSED: fanOutFanIn");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: " + e.getMessage());
        }

        // Test 3
        total++;
        try {
            int maxConcurrent = practices.executeWithRateLimit(50, 5);
            if (maxConcurrent <= 5) {
                System.out.println("Test 3 PASSED: executeWithRateLimit (max=" + maxConcurrent + ")");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: max concurrent=" + maxConcurrent);
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            long time = practices.executeWithoutPinning(50, 20);
            if (time > 0 && time < 5000) {
                System.out.println("Test 4 PASSED: executeWithoutPinning (" + time + "ms)");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: time=" + time);
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: " + e.getMessage());
        }

        // Test 5
        total++;
        try {
            java.util.List<Callable<String>> tasks = java.util.List.of(
                () -> "fast",
                () -> { Thread.sleep(5000); return "slow"; }
            );
            java.util.List<String> results = practices.executeWithTimeout(tasks, 1000);
            if (results.size() == 2) {
                System.out.println("Test 5 PASSED: executeWithTimeout");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: " + results);
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
