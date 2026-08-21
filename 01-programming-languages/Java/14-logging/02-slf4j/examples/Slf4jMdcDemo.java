package academy.javaengineering.logging.slf4j.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

public class Slf4jMdcDemo {

    private static final Logger logger = LoggerFactory.getLogger(Slf4jMdcDemo.class);

    public static void main(String[] args) {
        // Simulate request processing with MDC context
        String requestId = UUID.randomUUID().toString();
        String userId = "user-456";

        MDC.put("requestId", requestId);
        MDC.put("userId", userId);

        try {
            processRequest();
        } finally {
            MDC.clear();
        }
    }

    private static void processRequest() {
        logger.info("Request received");
        logger.debug("Processing in thread {}", Thread.currentThread().getName());

        validateInput();
        processData();
        sendResponse();
    }

    private static void validateInput() {
        logger.debug("Validating input parameters");
        // Validation logic
    }

    private static void processData() {
        logger.info("Data processing started");
        // Processing logic
        logger.info("Data processing completed");
    }

    private static void sendResponse() {
        logger.debug("Sending response to client");
        // Response logic
    }
}
