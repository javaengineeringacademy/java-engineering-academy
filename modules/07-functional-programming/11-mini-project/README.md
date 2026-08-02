# Topic 11: Mini Project - Functional Data Pipeline Engine

## Table of Contents

1. [Introduction](#1-introduction)
2. [Learning Objectives](#2-learning-objectives)
3. [Prerequisites](#3-prerequisites)
4. [Project Overview](#4-project-overview)
5. [Requirements](#5-requirements)
6. [Architecture](#6-architecture)
7. [Implementation Guide](#7-implementation-guide)
8. [Code Examples](#8-code-examples)
9. [Testing](#9-testing)
10. [Performance](#10-performance)
11. [Extensions](#11-extensions)
12. [Summary](#12-summary)

---

## 1. Introduction

This mini project brings together all the functional programming concepts you've learned throughout this module. You will build a **Functional Data Pipeline Engine** that processes real-world datasets using pure functional techniques.

### Project Goals

1. Apply lambda expressions and functional interfaces
2. Use method references for clean syntax
3. Build stream pipelines for data processing
4. Implement custom collectors
5. Use Optional for null-safe processing
6. Apply function composition for complex transformations

---

## 2. Learning Objectives

After completing this project, you will be able to:

1. Design and implement functional data processing systems
2. Apply all functional programming concepts in a real-world scenario
3. Build reusable, composable data pipelines
4. Implement custom collectors for domain-specific aggregation
5. Handle errors functionally
6. Optimize functional code for performance

---

## 3. Prerequisites

Before starting this project, you should have completed:

- Topic 01-10: All functional programming topics
- Understanding of Java Streams, Lambdas, and Functional Interfaces

---

## 4. Project Overview

### 4.1 What You'll Build

A **Functional Data Pipeline Engine** that:
- Processes customer order data
- Calculates analytics and statistics
- Generates reports
- Handles errors gracefully
- Supports parallel processing

### 4.2 Key Components

1. **Data Models**: Immutable records for orders, customers, products
2. **Pipeline Builder**: Composable transformation pipeline
3. **Collectors**: Custom collectors for analytics
4. **Error Handling**: Functional error handling with Optional
5. **Analytics Engine**: Stream-based analytics

### 4.3 Sample Data

```java
// Customers
Customer("C001", "Alice", "alice@example.com", "New York")
Customer("C002", "Bob", "bob@example.com", "London")
Customer("C003", "Charlie", "charlie@example.com", "New York")

// Products
Product("P001", "Laptop", "Electronics", 999.99)
Product("P002", "Phone", "Electronics", 699.99)
Product("P003", "Desk", "Furniture", 299.99)

// Orders
Order("O001", "C001", List.of(OrderItem("P001", 1)), LocalDateTime.now().minusDays(5), OrderStatus.DELIVERED)
Order("O002", "C002", List.of(OrderItem("P002", 2)), LocalDateTime.now().minusDays(3), OrderStatus.DELIVERED)
Order("O003", "C001", List.of(OrderItem("P003", 1)), LocalDateTime.now().minusDays(1), OrderStatus.PENDING)
```

---

## 5. Requirements

### 5.1 Functional Requirements

1. **Data Processing Pipeline**
   - Filter orders by status
   - Transform order data for reporting
   - Calculate totals and aggregates

2. **Analytics**
   - Total revenue by customer
   - Revenue by product category
   - Order count by status
   - Average order value

3. **Error Handling**
   - Handle missing data gracefully
   - Validate input data
   - Provide default values for missing fields

4. **Reporting**
   - Generate customer summaries
   - Generate product performance reports
   - Generate time-based analytics

### 5.2 Technical Requirements

1. Use immutable records for data models
2. Implement functional interfaces for custom operations
3. Use stream pipelines for data processing
4. Implement custom collectors
5. Use Optional for null-safe operations
6. Apply function composition for complex transformations

---

## 6. Architecture

### 6.1 Package Structure

```
src/main/java/academy/javaengineering/functional/project/
├── model/
│   ├── Customer.java
│   ├── Product.java
│   ├── Order.java
│   └── OrderItem.java
├── pipeline/
│   ├── DataPipeline.java
│   └── PipelineBuilder.java
├── collectors/
│   ├── AnalyticsCollectors.java
│   └── ReportCollectors.java
├── service/
│   ├── OrderService.java
│   └── AnalyticsService.java
└── Application.java
```

### 6.2 Design Patterns

1. **Builder Pattern**: For constructing pipelines
2. **Strategy Pattern**: For interchangeable processing logic
3. **Decorator Pattern**: For adding behavior to pipelines
4. **Factory Pattern**: For creating processors

---

## 7. Implementation Guide

### Step 1: Create Data Models

```java
package academy.javaengineering.functional.project.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Customer(
    String id,
    String name,
    String email,
    String city
) {}

public record Product(
    String id,
    String name,
    String category,
    BigDecimal price
) {}

public record OrderItem(
    String productId,
    int quantity,
    BigDecimal unitPrice
) {}

public record Order(
    String id,
    String customerId,
    List<OrderItem> items,
    LocalDateTime createdAt,
    OrderStatus status
) {
    public BigDecimal totalAmount() {
        return items.stream()
            .map(item -> item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

public enum OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}
```

### Step 2: Implement Functional Interfaces

```java
package academy.javaengineering.functional.project.pipeline;

import java.util.function.Function;

@FunctionalInterface
public interface Pipeline<T, R> {
    R process(T input);
    
    default <V> Pipeline<T, V> andThen(Pipeline<R, V> after) {
        return input -> after.process(this.process(input));
    }
    
    static <T> Pipeline<T, T> identity() {
        return input -> input;
    }
}
```

### Step 3: Implement Custom Collectors

```java
package academy.javaengineering.functional.project.collectors;

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
    
    public static <T> Collector<T, ?, Optional<T>> maxByValue(
            Function<T, Double> valueExtractor) {
        return Collector.of(
            () -> (T[]) new Object[1],
            (acc, item) -> {
                if (acc[0] == null || valueExtractor.apply(item) > valueExtractor.apply((T) acc[0])) {
                    acc[0] = item;
                }
            },
            (acc1, acc2) -> {
                if (acc2[0] != null && (acc1[0] == null || valueExtractor.apply((T) acc2[0]) > valueExtractor.apply((T) acc1[0]))) {
                    acc1[0] = acc2[0];
                }
                return acc1;
            },
            acc -> Optional.ofNullable((T) acc[0])
        );
    }
}
```

### Step 4: Implement Analytics Service

```java
package academy.javaengineering.functional.project.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import academy.javaengineering.functional.project.model.*;

public class AnalyticsService {
    
    public Map<String, BigDecimal> calculateRevenueByCustomer(List<Order> orders) {
        return orders.stream()
            .filter(o -> o.status() != OrderStatus.CANCELLED)
            .collect(Collectors.groupingBy(
                Order::customerId,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    Order::totalAmount,
                    BigDecimal::add
                )
            ));
    }
    
    public Map<String, Long> countByStatus(List<Order> orders) {
        return orders.stream()
            .collect(Collectors.groupingBy(
                o -> o.status().name(),
                Collectors.counting()
            ));
    }
    
    public Optional<Order> findMostExpensiveOrder(List<Order> orders) {
        return orders.stream()
            .max(Comparator.comparing(Order::totalAmount));
    }
    
    public BigDecimal calculateAverageOrderValue(List<Order> orders) {
        return orders.stream()
            .mapToDouble(o -> o.totalAmount().doubleValue())
            .average()
            .orElse(0.0);
    }
}
```

---

## 8. Code Examples

### Example 1: Complete Pipeline

```java
package academy.javaengineering.functional.project;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import academy.javaengineering.functional.project.model.*;

public class CompleteExample {
    
    public static void main(String[] args) {
        // Create sample data
        List<Customer> customers = createCustomers();
        List<Product> products = createProducts();
        List<Order> orders = createOrders();
        
        // Build processing pipeline
        Function<Order, OrderSummary> toSummary = order -> {
            Optional<Customer> customer = customers.stream()
                .filter(c -> c.id().equals(order.customerId()))
                .findFirst();
            
            return new OrderSummary(
                order.id(),
                customer.map(Customer::name).orElse("Unknown"),
                order.totalAmount(),
                order.items().size(),
                order.status().name()
            );
        };
        
        // Process orders
        List<OrderSummary> summaries = orders.stream()
            .map(toSummary)
            .sorted(Comparator.comparing(OrderSummary::totalAmount).reversed())
            .toList();
        
        System.out.println("Order Summaries:");
        summaries.forEach(s -> System.out.println("  " + s));
        
        // Calculate analytics
        Map<String, BigDecimal> revenueByCustomer = orders.stream()
            .filter(o -> o.status() != OrderStatus.CANCELLED)
            .collect(Collectors.groupingBy(
                Order::customerId,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    Order::totalAmount,
                    BigDecimal::add
                )
            ));
        
        System.out.println("\nRevenue by Customer:");
        revenueByCustomer.forEach((id, amount) -> 
            System.out.printf("  %s: $%s%n", id, amount));
    }
    
    record OrderSummary(
        String orderId,
        String customerName,
        BigDecimal totalAmount,
        int itemCount,
        String status
    ) {}
    
    private static List<Customer> createCustomers() {
        return List.of(
            new Customer("C001", "Alice", "alice@example.com", "New York"),
            new Customer("C002", "Bob", "bob@example.com", "London"),
            new Customer("C003", "Charlie", "charlie@example.com", "New York")
        );
    }
    
    private static List<Product> createProducts() {
        return List.of(
            new Product("P001", "Laptop", "Electronics", new BigDecimal("999.99")),
            new Product("P002", "Phone", "Electronics", new BigDecimal("699.99")),
            new Product("P003", "Desk", "Furniture", new BigDecimal("299.99"))
        );
    }
    
    private static List<Order> createOrders() {
        return List.of(
            new Order("O001", "C001", 
                List.of(new OrderItem("P001", 1, new BigDecimal("999.99"))),
                LocalDateTime.now().minusDays(5), OrderStatus.DELIVERED),
            new Order("O002", "C002",
                List.of(new OrderItem("P002", 2, new BigDecimal("699.99"))),
                LocalDateTime.now().minusDays(3), OrderStatus.DELIVERED),
            new Order("O003", "C001",
                List.of(new OrderItem("P003", 1, new BigDecimal("299.99"))),
                LocalDateTime.now().minusDays(1), OrderStatus.PENDING)
        );
    }
}
```

---

## 9. Testing

### 9.1 Unit Tests

```java
package academy.javaengineering.functional.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import academy.javaengineering.functional.project.model.*;

class OrderTest {
    
    @Test
    void testTotalAmount() {
        Order order = new Order(
            "O001",
            "C001",
            List.of(
                new OrderItem("P001", 2, new BigDecimal("10.00")),
                new OrderItem("P002", 1, new BigDecimal("20.00"))
            ),
            LocalDateTime.now(),
            OrderStatus.PENDING
        );
        
        assertEquals(new BigDecimal("40.00"), order.totalAmount());
    }
    
    @Test
    void testEmptyOrderTotal() {
        Order order = new Order(
            "O001",
            "C001",
            List.of(),
            LocalDateTime.now(),
            OrderStatus.PENDING
        );
        
        assertEquals(BigDecimal.ZERO, order.totalAmount());
    }
}
```

### 9.2 Integration Tests

```java
package academy.javaengineering.functional.project;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import academy.javaengineering.functional.project.model.*;
import academy.javaengineering.functional.project.service.*;

class AnalyticsServiceTest {
    
    private final AnalyticsService service = new AnalyticsService();
    
    @Test
    void testRevenueByCustomer() {
        List<Order> orders = List.of(
            new Order("O001", "C001",
                List.of(new OrderItem("P001", 1, new BigDecimal("100.00"))),
                LocalDateTime.now(), OrderStatus.DELIVERED),
            new Order("O002", "C001",
                List.of(new OrderItem("P002", 1, new BigDecimal("200.00"))),
                LocalDateTime.now(), OrderStatus.DELIVERED)
        );
        
        Map<String, BigDecimal> revenue = service.calculateRevenueByCustomer(orders);
        
        assertEquals(new BigDecimal("300.00"), revenue.get("C001"));
    }
}
```

---

## 10. Performance

### 10.1 Performance Considerations

1. **Parallel Processing**: Use `parallelStream()` for large datasets
2. **Filter Early**: Reduce dataset size as soon as possible
3. **Primitive Streams**: Use `IntStream`, `LongStream`, `DoubleStream` for performance
4. **Cache Results**: Store computed results when appropriate

### 10.2 Benchmarking

```java
package academy.javaengineering.functional.project;

import java.util.*;
import java.util.stream.IntStream;

public class PerformanceBenchmark {
    
    public static void main(String[] args) {
        int size = 10_000_000;
        List<Integer> numbers = IntStream.rangeClosed(1, size).boxed().toList();
        
        // Sequential
        long start = System.nanoTime();
        long sumSeq = numbers.stream().mapToLong(Integer::longValue).sum();
        long seqTime = System.nanoTime() - start;
        
        // Parallel
        start = System.nanoTime();
        long sumPar = numbers.parallelStream().mapToLong(Integer::longValue).sum();
        long parTime = System.nanoTime() - start;
        
        System.out.printf("Sequential: %.2f ms%n", seqTime / 1_000_000.0);
        System.out.printf("Parallel: %.2f ms%n", parTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) seqTime / parTime);
    }
}
```

---

## 11. Extensions

### 11.1 Advanced Features

1. **Real-time Processing**: Add streaming support
2. **Persistence**: Add database integration
3. **REST API**: Expose via REST endpoints
4. **Monitoring**: Add metrics and monitoring

### 11.2 Possible Improvements

1. Add more analytics (cohort analysis, trends)
2. Support custom report templates
3. Add data validation framework
4. Implement caching layer

---

## 12. Summary

This mini project demonstrates how to apply functional programming concepts in a real-world scenario. Key takeaways:

1. **Immutability**: Use records for data models
2. **Composition**: Build complex pipelines from simple functions
3. **Streams**: Process data declaratively
4. **Collectors**: Implement custom aggregation logic
5. **Optional**: Handle null safely
6. **Error Handling**: Use functional patterns for error handling

### Next Steps

- Continue to Module 08: Design Patterns
- Explore advanced functional programming patterns
- Build more complex data processing systems

---

## Resources

1. [Oracle Java Tutorials: Streams](https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html)
2. [Effective Java, 3rd Edition](https://www.oreilly.com/library/view/effective-java/9780134686097/)
3. [Baeldung: Java Streams](https://www.baeldung.com/java-streams)
