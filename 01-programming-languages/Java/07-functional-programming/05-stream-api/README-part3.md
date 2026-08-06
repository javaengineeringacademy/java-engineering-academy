# Topic 05: Stream API (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

        return List.of(
            new Order("O1", "Alice", 
                List.of(new OrderItem("P1", "Laptop", 1, new BigDecimal("999.99"))),
                OrderStatus.COMPLETED, LocalDateTime.now().minusDays(2)),
            new Order("O2", "Bob",
                List.of(new OrderItem("P2", "Phone", 1, new BigDecimal("699.99"))),
                OrderStatus.COMPLETED, LocalDateTime.now().minusDays(5)),
            new Order("O3", "Alice",
                List.of(new OrderItem("P3", "Tablet", 1, new BigDecimal("399.99"))),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(1))
        );
    }
}
```

---

## 15. Performance

### 15.1 Stream vs Loop Performance

| Operation | Stream | Loop | Winner |
|-----------|--------|------|--------|
| **Simple iteration** | Baseline | Baseline | Tie |
| **Complex pipelines** | Better (JIT optimization) | Baseline | Stream |
| **Parallel processing** | Much better | Manual threads | Stream |
| **Small datasets** | Slightly slower | Slightly faster | Loop |

### 15.2 Performance Tips

1. **Use primitive streams**: `IntStream`, `LongStream`, `DoubleStream` avoid boxing
2. **Avoid parallel for small datasets**: Overhead exceeds benefit
3. **Use `toList()` instead of `collect(Collectors.toList())`**: More efficient
4. **Short-circuit when possible**: `findFirst()`, `findAny()`, `anyMatch()`
5. **Reuse stream operations**: Extract to methods when possible

---

## 16. Best Practices

1. **Keep pipelines simple**: One operation per line
2. **Use method references**: More readable
3. **Prefer `toList()` over `collect(Collectors.toList())`**
4. **Use parallel streams for large, CPU-bound datasets**
5. **Avoid side effects in stream operations**
6. **Use `peek()` for debugging only**
7. **Consider lazy evaluation for expensive operations**

---

## 17. Common Mistakes

### Mistake 1: Modifying Collection During Stream

```java
// WRONG: ConcurrentModificationException
List<String> list = new ArrayList<>(List.of("a", "b", "c"));
list.stream()
    .filter(s -> {
        list.remove(s);  // Modifying collection!
        return true;
    })
    .toList();

// CORRECT: Process separately
List<String> toRemove = list.stream()
    .filter(s -> s.length() > 1)
    .toList();
list.removeAll(toRemove);
```

### Mistake 2: Using Parallel for Small Datasets

```java
// WRONG: Overhead exceeds benefit
list.parallelStream()
    .filter(s -> s.length() > 3)
    .toList();  // For small lists, sequential is faster

// CORRECT: Use sequential for small datasets
list.stream()
    .filter(s -> s.length() > 3)
    .toList();
```

---

## 18. Pitfalls

1. **Single-use**: Streams can only be consumed once
2. **Ordering**: Parallel streams don't guarantee order
3. **Stateful operations**: `distinct()`, `sorted()` require full traversal
4. **Side effects**: Can break parallel processing

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

| Feature | Stream | Loop | Parallel Stream |
|---------|--------|------|-----------------|
| **Syntax** | Declarative | Imperative | Declarative |
| **Readability** | Excellent | Good | Good |
| **Performance** | Good | Good | Better (CPU-bound) |
| **Parallelization** | Built-in | Manual | Automatic |
| **Memory** | More objects | Less | More |

---

## 21. Decision Tree

```
Should you use a Stream?

┌─ Are you processing a collection?
│  ├─ YES → Consider Stream
│  └─ NO → Use other approaches
│
├─ Is the pipeline complex (3+ operations)?
│  ├─ YES → Use Stream for readability
│  └─ NO → Either is fine
│
├─ Is the dataset large (>10,000 elements)?
│  ├─ YES → Consider parallel stream
│  └─ NO → Use sequential stream
│
├─ Are you doing CPU-bound work?
│  ├─ YES → Parallel stream may help
│  └─ NO → Sequential stream
│
└─ Do you need side effects?
   ├─ YES → Avoid streams or use peek()
   └─ NO → Use streams freely
```

---

## 22. Interview Questions

### Q1: What is the difference between intermediate and terminal operations?

**Answer**: Intermediate operations return a new stream and are lazy. Terminal operations trigger processing and produce a result. Intermediate operations like `filter()`, `map()`, `sorted()` are deferred until a terminal operation like `collect()`, `forEach()`, `reduce()` is invoked.

### Q2: When should you use parallel streams?

**Answer**: Use parallel streams when:
1. The dataset is large (>10,000 elements)
2. The operation is CPU-bound
3. Order doesn't matter
4. Each element can be processed independently

### Q3: What is lazy evaluation in streams?

**Answer**: Lazy evaluation means intermediate operations don't execute until a terminal operation is invoked. This allows the stream library to optimize the pipeline, potentially fusing operations or short-circuiting.

### Q4: Can you reuse a Stream?

**Answer**: No. Streams are single-use. Once a terminal operation is invoked, the stream is consumed and cannot be reused. Create a new stream from the source if you need to process again.

### Q5: What is the difference between `reduce()` and `collect()`?

**Answer**: `reduce()` combines elements into a single value using a binary operator. `collect()` accumulates elements into a mutable container using a Collector. `reduce()` is for reduction, `collect()` is for mutable accumulation.

---

## 23. Exercises

### Exercise 1: Basic Stream Operations
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

### Exercise 3: Custom Collector
Implement a custom collector that:
1. Collects strings into a comma-separated string
2. Handles empty strings
3. Truncates to a maximum length

---

## 24. Assignments

### Assignment 1: Data Processing Pipeline
Build a data processing pipeline that:
1. Reads data from a source
2. Filters invalid records
3. Transforms data
4. Aggregates results
5. Outputs to a destination

### Assignment 2: Parallel Processing
Implement a parallel processing system that:
1. Processes large datasets in parallel
2. Uses custom ForkJoinPool
3. Handles exceptions gracefully
4. Measures performance

### Assignment 3: Stream Utilities
Create a utility class with stream helpers:
1. Custom `toUnmodifiableList()` collector
2. Custom `groupingBy()` with multiple values
3. Custom `partitioningBy()` with custom logic

---

## 25. Mini Project

### Project: Stream-Based Analytics Engine

Build an analytics engine using Stream API:

**Requirements:**
1. Process event streams
2. Calculate aggregates (sum, count, average)
3. Support time-based windowing
4. Implement custom collectors
5. Support parallel processing

**Starter Code:**
```java
package academy.javaengineering.functional.streams.project;

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

The Stream API provides a powerful, declarative approach to data processing. Key takeaways:

1. **Pipelines**: Source → Intermediate → Terminal → Result
2. **Lazy Evaluation**: Intermediate operations are deferred
3. **Parallel Processing**: Built-in support via `parallelStream()`
4. **Functional Style**: Use lambdas and method references
5. **Single-use**: Streams cannot be reused

### Next Steps
- Topic 06: Stream Operations — Advanced stream operations
- Topic 07: Collectors — Custom collector implementation

---

## 27. References

1. [Oracle Java Tutorials: Streams](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/stream/package-summary.html)
2. [Java Language Specification: Streams](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 43](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Streams](https://www.baeldung.com/java-streams)
```
