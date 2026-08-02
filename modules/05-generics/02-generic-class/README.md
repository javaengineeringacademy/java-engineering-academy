# 02 - Generic Classes

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

A **generic class** is a class that can work with different data types through type parameters. Instead of creating separate classes for each type (`StringBox`, `IntegerBox`, etc.), you create one generic class that accepts the type as a parameter. This is the foundation of reusable, type-safe code in Java.

Generic classes are the most common use of generics and are used extensively in the Java Collections Framework (`ArrayList<E>`, `HashMap<K,V>`), concurrent programming (`CompletableFuture<T>`), and throughout the Java API.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Design and implement generic classes with single and multiple type parameters
- Understand type parameter bounds in class declarations
- Implement generic interfaces
- Use diamond operator for type inference
- Create type-safe data structures
- Apply generics to real-world class designs

---

## Prerequisites

- Understanding of Java classes and objects
- Interface implementation
- Basic understanding of type erasure (Topic 01)
- Object-oriented design principles

---

## Why This Concept Exists

### Without Generic Classes

```java
// StringList.java
public class StringList {
    private String[] elements;
    private int size;
    
    public void add(String element) { /* ... */ }
    public String get(int index) { /* ... */ }
}

// IntegerList.java - Almost identical code!
public class IntegerList {
    private Integer[] elements;
    private int size;
    
    public void add(Integer element) { /* ... */ }
    public Integer get(int index) { /* ... */ }
}
```

**Problems:**
1. **Code duplication** — Nearly identical classes for each type
2. **Maintenance burden** — Bug fixes must be applied to every version
3. **Limited extensibility** — New types require new classes
4. **No type safety** — Without generics, you'd use `Object` and cast

### With Generic Classes

```java
// One class to rule them all
public class GenericList<T> {
    private T[] elements;
    private int size;
    
    public void add(T element) { /* ... */ }
    public T get(int index) { /* ... */ }
}

// Usage
GenericList<String> strings = new GenericList<>();
GenericList<Integer> integers = new GenericList<>();
GenericList<User> users = new GenericList<>();
```

---

## Problem Statement

Build a data structure that can store and retrieve elements of any type while maintaining type safety.

**Requirements:**
1. Single class that works with `String`, `Integer`, `User`, or any type
2. Compile-time guarantee that only the correct type can be added
3. No explicit casting when retrieving elements
4. Type information preserved through the API

---

## Theory

### Type Parameters in Class Declarations

```java
public class Box<T> {
    // T is a "type parameter" or "type variable"
    // It acts as a placeholder for an actual type
    
    private T content;  // T used as field type
    
    public void set(T content) {  // T used as parameter type
        this.content = content;
    }
    
    public T get() {  // T used as return type
        return content;
    }
}
```

### Multiple Type Parameters

```java
public class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}
```

**Convention:** Use single uppercase letters:
- `T` — Type
- `E` — Element (collections)
- `K` — Key
- `V` — Value
- `N` — Number
- `R` — Return

### Generic Interfaces

```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
}

// Implementation
public class UserRepository implements Repository<User, Long> {
    @Override
    public User findById(Long id) { /* ... */ }
    
    @Override
    public List<User> findAll() { /* ... */ }
    
    @Override
    public void save(User entity) { /* ... */ }
}
```

### Parameterized Types as Field Types

```java
public class DataProcessor<T> {
    private List<T> data;           // Parameterized List
    private Map<String, T> indexed; // Parameterized Map
    private Pair<T, T> range;       // Nested parameterized type
    
    public DataProcessor(List<T> data) {
        this.data = data;
        this.indexed = new HashMap<>();
    }
}
```

---

## Internal Working

### Compiler Transformation

```java
// Source code
public class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T v) { value = v; }
}

Box<String> box = new Box<>();
box.set("hello");
String s = box.get();
```

### What the Compiler Produces

```java
// After type erasure
public class Box {
    private Object value;  // T → Object (unbounded)
    
    public Object get() { return value; }
    
    public void set(Object v) { value = v; }
    
    // Compiler adds bridge method for polymorphism
    public void set(String v) { set((Object) v); }
}

// Usage after erasure
Box box = new Box();
box.set("hello");
String s = (String) box.get();  // Cast inserted
```

### Erasure with Bounds

```java
// Source
public class NumberBox<T extends Number> {
    private T value;
    public double doubleValue() { return value.doubleValue(); }
}

// After erasure
public class NumberBox {
    private Number value;  // T → Number (bounded)
    
    public double doubleValue() { return value.doubleValue(); }
}
```

---

## JVM Perspective

### Class File Structure

```
Box.class (after compilation):
├── This class: Box
├── Super class: java.lang.Object
├── Constant pool
│   ├── Type parameters: erased
│   └── Method signatures: raw
├── Fields
│   └── value: Object (not String)
├── Methods
│   ├── get(): Object
│   ├── set(Object): void
│   └── bridge set(String): void
└── Source file: Box.java
```

### Type Information in Bytecode

```bash
# Generic type info is stored in Signature attribute
javap -v Box.class | grep -A 5 "Signature"
# Signature: LBox<Ljava/lang/String;>;  ← Still in bytecode!
# But JVM doesn't use it for type checking
```

### Reflection and Type Tokens

```java
Box<String> box = new Box<>();

// Runtime type information
Class<?> clazz = box.getClass();
// clazz.getName() = "Box" (not "Box<String>")

// To get generic type info, use TypeToken pattern
Type superclass = clazz.getGenericSuperclass();
ParameterizedType paramType = (ParameterizedType) superclass;
Type[] typeArgs = paramType.getActualTypeArguments();
// typeArgs[0] = String.class (if available in bytecode)
```

---

## Memory Representation

### Generic vs Non-Generic Objects

```java
Box<String> stringBox = new Box<>();
Box<Integer> intBox = new Box<>();
Box<List<String>> listBox = new Box<>();
```

**Memory layout:**
```
All Box objects have IDENTICAL layout:
┌─────────────────────────┐
│ Object header (16 bytes)│
│ value: Object reference │
└─────────────────────────┘

The type parameter doesn't affect:
- Object size
- Field types
- Method signatures
- Memory alignment
```

### Type Information Storage

```
Class metadata (in Metaspace):
┌─────────────────────────────────────┐
│ Box.class                           │
├─────────────────────────────────────┤
│ Signature: LBox<Ljava/lang/String;> │ ← Generic info preserved
│ RuntimeVisibleAnnotations            │
│ SourceFile: Box.java                 │
└─────────────────────────────────────┘
```

---

## Syntax

### Basic Generic Class

```java
public class ClassName<T> {
    private T field;
    
    public ClassName(T field) {
        this.field = field;
    }
    
    public T getField() {
        return field;
    }
    
    public void setField(T field) {
        this.field = field;
    }
}
```

### Multiple Type Parameters

```java
public class ClassName<K, V> {
    private K key;
    private V value;
    
    public ClassName(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() { return key; }
    public V getValue() { return value; }
}
```

### Generic Class with Bounds

```java
public class ClassName<T extends Comparable<T>> {
    private T value;
    
    public int compareTo(ClassName<T> other) {
        return this.value.compareTo(other.value);
    }
}

// Multiple bounds
public class ClassName<T extends Number & Comparable<T>> {
    private T value;
    
    public boolean isGreaterThan(T other) {
        return value.compareTo(other) > 0;
    }
}
```

### Generic Interface Implementation

```java
// Generic interface
public interface Pair<A, B> {
    A getFirst();
    B getSecond();
}

// Concrete implementation
public class ImmutablePair<A, B> implements Pair<A, B> {
    private final A first;
    private final B second;
    
    public ImmutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public A getFirst() { return first; }
    
    @Override
    public B getSecond() { return second; }
}

// Specialized implementation
public class StringIntegerPair implements Pair<String, Integer> {
    // Type parameters fixed to String and Integer
}
```

---

## Easy Example

### Generic Box

```java
public class Box<T> {
    private T content;

    public Box() {
    }

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Box[" + content + "]";
    }

    public static void main(String[] args) {
        Box<String> stringBox = new Box<>("Hello");
        Box<Integer> intBox = new Box<>(42);
        
        System.out.println(stringBox);  // Box[Hello]
        System.out.println(intBox);     // Box[42]
        
        stringBox.setContent("World");
        System.out.println(stringBox);  // Box[World]
    }
}
```

---

## Medium Example

### Generic Pair with Comparison

```java
public class Pair<A extends Comparable<A>, B extends Comparable<B>> 
        implements Comparable<Pair<A, B>> {
    
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() { return first; }
    public B getSecond() { return second; }

    @Override
    public int compareTo(Pair<A, B> other) {
        int firstCompare = this.first.compareTo(other.first);
        if (firstCompare != 0) {
            return firstCompare;
        }
        return this.second.compareTo(other.second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair<?, ?>)) return false;
        Pair<?, ?> other = (Pair<?, ?>) obj;
        return first.equals(other.first) && second.equals(other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    public static <A extends Comparable<A>, B extends Comparable<B>> 
            Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public static void main(String[] args) {
        Pair<String, Integer> alice = Pair.of("Alice", 30);
        Pair<String, Integer> bob = Pair.of("Bob", 25);
        
        System.out.println(alice);  // (Alice, 30)
        System.out.println(alice.compareTo(bob));  // negative (Alice < Bob)
    }
}
```

---

## Hard Example

### Generic Binary Tree

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BinaryTree<T extends Comparable<T>> {
    
    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node<T> root;
    private int size;

    public BinaryTree() {
        this.root = null;
        this.size = 0;
    }

    public void insert(T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        root = insertRecursive(root, value);
        size++;
    }

    private Node<T> insertRecursive(Node<T> current, T value) {
        if (current == null) {
            return new Node<>(value);
        }

        int compare = value.compareTo(current.data);
        if (compare < 0) {
            current.left = insertRecursive(current.left, value);
        } else if (compare > 0) {
            current.right = insertRecursive(current.right, value);
        }
        // Duplicate values are ignored

        return current;
    }

    public boolean contains(T value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(Node<T> current, T value) {
        if (current == null) {
            return false;
        }

        int compare = value.compareTo(current.data);
        if (compare == 0) {
            return true;
        } else if (compare < 0) {
            return containsRecursive(current.left, value);
        } else {
            return containsRecursive(current.right, value);
        }
    }

    public List<T> inOrderTraversal() {
        List<T> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(Node<T> node, List<T> result) {
        if (node != null) {
            inOrderRecursive(node.left, result);
            result.add(node.data);
            inOrderRecursive(node.right, result);
        }
    }

    public T findMin() {
        if (root == null) {
            throw new IllegalStateException("Tree is empty");
        }
        return findMinRecursive(root);
    }

    private T findMinRecursive(Node<T> node) {
        if (node.left == null) {
            return node.data;
        }
        return findMinRecursive(node.left);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public static void main(String[] args) {
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);

        System.out.println("In-order: " + tree.inOrderTraversal());
        // [20, 30, 40, 50, 60, 70, 80]

        System.out.println("Contains 40: " + tree.contains(40));  // true
        System.out.println("Contains 25: " + tree.contains(25));  // false
        System.out.println("Min: " + tree.findMin());              // 20

        // Type safety enforced
        // BinaryTree<String> stringTree = new BinaryTree<>();
        // stringTree.insert(42);  // Compile error!
    }
}
```

---

## Enterprise Example

### Generic Result Type with Error Handling

```java
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Result<T> {
    
    private final T value;
    private final String error;
    private final boolean success;

    private Result(T value, String error, boolean success) {
        this.value = value;
        this.error = error;
        this.success = success;
    }

    public static <T> Result<T> success(T value) {
        Objects.requireNonNull(value, "Success value cannot be null");
        return new Result<>(value, null, true);
    }

    public static <T> Result<T> failure(String error) {
        Objects.requireNonNull(error, "Error message cannot be null");
        return new Result<>(null, error, false);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public T getValue() {
        if (!success) {
            throw new IllegalStateException("Cannot get value from failure: " + error);
        }
        return value;
    }

    public String getError() {
        if (success) {
            throw new IllegalStateException("Cannot get error from success");
        }
        return error;
    }

    public T orElse(T defaultValue) {
        return success ? value : defaultValue;
    }

    public T orElseGet(Supplier<T> supplier) {
        return success ? value : supplier.get();
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        if (success) {
            try {
                return Result.success(mapper.apply(value));
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        } else {
            return Result.failure(error);
        }
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        Objects.requireNonNull(mapper, "Mapper cannot be null");
        if (success) {
            try {
                return mapper.apply(value);
            } catch (Exception e) {
                return Result.failure(e.getMessage());
            }
        } else {
            return Result.failure(error);
        }
    }

    @Override
    public String toString() {
        return success ? "Success[" + value + "]" : "Failure[" + error + "]";
    }

    public static void main(String[] args) {
        Result<Integer> success = Result.success(42);
        Result<Integer> failure = Result.failure("Something went wrong");

        System.out.println(success);  // Success[42]
        System.out.println(failure);  // Failure[Something went wrong]

        // Chaining operations
        Result<String> mapped = success.map(i -> "Number: " + i);
        System.out.println(mapped);  // Success[Number: 42]

        // Error propagation
        Result<String> chained = failure.map(i -> "Number: " + i);
        System.out.println(chained);  // Failure[Something went wrong]

        // Safe value access
        int value1 = success.orElse(0);  // 42
        int value2 = failure.orElse(0);  // 0
    }
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

- [Oracle - Generic Classes and Type Parameters](https://docs.oracle.com/javase/tutorial/java/generics/types.html)
- [Java Language Specification §8.1.2 - Generic Class Declarations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-8.html#jls-8.1.2)
- [Effective Java - Item 29: Use wildcards to increase API flexibility](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Generics Tutorial](https://www.baeldung.com/java-generics)
