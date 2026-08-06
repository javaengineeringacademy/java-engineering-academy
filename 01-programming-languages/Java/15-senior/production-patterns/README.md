# Production Patterns

## Overview

Production-ready applications require robust patterns for reliability, observability, and graceful operation. This guide covers essential patterns for building resilient distributed systems in Java.

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
