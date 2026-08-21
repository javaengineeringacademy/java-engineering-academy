package academy.javaengineering.logging.structured.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Exercise 3: Create a JSON log entry builder.
 *
 * Requirements:
 * 1. Create a LogEntry class that builds JSON log entries
 * 2. Support standard fields: timestamp, level, message, logger
 * 3. Support MDC fields
 * 4. Support custom fields via builder pattern
 * 5. Support exception details
 *
 * Usage:
 * LogEntry entry = new LogEntry()
 *     .level("INFO")
 *     .message("Order processed")
 *     .field("orderId", "ORD-123")
 *     .field("total", 99.99)
 *     .exception(e);
 * logger.info(entry.toJson());
 */
public class Exercise3 {

    // TODO: Create LogEntry builder class
    // TODO: Implement JSON serialization
    // TODO: Demonstrate usage with different log scenarios
}
