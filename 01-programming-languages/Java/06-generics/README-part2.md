# Module 06: Generics (Part 2)

## Performance Comparison

| Operation | Time Complexity | Space | Notes |
|-----------|----------------|-------|-------|
| Generic class instantiation | O(1) | Type parameter | Compile-time only |
| Type erasure | O(1) | None | Happens at compile time |
| Bounded type check | O(1) | None | Compile-time check |
| Wildcard capture | O(1) | None | Compile-time only |
| Generic method call | O(1) | Type inference | Compile-time only |

## Examples

### 1. Builder Pattern with Generics

```java
public class Builder<T> {
    private T value;
    
    public Builder<T> with(T value) {
        this.value = value;
        return this;
    }
    
    public T build() {
        return value;
    }
}
```

### 2. Generic Repository

```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
}
```

### 3. Type-Safe Heterogeneous Container

```java
public class TypeSafeContainer {
    private final Map<Class<?>, Object> map = new HashMap<>();
    
    public <T> void put(Class<T> type, T value) {
        map.put(type, type.cast(value));
    }
    
    public <T> T get(Class<T> type) {
        return type.cast(map.get(type));
    }
}
```

## Interview Questions

### Q1: What is type erasure?
**Answer:** Type erasure removes generic type information at compile time, converting List<String> to List. This ensures backward compatibility with pre-generics code.

### Q2: Can you create a generic array?
**Answer:** No, you cannot create `new T[]` due to type erasure. Use `Array.newInstance()` or `Object[]` with casting.

### Q3: What is the PECS principle?
**Answer:** Producer Extends, Consumer Super. Use `? extends T` when producing data, `? super T` when consuming.

### Q4: Can you overload methods with different generic types?
**Answer:** No, due to type erasure both methods have the same signature at runtime.

### Q5: What is a reified type?
**Answer:** A type whose type information is available at runtime. Generics are not reified (erased), but arrays and primitives are.

## Best Practices

**Do's:**
- Use bounded types (`<T extends Comparable<T>>`) to constrain generic types
- Prefer `List<? extends T>` for read-only access (Producer Extends)
- Prefer `List<? super T>` for write access (Consumer Super)
- Use type wildcards for flexibility in method parameters

**Don'ts:**
- Don't use raw types — always parameterize
- Don't create `new T()` or `new T[]` — type erasure prevents this
- Don't use `List<Object>` when you mean `List<?>` or `List<String>`
- Don't ignore unchecked cast warnings — suppress with `@SuppressWarnings` only when justified
- Don't use wildcards in return types — use concrete generic types

## Production Incidents

### Incident 1: ClassCastException from Raw Type Usage

**Problem:** A data migration tool crashed with `ClassCastException` when processing records from legacy database.
**Cause:** Raw `ArrayList` was used instead of `ArrayList<Record>`; casting `Object` to `Record` failed at runtime.
**Impact:** Migration failed for 50,000 records; required manual intervention; delayed project by 2 days.
**Solution:** Added generic type parameter `ArrayList<Record>` and removed explicit casting.
**Prevention:** Never use raw types; enable compiler warnings for raw type usage.

### Incident 2: Unchecked Cast Warning Causing Runtime Error

**Problem:** A generic utility method threw `ClassCastException` intermittently when processing different data types.
**Cause:** Unchecked cast from `Object` to generic type `T` without proper type checking; compiler warning was suppressed.
**Impact:** Application crashed for specific input combinations; 10% of requests affected.
**Solution:** Added runtime type checking using `Class.cast()`; removed `@SuppressWarnings("unchecked")`.
**Prevention:** Never suppress unchecked cast warnings without understanding implications.

## Code Review Checklist

- [ ] No raw types — all generics parameterized
- [ ] Bounded types used (`<T extends Comparable<T>>`) where appropriate
- [ ] Wildcards used correctly (Producer Extends, Consumer Super)
- [ ] `@SuppressWarnings("unchecked")` only with documented justification
- [ ] No `new T()` or `new T[]` (type erasure prevents this)
- [ ] Generic methods have proper type inference
- [ ] PECS principle applied to collection method parameters

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| ClassCastException at runtime | Stack trace + type erasure awareness | Identify line causing cast; check if generic type info was erased |
| Unchecked cast warning suppressed | Compiler warnings review | Search for `@SuppressWarnings("unchecked")`; verify type safety |
| Wildcard capture issues | Type inference analysis | Use helper methods for wildcard capture; verify PECS principle |
| Generic array creation error | Refactor to `Object[]` or `Class<T>` | Replace `new T[]` with `Array.newInstance()` or `Object[]` |
| Type mismatch in generic method | IDE type inference | Use IDE autocomplete to verify type inference; add explicit type arguments |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Basic generics | Java 5 | Stable |
| Bounded types (`extends`, `super`) | Java 5 | Stable |
| Wildcards (`? extends`, `? super`) | Java 5 | Stable |
| Diamond operator (`<>`) | Java 7 | Stable |
| Type inference improvements | Java 8 | Stable |
| `var` with generic types | Java 10 | Stable |

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses basic generics; doesn't understand type erasure; creates raw types accidentally |
| Intermediate | Uses bounded types; understands type erasure; applies PECS principle |
| Advanced | Designs type-safe APIs; uses wildcard capture; implements generic utilities |
| Expert | Creates advanced generic patterns; understands compiler internals; teaches generics |

## Common Myths

1. **Myth**: Generics provide runtime type safety
   **Truth**: Generics are erased at compile time (type erasure); runtime type information is lost.

2. **Myth**: `List<Integer>` is a subtype of `List<Number>`
   **Truth**: Java generics are invariant; `List<Integer>` is NOT a subtype of `List<Number>`. Use wildcards for variance.

3. **Myth**: You can create `new T()` with generics
   **Truth**: Type erasure means `T` becomes `Object` at runtime; you cannot instantiate generic types directly.

4. **Myth**: Wildcards make code more complex without benefit
   **Truth**: Wildcards enable flexible APIs that work with different type arguments while maintaining type safety.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Type safety and code reuse |
| Complexity | O(1) (type erasure) |
| Thread Safe | Yes (no state) |
| Ordered | N/A |
| Allows Null | Yes |
| Best Alternative | Specific types (for performance) |
| When to Use | Generic algorithms |
| When to Avoid | Simple types |
