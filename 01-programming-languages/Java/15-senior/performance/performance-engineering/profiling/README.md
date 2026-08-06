# Profiling Techniques

## CPU Profiling

CPU profiling identifies which methods consume the most CPU time. This is essential for optimizing compute-bound applications.

### async-profiler

The most accurate CPU profiler for production use. Uses async signals to sample the stack trace without stopping JVM threads:

```bash
# Profile for 30 seconds, generate flame graph
./profiler.sh -d 30 -f flamegraph.html <pid>

# Profile CPU and alloc (combined)
./profiler.sh -d 30 -e cpu,alloc -f combined.html <pid>

# Profile specific threads
./profiler.sh -d 30 -t -f threads.html <pid>
```

**Advantages**: Near-zero overhead, production-safe, no safepoint bias, generates flame graphs.

### JProfiler

Commercial profiler with GUI and CLI. Good for interactive analysis:

```bash
# Attach to running JVM
jpenable --port=8849

# Remote profiling
jprofiler -r <host>:8849
```

### VisualVM

Free, bundled with JDK. Good for quick analysis:

```bash
# Start VisualVM
visualvm

# Connect to local or remote JVM
# Use Sampler or Profiler tab
```

### When to Use CPU Profiling

- Application is CPU-bound (high CPU, low I/O wait)
- Response time is slow but CPU utilization is high
- Optimizing hot methods after load testing
- Identifying unexpected CPU consumption

## Memory Profiling

Memory profiling helps identify memory leaks, excessive allocation, and heap bloat.

### Heap Dump Analysis

```bash
# Trigger heap dump
jcmd <pid> GC.heap_dump /tmp/heap.hprof

# Or on OutOfMemoryError
java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/ MyApp

# Analyze with Eclipse MAT
# Open heap.hprof → Leak Suspects → Top Consumers
```

### Eclipse Memory Analyzer Tool (MAT)

Key features:
- **Leak Suspects Report**: Automatically identifies potential leaks
- **Dominator Tree**: Shows objects retaining the most memory
- **Histogram**: Count and size of all object types
- **OQL**: SQL-like queries for heap objects

```sql
-- Find all String objects larger than 1000 chars
SELECT s.@objectId, s.count, s.value.@length
FROM java.lang.String s
WHERE s.value.@length > 1000
```

### VisualVM Memory Profiling

- Heap Dump: Capture and browse the heap
- Sampler → Memory: Real-time allocation tracking
- GC Monitor: Visualize GC activity

### When to Use Memory Profiling

- Application memory usage grows over time (memory leak)
- Frequent Full GC pauses
- OutOfMemoryError exceptions
- High heap usage relative to live data

## Thread Profiling

Thread profiling identifies contention, deadlocks, and thread pool issues.

### Thread Dump Analysis

```bash
# Capture thread dump
jstack <pid> > thread_dump.txt

# Or via jcmd
jcmd <pid> Thread.print > thread_dump.txt

# Multiple dumps over time for contention analysis
for i in {1..5}; do
    jstack <pid> > thread_dump_$i.txt
    sleep 5
done
```

### What to Look For

1. **BLOCKED threads**: Thread waiting to acquire a monitor
2. **Deadlock detection**: `jstack` reports deadlocks automatically
3. **Thread pool saturation**: All threads in WAITING/TIMED_WAITING
4. **Lock contention**: Multiple threads competing for same lock

### Tools

- **fastthread.io**: Online thread dump analyzer
- **TDA (Thread Dump Analyzer)**: GUI tool for thread dumps
- **VisualVM**: Thread tab shows live thread state

### When to Use Thread Profiling

- Application throughput drops under load
- High CPU but low request completion rate
- Response times increase with concurrency
- Suspected deadlocks or livelocks

## Allocation Profiling

Allocation profiling tracks where objects are created on the heap. Critical for reducing GC pressure.

### TeraPipe

High-performance allocation profiler using hardware performance counters:

```bash
# Profile allocations
./terapipe -alloc -d 30 <pid>
```

### Allocation Profiler (async-profiler)

```bash
# Profile allocations
./profiler.sh -d 30 -e alloc -f alloc.html <pid>
```

### Key Metrics

- **Allocation rate**: Objects/second being created
- **Allocation hotspots**: Methods creating the most objects
- **TLAB allocations**: Thread-Local Allocation Buffer usage
- **Direct allocations**: Off-heap memory (ByteBuffer, etc.)

### When to Use Allocation Profiling

- High GC frequency or long GC pauses
- Application slows down after running for hours
- Tuning GC parameters (G1, ZGC, Shenandoah)
- Reducing memory footprint

## Profiling Workflow

A systematic approach ensures you measure, not guess:

```
┌─────────────┐
│  1. IDENTIFY │  What is slow? (metrics, logs, user reports)
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  2. PROFILE  │  Measure the specific bottleneck
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  3. ANALYZE  │  Understand the root cause
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  4. OPTIMIZE │  Apply targeted fix
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  5. VERIFY   │  Confirm improvement with benchmarks
└─────────────┘
```

### Step 1: Identify

- Monitor application metrics (latency percentiles, throughput, error rates)
- Check logs for warnings and errors
- Review user reports and SLA breaches
- Use APM tools (Datadog, New Relic, Dynatrace)

### Step 2: Profile

- Choose the right profiler for the bottleneck type
- Run profiler in a controlled environment (staging) or production
- Collect enough data for statistical significance
- Compare against baseline

### Step 3: Analyze

- Examine flame graphs for CPU bottlenecks
- Review allocation sites for memory issues
- Check thread dumps for contention
- Identify the critical path

### Step 4: Optimize

- Make one change at a time
- Use JMH to validate micro-optimizations
- Consider architectural changes for systemic issues
- Document the optimization and rationale

### Step 5: Verify

- Run load tests to confirm improvement
- Compare metrics before and after
- Ensure no regression in other areas
- Update performance baselines

## References

- [async-profiler](https://github.com/async-profiler/async-profiler)
- [Eclipse MAT](https://eclipse.dev/mat/)
- [VisualVM](https://visualvm.github.io/)
- [JDK Mission Control](https://openjdk.org/projects/jmc/)
