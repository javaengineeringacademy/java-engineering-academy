# 06. GC Algorithms - Decision Guide

## When to Study This Topic

| Scenario | Priority |
|----------|----------|
| Choosing between G1, ZGC, Shenandoah | **Must** |
| Tuning specific GC algorithm parameters | **Must** |
| Understanding collector internals for debugging | **Should** |
| Migrating from one collector to another | **Should** |
| Optimizing for specific latency targets | **Should** |
| Simple applications with default G1 | **Nice to have** |

## When This Knowledge is Essential

- **Collector selection**: Matching algorithm to workload (throughput vs latency)
- **Algorithm-specific tuning**: Each collector has unique parameters
- **Production debugging**: Understanding collector behavior explains GC logs
- **Upgrading JDK versions**: Collector defaults change between versions

## Key Decision Points

| Decision | Algorithm Knowledge Impact |
|----------|---------------------------|
| G1 vs ZGC vs Shenandoah | Different pause time and throughput characteristics |
| Region size tuning (G1) | Affects pause predictability |
| Concurrent GC threads | Balances GC overhead vs pause times |
| Heap size per algorithm | Each algorithm has different memory overhead |
