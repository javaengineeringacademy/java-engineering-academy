package academy.javaengineering.logging.mdc.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 1: Request context manager.
 */
public class Solution1 {

    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    private static final String REQUEST_ID_KEY = "requestId";
    private static final String USER_ID_KEY = "userId";
    private static final String SERVICE_KEY = "service";
    private static final String VERSION_KEY = "version";
    private static final String TIMESTAMP_KEY = "startTime";

    public static void start(String userId, String service, String version) {
        MDC.put(REQUEST_ID_KEY, UUID.randomUUID().toString());
        MDC.put(USER_ID_KEY, userId);
        MDC.put(SERVICE_KEY, service);
        MDC.put(VERSION_KEY, version);
        MDC.put(TIMESTAMP_KEY, String.valueOf(System.currentTimeMillis()));
        logger.info("Request context started");
    }

    public static void update(String key, String value) {
        MDC.put(key, value);
    }

    public static void end() {
        String startTime = MDC.get(TIMESTAMP_KEY);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - Long.parseLong(startTime);
            logger.info("Request context ended, duration={}ms", duration);
        }
        MDC.clear();
    }

    public void processRequest() {
        start("user-123", "order-service", "1.0.0");
        try {
            logger.info("Processing order");
            update("orderId", "ORD-456");
            logger.info("Order processed");
        } finally {
            end();
        }
    }
}
