# 08. Profiling Tools - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Diagnosing production performance issues | **Must** |
| Identifying CPU hotspots and memory leaks | **Must** |
| Setting up continuous profiling | **Should** |
| Choosing between VisualVM, JProfiler, async-profiler | **Should** |
| Optimizing application throughput/latency | **Should** |
| Simple applications with no performance issues | **Nice to have** |

## When This Knowledge is Essential

- **Performance bottlenecks**: Profiling reveals where time is actually spent
- **Memory leak detection**: Heap profiling identifies objects preventing GC
- **Production monitoring**: JFR provides low-overhead continuous profiling
- **Thread contention**: Thread profiling reveals synchronization bottlenecks

## Key Decision Points

| Decision | Profiling Knowledge Impact |
|----------|---------------------------|
| Sampling vs instrumentation profiling | Overhead vs accuracy trade-off |
| CPU vs memory vs thread profiling | Different tools for different issues |
| Production vs development profiling | Different overhead tolerance |
| Async-profiler vs JFR vs JProfiler | Different capabilities and costs |
