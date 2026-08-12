# Decision Guide: Bounded Type Parameters

## Decision Tree

```
Does your generic type need to support operations beyond Object?
├── Yes → Do you need a single supertype constraint?
│   ├── Yes → Use upper bound: <T extends UpperType>
│   └── No → Do you need multiple constraints?
│       ├── Yes → Use multiple bounds: <T extends A & B & C>
│       └── No → Reconsider — multiple bounds are rare
├── Do you need lower bounds?
│   ├── Yes → Use wildcards (? super T), not bounded type params
│   └── No → Use unbounded <T>
└── Is this for recursive type comparison?
    └── Yes → Use self-bounded: <T extends Comparable<T>>
```

## Comparison Matrix

| Bound Type | Syntax | Purpose | Example |
|---|---|---|---|
| Upper bound | `<T extends Number>` | T must be Number or subclass | `method(List<T extends Number>)` |
| Multiple bounds | `<T extends A & B>` | T must implement all | `<T extends Comparable & Serializable>` |
| Self-bounded | `<T extends Comparable<T>>` | T must compare to itself | `sort(List<T>)` |
| Unbounded | `<T>` | No constraints | `identity(T t)` |

## When to Use Bounded Type Parameters

- Need to call methods on the type parameter (beyond `Object` methods)
- Enforcing contracts at compile time (e.g., `Comparable`, `Serializable`)
- Multiple interface requirements (e.g., `Cloneable & Serializable`)
- Recursive type bounds for self-referential generics
- Building type-safe comparison/sorting utilities

## When to Avoid

- When wildcards (`? extends T`) would suffice for read-only access
- Over-constraining reduces flexibility — prefer the weakest sufficient bound
- When the bound is never actually used in the method body
- Deeply nested self-bounded types that hurt readability

## Decision Rules

1. **Prefer wildcards over bounded type params** when only read access is needed
2. **Order bounds by importance** — class first, then interfaces: `<T extends Comparable<T> & Serializable>`
3. **Only one class** can appear in bounds (Java single inheritance)
4. **Self-bounds should be used sparingly** — only when the type must refer to itself
5. **Use `&` for multiple bounds** — comma is not valid here

## Engineering Trade-offs

| Factor | Upper Bound | Multiple Bounds | Self-Bound |
|---|---|---|---|
| Simplicity | High | Low | Low |
| Safety | High | Very High | Very High |
| Flexibility | Moderate | Low | Low |
| Readability | High | Moderate | Low |
| Use frequency | Common | Rare | Rare |

## Common Code Review Comments

- "This bound is unnecessary — the method doesn't use it"
- "You have a class before an interface in bounds — swap the order"
- "Consider using `? extends T` instead of `T` here"
- "The self-bounded pattern is overly complex — simplify the API"
- "Multiple bounds should be avoided if only one is actually needed"

## Production Patterns

```java
// Pattern: Single upper bound
public static <T extends Comparable<T>> T max(List<T> list) { ... }

// Pattern: Multiple bounds (class first)
public static <T extends Comparable<T> & Serializable> T serializeMax(List<T> list) { ... }

// Pattern: Recursive type bound
public abstract class Builder<T extends Builder<T>> {
    @SuppressWarnings("unchecked")
    public T self() { return (T) this; }
}
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Interface before class in bounds | Swap: `<T extends Comparable<T> & Serializable>` |
| Bounding to an interface when no methods used | Remove the bound |
| Using `?` where `<T extends X>` is needed | Use `<T>` when you need to capture the type |
| Missing self-bound in builder pattern | Add `<T extends Builder<T>>` |
