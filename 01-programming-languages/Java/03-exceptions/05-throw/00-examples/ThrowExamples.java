package academy.javaengineering.exceptions.throwstatement;

/**
 * Demonstrates various uses of the throw keyword in Java.
 *
 * <p>Complexity: O(1) per method — each demonstrates a specific throw pattern
 * with negligible computational cost.</p>
 *
 * <p>Thread-Safety: Yes — all methods are stateless and operate only on parameters.</p>
 *
 * <p>Key Characteristics:
 * <ul>
 *   <li>throw statement transfers control to the nearest enclosing catch block</li>
 *   <li>Checked exceptions must be declared or caught at the call site</li>
 *   <li>Unchecked exceptions (RuntimeException) have no compile-time enforcement</li>
 *   <li>Exception chaining preserves the original cause for debugging</li>
 *   <li>Custom exceptions extend Exception or RuntimeException for domain-specific errors</li>
 * </ul>
 */
public class ThrowExamples {

    public static void main(String[] args) {
        System.out.println("=== Throw Examples ===\n");

        try {
            throwCheckedException(10);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught checked-style: " + e.getMessage());
        }

        try {
            throwUncheckedException(-1);
        } catch (ArithmeticException e) {
            System.out.println("Caught unchecked: " + e.getMessage());
        }

        try {
            throwRethrow(5);
        } catch (RuntimeException e) {
            System.out.println("Caught rethrown: " + e.getMessage());
        }

        try {
            throwWithChaining("user_id");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught chained: " + e.getMessage());
            System.out.println("  Cause: " + e.getCause());
        }

        try {
            throwCustomException("ADMIN");
        } catch (InvalidRoleException e) {
            System.out.println("Caught custom: " + e.getMessage());
        }
    }

    /**
     * Demonstrates throwing a checked exception (IllegalArgumentException is unchecked,
     * but this pattern applies to checked exceptions like IOException).
     */
    public static void throwCheckedException(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Value must be between 0 and 100, got: " + value);
        }
        System.out.println("Valid value: " + value);
    }

    /**
     * Demonstrates throwing an unchecked exception (RuntimeException subclass).
     */
    public static void throwUncheckedException(int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        System.out.println("Result: " + (100 / divisor));
    }

    /**
     * Demonstrates rethrowing an exception, possibly wrapping it.
     */
    public static void throwRethrow(int input) {
        try {
            validatePositive(input);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Validation failed during processing", e);
        }
    }

    /**
     * Demonstrates exception chaining — preserving the original cause.
     */
    public static void throwWithChaining(String input) {
        try {
            parseNumber(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid input provided: " + input, e);
        }
    }

    /**
     * Demonstrates throwing a custom checked exception.
     */
    public static void throwCustomException(String role) throws InvalidRoleException {
        if (role == null || role.isBlank()) {
            throw new InvalidRoleException("Role must not be null or blank");
        }
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new InvalidRoleException("Unknown role: " + role);
        }
        System.out.println("Valid role: " + role);
    }

    private static void validatePositive(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("Expected positive value, got: " + value);
        }
    }

    private static void parseNumber(String s) {
        Integer.parseInt(s);
    }
}
