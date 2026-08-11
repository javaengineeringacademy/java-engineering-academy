package academy.javaengineering.exceptions.practices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Mixed-concept exception handling exercises.
 * Each method is a separate exercise. Complete the TODO sections.
 */
public class ExceptionPractices {

    // Exercise 1: Basic try-catch
    public static int safeDivide(int a, int b) {
        // TODO: Use try-catch to handle ArithmeticException
        // Return 0 if division fails
        return 0;
    }

    // Exercise 2: Multiple catch blocks
    public static int parseAndAccess(String input, int index) {
        // TODO: Catch NumberFormatException AND ArrayIndexOutOfBoundsException
        // Return -1 if either fails
        return -1;
    }

    // Exercise 3: Multi-catch + throw
    public static String parseAndThrow(String input) throws IllegalArgumentException, IllegalStateException {
        // TODO: Use multi-catch (|) to catch both exception types
        // Re-throw as a new IllegalStateException with the original cause
        return null;
    }

    // Exercise 4: Try-finally
    public static String readFirstLine(String filePath) {
        // TODO: Use try-finally to guarantee the reader is closed
        // Return the first line of the file, or "ERROR" on any exception
        return "ERROR";
    }

    // Exercise 5: Try-with-resources + exception translation
    public static List<String> readAllLines(String filePath) throws IOException {
        // TODO: Use try-with-resources to read all lines from a file
        // If an IOException occurs, translate it to a RuntimeException
        // wrapping the original cause
        return new ArrayList<>();
    }

    // Exercise 6: Exception chaining
    public static void validateAndProcess(String input) throws RuntimeException {
        // TODO: Parse input to int, validate it is positive
        // If parsing fails, throw NumberFormatException chained to a RuntimeException
        // If number is not positive, throw IllegalArgumentException chained to RuntimeException
    }

    // Exercise 7: RuntimeException recovery
    public static int reliableOperation(String input, int fallback) {
        // TODO: Attempt to parse input as int
        // If any RuntimeException occurs, return the fallback value
        // Log the exception message to System.err
        return fallback;
    }

    // Exercise 8: Custom exception hierarchy
    public static void registerUser(String username, String email) throws ValidationException {
        // TODO: Validate username (non-null, 3-20 chars) and email (non-null, contains @)
        // Throw UsernameValidationException if username is invalid
        // Throw EmailValidationException if email is invalid
        // Both must extend ValidationException
    }

    // Exercise 9: Retry pattern with backoff
    public static int retryOperation(RetryableOperation operation, int maxRetries) throws InterruptedException {
        // TODO: Retry the operation up to maxRetries times
        // On failure, wait with exponential backoff: 100ms, 200ms, 400ms, ...
        // If all retries fail, throw the last caught exception
        return 0;
    }

    // Exercise 10: UncaughtExceptionHandler
    public static void setupGlobalHandler() {
        // TODO: Set a default UncaughtExceptionHandler for all threads
        // The handler should print the thread name and exception message to System.err
    }

    // --- Helper interfaces/classes for exercises ---

    @FunctionalInterface
    public interface RetryableOperation {
        int execute() throws Exception;
    }

    // TODO: Uncomment and use these in Exercise 8
    /*
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static class UsernameValidationException extends ValidationException {
        public UsernameValidationException(String message) {
            super(message);
        }
    }

    public static class EmailValidationException extends ValidationException {
        public EmailValidationException(String message) {
            super(message);
        }
    }
    */
}
