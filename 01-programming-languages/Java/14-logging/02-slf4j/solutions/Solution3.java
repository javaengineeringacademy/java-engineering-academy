package academy.javaengineering.logging.slf4j.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Solution 3: Logging utility with markers.
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);

    private static final Marker AUDIT = MarkerFactory.getMarker("AUDIT");
    private static final Marker SECURITY = MarkerFactory.getMarker("SECURITY");
    private static final Marker PERFORMANCE = MarkerFactory.getMarker("PERFORMANCE");

    // Audit methods
    public void logUserAction(String userId, String action, String resource) {
        logger.info(AUDIT, "User={} performed {} on {}", userId, action, resource);
    }

    public void logDataAccess(String userId, String dataType, String operation) {
        logger.info(AUDIT, "Data access: user={}, type={}, operation={}", userId, dataType, operation);
    }

    // Security methods
    public void logLoginAttempt(String username, boolean success, String sourceIp) {
        if (success) {
            logger.info(SECURITY, "Login success: user={}, ip={}", username, sourceIp);
        } else {
            logger.warn(SECURITY, "Login failure: user={}, ip={}", username, sourceIp);
        }
    }

    public void logPrivilegeEscalation(String userId, String fromRole, String toRole) {
        logger.warn(SECURITY, "Privilege escalation: user={}, from={}, to={}", userId, fromRole, toRole);
    }

    // Performance methods
    public void logSlowQuery(String query, long durationMs, long threshold) {
        if (durationMs > threshold) {
            logger.warn(PERFORMANCE, "Slow query ({}ms > {}ms): {}", durationMs, threshold, query);
        } else {
            logger.debug(PERFORMANCE, "Query executed in {}ms: {}", durationMs, query);
        }
    }

    public void logCacheOperation(String operation, boolean hit, long durationMs) {
        if (hit) {
            logger.debug(PERFORMANCE, "Cache HIT: operation={}, duration={}ms", operation, durationMs);
        } else {
            logger.debug(PERFORMANCE, "Cache MISS: operation={}, duration={}ms", operation, durationMs);
        }
    }
}
