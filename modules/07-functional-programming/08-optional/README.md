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
Optional<String> upper = optional.map(String::toUpperCase);

// FlatMap
Optional<String> upper = optional.flatMap(s -> Optional.of(s.toUpperCase()));

// Filter
Optional<String> filtered = optional.filter(s -> s.length() > 3);
```

---

## 11. Easy Example

### Example 1: Basic Optional Operations

```java
package academy.javaengineering.functional.optional;

import java.util.Optional;

public class BasicOptional {
    public static void main(String[] args) {
        // Create Optional instances
        Optional<String> empty = Optional.empty();
        Optional<String> present = Optional.of("Hello");
        Optional<String> nullable = Optional.ofNullable(null);
        
        // Check if present
        System.out.println("Empty is present: " + empty.isPresent());
        System.out.println("Present is present: " + present.isPresent());
        System.out.println("Nullable is present: " + nullable.isPresent());
        
        // Get values
        System.out.println("Present value: " + present.get());
        System.out.println("Empty orElse: " + empty.orElse("Default"));
        System.out.println("Nullable orElse: " + nullable.orElse("Default"));
    }
}
```

### Example 2: Optional with Methods

```java
package academy.javaengineering.functional.optional;

import java.util.Optional;

public class OptionalMethods {
    
    public static Optional<String> findName(int id) {
        if (id == 1) return Optional.of("Alice");
        if (id == 2) return Optional.of("Bob");
        return Optional.empty();
    }
    
    public static void main(String[] args) {
        // Find names
        Optional<String> name1 = findName(1);
        Optional<String> name2 = findName(3);
        
        // Use ifPresent
        name1.ifPresent(name -> System.out.println("Found: " + name));
        name2.ifPresent(name -> System.out.println("Found: " + name));
        
        // Use orElse
        System.out.println("Name 1: " + name1.orElse("Unknown"));
        System.out.println("Name 2: " + name2.orElse("Unknown"));
        
        // Use map
        Optional<Integer> length = name1.map(String::length);
        System.out.println("Name 1 length: " + length.orElse(0));
    }
}
```

---

## 12. Medium Example

### Example 1: Optional Chaining

```java
package academy.javaengineering.functional.optional;

import java.util.Optional;

public class OptionalChaining {
    
    record Address(String city, String zipCode) {}
    record User(String name, Address address) {}
    
    public static Optional<String> getUserCity(User user) {
        return Optional.ofNullable(user)
            .map(User::address)
            .map(Address::city);
    }
    
    public static void main(String[] args) {
        User userWithAddress = new User("Alice", new Address("New York", "10001"));
        User userWithoutAddress = new User("Bob", null);
        User nullUser = null;
        
        System.out.println("Alice's city: " + getUserCity(userWithAddress).orElse("Unknown"));
        System.out.println("Bob's city: " + getUserCity(userWithoutAddress).orElse("Unknown"));
        System.out.println("Null's city: " + getUserCity(nullUser).orElse("Unknown"));
    }
}
```

### Example 2: Optional with Streams

```java
package academy.javaengineering.functional.optional;

import java.util.*;
import java.util.stream.Collectors;

public class OptionalWithStreams {
    
    record Product(String name, Optional<String> discount) {}
    
    public static void main(String[] args) {
        List<Product> products = List.of(
            new Product("Laptop", Optional.of("10%")),
            new Product("Phone", Optional.empty()),
            new Product("Tablet", Optional.of("5%"))
        );
        
        // Find products with discounts
        List<String> productsWithDiscount = products.stream()
            .filter(p -> p.discount().isPresent())
            .map(Product::name)
            .toList();
        System.out.println("Products with discount: " + productsWithDiscount);
        
        // Get all discounts
        List<String> discounts = products.stream()
            .map(Product::discount)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        System.out.println("Discounts: " + discounts);
        
        // Better approach with flatMap
        List<String> discountsBetter = products.stream()
            .map(Product::discount)
            .flatMap(Optional::stream)
            .toList();
        System.out.println("Discounts (better): " + discountsBetter);
    }
}
```

---

## 13. Hard Example

### Example 1: Optional Builder

```java
package academy.javaengineering.functional.optional;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class OptionalBuilder<T> {
    
    private final Optional<T> optional;
    
    private OptionalBuilder(Optional<T> optional) {
        this.optional = optional;
    }
    
    public static <T> OptionalBuilder<T> of(T value) {
        return new OptionalBuilder<>(Optional.ofNullable(value));
    }
    
    public static <T> OptionalBuilder<T> empty() {
        return new OptionalBuilder<>(Optional.empty());
    }
    
    public <R> OptionalBuilder<R> map(Function<T, R> mapper) {
        return new OptionalBuilder<>(optional.map(mapper));
    }
    
    public <R> OptionalBuilder<R> flatMap(Function<T, Optional<R>> mapper) {
        return new OptionalBuilder<>(optional.flatMap(mapper));
    }
    
    public OptionalBuilder<T> filter(Predicate<T> predicate) {
        return new OptionalBuilder<>(optional.filter(predicate));
    }
    
    public Optional<T> toOptional() {
        return optional;
    }
    
    public T orElse(T defaultValue) {
        return optional.orElse(defaultValue);
    }
    
    public T orElseGet(java.util.function.Supplier<T> supplier) {
        return optional.orElseGet(supplier);
    }
    
    public void ifPresent(java.util.function.Consumer<T> consumer) {
        optional.ifPresent(consumer);
    }
    
    public static void main(String[] args) {
        // Build Optional chain
        String result = OptionalBuilder.of("hello")
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .map(s -> s + " WORLD")
            .orElse("DEFAULT");
        
        System.out.println("Result: " + result);
        
        // With null input
        String result2 = OptionalBuilder.of(null)
            .map(String::toUpperCase)
            .filter(s -> s.length() > 3)
            .map(s -> s + " WORLD")
            .orElse("DEFAULT");
        
        System.out.println("Result2: " + result2);
    }
}
```

---

## 14. Enterprise Example

### Example 1: Optional in Service Layer

```java
package academy.javaengineering.functional.optional;

import java.util.Optional;

public class UserService {
    
    public record User(String id, String name, String email, boolean active) {}
    
    public interface UserRepository {
        Optional<User> findById(String id);
        Optional<User> findByEmail(String email);
    }
    
    public static class UserServiceImpl {
        private final UserRepository repository;
        
        public UserServiceImpl(UserRepository repository) {
            this.repository = repository;
        }
        
        public Optional<String> getUserName(String userId) {
            return repository.findById(userId)
                .map(User::name);
        }
        
        public Optional<String> getUserEmail(String userId) {
            return repository.findById(userId)
                .filter(User::active)
                .map(User::email);
        }
        
        public Optional<User> findActiveUser(String userId) {
            return repository.findById(userId)
                .filter(User::active);
        }
        
        public String getDisplayName(String userId) {
            return repository.findById(userId)
                .map(User::name)
                .orElse("Unknown User");
        }
    }
    
    public static void main(String[] args) {
        // Mock repository
        UserRepository repo = new UserRepository() {
            public Optional<User> findById(String id) {
                if ("U001".equals(id)) return Optional.of(new User("U001", "Alice", "alice@example.com", true));
                if ("U002".equals(id)) return Optional.of(new User("U002", "Bob", "bob@example.com", false));
                return Optional.empty();
            }
            public Optional<User> findByEmail(String email) {
                return Optional.empty();
            }
        };
        
        UserServiceImpl service = new UserServiceImpl(repo);
        
        // Use Optional
        System.out.println("Alice's name: " + service.getUserName("U001").orElse("N/A"));
        System.out.println("Bob's email: " + service.getUserEmail("U002").orElse("N/A"));
        System.out.println("Unknown: " + service.getUserName("U999").orElse("N/A"));
    }
}
```

---

## 15. Performance

### 15.1 Optional Performance

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| `of()` | O(1) | Creates Optional |
| `empty()` | O(1) | Returns cached instance |
| `orElse()` | O(1) | Returns value or default |
| `map()` | O(1) | Transforms if present |
| `filter()` | O(1) | Filters if present |

### 15.2 Performance Tips

1. **Avoid Optional for fields**: Use null with documentation
2. **Avoid Optional for parameters**: Use overloading
3. **Cache empty Optional**: Use `Optional.empty()`
4. **Use orElse over get**: Avoid exceptions

---

## 16. Best Practices

1. **Use Optional for return types**: When value may be absent
2. **Avoid Optional for fields**: Use null with documentation
3. **Avoid Optional for parameters**: Use overloading
4. **Use orElse over get**: Avoid NoSuchElementException
5. **Use ifPresent for side effects**: Not for transformation
6. **Document Optional usage**: In Javadoc

---

## 17. Common Mistakes

### Mistake 1: Using Optional.get() Without Checking

```java
// WRONG: May throw NoSuchElementException
Optional<String> optional = Optional.empty();
String value = optional.get();  // Throws!

// CORRECT: Use orElse or isPresent
String value = optional.orElse("default");
```

### Mistake 2: Using Optional for Fields

```java
// WRONG: Unnecessary overhead
class User {
    Optional<String> name;  // Bad!
}

// CORRECT: Use null with documentation
class User {
    @Nullable String name;  // Good!
}
```

### Mistake 3: Using Optional for Parameters

```java
// WRONG: Unnecessary complexity
void process(Optional<String> value) { }

// CORRECT: Use overloading
void process(String value) { }
void process() { }
```

---

## 18. Pitfalls

1. **Serialization**: Optional is not serializable
2. **Reflection**: Optional internals are not accessible
3. **Performance**: Overhead for simple cases
4. **Complexity**: Can make code harder to read

---

## 19. Debugging Tips

### 1. Use orElse for Debugging

```java
Optional<String> optional = Optional.empty();
System.out.println("Value: " + optional.orElse("EMPTY"));
```

### 2. Use ifPresent for Debugging

```java
optional.ifPresent(value -> System.out.println("Found: " + value));
optional.ifPresentOrElse(
    value -> System.out.println("Found: " + value),
    () -> System.out.println("Empty")
);
```

---

## 20. Comparison Table

| Feature | Optional | Null |
|---------|----------|------|
| **Explicit** | Yes | No |
| **Null-safe** | Yes | No |
| **Composable** | Yes | No |
| **Performance** | Overhead | None |
| **Readability** | Good | Verbose |

---

## 21. Decision Tree

```
Should you use Optional?

┌─ Is this a method return type?
│  ├─ YES → Use Optional
│  └─ NO → Continue
│
├─ Is the value potentially absent?
│  ├─ YES → Use Optional
│  └─ NO → Use regular type
│
├─ Is this a class field?
│  ├─ YES → Use null with @Nullable
│  └─ NO → Continue
│
├─ Is this a method parameter?
│  ├─ YES → Use overloading
│  └─ NO → Continue
│
└─ Do you need composable operations?
   ├─ YES → Use Optional
   └─ NO → Use null checks
```

---

## 22. Interview Questions

### Q1: What is Optional and when should you use it?

**Answer**: Optional is a container that may or may not contain a non-null value. Use it for method return types when the value may be absent. Avoid using it for class fields or method parameters.

### Q2: What is the difference between orElse() and orElseGet()?

**Answer**: `orElse()` always evaluates the default value. `orElseGet()` only evaluates the supplier if Optional is empty. Use `orElseGet()` when computing the default is expensive.

### Q3: What is the difference between map() and flatMap()?

**Answer**: `map()` transforms the value and wraps in Optional. `flatMap()` transforms the value to another Optional and unwraps it. Use `flatMap` when the mapping function returns Optional.

### Q4: Can Optional be serialized?

**Answer**: No. Optional is not serializable. This is a deliberate design decision to discourage using Optional for fields or in serialization contexts.

### Q5: How do you use Optional with streams?

**Answer**: Use `Optional::stream` to convert Optional to Stream:
```java
list.stream()
    .map(Item::optionalField)
    .flatMap(Optional::stream)
    .toList();
```

---

## 23. Exercises

### Exercise 1: Basic Optional
Create methods that return Optional:
1. `findUser(int id)` - returns Optional<User>
2. `parseInteger(String s)` - returns Optional<Integer>
3. `divide(int a, int b)` - returns Optional<Integer>

### Exercise 2: Optional Chaining
Chain Optional operations to:
1. Find a user by ID
2. Get their address
3. Get their city
4. Return "Unknown" if any step fails

### Exercise 3: Optional with Streams
Use Optional with streams to:
1. Filter out null values
2. Extract present values
3. Transform present values

---

## 24. Assignments

### Assignment 1: Optional API
Design an API using Optional:
1. Create a service that returns Optional
2. Document when values are absent
3. Handle null inputs gracefully

### Assignment 2: Optional Composition
Implement Optional composition:
1. Combine two Optionals
2. Handle multiple optional values
3. Create a builder for Optional chains

### Assignment 3: Optional Utilities
Create utility methods for Optional:
1. `firstPresent(Optional<T>... optionals)`
2. `allPresent(List<Optional<T>> optionals)`
3. `toStream(Optional<T> optional)`

---

## 25. Mini Project

### Project: Optional-Based Configuration System

Build a configuration system using Optional:

**Requirements:**
1. Type-safe configuration access
2. Default value support
3. Configuration composition
4. Null-safe access

**Starter Code:**
```java
package academy.javaengineering.functional.optional.project;

import java.util.Optional;

public class ConfigSystem {
    
    public static class Config {
        public Optional<String> getString(String key) {
            return Optional.empty();
        }
        
        public Optional<Integer> getInteger(String key) {
            return Optional.empty();
        }
        
        // TODO: Implement configuration access
    }
}
```

---

## 26. Summary

Optional provides explicit, null-safe handling of potentially absent values. Key takeaways:

1. **Use for return types**: When value may be absent
2. **Avoid for fields/parameters**: Use null with documentation
3. **Compose operations**: map, flatMap, filter
4. **Access safely**: orElse, ifPresent, not get()
5. **Not serializable**: Don't use in serialization contexts

### Next Steps
- Topic 09: Composition — Function composition patterns
- Topic 10: Best Practices — Functional programming best practices

---

## 27. References

1. [Oracle Java Tutorials: Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)
2. [Java Language Specification: Optional](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 54](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Optional](https://www.baeldung.com/java-optional)
