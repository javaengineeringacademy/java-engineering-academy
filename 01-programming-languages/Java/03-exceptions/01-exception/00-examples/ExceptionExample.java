package academy.javaengineering.exceptions.exception.examples;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical examples of using Exception correctly.
 *
 * <p>Each example demonstrates a specific pattern or anti-pattern with
 * explanation of why it works (or does not).</p>
 */
public class ExceptionExample {

    // ============================================================
    // Custom checked exceptions
    // ============================================================

    /**
     * Business exception for order processing failures.
     * Extends Exception because callers can recover (retry, show message).
     */
    static class OrderProcessingException extends Exception {
        private final String orderId;

        public OrderProcessingException(String orderId, String message) {
            super(message);
            this.orderId = orderId;
        }

        public OrderProcessingException(String orderId, String message, Throwable cause) {
            super(message, cause);
            this.orderId = orderId;
        }

        public String getOrderId() {
            return orderId;
        }
    }

    /**
     * Validation exception for input validation failures.
     */
    static class ValidationException extends Exception {
        private final String field;

        public ValidationException(String field, String message) {
            super(message);
            this.field = field;
        }

        public String getField() {
            return field;
        }
    }

    // ============================================================
    // Example 1: Proper checked exception usage
    // ============================================================

    /**
     * Declares specific exceptions that callers must handle.
     * This is the correct way to use checked exceptions.
     */
    static String processOrder(String orderId, String email)
            throws OrderProcessingException, ValidationException {
        if (orderId == null || orderId.isBlank()) {
            throw new ValidationException("orderId", "must not be blank");
        }
        if (email == null || !email.contains("@")) {
            throw new ValidationException("email", "must be valid email");
        }

        try {
            // simulate remote call
            boolean success = sendConfirmation(email, orderId);
            if (!success) {
                throw new OrderProcessingException(orderId, "failed to send confirmation");
            }
        } catch (IOException e) {
            throw new OrderProcessingException(orderId, "email service unavailable", e);
        }

        return "Order " + orderId + " processed";
    }

    static boolean sendConfirmation(String email, String orderId) throws IOException {
        // simulate I/O
        throw new IOException("SMTP server unreachable");
    }

    // ============================================================
    // Example 2: Exception translation at boundaries
    // ============================================================

    static class DataAccessException extends Exception {
        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * DAO layer translates low-level exceptions into domain exceptions.
     * Callers see DataAccessException, not SQLException.
     */
    static class OrderDao {
        List<String> findById(String id) throws DataAccessException {
            try {
                // simulate database call
                throw new SQLException("Connection timeout");
            } catch (SQLException e) {
                throw new DataAccessException("Failed to find order " + id, e);
            }
        }
    }

    static void exampleTranslation() {
        OrderDao dao = new OrderDao();
        try {
            List<String> order = dao.findById("ORD-001");
        } catch (DataAccessException e) {
            System.out.println("Translation example: " + e.getMessage());
            System.out.println("Root cause: " + e.getCause().getMessage());
        }
    }

    // ============================================================
    // Example 3: Exception in loops (catch outside)
    // ============================================================

    /**
     * Bad: catching inside the loop hides the problem.
     */
    static void badLoopHandling(List<String> items) {
        List<String> results = new ArrayList<>();
        for (String item : items) {
            try {
                results.add(processItem(item));
            } catch (Exception e) {
                System.out.println("Skipping " + item + ": " + e.getMessage());
                // silently skips bad items - usually wrong
            }
        }
    }

    /**
     * Good: let the exception propagate, or handle it explicitly.
     */
    static List<String> goodLoopHandling(List<String> items) throws Exception {
        List<String> results = new ArrayList<>();
        for (String item : items) {
            results.add(processItem(item));
        }
        return results;
    }

    static String processItem(String item) throws Exception {
        if (item == null) throw new IllegalArgumentException("item must not be null");
        return item.toUpperCase();
    }

    // ============================================================
    // Example 4: Multi-catch (Java 7+)
    // ============================================================

    static String multiCatchExample(String input) {
        try {
            return validateAndTransform(input);
        } catch (NumberFormatException | ValidationException e) {
            // handle either type the same way
            return "invalid input: " + e.getMessage();
        }
    }

    static String validateAndTransform(String input) throws NumberFormatException, ValidationException {
        if (input == null) throw new ValidationException("input", "must not be null");
        int value = Integer.parseInt(input);
        return String.valueOf(value * 2);
    }

    // ============================================================
    // Main
    // ============================================================

    public static void main(String[] args) {
        System.out.println("=== Example 1: Checked Exception Usage ===");
        try {
            processOrder("ORD-001", "user@example.com");
        } catch (ValidationException e) {
            System.out.println("Validation failed - field: " + e.getField() + ", " + e.getMessage());
        } catch (OrderProcessingException e) {
            System.out.println("Processing failed - order: " + e.getOrderId() + ", " + e.getMessage());
            System.out.println("Root cause: " + e.getCause());
        }

        System.out.println("\n=== Example 2: Exception Translation ===");
        exampleTranslation();

        System.out.println("\n=== Example 3: Loop Handling ===");
        badLoopHandling(List.of("a", null, "c"));
        try {
            goodLoopHandling(List.of("a", null, "c"));
        } catch (Exception e) {
            System.out.println("Good handling propagates: " + e.getMessage());
        }

        System.out.println("\n=== Example 4: Multi-Catch ===");
        System.out.println(multiCatchExample("abc"));
        System.out.println(multiCatchExample(null));
        System.out.println(multiCatchExample("42"));
    }
}
