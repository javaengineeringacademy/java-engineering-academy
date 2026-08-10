# TimSort Decision Guide

## Decision Tree

```
Need to sort objects?
├── Sort objects? → TimSort (default)
├── Sort primitives? → DualPivotQuicksort (default)
├── Need stable sort? → TimSort
├── Need unstable sort? → DualPivotQuicksort
├── Need parallel sort? → ParallelTimSort
└── Need memory-efficient sort? → DualPivotQuicksort
```

## Comparison Matrix

| Feature | TimSort | DualPivotQuicksort | Merge Sort |
|---------|---------|-------------------|------------|
| Stability | Yes | No | Yes |
| Time (avg) | O(n log n) | O(n log n) | O(n log n) |
| Space | O(n) | O(log n) | O(n) |
| Use case | Objects | Primitives | Linked lists |
| Performance | Good | Better | Good |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Sort objects | TimSort | Default for objects, stable |
| Sort primitives | DualPivotQuicksort | Default for primitives, faster |
| Stable sort | TimSort | Preserves equal element order |
| Memory-constrained | DualPivotQuicksort | O(log n) space |
| Parallel sort | ParallelTimSort | Multi-threaded |

## Production Recommendations

> **Use TimSort for object sorting** — it's the default, O(n log n), and stable.

> **Use DualPivotQuicksort for primitive sorting** — it's the default and faster.

> **TimSort is adaptive** — it performs well on partially sorted data.

> **TimSort requires random access** — for LinkedList, convert to ArrayList first.

## Engineering Trade-offs

| Trade-off | TimSort | DualPivotQuicksort |
|-----------|---------|-------------------|
| Stability vs Speed | Stable | Faster |
| Space vs Speed | O(n) space | O(log n) space |
| Adaptive vs Simple | Adaptive | Simple |
| Object vs Primitive | Objects | Primitives |

## Common Code Review Comments

- "TimSort is stable — use it when you need to preserve equal element order."
- "DualPivotQuicksort is faster for primitives — use it when stability isn't needed."
- "TimSort requires random access — convert LinkedList to ArrayList first."
- "TimSort is adaptive — it performs well on partially sorted data."

## Common Production Mistakes

> Notice: TimSort requires random access — for LinkedList, convert to ArrayList first for better performance.

> Notice: TimSort is not in-place — it requires O(n) extra space.

> Notice: TimSort is not suitable for very small arrays — Insertion Sort is faster for small arrays.

> Notice: TimSort is not suitable for very large arrays — ParallelTimSort is faster for large arrays.