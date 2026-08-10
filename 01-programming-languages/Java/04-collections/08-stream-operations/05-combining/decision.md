# flatMap() Decision Guide

## Decision Tree

```
Need to flatten nested structures?
├── Need to flatten? → flatMap()
├── Need to transform? → map()
├── Need to concatenate? → concat()
├── Need to flatten and collect? → flatMap().collect()
└── Need to flatten and reduce? → flatMap().reduce()
```

## Comparison Matrix

| Feature | flatMap() | map() | concat() |
|---------|-----------|-------|----------|
| Returns | Stream | Stream | Stream |
| Flattens | Yes | No | No |
| Time | O(n) | O(n) | O(n) |
| Use case | Nested structures | Transformation | Concatenation |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Flatten nested structures | flatMap() | Returns flattened Stream |
| Transform elements | map() | Returns transformed Stream |
| Concatenate streams | concat() | Returns concatenated Stream |
| Flatten and collect | flatMap().collect() | Returns flattened collection |
| Flatten and reduce | flatMap().reduce() | Returns reduced value |

## Production Recommendations

> **Use flatMap() for flattening** — it's more functional and composable.

> **Use map() for transformation** — it's simpler for non-nested structures.

> **Use concat() for concatenation** — it's simpler for two streams.

> **Use flatMap().collect() for flattened collection** — it's more functional.

## Engineering Trade-offs

| Trade-off | flatMap() | map() |
|-----------|-----------|-------|
| Flattening vs Transformation | Flattening | Transformation |
| Nested vs Simple | Nested | Simple |
| Composability vs Simplicity | Composable | Simple |
| Flexible vs Specific | Flexible | Specific |

## Common Code Review Comments

- "Use flatMap() for flattening — it's more functional."
- "Use map() for transformation — it's simpler."
- "Use concat() for concatenation — it's simpler."
- "This nested for loop should be flatMap()."

## Common Production Mistakes

> Notice: flatMap() can throw NullPointerException — use flatMap(Objects::nonNull) for null safety.

> Notice: flatMap() is O(n) — for large collections, consider parallel stream.

> Notice: flatMap() is lazy — it doesn't execute until a terminal operation is called.

> Notice: flatMap() can cause stack overflow — use iterative approach for deep nesting.