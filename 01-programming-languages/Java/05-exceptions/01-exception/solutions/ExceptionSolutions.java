package academy.javaengineering.exceptions.exception.solutions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Solutions for the Exception exercises.
 *
 * <p>Each solution implements the corresponding exercise from
 * ExceptionExercises.java.</p>
 */
public class ExceptionSolutions {

    // ============================================================
    // Exercise 1: Custom checked exception
    // ============================================================

    /**
     * A checked exception for resource-not-found conditions.
     */
    static class NotFoundException extends Exception {
        private final String resourceId;

        public NotFoundException(String resourceId, String message) {
            super(message);
            this.resourceId = resourceId;
        }

        public NotFoundException(String resourceId, String message, Throwable cause) {
            super(message, cause);
            this.resourceId = resourceId;
        }

        public String getResourceId() {
            return resourceId;
        }
    }

    /**
     * Solution: throws NotFoundException when resourceId is null or empty.
     */
    static String findResource(String resourceId) throws NotFoundException {
        if (resourceId == null || resourceId.isBlank()) {
            throw new NotFoundException(resourceId, "Resource ID must not be null or blank");
        }
        return "data-for-" + resourceId;
    }

    // ============================================================
    // Exercise 2: Exception chaining
    // ============================================================

    /**
     * Custom exception for data access failures.
     */
    static class DataAccessException extends Exception {
        public DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Simulated database operation.
     */
    static String databaseQuery(String query) throws IOException {
        throw new IOException("Database connection refused");
    }

    /**
     * Solution: wraps IOException in DataAccessException with cause chaining.
     */
    static String executeQuery(String query) throws DataAccessException {
        try {
            return databaseQuery(query);
        } catch (IOException e) {
            throw new DataAccessException("Failed to execute query: " + query, e);
        }
    }

    // ============================================================
    // Exercise 3: Multi-catch
    // ============================================================

    static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * Solution: uses multi-catch to handle both exception types.
     */
    static int safeParseInt(String input) {
        try {
            if (input == null || input.isBlank()) {
                throw new ValidationException("Input must not be null or blank");
            }
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException | ValidationException e) {
            return -1;
        }
    }

    // ============================================================
    // Exercise 4: Exception in a loop
    // ============================================================

    /**
     * Solution: catches IllegalArgumentException for null items and skips them.
     */
    static List<String> processItems(List<String> items) {
        List<String> results = new ArrayList<>();
        for (String item : items) {
            try {
                if (item == null) {
                    throw new IllegalArgumentException("Item must not be null");
                }
                results.add(item.toUpperCase());
            } catch (IllegalArgumentException e) {
                // skip null items
            }
        }
        return results;
    }

    // ============================================================
    // Exercise 5: fillInStackTrace override
    // ============================================================

    /**
     * Lightweight exception that skips stack trace filling.
     */
    static class FastException extends Exception {
        public FastException(String message) {
            super(message);
        }

        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }

    /**
     * Solution: returns a FastException that does not fill the stack trace.
     */
    static Exception createFastException(String message) {
        return new FastException(message);
    }

    // ============================================================
    // Exercise 6: try-with-resources
    // ============================================================

    /**
     * Mock Closeable that throws during close.
     */
    static class MockResource implements AutoCloseable {
        private final String data;

        public MockResource(String data) {
            this.data = data;
        }

        String read() {
            return data;
        }

        @Override
        public void close() throws Exception {
            throw new IOException("Close failed");
        }
    }

    /**
     * Solution: uses try-with-resources; close exception becomes suppressed.
     */
    static String readWithResource() throws IOException {
        try (MockResource resource = new MockResource("hello")) {
            return resource.read();
        } catch (Exception e) {
            // The close exception is added as suppressed automatically
            throw new IOException("Read failed", e);
        }
    }

    // ============================================================
    // Exercise 7: Cause chain depth
    // ============================================================

    static class ExceptionA extends Exception {
        public ExceptionA(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ExceptionB extends Exception {
        public ExceptionB(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ExceptionC extends Exception {
        public ExceptionC(String message) {
            super(message);
        }
    }

    /**
     * Solution: creates a 3-level cause chain.
     */
    static Exception createCauseChain() {
        ExceptionC root = new ExceptionC("root cause");
        ExceptionB middle = new ExceptionB("middle layer", root);
        return new ExceptionA("outer wrapper", middle);
    }

    // ============================================================
    // Main - run all solutions
    // ============================================================

    public static void main(String[] args) throws Exception {
        System.out.println("=== Exercise 1: Custom Exception ===");
        try {
            findResource("R-001");
            System.out.println("Found: " + findResource("R-001"));
            findResource("");
        } catch (NotFoundException e) {
            System.out.println("Caught: " + e.getMessage() + " (resourceId=" + e.getResourceId() + ")");
        }

        System.out.println("\n=== Exercise 2: Exception Chaining ===");
        try {
            executeQuery("SELECT * FROM orders");
        } catch (DataAccessException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        }

        System.out.println("\n=== Exercise 3: Multi-Catch ===");
        System.out.println("Parse '42': " + safeParseInt("42"));
        System.out.println("Parse 'abc': " + safeParseInt("abc"));
        System.out.println("Parse null: " + safeParseInt(null));

        System.out.println("\n=== Exercise 4: Loop Handling ===");
        List<String> items = List.of("hello", null, "world", null, "java");
        System.out.println("Input: " + items);
        System.out.println("Result: " + processItems(items));

        System.out.println("\n=== Exercise 5: Fast Exception ===");
        Exception fast = createFastException("fast error");
        System.out.println("Exception: " + fast.getMessage());
        System.out.println("Stack trace length: " + fast.getStackTrace().length);

        System.out.println("\n=== Exercise 6: try-with-resources ===");
        try {
            String result = readWithResource();
            System.out.println("Read: " + result);
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("  Suppressed: " + suppressed.getMessage());
            }
        }

        System.out.println("\n=== Exercise 7: Cause Chain ===");
        Exception chain = createCauseChain();
        System.out.println("Level 0: " + chain.getMessage());
        System.out.println("Level 1: " + chain.getCause().getMessage());
        System.out.println("Level 2: " + chain.getCause().getCause().getMessage());
        System.out.println("Level 3 (null): " + chain.getCause().getCause().getCause());
    }
}
