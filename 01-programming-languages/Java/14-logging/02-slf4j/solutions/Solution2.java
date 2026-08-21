package academy.javaengineering.logging.slf4j.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution 2: Proper parameterized logging.
 */
public class Solution2 {

    private static final Logger logger = LoggerFactory.getLogger(Solution2.class);

    public void processBatch(String batchId, int expectedCount) {
        logger.info("Batch {} started, expected items: {}", batchId, expectedCount);

        int processed = 0;
        int failed = 0;

        for (int i = 0; i < expectedCount; i++) {
            try {
                processItem(batchId, i);
                processed++;
            } catch (Exception e) {
                failed++;
                logger.warn("Item {} in batch {} failed: {}", i, batchId, e.getMessage());
            }
        }

        logger.info("Batch {} completed: processed={}, failed={}, total={}",
                batchId, processed, failed, expectedCount);
    }

    private void processItem(String batchId, int index) {
        logger.trace("Processing item {} in batch {}", index, batchId);
        if (Math.random() < 0.1) {
            throw new RuntimeException("Simulated processing error");
        }
    }

    public String convertData(String input, String format) {
        long start = System.currentTimeMillis();
        logger.debug("Converting data to format={}", format);

        String result = input; // Simplified conversion

        long duration = System.currentTimeMillis() - start;
        logger.debug("Conversion completed in {}ms", duration);
        return result;
    }
}
