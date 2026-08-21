package academy.javaengineering.logging.bestpractices.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 3: Comprehensive logging style guide with examples.
 *
 * LOGGER DECLARATION
 * ==================
 * CORRECT:
 *   private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
 *
 * WRONG:
 *   private Logger logger = LoggerFactory.getLogger(getClass());
 *   public static Logger logger = LoggerFactory.getLogger("myapp");
 *
 * PARAMETERIZED LOGGING
 * =====================
 * CORRECT:
 *   logger.debug("User {} logged in from {}", username, ipAddress);
 *   logger.error("Failed to process order {}: {}", orderId, e.getMessage(), e);
 *
 * WRONG:
 *   logger.debug("User " + username + " logged in from " + ipAddress);
 *   logger.debug(String.format("User %s logged in", username));
 *
 * EXCEPTION LOGGING
 * =================
 * CORRECT:
 *   logger.error("Failed to process order", exception);
 *   logger.error("Failed to process order {}: {}", orderId, e.getMessage(), e);
 *
 * WRONG:
 *   logger.error("Failed: " + e.getMessage());
 *   logger.error(e.toString());
 *
 * MDC USAGE
 * =========
 * CORRECT:
 *   MDC.put("requestId", UUID.randomUUID().toString());
 *   try {
 *       processRequest();
 *   } finally {
 *       MDC.clear();
 *   }
 *
 * SENSITIVE DATA
 * ==============
 * WRONG:
 *   logger.info("User {} with password {}", username, password);
 *   logger.debug("Card: {}", cardNumber);
 *
 * CORRECT:
 *   logger.info("User {} authenticated", username);
 *   logger.debug("Card: {}", maskCardNumber(cardNumber));
 *
 * LOG LEVELS
 * ==========
 * TRACE: Method entry/exit, variable values
 * DEBUG: Development diagnostics
 * INFO: Normal business operations
 * WARN: Unexpected but recoverable
 * ERROR: Failures requiring attention
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);

    public static void main(String[] args) {
        MDC.put("requestId", UUID.randomUUID().toString());

        try {
            logger.info("Application started");

            Solution3 example = new Solution3();
            example.processOrder("ORD-123", "user-456", 99.99);

            logger.info("Application finished");
        } finally {
            MDC.clear();
        }
    }

    public void processOrder(String orderId, String userId, double total) {
        logger.info("Order processing started: orderId={}, userId={}", orderId, userId);

        if (logger.isDebugEnabled()) {
            logger.debug("Order details: total={}, userId={}", total, userId);
        }

        try {
            validateOrder(orderId, total);
            processPayment(orderId, total);
            logger.info("Order completed: orderId={}, total={}", orderId, total);
        } catch (Exception e) {
            logger.error("Order failed: orderId={}, userId={}", orderId, userId, e);
        }
    }

    private void validateOrder(String orderId, double total) {
        if (total <= 0) {
            logger.warn("Invalid total for order {}: {}", orderId, total);
            throw new IllegalArgumentException("Total must be positive");
        }
    }

    private void processPayment(String orderId, double total) {
        logger.info("Processing payment: orderId={}, total={}", orderId, total);
    }
}
