package academy.javaengineering.logging.logback.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example: Demonstrates Logback's rolling file appender behavior.
 * Shows different log levels and how they appear in output.
 */
public class LogbackRollingExample {

    private static final Logger logger = LoggerFactory.getLogger(LogbackRollingExample.class);

    public static void main(String[] args) {
        logger.info("Application starting with Logback");

        LogbackRollingExample example = new LogbackRollingExample();
        example.runBusinessLogic();

        logger.info("Application finished");
    }

    private void runBusinessLogic() {
        logger.debug("Starting business logic");

        for (int i = 0; i < 10; i++) {
            processItem(i);
        }

        logger.info("Business logic completed");
    }

    private void processItem(int itemId) {
        logger.debug("Processing item {}", itemId);

        try {
            if (itemId == 5) {
                throw new RuntimeException("Simulated error on item " + itemId);
            }
            logger.trace("Item {} processed successfully", itemId);
        } catch (Exception e) {
            logger.error("Failed to process item {}: {}", itemId, e.getMessage(), e);
        }
    }
}
