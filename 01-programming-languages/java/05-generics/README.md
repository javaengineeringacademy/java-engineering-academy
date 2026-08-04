# Module 05: Generics

## Overview

Generics enable you to write code that works with any object type while providing compile-time type safety. Introduced in Java 5, generics eliminate the need for explicit type casting and catch type mismatches at compile time rather than runtime.

## Why This Concept Exists

Without generics:
- Code must use Object and cast everywhere
- No compile-time type checking
- Runtime ClassCastException possible
- Code duplication for each type

With generics:
- Single codebase for all types
- Compile-time type safety
- No casting required
- Better code readability

## Learning Objectives

By the end of this module, you will be able to:

- Understand generic classes, interfaces, and methods
- Apply bounded type parameters
- Use wildcards correctly
- Avoid type erasure pitfalls
- Design type-safe APIs

## Topics

| # | Topic | Duration | Difficulty | Description |
|---|-------|----------|------------|-------------|
| 01 | [Introduction](01-introduction/) | 1 hour | Beginner | What generics are and why they matter |
| 02 | [Generic Classes](02-generic-class/) | 2 hours | Beginner | Creating type-parameterized classes |
| 03 | [Generic Methods](03-generic-method/) | 2 hours | Intermediate | Writing methods with their own type parameters |
| 04 | [Bounded Types](04-bounded-types/) | 2 hours | Intermediate | Restricting types with `extends` and `super` |
| 05 | [Wildcards](05-wildcards/) | 2 hours | Intermediate | Unknown type parameters with `?` |
| 06 | [Type Erasure](06-type-erasure/) | 2 hours | Advanced | How generics are implemented at the JVM level |
| 07 | [Best Practices](07-best-practices/) | 1.5 hours | Intermediate | Guidelines for effective generic code |
| 08 | [Real-World](08-real-world/) | 2 hours | Advanced | Industry patterns and production code |
| 09 | [Mini Project](09-mini-project/) | 3 hours | Advanced | Hands-on application of all concepts |

**Total Estimated Time: 18-20 hours**

## Prerequisites

- Solid understanding of OOP (inheritance, polymorphism)
- Familiarity with collections framework
- Basic understanding of interfaces and abstract classes

## Learning Path

```
Introduction → Generic Class → Generic Method → Bounded Types
      ↓                                              ↓
 Type Erasure ← Wildcards ←──────────────────────────┘
      ↓
Best Practices → Real-World → Mini Project
```

## Difficulty Progression

- **Beginner** (Topics 01-02): Basic concepts and syntax
- **Intermediate** (Topics 03-07): Advanced features and patterns
- **Advanced** (Topics 08-09): Real-world applications and projects

## Key Concepts

### Type Safety at Compile Time

```java
// Without generics - runtime ClassCastException possible
List list = new ArrayList();
list.add("hello");
String s = (String) list.get(0); // Manual cast required

// With generics - compile-time error
List<String> list = new ArrayList<>();
list.add("hello");
String s = list.get(0); // No cast needed
list.add(42); // Compile-time error!
```

### Bounded Types

```java
// Upper bound - T must be Number or subclass
public <T extends Number> double sum(List<T> list) {
    return list.stream()
        .mapToDouble(Number::doubleValue)
        .sum();
}

// Multiple bounds
public <T extends Comparable<T> & Serializable> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
```

### Wildcards

```java
// Upper bounded - read-only
public double sum(List<? extends Number> list) {
    return list.stream()
        .mapToDouble(Number::doubleValue)
        .sum();
}

// Lower bounded - write-only
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}

// Unbounded - read-only Object
public void printList(List<?> list) {
    list.forEach(System.out::println);
}
```

### Type Erasure

Generics are a compile-time feature. The JVM sees raw types:

```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();
// At runtime: both are ArrayList (raw type)
System.out.println(strings.getClass() == integers.getClass()); // true
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│              Generic Type Hierarchy                      │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │              Generic Class<T>                    │   │
│  │                  (Source Code)                   │   │
│  └──────────────────────┬──────────────────────────┘   │
│                         │                               │
│                    [Type Erasure]                       │
│                         │                               │
│  ┌──────────────────────▼──────────────────────────┐   │
│  │            Raw Class (JVM Bytecode)             │   │
│  │              Object (T → Object)                │   │
│  └─────────────────────────────────────────────────┘   │
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │          Bounded Type<T extends X>              │   │
│  │              (Source Code)                       │   │
│  └──────────────────────┬──────────────────────────┘   │
│                         │                               │
│                    [Type Erasure]                       │
│                         │                               │
│  ┌──────────────────────▼──────────────────────────┐   │
│  │            Raw Class (JVM Bytecode)             │   │
│  │              X (T → upper bound)                │   │
│  └─────────────────────────────────────────────────┘   │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## Quick Reference

| Feature | Syntax | Example |
|---------|--------|---------|
| Generic Class | `class Name<T>` | `class Box<T>` |
| Generic Method | `<T> T method(T param)` | `<T> List<T> asList(T a)` |
| Bounded Type | `<T extends Upper>` | `<T extends Comparable<T>>` |
| Wildcard | `<?>` | `List<?>` |
| Upper Bounded | `<? extends T>` | `List<? extends Number>` |
| Lower Bounded | `<? super T>` | `List<? super Integer>` |
| Type Erasure | Removed at compile time | `List<String>` → `List` |

## Estimated Time

- **Total:** 18-20 hours
- **Per topic:** 1.5-3 hours
- **Mini project:** 3-4 hours

## Resources

- [Oracle Generics Tutorial](https://docs.oracle.com/en/java/javase/21/java/generics/)
- [Effective Java - Chapter on Generics](https://learning.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Language Specification - Generics](https://docs.oracle.com/javase/specs/jls/se21/html/jls-4.html)

## Performance Comparison

| Operation | Time Complexity | Space | Notes |
|-----------|----------------|-------|-------|
| Generic class instantiation | O(1) | Type parameter | Compile-time only |
| Type erasure | O(1) | None | Happens at compile time |
| Bounded type check | O(1) | None | Compile-time check |
| Wildcard capture | O(1) | None | Compile-time only |
| Generic method call | O(1) | Type inference | Compile-time only |

## Common Patterns

### 1. Builder Pattern with Generics
```java
public class Builder<T> {
    private T value;
    
    public Builder<T> with(T value) {
        this.value = value;
        return this;
    }
    
    public T build() {
        return value;
    }
}
```

### 2. Generic Repository
```java
public interface Repository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(T entity);
}
```

### 3. Type-Safe Heterogeneous Container
```java
public class TypeSafeContainer {
    private final Map<Class<?>, Object> map = new HashMap<>();
    
    public <T> void put(Class<T> type, T value) {
        map.put(type, type.cast(value));
    }
    
    public <T> T get(Class<T> type) {
        return type.cast(map.get(type));
    }
}
```

## Interview Questions

### Q1: What is type erasure?
**Answer:** Type erasure removes generic type information at compile time, converting List<String> to List. This ensures backward compatibility with pre-generics code.

### Q2: Can you create a generic array?
**Answer:** No, you cannot create `new T[]` due to type erasure. Use `Array.newInstance()` or `Object[]` with casting.

### Q3: What is the PECS principle?
**Answer:** Producer Extends, Consumer Super. Use `? extends T` when producing data, `? super T` when consuming.

### Q4: Can you overload methods with different generic types?
**Answer:** No, due to type erasure both methods have the same signature at runtime.

### Q5: What is a reified type?
**Answer:** A type whose type information is available at runtime. Generics are not reified (erased), but arrays and primitives are.

---

**Note:** This module contains comprehensive documentation with 27 sections per topic, including theory, examples, best practices, interview questions, exercises, and assignments.
