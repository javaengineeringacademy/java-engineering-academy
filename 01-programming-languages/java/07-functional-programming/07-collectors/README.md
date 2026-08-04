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
    Collectors.summingDouble(valueMapper)
));

// Mapping
Map<K, List<V>> mapped = stream.collect(Collectors.groupingBy(
    classifier,
    Collectors.mapping(valueMapper, Collectors.toList())
));
```

---

## 11. Easy Example

### Example 1: Basic Collectors

```java
package academy.javaengineering.functional.collectors;

import java.util.*;
import java.util.stream.Collectors;

public class BasicCollectors {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // toList
        List<String> list = names.stream()
            .filter(name -> name.length() > 3)
            .collect(Collectors.toList());
        System.out.println("List: " + list);
        
        // toSet
        Set<String> set = names.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        System.out.println("Set: " + set);
        
        // joining
        String joined = names.stream()
            .collect(Collectors.joining(", "));
        System.out.println("Joined: " + joined);
        
        // counting
        long count = names.stream()
            .collect(Collectors.counting());
        System.out.println("Count: " + count);
    }
}
```

### Example 2: toMap

```java
package academy.javaengineering.functional.collectors;

import java.util.*;
import java.util.stream.Collectors;

public class ToMapExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // toMap with key and value
        Map<String, Integer> nameLengths = names.stream()
            .collect(Collectors.toMap(
                name -> name,
                String::length
            ));
        System.out.println("Name lengths: " + nameLengths);
        
        // toMap with merge function
        Map<Character, String> firstLetters = names.stream()
            .collect(Collectors.toMap(
                name -> name.charAt(0),
                name -> name,
                (existing, replacement) -> existing + ", " + replacement
            ));
        System.out.println("First letters: " + firstLetters);
    }
}
```

---

## 12. Medium Example

### Example 1: Grouping and Aggregation

```java
package academy.javaengineering.functional.collectors;

import java.util.*;
import java.util.stream.Collectors;

public class GroupingExample {
    
    record Student(String name, String department, double gpa) {}
    
    public static void main(String[] args) {
        List<Student> students = List.of(
            new Student("Alice", "CS", 3.8),
            new Student("Bob", "CS", 3.5),
            new Student("Charlie", "Math", 3.9),
            new Student("Diana", "Math", 3.7),
            new Student("Eve", "CS", 3.6)
        );
        
        // Group by department
        Map<String, List<Student>> byDepartment = students.stream()
            .collect(Collectors.groupingBy(Student::department));
        System.out.println("By department: " + byDepartment);
        
        // Count by department
        Map<String, Long> countByDept = students.stream()
            .collect(Collectors.groupingBy(
                Student::department,
                Collectors.counting()
            ));
        System.out.println("Count by dept: " + countByDept);
        
        // Average GPA by department
        Map<String, Double> avgGpaByDept = students.stream()
            .collect(Collectors.groupingBy(
                Student::department,
                Collectors.averagingDouble(Student::gpa)
            ));
        System.out.println("Avg GPA by dept: " + avgGpaByDept);
        
        // Partition by GPA >= 3.7
        Map<Boolean, List<Student>> partitioned = students.stream()
            .collect(Collectors.partitioningBy(s -> s.gpa() >= 3.7));
        System.out.println("High GPA: " + partitioned.get(true));
        System.out.println("Low GPA: " + partitioned.get(false));
    }
}
```

### Example 2: Complex Aggregation

```java
package academy.javaengineering.functional.collectors;

import java.util.*;
import java.util.stream.Collectors;

public class ComplexAggregation {
    
    record Order(String id, String customer, String product, double amount) {}
    
    public static void main(String[] args) {
        List<Order> orders = List.of(
            new Order("O1", "Alice", "Laptop", 999.99),
            new Order("O2", "Bob", "Phone", 699.99),
            new Order("O3", "Alice", "Tablet", 399.99),
            new Order("O4", "Bob", "Headphones", 199.99),
            new Order("O5", "Alice", "Case", 49.99)
        );
        
        // Total by customer
        Map<String, Double> totalByCustomer = orders.stream()
            .collect(Collectors.groupingBy(
                Order::customer,
                Collectors.summingDouble(Order::amount)
            ));
        System.out.println("Total by customer: " + totalByCustomer);
        
        // Order count by customer
        Map<String, Long> countByCustomer = orders.stream()
            .collect(Collectors.groupingBy(
                Order::customer,
                Collectors.counting()
            ));
        System.out.println("Count by customer: " + countByCustomer);
        
        // Products by customer
        Map<String, List<String>> productsByCustomer = orders.stream()
            .collect(Collectors.groupingBy(
                Order::customer,
                Collectors.mapping(Order::product, Collectors.toList())
            ));
        System.out.println("Products by customer: " + productsByCustomer);
        
        // Summary statistics
        var stats = orders.stream()
            .collect(Collectors.summarizingDouble(Order::amount));
        System.out.println("Stats: " + stats);
    }
}
```

---

## 13. Hard Example

### Example 1: Custom Collector

```java
package academy.javaengineering.functional.collectors;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class CustomCollectorExample {
    
    public static <T> Collector<T, ?, List<T>> toSortedList(Comparator<T> comparator) {
        return Collector.of(
            ArrayList::new,
            List::add,
            (list1, list2) -> {
                list1.addAll(list2);
                return list1;
            },
            list -> {
                list.sort(comparator);
                return Collections.unmodifiableList(list);
            }
        );
    }
    
    public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningByWithCount(
            Predicate<T> predicate) {
        return Collector.of(
            () -> new AbstractMap.SimpleEntry<>(new ArrayList<T>(), new ArrayList<T>()),
            (entry, item) -> {
                if (predicate.test(item)) {
                    entry.getKey().add(item);
                } else {
                    entry.getValue().add(item);
                }
            },
            (entry1, entry2) -> {
                entry1.getKey().addAll(entry2.getKey());
                entry1.getValue().addAll(entry2.getValue());
                return entry1;
            },
            entry -> {
                Map<Boolean, List<T>> result = new HashMap<>();
                result.put(true, Collections.unmodifiableList(entry.getKey()));
                result.put(false, Collections.unmodifiableList(entry.getValue()));
                return Collections.unmodifiableMap(result);
            }
        );
    }
    
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 3, 1, 4, 2, 8, 7, 6);
        
        // Custom sorted list collector
        List<Integer> sorted = numbers.stream()
            .collect(toSortedList(Comparator.naturalOrder()));
        System.out.println("Sorted: " + sorted);
        
        // Custom partitioning
        Map<Boolean, List<Integer>> partitioned = numbers.stream()
            .collect(partitioningByWithCount(n -> n % 2 == 0));
        System.out.println("Even: " + partitioned.get(true));
        System.out.println("Odd: " + partitioned.get(false));
    }
}
```

---

## 14. Enterprise Example

### Example 1: Order Analytics with Collectors

```java
package academy.javaengineering.functional.collectors;

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
    
    public static class OrderAnalyticsService {
        
        public Map<String, BigDecimal> getRevenueByCustomer(List<Order> orders) {
            return orders.stream()
                .filter(o -> o.status() != OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                    Order::customerId,
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        order -> order.items().stream()
                            .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add),
                        BigDecimal::add
                    )
                ));
        }
        
        public Map<OrderStatus, Long> countByStatus(List<Order> orders) {
            return orders.stream()
                .collect(Collectors.groupingBy(
                    Order::status,
                    Collectors.counting()
                ));
        }
        
        public Map<String, List<Order>> groupByCustomer(List<Order> orders) {
            return orders.stream()
                .collect(Collectors.groupingBy(
                    Order::customerId,
                    Collectors.toUnmodifiableList()
                ));
        }
        
        public String getCustomerOrderSummary(List<Order> orders, String customerId) {
            return orders.stream()
                .filter(o -> o.customerId().equals(customerId))
                .map(o -> o.id())
                .collect(Collectors.joining(", "));
        }
    }
    
    public static void main(String[] args) {
        OrderAnalyticsService service = new OrderAnalyticsService();
        List<Order> orders = createSampleOrders();
        
        System.out.println("Revenue by customer:");
        service.getRevenueByCustomer(orders).forEach((customer, revenue) -> 
            System.out.printf("  %s: $%s%n", customer, revenue));
        
        System.out.println("\nCount by status:");
        service.countByStatus(orders).forEach((status, count) -> 
            System.out.printf("  %s: %d%n", status, count));
        
        System.out.println("\nOrders for Alice:");
        System.out.println("  " + service.getCustomerOrderSummary(orders, "Alice"));
    }
    
    private static List<Order> createSampleOrders() {
        return List.of(
            new Order("O1", "Alice", List.of(new OrderItem("P1", 1, new BigDecimal("99.99"))), OrderStatus.DELIVERED, LocalDateTime.now().minusDays(5)),
            new Order("O2", "Bob", List.of(new OrderItem("P2", 2, new BigDecimal("49.99"))), OrderStatus.DELIVERED, LocalDateTime.now().minusDays(3)),
            new Order("O3", "Alice", List.of(new OrderItem("P3", 1, new BigDecimal("199.99"))), OrderStatus.PENDING, LocalDateTime.now().minusDays(1))
        );
    }
}
```

---

## 15. Performance

### 15.1 Collector Performance

| Collector | Time Complexity | Space Complexity | Notes |
|-----------|-----------------|------------------|-------|
| `toList()` | O(n) | O(n) | Creates ArrayList |
| `toSet()` | O(n) | O(n) | Creates HashSet |
| `toMap()` | O(n) | O(n) | Creates HashMap |
| `joining()` | O(n) | O(n) | Uses StringBuilder |
| `groupingBy()` | O(n) | O(n) | Creates nested maps |

### 15.2 Performance Tips

1. **Use `toUnmodifiableList()`**: Better for parallel streams
2. **Avoid `groupingBy()` with `toList()`**: Use `toUnmodifiableList()` for parallel
3. **Use `summingDouble()` over `reduce()`**: More efficient
4. **Consider custom collectors**: For complex aggregation logic

---

## 16. Best Practices

1. **Use `toUnmodifiableList()`**: Better for parallel streams
2. **Prefer `summingDouble()` over `reduce()`**: More efficient
3. **Use downstream collectors**: For nested aggregation
4. **Document custom collectors**: Explain the purpose and behavior
5. **Test collectors independently**: Verify correctness

---

## 17. Common Mistakes

### Mistake 1: Using toMap with Duplicate Keys

```java
// WRONG: May throw IllegalStateException
Map<String, String> map = stream.collect(Collectors.toMap(
    Item::category,
    Item::name
));

// CORRECT: Handle duplicates
Map<String, String> map = stream.collect(Collectors.toMap(
    Item::category,
    Item::name,
    (existing, replacement) -> existing + ", " + replacement
));
```

### Mistake 2: Not Using Unmodifiable Collectors

```java
// WRONG: Mutable result
List<String> list = stream.collect(Collectors.toList());

// CORRECT: Unmodifiable result
List<String> list = stream.collect(Collectors.toUnmodifiableList());
```

---

## 18. Pitfalls

1. **Mutable state**: Collectors may use mutable containers
2. **Ordering**: Some collectors don't preserve order
3. **Null handling**: Some collectors don't accept null elements
4. **Thread safety**: Ensure collectors are thread-safe for parallel streams

---

## 19. Debugging Tips

### 1. Use peek() for Debugging

```java
stream
    .peek(item -> System.out.println("Before collect: " + item))
    .collect(Collectors.toList());
```

### 2. Test Custom Collectors

```java
// Test supplier
A container = collector.supplier().get();

// Test accumulator
collector.accumulator().accept(container, item);

// Test finisher
R result = collector.finisher().apply(container);
```

---

## 20. Comparison Table

| Collector | Use Case | Result Type | Notes |
|-----------|----------|-------------|-------|
| `toList()` | Simple list | List<T> | Mutable |
| `toUnmodifiableList()` | Unmodifiable list | List<T> | Immutable |
| `toSet()` | Unique elements | Set<T> | No order |
| `toMap()` | Key-value mapping | Map<K,V> | Handles duplicates |
| `joining()` | String concatenation | String | Custom delimiter |
| `groupingBy()` | Grouping | Map<K,List<T>> | With downstream |
| `partitioningBy()` | Partitioning | Map<Boolean,List<T>> | Two groups |
| `counting()` | Count | long | Simple |
| `summingDouble()` | Sum | double | Primitive |

---

## 21. Decision Tree

```
Which collector should you use?

┌─ Do you need a List?
│  ├─ YES → toUnmodifiableList()
│  └─ NO → Continue
│
├─ Do you need a Set?
│  ├─ YES → toUnmodifiableSet()
│  └─ NO → Continue
│
├─ Do you need a Map?
│  ├─ YES → toMap(keyMapper, valueMapper, mergeFunction)
│  └─ NO → Continue
│
├─ Do you need to join strings?
│  ├─ YES → joining(delimiter)
│  └─ NO → Continue
│
├─ Do you need to group by key?
│  ├─ YES → groupingBy(classifier)
│  └─ NO → Continue
│
├─ Do you need to partition by predicate?
│  ├─ YES → partitioningBy(predicate)
│  └─ NO → Continue
│
└─ Do you need a custom accumulation?
   └─ YES → Implement Collector
```

---

## 22. Interview Questions

### Q1: What is the difference between `toList()` and `toUnmodifiableList()`?

**Answer**: `toList()` returns a mutable List that can be modified after collection. `toUnmodifiableList()` returns an immutable List that throws UnsupportedOperationException if modified. Use `toUnmodifiableList()` for thread safety and encapsulation.

### Q2: How do you handle duplicate keys in `toMap()`?

**Answer**: Use the three-argument version with a merge function:
```java
stream.collect(Collectors.toMap(
    keyMapper,
    valueMapper,
    (existing, replacement) -> existing + ", " + replacement
));
```

### Q3: What are downstream collectors?

**Answer**: Downstream collectors are used with `groupingBy` and `partitioningBy` to further aggregate grouped elements. Examples include `counting()`, `summingDouble()`, `mapping()`, `toList()`.

### Q4: How do you implement a custom collector?

**Answer**: Implement the `Collector` interface with:
1. `supplier()`: Creates mutable container
2. `accumulator()`: Adds element to container
3. `combiner()`: Merges two containers
4. `finisher()`: Transforms container to result

### Q5: What are the characteristics of a collector?

**Answer**: Characteristics indicate collector properties:
- **CONCURRENT**: Can be called from multiple threads
- **UNORDERED**: Doesn't guarantee encounter order
- **IDENTITY_FINISH**: finisher is identity function

---

## 23. Exercises

### Exercise 1: Basic Collectors
Given a list of integers, use collectors to:
1. Create a list of even numbers
2. Create a set of squares
3. Join numbers with commas
4. Count numbers greater than 5

### Exercise 2: Grouping
Given a list of students with names and grades, use collectors to:
1. Group by grade
2. Count students per grade
3. Find average grade per group

### Exercise 3: Custom Collector
Implement a custom collector that:
1. Collects strings into a comma-separated string
2. Handles empty strings
3. Truncates to a maximum length

---

## 24. Assignments

### Assignment 1: Data Aggregation
Build a data aggregation system that:
1. Groups data by multiple keys
2. Calculates aggregates (sum, count, average)
3. Supports custom collectors

### Assignment 2: Reporting System
Implement a reporting system that:
1. Processes sales data
2. Generates reports by region, product, and time
3. Supports export to different formats

### Assignment 3: Stream Utilities
Create a utility class with collector helpers:
1. Custom `toUnmodifiableMap()` collector
2. Custom `flatGroupingBy()` collector
3. Custom `joiningWithLimit()` collector

---

## 25. Mini Project

### Project: Data Analytics Collector Library

Build a comprehensive collector library:

**Requirements:**
1. Implement custom collectors for common patterns
2. Support downstream collectors
3. Handle parallel streams correctly
4. Provide documentation

**Starter Code:**
```java
package academy.javaengineering.functional.collectors.project;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class AnalyticsCollectors {
    
    public static <T, K> Collector<T, ?, Map<K, Long>> countingByGroup(
            Function<T, K> classifier) {
        return Collector.of(
            HashMap::new,
            (map, item) -> map.merge(classifier.apply(item), 1L, Long::sum),
            (map1, map2) -> {
                map2.forEach((k, v) -> map1.merge(k, v, Long::sum));
                return map1;
            }
        );
    }
    
    // TODO: Implement more collectors
}
```

---

## 26. Summary

Collectors provide a flexible, composable way to accumulate stream elements. Key takeaways:

1. **Built-in collectors**: toList, toSet, toMap, joining, groupingBy, partitioningBy
2. **Downstream collectors**: Further aggregate grouped elements
3. **Custom collectors**: Implement for complex aggregation logic
4. **Unmodifiable results**: Use toUnmodifiableList/Set/Map for thread safety
5. **Parallel support**: Collectors work correctly with parallel streams

### Next Steps
- Topic 08: Optional — Null-safe value handling
- Topic 09: Composition — Function composition patterns

---

## 27. References

1. [Oracle Java Tutorials: Collectors](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/stream/Collectors.html)
2. [Java Language Specification: Collectors](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 43](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Collectors](https://www.baeldung.com/java-collectors)
