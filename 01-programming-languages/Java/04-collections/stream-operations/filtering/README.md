# Filtering Operations

## Table of Contents
1. [Overview](#overview)
2. [filter(Predicate)](#filterpredicate)
3. [distinct()](#distinct)
4. [takeWhile(Predicate)](#takewhilepredicate)
5. [dropWhile(Predicate)](#dropwhilepredicate)
6. [Predicate Functional Interface](#predicate-functional-interface)
7. [Combining Filters](#combining-filters)
8. [Practical Examples](#practical-examples)

---

## Overview

Filtering operations select elements from a stream based on criteria. They are intermediate operations that return a new Stream.

| Method | Description | Java Version |
|--------|-------------|--------------|
| `filter(Predicate)` | Keep matching elements | Java 8 |
| `distinct()` | Remove duplicates | Java 8 |
| `takeWhile(Predicate)` | Take while true | Java 9 |
| `dropWhile(Predicate)` | Drop while true | Java 9 |

---

## filter(Predicate)

```java
Stream<T> filter(Predicate<? super T> predicate)
```

Keeps only elements where the predicate returns `true`.

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Filter even numbers
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// Result: [2, 4, 6, 8, 10]

// Filter strings by length
List<String> names = List.of("Alice", "Bob", "Charlie", "David");
List<String> longNames = names.stream()
    .filter(name -> name.length() > 4)
    .collect(Collectors.toList());
// Result: [Alice, Charlie, David]

// Method reference
List<String> aNames = names.stream()
    .filter(name -> name.startsWith("A"))
    .collect(Collectors.toList());
// Result: [Alice]
```

---

## distinct()

```java
Stream<T> distinct()
```

Removes duplicate elements using `equals()` and `hashCode()`.

```java
List<Integer> numbers = List.of(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4]

// With objects
List<Person> people = List.of(
    new Person("Alice", 30),
    new Person("Bob", 25),
    new Person("Alice", 30)  // Duplicate by equals()
);
List<Person> uniquePeople = people.stream()
    .distinct()
    .collect(Collectors.toList());
// Result: [Alice(30), Bob(25)]
```

**Important**: Your class must properly override `equals()` and `hashCode()`.

---

## takeWhile(Predicate)

```java
Stream<T> takeWhile(Predicate<? super T> predicate)
```

Takes elements while predicate is `true`, stops at first `false`. Order matters!

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2, 3);
List<Integer> taken = numbers.stream()
    .takeWhile(n -> n < 4)
    .collect(Collectors.toList());
// Result: [1, 2, 3]  (stops at 4)

// With sorted data
List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6);
List<Integer> takenSorted = sorted.stream()
    .takeWhile(n -> n < 4)
    .collect(Collectors.toList());
// Result: [1, 2, 3]
```

---

## dropWhile(Predicate)

```java
Stream<T> dropWhile(Predicate<? super T> predicate)
```

Drops elements while predicate is `true`, keeps the rest. Order matters!

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 1, 2, 3);
List<Integer> dropped = numbers.stream()
    .dropWhile(n -> n < 4)
    .collect(Collectors.toList());
// Result: [4, 5, 1, 2, 3]

// With sorted data
List<Integer> sorted = List.of(1, 2, 3, 4, 5, 6);
List<Integer> droppedSorted = sorted.stream()
    .dropWhile(n -> n < 4)
    .collect(Collectors.toList());
// Result: [4, 5, 6]
```

---

## Predicate Functional Interface

`Predicate<T>` is a functional interface: `T → boolean`

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);

    // Default methods
    default Predicate<T> and(Predicate<? super T> other);
    default Predicate<T> negate();
    default Predicate<T> or(Predicate<? super T> other);

    // Static method
    static <T> Predicate<T> isEqual(Object targetRef);
}
```

### Creating Predicates

```java
// Lambda
Predicate<Integer> isPositive = n -> n > 0;

// Method reference
Predicate<String> isEmpty = String::isEmpty;

// Negation
Predicate<Integer> isNotPositive = isPositive.negate();

// Composition
Predicate<Integer> isBetween1And10 = n -> n >= 1 && n <= 10;
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> isPositiveAndEven = isPositive.and(isEven);

// Equality
Predicate<String> isAlice = Predicate.isEqual("Alice");
```

---

## Combining Filters

Chain multiple filter operations for complex conditions:

```java
List<Person> people = List.of(
    new Person("Alice", 30, "Engineer"),
    new Person("Bob", 25, "Designer"),
    new Person("Charlie", 35, "Engineer"),
    new Person("David", 28, "Manager"),
    new Person("Eve", 32, "Designer")
);

// Find engineers over 30
List<Person> result = people.stream()
    .filter(p -> p.getRole().equals("Engineer"))
    .filter(p -> p.getAge() > 30)
    .collect(Collectors.toList());
// Result: [Charlie(35, Engineer)]

// Combined predicate
List<Person> result2 = people.stream()
    .filter(p -> p.getRole().equals("Engineer") && p.getAge() > 30)
    .collect(Collectors.toList());
```

---

## Practical Examples

### Example 1: Remove Nulls
```java
List<String> names = List.of("Alice", null, "Bob", null, "Charlie");
List<String> nonNull = names.stream()
    .filter(Objects::nonNull)
    .collect(Collectors.toList());
// Result: [Alice, Bob, Charlie]
```

### Example 2: Filter and Transform
```java
List<String> words = List.of("hello", "world", "java", "stream", "api");
List<String> result = words.stream()
    .filter(w -> w.length() > 4)
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// Result: [HELLO, WORLD, STREAM]
```

### Example 3: Custom Object Filtering
```java
public class Product {
    private String name;
    private double price;
    private String category;
    // getters, setters
}

List<Product> products = getProducts();

// Electronics under $100
List<Product> deals = products.stream()
    .filter(p -> p.getCategory().equals("Electronics"))
    .filter(p -> p.getPrice() < 100.0)
    .collect(Collectors.toList());
```

### Example 4: Using takeWhile for Pagination
```java
List<Order> sortedOrders = getSortedOrders();
int pageSize = 10;
int page = 2;

List<Order> pageOrders = sortedOrders.stream()
    .skip((long) (page - 1) * pageSize)
    .limit(pageSize)
    .collect(Collectors.toList());
```

### Example 5: Deduplication with Objects
```java
List<String> urls = List.of(
    "https://example.com",
    "https://example.com",  // duplicate
    "https://other.com"
);
List<String> uniqueUrls = urls.stream()
    .distinct()
    .collect(Collectors.toList());
```

---

## Key Takeaways

1. `filter()` keeps elements matching a condition
2. `distinct()` removes duplicates using equals/hashCode
3. `takeWhile()` stops at first false (order matters!)
4. `dropWhile()` skips while true (order matters!)
5. Chain multiple `filter()` calls for complex conditions
6. Predicates can be composed with `and()`, `or()`, `negate()`
