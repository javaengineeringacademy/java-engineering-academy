# GC Tuning Guide

## GC Algorithm Comparison

| Algorithm | Pause Time | Throughput | Heap Range | Java Version |
|-----------|-----------|------------|------------|--------------|
| SerialGC | High | High (single CPU) | < 256MB | All |
| ParallelGC | Medium | Highest | 256MB - 4GB | All |
| G1GC | Low-Medium | High | 4GB - 32GB | 7+ (default 9+) |
| ZGC | < 1ms | Medium-High | 4GB - 16TB | 15+ (prod 21+) |
| Shenandoah | < 10ms | Medium-High | 4GB - 16TB | 12+ (OpenJDK) |

## Tuning Methodology

### Step 1: Set Baseline
```bash
java -XX:+UseG1GC -Xlog:gc*:file=gc.log -jar app.jar
# Run load test, capture GC logs
```

### Step 2: Analyze Current Behavior
- Check pause times (`gc.log` → pause duration)
- Check heap occupancy after full GC
- Identify promotion rates and allocation rates

### Step 3: Tune Heap Sizing
```
# Fixed heap avoids resize pauses
-Xms8g -Xmx8g

# Young gen: 25-40% of heap for allocation-heavy apps
-Xmn2g

# G1 region size: sqrt(heap / 2048) in MB
-XX:G1HeapRegionSize=8m
```

### Step 4: Tune Collection Thresholds
```
# G1: Start concurrent cycle at 45% occupancy
-XX:InitiatingHeapOccupancyPercent=45

# ZGC: Soft limit before active collection
-XX:SoftMaxHeapSize=8g
```

### Step 5: Tune Threads
```
-XX:ParallelGCThreads=<cores/2>
-XX:ConcGCThreads=<ParallelGCThreads/4>
```

## Common GC Issues

### High Allocation Rate
- Symptom: Frequent young GC, high CPU in GC threads
- Fix: Increase young gen (`-Xmn`), reduce object creation

### Premature Promotion
- Symptom: Objects promoted to old gen before dying
- Fix: Increase young gen size, reduce object lifetime

### To-Space Exhaustion
- Symptom: Full GC with evacuation failure
- Fix: Increase `-XX:G1ReservePercent=20`, increase heap

### Long Mixed GC
- Symptom: Mixed GC takes > 500ms
- Fix: Reduce `IHOP`, increase `ConcGCThreads`

### Metaspace OOM
- Symptom: `java.lang.OutOfMemoryError: Metaspace`
- Fix: `-XX:MaxMetaspaceSize=256m`, check for classloader leaks

## Monitoring Tools

### Built-in Tools
- `jstat -gc <pid>` - Real-time GC stats
- `jmap -heap <pid>` - Heap configuration
- `jinfo -flags <pid>` - Active JVM flags
- `jcmd <pid> GC.heap_info` - Detailed heap info

### GC Log Analysis
- **GCEasy** (gceasy.io) - Upload GC logs, get analysis
- **GCViewer** - Open source GC log visualizer
- **HPjmeter** - HP's GC analysis tool

### Runtime Monitoring
- **VisualVM** - Heap dumps, thread dumps, GC monitoring
- **JConsole** - Basic JVM monitoring
- **Micrometer** - Metrics export to Prometheus/Grafana
- **Elastic APM** - JVM metrics in APM dashboards

## Key JVM Flags Reference

```bash
# Heap sizing
-Xms<size>          # Initial heap size
-Xmx<size>          # Maximum heap size
-Xmn<size>          # Young generation size
-Xss<size>          # Thread stack size

# G1 specific
-XX:+UseG1GC
-XX:MaxGCPauseMillis=<ms>
-XX:G1HeapRegionSize=<size>
-XX:InitiatingHeapOccupancyPercent=<percent>

# ZGC specific
-XX:+UseZGC
-XX:+ZGenerational          # Java 21+
-XX:SoftMaxHeapSize=<size>
-XX:ConcGCThreads=<count>

# Logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags
```

## GC Selection Decision Framework

| Workload | Recommended GC | Why |
|----------|---------------|-----|
| Small app (<256MB) | Serial | Simple, low overhead |
| Throughput-critical | Parallel | Max throughput |
| Latency-sensitive (<200ms) | G1 | Balanced |
| Ultra-low latency (<10ms) | ZGC or Shenandoah | Concurrent |
| Testing/Development | Epsilon | No GC overhead |

### Decision Flowchart
Heap size? → <256MB → Serial
Heap size? → 256MB-4GB → Parallel
Heap size? → >4GB → Latency requirement?
Latency? → <200ms → G1
Latency? → <10ms → ZGC or Shenandoah

## Overview

GC tuning is the process of optimizing the JVM's garbage collector to meet application-specific latency and throughput requirements. The JVM offers five main GC algorithms: Serial (single-threaded), Parallel (multi-threaded, throughput-focused), G1 (balanced, default since Java 9), ZGC (ultra-low latency), and Shenandoah (low-latency, OpenJDK). Tuning involves heap sizing, generation ratios, concurrent cycle thresholds, and thread configuration.

## Why This Concept Exists

Garbage collection exists because manual memory management is error-prone and unsafe. However, automatic GC introduces pauses that can violate latency SLAs. A banking application requiring <100ms P99 latency cannot tolerate 500ms GC pauses. GC tuning bridges this gap—configuring the collector to meet business requirements. The JVM provides multiple GC algorithms because no single algorithm works for all workloads: throughput-oriented batch processing needs different tuning than latency-sensitive API servers.

## Internal Working

### G1GC: Region-Based Collection

```
Heap divided into equal-sized regions (1-32MB)
Each region is: Eden, Survivor, Old, or Humongous

Allocation: Objects allocated in Eden regions
Young GC: Evacuate Eden + Survivor → new Survivor/Old
Mixed GC: Evacuate selected Old regions (based on liveness)
Concurrent Cycle: Mark Old regions for mixed GC

Key tuning:
-XX:G1HeapRegionSize=N (sqrt(heap/2048))
-XX:MaxGCPauseMillis=200 (target pause)
-XX:InitiatingHeapOccupancyPercent=45 (IHOP)
```

### ZGC: Concurrent Collection

```
Key innovation: Almost all work happens concurrently
- Concurrent marking
- Concurrent relocation
- Max pause: <1ms (regardless of heap size)

How it works:
1. Pause roots (sub-millisecond)
2. Concurrent mark (scan object graph)
3. Concurrent relocation (move objects, update pointers)
4. Load barriers (intercept pointer reads)

Generational ZGC (Java 21+):
- Young generation: short-lived objects
- Old generation: long-lived objects
- Better performance than single-generation ZGC
```

### GC Log Analysis

```
// GC log format (Java 9+)
[2024-01-15T10:30:15.123+0000][0.456s][info][gc,heap] Heap region size 4M
[2024-01-15T10:30:15.456+0000][0.789s][info][gc,phases] GC(3) Pause Young (Normal)
[2024-01-15T10:30:15.460+0000][0.793s][info][gc,phases] GC(3)   Using 4 workers
[2024-01-15T10:30:15.480+0000][0.813s][info][gc,phases] GC(3)   Eden regions: 16->4
[2024-01-15T10:30:15.485+0000][0.818s][info][gc,phases] GC(3)   Old regions: 8->10
[2024-01-15T10:30:15.490+0000][0.823s][info][gc,heap] GC(3) Pause Young 25.345ms
```

## Examples

### G1GC Tuning for Latency

```java
// Target: <200ms P99 latency, 4GB heap
java -XX:+UseG1GC \
     -Xms4g -Xmx4g \
     -XX:MaxGCPauseMillis=200 \
     -XX:G1HeapRegionSize=8m \
     -XX:InitiatingHeapOccupancyPercent=45 \
     -XX:G1ReservePercent=20 \
     -XX:ParallelGCThreads=8 \
     -XX:ConcGCThreads=2 \
     -Xlog:gc*:file=gc.log:time,uptime,level,tags \
     -jar app.jar

// Tuning rationale:
// - Fixed heap (-Xms=-Xmx): Avoid resize pauses
// - Region size 8MB: Good for medium-sized objects
// - IHOP 45%: Start concurrent cycle early
// - G1Reserve 20%: Prevent to-space exhaustion
```

### ZGC for Ultra-Low Latency

```java
// Target: <10ms P99 latency, 16GB heap
java -XX:+UseZGC \
     -XX:+ZGenerational \
     -Xms16g -Xmx16g \
     -XX:ConcGCThreads=4 \
     -XX:SoftMaxHeapSize=12g \
     -Xlog:gc*:file=gc.log:time,uptime,level,tags \
     -jar app.jar

// ZGC tuning rationale:
// - Generational mode (Java 21+): Better performance
// - Fixed heap: Avoid resize pauses
// - SoftMaxHeapSize: Allows burst beyond 12GB
// - ConcGCThreads: Control concurrent marking speed
```

### Monitoring in Production

```java
// JMX-based monitoring
public class GCMonitor {
    public static void monitor() {
        for (GarbageCollectorMXBean gcBean : 
                ManagementFactory.getGarbageCollectorMXBeans()) {
            long collections = gcBean.getCollectionCount();
            long time = gcBean.getCollectionTime();
            System.out.printf("%s: %d collections, %dms total%n",
                gcBean.getName(), collections, time);
        }
    }
}

// Micrometer metrics for Prometheus
@Bean
public MeterBinder gcMetrics() {
    return registry -> {
        registry.gauge("jvm.gc.pause.max", 
            ManagementFactory.getGarbageCollectorMXBeans(),
            beans -> beans.stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionTime)
                .max().orElse(0));
    };
}
```

## Performance

### GC Algorithm Comparison

| Algorithm | Pause Time | Throughput | Heap Range | Best For |
|-----------|-----------|------------|------------|----------|
| Serial | High (>100ms) | High (single CPU) | <256MB | CLI tools |
| Parallel | Medium (50-200ms) | Highest | 256MB-4GB | Batch processing |
| G1 | Low-Medium (50-200ms) | High | 4GB-32GB | General purpose |
| ZGC | <1ms | Medium-High | 4GB-16TB | Latency-sensitive |
| Shenandoah | <10ms | Medium-High | 4GB-16TB | Latency-sensitive |

### Heap Sizing Impact

| Heap Size | GC Frequency | Pause Time | Memory Efficiency |
|-----------|-------------|------------|-------------------|
| 256MB | Very high | Short | High |
| 1GB | High | Short-Medium | High |
| 4GB | Medium | Medium | Medium |
| 16GB | Low | Medium-Long | Medium |
| 64GB | Very low | Long | Low |

### GC Tuning Checklist

```
□ Set fixed heap (-Xms = -Xmx)
□ Choose appropriate GC algorithm
□ Tune MaxGCPauseMillis for latency SLA
□ Configure IHOP for G1/ZGC
□ Set ParallelGCThreads and ConcGCThreads
□ Enable GC logging
□ Monitor with JMX/Micrometer
□ Analyze GC logs with GCEasy/GCViewer
□ Set -XX:+HeapDumpOnOutOfMemoryError
□ Configure MetaspaceSize for application
```

## Pitfalls

### 1. Over-Tuning GC

```java
// BAD: Optimizing GC before measuring
// Don't tune GC until you have baseline measurements

// GOOD: Measure first
java -XX:+UseG1GC -Xlog:gc*:file=gc.log -jar app.jar
// Run load test, analyze GC log, then tune

// BETTER: Use monitoring tools
// VisualVM, JConsole, Prometheus/Grafana
```

### 2. Ignoring Full GC

```java
// BAD: Full GC not monitored
// Full GC can cause multi-second pauses

// GOOD: Alert on Full GC
// GC log: [GC (Allocation Failure) ... → ...]
// Monitor: jstat -gcutil <pid> 1000
```

### 3. Setting Heap Too Large

```java
// BAD: 64GB heap for small application
// Full GC can take seconds with large heap

// GOOD: Match heap to application needs
// Use -XX:MaxRAMPercentage for containerized apps
java -XX:MaxRAMPercentage=75.0 -jar app.jar
```

### 4. Not Using Container-Aware JVM

```java
// BAD: Hardcoded heap size in container
java -Xmx4g -jar app.jar
// Container limit: 2GB → OOM killed

// GOOD: Use container-aware settings
java -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -jar app.jar
```

### 5. Ignoring GC Log Analysis

```java
// BAD: Not analyzing GC logs
// You won't know about memory leaks, promotion issues, etc.

// GOOD: Regular GC log analysis
// Use GCEasy (gceasy.io) or GCViewer
// Check: pause times, frequency, heap usage, promotion rates
```

## References

- [Oracle GC Tuning Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/index.html)
- [OpenJDK ZGC](https://openjdk.org/projects/zgc/)
- [OpenJDK Shenandoah](https://openjdk.org/projects/shenandoah/)
- *Java Performance* by Scott Oaks
- [GCEasy](https://gceasy.io/) — GC log analyzer
- [GCViewer](https://github.com/chewiebug/GCViewer) — Open source GC visualizer
- [JVM Parameters](https://www.vmware.com/content/dam/digitalmarketing/vmware/en/pdf/techpaper/performance/vmware-jvm-options.pdf)
