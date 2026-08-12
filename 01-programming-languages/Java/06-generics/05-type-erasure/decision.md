# Decision Guide: Type Erasure

## Decision Tree

```
Do you need to understand type erasure behavior?
├── Are you doing runtime type checks on generics?
│   ├── Yes → Use `instanceof` with raw type or Class<T> token
│   └── No → Type erasure doesn't affect you
├── Do you need the actual type at runtime?
│   ├── Yes → Pass Class<T> as a type token
│   └── No → Rely on compile-time safety
└── Are you hitting erasure-related limitations?
    ├── Yes → See limitations table below
    └── No → Proceed normally
```

## Key Facts About Type Erasure

| Aspect | Behavior |
|---|---|
| Generic type info at runtime | Erased — replaced with bounds or `Object` |
| `<T>` becomes | `Object` (no bound) or first bound type |
| `<T extends Comparable>` becomes | `Comparable` |
| Type checks | Performed by compiler, not JVM |
| Bridge methods | Generated to preserve polymorphism |
| Overloading conflicts | Two methods differing only in generics can conflict |

## When Type Erasure Matters

- Writing code that inspects types at runtime (reflection, serialization)
- Overloading methods that differ only by generic parameters
- Understanding why `List<String>` and `List<Integer>` are the same at runtime
- Serialization/deserialization of generic types
- Debugging `ClassCastException` in generic code

## When It Doesn't Matter

- Normal compile-time generic usage
- Collections framework usage
- Method type inference
- Lambda expressions with generics
- Most application code

## Decision Rules

1. **Never rely on generic type info at runtime** — it's erased
2. **Use `Class<T>` tokens** when runtime type info is needed
3. **Don't overload methods differing only in generic params** — erasure causes conflicts
4. **`instanceof List<String>` is illegal** — use `List.class` or `List<?>`
5. **Serialization of generic types needs extra care** — use `TypeToken` pattern (Gson) or `ParameterizedTypeReference` (Spring)

## Engineering Trade-offs

| Concern | Erasure Approach | Reified Approach (C#) |
|---|---|---|
| Runtime type info | Not available | Available |
| Binary compatibility | Excellent | Complex |
| Performance | No boxing overhead | Potential boxing |
| Array creation | `new T[]` illegal | `new T[]` legal |
| `instanceof` generics | Not supported | Supported |

## Common Code Review Comments

- "This won't work at runtime — type info is erased"
- "Use a `Class<T>` token if you need the type at runtime"
- "This overload conflicts with erasure — rename or refactor"
- "Generic array creation is not allowed — use `List<T>` instead"
- "Add `@SuppressWarnings(\"unchecked\")` with a comment explaining why"

## Production Patterns

```java
// Pattern: Type token for runtime type info
public class Repository<T> {
    private final Class<T> type;
    public Repository(Class<T> type) { this.type = type; }
    public T create() { return type.getDeclaredConstructor().newInstance(); }
}

// Pattern: Safe generic array creation
@SuppressWarnings("unchecked")
T[] array = (T[]) new Object[size];

// Pattern: Avoiding erasure conflicts via different names
public void process(List<String> strings) { ... }
public void processNumbers(List<Integer> numbers) { ... }
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| `instanceof List<String>` | Use `instanceof List<?>` or `List.class` |
| `new T[size]` | Use `Object[]` with cast, or `Array.newInstance` |
| Overloading `process(List<String>)` / `process(List<Integer>)` | Rename methods |
| Assuming `ClassCastException` on generic type failure | Understand erasure removes the check |
