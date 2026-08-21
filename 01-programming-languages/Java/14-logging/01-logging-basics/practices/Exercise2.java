package academy.javaengineering.logging.basics.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 2: Refactor the code to fix logging anti-patterns.
 *
 * Find and fix these issues:
 * 1. String concatenation in log statements
 * 2. Missing level checks before expensive operations
 * 3. Exception logging without stack trace
 * 4. Creating loggers incorrectly
 * 5. Logging sensitive data
 *
 * Rewrite the methods with proper logging.
 */
public class Exercise2 {

    // ANTI-PATTERN: Logger created incorrectly
    // Fix this
    public Logger logger = LoggerFactory.getLogger("exercise2");

    public void processItems(List<String> items) {
        // ANTI-PATTERN: String concatenation
        System.out.println("Processing " + items.size() + " items");

        for (String item : items) {
            // ANTI-PATTERN: No level check before expensive toString()
            logger.debug("Item details: " + item.toString());

            // ANTI-PATTERN: Logging sensitive data
            logger.info("Processing item with token: " + item);

            try {
                validateItem(item);
            } catch (Exception e) {
                // ANTI-PATTERN: Exception logged without stack trace
                logger.error("Validation failed: " + e.getMessage());
            }
        }
    }

    public void authenticateUser(String username, String password) {
        // ANTI-PATTERN: Logging password
        logger.info("Authenticating user: {} with password: {}", username, password);

        // ANTI-PATTERN: System.out for errors
        System.out.println("Authentication attempt for: " + username);
    }

    private void validateItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item cannot be empty");
        }
    }
}
