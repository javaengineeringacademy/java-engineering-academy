package academy.javaengineering.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates SLF4J logging facade.
 * SLF4J provides a simple facade for various logging frameworks.
 */
public class Slf4jLoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jLoggingExample.class);

    public static void main(String[] args) {
        demonstrateBasicLogging();
        demonstrateParameterizedLogging();
        demonstrateExceptionLogging();
        demonstrateMarkerLogging();
    }

    private static void demonstrateBasicLogging() {
        logger.error("This is an error message");
        logger.warn("This is a warning message");
        logger.info("This is an info message");
        logger.debug("This is a debug message");
        logger.trace("This is a trace message");
    }

    private static void demonstrateParameterizedLogging() {
        String userId = "user123";
        int attemptCount = 3;
        
        logger.info("Login attempt for user: {}", userId);
        logger.warn("Failed login attempt {} for user: {}", attemptCount, userId);
        logger.debug("Processing request with parameters: userId={}, attempts={}", userId, attemptCount);
    }

    private static void demonstrateExceptionLogging() {
        try {
            String data = null;
            data.length();
        } catch (NullPointerException e) {
            logger.error("Null pointer exception occurred", e);
            logger.error("Error processing request: {}", e.getMessage());
        }
    }

    private static void demonstrateMarkerLogging() {
        org.slf4j.Marker auditMarker = org.slf4j.MarkerFactory.getMarker("AUDIT");
        logger.info(auditMarker, "User performed sensitive operation");
        
        org.slf4j.Marker errorMarker = org.slf4j.MarkerFactory.getMarker("ERROR");
        logger.error(errorMarker, "Critical system error");
    }
}
