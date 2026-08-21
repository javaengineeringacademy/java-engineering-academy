package academy.javaengineering.senior.solutions;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class AdvancedConcurrencySolutions {

    // Exercise 1: Parallel Data Pipeline
    static List<Long> parallelPipeline(List<Integer> input) throws Exception {
        List<CompletableFuture<Long>> futures = input.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> CompletableFuture.supplyAsync(() -> (long) n * n))
            .toList();

        List<Long> results = futures.stream()
            .map(CompletableFuture::join)
            .sorted()
            .toList();
        return results;
    }

    // Exercise 2: Async Retry with Fallback
    static <T> CompletableFuture<T> asyncRetry(
            java.util.function.Supplier<CompletableFuture<T>> action,
            int maxRetries,
            long timeoutMs,
            T fallback) {

        CompletableFuture<T> result = new CompletableFuture<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            long deadline = System.nanoTime() + timeoutMs * 1_000_000;
            long delay = 100;

            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                if (System.nanoTime() > deadline) {
                    result.complete(fallback);
                    return;
                }

                try {
                    T value = action.get().get(
                        Math.max(1, deadline - System.nanoTime()) / 1_000_000,
                        TimeUnit.MILLISECONDS
                    );
                    result.complete(value);
                    return;
                } catch (Exception e) {
                    if (attempt < maxRetries) {
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            result.complete(fallback);
                            return;
                        }
                        delay = Math.min(delay * 2, 5000);
                    }
                }
            }
            result.complete(fallback);
        });

        executor.shutdown();
        return result;
    }

    // Exercise 3: Virtual Thread Work Stealing
    static long executeWorkStealing(List<Runnable> tasks, int threadCount) throws Exception {
        long start = System.nanoTime();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = new ArrayList<>();
            // Distribute tasks across virtual threads
            for (Runnable task : tasks) {
                futures.add(executor.submit(task));
            }
            for (Future<?> f : futures) {
                f.get();
            }
        }

        return (System.nanoTime() - start) / 1_000_000;
    }

    // Exercise 4: Read-Write Lock with Starvation Prevention
    static class FairReadWriteLock {
        private final Object lock = new Object();
        private int readers = 0;
        private boolean writing = false;
        private int waitingWriters = 0;
        private static final int MAX_READERS_BEFORE_YIELD = 3;

        void readLock() {
            synchronized (lock) {
                while (writing || waitingWriters > 0) {
                    try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
                readers++;
            }
        }

        void readUnlock() {
            synchronized (lock) {
                readers--;
                if (readers == 0) lock.notifyAll();
            }
        }

        void writeLock() {
            synchronized (lock) {
                waitingWriters++;
                while (writing || readers > 0) {
                    try { lock.wait(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }
                waitingWriters--;
                writing = true;
            }
        }

        void writeUnlock() {
            synchronized (lock) {
                writing = false;
                lock.notifyAll();
            }
        }
    }

    // Exercise 5: CompletableFuture Composition with Partial Failure
    record MultiResult<T>(List<T> successes, List<String> errors) {}

    static <T> CompletableFuture<MultiResult<T>> parallelWithPartialFailure(
            List<java.util.function.Supplier<CompletableFuture<T>>> operations,
            long timeoutMs) throws Exception {

        List<CompletableFuture<T>> futures = operations.stream()
            .map(op -> op.get().exceptionally(ex -> null))
            .toList();

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
            .thenApply(v -> {
                List<T> successes = new ArrayList<>();
                List<String> errors = new ArrayList<>();
                for (int i = 0; i < futures.size(); i++) {
                    T val = futures.get(i).join();
                    if (val != null) {
                        successes.add(val);
                    } else {
                        errors.add("Operation " + i + " failed");
                    }
                }
                if (successes.isEmpty()) {
                    throw new CompletionException(new RuntimeException("All operations failed: " + errors));
                }
                return new MultiResult<>(successes, errors);
            });
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Advanced Concurrency Solutions ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: Parallel Pipeline ---");
        List<Long> result1 = parallelPipeline(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        System.out.println("  Result: " + result1);
        System.out.println("  Expected: [4, 16, 36, 64, 100]");

        // Exercise 2
        System.out.println("\n--- Exercise 2: Async Retry ---");
        AtomicInteger attempts = new AtomicInteger(0);
        CompletableFuture<String> result2 = asyncRetry(
            () -> CompletableFuture.supplyAsync(() -> {
                if (attempts.incrementAndGet() < 3) throw new RuntimeException("Fail");
                return "success";
            }),
            5, 5000, "fallback"
        );
        System.out.println("  Result: " + result2.get());

        // Exercise 3
        System.out.println("\n--- Exercise 3: Work Stealing ---");
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tasks.add(() -> {
                try { Thread.sleep(10); } catch (InterruptedException e) {}
            });
        }
        long time = executeWorkStealing(tasks, 4);
        System.out.println("  Completed in: " + time + "ms");

        // Exercise 4
        System.out.println("\n--- Exercise 4: Fair Read-Write Lock ---");
        FairReadWriteLock rwLock = new FairReadWriteLock();
        rwLock.writeLock();
        System.out.println("  Write lock acquired");
        rwLock.writeUnlock();
        System.out.println("  Write lock released");
        rwLock.readLock();
        System.out.println("  Read lock acquired");
        rwLock.readUnlock();
        System.out.println("  Read lock released");

        // Exercise 5
        System.out.println("\n--- Exercise 5: Partial Failure ---");
        List<java.util.function.Supplier<CompletableFuture<String>>> ops = List.of(
            () -> CompletableFuture.completedFuture("result1"),
            () -> CompletableFuture.failedFuture(new RuntimeException("err1")),
            () -> CompletableFuture.completedFuture("result3")
        );
        CompletableFuture<MultiResult<String>> result5 = parallelWithPartialFailure(ops, 5000);
        MultiResult<String> mr = result5.get();
        System.out.println("  Successes: " + mr.successes());
        System.out.println("  Errors: " + mr.errors());

        System.out.println("\n=== All Solutions Complete ===");
    }
}
