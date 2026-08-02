# 05 - Wildcards

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

**Wildcards** (`?`) represent unknown types in generic code. They provide flexibility when you don't need to name or create the type, but only need to read from or write to a generic structure. Wildcards are essential for designing APIs that work with different generic types while maintaining type safety.

The three forms of wildcards are:
- `?` — Unbounded wildcard (unknown type)
- `? extends T` — Upper bounded wildcard (unknown type that is T or a subclass)
- `? super T` — Lower bounded wildcard (unknown type that is T or a superclass)

Wildcards are the key to understanding the **PECS principle** (Producer Extends, Consumer Super).

---

## Learning Objectives

By the end of this topic, you will be able to:

- Understand and apply all three wildcard types
- Apply the PECS principle (Producer Extends, Consumer Super)
- Perform wildcard capture for type-safe operations
- Distinguish between wildcards and type parameters
- Use wildcards for API flexibility
- Avoid common wildcard pitfalls

---

## Prerequisites

- Generic classes and methods (Topics 02-03)
- Bounded type parameters (Topic 04)
- Understanding of type erasure (Topic 01)
- Collections Framework basics

---

## Why This Concept Exists

### Without Wildcards

```java
// This method only works with List<Number>
public static double sum(List<Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

// These DON'T work:
List<Integer> ints = List.of(1, 2, 3);
sum(ints);  // Compile error! List<Integer> is not List<Number>

List<Double> doubles = List.of(1.0, 2.0, 3.0);
sum(doubles);  // Compile error! List<Double> is not List<Number>
```

### With Wildcards

```java
// This works with ANY List of Number subclasses
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

List<Integer> ints = List.of(1, 2, 3);
sum(ints);  // OK!

List<Double> doubles = List.of(1.0, 2.0, 3.0);
sum(doubles);  // OK!

List<Number> numbers = List.of(1, 2.0, 3L);
sum(numbers);  // OK!
```

---

## Problem Statement

Create APIs that:
1. Accept different generic types (e.g., `List<Integer>`, `List<Double>`)
2. Read values without modifying the collection
3. Write values to a collection without knowing the exact type
4. Support both reading and writing in the same method
5. Maintain compile-time type safety

---

## Theory

### Three Forms of Wildcards

#### 1. Unbounded Wildcard (`<?>`)

```java
// Accepts List of ANY type
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// Can read as Object
List<String> strings = List.of("a", "b");
List<Integer> numbers = List.of(1, 2, 3);
printList(strings);  // OK
printList(numbers);  // OK
```

#### 2. Upper Bounded Wildcard (`<? extends T>`)

```java
// Accepts List of T or any subclass of T
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

// Can read as Number (or the bound type)
List<Integer> ints = List.of(1, 2, 3);
List<Double> doubles = List.of(1.0, 2.0, 3.0);
sum(ints);     // OK
sum(doubles);  // OK
```

#### 3. Lower Bounded Wildcard (`<? super T>`)

```java
// Accepts List of T or any superclass of T
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

// Can write Integer values
List<Integer> ints = new ArrayList<>();
List<Number> numbers = new ArrayList<>();
List<Object> objects = new ArrayList<>();
addNumbers(ints);      // OK
addNumbers(numbers);   // OK
addNumbers(objects);   // OK
```

### PECS Principle

**Producer Extends, Consumer Super:**

```java
// Producer (reading): use extends
public static <T> T getFirst(List<? extends T> list) {
    return list.get(0);  // Reading from producer
}

// Consumer (writing): use super
public static <T> void addAll(List<? super T> dest, List<? extends T> src) {
    dest.addAll(src);  // Writing to consumer
}

// Both (reading and writing): use type parameter
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {
        dest.add(item);
    }
}
```

### Wildcard Capture

```java
// Wildcard capture allows using the captured type
public static void swap(List<?> list, int i, int j) {
    // Can't directly: list.set(i, list.get(j));  // Compile error
    
    // Use wildcard capture
    swapHelper(list, i, j);
}

private static <T> void swapHelper(List<T> list, int i, int j) {
    T temp = list.get(i);
    list.set(i, list.get(j));
    list.set(j, temp);
}
```

---

## Internal Working

### Compiler Actions

```java
// Source
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

List<Integer> ints = List.of(1, 2, 3);
double result = sum(ints);
```

**Compiler steps:**
1. **Capture wildcard** — `? extends Number` becomes fresh type `CAP#1`
2. **Type check** — Verify `List<Integer>` is compatible with `List<CAP#1>`
3. **Insert bounds** — `CAP#1 extends Number`
4. **Erase** — Replace `CAP#1` with `Number`

### Bytecode After Erasure

```java
// What the JVM sees
public static double sum(List list) {
    return list.stream()
               .mapToDouble(((Number) x -> x).doubleValue())
               .sum();
}
```

---

## JVM Perspective

### Wildcard in Bytecode

```bash
javap -v MyClass.class | grep "Signature"
# Shows wildcard info in Signature attribute
# But JVM doesn't use it for type checking
```

### Runtime Type Information

```java
List<? extends Number> list = List.of(1, 2, 3);
// At runtime: list is just List
// The ? extends Number is erased
System.out.println(list.getClass());  // java.util.Arrays$ArrayList
```

---

## Memory Representation

### Wildcards Don't Affect Memory

```java
List<String> strings = List.of("a", "b");
List<Integer> integers = List.of(1, 2);
List<? extends Number> numbers = integers;

// All have identical memory layout
// The wildcard exists only at compile time
```

---

## Syntax

### Unbounded Wildcard

```java
public static void print(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

public static boolean isEmpty(Collection<?> collection) {
    return collection.isEmpty();
}
```

### Upper Bounded Wildcard

```java
public static double sum(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

public static <T extends Comparable<T>> T max(List<? extends T> list) {
    return list.stream().max(Comparable::compareTo).orElseThrow();
}
```

### Lower Bounded Wildcard

```java
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}

public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    dest.addAll(src);
}
```

### Multiple Bounds with Wildcards

```java
public static <T extends Number & Comparable<T>> T max(List<? extends T> list) {
    return list.stream().max(Comparable::compareTo).orElseThrow();
}
```

---

## Easy Example

### Basic Wildcard Usage

```java
import java.util.List;

public class WildcardBasics {
    
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }
    
    public static double sum(List<? extends Number> list) {
        double total = 0;
        for (Number num : list) {
            total += num.doubleValue();
        }
        return total;
    }
    
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }
    
    public static void main(String[] args) {
        // Unbounded wildcard
        List<String> names = List.of("Alice", "Bob");
        List<Integer> numbers = List.of(1, 2, 3);
        printList(names);   // OK
        printList(numbers); // OK
        
        // Upper bounded wildcard
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.0, 2.0, 3.0);
        System.out.println(sum(ints));     // 6.0
        System.out.println(sum(doubles));  // 6.0
        
        // Lower bounded wildcard
        java.util.List<Integer> intList = new java.util.ArrayList<>();
        java.util.List<Number> numList = new java.util.ArrayList<>();
        addIntegers(intList);  // OK
        addIntegers(numList);  // OK
    }
}
```

---

## Medium Example

### PECS Principle in Action

```java
import java.util.ArrayList;
import java.util.List;

public class pecsExample {
    
    // Producer Extends: reading from src
    public static <T> List<T> copy(List<? extends T> src) {
        List<T> dest = new ArrayList<>();
        for (T item : src) {
            dest.add(item);
        }
        return dest;
    }
    
    // Consumer Super: writing to dest
    public static <T> void fill(List<? super T> dest, T value, int count) {
        for (int i = 0; i < count; i++) {
            dest.add(value);
        }
    }
    
    // Both: reading from src, writing to dest
    public static <T> void transfer(
            List<? super T> dest, 
            List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }
    
    // Complex PECS example
    public static <T extends Comparable<T>> T findMin(List<? extends T> list) {
        T min = list.get(0);
        for (T item : list) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }
    
    public static void main(String[] args) {
        // copy (Producer Extends)
        List<Integer> ints = List.of(1, 2, 3);
        List<Number> copied = copy(ints);
        System.out.println(copied);  // [1, 2, 3]
        
        // fill (Consumer Super)
        List<Object> objects = new ArrayList<>();
        fill(objects, "hello", 3);
        System.out.println(objects);  // [hello, hello, hello]
        
        // transfer (Both)
        List<Number> dest = new ArrayList<>();
        List<Integer> src = List.of(10, 20, 30);
        transfer(dest, src);
        System.out.println(dest);  // [10, 20, 30]
        
        // findMin
        List<Double> doubles = List.of(3.14, 2.71, 1.41);
        System.out.println(findMin(doubles));  // 1.41
    }
}
```

---

## Hard Example

### Wildcard Capture and Complex Patterns

```java
import java.util.ArrayList;
import java.util.List;

public class AdvancedWildcards {
    
    // Wildcard capture
    public static void swap(List<?> list, int i, int j) {
        swapHelper(list, i, j);
    }
    
    private static <T> void swapHelper(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    // Complex wildcard with multiple bounds
    public static <T extends Number & Comparable<T>> T max(List<? extends T> list) {
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
    
    // Wildcard capture with return type
    public static List<?> filter(List<?> list, java.util.function.Predicate<Object> predicate) {
        List<Object> result = new ArrayList<>();
        for (Object item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    // Nested wildcards
    public static <T> void copyAll(
            List<? super T> dest, 
            List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }
    
    // Complex nested generic with wildcards
    public static <T> List<T> flatten(List<List<? extends T>> lists) {
        List<T> result = new ArrayList<>();
        for (List<? extends T> list : lists) {
            result.addAll(list);
        }
        return result;
    }
    
    public static void main(String[] args) {
        // swap
        List<String> strings = new ArrayList<>(List.of("a", "b", "c"));
        swap(strings, 0, 2);
        System.out.println(strings);  // [c, b, a]
        
        // max with multiple bounds
        List<Integer> nums = List.of(3, 1, 4, 1, 5, 9);
        System.out.println(max(nums));  // 9
        
        // filter
        List<Number> numbers = List.of(1, 2.0, 3L, 4.0f);
        List<?> evens = filter(numbers, n -> {
            if (n instanceof Integer i) return i % 2 == 0;
            if (n instanceof Double d) return d % 2 == 0;
            return false;
        });
        System.out.println(evens);  // [2.0, 4.0]
        
        // flatten
        List<List<Integer>> nested = List.of(
            List.of(1, 2),
            List.of(3, 4),
            List.of(5, 6)
        );
        List<Integer> flat = flatten(nested);
        System.out.println(flat);  // [1, 2, 3, 4, 5, 6]
    }
}
```

---

## Enterprise Example

### Type-Safe Collection Utilities

```java
import java.util.*;
import java.util.function.*;

public final class CollectionUtils {
    
    private CollectionUtils() {}
    
    // Producer Extends: read-only access
    public static <T> Optional<T> findFirst(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        return source.stream().filter(predicate).findFirst();
    }
    
    public static <T> boolean allMatch(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        return source.stream().allMatch(predicate);
    }
    
    public static <T> boolean anyMatch(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        return source.stream().anyMatch(predicate);
    }
    
    // Consumer Super: write-only access
    public static <T> void addAll(
            List<? super T> dest, 
            Collection<? extends T> src) {
        dest.addAll(src);
    }
    
    public static <T> List<T> newFilledList(int size, T value) {
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(value);
        }
        return list;
    }
    
    // Both: read and write
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
    
    public static <T, R> List<R> map(
            List<? extends T> source, 
            Function<? super T, ? extends R> mapper) {
        List<R> result = new ArrayList<>();
        for (T item : source) {
            result.add(mapper.apply(item));
        }
        return result;
    }
    
    // Complex wildcard usage
    public static <T extends Comparable<T>> List<T> sorted(
            List<? extends T> source) {
        return source.stream().sorted().toList();
    }
    
    public static <T> Map<Boolean, List<T>> partition(
            List<? extends T> source, 
            Predicate<? super T> predicate) {
        Map<Boolean, List<T>> result = new HashMap<>();
        result.put(true, new ArrayList<>());
        result.put(false, new ArrayList<>());
        for (T item : source) {
            result.get(predicate.test(item)).add(item);
        }
        return result;
    }
    
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // filter with wildcard
        List<Integer> evens = filter(numbers, n -> n % 2 == 0);
        System.out.println(evens);  // [2, 4, 6, 8, 10]
        
        // map with wildcard
        List<String> strings = map(numbers, n -> "Num: " + n);
        System.out.println(strings);  // [Num: 1, Num: 2, ...]
        
        // partition
        Map<Boolean, List<Integer>> partitioned = 
            partition(numbers, n -> n % 2 == 0);
        System.out.println("Evens: " + partitioned.get(true));
        System.out.println("Odds: " + partitioned.get(false));
        
        // findFirst
        Optional<Integer> first = findFirst(numbers, n -> n > 5);
        System.out.println(first.orElse(0));  // 6
    }
}
```

---

## Performance

### Wildcard Overhead

| Aspect | Impact |
|--------|--------|
| Compile time | Slightly more (wildcard checking) |
| Runtime | Zero (erased) |
| Method calls | Identical |
| Memory | Identical |

### PECS Efficiency

```java
// Wildcards allow efficient API design
// No unnecessary copying or casting
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {  // Efficient iteration
        dest.add(item);   // Direct add, no cast
    }
}
```

---

## Best Practices

1. **Apply PECS consistently** — Producer Extends, Consumer Super
2. **Prefer wildcards over type parameters** — When you don't need to name the type
3. **Use `? extends T` for read-only access** — When you only read from a collection
4. **Use `? super T` for write-only access** — When you only write to a collection
5. **Use `<?>` for truly unknown types** — When you only use Object methods
6. **Document wildcard usage** — Explain why wildcards are used

---

## Common Mistakes

### 1. Adding to Upper Bounded List

```java
// WRONG - can't add to ? extends
public static <T> void bad(List<? extends T> list, T item) {
    list.add(item);  // Compile error!
}

// RIGHT - use type parameter for writing
public static <T> void good(List<T> list, T item) {
    list.add(item);  // OK
}
```

### 2. Reading from Lower Bounded List

```java
// WRONG - can only read as Object
public static <T> T bad(List<? super T> list) {
    return list.get(0);  // Returns Object, not T
}

// RIGHT - use type parameter for reading
public static <T> T good(List<T> list) {
    return list.get(0);  // Returns T
}
```

### 3. Confusing Wildcards and Type Parameters

```java
// Type parameter: you CREATE or RETURN values of type T
public static <T> T identity(T value) { return value; }

// Wildcard: you only READ values of type T
public static boolean isNull(List<?> list) { return list.isEmpty(); }
```

---

## Pitfalls

### 1. Wildcard Capture Limitations

```java
// This doesn't work:
List<?> list = List.of(1, 2, 3);
list.set(0, 42);  // Compile error! Can't add to ?

// Workaround: wildcard capture
public static <T> void set(List<?> list, int index, T value) {
    setHelper(list, index, value);
}
private static <T> void setHelper(List<T> list, int index, T value) {
    list.set(index, value);
}
```

### 2. Nested Wildcards

```java
// Nested wildcards can be confusing
List<? extends List<?>> nested = List.of(List.of(1), List.of(2));
// What can you do with this?

// Better approach: use explicit type parameters
public static <T> List<T> flatten(List<List<? extends T>> lists) {
    List<T> result = new ArrayList<>();
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

- [Oracle - Wildcards](https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html)
- [Java Language Specification §4.5.1 - Type Arguments of Parameterized Types](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html#jls-4.5.1)
- [Effective Java - Item 31: Use bounded wildcards to increase API flexibility](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Angelika Langer - Wildcard FAQ](https://www.angelikalanger.com/GenericsFAQ/FAQSections/Wildcards.html)
