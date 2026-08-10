# filter() Decision Guide

## Decision Tree

```
Need to select elements?
├── Need filtered view? → filter()
├── Need in-place removal? → removeIf()
├── Need to filter and collect? → filter().collect()
├── Need to filter and count? → filter().count()
└── Need to filter and find? → filter().findFirst()
```

## Comparison Matrix

| Feature | filter() | removeIf() | if-else |
|---------|----------|------------|---------|
| Modifies original | No | Yes | No |
| Returns | Stream | boolean | void |
| Time | O(n) | O(n) | O(n) |
| Use case | Functional | Imperative | Imperative |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Filtered view | filter() | Returns Stream |
| In-place removal | removeIf() | Modifies original |
| Functional style | filter() | Functional |
| Imperative style | removeIf() | Imperative |
| Filter and collect | filter().collect() | Returns collection |

## Production Recommendations

> **Use filter() for functional style** — it's more readable and composable.

> **Use removeIf() for in-place removal** — it's more efficient for mutable lists.

> **Use filter().collect() to collect results** — it's more functional.

> **Use filter().findFirst() to find first match** — it's more efficient than filter().collect().

## Engineering Trade-offs

| Trade-off | filter() | removeIf() |
|-----------|----------|------------|
| Functional vs Imperative | Functional | Imperative |
| Immutable vs Mutable | Immutable | Mutable |
| View vs Copy | View | Copy |
| Composability vs Simplicity | Composable | Simple |

## Common Code Review Comments

- "Use filter() for functional style — it's more readable."
- "Use removeIf() for in-place removal — it's more efficient."
- "Use filter().collect() to collect results — it's more functional."
- "This if-else should be filter()."

## Common Production Mistakes

> Notice: filter() doesn't modify the original collection — use removeIf() for in-place removal.

> Notice: filter() is lazy — it doesn't execute until a terminal operation is called.

> Notice: filter() can throw NullPointerException — use filter(Objects::nonNull) for null safety.

> Notice: filter() is O(n) — for large collections, consider parallel stream.