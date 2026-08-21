package academy.javaengineering.logging.structured.examples;

import net.logstash.logback.encoder.LogstashEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Example: Structured JSON logging with Logstash encoder.
 */
public class StructuredLoggingExample {

    private static final Logger logger = LoggerFactory.getLogger(StructuredLoggingExample.class);

    public static void main(String[] args) {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);
        MDC.put("userId", "john_doe");
        MDC.put("service", "order-service");

        try {
            processOrder("ORD-123", 99.99);
        } finally {
            MDC.clear();
        }
    }

    private static void processOrder(String orderId, double total) {
        MDC.put("orderId", orderId);
        MDC.put("orderTotal", String.valueOf(total));

        logger.info("Order processing started");

        try {
            validateOrder(orderId);
            processPayment(orderId, total);
            logger.info("Order completed successfully");
        } catch (Exception e) {
            logger.error("Order processing failed", e);
        } finally {
            MDC.remove("orderId");
            MDC.remove("orderTotal");
        }
    }

    private static void validateOrder(String orderId) {
        logger.debug("Validating order {}", orderId);
    }

    private static void processPayment(String orderId, double total) {
        MDC.put("paymentMethod", "credit_card");
        logger.info("Payment processed for order {}", orderId);
    }
}
