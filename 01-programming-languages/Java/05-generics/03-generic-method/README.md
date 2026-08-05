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
        System.out.println(numbers);  // [1, 2, 3]
        
        // Swap
        String[] arr = {"first", "second"};
        swap(arr, 0, 1);
        System.out.println(Arrays.toString(arr));  // [second, first]
    }
}
```

---

## Medium Example

### Bounded Generic Methods

```java
import java.util.List;
import java.util.Objects;

public class BoundedGenericMethods {
    
    public static <T extends Comparable<T>> T max(List<T> list) {
        Objects.requireNonNull(list, "List cannot be null");
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List cannot be empty");
        }
        
        T max = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            T current = list.get(i);
            if (current.compareTo(max) > 0) {
                max = current;
            }
        }
        return max;
    }
    
    public static <T extends Number> double sum(List<T> numbers) {
        return numbers.stream()
                      .mapToDouble(Number::doubleValue)
                      .sum();
    }
    
    public static <T extends Number & Comparable<T>> T clamp(
            T value, T min, T max) {
        if (value.compareTo(min) < 0) return min;
        if (value.compareTo(max) > 0) return max;
        return value;
    }
    
    public static void main(String[] args) {
        // max with different types
        List<String> names = List.of("Charlie", "Alice", "Bob");
        System.out.println(max(names));  // Charlie
        
        List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9);
        System.out.println(max(numbers));  // 9
        
        // sum
        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        System.out.println(sum(ints));  // 15.0
        
        // clamp
        System.out.println(clamp(15, 0, 10));   // 10
        System.out.println(clamp(-5, 0, 10));    // 0
        System.out.println(clamp(5, 0, 10));     // 5
    }
}
```

---

## Hard Example

### Advanced Generic Method Patterns

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class AdvancedGenericMethods {
    
    // Method with both class-level and method-level type parameters
    public static <T, R> List<R> map(List<T> source, Function<T, R> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }
    
    // Recursive generic method
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> T[] sort(T[] array) {
        Object[] temp = Arrays.copyOf(array, array.length);
        Arrays.sort(temp);
        return (T[]) temp;
    }
    
    // Generic method with complex bounds
    public static <T extends Comparable<T> & Serializable> T median(List<T> list) {
        List<T> sorted = list.stream()
                             .sorted()
                             .collect(Collectors.toList());
        int mid = sorted.size() / 2;
        return sorted.get(mid);
    }
    
    // Generic factory method
    public static <T> Supplier<T> memoize(Supplier<T> supplier) {
        T cached = null;
        boolean computed = false;
        return () -> {
            if (!computed) {
                cached = supplier.get();
                computed = true;
            }
            return cached;
        };
    }
    
    // Generic method with varargs and bounds
    @SafeVarargs
    public static <T extends Comparable<T>> List<T> sortedList(T... elements) {
        return Arrays.stream(elements)
                     .sorted()
                     .collect(Collectors.toList());
    }
    
    // Type-safe heterogeneous method
    public static <T> T checkedCast(Object obj, Class<T> type) {
        return type.cast(obj);
    }
    
    public static void main(String[] args) {
        // map
        List<String> words = List.of("hello", "world");
        List<Integer> lengths = map(words, String::length);
        System.out.println(lengths);  // [5, 5]
        
        // sortedList
        List<Integer> sorted = sortedList(5, 3, 1, 4, 2);
        System.out.println(sorted);  // [1, 2, 3, 4, 5]
        
        // median
        List<Integer> nums = List.of(7, 3, 1, 5, 9);
        System.out.println(median(nums));  // 5
        
        // checkedCast
        Object obj = "hello";
        String str = checkedCast(obj, String.class);
        System.out.println(str);  // hello
    }
}
```

---

## Enterprise Example

### Utility Class with Generic Methods

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class CollectionUtils {
    
    private CollectionUtils() {}
    
    public static <T> List<T> filter(List<T> source, Predicate<T> predicate) {
        return source.stream()
                     .filter(predicate)
                     .collect(Collectors.toList());
    }
    
    public static <T, R> Map<R, List<T>> groupBy(
            List<T> source, Function<T, R> keyExtractor) {
        return source.stream()
                     .collect(Collectors.groupingBy(keyExtractor));
    }
    
    public static <T, R> Map<T, R> toMap(
            List<T> source, Function<T, R> valueExtractor) {
        return source.stream()
                     .collect(Collectors.toMap(
                         Function.identity(), 
                         valueExtractor));
    }
    
    public static <T> Optional<T> findFirst(
            List<T> source, Predicate<T> predicate) {
        return source.stream()
                     .filter(predicate)
                     .findFirst();
    }
    
    public static <T> boolean anyMatch(
            List<T> source, Predicate<T> predicate) {
        return source.stream().anyMatch(predicate);
    }
    
    public static <T> boolean allMatch(
            List<T> source, Predicate<T> predicate) {
        return source.stream().allMatch(predicate);
    }
    
    @SafeVarargs
    public static <T> List<T> concat(List<T>... lists) {
        return Arrays.stream(lists)
                     .flatMap(Collection::stream)
                     .collect(Collectors.toList());
    }
    
    public static <T> Map<T, Integer> frequency(List<T> source) {
        return source.stream()
                     .collect(Collectors.toMap(
                         Function.identity(),
                         t -> 1,
                         Integer::sum));
    }
    
    public static <T> List<List<T>> partition(
            List<T> source, int size) {
        return IntStream.range(0, (source.size() + size - 1) / size)
                        .mapToObj(i -> source.subList(
                            i * size, 
                            Math.min((i + 1) * size, source.size())))
                        .collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        List<String> names = List.of(
            "Alice", "Bob", "Charlie", "David", "Eve");
        
        // filter
        List<String> longNames = filter(names, n -> n.length() > 3);
        System.out.println(longNames);  // [Alice, Charlie, David]
        
        // groupBy
        Map<Integer, List<String>> byLength = groupBy(names, String::length);
        System.out.println(byLength);  // {3=[Bob, Eve], 5=[Alice], 7=[Charlie, David]}
        
        // frequency
        List<String> fruits = List.of("apple", "banana", "apple", "cherry");
        Map<String, Integer> freq = frequency(fruits);
        System.out.println(freq);  // {apple=2, banana=1, cherry=1}
        
        // partition
        List<List<String>> partitions = partition(names, 2);
        System.out.println(partitions);  // [[Alice, Bob], [Charlie, David], [Eve]]
    }
}
```

---

## Performance

### Generic Method Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Minimal (type inference) |
| Runtime | Zero (type erasure) |
| Method dispatch | Same as non-generic |
| Inlining | JVM can inline generic methods |

### Type Inference Cost

```java
// Simple inference (cheap)
String s = identity("hello");

// Complex inference (still cheap at runtime)
// The compiler does the work, not the JVM
<T extends Comparable<T>> T max(T a, T b);
```

---

## Best Practices

1. **Place type parameters before return type** — `<T> T method()`, not `T <T> method()`
2. **Use bounded types when needed** — `<T extends Comparable<T>>`
3. **Let the compiler infer types** — Avoid explicit type arguments when possible
4. **Name type parameters meaningfully** — `E` for elements, `R` for return
5. **Prefer methods over classes** — When only one method needs generics
6. **Use `@SafeVarargs`** — For generic varargs methods

---

## Common Mistakes

### 1. Type Parameter Placement

```java
// WRONG
public T <T> identity(T value) { return value; }

// RIGHT
public <T> T identity(T value) { return value; }
```

### 2. Confusing Class and Method Type Parameters

```java
public class Box<T> {
    // Class-level: T
    private T value;
    
    // Method-level: U (independent)
    public <U> U convert(Function<T, U> fn) {
        return fn.apply(value);
    }
    
    // This would shadow class T (bad practice)
    public <T> T badMethod(T value) { return value; }
}
```

### 3. Using Raw Types in Generic Methods

```java
// WRONG
public static <T> List<T> bad(List raw) {
    return raw;  // Unchecked warning
}

// RIGHT
public static <T> List<T> good(List<T> raw) {
    return raw;
}
```

---

## Pitfalls

### 1. Type Erasure in Overloading

```java
// These have the SAME erasure - compile error!
public static <T> void process(List<T> list) { }
public static <T> void process(List<String> list) { }
// Erasure: both become process(List)
```

### 2. Generic Method in Generic Class

```java
public class Box<T> {
    // T is class-level
    private T value;
    
    // This T shadows class T
    public <T> T bad(T input) { return input; }
    
    // Use different name
    public <U> U convert(Function<T, U> fn) {
        return fn.apply(value);
    }
}
```

### 3. Varargs Heap Pollution

```java
// Potential heap pollution warning
public static <T> T[] bad(T... elements) {
    return elements;  // Warning: Possible heap pollution
}

// Safe varargs annotation
@SafeVarargs
public static <T> T[] safe(T... elements) {
    return elements;
}
```

---

## Debugging Tips

### 1. Check Type Inference

```java
// If type inference fails, provide explicit types
List<String> list = GenericMethodBasics.list("a", "b");
// vs
var list = GenericMethodBasics.list("a", "b");  // Inferred as List<String>
```

### 2. Read Compiler Errors

```
Error: incompatible types: String cannot be converted to Integer
// This tells you exactly which types are mismatched
// Check the generic method's type constraints
```

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
