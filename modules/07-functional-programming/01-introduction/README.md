# Topic 01: Introduction to Functional Programming

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

Functional Programming (FP) is a programming paradigm that treats computation as the evaluation of mathematical functions. Unlike Object-Oriented Programming (OOP), which organizes code around objects and their mutable state, FP emphasizes immutability, pure functions, and declarative code.

Java 8 introduced first-class support for functional programming with lambda expressions and the Stream API. Java 21 continues this evolution with pattern matching for `switch` expressions, record patterns, and enhanced string templates that further reduce boilerplate code.

### Key Characteristics of Functional Programming

| Characteristic | Description |
|----------------|-------------|
| **Immutability** | Data cannot be modified after creation |
| **Pure Functions** | Same input always produces same output, no side effects |
| **First-Class Functions** | Functions can be passed as arguments and returned as values |
| **Declarative Style** | Describe *what* to do, not *how* to do it |
| **Referential Transparency** | Function calls can be replaced with their return values |

### The Java 21 Functional Landscape

Java 21 is an LTS (Long-Term Support) release that solidifies several functional features:

- **Lambda expressions** (Java 8+) — anonymous functions
- **Method references** (Java 8+) — shorthand for lambdas
- **Stream API** (Java 8+) — declarative data processing
- **Optional** (Java 8+) — null-safe value containers
- **Records** (Java 14+) — immutable data carriers
- **Sealed classes** (Java 17+) — restricted inheritance
- **Pattern matching for switch** (Java 21) — cleaner conditional logic
- **Record patterns** (Java 21) — destructure records in patterns

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Define functional programming and distinguish it from imperative programming
2. Identify the four core principles of functional programming
3. Explain why Java adopted functional programming features
4. Recognize when to use functional vs. object-oriented approaches
5. Write simple lambda expressions
6. Understand the role of functional interfaces in Java

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Java Basics**: Classes, methods, variables, control flow
- **OOP Concepts**: Inheritance, interfaces, polymorphism
- **Collections Framework**: List, Set, Map, iteration patterns
- **Anonymous Classes**: Creating anonymous implementations of interfaces

---

## 4. Why This Concept Exists

### The Problem with Imperative Programming

Consider processing a list of orders to find total revenue for completed orders:

```java
// Imperative approach
double totalRevenue = 0.0;
for (Order order : orders) {
    if (order.getStatus() == OrderStatus.COMPLETED) {
        totalRevenue += order.getTotalAmount();
    }
}
```

This code has several issues:

1. **Mutable state**: `totalRevenue` is modified in a loop
2. **Manual iteration**: Developer must manage loop mechanics
3. **Hard to parallelize**: The mutable accumulator prevents parallel processing
4. **Verbosity**: The "how" obscures the "what"

### The Functional Solution

```java
// Functional approach
double totalRevenue = orders.stream()
    .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
    .mapToDouble(Order::getTotalAmount)
    .sum();
```

### Benefits Achieved

- **No mutable state**: Each operation returns a new value
- **Declarative**: Clearly states intent (filter, map, sum)
- **Parallelizable**: Can add `.parallel()` with zero code changes
- **Testable**: Each operation is independently testable
- **Composable**: Operations can be chained and reused

---

## 5. Problem Statement

### Real-World Scenario: E-Commerce Order Processing

An e-commerce platform needs to process millions of daily orders. The current imperative codebase suffers from:

- **Bug-prone code**: Mutable state leads to race conditions
- **Difficult maintenance**: Complex nested loops and conditionals
- **Poor performance**: Sequential processing cannot leverage multi-core CPUs
- **Testing challenges**: Side effects make unit testing difficult

### Requirements

1. Process orders declaratively (filter by status, calculate totals)
2. Support both sequential and parallel processing
3. Eliminate null pointer exceptions through safe value handling
4. Enable function composition for building complex transformations
5. Maintain readability while reducing boilerplate

---

## 6. Theory

### 6.1 Lambda Calculus Foundation

Functional programming is rooted in lambda calculus, a formal system developed by Alonzo Church in the 1930s. Lambda calculus provides a mathematical foundation for expressing computation using:

- **Variable binding**: `x` (a variable)
- **Abstraction**: `λx. body` (a function)
- **Application**: `(λx. body) argument` (function call)

Java's lambda expressions map directly to these concepts:

```java
// Lambda abstraction: λx. x + 1
// Java equivalent:
x -> x + 1
```

### 6.2 Pure Functions

A pure function has two properties:

1. **Deterministic**: Same input always produces same output
2. **No side effects**: Doesn't modify external state

```java
// Pure function
int add(int a, int b) {
    return a + b;
}

// Impure function (side effect)
int total = 0;
void accumulate(int value) {
    total += value; // modifies external state
}
```

### 6.3 Immutability

Immutable data cannot be changed after creation. Java provides several mechanisms:

```java
// Records (Java 14+) - immutable by default
public record Point(double x, double y) {}

// Final fields
public class ImmutablePoint {
    private final double x;
    private final double y;
    
    public ImmutablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

// Collections.unmodifiableList
List<String> names = List.of("Alice", "Bob", "Charlie");
```

### 6.4 Higher-Order Functions

Functions that accept or return other functions:

```java
// Function that accepts a function
<T> List<T> filter(List<T> list, Predicate<T> predicate) {
    return list.stream()
        .filter(predicate)
        .collect(Collectors.toList());
}

// Function that returns a function
Function<Integer, Integer> multiplier(int factor) {
    return x -> x * factor;
}
```

### 6.5 Referential Transparency

An expression is referentially transparent if it can be replaced with its value without changing the program's behavior:

```java
// Referentially transparent
int x = add(2, 3); // can be replaced with 5

// Not referentially transparent
int x = counter++; // counter changes each time
```

---

## 7. Internal Working

### 7.1 How Java Implements Lambdas

Java implements lambda expressions using **invokedynamic** (introduced in Java 7). When the compiler encounters a lambda, it:

1. Creates a synthetic method containing the lambda body
2. Generates an invokedynamic call site
3. Uses a **LambdaMetafactory** to create the functional interface implementation at runtime

```java
// Source code
Function<String, Integer> length = String::length;

// Compiler generates (conceptually):
// 1. A static method: static int lambda$main$0(String s) { return s.length(); }
// 2. An invokedynamic call that creates the Function implementation
```

### 7.2 Method Resolution

When a lambda or method reference is compiled:

1. The compiler identifies the target functional interface
2. Determines the SAM (Single Abstract Method) signature
3. Validates that the lambda/method reference is compatible
4. Generates bytecode using invokedynamic

```
Lambda Expression → Compiler → Synthetic Method + InvokeDynamic → LambdaMetafactory → Functional Interface Implementation
```

### 7.3 Type Inference

Java uses **target typing** to infer the type of lambda expressions:

```java
// The compiler knows this must be a Predicate<String>
Predicate<String> isLong = s -> s.length() > 10;

// The compiler infers the parameter type from context
list.stream()
    .filter(s -> s.length() > 10)  // s is inferred as String
    .forEach(System.out::println);
```

---

## 8. JVM Perspective

### 8.1 Bytecode Generation

Lambda expressions generate different bytecode than anonymous classes:

| Aspect | Anonymous Class | Lambda |
|--------|----------------|--------|
| **File** | Separate .class file | Same .class file |
| **Instantiation** | `new` keyword | `invokedynamic` |
| **Performance** | Class loading overhead | Lazy instantiation |
| **Memory** | One class per anonymous class | Shared proxy class |

### 8.2 InvokeDynamic Benefits

The `invokedynamic` instruction provides:

1. **Lazy class generation**: The functional interface implementation is created on first use
2. **JVM optimization**: The JVM can optimize call sites based on actual types
3. **Reduced class loading**: No separate class files for each lambda
4. **Better inlining**: JIT compiler can inline lambda bodies more effectively

### 8.3 LambdaMetafactory

`LambdaMetafactory` is the bootstrap method that creates lambda implementations:

```java
// Bootstrap method signature
public static CallSite metafactory(
    MethodHandles.Lookup caller,
    String invokedName,
    MethodType invokedType,
    MethodType samMethodType,
    MethodHandle implMethod,
    MethodType instantiatedMethodType
)
```

---

## 9. Memory Representation

### 9.1 Lambda vs Anonymous Class Memory

```
Anonymous Class:
┌─────────────────────────────┐
│ OrderProcessor$1.class      │  ← Separate class file
├─────────────────────────────┤
│ - Enclosing class reference │
│ - Captured variables         │
│ - Method: process()         │
└─────────────────────────────┘

Lambda:
┌─────────────────────────────┐
│ OrderProcessor.class        │  ← Same class file
├─────────────────────────────┤
│ - Synthetic method          │  ← Lambda body
│ - InvokeDynamic call site   │  ← Factory reference
└─────────────────────────────┘
```

### 9.2 Variable Capture

Lambdas can capture variables from their enclosing scope:

```java
void processOrders(List<Order> orders) {
    String prefix = "ORDER-"; // effectively final
    
    // Lambda captures 'prefix' variable
    orders.forEach(order -> 
        System.out.println(prefix + order.getId())
    );
}
```

**Memory Impact**: Captured variables are copied into the lambda object. For reference types, only the reference is copied (not the object).

### 9.3 Method Handle Caching

The JVM caches method handles for frequently used lambdas:

```
First invocation:
  LambdaMetafactory → creates implementation class → instantiates

Subsequent invocations:
  Cached implementation → direct instantiation (no metafactory)
```

---

## 10. Syntax

### 10.1 Lambda Expression Syntax

```java
// Full syntax
(parameters) -> expression

// With block body
(parameters) -> { statements; }

// Examples
() -> 42                                    // No parameters, returns 42
x -> x * 2                                 // One parameter (implicit type)
(int x, int y) -> x + y                    // Multiple parameters with types
(String name) -> { System.out.println(name); } // Block body
```

### 10.2 Method Reference Syntax

```java
// Reference to static method
ClassName::staticMethod

// Reference to instance method of particular object
object::instanceMethod

// Reference to instance method of arbitrary object
ClassName::instanceMethod

// Reference to constructor
ClassName::new
```

### 10.3 Functional Interface Declaration

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);  // Single abstract method
    
    // Default method (allowed)
    default Transformer<T, R> andThen(Transformer<R, ?> after) {
        return input -> after.transform(transform(input));
    }
    
    // Static method (allowed)
    static <T> Transformer<T, T> identity() {
        return input -> input;
    }
}
```

---

## 11. Easy Example

### Example 1: Runnable Lambda

```java
package academy.javaengineering.functional.introduction;

public class BasicLambda {
    public static void main(String[] args) {
        // Anonymous class (pre-Java 8)
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from anonymous class");
            }
        };
        
        // Lambda expression (Java 8+)
        Runnable newWay = () -> System.out.println("Hello from lambda");
        
        oldWay.run();
        newWay.run();
    }
}
```

### Example 2: Sorting with Comparator

```java
package academy.javaengineering.functional.introduction;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SortingExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob");
        
        // Old way
        names.sort(new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        
        // Lambda way
        names.sort((a, b) -> a.compareTo(b));
        
        // Method reference way
        names.sort(String::compareTo);
        
        System.out.println(names); // [Alice, Bob, Charlie]
    }
}
```

### Example 3: Simple Predicate

```java
package academy.javaengineering.functional.introduction;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        Predicate<Integer> isEven = n -> n % 2 == 0;
        
        List<Integer> evenNumbers = numbers.stream()
            .filter(isEven)
            .collect(Collectors.toList());
        
        System.out.println("Even numbers: " + evenNumbers);
        // Output: Even numbers: [2, 4, 6, 8, 10]
    }
}
```

---

## 12. Medium Example

### Example 1: Building a Data Transformer

```java
package academy.javaengineering.functional.introduction;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class DataTransformer {
    
    public static <T, R> List<R> transform(List<T> input, Function<T, R> transformer) {
        return input.stream()
            .map(transformer)
            .collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("alice", "bob", "charlie", "diana");
        
        // Transform 1: Capitalize first letter
        Function<String, String> capitalize = name -> 
            name.substring(0, 1).toUpperCase() + name.substring(1);
        
        List<String> capitalized = transform(names, capitalize);
        System.out.println("Capitalized: " + capitalized);
        // Output: Capitalized: [Alice, Bob, Charlie, Diana]
        
        // Transform 2: Get name length
        Function<String, Integer> nameLength = String::length;
        
        List<Integer> lengths = transform(names, nameLength);
        System.out.println("Lengths: " + lengths);
        // Output: Lengths: [5, 3, 7, 5]
        
        // Transform 3: Chain transformations
        Function<String, String> addPrefix = name -> "Mr./Ms. " + capitalize.apply(name);
        
        List<String> formalNames = transform(names, addPrefix);
        System.out.println("Formal: " + formalNames);
    }
}
```

### Example 2: Conditional Processing

```java
package academy.javaengineering.functional.introduction;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConditionalProcessor {
    
    public static <T> List<T> process(List<T> items, Predicate<T> condition) {
        return items.stream()
            .filter(condition)
            .collect(Collectors.toList());
    }
    
    public static <T> List<T> union(List<T> a, List<T> b) {
        return Arrays.asList(
            process(a, x -> true).stream()
                .concat(process(b, x -> true).stream())
                .collect(Collectors.toList())
        ).get(0);
    }
    
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        Predicate<Integer> greaterThan5 = n -> n > 5;
        Predicate<Integer> lessThan8 = n -> n < 8;
        
        // Combine predicates
        Predicate<Integer> between5And8 = greaterThan5.and(lessThan8);
        
        List<Integer> result = process(numbers, between5And8);
        System.out.println("Between 5 and 8: " + result);
        // Output: Between 5 and 8: [6, 7]
        
        // Negate a predicate
        Predicate<Integer> notEven = Predicate.isEqual(0).negate().and(n -> n % 2 != 0);
        List<Integer> oddNumbers = process(numbers, notEven);
        System.out.println("Odd numbers: " + oddNumbers);
    }
}
```

---

## 13. Hard Example

### Example 1: Generic Functional Pipeline

```java
package academy.javaengineering.functional.introduction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class FunctionalPipeline<I, O> {
    
    private final Function<I, O> transformer;
    
    private FunctionalPipeline(Function<I, O> transformer) {
        this.transformer = transformer;
    }
    
    public <R> FunctionalPipeline<I, R> andThen(Function<O, R> next) {
        return new FunctionalPipeline<>(transformer.andThen(next));
    }
    
    public O apply(I input) {
        return transformer.apply(input);
    }
    
    public List<O> applyAll(List<I> inputs) {
        List<O> results = new ArrayList<>();
        for (I input : inputs) {
            results.add(transformer.apply(input));
        }
        return results;
    }
    
    public static <T> FunctionalPipeline<T, T> identity() {
        return new FunctionalPipeline<>(Function.identity());
    }
    
    public static <T> FunctionalPipeline<T, T> filter(Predicate<T> predicate) {
        return new FunctionalPipeline<>(t -> {
            if (!predicate.test(t)) {
                throw new IllegalArgumentException("Predicate not satisfied");
            }
            return t;
        });
    }
    
    public static void main(String[] args) {
        // Build a pipeline: String → uppercase → add prefix → get length
        FunctionalPipeline<String, Integer> pipeline = 
            FunctionalPipeline.<String>identity()
                .andThen(String::toUpperCase)
                .andThen(s -> "ID: " + s)
                .andThen(String::length);
        
        List<String> names = List.of("alice", "bob", "charlie");
        List<Integer> lengths = pipeline.applyAll(names);
        
        System.out.println("Processed lengths: " + lengths);
        // Output: Processed lengths: [10, 7, 12]
    }
}
```

### Example 2: Lazy Evaluation Simulation

```java
package academy.javaengineering.functional.introduction;

import java.util.function.Supplier;

public class LazyEvaluation<T> {
    
    private final Supplier<T> supplier;
    private T cachedValue;
    private boolean computed = false;
    
    private LazyEvaluation(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public static <T> LazyEvaluation<T> of(Supplier<T> supplier) {
        return new LazyEvaluation<>(supplier);
    }
    
    public T get() {
        if (!computed) {
            cachedValue = supplier.get();
            computed = true;
        }
        return cachedValue;
    }
    
    public <R> LazyEvaluation<R> map(java.util.function.Function<T, R> mapper) {
        return new LazyEvaluation<>(() -> mapper.apply(get()));
    }
    
    public <R> LazyEvaluation<R> flatMap(java.util.function.Function<T, LazyEvaluation<R>> mapper) {
        return new LazyEvaluation<>(() -> mapper.apply(get()).get());
    }
    
    public static void main(String[] args) {
        System.out.println("Creating lazy value...");
        
        LazyEvaluation<String> lazy = LazyEvaluation.of(() -> {
            System.out.println("Computing expensive value...");
            return "EXPENSIVE_RESULT";
        });
        
        System.out.println("Value not computed yet");
        
        LazyEvaluation<Integer> mapped = lazy.map(s -> {
            System.out.println("Mapping value...");
            return s.length();
        });
        
        System.out.println("Still not computed");
        
        Integer result = mapped.get();
        System.out.println("Result: " + result);
        
        // Second access uses cached value
        Integer result2 = mapped.get();
        System.out.println("Cached result: " + result2);
    }
}
```

---

## 14. Enterprise Example

### Example 1: Order Processing Service

```java
package academy.javaengineering.functional.introduction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.*;

public class OrderProcessingService {
    
    public record Order(
        String id,
        String customerId,
        List<OrderItem> items,
        OrderStatus status,
        LocalDateTime createdAt
    ) {}
    
    public record OrderItem(
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice
    ) {}
    
    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
    
    // Functional interfaces for order operations
    @FunctionalInterface
    public interface OrderValidator {
        boolean validate(Order order);
        default OrderValidator and(OrderValidator other) {
            return order -> this.validate(order) && other.validate(order);
        }
    }
    
    @FunctionalInterface
    public interface OrderTransformer<T> {
        T transform(Order order);
        default <R> OrderTransformer<R> andThen(OrderTransformer<T> after) {
            return order -> after.transform(this.transform(order));
        }
    }
    
    public static void main(String[] args) {
        // Create test data
        List<Order> orders = createSampleOrders();
        
        // Define validators
        OrderValidator hasItems = order -> !order.items().isEmpty();
        OrderValidator isPending = order -> order.status() == OrderStatus.PENDING;
        OrderValidator isRecent = order -> order.createdAt().isAfter(LocalDateTime.now().minusDays(7));
        
        // Compose validators
        OrderValidator processable = hasItems.and(isPending).and(isRecent);
        
        // Filter and process
        List<Order> processableOrders = orders.stream()
            .filter(processable::validate)
            .toList();
        
        System.out.println("Processable orders: " + processableOrders.size());
    }
    
    private static List<Order> createSampleOrders() {
        return List.of(
            new Order("ORD-001", "CUST-001", 
                List.of(new OrderItem("P001", "Laptop", 1, new BigDecimal("999.99"))),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(2)),
            new Order("ORD-002", "CUST-002", 
                List.of(new OrderItem("P002", "Mouse", 2, new BigDecimal("29.99"))),
                OrderStatus.SHIPPED, LocalDateTime.now().minusDays(10)),
            new Order("ORD-003", "CUST-003", List.of(),
                OrderStatus.PENDING, LocalDateTime.now().minusDays(1))
        );
    }
}
```

### Example 2: Configuration Builder with Immutability

```java
package academy.javaengineering.functional.introduction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ImmutableConfig {
    
    private final Map<String, Object> properties;
    
    private ImmutableConfig(Map<String, Object> properties) {
        this.properties = Map.copyOf(properties);
    }
    
    public <T> T get(String key, Class<T> type) {
        Object value = properties.get(key);
        if (value == null) return null;
        return type.cast(value);
    }
    
    public <T> T getOrDefault(String key, Class<T> type, T defaultValue) {
        T value = get(key, type);
        return value != null ? value : defaultValue;
    }
    
    public ImmutableConfig with(String key, Object value) {
        Map<String, Object> newProps = new HashMap<>(properties);
        newProps.put(key, value);
        return new ImmutableConfig(newProps);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private final Map<String, Object> properties = new HashMap<>();
        
        public <T> Builder set(String key, T value, Class<T> type) {
            properties.put(key, type.cast(value));
            return this;
        }
        
        public Builder setString(String key, String value) {
            properties.put(key, value);
            return this;
        }
        
        public Builder setInt(String key, int value) {
            properties.put(key, value);
            return this;
        }
        
        public ImmutableConfig build() {
            return new ImmutableConfig(properties);
        }
    }
    
    public void forEach(BiConsumer<String, Object> action) {
        properties.forEach(action);
    }
    
    public static void main(String[] args) {
        ImmutableConfig config = ImmutableConfig.builder()
            .setString("database.url", "jdbc:postgresql://localhost:5432/mydb")
            .setString("database.username", "admin")
            .setInt("database.pool.size", 10)
            .setString("app.name", "OrderService")
            .build();
        
        String url = config.get("database.url", String.class);
        int poolSize = config.getOrDefault("database.pool.size", Integer.class, 5);
        
        System.out.println("Database URL: " + url);
        System.out.println("Pool size: " + poolSize);
        
        // Immutability: with() returns new instance
        ImmutableConfig devConfig = config.with("app.name", "OrderService-DEV");
        System.out.println("Original: " + config.get("app.name", String.class));
        System.out.println("Dev: " + devConfig.get("app.name", String.class));
    }
}
```

---

## 15. Performance

### 15.1 Lambda vs Anonymous Class Benchmarks

| Metric | Lambda | Anonymous Class |
|--------|--------|-----------------|
| **Creation Time** | ~2x faster | Baseline |
| **Memory per Instance** | ~16 bytes | ~40 bytes |
| **Class Loading** | No separate class | Separate .class file |
| **JIT Optimization** | Better inlining | Limited inlining |

### 15.2 Performance Tips

1. **Reuse lambda instances**: Store frequently used lambdas in final fields
2. **Avoid autoboxing**: Use primitive specializations (IntStream, etc.)
3. **Prefer method references**: Often generate more optimizable bytecode
4. **Consider lazy evaluation**: Stream operations are lazy by default

### 15.3 Benchmarking Example

```java
package academy.javaengineering.functional.introduction;

import java.util.function.IntBinaryOperator;

public class LambdaPerformance {
    
    // Reused lambda (preferred)
    private static final IntBinaryOperator ADD = (a, b) -> a + b;
    
    public static void main(String[] args) {
        int iterations = 100_000_000;
        
        // Benchmark 1: Method reference
        long start = System.nanoTime();
        int sum1 = 0;
        for (int i = 0; i < iterations; i++) {
            sum1 = ADD.applyAsInt(sum1, i);
        }
        long methodRefTime = System.nanoTime() - start;
        
        // Benchmark 2: Lambda in loop (creates new instance each time)
        start = System.nanoTime();
        int sum2 = 0;
        for (int i = 0; i < iterations; i++) {
            sum2 = ((IntBinaryOperator) (a, b) -> a + b).applyAsInt(sum2, i);
        }
        long lambdaInLoopTime = System.nanoTime() - start;
        
        System.out.printf("Method reference: %.2f ms%n", methodRefTime / 1_000_000.0);
        System.out.printf("Lambda in loop: %.2f ms%n", lambdaInLoopTime / 1_000_000.0);
        System.out.printf("Results match: %b%n", sum1 == sum2);
    }
}
```

---

## 16. Best Practices

1. **Keep lambdas short**: If a lambda exceeds 3-4 lines, extract it to a named method
2. **Use method references**: When a lambda simply calls an existing method
3. **Prefer immutable captures**: Don't capture mutable variables
4. **Name functional interfaces**: Use `@FunctionalInterface` annotation
5. **Document side effects**: If a lambda has side effects, document them
6. **Prefer primitive streams**: Use `IntStream`, `LongStream`, `DoubleStream` for performance
7. **Avoid null in lambdas**: Use `Optional` instead of returning null
8. **Test lambdas independently**: Extract complex lambdas to testable methods

---

## 17. Common Mistakes

### Mistake 1: Mutable Variable Capture

```java
// WRONG: Compiler error - variable must be effectively final
int counter = 0;
list.forEach(item -> counter++);  // Won't compile!

// CORRECT: Use AtomicInteger or collect
AtomicInteger counter = new AtomicInteger(0);
list.forEach(item -> counter.incrementAndGet());
```

### Mistake 2: Overusing Lambdas

```java
// WRONG: Lambda is too complex
list.stream()
    .filter(item -> {
        if (item == null) return false;
        if (item.getStatus() == null) return false;
        if (item.getStatus() == Status.INACTIVE) return false;
        if (item.getCreatedAt().isBefore(LocalDate.now().minusDays(30))) return false;
        return true;
    })
    .toList();

// CORRECT: Extract to a named Predicate
Predicate<Item> isActiveRecentItem = this::isActiveRecent;
list.stream().filter(isActiveRecentItem).toList();
```

### Mistake 3: Ignoring Exception Handling

```java
// WRONG: Unchecked exception handling
list.forEach(item -> riskyOperation(item));

// CORRECT: Handle exceptions explicitly
list.forEach(item -> {
    try {
        riskyOperation(item);
    } catch (Exception e) {
        logger.error("Failed to process item: " + item, e);
    }
});
```

---

## 18. Pitfalls

1. **Performance with large datasets**: Intermediate operations create new Stream objects; avoid creating unnecessary streams
2. **Parallel stream overhead**: Parallel streams use ForkJoinPool; don't use for small datasets
3. **Side effects in lambdas**: Side effects break referential transparency and make code unpredictable
4. **Debugging difficulty**: Stack traces with lambdas can be cryptic; use named methods for complex operations
5. **Memory leaks with captured variables**: Long-lived lambdas can prevent garbage collection of captured objects

---

## 19. Debugging Tips

### 1. Use Named Methods for Complex Logic

```java
// Instead of complex lambda
list.stream()
    .filter(item -> item.getStatus() == Status.ACTIVE && item.getPriority() > 5)
    .toList();

// Extract to named method
Predicate<Item> isHighPriorityActive = this::isHighPriorityActive;
list.stream().filter(isHighPriorityActive).toList();
```

### 2. Add Debug Logging

```java
list.stream()
    .filter(item -> {
        boolean result = item.getPrice() > 100;
        System.out.println("Item " + item.getId() + " > 100: " + result);
        return result;
    })
    .toList();
```

### 3. Use peek() for Stream Debugging

```java
list.stream()
    .filter(item -> item.getPrice() > 100)
    .peek(item -> System.out.println("After filter: " + item))
    .map(Item::getName)
    .peek(name -> System.out.println("After map: " + name))
    .toList();
```

### 4. Enable JVM Lambda Debugging

```bash
java -Djdk.internal.lambdaDumpProxyClasses=true -jar app.jar
```

---

## 20. Comparison Table

| Feature | Imperative | Functional |
|---------|-----------|------------|
| **State** | Mutable | Immutable |
| **Loop** | for/while | Stream operations |
| **Conditionals** | if/else | Predicate composition |
| **Assignment** | Variable mutation | Expression evaluation |
| **Parallelization** | Manual threading | Parallel streams |
| **Testability** | Hard (side effects) | Easy (pure functions) |
| **Readability** | Verbose | Concise |
| **Debugging** | Step through | Harder to trace |

---

## 21. Decision Tree

```
Should you use functional programming in Java?

┌─ Is the operation data transformation?
│  ├─ YES → Use Stream API with lambdas
│  └─ NO → Continue
│
├─ Is the operation filtering/reducing?
│  ├─ YES → Use Stream operations (filter, reduce)
│  └─ NO → Continue
│
├─ Is the logic simple (< 3 lines)?
│  ├─ YES → Use inline lambda
│  └─ NO → Extract to named method
│
├─ Is the operation parallelizable?
│  ├─ YES → Consider parallel streams
│  └─ NO → Use sequential streams
│
└─ Do you need to reuse the logic?
   ├─ YES → Create @FunctionalInterface
   └─ NO → Use inline lambda
```

---

## 22. Interview Questions

### Q1: What is the difference between a lambda and an anonymous class?

**Answer**: Lambdas use `invokedynamic` for efficient implementation without creating a separate class file. Anonymous classes create a separate `.class` file and have more overhead. Lambdas can only be used with functional interfaces (single abstract method).

### Q2: Can lambdas capture mutable variables?

**Answer**: No. Lambdas can only capture **effectively final** variables. This is a design decision to avoid concurrency issues and maintain referential transparency.

### Q3: What is a functional interface?

**Answer**: An interface with exactly one abstract method (SAM - Single Abstract Method). Examples include `Predicate<T>`, `Function<T,R>`, `Consumer<T>`, and `Supplier<T>`. Annotated with `@FunctionalInterface` for compile-time validation.

### Q4: When should you use method references over lambdas?

**Answer**: Use method references when a lambda simply calls an existing method. Method references are more readable and sometimes generate more efficient bytecode. Prefer `ClassName::methodName` over `x -> ClassName.methodName(x)`.

### Q5: What is the performance impact of using functional programming in Java?

**Answer**: Lambdas are generally faster than anonymous classes due to `invokedynamic` optimization. However, creating streams and intermediate operations has overhead. For small datasets, traditional loops may be faster. For large datasets, parallel streams can provide significant speedup.

---

## 23. Exercises

### Exercise 1: Lambda Basics
Convert the following anonymous classes to lambda expressions:

```java
// 1. Comparator
Comparator<String> comp = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};

// 2. ActionListener
ActionListener listener = new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Button clicked!");
    }
};

// 3. Thread
Thread thread = new Thread(new Runnable() {
    @Override
    public void run() {
        System.out.println("Running in thread");
    }
});
```

### Exercise 2: Functional Interface Implementation
Create a `@FunctionalInterface` called `StringProcessor` that takes a `String` and returns a `String`. Implement it with lambdas to:
1. Convert to uppercase
2. Remove whitespace
3. Add a prefix

### Exercise 3: Method References
Convert these lambdas to method references:

```java
Function<String, Integer> length = s -> s.length();
Predicate<String> isEmpty = s -> s.isEmpty();
Supplier<List<String>> listFactory = () -> new ArrayList<>();
```

---

## 24. Assignments

### Assignment 1: Data Transformation Library
Create a utility class with the following methods:
- `<T, R> List<R> map(List<T> list, Function<T, R> mapper)`
- `<T> List<T> filter(List<T> list, Predicate<T> predicate)`
- `<T> T reduce(List<T> list, T identity, BinaryOperator<T> accumulator)`

### Assignment 2: Functional Configuration
Build an immutable configuration system using functional interfaces that supports:
- Type-safe property access
- Default values
- Configuration composition (merging two configurations)

### Assignment 3: Event Processing Pipeline
Design an event processing system using:
- Functional interfaces for event validation
- Lambda expressions for event transformation
- Method references for event logging

---

## 25. Mini Project

### Project: Functional String Calculator

Build a string calculator that uses functional programming concepts:

**Requirements:**
1. Create a `@FunctionalInterface` for operations: `double apply(String expression)`
2. Implement basic operations: add, subtract, multiply, divide
3. Support operator composition: `addThenMultiply(double a, double b, double factor)`
4. Use method references for built-in operations
5. Implement error handling using `Optional`
6. Support custom operations via lambda injection

**Starter Code:**
```java
package academy.javaengineering.functional.introduction.project;

import java.util.Optional;
import java.util.function.BinaryOperator;

public class FunctionalCalculator {
    
    @FunctionalInterface
    public interface Operation {
        Optional<Double> apply(double a, double b);
    }
    
    public static final Operation ADD = (a, b) -> Optional.of(a + b);
    public static final Operation SUBTRACT = (a, b) -> Optional.of(a - b);
    public static final Operation MULTIPLY = (a, b) -> Optional.of(a * b);
    public static final Operation DIVIDE = (a, b) -> b != 0 ? Optional.of(a / b) : Optional.empty();
    
    // TODO: Implement compose method
    // TODO: Implement chain method
    // TODO: Implement withLogging decorator
}
```

---

## 26. Summary

Functional programming in Java provides a powerful alternative to traditional imperative programming. Key takeaways:

1. **Lambda expressions** provide concise syntax for creating function implementations
2. **Functional interfaces** are the foundation of Java's functional programming support
3. **Immutability** and **pure functions** lead to more predictable and testable code
4. **Method references** simplify lambda expressions and improve readability
5. **Stream API** enables declarative data processing with built-in parallelization support

### Next Steps
- Topic 02: Lambda Expressions — Deep dive into lambda syntax and scoping
- Topic 03: Functional Interfaces — Mastering built-in and custom interfaces

---

## 27. References

1. [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
2. [Oracle Java Tutorials: Functional Interfaces](https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html)
3. [Java 21 Language Specification: Lambda Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html)
4. [Effective Java, 3rd Edition - Item 42: Prefer lambdas to anonymous classes](https://www.oreilly.com/library/view/effective-java/9780134686097/)
5. [Java Performance, 2nd Edition - Chapter on Lambdas](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/)
6. [Baeldung: Functional Programming in Java](https://www.baeldung.com/java-functional-programming)
