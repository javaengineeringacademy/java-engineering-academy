package academy.javaengineering.exceptions.solutions;

import java.io.*;
import java.nio.file.*;
import java.sql.*;

/**
 * Solutions for module-level exception handling exercises.
 *
 * <p>Each solution demonstrates the correct implementation with
 * proper exception handling patterns.
 *
 * <p>Difficulty levels: Easy, Medium, Hard
 */
public class ExceptionSolutions {

    // ============================================================
    // Solution 1: Safe Division
    // ============================================================

    /**
     * Divide two numbers safely.
     * Throw ArithmeticException if divisor is zero.
     * Throw IllegalArgumentException if either argument is null.
     */
    public static Double safeDivide(Double dividend, Double divisor) {
        if (dividend == null || divisor == null) {
            throw new IllegalArgumentException(
                "Arguments must not be null: dividend=" + dividend
                + ", divisor=" + divisor);
        }
        if (divisor == 0.0) {
            throw new ArithmeticException("Division by zero");
        }
        return dividend / divisor;
    }

    // ============================================================
    // Solution 2: File Reader with Custom Exception
    // ============================================================

    /**
     * Read a file and return its content with custom exceptions.
     */
    public static String readFileContent(String path) throws FileReadException {
        if (path == null || path.isBlank()) {
            throw new FileReadException("File path must not be null or empty");
        }

        File file = new File(path);
        if (!file.exists()) {
            throw new FileReadException("File not found: " + path);
        }

        try {
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new FileReadException("Failed to read file: " + path, e);
        }
    }

    // ============================================================
    // Solution 3: Retry Logic
    // ============================================================

    /**
     * Execute an operation with exponential backoff retry.
     */
    public static String retryOperation(RetryableOperation<String> operation,
                                         int maxRetries) throws IOException {
        IOException lastException = null;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return operation.execute();
            } catch (IOException e) {
                lastException = e;
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep((long) Math.pow(2, attempt) * 100);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", ie);
                    }
                }
            }
        }

        throw new IOException(
            "Operation failed after " + (maxRetries + 1) + " attempts",
            lastException);
    }

    // ============================================================
    // Solution 4: Exception Chaining
    // ============================================================

    /**
     * Parse a configuration string with chained exceptions.
     */
    public static int parseConfig(String config)
            throws ConfigParseException, ConfigValidationException {
        if (config == null || !config.contains("=")) {
            throw new ConfigParseException(
                "Invalid format: expected 'key=value', got: " + config, null);
        }

        String[] parts = config.split("=", 2);
        try {
            int value = Integer.parseInt(parts[1]);
            if (value < 0 || value > 100) {
                throw new ConfigValidationException(
                    "Value out of range [0-100]: " + value);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new ConfigParseException(
                "Cannot parse value as integer: " + parts[1], e);
        }
    }

    // ============================================================
    // Solution 5: Multi-Catch Handler
    // ============================================================

    /**
     * Handle multiple exception types using multi-catch.
     */
    public static String handleMultipleExceptions(Exception ex) {
        return switch (ex) {
            case IllegalArgumentException e ->
                "Invalid input: " + e.getMessage();
            case IllegalStateException e ->
                "Invalid state: " + e.getMessage();
            case NullPointerException e ->
                "Unexpected null value";
            case UnsupportedOperationException e ->
                "Operation not supported";
            default ->
                "Unexpected error: " + ex.getClass().getSimpleName();
        };
    }

    // ============================================================
    // Solution 6: Resource Cleanup
    // ============================================================

    /**
     * Process a file with proper resource cleanup.
     */
    public static void processFile(String inputPath, String outputPath)
            throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(inputPath));
             BufferedWriter writer = Files.newBufferedWriter(Path.of(outputPath))) {

            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String transformed = lineNumber + ": " + line.toUpperCase();
                writer.write(transformed);
                writer.newLine();
            }
        }
    }

    // ============================================================
    // Functional interface
    // ============================================================

    @FunctionalInterface
    public interface RetryableOperation<T> {
        T execute() throws IOException;
    }

    // ============================================================
    // Custom exception types
    // ============================================================

    public static class FileReadException extends Exception {
        public FileReadException(String message) {
            super(message);
        }

        public FileReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ConfigParseException extends Exception {
        public ConfigParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ConfigValidationException extends Exception {
        public ConfigValidationException(String message) {
            super(message);
        }
    }

    // ============================================================
    // Main - demonstrate solutions
    // ============================================================

    public static void main(String[] args) {
        System.out.println("=== Exception Solutions ===\n");

        // Solution 1
        System.out.println("--- Solution 1: Safe Division ---");
        try {
            System.out.println("10 / 2 = " + safeDivide(10.0, 2.0));
            System.out.println("10 / 0 = " + safeDivide(10.0, 0.0));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Solution 4
        System.out.println("\n--- Solution 4: Exception Chaining ---");
        try {
            parseConfig("port=abc");
        } catch (Exception e) {
            System.out.println("Caught: " + e.getClass().getSimpleName());
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Cause: " + e.getCause().getClass().getSimpleName());
            }
        }

        try {
            parseConfig("port=200");
        } catch (Exception e) {
            System.out.println("Range error: " + e.getMessage());
        }

        // Solution 5
        System.out.println("\n--- Solution 5: Multi-Catch ---");
        System.out.println(handleMultipleExceptions(
            new IllegalArgumentException("null name")));
        System.out.println(handleMultipleExceptions(
            new UnsupportedOperationException()));
        System.out.println(handleMultipleExceptions(
            new NullPointerException()));
    }
}
