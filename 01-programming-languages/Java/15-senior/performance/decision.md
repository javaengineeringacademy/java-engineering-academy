# Performance: Decision Guide

## When to Apply Performance Engineering

### Profiling Decision Tree

```
High latency?
├── Check GC logs → GC pauses?
│   ├── Yes → Tune GC (G1/ZGC), reduce allocation rate
│   └── No → Profile CPU
├── High CPU?
├── Lock contention?
│   ├── Yes → Reduce lock scope, use concurrent structures
│   └── No → Check I/O
├── High I/O?
│   ├── Disk → Batch writes, use buffers
│   └── Network → Connection pooling, async I/O
└── High memory?
    ├── Heap → Reduce object creation, use object pools
    └── Off-heap → Check direct buffers, memory-mapped files
```

### GC Algorithm Selection

| Scenario | GC Algorithm | JVM Flags |
|----------|-------------|-----------|
| Default (general purpose) | G1GC | `-XX:+UseG1GC` |
| Ultra-low latency (<10ms) | ZGC | `-XX:+UseZGC` |
| Large heap (>16GB) | ZGC or G1GC | `-XX:+UseZGC -Xmx32g` |
| Container with memory limits | ZGC | `-XX:+UseZGC -XX:MaxRAMPercentage=75` |
| Throughput-first | Parallel GC | `-XX:+UseParallelGC` |
| Very small heap (<256MB) | Serial GC | `-XX:+UseSerialGC` |

### Profiling Tool Selection

| Tool | Type | Overhead | Best For |
|------|------|----------|----------|
| JFR (Java Flight Recorder) | Built-in | Very low (<1%) | Production profiling |
| async-profiler | Agent | Low (2-5%) | CPU and allocation profiling |
| JMH | Micro-benchmark | None (controlled) | Measuring code performance |
| VisualVM | Attach API | Medium | Development exploration |
| YourKit | Commercial | Low-Medium | Full-featured analysis |
| jstat | Built-in | Very low | Quick GC monitoring |

### Allocation Rate Optimization

| Pattern | Problem | Solution |
|---------|---------|----------|
| String concatenation in loop | O(n²) allocation | StringBuilder |
| Boxing in tight loop | Wrapper object churn | Use primitive streams |
| Creating objects per request | High GC pressure | Object pooling |
| Autoboxing collections | Wrapper overhead | Eclipse Collections or Trove |
| Exception for control flow | Stack trace allocation | Use return values or Optional |

### Benchmark Design Checklist

1. **Warmup** — Always include warmup iterations (JIT compilation)
2. **Fork** — Fork separate JVM processes to avoid state contamination
3. **Black hole** — Prevent dead code elimination with `Blackhole.consume()`
4. **State** — Use `@State` to manage benchmark state lifecycle
5. **Profiling** — Run with `-prof gc` to track allocation in benchmarks
6. **Multiple modes** — Use `@BenchmarkMode({SampleTime, AverageTime})`

## Memory Leak Diagnosis Workflow

1. **Heap dump** — `jmap -dump:live,format=b,file=heap.hprof <pid>`
2. **Analyze dominator tree** — Find objects holding most memory
3. **Check GC roots** — Why are objects not being collected?
4. **Common leak sources:**
   - Static collections growing unbounded
   - Unclosed resources (connections, streams)
   - Listener registrations without cleanup
   - ThreadLocal values not removed
   - Class loader leaks (in application servers)

## Performance Metrics Reference

| Metric | Target (typical) | Measurement |
|--------|-----------------|-------------|
| P50 latency | < 50ms | JFR or APM |
| P99 latency | < 200ms | JFR or APM |
| P999 latency | < 500ms | JFR or APM |
| GC pause (G1) | < 200ms | `-Xlog:gc*` |
| GC pause (ZGC) | < 1ms | `-Xlog:gc*` |
| Allocation rate | < 100MB/s | JFR `jdk.ObjectAllocationInNewTLAB` |
| CPU utilization | < 70% | `top` or JFR `jdk.CPULoad` |
| Heap usage | < 80% of max | `jstat -gcutil` |

## Further Reading

- *Java Performance* by Scott Oaks
- *Optimizing Java* by Benjamin Evans
- [JMH Samples](https://github.com/openjdk/jmh/tree/master/jmh-samples)
- [JFR Documentation](https://docs.oracle.com/en/java/javase/21/jfapi/)
