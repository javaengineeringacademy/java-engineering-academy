# Combining Operations

## Table of Contents
1. [Overview](#overview)
2. [concat(Stream, Stream)](#concatstream-stream)
3. [Stream.of(values)](#streamofvalues)
4. [Stream.empty()](#streamempty)
5. [Stream.iterate()](#streamiterate)
6. [Stream.generate()](#streamgenerate)
7. [Combining Multiple Streams](#combining-multiple-streams)
8. [Practical Examples](#practical-examples)

---

## Overview

Combining operations create or merge streams. They help build streams from various sources.

| Method | Description | Return Type |
|--------|-------------|-------------|
| `concat(Stream, Stream)` | Join two streams | Stream |
| `Stream.of(values)` | Create from values | Stream |
| `Stream.empty()` | Empty stream | Stream |
| `Stream.iterate()` | Infinite from seed | Stream |
| `Stream.generate()` | Infinite from supplier | Stream |
| `Stream.Builder` | Build stream incrementally | Stream |

---

## concat(Stream, Stream)

```java
static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b)
```

Concatenates two streams into one.

```java
Stream<Integer> stream1 = Stream.of(1, 2, 3);
Stream<Integer> stream2 = Stream.of(4, 5, 6);

Stream<Integer> combined = Stream.concat(stream1, stream2);
List<Integer> result = combined.collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5, 6]
```

**Note**: Both streams are consumed. Cannot reuse.

---

## Stream.of(values)

```java
static <T> Stream<T> of(T... values)
```

Creates a stream from values.

```java
// Create stream from values
Stream<String> stream = Stream.of("Alice", "Bob", "Charlie");
List<String> result = stream.collect(Collectors.toList());
// Result: [Alice, Bob, Charlie]

// Create stream from array
Integer[] numbers = {1, 2, 3, 4, 5};
Stream<Integer> fromArray = Stream.of(numbers);

// Create stream from single value
Stream<Integer> single = Stream.of(42);
```

---

## Stream.empty()

```java
static <T> Stream<T> empty()
```

Creates an empty stream.

```java
// Empty stream
Stream<String> empty = Stream.empty();
long count = empty.count();
// Result: 0

// Useful for conditional stream building
Stream<String> stream = condition ? Stream.of("value") : Stream.empty();
```

---

## Stream.iterate()

```java
static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)
static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> f)  // Java 9
```

Creates an infinite stream by repeatedly applying a function.

```java
// Infinite stream of powers of 2
Stream<Integer> powersOf2 = Stream.iterate(1, n -> n * 2);
List<Integer> first10 = powersOf2.limit(10).collect(Collectors.toList());
// Result: [1, 2, 4, 8, 16, 32, 64, 128, 256, 512]

// Fibonacci sequence
Stream<int[]> fibonacci = Stream.iterate(
    new int[]{0, 1},
    fib -> new int[]{fib[1], fib[0] + fib[1]}
);
List<Integer> fibNumbers = fibonacci
    .limit(10)
    .map(fib -> fib[0])
    .collect(Collectors.toList());
// Result: [0, 1, 1, 2, 3, 5, 8, 13, 21, 34]

// Java 9+ with predicate
Stream<Integer> naturals = Stream.iterate(1, n -> n <= 100, n -> n + 1);
List<Integer> result = naturals.collect(Collectors.toList());
// Result: [1, 2, ..., 100]
```

---

## Stream.generate()

```java
static <T> Stream<T> generate(Supplier<? extends T> s)
```

Creates an infinite stream from a supplier.

```java
// Generate random numbers
Stream<Double> randoms = Stream.generate(Math::random);
List<Double> first5 = randoms.limit(5).collect(Collectors.toList());

// Generate constant value
Stream<String> zeros = Stream.generate(() -> "0");
List<String> first3 = zeros.limit(3).collect(Collectors.toList());
// Result: [0, 0, 0]

// Generate with state (using AtomicInteger)
AtomicInteger counter = new AtomicInteger(0);
Stream<Integer> counting = Stream.generate(counter::incrementAndGet);
List<Integer> first10 = counting.limit(10).collect(Collectors.toList());
// Result: [1, 2, 3, ..., 10]
```

---

## Combining Multiple Streams

### Flatten Multiple Streams

```java
Stream<Integer> s1 = Stream.of(1, 2, 3);
Stream<Integer> s2 = Stream.of(4, 5, 6);
Stream<Integer> s3 = Stream.of(7, 8, 9);

// Using concat (two at a time)
Stream<Integer> combined = Stream.concat(
    Stream.concat(s1, s2),
    s3
);

// Or using flatMap
List<Stream<Integer>> streams = List.of(s1, s2, s3);
Stream<Integer> combined2 = streams.stream()
    .flatMap(s -> s);
```

### Merge Streams Alternately

```java
Stream<String> names = Stream.of("Alice", "Bob", "Charlie");
Stream<Integer> ages = Stream.of(30, 25, 35);

// Zip together
Stream<String> zipped = IntStream.range(0, 3)
    .mapToObj(i -> names.toList().get(i) + ":" + ages.toList().get(i));
```

---

## Practical Examples

### Example 1: Generate Test Data

```java
// Generate 100 random user IDs
Stream<String> userIds = Stream.generate(() ->
    UUID.randomUUID().toString().substring(0, 8)
).limit(100);
```

### Example 2: Sequence Numbers

```java
// Generate sequence 1 to 1000
List<Integer> sequence = Stream.iterate(1, n -> n + 1)
    .limit(1000)
    .collect(Collectors.toList());
```

### Example 3: Combine Multiple Data Sources

```java
Stream<String> dbUsers = getDbUsers();
Stream<String> apiUsers = getApiUsers();
Stream<String> fileUsers = getFileUsers();

List<String> allUsers = Stream.concat(
    Stream.concat(dbUsers, apiUsers),
    fileUsers
).collect(Collectors.toList());
```

### Example 4: Infinite Series

```java
// Geometric series: 1, 1/2, 1/4, 1/8, ...
Stream<Double> geometricSeries = Stream.iterate(1.0, n -> n / 2);
List<Double> first10 = geometricSeries.limit(10).collect(Collectors.toList());
```

### Example 5: Builder Pattern

```java
Stream<String> stream = Stream.<String>builder()
    .add("Alice")
    .add("Bob")
    .add("Charlie")
    .build();
List<String> result = stream.collect(Collectors.toList());
```

---

## Key Takeaways

1. `concat()` joins two streams sequentially
2. `Stream.of()` creates from varargs
3. `Stream.empty()` creates empty stream
4. `Stream.iterate()` creates infinite from seed and function
5. `Stream.generate()` creates infinite from supplier
6. Always use `limit()` with infinite streams
7. Builder pattern useful for incremental construction
