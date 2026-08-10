# WeakHashMap Decision Guide

## Decision Tree

```
Need weak references for caching?
├── Need keys to be GC'd when no longer referenced? → WeakHashMap
├── Need values to be GC'd? → Use SoftReference or Caffeine
├── Need thread safety? → Collections.synchronizedMap() (but Caffeine is better)
└── Need strong references? → HashMap
```

## Comparison Matrix

| Feature | WeakHashMap | HashMap | Caffeine |
|---------|-------------|---------|----------|
| Key references | Weak | Strong | Configurable |
| Value references | Strong | Strong | Configurable |
| GC behavior | Keys GC'd | Never GC'd | Configurable |
| Performance | O(1) | O(1) | O(1) |
| Thread-safe | No | No | Yes |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Cache with GC | WeakHashMap | Keys GC'd when unreferenced |
| Strong cache | HashMap | Never GC'd |
| Production cache | Caffeine | Better performance, features |
| Simple caching | WeakHashMap | No dependencies |

## Production Recommendations

> **Use WeakHashMap for simple caches** — keys are GC'd when no longer referenced.

> **Use Caffeine for production caches** — it's faster, more feature-rich, and thread-safe.

> **Never use WeakHashMap as a primary data store** — keys can be GC'd at any time.

> **Use SoftReference for memory-sensitive caches** — but Caffeine handles this better.

## Engineering Trade-offs

| Trade-off | WeakHashMap | Alternative |
|-----------|-------------|-------------|
| GC-friendliness vs Predictability | Keys can be GC'd | HashMap: predictable, never GC'd |
| Simplicity vs Features | Simple | Caffeine: feature-rich, faster |
| Thread-safety vs Performance | No safety | Caffeine: thread-safe, fast |
| Memory vs Reliability | GC-friendly | HashMap: reliable, higher memory |

## Common Code Review Comments

- "Why are you using WeakHashMap? Caffeine is better for production caches."
- "WeakHashMap keys can be GC'd at any time — don't use it as a primary data store."
- "Consider using SoftReference for memory-sensitive caches."
- "This WeakHashMap is being iterated concurrently — use Collections.synchronizedMap()."

## Common Production Mistakes

> Notice: WeakHashMap keys are weakly referenced — they can be GC'd at any time.

> Notice: WeakHashMap is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: WeakHashMap.size() includes stale entries — use entrySet() to check actual size.

> Notice: WeakHashMap is not a cache — it's a map with weak keys. Use Caffeine for caching.
