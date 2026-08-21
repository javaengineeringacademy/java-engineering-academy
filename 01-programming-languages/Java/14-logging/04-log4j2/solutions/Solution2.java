package academy.javaengineering.logging.log4j2.solutions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

/**
 * Solution 2: ThreadContext-based request tracing.
 */
public class Solution2 {

    private static final Logger logger = LogManager.getLogger(Solution2.class);

    public void processRequest(String userId, String sessionId) {
        String traceId = UUID.randomUUID().toString();

        ThreadContext.put("traceId", traceId);
        ThreadContext.put("userId", userId);
        ThreadContext.put("sessionId", sessionId);
        ThreadContext.put("service", "order-service");

        try {
            logger.info("Request started");

            validateInput();
            processData();
            sendResponse();

            logger.info("Request completed successfully");
        } catch (Exception e) {
            logger.error("Request failed: {}", e.getMessage(), e);
        } finally {
            ThreadContext.clearAll();
        }
    }

    private void validateInput() {
        logger.debug("Validating input parameters");
    }

    private void processData() {
        logger.debug("Processing data");
    }

    private void sendResponse() {
        logger.debug("Sending response");
    }
}
