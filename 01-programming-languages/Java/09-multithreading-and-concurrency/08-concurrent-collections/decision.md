# Concurrent Collections Decision Guide

## Synchronized Wrapper vs Concurrent Collection

| Aspect | Synchronized Wrapper | Concurrent Collection |
|--------|---------------------|----------------------|
| Lock granularity | Single lock | Lock striping / CAS |
| Performance under contention | Degrades | Scales |
| Atomic operations | Manual | compute(), merge(), putIfAbsent() |
| Iteration | Fail-fast (must sync) | Weakly consistent |
| Use case | Low contention | High contention |

## When to Use Each

| Situation | Collection |
|-----------|-----------|
| Simple map, low contention | Collections.synchronizedMap() |
| High-concurrency map | ConcurrentHashMap |
| Read-heavy list | CopyOnWriteArrayList |
| Producer-consumer queue | ArrayBlockingQueue / LinkedBlockingQueue |
| Sorted concurrent map | ConcurrentSkipListMap |
