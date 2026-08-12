# Decision Guide: Type Erasure

## Key Engineering Decisions

### Decision 1: Generic Class vs Generic Method

```
Do you need type flexibility across multiple methods?
│
├─ YES → Use generic class: class Box<T> { ... }
│
└─ NO → Do you need type flexibility in ONE method only?
   │
   ├─ YES → Use generic method: <T> T method(T param)
   │
   └─ NO → Use concrete type: String method(String param)
```

| Scenario | Use | Example |
|----------|-----|---------|
| Container holding values | Generic class | `Box<T>`, `List<T>` |
| Single method with type flexibility | Generic method | `<T> List<T> asList(T a, T b)` |
| Method needs its own type param | Generic method | `<T extends Comparable<T>> T max(T a, T b)` |
| Fixed type, no flexibility needed | Concrete type | `String capitalize(String s)` |

### Decision 2: Raw Types vs Parameterized Types

```
Is this legacy code (pre-Java 5)?
│
├─ YES → Use raw types temporarily, plan migration
│
└─ NO → Use parameterized types: List<String> not List
```

| Situation | Use | Why |
|-----------|-----|-----|
| New code | Parameterized | Type safety |
| Legacy maintenance | Raw type temporarily | Don't break existing code |
| Migration | Parameterized | Gradually add type safety |
| Raw type + @SuppressWarnings | Only if unavoidable | Document why |

### Decision 3: Type Erasure Workarounds

```
Do you need runtime type information?
│
├─ YES → Use Class<T> parameter or TypeReference
│
└─ NO → Erasure is fine, no workarounds needed
```

| Need | Workaround | Example |
|------|------------|---------|
| Create instances of T | Pass `Class<T>` | `T create(Class<T> clazz)` |
| Serialize/deserialize | TypeReference/TypeToken | Jackson's `TypeReference<T>` |
| Check type at runtime | `Class<T>.isInstance()` | `clazz.isInstance(obj)` |
| Get generic type via reflection | `getGenericSuperclass()` | Spring's type resolution |

### Decision 4: When Erasure Causes Problems

```
Does your code need to:
- Use instanceof with generics?
- Create generic arrays?
- Have static type parameters?
- Use type parameters in catch/throw?
│
├─ YES → You need a workaround (see table below)
│
└─ NO → Erasure is transparent
```

| Problem | Workaround |
|---------|------------|
| `instanceof List<String>` | Use `instanceof List<?>` |
| `new T[]` | Use `Array.newInstance()` or `Object[]` |
| `static T value` | Use `Class<T>` parameter |
| `catch (T e)` | Use `catch (Exception e)` and cast |

## Comparison Matrix

| Factor | Generic Class | Generic Method | Raw Type |
|--------|---------------|----------------|----------|
| Type safety | Compile-time | Compile-time | None |
| Scope | All methods | Single method | None |
| Complexity | Medium | Low | Low |
| Use case | Containers, APIs | Utility methods | Legacy code |

## When to Use Each

### Use Generic Class when:
- Building a container (Box, Stack, Queue)
- Creating an API that works with multiple types
- Type parameter used across multiple methods

### Use Generic Method when:
- Single method needs type flexibility
- Method is a utility (like `Collections.sort`)
- Type parameter is method-specific

### Use Raw Type when:
- Maintaining legacy code (temporarily)
- Interfacing with pre-generics APIs
- Never in new code

## Engineering Trade-offs

| Decision | Gain | Loss |
|----------|------|------|
| Generic class | Type safety across methods | More complex class definition |
| Generic method | Type safety for single method | Can't use type in other methods |
| Raw type | Simplicity | Loses type safety |
| TypeReference | Runtime type info | Extra class, more complexity |
| Class<T> parameter | Runtime type creation | Verbose method signature |
