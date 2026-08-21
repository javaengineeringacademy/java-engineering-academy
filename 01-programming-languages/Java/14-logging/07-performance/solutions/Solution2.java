package academy.javaengineering.logging.performance.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution 2: Performance-aware logging utility.
 */
public class Solution2 {

    private static final Logger logger = LoggerFactory.getLogger(Solution2.class);

    public static class PerformanceLogger {
        private final long startMs;
        private final String operation;

        public PerformanceLogger(String operation) {
            this.operation = operation;
            this.startMs = System.currentTimeMillis();
            logger.trace("Starting operation: {}", operation);
        }

        public void stop() {
            long duration = System.currentTimeMillis() - startMs;
            logger.info("Operation {} completed in {}ms", operation, duration);
        }

        public void stop(long thresholdMs) {
            long duration = System.currentTimeMillis() - startMs;
            if (duration > thresholdMs) {
                logger.warn("Operation {} exceeded threshold: {}ms > {}ms",
                        operation, duration, thresholdMs);
            } else {
                logger.debug("Operation {} completed in {}ms", operation, duration);
            }
        }
    }

    public void processOrder(String orderId) {
        PerformanceLogger timer = new PerformanceLogger("processOrder");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            timer.stop(50);
        }
    }
}
