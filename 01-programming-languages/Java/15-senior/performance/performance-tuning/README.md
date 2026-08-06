# Java Performance Tuning

## Overview

Performance tuning is the process of optimizing Java applications for speed, throughput, and resource efficiency. It involves understanding JVM internals, profiling bottlenecks, and applying targeted optimizations.

---

## JVM Tuning Flags

### Memory Flags

```bash
# Heap sizing
-Xms4g                    # Initial heap size
-Xmx4g                    # Maximum heap size (set equal to -Xms)

# Metaspace
-XX:MaxMetaspaceSize=256m # Maximum metaspace size
-XX:MetaspaceSize=128m    # Initial metaspace size

# Thread stack
-Xss512k                  # Thread stack size

# Direct memory (NIO)
-XX:MaxDirectMemorySize=1g
```

### GC Flags

```bash
# Algorithm selection
-XX:+UseG1GC              # G1 GC (default Java 9+)
-XX:+UseZGC               # ZGC (Java 15+)
-XX:+UseShenandoahGC      # Shenandoah (Java 15+)

# Pause time
-XX:MaxGCPauseMillis=200  # Target max pause

# GC logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags
```

### JIT Compilation Flags

```bash
# Tiered compilation (default)
-XX:+TieredCompilation
-XX:TieredStopAtLevel=4   # Maximum compilation level

# Compilation thresholds
-XX:CompileThreshold=10000  # Method invocation threshold

# Aggressive optimization
-XX:+AggressiveOpts
-XX:+UseCompressedOops      # Use compressed object pointers
```

---

## Heap Sizing

### Guidelines

```bash
# Rule of thumb: Set -Xms = -Xmx
-Xms4g -Xmx4g

# Young generation: 25-40% of heap
-XX:NewRatio=2            # Old:Young = 2:1
-XX:NewSize=1g            # Initial young gen
-XX:MaxNewSize=1g         # Maximum young gen

# Survivor spaces
-XX:SurvivorRatio=8       # Eden:Survivor = 8:1
```

### Monitoring Heap Usage

```bash
# JConsole
jconsole <pid>

# jstat
jstat -gcutil <pid> 1000

# Heap dump
jmap -dump:format=b,file=heap.hprof <pid>
```

---

## GC Selection

### Decision Matrix

| Workload | Recommended GC | Rationale |
|----------|----------------|-----------|
| Batch processing | Parallel GC | Maximize throughput |
| Web application | G1 GC | Balance throughput/latency |
| Real-time system | ZGC/Shenandoah | Ultra-low latency |
| Embedded device | Serial GC | Minimal overhead |

### G1 GC Tuning

```bash
# G1-specific flags
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m
-XX:InitiatingHeapOccupancyPercent=45
-XX:G1MixedGCCountTarget=8
```

### ZGC Tuning

```bash
# ZGC-specific flags
-XX:+UseZGC
-XX:SoftMaxHeapSize=4g
-XX:ConcGCThreads=4
```

---

## Thread Pool Tuning

### Core Concepts

```java
// ThreadPoolExecutor configuration
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,      // Minimum threads
    maximumPoolSize,   // Maximum threads
    keepAliveTime,     // Idle timeout
    TimeUnit.SECONDS,  // Time unit
    workQueue,         // Task queue
    threadFactory,     // Thread factory
    rejectionHandler   // Rejection policy
);
```

### Tuning Guidelines

```java
// CPU-bound tasks
int cpuThreads = Runtime.getRuntime().availableProcessors();
ThreadPoolExecutor cpuPool = new ThreadPoolExecutor(
    cpuThreads,
    cpuThreads,
    60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000)
);

// I/O-bound tasks
int ioThreads = cpuThreads * 2;
ThreadPoolExecutor ioPool = new ThreadPoolExecutor(
    ioThreads,
    ioThreads * 2,
    60L, TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(10000)
);
```

### Monitoring Thread Pools

```java
// Monitor pool metrics
ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();
monitor.scheduleAtFixedRate(() -> {
    System.out.println("Active: " + executor.getActiveCount());
    System.out.println("Pool size: " + executor.getPoolSize());
    System.out.println("Queue size: " + executor.getQueue().size());
    System.out.println("Completed: " + executor.getCompletedTaskCount());
}, 0, 1, TimeUnit.SECONDS);
```

---

## JIT Compilation Optimization

### How JIT Works

1. **Interpreter**: Executes bytecode directly (slow)
2. **C1 Compiler**: Client compiler, quick optimization
3. **C2 Compiler**: Server compiler, aggressive optimization
4. **Tiered Compilation**: Combines C1 and C2

### Optimization Techniques

```java
// Method inlining
public int calculate(int a, int b) {
    return a + b;  // Inlined: eliminates method call overhead
}

// Escape analysis
public void process() {
    Point p = new Point(1, 2);  // May be allocated on stack
    System.out.println(p.x + p.y);
}

// Loop unrolling
for (int i = 0; i < 100; i++) {
    process(i);  // May be unrolled for performance
}
```

### JIT Flags

```bash
# Print compilation logs
-XX:+PrintCompilation

# Control inlining
-XX:MaxInlineSize=35
-XX:FreqInlineSize=325

# Aggressive optimizations
-XX:+AggressiveOpts
-XX:+UseLoopPredicate
-XX:+OptimizeFill
```

---

## Profiling Tools

### JProfiler

```bash
# Start profiling
jprofiler -pid <pid>

# Features:
# - CPU profiling
# - Memory profiling
# - Thread profiling
# - Lock contention analysis
# - GC root analysis
```

### VisualVM

```bash
# Launch VisualVM
visualvm

# Connect to process
visualvm --openpid <pid>

# Features:
# - Real-time monitoring
# - Heap dumps
# - Thread dumps
# - Sampler
# - Profiler
```

### async-profiler

```bash
# CPU profiling
./profiler.sh -d 30 -f cpu_profile.html <pid>

# Memory allocation profiling
./profiler.sh -d 30 -e alloc -f alloc_profile.html <pid>

# Wall-clock profiling
./profiler.sh -d 30 -e wall -f wall_profile.html <pid>
```

### JFR (Java Flight Recorder)

```bash
# Start recording
jcmd <pid> JFR.start name=profile duration=60s filename=profile.jfr

# Continuous recording
jcmd <pid> JFR.start name=profile settings=profile filename=profile.jfr
```

---

## Common Performance Issues

### 1. Excessive Garbage Collection

```java
// Problem: Creating too many objects
public String concatenate(List<String> items) {
    String result = "";
    for (String item : items) {
        result += item;  // Creates new String each iteration
    }
    return result;
}

// Solution: Use StringBuilder
public String concatenate(List<String> items) {
    StringBuilder sb = new StringBuilder();
    for (String item : items) {
        sb.append(item);
    }
    return sb.toString();
}
```

### 2. Lock Contention

```java
// Problem: Excessive synchronization
public class Counter {
    private int count = 0;
    
    public synchronized void increment() {
        count++;
    }
}

// Solution: Use atomic variables
public class Counter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    public void increment() {
        count.incrementAndGet();
    }
}
```

### 3. Memory Leaks

```java
// Problem: Unclosed resources
public void readFile(String path) throws IOException {
    InputStream is = new FileInputStream(path);
    // ... process file
    // Resource not closed!
}

// Solution: Use try-with-resources
public void readFile(String path) throws IOException {
    try (InputStream is = new FileInputStream(path)) {
        // ... process file
    }
}
```

### 4. Inefficient Collections

```java
// Problem: Using wrong collection
List<String> list = new ArrayList<>();
// Frequent contains() checks: O(n)

// Solution: Use HashSet for fast lookups
Set<String> set = new HashSet<>();
// contains() is O(1)
```

---

## Performance Checklist

- [ ] Set `-Xms` = `-Xmx`
- [ ] Choose appropriate GC algorithm
- [ ] Monitor GC logs regularly
- [ ] Profile before optimizing
- [ ] Use appropriate data structures
- [ ] Avoid unnecessary object creation
- [ ] Use try-with-resources
- [ ] Minimize lock contention
- [ ] Use thread pools appropriately
- [ ] Cache expensive computations
- [ ] Optimize database queries
- [ ] Use async I/O where appropriate

---

## Summary

| Area | Key Actions |
|------|-------------|
| **JVM Flags** | Tune heap, GC, JIT flags |
| **Heap Sizing** | Set -Xms = -Xmx, tune young gen |
| **GC Selection** | Match GC to workload |
| **Thread Pools** | Size based on workload type |
| **JIT** | Understand tiered compilation |
| **Profiling** | Use JProfiler, VisualVM, JFR |
| **Monitoring** | GC logs, heap dumps, thread dumps |

## Interview Questions

1. **How do you find a memory leak?** — Take heap dumps with `jmap` or JFR, compare snapshots over time, look for objects with increasing counts that should be collected.

2. **What is the difference between throughput and latency tuning?** — Throughput: maximize work per time unit (Parallel GC). Latency: minimize pause times (ZGC, G1). They often trade off against each other.

3. **When should you set `-Xms` equal to `-Xmx`?** — Always for production. It prevents heap resizing pauses during GC. The JVM grows/shrinks the heap between these bounds, causing latency spikes.

4. **How do you tune GC pauses?** — Use G1 with `-XX:MaxGCPauseMillis=200`, or ZGC for sub-10ms pauses. Tune region size and initiating heap occupancy percentage.

5. **What is the first thing to check when an application is slow?** — Profile first. Don't guess. Use JFR or async-profiler to find the actual bottleneck before optimizing.

6. **What is false sharing and how do you fix it?** — Threads modifying variables on the same cache line cause cache bouncing. Fix with `@Contended` annotation or padding fields.

## Pitfalls

1. **Premature optimization**: Optimizing without profiling wastes time on non-bottlenecks
2. **Wrong GC choice**: Using Parallel GC for latency-sensitive apps causes long pauses
3. **Ignoring GC logs**: Not monitoring GC in production hides memory issues until they are critical
4. **Over-tuning flags**: Too many JVM flags create configuration drift — change one at a time
5. **Not testing under load**: Benchmarks without realistic traffic miss real-world issues

## Examples

```java
// Before: String concatenation in loop (O(n²))
public String badConcatenate(List<String> items) {
    String result = "";
    for (String item : items) {
        result += item;
    }
    return result;
}

// After: StringBuilder (O(n))
public String goodConcatenate(List<String> items) {
    StringBuilder sb = new StringBuilder(items.size() * 10);
    for (String item : items) {
        sb.append(item);
    }
    return sb.toString();
}

// Before: Lock contention
public class BadCounter {
    private int count = 0;
    public synchronized void increment() { count++; }
}

// After: Lock-free atomic
public class GoodCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    public void increment() { count.incrementAndGet(); }
}
```

## Internal Working

Performance tuning involves three JVM subsystems: memory (heap sizing, GC), JIT compilation (tiered compilation, inlining), and threading (pool sizing, contention). The profiling pipeline: measure → identify bottleneck → optimize → re-measure. Tools like JFR record events at ~1% overhead; async-profiler uses perf events for ~0.1% overhead. Never optimize without data.

## Why This Concept Exists

Java applications often need to meet strict latency (< 100ms) or throughput (10K+ req/s) requirements. The JVM provides many tuning knobs but they must be configured correctly. Default settings work for development but not production. Performance tuning bridges the gap between "it works" and "it works at scale."

## References

- [Java Performance Tuning Guide](https://www.baeldung.com/java-performance)
- [Oracle JVM Performance Documentation](https://docs.oracle.com/en/java/javase/21/gctuning/)
- [Java Flight Recorder](https://docs.oracle.com/en/java/javase/21/jfapi/)
- [async-profiler](https://github.com/async-profiler/async-profiler)
