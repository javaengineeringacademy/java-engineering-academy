package academy.javaengineering.concurrency.virtualthreads.solutions;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Solutions for virtual threads practice exercises.
 */
public class Solutions {

    // Solution 1: Execute virtual threads and return time
    public long executeVirtualThreads(int count, long sleepMs) throws Exception {
        Instant start = Instant.now();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executor.submit(() -> {
                    try { Thread.sleep(sleepMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return Duration.between(start, Instant.now()).toMillis();
    }

    // Solution 2: Fan-out/fan-in
    public <T> List<T> fanOutFanIn(List<Callable<T>> tasks) throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<T>> futures = new ArrayList<>();
            for (Callable<T> task : tasks) {
                futures.add(executor.submit(task));
            }

            List<T> results = new ArrayList<>();
            for (Future<T> f : futures) {
                results.add(f.get());
            }
            return results;
        }
    }

    // Solution 3: Rate limiting with Semaphore
    public int executeWithRateLimit(int taskCount, int maxConcurrency) throws Exception {
        Semaphore semaphore = new Semaphore(maxConcurrency);
        AtomicInteger maxConcurrent = new AtomicInteger(0);
        AtomicInteger currentConcurrent = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        semaphore.acquire();
                        int current = currentConcurrent.incrementAndGet();
                        maxConcurrent.updateAndGet(prev -> Math.max(prev, current));

                        Thread.sleep(10);

                        currentConcurrent.decrementAndGet();
                        semaphore.release();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }

        return maxConcurrent.get();
    }

    // Solution 4: Execute without pinning
    public long executeWithoutPinning(int taskCount, long sleepMs) throws Exception {
        ReentrantLock lock = new ReentrantLock();
        Instant start = Instant.now();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    lock.lock();
                    try {
                        Thread.sleep(sleepMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                });
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        return Duration.between(start, Instant.now()).toMillis();
    }

    // Solution 5: Execute with timeout
    public <T> List<T> executeWithTimeout(List<Callable<T>> tasks, long timeoutMs) throws Exception {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<T>> futures = new ArrayList<>();
            for (Callable<T> task : tasks) {
                futures.add(executor.submit(task));
            }

            List<T> results = new ArrayList<>();
            for (Future<T> future : futures) {
                try {
                    results.add(future.get(timeoutMs, TimeUnit.MILLISECONDS));
                } catch (TimeoutException e) {
                    future.cancel(true);
                    results.add(null);
                } catch (ExecutionException e) {
                    results.add(null);
                }
            }
            return results;
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        Solutions solutions = new Solutions();
        int passed = 0;
        int total = 0;

        System.out.println("=== Virtual Threads Solutions Tests ===\n");

        // Test 1
        total++;
        try {
            long time = solutions.executeVirtualThreads(100, 10);
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
            List<Callable<String>> tasks = List.of(() -> "A", () -> "B", () -> "C");
            List<String> results = solutions.fanOutFanIn(tasks);
            if (results.size() == 3 && results.contains("A")) {
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
            int max = solutions.executeWithRateLimit(50, 5);
            if (max <= 5) {
                System.out.println("Test 3 PASSED: executeWithRateLimit (max=" + max + ")");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: max=" + max);
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: " + e.getMessage());
        }

        // Test 4
        total++;
        try {
            long time = solutions.executeWithoutPinning(50, 20);
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
            List<Callable<String>> tasks = List.of(
                () -> "fast",
                () -> { Thread.sleep(5000); return "slow"; }
            );
            List<String> results = solutions.executeWithTimeout(tasks, 1000);
            if (results.size() == 2 && "fast".equals(results.get(0))) {
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
