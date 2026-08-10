# Binary Search Decision Guide

## Decision Tree

```
Need to find element?
├── Data sorted? → Binary search
├── Data unsorted? → Linear search
├── Large collection? → Binary search
├── Small collection? → Linear search
├── Need index? → Collections.binarySearch()
└── Need existence? → contains()
```

## Comparison Matrix

| Feature | Binary Search | Linear Search | Collections.binarySearch() |
|---------|---------------|---------------|---------------------------|
| Time | O(log n) | O(n) | O(log n) |
| Sorted required | Yes | No | Yes |
| Returns | Index | Element | Index |
| Use case | Sorted data | Unsorted data | Sorted List |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Sorted data | Binary search | Faster, O(log n) |
| Large collection | Binary search | Faster, O(log n) |
| Unsorted data | Linear search | No sorting required |
| Small collection | Linear search | Simple, no overhead |
| Find index in List | Collections.binarySearch() | Simple, built-in |
| Check existence | contains() | Returns boolean |

## Production Recommendations

> **Use Binary search for sorted data** — it's faster, O(log n).

> **Use Collections.binarySearch() for List** — it's simple and built-in.

> **Ensure data is sorted** — binary search doesn't work on unsorted data.

> **Use comparator for custom ordering** — it's more flexible than Comparable.

## Engineering Trade-offs

| Trade-off | Binary Search | Linear Search |
|-----------|---------------|---------------|
| Speed vs Simplicity | Faster | Simple |
| Sorted vs Unsorted | Requires sorted | Works on unsorted |
| Large vs Small | Good for large | Good for small |
| Index vs Element | Returns index | Returns element |

## Common Code Review Comments

- "Use Binary search for sorted data — it's faster."
- "Ensure data is sorted before using binary search."
- "Use Collections.binarySearch() for List — it's simpler."
- "This linear search is O(n) — consider sorting and using binary search."

## Common Production Mistakes

> Notice: Binary search requires sorted data — don't use it on unsorted data.

> Notice: Collections.binarySearch() returns -(insertion point) - 1 if not found — check for negative values.

> Notice: Binary search with Comparator must be consistent with equals — violating this causes unpredictable behavior.

> Notice: Binary search on LinkedList is O(n) — use ArrayList for better performance.