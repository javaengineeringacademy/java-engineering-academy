# Sorting Decision Guide

## Decision Tree

```
Need to sort elements?
├── Elements implement Comparable? → Collections.sort() or list.sort()
├── Need custom ordering? → Comparator
├── Need to sort primitives? → Arrays.sort()
├── Need stable sort? → TimSort (default for objects)
├── Need unstable sort? → DualPivotQuicksort (default for primitives)
└── Need parallel sort? → Arrays.parallelSort()
```

## Comparison Matrix

| Algorithm | Stable | Time (avg) | Space | Use Case |
|-----------|--------|------------|-------|----------|
| TimSort | Yes | O(n log n) | O(n) | Object sorting |
| DualPivotQuicksort | No | O(n log n) | O(log n) | Primitive sorting |
| Merge Sort | Yes | O(n log n) | O(n) | Linked lists |
| Heap Sort | No | O(n log n) | O(1) | Memory-constrained |
| Insertion Sort | Yes | O(n^2) | O(1) | Small arrays |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| General-purpose sorting | Collections.sort() | TimSort, O(n log n) |
| Custom ordering | Comparator | Flexible, functional |
| Primitive arrays | Arrays.sort() | DualPivotQuicksort |
| Parallel sorting | Arrays.parallelSort() | Multi-threaded |
| Stable sort | Collections.sort() | TimSort is stable |
| Memory-constrained | Arrays.sort() (primitives) | O(log n) space |

## Production Recommendations

> **Use Collections.sort()** — it's TimSort, which is O(n log n) and stable.

> **Implement Comparable** for natural ordering — it's faster than Comparator for single-type sorting.

> **Use Comparator for multiple orderings** — it's more flexible and can be composed.

> **Pre-size your list** before sorting — avoid resizing during the sort.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Natural vs Custom ordering | Comparable (natural) | Comparator (custom) |
| Stability vs Speed | TimSort (stable) | DualPivotQuicksort (faster) |
| In-place vs Copy | Arrays.sort() (in-place) | stream().sorted() (copy) |
| Sequential vs Parallel | Collections.sort() | Arrays.parallelSort() |
| Immutability vs Mutability | stream().sorted() (immutable) | Collections.sort() (mutable) |

## Common Code Review Comments

- "This class should implement Comparable for natural ordering."
- "Consider using Comparator.comparing() for cleaner code."
- "This sort is O(n^2) — use Collections.sort() instead."
- "This list should be pre-sized before sorting."

## Common Production Mistakes

> Notice: Collections.sort() modifies the original list — use stream().sorted().collect() if you need a sorted copy.

> Notice: Comparator.comparing() throws NullPointerException for null values — use Comparator.nullsFirst() or nullsLast().

> Notice: TimSort requires random access — for LinkedList, convert to ArrayList first for better performance.

> Notice: Comparable and Comparator contracts — violating them causes unpredictable behavior in sorted collections.
