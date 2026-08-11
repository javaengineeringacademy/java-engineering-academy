package academy.javaengineering.exceptions.production.examples;

import academy.javaengineering.exceptions.production.ProductionPatterns;
import academy.javaengineering.exceptions.production.ProductionPatterns.*;
import java.util.List;

/**
 * Real-world example: API error handling and resilience.
 */
public class ProductionPatternsExample {

    // Simulated external service
    private final CircuitBreaker externalServiceBreaker =
        new CircuitBreaker(3, 2000);
    private final GlobalExceptionHandler exceptionHandler =
        new GlobalExceptionHandler();

    public String callExternalService(String requestId) {
        if (!externalServiceBreaker.allowRequest()) {
            ErrorResponse error = exceptionHandler.handle(
                new ServiceUnavailableException("ExternalAPI"));
            System.out.println("Circuit open: " + error);
            return "fallback-response";
        }

        try {
            String result = performExternalCall(requestId);
            externalServiceBreaker.recordSuccess();
            return result;
        } catch (Exception ex) {
            externalServiceBreaker.recordFailure();
            ErrorResponse error = exceptionHandler.handle(ex);
            System.out.println("Error: " + error);
            return "error-response";
        }
    }

    private String performExternalCall(String requestId) {
        // Simulates occasional failures
        if (System.currentTimeMillis() % 3 == 0) {
            throw new ServiceUnavailableException("ExternalAPI");
        }
        return "data-" + requestId;
    }

    public ErrorResponse handleApiRequest(String userId, String email) {
        try {
            validateRequest(userId, email);
            return processRequest(userId);
        } catch (Exception ex) {
            return exceptionHandler.handle(ex);
        }
    }

    private void validateRequest(String userId, String email) {
        List<String> errors = new java.util.ArrayList<>();
        if (userId == null || userId.isBlank()) {
            errors.add("userId: must not be blank");
        }
        if (email == null || !email.contains("@")) {
            errors.add("email: must be valid");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private ErrorResponse processRequest(String userId) {
        String traceId = TraceIdGenerator.generate();
        System.out.println("Processing request for " + userId
            + " [traceId=" + traceId + "]");
        return new ErrorResponse("SUCCESS",
            "Request processed", traceId, null);
    }

    public static void main(String[] args) {
        ProductionPatternsExample example =
            new ProductionPatternsExample();

        System.out.println("=== Example: API Error Handling ===\n");

        // Valid request
        ErrorResponse result1 = example.handleApiRequest(
            "USR-001", "user@example.com");
        System.out.println("Valid: " + result1);

        // Invalid request
        ErrorResponse result2 = example.handleApiRequest("", "bad-email");
        System.out.println("Invalid: " + result2);

        // External service call
        System.out.println("\n--- External Service Calls ---");
        for (int i = 0; i < 5; i++) {
            String response = example.callExternalService("REQ-" + i);
            System.out.println("Call " + i + ": " + response);
        }
    }
}
