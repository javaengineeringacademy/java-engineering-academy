# HashSet Decision Guide

## Decision Tree

```
Need unique elements?
├── Need ordering? → No → HashSet (fastest)
├── Need insertion order? → LinkedHashSet
├── Need sorted order? → TreeSet
├── Need enum elements? → EnumSet (fastest)
├── Need thread safety? → Collections.synchronizedSet()
└── Need null element? → HashSet (one null allowed)
```

## Comparison Matrix

| Feature | HashSet | LinkedHashSet | TreeSet |
|---------|---------|---------------|---------|
| Order | None | Insertion | Sorted |
| Null | One | One | None |
| Performance | O(1) | O(1) | O(log n) |
| Memory | Low | Medium | Medium |
| Thread-safe | No | No | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose set | HashSet | Fastest, no ordering |
| Insertion order | LinkedHashSet | Maintains order |
| Sorted elements | TreeSet | Natural/custom ordering |
| Enum elements | EnumSet | Bit-vector, fastest |
| Thread-safe | Collections.synchronizedSet() | Simple wrapper |

## Production Recommendations

> **Default to HashSet** — it's the fastest and most memory-efficient for general-purpose use.

> **Use LinkedHashSet if you need insertion order** — it's almost as fast as HashSet.

> **Avoid TreeSet for simple deduplication** — it's slower and only needed for sorted order.

> **Use Set.of() for constants** — it's immutable and thread-safe.

## Engineering Trade-offs

| Trade-off | HashSet | Alternative |
|-----------|---------|-------------|
| Speed vs Ordering | Fast, no order | LinkedHashSet: fast, insertion order |
| Memory vs Sort | Low memory | TreeSet: sorted, higher memory |
| Immutability vs Flexibility | Mutable | Set.of(): immutable |
| Thread-safety vs Performance | No safety | Collections.synchronizedSet(): safe |

## Common Code Review Comments

- "Why are you using TreeSet? HashSet is faster if you don't need sorting."
- "This should be an EnumSet — you're using enum values as elements."
- "Consider using Set.of() if this set is immutable."
- "This set is being iterated concurrently — use Collections.synchronizedSet()."

## Common Production Mistakes

> Notice: HashSet doesn't maintain order — if you need insertion order, use LinkedHashSet.

> Notice: HashSet allows one null element — but in concurrent code, prefer Optional over null.

> Notice: HashSet is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: HashSet.hashCode() is called for each element — make sure your hashCode() implementation is efficient.
