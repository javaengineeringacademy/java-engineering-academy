package academy.javaengineering.logging.performance.solutions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Solution 3: Logging benchmark.
 */
public class Solution3 {

    private static final Logger logger = LoggerFactory.getLogger(Solution3.class);
    private static final int WARMUP = 100_000;
    private static final int ITERATIONS = 1_000_000;

    public static void main(String[] args) {
        System.out.println("Warming up...");
        for (int i = 0; i < WARMUP; i++) {
            logger.debug("Warmup {}", i);
        }

        System.out.println("Running benchmarks...");
        System.out.println("Iterations: " + ITERATIONS);
        System.out.println();

        long start, elapsed;

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.debug("Item {} for user {}", i, "user1");
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Parameterized:  " + formatResult(elapsed));

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            if (logger.isDebugEnabled()) {
                logger.debug("Item " + i + " for user " + "user1");
            }
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Guarded concat: " + formatResult(elapsed));

        start = System.nanoTime();
        for (int i = 0; i < ITERATIONS; i++) {
            logger.debug("Item {} for user {}", i, "user1");
        }
        elapsed = System.nanoTime() - start;
        System.out.println("Parameterized2: " + formatResult(elapsed));
    }

    private static String formatResult(long nanos) {
        double ms = nanos / 1_000_000.0;
        double opsPerSec = (ITERATIONS * 1_000_000_000.0) / nanos;
        return String.format("%.1fms  (%.0f ops/sec)", ms, opsPerSec);
    }
}
