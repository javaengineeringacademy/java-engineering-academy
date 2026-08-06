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
