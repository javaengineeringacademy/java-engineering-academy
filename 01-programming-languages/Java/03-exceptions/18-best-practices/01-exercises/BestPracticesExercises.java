/**
 * Exercises for applying exception handling best practices.
 * <p>
 * Complexity: O(1) per exercise (stub implementations).
 * Thread-safety: Stateless stubs, thread-safe by design.
 * Key characteristics: Each exercise targets a specific anti-pattern,
 * complete the TODO sections to transform bad code into good code.
 */
public class BestPracticesExercises {

    /**
     * Exercise 1: Fix the anti-patterns in the bad code below.
     * Convert empty catches, generic catches, and swallowed exceptions
     * into proper, specific exception handling.
     */
    static void exercise1_FixAntiPatterns() {
        System.out.println("Exercise 1: Fix Anti-Patterns");

        // TODO: Fix the empty catch block — add proper handling and logging
        try {
            String[] items = {"a", "b", "c"};
            int index = Integer.parseInt("not_a_number");
            System.out.println(items[index]);
        } catch (Exception e) {
            // TODO: Don't leave this empty!
            System.out.println("  [TODO] Handle this exception properly");
        }

        // TODO: Replace generic Exception with specific exception types
        try {
            riskyOperation();
        } catch (Exception e) {
            // TODO: Catch the most specific type possible
            System.out.println("  [TODO] Catch specific exception, not generic Exception");
        }

        // TODO: Don't swallow this exception — at minimum log it
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            // TODO: Swallowed exceptions hide bugs — add logging or rethrow
            System.out.println("  [TODO] Log or rethrow, don't swallow");
        }
    }

    /**
     * Exercise 2: Write clear, actionable exception messages.
     * Bad messages: "Error occurred", "Invalid", "Bad input".
     * Good messages include: what was attempted, what was received, and what was expected.
     */
    static void exercise2_WriteGoodMessages() {
        System.out.println("\nExercise 2: Write Good Messages");

        // TODO: Replace vague messages with descriptive ones
        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("  Current message: " + e.getMessage());
            // TODO: Write a better message that tells the user what went wrong
        }

        try {
            validateEmail("not-an-email");
        } catch (IllegalArgumentException e) {
            System.out.println("  Current message: " + e.getMessage());
            // TODO: Write a better message
        }

        try {
            validateUsername("ab");
        } catch (IllegalArgumentException e) {
            System.out.println("  Current message: " + e.getMessage());
            // TODO: Write a better message
        }
    }

    /**
     * Exercise 3: Implement exception translation.
     * Convert low-level exceptions (IOException, SQLException) into
     * meaningful application-level exceptions.
     */
    static void exercise3_ExceptionTranslation() {
        System.out.println("\nExercise 3: Exception Translation");

        try {
            loadUserProfile("user123");
        } catch (Exception e) {
            System.out.println("  Current: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            // TODO: Create a proper UserProfileException wrapping the cause
        }

        try {
            processOrder("order-001");
        } catch (Exception e) {
            System.out.println("  Current: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            // TODO: Create a proper OrderProcessingException wrapping the cause
        }
    }

    /**
     * Exercise 4: Write test cases for exception paths.
     * Ensure that exceptions are thrown when they should be,
     * and not thrown when inputs are valid.
     */
    static void exercise4_TestExceptionPaths() {
        System.out.println("\nExercise 4: Test Exception Paths");

        // TODO: Write assertions that verify:
        // 1. validateAge(-1) throws IllegalArgumentException
        // 2. validateAge(150) throws IllegalArgumentException
        // 3. validateAge(25) does NOT throw
        // 4. divide(10, 0) throws ArithmeticException
        // 5. divide(10, 2) returns 5 and does NOT throw
        // 6. parseInteger("abc") throws NumberFormatException
        // 7. parseInteger("42") returns 42

        System.out.println("  [TODO] Implement test assertions for exception paths");
        System.out.println("  [TODO] Use try-catch to verify exceptions are thrown");
        System.out.println("  [TODO] Verify normal cases do NOT throw");
    }

    /**
     * Exercise 5: Design a global exception handler pattern.
     * Create a handler that catches, logs, and routes exceptions
     * to appropriate handling logic based on exception type.
     */
    static void exercise5_GlobalExceptionHandler() {
        System.out.println("\nExercise 5: Global Exception Handler");

        // TODO: Implement a handleException method that:
        // - Logs the exception with context
        // - For IllegalArgumentException: returns user-friendly error message
        // - For IllegalStateException: alerts monitoring and retries
        // - For RuntimeException: wraps in a service exception
        // - For checked exceptions: translates to runtime exception
        // - Always preserves the original cause chain

        Object[] testCases = {
            new IllegalArgumentException("Invalid input"),
            new IllegalStateException("Service unavailable"),
            new RuntimeException("Unexpected failure"),
            new java.io.IOException("File not found")
        };

        for (Object testCase : testCases) {
            System.out.println("  [TODO] Handle " + testCase.getClass().getSimpleName() + " appropriately");
        }
    }

    // ==================== HELPER METHODS ====================

    static void riskyOperation() {
        throw new RuntimeException("Something went wrong");
    }

    static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Invalid");
        }
    }

    static void validateEmail(String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid");
        }
    }

    static void validateUsername(String username) {
        if (username.length() < 3) {
            throw new IllegalArgumentException("Invalid");
        }
    }

    static String loadUserProfile(String userId) throws java.io.IOException {
        throw new java.io.IOException("Connection refused");
    }

    static void processOrder(String orderId) throws java.sql.SQLException {
        throw new java.sql.SQLException("Timeout");
    }

    static int divide(int a, int b) {
        return a / b;
    }

    static int parseInteger(String s) {
        return Integer.parseInt(s);
    }
}
