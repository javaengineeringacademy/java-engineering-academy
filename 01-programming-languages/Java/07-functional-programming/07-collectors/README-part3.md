# Topic 07: Collectors (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

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
```
