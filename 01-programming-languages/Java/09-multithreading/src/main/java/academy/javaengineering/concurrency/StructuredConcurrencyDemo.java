package academy.javaengineering.concurrency;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Java 24 Structured Concurrency Demo (JEP 480).
 *
 * <p>Structured Concurrency simplifies concurrent programming by treating
 * groups of concurrent tasks as a unit. It provides better error handling,
 * clearer lifecycle management, and integrates with Scoped Values.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>StructuredTaskScope - managing concurrent tasks</li>
 *   <li>ShutdownOnFailure - cancel all on any failure</li>
 *   <li>ShutdownOnSuccess - cancel remaining on first success</li>
 *   <li>Forked tasks - structured subtasks</li>
 * </ul>
 *
 * <h3>Expected Output:</h3>
 * <pre>
 * === Structured Concurrency Demo ===
 *
 * --- ShutdownOnFailure ---
 * Task 1: result1
 * Task 2: result2
 * All tasks completed successfully
 *
 * --- ShutdownOnSuccess ---
 * Fastest result: result2
 *
 * --- Error Handling ---
 * Task failed: simulated error
 * All tasks cancelled
 *
 * --- Real-World: Aggregating Service Calls ---
 * User: alice
 * Orders: 5
 * Recommendations: [item1, item2, item3]
 * </pre>
 *
 * <h3>Production Use Cases:</h3>
 * <ul>
 *   <li>Microservice API aggregation</li>
 *   <li>Parallel data fetching</li>
 *   <li>Fan-out/fan-in patterns</li>
 *   <li>Timeout-based task management</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since Java 24
 */
public class StructuredConcurrencyDemo {

    private static final ScopedValue<String> CURRENT_USER =
        ScopedValue.newInstance();

    /**
     * Demonstrates ShutdownOnFailure strategy.
     */
    public static void shutdownOnFailureDemo() {
        System.out.println("--- ShutdownOnFailure ---");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            Subtask<String> task1 = scope.fork(() -> {
                Thread.sleep(100);
                return "result1";
            });

            Subtask<String> task2 = scope.fork(() -> {
                Thread.sleep(150);
                return "result2";
            });

            // Wait for all tasks to complete
            scope.join();

            // Check for errors
            scope.throwIfFailed();

            // Get results
            System.out.println("Task 1: " + task1.get());
            System.out.println("Task 2: " + task2.get());
            System.out.println("All tasks completed successfully");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates ShutdownOnSuccess strategy.
     */
    public static void shutdownOnSuccessDemo() {
        System.out.println("\n--- ShutdownOnSuccess ---");

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {

            scope.fork(() -> {
                Thread.sleep(200); // Slow
                return "result1";
            });

            scope.fork(() -> {
                Thread.sleep(50); // Fast
                return "result2";
            });

            scope.fork(() -> {
                Thread.sleep(150); // Medium
                return "result3";
            });

            scope.join();

            System.out.println("Fastest result: " + scope.result());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates error handling patterns.
     */
    public static void errorHandlingDemo() {
        System.out.println("\n--- Error Handling ---");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            scope.fork(() -> {
                return "success";
            });

            scope.fork(() -> {
                Thread.sleep(10);
                throw new RuntimeException("simulated error");
            });

            scope.join();
            scope.throwIfFailed();

        } catch (ExecutionException e) {
            System.out.println("Task failed: " +
                e.getCause().getMessage());
            System.out.println("All tasks cancelled");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Real-world example: API aggregation pattern.
     */
    public static void apiAggregationDemo() {
        System.out.println("\n--- Real-World: API Aggregation ---");

        ScopedValue.where(CURRENT_USER, "alice").run(() -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

                // Parallel service calls
                Subtask<String> userTask = scope.fork(() ->
                    fetchUser());

                Subtask<List<String>> ordersTask = scope.fork(() ->
                    fetchOrders());

                Subtask<List<String>> recommendationsTask = scope.fork(() ->
                    fetchRecommendations());

                scope.join();
                scope.throwIfFailed();

                // Aggregate results
                System.out.println("User: " + userTask.get());
                System.out.println("Orders: " + ordersTask.get().size());
                System.out.println("Recommendations: " +
                    recommendationsTask.get());

            } catch (Exception e) {
                System.err.println("Aggregation failed: " + e.getMessage());
            }
        });
    }

    /**
     * Real-world example: Parallel data processing.
     */
    public static void parallelProcessingDemo() {
        System.out.println("\n--- Parallel Data Processing ---");

        List<Integer> data = IntStream.rangeClosed(1, 100).boxed().toList();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            int chunkSize = 25;
            List<Subtask<Integer>> tasks = new ArrayList<>();

            // Split data into chunks
            for (int i = 0; i < data.size(); i += chunkSize) {
                List<Integer> chunk = data.subList(i,
                    Math.min(i + chunkSize, data.size()));

                tasks.add(scope.fork(() -> processChunk(chunk)));
            }

            scope.join();
            scope.throwIfFailed();

            // Collect results
            int total = tasks.stream()
                .mapToInt(Subtask::get)
                .sum();

            System.out.println("Processed " + data.size() + " items");
            System.out.println("Total: " + total);

        } catch (Exception e) {
            System.err.println("Processing failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrates timeout handling.
     */
    public static void timeoutDemo() {
        System.out.println("\n--- Timeout Handling ---");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

            scope.fork(() -> {
                Thread.sleep(100);
                return "fast";
            });

            scope.fork(() -> {
                Thread.sleep(5000); // Too slow
                return "slow";
            });

            // Set timeout for the scope
            scope.joinUntil(Instant.now().plusMillis(200));

            System.out.println("Completed within timeout");

        } catch (TimeoutException e) {
            System.out.println("Timeout exceeded, tasks cancelled");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String fetchUser() {
        return CURRENT_USER.get();
    }

    private static List<String> fetchOrders() {
        return List.of("order1", "order2", "order3", "order4", "order5");
    }

    private static List<String> fetchRecommendations() {
        return List.of("item1", "item2", "item3");
    }

    private static int processChunk(List<Integer> chunk) {
        return chunk.stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        System.out.println("=== Structured Concurrency Demo ===\n");

        shutdownOnFailureDemo();
        shutdownOnSuccessDemo();
        errorHandlingDemo();
        apiAggregationDemo();
        parallelProcessingDemo();
        timeoutDemo();
    }
}
