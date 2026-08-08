# Reducing Operations

## Table of Contents
1. [Overview](#overview)
2. [reduce(BinaryOperator)](#reducebinaryoperator)
3. [reduce(identity, accumulator)](#reduceidentity-accumulator)
4. [count()](#count)
5. [min/max(Comparator)](#minmaxcomparator)
6. [BinaryOperator Functional Interface](#binaryoperator-functional-interface)
7. [Collectors for Reduction](#collectors-for-reduction)
8. [Practical Examples](#practical-examples)

---

## Overview

Reducing operations combine stream elements into a single result. They are terminal operations that produce a value.

| Method | Description | Return Type |
|--------|-------------|-------------|
| `reduce(BinaryOperator)` | Combine to single value | Optional |
| `reduce(identity, accumulator)` | Combine with initial value | T |
| `reduce(identity, accumulator, combiner)` | Parallel reduction | T |
| `collect(Collector)` | Collect to collection | R |
| `count()` | Count elements | long |
| `min(Comparator)` | Find minimum | Optional |
| `max(Comparator)` | Find maximum | Optional |

---

## reduce(BinaryOperator)

```java
Optional<T> reduce(BinaryOperator<T> accumulator)
```

Combines all elements using the accumulator. No initial value.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Sum
Optional<Integer> sum = numbers.stream()
    .reduce((a, b) -> a + b);
// Result: Optional[15]

// Using method reference
Optional<Integer> sum2 = numbers.stream()
    .reduce(Integer::sum);
// Result: Optional[15]

// Max
Optional<Integer> max = numbers.stream()
    .reduce(Integer::max);
// Result: Optional[5]

// Concatenate strings
List<String> words = List.of("Hello", " ", "World");
Optional<String> sentence = words.stream()
    .reduce(String::concat);
// Result: Optional[Hello World]
```

**Note**: Returns Optional because the stream might be empty.

---

## reduce(identity, accumulator)

```java
T reduce(T identity, BinaryOperator<T> accumulator)
```

Combines elements with an initial value. Always returns a value.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);

// Sum with identity
int sum = numbers.stream()
    .reduce(0, Integer::sum);
// Result: 15

// Product with identity
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);
// Result: 120

// Concatenate with identity
String joined = numbers.stream()
    .map(String::valueOf)
    .reduce("", String::concat);
// Result: "12345"

// Join with separator
String csv = numbers.stream()
    .map(String::valueOf)
    .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
// Result: "1, 2, 3, 4, 5"
```

### Parallel Reduction

```java
// For parallel streams, use three-argument reduce
int sum = numbers.parallelStream()
    .reduce(0, Integer::sum, Integer::sum);
```

---

## count()

```java
long count()
```

Returns the count of elements in the stream.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

long count = names.stream()
    .count();
// Result: 4

long longNames = names.stream()
    .filter(name -> name.length() > 4)
    .count();
// Result: 2

long evenCount = IntStream.rangeClosed(1, 10)
    .filter(n -> n % 2 == 0)
    .count();
// Result: 5
```

---

## min/max(Comparator)

```java
Optional<T> min(Comparator<? super T> comparator)
Optional<T> max(Comparator<? super T> comparator)
```

Finds the minimum or maximum element using the comparator.

```java
List<Integer> numbers = List.of(3, 1, 4, 1, 5, 9, 2, 6);

Optional<Integer> min = numbers.stream()
    .min(Integer::compareTo);
// Result: Optional[1]

Optional<Integer> max = numbers.stream()
    .max(Integer::compareTo);
// Result: Optional[9]

// With Comparator.comparing
List<String> names = List.of("Alice", "Bob", "Charlie");
Optional<String> shortest = names.stream()
    .min(Comparator.comparingInt(String::length));
// Result: Optional[Bob]

Optional<String> longest = names.stream()
    .max(Comparator.comparingInt(String::length));
// Result: Optional[Charlie]

// Chained comparators
record Person(String name, int age, String city) {}
List<Person> people = List.of(
    new Person("Alice", 30, "New York"),
    new Person("Bob", 25, "Chicago"),
    new Person("Charlie", 35, "New York")
);

Optional<Person> oldest = people.stream()
    .max(Comparator.comparingInt(Person::age));
// Result: Optional[Charlie]

// Multiple criteria
Optional<Person> result = people.stream()
    .max(Comparator.comparing(Person::city)
        .thenComparing(Comparator.comparingInt(Person::age).reversed()));
```

---

## BinaryOperator Functional Interface

`BinaryOperator<T>` is a functional interface: `(T, T) → T`

```java
@FunctionalInterface
public interface BinaryOperator<T> extends BiFunction<T, T, T> {
    // Static methods
    static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator);
    static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator);
}
```

### Creating BinaryOperators

```java
// Lambda
BinaryOperator<Integer> sum = (a, b) -> a + b;

// Method reference
BinaryOperator<Integer> sum2 = Integer::sum;

// Using static factories
BinaryOperator<Integer> min = BinaryOperator.minBy(Integer::compareTo);
BinaryOperator<Integer> max = BinaryOperator.maxBy(Integer::compareTo);
```

---

## Collectors for Reduction

Collectors provide many reduction patterns:

```java
// To collection
List<String> list = stream.collect(Collectors.toList());
Set<String> set = stream.collect(Collectors.toSet());

// Joining
String joined = stream.collect(Collectors.joining(", "));
String joinedWithPrefix = stream.collect(Collectors.joining(", ", "[", "]"));

// Summarizing
IntSummaryStatistics stats = stream.collect(Collectors.summarizingInt(String::length));
// Returns count, sum, min, max, average

// Grouping
Map<String, List<String>> grouped = stream.collect(
    Collectors.groupingBy(s -> s.substring(0, 1))
);

// Partitioning
Map<Boolean, List<String>> partitioned = stream.collect(
    Collectors.partitioningBy(s -> s.length() > 3)
);
```

---

## Practical Examples

### Example 1: Calculate Statistics
```java
List<Double> prices = List.of(19.99, 29.99, 9.99, 49.99, 14.99);

double total = prices.stream()
    .reduce(0.0, Double::sum);

double max = prices.stream()
    .reduce(Double::max)
    .orElse(0.0);

double min = prices.stream()
    .reduce(Double::min)
    .orElse(0.0);
```

### Example 2: Build a String
```java
List<String> words = List.of("Java", "is", "awesome");

String sentence = words.stream()
    .reduce("", (a, b) -> a.isEmpty() ? b : a + " " + b);
// Result: "Java is awesome"

// Or using joining collector
String sentence2 = words.stream()
    .collect(Collectors.joining(" "));
```

### Example 3: Find Longest String
```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

String longest = names.stream()
    .reduce("", (a, b) -> a.length() >= b.length() ? a : b);
// Result: "Charlie"

// Or using max collector
String longest2 = names.stream()
    .max(Comparator.comparingInt(String::length))
    .orElse("");
```

### Example 4: Factorial
```java
int n = 5;
int factorial = IntStream.rangeClosed(1, n)
    .reduce(1, (a, b) -> a * b);
// Result: 120
```

### Example 5: Flatten and Sum
```java
List<List<Integer>> matrix = List.of(
    List.of(1, 2, 3),
    List.of(4, 5, 6),
    List.of(7, 8, 9)
);

int totalSum = matrix.stream()
    .flatMap(Collection::stream)
    .reduce(0, Integer::sum);
// Result: 45
```

---

## Key Takeaways

1. `reduce()` combines elements into a single value
2. Two-argument `reduce()` returns Optional (might be empty)
3. Three-argument `reduce()` with identity always returns a value
4. `count()` returns long, always non-negative
5. `min()` and `max()` return Optional
6. Use Collectors for more complex reduction patterns
