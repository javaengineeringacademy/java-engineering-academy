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
