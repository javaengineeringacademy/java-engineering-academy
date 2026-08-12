# Decision Guide: Wildcards

## Decision Tree

```
Do you need to use a generic type?
├── Need to READ from the generic type?
│   ├── Yes → Do you need a specific upper bound?
│   │   ├── Yes → Use <? extends BoundType> (upper wildcard)
│   │   └── No → Use <?> (unbounded wildcard)
│   └── No → Do you need to WRITE to the generic type?
│       ├── Yes → Do you need to write a specific supertype?
│       │   ├── Yes → Use <? super SpecificType> (lower wildcard)
│       │   └── No → Use a type parameter <T> instead
│       └── No → Use <?> for read-only flexibility
└── Need both READ and WRITE?
    └── Use a type parameter <T>, not a wildcard
```

## PECS Decision Framework

**Producer Extends, Consumer Super (PECS):**

| Role | Syntax | Use When |
|---|---|---|
| Producer (reads from) | `? extends T` | You only get items from the structure |
| Consumer (writes to) | `? super T` | You only put items into the structure |
| Both read/write | `<T>` | You need full read/write access |
| Invariant | `<T>` | Type safety requires exact type match |

## Three Forms of Wildcards

| Wildcard | Read | Write | Use Case |
|---|---|---|---|
| `<?>` | Objects only | Nothing | Passing to methods that don't modify |
| `<? extends T>` | T subtypes | Nothing | Returning data from a source |
| `<? super T>` | Objects | T or subtypes | Accepting data for storage |

## When to Use Wildcards

- Method doesn't modify the collection, only reads it
- You want API flexibility (accept `List<Integer>` where `List<Number>` is expected)
- Implementing PECS pattern in methods like `Collections.copy`
- Returning data from heterogeneous sources
- Accepting callbacks with flexible type signatures

## When to Avoid Wildcards

- When you need to write to the collection (use type parameters)
- When the method returns the same type it accepts (use type parameters)
- Overuse makes APIs hard to understand
- When a concrete type is always expected

## Decision Rules

1. **Always prefer wildcards for method parameters** over type parameters for flexibility
2. **Use `? super T` for consumers** — methods that add elements
3. **Use `? extends T` for producers** — methods that read elements
4. **Use `?` for truly unconstrained** — when type doesn't matter at all
5. **Never use wildcards for return types** — use type parameters instead
6. **Stacking wildcards `<? extends ? super T>`** is illegal — don't attempt

## Engineering Trade-offs

| Factor | `<?>` | `<? extends T>` | `<? super T>` | `<T>` |
|---|---|---|---|---|
| Read safety | Objects | T | Objects | T |
| Write safety | None | None | T | T |
| Flexibility | High | Moderate | Moderate | Low |
| Complexity | Low | Moderate | Moderate | Low |

## Common Code Review Comments

- "Use `? extends Number` instead of `?` to get type-safe reads"
- "This should be `? super T` since you're adding elements"
- "You can't write to `? extends T` — refactor to use a type parameter"
- "Wildcard in return type means callers lose type safety"
- "Apply PECS: this is a producer, use extends"

## Production Patterns

```java
// Pattern: Producer — reads from source
public static double sumOfList(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

// Pattern: Consumer — writes to destination
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) dest.add(item);
}

// Pattern: Unbounded — just iterates
public static void printAll(List<?> list) {
    list.forEach(System.out::println);
}

// Pattern: Super wildcard for Comparable
public static <T extends Comparable<? super T>> void sort(List<T> list) { ... }
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| `List<?>` as method parameter when you add items | Use `<T>` or `? super T` |
| `List<? extends Number>` and trying to add `Integer` | Use `List<Number>` or `? super Number` |
| Wildcard in return type | Use a type parameter instead |
| Using `?` when you need to reference the type | Use `<T>` to capture the type |
