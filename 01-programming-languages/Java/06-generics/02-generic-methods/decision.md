# Decision Guide: Generic Methods

## Key Engineering Decisions

### Decision 1: Generic Method vs Non-Generic Method

```
Does the method need to work with different types?
│
├─ YES → Does it need its OWN type parameter?
│  │
│  ├─ YES → Use generic method: <T> T method(T param)
│  │
│  └─ NO → Use generic class type parameter
│
└─ NO → Use concrete type: String method(String param)
```

| Scenario | Use | Example |
|----------|-----|---------|
| Method works with class's type param | Generic class | `Box<T>.get()` |
| Method needs its own type param | Generic method | `<T> List<T> asList(T a)` |
| Method returns different type than input | Generic method | `<T> T identity(T t)` |
| Fixed input/output types | Non-generic | `String join(String a, String b)` |

### Decision 2: Type Parameter Naming

```
What does the type parameter represent?
│
├─ A container or holder → Use T (Type)
├─ A key-value pair → Use K (Key), V (Value)
├─ An element → Use E (Element)
├─ A number → Use N (Number)
└─ Something else → Use meaningful name
```

| Name | Convention | Example |
|------|------------|---------|
| T | Generic type | `Box<T>` |
| E | Element | `List<E>` |
| K | Key | `Map<K, V>` |
| V | Value | `Map<K, V>` |
| N | Number | `<N extends Number>` |
| R | Return type | `<T, R> R map(T input)` |

### Decision 3: Multiple Type Parameters

```
How many unrelated types does the method need?
│
├─ 1 → Use single type parameter: <T>
├─ 2 → Use two: <T, U>
├─ 3+ → Consider refactoring (too many params = bad design)
```

| Count | Use | Example |
|-------|-----|---------|
| 1 | Most methods | `<T> T identity(T t)` |
| 2 | Key-value or input-output | `<T, U> Pair<T, U> pair(T a, U b)` |
| 3+ | Rarely — refactor instead | Consider a builder or config class |

### Decision 4: Bounded vs Unbounded

```
Does the method need to call methods on T?
│
├─ YES → Use bounded: <T extends Comparable<T>>
│
└─ NO → Use unbounded: <T>
```

| Need | Use | Example |
|------|-----|---------|
| Compare values | `<T extends Comparable<T>>` | `T max(T a, T b)` |
| Do arithmetic | `<T extends Number>` | `double sum(List<T> list)` |
| Serialize | `<T extends Serializable>` | `void save(T obj)` |
| No special methods needed | `<T>` | `T identity(T t)` |

## Comparison Matrix

| Factor | Generic Method | Non-Generic Method | Generic Class |
|--------|----------------|-------------------|---------------|
| Type safety | Compile-time | Compile-time | Compile-time |
| Flexibility | Single method | None | All methods |
| Complexity | Low | Lowest | Medium |
| Use case | Utilities | Fixed types | Containers |

## When to Use Each

### Use Generic Method when:
- Single method needs type flexibility
- Method is a utility (like `Collections.sort`)
- Type parameter is method-specific, not class-wide

### Use Non-Generic Method when:
- Type is fixed (always String, always int)
- Method doesn't benefit from generics
- Simplicity is preferred

### Use Generic Class when:
- Multiple methods share the same type parameter
- Building a container or API
- Type is used across the class

## Production Patterns

```java
// Pattern 1: Type-safe factory method
public static <T> T create(Class<T> clazz) throws Exception {
    return clazz.getDeclaredConstructor().newInstance();
}

// Pattern 2: Generic utility method
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

// Pattern 3: Generic conversion method
public static <T, U> U convert(T input, Function<T, U> converter) {
    return converter.apply(input);
}

// Pattern 4: Generic builder method
public static <T> Builder<T> builder(Class<T> clazz) {
    return new Builder<>(clazz);
}
```
