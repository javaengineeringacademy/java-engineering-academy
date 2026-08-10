# HashMap Decision Guide

## Decision Tree

```
Need key-value storage?
├── Need ordering? → No → HashMap (fastest)
├── Need sorted keys? → TreeMap
├── Need insertion order? → LinkedHashMap
├── Need LRU cache? → LinkedHashMap(accessOrder=true)
├── Need thread safety? → ConcurrentHashMap
├── Need weak references? → WeakHashMap
├── Need enum keys? → EnumMap (fastest)
└── Need null key? → HashMap (one null key allowed)
```

## Comparison Matrix

| Feature | HashMap | LinkedHashMap | TreeMap |
|---------|---------|---------------|---------|
| Order | None | Insertion/Access | Sorted |
| Null keys | One | One | None |
| Performance | O(1) | O(1) | O(log n) |
| Memory | Low | Medium | Medium |
| Thread-safe | No | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose map | HashMap | Fastest, no ordering |
| Sorted keys | TreeMap | Natural/custom ordering |
| Insertion order | LinkedHashMap | Maintains order |
| LRU cache | LinkedHashMap(accessOrder=true) | Built-in access-order |
| Thread-safe | ConcurrentHashMap | High concurrency |
| Enum keys | EnumMap | Bit-vector, fastest |

## Production Recommendations

> **Default to HashMap** — it's the fastest and most memory-efficient for general-purpose use.

> **Use ConcurrentHashMap for concurrent access** — never use Collections.synchronizedMap() in production.

> **Use LinkedHashMap for LRU caches** — set accessOrder=true and override removeEldestEntry().

> **Avoid Hashtable** — it's legacy and synchronized with performance overhead.

## Engineering Trade-offs

| Trade-off | HashMap | Alternative |
|-----------|---------|-------------|
| Speed vs Ordering | Fast, no order | TreeMap: sorted, O(log n) |
| Immutability vs Flexibility | Mutable | Map.of(): immutable |
| Thread-safety vs Performance | No safety | ConcurrentHashMap: safe, overhead |
| Memory vs GC-friendliness | Compact | WeakHashMap: GC-friendly, higher overhead |

## Common Code Review Comments

- "Why are you using Hashtable? Use ConcurrentHashMap instead."
- "This map is being accessed concurrently — use ConcurrentHashMap."
- "Consider using Map.of() if this map is immutable."
- "This should be an EnumMap — you're using enum values as keys."

## Common Production Mistakes

> Notice: HashMap doesn't maintain order — if you need insertion order, use LinkedHashMap.

> Notice: HashMap allows one null key and multiple null values — but in concurrent code, prefer Optional over null.

> Notice: HashMap is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: HashMap.hashCode() is called for each key — make sure your hashCode() implementation is efficient.
