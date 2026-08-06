# Topic 09: Function Composition

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

Function composition is the process of combining two or more functions to produce a new function. In Java, function composition is achieved through default methods on functional interfaces like `Function`, `Predicate`, and `UnaryOperator`.

Composition enables building complex transformations from simple, reusable functions. This promotes:
- **Modularity**: Small, focused functions
- **Reusability**: Compose functions in different ways
- **Readability**: Declarative function pipelines
- **Testability**: Test individual functions independently

### Composition Methods

| Method | Description | Order |
|--------|-------------|-------|
| `andThen(after)` | Apply this function first, then `after` | this → after |
| `compose(before)` | Apply `before` first, then this function | before → this |

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Understand function composition principles
2. Apply `andThen()` and `compose()` methods
3. Build complex transformations from simple functions
4. Use predicate composition (and, or, negate)
5. Create function pipelines
6. Apply composition in enterprise patterns

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Lambda Expressions**: Basic syntax (Topic 02)
- **Functional Interfaces**: Function, Predicate, Consumer (Topic 03)
- **Method References**: Shorthand for lambdas (Topic 04)

---

## 4. Why This Concept Exists

### The Problem Without Composition

Without function composition, developers must:
1. Write monolithic functions that do everything
2. Duplicate transformation logic
3. Create complex, hard-to-test methods

```java
// Monolithic function
Function<Order, String> processOrder = order -> {
    String status = order.status();
    String customer = order.customer();
    double amount = order.amount();
    // ... complex logic
    return result;
};
```

### The Composition Solution

```java
// Composable functions
Function<Order, String> getStatus = Order::status;
Function<Order, String> getCustomer = Order::customer;
Function<Order, Double> getAmount = Order::amount;

Function<Order, String> processOrder = order ->
    getStatus.apply(order) + " - " + getCustomer.apply(order);
```

---

## 5. Problem Statement

### Real-World Scenario: Data Transformation Pipeline

A data processing system needs to:
- **Transform** data through multiple steps
- **Reuse** transformation logic
- **Build** complex pipelines from simple functions
- **Test** each transformation independently

### Requirements

1. Compose functions declaratively
2. Support both andThen and compose
3. Enable predicate composition
4. Provide type safety
5. Support parallel composition

---

## 6. Theory

### 6.1 Function Composition with andThen

`andThen` applies the current function first, then the argument:

```java
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
// doubleThenAdd.apply(5) → 5 * 2 + 10 = 20
```

### 6.2 Function Composition with compose

`compose` applies the argument first, then the current function:

```java
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
// addThenDouble.apply(5) → (5 + 10) * 2 = 30
```

### 6.3 Predicate Composition

Predicates support `and`, `or`, and `negate`:

```java
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEven = n -> n % 2 == 0;

Predicate<Integer> isPositiveEven = isPositive.and(isEven);
Predicate<Integer> isPositiveOrEven = isPositive.or(isEven);
Predicate<Integer> isNotPositive = isPositive.negate();
```

### 6.4 Consumer Composition

Consumers support `andThen`:

```java
Consumer<String> print = System.out::println;
Consumer<String> log = s -> System.out.println("LOG: " + s);

Consumer<String> printAndLog = print.andThen(log);
```

### 6.5 UnaryOperator Composition

UnaryOperators support `andThen` and `compose`:

```java
UnaryOperator<String> trim = String::trim;
UnaryOperator<String> toLower = String::toLowerCase;

UnaryOperator<String> process = trim.andThen(toLower);
```

---

## 7. Internal Working

### 7.1 Composition Implementation

`andThen` creates a new function that chains operations:

```java
default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return t -> after.apply(this.apply(t));
}
```

### 7.2 Composition Chain

Composition creates a linked list of functions:

```
Function1.andThen(Function2).andThen(Function3)
↓
Function1 → Function2 → Function3
```

### 7.3 Type Safety

Composition maintains type safety through generics:

```java
Function<String, Integer> toLength = String::length;
Function<Integer, String> toString = Object::toString;

Function<String, String> pipeline = toLength.andThen(toString);
// Type: String → Integer → String
```

---

## 8. JVM Perspective

### 8.1 Composition Object Layout

Each composition creates a new function object:

```
Function.andThen(other):
┌─────────────────────────────────────┐
│  Header                             │
├─────────────────────────────────────┤
│  this function reference            │
│  after function reference           │
└─────────────────────────────────────┘
```

### 8.2 Method Dispatch

Composition uses virtual method dispatch:

```
pipeline.apply(input)
→ this.apply(input)  // First function
→ after.apply(result)  // Second function
```

### 8.3 JIT Optimization

The JIT compiler can optimize composition:

1. **Inlining**: Small functions are inlined
2. **Loop fusion**: Multiple compositions are combined
3. **Escape analysis**: Composition objects may be stack-allocated

---

## 9. Memory Representation

### 9.1 Composition Chain Memory

```
Function1.andThen(Function2).andThen(Function3):

Memory:
┌─────────────┐
│  Composition│
├─────────────┤
│  Function1  │
│  Function2  │
└─────────────┘
      ↓
┌─────────────┐
│  Composition│
├─────────────┤
│  Previous   │
│  Function3  │
└─────────────┘
```

### 9.2 Memory Usage

- **Single function**: ~16 bytes
- **Composition**: ~16 bytes per composition
- **Chain**: Proportional to chain length

---

## 10. Syntax

### 10.1 Function Composition

```java
// andThen: this → after
Function<T, R> composed = function1.andThen(function2);

// compose: before → this
Function<T, R> composed = function1.compose(function2);

// Chain
Function<T, V> pipeline = function1
    .andThen(function2)
    .andThen(function3);
```

### 10.2 Predicate Composition

```java
// and
Predicate<T> combined = predicate1.and(predicate2);

// or
Predicate<T> combined = predicate1.or(predicate2);

// negate
Predicate<T> negated = predicate.negate();
```

### 10.3 Consumer Composition

```java
// andThen
Consumer<T> combined = consumer1.andThen(consumer2);
```

---

## 11. Easy Example

### Example 1: Basic Function Composition

```java
package academy.javaengineering.functional.composition;

import java.util.function.Function;

public class BasicComposition {
    public static void main(String[] args) {
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        
        // andThen: double first, then add 10
        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
        System.out.println("5 double then add 10: " + doubleThenAdd.apply(5));
        
        // compose: add 10 first, then double
        Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
        System.out.println("5 add 10 then double: " + addThenDouble.apply(5));
        
        // Chain
        Function<Integer, Integer> pipeline = doubleIt
            .andThen(addTen)
            .andThen(x -> x * x);
        System.out.println("5 pipeline: " + pipeline.apply(5));
    }
}
```

### Example 2: Predicate Composition

```java
package academy.javaengineering.functional.composition;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateComposition {
    public static void main(String[] args) {
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isLessThan100 = n -> n < 100;
        
        // and
        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        System.out.println("5 is positive even: " + isPositiveEven.test(5));
        System.out.println("6 is positive even: " + isPositiveEven.test(6));
        
        // or
        Predicate<Integer> isSmallOrEven = isLessThan100.or(isEven);
        System.out.println("-5 is small or even: " + isSmallOrEven.test(-5));
        
        // negate
        Predicate<Integer> isNotPositive = isPositive.negate();
        System.out.println("-1 is not positive: " + isNotPositive.test(-1));
        
        List<Integer> numbers = Arrays.asList(-5, 0, 3, 6, 100);
        System.out.println("Positive and even: " + numbers.stream()
            .filter(isPositiveEven)
            .toList());
    }

---

[📖 Continue to Part 2](README-part2.md)
```
