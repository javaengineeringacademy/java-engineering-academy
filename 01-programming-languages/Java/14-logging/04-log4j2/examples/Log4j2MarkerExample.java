package academy.javaengineering.logging.log4j2.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Example: Log4j 2 Markers for structured logging.
 */
public class Log4j2MarkerExample {

    private static final Logger logger = LogManager.getLogger(Log4j2MarkerExample.class);

    // Markers in Log4j 2
    private static final Marker AUDIT = MarkerManager.getMarker("AUDIT");
    private static final Marker SECURITY = MarkerManager.getMarker("SECURITY");
    private static final Marker PERFORMANCE = MarkerManager.getMarker("PERFORMANCE");

    static {
        SECURITY.addParents(AUDIT); // SECURITY is child of AUDIT
    }

    public static void main(String[] args) {
        logger.info(AUDIT, "User admin accessed dashboard");
        logger.warn(SECURITY, "Failed login from 192.168.1.100");
        logger.info(PERFORMANCE, "Query executed in 150ms");

        try {
            throw new RuntimeException("Test error");
        } catch (Exception e) {
            logger.error(SECURITY, "Security violation detected", e);
        }
    }
}
