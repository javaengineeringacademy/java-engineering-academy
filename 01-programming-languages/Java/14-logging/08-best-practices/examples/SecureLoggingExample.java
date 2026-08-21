package academy.javaengineering.logging.bestpractices.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Example: Secure logging (no sensitive data).
 */
public class SecureLoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(SecureLoggingExample.class);

    public void authenticateUser(String username, String password, String token) {
        MDC.put("requestId", UUID.randomUUID().toString());

        logger.info("Authentication attempt for user: {}", username);

        // WRONG: Never log passwords or tokens
        // logger.debug("Password: {}, Token: {}", password, token);

        // CORRECT: Log only non-sensitive identifiers
        logger.debug("Authenticating user: {}", username);

        try {
            validateCredentials(username, password);
            logger.info("User {} authenticated successfully", username);
        } catch (Exception e) {
            // CORRECT: Log failure reason without sensitive data
            logger.warn("Authentication failed for user {}: {}", username, e.getMessage());
        }

        MDC.clear();
    }

    private void validateCredentials(String username, String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    public void processPayment(String cardNumber, double amount) {
        logger.info("Processing payment of ${}", amount);

        // CORRECT: Mask sensitive data
        String maskedCard = maskCardNumber(cardNumber);
        logger.debug("Payment method: {}", maskedCard);

        // Process payment...
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
