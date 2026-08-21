package academy.javaengineering.logging.mdc.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Exercise 3: Implement structured logging with MDC.
 *
 * Requirements:
 * 1. Create a logging utility that uses MDC for all context
 * 2. Include requestId, userId, sessionId, traceId
 * 3. Add custom business context (orderId, batchId, etc.)
 * 4. Provide a clean API for setting context
 * 5. Ensure proper cleanup
 *
 * Expected pattern:
 * [%d{HH:mm:ss}] [%X{requestId}] [%X{userId}] [%X{traceId}] %-5level %logger - %msg
 */
public class Exercise3 {

    // TODO: Create StructuredLogger class
    // TODO: Implement context builder pattern
    // TODO: Demonstrate usage in a realistic scenario

    public static void main(String[] args) {
        // TODO: Use StructuredLogger with context
        // TODO: Log through multiple methods
        // TODO: Show how MDC values appear in output
    }
}
