package academy.javaengineering.exceptions.error;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates Java Error types, when they occur, and why they should not be caught.
 *
 * <p>Run each method separately to observe different Error conditions.
 * Most of these will crash the JVM or the thread — this is intentional.
 */
public class ErrorExample {

    public static void main(String[] args) {
        System.out.println("=== Java Error Examples ===");
        System.out.println();

        demonstrateErrorHierarchy();
        demonstrateOutOfMemoryError();
        demonstrateStackOverflowError();
        demonstrateNoClassDefFoundError();
        demonstrateAssertionError();
        demonstrateVirtualMachineError();
        demonstrateLinkageError();
        demonstrateErrorContract();
        demonstrateUncaughtExceptionHandler();
    }

    // ----------------------------------------------------------------
    // 1. Error Hierarchy
    // ----------------------------------------------------------------

    /**
     * Shows that Error is a direct subclass of Throwable, not Exception.
     */
    static void demonstrateErrorHierarchy() {
        System.out.println("1. Error Hierarchy");
        System.out.println("-".repeat(40));

        Error error = new Error("test error");
        System.out.println("Error extends Throwable: " + (error instanceof Throwable));
        System.out.println("Error extends Exception: " + (error instanceof Exception));
        System.out.println("Error extends RuntimeException: " + (error instanceof RuntimeException));

        System.out.println("Error superclass: " + Error.class.getSuperclass().getName());
        System.out.println("Error superclass of Exception: " + Exception.class.getSuperclass().getName());
        System.out.println("Error and Exception share the same parent: Throwable");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 2. OutOfMemoryError
    // ----------------------------------------------------------------

    /**
     * Triggers OutOfMemoryError by filling the heap.
     * WARNING: This will crash the JVM. Uncomment to run.
     */
    static void demonstrateOutOfMemoryError() {
        System.out.println("2. OutOfMemoryError");
        System.out.println("-".repeat(40));

        System.out.println("Triggering OutOfMemoryError by allocating until heap is exhausted...");
        System.out.println("This will crash the current JVM instance.");
        System.out.println("Uncomment the call in main() to observe.");
        System.out.println();

        // Uncomment to actually trigger OOM:
        // fillHeap();

        // For demonstration, show what would happen
        System.out.println("Expected: java.lang.OutOfMemoryError: Java heap space");
        System.out.println("Cause: JVM cannot allocate more objects on the heap");
        System.out.println("Recovery: None — the JVM is in an unrecoverable state");
        System.out.println();
    }

    /**
     * Fills the heap until OutOfMemoryError is thrown.
     */
    static void fillHeap() {
        List<Object> memoryHog = new ArrayList<>();
        int count = 0;
        while (true) {
            // Each byte array consumes 1MB of heap
            memoryHog.add(new byte[1024 * 1024]);
            count++;
            if (count % 100 == 0) {
                System.out.println("Allocated " + count + " MB...");
            }
        }
    }

    // ----------------------------------------------------------------
    // 3. StackOverflowError
    // ----------------------------------------------------------------

    /**
     * Triggers StackOverflowError through infinite recursion.
     * WARNING: This will crash the thread. Uncomment to run.
     */
    static void demonstrateStackOverflowError() {
        System.out.println("3. StackOverflowError");
        System.out.println("-".repeat(40));

        System.out.println("Triggering StackOverflowError through infinite recursion...");
        System.out.println("This will crash the current thread.");
        System.out.println("Uncomment the call in main() to observe.");
        System.out.println();

        // Uncomment to actually trigger StackOverflow:
        // infiniteRecursion(0);

        System.out.println("Expected: java.lang.StackOverflowError");
        System.out.println("Cause: Call stack exceeded its maximum depth");
        System.out.println("Recovery: None — the recursion must be fixed");
        System.out.println();
    }

    /**
     * Infinite recursion with no base case.
     */
    static void infiniteRecursion(int depth) {
        System.out.println("Recursion depth: " + depth);
        infiniteRecursion(depth + 1); // No base case
    }

    // ----------------------------------------------------------------
    // 4. NoClassDefFoundError
    // ----------------------------------------------------------------

    /**
     * Demonstrates NoClassDefFoundError when a class is missing at runtime.
     */
    static void demonstrateNoClassDefFoundError() {
        System.out.println("4. NoClassDefFoundError");
        System.out.println("-".repeat(40));

        System.out.println("NoClassDefFoundError occurs when:");
        System.out.println("- A class was available at compile time but not at runtime");
        System.out.println("- A dependency is missing from the classpath");
        System.out.println("- A static initializer failed (ExceptionInInitializerError wraps it)");
        System.out.println();

        System.out.println("Example scenario:");
        System.out.println("  Compile with: library.jar on classpath");
        System.out.println("  Run without:  library.jar missing");
        System.out.println("  Result:       NoClassDefFoundError");
        System.out.println();

        System.out.println("This is different from ClassNotFoundException, which is checked");
        System.out.println("and thrown by explicit class loading (Class.forName()).");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 5. AssertionError
    // ----------------------------------------------------------------

    /**
     * Triggers AssertionError when an assert statement fails.
     * Must be run with -ea flag to enable assertions.
     */
    static void demonstrateAssertionError() {
        System.out.println("5. AssertionError");
        System.out.println("-".repeat(40));

        int value = -1;

        System.out.println("Value: " + value);
        System.out.println("Assertion: assert value >= 0 : \"value must be non-negative\"");
        System.out.println();

        try {
            // This will throw AssertionError if assertions are enabled (-ea)
            assert value >= 0 : "value must be non-negative";
            System.out.println("Assertion passed (assertions not enabled)");
        } catch (AssertionError e) {
            System.out.println("AssertionError caught: " + e.getMessage());
        }

        System.out.println();
        System.out.println("To enable assertions: java -ea ErrorExample");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 6. VirtualMachineError
    // ----------------------------------------------------------------

    /**
     * Shows the VirtualMachineError hierarchy.
     */
    static void demonstrateVirtualMachineError() {
        System.out.println("6. VirtualMachineError Hierarchy");
        System.out.println("-".repeat(40));

        System.out.println("VirtualMachineError is abstract. Subclasses include:");
        System.out.println("  OutOfMemoryError    - Heap/memory exhaustion");
        System.out.println("  StackOverflowError  - Call stack overflow");
        System.out.println("  InternalError       - JVM internal error");
        System.out.println("  UnknownError        - Unknown JVM error");
        System.out.println("  ClassCircularityError  - Circular class dependency");
        System.out.println("  ClassFormatError       - Malformed class file");
        System.out.println("  GenericError           - Generic JVM error");
        System.out.println();

        System.out.println("All VirtualMachineErrors indicate the JVM has broken its contract.");
        System.out.println("None should be caught by application code.");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 7. LinkageError
    // ----------------------------------------------------------------

    /**
     * Shows the LinkageError hierarchy.
     */
    static void demonstrateLinkageError() {
        System.out.println("7. LinkageError Hierarchy");
        System.out.println("-".repeat(40));

        System.out.println("LinkageError indicates class dependency issues. Subclasses include:");
        System.out.println("  NoClassDefFoundError     - Class definition not found at runtime");
        System.out.println("  ClassNotFoundException   - Checked, thrown by class loaders");
        System.out.println("  ClassFormatError          - Malformed class file");
        System.out.println("  ClassCircularityError     - Circular class loading");
        System.out.println("  UnsatisfiedLinkError      - Native method not found");
        System.out.println();

        System.out.println("LinkageErrors occur during class loading, linking, or initialization.");
        System.out.println("They indicate a problem with the deployment, not the code logic.");
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 8. Error Contract
    // ----------------------------------------------------------------

    /**
     * Demonstrates that Error follows the same contract as Throwable.
     */
    static void demonstrateErrorContract() {
        System.out.println("8. Error Contract (same as Throwable)");
        System.out.println("-".repeat(40));

        Error error = new Error("Something went wrong",
                new RuntimeException("root cause"));

        System.out.println("getMessage():    " + error.getMessage());
        System.out.println("getCause():      " + error.getCause());
        System.out.println("toString():      " + error.toString());
        System.out.println("getClass():      " + error.getClass().getName());
        System.out.println("getStackTrace(): " + error.getStackTrace().length + " elements");
        System.out.println();

        // Show stack trace
        System.out.println("Stack trace:");
        error.printStackTrace(System.out);
        System.out.println();
    }

    // ----------------------------------------------------------------
    // 9. Uncaught Exception Handler
    // ----------------------------------------------------------------

    /**
     * Demonstrates how to set up a global uncaught exception handler for Errors.
     */
    static void demonstrateUncaughtExceptionHandler() {
        System.out.println("9. Uncaught Exception Handler for Errors");
        System.out.println("-".repeat(40));

        // Set up a handler for uncaught Errors
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof Error) {
                System.err.println("FATAL: Uncaught Error in thread " + thread.getName());
                System.err.println("Type: " + throwable.getClass().getName());
                System.err.println("Message: " + throwable.getMessage());
                System.err.println("This should trigger a graceful shutdown.");
            }
        });

        System.out.println("Uncaught handler set. It will catch Errors on any thread.");
        System.out.println("In production, this handler would:");
        System.out.println("  1. Log the error with full context");
        System.out.println("  2. Capture a heap/thread dump");
        System.out.println("  3. Trigger graceful shutdown");
        System.out.println("  4. Alert the operations team");
        System.out.println();
    }
}