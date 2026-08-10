# LinkedHashSet Decision Guide

## Decision Tree

```
Need unique elements with order?
├── Need insertion order? → LinkedHashSet
├── Need sorted order? → TreeSet
├── Need no order? → HashSet (faster)
├── Need enum elements? → EnumSet (fastest)
└── Need thread safety? → Collections.synchronizedSet()
```

## Comparison Matrix

| Feature | LinkedHashSet | HashSet | TreeSet |
|---------|---------------|---------|---------|
| Order | Insertion | None | Sorted |
| Null | One | One | None |
| Performance | O(1) | O(1) | O(log n) |
| Memory | Medium | Low | Medium |
| Thread-safe | No | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Insertion order | LinkedHashSet | Maintains order |
| General-purpose | HashSet | Faster, no ordering |
| Sorted elements | TreeSet | Natural/custom ordering |
| Enum elements | EnumSet | Bit-vector, fastest |

## Production Recommendations

> **Use LinkedHashSet when insertion order matters** — it's almost as fast as HashSet.

> **Use HashSet if order doesn't matter** — it's faster and uses less memory.

> **Use LinkedHashMap for access-order (LRU)** — LinkedHashSet doesn't support access-order.

> **Use Set.of() for constants** — it's immutable and thread-safe.

## Engineering Trade-offs

| Trade-off | LinkedHashSet | Alternative |
|-----------|---------------|-------------|
| Order vs Memory | Medium memory, ordered | HashSet: low memory, no order |
| Order vs Speed | Slightly slower | HashSet: faster |
| Immutability vs Flexibility | Mutable | Set.of(): immutable |
| Thread-safety vs Performance | No safety | Collections.synchronizedSet(): safe |

## Common Code Review Comments

- "Why are you using LinkedHashSet? HashSet is faster if you don't need order."
- "This LinkedHashSet is for insertion order — LinkedHashMap doesn't support access-order for sets."
- "Consider using Set.of() if this set is immutable."
- "This set is being iterated concurrently — use Collections.synchronizedSet()."

## Common Production Mistakes

> Notice: LinkedHashSet is slightly slower than HashSet — don't use it if order doesn't matter.

> Notice: LinkedHashSet memory overhead is higher than HashSet — each element has a linked list node.

> Notice: LinkedHashSet is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: LinkedHashSet maintains insertion order — if you need access-order, use LinkedHashMap instead.
