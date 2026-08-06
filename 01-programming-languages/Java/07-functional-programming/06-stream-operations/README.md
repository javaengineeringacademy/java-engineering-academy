# Topic 06: Stream Operations

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

Stream operations are the building blocks of stream pipelines. They transform, filter, and aggregate data as it flows through the pipeline. Understanding the full catalog of stream operations is essential for writing efficient and expressive data processing code.

Stream operations are divided into two categories:
- **Intermediate Operations**: Transform a stream into another stream (lazy)
- **Terminal Operations**: Produce a result or side effect (trigger processing)

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Master all intermediate operations (filter, map, flatMap, sorted, distinct, peek, limit, skip)
2. Master all terminal operations (collect, reduce, forEach, count, min, max, findFirst, findAny, anyMatch, allMatch, noneMatch)
3. Understand stateful vs stateless operations
4. Apply short-circuit operations correctly
5. Build complex stream pipelines
6. Optimize stream performance

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Stream API Basics**: Creating streams (Topic 05)
- **Lambda Expressions**: Writing stream operations
- **Functional Interfaces**: Predicate, Function, Consumer

---

## 4. Why This Concept Exists

### The Problem with Limited Operations

Without a detailed set of stream operations, developers must:
1. Write manual loops for simple transformations
2. Implement custom logic for common patterns
3. Reuse the same boilerplate code repeatedly

### The Solution: Rich Operation Catalog

Java's Stream API provides a rich set of operations that:
1. Cover common data processing patterns
2. Enable declarative programming
3. Support lazy evaluation and optimization
4. Enable parallel processing

---

## 5. Problem Statement

### Real-World Scenario: Data Analytics Platform

A data analytics platform needs to process millions of records:
- **Filter** records by multiple criteria
- **Transform** data into different formats
- **Aggregate** statistics
- **Find** specific records
- **Sort** and **limit** results

### Requirements

1. Support complex filtering logic
2. Enable nested data flattening
3. Provide efficient aggregation
4. Support short-circuiting for performance
5. Enable debugging and inspection

---

## 6. Theory

### 6.1 Intermediate Operations

#### filter(Predicate<T> predicate)
Selects elements matching a predicate:

```java
Stream<T> filter(Predicate<? super T> predicate)
```

- **Stateless**: Each element is processed independently
- **Lazy**: Processing is deferred until terminal operation

#### map(Function<T, R> mapper)
Transforms each element:

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper)
```

- **Stateless**: Each element is processed independently
- **Returns**: New stream with transformed elements

#### flatMap(Function<T, Stream<R>> mapper)
Flattens nested streams:

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
```

- **Stateless**: Each element is processed independently
- **Use case**: Flattening collections of collections

#### distinct()
Removes duplicate elements:

```java
Stream<T> distinct()
```

- **Stateful**: Requires tracking seen elements
- **Uses**: `equals()` and `hashCode()` for comparison

#### sorted() / sorted(Comparator<T> comparator)
Sorts elements:

```java
Stream<T> sorted()
Stream<T> sorted(Comparator<? super T> comparator)
```

- **Stateful**: Requires buffering all elements
- **Uses**: Natural ordering or custom comparator

#### peek(Consumer<T> action)
Inspects elements without modifying them:

```java
Stream<T> peek(Consumer<? super T> action)
```

- **Stateless**: Each element is processed independently
- **Use case**: Debugging and logging

#### limit(long maxSize)
Takes first n elements:

```java
Stream<T> limit(long maxSize)
```

- **Stateful**: Tracks count of taken elements
- **Short-circuit**: Stops after n elements

#### skip(long n)
Skips first n elements:

```java
Stream<T> skip(long n)
```

- **Stateful**: Tracks count of skipped elements

### 6.2 Terminal Operations

#### collect(Collector<T, A, R> collector)
Accumulates elements into a collection:

```java
<R, A> R collect(Collector<? super T, A, R> collector)
```

- **Trigger**: Starts stream processing
- **Flexible**: Supports custom collection strategies

#### forEach(Consumer<T> action)
Performs action for each element:

```java
void forEach(Consumer<? super T> action)
```

- **Trigger**: Starts stream processing
- **Side effect**: May have side effects

#### reduce(BinaryOperator<T> accumulator)
Combines elements into a single value:

```java
Optional<T> reduce(BinaryOperator<T> accumulator)
T reduce(T identity, BinaryOperator<T> accumulator)
```

- **Trigger**: Starts stream processing
- **Flexible**: Supports identity value

#### count()
Counts elements:

```java
long count()
```

- **Trigger**: Starts stream processing
- **Returns**: Number of elements

#### anyMatch(Predicate<T> predicate)
Checks if any element matches:

```java
boolean anyMatch(Predicate<? super T> predicate)
```

- **Trigger**: Starts stream processing
- **Short-circuit**: Stops on first match

#### allMatch(Predicate<T> predicate)
Checks if all elements match:

```java
boolean allMatch(Predicate<? super T> predicate)
```

- **Trigger**: Starts stream processing
- **Short-circuit**: Stops on first mismatch

#### noneMatch(Predicate<T> predicate)
Checks if no element matches:

```java
boolean noneMatch(Predicate<? super T> predicate)
```

- **Trigger**: Starts stream processing
- **Short-circuit**: Stops on first match

#### findFirst() / findAny()
Finds first or any matching element:

```java
Optional<T> findFirst()
Optional<T> findAny()
```

- **Trigger**: Starts stream processing
- **Short-circuit**: Stops on first match

#### min(Comparator) / max(Comparator)
Finds minimum or maximum element:

```java
Optional<T> min(Comparator<? super T> comparator)
Optional<T> max(Comparator<? super T> comparator)
```

- **Trigger**: Starts stream processing

### 6.3 Stateful vs Stateless Operations

| Operation | Stateful/Stateless | Description |
|-----------|-------------------|-------------|
| `filter` | Stateless | Each element processed independently |
| `map` | Stateless | Each element processed independently |
| `flatMap` | Stateless | Each element processed independently |
| `distinct` | Stateful | Requires tracking seen elements |
| `sorted` | Stateful | Requires buffering all elements |
| `limit` | Stateful | Tracks count of taken elements |
| `skip` | Stateful | Tracks count of skipped elements |
| `peek` | Stateless | Each element processed independently |

---

## 7. Internal Working

### 7.1 Pipeline Execution

When a terminal operation is invoked:

1. The stream source provides elements
2. Each intermediate operation transforms elements
3. The terminal operation consumes elements
4. Results are collected/produced

```
Source → Stage 1 → Stage 2 → ... → Terminal → Result
```

### 7.2 Short-Circuiting

Some operations stop processing early:

- `limit(n)`: Stops after n elements
- `findFirst()`: Stops on first match
- `findAny()`: Stops on first match
- `anyMatch()`: Stops on first match
- `allMatch()`: Stops on first mismatch
- `noneMatch()`: Stops on first match

### 7.3 Lazy Evaluation

Intermediate operations are lazy:

```java
Stream<Integer> stream = list.stream()
    .filter(n -> {
        System.out.println("Filter: " + n);
        return n > 5;
    })
    .map(n -> {
        System.out.println("Map: " + n);
        return n * 2;
    });

// Nothing prints here
// Processing happens only when terminal operation is invoked
List<Integer> result = stream.toList();
```

---

## 8. JVM Perspective

### 8.1 Stream Implementation Classes

Each operation creates a new Stream implementation:

```
list.stream()           → ReferencePipeline$Head
    .filter(...)        → ReferencePipeline$StatelessOp
    .map(...)           → ReferencePipeline$StatelessOp
    .sorted(...)        → ReferencePipeline$StatefulOp
    .toList()           → ArrayList (result)
```

### 8.2 Operation Chaining

Operations are chained via linked list:

```
Head → StatelessOp → StatelessOp → StatefulOp → Terminal
```

### 8.3 JIT Optimization

The JIT compiler optimizes stream pipelines:

1. **Inlining**: Small operations are inlined
2. **Loop fusion**: Multiple operations are combined
3. **Vectorization**: Primitive streams can use SIMD instructions

---

## 9. Memory Representation

### 9.1 Stream Pipeline Memory

```
Stream Pipeline:
┌─────────────────────────────────────┐
│  Source Spliterator                  │
├─────────────────────────────────────┤
│  Stage 1: filter (Predicate)        │
├─────────────────────────────────────┤
│  Stage 2: map (Function)           │
├─────────────────────────────────────┤
│  Stage 3: sorted (Comparator)      │
└─────────────────────────────────────┘
```

### 9.2 Memory Usage

- **Source**: Depends on source (Collection, array, etc.)

---

[📖 Continue to Part 2](README-part2.md)
