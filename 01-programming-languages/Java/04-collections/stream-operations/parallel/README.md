# Parallel Operations

## Table of Contents
1. [Overview](#overview)
2. [Creating Parallel Streams](#creating-parallel-streams)
3. [ForkJoinPool](#forkjoinpool)
4. [Parallel Reduction](#parallel-reduction)
5. [When to Use Parallel](#when-to-use-parallel)
6. [When NOT to Use Parallel](#when-not-to-use-parallel)
7. [Performance Considerations](#performance-considerations)
8. [Practical Examples](#practical-examples)

---

## Overview

Parallel streams split work across multiple threads using ForkJoinPool. They can improve performance for large datasets with CPU-intensive operations.

| Method | Description | Return Type |
|--------|-------------|-------------|
| `parallelStream()` | Parallel from collection | Stream |
| `.parallel()` | Convert to parallel | Stream |
| `.sequential()` | Convert to sequential | Stream |
| `isParallel()` | Check if parallel | boolean |

---

## Creating Parallel Streams

```java
// From collection
Stream<Integer> parallel = List.of(1, 2, 3, 4, 5)
    .parallelStream();

// Convert existing stream
Stream<Integer> parallel2 = IntStream.range(1, 100)
    .boxed()
    .parallel();

// Check if parallel
boolean isPar = parallel.isParallel();  // true
```

---

## ForkJoinPool

Java uses ForkJoinPool for parallel streams:

- **Common pool**: `Runtime.getRuntime().availableProcessors()` threads
- **Work-stealing**: idle threads steal from busy threads
- **Custom pool**: Can override with `ForkJoinPool`

```java
// Default: uses common pool
List<Integer> result = numbers.parallelStream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());

// Custom pool (not recommended for most cases)
ForkJoinPool customPool = new ForkJoinPool(4);
List<Integer> result2 = customPool.submit(() ->
    numbers.parallelStream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList())
).get();
```

---

## Parallel Reduction

```java
// Parallel reduce with combiner
int sum = IntStream.rangeClosed(1, 1000)
    .parallel()
    .reduce(0, Integer::sum, Integer::sum);

// Parallel collect
List<Integer> result = IntStream.rangeClosed(1, 1000)
    .parallel()
    .boxed()
    .collect(Collectors.toList());
```

---

## When to Use Parallel

| Situation | Reason |
|-----------|--------|
| Large datasets (>10,000 elements) | Amortizes thread overhead |
| CPU-intensive operations | Utilizes multiple cores |
| Independent operations | No shared state |
| Stateless operations | No side effects |

```java
// Good candidate for parallel
double average = IntStream.rangeClosed(1, 10_000_000)
    .parallel()
    .mapToDouble(n -> Math.sin(n))
    .average()
    .orElse(0);

// Another good candidate
long count = largeList.parallelStream()
    .filter(item -> expensiveComputation(item))
    .count();
```

---

## When NOT to Use Parallel

| Situation | Reason |
|-----------|--------|
| Small datasets (<10,000) | Overhead > benefit |
| I/O-bound operations | Thread management overhead |
| Shared mutable state | Race conditions |
| Ordered results needed | May lose order |
| Simple operations | Sequential is faster |

```java
// BAD: Small dataset
List<Integer> small = List.of(1, 2, 3);
small.parallelStream().filter(n -> n > 1).collect(Collectors.toList());

// BAD: Shared mutable state
List<String> results = new ArrayList<>();
IntStream.range(1, 1000).parallel().forEach(n ->
    results.add(String.valueOf(n))  // Race condition!
);

// GOOD: Use collect instead
List<String> results2 = IntStream.range(1, 1000)
    .parallel()
    .mapToObj(String::valueOf)
    .collect(Collectors.toList());
```

---

## Performance Considerations

### Benchmarking

```java
long start = System.nanoTime();
long sum = IntStream.rangeClosed(1, 100_000_000).sum();
long sequentialTime = System.nanoTime() - start;

start = System.nanoTime();
long parallelSum = IntStream.rangeClosed(1, 100_000_000)
    .parallel().sum();
long parallelTime = System.nanoTime() - start;

System.out.println("Sequential: " + sequentialTime / 1_000_000 + " ms");
System.out.println("Parallel: " + parallelTime / 1_000_000 + " ms");
```

### When Parallel Wins

| Factor | Sequential | Parallel |
|--------|-----------|----------|
| Small data | Better | Overhead |
| Large CPU-bound | Good | Better |
| I/O-bound | Limited | Limited |
| Stateful ops | Safe | Unsafe |

---

## Practical Examples

### Example 1: Parallel Processing Large Dataset

```java
List<Integer> largeList = IntStream.rangeClosed(1, 1_000_000)
    .boxed()
    .collect(Collectors.toList());

// Sequential
long seqStart = System.nanoTime();
long seqCount = largeList.stream()
    .filter(n -> n % 2 == 0)
    .count();
long seqTime = System.nanoTime() - seqStart;

// Parallel
long parStart = System.nanoTime();
long parCount = largeList.parallelStream()
    .filter(n -> n % 2 == 0)
    .count();
long parTime = System.nanoTime() - parStart;
```

### Example 2: Parallel Sum

```java
long sum = IntStream.rangeClosed(1, 100_000_000)
    .parallel()
    .sum();
```

### Example 3: Parallel Map-Reduce

```java
double result = DoubleStream.generate(Math::random)
    .limit(10_000_000)
    .parallel()
    .map(d -> d * d)
    .reduce(0.0, Double::sum);
```

### Example 4: Parallel Collect

```java
List<String> result = IntStream.rangeClosed(1, 10_000)
    .parallel()
    .mapToObj(n -> "Item " + n)
    .collect(Collectors.toList());
```

---

## Key Takeaways

1. `parallelStream()` creates parallel stream from collection
2. `.parallel()` converts existing stream to parallel
3. ForkJoinPool manages thread execution
4. Use parallel for large, CPU-intensive, stateless operations
5. Avoid parallel for small datasets or I/O-bound work
6. Never use shared mutable state in parallel streams
7. Always benchmark to verify parallel is actually faster
