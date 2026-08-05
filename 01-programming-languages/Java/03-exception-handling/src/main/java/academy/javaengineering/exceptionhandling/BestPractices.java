package academy.javaengineering.exceptionhandling;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Demonstrates exception handling best practices.
 *
 * <p>This class covers industry-standard practices for exception handling
 * including specific exception handling, logging, and recovery patterns.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Specific exception handling</li>
 *   <li>Exception logging</li>
 *   <li>Resource cleanup</li>
 *   <li>Fail-fast patterns</li>
 *   <li>Exception recovery</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class BestPractices {

    private static final Logger logger = Logger.getLogger(BestPractices.class.getName());

    /**
     * Demonstrates exception handling best practices.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Specific exception handling
        specificExceptionHandling();

        // Don't catch generic exceptions
        dontCatchGeneric();

        // Fail fast pattern
        failFastPattern();

        // Exception recovery
        exceptionRecovery();
    }

    /**
     * Demonstrates catching specific exceptions instead of generic ones.
     */
    public static void specificExceptionHandling() {
        System.out.println("=== Specific Exception Handling ===");
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);

        try {
            Integer value = map.get("key");
            int result = value / 0;
        } catch (NullPointerException e) {
            System.out.println("Null pointer: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic error: " + e.getMessage());
        }
        // Expected output:
        // === Specific Exception Handling ===
        // Arithmetic error: / by zero
    }

    /**
     * Demonstrates avoiding catching generic Exception or Throwable.
     */
    public static void dontCatchGeneric() {
        System.out.println("\n=== Don't Catch Generic ===");
        try {
            riskyOperation();
        } catch (SpecificException e) {
            // Handle specific expected exception
            System.out.println("Expected exception: " + e.getMessage());
        }
        // Expected output:
        // === Don't Catch Generic ===
        // Expected exception: Expected failure
    }

    /**
     * Demonstrates fail-fast validation pattern.
     *
     * @param name the name
     * @param email the email
     * @param age the age
     * @return true if valid
     * @throws IllegalArgumentException if validation fails
     */
    public static boolean validateUser(String name, String email, int age) {
        System.out.println("\n=== Fail Fast Pattern ===");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Invalid age: " + age);
        }
        System.out.println("Validation passed for: " + name);
        return true;
    }

    /**
     * Demonstrates exception recovery pattern.
     */
    public static void exceptionRecovery() {
        System.out.println("\n=== Exception Recovery ===");
        int retryCount = 0;
        int maxRetries = 3;

        while (retryCount < maxRetries) {
            try {
                riskyOperation();
                System.out.println("Operation succeeded");
                break;
            } catch (SpecificException e) {
                retryCount++;
                System.out.println("Retry " + retryCount + ": " + e.getMessage());
                if (retryCount >= maxRetries) {
                    System.out.println("Max retries reached, giving up");
                }
            }
        }
        // Expected output:
        // === Exception Recovery ===
        // Retry 1: Expected failure
        // Retry 2: Expected failure
        // Retry 3: Expected failure
        // Max retries reached, giving up
    }

    /**
     * Simulates a risky operation.
     */
    public static void riskyOperation() {
        throw new SpecificException("Expected failure");
    }

    /**
     * Demonstrates fail-fast pattern main method.
     */
    public static void failFastPattern() {
        try {
            validateUser(null, "test@example.com", 25);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }

        try {
            validateUser("John", "invalid-email", 25);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
        // Expected output:
        // === Fail Fast Pattern ===
        // Validation failed: Name cannot be empty
        // Validation failed: Invalid email format
    }

    /**
     * Custom specific exception for examples.
     */
    public static class SpecificException extends RuntimeException {

        /**
         * Constructs a SpecificException.
         *
         * @param message the error message
         */
        public SpecificException(String message) {
            super(message);
        }
    }
}
