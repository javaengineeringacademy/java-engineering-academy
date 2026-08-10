# LinkedHashMap Decision Guide

## Decision Tree

```
Need ordered key-value storage?
├── Need insertion order? → LinkedHashMap (default)
├── Need access order (LRU)? → LinkedHashMap(accessOrder=true)
├── Need sorted order? → TreeMap
├── Need no order? → HashMap (faster)
└── Need thread safety? → Collections.synchronizedMap()
```

## Comparison Matrix

| Feature | LinkedHashMap | HashMap | TreeMap |
|---------|---------------|---------|---------|
| Order | Insertion/Access | None | Sorted |
| Null keys | One | One | None |
| Performance | O(1) | O(1) | O(log n) |
| Memory | Medium | Low | Medium |
| Thread-safe | No | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Insertion order | LinkedHashMap | Maintains order |
| LRU cache | LinkedHashMap(accessOrder=true) | Built-in access-order |
| General-purpose | HashMap | Faster, no ordering |
| Sorted keys | TreeMap | Natural/custom ordering |

## Production Recommendations

> **Use LinkedHashMap for insertion order** — it's almost as fast as HashMap.

> **Use LinkedHashMap for LRU caches** — set accessOrder=true and override removeEldestEntry().

> **Use HashMap if order doesn't matter** — it's faster and uses less memory.

> **Use Map.of() for constants** — it's immutable and thread-safe.

## Engineering Trade-offs

| Trade-off | LinkedHashMap | Alternative |
|-----------|---------------|-------------|
| Order vs Memory | Medium memory, ordered | HashMap: low memory, no order |
| Order vs Speed | Slightly slower | HashMap: faster |
| Immutability vs Flexibility | Mutable | Map.of(): immutable |
| Thread-safety vs Performance | No safety | Collections.synchronizedMap(): safe |

## Common Code Review Comments

- "Why are you using LinkedHashMap? HashMap is faster if you don't need order."
- "This LinkedHashMap is for LRU cache — make sure to override removeEldestEntry()."
- "Consider using Map.of() if this map is immutable."
- "This map is being iterated concurrently — use Collections.synchronizedMap()."

## Common Production Mistakes

> Notice: LinkedHashMap is slightly slower than HashMap — don't use it if order doesn't matter.

> Notice: LinkedHashMap memory overhead is higher than HashMap — each entry has a linked list node.

> Notice: LinkedHashMap is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: LinkedHashMap(accessOrder=true) moves entries on access — this affects iteration order.
