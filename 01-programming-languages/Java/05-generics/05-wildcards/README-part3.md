# 05 - Wildcards (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

    for (List<? extends T> list : lists) {
        result.addAll(list);
    }
    return result;
}
```

### 3. Wildcard with Multiple Bounds

```java
// Complex wildcard bounds can be hard to read
public static <T extends Number & Comparable<T>> T max(List<? extends T> list) {
    // T is bounded, but list elements are ? extends T
    // This works but is complex
}
```

---

## Debugging Tips

### 1. Check Wildcard Type

```java
// Use IDE to see inferred wildcard type
List<?> list = List.of(1, 2, 3);
// IntelliJ: hover to see type
```

### 2. Read Error Messages

```
Error: incompatible types: Object cannot be converted to String
// This means you're trying to read from a ? wildcard
// Use ? extends T to get a more specific type
```

### 3. Use Explicit Type Arguments

```java
// If wildcard inference fails, provide explicit types
List<Number> result = CollectionUtils.<Number>filter(numbers, n -> n.intValue() > 5);
```

### 4. Inspect Bytecode

```bash
javap -c -p MyClass.class | grep "Object\|Number"
# Shows erased types in bytecode
```

---

## Comparison Table

| Feature | `<?>` | `<? extends T>` | `<? super T>` |
|---------|-------|------------------|---------------|
| Read as | Object | T | Object |
| Write | Nothing | Nothing | T |
| Use case | Generic processing | Producer | Consumer |
| PECS | Neither | Producer | Consumer |
| Flexibility | Maximum | Read flexibility | Write flexibility |

---

## Decision Tree

```
Do you need to READ from the collection?
├── No → Do you need to WRITE to it?
│   ├── Yes → Use ? super T
│   └── No → Use ? (unbounded)
└── Yes → Do you need to WRITE to it?
    ├── Yes → Use a type parameter <T>
    └── No → Use ? extends T
```

---

## Interview Questions

### Q1: What is a wildcard in Java generics?

**A:** A wildcard (`?`) represents an unknown type in generic code. It provides flexibility when you don't need to name the type, such as when reading from or writing to a collection of unknown element type.

### Q2: What is the PECS principle?

**A:** PECS stands for "Producer Extends, Consumer Super." When a collection produces (provides) values, use `<? extends T>`. When it consumes (accepts) values, use `<? super T>`.

### Q3: Why can't you add elements to a `List<? extends T>`?

**A:** Because the actual type could be any subclass of T, and you don't know which one. Adding a T might violate type safety if the actual type is a different subclass. The compiler prevents this.

### Q4: What's the difference between `<?>` and `<Object>`?

**A:** `<?>` is a wildcard that accepts any type. `<Object>` is a specific type parameter. `List<?>` can accept `List<String>`, but `List<Object>` cannot (due to invariance).

### Q5: How does wildcard capture work?

**A:** Wildcard capture allows using a wildcard by assigning it to a type parameter in a helper method. The type parameter captures the unknown type, allowing type-safe operations.

---

## Exercises

### Exercise 1: Wildcard Methods

Write methods that demonstrate all three wildcard types:
1. `printAll(List<?> list)` — print all elements
2. `sum(List<? extends Number> list)` — sum numeric values
3. `addAll(List<? super Integer> dest, List<Integer> src)` — add integers

### Exercise 2: PECS Application

Implement `copy(List<? super T> dest, List<? extends T> src)` using the PECS principle.

### Exercise 3: Wildcard Capture

Implement `swap(List<?> list, int i, int j)` using wildcard capture.

---

## Assignments

### Assignment 1: Type-Safe Collection Utils

Create a `CollectionUtils` class with wildcard-based methods:
1. `<T> T max(List<? extends T> list)` — find maximum
2. `<T> void copy(List<? super T> dest, List<? extends T> src)` — copy elements
3. `<T> List<T> filter(List<? extends T> list, Predicate<? super T> predicate)` — filter elements

### Assignment 2: Generic Stack with Wildcards

Enhance a `Stack<T>` class with wildcard-based methods:
1. `void pushAll(Collection<? extends T> src)` — push multiple elements
2. `void popAll(Collection<? super T> dest)` — pop to collection

---

## Mini Project

### Type-Safe Event System

Build an event system using wildcards:

1. `Event<T>` class representing an event
2. `EventHandler<T>` interface for handling events
3. `EventBus` with wildcard-based registration and dispatch
4. Type-safe event filtering and transformation

**Key methods:**
```java
eventBus.register(EventHandler<? super T> handler);
eventBus.post(Event<? extends T> event);
```

---

## Summary

Wildcards are essential for flexible, type-safe generic code:

1. **`<?>`** — Unknown type, read as Object
2. **`<? extends T>`** — Upper bounded, read as T
3. **`<? super T>`** — Lower bounded, write T
4. **PECS principle** — Producer Extends, Consumer Super
5. **Wildcard capture** — Enable type-safe operations

Wildcards enable APIs that work with different generic types while maintaining compile-time safety.

---

## References

- [Oracle - Wildcards](https://docs.oracle.com/en/java/javase/21/java/generics/wildcards.html)
- [Java Language Specification §4.5.1 - Type Arguments of Parameterized Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5.1)
- [Effective Java - Item 31: Use bounded wildcards to increase API flexibility](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Wildcard FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/Wildcards.html)
