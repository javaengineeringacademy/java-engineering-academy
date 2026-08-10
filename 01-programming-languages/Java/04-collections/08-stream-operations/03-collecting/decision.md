# collect() Decision Guide

## Decision Tree

```
Need to collect elements?
├── Need collection? → collect()
├── Need string join? → Collectors.joining()
├── Need grouping? → Collectors.groupingBy()
├── Need partitioning? → Collectors.partitioningBy()
└── Need simple aggregation? → reduce()
```

## Comparison Matrix

| Feature | collect() | reduce() | forEach |
|---------|-----------|----------|---------|
| Returns | Collection | Single value | void |
| Time | O(n) | O(n) | O(n) |
| Use case | Collection | Aggregation | Side effects |
| Functional | Yes | Yes | No |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Collect to collection | collect() | Returns collection |
| Join strings | Collectors.joining() | Simple, readable |
| Group by | Collectors.groupingBy() | Simple, readable |
| Partition | Collectors.partitioningBy() | Simple, readable |
| Simple aggregation | reduce() | Returns single value |

## Production Recommendations

> **Use collect() for collection aggregation** — it's more functional and composable.

> **Use Collectors.joining() for string join** — it's more readable.

> **Use Collectors.groupingBy() for grouping** — it's more readable.

> **Use Collectors.partitioningBy() for partitioning** — it's more readable.

## Engineering Trade-offs

| Trade-off | collect() | reduce() |
|-----------|-----------|----------|
| Collection vs Value | Collection | Value |
| Functional vs Imperative | Functional | Functional |
| Composability vs Simplicity | Composable | Simple |
| Flexible vs Specific | Flexible | Specific |

## Common Code Review Comments

- "Use collect() for collection aggregation — it's more functional."
- "Use Collectors.joining() for string join — it's more readable."
- "Use Collectors.groupingBy() for grouping — it's more readable."
- "This for loop should be collect()."

## Common Production Mistakes

> Notice: collect() is mutable reduction — use reduce() for immutable reduction.

> Notice: collect() can throw NullPointerException — use Collectors.toUnmodifiableList() for null safety.

> Notice: collect() is O(n) — for large collections, consider parallel stream.

> Notice: collect() can throw UnsupportedOperationException — use Collectors.toList() for mutable collection.