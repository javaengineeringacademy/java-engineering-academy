# Set Interface Decision Guide

## Decision Tree

```
Need unique elements?
├── Need ordering?
│   ├── Insertion order → LinkedHashSet
│   ├── Sorted order → TreeSet
│   └── No order → HashSet (fastest)
├── Need null element?
│   ├── Yes → HashSet (one null allowed)
│   └── No → Any implementation
├── Need enum keys? → EnumSet (fastest)
└── Need thread safety?
    ├── Yes → Collections.synchronizedSet() or CopyOnWriteArraySet
    └── No → Any implementation
```

## Comparison Matrix

| Implementation | Order | Null | Thread-Safe | Performance | Memory |
|---------------|-------|------|-------------|-------------|--------|
| HashSet | None | One null | No | O(1) | Low |
| LinkedHashSet | Insertion | One null | No | O(1) | Medium |
| TreeSet | Sorted | No | No | O(log n) | Medium |
| EnumSet | Enum order | No | No | O(1) bit-vector | Very Low |
| CopyOnWriteArraySet | None | One null | Yes (copy) | O(n) | High |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose set | HashSet | Fastest, no ordering |
| Insertion order | LinkedHashSet | Maintains order |
| Sorted elements | TreeSet | Natural/custom ordering |
| Enum constants | EnumSet | Bit-vector, fastest |
| Thread-safe | CopyOnWriteArraySet | Read-heavy concurrent |
| Immutable | Set.of() | Thread-safe, no modification |

## Production Recommendations

> **Default to HashSet** unless you need ordering. It's the fastest and most memory-efficient.

> **Use EnumSet for enum constants** — it's a bit-vector implementation with O(1) operations and minimal memory.

> **Avoid TreeSet for simple deduplication** — it's slower than HashSet and only needed when you need sorted order.

> **Use Set.of() for constants** — it's immutable and thread-safe.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Speed vs Ordering | HashSet (fast, no order) | LinkedHashSet (fast, insertion order) |
| Memory vs Sort | HashSet (low memory) | TreeSet (sorted, higher memory) |
| Immutability vs Flexibility | Set.of() (immutable) | HashSet (mutable) |
| Thread-safety vs Performance | CopyOnWriteArraySet (safe, slow writes) | HashSet (fast, no safety) |
| Generality vs Specialization | HashSet (general) | EnumSet (enum-specific, fastest) |

## Common Code Review Comments

- "Why are you using TreeSet? HashSet is faster if you don't need sorting."
- "This should be an EnumSet — you're using enum values as elements."
- "Consider using Set.of() if this set is immutable."
- "This set is being iterated concurrently — use CopyOnWriteArraySet or Collections.synchronizedSet()."

## Common Production Mistakes

> Notice: HashSet doesn't maintain order — if you need insertion order, use LinkedHashSet.

> Notice: TreeSet requires elements to be Comparable or you must provide a Comparator — otherwise you get ClassCastException at runtime.

> Notice: EnumSet is the fastest Set implementation — always use it when your elements are enums.

> Notice: HashSet allows one null element — but in concurrent code, prefer Optional over null.
