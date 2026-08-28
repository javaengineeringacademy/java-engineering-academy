package academy.javaengineering.modern.multicatch;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Solutions for Multi-catch practice exercises.
 */
public class MultiCatchSolutions {

    // Exercise 1: Safe Parser
    public record ParseResult<T>(T value, String error) {
        public static <T> ParseResult<T> success(T value) {
            return new ParseResult<>(value, null);
        }

        public static <T> ParseResult<T> failure(String error) {
            return new ParseResult<>(null, error);
        }

        public boolean isSuccess() {
            return error == null;
        }
    }

    public static ParseResult<Integer> safeParseInt(String input) {
        try {
            return ParseResult.success(Integer.parseInt(input));
        } catch (NumberFormatException | NullPointerException e) {
            return ParseResult.failure("Invalid integer: " + input);
        }
    }

    public static ParseResult<Double> safeParseDouble(String input) {
        try {
            return ParseResult.success(Double.parseDouble(input));
        } catch (NumberFormatException | NullPointerException e) {
            return ParseResult.failure("Invalid double: " + input);
        }
    }

    // Exercise 2: File Processor
    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    public static String processFile(String filename) throws IOException, ParseException, ValidationException {
        try {
            // Simulate file reading
            if (filename == null) {
                throw new IOException("Filename is null");
            }
            
            // Simulate JSON parsing
            String content = "{\"name\": \"test\"}";
            if (!content.startsWith("{")) {
                throw new ParseException("Invalid JSON format", 0);
            }
            
            // Simulate validation
            if (!content.contains("name")) {
                throw new ValidationException("Missing required field: name");
            }
            
            return content;
        } catch (IOException | ParseException | ValidationException e) {
            throw e;
        }
    }

    // Exercise 3: Network Client
    public static class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }

    public static class AuthenticationException extends Exception {
        public AuthenticationException(String message) {
            super(message);
        }
    }

    public static String connectToServer(String host, int port) 
            throws IOException, TimeoutException, AuthenticationException {
        try {
            // Simulate connection
            if (host == null) {
                throw new IOException("Invalid host");
            }
            
            // Simulate timeout
            if (port > 1000) {
                throw new TimeoutException("Connection timed out");
            }
            
            // Simulate authentication
            if (host.equals("blocked")) {
                throw new AuthenticationException("Authentication failed");
            }
            
            return "Connected to " + host + ":" + port;
        } catch (IOException | TimeoutException | AuthenticationException e) {
            throw e;
        }
    }

    // Exercise 4: Data Transformer
    public static List<Integer> transformData(List<String> inputs) {
        List<Integer> results = new ArrayList<>();
        for (String input : inputs) {
            try {
                int value = Integer.parseInt(input);
                results.add(value * 2);
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println("Skipping invalid input: " + input);
            }
        }
        return results;
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Safe Parser ---");
        System.out.println("Parse '123': " + safeParseInt("123"));
        System.out.println("Parse 'abc': " + safeParseInt("abc"));
        System.out.println("Parse '3.14': " + safeParseDouble("3.14"));
        System.out.println("Parse 'xyz': " + safeParseDouble("xyz"));

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: File Processor ---");
        try {
            System.out.println("Process 'test.txt': " + processFile("test.txt"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Network Client ---");
        try {
            System.out.println(connectToServer("localhost", 80));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Data Transformer ---");
        List<String> inputs = List.of("1", "2", "abc", "4", "def", "6");
        List<Integer> transformed = transformData(inputs);
        System.out.println("Transformed: " + transformed);
    }
}
