# CopyOnWriteArrayList Decision Guide

## Decision Tree

```
Need a thread-safe list?
├── Read-heavy workload? → CopyOnWriteArrayList
├── Write-heavy workload? → Collections.synchronizedList()
├── High concurrency? → ConcurrentHashMap (not List)
├── Need iteration during modification? → CopyOnWriteArrayList
└── Need random access? → CopyOnWriteArrayList
```

## Comparison Matrix

| Feature | CopyOnWriteArrayList | Collections.synchronizedList | ArrayList |
|---------|---------------------|------------------------------|-----------|
| Thread-safe | Yes (copy on write) | Yes (synchronized) | No |
| Read performance | Excellent | Good | Excellent |
| Write performance | Poor (copies array) | Good | Excellent |
| Iteration | Snapshot (no CME) | Fail-fast (CME) | Fail-fast (CME) |
| Memory | Very High | Medium | Low |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Read-heavy concurrent | CopyOnWriteArrayList | No locks on reads |
| Write-heavy concurrent | Collections.synchronizedList() | No copy overhead |
| Iteration during modification | CopyOnWriteArrayList | Snapshot iteration |
| General-purpose | ArrayList | Fastest, no safety |

## Production Recommendations

> **Use CopyOnWriteArrayList for read-heavy concurrent access** — it's the fastest thread-safe list for reads.

> **Never use CopyOnWriteArrayList for write-heavy workloads** — each write copies the entire array.

> **Use CopyOnWriteArrayList for listener/event lists** — iteration is common, modification is rare.

> **Use Collections.synchronizedList() for general concurrent access** — it's more memory-efficient.

## Engineering Trade-offs

| Trade-off | CopyOnWriteArrayList | Alternative |
|-----------|---------------------|-------------|
| Read vs Write performance | Fast reads, slow writes | synchronizedList: balanced |
| Memory vs Thread-safety | Very High memory | synchronizedList: medium memory |
| Snapshot vs Fail-fast | Snapshot (no CME) | synchronizedList: fail-fast |
| Simplicity vs Performance | Simple | synchronizedList: simple wrapper |

## Common Code Review Comments

- "This CopyOnWriteArrayList is write-heavy — use Collections.synchronizedList() instead."
- "CopyOnWriteArrayList is perfect for listener lists — reads are common, writes are rare."
- "This iteration during modification — CopyOnWriteArrayList avoids ConcurrentModificationException."
- "CopyOnWriteArrayList memory is very high — make sure you need it."

## Common Production Mistakes

> Notice: CopyOnWriteArrayList copies the entire array on every write — don't use it for write-heavy workloads.

> Notice: CopyOnWriteArrayList snapshot iteration doesn't reflect changes after iteration starts — this is by design.

> Notice: CopyOnWriteArrayList is not lock-free — it uses a lock for writes, not reads.

> Notice: CopyOnWriteArrayList is expensive for large lists — consider ConcurrentHashMap for key-value data.
