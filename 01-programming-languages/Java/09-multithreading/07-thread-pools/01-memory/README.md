# Thread Pool Memory Model

## Per-Thread Memory

| Component | Size | Notes |
|-----------|------|-------|
| Thread stack | 1MB default (`-Xss`) | Configurable per thread |
| Worker object | ~48 bytes | Thread reference + first task |
| Task object (Runnable) | ~16-64 bytes | Depends on captured state |

## Queue Memory

| Queue Type | Memory Behavior |
|------------|-----------------|
| LinkedBlockingQueue | Grows with tasks — OOM risk |
| ArrayBlockingQueue | Fixed size — predictable |
| SynchronousQueue | Zero capacity — no memory |
| PriorityBlockingQueue | Grows with tasks — OOM risk |

## Memory Impact by Pool Size

| Pool Size | Stack Memory | Queue Capacity (1000) |
|-----------|-------------|----------------------|
| 10 threads | 10MB | 1000 tasks |
| 50 threads | 50MB | 1000 tasks |
| 200 threads | 200MB | 1000 tasks |

## Common Memory Issues

### 1. Unbounded Queue Growth
```
Task rate > Processing rate
→ Queue grows unbounded
→ Heap exhaustion
→ OutOfMemoryError
```

### 2. Thread Stack Overflow
```
Deep recursion in pool thread
→ Stack grows beyond -Xss limit
→ StackOverflowError
```

### 3. ThreadLocal Leaks
```
Pool thread reused by different task
→ Previous task's ThreadLocal still referenced
→ Memory leak if not cleaned
```

## Best Practices

1. Use bounded queues to cap memory usage
2. Size pools based on workload, not arbitrarily
3. Clean ThreadLocal in finally blocks
4. Monitor queue depth as memory pressure indicator
5. Set task timeouts to free blocked threads
