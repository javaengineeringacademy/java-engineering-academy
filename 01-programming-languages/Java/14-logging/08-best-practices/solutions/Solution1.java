package academy.javaengineering.logging.bestpractices.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Solution 1: Code review fixes.
 */
public class Solution1 {

    // FIXED: private static final with class reference
    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    public void processPayment(String userId, String cardNumber, double amount) {
        // FIXED: Parameterized logging with context
        logger.info("Processing payment: userId={}, amount={}", userId, amount);

        // FIXED: No sensitive data logged
        logger.debug("Payment method: {}", maskCardNumber(cardNumber));

        MDC.put("requestId", UUID.randomUUID().toString());
        MDC.put("userId", userId);

        try {
            validatePayment(userId, amount);
            logger.info("Payment processed successfully: userId={}", userId);
        } catch (Exception e) {
            // FIXED: Exception as last argument with context
            logger.error("Payment failed: userId={}, amount={}", userId, amount, e);
        } finally {
            // FIXED: MDC cleanup in finally
            MDC.clear();
        }
    }

    private void validatePayment(String userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
