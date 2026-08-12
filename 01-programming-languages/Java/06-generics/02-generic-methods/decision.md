# Decision Guide: Generic Methods

## Decision Tree

```
Do you need a method that works with different types?
├── Yes → Do you need the type to vary per invocation?
│   ├── Yes → Is the type related to the class's type parameter?
│   │   ├── Yes → Use the class type parameter directly
│   │   └── No → Declare a generic method with its own type parameter
│   └── No → Use a wildcard parameter (?)
└── No → Use a regular method with specific type
```

## Comparison Matrix

| Approach | Type Parameter Scope | Inference | Use Case |
|---|---|---|---|
| Generic class type param | Entire class | At class instantiation | Class fundamentally works with a type |
| Generic method type param | Method only | At method invocation | Single method needs a type |
| Wildcard `<?>` | N/A | At call site | Accepts any type, read-only |
| Overloaded methods | N/A | Compile-time | Fixed set of known types |

## When to Use Generic Methods

- Method logic is independent of the enclosing class's type parameter
- Utility/helper methods that operate on arbitrary types
- Factory methods returning different types based on input
- Methods converting between types (e.g., `List.toArray`)
- Static utility methods in non-generic classes

## When to Avoid

- When the class already has the appropriate type parameter — use it instead
- Overuse makes API signatures harder to read
- When only one concrete type is ever expected — generics add unnecessary complexity
- When raw types would be clearer for legacy interop

## Decision Rules

1. **Prefer instance method type parameters over static** — allows inference from the object
2. **Limit type parameters to 2–3** — more indicates the method does too much
3. **Use `<T extends Comparable<T>>` over `<T>`** when comparison is needed
4. **Place type parameters before return type** — `<T> List<T>`, not `List<T> method()`
5. **Name type parameters meaningfully** — `E` for elements, `K/V` for key/value, `T` for general

## Engineering Trade-offs

| Factor | Generic Method | Wildcard Method | Overloaded |
|---|---|---|---|
| Flexibility | High | High | Low |
| Readability | Moderate | High | High |
| Compile-time safety | High | Moderate | High |
| API surface | Single method | Single method | Multiple methods |
| Binary compatibility | Stable | Stable | Fragile |

## Common Code Review Comments

- "This should be a generic method instead of accepting `Object`"
- "You're not using the type parameter — remove it"
- "Consider using `? extends T` instead of `T` for covariance"
- "Type inference fails here — add an explicit type witness `<String>`"
- "This generic method could be simplified with a wildcard"

## Production Patterns

```java
// Pattern: Generic utility method
public static <T> List<T> filter(List<T> source, Predicate<T> pred) {
    return source.stream().filter(pred).collect(Collectors.toList());
}

// Pattern: Generic factory method
public static <T extends Enum<T>> T safeValueOf(Class<T> type, String name) {
    return Enum.valueOf(type, name);
}

// Pattern: Type witness for ambiguity resolution
Collections.<String>singletonList("hello")
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Using `Object` instead of type parameter | Replace with `<T>` and cast |
| Redundant type parameter (same as class) | Remove; use class-level parameter |
| Type parameter in wrong position | Move before return type |
| Forgetting type witness when inference fails | Add explicit `<Type>` |
