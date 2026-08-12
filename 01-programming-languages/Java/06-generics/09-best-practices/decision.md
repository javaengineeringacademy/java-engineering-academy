# Decision Guide: Best Practices

## Decision Tree

```
Are you designing a generic API?
├── Yes → Follow naming conventions
│   ├── E → Element (collections)
│   ├── K, V → Key, Value (maps)
│   ├── T, U, S → General types
│   ├── N → Number
│   └── R → Return type
├── Method parameter design
│   ├── Read-only → Use ? extends T (PECS producer)
│   ├── Write-only → Use ? super T (PECS consumer)
│   ├── Both → Use <T>
│   └── Don't care → Use <?>
└── Class design
    ├── Single type → <T>
    ├── Key-value → <K, V>
    └── Complex hierarchy → F-bounded <T extends Base<T>>
```

## Naming Conventions

| Letter | Convention | Use Case |
|---|---|---|
| `E` | Element | Collection type parameters |
| `K` | Key | Map key type |
| `V` | Value | Map value type |
| `T` | Type | General purpose |
| `U`, `S` | Additional types | Second, third type parameters |
| `N` | Number | Numeric type parameters |
| `R` | Result | Return type in functional interfaces |

## Wildcards vs Type Parameters

| Situation | Use Wildcard | Use Type Parameter |
|---|---|---|
| Method only reads the generic | `? extends T` | — |
| Method only writes to the generic | `? super T` | — |
| Method reads and writes | — | `<T>` |
| Return type is generic | — | `<T>` (never wildcards) |
| Field type | — | `<T>` (wildcards in fields are rare) |
| Method parameter with flexibility | `? extends T` / `? super T` | — |

## PECS Decision Rules

1. **Producer Extends** — if the generic type gives you data, use `? extends T`
2. **Consumer Super** — if the generic type receives data, use `? super T`
3. **Both** — don't use wildcards; use a type parameter `<T>`
4. **Neither** — use `<?>` or `<T>` depending on whether you need the type reference

## When to Follow Each Practice

- **PECS**: API methods that work with collections from external sources
- **F-bounded polymorphism**: Builder patterns, self-referential types
- **Named type parameters**: Public APIs, library design
- **Unbounded wildcards**: Methods that just iterate or print

## Decision Rules

1. **Prefer wildcards over type parameters** for method arguments — more flexible
2. **Never use wildcards in return types** — use type parameters
3. **Name type parameters meaningfully** — avoid `A`, `B`, `C` in public APIs
4. **Limit type parameters to 2–3** — more indicates the design needs simplification
5. **Use bounded types when operations are needed** — `<T extends Comparable<T>>`
6. **Document type parameter constraints** — Javadoc should explain bounds

## Engineering Trade-offs

| Practice | Benefit | Cost |
|---|---|---|
| PECS | Maximum API flexibility | Harder to understand for beginners |
| Named conventions | Readability | Slightly more verbose |
| Bounded types | Compile-time safety | Less flexible for callers |
| Wildcards | Subtype acceptance | Cannot write to collection |
| F-bounded | Self-referential safety | Complex syntax |

## Common Code Review Comments

- "This should use `? extends T` for producer pattern"
- "Apply PECS: this parameter is a consumer, use `? super T`"
- "Rename this type parameter — `A` is not descriptive"
- "You have too many type parameters — simplify the design"
- "Don't use wildcards in return types — use a type parameter"
- "Add Javadoc explaining what `T` represents here"

## Production Patterns

```java
// Pattern: PECS in method signatures
public static <T> void copy(List<? super T> dest, List<? extends T> src) { ... }

// Pattern: Named type parameters for clarity
public interface Repository<Entity, Id> { ... }

// Pattern: F-bounded for builders
public abstract class Builder<T extends Builder<T>> { ... }

// Pattern: Wildcard for flexible API
public void process(Consumer<? super T> consumer) { ... }
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Wildcard in return type | Use `<T>` type parameter |
| Type parameter when wildcard suffices | Use `? extends T` or `? super T` |
| Single-letter names in public API | Use descriptive names or document |
| No bounds when operations needed | Add `<T extends X>` |
| Overusing type parameters | Simplify; use wildcards where possible |
