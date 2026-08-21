package academy.javaengineering.logging.slf4j.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jParameterizedDemo {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jParameterizedDemo.class);

    public static void main(String[] args) {
        demonstrateParameterizedLogging();
        demonstrateArrayLogging();
        demonstrateConditionalLogging();
    }

    private static void demonstrateParameterizedLogging() {
        String username = "john_doe";
        int itemCount = 5;
        double total = 299.99;

        // Single parameter
        logger.debug("Processing user: {}", username);

        // Multiple parameters
        logger.info("Order placed: user={}, items={}, total={}", username, itemCount, total);

        // Exception as last param (no placeholder needed)
        try {
            riskyOperation();
        } catch (Exception e) {
            logger.error("Failed to process order for user={}: {}", username, e.getMessage(), e);
        }
    }

    private static void demonstrateArrayLogging() {
        String[] tags = {"urgent", "review", "approved"};
        String itemId = "ITEM-789";

        // Arrays are printed with brackets
        logger.info("Item {} has tags: {}", itemId, (Object) tags);
    }

    private static void demonstrateConditionalLogging() {
        // Check before expensive operation
        if (logger.isTraceEnabled()) {
            String expensiveData = computeExpensiveData();
            logger.trace("Computed data: {}", expensiveData);
        }

        // Parameterized handles this automatically (no allocation if disabled)
        logger.debug("Data: {}", computeExpensiveData());
    }

    private static void riskyOperation() {
        throw new RuntimeException("Simulated failure");
    }

    private static String computeExpensiveData() {
        // Simulate expensive computation
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            sb.append("item-").append(i).append(" ");
        }
        return sb.toString();
    }
}
