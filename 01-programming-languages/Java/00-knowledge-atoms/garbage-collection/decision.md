# Decision Guide: Garbage Collection

## When to Use
- All Java applications — GC is the default memory management mechanism
- Tune GC when latency requirements demand it (sub-100ms pauses)
- Use G1 for general-purpose, ZGC for ultra-low latency

## When NOT to Use
- Don't call `System.gc()` explicitly — it's a hint, not a command, and causes full GC pauses
- Don't use Serial GC for multi-threaded applications
- Don't use CMS (deprecated in Java 9, removed in Java 14)

## Trade-offs
| Collector | Latency | Throughput | Heap Size | Use Case |
|-----------|---------|------------|-----------|----------|
| Serial | High | Low | Small | Embedded, single-core |
| Parallel | Medium | High | Medium | Batch processing |
| G1 | Medium | High | Large | General purpose (default) |
| ZGC | Ultra-low | Medium | Very Large | Latency-critical |
| Shenandoah | Ultra-low | Medium | Any | Latency-critical (OpenJDK) |

## Expert Recommendation
Set `-Xms` = `-Xmx` to avoid resize pauses. Use G1 by default. Switch to ZGC for latency-critical workloads. Monitor with JFR in production. Profile before tuning.
