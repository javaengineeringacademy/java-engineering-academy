package academy.javaengineering.logging.slf4j.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class Slf4jMarkerDemo {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jMarkerDemo.class);

    // Define markers for different categories
    private static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");
    private static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
    private static final Marker PERFORMANCE = MarkerFactory.getMarker("PERFORMANCE");

    // Add markers as related markers
    static {
        SECURITY.add(AUDIT); // SECURITY relates to AUDIT
    }

    public static void main(String[] args) {
        auditUserAction("admin", "DELETE", "/api/users/123");
        securityEvent("Failed login attempt", "192.168.1.100");
        performanceMetric("queryExecution", 150);
    }

    private static void auditUserAction(String user, String action, String resource) {
        // Audit marker for compliance logging
        logger.info(AUDIT, "User={} performed {} on {}", user, action, resource);
    }

    private static void securityEvent(String event, String sourceIp) {
        // Security marker for security monitoring
        logger.warn(SECURITY, "Security event: {} from IP={}", event, sourceIp);
    }

    private static void performanceMetric(String operation, long durationMs) {
        // Performance marker for monitoring
        if (durationMs > 100) {
            logger.info(PERFORMANCE, "Slow operation: {} took {}ms", operation, durationMs);
        } else {
            logger.debug(PERFORMANCE, "Operation: {} took {}ms", operation, durationMs);
        }
    }
}
