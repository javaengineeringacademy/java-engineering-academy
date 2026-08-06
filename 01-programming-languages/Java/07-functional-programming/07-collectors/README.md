# Topic 07: Collectors

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

Collectors are the terminal operation that accumulates stream elements into a final result. They provide a flexible, composable way to build complex aggregation logic. The `java.util.stream.Collectors` class contains a rich set of predefined collectors for common operations.

Collectors follow the builder pattern: you compose collectors together to build complex accumulation logic. This makes them highly reusable and testable.

### Key Characteristics

| Characteristic | Description |
|----------------|-------------|
| **Composable** | Build complex collectors from simple ones |
| **Reusable** | Collectors can be shared and reused |
| **Type-safe** | Strong type inference |
| **Parallel-friendly** | Work correctly with parallel streams |

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Use built-in collectors (toList, toSet, toMap, joining, groupingBy, partitioningBy)
2. Implement custom collectors
3. Apply downstream collectors for nested aggregation
4. Build complex aggregation pipelines
5. Optimize collector performance
6. Understand collector composition patterns

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Stream API Basics**: Creating and processing streams (Topic 05)
- **Stream Operations**: Intermediate and terminal operations (Topic 06)
- **Functional Interfaces**: Function, Supplier, BinaryOperator

---

## 4. Why This Concept Exists

### The Problem with Manual Accumulation

Without collectors, accumulating stream elements requires:

1. **Manual loops**: Creating and populating collections
2. **Mutable state**: Managing accumulators
3. **Boilerplate**: Repeating collection logic

```java
// Manual accumulation
Map<String, List<Order>> ordersByCustomer = new HashMap<>();
for (Order order : orders) {
    ordersByCustomer.computeIfAbsent(order.customerId(), k -> new ArrayList<>())
        .add(order);
}
```

### The Collector Solution

```java
// Using collectors
Map<String, List<Order>> ordersByCustomer = orders.stream()
    .collect(Collectors.groupingBy(Order::customerId));
```

---

## 5. Problem Statement

### Real-World Scenario: Reporting System

A reporting system needs to:
- **Group** data by categories
- **Aggregate** statistics (sum, count, average)
- **Partition** data by conditions
- **Join** strings
- **Map** keys to values

### Requirements

1. Support complex grouping logic
2. Enable nested aggregation
3. Provide efficient accumulation
4. Support custom accumulation strategies
5. Work with parallel streams

---

## 6. Theory

### 6.1 Built-in Collectors

#### toList() / toUnmodifiableList()

```java
List<T> list = stream.collect(Collectors.toList());
List<T> unmodifiable = stream.collect(Collectors.toUnmodifiableList());
```

#### toSet() / toUnmodifiableSet()

```java
Set<T> set = stream.collect(Collectors.toSet());
Set<T> unmodifiable = stream.collect(Collectors.toUnmodifiableSet());
```

#### toMap()

```java
Map<K, V> map = stream.collect(Collectors.toMap(
    keyMapper,
    valueMapper,
    mergeFunction,  // Optional: handles duplicate keys
    mapFactory     // Optional: specifies map implementation
));
```

#### joining()

```java
String joined = stream.collect(Collectors.joining());
String joinedWithDelimiter = stream.collect(Collectors.joining(", "));
String joinedWithPrefix = stream.collect(Collectors.joining(", ", "[", "]"));
```

#### counting()

```java
long count = stream.collect(Collectors.counting());
// Equivalent to: stream.count()
```

#### summarizingInt/Long/Double()

```java
IntSummaryStatistics stats = stream.collect(Collectors.summarizingInt(Item::getQuantity));
// Returns: count, sum, min, max, average
```

#### minBy() / maxBy()

```java
Optional<T> min = stream.collect(Collectors.minBy(Comparator.naturalOrder()));
Optional<T> max = stream.collect(Collectors.maxBy(Comparator.naturalOrder()));
```

### 6.2 Grouping Collectors

#### groupingBy()

```java
// Simple grouping
Map<K, List<T>> grouped = stream.collect(Collectors.groupingBy(classifier));

// With downstream collector
Map<K, D> grouped = stream.collect(Collectors.groupingBy(
    classifier,
    downstreamCollector
));

// With map factory
Map<K, D> grouped = stream.collect(Collectors.groupingBy(
    classifier,
    mapFactory,
    downstreamCollector
));
```

#### partitioningBy()

```java
// Simple partitioning
Map<Boolean, List<T>> partitioned = stream.collect(Collectors.partitioningBy(predicate));

// With downstream collector
Map<Boolean, D> partitioned = stream.collect(Collectors.partitioningBy(
    predicate,
    downstreamCollector
));
```

### 6.3 Downstream Collectors

Downstream collectors are used with `groupingBy` and `partitioningBy`:

```java
// Count by group
Map<String, Long> countByCustomer = orders.stream()
    .collect(Collectors.groupingBy(
        Order::customerId,
        Collectors.counting()
    ));

// Sum by group
Map<String, Double> totalByCustomer = orders.stream()
    .collect(Collectors.groupingBy(
        Order::customerId,
        Collectors.summingDouble(Order::getAmount)
    ));

// Join by group
Map<String, String> namesByCustomer = orders.stream()
    .collect(Collectors.groupingBy(
        Order::customerId,
        Collectors.mapping(
            Order::getProductName,
            Collectors.joining(", ")
        )
    ));
```

### 6.4 Custom Collectors

Implement the `Collector` interface:

```java
Collector<T, A, R> collector = Collector.of(
    supplier,        // Creates mutable container
    accumulator,     // Adds element to container
    combiner,        // Merges two containers (for parallel)
    finisher         // Transforms container to result (optional)
);
```

---

## 7. Internal Working

### 7.1 Collector Interface

The `Collector` interface defines:

```java
public interface Collector<T, A, R> {
    Supplier<A> supplier();
    BiConsumer<A, T> accumulator();
    BinaryOperator<A> combiner();
    Function<A, R> finisher();
    Set<Characteristics> characteristics();
}
```

### 7.2 Accumulation Process

When `collect()` is invoked:

1. `supplier` creates a new mutable container
2. `accumulator` adds each element to the container
3. `combiner` merges containers (for parallel streams)
4. `finisher` transforms the container to the result

### 7.3 Characteristics

Characteristics indicate collector properties:

- **CONCURRENT**: Can be called from multiple threads
- **UNORDERED**: Doesn't guarantee encounter order
- **IDENTITY_FINISH**: finisher is identity function

---

## 8. JVM Perspective

### 8.1 Collector Implementation

Each built-in collector is a singleton instance:

```
Collectors.toList() → ReducedOps$3
Collectors.toSet() → ReducedOps$4
Collectors.groupingBy() → GroupingOps$1
```

### 8.2 Parallel Collection

For parallel streams, collectors use `combiner` to merge partial results:

```
Stream splits → Parallel accumulation → Combine partial results → Final result
```

### 8.3 JIT Optimization

The JIT compiler can optimize collector pipelines:

1. **Inlining**: Small operations are inlined
2. **Escape analysis**: Containers may be stack-allocated
3. **Loop fusion**: Multiple operations are combined

---

## 9. Memory Representation

### 9.1 Collector Object Layout

```
Collector Implementation:
┌─────────────────────────────────────┐
│  Header (mark word + klass pointer) │
├─────────────────────────────────────┤
│  Supplier (creates containers)      │
│  Accumulator (adds elements)        │
│  Combiner (merges containers)       │
│  Finisher (transforms result)       │
│  Characteristics (flags)            │
└─────────────────────────────────────┘
```

### 9.2 Memory Usage

- **toList()**: Creates ArrayList, grows dynamically
- **toMap()**: Creates HashMap, may resize
- **groupingBy()**: Creates nested maps
- **Custom collectors**: Depends on implementation

---

## 10. Syntax

### 10.1 Basic Collectors

```java
// toList
List<T> list = stream.collect(Collectors.toList());
List<T> unmodifiable = stream.collect(Collectors.toUnmodifiableList());

// toSet
Set<T> set = stream.collect(Collectors.toSet());

// toMap
Map<K, V> map = stream.collect(Collectors.toMap(keyMapper, valueMapper));

// joining
String joined = stream.collect(Collectors.joining(", "));
```

### 10.2 Grouping Collectors

```java
// Simple grouping
Map<K, List<T>> grouped = stream.collect(Collectors.groupingBy(classifier));

// With downstream collector
Map<K, D> grouped = stream.collect(Collectors.groupingBy(
    classifier,
    downstreamCollector
));

// Partitioning
Map<Boolean, List<T>> partitioned = stream.collect(Collectors.partitioningBy(predicate));
```

### 10.3 Downstream Collectors

```java
// Counting
Map<K, Long> counts = stream.collect(Collectors.groupingBy(
    classifier,
    Collectors.counting()
));

// Summing
Map<K, Double> sums = stream.collect(Collectors.groupingBy(
    classifier,

---

[📖 Continue to Part 2](README-part2.md)
