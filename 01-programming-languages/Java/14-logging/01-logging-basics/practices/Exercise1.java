package academy.javaengineering.logging.basics.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 1: Add appropriate logging to the given methods.
 *
 * Requirements:
 * 1. Use the correct log level for each scenario
 * 2. Use parameterized logging (not string concatenation)
 * 3. Log exceptions as the last argument
 * 4. Include relevant context in messages
 *
 * Scenarios to log:
 * - Method entry with parameters (DEBUG)
 * - Successful completion (INFO)
 * - Validation failure (WARN)
 * - Unexpected error (ERROR)
 * - State transition (DEBUG)
 */
public class Exercise1 {

    // TODO: Declare a proper Logger instance

    public String processPayment(String userId, double amount) {
        // TODO: Log that payment processing started (DEBUG)

        // TODO: Log if amount exceeds limit (WARN)

        // TODO: Log successful payment (INFO)

        // TODO: Log failure with exception (ERROR)

        try {
            if (amount > 10000) {
                throw new IllegalArgumentException("Amount exceeds daily limit");
            }
            if (userId == null || userId.isBlank()) {
                throw new IllegalArgumentException("User ID required");
            }
            return "PAY-" + System.currentTimeMillis();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Payment failed", e);
        }
    }

    public void transferFunds(String fromAccount, String toAccount, double amount) {
        // TODO: Log transfer initiation (INFO)

        // TODO: Log each step of the transfer (DEBUG)

        // TODO: Log if accounts are the same (WARN)

        // TODO: Log completion (INFO)

        // TODO: Log failure (ERROR)
    }
}
