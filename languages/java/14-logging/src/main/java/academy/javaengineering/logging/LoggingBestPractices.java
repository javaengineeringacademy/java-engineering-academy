package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates logging best practices: parameterized messages, lazy evaluation,
 * exception handling, structured logging, performance considerations, and MDC.
 */
public class LoggingBestPractices {

    private static final Logger logger = LoggerFactory.getLogger(LoggingBestPractices.class);

    public void demonstrateParameterizedMessages() {
        String userId = "USR-12345";
        int retryCount = 3;
        long timeout = 5000L;

        logger.info("User {} attempted login {} times with {}ms timeout", userId, retryCount, timeout);

        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "John Doe");
        userData.put("email", "john@example.com");
        logger.info("User profile updated: {}", userData);

        logger.debug("Processing order {} for customer {} with {} items",
                "ORD-789", "CUST-456", 3);
    }

    public void demonstrateLazyEvaluation() {
        if (logger.isDebugEnabled()) {
            logger.debug("Detailed state: {}", computeExpensiveDebugInfo());
        }

        logger.atDebug()
                .setMessage("Computed value: {}")
                .addArgument(this::computeExpensiveDebugInfo)
                .log();

        if (logger.isTraceEnabled()) {
            logger.trace("Trace with expensive computation: {}", getSystemDiagnostics());
        }
    }

    public void demonstrateExceptionHandling() {
        try {
            processPayment("ORD-123", 99.99);
        } catch (RuntimeException e) {
            logger.error("Payment processing failed for order ORD-123", e);
        }

        try {
            validateInput(null);
        } catch (IllegalArgumentException e) {
            logger.warn("Input validation failed: {}", e.getMessage());
        }

        try {
            connectToService("payment-gateway");
        } catch (java.io.IOException e) {
            logger.error("Service connection failed: service={}, error={}",
                    "payment-gateway", e.getMessage(), e);
        }
    }

    public void demonstrateStructuredLogging() {
        MDC.put("requestId", "REQ-" + System.currentTimeMillis());
        MDC.put("userId", "USR-789");
        MDC.put("service", "order-service");
        MDC.put("operation", "create-order");

        logger.info("Starting order processing");
        logger.debug("Validating order items");
        logger.info("Order ORD-456 created successfully");

        MDC.clear();
    }

    public void demonstratePerformanceConsiderations() {
        String level = "DEBUG";
        logger.info("Logging level: {}", level);

        logger.atInfo()
                .addKeyValue("orderId", "ORD-100")
                .addKeyValue("amount", 250.00)
                .log("Order details");

        if (logger.isDebugEnabled()) {
            logger.debug("Heavy computation result: {}", expensiveCalculation());
        }
    }

    public void demonstrateMDC() {
        MDC.put("correlationId", "CORR-" + java.util.UUID.randomUUID());
        MDC.put("userId", "USR-42");
        MDC.put("sessionId", "SES-abc123");

        logger.info("User action logged with full context");

        MDC.put("phase", "processing");
        logger.debug("Processing step 1 of 3");

        MDC.put("phase", "validation");
        logger.debug("Processing step 2 of 3");

        MDC.put("phase", "completion");
        logger.info("Processing completed");

        MDC.clear();
    }

    public void demonstrateLogMessageStructure() {
        logger.info("USER_LOGIN: user={}, ip={}, success={}", "admin", "192.168.1.1", true);
        logger.info("ORDER_CREATED: orderId={}, items={}, total={}", "ORD-001", 5, 299.99);
        logger.warn("PAYMENT_RETRY: orderId={}, attempt={}, maxRetries={}", "ORD-001", 3, 5);
        logger.error("SYSTEM_ERROR: component={}, code={}, message={}",
                "PaymentService", "PAY_500", "Gateway timeout");
    }

    public void demonstrateSecurityLogging() {
        MDC.put("requestId", "REQ-SEC-" + System.currentTimeMillis());

        logger.info("LOGIN_SUCCESS: user={}, ip={}, method={}", "admin", "10.0.0.1", "password");
        logger.warn("LOGIN_FAILURE: user={}, ip={}, attempts={}", "unknown", "10.0.0.1", 5);
        logger.warn("PRIVILEGE_ESCALATION_ATTEMPT: user={}, targetRole={}", "regular", "admin");

        MDC.clear();
    }

    public void demonstrateContextPreservation() {
        MDC.put("traceId", "trace-" + java.util.UUID.randomUUID());
        MDC.put("spanId", "span-" + java.util.UUID.randomUUID());

        logger.info("Request started");
        logger.debug("External call to payment service");
        logger.info("Request completed successfully");

        MDC.clear();
    }

    private String computeExpensiveDebugInfo() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "ComputedDebugInfo{threads=" + Thread.activeCount() +
                ", memory=" + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB}";
    }

    private String getSystemDiagnostics() {
        return "SystemDiagnostics{cpu=" + Runtime.getRuntime().availableProcessors() +
                ", memory=" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB}";
    }

    private void processPayment(String orderId, double amount) {
        logger.debug("Processing payment for order {}: ${}", orderId, amount);
        if (amount > 1000) {
            throw new RuntimeException("Payment amount exceeds limit");
        }
        logger.info("Payment processed: order={}, amount=${}", orderId, amount);
    }

    private void validateInput(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        logger.debug("Input validated: {}", input);
    }

    private void connectToService(String serviceName) throws java.io.IOException {
        logger.debug("Connecting to service: {}", serviceName);
        throw new java.io.IOException("Connection refused: " + serviceName);
    }

    private double expensiveCalculation() {
        return Math.PI * Math.E * Math.sqrt(2);
    }

    public static void main(String[] args) {
        LoggingBestPractices demo = new LoggingBestPractices();

        System.out.println("=== Logging Best Practices Demo ===");

        System.out.println("\n--- Parameterized Messages ---");
        demo.demonstrateParameterizedMessages();

        System.out.println("\n--- Lazy Evaluation ---");
        demo.demonstrateLazyEvaluation();

        System.out.println("\n--- Exception Handling ---");
        demo.demonstrateExceptionHandling();

        System.out.println("\n--- Structured Logging ---");
        demo.demonstrateStructuredLogging();

        System.out.println("\n--- Performance Considerations ---");
        demo.demonstratePerformanceConsiderations();

        System.out.println("\n--- MDC ---");
        demo.demonstrateMDC();

        System.out.println("\n--- Log Message Structure ---");
        demo.demonstrateLogMessageStructure();

        System.out.println("\n--- Security Logging ---");
        demo.demonstrateSecurityLogging();

        System.out.println("\n--- Context Preservation ---");
        demo.demonstrateContextPreservation();
    }
}
