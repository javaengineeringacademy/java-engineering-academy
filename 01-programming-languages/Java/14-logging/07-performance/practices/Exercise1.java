package academy.javaengineering.logging.performance.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 1: Optimize a slow logging implementation.
 *
 * The given code has multiple performance issues:
 * 1. String concatenation in log statements
 * 2. Missing level checks for expensive operations
 * 3. Unnecessary exception stack traces
 * 4. Logging in tight loops without guards
 *
 * Optimize the code while maintaining the same information content.
 */
public class Exercise1 {

    private static final Logger logger = LoggerFactory.getLogger(Exercise1.class);

    public void processItems(java.util.List<String> items) {
        // ISSUE 1: String concatenation
        System.out.println("Processing " + items.size() + " items");

        for (String item : items) {
            // ISSUE 2: No level check
            logger.debug("Item details: " + item.toString());

            // ISSUE 3: Logging full state
            logger.debug("Full state: " + dumpState());

            // ISSUE 4: Exception without level check
            try {
                validateItem(item);
            } catch (Exception e) {
                logger.error("Validation failed: " + e.getMessage());
            }
        }
    }

    private void validateItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item cannot be empty");
        }
    }

    private String dumpState() {
        // Simulate expensive state dump
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("state-").append(i).append(" ");
        }
        return sb.toString();
    }
}
