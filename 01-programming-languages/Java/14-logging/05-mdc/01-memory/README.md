# Memory Considerations in MDC

## ThreadLocal Memory Model

```
Thread 1: [MDC Map: {requestId: "abc", userId: "user1"}]
Thread 2: [MDC Map: {requestId: "def", userId: "user2"}]
Thread 3: [MDC Map: {requestId: "ghi", userId: "user3"}]

Each thread has its OWN map instance.
Memory grows linearly with active threads.
```

## Memory Per Thread

```java
// Typical MDC entry: ~100-200 bytes
// Key: ~50 bytes (String object + chars)
// Value: ~50-150 bytes (String object + chars)

// Example request context:
MDC.put("requestId", "550e8400-e29b-41d4-a716-446655440000"); // ~100 bytes
MDC.put("userId", "user-12345"); // ~80 bytes
MDC.put("sessionId", "sess-67890"); // ~80 bytes
MDC.put("service", "order-service"); // ~70 bytes

// Total per thread: ~330 bytes
// 100 concurrent requests: ~33 KB
// 10,000 concurrent requests: ~3.3 MB
```

## Thread Pool Memory

```java
// FixedThreadPool with 100 threads
// Each thread retains MDC values until cleared
// If not cleaned: memory persists for thread lifetime

// LEAK:
ExecutorService pool = Executors.newFixedThreadPool(100);
pool.submit(() -> {
    MDC.put("requestId", UUID.randomUUID().toString());
    processRequest();
    // NOT cleaned → requestId stays in ThreadLocal
});
// After 1000 requests: 1000 × 330 bytes = 330 KB leaked

// CORRECT:
pool.submit(() -> {
    MDC.put("requestId", UUID.randomUUID().toString());
    try {
        processRequest();
    } finally {
        MDC.clear(); // Reclaims memory
    }
});
// After 1000 requests: ~33 KB (only current threads)
```

## Large Value Risk

```java
// DANGEROUS: Storing large objects in MDC
MDC.put("requestBody", largeJsonString); // Could be MBs
MDC.put("resultSet", resultSet.toString()); // Could be huge

// This memory is PER-THREAD and persists until cleared
// Can cause OutOfMemoryError with many concurrent threads

// SAFE: Store only identifiers
MDC.put("requestId", requestId);
MDC.put("userId", userId);
// Log the full data separately if needed
```

## Cleanup Strategy

```java
// Pattern 1: Try-finally (always use this)
try {
    MDC.put("key", value);
    doWork();
} finally {
    MDC.clear(); // ALWAYS in finally
}

// Pattern 2: Wrapper (for reusable code)
public static void withMdc(Map<String, String> context, Runnable task) {
    MDC.setContextMap(context);
    try {
        task.run();
    } finally {
        MDC.clear();
    }
}

// Pattern 3: Spring's RequestContextHolder (for web apps)
// Spring manages MDC lifecycle automatically
```

## Best Practices Summary

| Practice | Memory Impact |
|----------|--------------|
| Always clean MDC in finally | Prevents ThreadLocal leaks |
| Store only identifiers | Limits per-thread memory |
| Use fixed-size thread pools | Bounds total MDC memory |
| Clear before returning to pool | Reclaims memory immediately |
| Avoid large values | Prevents memory bloat |
| Monitor thread count | More threads = more MDC memory |
