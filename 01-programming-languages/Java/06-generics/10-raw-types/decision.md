# Decision Guide: Raw Types

## Decision Tree

```
Are you encountering raw types?
├── Is this legacy code being maintained?
│   ├── Yes → Keep raw types if changing would break compatibility
│   └── No → Add proper type parameters
├── Is this a new codebase?
│   ├── Yes → Never use raw types — always parameterize
│   └── No → Gradually migrate to parameterized types
└── Are you interoping with pre-generics code?
    ├── Yes → Use raw types at the boundary, add @SuppressWarnings
    └── No → Use parameterized types throughout
```

## Raw Types vs Parameterized Types

| Aspect | Raw Type | Parameterized Type |
|---|---|---|
| Type safety | None | Compile-time checked |
| Runtime behavior | All operations return `Object` | Same runtime (type erased) |
| Warnings | Yes — unchecked warnings | Clean compilation |
| Legacy support | Yes — pre-JDK 5 compatibility | No — requires JDK 5+ |
| Subtyping | Raw `List` is supertype of all `List<T>` | Invariant subtyping |

## When Raw Types Appear

- Legacy code written before JDK 5
- Interoperating with pre-generics libraries
- Intentional use for runtime type flexibility
- Missing type parameters in dependency code
- Accidentally using raw types in generic contexts

## When to Use Raw Types

- Maintaining legacy code where parameterization would break API
- Bridging pre-generics and modern code at boundaries
- Never in new code — always use parameterized types

## When to Avoid Raw Types

- All new development
- When type safety matters
- When you want clean compilation without warnings
- When the compiler can help catch bugs

## Decision Rules

1. **Never use raw types in new code** — always add type parameters
2. **Use `@SuppressWarnings("unchecked")`** when raw types are unavoidable at boundaries
3. **Migrate gradually** — don't rewrite all legacy code at once
4. **Use `List<?>` over raw `List`** when you don't know the type
5. **Raw type subtyping**: `List` is a supertype of `List<String>`, `List<Integer>`, etc.
6. **Raw types are backward compatible** — they exist for pre-generics code

## Engineering Trade-offs

| Factor | Raw Type | Parameterized Type |
|---|---|---|
| Compatibility | Pre-JDK 5 | JDK 5+ |
| Safety | Unsafe | Type-safe |
| Warnings | Unchecked warnings | Clean |
| Flexibility | Maximum (unsafe) | Controlled |
| Readability | Lower | Higher |

## Common Code Review Comments

- "Replace raw type with parameterized type"
- "Use `List<?>` instead of raw `List` if the type is unknown"
- "Add `@SuppressWarnings` with explanation if raw type is intentional"
- "This raw type bypasses all generics safety checks"
- "Consider migrating this legacy code to use generics"

## Production Patterns

```java
// Pattern: Legacy bridge with suppression
@SuppressWarnings("unchecked")
List<String> legacy = (List<String>) rawList;

// Pattern: Wildcard instead of raw type
void process(List<?> list) { ... }  // not: void process(List list)

// Pattern: Safe migration from raw to parameterized
// Before:
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);

// After:
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Using raw `List` in method parameters | Use `List<T>` or `List<?>` |
| Raw type in field declarations | Add type parameter |
| Forgetting to migrate legacy code | Plan gradual migration |
| Using raw type to avoid warnings | Use `@SuppressWarnings` instead |
| Mixing raw and parameterized types | Standardize on parameterized |
