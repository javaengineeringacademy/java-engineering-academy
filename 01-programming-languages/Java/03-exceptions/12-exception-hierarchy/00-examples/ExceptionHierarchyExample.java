package academy.javaengineering.exceptions.hierarchy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Example demonstrating Java exception hierarchy concepts.
 */
public class ExceptionHierarchyExample {

    /**
     * Demonstrates exception hierarchy concepts.
     */
    public static void main(String[] args) {
        System.out.println("=== Exception Hierarchy Examples ===\n");

        // 1. Catching at different levels
        demonstrateCatchingLevels();

        // 2. Custom exception hierarchy
        demonstrateCustomHierarchy();

        // 3. Exception chaining
        demonstrateExceptionChaining();

        // 4. Hierarchy traversal
        demonstrateHierarchyTraversal();
    }

    /**
     * Demonstrates catching exceptions at different hierarchy levels.
     */
    private static void demonstrateCatchingLevels() {
        System.out.println("--- Catching at Different Levels ---");

        // Catching RuntimeException specifically
        try {
            String s = null;
            s.length();
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException: " + e.getMessage());
        }

        // Catching at Exception level (broader)
        try {
            int result = 10 / 0;
        } catch (Exception e) {
            System.out.println("Caught Exception: " + e.getClass().getSimpleName());
        }

        // Catching at RuntimeException level
        try {
            throw new IllegalArgumentException("Bad argument");
        } catch (RuntimeException e) {
            System.out.println("Caught RuntimeException: " + e.getMessage());
        }
    }

    /**
     * Demonstrates custom exception hierarchy.
     */
    private static void demonstrateCustomHierarchy() {
        System.out.println("\n--- Custom Exception Hierarchy ---");

        try {
            throw new OrderException("Order not found");
        } catch (OrderException e) {
            System.out.println("Caught OrderException: " + e.getMessage());
        }

        try {
            throw new PaymentException("Payment failed");
        } catch (PaymentException e) {
            System.out.println("Caught PaymentException: " + e.getMessage());
        }

        try {
            throw new InventoryException("Item out of stock");
        } catch (InventoryException e) {
            System.out.println("Caught InventoryException: " + e.getMessage());
        }
    }

    /**
     * Demonstrates exception chaining (preserving the cause).
     */
    private static void demonstrateExceptionChaining() {
        System.out.println("\n--- Exception Chaining ---");

        try {
            try {
                throw new IOException("Original IO error");
            } catch (IOException e) {
                throw new ApplicationCheckedException("Wrapped exception", e);
            }
        } catch (ApplicationCheckedException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());
        }
    }

    /**
     * Demonstrates hierarchy traversal using instanceof checks.
     */
    private static void demonstrateHierarchyTraversal() {
        System.out.println("\n--- Hierarchy Traversal ---");

        Throwable[] throwables = {
            new Error("Error"),
            new OutOfMemoryError("OOM"),
            new Exception("Checked"),
            new IOException("IO"),
            new RuntimeException("Runtime"),
            new NullPointerException("NPE"),
            new IllegalArgumentException("IllegalArgument")
        };

        for (Throwable t : throwables) {
            System.out.println(t.getClass().getSimpleName() + " ->");
            System.out.println("  Is Throwable: " + (t instanceof Throwable));
            System.out.println("  Is Error: " + (t instanceof Error));
            System.out.println("  Is Exception: " + (t instanceof Exception));
            System.out.println("  Is RuntimeException: " + (t instanceof RuntimeException));
            System.out.println();
        }
    }

    // ========================================
    // Custom Exception Classes
    // ========================================

    public static class ApplicationCheckedException extends Exception {
        public ApplicationCheckedException(String message) {
            super(message);
        }
        public ApplicationCheckedException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class OrderException extends ApplicationCheckedException {
        public OrderException(String message) {
            super(message);
        }
    }

    public static class PaymentException extends ApplicationCheckedException {
        public PaymentException(String message) {
            super(message);
        }
    }

    public static class InventoryException extends ApplicationCheckedException {
        public InventoryException(String message) {
            super(message);
        }
    }
}
