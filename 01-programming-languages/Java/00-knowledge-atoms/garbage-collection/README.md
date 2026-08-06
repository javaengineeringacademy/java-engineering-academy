# Garbage Collection

## Overview

Garbage Collection (GC) is the automatic memory management mechanism in Java. It identifies and reclaims memory occupied by objects that are no longer reachable by the application. Understanding GC is essential for performance tuning and avoiding memory-related issues.

---

## What GC Does

The Garbage Collector performs three fundamental tasks:

### 1. Memory Allocation
GC manages allocation of new objects on the heap, typically using thread-local allocation buffers (TLABs) for efficiency.

### 2. Memory Reclamation
GC identifies objects that are no longer reachable and reclaims their memory for reuse.

### 3. Compaction (optional)
Some GC algorithms compact memory to reduce fragmentation and improve cache locality.

```java
public void demonstrate() {
    // Object is allocated on the heap
    String message = new String("Hello");
    
    // After this method returns, 'message' goes out of scope
    // The String object becomes eligible for garbage collection
    // GC will eventually reclaim this memory
}
```

---

## Generational GC

The heap is divided into generations based on object lifetime characteristics:

### Young Generation (Minor GC)

Objects that are recently created live here. Most objects die young, so this area is collected frequently.

```
┌─────────────────────────────────────┐
│         Young Generation            │
│  ┌─────┬─────┬─────┐               │
│  │ Eden│ S0  │ S1  │               │
│  │     │(from)│(to) │               │
│  └─────┴─────┴─────┘               │
│  Typical size: 25-40% of heap       │
└─────────────────────────────────────┘
```

- **Eden**: Where new objects are allocated
- **S0 (From Space)**: Survivor space for objects that survived one Minor GC
- **S1 (To Space)**: Destination for objects during copying

**Process:**
1. Objects allocated in Eden
2. When Eden fills up, Minor GC occurs
3. Surviving objects are copied to S0 (or S1)
4. Survivors are copied between S0 and S1 on subsequent GCs
5. Objects surviving multiple GCs are promoted to Old Generation

### Old Generation (Major GC)

Long-lived objects are promoted here. Major GCs are less frequent but take longer.

```
┌─────────────────────────────────────┐
│         Old Generation              │
│  ┌─────────────────────────────┐    │
│  │     Long-lived objects      │    │
│  └─────────────────────────────┘    │
│  Typical size: 60-75% of heap       │
└─────────────────────────────────────┘
```

### Metaspace

Stores class metadata (not part of the generational model):
- Class definitions
- Method metadata
- Constant pools
- Field information

```java
// Tuning generational sizes:
// -XX:NewRatio=2       (Old:Young = 2:1)
// -XX:NewSize=256m     (Initial young gen size)
// -XX:MaxNewSize=1g    (Maximum young gen size)
// -XX:SurvivorRatio=8  (Eden:Survivor = 8:1)
```

---

## GC Algorithms

### Serial GC

Single-threaded collector, good for small applications and single-core machines.

```bash
# Enable Serial GC
-XX:+UseSerialGC
```

**Characteristics:**
- Single-threaded (stop-the-world)
- Simple and low overhead
- Good for single-core systems
- Not suitable for large heaps or latency-sensitive applications
- Uses Mark-Sweep-Compact for Old Gen, Copying for Young Gen

### Parallel GC (Throughput Collector)

Multi-threaded collector optimized for throughput.

```bash
# Enable Parallel GC
-XX:+UseParallelGC
# Tune threads
-XX:ParallelGCThreads=8
# Target pause time
-XX:MaxGCPauseMillis=200
```

**Characteristics:**
- Multi-threaded (stop-the-world)
- Good throughput (minimize GC overhead)
- Default collector before Java 9
- Can use large pauses for maximum throughput
- Uses Copying for Young Gen, Mark-Sweep-Compact for Old Gen

### CMS (Concurrent Mark Sweep)

Low-pause collector (deprecated in Java 9, removed in Java 14).

```bash
# Enable CMS (Java 8 only)
-XX:+UseConcMarkSweepGC
# Threads
-XX:ParallelGCThreads=4
# CMS threads
-XX:ConcGCThreads=2
```

**Characteristics:**
- Concurrent phases (minimal stop-the-world)
- Lower latency than Parallel
- Higher CPU usage
- Fragmentation issues (no compaction)
- Uses Mark-Sweep for Old Gen, Copying for Young Gen

### G1 (Garbage First) GC

Region-based collector designed for large heaps and low latency. Default since Java 9.

```bash
# Enable G1 GC
-XX:+UseG1GC
# Target pause time
-XX:MaxGCPauseMillis=200
# Region size
-XX:G1HeapRegionSize=16m
# Initial/Max heap
-Xms4g -Xmx4g
```

**Characteristics:**
- Region-based heap division
- Predictable pause times (pause target)
- Concurrent collection phases
- Compaction to avoid fragmentation
- Best for heaps 4GB to hundreds of GB
- Balances throughput and latency

**G1 Regions:**
```
┌────┬────┬────┬────┬────┬────┐
│ E  │ E  │ E  │ S  │ S  │ O  │
├────┼────┼────┼────┼────┼────┤
│ O  │ O  │ H  │ H  │ O  │ O  │
├────┼────┼────┼────┼────┼────┤
│ O  │ O  │ E  │ O  │ O  │ O  │
└────┴────┴────┴────┴────┴────┘
E = Eden, S = Survivor, O = Old, H = Humongous
```

### ZGC

Ultra-low latency collector (< 10ms pauses) for large heaps.

```bash
# Enable ZGC
-XX:+UseZGC
# Concurrent phases
-XX:ConcGCThreads=4
# Heap size
-Xmx16g
```

**Characteristics:**
- Sub-millisecond pauses (typically < 1ms)
- Pause times don't increase with heap size
- Concurrent collection
- Good for heaps up to 16TB
- Higher CPU usage than G1
- Requires Java 15+ for production

### Shenandoah

Low-pause collector developed by Red Hat, similar to ZGC.

```bash
# Enable Shenandoah
-XX:+UseShenandoahGC
# Degenerated/Concurrent GC
-XX:ShenandoahGCHeuristics=compact
```

**Characteristics:**
- Sub-millisecond pauses
- Concurrent compaction
- Works with any heap size
- Available in OpenJDK (not Oracle JDK)
- Requires Java 12+ (non-production) or Java 15+ (production)

### Collector Comparison

| Collector | Pause Time | Throughput | Heap Size | Use Case |
|-----------|------------|------------|-----------|----------|
| Serial | High | Low-Medium | Small | Single-core, embedded |
| Parallel | Medium-High | High | Medium | Batch processing |
| CMS | Low | Medium | Medium | Legacy apps (Java 8) |
| G1 | Medium (predictable) | High | Large | General purpose |
| ZGC | Ultra-low (< 10ms) | Medium-High | Very Large | Latency-critical |
| Shenandoah | Ultra-low (< 10ms) | Medium-High | Any | Latency-critical |

---

## When GC Happens

GC is triggered by several events:

### 1. Eden Space Fills Up
```java
// This triggers Minor GC when Eden is full:
for (int i = 0; i < 1000000; i++) {
    new Object();  // allocation in Eden
}
```

### 2. Old Generation Fills Up
```java
// Objects promoted to Old Gen eventually fill it up
List<byte[]> cache = new ArrayList<>();
for (int i = 0; i < 10000; i++) {
    cache.add(new byte[1024 * 1024]);  // 1MB objects, long-lived
}
```

### 3. Explicit System.gc()
```java
System.gc();  // Requests GC (not guaranteed)
Runtime.getRuntime().gc();  // Same thing
// Use -XX:+DisableExplicitGC to ignore these calls
```

### 4. Metaspace Fills Up
```java
// Loading many classes can trigger Metaspace GC
// (especially with dynamic class generation like CGLIB, Javassist)
```

### 5. Promotion Failure
```java
// When objects need to be promoted but Old Gen is full
// triggers Full GC (stop-the-world)
```

---

## GC Tuning Flags

### Essential Flags

```bash
# Heap sizing
-Xms4g                    # Initial heap size
-Xmx4g                    # Maximum heap size (set equal to -Xms)

# Young Generation
-XX:NewSize=1g            # Initial young gen size
-XX:MaxNewSize=1g         # Maximum young gen size
-XX:NewRatio=2            # Old:Young ratio
-XX:SurvivorRatio=8       # Eden:Survivor ratio

# GC algorithm selection
-XX:+UseG1GC              # Use G1 (default Java 9+)
-XX:+UseZGC               # Use ZGC
-XX:+UseShenandoahGC      # Use Shenandoah

# Pause time target (G1)
-XX:MaxGCPauseMillis=200  # Target max pause in milliseconds

# GC logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags  # Java 11+ unified logging
-XX:+PrintGCDetails        # Java 8 GC logging
-XX:+PrintGCDateStamps     # Java 8 GC logging
```

### Advanced Flags

```bash
# Concurrent marking (G1)
-XX:InitiatingHeapOccupancyPercent=45  # Start concurrent cycle at 45% occupancy

# Threads
-XX:ParallelGCThreads=8       # Number of GC threads
-XX:ConcGCThreads=2           # Number of concurrent GC threads

# Evacuation failure
-XX:+UseGCOverheadLimit       # Throw OOM if GC overhead > 98%
-XX:GCTimeLimit=98            # Max GC time percentage

# JEP 291: Deprecate Compact Collections
# ZGC/Shenandoah specific
-XX:SoftMaxHeapSize=4g        # Soft limit for heap (ZGC)
```

---

## How to Monitor GC

### 1. GC Logging

```bash
# Java 11+ unified logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=5,filesize=50m

# Java 8
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log
-XX:+PrintHeapAtGC
-XX:+PrintTenuringDistribution
```

**Sample GC log output:**
```
[2024-01-15T10:30:45.123+0000][0.456][GC (Allocation Failure) [PSYoungGen: 65536K->10752K(76288K)] 65536K->10816K(251392K), 0.0123456 secs] [Times: user=0.05 sys=0.01, real=0.01 secs]
```

### 2. JVisualVM / VisualVM

- Real-time GC monitoring graphs
- Heap dump analysis
- Thread monitoring
- Available in JDK (`jvisualvm`)

### 3. JConsole

```bash
# Launch JConsole
jconsole

# Or connect to specific process
jconsole <pid>
```

### 4. jstat

```bash
# GC statistics
jstat -gcutil <pid> 1000 10  # Update every 1 second, 10 times

# Output columns:
# S0, S1, E, O, M   - utilization percentages
# YGC, YGCT         - Young GC count and time
# FGC, FGCT         - Full GC count and time
# GCT               - Total GC time
```

### 5. JFR (Java Flight Recorder)

```bash
# Start recording
jcmd <pid> JFR.start duration=60s filename=recording.jfr

# Or with JFR API in code
Recording recording = new Recording();
recording.enable(GarbageCollection.class);
recording.start();
```

### 6. Programmatic Monitoring

```java
// Get GC statistics
ManagementFactory.getGarbageCollectorMXBeans().forEach(gc -> {
    System.out.println("GC: " + gc.getName());
    System.out.println("Collection Count: " + gc.getCollectionCount());
    System.out.println("Collection Time: " + gc.getCollectionTime());
});

// Get memory pool statistics
ManagementFactory.getMemoryPoolMXBeans().forEach(pool -> {
    System.out.println("Pool: " + pool.getName());
    System.out.println("Used: " + pool.getUsage().getUsed());
    System.out.println("Committed: " + pool.getUsage().getCommitted());
    System.out.println("Max: " + pool.getUsage().getMax());
});
```

---

## GC Best Practices

1. **Set heap sizes equal**: `-Xms` = `-Xmx` to avoid resize pauses
2. **Choose the right collector**: G1 for general use, ZGC/Shenandoah for ultra-low latency
3. **Monitor regularly**: Use JFR, GC logs, or JVisualVM
4. **Tune for your workload**: Batch vs interactive vs latency-critical
5. **Avoid premature optimization**: Profile first, then tune
6. **Use soft references for caches**: Let GC handle cache eviction
7. **Minimize object creation**: Reuse objects where possible
8. **Size survivor spaces appropriately**: Avoid premature promotion

---

## Common GC Issues

### Long GC Pauses
- Switch to G1, ZGC, or Shenandoah
- Reduce heap size (smaller heap = faster GC)
- Tune `-XX:MaxGCPauseMillis`

### High GC Overhead
- Reduce allocation rate
- Use object pooling
- Tune young generation size

### Memory Leaks
- Use heap dumps (`jmap -dump:format=b,file=heap.hprof <pid>`)
- Analyze with MAT (Memory Analyzer Tool) or VisualVM
- Look for growing old generation usage

### Promotion Failures
- Increase Old Generation size
- Reduce promotion threshold
- Tune survivor spaces

---

## Summary

| Aspect | Details |
|--------|---------|
| **What** | Automatic memory reclamation |
| **When** | Eden full, Old Gen full, explicit call |
| **Where** | Heap (Young + Old), Metaspace |
| **How** | Serial, Parallel, CMS, G1, ZGC, Shenandoah |
| **Monitor** | Logs, jstat, JFR, VisualVM |
| **Tune** | Heap size, GC algorithm, pause targets |
