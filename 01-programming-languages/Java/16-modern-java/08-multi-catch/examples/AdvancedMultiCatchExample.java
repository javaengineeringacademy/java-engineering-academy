package academy.javaengineering.modern.multicatch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Advanced multi-catch usage patterns.
 */
public class AdvancedMultiCatchExample {

    public record Result<T>(T value, String error) {
        public static <T> Result<T> success(T value) {
            return new Result<>(value, null);
        }

        public static <T> Result<T> failure(String error) {
            return new Result<>(null, error);
        }

        public boolean isSuccess() {
            return error == null;
        }
    }

    public static void main(String[] args) {
        // Multi-catch with resource management
        System.out.println("=== Multi-catch with Resources ===");
        try {
            processWithResources();
        } catch (IOException | SQLException e) {
            System.out.println("Error in resource processing: " + e.getMessage());
        }

        // Multi-catch returning Result type
        System.out.println("\n=== Multi-catch with Result Type ===");
        List<String> inputs = List.of("123", "abc", "456", "def", "789");
        for (String input : inputs) {
            Result<Integer> result = safeParse(input);
            if (result.isSuccess()) {
                System.out.println("Success: " + result.value());
            } else {
                System.out.println("Failure: " + result.error());
            }
        }

        // Multi-catch in stream operations
        System.out.println("\n=== Multi-catch in Streams ===");
        List<String> numbers = List.of("1", "2", "abc", "4", "def", "6");
        List<Integer> parsed = numbers.stream()
            .map(AdvancedMultiCatchExample::safeParseInt)
            .filter(result -> result.isSuccess())
            .map(Result::value)
            .toList();
        System.out.println("Parsed numbers: " + parsed);

        // Multi-catch with retry logic
        System.out.println("\n=== Multi-catch with Retry ===");
        int attempts = 0;
        boolean success = false;
        while (!success && attempts < 3) {
            try {
                riskyOperation();
                success = true;
                System.out.println("Operation succeeded on attempt " + (attempts + 1));
            } catch (IOException | SQLException e) {
                attempts++;
                System.out.println("Attempt " + attempts + " failed: " + e.getMessage());
            }
        }
        if (!success) {
            System.out.println("Operation failed after 3 attempts");
        }
    }

    static void processWithResources() throws IOException, SQLException {
        // Simulate resource processing
        if (Math.random() < 0.5) {
            throw new IOException("Resource IO error");
        } else {
            throw new SQLException("Resource SQL error");
        }
    }

    static Result<Integer> safeParse(String input) {
        try {
            return Result.success(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Result.failure("Invalid number: " + input);
        }
    }

    static Result<Integer> safeParseInt(String input) {
        try {
            return Result.success(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Result.failure("Invalid number: " + input);
        }
    }

    static void riskyOperation() throws IOException, SQLException {
        if (Math.random() < 0.5) {
            throw new IOException("IO error");
        } else {
            throw new SQLException("SQL error");
        }
    }
}
