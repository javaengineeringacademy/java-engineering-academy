package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates memory layout in ExecutorService.
 */
public class ExecutorServiceMemory {

    public static void main(String[] args) throws Exception {
        demonstrateTaskObjectLifecycle();
        demonstrateResultMemory();
        demonstrateThreadLocalInteraction();
    }

    static void demonstrateTaskObjectLifecycle() throws Exception {
        System.out.println("=== Task Object Lifecycle ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<String> future = executor.submit(() -> {
            // Task object allocated on heap, captures this string
            String result = "Task completed";
            return result;
        });

        // After get(), the FutureTask result is set and the task is eligible for GC
        System.out.println("Result: " + future.get());

        // Task object is no longer reachable
        future = null;
        System.gc();

        executor.shutdown();
    }

    static void demonstrateResultMemory() throws Exception {
        System.out.println("\n=== Result Memory ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<Long> future = executor.submit(() -> {
            long result = 0;
            for (int i = 0; i < 1000000; i++) result += i;
            return result; // stored in FutureTask.outcome field
        });

        System.out.println("Computed sum: " + future.get()); // volatile read of outcome

        executor.shutdown();
    }

    static void demonstrateThreadLocalInteraction() throws Exception {
        System.out.println("\n=== ThreadLocal in Executor ===");

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("Initial value");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Future<String> future = executor.submit(() -> {
            // ThreadLocal from submitting thread is NOT automatically visible
            // The executor thread has its own ThreadLocal
            String value = threadLocal.get();
            threadLocal.set("Worker thread value");
            return value;
        });

        System.out.println("Worker sees: " + future.get()); // null — different thread
        System.out.println("Main still sees: " + threadLocal.get()); // "Initial value"

        executor.shutdown();
        threadLocal.remove();
    }
}
