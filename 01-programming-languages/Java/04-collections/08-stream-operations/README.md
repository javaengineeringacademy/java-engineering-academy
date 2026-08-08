# Stream Operations - Complete Guide

## Scope

This folder covers all Stream API operations: filtering, transforming, reducing, collecting, sorting, combining, and parallel processing.
Examples and exercises integrate stream operations with collection types in real-world scenarios.

## Table of Contents
1. [What Are Stream Operations](#what-are-stream-operations)
2. [Why Use Stream Operations](#why-use-stream-operations)
3. [Stream Pipeline Anatomy](#stream-pipeline-anatomy)
4. [Operation Categories](#operation-categories)
5. [Operation Reference](#operation-reference)
6. [When to Use Streams vs Loops](#when-to-use-streams-vs-loops)
7. [Common Pitfalls](#common-pitfalls)

---

## What Are Stream Operations

Streams process data declaratively - you describe **WHAT** to do, Java figures out **HOW**.

```java
// Imperative: HOW to do it
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.length() > 3) {
        result.add(name.toUpperCase());
    }
}

// Declarative: WHAT to do
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

## Why Use Stream Operations

| Benefit | Description |
|---------|-------------|
| **Declarative** | Describe logic, not implementation steps |
| **Lazy** | Intermediate operations execute only when terminal is invoked |
| **Parallelizable** | `parallelStream()` for automatic multi-core usage |
| **Composable** | Chain operations like building blocks |
| **Concise** | Reduce boilerplate code |

## Stream Pipeline Anatomy

```
Source → Intermediate → Intermediate → Terminal
         Operation      Operation      Operation

names.stream()         .filter(...)    .collect(...)
                       .map(...)
```

- **Source**: Creates the stream (Collection.stream(), Arrays.stream(), Stream.of())
- **Intermediate**: Transforms stream, returns new Stream (lazy)
- **Terminal**: Produces result or side effect (triggers processing)

## Operation Categories

| Type | Purpose | Examples |
|------|---------|----------|
| **Intermediate** | Transform data | filter, map, sorted, distinct, peek |
| **Terminal** | Produce result | collect, forEach, reduce, count, findFirst |
| **Short-circuit** | Stop early | findFirst, anyMatch, allMatch, limit |

---

## Operation Reference

### Filtering
| Method | Description | Return |
|--------|-------------|--------|
| `filter(Predicate)` | Keep matching elements | Stream |
| `distinct()` | Remove duplicates | Stream |
| `takeWhile(Predicate)` | Take while true | Stream |
| `dropWhile(Predicate)` | Drop while true | Stream |

```java
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
```

### Transforming
| Method | Description | Return |
|--------|-------------|--------|
| `map(Function)` | Transform each element | Stream |
| `flatMap(Function)` | Flatten nested streams | Stream |
| `mapToInt/Long/Double()` | Primitive streams | IntStream |

```java
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

### Reducing
| Method | Description | Return |
|--------|-------------|--------|
| `reduce(BinaryOperator)` | Combine to single value | Optional |
| `reduce(identity, accumulator)` | Combine with initial value | T |
| `collect(Collector)` | Collect to collection | R |
| `count()` | Count elements | long |
| `min/max(Comparator)` | Find extremes | Optional |

```java
int sum = numbers.stream().reduce(0, Integer::sum);
```

### Sorting
| Method | Description | Return |
|--------|-------------|--------|
| `sorted()` | Natural order | Stream |
| `sorted(Comparator)` | Custom order | Stream |

```java
List<String> sorted = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());
```

### Combining
| Method | Description | Return |
|--------|-------------|--------|
| `concat(Stream, Stream)` | Join two streams | Stream |
| `Stream.of(values)` | Create from values | Stream |
| `Stream.iterate()` | Infinite from seed | Stream |
| `Stream.generate()` | Infinite from supplier | Stream |

### Parallel
| Method | Description | Return |
|--------|-------------|--------|
| `parallelStream()` | Parallel from collection | Stream |
| `.parallel()` | Convert to parallel | Stream |
| `.sequential()` | Convert to sequential | Stream |

### Collectors
| Method | Description | Return |
|--------|-------------|--------|
| `toList()` | Collect to List | List |
| `toSet()` | Collect to Set | Set |
| `toMap()` | Collect to Map | Map |
| `joining()` | Join strings | String |
| `groupingBy()` | Group by classifier | Map |
| `partitioningBy()` | Partition by predicate | Map |

---

## When to Use Streams vs Loops

| Situation | Use | Reason |
|-----------|-----|--------|
| Simple iteration | Enhanced for | Clearer, less overhead |
| Data transformation pipeline | Stream | Declarative, composable |
| Complex filtering + mapping | Stream | Chain operations naturally |
| Parallel processing | parallelStream | Automatic thread management |
| Breaking early | for loop | More direct |

## Common Pitfalls

### 1. Consuming a Stream Twice
```java
Stream<String> stream = names.stream();
stream.count(); // OK
stream.count(); // IllegalStateException!
```

### 2. Modifying Collection During Stream
```java
names.stream()
    .filter(name -> {
        names.remove(name); // ConcurrentModificationException!
        return true;
    });
```

### 3. Forgetting Terminal Operation
```java
names.stream().filter(n -> n.length() > 3); // Nothing happens!
```

### 4. Side Effects in Intermediate Operations
```java
// BAD
List<String> result = new ArrayList<>();
names.stream().map(name -> {
    result.add(name.toUpperCase()); // Side effect!
    return name.toUpperCase();
});

// GOOD
List<String> result = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

## Performance Considerations

| Factor | Sequential | Parallel |
|--------|-----------|----------|
| Small datasets (<1000) | Better | Overhead too high |
| Large datasets (>10000) | Good | Often better |
| CPU-intensive | Good | Better (if independent) |
| I/O-bound | Limited by I/O | Limited by I/O |

## Additional Resources

- [Oracle Stream Tutorial](https://docs.oracle.com/javase/tutorial/collections/streams/)
- [Java Stream API Javadoc](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/stream/Stream.html)

---

**See individual operation folders for detailed examples and tests.**
