package academy.javaengineering.logging.bestpractices.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.List;
import java.util.UUID;

/**
 * Example: Proper logging in batch processing.
 */
public class BatchProcessingExample {

    private static final Logger logger = LoggerFactory.getLogger(BatchProcessingExample.class);

    public void processBatch(String batchId, List<String> items) {
        MDC.put("batchId", batchId);
        MDC.put("totalItems", String.valueOf(items.size()));

        logger.info("Batch processing started: {} items", items.size());

        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < items.size(); i++) {
            MDC.put("itemIndex", String.valueOf(i));
            String item = items.get(i);

            try {
                processItem(item);
                successCount++;
            } catch (Exception e) {
                failCount++;
                logger.warn("Item {} failed: {}", i, e.getMessage());
            }
        }

        MDC.put("successCount", String.valueOf(successCount));
        MDC.put("failCount", String.valueOf(failCount));
        logger.info("Batch completed: success={}, failed={}", successCount, failCount);

        MDC.clear();
    }

    private void processItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item cannot be empty");
        }
        logger.debug("Processing item: {}", item);
    }
}
