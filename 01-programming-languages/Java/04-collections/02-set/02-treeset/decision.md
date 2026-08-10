# TreeSet Decision Guide

## Decision Tree

```
Need sorted unique elements?
├── Need natural ordering? → TreeSet (implements Comparable)
├── Need custom ordering? → TreeSet(Comparator)
├── Need no ordering? → HashSet (faster)
├── Need insertion order? → LinkedHashSet
└── Need thread safety? → Collections.synchronizedSortedSet()
```

## Comparison Matrix

| Feature | TreeSet | HashSet | LinkedHashSet |
|---------|---------|---------|---------------|
| Order | Sorted | None | Insertion |
| Null | No | One | One |
| Performance | O(log n) | O(1) | O(1) |
| Memory | Medium | Low | Medium |
| Thread-safe | No | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Sorted elements | TreeSet | Natural/custom ordering |
| General-purpose | HashSet | Faster, no ordering |
| Insertion order | LinkedHashSet | Maintains order |
| Enum elements | EnumSet | Bit-vector, fastest |

## Production Recommendations

> **Use TreeSet only when you need sorted order** — it's slower than HashSet for simple deduplication.

> **Implement Comparable** for natural ordering — it's faster than Comparator for single-type sorting.

> **Use Comparator for multiple orderings** — it's more flexible and can be composed.

> **Use NavigableSet methods** — floor(), ceiling(), higher(), lower() for range queries.

## Engineering Trade-offs

| Trade-off | TreeSet | Alternative |
|-----------|---------|-------------|
| Sorting vs Speed | O(log n), sorted | HashSet: O(1), no order |
| Memory vs Sort | Medium memory | HashSet: low memory |
| Immutability vs Flexibility | Mutable | Set.of(): immutable |
| Thread-safety vs Performance | No safety | Collections.synchronizedSortedSet(): safe |

## Common Code Review Comments

- "Why are you using TreeSet? HashSet is faster if you don't need sorting."
- "This TreeSet requires Comparable — make sure your elements implement it."
- "Consider using NavigableSet methods for range queries."
- "This TreeSet is being iterated concurrently — use Collections.synchronizedSortedSet()."

## Common Production Mistakes

> Notice: TreeSet requires elements to be Comparable or you must provide a Comparator — otherwise you get ClassCastException.

> Notice: TreeSet doesn't allow null elements — it will throw NullPointerException.

> Notice: TreeSet is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: TreeSet performance is O(log n) — don't use it for simple deduplication where HashSet is faster.
