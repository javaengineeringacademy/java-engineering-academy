package academy.javaengineering.logging.bestpractices.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Example: Demonstrates all best practices in a real application.
 */
public class BestPracticesExample {

    // BEST PRACTICE: private static final with class reference
    private static final Logger logger = LoggerFactory.getLogger(BestPracticesExample.class);

    public void processOrder(String orderId, String userId, double total) {
        // BEST PRACTICE: Use MDC with cleanup
        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("userId", userId);
        MDC.put("orderId", orderId);

        try {
            // BEST PRACTICE: INFO for business operations
            logger.info("Order processing started");

            // BEST PRACTICE: Parameterized logging
            logger.debug("Order details: total={}, userId={}", total, userId);

            // BEST PRACTICE: Guard expensive operations
            if (logger.isTraceEnabled()) {
                String state = computeDetailedState();
                logger.trace("Detailed state: {}", state);
            }

            validateOrder(orderId, total);
            processPayment(orderId, total);

            // BEST PRACTICE: Include context in messages
            logger.info("Order {} completed: total={}", orderId, total);

        } catch (Exception e) {
            // BEST PRACTICE: Exception as last argument
            logger.error("Failed to process order {}: {}", orderId, e.getMessage(), e);
        } finally {
            // BEST PRACTICE: Always clean MDC
            MDC.clear();
        }
    }

    private void validateOrder(String orderId, double total) {
        logger.debug("Validating order {}", orderId);

        if (total <= 0) {
            // BEST PRACTICE: WARN for unexpected conditions
            logger.warn("Invalid total for order {}: {}", orderId, total);
            throw new IllegalArgumentException("Total must be positive");
        }
    }

    private void processPayment(String orderId, double total) {
        logger.info("Processing payment for order {}: {}", orderId, total);
    }

    private String computeDetailedState() {
        return "detailed-state-info";
    }
}
