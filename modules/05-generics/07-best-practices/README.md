# 07 - Best Practices

## Table of Contents

1. [Introduction](#introduction)
2. [Learning Objectives](#learning-objectives)
3. [Prerequisites](#prerequisites)
4. [Why This Concept Exists](#why-this-concept-exists)
5. [Problem Statement](#problem-statement)
6. [Theory](#theory)
7. [Internal Working](#internal-working)
8. [JVM Perspective](#jvm-perspective)
9. [Memory Representation](#memory-representation)
10. [Syntax](#syntax)
11. [Easy Example](#easy-example)
12. [Medium Example](#medium-example)
13. [Hard Example](#hard-example)
14. [Enterprise Example](#enterprise-example)
15. [Performance](#performance)
16. [Best Practices](#best-practices-section)
17. [Common Mistakes](#common-mistakes)
18. [Pitfalls](#pitfalls)
19. [Debugging Tips](#debugging-tips)
20. [Comparison Table](#comparison-table)
21. [Decision Tree](#decision-tree)
22. [Interview Questions](#interview-questions)
23. [Exercises](#exercises)
24. [Assignments](#assignments)
25. [Mini Project](#mini-project)
26. [Summary](#summary)
27. [References](#references)

---

## Introduction

Best practices for Java generics ensure type safety, code clarity, and maintainability. This topic consolidates guidelines from Effective Java, the Java API designers, and industry standards for writing production-quality generic code.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Apply established best practices for generic code
- Avoid common anti-patterns and pitfalls
- Design type-safe, flexible generic APIs
- Write clear, maintainable generic code
- Apply the PECS principle correctly
- Document generic types and constraints

---

## Prerequisites

- All previous topics (01-06)
- Understanding of Java coding conventions
- Experience with Java Collections Framework
- Basic understanding of API design principles

---

## Why This Concept Exists

### Without Best Practices

```java
// Bad: Raw types
public void process(List list) {
    for (Object item : list) {
        String s = (String) item;  // Unsafe cast
        System.out.println(s);
    }
}

// Bad: Overly complex bounds
public static <T extends Comparable<T> & Serializable & Cloneable> T bad(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Bad: Unclear naming
public class A<T, U, V> {
    private T a;
    private U b;
    private V c;
}
```

### With Best Practices

```java
// Good: Parameterized types
public void process(List<String> items) {
    for (String item : items) {
        System.out.println(item);
    }
}

// Good: Minimal bounds
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Good: Clear naming
public class Pair<Key, Value> {
    private Key key;
    private Value value;
}
```

---

## Problem Statement

Establish guidelines for:
1. Choosing between raw types, wildcards, and type parameters
2. Naming type parameters clearly
3. Documenting generic constraints
4. Avoiding common anti-patterns
5. Writing maintainable generic APIs

---

## Theory

### Naming Conventions

| Letter | Meaning | Example | When to Use |
|--------|---------|---------|-------------|
| `T` | Type | `Box<T>` | Simple generic classes |
| `E` | Element | `List<E>` | Collections |
| `K` | Key | `Map<K,V>` | Maps |
| `V` | Value | `Map<K,V>` | Maps |
| `N` | Number | `Math<N>` | Numeric generics |
| `R` | Return | `Function<T,R>` | Functional interfaces |
| `S`, `U`, `V` | Second, third, fourth types | `Triple<S,T,U>` | Multiple parameters |

### When to Use Wildcards vs Type Parameters

```java
// Use wildcards when you only READ from a collection
public static <T> T getFirst(List<? extends T> list) {
    return list.get(0);
}

// Use type parameters when you need to CREATE or RETURN values
public static <T> List<T> asList(T a, T b) {
    return List.of(a, b);
}

// Use wildcards when you only WRITE to a collection
public static <T> void addAll(List<? super T> dest, List<T> src) {
    dest.addAll(src);
}
```

### PECS Principle

**Producer Extends, Consumer Super:**

```java
// Producer (provides elements): use extends
public static <T> T max(List<? extends T> list) { ... }

// Consumer (accepts elements): use super
public static <T> void copy(List<? super T> dest, List<? extends T> src) { ... }

// Both: use type parameters
public static <T> void swap(List<T> list, int i, int j) { ... }
```

### Type Safety Guidelines

```java
// Always use parameterized types
List<String> list = new ArrayList<>();  // Good
List list = new ArrayList();            // Bad

// Use @SuppressWarnings sparingly
@SuppressWarnings("unchecked")  // Document why
List<String> raw = (List<String>) legacyList;

// Use @SafeVarargs for generic varargs
@SafeVarargs
public static <T> List<T> asList(T... elements) {
    return Arrays.asList(elements);
}
```

---

## Internal Working

### Compiler Enforcement

The compiler enforces best practices through:

1. **Type checking** — Verifies type parameter usage
2. **Raw type warnings** — Warns when raw types are used
3. **Unchecked cast warnings** — Warns about unsafe casts
4. **Bridge method generation** — Ensures polymorphism works

```java
// Compiler warnings for bad practices
List raw = new ArrayList();
raw.add("hello");
String s = (String) raw.get(0);  // Warning: unchecked cast

// No warnings for good practices
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);  // No warnings
```

---

## JVM Perspective

### Runtime Behavior

Best practices do not affect runtime behavior:

```java
// Both produce identical bytecode
List<String> good = new ArrayList<>();
List raw = new ArrayList<>();

// The difference is compile-time safety only
```

### Performance Impact

Best practices have zero runtime overhead:

```java
// Bounded types: no runtime cost
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Wildcards: no runtime cost
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}
```

---

## Memory Representation

### No Impact from Best Practices

Best practices are compile-time guidelines only:

```java
// These have identical memory layout
Box<String> good = new Box<>();
Box raw = new Box<>();

// Type safety is enforced at compile time, not runtime
```

---

## Syntax

### Naming Conventions

```java
// Single type parameter
public class Box<T> {
    private T value;
}

// Multiple type parameters
public class Pair<K, V> {
    private K key;
    private V value;
}

// Bounded type parameters
public class SortedList<T extends Comparable<T>> {
    private List<T> elements;
}
```

### Wildcard Usage

```java
// Producer: extends
public static <T> T getFirst(List<? extends T> list) {
    return list.get(0);
}

// Consumer: super
public static <T> void addAll(List<? super T> dest, List<? extends T> src) {
    dest.addAll(src);
}

// Unbounded
public static void printAll(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}
```

### Documentation

```java
/**
 * A generic container that holds a single value.
 *
 * @param <T> the type of the contained value
 */
public class Container<T> {
    private T value;

    /**
     * Creates a new container with the specified value.
     *
     * @param value the value to store
     */
    public Container(T value) {
        this.value = value;
    }

    /**
     * Gets the contained value.
     *
     * @return the contained value
     */
    public T getValue() {
        return value;
    }
}
```

---

## Easy Example

### Basic Best Practices

```java
import java.util.ArrayList;
import java.util.List;

public class BestPracticesBasics {

    // Good: Clear naming
    public static <T> List<T> asList(T a, T b) {
        List<T> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        return list;
    }

    // Good: Bounded type
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    // Good: Wildcard for read-only
    public static void printAll(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        List<String> names = asList("Alice", "Bob");
        printAll(names);

        System.out.println(max(10, 20));        // 20
        System.out.println(max("hello", "world")); // world
    }
}
```

---

## Medium Example

### PECS in Practice

```java
import java.util.ArrayList;
import java.util.List;

public class PecsExample {

    // Producer Extends: read from source
    public static <T> T getFirst(List<? extends T> source) {
        return source.get(0);
    }

    // Consumer Super: write to destination
    public static <T> void addAll(List<? super T> dest, List<T> src) {
        dest.addAll(src);
    }

    // Both: copy from source to destination
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    public static void main(String[] args) {
        // Producer
        List<Integer> ints = List.of(1, 2, 3);
        int first = getFirst(ints);
        System.out.println(first);  // 1

        // Consumer
        List<Number> numbers = new ArrayList<>();
        addAll(numbers, List.of(1, 2, 3));
        System.out.println(numbers);  // [1, 2, 3]

        // Both
        List<Object> objects = new ArrayList<>();
        copy(objects, List.of("a", "b", "c"));
        System.out.println(objects);  // [a, b, c]
    }
}
```

---

## Hard Example

### Comprehensive Generic API

```java
import java.util.*;
import java.util.function.*;

public final class GenericUtils {

    private GenericUtils() {}

    // Bounded type with multiple constraints
    public static <T extends Number & Comparable<T>> T max(List<? extends T> list) {
        return list.stream().max(Comparable::compareTo).orElseThrow();
    }

    // Recursive bound
    public static <T extends Comparable<T>> List<T> sorted(List<T> list) {
        return list.stream().sorted().toList();
    }

    // Wildcard with complex bounds
    public static <T, R> List<R> map(
            List<? extends T> source,
            Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : source) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    // Type-safe heterogeneous container
    public static <T> T safeGet(
            Map<String, Object> map, String key, Class<T> type) {
        Object value = map.get(key);
        if (value == null) return null;
        return type.cast(value);
    }

    public static void main(String[] args) {
        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9);
        System.out.println(max(nums));  // 9

        List<String> sorted = sorted(List.of("Charlie", "Alice", "Bob"));
        System.out.println(sorted);  // [Alice, Bob, Charlie]

        List<String> lengths = map(List.of("hello", "world"), String::length);
        System.out.println(lengths);  // [5, 5]
    }
}
```

---

## Enterprise Example

### Production-Quality Generic Library

```java
import java.util.*;
import java.util.function.*;

public final class ProductionUtils {

    private ProductionUtils() {}

    // Documented generic method
    /**
     * Filters elements matching the predicate.
     *
     * @param <T>       the element type
     * @param source    the source list
     * @param predicate the filter predicate
     * @return a new list containing matching elements
     */
    public static <T> List<T> filter(
            List<? extends T> source,
            Predicate<? super T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : source) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }

    // Type-safe grouping
    public static <T, K> Map<K, List<T>> groupBy(
            List<? extends T> source,
            Function<? super T, ? extends K> keyExtractor) {
        Map<K, List<T>> result = new HashMap<>();
        for (T item : source) {
            K key = keyExtractor.apply(item);
            result.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }
        return result;
    }

    // Safe accumulation
    public static <T, R> R reduce(
            List<? extends T> source,
            R identity,
            BiFunction<R, ? super T, R> accumulator) {
        R result = identity;
        for (T item : source) {
            result = accumulator.apply(result, item);
        }
        return result;
    }

    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie", "David");

        List<String> longNames = filter(names, n -> n.length() > 3);
        System.out.println(longNames);  // [Alice, Charlie, David]

        Map<Integer, List<String>> byLength = groupBy(names, String::length);
        System.out.println(byLength);  // {3=[Bob], 5=[Alice], 7=[Charlie, David]}

        int totalLength = reduce(names, 0, (sum, name) -> sum + name.length());
        System.out.println(totalLength);  // 23
    }
}
```

---

## Performance

### Zero Runtime Overhead

Best practices have no performance impact:

| Practice | Compile Time | Runtime | Memory |
|----------|-------------|---------|--------|
| Using parameterized types | +minor | Same | Same |
| Bounded types | +minor | Same | Same |
| Wildcards | +minor | Same | Same |
| Type parameters | +minor | Same | Same |

---

## Best Practices Section

### The Essential Guidelines

1. **Always use parameterized types** — Never use raw types in new code
2. **Apply PECS consistently** — Producer Extends, Consumer Super
3. **Name type parameters clearly** — T, E, K, V for standard cases
4. **Document type constraints** — Javadoc @param tags
5. **Use minimal bounds** — Only require what you actually use
6. **Prefer wildcards over type parameters** — When you don't need to name the type
7. **Use diamond operator** — `new Box<>()` not `new Box<String>()`
8. **Suppress warnings properly** — Document why with @SuppressWarnings
9. **Use @SafeVarargs** — For generic varargs methods
10. **Design for flexibility** — Use wildcards in public APIs

---

## Common Mistakes

### 1. Using Raw Types

```java
// BAD
List list = new ArrayList();
Map map = new HashMap();

// GOOD
List<String> list = new ArrayList<>();
Map<String, Integer> map = new HashMap<>();
```

### 2. Over-Bounding

```java
// BAD - too restrictive
public static <T extends Number & Comparable<T> & Serializable & Cloneable> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// GOOD - minimal bounds
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### 3. Ignoring PECS

```java
// BAD - not flexible
public static double sum(List<Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

// GOOD - PECS
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}
```

---

## Pitfalls

### 1. Type Erasure Surprise

```java
// These are the same class at runtime
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
System.out.println(strings.getClass() == integers.getClass()); // true
```

### 2. Generic Array Creation

```java
// ILLEGAL
// Box<String>[] boxes = new Box<String>[10];

// WORKAROUND
@SuppressWarnings("unchecked")
Box<String>[] boxes = (Box<String>[]) new Box[10];
```

### 3. Static Members

```java
// ILLEGAL
public class Box<T> {
    private static T value;  // Compile error!
}
```

---

## Debugging Tips

### 1. Read Compiler Errors

```
Error: incompatible types: String cannot be converted to Integer
// Check type parameter usage
```

### 2. Use IDE Type Hints

```java
Box<> box = new Box<>();  // IDE shows inferred type
```

### 3. Inspect Bytecode

```bash
javap -v Box.class | grep "Signature"
```

---

## Comparison Table

| Practice | Type Safety | Flexibility | Readability | Maintenance |
|----------|------------|-------------|-------------|-------------|
| Raw types | Low | Low | Low | Low |
| Parameterized types | High | Medium | High | High |
| Wildcards | High | High | Medium | High |
| Bounded types | Very High | Medium | High | Very High |

---

## Decision Tree

```
Do you need to store/access multiple types?
├── No → Use specific type
└── Yes → Do you need to READ from the collection?
    ├── Yes → Use ? extends T
    └── No → Do you need to WRITE to it?
        ├── Yes → Use ? super T
        └── No → Use ? (unbounded)
```

---

## Interview Questions

### Q1: What is the PECS principle?

**A:** PECS stands for Producer Extends, Consumer Super. When a collection provides (produces) values, use `<? extends T>`. When it accepts (consumes) values, use `<? super T>`.

### Q2: When should you use a type parameter vs a wildcard?

**A:** Use a type parameter when you need to create or return values of that type. Use a wildcard when you only read from or write to a collection.

### Q3: Why should you avoid raw types?

**A:** Raw types bypass compile-time type checking, leading to potential ClassCastException at runtime. Always use parameterized types.

### Q4: What is the diamond operator?

**A:** The diamond operator `<>` (Java 7+) allows the compiler to infer type parameters from the left-hand side. Example: `Box<String> box = new Box<>()`.

### Q5: How do you document generic types?

**A:** Use Javadoc `@param <T>` tags to document type parameters. Explain what the type represents and any constraints.

---

## Exercises

### Exercise 1: PECS Application

Refactor a method that takes `List<Number>` to use wildcards properly.

### Exercise 2: Generic Documentation

Write Javadoc for a generic class with proper `@param` tags.

### Exercise 3: Best Practice Review

Review existing code and identify violations of generics best practices.

---

## Assignments

### Assignment 1: Generic API Design

Design a type-safe API using best practices:
1. Use parameterized types consistently
2. Apply PECS principle
3. Document type parameters
4. Avoid raw types

### Assignment 2: Code Review

Perform a code review focusing on:
1. Generic type usage
2. Wildcard usage
3. Naming conventions
4. Documentation

---

## Mini Project

### Generic Utility Library

Build a production-quality generic utility library:
1. Follow all best practices
2. Document all generic types
3. Apply PECS principle
4. Handle type erasure gracefully

---

## Summary

Key best practices for Java generics:

1. **Always use parameterized types** — Never use raw types
2. **Apply PECS** — Producer Extends, Consumer Super
3. **Name clearly** — Use T, E, K, V conventions
4. **Document constraints** — Javadoc @param tags
5. **Use minimal bounds** — Only require what you need
6. **Prefer wildcards** — For flexible APIs
7. **Suppress warnings properly** — Document why

---

## References

- [Effective Java - Chapter 6: Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Oracle Generics Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/)
- [Google Java Style Guide - Generics](https://google.github.io/styleguide/javaguide.html)
- [Angelika Langer - Generics FAQ](https://www.angelikalanger.com/GenericsFAQ/)
