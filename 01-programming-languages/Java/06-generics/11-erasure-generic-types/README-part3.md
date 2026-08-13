# Erasure of Generic Types - Part 3: Advanced Topics

## 1. Type Erasure Rules Summary

| Generic Type | Erased To |
|--------------|-----------|
| `T` | `Object` |
| `T extends Number` | `Number` |
| `T extends Comparable` | `Comparable` |
| `?` | `Object` |
| `? extends Number` | `Number` |
| `? super Integer` | `Integer` (for return), `Object` (for params) |

## 2. ClassCastException Scenarios

### Unchecked Cast
```java
@SuppressWarnings("unchecked")
List<String> list = (List<String>) rawList;
```

### Array Store
```java
Object[] arr = new String[10];
arr[0] = 42; // ArrayStoreException at runtime
```

### Bridge Method Mismatch
```java
// Can cause unexpected CCE
```

## 3. Reflection with Erased Types

```java
Field f = Container.class.getDeclaredField("value");
Type genericType = f.getGenericType(); // Object
Class<?> rawType = f.getType(); // Object
```

## 4. Avoiding Erasure Issues

- Use bounded wildcards for flexibility
- Prefer composition over inheritance
- Use TypeToken for runtime type info
- Consider serialization frameworks that preserve types
