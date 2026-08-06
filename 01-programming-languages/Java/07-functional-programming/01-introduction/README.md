# Topic 01: Introduction to Functional Programming

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

Functional Programming (FP) is a programming approach that treats computation as the evaluation of mathematical functions. Unlike Object-Oriented Programming (OOP), which organizes code around objects and their mutable state, FP emphasizes immutability, pure functions, and declarative code.

Java 8 introduced first-class support for functional programming with lambda expressions and the Stream API. Java 21 continues this evolution with pattern matching for `switch` expressions, record patterns, and enhanced string templates that further reduce boilerplate code.

### Key Characteristics of Functional Programming

| Characteristic | Description |
|----------------|-------------|
| **Immutability** | Data cannot be modified after creation |
| **Pure Functions** | Same input always produces same output, no side effects |
| **First-Class Functions** | Functions can be passed as arguments and returned as values |
| **Declarative Style** | Describe *what* to do, not *how* to do it |
| **Referential Transparency** | Function calls can be replaced with their return values |

### The Java 21 Functional Landscape

Java 21 is an LTS (Long-Term Support) release that solidifies several functional features:

- **Lambda expressions** (Java 8+) — anonymous functions
- **Method references** (Java 8+) — shorthand for lambdas
- **Stream API** (Java 8+) — declarative data processing
- **Optional** (Java 8+) — null-safe value containers
- **Records** (Java 14+) — immutable data carriers
- **Sealed classes** (Java 17+) — restricted inheritance
- **Pattern matching for switch** (Java 21) — cleaner conditional logic
- **Record patterns** (Java 21) — destructure records in patterns

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Define functional programming and distinguish it from imperative programming
2. Identify the four core principles of functional programming
3. Explain why Java adopted functional programming features
4. Recognize when to use functional vs. object-oriented approaches
5. Write simple lambda expressions
6. Understand the role of functional interfaces in Java

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Java Basics**: Classes, methods, variables, control flow
- **OOP Concepts**: Inheritance, interfaces, polymorphism
- **Collections Framework**: List, Set, Map, iteration patterns
- **Anonymous Classes**: Creating anonymous implementations of interfaces

---

## 4. Why This Concept Exists

### The Problem with Imperative Programming

Consider processing a list of orders to find total revenue for completed orders:

```java
// Imperative approach
double totalRevenue = 0.0;
for (Order order : orders) {
    if (order.getStatus() == OrderStatus.COMPLETED) {
        totalRevenue += order.getTotalAmount();
    }
}
```

This code has several issues:

1. **Mutable state**: `totalRevenue` is modified in a loop
2. **Manual iteration**: Developer must manage loop mechanics
3. **Hard to parallelize**: The mutable accumulator prevents parallel processing
4. **Verbosity**: The "how" obscures the "what"

### The Functional Solution

```java
// Functional approach
double totalRevenue = orders.stream()
    .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
    .mapToDouble(Order::getTotalAmount)
    .sum();
```

### Benefits Achieved

- **No mutable state**: Each operation returns a new value
- **Declarative**: Clearly states intent (filter, map, sum)
- **Parallelizable**: Can add `.parallel()` with zero code changes
- **Testable**: Each operation is independently testable
- **Composable**: Operations can be chained and reused

---

## 5. Problem Statement

### Real-World Scenario: E-Commerce Order Processing

An e-commerce platform needs to process millions of daily orders. The current imperative codebase suffers from:

- **Bug-prone code**: Mutable state leads to race conditions
- **Difficult maintenance**: Complex nested loops and conditionals
- **Poor performance**: Sequential processing cannot use multi-core CPUs
- **Testing challenges**: Side effects make unit testing difficult

### Requirements

1. Process orders declaratively (filter by status, calculate totals)
2. Support both sequential and parallel processing
3. Eliminate null pointer exceptions through safe value handling
4. Enable function composition for building complex transformations
5. Maintain readability while reducing boilerplate

---

## 6. Theory

### 6.1 Lambda Calculus Foundation

Functional programming is rooted in lambda calculus, a formal system developed by Alonzo Church in the 1930s. Lambda calculus provides a mathematical foundation for expressing computation using:

- **Variable binding**: `x` (a variable)
- **Abstraction**: `λx. body` (a function)
- **Application**: `(λx. body) argument` (function call)

Java's lambda expressions map directly to these concepts:

```java
// Lambda abstraction: λx. x + 1
// Java equivalent:
x -> x + 1
```

### 6.2 Pure Functions

A pure function has two properties:

1. **Deterministic**: Same input always produces same output
2. **No side effects**: Doesn't modify external state

```java
// Pure function
int add(int a, int b) {
    return a + b;
}

// Impure function (side effect)
int total = 0;
void accumulate(int value) {
    total += value; // modifies external state
}
```

### 6.3 Immutability

Immutable data cannot be changed after creation. Java provides several mechanisms:

```java
// Records (Java 14+) - immutable by default
public record Point(double x, double y) {}

// Final fields
public class ImmutablePoint {
    private final double x;
    private final double y;
    
    public ImmutablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

// Collections.unmodifiableList
List<String> names = List.of("Alice", "Bob", "Charlie");
```

### 6.4 Higher-Order Functions

Functions that accept or return other functions:

```java
// Function that accepts a function
<T> List<T> filter(List<T> list, Predicate<T> predicate) {
    return list.stream()
        .filter(predicate)
        .collect(Collectors.toList());
}

// Function that returns a function
Function<Integer, Integer> multiplier(int factor) {
    return x -> x * factor;
}
```

### 6.5 Referential Transparency

An expression is referentially transparent if it can be replaced with its value without changing the program's behavior:

```java
// Referentially transparent
int x = add(2, 3); // can be replaced with 5

// Not referentially transparent
int x = counter++; // counter changes each time
```

---

## 7. Internal Working

### 7.1 How Java Implements Lambdas

Java implements lambda expressions using **invokedynamic** (introduced in Java 7). When the compiler encounters a lambda, it:

1. Creates a synthetic method containing the lambda body
2. Generates an invokedynamic call site
3. Uses a **LambdaMetafactory** to create the functional interface implementation at runtime

```java
// Source code
Function<String, Integer> length = String::length;

// Compiler generates (conceptually):
// 1. A static method: static int lambda$main$0(String s) { return s.length(); }
// 2. An invokedynamic call that creates the Function implementation
```

### 7.2 Method Resolution

When a lambda or method reference is compiled:

1. The compiler identifies the target functional interface
2. Determines the SAM (Single Abstract Method) signature
3. Validates that the lambda/method reference is compatible
4. Generates bytecode using invokedynamic

```
Lambda Expression → Compiler → Synthetic Method + InvokeDynamic → LambdaMetafactory → Functional Interface Implementation
```

### 7.3 Type Inference

Java uses **target typing** to infer the type of lambda expressions:

```java
// The compiler knows this must be a Predicate<String>
Predicate<String> isLong = s -> s.length() > 10;

// The compiler infers the parameter type from context
list.stream()
    .filter(s -> s.length() > 10)  // s is inferred as String
    .forEach(System.out::println);
```

---

## 8. JVM Perspective

### 8.1 Bytecode Generation

Lambda expressions generate different bytecode than anonymous classes:

| Aspect | Anonymous Class | Lambda |
|--------|----------------|--------|
| **File** | Separate .class file | Same .class file |
| **Instantiation** | `new` keyword | `invokedynamic` |
| **Performance** | Class loading overhead | Lazy instantiation |
| **Memory** | One class per anonymous class | Shared proxy class |

### 8.2 InvokeDynamic Benefits

The `invokedynamic` instruction provides:

1. **Lazy class generation**: The functional interface implementation is created on first use
2. **JVM optimization**: The JVM can optimize call sites based on actual types
3. **Reduced class loading**: No separate class files for each lambda
4. **Better inlining**: JIT compiler can inline lambda bodies more effectively

### 8.3 LambdaMetafactory

`LambdaMetafactory` is the bootstrap method that creates lambda implementations:

```java
// Bootstrap method signature
public static CallSite metafactory(
    MethodHandles.Lookup caller,
    String invokedName,
    MethodType invokedType,
    MethodType samMethodType,
    MethodHandle implMethod,
    MethodType instantiatedMethodType
)
```

---

## 9. Memory Representation

### 9.1 Lambda vs Anonymous Class Memory

```
Anonymous Class:
┌─────────────────────────────┐
│ OrderProcessor$1.class      │  ← Separate class file
├─────────────────────────────┤
│ - Enclosing class reference │
│ - Captured variables         │
│ - Method: process()         │
└─────────────────────────────┘

Lambda:
┌─────────────────────────────┐
│ OrderProcessor.class        │  ← Same class file
├─────────────────────────────┤
│ - Synthetic method          │  ← Lambda body
│ - InvokeDynamic call site   │  ← Factory reference
└─────────────────────────────┘
```

### 9.2 Variable Capture

Lambdas can capture variables from their enclosing scope:

```java
void processOrders(List<Order> orders) {
    String prefix = "ORDER-"; // effectively final
    
    // Lambda captures 'prefix' variable
    orders.forEach(order -> 
        System.out.println(prefix + order.getId())
    );
}
```

**Memory Impact**: Captured variables are copied into the lambda object. For reference types, only the reference is copied (not the object).

### 9.3 Method Handle Caching

The JVM caches method handles for frequently used lambdas:

```
First invocation:
  LambdaMetafactory → creates implementation class → instantiates

Subsequent invocations:
  Cached implementation → direct instantiation (no metafactory)
```

---

## 10. Syntax

### 10.1 Lambda Expression Syntax

```java
// Full syntax
(parameters) -> expression

// With block body
(parameters) -> { statements; }

// Examples

---

[📖 Continue to Part 2](README-part2.md)
```
