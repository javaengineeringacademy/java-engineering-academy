# Java Production War Stories

Real-world production incidents and their solutions. Each story includes the investigation process, root cause analysis, fix, and prevention strategies.

## Stories Overview

| Story | Impact | Root Cause | Fix |
|-------|--------|------------|-----|
| [OutOfMemoryError](#1-outofmemoryerror---silent-memory-leak) | Service crashes every 4 hours | Unbounded cache without eviction | TTL-based cache with cleanup |
| [Deadlock](#2-deadlock---silent-service-freeze) | Complete service freeze | Inconsistent lock ordering | Consistent lock ordering + tryLock |
| [High Latency](#3-high-latency---gc-pause-investigation) | 500ms latency spikes every 30s | G1GC Full GC cycles | ZGC + allocation rate reduction |
| [Connection Leak](#4-connection-leak---slow-death) | DB connections exhausted after 6 hours | Unclosed connections in error paths | try-with-resources + leak detection |
| [Thread Starvation](#5-thread-starvation---queue-overflow) | 80% request timeouts | Single thread pool for all operations | Separate pools + virtual threads |

---

## 1. OutOfMemoryError - Silent Memory Leak

### Scenario
A caching service started crashing every 4 hours during peak traffic. The heap dump showed 95% old generation usage, but no single object dominated.

### Investigation Process
1. Enable heap dump on OOM: `-XX:+HeapDumpOnOutOfMemoryError`
2. Analyze heap dump with Eclipse MAT
3. Check Dominator Tree for largest objects
4. Look for collections that grow but never shrink

### Root Cause
Custom `ConcurrentHashMap` cache stored user session objects but never evicted expired entries. Each session was 50KB, and with 100K active users, the cache grew to 5GB over time.

### Key Code Pattern
```java
// BUG: No TTL, no eviction, no cleanup!
ConcurrentHashMap<String, UserSession> sessions = new ConcurrentHashMap<>();

// FIX: Add scheduled cleanup
ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
cleanupExecutor.scheduleAtFixedRate(this::cleanupExpiredSessions, 60, 60, TimeUnit.SECONDS);
```

### Prevention
- Use bounded caches with TTL (Caffeine, Guava Cache)
- Enable leak detection in connection pools
- Monitor heap usage trends and set alerts
- Regular profiling in staging environment

---

## 2. Deadlock - Silent Service Freeze

### Scenario
An order processing service became completely unresponsive. All 200 threads were blocked, but no errors appeared in logs. The service appeared healthy but processed zero requests.

### Investigation Process
1. Take thread dump: `jstack <pid>` or `kill -3 <pid>`
2. Look for threads in BLOCKED state
3. Identify lock ownership and wait chains
4. Find circular dependencies (A waits for B, B waits for A)

### Root Cause
Two methods acquired locks in opposite orders:
- `processOrder()`: orderLock → inventoryLock
- `updateInventory()`: inventoryLock → orderLock

When called concurrently, they deadlocked.

### Key Code Pattern
```java
// BUG: Inconsistent lock ordering
void processOrder() {
    synchronized (orderLock) {
        synchronized (inventoryLock) { /* ... */ }
    }
}

void updateInventory() {
    synchronized (inventoryLock) {  // Wrong order!
        synchronized (orderLock) { /* ... */ }
    }
}

// FIX: Consistent lock ordering
void updateInventory() {
    synchronized (orderLock) {  // Same order as processOrder
        synchronized (inventoryLock) { /* ... */ }
    }
}

// BETTER: Use tryLock with timeout
if (orderLock.tryLock(1, TimeUnit.SECONDS)) {
    try {
        if (inventoryLock.tryLock(1, TimeUnit.SECONDS)) {
            try { /* ... */ } finally { inventoryLock.unlock(); }
        }
    } finally { orderLock.unlock(); }
}
```

### Prevention
- Always acquire locks in consistent order
- Use `tryLock` with timeout instead of `synchronized`
- Minimize lock scope and duration
- Consider lock-free alternatives (ConcurrentHashMap)
- Enable JMX for thread monitoring

---

## 3. High Latency - GC Pause Investigation

### Scenario
A trading platform experienced intermittent 500ms latency spikes. The spikes occurred every 30 seconds and coincided with Full GC cycles.

### Investigation Process
1. Enable JFR recording: `jcmd <pid> JFR.start duration=60s filename=recording.jfr`
2. Analyze GC events in JDK Mission Control
3. Check GC pause times and frequency
4. Identify allocation hotspots

### Root Cause
G1GC with default settings on 16GB heap:
- High allocation rate (100K objects/second)
- Default IHOP (45%) causing late concurrent marking
- Mixed collections couldn't keep up with old gen growth

### Key JVM Flags
```bash
# Bad: Default G1GC
-XX:+UseG1GC

# Good: Tuned G1GC
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:InitiatingHeapOccupancyPercent=45
-XX:G1HeapRegionSize=16m

# Better: ZGC (Java 15+)
-XX:+UseZGC
-XX:+ZGenerational  # Java 21+
```

### Prevention
- Use ZGC for latency-sensitive services
- Reduce allocation rate in hot paths
- Monitor GC pause times, not just heap usage
- Profile allocation rates in staging

---

## 4. Connection Leak - Slow Death

### Scenario
A reporting service ran fine for hours, then started timing out. After 6 hours, all database connections were exhausted.

### Investigation Process
1. Check HikariCP metrics: active connections, pending threads
2. Enable leak detection: `leakDetectionThreshold=5000`
3. Analyze thread dumps for JDBC connection holders
4. Use connection pool monitoring to find leak patterns

### Root Cause
A reporting endpoint had an unclosed connection in an error path. When queries failed, the connection was never returned to the pool.

### Key Code Pattern
```java
// BUG: Connection leaked in error path
public String generateReport(String type) {
    Connection conn = pool.borrowConnection();
    try {
        // ... work ...
        return result;
    } catch (Exception e) {
        throw new RuntimeException(e);  // Connection leaked!
    }
}

// FIX: Always return in finally
public String generateReport(String type) {
    Connection conn = null;
    try {
        conn = pool.borrowConnection();
        // ... work ...
        return result;
    } finally {
        pool.returnConnection(conn);  // Always return
    }
}

// BETTER: Try-with-resources
public String generateReport(String type) {
    try (Connection conn = pool.borrowConnection();
         PreparedStatement ps = conn.prepareStatement("...")) {
        // ... work ...
        return result;
    }
}
```

### HikariCP Configuration
```java
config.setLeakDetectionThreshold(5000);  // Log slow connections
config.setConnectionTimeout(30000);      // Fail fast
config.setMaxLifetime(1800000);          // Recycle connections
```

### Prevention
- Always use try-with-resources for JDBC
- Enable leak detection in HikariCP
- Monitor connection pool metrics
- Set appropriate pool size based on concurrency

---

## 5. Thread Starvation - Queue Overflow

### Scenario
An API gateway started timing out requests after 30 seconds. The gateway had 200 threads, but only 20 were available for processing.

### Investigation Process
1. Check thread pool metrics: active threads, queue size
2. Analyze thread dumps for blocked/waiting threads
3. Identify slow downstream services consuming threads
4. Measure actual latency distribution per endpoint

### Root Cause
Single fixed thread pool for all operations. A slow downstream service (5-10 seconds) consumed all threads, leaving none for fast operations.

### Key Code Pattern
```java
// BUG: Single thread pool for everything
ExecutorService executor = Executors.newFixedThreadPool(200);
// All requests compete for same threads

// FIX: Separate pools by latency class
ExecutorService fastPool = Executors.newFixedThreadPool(50);    // Auth, health
ExecutorService slowPool = Executors.newFixedThreadPool(100);   // External calls
ExecutorService criticalPool = Executors.newFixedThreadPool(20); // Payments

// BETTER: Virtual threads (Java 21+)
ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
// Scales to millions of threads, perfect for I/O-bound work
```

### Thread Pool Sizing
- CPU-bound: `N` CPU cores
- I/O-bound: `N * (1 + wait_time/service_time)`
- Mixed: Separate pools per operation type

### Prevention
- Separate thread pools by latency class
- Use virtual threads for I/O-bound work
- Implement circuit breakers for slow services
- Set aggressive timeouts for non-critical operations
- Add backpressure to prevent queue overflow

---

## General Investigation Tools

### Thread Dump Analysis
```bash
# Take thread dump
jstack <pid>
kill -3 <pid>
jcmd <pid> Thread.print

# Analyze blocked threads
jstack <pid> | grep -A 5 'BLOCKED\|WAITING'
```

### Heap Dump Analysis
```bash
# Trigger heap dump
jmap -dump:live,format=b,file=heap.hprof <pid>
jcmd <pid> GC.heap_dump /tmp/heap.hprof

# Enable on OOM
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdumps
```

### JFR Recording
```bash
# Start recording
jcmd <pid> JFR.start name=profile duration=60s filename=profile.jfr

# Continuous recording
jcmd <pid> JFR.start name=continuous settings=profile
```

### GC Logging
```bash
# Java 11+
-Xlog:gc*:file=gc.log:time,uptime,level,tags
-Xlog:gc+heap=debug
-Xlog:gc+phases=debug
```

---

## Monitoring Checklist

### JVM Metrics
- [ ] GC pause duration (alert if > 500ms)
- [ ] Heap usage by generation
- [ ] Thread count and states
- [ ] CPU usage per thread

### Application Metrics
- [ ] Request latency (p50, p95, p99)
- [ ] Error rates by type
- [ ] Thread pool utilization
- [ ] Connection pool metrics

### Infrastructure Metrics
- [ ] Database connection count
- [ ] Network I/O
- [ ] Disk I/O
- [ ] Memory pressure

---

## Prevention Principles

1. **Measure before optimizing** — Profile in staging, not production
2. **Fail fast** — Set timeouts on all external calls
3. **Isolate failures** — Use bulkheads and circuit breakers
4. **Monitor everything** — You can't fix what you can't see
5. **Test failure modes** — Chaos engineering in staging
6. **Document runbooks** — Know how to fix issues before they happen
7. **Review configs** — Pool sizes, timeouts, GC settings
8. **Load test regularly** — Validate assumptions under load

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
