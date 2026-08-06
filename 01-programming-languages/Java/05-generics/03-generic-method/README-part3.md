# 03 - Generic Methods (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

### 3. Use IDE Type Hints

```java
// IntelliJ: View > Tool Windows > Structure
// Shows inferred types for generic methods
```

### 4. Inspect Bytecode

```bash
javap -c -p Utility.class | grep -A 5 "methodName"
# Shows erased method signature
```

---

## Comparison Table

| Feature | Generic Method | Generic Class |
|---------|----------------|---------------|
| Type parameter scope | Method only | Entire class |
| When to use | One method needs generics | Multiple members need generics |
| Type inference | From arguments | From declaration |
| Static usage | Can be static | Cannot use class T in static |
| Complexity | Lower | Higher |

---

## Decision Tree

```
Does only ONE method need to work with different types?
├── Yes → Use generic method
└── No → Do MULTIPLE members need the same type?
    ├── Yes → Use generic class
    └── No → Consider specific types or wildcards
```

---

## Interview Questions

### Q1: What is a generic method?

**A:** A generic method is a method that declares its own type parameters, independent of any class-level type parameters. The type parameters are declared before the return type: `public static <T> T identity(T value)`.

### Q2: How does type inference work in generic methods?

**A:** The compiler infers the type parameter from the method arguments. For `identity("hello")`, the compiler infers `T = String`. Explicit type arguments can be provided but are rarely needed.

### Q3: Can a generic method have multiple type parameters?

**A:** Yes. Example: `public static <K, V> Map<K, V> of(K key, V value)`. Each type parameter is inferred independently from the arguments.

### Q4: What's the difference between `<T extends Number>` and `Number` as a parameter type?

**A:** `<T extends Number>` allows the method to return the specific type `T`, not just `Number`. This preserves type information: `<T extends Number> T first(List<T> list)` returns the actual type, while `Number first(List<Number> list)` always returns `Number`.

### Q5: Can generic methods be static?

**A:** Yes! Generic methods can be static, even in non-generic classes. The type parameters belong to the method, not the class.

---

## Exercises

### Exercise 1: Generic Swap

Write a generic method `swap(T[] array, int i, int j)` that swaps elements at positions i and j.

### Exercise 2: Generic Filter

Write a generic method `filter(List<T> list, Predicate<T> predicate)` that returns a new list containing only elements matching the predicate.

### Exercise 3: Generic Max

Write a generic method `max(T a, T b)` that returns the greater of two `Comparable` values.

---

## Assignments

### Assignment 1: Generic Utility Class

Create a `GenericUtils` class with these static generic methods:

1. `<T> List<T> of(T... elements)` — create list from varargs
2. `<T> Optional<T> findFirst(List<T> list, Predicate<T> predicate)`
3. `<T, R> List<R> map(List<T> list, Function<T, R> mapper)`
4. `<T> T reduce(List<T> list, T identity, BinaryOperator<T> accumulator)`
5. `<T> Map<T, Long> frequency(List<T> list)` — count occurrences

### Assignment 2: Type-Safe Builder

Create a generic builder pattern:

```java
public static <T> Builder<T> builder(Class<T> type) {
    return new Builder<>(type);
}

// Usage
User user = GenericUtils.builder(User.class)
    .set("name", "Alice")
    .set("age", 30)
    .build();
```

---

## Mini Project

### Generic Stream Processing Pipeline

Build a generic stream processing system:

1. `Pipeline<T>` class with chainable operations
2. `filter`, `map`, `flatMap`, `reduce` operations
3. Type-safe builder pattern
4. Support for parallel processing
5. Custom collector support

**Key methods:**
```java
Pipeline<String> pipeline = Pipeline.of("hello", "world", "foo")
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

## Summary

Generic methods provide fine-grained type safety at the method level. They:

1. **Declare their own type parameters** — Independent of class parameters
2. **Infer types from arguments** — No explicit type casting needed
3. **Work in non-generic classes** — You don't need a generic class for generic methods
4. **Support bounded types** — `<T extends Number>` for type constraints
5. **Are erased at compile time** — No runtime overhead

Generic methods are essential for utility classes, factory methods, and type-safe operations that don't require class-level parameterization.

---

## References

- [Oracle - Generic Methods](https://docs.oracle.com/en/java/javase/21/java/generics/methods.html)
- [Java Language Specification §8.4.4 - Generic Methods](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.4.4)
- [Effective Java - Item 33: Use generic types safely](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Baeldung - Java Generics](https://www.baeldung.com/java-generics)
