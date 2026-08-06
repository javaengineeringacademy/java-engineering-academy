package academy.javaengineering.senior.production;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Retry Pattern Demo
 * Fixed delay, exponential backoff, jitter, max attempts
 */
public class RetryDemo {

    // --- Fixed Delay Retry ---
    static <T> T retryFixed(java.util.function.Supplier<T> action,
                            int maxAttempts, long delayMs) throws Exception {
        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("Attempt " + attempt + " (fixed delay)");
                return action.get();
            } catch (RuntimeException e) {
                lastException = e;
                System.out.println("Failed: " + e.getMessage());
                if (attempt < maxAttempts) {
                    Thread.sleep(delayMs);
                }
            }
        }
        throw lastException;
    }

    // --- Exponential Backoff ---
    static <T> T retryExponential(java.util.function.Supplier<T> action,
                                  int maxAttempts, long baseDelayMs) throws Exception {
        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("Attempt " + attempt + " (exponential backoff)");
                return action.get();
            } catch (RuntimeException e) {
                lastException = e;
                System.out.println("Failed: " + e.getMessage());
                if (attempt < maxAttempts) {
                    long delay = baseDelayMs * (1L << (attempt - 1));
                    System.out.println("  Waiting " + delay + "ms before retry");
                    Thread.sleep(delay);
                }
            }
        }
        throw lastException;
    }

    // --- Exponential Backoff with Jitter ---
    static <T> T retryWithJitter(java.util.function.Supplier<T> action,
                                 int maxAttempts, long baseDelayMs) throws Exception {
        Exception lastException = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                System.out.println("Attempt " + attempt + " (backoff + jitter)");
                return action.get();
            } catch (RuntimeException e) {
                lastException = e;
                System.out.println("Failed: " + e.getMessage());
                if (attempt < maxAttempts) {
                    long exponentialDelay = baseDelayMs * (1L << (attempt - 1));
                    long jitter = ThreadLocalRandom.current().nextLong(0, exponentialDelay / 2);
                    long totalDelay = exponentialDelay + jitter;
                    System.out.println("  Waiting " + totalDelay + "ms (backoff=" + exponentialDelay + ", jitter=" + jitter + ")");
                    Thread.sleep(totalDelay);
                }
            }
        }
        throw lastException;
    }

    // Simulated flaky service
    static int callCount = 0;
    static String unreliableService() {
        callCount++;
        if (callCount % 3 != 0) {
            throw new RuntimeException("Connection timeout");
        }
        return "Success!";
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Retry Demo ===\n");

        // Fixed delay retry
        System.out.println("--- Fixed Delay Retry (3 attempts, 500ms) ---");
        callCount = 0;
        try {
            String result = retryFixed(() -> unreliableService(), 3, 500);
            System.out.println("Result: " + result);
        } catch (RuntimeException e) {
            System.out.println("All attempts failed: " + e.getMessage());
        }

        System.out.println();

        // Exponential backoff
        System.out.println("--- Exponential Backoff (4 attempts, 200ms base) ---");
        callCount = 0;
        try {
            String result = retryExponential(() -> unreliableService(), 4, 200);
            System.out.println("Result: " + result);
        } catch (RuntimeException e) {
            System.out.println("All attempts failed: " + e.getMessage());
        }

        System.out.println();

        // Jitter retry
        System.out.println("--- Backoff + Jitter (5 attempts, 100ms base) ---");
        callCount = 0;
        try {
            String result = retryWithJitter(() -> unreliableService(), 5, 100);
            System.out.println("Result: " + result);
        } catch (RuntimeException e) {
            System.out.println("All attempts failed: " + e.getMessage());
        }
    }
}
