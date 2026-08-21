package academy.javaengineering.logging.log4j2.solutions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Solution 3: Custom logging utilities with markers.
 */
public class Solution3 {

    private static final Logger logger = LogManager.getLogger(Solution3.class);
    private static final Logger auditLogger = LogManager.getLogger("AuditLogger");
    private static final Logger securityLogger = LogManager.getLogger("SecurityLogger");
    private static final Logger metricsLogger = LogManager.getLogger("MetricsLogger");

    private static final Marker AUDIT = MarkerManager.getMarker("AUDIT");
    private static final Marker SECURITY = MarkerManager.getMarker("SECURITY");
    private static final Marker METRICS = MarkerManager.getMarker("METRICS");

    // Audit logging
    public void logUserAction(String userId, String action, String resource) {
        auditLogger.info(AUDIT, "user={}, action={}, resource={}", userId, action, resource);
    }

    public void logDataAccess(String userId, String dataType, String operation) {
        auditLogger.info(AUDIT, "user={}, dataType={}, operation={}", userId, dataType, operation);
    }

    // Security logging
    public void logLoginAttempt(String username, boolean success, String sourceIp) {
        if (success) {
            securityLogger.info(SECURITY, "login=success, user={}, ip={}", username, sourceIp);
        } else {
            securityLogger.warn(SECURITY, "login=failure, user={}, ip={}", username, sourceIp);
        }
    }

    public void logPrivilegeEscalation(String userId, String fromRole, String toRole) {
        securityLogger.warn(SECURITY, "user={}, from={}, to={}", userId, fromRole, toRole);
    }

    // Metrics logging
    public void logQueryPerformance(String query, long durationMs, long threshold) {
        if (durationMs > threshold) {
            metricsLogger.warn(METRICS, "query={}, duration={}ms, threshold={}ms (SLOW)",
                    query, durationMs, threshold);
        } else {
            metricsLogger.debug(METRICS, "query={}, duration={}ms", query, durationMs);
        }
    }

    public void logCacheHit(String key, long durationMs) {
        metricsLogger.debug(METRICS, "cache=hit, key={}, duration={}ms", key, durationMs);
    }

    public static void main(String[] args) {
        Solution3 demo = new Solution3();
        demo.logUserAction("admin", "DELETE", "/api/users/123");
        demo.logLoginAttempt("user1", false, "192.168.1.100");
        demo.logQueryPerformance("SELECT * FROM users", 150, 100);
    }
}
