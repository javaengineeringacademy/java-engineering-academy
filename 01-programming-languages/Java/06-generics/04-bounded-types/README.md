# 04 - Bounded Type Parameters

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
16. [Best Practices](#best-practices)
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

**Bounded type parameters** restrict the types that can be used as generic arguments. By using the `extends` keyword, you can require that a type parameter must be a subclass of a specified upper bound. This enables you to call methods specific to the bound type while maintaining generic flexibility.

Bounded types are essential for writing generic code that requires specific capabilities — like `Comparable` for sorting, `Number` for arithmetic, or `Serializable` for serialization.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Declare upper-bounded type parameters with `extends`
- Apply multiple bounds with `&` syntax
- Understand the difference between bounded and unbounded types
- Use recursive type bounds
- Apply bounds to enforce API contracts
- Choose appropriate bounds for generic types

---

## Prerequisites

- Generic classes and methods (Topics 02-03)
- Understanding of inheritance and interfaces
- Type erasure concepts (Topic 01)
- Basic knowledge of Java Collections

---

## Why This Concept Exists

### Without Bounded Types

```java
// Unbounded - can't call any type-specific methods
public static <T> int compare(T a, T b) {
    return a.compareTo(b);  // Compile error! T doesn't have compareTo
}

// Workaround with Object - loses type safety
public static <T> int compare(Object a, Object b) {
    return ((Comparable) a).compareTo(b);  // Unsafe cast
}
```

### With Bounded Types

```java
// Bounded - T must be Comparable
public static <T extends Comparable<T>> int compare(T a, T b) {
    return a.compareTo(b);  // OK! compareTo is guaranteed
}

// Type-safe and flexible
compare("hello", "world");  // String is Comparable
compare(1, 2);               // Integer is Comparable
// compare(new Object(), new Object());  // Compile error! Object isn't Comparable
```

---

## Problem Statement

Create generic code that:
1. Requires specific methods on the type parameter
2. Enforces contracts at compile time
3. Supports multiple constraints (e.g., `Number` AND `Comparable`)
4. Enables recursive type relationships
5. Maintains flexibility while ensuring safety

---

## Theory

### Upper Bounded Type Parameters

The `extends` keyword specifies an upper bound:

```java
// T must be Number or a subclass of Number
public static <T extends Number> double square(T value) {
    return value.doubleValue() * value.doubleValue();
}

// T must be Comparable<T> or implement it
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### Bounds Hierarchy Diagram

```mermaid
graph TD
    A[Type Parameter Bounds] --> B[Unbounded T]
    A --> C[Upper Bounded T extends X]
    A --> D[Multiple Bounds T extends A and B]
    
    B --> E[Object methods only]
    C --> F[X methods available]
    D --> G[A methods + B methods]
    
    F --> H{X is class or interface?}
    H -->|Class| I[Class methods]
    H -->|Interface| J[Interface methods]
    
    G --> K{A is class, B is interface?}
    K -->|Yes| L[A class methods + B interface methods]
    K -->|No| M[Compiler Error]
    
    style A fill:#4a90d9,color:#fff
    style B fill:#51cf66,color:#fff
    style C fill:#ffd43b,color:#333
    style D fill:#ff922b,color:#fff
    style M fill:#ff6b6b,color:#fff
```

### Multiple Bounds

Use `&` to specify multiple bounds:

```java
// T must extend Number AND implement Comparable
public static <T extends Number & Comparable<T>> T max(List<T> list) {
    T max = list.get(0);
    for (T item : list) {
        if (item.compareTo(max) > 0) {
            max = item;
        }
    }
    return max;
}
```

**Rules:**
- First bound can be a class (if any)
- Subsequent bounds must be interfaces
- `extends` is used for both classes and interfaces in generic bounds

### Recursive Type Bounds

A type parameter can bound itself:

```java
// T must implement Comparable<T> (comparable to itself)
public static <T extends Comparable<T>> void sort(T[] array) {
    Arrays.sort(array);  // OK: T implements Comparable<T>
}
```

### Upper Bounded Wildcards vs Type Parameters

```java
// Type parameter - you need to CREATE or MODIFY values
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;  // Need T for return
}

// Wildcard - you only READ values
public static boolean isGreater(Comparable<?> a, Comparable<?> b) {
    return a.compareTo(b) > 0;  // Don't need T for return
}
```

---

## Internal Working

### Compiler Actions

```java
// Source
public static <T extends Number> double sum(List<T> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

List<Integer> ints = List.of(1, 2, 3);
double result = sum(ints);
```

**Compiler steps:**
1. **Verify bound** — `Integer extends Number` ✓
2. **Type check** — `List<Integer>` matches `List<T>` where `T extends Number` ✓
3. **Erase bound** — Replace `T` with `Number` (the upper bound)
4. **Insert cast** — Add `(Number)` cast where needed

### Bytecode After Erasure

```java
// What the JVM sees
public static double sum(List list) {
    return list.stream()
               .mapToDouble(((Number) x -> x).doubleValue())
               .sum();
}
```

### Multiple Bounds Erasure

```java
// Source
public static <T extends Number & Comparable<T>> T max(List<T> list) { ... }

// After erasure - T becomes Number (first bound)
public static Number max(List list) { ... }
```

---

## JVM Perspective

### Bound Information in Bytecode

```java
public class NumberBox<T extends Number> {
    private T value;
}

// Bytecode signature
// Signature: LNumberBox<TT;>;  where T:Ljava/lang/Number;
// The bound is preserved in the Signature attribute
```

### Runtime Verification

```java
// Bounds are checked at compile time AND runtime (for generic types)
Box<Number> box = new Box<>();
box.setValue(42);      // OK
box.setValue(3.14);    // OK

// But this fails at runtime (due to type erasure workarounds):
// box.setValue("hello");  // Compile error, but runtime would succeed
// The bound prevents this at compile time
```

---

## Memory Representation

### Bounded vs Unbounded

```java
Box<Number> bounded = new Box<>();
Box<Object> unbounded = new Box<>();
```

**Memory layout:**
```
Both have IDENTICAL layout:
┌─────────────────────────┐
│ Object header (16 bytes)│
│ value: Object reference │
└─────────────────────────┘

The bound affects:
- Which methods can be called (compile time)
- What types can be assigned (compile time)
- NOT memory layout or performance
```

---

## Syntax

### Basic Upper Bound

```java
public class ClassName<T extends BoundType> {
    // T is bounded by BoundType
}

public static <T extends BoundType> ReturnType method(T param) {
    // T is bounded by BoundType
}
```

### Multiple Bounds

```java
public class ClassName<T extends ClassA & InterfaceB & InterfaceC> {
    // T extends ClassA AND implements InterfaceB AND InterfaceC
}

// Order matters: class first, then interfaces
public static <T extends Number & Comparable<T> & Serializable> T method(T param) {
    // T is Number, Comparable, and Serializable
}
```

### Recursive Bound

```java
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// T must be comparable to itself
```

### Wildcard Bounds (Preview for Topic 05)

```java
// Upper bounded wildcard
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}
```

---

## Easy Example

### Basic Bounded Type

```java
public class BoundedBox<T extends Number> {
    private T value;

    public BoundedBox(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public double doubleValue() {
        return value.doubleValue();  // OK: Number has doubleValue()
    }

    public int intValue() {
        return value.intValue();  // OK: Number has intValue()
    }

    public static void main(String[] args) {
        BoundedBox<Integer> intBox = new BoundedBox<>(42);
        BoundedBox<Double> doubleBox = new BoundedBox<>(3.14);
        
        System.out.println(intBox.doubleValue());   // 42.0
        System.out.println(doubleBox.doubleValue()); // 3.14
        
        // BoundedBox<String> stringBox = new BoundedBox<>("hello");
        // Compile error: String does not extend Number
    }
}
```

---

## Medium Example

### Comparable Bounded Type

```java
import java.util.List;
import java.util.Objects;

public class SortableUtil {
    
    public static <T extends Comparable<T>> T max(List<T> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Empty list");
        }

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
