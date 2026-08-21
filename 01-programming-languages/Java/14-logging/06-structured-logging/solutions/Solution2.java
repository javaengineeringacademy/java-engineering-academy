package academy.javaengineering.logging.structured.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 2: Structured logging utility.
 */
public class Solution2 {

    private static final Logger logger = LoggerFactory.getLogger(Solution2.class);

    public static class StructuredLogHelper {

        public static void logRequest(String method, String path, String userId) {
            MDC.put("requestId", UUID.randomUUID().toString());
            MDC.put("method", method);
            MDC.put("path", path);
            MDC.put("userId", userId);
            MDC.put("startTime", String.valueOf(System.currentTimeMillis()));
            logger.info("Request received");
        }

        public static void logResponse(int statusCode, long durationMs) {
            MDC.put("statusCode", String.valueOf(statusCode));
            MDC.put("durationMs", String.valueOf(durationMs));
            logger.info("Response sent");
            MDC.clear();
        }

        public static void logError(String errorCode, String message, Exception e) {
            MDC.put("errorCode", errorCode);
            if (e != null) {
                logger.error("{}: {}", message, e.getMessage(), e);
            } else {
                logger.error(message);
            }
        }

        public static void logBusinessEvent(String eventType, String data) {
            MDC.put("eventType", eventType);
            MDC.put("eventData", data);
            logger.info("Business event: {}", eventType);
            MDC.remove("eventType");
            MDC.remove("eventData");
        }
    }

    public static void main(String[] args) {
        StructuredLogHelper.logRequest("POST", "/api/orders", "user-123");
        try {
            StructuredLogHelper.logBusinessEvent("order_created", "orderId=ORD-456");
            StructuredLogHelper.logResponse(201, 150);
        } catch (Exception e) {
            StructuredLogHelper.logError("ERR_001", "Order creation failed", e);
        }
    }
}
