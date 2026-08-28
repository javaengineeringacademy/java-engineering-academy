# Production Patterns

## Overview

Production-ready applications require reliable patterns for reliability, observability, and graceful operation. This guide covers essential patterns for building resilient distributed systems in Java.

---

## Graceful Shutdown

### JVM Shutdown Hooks

```java
public class Application {
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final Server server = new Server();

    public static void main(String[] args) {
        // Register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook triggered");
            
            // 1. Stop accepting new requests
            server.stop();
            
            // 2. Wait for in-flight requests to complete
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
            
            // 3. Close resources
            server.close();
            
            System.out.println("Shutdown complete");
        }));

        // Start application
        server.start();
    }
}
```

### Spring Boot Shutdown

```java
@Component
public class GracefulShutdown implements DisposableBean, ApplicationListener<ContextClosedEvent> {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final Server server;

    @Override
    public void destroy() {
        // Stop accepting new work
        server.pause();
        
        // Wait for in-flight requests
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        destroy();
    }
}
```

### Docker/Container Shutdown

```dockerfile
# Use SIGTERM (default) instead of SIGKILL
STOPSIGNAL SIGTERM

# Or handle specific signal
CMD ["java", "-jar", "app.jar"]
# Docker sends SIGTERM, application has 30s to shutdown
```

---

## Health Checks

### Liveness vs Readiness

```java
// Liveness: Is the application alive?
@Component
public class LivenessHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try {
            // Check critical components
            database.ping();
            return Health.up().build();
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
    }
}

// Readiness: Is the application ready to serve traffic?
@Component
public class ReadinessHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        if (!isWarmedUp()) {
            return Health.down().withDetail("reason", "Warming up").build();
        }
        if (isOverloaded()) {
            return Health.down().withDetail("reason", "Overloaded").build();
        }
        return Health.up().build();
    }
}
```

### Custom Health Checks

```java
@Component
public class DependencyHealthCheck {
    private final HealthIndicator databaseHealth;
    private final HealthIndicator cacheHealth;
    private final HealthIndicator externalServiceHealth;

    public Health check() {
        Map<String, Health> checks = new HashMap<>();
        
        checks.put("database", databaseHealth.health());
        checks.put("cache", cacheHealth.health());
        checks.put("externalService", externalServiceHealth.health());
        
        boolean allHealthy = checks.values().stream()
            .allMatch(h -> h.getStatus() == Status.UP);
        
        if (allHealthy) {
            return Health.up().withDetails(checks).build();
        } else {
            return Health.down().withDetails(checks).build();
        }
    }
}
```

### Health Check Endpoint

```java
@RestController
@RequestMapping("/health")
public class HealthController {
    private final DependencyHealthCheck healthCheck;

    @GetMapping
    public ResponseEntity<Health> check() {
        Health health = healthCheck.check();
        HttpStatus status = health.getStatus() == Status.UP 
            ? HttpStatus.OK 
            : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(health);
    }
}
```

---

## Circuit Breakers

### Resilience4j Circuit Breaker

```java
@Service
public class ExternalServiceClient {
    private final CircuitBreaker circuitBreaker;
    private final Retry retry;

    public ExternalServiceClient() {
        this.circuitBreaker = CircuitBreaker.ofDefaults("externalService");
        this.retry = Retry.ofDefaults("externalService");
    }

    public String callExternalService() {
        return Try.ofSupplier(() -> 
            CircuitBreaker.decorateSupplier(circuitBreaker, this::doCall)
                .compose(Retry.decorateSupplier(retry, this::doCall))
                .get()
        )
        .recover(CallNotPermittedException.class, e -> "Fallback: Circuit open")
        .recover(TimeoutException.class, e -> "Fallback: Timeout")
        .recover(RuntimeException.class, e -> "Fallback: Error")
        .get();
    }

    private String doCall() {
        // Actual HTTP call
        return restTemplate.getForObject("https://api.example.com/data", String.class);
    }
}
```

### Circuit Breaker States

```java
// CLOSED: Normal operation, requests pass through
// OPEN: Failure threshold exceeded, requests fail fast
// HALF_OPEN: Testing if service recovered

CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)           // 50% failure rate opens circuit
    .waitDurationInOpenState(Duration.ofSeconds(30))
    .ringBufferSizeInHalfOpenState(10)
    .ringBufferSizeInClosedState(100)
    .recordExceptions(IOException.class, TimeoutException.class)
    .ignoreExceptions(BusinessException.class)
    .build();

CircuitBreaker circuitBreaker = CircuitBreaker.of("service", config);
```

### Fallback Patterns

```java
// Try with fallback
String result = Try.ofSupplier(() -> externalService.call())
    .recover(RuntimeException.class, e -> fallbackService.call())
    .get();

// Bulkhead + Circuit Breaker
Bulkhead bulkhead = Bulkhead.of("service", BulkheadConfig.custom()
    .maxConcurrentCalls(25)
    .maxWaitDuration(Duration.ofMillis(500))
    .build());

Supplier<String> decoratedSupplier = Decorators.ofSupplier(this::callExternalService)
    .withCircuitBreaker(circuitBreaker)
    .withBulkhead(bulkhead)
    .withRetry(retry)
    .decorate();

String result = Try.ofSupplier(decoratedSupplier)
    .recover(RuntimeException.class, e -> "Fallback")
    .get();
```

---

## Rate Limiting

### Token Bucket Algorithm

```java
public class TokenBucket {
    private final long capacity;
    private final double refillRate;
    private double tokens;
    private long lastRefillTime;

    public TokenBucket(long capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.nanoTime();
    }

    public synchronized boolean tryConsume() {
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
```

### Resilience4j RateLimiter

```java
RateLimiterConfig config = RateLimiterConfig.custom()
    .limitForPeriod(100)                    // 100 requests
    .limitRefreshPeriod(Duration.ofSeconds(1))  // per second
    .timeoutDuration(Duration.ofMillis(500))    // wait timeout
    .build();

RateLimiter rateLimiter = RateLimiter.of("api", config);

// Use with decorated supplier
Supplier<String> decoratedSupplier = Decorators.ofSupplier(this::callApi)
    .withRateLimiter(rateLimiter)
    .decorate();

String result = Try.ofSupplier(decoratedSupplier)
    .recover(RequestNotPermitted.class, e -> "Rate limit exceeded")
    .get();
```

### Guava RateLimiter

```java
// Smooth bursty: allows burst up to permitsPerSecond
RateLimiter rateLimiter = RateLimiter.create(100.0);  // 100 permits/second

// Smooth warming up: gradually increases rate
RateLimiter rateLimiter = RateLimiter.create(100.0, 1, TimeUnit.MINUTES);

// Usage
if (rateLimiter.tryAcquire()) {
    // Process request
} else {
    // Reject with 429 Too Many Requests
}
```

---

## Connection Pooling

### HikariCP Configuration

```java
@Configuration
public class DataSourceConfig {
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        config.setUsername("user");
        config.setPassword("password");
        
        // Pool sizing
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        
        // Timeouts
        config.setConnectionTimeout(30000);  // 30 seconds
        config.setIdleTimeout(600000);       // 10 minutes
        config.setMaxLifetime(1800000);      // 30 minutes
        
        // Leak detection
        config.setLeakDetectionThreshold(60000);  // 60 seconds
        
        // Performance
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        return new HikariDataSource(config);
    }
}
```

### Connection Pool Monitoring

```java
@Component
public class ConnectionPoolMonitor {
    private final HikariDataSource dataSource;

    @Scheduled(fixedRate = 60000)
    public void logPoolStats() {
        HikariPoolMXBean poolMXBean = dataSource.getHikariPoolMXBean();
        
        log.info("Connection pool stats: " +
            "Active={}, Idle={}, Waiting={}, Total={}",
            poolMXBean.getActiveConnections(),
            poolMXBean.getIdleConnections(),
            poolMXBean.getThreadsAwaitingConnection(),
            poolMXBean.getTotalConnections());
    }
}
```

---

## Retry with Backoff

### Exponential Backoff

```java
public class RetryWithBackoff {
    private final int maxRetries;
    private final long initialDelayMs;
    private final double multiplier;

    public RetryWithBackoff(int maxRetries, long initialDelayMs, double multiplier) {
        this.maxRetries = maxRetries;
        this.initialDelayMs = initialDelayMs;
        this.multiplier = multiplier;
    }

    public <T> T execute(Callable<T> operation) throws Exception {
        Exception lastException = null;
        
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return operation.call();
            } catch (Exception e) {
                lastException = e;
                
                if (attempt < maxRetries) {
                    long delay = (long) (initialDelayMs * Math.pow(multiplier, attempt));
                    log.warn("Attempt {} failed, retrying in {} ms", attempt + 1, delay);
                    Thread.sleep(delay);
                }
            }
        }
        
        throw lastException;
    }
}
```

### Resilience4j Retry

```java
RetryConfig config = RetryConfig.custom()
    .maxAttempts(3)
    .waitDuration(Duration.ofMillis(500))
    .intervalFunction(IntervalFunction.ofExponentialBackoff(500, 2))
    .retryExceptions(IOException.class, TimeoutException.class)
    .ignoreExceptions(BusinessException.class)
    .build();

Retry retry = Retry.of("service", config);

// Decorate supplier
Supplier<String> decoratedSupplier = Decorators.ofSupplier(this::callService)
    .withRetry(retry)
    .decorate();

String result = decoratedSupplier.get();
```

### Circuit Breaker + Retry

```java
// Combine patterns for maximum resilience
Supplier<String> decoratedSupplier = Decorators.ofSupplier(this::callExternalService)
    .withCircuitBreaker(circuitBreaker)
    .withBulkhead(bulkhead)
    .withRateLimiter(rateLimiter)
    .withRetry(retry)
    .decorate();

String result = Try.ofSupplier(decoratedSupplier)
    .recover(CallNotPermittedException.class, e -> "Circuit open")
    .recover(RequestNotPermitted.class, e -> "Rate limited")
    .recover(BulkheadFullException.class, e -> "Bulkhead full")
    .recover(TimeoutException.class, e -> "Timeout")
    .get();
```

---

## Summary

| Pattern | Purpose | Tool |
|---------|---------|------|
| **Graceful Shutdown** | Clean shutdown on SIGTERM | Shutdown hooks, DisposableBean |
| **Health Checks** | Monitor application health | Liveness/Readiness probes |
| **Circuit Breaker** | Prevent cascade failures | Resilience4j, Hystrix |
| **Rate Limiting** | Control request rate | Token bucket, Resilience4j |
| **Connection Pooling** | Manage database connections | HikariCP, Druid |
| **Retry with Backoff** | Handle transient failures | Resilience4j, custom |

## Interview Questions

1. **What is the difference between a liveness and readiness health check?**
   Liveness: "Is the application alive?" — checks if the process is running and critical components are functional. If it fails, the container is restarted. Readiness: "Is the application ready to serve traffic?" — checks if the application can handle requests (dependencies available, warmed up). If it fails, the pod is removed from load balancer.

2. **How does a circuit breaker prevent cascade failures?**
   A circuit breaker monitors failures and trips (opens) when failure rate exceeds threshold (e.g., 50%). When open, all requests fail fast without calling the downstream service. After a wait duration, it enters half-open state and allows a test request. If it succeeds, the circuit closes; if it fails, it stays open. This prevents one failing service from consuming all resources.

3. **What is the thundering herd problem and how do you prevent it?**
   Thundering herd occurs when many requests hit a resource simultaneously after it becomes available (e.g., cache expiry, service recovery). Prevention: request coalescing (singleflight), staggered TTLs for caches, exponential backoff with jitter, and rate limiting. Without prevention, 10K cache misses can overwhelm the database.

4. **How do you size a database connection pool?**
   Formula: `pool_size = (2 × number_of_cores) + effective_spindle_count`. For most applications: (2 × 8) + 1 = 17 connections. Monitor `hikaricp_connections_pending` — if > 0, increase pool size. Set `connectionTimeout` to fail fast (5-10 seconds). Never set pool size > database `max_connections`.

5. **What is the difference between rate limiting algorithms?**
   Token Bucket: allows bursts up to bucket capacity, best for APIs. Fixed Window: simple count per window, boundary issues (2x burst). Sliding Window Log: most accurate, memory-intensive. Sliding Window Counter: weighted average, best balance of accuracy and efficiency. Token Bucket is the most common for API rate limiting.

## Pitfalls

**Forgetting to close resources in shutdown hooks:**
```java
// BAD: Resources not closed in shutdown hook
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    server.stop();
    // Database connection pool not closed
    // Message consumer not stopped
    // Cache not flushed
}));

// GOOD: Close all resources in order
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    try { server.stop(); } catch (Exception e) { log.error("Server stop failed", e); }
    try { messageConsumer.stop(); } catch (Exception e) { log.error("Consumer stop failed", e); }
    try { connectionPool.close(); } catch (Exception e) { log.error("Pool close failed", e); }
    try { cache.flush(); } catch (Exception e) { log.error("Cache flush failed", e); }
}));
```

**Checking database in liveness probe:**
```java
// BAD: Liveness probe checks database
@Component
public class LivenessHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        database.ping(); // If DB is down, restart won't fix it
        return Health.up().build();
    }
}

// GOOD: Liveness checks only internal state
@Component
public class LivenessHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        // Only check things that restart CAN fix
        if (isDeadlocked()) return Health.down().build();
        if (isOutOfMemory()) return Health.down().build();
        return Health.up().build();
    }
}
```

**Not using circuit breaker with retry:**
```java
// BAD: Retrying without circuit breaker
// If service is down, all retries hit it, consuming resources
for (int i = 0; i < 3; i++) {
    try {
        return callService(); // 3 requests hit failing service
    } catch (Exception e) {
        Thread.sleep(1000);
    }
}

// GOOD: Circuit breaker + retry
Supplier<String> decorated = Decorators.ofSupplier(this::callService)
    .withCircuitBreaker(circuitBreaker) // Fast-fail when service is down
    .withRetry(retry) // Retry when circuit is closed
    .decorate();
```

## Performance

**Production Pattern Performance Overhead:**

| Pattern | Latency Overhead | Memory Overhead | CPU Overhead |
|---------|------------------|-----------------|--------------|
| Health Check | 0.1-1ms | 1MB | <1% |
| Circuit Breaker | 0.01ms | 10KB | <0.1% |
| Rate Limiter | 0.05ms | 1KB | <0.1% |
| Retry (with backoff) | 500ms-5s (delay) | 1KB | <0.1% |
| Connection Pool | 0.5-1ms | 50-200MB | 1-2% |
| Graceful Shutdown | 10-30s (drain) | None | 0% |

**Resilience4j Performance:**
```
Circuit Breaker:
- State transition: <1ms
- Call decoration: 0.01ms overhead
- Memory per instance: 10KB

Rate Limiter:
- Token check: 0.05ms
- Throughput: 1M+ checks/second
- Memory per instance: 1KB
```

## Internal Working

**Circuit Breaker State Machine:**
1. **CLOSED**: Normal operation. Requests pass through. Failures are counted.
2. **OPEN**: Failure threshold exceeded. All requests fail fast immediately.
3. **HALF_OPEN**: Wait duration elapsed. Limited requests allowed to test recovery.
4. **Transition**: If test request succeeds → CLOSED. If fails → OPEN.

**HikariCP Connection Lifecycle:**
1. Application requests connection from pool
2. Pool checks for idle connection (ready to use)
3. If no idle, pool creates new connection (if below max)
4. If at max, pool waits for connection (connectionTimeout)
5. Connection is assigned to application
6. Application uses connection for queries
7. Connection returned to pool (not closed)
8. Idle connections closed after idleTimeout

**Graceful Shutdown Sequence:**
1. SIGTERM received
2. Application stops accepting new connections/requests
3. In-flight requests continue processing
4. Health check returns NOT_READY (removed from load balancer)
5. Pending tasks complete or timeout expires
6. Resources closed (connections, caches, consumers)
7. JVM exits

## Why This Concept Exists

Production patterns exist because:

1. **Networks are unreliable**: HTTP calls fail, databases timeout, caches expire
2. **Services fail**: Bugs, memory leaks, resource exhaustion happen
3. **Traffic is unpredictable**: Flash sales, viral events, DDoS attacks
4. **Dependencies fail**: Third-party services, databases, message queues go down
5. **Users expect reliability**: 99.9% uptime = 8.76 hours downtime/year
6. **Cost of failure**: Lost revenue, damaged reputation, SLA penalties

The patterns (circuit breaker, rate limiter, health check, retry, connection pool, graceful shutdown) provide battle-tested solutions to these production challenges.

## Overview

Production patterns are battle-tested solutions for building resilient distributed systems in Java. They cover graceful shutdown (clean termination), health checks (liveness/readiness), circuit breakers (failure isolation), rate limiting (traffic control), connection pooling (resource management), and retry with backoff (transient failure handling). These patterns are implemented using Resilience4j, HikariCP, and Spring Boot Actuator.

## References

- Resilience4j documentation: https://resilience4j.readme.io/
- HikariCP GitHub: https://github.com/brettwooldridge/HikariCP
- Spring Boot Actuator: https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html
- "Release It!" by Michael Nygard — Production patterns
- "Building Microservices" by Sam Newman — Chapter on resilience
- Kubernetes health probes: https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/
