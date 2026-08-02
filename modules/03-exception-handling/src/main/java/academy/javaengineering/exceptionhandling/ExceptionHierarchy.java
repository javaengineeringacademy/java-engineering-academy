package academy.javaengineering.exceptionhandling;

import java.io.IOException;

/**
 * Demonstrates the exception hierarchy and checked vs unchecked exceptions.
 *
 * <p>This class explains the Throwable hierarchy, including checked exceptions,
 * unchecked exceptions, and error types.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Throwable hierarchy</li>
 *   <li>Checked exceptions (Exception)</li>
 *   <li>Unchecked exceptions (RuntimeException)</li>
 *   <li>Error types</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class ExceptionHierarchy {

    /**
     * Demonstrates different exception types in the hierarchy.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Checked exception example
        demonstrateCheckedException();

        // Unchecked exception example
        demonstrateUncheckedException();

        // Error example (should not be caught normally)
        demonstrateError();

        // Multi-catch with final syntax
        demonstrateMultiCatch();
    }

    /**
     * Demonstrates checked exceptions that must be declared or caught.
     */
    public static void demonstrateCheckedException() {
        System.out.println("=== Checked Exceptions ===");
        System.out.println("Checked exceptions: IOException, SQLException, ClassNotFoundException");
        System.out.println("Must be caught or declared in throws clause");

        try {
            readFile("/nonexistent/file.txt");
        } catch (IOException e) {
            System.out.println("Checked exception caught: " + e.getMessage());
        }
        // Expected output:
        // === Checked Exceptions ===
        // Checked exceptions: IOException, SQLException, ClassNotFoundException
        // Must be caught or declared in throws clause
        // Checked exception caught: /nonexistent/file.txt (No such file or directory)
    }

    /**
     * Demonstrates unchecked exceptions (RuntimeException subclasses).
     */
    public static void demonstrateUncheckedException() {
        System.out.println("\n=== Unchecked Exceptions ===");
        System.out.println("Unchecked exceptions: NullPointerException, ArrayIndexOutOfBoundsException");
        System.out.println("No need to declare or catch explicitly");

        try {
            String str = null;
            str.length();
        } catch (NullPointerException e) {
            System.out.println("Unchecked exception caught: " + e.getClass().getSimpleName());
        }
        // Expected output:
        // === Unchecked Exceptions ===
        // Unchecked exceptions: NullPointerException, ArrayIndexOutOfBoundsException
        // No need to declare or catch explicitly
        // Unchecked exception caught: NullPointerException
    }

    /**
     * Demonstrates Error types (should not be caught).
     */
    public static void demonstrateError() {
        System.out.println("\n=== Error Types ===");
        System.out.println("Errors: OutOfMemoryError, StackOverflowError");
        System.out.println("These are serious problems, should not be caught");

        try {
            throw new StackOverflowError("Simulated stack overflow");
        } catch (StackOverflowError e) {
            System.out.println("Error caught (not recommended): " + e.getMessage());
        }
        // Expected output:
        // === Error Types ===
        // Errors: OutOfMemoryError, StackOverflowError
        // These are serious problems, should not be caught
        // Error caught (not recommended): Simulated stack overflow
    }

    /**
     * Demonstrates multi-catch block with final syntax.
     */
    public static void demonstrateMultiCatch() {
        System.out.println("\n=== Multi-Catch Block ===");

        try {
            processInput("invalid");
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            System.out.println("Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        // Expected output:
        // === Multi-Catch Block ===
        // Caught: IllegalArgumentException - Invalid input: invalid
    }

    /**
     * Simulates reading a file (checked exception).
     *
     * @param path the file path
     * @throws IOException if file not found
     */
    public static void readFile(String path) throws IOException {
        if (!java.nio.file.Files.exists(java.nio.file.Path.of(path))) {
            throw new IOException(path + " (No such file or directory)");
        }
    }

    /**
     * Processes input with different exception types.
     *
     * @param input the input to process
     */
    public static void processInput(String input) {
        if (input == null) {
            throw new NullPointerException("Input cannot be null");
        }
        if ("invalid".equals(input)) {
            throw new IllegalArgumentException("Invalid input: " + input);
        }
        if ("unsupported".equals(input)) {
            throw new UnsupportedOperationException("Unsupported operation");
        }
        System.out.println("Processed: " + input);
    }
}
