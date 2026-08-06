# Topic 06: Stream Operations (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

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

1. [Oracle Java Tutorials: Stream Operations](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/stream/package-summary.html)
2. [Java Language Specification: Stream Operations](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 43](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Stream Operations](https://www.baeldung.com/java-streams)
