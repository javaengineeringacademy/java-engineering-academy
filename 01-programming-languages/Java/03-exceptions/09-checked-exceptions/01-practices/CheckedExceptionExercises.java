package academy.javaengineering.exceptions.checkedexception;

import java.io.IOException;

/**
 * Exercises on checked exception handling.
 *
 * <p>Complete each exercise by implementing the method body.
 * Each exercise demonstrates a different aspect of checked exceptions.
 */
public class CheckedExceptionExercises {

    /**
     * Custom checked exception for the exercises.
     */
    public static class InsufficientBalanceException extends Exception {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    /**
     * Custom checked exception for file processing.
     */
    public static class FileProcessingException extends Exception {
        public FileProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ========================================================================
    // Exercise 1: Catch-or-Specify
    // ========================================================================

    /**
     * This method reads a line from a file path.
     *
     * <p>TASK: Implement this method so that it compiles.
     * Either catch the IOException inside the method OR declare it
     * in the throws clause so the caller must handle it.
     *
     * @param path the file path to read
     * @return the first line of the file, or "DEFAULT" if an error occurs
     */
    public static String readLine(String path) {
        // TODO: Implement using try-catch or throws declaration
        // Hint: Use java.io.BufferedReader and java.io.FileReader
        return "DEFAULT";
    }

    // ========================================================================
    // Exercise 2: Exception Translation
    // ========================================================================

    /**
     * This method reads a file and wraps any IOException in a
     * FileProcessingException (an unchecked exception).
     *
     * <p>TASK: Implement the method body so that:
     * - It attempts to read the file using readLine(path)
     * - If an IOException occurs, it throws FileProcessingException
     *   with the original exception as the cause
     *
     * @param path the file path to read
     * @return the file content
     * @throws FileProcessingException if reading fails
     */
    public static String readWithTranslation(String path) {
        // TODO: Implement exception translation
        return null;
    }

    // ========================================================================
    // Exercise 3: Partial Failure Handling
    // ========================================================================

    /**
     * Reads multiple files and counts how many succeed.
     *
     * <p>TASK: Implement this method so that:
     * - It tries to read each file in the array
     * - If a file fails to read, it prints the error and continues
     * - It returns the number of successfully read files
     *
     * @param paths array of file paths
     * @return the count of successfully read files
     */
    public static int countSuccessfulReads(String[] paths) {
        // TODO: Implement partial failure handling
        return 0;
    }

    // ========================================================================
    // Exercise 4: Fallback Pattern
    // ========================================================================

    /**
     * Attempts to read a file and returns a fallback if it fails.
     *
     * <p>TASK: Implement this method so that:
     * - It tries to read the file
     * - If it succeeds, returns the file content
     * - If an IOException occurs, returns the fallback value
     *
     * @param path the file path
     * @param fallback the value to return on failure
     * @return the file content or the fallback
     */
    public static String readOrFallback(String path, String fallback) {
        // TODO: Implement fallback pattern
        return fallback;
    }

    // ========================================================================
    // Exercise 5: Custom Checked Exception
    // ========================================================================

    /**
     * Validates that a bank balance is sufficient for a withdrawal.
     *
     * <p>TASK: Implement this method so that:
     * - If amount > balance, throw InsufficientBalanceException
     *   with message "Insufficient balance: have " + balance + ", need " + amount
     * - Otherwise, return the new balance (balance - amount)
     *
     * @param balance the current balance
     * @param amount the amount to withdraw
     * @return the new balance after withdrawal
     * @throws InsufficientBalanceException if amount exceeds balance
     */
    public static double withdraw(double balance, double amount)
            throws InsufficientBalanceException {
        // TODO: Implement balance validation
        return balance;
    }

    // ========================================================================
    // Exercise 6: Propagation Through Layers
    // ========================================================================

    /**
     * Simulates a method that calls a lower-level method and lets the
     * checked exception propagate.
     *
     * <p>TASK: Implement this method so that:
     * - It calls readLine(path) from Exercise 1
     * - If an IOException occurs, it should propagate (use throws declaration)
     * - It returns the line prefixed with "READ: "
     *
     * @param path the file path
     * @return the prefixed line
     * @throws IOException if reading fails
     */
    public static String readAndPrefix(String path) throws IOException {
        // TODO: Implement with exception propagation
        return null;
    }

    // ========================================================================
    // Exercise 7: Try-With-Resources
    // ========================================================================

    /**
     * Counts the number of characters in a file.
     *
     * <p>TASK: Implement this method so that:
     * - It uses try-with-resources to open a BufferedReader
     * - It reads the entire file and counts all characters
     * - If an IOException occurs, it returns -1
     *
     * @param path the file path
     * @return the character count, or -1 on error
     */
    public static int countCharacters(String path) {
        // TODO: Implement using try-with-resources
        return -1;
    }

    // ========================================================================
    // Exercise 8: Combined Patterns
    // ========================================================================

    /**
     * Reads a file, validates its content, and returns it.
     *
     * <p>TASK: Implement this method so that:
     * - It reads the file using readLine(path)
     * - If the content is null or empty, throw IllegalArgumentException
     * - If an IOException occurs, throw FileProcessingException wrapping it
     * - Otherwise, return the content trimmed and converted to uppercase
     *
     * @param path the file path
     * @return the processed content
     * @throws FileProcessingException if reading fails
     */
    public static String readAndValidate(String path) {
        // TODO: Implement combined patterns
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== Checked Exception Exercises ===");
        System.out.println("Implement the TODO methods above and test them.\n");

        // Test Exercise 1
        String line = readLine("/tmp/test.txt");
        System.out.println("Exercise 1 - readLine: " + line);

        // Test Exercise 2
        try {
            String content = readWithTranslation("/tmp/test.txt");
            System.out.println("Exercise 2 - readWithTranslation: " + content);
        } catch (Exception e) {
            System.out.println("Exercise 2 - Exception: " + e.getMessage());
        }

        // Test Exercise 5
        try {
            double newBalance = withdraw(100.0, 30.0);
            System.out.println("Exercise 5 - withdraw: New balance = " + newBalance);
        } catch (InsufficientBalanceException e) {
            System.out.println("Exercise 5 - Exception: " + e.getMessage());
        }

        try {
            double newBalance = withdraw(100.0, 150.0);
            System.out.println("Exercise 5 - withdraw: New balance = " + newBalance);
        } catch (InsufficientBalanceException e) {
            System.out.println("Exercise 5 - Exception: " + e.getMessage());
        }
    }
}
