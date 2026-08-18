# Atomic Classes Decision Guide

## AtomicInteger vs synchronized vs volatile

| Feature | volatile | AtomicInteger | synchronized |
|---------|----------|---------------|-------------|
| Atomicity | No | Yes | Yes |
| Visibility | Yes | Yes | Yes |
| Blocking | No | No | Yes |
| Performance | Fastest | Fast | Slower under contention |
| Use case | Simple flags | Counters, CAS | Complex operations |

## When to Use Each

| Situation | Solution |
|-----------|----------|
| Simple boolean flag | `volatile` |
| Counter (increment/decrement) | `AtomicInteger` |
| Compare-and-swap logic | `AtomicReference.compareAndSet()` |
| High-throughput counter | `LongAdder` |
| Complex state update | `synchronized` or `Lock` |
