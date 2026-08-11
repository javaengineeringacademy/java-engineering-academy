package academy.javaengineering.exceptions.exercises;

import java.io.*;
import java.sql.*;

/**
 * Module-level exception handling exercises.
 *
 * <p>Complete each exercise by implementing the method body.
 * Tests are in the solutions folder.
 *
 * <p>Difficulty levels: Easy, Medium, Hard
 */
public class ExceptionExercises {

    // ============================================================
    // Exercise 1: Safe Division (Easy)
    // ============================================================

    /**
     * Divide two numbers safely.
     * Throw ArithmeticException if divisor is zero.
     * Throw IllegalArgumentException if either argument is null.
     *
     * @param dividend the number to divide
     * @param divisor the number to divide by
     * @return the result of division
     */
    public static Double safeDivide(Double dividend, Double divisor) {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Implement safeDivide");
    }

    // ============================================================
    // Exercise 2: File Reader with Custom Exception (Medium)
    // ============================================================

    /**
     * Read a file and return its content.
     * If file not found, throw a custom FileNotFoundException with context.
     * If IO error occurs, throw a custom ReadException wrapping the cause.
     *
     * @param path the file path
     * @return the file content as string
     * @throws FileReadException if any error occurs reading the file
     */
    public static String readFileContent(String path) throws FileReadException {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Implement readFileContent");
    }

    // ============================================================
    // Exercise 3: Retry Logic (Medium)
    // ============================================================

    /**
     * Execute an operation with retry logic.
     * Retry up to maxRetries times on IOException.
     * Throw the last exception if all retries fail.
     *
     * @param operation the operation to execute
     * @param maxRetries maximum number of retries
     * @return the result of the operation
     * @throws IOException if all retries fail
     */
    public static String retryOperation(RetryableOperation<String> operation,
                                         int maxRetries) throws IOException {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Implement retryOperation");
    }

    // ============================================================
    // Exercise 4: Exception Chaining (Medium)
    // ============================================================

    /**
     * Parse a configuration string.
     * If format is wrong, throw ConfigParseException wrapping the cause.
     * If value is out of range, throw ConfigValidationException.
     *
     * @param config the configuration string in format "key=value"
     * @return the parsed value as integer
     * @throws ConfigParseException if format is wrong
     * @throws ConfigValidationException if value is out of range
     */
    public static int parseConfig(String config)
            throws ConfigParseException, ConfigValidationException {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Implement parseConfig");
    }

    // ============================================================
    // Exercise 5: Multi-Catch Handler (Easy)
    // ============================================================

    /**
     * Handle multiple exception types in a single catch block.
     * Return a user-friendly message based on exception type.
     *
     * @param ex the exception to handle
     * @return a user-friendly error message
     */
    public static String handleMultipleExceptions(Exception ex) {
        // TODO: Implement this method using multi-catch (Java 7+)
        throw new UnsupportedOperationException("Implement handleMultipleExceptions");
    }

    // ============================================================
    // Exercise 6: Resource Cleanup (Hard)
    // ============================================================

    /**
     * Process a file with proper resource cleanup.
     * Read each line, transform it, write to output.
     * Ensure both resources are closed even if an exception occurs.
     * If both read and write fail, the read exception should be primary
     * and write exception should be suppressed.
     *
     * @param inputPath input file path
     * @param outputPath output file path
     * @throws IOException if processing fails
     */
    public static void processFile(String inputPath, String outputPath)
            throws IOException {
        // TODO: Implement this method
        throw new UnsupportedOperationException("Implement processFile");
    }

    // ============================================================
    // Functional interface for retry exercise
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
    // Main - run exercises
    // ============================================================

    public static void main(String[] args) {
        System.out.println("=== Exception Exercises ===");
        System.out.println("Implement each method and test with the solutions.");
        System.out.println();

        try {
            System.out.println("Exercise 1: safeDivide(10.0, 0.0)");
            Double result = safeDivide(10.0, 0.0);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Expected: " + e.getClass().getSimpleName()
                + " - " + e.getMessage());
        }

        try {
            System.out.println("\nExercise 5: handleMultipleExceptions");
            String msg = handleMultipleExceptions(
                new IllegalArgumentException("bad input"));
            System.out.println("Message: " + msg);
        } catch (Exception e) {
            System.out.println("Unexpected: " + e.getMessage());
        }
    }
}
