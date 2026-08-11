package academy.javaengineering.exceptions.production.exercises;

import academy.javaengineering.exceptions.production.ProductionPatterns.*;
import java.util.List;

/**
 * Exercises for production exception handling patterns.
 *
 * Complete each exercise by implementing the method body.
 */
public class ProductionPatternsExercises {

    // ========================================
    // Exercise 1: ExceptionStatusMapper
    // ========================================
    // Extend the mapper to handle these new exceptions:
    //   - IllegalArgumentException -> 400
    //   - UnsupportedOperationException -> 501
    //   - SecurityException -> 403

    public static int resolveHttpStatus(Exception ex) {
        // TODO: Implement status mapping for the three exception types
        // Return 500 for unknown exceptions
        return 0;
    }

    // ========================================
    // Exercise 2: ErrorResponse Builder
    // ========================================
    // Create an ErrorResponse from an exception with traceId.

    public static ErrorResponse buildErrorResponse(
            Exception ex, String traceId) {
        // TODO: Create ErrorResponse using exception details
        // - Use "INTERNAL_ERROR" as default errorCode
        // - Use ex.getMessage() as message
        // - Return the ErrorResponse
        return null;
    }

    // ========================================
    // Exercise 3: Circuit Breaker State Check
    // ========================================
    // Implement a method that checks circuit state before
    // executing an operation.

    public static String executeWithCircuitBreaker(
            CircuitBreaker cb, String operation) {
        // TODO: Check if request is allowed
        // - If allowed, return "executed: " + operation
        // - If not allowed, return "blocked: " + operation
        return null;
    }

    // ========================================
    // Exercise 4: Retry with Specific Exceptions
    // ========================================
    // Implement retry logic that only retries on
    // ServiceUnavailableException.

    @FunctionalInterface
    public interface Operation<T> {
        T execute() throws Exception;
    }

    public static <T> T retryOnUnavailable(
            int maxAttempts, Operation<T> op) throws Exception {
        // TODO: Implement retry logic
        // - Retry only when ServiceUnavailableException is thrown
        // - For other exceptions, throw immediately
        // - Return result on success
        return null;
    }

    // ========================================
    // Exercise 5: Validation Handler
    // ========================================
    // Collect all validation errors and throw
    // a single ValidationException.

    public static void validateUser(String name, String email, int age) {
        // TODO: Collect errors for:
        // - name is null or blank
        // - email is null or doesn't contain '@'
        // - age is negative
        // Throw ValidationException if any errors found
    }

    // ========================================
    // Exercise 6: Graceful Fallback
    // ========================================
    // Implement a method that tries primary source,
    // falls back to secondary, then to default.

    public static String getDataWithFallback(String id) {
        // TODO: Try primary source (may throw)
        // - If primary fails, try secondary (may throw)
        // - If secondary fails, return "default-" + id
        return null;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercises: Production Patterns ===");
        System.out.println("Implement each exercise method.\n");

        // Test Exercise 1
        System.out.println("--- Exercise 1: Status Mapping ---");
        System.out.println("IAE: " + resolveHttpStatus(
            new IllegalArgumentException("bad")));
        System.out.println("OSE: " + resolveHttpStatus(
            new UnsupportedOperationException()));
        System.out.println("Sec: " + resolveHttpStatus(
            new SecurityException()));
        System.out.println("Gen: " + resolveHttpStatus(
            new RuntimeException()));

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Error Response ---");
        ErrorResponse resp = buildErrorResponse(
            new RuntimeException("something broke"), "trace-abc");
        System.out.println("Response: " + resp);

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Circuit Breaker ---");
        CircuitBreaker cb = new CircuitBreaker(2, 1000);
        for (int i = 0; i < 5; i++) {
            System.out.println("Req " + i + ": "
                + executeWithCircuitBreaker(cb, "op-" + i));
        }

        // Test Exercise 4
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

        // Test Exercise 5
        System.out.println("\n--- Exercise 5: Validation ---");
        try {
            validateUser("", "bad", -1);
        } catch (ValidationException e) {
            System.out.println("Errors: " + e.getFieldErrors());
        }

        // Test Exercise 6
        System.out.println("\n--- Exercise 6: Fallback ---");
        System.out.println("Data: " + getDataWithFallback("X-1"));
    }
}
