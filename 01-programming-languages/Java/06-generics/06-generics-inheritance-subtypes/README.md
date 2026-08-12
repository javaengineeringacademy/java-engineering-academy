# 06 - Type Erasure

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

**Type erasure** is the process by which the Java compiler removes all generic type information at compile time, replacing type parameters with their bounds (or `Object` if unbounded). This ensures backward compatibility with pre-Java 5 code but means generic type information is not available at runtime.

Understanding type erasure is crucial for avoiding pitfalls, debugging generic code, and designing APIs that work within Java's type system constraints.

---

## Learning Objectives

By the end of this topic, you will be able to:

- Explain how type erasure works in Java
- Identify the limitations imposed by type erasure
- Work around type erasure restrictions
- Use reflection to access erased type information
- Debug generic code using type erasure knowledge
- Design APIs that account for type erasure

---

## Prerequisites

- Generic classes and methods (Topics 02-03)
- Bounded types (Topic 04)
- Wildcards (Topic 05)
- Basic understanding of bytecode and JVM
- Reflection basics (helpful but not required)

---

## Why This Concept Exists

### Backward Compatibility

```java
// Pre-Java 5 code (still compiles!)
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0);  // Manual cast

// Post-Java 5 code
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0);  // No cast needed

// Both produce IDENTICAL bytecode after type erasure
// This was intentional for backward compatibility
```

### The Problem Type Erasure Solves

```java
// If generics were reified (type info at runtime):
String[] strings = new String[10];
Number[] numbers = new Number[10];
// These are different types at runtime

// With generics:
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
// At runtime: both are ArrayList (same type!)
// This allows pre-Java 5 code to work with generic code
```

---

## Problem Statement

Understand and work within the constraints of type erasure:

1. Cannot use `instanceof` with parameterized types
2. Cannot create generic arrays
3. Cannot use `new T()` or `T.class`
4. Cannot overload methods with different generic signatures
5. Must handle runtime type information through other means

---

## Theory

### How Type Erasure Works

**Rule 1: Unbounded type parameters erase to `Object`**

```java
// Source
public class Box<T> {
    private T value;
    public T get() { return value; }
    public void set(T v) { value = v; }
}

// After erasure
public class Box {
    private Object value;
    public Object get() { return value; }
    public void set(Object v) { value = v; }
}
```

**Rule 2: Bounded type parameters erase to their first bound**

```java
// Source
public class NumberBox<T extends Number> {
    private T value;
    public double doubleValue() { return value.doubleValue(); }
}

// After erasure
public class NumberBox {
    private Number value;
    public double doubleValue() { return value.doubleValue(); }
}
```

**Rule 3: Compiler inserts casts for type safety**

```java
// Source
Box<String> box = new Box<>();
box.set("hello");
String s = box.get();

// After erasure
Box box = new Box<>();
box.set("hello");
String s = (String) box.get();  // Cast inserted
```

**Rule 4: Bridge methods maintain polymorphism**

```java
// Source
public class StringBox extends Box<String> {
    @Override
    public void set(String value) { super.set(value); }
}

// After erasure
public class StringBox extends Box {
    @Override
    public void set(String value) { super.set(value); }
    
    // Bridge method added by compiler
    public void set(Object value) { set((String) value); }
}
```

### Type Erasure Limitations

```java
// 1. Cannot use instanceof with parameterized types
// if (obj instanceof List<String>) { }  // Compile error!

// 2. Cannot create generic arrays
// List<String>[] arrays = new List<String>[10];  // Compile error!

// 3. Cannot use new T()
// public class Box<T> { T value = new T(); }  // Compile error!

// 4. Cannot use T.class
// Class<T> clazz = T.class;  // Compile error!

// 5. Cannot overload with different generic signatures
// public void process(List<String> list) { }
// public void process(List<Integer> list) { }  // Compile error!
```

---

## Internal Working

### Compiler Transformation Steps

```java
// Source code
public class Container<T extends Comparable<T>> {
    private T value;
    
    public Container(T value) {
        this.value = value;
    }
    
    public T getValue() {
        return value;
    }
    
    public int compareTo(Container<T> other) {
        return value.compareTo(other.value);
    }
}

// Usage
Container<String> container = new Container<>("hello");
String v = container.getValue();
int result = container.compareTo(new Container<>("world"));
```

**Compiler steps:**

1. **Type erasure** — Replace `T` with `Comparable`
2. **Cast insertion** — Add `(Comparable)` cast where needed
3. **Bridge method generation** — Add synthetic methods for polymorphism

**After erasure:**

```java
public class Container {
    private Comparable value;
    
    public Container(Comparable value) {
        this.value = value;
    }
    
