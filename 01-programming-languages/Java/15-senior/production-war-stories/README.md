# Production War Stories

## 1. GC Pause → 10-Second Latency Spike

### What Happened
A payment processing service experienced intermittent 10-second latency spikes during peak hours. Users reported transactions timing out, and the monitoring dashboard showed p99 latency jumping from 50ms to 10,000ms. The spikes occurred every 2-3 hours and lasted 10-15 seconds each time.

### Root Cause
The service used G1GC with default settings. During peak traffic, the application allocated objects at 15GB/sec. G1 triggered a Full GC when the heap reached 85% capacity. The Full GC paused all application threads for 10 seconds while it compacted the old generation.

The allocation spike was caused by a new feature that serialized entire order histories into JSON for an audit log. Each order history was 2-5MB, and during peak, thousands were generated per second.

### How to Prevent
```java
// Before: Unbounded serialization
String json = objectMapper.writeValueAsString(orderHistory);

// After: Streaming serialization with size limits
JsonGenerator generator = jsonFactory.createGenerator(outputStream);
generator.writeStartArray();
for (Order order : orderHistory) {
    generator.writeObject(order); // Stream directly to output
}
generator.writeEndArray();
generator.flush(); // No full serialization in memory
```

```bash
# Tune G1GC for predictable pauses
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200          # Target 200ms max pause
-XX:InitiatingHeapOccupancyPercent=45  # Start concurrent GC earlier
-XX:G1HeapRegionSize=16m          # Larger regions for large objects
-XX:G1MixedGCCountTarget=8        # Spread mixed GC over more cycles
```

**Prevention checklist:**
- Profile allocation rates in staging before deployment
- Set `-XX:+PrintGCDetails -Xlog:gc*` and monitor pause times
- Use JMH to benchmark serialization approaches
- Implement allocation budgets in code review

---

## 2. Thread Leak → Service Crash

### What Happened
A notification service ran perfectly for 3 days, then crashed with `OutOfMemoryError: unable to create new native thread`. After restart, it ran for another 3 days before crashing again. The pattern was consistent and predictable.

### Root Cause
A developer added a retry mechanism using `Executors.newFixedThreadPool(10)` inside a method that was called on every request. Each call created a new thread pool but never shut it down. After 3 days of traffic, the service had accumulated 50,000+ threads, exhausting the OS thread limit.

```java
// Leaked code (called per request)
public NotificationResult sendWithRetry(Notification notification) {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    Future<NotificationResult> future = executor.submit(() -> send(notification));
    return future.get(30, TimeUnit.SECONDS);
    // executor never shut down!
}
```

### How to Prevent
```java
// Fixed: Reuse a single thread pool
private final ExecutorService retryExecutor = 
    Executors.newFixedThreadPool(10, new ThreadFactory() {
        private final AtomicInteger count = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "notification-retry-" + count.incrementAndGet());
            t.setDaemon(true);
            return t;
        }
    });

public NotificationResult sendWithRetry(Notification notification) {
    Future<NotificationResult> future = 
        retryExecutor.submit(() -> send(notification));
    return future.get(30, TimeUnit.SECONDS);
}
```

**Prevention checklist:**
- Use static analysis (SpotBugs, ErrorProne) to detect unclosed resources
- Test long-running scenarios (24+ hours) in staging
- Monitor thread count in production (`jstack`, JMX)
- Use try-with-resources for all `ExecutorService` instances

---

## 3. Connection Pool Exhaustion → Database Timeout

### What Happened
An e-commerce service started returning 503 errors during a flash sale. The database was healthy, network was fine, but the application couldn't establish connections. HikariCP logs showed "Connection is not available, request timed out after 30000ms."

### Root Cause
HikariCP was configured with `maximumPoolSize=10` (the default). The application had 200 threads handling requests, each requiring a database connection. During the flash sale, all 10 connections were occupied by long-running queries (full table scans on the orders table). The remaining 190 threads waited for connections, causing cascading timeouts.

Additionally, one endpoint had an unclosed connection:
```java
// Leaked connection
public Order getOrder(long id) {
    Connection conn = dataSource.getConnection();
    PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE id = ?");
    ps.setLong(1, id);
    ResultSet rs = ps.executeQuery();
    // rs and ps closed, but conn never closed!
    return mapToOrder(rs);
}
```

### How to Prevent
```java
// Configuration fix
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(50);          // Match expected concurrency
config.setMinimumIdle(10);              // Keep connections warm
config.setConnectionTimeout(5000);      // Fail fast, don't queue
config.setIdleTimeout(60000);           // Close idle connections
config.setMaxLifetime(1800000);         // Recycle connections
config.setLeakDetectionThreshold(5000); // Log slow connections

// Code fix: Always close connections
public Order getOrder(long id) {
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders WHERE id = ?")) {
        ps.setLong(1, id);
        try (ResultSet rs = ps.executeQuery()) {
            return mapToOrder(rs);
        }
    }
}
```

**Prevention checklist:**
- Set `maximumPoolSize` based on actual concurrency needs
- Enable leak detection (`leakDetectionThreshold`)
- Use try-with-resources for all JDBC resources
- Load test with realistic connection patterns
- Monitor HikariCP metrics (active connections, pending threads)

---

## 4. Serialization Bug → Data Loss

### What Happened
A message queue consumer started failing silently after a deployment. Messages were acked but data wasn't persisted. After 2 hours, 50,000 orders were lost. The producer was sending messages, the consumer was receiving them, but the deserialized objects had null fields.

### Root Cause
The `Order` class implemented `Serializable` and had a `serialVersionUID` explicitly set. A developer added a new field (`discountCode`) without updating the `serialVersionUID`. When the new producer version serialized `Order` objects, the old consumer version couldn't deserialize the new format. Instead of throwing an exception, Java's default serialization returned an object with null fields.

```java
// Producer (v2.1) - new field added
public class Order implements Serializable {
    private static final long serialVersionUID = 1L; // Old value
    private long id;
    private BigDecimal amount;
    private String discountCode; // New field
}

// Consumer (v2.0) - doesn't know about discountCode
// Deserializes without error, but discountCode is null
```

### How to Prevent
```java
// Option 1: Always update serialVersionUID when fields change
public class Order implements Serializable {
    private static final long serialVersionUID = 2L; // Updated!
    // ...
}

// Option 2: Use JSON instead of Java serialization
// Producer
String json = objectMapper.writeValueAsString(order);
kafkaTemplate.send("orders", order.getId().toString(), json);

// Consumer
Order order = objectMapper.readValue(message, Order.class);
// Missing fields get default values, not null

// Option 3: Use Avro/Protobuf with schema evolution
@AvroGenerated
public class Order {
    @AvrifyField(version = 1)
    private long id;
    @AvrifyField(version = 1)
    private BigDecimal amount;
    @AvrifyField(version = 2) // Explicitly versioned
    private String discountCode;
}
```

**Prevention checklist:**
- Never use Java serialization for persistent storage or messaging
- Use schema-based formats (Avro, Protobuf, JSON Schema)
- Implement schema validation in consumers
- Test deserialization with old message versions
- Add monitoring for null field rates

---

## 5. Cascade Failure → 3 Services Down

### What Happened
A user profile service became slow (2-second response times). This caused the order service (which calls the profile service) to queue requests. The queue filled up, causing the order service to reject new requests. The notification service (which depends on orders) started failing because it couldn't create order confirmations. Within 15 minutes, three services were down.

### Root Cause
- No circuit breakers between services
- No timeouts on HTTP calls (used default 30-second timeout)
- No bulkhead pattern (shared thread pool for all dependencies)
- No retry budget (infinite retries on failure)

```java
// Dangerous: No timeout, no circuit breaker
public UserProfile getProfile(long userId) {
    return restTemplate.getForObject(
        "http://profile-service/users/" + userId, 
        UserProfile.class
    );
    // Default timeout: 30 seconds
    // No circuit breaker
    // No fallback
}
```

### How to Prevent
```java
// Circuit breaker with Resilience4j
CircuitBreakerConfig config = CircuitBreakerConfig.custom()
    .failureRateThreshold(50)
    .waitDurationInOpenState(Duration.ofSeconds(10))
    .slidingWindowSize(10)
    .build();

CircuitBreaker breaker = CircuitBreaker.of("profileService", config);

Supplier<UserProfile> decoratedSupplier = CircuitBreaker
    .decorateSupplier(breaker, () -> getProfileDirect(userId));

// With timeout
UserProfile profile = TimeLimiter
    .of("profileService", Duration.ofSeconds(2))
    .executeFutureSupplier(() -> 
        CompletableFuture.supplyAsync(decoratedSupplier)
    );

// Fallback
public UserProfile getProfile(long userId) {
    try {
        return getProfileDirect(userId);
    } catch (Exception e) {
        return fallbackProfile(userId); // Cached or default
    }
}
```

**Prevention checklist:**
- Add circuit breakers to all inter-service calls
- Set explicit timeouts (connect: 5s, read: 10s)
- Implement bulkheads (separate thread pools per dependency)
- Add retry budgets (max 3 retries, exponential backoff)
- Create runbooks for cascade failure scenarios

---

## 6. Dirty Read → Wrong Data

### What Happened
An accounting service showed incorrect balances for 200 customers. The amounts were off by $0.01 to $5.00. After investigation, the data was actually correct in the database, but the application read intermediate states during concurrent updates.

### Root Cause
The accounting service used `READ_UNCOMMITTED` isolation level for performance. When two transactions updated the same account simultaneously, one transaction could read the other's uncommitted changes. If the first transaction rolled back, the second transaction had already processed stale data.

```java
// Dangerous isolation level
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public Account getAccount(long id) {
    return accountRepository.findById(id).orElseThrow();
}
```

The specific scenario:
1. Transaction A: Debit $100 (uncommitted)
2. Transaction B (read): Sees balance = original - $100
3. Transaction A: Rollback (insufficient funds)
4. Transaction B: Already processed with wrong balance

### How to Prevent
```java
// Use appropriate isolation level
@Transactional(isolation = Isolation.READ_COMMITTED)  // Minimum safe level
public Account getAccount(long id) {
    return accountRepository.findById(id).orElseThrow();
}

// For strong consistency (financial operations)
@Transactional(isolation = Isolation.REPEATABLE_READ)
public void transfer(long fromId, long toId, BigDecimal amount) {
    Account from = accountRepository.findById(fromId).orElseThrow();
    Account to = accountRepository.findById(toId).orElseThrow();
    
    from.debit(amount);
    to.credit(amount);
    
    accountRepository.save(from);
    accountRepository.save(to);
}
```

**Prevention checklist:**
- Never use `READ_UNCOMMITTED` for financial data
- Use `READ_COMMITTED` minimum, `REPEATABLE_READ` for transactions
- Implement optimistic locking for concurrent updates
- Add reconciliation jobs to detect inconsistencies
- Test concurrent access patterns in staging

---

## 7. Thread Starvation → Request Timeout

### What Happened
An API gateway started timing out requests after 30 seconds. The gateway had 200 threads, but only 20 were available for processing. The rest were blocked waiting for downstream services. Users experienced timeouts on 80% of requests.

### Root Cause
The gateway used a fixed thread pool of 200 threads for all operations. A downstream service (recommendation engine) started responding slowly (5-10 seconds per request). The 200 threads were quickly consumed by slow requests, leaving no threads for fast operations (health checks, authentication, cached responses).

```java
// Single thread pool for everything
ExecutorService executor = Executors.newFixedThreadPool(200);

// All requests compete for same threads
public Response handleRequest(Request request) {
    Future<Response> future = executor.submit(() -> {
        if (needsRecommendation(request)) {
            return callRecommendationEngine(request); // 5-10 seconds
        }
        return cachedResponse(request); // 10ms
    });
    return future.get(30, TimeUnit.SECONDS);
}
```

### How to Prevent
```java
// Separate thread pools by operation type
ExecutorService fastPool = Executors.newFixedThreadPool(50);   // Health, auth, cache
ExecutorService slowPool = Executors.newFixedThreadPool(100);  // External calls
ExecutorService criticalPool = Executors.newFixedThreadPool(20); // Payment, orders

// Route requests to appropriate pool
public Response handleRequest(Request request) {
    if (isHealthCheck(request)) {
        return fastPool.submit(() -> healthCheck()).get(1, TimeUnit.SECONDS);
    } else if (isExternalCall(request)) {
        return slowPool.submit(() -> callExternal(request)).get(10, TimeUnit.SECONDS);
    } else {
        return criticalPool.submit(() -> processRequest(request)).get(5, TimeUnit.SECONDS);
    }
}

// Better: Use virtual threads (Java 21+)
ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
```

**Prevention checklist:**
- Separate thread pools by dependency and latency
- Set aggressive timeouts for non-critical operations
- Monitor thread pool utilization per category
- Use virtual threads for I/O-bound workloads
- Implement load shedding for degraded scenarios

---

## 8. Cache Stampede → Database Overload

### What Happened
A product catalog service experienced a "thundering herd" when a popular product's cache expired. 10,000 concurrent requests all missed the cache simultaneously, hitting the database with 10,000 identical queries. The database CPU spiked to 100%, response times increased from 5ms to 5 seconds, and the service became unresponsive.

### Root Cause
The caching layer had no request coalescing. When the cache key expired, every request that checked the cache got a miss and independently queried the database. There was no mechanism to deduplicate concurrent requests for the same data.

```java
// Dangerous: No request coalescing
public Product getProduct(long id) {
    Product cached = cache.get("product:" + id);
    if (cached != null) {
        return cached;
    }
    
    // 10,000 threads all execute this simultaneously
    Product product = productRepository.findById(id).orElseThrow();
    cache.put("product:" + id, product, Duration.ofMinutes(5));
    return product;
}
```

### How to Prevent
```java
// Solution 1: Request coalescing with CompletableFuture
private final ConcurrentHashMap<Long, CompletableFuture<Product>> inflight = 
    new ConcurrentHashMap<>();

public Product getProduct(long id) {
    Product cached = cache.get("product:" + id);
    if (cached != null) {
        return cached;
    }
    
    CompletableFuture<Product> future = inflight.computeIfAbsent(id, 
        key -> CompletableFuture.supplyAsync(() -> {
            try {
                Product product = productRepository.findById(key).orElseThrow();
                cache.put("product:" + key, product, Duration.ofMinutes(5));
                return product;
            } finally {
                inflight.remove(key);
            }
        })
    );
    
    return future.join();
}

// Solution 2: Cache-aside with refresh-ahead
public Product getProduct(long id) {
    Product cached = cache.get("product:" + id);
    if (cached != null) {
        // Refresh in background if near expiry
        if (cache.getTtl("product:" + id) < Duration.ofMinutes(1)) {
            refreshAsync(id);
        }
        return cached;
    }
    return loadAndCache(id);
}

private void refreshAsync(long id) {
    CompletableFuture.runAsync(() -> loadAndCache(id));
}
```

**Prevention checklist:**
- Implement request coalescing for hot keys
- Use refresh-ahead caching for predictable expiry patterns
- Set staggered TTLs to avoid synchronized expiry
- Monitor cache hit rates and stampede detection
- Load test cache expiry scenarios

---

## Summary

| War Story | Impact | Prevention |
|-----------|--------|------------|
| GC Pause | 10s latency spikes | Tune GC, reduce allocation rate |
| Thread Leak | Service crash every 3 days | Static analysis, long-running tests |
| Connection Pool | 503 errors during peak | Proper pool sizing, leak detection |
| Serialization | 50K orders lost | Use JSON/Protobuf, schema validation |
| Cascade Failure | 3 services down | Circuit breakers, timeouts, bulkheads |
| Dirty Read | Wrong financial data | Proper isolation level, reconciliation |
| Thread Starvation | 80% request timeouts | Separate thread pools, virtual threads |
| Cache Stampede | Database overload | Request coalescing, refresh-ahead |
