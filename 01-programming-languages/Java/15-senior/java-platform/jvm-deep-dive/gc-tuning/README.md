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
