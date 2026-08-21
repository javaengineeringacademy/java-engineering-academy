package academy.javaengineering.logging.logback.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Solution 1: Production-ready Logback configuration and usage.
 */
public class Solution1 {

    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    public static void main(String[] args) {
        Solution1 app = new Solution1();
        app.runApplication();
    }

    public void runApplication() {
        logger.info("Application starting");

        List<String> items = List.of("item-1", "item-2", "item-3", "item-4", "item-5");
        processItems(items);

        logger.warn("Application completed with warnings");
        logger.info("Application finished");
    }

    private void processItems(List<String> items) {
        logger.debug("Processing {} items", items.size());

        for (String item : items) {
            try {
                processItem(item);
                logger.debug("Item {} processed successfully", item);
            } catch (Exception e) {
                logger.error("Failed to process item {}: {}", item, e.getMessage(), e);
            }
        }
    }

    private void processItem(String item) {
        logger.trace("Starting processing for {}", item);
        if (item.contains("3")) {
            throw new RuntimeException("Simulated error for " + item);
        }
    }
}
