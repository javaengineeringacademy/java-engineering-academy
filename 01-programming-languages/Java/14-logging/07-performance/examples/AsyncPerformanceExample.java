package academy.javaengineering.logging.performance.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncPerformanceExample {

    private static final Logger logger = LoggerFactory.getLogger(AsyncPerformanceExample.class);
    private static final int ITERATIONS = 100_000;

    public static void main(String[] args) {
        System.out.println("Testing synchronous logging...");
        long start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.info("Synchronous log event {}", i);
        }
        long elapsed = System.nanoTime() - start;
        System.out.println("Sync: " + (elapsed / 1_000_000) + "ms");

        System.out.println("Testing async logging...");
        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.info("Async log event {}", i);
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Async: " + (elapsed / 1_000_000) + "ms");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
