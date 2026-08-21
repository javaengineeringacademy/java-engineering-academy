package academy.javaengineering.logging.basics.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution 1: Proper logging implementation for Exercise 1.
 */
public class Solution1 {

    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    public String processPayment(String userId, double amount) {
        logger.debug("Processing payment for user={}, amount={}", userId, amount);

        if (amount > 10000) {
            logger.warn("Payment amount {} exceeds daily limit of 10000 for user={}", amount, userId);
        }

        try {
            if (amount > 10000) {
                throw new IllegalArgumentException("Amount exceeds daily limit");
            }
            if (userId == null || userId.isBlank()) {
                throw new IllegalArgumentException("User ID required");
            }

            String paymentId = "PAY-" + System.currentTimeMillis();
            logger.info("Payment processed successfully: paymentId={}, user={}, amount={}",
                    paymentId, userId, amount);
            return paymentId;

        } catch (IllegalArgumentException e) {
            logger.error("Payment validation failed: user={}, amount={}, reason={}",
                    userId, amount, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected payment failure: user={}, amount={}", userId, amount, e);
            throw new RuntimeException("Payment failed", e);
        }
    }

    public void transferFunds(String fromAccount, String toAccount, double amount) {
        logger.info("Initiating transfer: from={}, to={}, amount={}", fromAccount, toAccount, amount);

        logger.debug("Validating accounts");
        if (fromAccount.equals(toAccount)) {
            logger.warn("Source and destination accounts are the same: {}", fromAccount);
            throw new IllegalArgumentException("Cannot transfer to same account");
        }

        logger.debug("Debiting account {}", fromAccount);
        logger.debug("Crediting account {}", toAccount);

        logger.info("Transfer completed: from={}, to={}, amount={}", fromAccount, toAccount, amount);
    }
}
