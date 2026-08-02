package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates Logback configuration and advanced features.
 * Logback is the default implementation for SLF4J.
 */
public class LogbackExample {

    private static final Logger logger = LoggerFactory.getLogger(LogbackExample.class);

    public static void main(String[] args) {
        demonstrateStructuredLogging();
        demonstratePerformanceLogging();
        demonstrateContextualLogging();
    }

    private static void demonstrateStructuredLogging() {
        logger.info("Structured logging example");
        logger.info("User action: {}", new Object[]{"login", "success", "192.168.1.1"});
    }

    private static void demonstratePerformanceLogging() {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 1000; i++) {
            logger.debug("Processing item: {}", i);
        }
        
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Processed 1000 items in {}ms", duration);
    }

    private static void demonstrateContextualLogging() {
        org.slf4j.MDC.put("userId", "user123");
        org.slf4j.MDC.put("requestId", "req-456");
        
        try {
            logger.info("Processing request");
            logger.debug("Validating input");
            logger.info("Request completed successfully");
        } finally {
            org.slf4j.MDC.clear();
        }
    }
}
