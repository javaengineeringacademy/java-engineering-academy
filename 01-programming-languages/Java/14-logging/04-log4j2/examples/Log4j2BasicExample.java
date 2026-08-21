package academy.javaengineering.logging.log4j2.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Example: Basic Log4j 2 usage with different log levels.
 */
public class Log4j2BasicExample {

    private static final Logger logger = LogManager.getLogger(Log4j2BasicExample.class);

    public static void main(String[] args) {
        logger.info("Application starting with Log4j 2");

        Log4j2BasicExample example = new Log4j2BasicExample();
        example.processData();

        logger.info("Application finished");
    }

    private void processData() {
        logger.debug("Starting data processing");

        for (int i = 0; i < 10; i++) {
            try {
                processItem(i);
            } catch (Exception e) {
                logger.error("Failed to process item {}: {}", i, e.getMessage(), e);
            }
        }

        logger.info("Data processing completed");
    }

    private void processItem(int itemId) {
        logger.trace("Processing item {}", itemId);

        if (itemId == 5) {
            throw new RuntimeException("Simulated error on item " + itemId);
        }

        logger.debug("Item {} processed", itemId);
    }
}
