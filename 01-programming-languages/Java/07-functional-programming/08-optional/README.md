# Topic 08: Optional

## Table of Contents

1. [Introduction](#1-introduction)
2. [Learning Objectives](#2-learning-objectives)
3. [Prerequisites](#3-prerequisites)
4. [Why This Concept Exists](#4-why-this-concept-exists)
5. [Problem Statement](#5-problem-statement)
6. [Theory](#6-theory)
7. [Internal Working](#7-internal-working)
8. [JVM Perspective](#8-jvm-perspective)
9. [Memory Representation](#9-memory-representation)
10. [Syntax](#10-syntax)
11. [Easy Example](#11-easy-example)
12. [Medium Example](#12-medium-example)
13. [Hard Example](#13-hard-example)
14. [Enterprise Example](#14-enterprise-example)
15. [Performance](#15-performance)
16. [Best Practices](#16-best-practices)
17. [Common Mistakes](#17-common-mistakes)
18. [Pitfalls](#18-pitfalls)
19. [Debugging Tips](#19-debugging-tips)
20. [Comparison Table](#20-comparison-table)
21. [Decision Tree](#21-decision-tree)
22. [Interview Questions](#22-interview-questions)
23. [Exercises](#23-exercises)
24. [Assignments](#24-assignments)
25. [Mini Project](#25-mini-project)
26. [Summary](#26-summary)
27. [References](#27-references)

---

## 1. Introduction

`Optional<T>` is a container object that may or may not contain a non-null value. Introduced in Java 8, `Optional` provides a explicit way to handle the absence of a value, replacing the error-prone null checks with a functional, composable API.

### Key Characteristics

| Characteristic | Description |
|----------------|-------------|
| **Explicit** | Forces consideration of absence |
| **Composable** | Chain operations on optional values |
| **Null-safe** | Prevents NullPointerException |
| **Functional** | Supports map, flatMap, filter |

### When to Use Optional

| Use Case | Recommendation |
|----------|----------------|
| Method return type | Use Optional for potentially absent values |
| Method parameter | Avoid Optional - use overloading instead |
| Class field | Avoid Optional - use null with documentation |
| Collection element | Avoid Optional - use empty collection |

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Create and use Optional instances correctly
2. Apply Optional operations (map, flatMap, filter, orElse, ifPresent)
3. Avoid common Optional pitfalls
4. Use Optional in stream operations
5. Design APIs with Optional return types
6. Handle Optional in enterprise applications

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Lambda Expressions**: Basic syntax (Topic 02)
- **Functional Interfaces**: Function, Predicate, Consumer (Topic 03)
- **Stream API**: Basic stream operations (Topic 05)

---

## 4. Why This Concept Exists

### The Problem with Null

Null represents the absence of a value, but it's problematic:

1. **NullPointerException**: The most common Java exception
2. **Ambiguity**: Does null mean "not found", "error", or "not applicable"?
3. **Verbosity**: Null checks are repetitive
4. **Hidden**: Compiler doesn't warn about potential null

```java
// Problematic null handling
String name = getCustomerName(id);
if (name != null) {
    System.out.println(name.toUpperCase());
}
```

### The Optional Solution

```java
// Explicit null handling
Optional<String> name = getCustomerName(id);
name.ifPresent(n -> System.out.println(n.toUpperCase()));
```

---

## 5. Problem Statement

### Real-World Scenario: User Lookup Service

A user lookup service needs to:
- **Find** users by various criteria
- **Handle** cases where user doesn't exist
- **Chain** operations on found users
- **Provide** default values when absent

### Requirements

1. Explicit handling of absent values
2. Composable operations on optional values
3. Clear API documentation
4. Integration with streams
5. Performance-friendly

---

## 6. Theory

### 6.1 Creating Optional Instances

```java
// Empty Optional
Optional<String> empty = Optional.empty();

// Optional with value
Optional<String> present = Optional.of("hello");

// Optional with nullable value
Optional<String> nullable = Optional.ofNullable(null);
Optional<String> nonNull = Optional.ofNullable("hello");
```

### 6.2 Optional Operations

#### Value Access

```java
// Get value (throws NoSuchElementException if empty)
T value = optional.get();

// Get value or default
T value = optional.orElse(defaultValue);

// Get value or compute default
T value = optional.orElseGet(() -> computeDefault());

// Get value or throw exception
T value = optional.orElseThrow(() -> new RuntimeException("Missing"));
```

#### Conditional Operations

```java
// Execute if present
optional.ifPresent(value -> System.out.println(value));

// Execute if present, else run alternative
optional.ifPresentOrElse(
    value -> System.out.println("Found: " + value),
    () -> System.out.println("Not found")
);

// Check if present
boolean present = optional.isPresent();
```

#### Transformation Operations

```java
// Transform value
Optional<R> result = optional.map(value -> transform(value));

// Transform with Optional
Optional<R> result = optional.flatMap(value -> transform(value));

// Filter value
Optional<T> filtered = optional.filter(value -> predicate.test(value));
```

### 6.3 Optional with Streams

```java
// Find first element
Optional<T> first = stream.findFirst();

// Find any element
Optional<T> any = stream.findAny();

// Get minimum
Optional<T> min = stream.min(comparator);

// Get maximum
Optional<T> max = stream.max(comparator);
```

### 6.4 Optional Composition

```java
// Chain operations
Optional<String> result = Optional.of("hello")
    .map(String::toUpperCase)
    .filter(s -> s.length() > 3)
    .map(s -> s + " WORLD");

// FlatMap for nested Optionals
Optional<String> result = Optional.of("hello")
    .flatMap(s -> Optional.of(s.toUpperCase()));
```

---

## 7. Internal Working

### 7.1 Optional Implementation

`Optional` is a final class with two factory methods:

```java
public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>();
    private final T value;
    
    private Optional() {
        this.value = null;
    }
    
    private Optional(T value) {
        this.value = Objects.requireNonNull(value);
    }
    
    public static <T> Optional<T> empty() {
        @SuppressWarnings("unchecked")
        Optional<T> t = (Optional<T>) EMPTY;
        return t;
    }
    
    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }
    
    public static <T> Optional<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }
}
```

### 7.2 Method Chaining

Optional operations return new Optional instances:

```
Optional.of("hello")
    .map(String::toUpperCase)  → Optional.of("HELLO")
    .filter(s -> s.length() > 3)  → Optional.of("HELLO")
    .map(s -> s + " WORLD")  → Optional.of("HELLO WORLD")
```

### 7.3 Lazy Evaluation

Some Optional operations are lazy:

```java
Optional<String> result = Optional.of("hello")
    .map(value -> {
        System.out.println("Mapping: " + value);
        return value.toUpperCase();
    });
// Nothing prints here - mapping is lazy
```

---

## 8. JVM Perspective

### 8.1 Optional Object Layout

```
Optional Object:
┌─────────────────────────────────────┐
│  Header (mark word + klass pointer) │
├─────────────────────────────────────┤
│  Value reference (or null)          │
└─────────────────────────────────────┘
```

### 8.2 Memory Overhead

- **Empty Optional**: ~16 bytes
- **Present Optional**: ~16 bytes + value reference
- **Value object**: Depends on the value

### 8.3 JIT Optimization

The JIT compiler can optimize Optional operations:

1. **Inlining**: Small operations are inlined
2. **Escape analysis**: Optional may be stack-allocated
3. **Dead code elimination**: Unused Optional operations are removed

---

## 9. Memory Representation

### 9.1 Empty vs Present

```
Empty Optional:
┌─────────────────────────────────────┐
│  Header                             │
├─────────────────────────────────────┤
│  value = null                       │
└─────────────────────────────────────┘

Present Optional:
┌─────────────────────────────────────┐
│  Header                             │
├─────────────────────────────────────┤
│  value → "hello"                    │
└─────────────────────────────────────┘
```

### 9.2 Optional Chain

```
Optional.of("hello")
    .map(String::toUpperCase)
    .filter(s -> s.length() > 3)

Memory:
Optional("hello") → Optional("HELLO") → Optional("HELLO")
```

---

## 10. Syntax

### 10.1 Creating Optional

```java
// Empty
Optional<String> empty = Optional.empty();

// Present
Optional<String> present = Optional.of("hello");

// Nullable
Optional<String> nullable = Optional.ofNullable(null);
Optional<String> nonNull = Optional.ofNullable("hello");
```

### 10.2 Accessing Values

```java
// Get (throws if empty)
String value = optional.get();

// Or default
String value = optional.orElse("default");

// Or compute
String value = optional.orElseGet(() -> computeDefault());

// Or throw
String value = optional.orElseThrow(() -> new RuntimeException("Missing"));
```

### 10.3 Conditional Operations

```java
// If present
optional.ifPresent(value -> System.out.println(value));

// If present, else
optional.ifPresentOrElse(
    value -> System.out.println("Found: " + value),
    () -> System.out.println("Not found")
);

// Is present
boolean present = optional.isPresent();
```

### 10.4 Transformation Operations

```java
// Map

---

[📖 Continue to Part 2](README-part2.md)
```
