package academy.javaengineering.exceptions.checkedexception;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Solutions for checked exception exercises.
 */
public class CheckedExceptionSolutions {

    /**
     * Custom checked exception for the exercises.
     */
    public static class InsufficientBalanceException extends Exception {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    /**
     * Custom unchecked exception for exception translation.
     */
    public static class FileProcessingException extends RuntimeException {
        public FileProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ========================================================================
    // Exercise 1: Catch-or-Specify — SOLUTION
    // ========================================================================

    /**
     * Solution: Uses try-catch to handle the checked exception internally.
     */
    public static String readLine(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return "DEFAULT";
        }
    }

    // ========================================================================
    // Exercise 2: Exception Translation — SOLUTION
    // ========================================================================

    /**
     * Solution: Wraps IOException in an unchecked FileProcessingException.
     */
    public static String readWithTranslation(String path) {
        try {
            return readLine(path);
        } catch (Exception e) {
            throw new FileProcessingException(
                "Failed to process file: " + path, e);
        }
    }

    // ========================================================================
    // Exercise 3: Partial Failure Handling — SOLUTION
    // ========================================================================

    /**
     * Solution: Continues processing even when individual files fail.
     */
    public static int countSuccessfulReads(String[] paths) {
        int count = 0;
        for (String path : paths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                reader.readLine();
                count++;
                System.out.println("OK: " + path);
            } catch (IOException e) {
                System.out.println("FAIL: " + path + " — " + e.getMessage());
            }
        }
        return count;
    }

    // ========================================================================
    // Exercise 4: Fallback Pattern — SOLUTION
    // ========================================================================

    /**
     * Solution: Returns a fallback value when reading fails.
     */
    public static String readOrFallback(String path, String fallback) {
        try {
            return readLine(path);
        } catch (Exception e) {
            System.out.println("Using fallback for: " + path);
            return fallback;
        }
    }

    // ========================================================================
    // Exercise 5: Custom Checked Exception — SOLUTION
    // ========================================================================

    /**
     * Solution: Validates balance and throws custom checked exception.
     */
    public static double withdraw(double balance, double amount)
            throws InsufficientBalanceException {
        if (amount > balance) {
            throw new InsufficientBalanceException(
                "Insufficient balance: have " + balance + ", need " + amount);
        }
        return balance - amount;
    }

    // ========================================================================
    // Exercise 6: Propagation Through Layers — SOLUTION
    // ========================================================================

    /**
     * Solution: Lets the checked exception propagate via throws declaration.
     */
    public static String readAndPrefix(String path) throws IOException {
        String line = readLine(path);
        return "READ: " + line;
    }

    // ========================================================================
    // Exercise 7: Try-With-Resources — SOLUTION
    // ========================================================================

    /**
     * Solution: Uses try-with-resources for automatic resource management.
     */
    public static int countCharacters(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            int count = 0;
            int ch;
            while ((ch = reader.read()) != -1) {
                count++;
            }
            return count;
        } catch (IOException e) {
            System.out.println("Error counting characters: " + e.getMessage());
            return -1;
        }
    }

    // ========================================================================
    // Exercise 8: Combined Patterns — SOLUTION
    // ========================================================================

    /**
     * Solution: Combines reading, validation, and exception wrapping.
     */
    public static String readAndValidate(String path) {
        try {
            String content = readLine(path);
            if (content == null || content.isEmpty()) {
                throw new IllegalArgumentException("File content is empty");
            }
            return content.trim().toUpperCase();
        } catch (IOException e) {
            throw new FileProcessingException(
                "Failed to read file: " + path, e);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Checked Exception Solutions ===\n");

        // Exercise 1
        String line = readLine("/tmp/test.txt");
        System.out.println("Exercise 1 - readLine: " + line);

        // Exercise 3
        System.out.println("\nExercise 3 - Partial failure:");
        int successes = countSuccessfulReads(new String[]{
            "/tmp/test.txt", "/nonexistent.txt", "/tmp/another.txt"
        });
        System.out.println("Successful reads: " + successes);

        // Exercise 5
        System.out.println("\nExercise 5 - Withdraw:");
        try {
            double balance = withdraw(100.0, 30.0);
            System.out.println("New balance: " + balance);
        } catch (InsufficientBalanceException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        try {
            double balance = withdraw(100.0, 150.0);
            System.out.println("New balance: " + balance);
        } catch (InsufficientBalanceException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // Exercise 6
        System.out.println("\nExercise 6 - Propagation:");
        try {
            String prefixed = readAndPrefix("/tmp/test.txt");
            System.out.println(prefixed);
        } catch (IOException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        // Exercise 8
        System.out.println("\nExercise 8 - Combined:");
        try {
            String validated = readAndValidate("/tmp/test.txt");
            System.out.println("Validated: " + validated);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
