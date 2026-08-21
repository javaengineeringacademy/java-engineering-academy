package academy.javaengineering.logging.logback.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Example: Demonstrates Logback's filter behavior with markers.
 * Shows how different filters can accept/deny events based on criteria.
 */
public class LogbackFilterExample {

    private static final Logger logger = LoggerFactory.getLogger(LogbackFilterExample.class);

    private static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");
    private static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
    private static final Marker DEBUG_VERBOSE = MarkerFactory.getMarker("DEBUG_VERBOSE");

    public static void main(String[] args) {
        logger.info("Normal application log");

        logger.info(AUDIT, "User admin accessed dashboard");

        logger.warn(SECURITY, "Failed login from 192.168.1.100");

        logger.debug(DEBUG_VERBOSE, "Entering method processOrder with args: [order-123, true]");

        logger.info("Application events logged");

        try {
            throw new RuntimeException("Simulated error");
        } catch (Exception e) {
            logger.error("Error occurred during processing", e);
        }
    }
}
