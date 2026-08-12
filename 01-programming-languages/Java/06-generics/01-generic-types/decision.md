# Decision Tree: When to Use Generic Types

## Quick Decision Flowchart

```
Do you need to write a class that works with multiple data types?
│
├─ NO → Use a concrete class with a specific type
│
└─ YES → Is the type known at compile time?
   │
   ├─ YES → Use a generic class with type parameter
   │  │
   │  ├─ Is the type constrained (e.g., must be Comparable)?
   │  │  ├─ YES → Use bounded type: <T extends Comparable<T>>
   │  │  └─ NO  → Use unbounded type: <T>
   │  │
   │  └─ Do you need multiple unrelated types?
   │     ├─ YES → Use multiple type parameters: <T, U, V>
   │     └─ NO  → Use single type parameter: <T>
   │
   └─ NO → Use raw types (not recommended) or Object with casting
```

## Comparison Matrix

| Criterion | Generic Class | Concrete Class | Raw Type | Object + Casting |
|---|---|---|---|---|
| **Type safety** | Compile-time | Compile-time | None | Runtime only |
| **Code reuse** | High | Low | Medium | Medium |
| **Readability** | High | High | Low | Low |
| **Performance** | Same as concrete | Best | Same as generic | Casting overhead |
| **Maintenance** | Easy | Hard (duplicated) | Error-prone | Error-prone |
| **When to use** | Multiple types | Single type | Legacy code only | Never |

## When to Use Generic Types

### Use Generic Types when:
- Writing a container that holds different types (Box, Stack, Queue)
- Implementing a data structure (ArrayList, HashMap)
- Creating a utility class that operates on various types (Collections.sort)
- Building a framework that must be type-agnostic (JPA Repository)
- Designing an API that multiple teams will consume

### Use Concrete Types when:
- The type is fixed and will never change
- Performance is critical and generics add no value
- The class is specific to one domain (UserRepository)

### Avoid Raw Types when:
- Writing new code (always parameterize)
- Maintaining existing code (gradually add type parameters)
- The type is truly unknown (use `Object` or `?` wildcard)

## Decision Rules

1. **Default to generics** — they are the modern Java approach
2. **Use bounded types** — constrain T when you need to call methods on it
3. **Avoid raw types** — they defeat the purpose of generics
4. **Use wildcards** — for flexibility in method parameters
5. **Prefer single type parameters** — unless multiple are truly needed

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Using generics | Type safety, code reuse | Slightly more complex syntax |
| Using bounded types | Can call methods on T | More verbose, harder to understand |
| Using raw types | Simpler syntax | Loses type safety, casting required |
| Multiple type parameters | Maximum flexibility | Complexity, readability |
| Single type parameter | Simplicity | Less flexible |

## Common Code Review Comments

- "This class should be generic — you're duplicating code for each type."
- "Use `Box<T>` not `Box` — raw types lose type safety."
- "Add bounds: `<T extends Comparable<T>>` — you're calling compareTo without constraint."
- "Why multiple type parameters? Can you simplify to one or two?"
- "This raw type usage will cause ClassCastException at runtime."

## Production Patterns

### Pattern 1: Simple Generic Container
```java
public class Container<T> {
    private T value;
    
    public void set(T value) { this.value = value; }
    public T get() { return value; }
}
```
Use when: You need a simple wrapper for a single value.

### Pattern 2: Bounded Generic Container
```java
public class SortedContainer<T extends Comparable<T>> {
    private List<T> items = new ArrayList<>();
    
    public void add(T item) {
        items.add(item);
        items.sort(Comparator.naturalOrder());
    }
}
```
Use when: You need to call specific methods on the type parameter.

### Pattern 3: Multiple Type Parameters
```java
public class Pair<K, V> {
    private final K key;
    private final V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
```
Use when: You need to associate two different types.

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| Using raw types | Loses type safety | Always parameterize: `Box<T>` not `Box` |
| Over-parameterizing | Hard to read, maintain | Use 1-2 type parameters maximum |
| No bounds when needed | Can't call methods on T | Add bounds: `<T extends Comparable<T>>` |
| Using `Object` instead of `T` | Loses type information | Use generic type parameter |
| Creating `new T()` | Type erasure prevents this | Pass `Class<T>` and use reflection |
