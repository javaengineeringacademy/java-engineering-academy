package academy.javaengineering.logging.basics.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicLoggingDemo {

    private static final Logger logger = LoggerFactory.getLogger(BasicLoggingDemo.class);

    public static void main(String[] args) {
        logger.info("Application starting");

        BasicLoggingDemo demo = new BasicLoggingDemo();
        demo.processOrder("ORD-12345", 99.99);

        logger.info("Application finished");
    }

    public void processOrder(String orderId, double total) {
        logger.debug("Processing order: id={}, total={}", orderId, total);

        try {
            validateOrder(orderId, total);
            logger.info("Order {} validated successfully", orderId);

            applyDiscount(orderId, total);
            logger.info("Order {} completed, final total={}", orderId, total * 0.9);

        } catch (IllegalArgumentException e) {
            logger.error("Failed to process order {}: {}", orderId, e.getMessage(), e);
        }
    }

    private void validateOrder(String orderId, double total) {
        logger.trace("Entering validateOrder(orderId={})", orderId);

        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be null or blank");
        }
        if (total <= 0) {
            throw new IllegalArgumentException("Total must be positive: " + total);
        }

        logger.trace("Exiting validateOrder - validation passed");
    }

    private void applyDiscount(String orderId, double total) {
        if (total > 100) {
            logger.debug("Applying bulk discount for order {}", orderId);
        } else {
            logger.debug("No discount applicable for order {}", orderId);
        }
    }
}
