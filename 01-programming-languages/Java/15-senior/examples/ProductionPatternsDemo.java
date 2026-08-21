package academy.javaengineering.senior.examples;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ProductionPatternsDemo {

    // Circuit Breaker
    enum CircuitState { CLOSED, OPEN, HALF_OPEN }

    static class CircuitBreaker {
        private final AtomicInteger failureCount = new AtomicInteger(0);
        private final AtomicInteger successCount = new AtomicInteger(0);
        private final AtomicReference<CircuitState> state = new AtomicReference<>(CircuitState.CLOSED);
        private final int failureThreshold;
        private final int successThreshold;
        private final long openDurationMs;
        private volatile long openedAt;

        CircuitBreaker(int failureThreshold, int successThreshold, long openDurationMs) {
            this.failureThreshold = failureThreshold;
            this.successThreshold = successThreshold;
            this.openDurationMs = openDurationMs;
        }

        <T> T execute(java.util.function.Supplier<T> action, java.util.function.Supplier<T> fallback) {
            if (state.get() == CircuitState.OPEN) {
                if (System.currentTimeMillis() - openedAt > openDurationMs) {
                    state.set(CircuitState.HALF_OPEN);
                    successCount.set(0);
                } else {
                    return fallback.get();
                }
            }

            try {
                T result = action.get();
                onSuccess();
                return result;
            } catch (Exception e) {
                onFailure();
                return fallback.get();
            }
        }

        private void onSuccess() {
            failureCount.set(0);
            if (state.get() == CircuitState.HALF_OPEN) {
                if (successCount.incrementAndGet() >= successThreshold) {
                    state.set(CircuitState.CLOSED);
                }
            }
        }

        private void onFailure() {
            if (state.get() == CircuitState.HALF_OPEN) {
                state.set(CircuitState.OPEN);
                openedAt = System.currentTimeMillis();
                return;
            }
            if (failureCount.incrementAndGet() >= failureThreshold) {
                state.set(CircuitState.OPEN);
                openedAt = System.currentTimeMillis();
            }
        }

        CircuitState getState() { return state.get(); }
    }

    // Health Check
    record HealthCheck(String name, boolean healthy, String message, long responseMs) {}

    interface HealthIndicator {
        HealthCheck check();
    }

    static class HealthCheckRegistry {
        private final Map<String, HealthIndicator> indicators = new ConcurrentHashMap<>();

        void register(String name, HealthIndicator indicator) {
            indicators.put(name, indicator);
        }

        Map<String, Object> healthReport() {
            Map<String, Object> report = new LinkedHashMap<>();
            boolean allHealthy = true;

            for (var entry : indicators.entrySet()) {
                HealthCheck check = entry.getValue().check();
                Map<String, Object> details = new LinkedHashMap<>();
                details.put("status", check.healthy() ? "UP" : "DOWN");
                details.put("message", check.message());
                details.put("responseMs", check.responseMs());
                report.put(entry.getKey(), details);
                if (!check.healthy()) allHealthy = false;
            }

            report.put("overall", allHealthy ? "UP" : "DOWN");
            return report;
        }
    }

    // Rate Limiter (Token Bucket)
    static class TokenBucketRateLimiter {
        private final int maxTokens;
        private final double refillRate; // tokens per second
        private double tokens;
        private long lastRefillTime;

        TokenBucketRateLimiter(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = maxTokens;
            this.lastRefillTime = System.nanoTime();
        }

        synchronized boolean tryAcquire() {
            refill();
            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.nanoTime();
            double elapsed = (now - lastRefillTime) / 1_000_000_000.0;
            tokens = Math.min(maxTokens, tokens + elapsed * refillRate);
            lastRefillTime = now;
        }
    }

    // Retry with exponential backoff
    static <T> T retryWithBackoff(
            java.util.function.Supplier<T> action,
            int maxRetries,
            long initialDelayMs) throws Exception {

        Exception lastException = null;
        long delay = initialDelayMs;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return action.get();
            } catch (Exception e) {
                lastException = e;
                if (attempt < maxRetries) {
                    Thread.sleep(delay);
                    delay = Math.min(delay * 2, 10_000); // cap at 10s
                }
            }
        }
        throw lastException;
    }

    // Bulkhead pattern
    static class Bulkhead {
        private final Semaphore semaphore;
        private final int maxConcurrent;

        Bulkhead(int maxConcurrent) {
            this.maxConcurrent = maxConcurrent;
            this.semaphore = new Semaphore(maxConcurrent);
        }

        <T> T execute(java.util.function.Supplier<T> action) throws InterruptedException {
            if (!semaphore.tryAcquire()) {
                throw new RuntimeException("Bulkhead full - " + semaphore.availablePermits() + " of " + maxConcurrent + " permits in use");
            }
            try {
                return action.get();
            } finally {
                semaphore.release();
            }
        }

        int available() { return semaphore.availablePermits(); }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Production Patterns Demo ===\n");

        // 1. Circuit Breaker
        System.out.println("--- Circuit Breaker ---");
        CircuitBreaker cb = new CircuitBreaker(3, 2, 2000);

        // Simulate failures
        for (int i = 0; i < 5; i++) {
            String result = cb.execute(
                () -> {
                    if (Math.random() > 0.3) throw new RuntimeException("Service down");
                    return "OK";
                },
                () -> "FALLBACK"
            );
            System.out.printf("  Call %d: %s (state: %s)%n", i + 1, result, cb.getState());
        }

        // Wait for half-open
        System.out.println("  Waiting for circuit to half-open...");
        Thread.sleep(2100);

        // Successful calls to close circuit
        for (int i = 0; i < 3; i++) {
            String result = cb.execute(() -> "OK", () -> "FALLBACK");
            System.out.printf("  Call %d: %s (state: %s)%n", i + 1, result, cb.getState());
        }

        // 2. Health Checks
        System.out.println("\n--- Health Checks ---");
        HealthCheckRegistry registry = new HealthCheckRegistry();

        registry.register("database", () -> {
            long start = System.currentTimeMillis();
            boolean healthy = true; // simulate DB check
            return new HealthCheck("database", healthy, "Connected", System.currentTimeMillis() - start);
        });

        registry.register("redis", () -> {
            long start = System.currentTimeMillis();
            boolean healthy = false;
            return new HealthCheck("redis", healthy, "Connection refused", System.currentTimeMillis() - start);
        });

        registry.register("kafka", () -> {
            long start = System.currentTimeMillis();
            boolean healthy = true;
            return new HealthCheck("kafka", healthy, "3/3 brokers alive", System.currentTimeMillis() - start);
        });

        Map<String, Object> report = registry.healthReport();
        report.forEach((k, v) -> System.out.printf("  %s: %s%n", k, v));

        // 3. Rate Limiter
        System.out.println("\n--- Token Bucket Rate Limiter ---");
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(5, 2.0); // 5 tokens, 2/sec refill

        int allowed = 0, rejected = 0;
        for (int i = 0; i < 15; i++) {
            if (limiter.tryAcquire()) {
                allowed++;
            } else {
                rejected++;
            }
            if (i == 4 || i == 9) Thread.sleep(1500); // let tokens refill
        }
        System.out.printf("  Allowed: %d, Rejected: %d%n", allowed, rejected);

        // 4. Retry with Backoff
        System.out.println("\n--- Retry with Exponential Backoff ---");
        AtomicInteger attempts = new AtomicInteger(0);
        try {
            String result = retryWithBackoff(() -> {
                int attempt = attempts.incrementAndGet();
                System.out.printf("  Attempt %d...%n", attempt);
                if (attempt < 4) throw new RuntimeException("Temporary failure");
                return "Success on attempt " + attempt;
            }, 5, 100);
            System.out.println("  Result: " + result);
        } catch (Exception e) {
            System.out.println("  Failed: " + e.getMessage());
        }

        // 5. Bulkhead
        System.out.println("\n--- Bulkhead Pattern ---");
        Bulkhead bulkhead = new Bulkhead(3);

        ExecutorService pool = Executors.newFixedThreadPool(5);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            final int id = i;
            futures.add(pool.submit(() -> {
                try {
                    bulkhead.execute(() -> {
                        System.out.printf("  Task %d running (available: %d)%n", id, bulkhead.available());
                        Thread.sleep(200);
                        return null;
                    });
                } catch (Exception e) {
                    System.out.printf("  Task %d rejected: %s%n", id, e.getMessage());
                }
            }));
        }

        for (Future<?> f : futures) f.get();
        pool.shutdown();

        System.out.println("\n=== Demo Complete ===");
    }
}
