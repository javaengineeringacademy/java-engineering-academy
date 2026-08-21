package academy.javaengineering.logging.performance.practices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exercise 2: Implement a performance-aware logging utility.
 *
 * Requirements:
 * 1. Create a PerformanceLogger class
 * 2. Add timing capabilities (start/stop timer)
 * 3. Log only if duration exceeds threshold
 * 4. Use parameterized logging
 * 5. Include metrics in log output
 */
public class Exercise2 {

    private static final Logger logger = LoggerFactory.getLogger(Exercise2.class);

    public static class PerformanceLogger {
        private final long startMs;
        private final String operation;

        public PerformanceLogger(String operation) {
            this.operation = operation;
            this.startMs = System.currentTimeMillis();
            // TODO: Log start only at TRACE level
        }

        public void stop() {
            long duration = System.currentTimeMillis() - startMs;
            // TODO: Log completion with duration
            // TODO: Log WARN if duration exceeds threshold
        }

        public void stop(long thresholdMs) {
            long duration = System.currentTimeMillis() - startMs;
            // TODO: Log based on threshold comparison
        }
    }

    public void processOrder(String orderId) {
        PerformanceLogger timer = new PerformanceLogger("processOrder");
        try {
            // Simulate work
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            timer.stop(50); // Warn if > 50ms
        }
    }
}
