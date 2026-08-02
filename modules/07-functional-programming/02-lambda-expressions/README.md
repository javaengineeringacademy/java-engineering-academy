# Topic 02: Lambda Expressions

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

Lambda expressions are anonymous functions that provide a concise way to implement functional interfaces. Introduced in Java 8, lambdas enable functional programming by treating functions as first-class citizens—passing them as arguments, returning them from methods, and storing them in variables.

A lambda expression in Java consists of:
- **Parameter list**: The input parameters
- **Arrow token**: `->`
- **Body**: The expression or block of code to execute

```java
// Full syntax
(parameters) -> expression

// With block body
(parameters) -> { statements; }
```

Lambda expressions are NOT a new feature in the language itself but rather a syntactic sugar for creating anonymous class instances that implement functional interfaces.

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Write lambda expressions with correct syntax
2. Understand lambda scoping rules and variable capture
3. Distinguish between expression lambdas and block lambdas
4. Apply type inference in lambda parameters
5. Use effectively final variables in lambdas
6. Implement custom functional interfaces with lambdas
7. Recognize when to use lambdas vs method references

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Java Basics**: Variables, methods, control flow
- **Anonymous Classes**: Creating inline implementations of interfaces
- **Generics**: Understanding type parameters
- **Functional Interfaces**: What they are and how they work (covered in Topic 03)

---

## 4. Why This Concept Exists

### The Problem with Anonymous Classes

Before Java 8, implementing functional interfaces required verbose anonymous classes:

```java
// Pre-Java 8: Verbose anonymous class
Comparator<String> comparator = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
};
```

This code is unnecessarily verbose because:
1. The class name is redundant (`Comparator<String>`)
2. The method name is obvious from context (`compare`)
3. The type parameters repeat (`<String>`)

### The Lambda Solution

```java
// Java 8+: Concise lambda
Comparator<String> comparator = (a, b) -> a.compareTo(b);
```

Lambda expressions reduce boilerplate by:
1. Inferring parameter types from context
2. Eliminating the need for class/method declarations
3. Providing a concise syntax for simple implementations

---

## 5. Problem Statement

### Real-World Scenario: Event Handler Registration

An application needs to register event handlers for user interactions. The current codebase uses anonymous classes, resulting in:

- **Verbosity**: Each handler requires 5-7 lines of boilerplate
- **Noise**: Class/method declarations obscure the actual logic
- **Maintenance burden**: Adding handlers requires writing repetitive code

### Requirements

1. Reduce boilerplate for implementing functional interfaces
2. Maintain type safety
3. Support both simple expressions and complex block logic
4. Enable variable capture from enclosing scope
5. Provide clear scoping rules for captured variables

---

## 6. Theory

### 6.1 Lambda Anatomy

A lambda expression consists of three parts:

```
┌─────────────┬─────────────┬─────────────┐
│  Parameters  │  Arrow (→)  │     Body    │
├─────────────┼─────────────┼─────────────┤
│  (a, b)     │      →      │  a + b      │
│  x          │      →      │  { ... }    │
│  ()         │      →      │  42         │
└─────────────┴─────────────┴─────────────┘
```

### 6.2 Parameter Variations

```java
// No parameters
() -> System.out.println("Hello")

// One parameter (parentheses optional)
x -> x * 2
(x) -> x * 2

// Multiple parameters
(x, y) -> x + y
(int x, int y) -> x + y

// Var parameters (Java 11+)
(var x, var y) -> x + y
```

### 6.3 Body Variations

```java
// Expression body (single expression, implicit return)
x -> x * 2
x -> x > 0 ? "positive" : "negative"

// Block body (multiple statements, explicit return)
x -> {
    System.out.println("Processing: " + x);
    return x * 2;
}
```

### 6.4 Type Inference

Java infers lambda types from the target context:

```java
// Target type: Comparator<String>
Comparator<String> comp = (a, b) -> a.compareTo(b);
// a and b are inferred as String

// Target type: Predicate<Integer>
Predicate<Integer> isPositive = n -> n > 0;
// n is inferred as Integer

// Explicit types when needed (rare)
Comparator<String> comp2 = (String a, String b) -> a.compareTo(b);
```

### 6.5 Variable Capture Rules

Lambdas can capture variables from their enclosing scope with these rules:

1. **Effectively final**: The variable must not be modified after initialization
2. **Stack-based**: Only stack-local variables can be captured
3. **Instance fields**: Can be accessed but not reassigned
4. **Static fields**: Can be accessed and reassigned (but discouraged)

```java
void process(List<String> items) {
    String prefix = "ITEM-"; // effectively final
    
    items.forEach(item -> {
        // Can read 'prefix' (effectively final)
        System.out.println(prefix + item);
        
        // Cannot modify 'prefix'
        // prefix = "NEW-"; // Compilation error!
    });
}
```

### 6.6 Scope Resolution

Lambda expressions have their own scope but can access:

```java
public class ScopeExample {
    private int instanceField = 10; // Accessible
    
    public void method() {
        int localVar = 20; // Effectively final
        
        Runnable lambda = () -> {
            System.out.println(instanceField); // OK
            System.out.println(localVar);      // OK (effectively final)
            // localVar++;                     // Compilation error
        };
    }
}
```

---

## 7. Internal Working

### 7.1 Compilation Process

When the Java compiler encounters a lambda expression, it:

1. **Validates the target type**: Ensures the lambda matches a functional interface
2. **Generates a synthetic method**: Creates a private method containing the lambda body
3. **Emits invokedynamic**: Generates a call site using `invokedynamic`

```java
// Source code
Function<String, Integer> length = s -> s.length();

// Compiled to (conceptual bytecode):
// 1. Synthetic method: private static int lambda$main$0(String s) { return s.length(); }
// 2. invokedynamic call: LambdaMetafactory.metafactory(..., "apply", ..., lambda$main$0, ...)
```

### 7.2 LambdaMetafactory

`LambdaMetafactory` is the bootstrap method that creates functional interface implementations at runtime:

```
┌─────────────────────────────────────────────────────────┐
│                    LambdaMetafactory                     │
├─────────────────────────────────────────────────────────┤
│ 1. Creates proxy class implementing the functional interface │
│ 2. Delegates calls to the synthetic method               │
│ 3. Caches the implementation for future use              │
└─────────────────────────────────────────────────────────┘
```

### 7.3 Accessor Methods

Lambdas access captured variables through accessor methods generated by the compiler:

```java
void process(List<String> items) {
    String prefix = "ITEM-";
    
    items.forEach(item -> System.out.println(prefix + item));
}

// Compiler generates:
private static void lambda$process$0(String prefix, String item) {
    System.out.println(prefix + item);
}
```

---

## 8. JVM Perspective

### 8.1 Bytecode Comparison

| Aspect | Anonymous Class | Lambda |
|--------|----------------|--------|
| **Class Files** | One per anonymous class | None (same class) |
| **Instantiation** | `new` instruction | `invokedynamic` |
| **Method Invocation** | Virtual call | Direct method call |
| **JIT Optimization** | Limited | Aggressive inlining |

### 8.2 InvokeDynamic Benefits

The `invokedynamic` instruction provides several advantages:

1. **Lazy instantiation**: The implementation class is created on first use
2. **Type specialization**: The JVM can create specialized implementations
3. **Better inlining**: JIT can inline lambda bodies more effectively
4. **Reduced memory**: No separate class files needed

### 8.3 Lambda Implementation Classes

For each lambda, the JVM creates a hidden class:

```
TargetClass$$Lambda$1/0x0000000800c01c00
```

These classes are:
- Loaded by the bootstrap class loader
- Not visible in standard class loading
- Subject to garbage collection when no longer referenced

---

## 9. Memory Representation

### 9.1 Lambda Object Layout

```
┌─────────────────────────────────────┐
│        Lambda Implementation        │
├─────────────────────────────────────┤
│  Header (mark word + klass pointer) │
├─────────────────────────────────────┤
│  Captured variable 1 (reference)    │
│  Captured variable 2 (reference)    │
│  ...                                │
├─────────────────────────────────────┤
│  Method handle to synthetic method  │
└─────────────────────────────────────┘
```

### 9.2 Variable Capture Memory

When a lambda captures variables:

```java
void process(String prefix, List<String> items) {
    items.forEach(item -> System.out.println(prefix + item));
}
```

Memory layout:

```
Stack Frame:
┌─────────────────────────────────────┐
│  prefix (String reference)          │  ← Captured by lambda
│  items (List reference)             │  ← Not captured (used directly)
└─────────────────────────────────────┘

Lambda Object:
┌─────────────────────────────────────┐
│  prefix reference                   │  ← Copied reference
│  Method handle to lambda$process$0  │
└─────────────────────────────────────┘
```

### 9.3 Performance Implications

- **Captured references**: Only the reference is copied, not the object
- **Captured primitives**: The value is boxed and copied
- **No capture**: Lambda has no overhead for captured variables

---

## 10. Syntax

### 10.1 Basic Lambda Forms

```java
// Expression body
(x) -> x * 2
x -> x * 2
() -> 42

// Block body
(x) -> { return x * 2; }
(x) -> { System.out.println(x); return x; }

// Multiple statements
(x, y) -> {
    int sum = x + y;
    System.out.println("Sum: " + sum);
    return sum;
}
```

### 10.2 Parameter Type Inference

```java
// Types inferred from context
Comparator<String> comp = (a, b) -> a.compareTo(b);
// a, b → String

Predicate<Integer> pred = n -> n > 0;
// n → Integer

// Explicit types (when needed)
Comparator<String> comp2 = (String a, String b) -> a.compareTo(b);
```

### 10.3 Var Parameters (Java 11+)

```java
// Using var for lambda parameters
Function<String, Integer> length = (var s) -> s.length();
Comparator<String> comp = (var a, var b) -> a.compareTo(b);

// Mixing var with explicit types (NOT allowed)
// Comparator<String> comp = (var a, String b) -> a.compareTo(b); // Compilation error
```

### 10.4 Single-Expression Lambdas

```java
// Implicit return
Function<Integer, Integer> doubleIt = x -> x * 2;

// Conditional expression
Function<Integer, String> classify = n -> n > 0 ? "positive" : "non-positive";

// Method call
Consumer<String> printer = System.out::println; // Method reference
Consumer<String> printer2 = s -> System.out.println(s); // Lambda equivalent
```

---

## 11. Easy Example

### Example 1: Runnable Lambda

```java
package academy.javaengineering.functional.lambda;

public class BasicLambdas {
    public static void main(String[] args) {
        // No parameters
        Runnable hello = () -> System.out.println("Hello, World!");
        hello.run();
        
        // With side effect
        Runnable logTime = () -> {
            System.out.println("Current time: " + System.currentTimeMillis());
            System.out.println("Thread: " + Thread.currentThread().getName());
        };
        logTime.run();
    }
}
```

### Example 2: Predicate Lambda

```java
package academy.javaengineering.functional.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateLambdas {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        Predicate<String> startsWithA = name -> name.startsWith("A");
        Predicate<String> hasFourChars = name -> name.length() == 4;
        
        System.out.println("Names starting with A:");
        names.stream()
            .filter(startsWithA)
            .forEach(name -> System.out.println("  " + name));
        
        System.out.println("\nNames with 4 characters:");
        names.stream()
            .filter(hasFourChars)
            .forEach(name -> System.out.println("  " + name));
    }
}
```

### Example 3: Function Lambda

```java
package academy.javaengineering.functional.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionLambdas {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("hello", "world", "java", "lambda");
        
        Function<String, String> capitalize = word -> 
            word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
        
        Function<String, Integer> toLength = String::length;
        
        List<String> capitalized = words.stream()
            .map(capitalize)
            .toList();
        
        List<Integer> lengths = words.stream()
            .map(toLength)
            .toList();
        
        System.out.println("Capitalized: " + capitalized);
        System.out.println("Lengths: " + lengths);
    }
}
```

---

## 12. Medium Example

### Example 1: Lambda with Closure

```java
package academy.javaengineering.functional.lambda;

import java.util.function.UnaryOperator;
import java.util.function.Function;

public class ClosureExample {
    
    public static Function<String, String> createPrefixer(String prefix) {
        // Lambda captures 'prefix' variable
        return s -> prefix + s;
    }
    
    public static Function<Integer, Integer> createMultiplier(int factor) {
        // Lambda captures 'factor' variable
        return x -> x * factor;
    }
    
    public static void main(String[] args) {
        // Create prefixers
        Function<String, String> mrPrefix = createPrefixer("Mr. ");
        Function<String, String> drPrefix = createPrefixer("Dr. ");
        
        System.out.println(mrPrefix.apply("Smith"));   // Mr. Smith
        System.out.println(drPrefix.apply(" Wilson"));  // Dr.  Wilson
        
        // Create multipliers
        Function<Integer, Integer> doubler = createMultiplier(2);
        Function<Integer, Integer> tripler = createMultiplier(3);
        
        System.out.println("5 doubled: " + doubler.apply(5));   // 10
        System.out.println("5 tripled: " + tripler.apply(5));   // 15
    }
}
```

### Example 2: Complex Predicate Composition

```java
package academy.javaengineering.functional.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateComposition {
    
    record User(String name, int age, boolean active, String email) {}
    
    public static void main(String[] args) {
        List<User> users = Arrays.asList(
            new User("Alice", 25, true, "alice@example.com"),
            new User("Bob", 17, true, "bob@example.com"),
            new User("Charlie", 30, false, "charlie@example.com"),
            new User("Diana", 22, true, "diana@example.com"),
            new User("Eve", 19, true, "eve@example.com")
        );
        
        // Build complex predicates
        Predicate<User> isActive = User::active;
        Predicate<User> isAdult = user -> user.age() >= 18;
        Predicate<User> hasValidEmail = user -> user.email() != null && user.email().contains("@");
        
        Predicate<User> eligibleUser = isActive
            .and(isAdult)
            .and(hasValidEmail);
        
        Predicate<User> minorOrInactive = isAdult.negate().or(isActive.negate());
        
        System.out.println("Eligible users:");
        users.stream()
            .filter(eligibleUser)
            .forEach(user -> System.out.println("  " + user.name()));
        
        System.out.println("\nMinor or inactive users:");
        users.stream()
            .filter(minorOrInactive)
            .forEach(user -> System.out.println("  " + user.name()));
    }
}
```

### Example 3: Lambda in Stream Pipeline

```java
package academy.javaengineering.functional.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamPipeline {
    
    record Product(String name, double price, String category, int stock) {}
    
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99, "Electronics", 10),
            new Product("Phone", 699.99, "Electronics", 25),
            new Product("Desk", 299.99, "Furniture", 5),
            new Product("Chair", 149.99, "Furniture", 15),
            new Product("Headphones", 199.99, "Electronics", 30)
        );
        
        // Complex pipeline with lambdas
        Function<Product, String> formatProduct = p -> 
            String.format("%s ($%.2f) - %d in stock", p.name(), p.price(), p.stock());
        
        Predicate<Product> isInStock = p -> p.stock() > 0;
        Predicate<Product> isAffordable = p -> p.price() < 500;
        
        System.out.println("Affordable products in stock:");
        products.stream()
            .filter(isInStock)
            .filter(isAffordable)
            .map(formatProduct)
            .forEach(p -> System.out.println("  " + p));
        
        // Calculate total value
        double totalValue = products.stream()
            .filter(isInStock)
            .mapToDouble(p -> p.price() * p.stock())
            .sum();
        
        System.out.printf("\nTotal inventory value: $%.2f%n", totalValue);
    }
}
```

---

## 13. Hard Example

### Example 1: Generic Lambda Pipeline Builder

```java
package academy.javaengineering.functional.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class LambdaPipeline<I, O> {
    
    private final Function<I, O> pipeline;
    
    private LambdaPipeline(Function<I, O> pipeline) {
        this.pipeline = pipeline;
    }
    
    public <R> LambdaPipeline<I, R> addStep(Function<O, R> step) {
        return new LambdaPipeline<>(pipeline.andThen(step));
    }
    
    public LambdaPipeline<I, O> addFilter(Predicate<O> predicate) {
        return new LambdaPipeline<>(pipeline.andThen(result -> {
            if (!predicate.test(result)) {
                throw new FilteredException("Filter failed: " + result);
            }
            return result;
        }));
    }
    
    public O execute(I input) {
        return pipeline.apply(input);
    }
    
    public List<O> executeAll(List<I> inputs) {
        List<O> results = new ArrayList<>();
        for (I input : inputs) {
            try {
                results.add(pipeline.apply(input));
            } catch (FilteredException e) {
                // Skip filtered items
            }
        }
        return results;
    }
    
    public static <T> LambdaPipeline<T, T> identity() {
        return new LambdaPipeline<>(Function.identity());
    }
    
    public static class FilteredException extends RuntimeException {
        public FilteredException(String message) {
            super(message);
        }
    }
    
    public static void main(String[] args) {
        // Build a text processing pipeline
        LambdaPipeline<String, String> textPipeline = LambdaPipeline.<String>identity()
            .addStep(String::trim)
            .addStep(String::toLowerCase)
            .addStep(s -> s.replaceAll("[^a-z0-9\\s]", ""))
            .addStep(s -> s.replaceAll("\\s+", " "))
            .addFilter(s -> !s.isEmpty());
        
        List<String> texts = List.of(
            "  Hello, World!  ",
            "Java Programming  101",
            "   ",
            "Lambda Expressions are awesome!!!",
            ""
        );
        
        System.out.println("Processed texts:");
        List<String> processed = textPipeline.executeAll(texts);
        processed.forEach(text -> System.out.println("  [" + text + "]"));
    }
}
```

### Example 2: Recursive Lambda with Y-Combinator

```java
package academy.javaengineering.functional.lambda;

import java.util.function.UnaryOperator;

public class YCombinator {
    
    @FunctionalInterface
    public interface Function<T, R> {
        R apply(T t);
    }
    
    @FunctionalInterface
    public interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }
    
    @FunctionalInterface
    public interface UnaryOperatorWithSelf<T> {
        T apply(T self, T arg);
    }
    
    // Y-combinator: enables recursion in lambda expressions
    @SuppressWarnings("unchecked")
    public static <T, R> Function<T, R> y(UnaryOperatorWithSelf<Function<T, R>> f) {
        return (Function<T, R>) new Object() {
            Function<T, R> func = arg -> f.apply(this.func, arg);
        }.func;
    }
    
    public static void main(String[] args) {
        // Factorial using Y-combinator
        Function<Integer, Integer> factorial = y(
            (self, n) -> n <= 1 ? 1 : n * self.apply(n - 1)
        );
        
        // Fibonacci using Y-combinator
        Function<Integer, Integer> fibonacci = y(
            (self, n) -> n <= 1 ? n : self.apply(n - 1) + self.apply(n - 2)
        );
        
        System.out.println("Factorials:");
        for (int i = 0; i <= 10; i++) {
            System.out.printf("  %d! = %d%n", i, factorial.apply(i));
        }
        
        System.out.println("\nFibonacci sequence:");
        for (int i = 0; i <= 15; i++) {
            System.out.printf("  F(%d) = %d%n", i, fibonacci.apply(i));
        }
    }
}
```

### Example 3: Stateful Lambda with Thread Safety

```java
package academy.javaengineering.functional.lambda;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class StatefulLambdas {
    
    // Thread-safe counter using AtomicInteger
    public static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        
        public IntUnaryOperator incrementer() {
            return count::incrementAndGet;
        }
        
        public IntUnaryOperator adder(int value) {
            return x -> count.addAndGet(value);
        }
        
        public int get() {
            return count.get();
        }
    }
    
    // Accumulator using functional interface
    @FunctionalInterface
    public interface Accumulator<T> {
        T accumulate(T current, T value);
        
        default Accumulator<T> andThen(Accumulator<T> after) {
            return (current, value) -> after.accumulate(this.accumulate(current, value), value);
        }
    }
    
    public static void main(String[] args) {
        // Atomic counter example
        AtomicCounter counter = new AtomicCounter();
        IntUnaryOperator increment = counter.incrementer();
        
        System.out.println("Counter: " + increment.applyAsInt(0));
        System.out.println("Counter: " + increment.applyAsInt(0));
        System.out.println("Counter: " + increment.applyAsInt(0));
        
        // Accumulator example
        Accumulator<Integer> sumAccumulator = Integer::sum;
        Accumulator<Integer> productAccumulator = (a, b) -> a * b;
        
        // Chain accumulators
        Accumulator<Integer> combined = sumAccumulator.andThen(productAccumulator);
        
        int result = 0;
        result = combined.accumulate(result, 5);  // (0 + 5) * 5 = 25
        result = combined.accumulate(result, 3);  // (25 + 3) * 3 = 84
        System.out.println("Combined accumulator result: " + result);
    }
}
```

---

## 14. Enterprise Example

### Example 1: Event-Driven Architecture with Lambdas

```java
package academy.javaengineering.functional.lambda;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class EventSystem {
    
    @FunctionalInterface
    public interface EventListener<T> {
        void onEvent(T event);
    }
    
    @FunctionalInterface
    public interface EventFilter<T> {
        boolean shouldHandle(T event);
    }
    
    public static class EventPublisher<T> {
        private final Map<String, List<EventListener<T>>> listeners = new ConcurrentHashMap<>();
        private final Map<String, List<EventFilter<T>>> filters = new ConcurrentHashMap<>();
        
        public void subscribe(String eventType, EventListener<T> listener) {
            listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
        }
        
        public void addFilter(String eventType, EventFilter<T> filter) {
            filters.computeIfAbsent(eventType, k -> new ArrayList<>()).add(filter);
        }
        
        public void publish(String eventType, T event) {
            List<EventFilter<T>> eventFilters = filters.getOrDefault(eventType, List.of());
            boolean shouldHandle = eventFilters.stream()
                .allMatch(filter -> filter.shouldHandle(event));
            
            if (shouldHandle) {
                List<EventListener<T>> eventListeners = listeners.getOrDefault(eventType, List.of());
                eventListeners.forEach(listener -> listener.onEvent(event));
            }
        }
    }
    
    // Event types
    public record UserCreatedEvent(String userId, String username, String email) {}
    public record OrderPlacedEvent(String orderId, String userId, double amount) {}
    
    public static void main(String[] args) {
        // Create event publisher
        EventPublisher<Object> publisher = new EventPublisher<>();
        
        // Subscribe to events with lambdas
        publisher.subscribe("UserCreated", event -> {
            UserCreatedEvent e = (UserCreatedEvent) event;
            System.out.println("Welcome email sent to: " + e.email());
        });
        
        publisher.subscribe("UserCreated", event -> {
            UserCreatedEvent e = (UserCreatedEvent) event;
            System.out.println("User created in database: " + e.username());
        });
        
        publisher.subscribe("OrderPlaced", event -> {
            OrderPlacedEvent e = (OrderPlacedEvent) event;
            System.out.println("Order processed: " + e.orderId() + " - $" + e.amount());
        });
        
        // Add filter
        publisher.addFilter("OrderPlaced", event -> {
            OrderPlacedEvent e = (OrderPlacedEvent) event;
            return e.amount() > 0;
        });
        
        // Publish events
        publisher.publish("UserCreated", new UserCreatedEvent("U001", "alice", "alice@example.com"));
        publisher.publish("OrderPlaced", new OrderPlacedEvent("ORD001", "U001", 99.99));
        publisher.publish("OrderPlaced", new OrderPlacedEvent("ORD002", "U001", -10.00)); // Filtered
    }
}
```

### Example 2: Configuration System with Lambda Validation

```java
package academy.javaengineering.functional.lambda;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConfigSystem {
    
    @FunctionalInterface
    public interface ConfigValidator<T> {
        ValidationResult validate(T value);
        
        default ConfigValidator<T> and(ConfigValidator<T> other) {
            return value -> {
                ValidationResult result = this.validate(value);
                if (!result.isValid()) return result;
                return other.validate(value);
            };
        }
    }
    
    public record ValidationResult(boolean isValid, String message) {
        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }
        
        public static ValidationResult invalid(String message) {
            return new ValidationResult(false, message);
        }
    }
    
    public static class ConfigValue<T> {
        private final String key;
        private final T value;
        private final List<ConfigValidator<T>> validators;
        
        private ConfigValue(String key, T value, List<ConfigValidator<T>> validators) {
            this.key = key;
            this.value = value;
            this.validators = validators;
        }
        
        public ValidationResult validate() {
            for (ConfigValidator<T> validator : validators) {
                ValidationResult result = validator.validate(value);
                if (!result.isValid()) return result;
            }
            return ValidationResult.valid();
        }
        
        public T getValue() { return value; }
        public String getKey() { return key; }
        
        public static <T> Builder<T> builder(String key) {
            return new Builder<>(key);
        }
        
        public static class Builder<T> {
            private final String key;
            private T value;
            private final List<ConfigValidator<T>> validators = new ArrayList<>();
            
            Builder(String key) { this.key = key; }
            
            public Builder<T> value(T value) {
                this.value = value;
                return this;
            }
            
            public Builder<T> validate(ConfigValidator<T> validator) {
                this.validators.add(validator);
                return this;
            }
            
            public Builder<T> required() {
                this.validators.add(v -> v != null 
                    ? ValidationResult.valid() 
                    : ValidationResult.invalid(key + " is required"));
                return this;
            }
            
            public ConfigValue<T> build() {
                return new ConfigValue<>(key, value, validators);
            }
        }
    }
    
    public static void main(String[] args) {
        // Create config values with validators
        ConfigValue<String> dbUrl = ConfigValue.<String>builder("database.url")
            .value("jdbc:postgresql://localhost:5432/mydb")
            .required()
            .validate(url -> url.startsWith("jdbc:") 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("Invalid JDBC URL"))
            .build();
        
        ConfigValue<Integer> poolSize = ConfigValue.<Integer>builder("database.pool.size")
            .value(10)
            .required()
            .validate(size -> size > 0 && size <= 100 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("Pool size must be 1-100"))
            .build();
        
        ConfigValue<String> appName = ConfigValue.<String>builder("app.name")
            .value("OrderService")
            .required()
            .validate(name -> name.length() >= 3 
                ? ValidationResult.valid() 
                : ValidationResult.invalid("App name must be at least 3 characters"))
            .build();
        
        // Validate all configs
        List<ConfigValue<?>> configs = List.of(dbUrl, poolSize, appName);
        
        System.out.println("Configuration validation:");
        configs.forEach(config -> {
            ValidationResult result = config.validate();
            String status = result.isValid() ? "VALID" : "INVALID: " + result.message();
            System.out.printf("  %s: %s%n", config.getKey(), status);
        });
    }
}
```

---

## 15. Performance

### 15.1 Lambda Performance Characteristics

| Metric | Lambda | Anonymous Class | Improvement |
|--------|--------|-----------------|-------------|
| **Creation Time** | ~100ns | ~200ns | 2x faster |
| **Memory per Instance** | ~16 bytes | ~40 bytes | 60% less |
| **Call Overhead** | ~5ns | ~10ns | 2x faster |
| **JIT Optimization** | Excellent | Good | Better inlining |

### 15.2 Performance Best Practices

```java
// PREFERRED: Reuse lambda instances
private static final Function<String, Integer> TO_LENGTH = String::length;

// LESS PREFERRED: Create new instance each time
public int getLength(String s) {
    return ((Function<String, Integer>) String::length).apply(s);
}

// PREFERRED: Use primitive streams
int sum = IntStream.range(0, 1000).sum();

// LESS PREFERRED: Boxing overhead
int sum = Stream.iterate(0, i -> i + 1).limit(1000).mapToInt(Integer::intValue).sum();
```

### 15.3 Benchmarking

```java
package academy.javaengineering.functional.lambda;

import java.util.function.IntBinaryOperator;

public class LambdaBenchmark {
    
    private static final IntBinaryOperator ADD = (a, b) -> a + b;
    private static final int ITERATIONS = 100_000_000;
    
    public static void main(String[] args) {
        // Warmup
        for (int i = 0; i < 10_000_000; i++) {
            ADD.applyAsInt(0, i);
        }
        
        // Benchmark: Reused lambda
        long start = System.nanoTime();
        int sum1 = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            sum1 = ADD.applyAsInt(sum1, i);
        }
        long reusedTime = System.nanoTime() - start;
        
        // Benchmark: New lambda each time
        start = System.nanoTime();
        int sum2 = 0;
        for (int i = 0; i < ITERATIONS; i++) {
            IntBinaryOperator newLambda = (a, b) -> a + b;
            sum2 = newLambda.applyAsInt(sum2, i);
        }
        long newTime = System.nanoTime() - start;
        
        System.out.printf("Reused lambda: %.2f ms%n", reusedTime / 1_000_000.0);
        System.out.printf("New lambda: %.2f ms%n", newTime / 1_000_000.0);
        System.out.printf("Speedup: %.2fx%n", (double) newTime / reusedTime);
    }
}
```

---

## 16. Best Practices

1. **Keep lambdas short**: If a lambda exceeds 3-4 lines, extract it to a named method
2. **Use method references**: When a lambda simply calls an existing method
3. **Prefer effectively final captures**: Don't capture mutable variables
4. **Document side effects**: If a lambda has side effects, document them
5. **Use var parameters (Java 11+)**: For clarity when types are obvious
6. **Test lambdas independently**: Extract complex lambdas to testable methods
7. **Reuse frequently used lambdas**: Store in static final fields
8. **Avoid complex nesting**: Deeply nested lambdas are hard to read

---

## 17. Common Mistakes

### Mistake 1: Mutable Variable Capture

```java
// WRONG: Variable must be effectively final
int counter = 0;
list.forEach(item -> counter++);  // Compilation error!

// CORRECT: Use AtomicInteger
AtomicInteger counter = new AtomicInteger(0);
list.forEach(item -> counter.incrementAndGet());
```

### Mistake 2: Overly Complex Lambdas

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

// CORRECT: Extract to named Predicate
Predicate<Item> isActiveRecentItem = this::isActiveRecent;
list.stream().filter(isActiveRecentItem).toList();
```

### Mistake 3: Incorrect Return in Block Lambda

```java
// WRONG: Missing return statement
Function<String, Integer> length = s -> {
    s.length();  // Compilation error!
};

// CORRECT: Explicit return
Function<String, Integer> length = s -> {
    return s.length();
};

// CORRECT: Expression body (implicit return)
Function<String, Integer> length = s -> s.length();
```

---

## 18. Pitfalls

1. **Scoping confusion**: Lambda variables shadow outer variables of the same name
2. **Type inference failures**: Complex generic contexts may require explicit types
3. **Debugging difficulty**: Stack traces with lambdas can be cryptic
4. **Performance with captures**: Captured variables add overhead
5. **Serialization issues**: Lambdas are not serializable by default

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

| Feature | Lambda | Anonymous Class | Method Reference |
|---------|--------|-----------------|------------------|
| **Syntax** | `(x) -> x * 2` | `new Func() { int f(x) { return x*2; } }` | `Math::abs` |
| **Type Inference** | Full | Explicit | Full |
| **Multiple Methods** | No (SAM only) | Yes | No (SAM only) |
| **Variable Capture** | Effectively final | Effectively final | N/A |
| **Performance** | Excellent | Good | Excellent |
| **Readability** | Good | Verbose | Excellent |
| **Use Case** | Simple implementations | Complex implementations | Delegation |

---

## 21. Decision Tree

```
Should you use a lambda expression?

┌─ Is the implementation simple (1-3 lines)?
│  ├─ YES → Use lambda expression
│  │        ├─ Does it just call an existing method?
│  │        │  ├─ YES → Use method reference
│  │        │  └─ NO → Use lambda
│  │        └─ Continue
│  └─ NO → Use named method or class
│
├─ Does the implementation need multiple methods?
│  ├─ YES → Use anonymous class or regular class
│  └─ NO → Use lambda
│
├─ Is the implementation reused frequently?
│  ├─ YES → Store in static final field
│  └─ NO → Inline lambda
│
└─ Does the lambda capture variables?
   ├─ YES → Ensure variables are effectively final
   └─ NO → Use lambda freely
```

---

## 22. Interview Questions

### Q1: What are the scoping rules for lambda expressions?

**Answer**: Lambda expressions have their own scope but can access:
- Effectively final local variables from enclosing scope
- Instance fields (read/write)
- Static fields (read/write)
- Cannot declare local variables that shadow outer variables

### Q2: Can a lambda throw checked exceptions?

**Answer**: Lambdas cannot throw checked exceptions unless:
1. The functional interface's abstract method declares the exception
2. The lambda is wrapped in a try-catch block

### Q3: How does variable capture work in lambdas?

**Answer**: When a lambda captures a variable:
1. The variable must be effectively final
2. For reference types: only the reference is copied (not the object)
3. For primitives: the value is boxed and copied
4. Captured variables are stored in the lambda object

### Q4: What is the difference between `var` and explicit types in lambda parameters?

**Answer**: `var` (Java 11+) allows shorter syntax while maintaining type inference:
```java
// Both are equivalent
Comparator<String> comp = (var a, var b) -> a.compareTo(b);
Comparator<String> comp2 = (a, b) -> a.compareTo(b);
```
`var` is useful when you want to add annotations to parameters.

### Q5: Can lambdas be static or instance members?

**Answer**: Lambdas can be:
1. Local variables (most common)
2. Fields (static or instance)
3. Method parameters
4. Method return values
5. Lambda body expressions

---

## 23. Exercises

### Exercise 1: Lambda Syntax
Convert these anonymous classes to lambda expressions:

```java
// 1. Runnable
Runnable r = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running");
    }
};

// 2. Comparator
Comparator<Integer> comp = new Comparator<Integer>() {
    @Override
    public int compare(Integer a, Integer b) {
        return b.compareTo(a);
    }
};

// 3. Function
Function<String, Integer> func = new Function<String, Integer>() {
    @Override
    public Integer apply(String s) {
        return s.length();
    }
};
```

### Exercise 2: Variable Capture
Fix the following code to work with lambdas:

```java
void processItems(List<Item> items) {
    int totalProcessed = 0;
    
    items.forEach(item -> {
        process(item);
        totalProcessed++;  // Error!
    });
    
    System.out.println("Total processed: " + totalProcessed);
}
```

### Exercise 3: Lambda Composition
Create a pipeline that:
1. Trims whitespace
2. Converts to lowercase
3. Removes special characters
4. Capitalizes first letter

Using function composition with `andThen`.

---

## 24. Assignments

### Assignment 1: Custom Functional Interface
Create a `@FunctionalInterface` called `Transformer<T, R>` that:
1. Has a single abstract method `R transform(T input)`
2. Has a default method `andThen` for composition
3. Has a static method `identity()`
4. Implement it with lambdas for string transformations

### Assignment 2: Event Handler System
Build an event handler system using lambdas:
1. Create `EventHandler<T>` functional interface
2. Implement event registration and dispatch
3. Support event filtering with predicates
4. Add event logging with consumers

### Assignment 3: Data Validator
Create a data validation framework:
1. Define `Validator<T>` functional interface
2. Implement validators for common data types
3. Support validator composition (and, or, not)
4. Use lambdas for custom validation rules

---

## 25. Mini Project

### Project: Lambda-Based Configuration Builder

Build a configuration builder that uses lambda expressions for validation and transformation:

**Requirements:**
1. Create a `ConfigBuilder` class with fluent API
2. Support typed configuration values
3. Implement validation using lambda predicates
4. Support transformation pipelines
5. Generate immutable configuration objects

**Starter Code:**
```java
package academy.javaengineering.functional.lambda.project;

import java.util.*;
import java.util.function.*;

public class LambdaConfigBuilder {
    
    private final Map<String, Object> properties = new HashMap<>();
    private final Map<String, List<Function<Object, Object>>> transformers = new HashMap<>();
    private final Map<String, List<Predicate<Object>>> validators = new HashMap<>();
    
    // TODO: Implement builder methods
    // TODO: Implement validation
    // TODO: Implement transformation
    // TODO: Build immutable config
}
```

---

## 26. Summary

Lambda expressions are a fundamental feature of modern Java that enable functional programming. Key takeaways:

1. **Syntax**: `(parameters) -> expression` or `(parameters) -> { statements; }`
2. **Type Inference**: Parameter types are inferred from context
3. **Variable Capture**: Only effectively final variables can be captured
4. **Performance**: Lambdas are more efficient than anonymous classes
5. **Use Cases**: Implementing functional interfaces, callbacks, stream operations

### Next Steps
- Topic 03: Functional Interfaces — Mastering built-in interfaces
- Topic 04: Method References — Simplifying lambda syntax

---

## 27. References

1. [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
2. [Java Language Specification: Lambda Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.27)
3. [Effective Java, 3rd Edition - Item 42](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Lambda Expressions](https://www.baeldung.com/java-lambda-expressions)
5. [Java Performance, 2nd Edition](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/)
