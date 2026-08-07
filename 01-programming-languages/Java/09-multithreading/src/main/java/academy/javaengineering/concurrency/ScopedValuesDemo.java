package academy.javaengineering.concurrency;

import java.util.concurrent.*;

/**
 * Java 24 Scoped Values Demo (JEP 481).
 *
 * <p>Scoped Values provide a thread-local variable mechanism with limited
 * scope, replacing ThreadLocal for many use cases. They offer better
 * performance, clearer lifecycle, and work seamlessly with virtual threads.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>ScopedValue - immutable, scoped thread-local variables</li>
 *   <li>where() - defining scope for values</li>
 *   <li>get() - accessing scoped values</li>
 *   <li>Automatic cleanup when scope exits</li>
 * </ul>
 *
 * <h3>Expected Output:</h3>
 * <pre>
 * === Scoped Values Demo ===
 *
 * --- Basic Scoped Value ---
 * Thread: thread-1, User: alice
 *
 * --- Nested Scopes ---
 * Outer: outerValue
 * Inner: innerValue
 * Back to Outer: outerValue
 *
 * --- Virtual Thread Integration ---
 * Virtual thread user: bob
 * </pre>
 *
 * <h3>Production Use Cases:</h3>
 * <ul>
 *   <li>Request context propagation</li>
 *   <li>Security credentials passing</li>
 *   <li>Distributed tracing context</li>
 *   <li>Multi-tenant applications</li>
 * </ul>
 *
 * @author JavaEngineering Academy
 * @since Java 24
 */
public class ScopedValuesDemo {

    // Define scoped values
    private static final ScopedValue<String> CURRENT_USER =
        ScopedValue.newInstance();

    private static final ScopedValue<Integer> REQUEST_ID =
        ScopedValue.newInstance();

    private static final ScopedValue<String> TRACE_CONTEXT =
        ScopedValue.newInstance();

    /**
     * Demonstrates basic scoped value usage.
     */
    public static void basicScopedValueDemo() {
        System.out.println("--- Basic Scoped Value ---");

        // Run code with a scoped value
        ScopedValue.where(CURRENT_USER, "alice").run(() -> {
            String user = CURRENT_USER.get();
            System.out.println("Thread: " +
                Thread.currentThread().getName() + ", User: " + user);
        });
        // CURRENT_USER is not accessible outside the scope
    }

    /**
     * Demonstrates nested scopes.
     */
    public static void nestedScopesDemo() {
        System.out.println("\n--- Nested Scopes ---");

        ScopedValue<String> OUTER = ScopedValue.newInstance();
        ScopedValue<String> INNER = ScopedValue.newInstance();

        ScopedValue.where(OUTER, "outerValue").run(() -> {
            System.out.println("Outer: " + OUTER.get());

            // Inner scope shadows outer
            ScopedValue.where(INNER, "innerValue").run(() -> {
                System.out.println("Inner: " + INNER.get());
                System.out.println("Still has outer: " + OUTER.get());
            });

            // Back to outer scope
            System.out.println("Back to Outer: " + OUTER.get());
        });
    }

    /**
     * Demonstrates scoped values with virtual threads.
     */
    public static void virtualThreadDemo() {
        System.out.println("\n--- Virtual Thread Integration ---");

        ScopedValue.where(CURRENT_USER, "bob").run(() -> {
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                executor.submit(() -> {
                    // Scoped value is available in virtual threads
                    System.out.println("Virtual thread user: " +
                        CURRENT_USER.get());
                }).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Demonstrates scoped values with structured concurrency.
     */
    public static void structuredConcurrencyDemo() {
        System.out.println("\n--- Structured Concurrency ---");

        ScopedValue.where(REQUEST_ID, 1001).run(() -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                // Spawn subtasks that inherit scoped values
                scope.fork(() -> {
                    System.out.println("Subtask 1 - Request: " +
                        REQUEST_ID.get());
                    return "Result 1";
                });

                scope.fork(() -> {
                    System.out.println("Subtask 2 - Request: " +
                        REQUEST_ID.get());
                    return "Result 2";
                });

                scope.join();
                System.out.println("Request " + REQUEST_ID.get() +
                    " completed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Demonstrates request context pattern.
     */
    public static void requestContextDemo() {
        System.out.println("\n--- Request Context Pattern ---");

        // Simulate HTTP request handling
        simulateRequest("req-001", "alice");
        simulateRequest("req-002", "bob");
    }

    private static void simulateRequest(String requestId, String user) {
        ScopedValue.where(REQUEST_ID, requestId)
            .where(CURRENT_USER, user)
            .run(() -> {
                processRequest();
            });
    }

    private static void processRequest() {
        System.out.printf("Processing %s for user %s%n",
            REQUEST_ID.get(), CURRENT_USER.get());

        // Call downstream services - scoped values propagate
        callDatabase();
        callExternalService();
    }

    private static void callDatabase() {
        System.out.println("  DB query by " + CURRENT_USER.get());
    }

    private static void callExternalService() {
        System.out.println("  External API call for " + REQUEST_ID.get());
    }

    /**
     * Demonstrates tracing context propagation.
     */
    public static void tracingDemo() {
        System.out.println("\n--- Tracing Context ---");

        ScopedValue.where(TRACE_CONTEXT, "trace-abc-123").run(() -> {
            System.out.println("Starting span: " + TRACE_CONTEXT.get());

            // Nested operation
            System.out.println("  Child span: " + TRACE_CONTEXT.get());

            System.out.println("Ending span: " + TRACE_CONTEXT.get());
        });
    }

    /**
     * Main method to run all demonstrations.
     */
    public static void main(String[] args) {
        System.out.println("=== Scoped Values Demo ===\n");

        basicScopedValueDemo();
        nestedScopesDemo();
        virtualThreadDemo();
        structuredConcurrencyDemo();
        requestContextDemo();
        tracingDemo();
    }
}
