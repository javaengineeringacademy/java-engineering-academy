# Performance Patterns

## Object Pooling

Object pooling reuses expensive-to-create objects instead of creating new ones each time.

### When to Use

- **Database connections**: Creation involves TCP handshake, authentication
- **Thread creation**: Thread instantiation is expensive (stack allocation)
- **Large buffers**: Byte arrays, NIO buffers
- **Expensive objects**: Parsers, formatters, SSL contexts

### When NOT to Use

- Simple objects (String, Integer, small POJOs)
- Objects that are cheap to create in modern JVMs
- Objects with state that is expensive to reset

### Connection Pool Example

```java
// HikariCP configuration
HikariConfig config = new HikariConfig();
config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
config.setUsername("user");
config.setPassword("pass");
config.setMaximumPoolSize(20);
config.setMinimumIdle(5);
config.setConnectionTimeout(30000);
config.setIdleTimeout(600000);
config.setMaxLifetime(1800000);

HikariDataSource dataSource = new HikariDataSource(config);
```

### Pool Tuning Guidelines

| Parameter | Default | Guidance |
|-----------|---------|----------|
| `maximumPoolSize` | 10 | Set to (cores * 2) for CPU-bound, higher for I/O-bound |
| `minimumIdle` | same as max | For bursty traffic, set lower than max |
| `connectionTimeout` | 30s | Lower for fast-fail, higher for resilience |
| `maxLifetime` | 30m | Set lower than database `wait_timeout` |

## Lazy Initialization

Lazy initialization defers object creation until it is actually needed.

### Eager Initialization

```java
// Created at class load time
private static final expensiveObject = createExpensiveObject();

public static ExpensiveObject get() {
    return expensiveObject;
}
```

### Lazy Initialization

```java
// Created on first access
private static volatile ExpensiveObject instance;

public static ExpensiveObject get() {
    if (instance == null) {
        synchronized (ExpensiveObject.class) {
            if (instance == null) {
                instance = createExpensiveObject();
            }
        }
    }
    return instance;
}
```

### When to Use Lazy

- Object is expensive and may never be used
- Startup time is critical (avoid creating unnecessary objects)
- Memory is constrained

### When to Use Eager

- Object will definitely be used
- Thread safety is important and lazy adds complexity
- Object creation is fast

## Caching Strategies

### Local Cache

In-process cache with no network overhead:

```java
// Caffeine (recommended)
Cache<String, User> cache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .expireAfterWrite(Duration.ofMinutes(5))
    .recordStats()
    .build();

// Loading cache
LoadingCache<String, User> loadingCache = Caffeine.newBuilder()
    .maximumSize(10_000)
    .expireAfterWrite(Duration.ofMinutes(5))
    .build(key -> userDetailsService.loadUser(key));
```

### Distributed Cache

Shared across multiple application instances:

```java
// Redis
RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
    .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(10))
        .serializeValuesWith(RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer())))
    .build();
```

### Cache-Aside Pattern

```
Application → Check Cache → Cache Hit? → Return cached value
                     ↓ (miss)
               Query Database → Store in Cache → Return value
```

### Cache Tuning

- **TTL**: Set based on data freshness requirements
- **Maximum size**: Balance memory vs hit rate
- **Eviction policy**: LRU, LFU, or time-based
- **Warm-up**: Pre-populate cache at startup for critical data

## Batch Processing

Batch processing groups operations to amortize overhead.

### Batch Size Tuning

```java
// JDBC batch inserts
PreparedStatement ps = connection.prepareStatement(sql);
int batchSize = 0;
for (Record record : records) {
    ps.setString(1, record.name);
    ps.setLong(2, record.value);
    ps.addBatch();

    if (++batchSize >= 1000) {
        ps.executeBatch();
        batchSize = 0;
    }
}
if (batchSize > 0) {
    ps.executeBatch();
}
```

### Optimal Batch Sizes

| Operation | Recommended Batch Size |
|-----------|----------------------|
| JDBC inserts | 500-1000 |
| Kafka produces | 100-500 |
| Elasticsearch bulk | 1000-5000 |
| File I/O | 8KB-64KB chunks |

### When to Batch

- Network I/O (reduce round trips)
- Database operations (reduce transaction overhead)
- Message publishing (amortize broker overhead)

## Async Processing

Async processing decouples request handling from response, improving throughput.

### When to Go Async

- Operation is I/O-bound and doesn't need immediate result
- User can tolerate eventual consistency
- Response time SLA allows background processing
- Operation is idempotent and can be retried

### When NOT to Go Async

- User needs immediate confirmation
- Operation has side effects that must be visible immediately
- Error handling requires user interaction
- Adds unnecessary complexity

### Spring Async Example

```java
@Async("taskExecutor")
public CompletableFuture<EmailResult> sendEmail(Email email) {
    // long-running operation
    emailService.send(email);
    return CompletableFuture.completedFuture(new EmailResult(true));
}
```

### Virtual Threads (JDK 21+)

```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<Page>> futures = urls.stream()
        .map(url -> executor.submit(() -> fetchPage(url)))
        .toList();

    for (Future<Page> future : futures) {
        Page page = future.get();
        processPage(page);
    }
}
```

## Compression

Compression reduces data size at the cost of CPU time.

### When to Compress

- Large text payloads (JSON, XML, HTML)
- Data transferred over network frequently
- Storage is expensive relative to CPU
- Data is compressible (text > binary)

### When NOT to Compress

- Data is already compressed (images, video, zip)
- Payload is small (< 1KB)
- CPU is the bottleneck
- Latency is critical (compression adds time)

### Compression Algorithms

| Algorithm | Speed | Ratio | Use Case |
|-----------|-------|-------|----------|
| LZ4 | Fastest | Good | Real-time, network |
| Gzip | Medium | Better | HTTP, storage |
| Zstd | Fast | Best | General purpose |
| Snappy | Fast | Moderate | Hadoop, Kafka |

### HTTP Compression

```java
// Spring Boot
server.compression.enabled=true
server.compression.mime-types=application/json,text/html,text/plain
server.compression.min-response-size=1024
```

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
