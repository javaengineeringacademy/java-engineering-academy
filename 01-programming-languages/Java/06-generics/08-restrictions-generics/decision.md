# Decision Guide: Restrictions on Generics

## Decision Tree

```
Encountering a generic restriction?
├── Need primitive type as type argument?
│   ├── Yes → Use wrapper class (int → Integer, etc.)
│   └── No → Proceed
├── Need to create instanceof with generic type?
│   ├── Yes → Use raw type or Class<T> token
│   └── No → Proceed
├── Need to create array of generic type?
│   ├── Yes → Use List<T> or Object[] with cast
│   └── No → Proceed
├── Need static field per type parameter?
│   ├── Yes → Use static inner class pattern
│   └── No → Proceed
├── Need to catch/throw parameterized exception?
│   ├── Yes → Use Throwable or raw type
│   └── No → Proceed
└── Need reified type at runtime?
    ├── Yes → Use Class<T> token or TypeReference
    └── No → Rely on compile-time safety
```

## Complete Restrictions Table

| Restriction | Reason | Workaround |
|---|---|---|
| No primitive type arguments | Erasure replaces with Object; primitives aren't Objects | Use wrapper classes |
| No `instanceof` with generics | Type info erased at runtime | Use `instanceof List<?>` or `Class<T>` |
| No generic array creation | Arrays need reified type; generics are erased | Use `List<T>` or `Object[]` with cast |
| No static fields of type parameter | Static context has no instance type parameter | Use static inner class or `Class<T>` |
| No `new T()` | T is erased; constructor unknown | Pass `Class<T>` and use `type.newInstance()` |
| No `throws E` on generic exceptions | Erasure makes E Object/Throwable | Declare specific exception or use `Throwable` |
| No `class A<T> extends T` | Cannot extend a type parameter | Use F-bounded: `<T extends Base<T>>` |
| No `new E[]` | Array component type erased | Use `Array.newInstance()` |

## When Restrictions Impact Design

- API design needing primitive generics → consider libraries like Eclipse Collections
- Framework design needing runtime type info → use TypeToken/ParameterizedTypeReference
- Testing generic code → use concrete types in tests, not raw types
- Serialization frameworks → need special handling for generic types

## Decision Rules

1. **Always use wrapper classes** for generic type arguments
2. **Prefer `List<T>` over `T[]`** in generic contexts
3. **Use `Class<T>` tokens** when runtime type info is needed
4. **Static fields per type** require static inner class pattern
5. **Avoid generic exceptions** — use concrete exception types
6. **Reified generics** are not available in Java — work around with tokens

## Engineering Trade-offs

| Restriction | Impact | Workaround Complexity | Alternative Language |
|---|---|---|---|
| No primitives | Low | Trivial (autoboxing) | C# (has reified generics) |
| No generic arrays | Moderate | Use List<T> | C# (reified) |
| No instanceof | Moderate | Class<T> token | C# (reified) |
| No static type fields | Low | Inner class pattern | Kotlin (companion objects) |
| No reified generics | High | TypeToken patterns | C# (reified), Kotlin (reified inline) |

## Common Code Review Comments

- "Use `Integer` instead of `int` as type argument"
- "Replace generic array with `List<T>`"
- "Use `Class<T>` token instead of `instanceof` with generic"
- "Static fields cannot use type parameters — use inner class"
- "Generic exceptions are erased — use concrete exception types"

## Production Patterns

```java
// Pattern: Wrapper for primitives
List<Integer> numbers = List.of(1, 2, 3);

// Pattern: Type token for runtime info
public static <T> T deserialize(String json, Class<T> type) {
    return objectMapper.readValue(json, type);
}

// Pattern: Static inner class for per-type static state
class Factory<T> {
    private static class Registry<T> {
        static final Map<Class<?>, Factory<?>> REGISTRY = new HashMap<>();
    }
}

// Pattern: Array creation workaround
@SuppressWarnings("unchecked")
T[] array = (T[]) Array.newInstance(clazz, size);
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| `List<int>` | Use `List<Integer>` |
| `instanceof List<String>` | Use `instanceof List<?>` |
| `new T[10]` | Use `List<T>` or `Array.newInstance` |
| `static T field` | Move to static inner class |
| `class Ex<T> extends Exception` | Use concrete exception type |
