# Topic 10: Best Practices

## Table of Contents

1. [Introduction](#1-introduction)
2. [Learning Objectives](#2-learning-objectives)
3. [Prerequisites](#3-prerequisites)
4. [Why This Concept Exists](#4-why-this-concept-exists)
5. [Problem Statement](#5-problem-statement)
6. [Theory](#6-theory)
7. [Internal Working](#7-internal-working)
8. [JVM Perspective](#8-jvm-perspective)
9. [Memory Representation](#9-memory-representation)
10. [Syntax](#10-syntax)
11. [Easy Example](#11-easy-example)
12. [Medium Example](#12-medium-example)
13. [Hard Example](#13-hard-example)
14. [Enterprise Example](#14-enterprise-example)
15. [Performance](#15-performance)
16. [Best Practices](#16-best-practices)
17. [Common Mistakes](#17-common-mistakes)
18. [Pitfalls](#18-pitfalls)
19. [Debugging Tips](#19-debugging-tips)
20. [Comparison Table](#20-comparison-table)
21. [Decision Tree](#21-decision-tree)
22. [Interview Questions](#22-interview-questions)
23. [Exercises](#23-exercises)
24. [Assignments](#24-assignments)
25. [Mini Project](#25-mini-project)
26. [Summary](#26-summary)
27. [References](#27-references)

---

## 1. Introduction

This topic consolidates the best practices for functional programming in Java. Following these guidelines will help you write cleaner, more maintainable, and performant functional code.

### Key Principles

1. **Immutability**: Prefer immutable data and stateless functions
2. **Pure Functions**: Avoid side effects
3. **Composition**: Build complex logic from simple functions
4. **Declarative Style**: Describe what, not how
5. **Type Safety**: Leverage the type system

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Apply functional programming best practices in real code
2. Avoid common functional programming pitfalls
3. Write testable, maintainable functional code
4. Optimize functional code for performance
5. Document functional code effectively

---

## 3. Prerequisites

Before starting this topic, you should have completed:

- Topic 01-09: All functional programming topics

---

## 4. Why This Concept Exists

### The Problem Without Best Practices

Without best practices, functional code can become:
1. **Hard to read**: Complex lambda chains
2. **Bug-prone**: Side effects and mutable state
3. **Slow**: Inefficient use of streams and lambdas
4. **Hard to test**: Dependencies and side effects

### The Solution: Best Practices

Best practices ensure:
1. **Readability**: Clear, concise code
2. **Correctness**: Avoid common bugs
3. **Performance**: Efficient use of features
4. **Testability**: Easy to test

---

## 5. Problem Statement

### Real-World Scenario: Code Review

A team is reviewing functional code and finds:
1. **Overly complex lambdas**: Hard to understand
2. **Side effects**: Breaking stream independence
3. **Inefficient streams**: Unnecessary operations
4. **Missing documentation**: Unclear intent

### Requirements

1. Establish coding standards
2. Provide guidelines for common patterns
3. Document pitfalls to avoid
4. Ensure performance

---

## 6. Theory

### 6.1 Immutability

Prefer immutable data:

```java
// GOOD: Immutable
record Person(String name, int age) {}

// BAD: Mutable
class Person {
    String name;
    int age;
}
```

### 6.2 Pure Functions

Avoid side effects:

```java
// GOOD: Pure function
int add(int a, int b) {
    return a + b;
}

// BAD: Side effect
int total = 0;
void accumulate(int value) {
    total += value;
}
```

### 6.3 Declarative Style

Describe what, not how:

```java
// GOOD: Declarative
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .toList();

// BAD: Imperative
List<String> result = new ArrayList<>();
for (String s : list) {
    if (s.length() > 3) {
        result.add(s.toUpperCase());
    }
}
```

### 6.4 Composition

Build from small functions:

```java
// GOOD: Composed
Function<String, String> process = trim
    .andThen(toLower)
    .andThen(removeSpecial);

// BAD: Monolithic
Function<String, String> process = s -> {
    String trimmed = s.trim();
    String lower = trimmed.toLowerCase();
    String clean = lower.replaceAll("[^a-z0-9\\s]", "");
    return clean;
};
```

---

## 7. Internal Working

### 7.1 Lambda Scoping

Lambdas capture effectively final variables:

```java
void process(List<String> items) {
    String prefix = "ITEM-";  // Effectively final
    
    items.forEach(item -> {
        System.out.println(prefix + item);  // OK
        // prefix = "NEW-";  // Compilation error!
    });
}
```

### 7.2 Stream Independence

Stream operations should be independent:

```java
// BAD: Side effect
int[] counter = {0};
list.stream()
    .filter(item -> {
        counter[0]++;  // Side effect!
        return true;
    })
    .toList();

// GOOD: Use peek for side effects
list.stream()
    .filter(item -> true)
    .peek(item -> counter[0]++)  // Still bad, but better
    .toList();
```

### 7.3 Lazy Evaluation

Leverage lazy evaluation:

```java
// GOOD: Filter early
list.stream()
    .filter(expensivePredicate)  // Runs first
    .map(cheapMapper)  // Only runs on filtered elements
    .toList();

// BAD: Map first
list.stream()
    .map(cheapMapper)  // Runs on all elements
    .filter(expensivePredicate)  // Then filters
    .toList();
```

---

## 8. JVM Perspective

### 8.1 Lambda Optimization

The JVM optimizes lambdas:

1. **Inlining**: Small lambdas are inlined
2. **Caching**: Lambda objects are cached
3. **Escape analysis**: Lambdas may be stack-allocated

### 8.2 Stream Optimization

The JVM optimizes streams:

1. **Loop fusion**: Multiple operations are combined
2. **Short-circuiting**: Early termination
3. **Parallelization**: Automatic thread management

---

## 9. Memory Representation

### 9.1 Lambda Memory

Lambdas have minimal memory overhead:

```
Lambda Object:
┌─────────────────────────────────────┐
│  Header                             │
├─────────────────────────────────────┤
│  Captured variables (references)    │
│  Method handle                      │
└─────────────────────────────────────┘
```

### 9.2 Stream Memory

Streams create temporary objects:

```
Stream Pipeline:
┌─────────────────────────────────────┐
│  Source reference                    │
│  Pipeline stages (linked list)      │
│  Operation functions                │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Lambda Best Practices

```java
// GOOD: Short lambda
list.stream()
    .filter(s -> s.length() > 3)
    .toList();

// GOOD: Method reference
list.stream()
    .map(String::toUpperCase)
    .toList();

// BAD: Long lambda
list.stream()
    .filter(s -> {
        if (s == null) return false;
        if (s.isEmpty()) return false;
        if (s.length() < 3) return false;
        return true;
    })
    .toList();
```

### 10.2 Stream Best Practices

```java
// GOOD: Filter before map
list.stream()
    .filter(item -> item.getPrice() > 100)
    .map(Item::getName)
    .toList();

// GOOD: Use toList()
list.stream()
    .filter(...)
    .toList();

// BAD: Use collect(Collectors.toList())
list.stream()
    .filter(...)
    .collect(Collectors.toList());
```

---

## 11. Easy Example

### Example 1: Lambda Best Practices

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaBestPractices {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // GOOD: Short lambda
        List<String> longNames = names.stream()
            .filter(s -> s.length() > 3)
            .toList();
        System.out.println("Long names: " + longNames);
        
        // GOOD: Method reference
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("Uppercase: " + upperNames);
        
        // GOOD: Named predicate
        Predicate<String> isLong = s -> s.length() > 3;
        List<String> longNamesNamed = names.stream()
            .filter(isLong)
            .toList();
        System.out.println("Long names (named): " + longNamesNamed);
    }
}
```

### Example 2: Stream Best Practices

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Arrays;
import java.util.List;

public class StreamBestPractices {
    
    record Product(String name, double price, int stock) {}
    
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99, 10),
            new Product("Phone", 699.99, 25),
            new Product("Tablet", 299.99, 5),
            new Product("Headphones", 199.99, 30)
        );
        
        // GOOD: Filter before map
        List<String> affordableProducts = products.stream()
            .filter(p -> p.price() < 500)

---

[📖 Continue to Part 2](README-part2.md)
