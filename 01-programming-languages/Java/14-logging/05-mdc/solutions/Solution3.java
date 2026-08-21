package academy.javaengineering.logging.mdc.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 3: Structured logging utility.
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);

    public static class StructuredLogger {
        
        public static void startRequest(String userId, String service) {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("userId", userId);
            MDC.put("service", service);
            MDC.put("version", "1.0.0");
            MDC.put("startTime", String.valueOf(System.currentTimeMillis()));
            logger.info("Request started");
        }

        public static void setBusinessContext(String key, String value) {
            MDC.put(key, value);
        }

        public static void logInfo(String message, Object... args) {
            logger.info(message, args);
        }

        public static void logDebug(String message, Object... args) {
            logger.debug(message, args);
        }

        public static void logWarn(String message, Object... args) {
            logger.warn(message, args);
        }

        public static void logError(String message, Throwable t) {
            logger.error(message, t);
        }

        public static void endRequest() {
            String startTime = MDC.get("startTime");
            if (startTime != null) {
                long duration = System.currentTimeMillis() - Long.parseLong(startTime);
                MDC.put("duration", String.valueOf(duration));
            }
            logger.info("Request completed");
            MDC.clear();
        }
    }

    public static void main(String[] args) {
        StructuredLogger.startRequest("user-123", "order-service");
        try {
            StructuredLogger.setBusinessContext("orderId", "ORD-456");
            StructuredLogger.logInfo("Processing order {}", "ORD-456");
            
            StructuredLogger.setBusinessContext("step", "validation");
            StructuredLogger.logDebug("Validating order");
            
            StructuredLogger.setBusinessContext("step", "payment");
            StructuredLogger.logInfo("Processing payment");
            
            StructuredLogger.logInfo("Order completed");
        } catch (Exception e) {
            StructuredLogger.logError("Order failed", e);
        } finally {
            StructuredLogger.endRequest();
        }
    }
}
