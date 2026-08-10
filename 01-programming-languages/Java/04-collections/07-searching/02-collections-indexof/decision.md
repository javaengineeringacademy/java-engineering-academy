# Collections.indexOf() Decision Guide

## Decision Tree

```
Need to find element?
├── Need index? → indexOf()
├── Need existence? → contains()
├── Need first occurrence? → indexOf()
├── Need last occurrence? → lastIndexOf()
└── Need all occurrences? → Stream filter with index
```

## Comparison Matrix

| Feature | indexOf() | contains() | lastIndexOf() |
|---------|-----------|------------|---------------|
| Returns | Index | Boolean | Index |
| Time | O(n) | O(n) | O(n) |
| Use case | Find index | Check existence | Find last index |
| Null handling | Returns -1 | Returns false | Returns -1 |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Find index | indexOf() | Returns index |
| Check existence | contains() | Returns boolean |
| Find first occurrence | indexOf() | Returns first index |
| Find last occurrence | lastIndexOf() | Returns last index |
| Find all occurrences | Stream filter with index | Returns all indices |

## Production Recommendations

> **Use indexOf() to find index** — it's simpler than manual search.

> **Use contains() to check existence** — it's more readable than indexOf() != -1.

> **Use lastIndexOf() for last occurrence** — it's simpler than manual search.

> **Use Stream filter with index for all occurrences** — it's more functional.

## Engineering Trade-offs

| Trade-off | indexOf() | contains() |
|-----------|-----------|------------|
| Index vs Boolean | Returns index | Returns boolean |
| Simplicity vs Readability | Simple | More readable |
| First vs Last | First occurrence | Last occurrence |
| Performance vs Flexibility | Simple | Flexible |

## Common Code Review Comments

- "Use indexOf() to find index — it's simpler."
- "Use contains() to check existence — it's more readable."
- "Use lastIndexOf() for last occurrence — it's simpler."
- "This indexOf() != -1 check should be contains()."

## Common Production Mistakes

> Notice: indexOf() returns -1 if not found — check for -1, not null.

> Notice: contains() returns boolean — don't use indexOf() != -1 for existence check.

> Notice: indexOf() returns first occurrence — use lastIndexOf() for last occurrence.

> Notice: indexOf() is O(n) — for large sorted collections, use binary search.