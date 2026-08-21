package academy.javaengineering.senior.solutions;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ProductionSolutions {

    // Exercise 1: Circuit Breaker with Metrics
    enum State { CLOSED, OPEN, HALF_OPEN }
    record CircuitMetrics(State state, long totalCalls, long failures, double failureRate) {}

    static class MetricsCircuitBreaker {
        private final int failureThreshold;
        private final long resetTimeoutMs;
        private final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);
        private final AtomicLong totalCalls = new AtomicLong(0);
        private final AtomicLong failures = new AtomicLong(0);
        private volatile long openedAt;

        MetricsCircuitBreaker(int failureThreshold, long resetTimeoutMs) {
            this.failureThreshold = failureThreshold;
            this.resetTimeoutMs = resetTimeoutMs;
        }

        <T> T execute(java.util.function.Supplier<T> action, java.util.function.Supplier<T> fallback) {
            totalCalls.incrementAndGet();

            if (state.get() == State.OPEN) {
                if (System.currentTimeMillis() - openedAt > resetTimeoutMs) {
                    state.set(State.HALF_OPEN);
                } else {
                    return fallback.get();
                }
            }

            try {
                T result = action.get();
                if (state.get() == State.HALF_OPEN) {
                    state.set(State.CLOSED);
                    failures.set(0);
                } else {
                    failures.set(0);
                }
                return result;
            } catch (Exception e) {
                long failCount = failures.incrementAndGet();
                if (state.get() == State.HALF_OPEN || failCount >= failureThreshold) {
                    state.set(State.OPEN);
                    openedAt = System.currentTimeMillis();
                }
                return fallback.get();
            }
        }

        CircuitMetrics metrics() {
            long total = totalCalls.get();
            long fail = failures.get();
            return new CircuitMetrics(state.get(), total, fail,
                total > 0 ? (double) fail / total : 0.0);
        }
    }

    // Exercise 2: Health Check System
    enum HealthStatus { UP, DOWN, DEGRADED }
    record HealthResult(String name, HealthStatus status, String message, long responseMs) {}

    static class HealthCheckSystem {
        private final Map<String, java.util.function.Supplier<Boolean>> checks = new ConcurrentHashMap<>();
        private final List<HealthResult> lastResults = new CopyOnWriteArrayList<>();
        private final List<HealthStatus> history = new CopyOnWriteArrayList<>();

        void register(String name, java.util.function.Supplier<Boolean> check) {
            checks.put(name, check);
        }

        HealthStatus checkAll(long timeoutMs) {
            ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
            List<Future<HealthResult>> futures = new ArrayList<>();

            for (var entry : checks.entrySet()) {
                futures.add(executor.submit(() -> {
                    long start = System.nanoTime();
                    try {
                        boolean healthy = entry.getValue().get();
                        long elapsed = (System.nanoTime() - start) / 1_000_000;
                        return new HealthResult(entry.getKey(),
                            healthy ? HealthStatus.UP : HealthStatus.DOWN,
                            healthy ? "OK" : "FAILED", elapsed);
                    } catch (Exception e) {
                        long elapsed = (System.nanoTime() - start) / 1_000_000;
                        return new HealthResult(entry.getKey(), HealthStatus.DOWN,
                            e.getMessage(), elapsed);
                    }
                }));
            }

            executor.shutdown();

            lastResults.clear();
            int downCount = 0;
            for (Future<HealthResult> f : futures) {
                try {
                    HealthResult r = f.get(timeoutMs, TimeUnit.MILLISECONDS);
                    lastResults.add(r);
                    if (r.status() == HealthStatus.DOWN) downCount++;
                } catch (Exception e) {
                    lastResults.add(new HealthResult("unknown", HealthStatus.DOWN, e.getMessage(), 0));
                    downCount++;
                }
            }

            HealthStatus overall;
            if (downCount == 0) overall = HealthStatus.UP;
            else if (downCount < checks.size()) overall = HealthStatus.DEGRADED;
            else overall = HealthStatus.DOWN;

            history.add(overall);
            return overall;
        }

        List<HealthResult> details() { return List.copyOf(lastResults); }
        List<HealthStatus> history(int lastN) {
            int from = Math.max(0, history.size() - lastN);
            return history.subList(from, history.size());
        }
    }

    // Exercise 3: Multi-Strategy Rate Limiter
    record RateLimitResult(boolean allowed, int remaining, long retryAfterMs) {}
    enum Strategy { FIXED_WINDOW, SLIDING_WINDOW, TOKEN_BUCKET }

    static class MultiStrategyRateLimiter {
        private final int maxRequests;
        private final long windowMs;
        private final Strategy strategy;
        private final AtomicInteger counter = new AtomicInteger(0);
        private final Deque<Long> slidingWindow = new ConcurrentLinkedDeque<>();
        private double tokens;
        private long lastRefill;

        MultiStrategyRateLimiter(int maxRequests, long windowMs, Strategy strategy) {
            this.maxRequests = maxRequests;
            this.windowMs = windowMs;
            this.strategy = strategy;
            this.tokens = maxRequests;
            this.lastRefill = System.nanoTime();
        }

        synchronized RateLimitResult tryAcquire() {
            long now = System.currentTimeMillis();
            long nowNs = System.nanoTime();

            return switch (strategy) {
                case FIXED_WINDOW -> {
                    if (now % windowMs < windowMs / 2) counter.set(0);
                    int current = counter.incrementAndGet();
                    yield new RateLimitResult(current <= maxRequests,
                        Math.max(0, maxRequests - current), 0);
                }
                case SLIDING_WINDOW -> {
                    while (!slidingWindow.isEmpty() && slidingWindow.peekFirst() < now - windowMs) {
                        slidingWindow.pollFirst();
                    }
                    int current = slidingWindow.size();
                    if (current < maxRequests) {
                        slidingWindow.addLast(now);
                        yield new RateLimitResult(true, maxRequests - current - 1, 0);
                    } else {
                        long retryAfter = slidingWindow.peekFirst() + windowMs - now;
                        yield new RateLimitResult(false, 0, Math.max(0, retryAfter));
                    }
                }
                case TOKEN_BUCKET -> {
                    double elapsed = (nowNs - lastRefill) / 1_000_000_000.0;
                    tokens = Math.min(maxRequests, tokens + elapsed * (maxRequests / (windowMs / 1000.0)));
                    lastRefill = nowNs;
                    if (tokens >= 1.0) {
                        tokens -= 1.0;
                        yield new RateLimitResult(true, (int) tokens, 0);
                    } else {
                        long retryMs = (long)((1.0 - tokens) / (maxRequests / (windowMs / 1000.0)) * 1000);
                        yield new RateLimitResult(false, 0, retryMs);
                    }
                }
            };
        }

        void reset() {
            counter.set(0);
            slidingWindow.clear();
            tokens = maxRequests;
            lastRefill = System.nanoTime();
        }
    }

    // Exercise 4: Graceful Shutdown Manager
    record ShutdownResult(String hookName, boolean success, long durationMs) {}

    static class ShutdownManager {
        private final TreeMap<Integer, List<Map.Entry<String, Runnable>>> hooks = new TreeMap<>();

        void register(String name, int priority, Runnable hook) {
            hooks.computeIfAbsent(priority, k -> new ArrayList<>()).add(Map.entry(name, hook));
        }

        List<ShutdownResult> shutdown(long perHookTimeoutMs) {
            List<ShutdownResult> results = new ArrayList<>();

            for (var entry : hooks.descendingMap().entrySet()) {
                for (var hook : entry.getValue()) {
                    long start = System.currentTimeMillis();
                    try {
                        Thread thread = Thread.ofVirtual().start(hook.getValue());
                        thread.join(perHookTimeoutMs);
                        if (thread.isAlive()) {
                            thread.interrupt();
                            results.add(new ShutdownResult(hook.getKey(), false,
                                System.currentTimeMillis() - start));
                        } else {
                            results.add(new ShutdownResult(hook.getKey(), true,
                                System.currentTimeMillis() - start));
                        }
                    } catch (Exception e) {
                        results.add(new ShutdownResult(hook.getKey(), false,
                            System.currentTimeMillis() - start));
                    }
                }
            }
            return results;
        }
    }

    // Exercise 5: Distributed Tracer
    record Span(String traceId, String spanId, String parentId, String name, long startNs, long endNs) {}

    static class Tracer {
        private final Map<String, Span> spans = new ConcurrentHashMap<>();
        private final Map<String, String> traceRoots = new ConcurrentHashMap<>();

        String startTrace(String name) {
            String traceId = UUID.randomUUID().toString().substring(0, 8);
            String spanId = UUID.randomUUID().toString().substring(0, 8);
            spans.put(spanId, new Span(traceId, spanId, null, name, System.nanoTime(), 0));
            traceRoots.put(traceId, spanId);
            return traceId;
        }

        String startSpan(String traceId, String parentSpanId, String name) {
            String spanId = UUID.randomUUID().toString().substring(0, 8);
            spans.put(spanId, new Span(traceId, spanId, parentSpanId, name, System.nanoTime(), 0));
            return spanId;
        }

        void endSpan(String spanId) {
            Span span = spans.get(spanId);
            if (span != null) {
                spans.put(spanId, new Span(span.traceId(), span.spanId(), span.parentId(),
                    span.name(), span.startNs(), System.nanoTime()));
            }
        }

        List<Span> getTrace(String traceId) {
            return spans.values().stream()
                .filter(s -> s.traceId().equals(traceId))
                .toList();
        }

        void printTraceTree(String traceId) {
            String rootId = traceRoots.get(traceId);
            if (rootId == null) return;
            printSpan(traceId, rootId, 0);
        }

        private void printSpan(String traceId, String spanId, int depth) {
            Span span = spans.get(spanId);
            if (span == null) return;
            String indent = "  ".repeat(depth);
            long durationUs = (span.endNs() - span.startNs()) / 1000;
            System.out.printf("    %s%s [%s] %dμs%n", indent, span.name(), span.spanId(), durationUs);

            spans.values().stream()
                .filter(s -> s.traceId().equals(traceId) && spanId.equals(s.parentId()))
                .forEach(s -> printSpan(traceId, s.spanId(), depth + 1));
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Production Solutions ===\n");

        // Exercise 1
        System.out.println("--- Exercise 1: Circuit Breaker ---");
        MetricsCircuitBreaker cb = new MetricsCircuitBreaker(3, 2000);
        for (int i = 0; i < 6; i++) {
            final int idx = i;
            cb.execute(() -> {
                if (idx < 3) throw new RuntimeException("fail");
                return "ok";
            }, () -> "fallback");
        }
        CircuitMetrics cm = cb.metrics();
        System.out.printf("  State: %s, Calls: %d, Failure rate: %.1f%%%n",
            cm.state(), cm.totalCalls(), cm.failureRate() * 100);

        // Exercise 2
        System.out.println("\n--- Exercise 2: Health Checks ---");
        HealthCheckSystem hcs = new HealthCheckSystem();
        hcs.register("database", () -> true);
        hcs.register("redis", () -> false);
        hcs.register("kafka", () -> true);
        HealthStatus status = hcs.checkAll(5000);
        System.out.println("  Overall: " + status);
        hcs.details().forEach(r -> System.out.printf("    %s: %s (%dms)%n", r.name(), r.status(), r.responseMs()));

        // Exercise 3
        System.out.println("\n--- Exercise 3: Rate Limiter ---");
        MultiStrategyRateLimiter rl = new MultiStrategyRateLimiter(5, 1000, Strategy.SLIDING_WINDOW);
        for (int i = 0; i < 8; i++) {
            RateLimitResult r = rl.tryAcquire();
            System.out.printf("  Request %d: allowed=%s, remaining=%d, retryAfter=%dms%n",
                i + 1, r.allowed(), r.remaining(), r.retryAfterMs());
        }

        // Exercise 4
        System.out.println("\n--- Exercise 4: Shutdown Manager ---");
        ShutdownManager sm = new ShutdownManager();
        sm.register("close-db", 1, () -> System.out.println("  Closing database connections"));
        sm.register("flush-logs", 2, () -> System.out.println("  Flushing log buffers"));
        sm.register("deregister", 3, () -> System.out.println("  Deregistering from service registry"));
        List<ShutdownResult> results = sm.shutdown(5000);
        results.forEach(r -> System.out.printf("    %s: %s (%dms)%n", r.hookName(), r.success() ? "OK" : "FAIL", r.durationMs()));

        // Exercise 5
        System.out.println("\n--- Exercise 5: Distributed Tracer ---");
        Tracer tracer = new Tracer();
        String traceId = tracer.startTrace("http-request");
        String authSpan = tracer.startSpan(traceId, null, "auth-check");
        Thread.sleep(10);
        tracer.endSpan(authSpan);

        String dbSpan = tracer.startSpan(traceId, null, "db-query");
        Thread.sleep(5);
        String sqlSpan = tracer.startSpan(traceId, dbSpan, "sql-execute");
        Thread.sleep(5);
        tracer.endSpan(sqlSpan);
        tracer.endSpan(dbSpan);

        System.out.println("  Trace tree:");
        tracer.printTraceTree(traceId);
        System.out.println("  Spans: " + tracer.getTrace(traceId).size());

        System.out.println("\n=== All Solutions Complete ===");
    }
}
