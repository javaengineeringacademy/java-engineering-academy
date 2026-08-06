# 01 - Introduction to Generics (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


Create a `Pair<A, B>` class that:
- Stores two values of different types
- Has `getFirst()` and `getSecond()` methods
- Implements `equals()` and `hashCode()`
- Has a `swap()` method returning a new Pair with reversed values

### Exercise 3: Type-Safe Cache

Create a `TypeSafeCache` class that:
- Stores values with string keys
- Uses `Class<T>` tokens for type-safe retrieval
- Throws `ClassCastException` on type mismatch
- Supports `getOrDefault()` with type safety

---

## Assignments

### Assignment 1: Generic Repository

Create a generic `Repository<T, ID>` interface with:
- `T findById(ID id)`
- `List<T> findAll()`
- `void save(T entity)`
- `void update(T entity)`
- `void delete(ID id)`

Implement `InMemoryRepository<T, ID>` that stores entities in a `Map<ID, T>`.

### Assignment 2: Generic Result Type

Create a `Result<T>` class representing success or failure:
- `static <T> Result<T> success(T value)`
- `static <T> Result<T> failure(String error)`
- `boolean isSuccess()`
- `T getValue()` (throws if failure)
- `String getError()` (throws if success)
- `T orElse(T defaultValue)`
- `<U> Result<U> map(Function<T, U> mapper)`

---

## Mini Project

### Type-Safe Event System

Build an event system that:
1. Uses generics to type events and handlers
2. Provides compile-time type safety for event registration
3. Ensures handlers receive correctly typed events
4. Supports event priority and filtering

**Key classes:**
- `Event<T>` — base event class
- `EventHandler<T>` — functional interface for handling events
- `EventBus` — central dispatcher
- `TypedEvent<T>` — specific event implementation

---

## Summary

Generics are a fundamental Java feature that:

1. **Provides compile-time type safety** — catches errors before runtime
2. **Eliminates explicit casts** — cleaner, safer code
3. **Enables code reusability** — one implementation for all types
4. **Uses type erasure** — no runtime overhead, but limits reflection
5. **Is essential for collections** — `List<E>`, `Map<K,V>`, etc.

Understanding generics is crucial for writing robust, maintainable Java code. While type erasure introduces some limitations (no generic arrays, no `instanceof` with type parameters), the benefits far outweigh the costs.

---

## References

- [Oracle Generics Tutorial](https://docs.oracle.com/en/java/javase/21/java/generics/)
- [Java Language Specification §4.5 - Type Parameters](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5)
- [Effective Java, 3rd Edition - Chapter 26: Generic Types](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Generics and Collections - Maurice Naftalin](https://www.oreilly.com/library/view/java-generics-and/9780596527754/)
- [Angelika Langer - Java Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/TypeParameters.html)
