package academy.javaengineering.exceptions.production;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Demonstrates production exception handling patterns.
 */
public class ProductionPatterns {

    // ========================================
    // Custom Exception Hierarchy
    // ========================================

    public static class AppException extends RuntimeException {
        private final String errorCode;
        private final int statusCode;

        public AppException(String errorCode, String message, int statusCode) {
            super(message);
            this.errorCode = errorCode;
            this.statusCode = statusCode;
        }

        public String getErrorCode() { return errorCode; }
        public int getStatusCode() { return statusCode; }
    }

    public static class ResourceNotFoundException extends AppException {
        public ResourceNotFoundException(String resource, String id) {
            super("RESOURCE_NOT_FOUND",
                resource + " with id " + id + " not found", 404);
        }
    }

    public static class ValidationException extends AppException {
        private final List<String> fieldErrors;

        public ValidationException(List<String> fieldErrors) {
            super("VALIDATION_FAILED",
                "Validation failed: " + String.join(", ", fieldErrors), 400);
            this.fieldErrors = List.copyOf(fieldErrors);
        }

        public List<String> getFieldErrors() { return fieldErrors; }
    }

    public static class RateLimitExceededException extends AppException {
        public RateLimitExceededException(int retryAfterSeconds) {
            super("RATE_LIMIT_EXCEEDED",
                "Rate limit exceeded. Retry after " + retryAfterSeconds + "s",
                429);
        }
    }

    public static class ServiceUnavailableException extends AppException {
        public ServiceUnavailableException(String service) {
            super("SERVICE_UNAVAILABLE",
                service + " is currently unavailable", 503);
        }
    }

    // ========================================
    // Structured Error Response
    // ========================================

    public static class ErrorResponse {
        private final String errorCode;
        private final String message;
        private final Instant timestamp;
        private final String traceId;
        private final List<FieldError> fieldErrors;

        public ErrorResponse(String errorCode, String message,
                String traceId, List<FieldError> fieldErrors) {
            this.errorCode = errorCode;
            this.message = message;
            this.timestamp = Instant.now();
            this.traceId = traceId;
            this.fieldErrors = fieldErrors != null
                ? List.copyOf(fieldErrors) : List.of();
        }

        @Override
        public String toString() {
            return String.format(
                "{\"errorCode\":\"%s\",\"message\":\"%s\","
                + "\"timestamp\":\"%s\",\"traceId\":\"%s\"}",
                errorCode, message, timestamp, traceId);
        }

        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public Instant getTimestamp() { return timestamp; }
        public String getTraceId() { return traceId; }
        public List<FieldError> getFieldErrors() { return fieldErrors; }
    }

    public static class FieldError {
        private final String field;
        private final String rejectedValue;
        private final String message;

        public FieldError(String field, String rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }

        public String getField() { return field; }
        public String getRejectedValue() { return rejectedValue; }
        public String getMessage() { return message; }
    }

    // ========================================
    // Trace ID Generator
    // ========================================

    public static class TraceIdGenerator {
        public static String generate() {
            return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 16);
        }
    }

    // ========================================
    // Exception-to-HTTP Status Mapper
    // ========================================

    public static class ExceptionStatusMapper {

        private static final Map<Class<?>, Integer> MAPPING = Map.of(
            ResourceNotFoundException.class, 404,
            ValidationException.class, 400,
            RateLimitExceededException.class, 429,
            ServiceUnavailableException.class, 503
        );

        public static int resolve(Exception ex) {
            return MAPPING.getOrDefault(ex.getClass(), 500);
        }
    }

    // ========================================
    // Global Exception Handler
    // ========================================

    public static class GlobalExceptionHandler {

        public ErrorResponse handle(Exception ex) {
            String traceId = TraceIdGenerator.generate();

            if (ex instanceof ResourceNotFoundException) {
                return new ErrorResponse(
                    ((AppException) ex).getErrorCode(),
                    ex.getMessage(), traceId, null);
            }

            if (ex instanceof ValidationException) {
                ValidationException ve = (ValidationException) ex;
                List<FieldError> fields = ve.getFieldErrors().stream()
                    .map(e -> new FieldError("field", "N/A", e))
                    .toList();
                return new ErrorResponse(
                    ve.getErrorCode(), ve.getMessage(),
                    traceId, fields);
            }

            if (ex instanceof AppException) {
                AppException ae = (AppException) ex;
                return new ErrorResponse(
                    ae.getErrorCode(), ae.getMessage(),
                    traceId, null);
            }

            // Generic fallback - never expose internals
            return new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                traceId, null);
        }
    }

    // ========================================
    // Circuit Breaker (Simplified)
    // ========================================

    public enum CircuitState { CLOSED, OPEN, HALF_OPEN }

    public static class CircuitBreaker {
        private CircuitState state = CircuitState.CLOSED;
        private int failureCount = 0;
        private final int failureThreshold;
        private final long resetDurationMs;
        private long lastFailureTime = 0;

        public CircuitBreaker(int failureThreshold,
                long resetDurationMs) {
            this.failureThreshold = failureThreshold;
            this.resetDurationMs = resetDurationMs;
        }

        public synchronized boolean allowRequest() {
            if (state == CircuitState.CLOSED) {
                return true;
            }
            if (state == CircuitState.OPEN) {
                if (System.currentTimeMillis() - lastFailureTime
                        > resetDurationMs) {
                    state = CircuitState.HALF_OPEN;
                    return true;
                }
                return false;
            }
            return true; // HALF_OPEN allows one request
        }

        public synchronized void recordSuccess() {
            failureCount = 0;
            state = CircuitState.CLOSED;
        }

        public synchronized void recordFailure() {
            failureCount++;
            lastFailureTime = System.currentTimeMillis();
            if (failureCount >= failureThreshold) {
                state = CircuitState.OPEN;
            }
        }

        public CircuitState getState() { return state; }
    }

    // ========================================
    // Retry with Exponential Backoff
    // ========================================

    public static class RetryPolicy {
        private final int maxAttempts;
        private final long initialDelayMs;
        private final double multiplier;

        public RetryPolicy(int maxAttempts, long initialDelayMs,
                double multiplier) {
            this.maxAttempts = maxAttempts;
            this.initialDelayMs = initialDelayMs;
            this.multiplier = multiplier;
        }

        @FunctionalInterface
        public interface RetryableOperation<T> {
            T execute() throws Exception;
        }

        public <T> T execute(RetryableOperation<T> operation)
                throws Exception {
            long delay = initialDelayMs;
            Exception lastException = null;

            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    return operation.execute();
                } catch (Exception ex) {
                    lastException = ex;
                    if (attempt < maxAttempts) {
                        System.out.printf(
                            "Attempt %d failed, retrying in %dms...%n",
                            attempt, delay);
                        Thread.sleep(delay);
                        delay = (long) (delay * multiplier);
                    }
                }
            }
            throw lastException;
        }
    }

    // ========================================
    // Graceful Degradation with Fallback
    // ========================================

    public static class ProductService {
        private final CircuitBreaker circuitBreaker;

        public ProductService(CircuitBreaker circuitBreaker) {
            this.circuitBreaker = circuitBreaker;
        }

        public String getProduct(String id) {
            if (!circuitBreaker.allowRequest()) {
                System.out.println(
                    "Circuit open, returning cached product");
                return getCachedProduct(id);
            }

            try {
                String product = fetchFromPrimaryService(id);
                circuitBreaker.recordSuccess();
                return product;
            } catch (Exception ex) {
                circuitBreaker.recordFailure();
                System.out.println(
                    "Primary failed: " + ex.getMessage());
                return getCachedProduct(id);
            }
        }

        private String fetchFromPrimaryService(String id) {
            if (Math.random() < 0.5) {
                throw new ServiceUnavailableException("ProductService");
            }
            return "Product-" + id;
        }

        private String getCachedProduct(String id) {
            return "CachedProduct-" + id;
        }
    }

    // ========================================
    // Demo
    // ========================================

    public static void main(String[] args) {
        System.out.println("=== Production Exception Patterns ===\n");

        // Demo 1: Structured error response
        System.out.println("--- Structured Error Response ---");
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        try {
            throw new ResourceNotFoundException("User", "USR-456");
        } catch (Exception ex) {
            ErrorResponse response = handler.handle(ex);
            System.out.println(response);
        }

        // Demo 2: Validation error with field details
        System.out.println("\n--- Validation Error ---");
        try {
            throw new ValidationException(List.of(
                "email: must be valid",
                "age: must be positive"));
        } catch (Exception ex) {
            ErrorResponse response = handler.handle(ex);
            System.out.println(response);
        }

        // Demo 3: Generic exception (no internal details)
        System.out.println("\n--- Generic Exception ---");
        try {
            throw new RuntimeException("database connection failed");
        } catch (Exception ex) {
            ErrorResponse response = handler.handle(ex);
            System.out.println(response);
        }

        // Demo 4: Exception status mapping
        System.out.println("\n--- Status Mapping ---");
        System.out.println("ResourceNotFound -> "
            + ExceptionStatusMapper.resolve(
                new ResourceNotFoundException("Order", "123")));
        System.out.println("Unknown -> "
            + ExceptionStatusMapper.resolve(
                new RuntimeException("oops")));

        // Demo 5: Circuit breaker
        System.out.println("\n--- Circuit Breaker ---");
        CircuitBreaker cb = new CircuitBreaker(3, 1000);
        ProductService service = new ProductService(cb);

        for (int i = 0; i < 6; i++) {
            String result = service.getProduct("P-001");
            System.out.printf("Request %d: %s (state: %s)%n",
                i + 1, result, cb.getState());
        }

        // Demo 6: Retry with backoff
        System.out.println("\n--- Retry Pattern ---");
        RetryPolicy retry = new RetryPolicy(3, 500, 2.0);
        try {
            String result = retry.execute(() -> {
                if (Math.random() < 0.7) {
                    throw new RuntimeException("transient error");
                }
                return "success";
            });
            System.out.println("Result: " + result);
        } catch (Exception ex) {
            System.out.println("All retries failed: " + ex.getMessage());
        }

        // Demo 7: Trace ID generation
        System.out.println("\n--- Trace IDs ---");
        for (int i = 0; i < 3; i++) {
            System.out.println("Trace: " + TraceIdGenerator.generate());
        }

        System.out.println("\n=== Done ===");
    }
}
