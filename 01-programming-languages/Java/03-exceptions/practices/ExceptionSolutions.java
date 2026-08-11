package academy.javaengineering.exceptions.practices;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Complete solutions for all 10 exception handling exercises.
 */
public class ExceptionSolutions {

    // Exercise 1: Basic try-catch
    public static int safeDivide(int a, int b) {
        try {
            return a / b;
        } catch (ArithmeticException e) {
            System.err.println("Division failed: " + e.getMessage());
            return 0;
        }
    }

    // Exercise 2: Multiple catch blocks
    public static int parseAndAccess(String input, int index) {
        try {
            int value = Integer.parseInt(input);
            String[] parts = {"zero", "one", "two", "three"};
            return value + parts[index].length();
        } catch (NumberFormatException e) {
            System.err.println("Invalid number: " + input);
            return -1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid index: " + index);
            return -1;
        }
    }

    // Exercise 3: Multi-catch + throw
    public static String parseAndThrow(String input) throws IllegalArgumentException, IllegalStateException {
        try {
            int value = Integer.parseInt(input);
            if (value < 0) {
                throw new IllegalArgumentException("Negative value: " + value);
            }
            return "Value is " + value;
        } catch (NumberFormatException | IllegalArgumentException e) {
            throw new IllegalStateException("Failed to process input", e);
        }
    }

    // Exercise 4: Try-finally
    public static String readFirstLine(String filePath) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            return reader.readLine();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "ERROR";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing reader: " + e.getMessage());
                }
            }
        }
    }

    // Exercise 5: Try-with-resources + exception translation
    public static List<String> readAllLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
        return lines;
    }

    // Exercise 6: Exception chaining
    public static void validateAndProcess(String input) throws RuntimeException {
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid input format", e);
        }

        if (number <= 0) {
            throw new RuntimeException("Value must be positive",
                    new IllegalArgumentException("Non-positive value: " + number));
        }

        System.out.println("Processed: " + number);
    }

    // Exercise 7: RuntimeException recovery
    public static int reliableOperation(String input, int fallback) {
        try {
            return Integer.parseInt(input) * 2;
        } catch (RuntimeException e) {
            System.err.println("Operation failed, using fallback: " + e.getMessage());
            return fallback;
        }
    }

    // Exercise 8: Custom exception hierarchy
    public static void registerUser(String username, String email) throws ValidationException {
        if (username == null || username.length() < 3 || username.length() > 20) {
            throw new UsernameValidationException(
                    "Username must be 3-20 characters, got: " +
                    (username == null ? "null" : username.length()));
        }
        if (email == null || !email.contains("@")) {
            throw new EmailValidationException(
                    "Email must contain @, got: " + email);
        }
        System.out.println("User registered: " + username + " <" + email + ">");
    }

    // Exercise 9: Retry pattern with backoff
    public static int retryOperation(RetryableOperation operation, int maxRetries) throws InterruptedException {
        Exception lastException = null;
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return operation.execute();
            } catch (Exception e) {
                lastException = e;
                System.err.println("Attempt " + (attempt + 1) + " failed: " + e.getMessage());
                if (attempt < maxRetries) {
                    long backoffMs = (long) (100 * Math.pow(2, attempt));
                    System.err.println("Retrying in " + backoffMs + "ms...");
                    Thread.sleep(backoffMs);
                }
            }
        }
        throw new RuntimeException("All " + (maxRetries + 1) + " attempts failed", lastException);
    }

    // Exercise 10: UncaughtExceptionHandler
    public static void setupGlobalHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught exception in thread '" + thread.getName() + "': "
                    + throwable.getClass().getSimpleName() + " - " + throwable.getMessage());
            throwable.printStackTrace(System.err);
        });
    }

    // --- Helper interfaces/classes ---

    @FunctionalInterface
    public interface RetryableOperation {
        int execute() throws Exception;
    }

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

    // --- Demo main method ---
    public static void main(String[] args) {
        System.out.println("=== Exception Handling Solutions Demo ===\n");

        // Exercise 1
        System.out.println("Exercise 1: safeDivide(10, 3) = " + safeDivide(10, 3));
        System.out.println("Exercise 1: safeDivide(10, 0) = " + safeDivide(10, 0));

        // Exercise 2
        System.out.println("\nExercise 2: parseAndAccess(\"42\", 1) = " + parseAndAccess("42", 1));
        System.out.println("Exercise 2: parseAndAccess(\"abc\", 1) = " + parseAndAccess("abc", 1));
        System.out.println("Exercise 2: parseAndAccess(\"42\", 10) = " + parseAndAccess("42", 10));

        // Exercise 3
        try {
            System.out.println("\nExercise 3: parseAndThrow(\"42\") = " + parseAndThrow("42"));
            parseAndThrow("abc");
        } catch (IllegalStateException e) {
            System.out.println("Exercise 3: Caught: " + e.getMessage());
            System.out.println("  Cause: " + e.getCause().getClass().getSimpleName());
        }

        // Exercise 4
        System.out.println("\nExercise 4: readFirstLine(\"nonexistent.txt\") = " + readFirstLine("nonexistent.txt"));

        // Exercise 5
        try {
            System.out.println("\nExercise 5: readAllLines(\"nonexistent.txt\") = " + readAllLines("nonexistent.txt"));
        } catch (RuntimeException e) {
            System.out.println("Exercise 5: Caught RuntimeException wrapping IOException");
        }

        // Exercise 6
        System.out.println("\nExercise 6:");
        try {
            validateAndProcess("not-a-number");
        } catch (RuntimeException e) {
            System.out.println("  Caught: " + e.getMessage() + " (cause: " + e.getCause().getClass().getSimpleName() + ")");
        }

        // Exercise 7
        System.out.println("\nExercise 7: reliableOperation(\"abc\", 99) = " + reliableOperation("abc", 99));
        System.out.println("Exercise 7: reliableOperation(\"21\", 99) = " + reliableOperation("21", 99));

        // Exercise 8
        System.out.println("\nExercise 8:");
        try {
            registerUser("ab", "test@example.com");
        } catch (ValidationException e) {
            System.out.println("  Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        try {
            registerUser("validuser", "invalid-email");
        } catch (ValidationException e) {
            System.out.println("  Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        // Exercise 9
        System.out.println("\nExercise 9:");
        try {
            int attemptCount = 0;
            int result = retryOperation(() -> {
                attemptCount++;
                if (attemptCount < 3) {
                    throw new RuntimeException("Simulated failure #" + attemptCount);
                }
                return 42;
            }, 5);
            System.out.println("  Result: " + result + " after " + attemptCount + " attempts");
        } catch (Exception e) {
            System.out.println("  Failed: " + e.getMessage());
        }

        // Exercise 10
        System.out.println("\nExercise 10: Setting up global handler...");
        setupGlobalHandler();
        Thread t = new Thread(() -> {
            throw new RuntimeException("Test exception from thread");
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== All exercises complete ===");
    }
}
