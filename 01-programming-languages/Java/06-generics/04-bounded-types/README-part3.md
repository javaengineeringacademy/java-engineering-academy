# 04 - Bounded Type Parameters (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


**A:** Yes, using `&`: `<T extends Number & Comparable<T> & Serializable>`. The class (if any) must come first, followed by interfaces.

### Q4: What is a recursive type bound?

**A:** A recursive bound is when a type parameter bounds itself: `<T extends Comparable<T>>`. This means T must be comparable to instances of its own type.

### Q5: How do bounds affect type erasure?

**A:** After erasure, the type parameter is replaced by its first bound. `<T extends Number & Comparable<T>>` erases to `Number`. Subsequent bounds are erased completely.

---

## Exercises

### Exercise 1: Bounded Sum

Write a generic method `sum(List<T> list)` that returns the sum of all elements. The type parameter should be bounded to `Number`.

### Exercise 2: Comparable Search

Write a generic method `binarySearch(T[] array, T target)` that performs binary search using `Comparable`.

### Exercise 3: Multiple Bounds

Write a generic method that finds the maximum of two values, where the type must be both `Number` and `Comparable`.

---

## Assignments

### Assignment 1: Type-Safe Validator

Create a `Validator<T>` class that:
- Accepts rules that implement `Predicate<T>`
- Validates values against all rules
- Provides meaningful error messages
- Works with any type that implements `Comparable`

### Assignment 2: Generic Range

Create a `Range<T extends Comparable<T>>` class that:
- Represents a range from min to max
- Supports `contains(T value)`
- Supports `overlaps(Range<T> other)`
- Supports `intersection(Range<T> other)`
- Implements `Comparable<Range<T>>`

---

## Mini Project

### Type-Safe Math Library

Build a generic math library that works with different numeric types:

1. `MathUtils<T extends Number & Comparable<T>>` class
2. Methods: `max`, `min`, `sum`, `average`, `clamp`
3. Support for `Integer`, `Long`, `Double`, `BigDecimal`
4. Type-safe conversion methods
5. Range operations

---

## Summary

Bounded type parameters are essential for writing generic code that requires specific capabilities:

1. **`extends` keyword** — Specifies upper bound
2. **Multiple bounds** — Use `&` for multiple constraints
3. **Recursive bounds** — `<T extends Comparable<T>>` for self-referencing types
4. **Compile-time safety** — Bounds are checked at compile time
5. **No runtime overhead** — Bounds are erased at compile time

Bounded types enable type-safe operations while maintaining generic flexibility.

---

## References

- [Oracle - Bounded Type Parameters](https://docs.oracle.com/en/java/javase/21/java/generics/bounded.html)
- [Java Language Specification §4.5 - Type Parameters](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5)
- [Effective Java - Item 30: Use bounded wildcards to increase API flexibility](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/)
