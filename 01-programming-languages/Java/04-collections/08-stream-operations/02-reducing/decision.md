# reduce() Decision Guide

## Decision Tree

```
Need to aggregate elements?
├── Need single value? → reduce()
├── Need sum/count? → sum(), count()
├── Need collection? → collect()
├── Need to forEach? → forEach()
└── Need to reduce and collect? → reduce().collect()
```

## Comparison Matrix

| Feature | reduce() | sum()/count() | collect() |
|---------|----------|---------------|-----------|
| Returns | Single value | Single value | Collection |
| Time | O(n) | O(n) | O(n) |
| Use case | Aggregation | Simple aggregation | Collection |
| Functional | Yes | Yes | Yes |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Aggregate to single value | reduce() | Returns single value |
| Sum/count | sum(), count() | Simpler, more readable |
| Collect to collection | collect() | Returns collection |
| forEach | forEach() | Side effects |
| Reduce and collect | reduce().collect() | Returns collection |

## Production Recommendations

> **Use reduce() for aggregation** — it's more functional and composable.

> **Use sum()/count() for simple aggregation** — it's more readable.

> **Use collect() for collection aggregation** — it's more functional.

> **Use forEach() for side effects** — it's more imperative.

## Engineering Trade-offs

| Trade-off | reduce() | sum()/count() |
|-----------|----------|---------------|
| Flexibility vs Simplicity | Flexible | Simple |
| Functional vs Imperative | Functional | Functional |
| Generic vs Specific | Generic | Specific |
| Composability vs Readability | Composable | Readable |

## Common Code Review Comments

- "Use reduce() for aggregation — it's more functional."
- "Use sum()/count() for simple aggregation — it's more readable."
- "Use collect() for collection aggregation — it's more functional."
- "This for loop should be reduce()."

## Common Production Mistakes

> Notice: reduce() can throw NullPointerException — use reduce(0, Integer::sum) for null safety.

> Notice: reduce() is associative — violating this causes unpredictable behavior.

> Notice: reduce() is O(n) — for large collections, consider parallel stream.

> Notice: reduce() can throw UnsupportedOperationException — use collect() for mutable reduction.