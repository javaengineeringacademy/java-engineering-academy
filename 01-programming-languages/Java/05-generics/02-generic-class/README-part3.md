# 02 - Generic Classes (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

}
```

---

## Performance

### Generic Class Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Minimal increase |
| Runtime | Zero overhead (type erasure) |
| Memory | Identical to raw types |
| Bytecode size | Slightly larger (bridge methods) |

### Bridge Methods

```java
// Source
public class StringComparator implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
}

// Compiler generates bridge method
public int compare(Object a, Object b) {
    return compare((String) a, (String) b);  // Bridge
}
```

---

## Best Practices

1. **Use meaningful type parameter names** — `T` for simple, `KeyType` for clarity
2. **Document type constraints** — `@param T the type of elements`
3. **Prefer bounded types** — `<T extends Comparable<T>>` over `<T>`
4. **Use `@SuppressWarnings` sparingly** — Only when you've verified type safety
5. **Implement `equals()` and `hashCode()`** — Generic classes often need them
6. **Consider immutable designs** — Final fields with constructors

---

## Common Mistakes

### 1. Confusing Type Parameters with Types

```java
// WRONG
Box<String> box = new Box<Integer>();  // Cannot change type parameter

// RIGHT
Box<String> box = new Box<>();
```

### 2. Mixing Type Parameters

```java
// WRONG
public class Pair<T, T> { }  // Duplicate type parameter

// RIGHT
public class Pair<T, U> { }  // Different type parameters
```

### 3. Using `new T()`

```java
// WRONG - won't compile
public class Box<T> {
    private T createDefault() {
        return new T();  // Compile error!
    }
}

// RIGHT - pass class token
public class Box<T> {
    private final Class<T> type;
    
    public Box(Class<T> type) {
        this.type = type;
    }
    
    private T createDefault() throws InstantiationException {
        return type.getDeclaredConstructor().newInstance();
    }
}
```

---

## Pitfalls

### 1. Generic Arrays

```java
// ILLEGAL
// Box<String>[] boxes = new Box<String>[10];

// Why? Arrays are reified (carry type info at runtime)
// But generics use type erasure — they're incompatible

// WORKAROUND
Box<String>[] boxes = (Box<String>[]) new Box[10];
```

### 2. Static Members

```java
// ILLEGAL
public class Box<T> {
    private static T value;  // Compile error!
    // T is per-instance, but static is per-class
}

// RIGHT
public class Box<T> {
    private static int count;  // This is fine
    private T instanceValue;   // This is fine
}
```

### 3. Type Parameter as Superclass

```java
// ILLEGAL
public class Box<T> extends T { }  // Compile error!

// RIGHT
public class Box<T> extends ComparableBox<T> { }  // Extend a generic class
```

---

## Debugging Tips

### 1. Check Type Parameters in Stack Traces

```java
// ClassCastException shows erased type
// java.lang.ClassCastException: String cannot be cast to Integer
// At Box.set(Box.java:10)  ← Check the line
```

### 2. Use IDE Type Hints

```java
Box<> box = new Box<>();  // IDE shows: Box<String> (inferred)
// IntelliJ: View > Tool Windows > Structure
// Eclipse: Open Declaration
```

### 3. Inspect Bytecode

```bash
javap -v out/Box.class | grep -A 2 "Signature"
# Shows generic type info in bytecode
```

---

## Comparison Table

| Aspect | Generic Class | Non-Generic Class |
|--------|---------------|-------------------|
| Type safety | Compile time | Runtime only |
| Code reuse | High | Low |
| Casting | Automatic | Manual |
| Readability | High | Low |
| Refactoring | Easy | Difficult |
| IDE support | Full | Limited |
| Collections usage | Standard | Exception |

---

## Decision Tree

```
Do you need a class that works with multiple types?
├── No → Use specific type
└── Yes → Do all instances work with the SAME type?
    ├── Yes → Generic class with type parameter
    └── No → Do you need multiple different types?
        ├── Yes → Multiple type parameters
        └── No → Consider wildcards or bounded types
```

---

## Interview Questions

### Q1: What is a generic class?

**A:** A generic class is a class that takes type parameters as arguments, allowing it to work with different data types while maintaining compile-time type safety. Example: `class Box<T> { private T value; }`.

### Q2: Can a generic class have multiple type parameters?

**A:** Yes. Example: `class Pair<K, V> { K key; V value; }`. Each type parameter is independent and can be used throughout the class.

### Q3: What is the diamond operator?

**A:** The diamond operator `<>` (Java 7+) allows the compiler to infer type parameters from the left-hand side. Instead of `Box<String> box = new Box<String>()`, write `Box<String> box = new Box<>()`.

### Q4: Can a generic class extend another generic class?

**A:** Yes. `class StringList extends ArrayList<String>` is valid. Or `class GeneriList<T> extends ArrayList<T>` to preserve the type parameter.

### Q5: Can you have static members in a generic class?

**A:** Static fields cannot use the class's type parameters (they're per-class, not per-instance). Static methods can declare their own type parameters: `static <T> T method(T param)`.

---

## Exercises

### Exercise 1: Generic Stack

Implement a `Stack<T>` class with:
- `push(T item)`
- `T pop()`
- `T peek()`
- `boolean isEmpty()`
- `int size()`
- `void clear()`

### Exercise 2: Generic Cache

Implement a `Cache<K, V>` class with:
- `void put(K key, V value)`
- `V get(K key)`
- `V getOrDefault(K key, V defaultValue)`
- `boolean containsKey(K key)`
- `void remove(K key)`
- `int size()`

### Exercise 3: Generic Pair

Implement a `Pair<A, B>` class with:
- `A getFirst()`
- `B getSecond()`
- `Pair<B, A> swap()` — returns new pair with swapped values
- `boolean equals(Object obj)`
- `int hashCode()`
- `String toString()`

---

## Assignments

### Assignment 1: Generic Repository Pattern

Create a generic `Repository<T, ID>` interface and `InMemoryRepository<T, ID>` implementation.

**Interface methods:**
- `T findById(ID id)`
- `Optional<T> findByIdOptional(ID id)`
- `List<T> findAll()`
- `void save(T entity)`
- `void update(T entity)`
- `void delete(ID id)`
- `long count()`

### Assignment 2: Generic Builder

Create a generic `Builder<T>` pattern for constructing objects.

**Requirements:**
- Generic builder that works with any class
- Type-safe method chaining
- Validation support
- `build()` method that returns the constructed object

---

## Mini Project

### Generic Collection Framework

Implement a simplified version of Java's collection framework:

1. `SimpleList<T>` interface with `add`, `get`, `size`, `remove`
2. `ArrayList<T>` implementation using arrays
3. `LinkedList<T>` implementation with nodes
4. `SimpleIterator<T>` interface for iteration
5. `Collections` utility class with `sort`, `reverse`, `shuffle`

---

## Summary

Generic classes are the cornerstone of type-safe, reusable Java code. They enable you to:

1. **Write once, use everywhere** — One class for all types
2. **Catch errors at compile time** — No runtime ClassCastException
3. **Eliminate explicit casts** — Cleaner, safer code
4. **Build type-safe data structures** — Collections, caches, repositories
5. **Express design intent** — `Box<String>` clearly communicates purpose

Understanding generic classes is essential for working with Java collections, frameworks, and writing production-quality code.

---

## References

- [Oracle - Generic Classes and Type Parameters](https://docs.oracle.com/en/java/javase/21/java/generics/types.html)
- [Java Language Specification §8.1.2 - Generic Class Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.2)
- [Effective Java - Item 29: Use wildcards to increase API flexibility](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Generics Tutorial](https://www.baeldung.com/java-generics)
