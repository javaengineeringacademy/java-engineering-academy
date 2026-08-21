package academy.javaengineering.logging.performance.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Solution 1: Optimized logging implementation.
 */
public class Solution1 {

    private static final Logger logger = LoggerFactory.getLogger(Solution1.class);

    public void processItems(List<String> items) {
        logger.info("Processing {} items", items.size());

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);

            if (logger.isDebugEnabled()) {
                logger.debug("Item details: {}", item.toString());
            }

            if (logger.isTraceEnabled()) {
                logger.trace("Full state: {}", dumpState());
            }

            try {
                validateItem(item);
            } catch (Exception e) {
                logger.error("Validation failed for item at index {}: {}", i, e.getMessage(), e);
            }
        }
    }

    private void validateItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item cannot be empty");
        }
    }

    private String dumpState() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("state-").append(i).append(" ");
        }
        return sb.toString();
    }
}
