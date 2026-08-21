package academy.javaengineering.logging.basics.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Solution 2: Refactored code with proper logging (fixes from Exercise 2).
 */
public class Solution2 {

    // FIXED: private static final logger with class reference
    private static final Logger logger = LoggerFactory.getLogger(Solution2.class);

    public void processItems(List<String> items) {
        // FIXED: Use logger instead of System.out, use parameterized logging
        logger.info("Processing {} items", items.size());

        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);

            // FIXED: Parameterized logging (no concatenation)
            // FIXED: Guard expensive operation with level check
            if (logger.isDebugEnabled()) {
                logger.debug("Processing item {}: {}", i, item.toString());
            }

            // FIXED: Don't log sensitive data (assuming item might contain tokens)
            logger.debug("Processing item at index {}", i);

            try {
                validateItem(item);
            } catch (Exception e) {
                // FIXED: Include exception as last argument for stack trace
                logger.error("Validation failed for item at index {}: {}", i, e.getMessage(), e);
            }
        }

        logger.info("Completed processing {} items", items.size());
    }

    public void authenticateUser(String username, String password) {
        // FIXED: Never log passwords or sensitive credentials
        logger.info("Authentication attempt for user: {}", username);

        // FIXED: Use logger instead of System.out
        logger.debug("Processing authentication for user: {}", username);
    }

    private void validateItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item cannot be empty");
        }
    }
}
