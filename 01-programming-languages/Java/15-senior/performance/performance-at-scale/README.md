# Performance at Scale

## What Does 1M Requests/Sec Look Like?

### Hardware Requirements
- 50-100 application nodes (8-16 cores each)
- Load balancer tier (L7 or L4 depending on SSL termination)
- 10-20 database replicas (read replicas for query distribution)
- Redis/Memcached cluster for hot data caching
- Message queue (Kafka) with 3+ brokers for async processing

### Network Requirements
- 10 Gbps+ NICs on application servers
- Low-latency switching (<100μs within datacenter)
- Geographic distribution for latency-sensitive workloads
- CDN for static assets (offload 60-80% of traffic)

### Memory Requirements
- 32-64 GB heap per application node
- Off-heap for large caches or memory-mapped data
- OS page cache utilization (leave 25-30% of RAM for OS)
- Connection pools: 50-200 connections per database node

### Throughput Math
```
1M req/sec ÷ 100 nodes = 10,000 req/sec/node
10,000 req/sec × 50ms avg latency = 500 concurrent requests/node
500 concurrent × 4 threads = 2,000 threads/node
```

---

## Handling 10TB of Data in Memory

### Off-Heap Memory
```java
// Using Unsafe for direct memory allocation
Unsafe unsafe = getUnsafe();
long address = unsafe.allocateMemory(1024 * 1024); // 1MB
try {
    // Use memory directly
    unsafe.putByte(address, (byte) 42);
} finally {
    unsafe.freeMemory(address);
}

// Using ByteBuffer (safer alternative)
ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
buffer.putInt(42);
buffer.flip();
```

### Memory-Mapped Files
```java
// Map file directly into virtual address space
FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
MappedByteBuffer buffer = channel.map(
    FileChannel.MapMode.READ_ONLY, 0, channel.size()
);
// OS handles paging automatically
// No explicit read() calls needed
```

### Chunking Strategy
```java
// Process large files in manageable chunks
try (Stream<String> lines = Files.lines(path)) {
    lines.chunk(10_000)
         .forEach(chunk -> processBatch(chunk));
}
```

### When to Use What
| Approach | Use Case | Risk |
|----------|----------|------|
| Off-heap | Large caches, bypass GC | Manual memory management |
| Memory-mapped | File-based datasets | May cause page faults under pressure |
| Chunking | Streaming large files | Higher latency per chunk |
| External storage (Redis) | Shared state across nodes | Network overhead |

---

## When GC Matters

### P99 Latency Impact
- **G1GC**: Typically 10-50ms pauses for mixed GC
- **ZGC**: Sub-millisecond pauses (99th percentile <1ms)
- **Shenandoah**: Similar to ZGC, concurrent compaction
- **Throughput GC**: Can have multi-second pauses

### Allocation Rate Thresholds
```
Low allocation:    < 1 GB/sec   → GC rarely matters
Medium allocation: 1-10 GB/sec  → Monitor pause times
High allocation:   > 10 GB/sec  → Consider off-heap or object pooling
```

### When GC Matters Most
- Latency-sensitive services (payment processing, real-time bidding)
- High-throughput systems with tight SLA requirements
- Applications with large heap (>32GB)
- Systems with frequent full GC cycles

### When GC Doesn't Matter
- Batch processing with no latency requirements
- Services with generous SLA (>1s acceptable)
- Short-lived jobs that exit before GC pressure builds

---

## JMH Methodology

### Warmup Configuration
```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2) // Fork twice to avoid JIT bias
@State(Scope.Thread)
public class MyBenchmark {
    @Benchmark
    public void testMethod(Blackhole blackhole) {
        // Code under test
        blackhole.consume(result);
    }
}
```

### Key Pitfalls
- **Dead code elimination**: Use `Blackhole.consume()` or return results
- **Constant folding**: Ensure inputs vary across iterations
- **Benchmark in isolation**: Run with `-prof gc` to check allocation
- **Account for JIT**: Minimum 5 warmup iterations

### Profilers
```bash
# GC profiler
java -jar benchmarks.jar -prof gc

# Stack profiler
java -jar benchmarks.jar -prof stack

# Flight Recorder (production-safe)
java -XX:StartFlightRecording=filename=recording.jfr -jar benchmarks.jar
```

---

## Profiling Workflow

### Step-by-Step
1. **Establish baseline**: Run load test, capture metrics (latency, throughput, CPU)
2. **Identify bottleneck**: async-profiler CPU/lock profiling
3. **Generate flame graph**: Visualize call stacks
4. **Locate hotspot**: Find methods consuming most CPU
5. **Optimize**: Refactor, cache, or parallelize
6. **Re-measure**: Confirm improvement, check for regressions

### async-profiler Usage
```bash
# CPU profiling (wall-clock)
./profiler.sh -d 30 -f cpu_profile.html -o flamegraph <pid>

# Lock profiling (detect contention)
./profiler.sh -d 30 -e lock -f lock_profile.html <pid>

# Allocation profiling
./profiler.sh -d 30 -e alloc -f alloc_profile.html <pid>
```

### Flame Graph Interpretation
- Wide bars = methods consuming most CPU
- Deep stacks = potential recursion or deep call chains
- `jvm_*` frames = JVM overhead (GC, compilation, safepoints)
- Look for unexpected methods in hot paths

---

## When to Optimize vs. When to Add Hardware

### Cost Analysis Framework
```
Optimization Cost = Developer Time × Hourly Rate
Hardware Cost = Monthly Server Cost × Duration

If Optimization Cost < Hardware Cost (6-month horizon) → Optimize
If Optimization Cost > Hardware Cost → Add hardware
```

### Decision Matrix
| Scenario | Action | Rationale |
|----------|--------|-----------|
| CPU-bound, clear hotspot | Optimize | High ROI, measurable improvement |
| CPU-bound, no clear hotspot | Add hardware | Micro-optimizations yield little |
| Memory-bound | Add hardware | Usually cheaper than refactoring |
| I/O-bound | Add hardware + caching | Optimize I/O patterns |
| Latency-sensitive (p99) | Optimize | Hardware won't fix GC pauses |

### Real-World Example
- **Optimization**: 2 weeks dev time ($10K) → saves 4 servers ($2K/month)
  - Break-even: 5 months → Worth it if servers needed >5 months
- **Add hardware**: $500/month × 6 months = $3K
  - Faster to implement, no risk of introducing bugs

---

## Cost of a Millisecond

### Amazon's Famous Number
- 100ms latency improvement = $160M/year in revenue
- 1ms = ~$1.6M/year

### Other Industry Benchmarks
- **Google**: 500ms delay = 20% reduction in search volume
- **Yahoo**: 400ms delay = 5-9% drop in page views
- **Netflix**: 1 second delay = 5% increase in cancellations
- **Retail**: 100ms delay = 1% reduction in sales

### Calculation for Your Service
```
Annual Revenue = $X
Requests per Year = Y
Value per Request = $X / Y
Latency Sensitivity = Conversion drop per ms (typically 0.1-1%)

Example:
$100M revenue / 10B requests = $0.01 per request
1% conversion drop per 100ms = $0.0001 per ms per request
At 10B requests: $1M per ms per year
```

---

## Performance Budget Per Request

### CPU Budget
```
Typical web request budget: 10-50ms total CPU time
├── Framework overhead:     2-5ms
├── Authentication:         1-3ms
├── Business logic:         5-20ms
├── Database queries:       5-15ms
├── Cache lookups:          1-2ms
├── Serialization:          2-5ms
└── Network I/O:           (async, not counted in CPU budget)
```

### Memory Budget
```
Per-request allocation:
├── Request object:        ~1KB
├── Response object:       ~2KB
├── Temporary buffers:     ~5KB
├── Database result set:   ~10KB (varies widely)
└── Total:                 ~18KB per request

At 100K req/sec = 1.8 GB/sec allocation rate
```

### Network Budget
```
External API call budget:
├── DNS resolution:        1-10ms (cached: 0ms)
├── TCP handshake:         1-5ms (keep-alive: 0ms)
├── TLS handshake:         10-50ms (resumed: 0ms)
├── Request transmission:  1-5ms
├── Server processing:     variable
└── Response reception:    1-5ms

Total per external call: 15-75ms (first), 2-10ms (subsequent)
```

### How to Measure
```java
// Timing budget enforcement
@Around("@annotation(Timed)")
public Object enforceBudget(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.nanoTime();
    Object result = pjp.proceed();
    long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    
    if (elapsed > BUDGET_MS) {
        log.warn("Budget exceeded: {}ms (limit: {}ms)", elapsed, BUDGET_MS);
        metrics.record("budget.exceeded", 1);
    }
    return result;
}
```

---

## Key Takeaways

1. 1M req/sec is achievable with proper architecture and hardware distribution
2. 10TB in memory requires off-heap or memory-mapped approaches
3. GC only matters when you have latency requirements that demand it
4. JMH provides microbenchmark correctness; async-profiler provides production insight
5. Profile → Flame graph → Hotspot → Optimize → Re-measure
6. Add hardware when optimization cost exceeds infrastructure cost
7. Every millisecond has a measurable business impact
8. Performance budgets keep teams accountable and systems predictable

## Interview Questions

1. **How do you calculate the hardware needed for 1M requests/second?**
   Start with throughput math: 1M req/sec ÷ nodes = req/sec per node. With 50ms avg latency, each request holds a thread for 50ms. Concurrent requests = req/sec × latency = 10K × 0.05 = 500. With 4 threads per core, need 125 cores per node. Add 30% headroom. Database: if each query takes 5ms, a single DB handles 200 queries/sec, so need 5000 read replicas for 1M reads/sec.

2. **When does GC pause time matter vs throughput?**
   GC matters for latency-sensitive services (payments, real-time bidding, trading). A 200ms G1 pause on a p99 latency SLA of 100ms causes SLA breaches. For batch processing, throughput matters more — Parallel GC's multi-second pauses are acceptable if total throughput is highest. Decision matrix: latency SLA <10ms → ZGC/Shenandoah; throughput-focused → Parallel GC; balanced → G1.

3. **How do you decide between optimizing code vs adding hardware?**
   Formula: Optimization Cost = Dev Time × Rate. Hardware Cost = Monthly Cost × Months. If optimization cost < hardware cost over 6 months → optimize. If hardware is cheaper → add hardware. Exception: latency-sensitive systems require optimization regardless (GC pauses can't be fixed by adding servers). Also consider: optimization reduces future cloud bills permanently.

4. **Explain the cost of a millisecond for an e-commerce site.**
   Amazon: 100ms delay = $160M/year revenue loss. For your service: Annual Revenue / Requests per Year = Value per Request. If $100M revenue / 10B requests = $0.01/request. With 0.1% conversion drop per 100ms = $1M/ms/year. Calculate: Revenue × (Conversion drop % / Latency sensitivity ms). This justifies performance engineering investment.

5. **What is a performance budget and how do you enforce it?**
   A performance budget allocates time to each layer: 5ms auth + 20ms business logic + 15ms DB + 5ms serialization = 45ms total. Enforce via: (1) AOP timing annotations; (2) Circuit breakers that fail fast on slow calls; (3) Load testing in CI/CD; (4) Real-time dashboards with budget alerts. When budget exceeded, alert team before production impact.

6. **How do you handle 10TB of data that must be in memory?**
   Options: (1) Off-heap memory via `ByteBuffer.allocateDirect()` or `Unsafe` — bypasses GC; (2) Memory-mapped files (`FileChannel.map()`) — OS handles paging; (3) External storage (Redis cluster) — distributed memory; (4) Chunked streaming — process 10K records at a time. Trade-off: off-heap avoids GC but requires manual memory management; memory-mapped may cause page faults under pressure.

## Pitfalls

```java
// PITFALL 1: Over-provisioning application nodes without load testing
// Assuming 1 node = 10K req/sec without measuring actual throughput

// PITFALL 2: Synchronous calls in request path
// Each external API call adds 50-200ms latency
// Fix: Use CompletableFuture.allOf() for parallel calls

// PITFALL 3: Ignoring connection pool sizing
// Too few connections → request queuing; too many → DB overload

// PITFALL 4: Not setting timeouts
// One slow backend service degrades entire system
// Fix: Set connect timeout (5s), read timeout (30s), circuit breaker

// PITFALL 5: Logging every request at DEBUG level
// 1M req/sec × 1KB log = 1GB/sec disk I/O

// PITFALL 6: Not monitoring GC in production
// A single Full GC can pause all threads for 5+ seconds
```

## Performance

### Latency Budget Example
```
Total SLA: 100ms
├── Network (client → server):    10ms
├── Auth + Rate limiting:          5ms
├── Business logic:               20ms
├── Database query (cached):       1ms
├── Database query (uncached):    15ms
├── Serialization:                 5ms
├── Network (server → client):    10ms
└── Buffer (headroom):            34ms
```

### Throughput Capacity Planning
```
1M req/sec across 100 nodes:
├── Each node: 10K req/sec
├── CPU per core: 3-5K req/sec (typical)
├── Cores per node needed: 2-3 cores (plus headroom)
├── Recommended: 8 cores per node (50% utilization target)
├── Memory per node: 4-8GB heap
└── Total: 800 cores, 400-800GB RAM
```

### Cloud Cost Comparison (AWS us-east-1)
```
Optimized application (100 nodes, c6i.2xlarge):
  100 × $0.34/hr × 730hr = $24,820/month

Unoptimized (200 nodes, c6i.2xlarge):
  200 × $0.34/hr × 730hr = $49,640/month

Savings from optimization: $24,820/month
```

## Examples

```java
// Performance budget enforcement with AOP
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Timed {
    long budgetMs() default 50;
}

@Aspect
@Component
public class PerformanceBudgetAspect {
    @Around("@annotation(timed)")
    public Object enforceBudget(ProceedingJoinPoint pjp, Timed timed) throws Throwable {
        long start = System.nanoTime();
        Object result = pjp.proceed();
        long elapsed = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        if (elapsed > timed.budgetMs()) {
            log.warn("Budget exceeded: {}ms (limit: {}ms) in {}",
                elapsed, timed.budgetMs(), pjp.getSignature().toShortString());
            metrics.record("budget.exceeded", 1);
        }
        return result;
    }
}

// Parallel external calls for throughput
CompletableFuture<User> userFuture = CompletableFuture.supplyAsync(() -> userService.getUser(id));
CompletableFuture<List<Order>> ordersFuture = CompletableFuture.supplyAsync(() -> orderService.getOrders(id));
CompletableFuture.allOf(userFuture, ordersFuture).join();
// Both calls run in parallel — total time = max(t1, t2) not t1 + t2

// Off-heap memory for large caches
ByteBuffer cache = ByteBuffer.allocateDirect(10 * 1024 * 1024 * 1024L); // 10GB
cache.putInt(0, 42); // Write at offset 0
int value = cache.getInt(0); // Read from offset 0
// Not subject to GC — must manage manually
```

## Internal Working

### Request Processing Pipeline
```
Client Request
    ↓
Load Balancer (L7) → health check, routing
    ↓
Application Server
    ├── Thread Pool (virtual or platform threads)
    ├── Request parsing + auth
    ├── Business logic execution
    ├── Database query (or cache hit)
    └── Response serialization
    ↓
Network → Client Response
```

### Memory Management at Scale
1. **Heap sizing**: `-Xmx4g` for most workloads; >32GB uses compressed oops
2. **Off-heap**: Bypass GC for large caches, manual memory management
3. **Memory-mapped files**: OS page cache handles paging, virtual address space
4. **Object pooling**: Reuse expensive objects (connections, buffers)

## Why This Concept Exists

Performance at scale exists because modern applications serve millions of users simultaneously. At scale: (1) Small inefficiencies multiply into massive costs (1ms × 1M req = 1000 seconds of CPU per day); (2) Latency directly impacts revenue (Amazon's $160M/ms); (3) Hardware costs scale linearly with poor optimization; (4) User experience degrades non-linearly with latency. The discipline combines profiling, benchmarking, architecture design, and cost analysis to build systems that perform efficiently at any scale.

## Overview

Performance at scale is the discipline of building and operating systems that handle millions of requests per second while maintaining acceptable latency, reliability, and cost efficiency. It encompasses capacity planning (how many nodes do you need), GC tuning (minimizing pause times), profiling (finding bottlenecks), load testing (validating assumptions), and cost optimization (balancing performance vs infrastructure spend). Key principle: measure, don't guess — use JMH for microbenchmarks and async-profiler for production profiling.

## References

- [Java Performance by Scott Oaks](https://www.oreilly.com/library/view/java-performance/9781492056102/)
- [Google SRE Book](https://sre.google/sre-book/table-of-contents/)
- [The Art of Capacity Planning by John Allspaw](https://www.oreilly.com/library/view/the-art-of/9780596518578/)
- [async-profiler](https://github.com/async-profiler/async-profiler)
- [JMH Documentation](https://openjdk.org/projects/code-tools/jmh/)
