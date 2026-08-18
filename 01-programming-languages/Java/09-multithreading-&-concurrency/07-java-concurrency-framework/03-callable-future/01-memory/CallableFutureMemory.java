package concurrency;

import java.util.concurrent.*;

/**
 * Demonstrates memory layout in Callable and Future.
 */
public class CallableFutureMemory {

    public static void main(String[] args) throws Exception {
        demonstrateResultStorage();
        demonstrateExceptionStorage();
        demonstrateCallableCapture();
    }

    static void demonstrateResultStorage() throws Exception {
        System.out.println("=== Result Storage ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Callable<Long> task = () -> {
            long result = 0;
            for (int i = 0; i < 1000000; i++) result += i;
            return result; // stored in FutureTask.outcome field
        };

        Future<Long> future = executor.submit(task);

        // get() does a volatile read of outcome field
        // This establishes a happens-before edge with the task's write
        System.out.println("Result: " + future.get());

        executor.shutdown();
    }

    static void demonstrateExceptionStorage() throws Exception {
        System.out.println("\n=== Exception Storage ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        Callable<String> failingTask = () -> {
            throw new RuntimeException("Task failed");
        };

        Future<String> future = executor.submit(failingTask);

        try {
            future.get();
        } catch (ExecutionException e) {
            // Exception stored in FutureTask.exception field
            // Wrapped in ExecutionException on get()
            System.out.println("Original exception: " + e.getCause().getMessage());
        }

        executor.shutdown();
    }

    static void demonstrateCallableCapture() throws Exception {
        System.out.println("\n=== Callable Capture Memory ===");

        ExecutorService executor = Executors.newFixedThreadPool(1);

        String captured = "captured value"; // referenced by lambda
        Callable<String> task = () -> captured;

        Future<String> future = executor.submit(task);
        System.out.println("Captured: " + future.get());

        // captured is still referenced by the task until GC
        executor.shutdown();
    }
}
