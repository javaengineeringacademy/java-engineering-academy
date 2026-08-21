package academy.javaengineering.logging.logback.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 2: Structured logging with MDC.
 */
public class Solution2 {

    private static final Logger logger = LoggerFactory.getLogger(Solution2.class);

    public void processOrder(String orderId, String userId) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("userId", userId);
        MDC.put("orderId", orderId);

        try {
            logger.info("Order received");

            validateOrder(orderId);
            processPayment(orderId);
            shipOrder(orderId);

            logger.info("Order completed successfully");
        } catch (Exception e) {
            logger.error("Order processing failed: {}", e.getMessage(), e);
        } finally {
            MDC.clear();
        }
    }

    private void validateOrder(String orderId) {
        logger.debug("Validating order {}", orderId);
        // Validation logic
        logger.debug("Order {} validation passed", orderId);
    }

    private void processPayment(String orderId) {
        logger.info("Processing payment for order {}", orderId);
        // Payment logic
        logger.info("Payment processed for order {}", orderId);
    }

    private void shipOrder(String orderId) {
        logger.debug("Preparing shipment for order {}", orderId);
        // Shipping logic
        logger.info("Order {} shipped", orderId);
    }
}
