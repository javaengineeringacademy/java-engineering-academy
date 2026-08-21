package academy.javaengineering.logging.performance.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingPerformanceExample {

    private static final Logger logger = LoggerFactory.getLogger(LoggingPerformanceExample.class);
    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) {
        warmup();

        long start, elapsed;

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.debug("Processing item {} for user {}", i, "user1");
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Parameterized: " + (elapsed / 1_000_000) + "ms");

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("Processing item " + i + " for user " + "user1");
            }
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Guarded concat: " + (elapsed / 1_000_000) + "ms");

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.debug("Processing item {} for user {}", i, "user1");
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Level off (final): " + (elapsed / 1_000_000) + "ms");
    }

    private static void warmup() {
        for (int i = 0; i < 100_000; i++) {
            logger.debug("Warmup {}", i);
        }
    }
}
