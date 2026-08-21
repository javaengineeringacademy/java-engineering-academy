package academy.javaengineering.logging.basics.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates different ways to create and name loggers.
 * Best practice: always use private static final with the class reference.
 */
public class LoggerCreationDemo {

    // PREFERRED: Using class reference
    private static final Logger logger = LoggerFactory.getLogger(LoggerCreationDemo.class);

    // ALTERNATIVE: Using string name (less type-safe but valid for non-class contexts)
    private static final Logger namedLogger = LoggerFactory.getLogger("myapp.custom.category");

    public static void main(String[] args) {
        logger.info("Logger name from class: {}", logger.getName());
        namedLogger.info("Logger name from string: {}", namedLogger.getName());

        demonstrateLevelChecks();
    }

    private static void demonstrateLevelChecks() {
        // Check before expensive operation
        if (logger.isTraceEnabled()) {
            String expensiveData = computeExpensiveData();
            logger.trace("Expensive computation result: {}", expensiveData);
        }

        // Parameterized logging handles this automatically
        logger.debug("Processing with result: {}", computeExpensiveData());
    }

    private static String computeExpensiveData() {
        // Simulate expensive computation
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("item-").append(i).append(" ");
        }
        return sb.toString().trim();
    }
}
