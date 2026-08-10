# Searching Decision Guide

## Decision Tree

```
Need to find elements?
├── Need binary search? → Collections.binarySearch() (must be sorted)
├── Need linear search? → indexOf() or contains()
├── Need first/last? → NavigableSet/NavigableMap
├── Need parallel search? → stream().filter().findFirst()
└── Need custom search? → Predicate with removeIf()
```

## Comparison Matrix

| Method | Time | Sorted Required | Returns | Use Case |
|--------|------|-----------------|---------|----------|
| indexOf() | O(n) | No | Index | Linear search |
| contains() | O(n) | No | boolean | Membership check |
| binarySearch() | O(log n) | Yes | Index | Sorted search |
| stream().filter() | O(n) | No | Optional | Functional search |
| NavigableSet.floor() | O(log n) | Yes | Element | Closest match |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Unsorted collection | indexOf() or contains() | No sorted requirement |
| Sorted collection | Collections.binarySearch() | O(log n) |
| First/last element | NavigableSet/NavigableMap | Built-in methods |
| Custom predicate | stream().filter().findFirst() | Functional style |
| Membership check | contains() | Simplest |

## Production Recommendations

> **Use contains() for membership checks** — it's the simplest and most readable.

> **Use binarySearch() for sorted collections** — it's O(log n) and faster than linear search.

> **Use stream().filter() for complex predicates** — it's more readable than manual loops.

> **Pre-size your list** before binary search — avoid resizing during the search.

## Engineering Trade-offs

| Trade-off | Option A | Option B |
|-----------|----------|----------|
| Speed vs Simplicity | binarySearch() (fast) | indexOf() (simple) |
| Sorted vs Unsorted | binarySearch() (sorted) | indexOf() (unsorted) |
| Functional vs Imperative | stream().filter() (functional) | for loop (imperative) |
| Exact vs Fuzzy | contains() (exact) | stream().filter() (fuzzy) |
| Single vs Multiple | findFirst() (single) | filter().collect() (multiple) |

## Common Code Review Comments

- "This collection should be sorted before binary search."
- "Consider using stream().filter() for complex search logic."
- "This indexOf() is O(n) — use a Set for frequent lookups."
- "This contains() call is in a loop — use a Set for O(1) lookups."

## Common Production Mistakes

> Notice: binarySearch() requires sorted collections — otherwise the result is undefined.

> Notice: indexOf() returns -1 if not found — don't use it as an index without checking.

> Notice: contains() calls equals() — make sure your equals() implementation is correct.

> Notice: stream().filter().findFirst() is lazy — it stops at the first match, which is efficient.
