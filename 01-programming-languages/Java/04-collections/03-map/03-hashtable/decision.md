# Hashtable Decision Guide

## Decision Tree

```
Need a synchronized map?
├── Is this new code? → Use ConcurrentHashMap
├── Is this legacy code? → Hashtable (but plan migration)
├── Need high concurrency? → ConcurrentHashMap (CAS-based)
└── Need simple wrapper? → Collections.synchronizedMap()
```

## Comparison Matrix

| Feature | Hashtable | ConcurrentHashMap | Collections.synchronizedMap() |
|---------|-----------|-------------------|-------------------------------|
| Thread-safe | Yes (all methods) | Yes (CAS) | Yes (synchronized) |
| Null keys/values | No | No | Yes (one null key) |
| Performance | Slow (all synchronized) | Fast (fine-grained) | Slow (all synchronized) |
| Legacy | Yes | No | No |
| Iterator | Fail-fast | Weakly consistent | Fail-fast |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| New code | ConcurrentHashMap | Better performance |
| Legacy code | Hashtable | Don't break existing code |
| High concurrency | ConcurrentHashMap | CAS-based, fine-grained |
| Simple wrapper | Collections.synchronizedMap() | Simple, but slow |

## Production Recommendations

> **Never use Hashtable in new code** — it's legacy and synchronized with performance overhead.

> **Migrate from Hashtable to ConcurrentHashMap** — it's faster and more scalable.

> **Use ConcurrentHashMap for concurrent access** — it's the fastest thread-safe map.

> **Use Collections.synchronizedMap() for simple wrappers** — but it's slower than ConcurrentHashMap.

## Engineering Trade-offs

| Trade-off | Hashtable | Alternative |
|-----------|-----------|-------------|
| Thread-safety vs Performance | Synchronized (slow) | ConcurrentHashMap: fast, fine-grained |
| Legacy vs Modern | Legacy | ConcurrentHashMap: modern, faster |
| Simplicity vs Performance | Simple | Collections.synchronizedMap(): simple wrapper |
| Memory vs Thread-safety | Medium memory | ConcurrentHashMap: medium memory, safe |

## Common Code Review Comments

- "Why are you using Hashtable? Use ConcurrentHashMap instead."
- "Hashtable is legacy — plan migration to ConcurrentHashMap."
- "This Hashtable should be a ConcurrentHashMap for better performance."
- "Hashtable toString() is synchronized — it can cause contention."

## Common Production Mistakes

> Notice: Hashtable is not deprecated but strongly discouraged — use ConcurrentHashMap instead.

> Notice: Hashtable.toString() is synchronized — it can cause contention in concurrent code.

> Notice: Hashtable doesn't allow null keys or values — ConcurrentHashMap also doesn't allow nulls.

> Notice: Hashtable is legacy — it was part of Java 1.0, before the Collections Framework.
