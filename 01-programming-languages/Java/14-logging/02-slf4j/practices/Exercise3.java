package academy.javaengineering.logging.slf4j.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Exercise 3: Implement a logging utility class with markers.
 *
 * Requirements:
 * 1. Create an AuditLogger that uses AUDIT marker
 * 2. Create a SecurityLogger that uses SECURITY marker
 * 3. Create a PerformanceLogger that uses PERFORMANCE marker
 * 4. Each logger should have specific methods for its domain
 * 5. Include proper parameterization and exception handling
 */
public class Exercise3 {

    // TODO: Create loggers with appropriate markers

    /**
     * AuditLogger methods:
     * - logUserAction(userId, action, resource)
     * - logDataAccess(userId, dataType, operation)
     */
    public void logUserAction(String userId, String action, String resource) {
        // TODO: Log with AUDIT marker
    }

    public void logDataAccess(String userId, String dataType, String operation) {
        // TODO: Log with AUDIT marker
    }

    /**
     * SecurityLogger methods:
     * - logLoginAttempt(username, success, sourceIp)
     * - logPrivilegeEscalation(userId, fromRole, toRole)
     */
    public void logLoginAttempt(String username, boolean success, String sourceIp) {
        // TODO: Log with SECURITY marker
    }

    public void logPrivilegeEscalation(String userId, String fromRole, String toRole) {
        // TODO: Log with SECURITY marker
    }

    /**
     * PerformanceLogger methods:
     * - logSlowQuery(query, durationMs, threshold)
     * - logCacheOperation(operation, hit, durationMs)
     */
    public void logSlowQuery(String query, long durationMs, long threshold) {
        // TODO: Log with PERFORMANCE marker, WARN if above threshold
    }

    public void logCacheOperation(String operation, boolean hit, long durationMs) {
        // TODO: Log with PERFORMANCE marker
    }
}
