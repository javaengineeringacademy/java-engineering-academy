# Stream Operations

## Overview

The Stream API (Java 8+) provides functional-style operations on collections. Streams support lazy evaluation, pipelining, and parallel processing, enabling concise and expressive data processing.

## Learning Objectives

- Understand intermediate vs terminal operations
- Master common stream operations (filter, map, reduce, collect)
- Learn about parallel streams and when to use them
- Understand stream laziness and short-circuit operations
- Apply Collectors for complex aggregations

## Intermediate Operations (Lazy)

| Operation | Description | Example |
|-----------|-------------|---------|
| `filter()` | Select elements matching predicate | `stream.filter(s -> s.length() > 3)` |
| `map()` | Transform elements | `stream.map(String::toUpperCase)` |
| `flatMap()` | Flatten nested structures | `stream.flatMap(Collection::stream)` |
| `distinct()` | Remove duplicates | `stream.distinct()` |
| `sorted()` | Sort elements | `stream.sorted()` |
| `peek()` | Debug/inspect | `stream.peek(System.out::println)` |
| `limit()` | Take first N elements | `stream.limit(5)` |
| `skip()` | Skip first N elements | `stream.skip(3)` |

## Terminal Operations (Trigger Execution)

| Operation | Description | Example |
|-----------|-------------|---------|
| `forEach()` | Iterate | `stream.forEach(System.out::println)` |
| `collect()` | Accumulate to collection | `stream.collect(Collectors.toList())` |
| `reduce()` | Combine elements | `stream.reduce(0, Integer::sum)` |
| `count()` | Count elements | `stream.count()` |
| `anyMatch()` | Check if any match | `stream.anyMatch(s -> s.startsWith("A"))` |
| `allMatch()` | Check if all match | `stream.allMatch(s -> s.length() > 2)` |
| `noneMatch()` | Check if none match | `stream.noneMatch(s -> s.isEmpty())` |
| `findFirst()` | Find first element | `stream.findFirst()` |
| `min()` / `max()` | Find minimum/maximum | `stream.min(Comparator.naturalOrder())` |

## Collectors

```java
// To collection
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());

// To map
Map<String, Integer> map = stream.collect(Collectors.toMap(s -> s, String::length));

// Joining
String joined = stream.collect(Collectors.joining(", "));

// Grouping
Map<Integer, List<String>> grouped = stream.collect(Collectors.groupingBy(String::length));

// Partitioning
Map<Boolean, List<String>> partitioned = stream.collect(Collectors.partitioningBy(s -> s.length() > 3));

// Summarizing
IntSummaryStatistics stats = stream.collect(Collectors.summarizingInt(String::length));
```

## Parallel Streams

```java
// Parallel processing
long count = list.parallelStream()
    .filter(s -> s.length() > 3)
    .count();

// Custom thread pool
ForkJoinPool customPool = new ForkJoinPool(4);
customPool.submit(() ->
    list.parallelStream().forEach(System.out::println)
);
```

## Best Practices

- Use method references (`String::length`) over lambdas when possible
- Prefer `toList()` (Java 16+) over `collect(Collectors.toList())`
- Avoid side effects in stream operations
- Use `peek()` for debugging only
- Consider parallel streams only for large datasets with CPU-intensive operations
- Don't create unnecessary intermediate collections
