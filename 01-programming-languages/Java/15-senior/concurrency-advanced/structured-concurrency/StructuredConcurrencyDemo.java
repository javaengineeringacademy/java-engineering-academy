import java.util.concurrent.*;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Demonstrates Structured Concurrency (Preview feature in JDK 21+).
 * Structured concurrency provides better error handling and lifecycle management
 * compared to CompletableFuture.
 *
 * Run with: --enable-preview
 */
public class StructuredConcurrencyDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Structured Concurrency Demo ===\n");
        System.out.println("Note: Requires JDK 21+ with --enable-preview flag\n");

        demonstrateBasicForkJoin();
        demonstrateShutdownOnFailure();
        demonstrateShutdownOnSuccess();
        demonstrateCancellation();
        demonstrateExceptionHandling();

        System.out.println("\n=== All demonstrations completed ===");
    }

    /**
     * Basic fork and join with structured concurrency.
     * Multiple tasks run concurrently, results are joined.
     */
    private static void demonstrateBasicForkJoin() throws Exception {
        System.out.println("1. Basic Fork and Join:");
        System.out.println("   Running multiple tasks concurrently.");

        Instant start = Instant.now();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            // Fork multiple tasks
            Subtask<String> userTask = scope.fork(() -> {
                Thread.sleep(100);  // Simulate work
                return "User: Alice";
            });

            Subtask<String> orderTask = scope.fork(() -> {
                Thread.sleep(150);  // Simulate work
                return "Order: #12345";
            });

            Subtask<String> inventoryTask = scope.fork(() -> {
                Thread.sleep(100);  // Simulate work
                return "Inventory: 10 units";
            });

            // Join all tasks
            scope.join();

            // Get results
            System.out.println("   Result 1: " + userTask.get());
            System.out.println("   Result 2: " + orderTask.get());
            System.out.println("   Result 3: " + inventoryTask.get());
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("   Total time: " + elapsed.toMillis() + "ms (parallel)\n");
    }

    /**
     * Shutdown on failure: If any task fails, cancel all others.
     * Useful when all tasks must succeed.
     */
    private static void demonstrateShutdownOnFailure() throws Exception {
        System.out.println("2. ShutdownOnFailure:");
        System.out.println("   Cancels all tasks if any fails.");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            Subtask<String> task1 = scope.fork(() -> {
                Thread.sleep(100);
                return "Task 1 completed";
            });

            Subtask<String> task2 = scope.fork(() -> {
                Thread.sleep(200);
                throw new RuntimeException("Task 2 failed!");
            });

            Subtask<String> task3 = scope.fork(() -> {
                Thread.sleep(300);
                return "Task 3 completed";
            });

            scope.join();  // Waits for all to complete or fail

            // Check for failures
            try {
                scope.throwIfFailed();
                System.out.println("   All tasks succeeded (should not reach here)");
            } catch (ExecutionException e) {
                System.out.println("   Caught failure: " + e.getCause().getMessage());
                System.out.println("   Other tasks were cancelled");
            }
        }

        System.out.println();
    }

    /**
     * Shutdown on success: Return first successful result, cancel others.
     * Useful for racing multiple implementations.
     */
    private static void demonstrateShutdownOnSuccess() throws Exception {
        System.out.println("3. ShutdownOnSuccess:");
        System.out.println("   Returns first successful result, cancels others.");

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {

            Subtask<String> fastProvider = scope.fork(() -> {
                Thread.sleep(50);
                return "Fast provider: Result A";
            });

            Subtask<String> slowProvider = scope.fork(() -> {
                Thread.sleep(200);
                return "Slow provider: Result B";
            });

            Subtask<String> anotherSlow = scope.fork(() -> {
                Thread.sleep(300);
                return "Another slow: Result C";
            });

            scope.join();

            try {
                String result = scope.result();
                System.out.println("   Winner: " + result);
                System.out.println("   Other tasks were cancelled");
            } catch (ExecutionException e) {
                System.out.println("   All tasks failed: " + e.getCause().getMessage());
            }
        }

        System.out.println();
    }

    /**
     * Cancellation: Structured concurrency provides clean cancellation.
     */
    private static void demonstrateCancellation() throws Exception {
        System.out.println("4. Cancellation:");
        System.out.println("   Tasks are cancelled when scope closes.");

        Instant start = Instant.now();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            Subtask<String> longTask = scope.fork(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(100);
                        System.out.println("   Long task progress: " + (i + 1) + "/10");
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("   Long task detected cancellation!");
                            throw new InterruptedException("Cancelled");
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("   Long task was cancelled");
                    throw e;
                }
                return "Long task completed";
            });

            Subtask<String> shortTask = scope.fork(() -> {
                Thread.sleep(50);
                return "Short task completed";
            });

            // Wait briefly then cancel (simulating timeout)
            scope.joinUntil(Instant.now().plus(Duration.ofMillis(250)));

            // Force shutdown
            scope.shutdown();

            try {
                scope.throwIfFailed();
            } catch (ExecutionException e) {
                System.out.println("   Task was interrupted: " + e.getCause().getMessage());
            }
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("   Elapsed: " + elapsed.toMillis() + "ms\n");
    }

    /**
     * Exception handling with structured concurrency.
     */
    private static void demonstrateExceptionHandling() throws Exception {
        System.out.println("5. Exception Handling:");
        System.out.println("   Proper error propagation and handling.");

        // Example 1: Exception in one task
        System.out.println("   Example 1: Single task exception");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            Subtask<String> successTask = scope.fork(() -> {
                Thread.sleep(50);
                return "Success";
            });

            Subtask<String> failTask = scope.fork(() -> {
                Thread.sleep(50);
                throw new IllegalArgumentException("Invalid input");
            });

            scope.join();

            try {
                scope.throwIfFailed();
            } catch (ExecutionException e) {
                System.out.println("   Handled: " + e.getCause().getMessage());
            }
        }

        // Example 2: Multiple exceptions
        System.out.println("\n   Example 2: Multiple exceptions");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            Subtask<String> task1 = scope.fork(() -> {
                throw new RuntimeException("Error 1");
            });

            Subtask<String> task2 = scope.fork(() -> {
                throw new RuntimeException("Error 2");
            });

            scope.join();

            try {
                scope.throwIfFailed();
            } catch (ExecutionException e) {
                System.out.println("   Primary: " + e.getCause().getMessage());
                // Multiple exceptions are suppressed
                for (Throwable suppressed : e.getSuppressed()) {
                    System.out.println("   Suppressed: " + suppressed.getMessage());
                }
            }
        }

        System.out.println("\n");
    }
}
