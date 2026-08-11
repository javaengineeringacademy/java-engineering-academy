package academy.javaengineering.exceptions.checkedexception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates checked exception handling in Java.
 *
 * <p>Checked exceptions extend {@code Exception} (not {@code RuntimeException})
 * and require the caller to either catch them or declare them in a {@code throws}
 * clause. This is enforced at compile time.
 */
public class CheckedException {

    /**
     * Demonstrates basic try-catch for IOException.
     * FileNotFoundException is a subclass of IOException.
     */
    public static void readFileBasic(String path) {
        System.out.println("--- Basic try-catch for IOException ---");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            System.out.println("First line: " + line);
            reader.close();
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
    }

    /**
     * Demonstrates try-with-resources for automatic resource management.
     * The reader is automatically closed even if an exception occurs.
     */
    public static void readFileWithTryWithResources(String path) {
        System.out.println("\n--- Try-With-Resources ---");
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Caught IOException: " + e.getMessage());
        }
    }

    /**
     * Demonstrates the catch-or-specify requirement.
     * This method declares the checked exception instead of catching it.
     */
    public static String readFirstLine(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.readLine();
        }
    }

    /**
     * Demonstrates catching multiple exception types.
     */
    public static void demonstrateMultiCatch(String path) {
        System.out.println("\n--- Multiple Catch Blocks ---");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            int value = Integer.parseInt(line);
            System.out.println("Parsed value: " + value);
            reader.close();
        } catch (IOException e) {
            System.out.println("I/O error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Number format error: " + e.getMessage());
        }
    }

    /**
     * Demonstrates catch-or-specify with SQLException.
     */
    public static void connectToDatabase(String url, String user, String password)
            throws SQLException {
        System.out.println("\n--- Database Connection (throws declaration) ---");
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected successfully.");
        conn.close();
    }

    /**
     * Demonstrates exception chaining — wrapping a checked exception
     * in a more meaningful exception.
     */
    public static void processFile(String path) {
        System.out.println("\n--- Exception Chaining ---");
        try {
            String content = readFirstLine(path);
            System.out.println("Content: " + content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process file: " + path, e);
        }
    }

    /**
     * Demonstrates InterruptedException handling.
     * This is a checked exception that requires special handling because
     * it clears the interrupt status.
     */
    public static void demonstrateInterrupted() {
        System.out.println("\n--- InterruptedException Handling ---");
        try {
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Sleep completed.");
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }

    /**
     * Demonstrates ClassNotFoundException — a checked exception thrown
     * when a class is not found at runtime.
     */
    public static void demonstrateClassNotFound(String className) {
        System.out.println("\n--- ClassNotFoundException ---");
        try {
            Class<?> clazz = Class.forName(className);
            System.out.println("Found class: " + clazz.getName());
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }
    }

    /**
     * Demonstrates the pattern of partial failure handling.
     * Continues processing even when some files fail.
     */
    public static void readMultipleFiles(String[] paths) {
        System.out.println("\n--- Partial Failure Handling ---");
        for (String path : paths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                System.out.println(path + ": " + reader.readLine());
            } catch (IOException e) {
                System.out.println(path + ": FAILED - " + e.getMessage());
            }
        }
    }

    /**
     * Demonstrates a checked exception in a method that performs
     * a recoverable operation with a fallback.
     */
    public static String readWithFallback(String path, String fallback) {
        System.out.println("\n--- Fallback Pattern ---");
        try {
            return readFirstLine(path);
        } catch (IOException e) {
            System.out.println("Using fallback value. Error: " + e.getMessage());
            return fallback;
        }
    }

    /**
     * Demonstrates a custom checked exception.
     */
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }

        public ValidationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Example method that throws a custom checked exception.
     */
    public static void validatePositive(int value) throws ValidationException {
        if (value <= 0) {
            throw new ValidationException("Value must be positive, got: " + value);
        }
        System.out.println("Valid value: " + value);
    }

    /**
     * Demonstrates handling a custom checked exception.
     */
    public static void demonstrateCustomCheckedException() {
        System.out.println("\n--- Custom Checked Exception ---");
        try {
            validatePositive(5);
            validatePositive(-3);
        } catch (ValidationException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }

    /**
     * Demonstrates exception propagation through call stack.
     */
    public static void methodA() throws IOException {
        methodB();
    }

    public static void methodB() throws IOException {
        methodC();
    }

    public static void methodC() throws IOException {
        throw new IOException("Error in methodC");
    }

    public static void demonstratePropagation() {
        System.out.println("\n--- Exception Propagation ---");
        try {
            methodA();
        } catch (IOException e) {
            System.out.println("Caught in methodA: " + e.getMessage());
            System.out.println("Stack trace:");
            for (StackTraceElement element : e.getStackTrace()) {
                System.out.println("  at " + element);
            }
        }
    }

    /**
     * Main method demonstrating all checked exception patterns.
     */
    public static void main(String[] args) {
        // Basic try-catch
        readFileBasic("/tmp/test.txt");
        readFileBasic("/nonexistent/file.txt");

        // Try-with-resources
        readFileWithTryWithResources("/tmp/test.txt");

        // Multiple catch blocks
        demonstrateMultiCatch("/tmp/test.txt");

        // Catch-or-specify — caller handles the exception
        try {
            String line = readFirstLine("/tmp/test.txt");
            System.out.println("Read: " + line);
        } catch (IOException e) {
            System.out.println("Caller caught: " + e.getMessage());
        }

        // Exception chaining
        processFile("/nonexistent/file.txt");

        // InterruptedException
        demonstrateInterrupted();

        // ClassNotFoundException
        demonstrateClassNotFound("java.lang.String");
        demonstrateClassNotFound("com.nonexistent.Class");

        // Partial failure handling
        readMultipleFiles(new String[]{
            "/tmp/test.txt",
            "/nonexistent/file.txt",
            "/tmp/another.txt"
        });

        // Fallback pattern
        String result = readWithFallback("/nonexistent/file.txt", "default");
        System.out.println("Result: " + result);

        // Custom checked exception
        demonstrateCustomCheckedException();

        // Exception propagation
        demonstratePropagation();
    }
}
