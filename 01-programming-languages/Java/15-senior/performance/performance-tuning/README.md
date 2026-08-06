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

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
