package academy.javaengineering.exceptions.production.solutions;

import academy.javaengineering.exceptions.production.ProductionPatterns.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Solutions for production exception handling exercises.
 */
public class ProductionPatternsSolutions {

    // ========================================
    // Exercise 1: ExceptionStatusMapper
    // ========================================

    public static int resolveHttpStatus(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return 400;
        }
        if (ex instanceof UnsupportedOperationException) {
            return 501;
        }
        if (ex instanceof SecurityException) {
            return 403;
        }
        return 500;
    }

    // ========================================
    // Exercise 2: ErrorResponse Builder
    // ========================================

    public static ErrorResponse buildErrorResponse(
            Exception ex, String traceId) {
        String errorCode = "INTERNAL_ERROR";
        if (ex instanceof AppException) {
            errorCode = ((AppException) ex).getErrorCode();
        }
        return new ErrorResponse(errorCode,
            ex.getMessage(), traceId, null);
    }

    // ========================================
    // Exercise 3: Circuit Breaker State Check
    // ========================================

    public static String executeWithCircuitBreaker(
            CircuitBreaker cb, String operation) {
        if (cb.allowRequest()) {
            cb.recordSuccess();
            return "executed: " + operation;
        }
        return "blocked: " + operation;
    }

    // ========================================
    // Exercise 4: Retry with Specific Exceptions
    // ========================================

    @FunctionalInterface
    public interface Operation<T> {
        T execute() throws Exception;
    }

    public static <T> T retryOnUnavailable(
            int maxAttempts, Operation<T> op) throws Exception {
        Exception lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return op.execute();
            } catch (ServiceUnavailableException ex) {
                lastException = ex;
                System.out.printf(
                    "Attempt %d failed (retryable), retrying...%n",
                    attempt);
            } catch (Exception ex) {
                throw ex; // Non-retryable, fail immediately
            }
        }
        throw lastException;
    }

    // ========================================
    // Exercise 5: Validation Handler
    // ========================================

    public static void validateUser(
            String name, String email, int age) {
        List<String> errors = new ArrayList<>();

        if (name == null || name.isBlank()) {
            errors.add("name: must not be blank");
        }
        if (email == null || !email.contains("@")) {
            errors.add("email: must be valid");
        }
        if (age < 0) {
            errors.add("age: must not be negative");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    // ========================================
    // Exercise 6: Graceful Fallback
    // ========================================

    public static String getDataWithFallback(String id) {
        try {
            return getFromPrimary(id);
        } catch (Exception e1) {
            System.out.println("Primary failed: " + e1.getMessage());
            try {
                return getFromSecondary(id);
            } catch (Exception e2) {
                System.out.println(
                    "Secondary failed: " + e2.getMessage());
                return "default-" + id;
            }
        }
    }

    private static String getFromPrimary(String id) {
        throw new ServiceUnavailableException("PrimaryDB");
    }

    private static String getFromSecondary(String id) {
        throw new ServiceUnavailableException("SecondaryDB");
    }

    // ========================================
    // Main - verify all solutions
    // ========================================

    public static void main(String[] args) {
        System.out.println("=== Solutions: Production Patterns ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: Status Mapping ---");
        System.out.println("IAE: " + resolveHttpStatus(
            new IllegalArgumentException("bad")));
        System.out.println("OSE: " + resolveHttpStatus(
            new UnsupportedOperationException()));
        System.out.println("Sec: " + resolveHttpStatus(
            new SecurityException()));
        System.out.println("Gen: " + resolveHttpStatus(
            new RuntimeException()));

        // Exercise 2
        System.out.println("\n--- Exercise 2: Error Response ---");
        ErrorResponse resp = buildErrorResponse(
            new RuntimeException("something broke"), "trace-abc");
        System.out.println("Response: " + resp);

        // Exercise 3
        System.out.println("\n--- Exercise 3: Circuit Breaker ---");
        CircuitBreaker cb = new CircuitBreaker(2, 1000);
        for (int i = 0; i < 5; i++) {
            System.out.println("Req " + i + ": "
                + executeWithCircuitBreaker(cb, "op-" + i));
        }

        // Exercise 4
        System.out.println("\n--- Exercise 4: Retry ---");
        try {
            String result = retryOnUnavailable(3, () -> {
                if (Math.random() < 0.5) {
                    throw new ServiceUnavailableException("API");
                }
                return "success";
            });
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Failed: " + e.getMessage());
        }

        // Exercise 5
        System.out.println("\n--- Exercise 5: Validation ---");
        try {
            validateUser("", "bad", -1);
        } catch (ValidationException e) {
            System.out.println("Errors: " + e.getFieldErrors());
        }

        // Exercise 6
        System.out.println("\n--- Exercise 6: Fallback ---");
        System.out.println("Data: " + getDataWithFallback("X-1"));
    }
}
