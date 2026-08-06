package academy.javaengineering.exercises;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 * Exercises: Production Patterns (Circuit Breaker, Rate Limiting)
 *
 * Complete the TODO sections below.
 */
public class ProductionExercises {

    // TODO 1: Implement a Circuit Breaker pattern
    public static class CircuitBreaker {
        public enum State { CLOSED, OPEN, HALF_OPEN }

        private final int failureThreshold;
        private final long resetTimeoutMs;
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
        private volatile long lastFailureTime = 0;

        public CircuitBreaker(int failureThreshold, long resetTimeoutMs) {
            this.failureThreshold = failureThreshold;
            this.resetTimeoutMs = resetTimeoutMs;
        }

        public <T> T execute(Callable<T> operation) throws Exception {
            // TODO: implement circuit breaker logic
            // If OPEN and timeout not elapsed, throw CircuitBreakerOpenException
            // If OPEN and timeout elapsed, try HALF_OPEN
            // If CLOSED, execute and track failures
            return operation.call();
        }

        public void recordSuccess() {
            // TODO: reset failure count, set state to CLOSED
        }

        public void recordFailure() {
            // TODO: increment failures, if threshold reached set state to OPEN
        }

        public State getState() {
            return state.get();
        }

        public int getFailureCount() {
            return failureCount.get();
        }
    }

    public static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }

    // TODO 2: Implement a Token Bucket Rate Limiter
    public static class TokenBucketRateLimiter {
        private final int maxTokens;
        private final int refillRate; // tokens per second
        private final AtomicInteger tokens;
        private volatile long lastRefillTime;

        public TokenBucketRateLimiter(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = new AtomicInteger(maxTokens);
            this.lastRefillTime = System.currentTimeMillis();
        }

        public boolean tryAcquire() {
            // TODO: implement token bucket algorithm
            // Refill tokens based on elapsed time
            // If tokens available, consume one and return true
            return false;
        }

        public int availableTokens() {
            return tokens.get();
        }
    }

    // TODO 3: Implement a Retry mechanism with exponential backoff
    public static class RetryPolicy {
        private final int maxRetries;
        private final long initialDelayMs;
        private final double backoffMultiplier;

        public RetryPolicy(int maxRetries, long initialDelayMs, double backoffMultiplier) {
            this.maxRetries = maxRetries;
            this.initialDelayMs = initialDelayMs;
            this.backoffMultiplier = backoffMultiplier;
        }

        public <T> T execute(Callable<T> operation) throws Exception {
            // TODO: implement retry with exponential backoff
            // Retry on exception, increase delay each time
            return operation.call();
        }

        public long getDelayForAttempt(int attempt) {
            // TODO: implement delay calculation
            return 0;
        }
    }

    // TODO 4: Implement a Bulkhead pattern (semaphore-based)
    public static class Bulkhead {
        private final Semaphore semaphore;
        private final int maxConcurrent;

        public Bulkhead(int maxConcurrent) {
            this.maxConcurrent = maxConcurrent;
            this.semaphore = new Semaphore(maxConcurrent);
        }

        public <T> T execute(Callable<T> operation) throws Exception {
            // TODO: implement - acquire permit, execute, release
            return operation.call();
        }

        public int availablePermits() {
            return semaphore.availablePermits();
        }

        public int getActiveCount() {
            return maxConcurrent - semaphore.availablePermits();
        }
    }

    // TODO 5: Implement a Timeout wrapper
    public static class TimeoutWrapper {
        private final long timeoutMs;

        public TimeoutWrapper(long timeoutMs) {
            this.timeoutMs = timeoutMs;
        }

        public <T> T execute(Callable<T> operation) throws Exception {
            // TODO: implement using ExecutorService with timeout
            return operation.call();
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) throws Exception {
        ProductionExercises exercises = new ProductionExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== ProductionExercises Tests ===\n");

        // Test 1 - Circuit Breaker
        total++;
        CircuitBreaker cb = new CircuitBreaker(3, 1000);
        if (cb.getState() == CircuitBreaker.State.CLOSED) {
            System.out.println("Test 1a PASSED: CircuitBreaker initial state");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: CircuitBreaker initial state");
        }

        total++;
        cb.recordFailure();
        cb.recordFailure();
        cb.recordFailure();
        if (cb.getState() == CircuitBreaker.State.OPEN && cb.getFailureCount() == 3) {
            System.out.println("Test 1b PASSED: CircuitBreaker opens after threshold");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: CircuitBreaker state=" + cb.getState());
        }

        total++;
        cb.recordSuccess();
        if (cb.getState() == CircuitBreaker.State.CLOSED && cb.getFailureCount() == 0) {
            System.out.println("Test 1c PASSED: CircuitBreaker resets on success");
            passed++;
        } else {
            System.out.println("Test 1c FAILED: CircuitBreaker reset");
        }

        // Test 2 - Rate Limiter
        total++;
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 10);
        int acquired = 0;
        for (int i = 0; i < 7; i++) {
            if (limiter.tryAcquire()) acquired++;
        }
        if (acquired == 5 && limiter.availableTokens() == 0) {
            System.out.println("Test 2 PASSED: TokenBucketRateLimiter");
            passed++;
        } else {
            System.out.println("Test 2 FAILED: TokenBucketRateLimiter - acquired=" + acquired);
        }

        // Test 3 - Retry Policy
        total++;
        RetryPolicy retry = new RetryPolicy(3, 100, 2.0);
        if (retry.getDelayForAttempt(0) == 100 && retry.getDelayForAttempt(1) == 200) {
            System.out.println("Test 3a PASSED: RetryPolicy delays");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: RetryPolicy delays");
        }

        total++;
        AtomicInteger attempts = new AtomicInteger(0);
        try {
            retry.execute(() -> {
                int attempt = attempts.incrementAndGet();
                if (attempt < 3) throw new RuntimeException("fail");
                return "success";
            });
            System.out.println("Test 3b PASSED: RetryPolicy retry");
            passed++;
        } catch (Exception e) {
            System.out.println("Test 3b FAILED: RetryPolicy - " + e.getMessage());
        }

        // Test 4 - Bulkhead
        total++;
        Bulkhead bulkhead = new Bulkhead(3);
        if (bulkhead.availablePermits() == 3 && bulkhead.getActiveCount() == 0) {
            System.out.println("Test 4a PASSED: Bulkhead initial state");
            passed++;
        } else {
            System.out.println("Test 4a FAILED: Bulkhead initial state");
        }

        total++;
        List<Future<?>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            futures.add(executor.submit(() -> {
                try {
                    bulkhead.execute(() -> {
                        Thread.sleep(200);
                        return null;
                    });
                } catch (Exception e) {}
            }));
        }
        Thread.sleep(50);
        if (bulkhead.getActiveCount() == 3) {
            System.out.println("Test 4b PASSED: Bulkhead active count");
            passed++;
        } else {
            System.out.println("Test 4b FAILED: Bulkhead active count=" + bulkhead.getActiveCount());
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Test 5 - Timeout Wrapper
        total++;
        TimeoutWrapper timeout = new TimeoutWrapper(1000);
        String result = timeout.execute(() -> "quick");
        if ("quick".equals(result)) {
            System.out.println("Test 5a PASSED: TimeoutWrapper success");
            passed++;
        } else {
            System.out.println("Test 5a FAILED: TimeoutWrapper");
        }

        total++;
        try {
            timeout.execute(() -> {
                Thread.sleep(5000);
                return "slow";
            });
            System.out.println("Test 5b FAILED: should timeout");
        } catch (Exception e) {
            System.out.println("Test 5b PASSED: TimeoutWrapper timeout");
            passed++;
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
