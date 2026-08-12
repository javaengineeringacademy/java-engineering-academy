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

