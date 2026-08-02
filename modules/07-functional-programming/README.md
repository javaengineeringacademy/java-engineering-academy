# Module 07: Functional Programming in Java

## Overview

This module provides a comprehensive deep-dive into Functional Programming (FP) concepts in Java 21. You will master lambda expressions, functional interfaces, method references, the Stream API, Collectors, Optional, and function composition. Through progressive examples—from simple syntax to enterprise-grade patterns—you will learn how to write cleaner, more testable, and highly concurrent code using Java's functional paradigms.

## Why This Concept Exists

Functional programming solves imperative programming problems:
- Mutable state leads to bugs
- Side effects make code unpredictable
- Sequential execution limits parallelism
- Code duplication for similar operations

Functional programming provides:
- Immutable data
- Pure functions
- Higher-order functions
- Declarative style

## Status

✅ **Complete** — Full content implementation

## Learning Objectives

By the end of this module, you will be able to:

- [ ] Explain the core principles of functional programming and how Java implements them
- [ ] Write lambda expressions with correct scoping and variable capture rules
- [ ] Identify and use built-in functional interfaces (Predicate, Function, Consumer, Supplier)
- [ ] Create custom functional interfaces with default and static methods
- [ ] Apply method references to simplify lambda expressions
- [ ] Build efficient Stream pipelines for data processing
- [ ] Use parallel streams correctly and understand ForkJoinPool behavior
- [ ] Implement custom Collectors for complex aggregation tasks
- [ ] Apply Optional to eliminate null-related bugs
- [ ] Compose functions using andThen, compose, andThenApply patterns
- [ ] Avoid common functional programming pitfalls in production code

## Prerequisites

- Module 01: Java Fundamentals (variables, types, control flow)
- Module 02: Object-Oriented Programming (classes, interfaces, inheritance)
- Module 03: Exception Handling
- Module 04: Collections Framework
- Module 05: Generics
- Module 06: Java I/O and NIO

## Topics

| # | Topic | Duration | Difficulty | Description |
|---|-------|----------|------------|-------------|
| 01 | [Introduction](01-introduction/) | 1.5 hours | Beginner | What is functional programming |
| 02 | [Lambda Expressions](02-lambda-expressions/) | 2.5 hours | Beginner | Syntax and scoping |
| 03 | [Functional Interfaces](03-functional-interfaces/) | 2 hours | Intermediate | Built-in and custom interfaces |
| 04 | [Method References](04-method-references/) | 1.5 hours | Intermediate | Simplifying lambdas |
| 05 | [Stream API](05-stream-api/) | 2.5 hours | Intermediate | Creating streams |
| 06 | [Stream Operations](06-stream-operations/) | 3 hours | Intermediate | Intermediate and terminal ops |
| 07 | [Collectors](07-collectors/) | 2.5 hours | Advanced | Custom aggregation |
| 08 | [Optional](08-optional/) | 2 hours | Intermediate | Null-safe processing |
| 09 | [Function Composition](09-composition/) | 2 hours | Advanced | Composing functions |
| 10 | [Best Practices](10-best-practices/) | 1.5 hours | Intermediate | Production guidelines |
| 11 | [Mini Project](11-mini-project/) | 3 hours | Advanced | Functional Data Pipeline |

**Total Estimated Time: 24 hours**

## Difficulty Progression

- **Beginner** (Topics 01-02): Core concepts and basic syntax
- **Intermediate** (Topics 03-08): Advanced features and patterns
- **Advanced** (Topics 09-11): Real-world applications and projects

## Learning Path

```
Introduction → Lambda → Functional Interfaces → Method References
      ↓                                              ↓
Best Practices ← Composition ← Optional ← Collectors ← Stream Ops ← Stream API
      ↓
Mini Project
```

## Key Concepts

### Lambda Expressions

```java
// Basic lambda
Comparator<String> comp = (a, b) -> a.compareTo(b);

// With type inference
comp = (String a, String b) -> a.compareTo(b);

// Multi-line lambda
Function<String, Integer> length = s -> {
    int len = s.length();
    return len;
};
```

### Functional Interfaces

| Interface | Input | Output | Description |
|-----------|-------|--------|-------------|
| `Predicate<T>` | T | boolean | Tests condition |
| `Function<T, R>` | T | R | Transforms value |
| `Consumer<T>` | T | void | Consumes value |
| `Supplier<T>` | None | T | Provides value |
| `UnaryOperator<T>` | T | T | Transforms same type |
| `BiFunction<T, U, R>` | T, U | R | Two inputs |

### Method References

```java
// Lambda
Function<String, Integer> len = s -> s.length();

// Method reference
Function<String, Integer> len = String::length;

// Constructor reference
Supplier<List<String>> list = ArrayList::new;
```

### Stream Operations

```java
List<String> result = names.stream()
    .filter(name -> name.length() > 3)        // Intermediate
    .map(String::toUpperCase)                  // Intermediate
    .sorted()                                  // Intermediate
    .collect(Collectors.toList());             // Terminal
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│              Functional Programming Flow                 │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  ┌──────────┐     ┌──────────┐     ┌──────────┐        │
│  │  Source   │────▶│  Stream  │────▶│  Result  │        │
│  │ (List,    │     │ Pipeline │     │  (List,  │        │
│  │  Array)   │     │          │     │  Value)  │        │
│  └──────────┘     └──────────┘     └──────────┘        │
│                       │                                 │
│            ┌──────────┴──────────┐                      │
│            │                     │                      │
│      ┌─────▼─────┐         ┌─────▼─────┐               │
│      │Intermediate│         │ Terminal  │               │
│      │ Operations │         │ Operations│               │
│      ├───────────┤         ├───────────┤               │
│      │ filter()  │         │ collect() │               │
│      │ map()     │         │ reduce()  │               │
│      │ sorted()  │         │ forEach() │               │
│      │ distinct()│         │ count()   │               │
│      │ limit()   │         │ findFirst()│              │
│      └───────────┘         └───────────┘               │
│                                                         │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│              Functional Interface Hierarchy              │
├─────────────────────────────────────────────────────────┤
│                                                         │
│                    FunctionalInterface                   │
│                           │                             │
│         ┌─────────────────┼─────────────────┐           │
│         │                 │                 │           │
│    ┌────▼────┐      ┌────▼────┐      ┌────▼────┐       │
│    │Predicate│      │Function │      │Consumer │       │
│    │  T→bool │      │  T→R    │      │  T→void │       │
│    └─────────┘      └─────────┘      └─────────┘       │
│         │                 │                 │           │
│         │           ┌─────┴─────┐           │           │
│         │           │           │           │           │
│    ┌────▼────┐ ┌────▼────┐ ┌────▼────┐ ┌────▼────┐    │
│    │BiPredic│ │BiFunction│ │BiConsumer│ │Supplier │    │
│    │T,U→bool│ │ T,U→R   │ │ T,U→void│ │  ()→T   │    │
│    └─────────┘ └─────────┘ └─────────┘ └─────────┘    │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

## Module Structure

```
07-functional-programming/
├── README.md                          # This file
├── src/main/java/academy/javaengineering/functional/
│   ├── introduction/                  # Topic 01 source files
│   ├── lambda/                        # Topic 02 source files
│   ├── interfaces/                    # Topic 03 source files
│   ├── references/                    # Topic 04 source files
│   ├── streams/                       # Topic 05 source files
│   ├── operations/                    # Topic 06 source files
│   ├── collectors/                    # Topic 07 source files
│   ├── optional/                      # Topic 08 source files
│   ├── composition/                   # Topic 09 source files
│   ├── bestpractices/                 # Topic 10 source files
│   └── project/                       # Topic 11 mini project files
├── 01-introduction/
│   └── README.md
├── 02-lambda-expressions/
│   └── README.md
├── 03-functional-interfaces/
│   └── README.md
├── 04-method-references/
│   └── README.md
├── 05-stream-api/
│   └── README.md
├── 06-stream-operations/
│   └── README.md
├── 07-collectors/
│   └── README.md
├── 08-optional/
│   └── README.md
├── 09-composition/
│   └── README.md
├── 10-best-practices/
│   └── README.md
└── 11-mini-project/
    └── README.md
```

## Performance Comparison

| Operation | Time Complexity | Parallel | Notes |
|-----------|----------------|----------|-------|
| filter() | O(n) | Yes | Lazy evaluation |
| map() | O(n) | Yes | 1:1 transformation |
| reduce() | O(n) | Yes | Combines elements |
| collect() | O(n) | Yes | Terminal operation |
| sorted() | O(n log n) | Yes | Requires full stream |
| distinct() | O(n) | Yes | Uses HashSet |
| parallelStream() | Varies | Yes | ForkJoinPool |

## Common Patterns

### 1. Pipeline Pattern
```java
List<String> result = orders.stream()
    .filter(order -> order.getStatus() == OrderStatus.ACTIVE)
    .map(Order::getCustomerName)
    .distinct()
    .sorted()
    .collect(Collectors.toList());
```

### 2. Function Composition
```java
Function<String, String> trim = String::trim;
Function<String, String> lower = String::toLowerCase;
Function<String, String> clean = trim.andThen(lower);

String cleaned = clean.apply("  Hello  ");
```

### 3. Optional Handling
```java
String result = Optional.ofNullable(user)
    .map(User::getAddress)
    .map(Address::getCity)
    .orElse("Unknown");
```

## Interview Questions

### Q1: What is the difference between map and flatMap?
**Answer:** map transforms each element 1:1. flatMap transforms each element to a stream and flattens.

### Q2: What is lazy evaluation?
**Answer:** Intermediate operations are not executed until a terminal operation is invoked.

### Q3: When should you use parallel streams?
**Answer:** For large datasets with CPU-bound operations. Avoid for small datasets or I/O-bound work.

### Q4: What is a spliterator?
**Answer:** An iterator designed for parallel traversal and element partitioning.

### Q5: How do you handle exceptions in streams?
**Answer:** Wrap in try-catch inside map/flatMap, or create custom utility methods.

## Resources

- [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
- [Oracle Java Tutorials: Streams](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/stream/package-summary.html)
- [Effective Java, 3rd Edition - Item 42-44: Functional Programming](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java 21 JEPs](https://openjdk.org/projects/jdk/21/)

---

**Note:** This module contains comprehensive documentation with 27 sections per topic, including theory, examples, best practices, interview questions, exercises, and assignments.
