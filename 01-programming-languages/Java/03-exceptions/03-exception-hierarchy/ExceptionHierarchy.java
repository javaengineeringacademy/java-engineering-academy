/**
 * Demonstrates the Java exception hierarchy including catching at different
 * levels, exception chaining, hierarchy traversal with instanceof, and
 * custom exception hierarchies.
 *
 * <p><b>Complexity:</b> O(1) per operation unless noted.</p>
 * <p><b>Thread-safety:</b> Not thread-safe — uses static mutable state.</p>
 * <p><b>Key characteristics:</b> Covers checked vs unchecked exceptions,
 * exception chaining, and proper catch ordering.</p>
 */
package academy.javaengineering.exceptions.hierarchy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main demonstration class for the Java exception hierarchy.
 * Covers hierarchy traversal, catching at different levels,
 * and exception chaining.
 */
public class ExceptionHierarchy {

    /**
     * Demonstrates the hierarchy by catching exceptions at different levels.
     */
    public static void demonstrateHierarchy() {
        System.out.println("=== Exception Hierarchy Demonstration ===\n");

        // 1. Catching at different levels
        System.out.println("--- Catching at Different Levels ---");
        demonstrateCatchingLevels();

        // 2. Exception chaining
        System.out.println("\n--- Exception Chaining ---");
        demonstrateExceptionChaining();

        // 3. Hierarchy traversal with instanceof
        System.out.println("\n--- Hierarchy Traversal with instanceof ---");
        demonstrateHierarchyTraversal();

        // 4. Custom exception hierarchy
        System.out.println("\n--- Custom Exception Hierarchy ---");
        demonstrateCustomHierarchy();

        // 5. Try-catch with multiple catch blocks
        System.out.println("\n--- Multiple Catch Blocks ---");
        demonstrateMultipleCatch();
    }

    /**
     * Demonstrates catching exceptions at different levels of the hierarchy.
     */
    private static void demonstrateCatchingLevels() {
        // Catching RuntimeException specifically
        System.out.println("1. Catching NullPointerException:");
        try {
            String s = null;
            s.length(); // throws NullPointerException
        } catch (NullPointerException e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName());
            System.out.println("   Message: " + e.getMessage());
        }

        // Catching at Exception level (broader)
        System.out.println("\n2. Catching Exception (broader):");
        try {
            String s = null;
            s.length(); // throws NullPointerException
        } catch (Exception e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName());
            System.out.println("   Message: " + e.getMessage());
        }

        // Catching at RuntimeException level
        System.out.println("\n3. Catching RuntimeException:");
        try {
            int result = 10 / 0; // throws ArithmeticException
        } catch (RuntimeException e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName());
            System.out.println("   Message: " + e.getMessage());
        }
    }

    /**
     * Demonstrates exception chaining (preserving the cause).
     */
    private static void demonstrateExceptionChaining() {
        try {
            try {
                throw new IOException("Original IO error");
            } catch (IOException e) {
                throw new CustomCheckedException("Wrapped exception", e);
            }
        } catch (CustomCheckedException e) {
            System.out.println("   Caught: " + e.getClass().getSimpleName());
            System.out.println("   Message: " + e.getMessage());
            System.out.println("   Cause: " + e.getCause().getClass().getSimpleName());
            System.out.println("   Cause Message: " + e.getCause().getMessage());
        }
    }

    /**
     * Demonstrates hierarchy traversal using instanceof checks.
     */
    private static void demonstrateHierarchyTraversal() {
        Throwable[] throwables = {
            new Error("Error"),
            new OutOfMemoryError("OOM"),
            new Exception("Checked"),
            new IOException("IO"),
            new RuntimeException("Runtime"),
            new NullPointerException("NPE"),
            new IllegalArgumentException("IllegalArgument"),
            new ArithmeticException("Arithmetic")
        };

        for (Throwable t : throwables) {
            System.out.println("   " + t.getClass().getSimpleName() + " ->");
            System.out.println("     Is Throwable: " + (t instanceof Throwable));
            System.out.println("     Is Error: " + (t instanceof Error));
            System.out.println("     Is Exception: " + (t instanceof Exception));
            System.out.println("     Is RuntimeException: " + (t instanceof RuntimeException));
            System.out.println();
        }
    }

    /**
     * Demonstrates a custom exception hierarchy.
     */
    private static void demonstrateCustomHierarchy() {
        try {
            throw new OrderException("Order not found");
        } catch (OrderException e) {
            System.out.println("   Caught OrderException: " + e.getMessage());
        }

        try {
            throw new PaymentException("Payment failed");
        } catch (PaymentException e) {
            System.out.println("   Caught PaymentException: " + e.getMessage());
        }

        try {
            throw new InventoryException("Item out of stock");
        } catch (InventoryException e) {
            System.out.println("   Caught InventoryException: " + e.getMessage());
        }
    }

    /**
     * Demonstrates multiple catch blocks in correct order (specific first).
     */
    private static void demonstrateMultipleCatch() {
        // Correct order: specific first, general last
        System.out.println("1. Correct order (specific first):");
        try {
            String s = null;
            s.length();
        } catch (NullPointerException e) {
            System.out.println("   Caught NullPointerException");
        } catch (RuntimeException e) {
            System.out.println("   Caught RuntimeException");
        } catch (Exception e) {
            System.out.println("   Caught Exception");
        }

        // Show what happens with wrong order (compiler error)
        System.out.println("\n2. Wrong order (general first):");
        System.out.println("   This would cause a compiler error:");
        System.out.println("   try { ... } catch (Exception e) { } catch (NullPointerException e) { }");
        System.out.println("   The NullPointerException handler is unreachable.");
    }

    // ========================================
    // Custom Exception Hierarchy
    // ========================================

    /**
     * Base checked exception for the application.
     */
    public static class ApplicationCheckedException extends Exception {
        public ApplicationCheckedException(String message) {
            super(message);
        }
        public ApplicationCheckedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Base unchecked exception for the application.
     */
    public static class ApplicationRuntimeException extends RuntimeException {
        public ApplicationRuntimeException(String message) {
            super(message);
        }
        public ApplicationRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Order-related checked exception.
     */
    public static class OrderException extends ApplicationCheckedException {
        public OrderException(String message) {
            super(message);
        }
    }

    /**
     * Payment-related checked exception.
     */
    public static class PaymentException extends ApplicationCheckedException {
        public PaymentException(String message) {
            super(message);
        }
    }

    /**
     * Inventory-related checked exception.
     */
    public static class InventoryException extends ApplicationCheckedException {
        public InventoryException(String message) {
            super(message);
        }
    }

    /**
     * Custom checked exception class.
     */
    public static class CustomCheckedException extends Exception {
        public CustomCheckedException(String message) {
            super(message);
        }
        public CustomCheckedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ========================================
    // Main Method
    // ========================================

    public static void main(String[] args) {
        demonstrateHierarchy();
    }
}
