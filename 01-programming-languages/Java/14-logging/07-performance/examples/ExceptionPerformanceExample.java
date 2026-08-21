package academy.javaengineering.logging.performance.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionPerformanceExample {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionPerformanceExample.class);
    private static final int ITERATIONS = 100_000;

    public static void main(String[] args) {
        RuntimeException ex = new RuntimeException("Test exception");
        long start, elapsed;

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.error("Error occurred: {}", ex.getMessage(), ex);
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Full stack: " + (elapsed / 1_000_000) + "ms");

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.error("Error occurred: {}", ex.getMessage());
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Message only: " + (elapsed / 1_000_000) + "ms");
    }
}
