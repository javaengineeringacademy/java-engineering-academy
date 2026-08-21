package academy.javaengineering.logging.structured.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Example: Log4j 2 JSON layout structured logging.
 */
public class Log4j2StructuredExample {

    private static final Logger logger = LoggerFactory.getLogger(Log4j2StructuredExample.class);

    public static void main(String[] args) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("service", "payment-service");
        MDC.put("version", "2.1.0");

        try {
            processPayment("PAY-456", "user-789", 149.99);
        } finally {
            MDC.clear();
        }
    }

    private static void processPayment(String paymentId, String userId, double amount) {
        MDC.put("paymentId", paymentId);
        MDC.put("userId", userId);
        MDC.put("amount", String.valueOf(amount));

        logger.info("Payment initiated");

        try {
            validatePayment(paymentId, amount);
            executePayment(paymentId, amount);
            logger.info("Payment successful");
        } catch (Exception e) {
            logger.error("Payment failed: {}", e.getMessage(), e);
        }
    }

    private static void validatePayment(String paymentId, double amount) {
        logger.debug("Validating payment {}", paymentId);
    }

    private static void executePayment(String paymentId, double amount) {
        logger.info("Executing payment {}", paymentId);
    }
}
