# ConcurrentHashMap Decision Guide

## Decision Tree

```
Need a thread-safe map?
├── High concurrency? → ConcurrentHashMap (default)
├── Read-heavy? → ConcurrentHashMap (CAS-based)
├── Write-heavy? → ConcurrentHashMap (fine-grained locking)
├── Need sorted keys? → ConcurrentSkipListMap
├── Need simple wrapper? → Collections.synchronizedMap() (but slower)
└── Need legacy? → Hashtable (but plan migration)
```

## Comparison Matrix

| Feature | ConcurrentHashMap | Hashtable | Collections.synchronizedMap() |
|---------|-------------------|-----------|-------------------------------|
| Thread-safe | Yes (CAS) | Yes (all synchronized) | Yes (synchronized) |
| Null keys/values | No | No | Yes (one null key) |
| Performance | Fast (fine-grained) | Slow (all synchronized) | Slow (all synchronized) |
| Iterator | Weakly consistent | Fail-fast | Fail-fast |
| Scalability | Excellent | Poor | Poor |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| High concurrency | ConcurrentHashMap | CAS-based, fine-grained |
| Read-heavy | ConcurrentHashMap | No locks on reads |
| Write-heavy | ConcurrentHashMap | Fine-grained locking |
| Sorted keys | ConcurrentSkipListMap | Sorted, concurrent |
| Simple wrapper | Collections.synchronizedMap() | Simple, but slow |

## Production Recommendations

> **Default to ConcurrentHashMap** for concurrent access — it's the fastest thread-safe map.

> **Never use Collections.synchronizedMap() in production** — it's slow under contention.

> **Use compute()/merge() for atomic operations** — they're more efficient than get+put.

> **Use ConcurrentHashMap.newKeySet()** for concurrent sets — it's faster than Collections.synchronizedSet().

## Engineering Trade-offs

| Trade-off | ConcurrentHashMap | Alternative |
|-----------|-------------------|-------------|
| Thread-safety vs Performance | CAS-based, fast | Hashtable: synchronized, slow |
| Null vs Safety | No nulls | Collections.synchronizedMap(): allows nulls |
| Complexity vs Simplicity | Complex | Collections.synchronizedMap(): simple |
| Memory vs Scalability | Medium memory | Hashtable: medium memory, poor scalability |

## Common Code Review Comments

- "Why are you using Hashtable? Use ConcurrentHashMap instead."
- "This map is being accessed concurrently — use ConcurrentHashMap."
- "Consider using compute()/merge() for atomic operations."
- "This ConcurrentHashMap.newKeySet() is faster than Collections.synchronizedSet()."

## Common Production Mistakes

> Notice: ConcurrentHashMap doesn't allow null keys or values — use Optional instead.

> Notice: ConcurrentHashMap.size() is O(n) — don't use it to check if the map is empty. Use isEmpty().

> Notice: ConcurrentHashMap iterator is weakly consistent — it reflects the state of the map at or since the creation of the iterator.

> Notice: ConcurrentHashMap is not a synchronized map — it's a concurrent map with fine-grained locking.
