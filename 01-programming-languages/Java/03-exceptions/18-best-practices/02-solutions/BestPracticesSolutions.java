/**
 * Complete solutions for all 5 best practices exercises.
 * <p>
 * Complexity: O(1) per exercise implementation.
 * Thread-safety: Stateless utility methods, thread-safe.
 * Key characteristics: Demonstrates fixed anti-patterns, good messages,
 * exception translation, path testing, and global handler pattern.
 */
public class BestPracticesSolutions {

    public static void main(String[] args) {
        exercise1_FixAntiPatterns();
        exercise2_WriteGoodMessages();
        exercise3_ExceptionTranslation();
        exercise4_TestExceptionPaths();
        exercise5_GlobalExceptionHandler();
    }

    // ==================== EXERCISE 1: FIX ANTI-PATTERNS ====================

    static void exercise1_FixAntiPatterns() {
        System.out.println("Exercise 1: Fix Anti-Patterns - SOLUTION");

        // FIXED: Empty catch -> proper handling with logging
        try {
            String[] items = {"a", "b", "c"};
            int index = Integer.parseInt("not_a_number");
            System.out.println(items[index]);
        } catch (NumberFormatException e) {
            System.out.println("  Fixed: Caught NumberFormatException - " + e.getMessage());
        }

        // FIXED: Generic Exception -> specific exception types
        try {
            riskyOperation();
        } catch (NullPointerException e) {
            System.out.println("  Fixed: Caught NPE specifically - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("  Fixed: Caught IllegalArgument specifically - " + e.getMessage());
        }

        // FIXED: Swallowed exception -> logged and handled
        try {
            riskyOperation();
        } catch (RuntimeException e) {
            System.out.println("  Fixed: Logged warning - " + e.getMessage());
        }

        System.out.println();
    }

    // ==================== EXERCISE 2: GOOD MESSAGES ====================

    static void exercise2_WriteGoodMessages() {
        System.out.println("Exercise 2: Write Good Messages - SOLUTION");

        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("  Fixed message: " + e.getMessage());
        }

        try {
            validateEmail("not-an-email");
        } catch (IllegalArgumentException e) {
            System.out.println("  Fixed message: " + e.getMessage());
        }

        try {
            validateUsername("ab");
        } catch (IllegalArgumentException e) {
            System.out.println("  Fixed message: " + e.getMessage());
        }

        System.out.println();
    }

    // ==================== EXERCISE 3: EXCEPTION TRANSLATION ====================

    static void exercise3_ExceptionTranslation() {
        System.out.println("Exercise 3: Exception Translation - SOLUTION");

        try {
            loadUserProfile("user123");
        } catch (UserProfileException e) {
            System.out.println("  Translated: " + e.getClass().getSimpleName()
                + " - " + e.getMessage());
            System.out.println("  Cause preserved: " + e.getCause().getClass().getSimpleName());
        }

        try {
            processOrder("order-001");
        } catch (OrderProcessingException e) {
            System.out.println("  Translated: " + e.getClass().getSimpleName()
                + " - " + e.getMessage());
            System.out.println("  Cause preserved: " + e.getCause().getClass().getSimpleName());
        }

        System.out.println();
    }

    // ==================== EXERCISE 4: TEST EXCEPTION PATHS ====================

    static void exercise4_TestExceptionPaths() {
        System.out.println("Exercise 4: Test Exception Paths - SOLUTION");

        int passed = 0;
        int failed = 0;

        try {
            validateAge(-1);
            System.out.println("  FAIL: validateAge(-1) did not throw");
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.println("  PASS: validateAge(-1) threw IllegalArgumentException");
            passed++;
        }

        try {
            validateAge(150);
            System.out.println("  FAIL: validateAge(150) did not throw");
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.println("  PASS: validateAge(150) threw IllegalArgumentException");
            passed++;
        }

        try {
            validateAge(25);
            System.out.println("  PASS: validateAge(25) did not throw");
            passed++;
        } catch (Exception e) {
            System.out.println("  FAIL: validateAge(25) threw " + e.getClass().getSimpleName());
            failed++;
        }

        try {
            divide(10, 0);
            System.out.println("  FAIL: divide(10, 0) did not throw");
            failed++;
        } catch (ArithmeticException e) {
            System.out.println("  PASS: divide(10, 0) threw ArithmeticException");
            passed++;
        }

        try {
            int result = divide(10, 2);
            if (result == 5) {
                System.out.println("  PASS: divide(10, 2) returned 5");
                passed++;
            } else {
                System.out.println("  FAIL: divide(10, 2) returned " + result);
                failed++;
            }
        } catch (Exception e) {
            System.out.println("  FAIL: divide(10, 2) threw " + e.getClass().getSimpleName());
            failed++;
        }

        try {
            parseInteger("abc");
            System.out.println("  FAIL: parseInteger(\"abc\") did not throw");
            failed++;
        } catch (NumberFormatException e) {
            System.out.println("  PASS: parseInteger(\"abc\") threw NumberFormatException");
            passed++;
        }

        try {
            int result = parseInteger("42");
            if (result == 42) {
                System.out.println("  PASS: parseInteger(\"42\") returned 42");
                passed++;
            } else {
                System.out.println("  FAIL: parseInteger(\"42\") returned " + result);
                failed++;
            }
        } catch (Exception e) {
            System.out.println("  FAIL: parseInteger(\"42\") threw " + e.getClass().getSimpleName());
            failed++;
        }

        System.out.println("  Results: " + passed + " passed, " + failed + " failed");
        System.out.println();
    }

    // ==================== EXERCISE 5: GLOBAL EXCEPTION HANDLER ====================

    static void exercise5_GlobalExceptionHandler() {
        System.out.println("Exercise 5: Global Exception Handler - SOLUTION");

        Object[] testCases = {
            new IllegalArgumentException("Invalid input"),
            new IllegalStateException("Service unavailable"),
            new RuntimeException("Unexpected failure"),
            new java.io.IOException("File not found")
        };

        for (Object testCase : testCases) {
            handleException((Exception) testCase);
        }

        System.out.println();
    }

    static void handleException(Exception e) {
        if (e instanceof IllegalArgumentException) {
            System.out.println("  [USER_ERROR] " + e.getMessage());
        } else if (e instanceof IllegalStateException) {
            System.out.println("  [ALERT] Monitoring notified, scheduling retry: " + e.getMessage());
        } else if (e instanceof RuntimeException) {
            System.out.println("  [SERVICE_ERROR] Wrapped in ServiceException: " + e.getMessage());
        } else {
            System.out.println("  [TRANSLATED] Checked exception wrapped as RuntimeException: "
                + e.getMessage());
            System.out.println("  [CAUSE] Original: " + e.getClass().getName());
        }
    }

    // ==================== HELPER METHODS ====================

    static void riskyOperation() {
        throw new RuntimeException("Something went wrong");
    }

    static void validateAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException(
                "Age must be between 0 and 150, but got: " + age);
        }
    }

    static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException(
                "Email must contain '@', but got: \"" + email + "\"");
        }
    }

    static void validateUsername(String username) {
        if (username == null || username.length() < 3) {
            throw new IllegalArgumentException(
                "Username must be at least 3 characters, but got " + username.length()
                + " chars: \"" + username + "\"");
        }
    }

    static int divide(int a, int b) {
        return a / b;
    }

    static int parseInteger(String s) {
        return Integer.parseInt(s);
    }

    static String loadUserProfile(String userId) throws UserProfileException {
        try {
            throw new java.io.IOException("Connection refused to database");
        } catch (java.io.IOException e) {
            throw new UserProfileException(
                "Failed to load profile for user: " + userId, e);
        }
    }

    static void processOrder(String orderId) throws OrderProcessingException {
        try {
            throw new java.sql.SQLException("Statement timeout after 30s");
        } catch (java.sql.SQLException e) {
            throw new OrderProcessingException(
                "Failed to process order: " + orderId, e);
        }
    }

    // ==================== CUSTOM EXCEPTION TYPES ====================

    static class UserProfileException extends Exception {
        UserProfileException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    static class OrderProcessingException extends Exception {
        OrderProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
