# Decision Guide: Erasure of Generic Types

## Decision Tree

```
Do you need to understand how a generic type is erased?
├── Is it a class/interface with type parameters?
│   ├── Yes → Type parameters replaced with bounds (or Object)
│   └── No → Standard erasure rules apply
├── Do you need runtime type info?
│   ├── Yes → Use Class<T> token, TypeReference, or reflection
│   └── No → Rely on compile-time safety
└── Is bytecode transformation relevant?
    ├── Yes → See transformation rules below
    └── No → Normal development
```

## Erasure Transformation Rules

| Generic Form | Erased Form |
|---|---|
| `<T>` (unbounded) | `Object` |
| `<T extends Number>` | `Number` |
| `<T extends Comparable & Serializable>` | `Comparable` (first bound) |
| `List<String>` | `List` |
| `Map<String, Integer>` | `Map` |
| `T method()` | `Object method()` |
| `T method(T arg)` | `Object method(Object arg)` |

## When Type Erasure Impacts Generic Types

- Understanding bytecode of generic classes
- Serialization/deserialization of generic objects
- Reflection on generic types
- Performance considerations (no boxing/unboxing overhead)
- Debugging type-related runtime errors

## Decision Rules

1. **Generic types erase to their first bound** or `Object` if unbounded
2. **Type arguments are completely removed** — `List<String>` and `List<Integer>` are both `List`
3. **Compiler inserts casts** where type info is needed
4. **Signature pollution** — bridge methods and synthetic methods are added
5. **No runtime type checking** — `instanceof List<String>` is illegal

## Engineering Trade-offs

| Aspect | Erasure (Java) | Reification (C#) |
|---|---|---|
| Performance | No overhead | Potential boxing |
| Runtime type info | Not available | Available |
| Binary compatibility | Excellent | Complex |
| Array creation | Not possible with generics | Possible |
| `instanceof` support | No | Yes |

## Common Code Review Comments

- "Type info is erased at runtime — use a type token"
- "This generic type becomes raw at runtime — expect casts"
- "Bridge methods are generated here — don't be surprised in stack traces"
- "Generic array creation not possible — use List<T>"

## Production Patterns

```java
// Pattern: Type token for runtime generic info
public class GenericService<T> {
    private final Class<T> type;
    public GenericService(Class<T> type) { this.type = type; }
}

// Pattern: Accessing generic info via reflection
Field field = MyClass.class.getDeclaredField("list");
Type genericType = field.getGenericType();
if (genericType instanceof ParameterizedType pt) {
    Type[] args = pt.getActualTypeArguments();
}

// Pattern: Safe generic array creation
@SuppressWarnings("unchecked")
T[] array = (T[]) Array.newInstance(type, size);
```

## Common Mistakes

| Mistake | Fix |
|---|---|
| Assuming `List<String>` exists at runtime | It's just `List` |
| Trying `instanceof List<String>` | Use `List.class` or `List<?>` |
| `new T[10]` | Use `Array.newInstance` or `List<T>` |
| Ignoring bridge methods in debugging | Check for synthetic methods in stack traces |
