# Transforming Operations

## Table of Contents
1. [Overview](#overview)
2. [map(Function)](#mapfunction)
3. [flatMap(Function)](#flatmapfunction)
4. [Primitive Streams](#primitive-streams)
5. [Function Functional Interface](#function-functional-interface)
6. [Combining Transformations](#combining-transformations)
7. [Practical Examples](#practical-examples)

---

## Overview

Transforming operations convert elements from one form to another. They are intermediate operations that return a new Stream.

| Method | Description | Return Type |
|--------|-------------|-------------|
| `map(Function)` | One-to-one transformation | Stream |
| `flatMap(Function)` | Flatten nested streams | Stream |
| `mapToInt(Function)` | Transform to int | IntStream |
| `mapToLong(Function)` | Transform to long | LongStream |
| `mapToDouble(Function)` | Transform to double | DoubleStream |

---

## map(Function)

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper)
```

Transforms each element using the mapper function. One-to-one mapping.

```java
List<String> names = List.of("alice", "bob", "charlie");

// Transform to uppercase
List<String> upper = names.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// Result: [ALICE, BOB, CHARLIE]

// Transform to length
List<Integer> lengths = names.stream()
    .map(String::length)
    .collect(Collectors.toList());
// Result: [5, 3, 7]

// Transform with lambda
List<String> greetings = names.stream()
    .map(name -> "Hello, " + name + "!")
    .collect(Collectors.toList());
// Result: [Hello, alice!, Hello, bob!, Hello, charlie!]
```

---

## flatMap(Function)

```java
<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)
```

Transforms each element to a stream, then flattens all streams into one.

```java
// Flattening nested lists
List<List<Integer>> nested = List.of(
    List.of(1, 2, 3),
    List.of(4, 5),
    List.of(6, 7, 8, 9)
);
List<Integer> flat = nested.stream()
    .flatMap(Collection::stream)
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5, 6, 7, 8, 9]

// Splitting strings into characters
List<String> words = List.of("Hello", "World");
List<Character> chars = words.stream()
    .flatMap(word -> word.chars()
        .mapToObj(c -> (char) c))
    .collect(Collectors.toList());
// Result: [H, e, l, l, o, W, o, r, l, d]

// Splitting sentences into words
List<String> sentences = List.of("Hello World", "Java Streams");
List<String> words2 = sentences.stream()
    .flatMap(sentence -> Arrays.stream(sentence.split(" ")))
    .collect(Collectors.toList());
// Result: [Hello, World, Java, Streams]
```

### map vs flatMap

```java
// map: T → R (one-to-one)
Stream<String> stream1 = names.stream()
    .map(name -> name.toUpperCase()); // Each name → one uppercase name

// flatMap: T → Stream<R> (one-to-many)
Stream<Character> stream2 = names.stream()
    .flatMap(name -> name.chars()
        .mapToObj(c -> (char) c)); // Each name → multiple characters
```

---

## Primitive Streams

### mapToInt / mapToLong / mapToDouble

```java
// Convert to IntStream for primitive operations
List<String> names = List.of("Alice", "Bob", "Charlie");
int totalLength = names.stream()
    .mapToInt(String::length)
    .sum();
// Result: 12

// IntStream methods: sum(), average(), min(), max(), count()
OptionalDouble average = names.stream()
    .mapToInt(String::length)
    .average();
// Result: OptionalDouble[4.0]

// Convert back to Stream
Stream<Integer> boxed = IntStream.range(1, 5)
    .boxed()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4]
```

### IntStream.range() / rangeClosed()

```java
// range: 1 to 4 (exclusive)
List<Integer> range = IntStream.range(1, 5)
    .boxed()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4]

// rangeClosed: 1 to 5 (inclusive)
List<Integer> rangeClosed = IntStream.rangeClosed(1, 5)
    .boxed()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5]
```

---

## Function Functional Interface

`Function<T, R>` is a functional interface: `T → R`

```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);

    // Default methods
    default <V> Function<V, R> compose(Function<? super V, ? extends T> before);
    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after);

    // Static methods
    static <T> Function<T, T> identity();
}
```

### Creating Functions

```java
// Lambda
Function<String, Integer> toLength = String::length;

// Method reference
Function<String, String> toUpper = String::toUpperCase;

// Composition
Function<String, String> trim = String::trim;
Function<String, String> trimThenUpper = trim.andThen(toUpper);

// Identity
Function<String, String> identity = Function.identity();
```

---

## Combining Transformations

```java
List<String> names = List.of("alice", "bob", "charlie", "david");

// Chain multiple transformations
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .map(name -> name + "!")
    .collect(Collectors.toList());
// Result: [ALICE!, CHARLIE!, DAVID!]

// FlatMap then Map
List<String> sentences = List.of("Hello World", "Java Streams");
List<String> upperWords = sentences.stream()
    .flatMap(s -> Arrays.stream(s.split(" ")))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// Result: [HELLO, WORLD, JAVA, STREAMS]
```

---

## Practical Examples

### Example 1: Extract and Transform
```java
List<String> emails = List.of(
    "alice@example.com",
    "bob@test.org",
    "charlie@example.com"
);

List<String> domains = emails.stream()
    .map(email -> email.split("@")[1])
    .distinct()
    .collect(Collectors.toList());
// Result: [example.com, test.org]
```

### Example 2: Nested Object Transformation
```java
record Employee(String name, List<String> skills) {}

List<Employee> employees = List.of(
    new Employee("Alice", List.of("Java", "Python")),
    new Employee("Bob", List.of("JavaScript", "TypeScript"))
);

List<String> allSkills = employees.stream()
    .flatMap(emp -> emp.skills().stream())
    .distinct()
    .sorted()
    .collect(Collectors.toList());
// Result: [Java, JavaScript, Python, TypeScript]
```

### Example 3: String Manipulation
```java
List<String> words = List.of("hello", "world", "java");

List<String> result = words.stream()
    .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
    .collect(Collectors.toList());
// Result: [Hello, World, Java]
```

### Example 4: Parsing Numbers
```java
List<String> numbers = List.of("1", "2", "3", "4", "5");

int sum = numbers.stream()
    .mapToInt(Integer::parseInt)
    .sum();
// Result: 15
```

### Example 5: Creating Custom Objects
```java
List<String> names = List.of("Alice", "Bob", "Charlie");

record PersonScore(String name, int score) {}

List<PersonScore> scores = names.stream()
    .map(name -> new PersonScore(name, name.length() * 10))
    .collect(Collectors.toList());
// Result: [PersonScore[name=Alice, score=50], ...]
```

---

## Key Takeaways

1. `map()` transforms each element one-to-one
2. `flatMap()` flattens nested streams into one
3. Primitive streams (IntStream, etc.) provide efficient numeric operations
4. Functions can be composed with `andThen()` and `compose()`
5. Combine `filter()` and `map()` for powerful data pipelines
