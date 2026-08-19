# 05. Garbage Collection - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Tuning GC for production applications | **Must** |
| Diagnosing Long GC pauses | **Must** |
| Understanding memory management | **Must** |
| Choosing the right garbage collector | **Should** |
| Debugging memory leaks | **Should** |
| Optimizing for latency or throughput | **Should** |
| Simple applications with default settings | **Nice to have** |

## When This Knowledge is Essential

- **Production performance**: GC tuning directly impacts latency, throughput, and resource usage
- **Memory leak diagnosis**: Understanding GC roots and object reachability is critical
- **Collector selection**: Different collectors (G1, ZGC, Shenandoah, Serial, Parallel) serve different needs
- **Container environments**: Heap sizing in Docker/Kubernetes requires GC knowledge
- **Long-running applications**: GC pauses accumulate and affect SLAs

## When This Knowledge is Less Critical

- Short-lived batch jobs with default GC settings
- Development environments
- Applications with very small heaps (< 256MB)

## Key Decision Points

| Decision | GC Knowledge Impact |
|----------|---------------------|
| Heap size (-Xms/-Xmx) | Affects GC frequency and pause times |
| GC algorithm selection | Throughput vs latency trade-offs |
| Young/Old generation ratio | Controls promotion and GC frequency |
| GC logging and analysis | Essential for production monitoring |
