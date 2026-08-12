# 09 - Mini Project: Type-Safe Collection Framework

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

This mini project brings together all concepts from the Generics module by building a simplified, type-safe collection framework. You will implement generic interfaces, classes, and methods while applying bounded types, wildcards, and the PECS principle.

---

## Learning Objectives

By the end of this project, you will be able to:

- Apply all generic concepts learned in this module
- Design and implement generic interfaces and classes
- Use bounded types and wildcards effectively
- Implement iterator and iterable patterns
- Build a functional, type-safe API
- Handle type erasure gracefully

---

## Prerequisites

- All previous topics (01-08)
- Understanding of Iterator pattern
- Familiarity with Java Collections API
- Basic stream operations (helpful)

---

## Why This Concept Exists

### Learning by Building

Understanding generics through theory is important, but building a real framework solidifies that knowledge. This project challenges you to:

1. Apply generic concepts in a real codebase
2. Handle type erasure in practice
3. Design flexible, type-safe APIs
4. Make design decisions about generic constraints

---

## Problem Statement

Build a simplified, type-safe collection framework that includes:

1. `SimpleList<T>` interface — core collection operations
2. `ArrayList<T>` implementation — dynamic array-based list
3. `LinkedList<T>` implementation — node-based list
4. `SimpleIterator<T>` interface — iteration support
5. `Collections` utility class — static utility methods
6. Type-safe filtering, mapping, and transformation

---

## Theory

### Generic Interface Design

```java
public interface SimpleList<E> extends Iterable<E> {
    boolean add(E element);
    E get(int index);
    E set(int index, E element);
    E remove(int index);
    int size();
    boolean isEmpty();
    boolean contains(E element);
    void clear();
}
```

### Iterator Pattern

```java
public interface SimpleIterator<E> {
    boolean hasNext();
    E next();
    void remove();
}
```

### Utility Methods

```java
public class Collections {
    public static <T> boolean addAll(SimpleList<? super T> dest, T... elements) { ... }
    public static <T> SimpleList<T> unmodifiable(SimpleList<T> list) { ... }
    public static <T> void swap(SimpleList<T> list, int i, int j) { ... }
}
```

---

## Internal Working

### ArrayList Implementation

```java
public class ArrayList<E> implements SimpleList<E> {
    private Object[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public ArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    @Override
    public boolean add(E element) {
        ensureCapacity();
        elements[size++] = element;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) elements[index];
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
```

---

## JVM Perspective

### Type Erasure in Action

```java
ArrayList<String> strings = new ArrayList<>();
ArrayList<Integer> integers = new ArrayList<>();

// After compilation, both become ArrayList
// Type parameters erased to Object
// Casts inserted for type safety
```

### Bridge Methods

```java
// When implementing generic interfaces
public class ArrayList<E> implements SimpleList<E> {
    @Override
    public boolean add(E element) { ... }

    // Bridge method added by compiler
    public boolean add(Object element) {
        return add((E) element);
    }
}
```

---

## Memory Representation

### ArrayList Memory Layout

```
```
ArrayList object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ elementData: Object[] ref ──┼──→ [Object, Object, Object, ...]
│ size: int (4 bytes)         │
└─────────────────────────────┘

Object[] array:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ length: int (4 bytes)       │
│ [0]: Object reference       │
│ [1]: Object reference       │
│ [2]: Object reference       │
│ ...                         │
└─────────────────────────────┘
```

### LinkedList Memory Layout

```
LinkedList object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ first: Node ref ────────────┼──→ Node → Node → Node → null
│ size: int (4 bytes)         │
└─────────────────────────────┘

Node object:
┌─────────────────────────────┐
│ Object header (16 bytes)    │
│ item: Object reference      │
│ next: Node reference        │
│ prev: Node reference        │
└─────────────────────────────┘
```

---

## Syntax

### Interface Definition

