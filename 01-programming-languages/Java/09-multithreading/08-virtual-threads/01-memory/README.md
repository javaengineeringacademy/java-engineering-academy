# Virtual Threads Memory Model

## Stack Memory Comparison

| Thread Type | Stack Size | Memory for 10K threads | Memory for 1M threads |
|-------------|-----------|----------------------|---------------------|
| Platform | 1MB | 10GB | 1TB |
| Virtual | 1KB (initial) | 10MB | 1GB |

### Stack Growth

- Virtual threads start with 1KB stack
- Stack grows on demand (up to platform thread size)
- Stored on heap (not native memory)
- GC can reclaim unused stack space

## Carrier Thread Memory

| Component | Size |
|-----------|------|
| Carrier thread stack | 1MB (platform thread) |
| ForkJoinPool overhead | ~1KB |
| Default carrier count | `availableProcessors()` |

## Per-Task Memory

| Component | Size |
|-----------|------|
| Virtual thread object | ~32 bytes |
| Continuation state | ~64 bytes |
| First task (Runnable) | ~16-64 bytes |

## Total Memory Impact

### 10,000 I/O-bound tasks

**Platform threads (pool=200):**
- Stack: 200MB
- Queue: ~2MB (50 tasks per thread average)
- Total: ~202MB

**Virtual threads (10,000):**
- Stack: 10MB
- Queue: 0 (one thread per task)
- Total: ~10MB

### 1,000,000 concurrent connections

**Platform threads (impossible):**
- Stack: 1TB (impractical)

**Virtual threads:**
- Stack: ~1GB
- Feasible on standard hardware

## Memory Leaks to Watch

### ThreadLocal in Virtual Threads
```
ThreadLocal created per virtual thread
→ Millions of copies
→ Memory leak if not cleaned
→ Use ScopedValue instead
```

### Carrier Thread ThreadLocal
```
ThreadLocal on carrier thread
→ Survives virtual thread unmount
→ Different virtual thread may see previous value
→ Use ScopedValue or pass explicitly
```

## Best Practices

1. Virtual threads reduce stack memory by ~1000x
2. Use ScopedValue instead of ThreadLocal
3. Don't pre-allocate huge thread counts — let JVM manage
4. Monitor carrier thread utilization, not virtual thread count
5. Stack memory is heap-managed — GC handles cleanup
