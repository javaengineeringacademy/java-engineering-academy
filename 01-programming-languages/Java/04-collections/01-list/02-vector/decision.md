# Vector Decision Guide

## Decision Tree

```
Need a synchronized list?
├── Is this new code? → Use ArrayList + Collections.synchronizedList()
├── Is this legacy code? → Vector (but plan migration)
├── Need read-heavy concurrent access? → CopyOnWriteArrayList
└── Need high concurrency? → ConcurrentHashMap (not Vector)
```

## Comparison Matrix

| Feature | Vector | ArrayList | CopyOnWriteArrayList |
|---------|--------|-----------|---------------------|
| Thread-safe | Yes (all methods) | No | Yes (copy on write) |
| Performance | Slow (synchronized) | Fast | Fast (reads), slow (writes) |
| Memory | Medium | Low | Very High |
| Legacy | Yes | No | No |
| Enumeration | Yes | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| New code | ArrayList + synchronizedList() | Better performance |
| Legacy code | Vector | Don't break existing code |
| Read-heavy concurrent | CopyOnWriteArrayList | No locks on reads |
| High concurrency | ConcurrentHashMap | Better than synchronized |

## Production Recommendations

> **Never use Vector in new code** — it's legacy and synchronized with performance overhead.

> **Migrate from Vector to ArrayList** — use Collections.synchronizedList() if thread safety is needed.

> **Use CopyOnWriteArrayList for read-heavy concurrent access** — it's faster than Vector for reads.

> **Vector Enumeration is legacy** — use Iterator or for-each loop instead.

## Engineering Trade-offs

| Trade-off | Vector | Alternative |
|-----------|--------|-------------|
| Thread-safety vs Performance | Synchronized (slow) | ArrayList: fast, no safety |
| Legacy vs Modern | Legacy | ArrayList: modern, faster |
| Simplicity vs Performance | Simple | Collections.synchronizedList(): simple wrapper |
| Memory vs Thread-safety | Medium memory | CopyOnWriteArrayList: high memory, safe |

## Common Code Review Comments

- "Why are you using Vector? Use ArrayList + Collections.synchronizedList() instead."
- "Vector is legacy — plan migration to ArrayList."
- "This Vector should be a CopyOnWriteArrayList for read-heavy workloads."
- "Vector Enumeration is legacy — use Iterator instead."

## Common Production Mistakes

> Notice: Vector is not deprecated but strongly discouraged — use ArrayList + Collections.synchronizedList() instead.

> Notice: Vector.toString() is synchronized — it can cause contention in concurrent code.

> Notice: Vector.grow() doubles the size — ArrayList grows by 50%, which is more memory-efficient.

> Notice: Vector is legacy — it was part of Java 1.0, before the Collections Framework.
