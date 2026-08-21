package academy.javaengineering.logging.logback.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Exercise 2: Implement structured logging with Logback.
 *
 * Requirements:
 * 1. Create a request processing pipeline with MDC context
 * 2. Use Logback's pattern to include MDC values
 * 3. Log each step with appropriate context
 * 4. Ensure MDC is cleaned up properly
 * 5. Demonstrate async logging behavior
 *
 * Expected output format:
 * [HH:mm:ss.SSS] [thread] [traceId] LEVEL logger - message
 */
public class Exercise2 {

    // TODO: Create logger

    public void processOrder(String orderId, String userId) {
        // TODO: Add traceId, userId, orderId to MDC
        // TODO: Log order received
        // TODO: Validate order
        // TODO: Process payment
        // TODO: Ship order
        // TODO: Log completion
        // TODO: Clean up MDC
    }

    private void validateOrder(String orderId) {
        // TODO: Log validation steps
    }

    private void processPayment(String orderId) {
        // TODO: Log payment processing
    }

    private void shipOrder(String orderId) {
        // TODO: Log shipping
    }
}
