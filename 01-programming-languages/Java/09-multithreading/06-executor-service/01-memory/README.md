# ExecutorService Memory Model

## Thread Stack Memory

Each thread (core or temporary) allocates stack memory:

| Component | Default Size | Configurable |
|-----------|-------------|--------------|
| Thread stack | 1MB (`-Xss`) | Yes via JVM flag |
| Task object (Runnable/Callable) | ~16-64 bytes | No |
| Future result | ~16-32 bytes | No |

### Memory Impact

| Pool Size | Stack Memory | Task Overhead |
|-----------|-------------|---------------|
| 10 threads | 10MB | ~640 bytes |
| 100 threads | 100MB | ~6.4KB |
| 1000 threads | 1GB | ~64KB |

## Queue Memory

| Queue Type | Memory Behavior |
|------------|-----------------|
| LinkedBlockingQueue (unbounded) | Grows with pending tasks — OOM risk |
| ArrayBlockingQueue (bounded) | Fixed size, predictable |
| SynchronousQueue | Zero capacity, no memory overhead |
| PriorityBlockingQueue | Grows with pending tasks |

### OOM from Unbounded Queue

```
Tasks submitted faster than processed
  → Queue grows unbounded
    → Heap fills up
      → OutOfMemoryError
```

## Worker Thread Memory

Each Worker holds:
- Reference to the first task (runnable)
- Reference to the thread pool
- ThreadLocal storage (if used)

## Best Practices

1. **Use bounded queues** to cap memory usage
2. **Size pools appropriately** — more threads = more stack memory
3. **Avoid ThreadLocal in pooled threads** — memory leak if not cleaned
4. **Monitor queue depth** as leading indicator of memory pressure
5. **Set task timeouts** to prevent memory held by blocked threads
