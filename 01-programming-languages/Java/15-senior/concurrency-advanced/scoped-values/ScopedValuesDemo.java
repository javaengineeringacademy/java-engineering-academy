import java.util.concurrent.*;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * Demonstrates Scoped Values (Preview feature in JDK 21+).
 * Scoped values provide a way to pass data down the call stack without
 * explicit parameters, similar to ThreadLocal but with better lifecycle management.
 *
 * Run with: --enable-preview
 */
public class ScopedValuesDemo {

    // Define scoped values
    private static final ScopedValue<String> CURRENT_USER = ScopedValue.newInstance();
    private static final ScopedValue<Integer> REQUEST_ID = ScopedValue.newInstance();
    private static final ScopedValue<Boolean> DEBUG_MODE = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        System.out.println("=== Scoped Values Demo ===\n");
        System.out.println("Note: Requires JDK 21+ with --enable-preview flag\n");

        demonstrateBasicUsage();
        demonstrateNestedScoping();
        demonstrateWithThreads();
        demonstrateRebinding();
        demonstratevsThreadLocal();

        System.out.println("\n=== All demonstrations completed ===");
    }

    /**
     * Basic usage of scoped values.
     * Use ScopedValue.where().run() to set a value within a scope.
     */
    private static void demonstrateBasicUsage() {
        System.out.println("1. Basic Scoped Value Usage:");
        System.out.println("   Setting and reading scoped values.");

        // Set value for the scope
        ScopedValue.where(CURRENT_USER, "Alice").run(() -> {
            // Value is available in this scope
            System.out.println("   Current user: " + CURRENT_USER.get());

            // Call nested method - value propagates automatically
            processRequest();
        });

        System.out.println("   After scope: " + CURRENT_USER.get() + "\n");
    }

    /**
     * Nested method can access scoped value without explicit parameter.
     */
    private static void processRequest() {
        String user = CURRENT_USER.get();
        System.out.println("   Processing request for: " + user);
    }

    /**
     * Multiple scoped values can be nested.
     */
    private static void demonstrateNestedScoping() {
        System.out.println("2. Nested Scoping:");
        System.out.println("   Multiple scoped values in nested scopes.");

        ScopedValue.where(CURRENT_USER, "Bob").run(() -> {
            System.out.println("   Outer scope - User: " + CURRENT_USER.get());

            ScopedValue.where(REQUEST_ID, 12345).run(() -> {
                System.out.println("   Inner scope - User: " + CURRENT_USER.get());
                System.out.println("   Inner scope - Request ID: " + REQUEST_ID.get());

                ScopedValue.where(DEBUG_MODE, true).run(() -> {
                    System.out.println("   Deepest scope - User: " + CURRENT_USER.get());
                    System.out.println("   Deepest scope - Request ID: " + REQUEST_ID.get());
                    System.out.println("   Deepest scope - Debug: " + DEBUG_MODE.get());
                });

                // DEBUG_MODE is no longer available here
                System.out.println("   Back to inner scope - Debug: " + DEBUG_MODE.get());
            });

            // REQUEST_ID is no longer available here
            System.out.println("   Back to outer scope - Request ID: " + REQUEST_ID.get());
        });

        System.out.println();
    }

    /**
     * Scoped values work with threads and structured concurrency.
     */
    private static void demonstrateThreads() throws Exception {
        System.out.println("3. Scoped Values with Threads:");
        System.out.println("   Scoped values propagate to child threads.");

        ScopedValue.where(CURRENT_USER, "Charlie").run(() -> {
            System.out.println("   Main thread - User: " + CURRENT_USER.get());

            // Regular thread - scoped value NOT inherited
            Thread regularThread = new Thread(() -> {
                try {
                    System.out.println("   Regular thread - User: " + CURRENT_USER.get());
                } catch (Exception e) {
                    System.out.println("   Regular thread - User: not available");
                }
            });
            regularThread.start();
            regularThread.join();

            // Virtual thread with ScopedValue scope - works
            Thread virtualThread = Thread.ofVirtual().name("virtual-1").start(() -> {
                try {
                    System.out.println("   Virtual thread - User: " + CURRENT_USER.get());
                } catch (Exception e) {
                    System.out.println("   Virtual thread - User: not available");
                }
            });
            virtualThread.join();
        });

        System.out.println();
    }

    /**
     * Scoped values can be rebound within nested scopes.
     */
    private static void demonstrateRebinding() {
        System.out.println("4. Scoped Value Rebinding:");
        System.out.println("   Values can be overridden in nested scopes.");

        ScopedValue.where(CURRENT_USER, "David").run(() -> {
            System.out.println("   Outer: " + CURRENT_USER.get());

            // Rebind with new value
            ScopedValue.where(CURRENT_USER, "Eve").run(() -> {
                System.out.println("   Inner (rebound): " + CURRENT_USER.get());
            });

            // Back to original
            System.out.println("   After inner: " + CURRENT_USER.get());
        });

        System.out.println();
    }

    /**
     * Comparison of ScopedValue vs ThreadLocal.
     */
    private static void demonstratevsThreadLocal() {
        System.out.println("5. ScopedValue vs ThreadLocal:");
        System.out.println("   Comparison of approaches.\n");

        // ThreadLocal approach
        ThreadLocal<String> threadLocalUser = new ThreadLocal<>();

        System.out.println("   ThreadLocal:");
        threadLocalUser.set("Frank");
        System.out.println("   Set: " + threadLocalUser.get());
        threadLocalUser.remove();  // Must remember to clean up!
        System.out.println("   After remove: " + threadLocalUser.get());

        // ScopedValue approach
        System.out.println("\n   ScopedValue:");
        ScopedValue.where(CURRENT_USER, "Grace").run(() -> {
            System.out.println("   Set: " + CURRENT_USER.get());
        });
        System.out.println("   After scope: " + CURRENT_USER.get() + " (auto-cleaned)\n");

        // Key differences
        System.out.println("   Key Differences:");
        System.out.println("   - ThreadLocal: Manual set/remove, can leak");
        System.out.println("   - ScopedValue: Automatic cleanup, scoped to block");
        System.out.println("   - ThreadLocal: Any thread can access");
        System.out.println("   - ScopedValue: Only within defined scope");
        System.out.println("   - ThreadLocal: Mutable state");
        System.out.println("   - ScopedValue: Immutable binding (rebind, not mutate)\n");
    }
}
