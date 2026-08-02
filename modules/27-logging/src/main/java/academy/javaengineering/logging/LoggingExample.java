package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.UUID;

/**
 * Comprehensive logging example demonstrating SLF4J, Logback, MDC, and Markers.
 */
public class LoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(LoggingExample.class);
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private static final Marker AUDIT_MARKER = MarkerFactory.getMarker("AUDIT");
    private static final Marker PERFORMANCE_MARKER = MarkerFactory.getMarker("PERFORMANCE");

    public static void main(String[] args) {
        System.out.println("=== Logging Example ===\n");
        
        basicLogging();
        parameterizedLogging();
        mdcExample();
        markerExample();
        exceptionLogging();
        performanceLogging();
        sensitiveDataMasking();
        
        System.out.println("\n=== Examples Complete ===");
    }

    private static void basicLogging() {
        System.out.println("\n--- Basic Logging ---");
        
        logger.trace("TRACE: Detailed debug information");
        logger.debug("DEBUG: Debug information for developers");
        logger.info("INFO: General information about application flow");
        logger.warn("WARN: Warning about potential issues");
        logger.error("ERROR: Error occurred in the application");
    }

    private static void parameterizedLogging() {
        System.out.println("\n--- Parameterized Logging ---");
        
        String userId = "USER-12345";
        int itemCount = 15;
        double total = 299.99;
        
        // Single parameter
        logger.info("Processing user: {}", userId);
        
        // Multiple parameters
        logger.info("User {} has {} items totaling ${}", userId, itemCount, total);
        
        // Performance: lazy evaluation
        logger.debug("User details: {}", () -> expensiveToString(userId));
    }

    private static void mdcExample() {
        System.out.println("\n--- MDC (Mapped Diagnostic Context) ---");
        
        String requestId = UUID.randomUUID().toString();
        String userId = "USER-67890";
        String sessionId = "SESSION-" + System.currentTimeMillis();
        
        try {
            // Set MDC values
            MDC.put("requestId", requestId);
            MDC.put("userId", userId);
            MDC.put("sessionId", sessionId);
            
            // These logs will include MDC values automatically
            logger.info("Starting request processing");
            logger.debug("User authenticated");
            logger.info("Request completed successfully");
            
        } finally {
            // Always clear MDC in finally block
            MDC.clear();
        }
    }

    private static void markerExample() {
        System.out.println("\n--- Markers ---");
        
        String orderId = "ORDER-" + System.currentTimeMillis();
        String customerId = "CUST-123";
        
        // Audit marker
        logger.info(AUDIT_MARKER, "User {} placed order {}", customerId, orderId);
        
        // Performance marker
        long startTime = System.currentTimeMillis();
        simulateProcessing();
        long duration = System.currentTimeMillis() - startTime;
        
        logger.info(PERFORMANCE_MARKER, "Order processing completed in {}ms", duration);
        
        // Composite markers
        Marker compositeMarker = MarkerFactory.getMarker("SECURITY_AUDIT");
        compositeMarker.add(AUDIT_MARKER);
        logger.warn(compositeMarker, "Suspicious login attempt from IP: {}", "192.168.1.100");
    }

    private static void exceptionLogging() {
        System.out.println("\n--- Exception Logging ---");
        
        try {
            processOrder("INVALID-ORDER");
        } catch (Exception e) {
            // Log with exception - includes stack trace
            logger.error("Failed to process order: {}", e.getMessage(), e);
            
            // Log with marker and exception
            logger.error(AUDIT_MARKER, "Order processing failed for system: {}", "ORDER-SERVICE", e);
        }
    }

    private static void performanceLogging() {
        System.out.println("\n--- Performance Logging ---");
        
        long startTime = System.currentTimeMillis();
        
        // Simulate work
        simulateProcessing();
        
        long duration = System.currentTimeMillis() - startTime;
        
        // Log performance metrics
        logger.info(PERFORMANCE_MARKER, "Operation completed in {}ms", duration);
        
        // Conditional logging for performance
        if (logger.isDebugEnabled()) {
            logger.debug("Performance details: {}", getPerformanceMetrics());
        }
    }

    private static void sensitiveDataMasking() {
        System.out.println("\n--- Sensitive Data Masking ---");
        
        String creditCard = "4111-1111-1111-1111";
        String ssn = "123-45-6789";
        String email = "user@example.com";
        
        // Mask sensitive data before logging
        logger.info("Payment processed for card: {}", maskSensitiveData(creditCard));
        logger.info("User SSN: {}", maskSensitiveData(ssn));
        logger.info("User email: {}", email);
    }

    // Helper methods
    private static String expensiveToString(String userId) {
        // Simulate expensive operation
        return "UserDetails{id=" + userId + ", name='John Doe', email='john@example.com'}";
    }

    private static void simulateProcessing() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void processOrder(String orderId) {
        if ("INVALID-ORDER".equals(orderId)) {
            throw new IllegalArgumentException("Invalid order ID: " + orderId);
        }
        logger.info("Processing order: {}", orderId);
    }

    private static String getPerformanceMetrics() {
        return String.format("CPU: %.1f%%, Memory: %dMB, Threads: %d", 
            45.2, Runtime.getRuntime().freeMemory() / 1024 / 1024, 
            Thread.activeCount());
    }

    private static String maskSensitiveData(String data) {
        if (data == null || data.length() < 4) {
            return "****";
        }
        return "*".repeat(data.length() - 4) + data.substring(data.length() - 4);
    }
}