# Linear Search Decision Guide

## Decision Tree

```
Need to find element?
├── Data sorted? → Binary search
├── Data unsorted? → Linear search
├── Small collection? → Linear search
├── Large collection? → Binary search
├── Need index? → indexOf()
└── Need existence? → contains()
```

## Comparison Matrix

| Feature | Linear Search | Binary Search | indexOf() |
|---------|---------------|---------------|-----------|
| Time | O(n) | O(log n) | O(n) |
| Sorted data required | No | Yes | No |
| Returns | Element | Index | Index |
| Use case | Unsorted data | Sorted data | Find index |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Unsorted data | Linear search | No sorting required |
| Small collection | Linear search | Simple, no overhead |
| Sorted data | Binary search | Faster, O(log n) |
| Large collection | Binary search | Faster, O(log n) |
| Find index | indexOf() | Returns index |
| Check existence | contains() | Returns boolean |

## Production Recommendations

> **Use Linear search for unsorted data** — it's simple and doesn't require sorting.

> **Use Binary search for sorted data** — it's faster, O(log n).

> **Use indexOf() to find index** — it's simpler than manual search.

> **Use contains() to check existence** — it's more readable than indexOf() != -1.

## Engineering Trade-offs

| Trade-off | Linear Search | Binary Search |
|-----------|---------------|---------------|
| Speed vs Simplicity | Simple | Faster |
| Sorted vs Unsorted | Works on unsorted | Requires sorted data |
| Index vs Element | Returns element | Returns index |
| Small vs Large | Good for small | Good for large |

## Common Code Review Comments

- "Use Binary search for sorted data — it's faster."
- "Use indexOf() to find index — it's simpler."
- "Use contains() to check existence — it's more readable."
- "This linear search is O(n) — consider sorting and using binary search."

## Common Production Mistakes

> Notice: Linear search is O(n) — for large sorted collections, use binary search.

> Notice: Binary search requires sorted data — don't use it on unsorted data.

> Notice: indexOf() returns -1 if not found — check for -1, not null.

> Notice: contains() returns boolean — don't use indexOf() != -1 for existence check.