package academy.javaengineering.logging.logback.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Example: Demonstrates MDC with Logback's %X{key} pattern.
 * Shows how MDC values appear in log output when configured properly.
 */
public class LogbackMdcExample {

    private static final Logger logger = LoggerFactory.getLogger(LogbackMdcExample.class);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            final int requestId = i;
            executor.submit(() -> processRequest(requestId));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void processRequest(int requestId) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        MDC.put("thread", Thread.currentThread().getName());

        try {
            logger.info("Processing request {}", requestId);
            logger.debug("Starting database query");
            Thread.sleep(100);
            logger.debug("Query completed");
            logger.info("Request {} completed", requestId);
        } catch (InterruptedException e) {
            logger.error("Request {} interrupted", requestId, e);
        } finally {
            MDC.clear();
        }
    }
}
