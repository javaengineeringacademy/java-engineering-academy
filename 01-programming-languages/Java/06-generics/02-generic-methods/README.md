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
