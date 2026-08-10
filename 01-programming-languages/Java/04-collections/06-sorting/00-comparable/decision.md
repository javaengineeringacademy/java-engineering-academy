# Comparable Decision Guide

## Decision Tree

```
Need to define natural ordering?
├── Single sort order? → Implement Comparable
├── Multiple sort orders? → Use Comparator
├── Need to sort custom class? → Implement Comparable
├── Need to sort existing class? → Use Comparator
└── Need to sort primitives? → Use Arrays.sort() with Comparator
```

## Comparison Matrix

| Feature | Comparable | Comparator |
|---------|------------|------------|
| Sorting orders | Single | Multiple |
| Implementation | In class | External |
| Method | compareTo() | compare() |
| Null handling | Class dependent | Can handle with nullsFirst/Last |
| Flexibility | Low | High |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Natural ordering | Comparable | Single sort order, class-specific |
| Multiple orderings | Comparator | Flexible, can compose |
| Sort existing class | Comparator | Can't modify existing class |
| Sort primitives | Comparator | Primitives can't implement Comparable |
| Null values | Comparator | Can use nullsFirst/Last |

## Production Recommendations

> **Implement Comparable for natural ordering** — it's faster than Comparator for single-type sorting.

> **Use Comparator for multiple orderings** — it's more flexible and can be composed.

> **Use Comparator.nullsFirst/Last for null handling** — don't let nulls cause NullPointerException.

> **Keep compareTo consistent with equals** — violating this causes bugs in sorted collections.

## Engineering Trade-offs

| Trade-off | Comparable | Comparator |
|-----------|------------|------------|
| Simplicity | Simple, one method | More complex, can compose |
| Flexibility | Single sort order | Multiple sort orders |
| Performance | Slightly faster | Slightly slower |
| Maintainability | In class | External, can change |

## Common Code Review Comments

- "This class should implement Comparable for natural ordering."
- "Consider using Comparator for multiple sort orders."
- "compareTo() should be consistent with equals()."
- "Use Comparator.nullsFirst() for null handling."

## Common Production Mistakes

> Notice: Comparable.compareTo() must be consistent with equals() — violating this causes bugs in TreeMap, TreeSet, and Collections.sort().

> Notice: Comparable.compareTo() can throw ClassCastException — always check type before casting.

> Notice: Primitives can't implement Comparable — use Comparator for primitive arrays.

> Notice: Comparable doesn't handle nulls — use Comparator.nullsFirst() for null values.