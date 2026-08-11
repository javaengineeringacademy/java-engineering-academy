package academy.javaengineering.exceptions.examples;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.concurrent.*;

/**
 * Module-level examples demonstrating exception handling patterns.
 *
 * <p>These examples combine concepts from all topic folders:
 * try-with-resources, multi-catch, exception chaining, custom exceptions,
 * and production patterns.
 *
 * <p>Complexity: O(n) per example
 * Thread-safety: Each example is self-contained and thread-safe
 */
public class ExceptionExamples {

    // --- Example 1: Retry with exponential backoff ---

    /**
     * Demonstrates retry logic with exponential backoff.
     * Common pattern for transient failures (network, database).
     */
    public static String retryWithBackoff(int maxRetries) {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                // Simulate a flaky operation
                return fetchDataFromService();
            } catch (IOException e) {
                attempt++;
                if (attempt >= maxRetries) {
                    throw new RuntimeException(
                        "Failed after " + maxRetries + " attempts", e);
                }
                try {
                    Thread.sleep((long) Math.pow(2, attempt) * 100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
        throw new RuntimeException("Unexpected state");
    }

    // --- Example 2: Exception translation ---

    /**
     * Demonstrates the exception translation pattern.
     * Catch low-level exception, wrap in domain-specific exception.
     */
    public static User findUserById(String id) {
        try {
            return databaseLookup(id);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to find user: " + id, e);
        }
    }

    // --- Example 3: Multi-resource cleanup ---

    /**
     * Demonstrates try-with-resources with multiple resources.
     * Resources are closed in reverse declaration order.
     */
    public static void copyFile(String source, String target)
            throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(source));
             BufferedWriter writer = Files.newBufferedWriter(Path.of(target))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // --- Example 4: Graceful degradation ---

    /**
     * Demonstrates graceful degradation with fallback.
     * Try primary, fall back to cache, fall back to default.
     */
    public static String getConfigWithFallback(String key) {
        try {
            return loadFromDatabase(key);
        } catch (SQLException e) {
            System.err.println("DB unavailable, trying cache: " + e.getMessage());
            try {
                return loadFromCache(key);
            } catch (CacheException e2) {
                System.err.println("Cache unavailable, using default: " + e2.getMessage());
                return getDefaultValue(key);
            }
        }
    }

    // --- Example 5: Structured error response ---

    /**
     * Demonstrates a structured error response pattern for APIs.
     */
    public static ErrorResponse handleException(Exception e) {
        if (e instanceof ValidationException) {
            return new ErrorResponse("VALIDATION_ERROR", e.getMessage(), 400);
        } else if (e instanceof NotFoundException) {
            return new ErrorResponse("NOT_FOUND", e.getMessage(), 404);
        } else if (e instanceof DataAccessException) {
            return new ErrorResponse("INTERNAL_ERROR", "Service unavailable", 503);
        }
        return new ErrorResponse("UNKNOWN", "An unexpected error occurred", 500);
    }

    // --- Helper methods (simulated) ---

    private static String fetchDataFromService() throws IOException {
        if (Math.random() < 0.7) {
            throw new IOException("Service temporarily unavailable");
        }
        return "data";
    }

    private static User databaseLookup(String id) throws SQLException {
        if (id == null) {
            throw new SQLException("Invalid ID");
        }
        return new User(id, "user@example.com");
    }

    private static String loadFromDatabase(String key) throws SQLException {
        throw new SQLException("Database connection refused");
    }

    private static String loadFromCache(String key) throws CacheException {
        throw new CacheException("Cache miss");
    }

    private static String getDefaultValue(String key) {
        return "default";
    }

    // --- Helper types ---

    record User(String id, String email) {}

    static class DataAccessException extends RuntimeException {
        DataAccessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class ValidationException extends RuntimeException {
        ValidationException(String message) {
            super(message);
        }
    }

    static class NotFoundException extends RuntimeException {
        NotFoundException(String message) {
            super(message);
        }
    }

    static class CacheException extends Exception {
        CacheException(String message) {
            super(message);
        }
    }

    record ErrorResponse(String code, String message, int status) {}

    // --- Main ---

    public static void main(String[] args) {
        System.out.println("=== Example 1: Retry with Backoff ===");
        try {
            String result = retryWithBackoff(3);
            System.out.println("Success: " + result);
        } catch (RuntimeException e) {
            System.out.println("Expected failure: " + e.getMessage());
        }

        System.out.println("\n=== Example 2: Exception Translation ===");
        User user = findUserById("123");
        System.out.println("Found user: " + user);

        System.out.println("\n=== Example 5: Structured Error Response ===");
        ErrorResponse response = handleException(new ValidationException("Name required"));
        System.out.println("Error response: " + response);
    }
}
