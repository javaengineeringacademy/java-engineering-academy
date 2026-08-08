# Sorting Operations

## Table of Contents
1. [Overview](#overview)
2. [sorted()](#sorted)
3. [sorted(Comparator)](#sortedcomparator)
4. [Comparator Factory Methods](#comparator-factory-methods)
5. [Custom Comparators](#custom-comparators)
6. [Practical Examples](#practical-examples)

---

## Overview

Sorting operations arrange elements in a specific order. They are intermediate operations that return a new Stream.

| Method | Description | Return Type |
|--------|-------------|-------------|
| `sorted()` | Natural order | Stream |
| `sorted(Comparator)` | Custom order | Stream |

---

## sorted()

```java
Stream<T> sorted()
```

Sorts elements in natural order (using `Comparable`).

```java
List<Integer> numbers = List.of(5, 3, 1, 4, 2);
List<Integer> sorted = numbers.stream()
    .sorted()
    .collect(Collectors.toList());
// Result: [1, 2, 3, 4, 5]

List<String> names = List.of("Charlie", "Alice", "Bob");
List<String> sortedNames = names.stream()
    .sorted()
    .collect(Collectors.toList());
// Result: [Alice, Bob, Charlie]
```

---

## sorted(Comparator)

```java
Stream<T> sorted(Comparator<? super T> comparator)
```

Sorts elements using the provided comparator.

```java
List<String> names = List.of("Alice", "Bob", "Charlie", "David");

// Sort by length
List<String> byLength = names.stream()
    .sorted(Comparator.comparingInt(String::length))
    .collect(Collectors.toList());
// Result: [Bob, Alice, David, Charlie]

// Sort by length descending
List<String> byLengthDesc = names.stream()
    .sorted(Comparator.comparingInt(String::length).reversed())
    .collect(Collectors.toList());
// Result: [Charlie, Alice, David, Bob]

// Sort by last character
List<String> byLastChar = names.stream()
    .sorted(Comparator.comparingInt(name -> name.charAt(name.length() - 1)))
    .collect(Collectors.toList());
// Result: [Alice, David, Bob, Charlie]
```

---

## Comparator Factory Methods

### Comparator.comparing()

```java
// Compare by extracted value
Comparator<T> comparator = Comparator.comparing(Function);

// Examples
Comparator<String> byLength = Comparator.comparingInt(String::length);
Comparator<String> byAlpha = Comparator.comparing(String::toLowerCase);
Comparator<Person> byAge = Comparator.comparingInt(Person::age);
```

### Comparator.thenComparing()

```java
// Chain comparators
Comparator<T> comparator = Comparator.comparing(Function)
    .thenComparing(Function);

// Examples
Comparator<Person> byAgeThenName = Comparator.comparingInt(Person::age)
    .thenComparing(Person::name);

Comparator<String> byLengthThenAlpha = Comparator.comparingInt(String::length)
    .thenComparing(String::compareTo);
```

### reversed()

```java
// Reverse any comparator
Comparator<T> reversed = comparator.reversed();

// Examples
Comparator<String> byLengthDesc = Comparator.comparingInt(String::length).reversed();
Comparator<Person> byAgeDesc = Comparator.comparingInt(Person::age).reversed();
```

### Comparator.naturalOrder() / reverseOrder()

```java
// Natural and reverse order for Comparable types
Comparator<String> natural = Comparator.naturalOrder();
Comparator<String> reverse = Comparator.reverseOrder();
```

### Comparator.comparingInt/Long/Double()

```java
// Primitive-specific comparators
Comparator<String> byLength = Comparator.comparingInt(String::length);
Comparator<Person> bySalary = Comparator.comparingDouble(Person::salary);
Comparator<Event> byTimestamp = Comparator.comparingLong(Event::timestamp);
```

---

## Custom Comparators

### Lambda Expression

```java
Comparator<String> byLength = (s1, s2) -> Integer.compare(
    s1.length(), s2.length()
);
```

### Method Reference

```java
Comparator<String> byAlpha = String::compareToIgnoreCase;
```

### Multi-criteria Comparison

```java
record Employee(String name, int age, double salary) {}

Comparator<Employee> comparator = Comparator
    .comparing(Employee::age)
    .thenComparing(Employee::salary)
    .thenComparing(Employee::name);

List<Employee> sorted = employees.stream()
    .sorted(comparator)
    .collect(Collectors.toList());
```

---

## Practical Examples

### Example 1: Sort Objects by Multiple Fields

```java
record Product(String name, String category, double price) {}

List<Product> products = List.of(
    new Product("Laptop", "Electronics", 999.99),
    new Product("Phone", "Electronics", 699.99),
    new Product("Desk", "Furniture", 299.99),
    new Product("Chair", "Furniture", 199.99)
);

List<Product> sorted = products.stream()
    .sorted(Comparator.comparing(Product::category)
        .thenComparing(Product::price))
    .collect(Collectors.toList());
```

### Example 2: Sort with Custom Logic

```java
List<String> words = List.of("Java", "is", "awesome", "for", "streams");

// Sort by length, then alphabetically for same length
List<String> sorted = words.stream()
    .sorted(Comparator.comparingInt(String::length)
        .thenComparing(String::compareTo))
    .collect(Collectors.toList());
// Result: [is, for, Java, streams, awesome]
```

### Example 3: Case-Insensitive Sort

```java
List<String> names = List.of("alice", "Bob", "CHARLIE", "david");

List<String> sorted = names.stream()
    .sorted(String::compareToIgnoreCase)
    .collect(Collectors.toList());
// Result: [alice, Bob, CHARLIE, david]
```

### Example 4: Sort and Limit

```java
List<Integer> numbers = List.of(5, 3, 1, 4, 2, 8, 6, 7);

List<Integer> top3 = numbers.stream()
    .sorted()
    .limit(3)
    .collect(Collectors.toList());
// Result: [1, 2, 3]
```

### Example 5: Sort Map by Value

```java
Map<String, Integer> scores = Map.of(
    "Alice", 85,
    "Bob", 92,
    "Charlie", 78
);

List<Map.Entry<String, Integer>> sorted = scores.entrySet().stream()
    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
    .collect(Collectors.toList());
```

---

## Key Takeaways

1. `sorted()` uses natural order (Comparable)
2. `sorted(Comparator)` uses custom comparison logic
3. `Comparator.comparing()` creates comparators from key extractors
4. Chain comparators with `thenComparing()`
5. Reverse any comparator with `reversed()`
6. Use primitive comparators (`comparingInt`, etc.) for performance
