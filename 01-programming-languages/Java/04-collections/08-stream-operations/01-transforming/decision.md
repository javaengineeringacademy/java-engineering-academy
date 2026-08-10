# map() Decision Guide

## Decision Tree

```
Need to transform elements?
├── Need transformation? → map()
├── Need to modify original? → forEach with modification
├── Need to transform and collect? → map().collect()
├── Need to transform and reduce? → map().reduce()
└── Need to transform and flatten? → flatMap()
```

## Comparison Matrix

| Feature | map() | forEach with modification | for loop |
|---------|-------|--------------------------|----------|
| Returns | Stream | void | void |
| Modifies original | No | Yes | Yes |
| Time | O(n) | O(n) | O(n) |
| Use case | Functional | Imperative | Imperative |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Transform elements | map() | Returns Stream |
| Modify original | forEach with modification | Modifies original |
| Functional style | map() | Functional |
| Imperative style | forEach with modification | Imperative |
| Transform and collect | map().collect() | Returns collection |

## Production Recommendations

> **Use map() for transformation** — it's more readable and composable.

> **Use map().collect() to collect results** — it's more functional.

> **Use map().reduce() to reduce results** — it's more functional.

> **Use map().flatMap() to flatten nested structures** — it's more functional.

## Engineering Trade-offs

| Trade-off | map() | forEach with modification |
|-----------|-------|--------------------------|
| Functional vs Imperative | Functional | Imperative |
| Immutable vs Mutable | Immutable | Mutable |
| View vs Copy | View | Copy |
| Composability vs Simplicity | Composable | Simple |

## Common Code Review Comments

- "Use map() for transformation — it's more readable."
- "Use map().collect() to collect results — it's more functional."
- "Use map().flatMap() to flatten nested structures — it's more functional."
- "This for loop should be map()."

## Common Production Mistakes

> Notice: map() doesn't modify the original collection — use forEach with modification for in-place changes.

> Notice: map() is lazy — it doesn't execute until a terminal operation is called.

> Notice: map() can throw NullPointerException — use map(Objects::nonNull) for null safety.

> Notice: map() is O(n) — for large collections, consider parallel stream.