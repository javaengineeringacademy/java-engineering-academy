# Collecting Operations

## Table of Contents
1. [Overview](#overview)
2. [Collector Interface](#collector-interface)
3. [toList() / toSet()](#tolist--toset)
4. [toMap()](#tomap)
5. [joining()](#joining)
6. [groupingBy()](#groupingby)
7. [partitioningBy()](#partitioningby)
8. [summarizingInt/Long/Double()](#summarizingintlongdouble)
9. [Custom Collectors](#custom-collectors)
10. [Practical Examples](#practical-examples)

---

## Overview

Collecting operations gather stream elements into collections or produce summary results. They are terminal operations.

| Method | Description | Return Type |
|--------|-------------|-------------|
| `collect(Collector)` | Generic collection | R |
| `toList()` | Collect to List | List |
| `toSet()` | Collect to Set | Set |
| `toMap()` | Collect to Map | Map |
| `joining()` | Join strings | String |
| `groupingBy()` | Group by classifier | Map |
| `partitioningBy()` | Partition by predicate | Map |
| `summarizingInt()` | Int statistics | IntSummaryStatistics |
| `counting()` | Count elements | Long |
| `reducing()` | Reduce with collector | Optional |

---

## Collector Interface

```java
Collector<T, A, R> {
    // T: input type
    // A: accumulator type
    // R: result type

    Supplier<A> supplier();           // Create accumulator
    BiConsumer<A, T> accumulator();   // Add element
    BinaryOperator<A> combiner();     // Combine accumulators
    Function<A, R> finisher();        // Transform to result
    Set<Collector.Characteristics> characteristics();
}
```

---

## toList() / toSet()

```java
// Collect to List (maintains order)
List<String> list = stream.collect(Collectors.toList());

// Collect to Set (removes duplicates, no guaranteed order)
Set<String> set = stream.collect(Collectors.toSet());

// Collect to unmodifiable list (Java 16+)
List<String> unmodifiable = stream.collect(Collectors.toUnmodifiableList());
```

```java
List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3);

List<Integer> list = numbers.stream()
    .collect(Collectors.toList());
// Result: [1, 2, 2, 3, 3, 3]

Set<Integer> set = numbers.stream()
    .collect(Collectors.toSet());
// Result: [1, 2, 3] (order may vary)
```

---

## toMap()

```java
// Basic toMap
Map<String, Integer> map = stream.collect(
    Collectors.toMap(
        keyMapper,      // T → K
        valueMapper     // T → V
    )
);

// With merge function (handle duplicates)
Map<String, Integer> map = stream.collect(
    Collectors.toMap(
        keyMapper,
        valueMapper,
        mergeFunction   // (V1, V2) → V
    )
);

// With map supplier (specific Map implementation)
Map<String, Integer> map = stream.collect(
    Collectors.toMap(
        keyMapper,
        valueMapper,
        mergeFunction,
        TreeMap::new     // Supplier
    )
);
```

```java
// Create map from names to lengths
List<String> names = List.of("Alice", "Bob", "Charlie");
Map<String, Integer> nameLengths = names.stream()
    .collect(Collectors.toMap(
        name -> name,
        String::length
    ));
// Result: {Alice=5, Bob=3, Charlie=7}

// Handle duplicate keys
Map<Character, String> firstByChar = names.stream()
    .collect(Collectors.toMap(
        name -> name.charAt(0),
        name -> name,
        (existing, replacement) -> existing  // Keep first
    ));
// Result: {A=Alice, B=Bob, C=Charlie}
```

---

## joining()

```java
// Join all strings
String joined = stream.collect(Collectors.joining());

// Join with delimiter
String joined = stream.collect(Collectors.joining(", "));

// Join with delimiter, prefix, suffix
String joined = stream.collect(Collectors.joining(", ", "[", "]"));
```

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

String joined = names.stream()
    .collect(Collectors.joining());
// Result: "AliceBobCharlie"

String joinedWithDelimiter = names.stream()
    .collect(Collectors.joining(", "));
// Result: "Alice, Bob, Charlie"

String joinedWithFixes = names.stream()
    .collect(Collectors.joining(", ", "{", "}"));
// Result: "{Alice, Bob, Charlie}"
```

---

## groupingBy()

```java
// Group by classifier
Map<K, List<T>> map = stream.collect(
    Collectors.groupingBy(classifier)
);

// Group by classifier with downstream collector
Map<K, D> map = stream.collect(
    Collectors.groupingBy(classifier, downstreamCollector)
);
```

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David", "Eve");

// Group by first letter
Map<Character, List<String>> byFirstLetter = names.stream()
    .collect(Collectors.groupingBy(name -> name.charAt(0)));
// Result: {A=[Alice], B=[Bob], C=[Charlie], D=[David], E=[Eve]}

// Group by length
Map<Integer, List<String>> byLength = names.stream()
    .collect(Collectors.groupingBy(String::length));
// Result: {3=[Bob, Eve], 5=[Alice, David], 7=[Charlie]}

// Group by length with counting
Map<Integer, Long> countByLength = names.stream()
    .collect(Collectors.groupingBy(
        String::length,
        Collectors.counting()
    ));
// Result: {3=2, 5=2, 7=1}

// Group by first letter with joining
Map<Character, String> joinedByLetter = names.stream()
    .collect(Collectors.groupingBy(
        name -> name.charAt(0),
        Collectors.joining(", ")
    ));
// Result: {A=Alice, B=Bob, C=Charlie, D=David, E=Eve}
```

---

## partitioningBy()

```java
// Partition by predicate (always two groups: true and false)
Map<Boolean, List<T>> map = stream.collect(
    Collectors.partitioningBy(predicate)
);

// Partition with downstream collector
Map<Boolean, D> map = stream.collect(
    Collectors.partitioningBy(predicate, downstreamCollector)
);
```

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Partition evens and odds
Map<Boolean, List<Integer>> partitioned = numbers.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
// Result: {false=[1, 3, 5, 7, 9], true=[2, 4, 6, 8, 10]}

// Partition with counting
Map<Boolean, Long> counts = numbers.stream()
    .collect(Collectors.partitioningBy(
        n -> n % 2 == 0,
        Collectors.counting()
    ));
// Result: {false=5, true=5}

// Partition with summing
Map<Boolean, Integer> sums = numbers.stream()
    .collect(Collectors.partitioningBy(
        n -> n % 2 == 0,
        Collectors.summingInt(Integer::intValue)
    ));
// Result: {false=25, true=30}
```

---

## summarizingInt/Long/Double()

```java
// Get statistics
IntSummaryStatistics stats = stream.collect(
    Collectors.summarizingInt(valueMapper)
);

// Stats include: count, sum, min, max, average
```

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

IntSummaryStatistics lengthStats = names.stream()
    .collect(Collectors.summarizingInt(String::length));

System.out.println("Count: " + lengthStats.getCount());        // 4
System.out.println("Sum: " + lengthStats.getSum());            // 19
System.out.println("Min: " + lengthStats.getMin());            // 3
System.out.println("Max: " + lengthStats.getMax());            // 7
System.out.println("Average: " + lengthStats.getAverage());    // 4.75

// Can also be done with mapToInt
IntSummaryStatistics stats2 = names.stream()
    .mapToInt(String::length)
    .summaryStatistics();
```

---

## Custom Collectors

Create your own collectors using `Collector.of()`:

```java
// Custom collector to join strings
Collector<String, ?, String> customJoiner = Collector.of(
    StringBuilder::new,                    // supplier
    StringBuilder::append,                 // accumulator
    StringBuilder::append,                 // combiner
    StringBuilder::toString                // finisher
);

String result = names.stream()
    .collect(customJoiner);
```

---

## Practical Examples

### Example 1: Employee Report
```java
record Employee(String name, String department, double salary) {}
List<Employee> employees = getEmployees();

// Group by department
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::department));

// Average salary by department
Map<String, Double> avgSalary = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.averagingDouble(Employee::salary)
    ));

// Employee names by department
Map<String, String> namesByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::department,
        Collectors.mapping(Employee::name, Collectors.joining(", "))
    ));
```

### Example 2: Word Frequency
```java
String text = "hello world hello java world hello";
Map<String, Long> wordFrequency = Arrays.stream(text.split(" "))
    .collect(Collectors.groupingBy(
        word -> word,
        Collectors.counting()
    ));
// Result: {hello=3, world=2, java=1}
```

### Example 3: Partition Adults
```java
record Person(String name, int age) {}
List<Person> people = getPeople();

Map<Boolean, List<Person>> partitioned = people.stream()
    .collect(Collectors.partitioningBy(p -> p.age() >= 18));

List<Person> adults = partitioned.get(true);
List<Person> minors = partitioned.get(false);
```

### Example 4: Statistics Report
```java
List<Integer> numbers = List.of(10, 20, 30, 40, 50);
IntSummaryStatistics stats = numbers.stream()
    .collect(Collectors.summarizingInt(Integer::intValue));

System.out.printf("Count: %d, Sum: %d, Avg: %.2f%n",
    stats.getCount(), stats.getSum(), stats.getAverage());
```

---

## Key Takeaways

1. `toList()` and `toSet()` are the simplest collectors
2. `toMap()` requires key and value mappers
3. `joining()` is convenient for string concatenation
4. `groupingBy()` creates multi-valued maps
5. `partitioningBy()` creates two-group maps (true/false)
6. `summarizingInt/Long/Double()` provide statistics
7. Custom collectors can be created with `Collector.of()`
