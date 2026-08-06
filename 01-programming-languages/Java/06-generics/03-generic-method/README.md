# 03 - Generic Methods

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

A **generic method** is a method that introduces its own type parameters, independent of the class's type parameters. This allows methods to be generic even when the containing class is not, and provides fine-grained type safety at the method level.

Generic methods are powerful because they can infer types from arguments, work with heterogeneous collections, and provide type-safe operations without requiring the entire class to be parameterized.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Declare generic methods with their own type parameters
- Understand type inference in generic methods
- Use bounded type parameters in methods
- Apply multiple type parameters in method signatures
- Distinguish between class-level and method-level type parameters
- Write type-safe utility methods

---

## Prerequisites

- Generic classes (Topic 02)
- Understanding of type erasure (Topic 01)
- Method overloading concepts
- Basic understanding of wildcards (helpful but not required)

---

## Why This Concept Exists

### Without Generic Methods

```java
// Without generics, utility methods need separate implementations
public static int max(int a, int b) { return (a > b) ? a : b; }
public static double max(double a, double b) { return (a > b) ? a : b; }
public static String max(String a, String b) { return a.compareTo(b) > 0 ? a : b; }

// Or use Object and cast (unsafe)
public static Object max(Object a, Object b) {
    return ((Comparable) a).compareTo(b) > 0 ? a : b;
}
// Usage: String s = (String) max("a", "b");  ← Manual cast!
```

### With Generic Methods

```java
// One method works for all Comparable types
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Usage - type inferred, no cast needed
String s = max("hello", "world");     // T inferred as String
Integer i = max(10, 20);              // T inferred as Integer
```

---

## Problem Statement

Create utility methods that:
1. Work with any type while maintaining type safety
2. Infer types automatically from method arguments
3. Can be used in non-generic classes
4. Support multiple type parameters when needed
5. Enforce type constraints at the method level

---

## Theory

### Generic Method Declaration

The type parameter is declared **before** the return type:

```java
public class Utility {
    // <T> declares a type parameter named T
    // T is the return type
    // T is the parameter type
    public static <T> T identity(T value) {
        return value;
    }
}
```

### Type Inference

The compiler infers the type parameter from the arguments:

```java
String result = identity("hello");  // T inferred as String
Integer num = identity(42);          // T inferred as Integer

// Explicit type arguments (rarely needed)
<String> identity("hello");  // Redundant but valid
```

### Multiple Type Parameters

```java
public static <K, V> Map<K, V> singletonMap(K key, V value) {
    return Map.of(key, value);
}

// Usage
Map<String, Integer> map = singletonMap("age", 30);
// K inferred as String, V inferred as Integer
```

### Bounded Type Parameters

```java
// Upper bounded
public static <T extends Number> double sum(List<T> list) {
    return list.stream()
               .mapToDouble(Number::doubleValue)
               .sum();
}

// Multiple bounds
public static <T extends Number & Comparable<T>> T max(List<T> list) {
    return list.stream()
               .max(Comparable::compareTo)
               .orElseThrow();
}
```

### Type Parameters vs Wildcards

```java
// Type parameter - you need to USE the type
public static <T> List<T> asList(T a, T b) {
    return List.of(a, b);
}

// Wildcard - you only READ the type
public static int size(List<?> list) {
    return list.size();
}
```

---

## Internal Working

### Compiler Actions

```java
// Source
public static <T> T first(List<T> list) {
    return list.get(0);
}

String s = first(List.of("a", "b"));
```

**Compiler steps:**
1. **Infer T** — From argument `List.of("a", "b")`, infer `T = String`
2. **Type check** — Verify `List<String>` matches `List<T>` where `T = String`
3. **Erase T** — Replace `T` with `Object` in bytecode
4. **Insert cast** — Add `(String)` cast on return

### Bytecode After Erasure

```java
// What the JVM sees
public static Object first(List list) {
    return list.get(0);
}

// Usage
String s = (String) first(List.of("a", "b"));
```

### Bridge Methods

```java
// Source
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Compiler generates (conceptually)
public static Comparable max(Comparable a, Comparable b) {
    return a.compareTo(b) > 0 ? a : b;
}

public static Comparable max(Object a, Object b) {
    return max((Comparable) a, (Comparable) b);  // Bridge
}
```

---

## JVM Perspective

### Method Signature in Bytecode

```bash
javap -c -p Utility.class
# public static java.lang.Object first(java.util.List)
# The generic type info is in Signature attribute, not the method descriptor
```

### Type Erasure in Generic Methods

```java
// These two methods have the SAME signature after erasure
public static <T> T first(List<T> list) { ... }
public static <E> E first(List<E> list) { ... }
// Compile error: both methods have same erasure
```

### Method-Level Type Parameters

```java
public class Container<T> {
    // Class-level: T
    private T value;
    
    // Method-level: U (independent of T)
    public <U> U convert(Function<T, U> converter) {
        return converter.apply(value);
    }
}
```

---

## Memory Representation

### Generic Method Invocation

```java
public static <T> T identity(T value) {
    return value;
}

String s = identity("hello");  // T = String
Integer i = identity(42);      // T = Integer
```

**Stack frame:**
```
identity("hello"):
┌─────────────────────┐
│ value: reference ───┼──→ String "hello"
│ return: reference ──┼──→ String "hello" (same object)
└─────────────────────┘

identity(42):
┌─────────────────────┐
│ value: Integer ref ─┼──→ Integer 42 (boxed)
│ return: reference ──┼──→ Integer 42 (autoboxed)
└─────────────────────┘
```

---

## Syntax

### Basic Generic Method

```java
// Declaration
public static <T> ReturnType methodName(T parameter) {
    // method body
}

// Examples
public static <T> T identity(T value) { return value; }
public static <T> List<T> list(T... elements) { return Arrays.asList(elements); }
public static <T> void swap(T[] arr, int i, int j) {
    T temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}
```

### Bounded Generic Methods

```java
// Upper bounded
public static <T extends Number> double sum(List<T> numbers) { ... }

// Multiple bounds
public static <T extends Number & Comparable<T>> T max(List<T> numbers) { ... }

// Recursive bound
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}
```

### Multiple Type Parameters

```java
public static <K, V> Map<K, V> of(K key, V value) {
    return Map.of(key, value);
}

public static <T1, T2, R> R combine(T1 a, T2 b, BiFunction<T1, T2, R> combiner) {
    return combiner.apply(a, b);
}
```

### Generic Methods in Non-Generic Classes

```java
public class StringUtils {
    // This class is NOT generic, but the method IS
    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }
    
    public static <T extends Comparable<T>> T max(T a, T b) {
        return a.compareTo(b) > 0 ? a : b;
    }
}
```

---

## Easy Example

### Basic Generic Method

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenericMethodBasics {
    
    public static <T> T identity(T value) {
        return value;
    }
    
    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }
    
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    public static void main(String[] args) {
        // Type inference
        String s = identity("hello");
        Integer i = identity(42);
        
        System.out.println(s);  // hello
        System.out.println(i);  // 42
        
        // Varargs
        List<String> strings = asList("a", "b", "c");
        List<Integer> numbers = asList(1, 2, 3);
        
        System.out.println(strings);  // [a, b, c]

---

[📖 Continue to Part 2](README-part2.md)
 | [📖 Continue to Part 3](README-part3.md)
```
