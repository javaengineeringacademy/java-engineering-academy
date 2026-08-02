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

Without a comprehensive set of stream operations, developers must:
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
- **Intermediate Stages**: Small objects (references only)
- **Stateful Operations**: May buffer elements (sorted, distinct)
- **Result**: Depends on terminal operation

---

## 10. Syntax

### 10.1 Intermediate Operations

```java
// filter
stream.filter(item -> item.getPrice() > 100)

// map
stream.map(Item::getName)

// flatMap
stream.flatMap(item -> item.getTags().stream())

// distinct
stream.distinct()

// sorted
stream.sorted()
stream.sorted(Comparator.comparing(Item::getPrice))

// peek
stream.peek(item -> System.out.println(item))

// limit
stream.limit(10)

// skip
stream.skip(5)
```

### 10.2 Terminal Operations

```java
// collect
stream.collect(Collectors.toList())
stream.collect(Collectors.toSet())
stream.collect(Collectors.toMap(Item::getId, Item::getName))

// forEach
stream.forEach(item -> System.out.println(item))

// reduce
stream.reduce(0, Integer::sum)
stream.reduce((a, b) -> a + b)

// count
stream.count()

// anyMatch, allMatch, noneMatch
stream.anyMatch(item -> item.getPrice() > 100)
stream.allMatch(item -> item.getPrice() > 0)
stream.noneMatch(item -> item.getPrice() < 0)

// findFirst, findAny
stream.findFirst()
stream.findAny()

// min, max
stream.min(Comparator.comparing(Item::getPrice))
stream.max(Comparator.comparing(Item::getPrice))
```

---

## 11. Easy Example

### Example 1: Basic Operations

```java
package academy.javaengineering.functional.operations;

import java.util.Arrays;
import java.util.List;

public class BasicOperations {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Filter
        List<Integer> even = numbers.stream()
            .filter(n -> n % 2 == 0)
            .toList();
        System.out.println("Even: " + even);
        
        // Map
        List<Integer> squares = numbers.stream()
            .map(n -> n * n)
            .toList();
        System.out.println("Squares: " + squares);
        
        // Reduce
        int sum = numbers.stream()
            .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
        
        // Count
        long count = numbers.stream()
            .filter(n -> n > 5)
            .count();
        System.out.println("Count > 5: " + count);
    }
}
```

### Example 2: String Operations

```java
package academy.javaengineering.functional.operations;

import java.util.Arrays;
import java.util.List;

public class StringOperations {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // Filter by length
        List<String> longNames = names.stream()
            .filter(name -> name.length() > 4)
            .toList();
        System.out.println("Long names: " + longNames);
        
        // Transform
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("Uppercase: " + upperNames);
        
        // Find
        String first = names.stream()
            .filter(name -> name.startsWith("C"))
            .findFirst()
            .orElse("Not found");
        System.out.println("First C: " + first);
        
        // Match
        boolean anyStartsWithA = names.stream()
            .anyMatch(name -> name.startsWith("A"));
        System.out.println("Any starts with A: " + anyStartsWithA);
    }
}
```

---

## 12. Medium Example

### Example 1: Complex Pipeline

```java
package academy.javaengineering.functional.operations;

import java.util.*;
import java.util.stream.Collectors;

public class ComplexPipeline {
    
    record Product(String name, String category, double price, int stock) {}
    
    public static void main(String[] args) {
        List<Product> products = List.of(
            new Product("Laptop", "Electronics", 999.99, 10),
            new Product("Phone", "Electronics", 699.99, 25),
            new Product("Desk", "Furniture", 299.99, 5),
            new Product("Chair", "Furniture", 149.99, 15),
            new Product("Headphones", "Electronics", 199.99, 30)
        );
        
        // Find affordable electronics in stock
        List<String> affordableElectronics = products.stream()
            .filter(p -> "Electronics".equals(p.category()))
            .filter(p -> p.price() < 500)
            .filter(p -> p.stock() > 0)
            .map(Product::name)
            .sorted()
            .toList();
        System.out.println("Affordable electronics: " + affordableElectronics);
        
        // Calculate total value by category
        Map<String, Double> valueByCategory = products.stream()
            .collect(Collectors.groupingBy(
                Product::category,
                Collectors.summingDouble(p -> p.price() * p.stock())
            ));
        System.out.println("Value by category: " + valueByCategory);
        
        // Find most expensive product
        Optional<Product> mostExpensive = products.stream()
            .max(Comparator.comparing(Product::price));
        mostExpensive.ifPresent(p -> 
            System.out.println("Most expensive: " + p.name() + " ($" + p.price() + ")"));
    }
}
```

### Example 2: FlatMap and Nested Data

```java
package academy.javaengineering.functional.operations;

import java.util.*;
import java.util.stream.Collectors;

public class FlatMapExample {
    
    record Order(String id, List<Item> items) {}
    record Item(String name, int quantity) {}
    
    public static void main(String[] args) {
        List<Order> orders = List.of(
            new Order("O1", List.of(new Item("Laptop", 1), new Item("Mouse", 2))),
            new Order("O2", List.of(new Item("Phone", 1))),
            new Order("O3", List.of(new Item("Tablet", 1), new Item("Case", 1)))
        );
        
        // Get all items from all orders
        List<Item> allItems = orders.stream()
            .flatMap(order -> order.items().stream())
            .toList();
        System.out.println("All items: " + allItems);
        
        // Count total items
        int totalQuantity = orders.stream()
            .flatMap(order -> order.items().stream())
            .mapToInt(Item::quantity)
            .sum();
        System.out.println("Total quantity: " + totalQuantity);
        
        // Get unique item names
        Set<String> uniqueItems = orders.stream()
            .flatMap(order -> order.items().stream())
            .map(Item::name)
            .collect(Collectors.toSet());
        System.out.println("Unique items: " + uniqueItems);
    }
}
```

---

## 13. Hard Example

### Example 1: Custom Collector

```java
package academy.javaengineering.functional.operations;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class CustomCollectorExample {
    
    public static <T> Collector<T, ?, String> joiningWithLimit(
            String delimiter, int limit) {
        return Collector.of(
            StringBuilder::new,
            (sb, item) -> {
                if (sb.length() > 0) sb.append(delimiter);
                sb.append(item);
                if (sb.length() > limit) sb.setLength(limit);
            },
            (sb1, sb2) -> {
                if (sb1.length() > 0 && sb2.length() > 0) sb1.append(delimiter);
                sb1.append(sb2);
                if (sb1.length() > limit) sb1.setLength(limit);
                return sb1;
            },
            StringBuilder::toString
        );
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // Custom joining with limit
        String result = names.stream()
            .collect(joiningWithLimit(", ", 20));
        System.out.println("Joined with limit: " + result);
        
        // Using built-in collectors
        String joined = names.stream()
            .collect(java.util.stream.Collectors.joining(", "));
        System.out.println("Built-in joining: " + joined);
    }
}
```

### Example 2: Advanced Stream Manipulation

```java
package academy.javaengineering.functional.operations;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdvancedManipulation {
    
    public static void main(String[] args) {
        // Generate Fibonacci sequence
        List<Integer> fibonacci = IntStream.iterate(0, n -> {
            int a = n / 1000000;
            int b = n % 1000000;
            return (a + b) * 1000000 + b;
        })
        .limit(20)
        .map(n -> n / 1000000)
        .boxed()
        .toList();
        System.out.println("Fibonacci: " + fibonacci);
        
        // Sliding window
        List<Integer> numbers = IntStream.rangeClosed(1, 10).boxed().toList();
        List<List<Integer>> windows = IntStream.range(0, numbers.size() - 2)
            .mapToObj(i -> numbers.subList(i, i + 3))
            .toList();
        System.out.println("Windows: " + windows);
        
        // Group and count
        String text = "hello world hello java world hello";
        Map<String, Long> wordCount = Arrays.stream(text.split(" "))
            .collect(Collectors.groupingBy(
                word -> word,
                Collectors.counting()
            ));
        System.out.println("Word count: " + wordCount);
    }
}
```

---

## 14. Enterprise Example

### Example 1: Order Analytics Pipeline

```java
package academy.javaengineering.functional.operations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderAnalytics {
    
    public record Order(
        String id,
        String customerId,
        List<OrderItem> items,
        OrderStatus status,
        LocalDateTime createdAt
    ) {}
    
    public record OrderItem(String productId, int quantity, BigDecimal unitPrice) {}
    
    public enum OrderStatus { PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED }
    
    public record AnalyticsResult(
        long totalOrders,
        long completedOrders,
        BigDecimal totalRevenue,
        BigDecimal averageOrderValue,
        String topCustomerId
    ) {}
    
    public static class OrderAnalyticsService {
        
        public AnalyticsResult calculateAnalytics(List<Order> orders) {
            long totalOrders = orders.size();
            
            List<Order> completedOrders = orders.stream()
                .filter(o -> o.status() == OrderStatus.DELIVERED)
                .toList();
            
            long completedCount = completedOrders.size();
            
            BigDecimal totalRevenue = completedOrders.stream()
                .flatMap(o -> o.items().stream())
                .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            BigDecimal averageOrderValue = completedCount > 0
                ? totalRevenue.divide(BigDecimal.valueOf(completedCount), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;
            
            String topCustomerId = orders.stream()
                .collect(Collectors.groupingBy(
                    Order::customerId,
                    Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
            
            return new AnalyticsResult(
                totalOrders,
                completedCount,
                totalRevenue,
                averageOrderValue,
                topCustomerId
            );
        }
    }
    
    public static void main(String[] args) {
        OrderAnalyticsService service = new OrderAnalyticsService();
        List<Order> orders = createSampleOrders();
        
        AnalyticsResult result = service.calculateAnalytics(orders);
        
        System.out.println("=== Order Analytics ===");
        System.out.println("Total orders: " + result.totalOrders());
        System.out.println("Completed orders: " + result.completedOrders());
        System.out.printf("Total revenue: $%s%n", result.totalRevenue());
        System.out.printf("Average order value: $%s%n", result.averageOrderValue());
        System.out.println("Top customer: " + result.topCustomerId());
    }
    
    private static List<Order> createSampleOrders() {
        return List.of(
            new Order("O1", "C1", List.of(new OrderItem("P1", 1, new BigDecimal("99.99"))), OrderStatus.DELIVERED, LocalDateTime.now().minusDays(5)),
            new Order("O2", "C2", List.of(new OrderItem("P2", 2, new BigDecimal("49.99"))), OrderStatus.DELIVERED, LocalDateTime.now().minusDays(3)),
            new Order("O3", "C1", List.of(new OrderItem("P3", 1, new BigDecimal("199.99"))), OrderStatus.PENDING, LocalDateTime.now().minusDays(1)),
            new Order("O4", "C3", List.of(new OrderItem("P4", 1, new BigDecimal("299.99"))), OrderStatus.DELIVERED, LocalDateTime.now().minusDays(2))
        );
    }
}
```

---

## 15. Performance

### 15.1 Operation Performance

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|-----------------|------------------|-------|
| `filter` | O(n) | O(1) | Stateless |
| `map` | O(n) | O(1) | Stateless |
| `flatMap` | O(n) | O(1) | Stateless |
| `distinct` | O(n) | O(n) | Stateful |
| `sorted` | O(n log n) | O(n) | Stateful |
| `limit` | O(n) | O(1) | Short-circuit |
| `skip` | O(n) | O(1) | Stateful |
| `reduce` | O(n) | O(1) | Terminal |
| `collect` | O(n) | O(n) | Terminal |

### 15.2 Performance Tips

1. **Filter early**: Reduce dataset size as soon as possible
2. **Use primitive streams**: Avoid boxing overhead
3. **Short-circuit**: Use `limit()`, `findFirst()`, `anyMatch()` when possible
4. **Avoid `sorted()`**: Sort only when necessary
5. **Parallel for large datasets**: Use `parallelStream()` for CPU-bound work

---

## 16. Best Practices

1. **Filter before map**: Reduce dataset size before transformation
2. **Use method references**: More readable
3. **Avoid side effects**: Don't modify external state
4. **Use peek() for debugging only**: Not for production code
5. **Prefer `toList()` over `collect(Collectors.toList())`**
6. **Consider parallel for large datasets**: But test performance first

---

## 17. Common Mistakes

### Mistake 1: Filtering After Mapping

```java
// WRONG: Mapping unnecessary elements
list.stream()
    .map(item -> item.getName())  // Maps all items
    .filter(name -> name.length() > 3)  // Then filters
    .toList();

// CORRECT: Filter first
list.stream()
    .filter(item -> item.getName().length() > 3)  // Filter first
    .map(Item::getName)  // Then map
    .toList();
```

### Mistake 2: Using forEach with collect

```java
// WRONG: Side effects
List<String> result = new ArrayList<>();
list.stream()
    .map(Item::getName)
    .forEach(result::add);  // Side effect!

// CORRECT: Use collect
List<String> result = list.stream()
    .map(Item::getName)
    .collect(Collectors.toList());
```

---

## 18. Pitfalls

1. **Single-use**: Streams can only be consumed once
2. **Ordering**: Parallel streams don't guarantee order
3. **Stateful operations**: `distinct()`, `sorted()` require full traversal
4. **Side effects**: Can break parallel processing
5. **Infinite streams**: Must use short-circuit operations

---

## 19. Debugging Tips

### 1. Use peek() for Debugging

```java
list.stream()
    .filter(predicate)
    .peek(item -> System.out.println("After filter: " + item))
    .map(transformer)
    .peek(item -> System.out.println("After map: " + item))
    .toList();
```

### 2. Extract Complex Operations

```java
// Instead of complex inline lambda
list.stream()
    .filter(item -> item.getStatus() == Status.ACTIVE && item.getPriority() > 5)
    .toList();

// Extract to named method
Predicate<Item> isActiveHighPriority = this::isActiveHighPriority;
list.stream().filter(isActiveHighPriority).toList();
```

---

## 20. Comparison Table

| Operation | Intermediate/Terminal | Stateless/Stateful | Short-circuit |
|-----------|----------------------|-------------------|---------------|
| `filter` | Intermediate | Stateless | No |
| `map` | Intermediate | Stateless | No |
| `flatMap` | Intermediate | Stateless | No |
| `distinct` | Intermediate | Stateful | No |
| `sorted` | Intermediate | Stateful | No |
| `limit` | Intermediate | Stateful | Yes |
| `skip` | Intermediate | Stateful | No |
| `forEach` | Terminal | - | No |
| `collect` | Terminal | - | No |
| `reduce` | Terminal | - | No |
| `count` | Terminal | - | No |
| `anyMatch` | Terminal | - | Yes |
| `allMatch` | Terminal | - | Yes |
| `noneMatch` | Terminal | - | Yes |
| `findFirst` | Terminal | - | Yes |
| `findAny` | Terminal | - | Yes |

---

## 21. Decision Tree

```
Which operation should you use?

┌─ Do you need to select elements?
│  ├─ YES → filter
│  └─ NO → Continue
│
├─ Do you need to transform elements?
│  ├─ YES → map (or flatMap for nested)
│  └─ NO → Continue
│
├─ Do you need to remove duplicates?
│  ├─ YES → distinct
│  └─ NO → Continue
│
├─ Do you need to sort elements?
│  ├─ YES → sorted
│  └─ NO → Continue
│
├─ Do you need to limit results?
│  ├─ YES → limit
│  └─ NO → Continue
│
├─ Do you need a single value?
│  ├─ YES → reduce
│  └─ NO → Continue
│
├─ Do you need a collection?
│  ├─ YES → collect
│  └─ NO → Continue
│
└─ Do you need to check conditions?
   ├─ YES → anyMatch/allMatch/noneMatch
   └─ NO → Use appropriate terminal operation
```

---

## 22. Interview Questions

### Q1: What is the difference between `map` and `flatMap`?

**Answer**: `map` transforms each element into a single value. `flatMap` transforms each element into a stream, then flattens all streams into a single stream. Use `flatMap` when you need to flatten nested collections.

### Q2: What is the difference between `findFirst` and `findAny`?

**Answer**: `findFirst` returns the first element in encounter order. `findAny` returns any element (may be non-deterministic). For parallel streams, `findAny` is often faster because it doesn't need to maintain order.

### Q3: When should you use `limit()` vs `skip()`?

**Answer**: `limit(n)` takes the first n elements. `skip(n)` skips the first n elements. Use `limit` for pagination (page 1: skip 0, limit 10; page 2: skip 10, limit 10).

### Q4: What is the difference between `reduce` and `collect`?

**Answer**: `reduce` combines elements into a single value using a binary operator. `collect` accumulates elements into a mutable container using a Collector. Use `reduce` for simple reductions, `collect` for complex accumulation.

### Q5: How do you debug a stream pipeline?

**Answer**: Use `peek()` to inspect elements at each stage. For production code, extract complex operations to named methods. Use a debugger with stream support.

---

## 23. Exercises

### Exercise 1: Basic Operations
Given a list of integers, use streams to:
1. Filter positive numbers
2. Square each number
3. Sum the squares

### Exercise 2: String Processing
Given a list of strings, use streams to:
1. Filter strings longer than 3 characters
2. Convert to uppercase
3. Sort alphabetically
4. Join with commas

### Exercise 3: Complex Pipeline
Given a list of orders, use streams to:
1. Filter completed orders
2. Calculate total revenue
3. Find the most expensive order
4. Group by customer

---

## 24. Assignments

### Assignment 1: Data Processing Pipeline
Build a data processing pipeline that:
1. Reads data from a source
2. Filters invalid records
3. Transforms data
4. Aggregates results
5. Outputs to a destination

### Assignment 2: Analytics System
Implement an analytics system that:
1. Processes event streams
2. Calculates aggregates (sum, count, average)
3. Supports time-based windowing
4. Implements custom collectors

### Assignment 3: Stream Utilities
Create a utility class with stream helpers:
1. Custom `toUnmodifiableList()` collector
2. Custom `groupingBy()` with multiple values
3. Custom `partitioningBy()` with custom logic

---

## 25. Mini Project

### Project: Stream-Based Analytics Engine

Build an analytics engine using stream operations:

**Requirements:**
1. Process event streams
2. Calculate aggregates (sum, count, average)
3. Support time-based windowing
4. Implement custom collectors
5. Support parallel processing

**Starter Code:**
```java
package academy.javaengineering.functional.operations.project;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsEngine {
    
    public record Event(
        String id,
        String type,
        double value,
        LocalDateTime timestamp
    ) {}
    
    public static class EventProcessor {
        
        public double calculateSum(List<Event> events, String type) {
            return events.stream()
                .filter(e -> e.type().equals(type))
                .mapToDouble(Event::value)
                .sum();
        }
        
        // TODO: Implement more analytics methods
    }
}
```

---

## 26. Summary

Stream operations are the building blocks of data processing pipelines. Key takeaways:

1. **Intermediate operations** are lazy and return new streams
2. **Terminal operations** trigger processing and produce results
3. **Stateless operations** process elements independently
4. **Stateful operations** require tracking across elements
5. **Short-circuit operations** stop processing early

### Next Steps
- Topic 07: Collectors — Custom collector implementation
- Topic 08: Optional — Null-safe value handling

---

## 27. References

1. [Oracle Java Tutorials: Stream Operations](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
2. [Java Language Specification: Stream Operations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 43](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Stream Operations](https://www.baeldung.com/java-streams)
