# War Story: GC Pause Causing 10-Second Latency

## What Happened

At 2:47 AM on a Tuesday, our payment processing service experienced a cascade of timeout errors. Users reported transactions hanging for 10+ seconds before completing or failing. The incident lasted 45 minutes and affected approximately 12,000 transactions.

## Timeline

| Time | Event |
|------|-------|
| 02:47 | First timeout alerts fire for payment service |
| 02:48 | Response time p99 spikes from 200ms to 12,000ms |
| 02:49 | On-call engineer paged, begins investigation |
| 02:52 | Grafana shows GC pause metric at 10.2 seconds |
| 02:55 | Heap dump triggered, old gen at 98% capacity |
| 03:00 | Decision to restart service instances |
| 03:05 | Rolling restart begins across 12 instances |
| 03:15 | Service stabilized, GC pauses normal |
| 03:32 | Full incident resolved |

## Root Cause

The service was configured with a 32GB heap using G1GC with default settings. A batch job that ran nightly was creating large temporary objects (report PDFs) that accumulated in old generation. When the GC finally triggered a mixed collection, the pause time was proportional to the number of live objects it needed to process.

Key factors:
- Heap size: 32GB (too large for G1GC pause time goals)
- Old gen occupancy: 98% before GC triggered
- Large object allocation: PDF generation created 500MB+ temporary objects
- No GC tuning: Using JVM defaults for G1GC
- No allocation rate monitoring

## Detection

### Monitoring Signals
- **Grafana GC dashboard**: `jvm_gc_pause_seconds_max` metric spiked to 10.2s
- **Application metrics**: Response time p99 exceeded 10s threshold
- **User reports**: 47 support tickets filed during the incident
- **Database metrics**: Connection pool utilization hit 100% as requests queued

### What We Missed
- No alerting on GC pause duration (only on heap usage)
- No allocation rate monitoring
- No old gen occupancy alerts
- Batch job resource usage not monitored separately

## Fix

### Immediate (During Incident)
1. Restarted all 12 service instances to clear old generation
2. Reduced traffic to 50% using load balancer weights

### Short-Term (Within 1 Week)
1. Switched from G1GC to ZGC: `-XX:+UseZGC -XX:+ZGenerational`
2. Reduced heap from 32GB to 16GB (ZGC handles larger heaps better)
3. Added GC pause alerting: `jvm_gc_pause_seconds_max > 0.5`
4. Added old gen occupancy alerting: `old_gen_usage > 80%`

### Long-Term (Within 1 Month)
1. Moved PDF generation to off-heap memory using `java.nio.ByteBuffer`
2. Implemented streaming PDF generation to avoid large object accumulation
3. Added allocation rate monitoring: `jvm_buffer_memory_used` and custom metrics
4. Separated batch job into its own service with isolated heap
5. Created GC tuning runbook for all services

## Prevention

### Monitoring
- Alert on GC pause > 500ms (warning) and > 2s (critical)
- Alert on old gen occupancy > 80%
- Monitor allocation rate per service
- Dashboard for JVM memory metrics across all services

### Configuration Standards
- All services must use ZGC or Shenandoah for low-pause GC
- Maximum heap size: 16GB per service instance
- GC logging enabled with structured format
- Allocation profiling in performance testing

### Process
- GC tuning review required for heap size changes
- Off-heap required for objects > 100MB
- Batch jobs must be isolated from user-facing services
- Load testing must include GC behavior validation

## Lessons Learned

1. **G1GC pause time scales with heap size** — 32GB heaps cause multi-second pauses
2. **ZGC is the right choice for latency-sensitive services** — sub-millisecond pauses regardless of heap size
3. **Monitor what matters** — GC pause duration is more important than heap usage
4. **Large objects belong off-heap** — Java heap is not designed for multi-GB temporary objects
5. **Batch jobs need isolation** — Don't share heap with user-facing request handling

## Interview Questions

1. **What causes long GC pauses and how do you prevent them?**
   Causes: (1) Large heap with G1GC (pause scales with live objects), (2) high allocation rate creating many short-lived objects, (3) large objects (>50% of region) spanning multiple regions, (4) Full GC triggered by old gen at 85%+. Prevention: use ZGC for sub-ms pauses, keep heap ≤16GB, monitor allocation rate, use off-heap for large objects.

2. **What is the difference between G1GC, ZGC, and Shenandoah?**
   G1GC: default since Java 9, pause times 10-200ms depending on heap size, good throughput. ZGC: sub-millisecond pauses regardless of heap size, slightly lower throughput. Shenandoah: similar to ZGC, pause times <10ms, good for large heaps. For latency-sensitive services: ZGC or Shenandoah. For throughput-sensitive: G1GC with tuning.

3. **How do you tune G1GC for predictable pause times?**
   Set `-XX:MaxGCPauseMillis=200` (target 200ms max). Set `-XX:InitiatingHeapOccupancyPercent=45` (start concurrent GC earlier). Set `-XX:G1HeapRegionSize=16m` (larger regions for large objects). Set `-XX:G1MixedGCCountTarget=8` (spread mixed GC over more cycles). Monitor with `-Xlog:gc*`.

4. **When should you move large objects off-heap?**
   When objects exceed 100MB (PDF generation, large JSON serialization, image processing). Off-heap avoids GC overhead: `java.nio.ByteBuffer.allocateDirect()`. Trade-off: manual memory management, no GC scanning, but allocation is slower. Use for: batch processing, streaming, temporary buffers.

5. **How do you monitor GC behavior in production?**
   Metrics: `jvm_gc_pause_seconds_max` (max pause time), `jvm_gc_pause_seconds_sum` (total pause time), `jvm_gc_memory_promoted_bytes_total` (objects promoted to old gen). Alerts: GC pause >500ms (warning), >2s (critical). Tools: Grafana + Prometheus, JConsole, VisualVM, GC logs (`-Xlog:gc*`).

## Pitfalls

**Using G1GC with 32GB heap for latency-sensitive services:**
```bash
# BAD: Default G1GC with large heap
-XX:+UseG1GC
-Xmx32g
# Pause time: 1-10 seconds (scales with live objects)

# GOOD: ZGC for latency-sensitive services
-XX:+UseZGC -XX:+ZGenerational
-Xmx16g
# Pause time: <1ms regardless of heap size
```

**Not monitoring allocation rate:**
```java
// BAD: No allocation rate monitoring
// Silent allocation of large objects → GC pause

// GOOD: Monitor allocation rate
// Enable GC logging
// -Xlog:gc*:file=gc.log:time,uptime,level,tags

// Monitor with Prometheus
// jvm_buffer_memory_used (direct buffer allocation)
// jvm_memory_pool_allocated_bytes_used (heap allocation)
```

**Large objects on-heap:**
```java
// BAD: Large JSON serialization on heap
String json = objectMapper.writeValueAsString(hugeOrderHistory); // 500MB in memory!
// GC must scan and compact 500MB → long pause

// GOOD: Streaming serialization off-heap
JsonGenerator generator = jsonFactory.createGenerator(outputStream);
generator.writeStartArray();
for (Order order : orderHistory) {
    generator.writeObject(order); // Stream directly to output
}
generator.writeEndArray();
generator.flush(); // No full serialization in memory
```

## Performance

**GC Pause Comparison:**
```
G1GC (32GB heap):
- Full GC pause: 5-10 seconds
- Mixed GC pause: 100-500ms
- Young GC pause: 10-50ms
- Throughput: 99.9%

ZGC (32GB heap):
- GC pause: <1ms (all types)
- Throughput: 99.5%
- Memory overhead: 15% (metadata)

Shenandoah (32GB heap):
- GC pause: <10ms
- Throughput: 99.7%
- Memory overhead: 10%
```

**Allocation Rate Impact:**
```
Allocation rate: 1GB/sec → GC every 10 seconds (10GB heap)
Allocation rate: 5GB/sec → GC every 2 seconds (10GB heap)
Allocation rate: 10GB/sec → GC every 1 second (10GB heap)

Each GC: pause time proportional to live objects
10GB heap with 8GB live: 1-2 second pause (G1GC)
10GB heap with 8GB live: <1ms pause (ZGC)
```

## Internal Working

**G1GC Mechanism:**
```
1. Heap divided into equal regions (1-32MB each)
2. Young GC: copy surviving objects to new regions
3. Mixed GC: select old regions with most garbage
4. Concurrent marking: scan old gen without pausing
5. Full GC: last resort, pauses all threads

Pause time = time to copy surviving objects
More live objects = longer pause
```

**ZGC Mechanism:**
```
1. Load barriers intercept all object accesses
2. Concurrent marking: scan heap without pausing
3. Concurrent compaction: move objects without pausing
4. Pause: only root scanning and reference processing
5. Pause time: <1ms regardless of heap size

Key innovation: colored pointers and load barriers
Enable concurrent heap operations
```

## Why This Concept Exists

GC pause prevention exists because:

1. **User experience**: 10-second pauses cause transaction timeouts and user frustration
2. **SLA violations**: 99.9% availability requires predictable latency
3. **Financial impact**: Payment processing failures lose revenue
4. **Scalability**: Large heaps make G1GC pauses worse, limiting vertical scaling
5. **Modern hardware**: Multi-GB heaps are common, but GC doesn't scale linearly
6. **ZGC/Shenandoah**: New GC algorithms provide sub-ms pauses, making Java competitive with Go/Rust

The evolution from Serial → Parallel → CMS → G1 → ZGC reflects the industry's demand for lower pause times.

## Overview

GC pause war story: a payment processing service experienced 10-second latency spikes caused by G1GC with 32GB heap. Root cause: large temporary objects (PDF generation) accumulated in old generation, causing Full GC. Fix: switched to ZGC with 16GB heap, moved PDF generation off-heap. Prevention: use ZGC for latency-sensitive services, monitor allocation rate, use off-heap for large objects.

## References

- ZGC documentation: https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html
- G1GC tuning: https://docs.oracle.com/en/java/javase/21/gctuning/garbage-first-g1-garbage-collector.html
- Shenandoah documentation: https://wiki.openjdk.org/display/shenandoah
- "Java Performance" by Scott Oaks — GC tuning
- GC logging: https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html
- JEP 439: Generational ZGC: https://openjdk.org/jeps/439
