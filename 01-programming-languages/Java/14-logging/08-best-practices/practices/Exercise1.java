package academy.javaengineering.logging.bestpractices.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Exercise 1: Code review - find and fix logging anti-patterns.
 *
 * Review the code below and fix all logging issues:
 * 1. Incorrect logger declaration
 * 2. String concatenation
 * 3. Missing exception in error log
 * 4. Sensitive data in logs
 * 5. MDC not cleaned up
 * 6. Vague log messages
 * 7. Using System.out
 */
public class Exercise1 {

    // ANTI-PATTERN: Wrong logger declaration
    public Logger log = LoggerFactory.getLogger("myapp");

    public void processPayment(String userId, String cardNumber, double amount) {
        // ANTI-PATTERN: String concatenation
        log.info("Processing payment for user " + userId + " amount " + amount);

        // ANTI-PATTERN: Logging sensitive data
        log.debug("Card number: " + cardNumber);

        MDC.put("userId", userId);

        try {
            validatePayment(userId, amount);
            // ANTI-PATTERN: Missing context
            log.info("Payment processed");
        } catch (Exception e) {
            // ANTI-PATTERN: No exception stack trace
            log.error("Payment failed: " + e.getMessage());
        }

        // ANTI-PATTERN: System.out
        System.out.println("Payment processing complete");

        // ANTI-PATTERN: MDC not cleaned
    }

    private void validatePayment(String userId, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
    }
}
