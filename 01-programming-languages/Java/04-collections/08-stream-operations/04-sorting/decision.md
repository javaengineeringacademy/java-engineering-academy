# sorted() Decision Guide

## Decision Tree

```
Need to sort stream?
├── Need sorted stream? → sorted()
├── Need sorted collection? → Collections.sort()
├── Need sorted copy? → sorted().collect()
├── Need sorted with comparator? → sorted(comparator)
└── Need sorted and limited? → sorted().limit()
```

## Comparison Matrix

| Feature | sorted() | Collections.sort() | sorted().collect() |
|---------|----------|-------------------|-------------------|
| Returns | Stream | void | Collection |
| Modifies original | No | Yes | No |
| Time | O(n log n) | O(n log n) | O(n log n) |
| Use case | Functional | Imperative | Functional |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Sorted stream | sorted() | Returns Stream |
| Sorted collection | Collections.sort() | Modifies original |
| Sorted copy | sorted().collect() | Returns collection |
| Sorted with comparator | sorted(comparator) | Custom ordering |
| Sorted and limited | sorted().limit() | Top N elements |

## Production Recommendations

> **Use sorted() for stream sorting** — it's more functional and composable.

> **Use Collections.sort() for list sorting** — it's more efficient for lists.

> **Use sorted().collect() for sorted copies** — it's more functional.

> **Use sorted(comparator) for custom ordering** — it's more flexible.

## Engineering Trade-offs

| Trade-off | sorted() | Collections.sort() |
|-----------|----------|-------------------|
| Functional vs Imperative | Functional | Imperative |
| Immutable vs Mutable | Immutable | Mutable |
| View vs Copy | View | Copy |
| Composability vs Simplicity | Composable | Simple |

## Common Code Review Comments

- "Use sorted() for stream sorting — it's more functional."
- "Use Collections.sort() for list sorting — it's more efficient."
- "Use sorted().collect() for sorted copies — it's more functional."
- "This for loop should be sorted()."

## Common Production Mistakes

> Notice: sorted() doesn't modify the original collection — use Collections.sort() for in-place sorting.

> Notice: sorted() is O(n log n) — for large collections, consider parallel stream.

> Notice: sorted() can throw ClassCastException — ensure elements are mutually comparable.

> Notice: sorted() is stable — it preserves equal element order.