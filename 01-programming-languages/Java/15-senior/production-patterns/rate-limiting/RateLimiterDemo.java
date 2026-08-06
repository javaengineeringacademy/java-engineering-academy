package academy.javaengineering.senior.production;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate Limiting Demo
 * Token Bucket, Sliding Window, and Fixed Window algorithms
 */
public class RateLimiterDemo {

    // --- Token Bucket Algorithm ---
    static class TokenBucket {
        private final int capacity;
        private final double refillRate; // tokens per second
        private double tokens;
        private long lastRefillTime;

        public TokenBucket(int capacity, double refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTime = System.nanoTime();
        }

        public synchronized boolean tryAcquire() {
            refill();
            if (tokens >= 1) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.nanoTime();
            double elapsed = (now - lastRefillTime) / 1_000_000_000.0;
            tokens = Math.min(capacity, tokens + elapsed * refillRate);
            lastRefillTime = now;
        }
    }

    // --- Fixed Window Counter ---
    static class FixedWindowLimiter {
        private final int maxRequests;
        private final long windowMs;
        private int counter = 0;
        private long windowStart = System.currentTimeMillis();

        public FixedWindowLimiter(int maxRequests, long windowMs) {
            this.maxRequests = maxRequests;
            this.windowMs = windowMs;
        }

        public synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            if (now - windowStart >= windowMs) {
                counter = 0;
                windowStart = now;
            }
            if (counter < maxRequests) {
                counter++;
                return true;
            }
            return false;
        }
    }

    // --- Sliding Window Log ---
    static class SlidingWindowLimiter {
        private final int maxRequests;
        private final long windowMs;
        private final java.utilLinkedList<Long> timestamps = new java.utilLinkedList<>();

        public SlidingWindowLimiter(int maxRequests, long windowMs) {
            this.maxRequests = maxRequests;
            this.windowMs = windowMs;
        }

        public synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            while (!timestamps.isEmpty() && timestamps.getFirst() <= now - windowMs) {
                timestamps.removeFirst();
            }
            if (timestamps.size() < maxRequests) {
                timestamps.addLast(now);
                return true;
            }
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Demo ===\n");

        // Token Bucket: capacity 5, refill 2/sec
        TokenBucket tokenBucket = new TokenBucket(5, 2);
        System.out.println("--- Token Bucket (capacity=5, refill=2/sec) ---");
        for (int i = 0; i < 8; i++) {
            boolean allowed = tokenBucket.tryAcquire();
            System.out.println("Request " + (i + 1) + ": " + (allowed ? "ALLOWED" : "REJECTED"));
        }

        // Fixed Window: 4 requests per 1 second
        FixedWindowLimiter fixedWindow = new FixedWindowLimiter(4, 1000);
        System.out.println("\n--- Fixed Window (4 req/1s) ---");
        for (int i = 0; i < 7; i++) {
            boolean allowed = fixedWindow.tryAcquire();
            System.out.println("Request " + (i + 1) + ": " + (allowed ? "ALLOWED" : "REJECTED"));
        }

        // Sliding Window: 3 requests per 1 second
        SlidingWindowLimiter slidingWindow = new SlidingWindowLimiter(3, 1000);
        System.out.println("\n--- Sliding Window (3 req/1s) ---");
        for (int i = 0; i < 6; i++) {
            boolean allowed = slidingWindow.tryAcquire();
            System.out.println("Request " + (i + 1) + ": " + (allowed ? "ALLOWED" : "REJECTED"));
        }

        // Concurrent test
        System.out.println("\n--- Concurrent Token Bucket Test ---");
        TokenBucket concurrentBucket = new TokenBucket(10, 5);
        AtomicInteger allowed = new AtomicInteger(0);
        AtomicInteger rejected = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 20; i++) {
            executor.submit(() -> {
                if (concurrentBucket.tryAcquire()) allowed.incrementAndGet();
                else rejected.incrementAndGet();
            });
        }
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
        System.out.println("Allowed: " + allowed.get() + ", Rejected: " + rejected.get());
    }
}
