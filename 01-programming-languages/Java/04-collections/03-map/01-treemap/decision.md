# TreeMap Decision Guide

## Decision Tree

```
Need sorted key-value storage?
├── Need natural ordering? → TreeMap (keys implement Comparable)
├── Need custom ordering? → TreeMap(Comparator)
├── Need no ordering? → HashMap (faster)
├── Need insertion order? → LinkedHashMap
└── Need thread safety? → Collections.synchronizedSortedMap()
```

## Comparison Matrix

| Feature | TreeMap | HashMap | LinkedHashMap |
|---------|---------|---------|---------------|
| Order | Sorted | None | Insertion |
| Null keys | No | One | One |
| Performance | O(log n) | O(1) | O(1) |
| Memory | Medium | Low | Medium |
| Thread-safe | No | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Sorted keys | TreeMap | Natural/custom ordering |
| General-purpose | HashMap | Faster, no ordering |
| Insertion order | LinkedHashMap | Maintains order |
| Enum keys | EnumMap | Bit-vector, fastest |

## Production Recommendations

> **Use TreeMap only when you need sorted order** — it's slower than HashMap for simple key-value storage.

> **Implement Comparable** for natural ordering — it's faster than Comparator for single-type sorting.

> **Use NavigableMap methods** — floorEntry(), ceilingEntry(), higherEntry(), lowerEntry() for range queries.

> **Use subMap() for ranges** — it's more efficient than filtering manually.

## Engineering Trade-offs

| Trade-off | TreeMap | Alternative |
|-----------|---------|-------------|
| Sorting vs Speed | O(log n), sorted | HashMap: O(1), no order |
| Memory vs Sort | Medium memory | HashMap: low memory |
| Immutability vs Flexibility | Mutable | Map.of(): immutable |
| Thread-safety vs Performance | No safety | Collections.synchronizedSortedMap(): safe |

## Common Code Review Comments

- "Why are you using TreeMap? HashMap is faster if you don't need sorting."
- "This TreeMap requires Comparable keys — make sure your keys implement it."
- "Consider using NavigableMap methods for range queries."
- "This TreeMap is being iterated concurrently — use Collections.synchronizedSortedMap()."

## Common Production Mistakes

> Notice: TreeMap requires keys to be Comparable or you must provide a Comparator — otherwise you get ClassCastException.

> Notice: TreeMap doesn't allow null keys — it will throw NullPointerException.

> Notice: TreeMap is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: TreeMap performance is O(log n) — don't use it for simple key-value storage where HashMap is faster.
