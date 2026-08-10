# Comparator Decision Guide

## Decision Tree

```
Need custom ordering?
├── Single sort order? → Implement Comparable
├── Multiple sort orders? → Use Comparator
├── Need to sort existing class? → Use Comparator
├── Need to sort primitives? → Use Comparator
├── Need to sort with complex logic? → Use Comparator
└── Need to compose orderings? → Use Comparator.comparing()
```

## Comparison Matrix

| Feature | Comparator | Comparable |
|---------|------------|------------|
| Sorting orders | Multiple | Single |
| Implementation | External | In class |
| Method | compare() | compareTo() |
| Null handling | Can handle | Class dependent |
| Flexibility | High | Low |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Multiple orderings | Comparator | Flexible, can compose |
| Sort existing class | Comparator | Can't modify existing class |
| Sort primitives | Comparator | Primitives can't implement Comparable |
| Complex sorting logic | Comparator | External, can change |
| Natural ordering | Comparable | Single sort order, class-specific |

## Production Recommendations

> **Use Comparator.comparing() for cleaner code** — it's more readable than anonymous classes.

> **Use Comparator for multiple orderings** — it's more flexible and can be composed.

> **Use Comparator.nullsFirst/Last for null handling** — don't let nulls cause NullPointerException.

> **Use Comparator.thenComparing() for secondary sorting** — it's more readable than nested if-else.

## Engineering Trade-offs

| Trade-off | Comparator | Comparable |
|-----------|------------|------------|
| Flexibility | Multiple sort orders | Single sort order |
| Complexity | More complex | Simple |
| Performance | Slightly slower | Slightly faster |
| Maintainability | External, can change | In class, harder to change |

## Common Code Review Comments

- "Consider using Comparator.comparing() for cleaner code."
- "Use Comparator.nullsFirst() for null handling."
- "Use Comparator.thenComparing() for secondary sorting."
- "This Comparator should be static final for reuse."

## Common Production Mistakes

> Notice: Comparator.comparing() throws NullPointerException for null values — use Comparator.nullsFirst() or nullsLast().

> Notice: Comparator must be consistent with equals — violating this causes bugs in TreeMap, TreeSet, and Collections.sort().

> Notice: Comparator.compare() must be transitive — violating this causes unpredictable behavior.

> Notice: Comparator should be immutable — don't change behavior after creation.