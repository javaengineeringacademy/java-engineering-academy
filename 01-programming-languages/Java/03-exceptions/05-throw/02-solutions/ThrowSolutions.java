/**
 * Complete solutions for the throw keyword exercises.
 *
 * <p>Complexity: O(1) per method — each solution focuses on a specific throw pattern.</p>
 *
 * <p>Thread-Safety: Yes — all methods are stateless and operate only on parameters.</p>
 *
 * <p>Key Characteristics:
 * <ul>
 *   <li>Demonstrates idiomatic throw usage for checked, unchecked, rethrown, chained, and custom exceptions</li>
 *   <li>Each method validates inputs and throws the appropriate exception type</li>
 *   <li>Exception chaining preserves the full cause stack trace for debugging</li>
 * </ul>
 */
public class ThrowSolutions {

    public static void main(String[] args) {
        System.out.println("=== Throw Solutions ===\n");

        // Exercise 1
        try {
            exercise1_ThrowChecked(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Ex1: " + e.getMessage());
        }

        // Exercise 2
        try {
            exercise2_ThrowUnchecked(new String[]{});
        } catch (IllegalArgumentException e) {
            System.out.println("Ex2: " + e.getMessage());
        }

        // Exercise 3
        try {
            exercise3_RethrowWithCause("abc");
        } catch (RuntimeException e) {
            System.out.println("Ex3: " + e.getMessage());
            System.out.println("  Cause: " + e.getCause());
        }

        // Exercise 4
        try {
            exercise4_ExceptionChaining("missing");
        } catch (IllegalStateException e) {
            System.out.println("Ex4: " + e.getMessage());
            System.out.println("  Cause: " + e.getCause());
        }

        // Exercise 5
        try {
            exercise5_CustomException(200);
        } catch (InvalidAgeException e) {
            System.out.println("Ex5: " + e.getMessage());
        }
    }

    /**
     * Solution 1: Throw a checked exception for null/empty input.
     */
    public static void exercise1_ThrowChecked(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input must not be null or empty");
        }
        System.out.println("Uppercased: " + input.toUpperCase());
    }

    /**
     * Solution 2: Throw unchecked exceptions for null/empty arrays.
     */
    public static String exercise2_ThrowUnchecked(String[] array) {
        if (array == null) {
            throw new NullPointerException("Array reference is null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        return array[0];
    }

    /**
     * Solution 3: Rethrow a NumberFormatException as RuntimeException with cause.
     */
    public static int exercise3_RethrowWithCause(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Failed to parse integer", e);
        }
    }

    /**
     * Solution 4: Exception chaining with validation and simulated lookup failure.
     */
    public static String exercise4_ExceptionChaining(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }

        try {
            // Simulated lookup that throws if userId == "missing"
            return simulateUserLookup(userId);
        } catch (RuntimeException e) {
            throw new IllegalStateException("User lookup failed for id: " + userId, e);
        }
    }

    /**
     * Solution 5: Throw a custom exception for invalid age.
     */
    public static boolean exercise5_CustomException(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException("Invalid age: " + age);
        }
        return true;
    }

    private static String simulateUserLookup(String userId) {
        if ("missing".equals(userId)) {
            throw new RuntimeException("User not found in database");
        }
        return "User-" + userId;
    }

    /**
     * Custom exception for exercise 5.
     */
    public static class InvalidAgeException extends Exception {
        public InvalidAgeException(String message) {
            super(message);
        }
    }
}
