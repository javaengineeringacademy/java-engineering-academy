# Topic 10: Best Practices

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

This topic consolidates the best practices for functional programming in Java. Following these guidelines will help you write cleaner, more maintainable, and performant functional code.

### Key Principles

1. **Immutability**: Prefer immutable data and stateless functions
2. **Pure Functions**: Avoid side effects
3. **Composition**: Build complex logic from simple functions
4. **Declarative Style**: Describe what, not how
5. **Type Safety**: Leverage the type system

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Apply functional programming best practices in real code
2. Avoid common functional programming pitfalls
3. Write testable, maintainable functional code
4. Optimize functional code for performance
5. Document functional code effectively

---

## 3. Prerequisites

Before starting this topic, you should have completed:

- Topic 01-09: All functional programming topics

---

## 4. Why This Concept Exists

### The Problem Without Best Practices

Without best practices, functional code can become:
1. **Hard to read**: Complex lambda chains
2. **Bug-prone**: Side effects and mutable state
3. **Slow**: Inefficient use of streams and lambdas
4. **Hard to test**: Dependencies and side effects

### The Solution: Best Practices

Best practices ensure:
1. **Readability**: Clear, concise code
2. **Correctness**: Avoid common bugs
3. **Performance**: Efficient use of features
4. **Testability**: Easy to test

---

## 5. Problem Statement

### Real-World Scenario: Code Review

A team is reviewing functional code and finds:
1. **Overly complex lambdas**: Hard to understand
2. **Side effects**: Breaking stream independence
3. **Inefficient streams**: Unnecessary operations
4. **Missing documentation**: Unclear intent

### Requirements

1. Establish coding standards
2. Provide guidelines for common patterns
3. Document pitfalls to avoid
4. Ensure performance

---

## 6. Theory

### 6.1 Immutability

Prefer immutable data:

```java
// GOOD: Immutable
record Person(String name, int age) {}

// BAD: Mutable
class Person {
    String name;
    int age;
}
```

### 6.2 Pure Functions

Avoid side effects:

```java
// GOOD: Pure function
int add(int a, int b) {
    return a + b;
}

// BAD: Side effect
int total = 0;
void accumulate(int value) {
    total += value;
}
```

### 6.3 Declarative Style

Describe what, not how:

```java
// GOOD: Declarative
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .toList();

// BAD: Imperative
List<String> result = new ArrayList<>();
for (String s : list) {
    if (s.length() > 3) {
        result.add(s.toUpperCase());
    }
}
```

### 6.4 Composition

Build from small functions:

```java
// GOOD: Composed
Function<String, String> process = trim
    .andThen(toLower)
    .andThen(removeSpecial);

// BAD: Monolithic
Function<String, String> process = s -> {
    String trimmed = s.trim();
    String lower = trimmed.toLowerCase();
    String clean = lower.replaceAll("[^a-z0-9\\s]", "");
    return clean;
};
```

---

## 7. Internal Working

### 7.1 Lambda Scoping

Lambdas capture effectively final variables:

```java
void process(List<String> items) {
    String prefix = "ITEM-";  // Effectively final
    
    items.forEach(item -> {
        System.out.println(prefix + item);  // OK
        // prefix = "NEW-";  // Compilation error!
    });
}
```

### 7.2 Stream Independence

Stream operations should be independent:

```java
// BAD: Side effect
int[] counter = {0};
list.stream()
    .filter(item -> {
        counter[0]++;  // Side effect!
        return true;
    })
    .toList();

// GOOD: Use peek for side effects
list.stream()
    .filter(item -> true)
    .peek(item -> counter[0]++)  // Still bad, but better
    .toList();
```

### 7.3 Lazy Evaluation

Leverage lazy evaluation:

```java
// GOOD: Filter early
list.stream()
    .filter(expensivePredicate)  // Runs first
    .map(cheapMapper)  // Only runs on filtered elements
    .toList();

// BAD: Map first
list.stream()
    .map(cheapMapper)  // Runs on all elements
    .filter(expensivePredicate)  // Then filters
    .toList();
```

---

## 8. JVM Perspective

### 8.1 Lambda Optimization

The JVM optimizes lambdas:

1. **Inlining**: Small lambdas are inlined
2. **Caching**: Lambda objects are cached
3. **Escape analysis**: Lambdas may be stack-allocated

### 8.2 Stream Optimization

The JVM optimizes streams:

1. **Loop fusion**: Multiple operations are combined
2. **Short-circuiting**: Early termination
3. **Parallelization**: Automatic thread management

---

## 9. Memory Representation

### 9.1 Lambda Memory

Lambdas have minimal memory overhead:

```
Lambda Object:
┌─────────────────────────────────────┐
│  Header                             │
├─────────────────────────────────────┤
│  Captured variables (references)    │
│  Method handle                      │
└─────────────────────────────────────┘
```

### 9.2 Stream Memory

Streams create temporary objects:

```
Stream Pipeline:
┌─────────────────────────────────────┐
│  Source reference                    │
│  Pipeline stages (linked list)      │
│  Operation functions                │
└─────────────────────────────────────┘
```

---

## 10. Syntax

### 10.1 Lambda Best Practices

```java
// GOOD: Short lambda
list.stream()
    .filter(s -> s.length() > 3)
    .toList();

// GOOD: Method reference
list.stream()
    .map(String::toUpperCase)
    .toList();

// BAD: Long lambda
list.stream()
    .filter(s -> {
        if (s == null) return false;
        if (s.isEmpty()) return false;
        if (s.length() < 3) return false;
        return true;
    })
    .toList();
```

### 10.2 Stream Best Practices

```java
// GOOD: Filter before map
list.stream()
    .filter(item -> item.getPrice() > 100)
    .map(Item::getName)
    .toList();

// GOOD: Use toList()
list.stream()
    .filter(...)
    .toList();

// BAD: Use collect(Collectors.toList())
list.stream()
    .filter(...)
    .collect(Collectors.toList());
```

---

## 11. Easy Example

### Example 1: Lambda Best Practices

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdaBestPractices {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // GOOD: Short lambda
        List<String> longNames = names.stream()
            .filter(s -> s.length() > 3)
            .toList();
        System.out.println("Long names: " + longNames);
        
        // GOOD: Method reference
        List<String> upperNames = names.stream()
            .map(String::toUpperCase)
            .toList();
        System.out.println("Uppercase: " + upperNames);
        
        // GOOD: Named predicate
        Predicate<String> isLong = s -> s.length() > 3;
        List<String> longNamesNamed = names.stream()
            .filter(isLong)
            .toList();
        System.out.println("Long names (named): " + longNamesNamed);
    }
}
```

### Example 2: Stream Best Practices

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Arrays;
import java.util.List;

public class StreamBestPractices {
    
    record Product(String name, double price, int stock) {}
    
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Laptop", 999.99, 10),
            new Product("Phone", 699.99, 25),
            new Product("Tablet", 299.99, 5),
            new Product("Headphones", 199.99, 30)
        );
        
        // GOOD: Filter before map
        List<String> affordableProducts = products.stream()
            .filter(p -> p.price() < 500)
            .map(Product::name)
            .toList();
        System.out.println("Affordable: " + affordableProducts);
        
        // GOOD: Use toList()
        List<String> allNames = products.stream()
            .map(Product::name)
            .toList();
        System.out.println("All names: " + allNames);
        
        // GOOD: Method references
        List<Product> inStock = products.stream()
            .filter(p -> p.stock() > 0)
            .toList();
        System.out.println("In stock: " + inStock.size());
    }
}
```

---

## 12. Medium Example

### Example 1: Complex Lambda with Named Functions

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ComplexLambdaBestPractices {
    
    record User(String name, int age, boolean active, String email) {}
    
    public static void main(String[] args) {
        List<User> users = Arrays.asList(
            new User("Alice", 25, true, "alice@example.com"),
            new User("Bob", 17, true, "bob@example.com"),
            new User("Charlie", 30, false, "charlie@example.com"),
            new User("Diana", 22, true, "diana@example.com")
        );
        
        // GOOD: Named predicates
        Predicate<User> isActive = User::active;
        Predicate<User> isAdult = user -> user.age() >= 18;
        Predicate<User> hasValidEmail = user -> user.email() != null && user.email().contains("@");
        
        Predicate<User> isEligible = isActive.and(isAdult).and(hasValidEmail);
        
        // GOOD: Named transformation
        Function<User, String> toSummary = user ->
            String.format("%s (%d) - %s", user.name(), user.age(), user.email());
        
        List<String> eligibleUsers = users.stream()
            .filter(isEligible)
            .map(toSummary)
            .toList();
        
        System.out.println("Eligible users:");
        eligibleUsers.forEach(u -> System.out.println("  " + u));
    }
}
```

### Example 2: Pipeline with Debugging

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class PipelineDebugging {
    
    public static void main(String[] args) {
        List<String> inputs = Arrays.asList("  Hello, World!  ", "  Java 8  ", "  Lambda  ");
        
        // GOOD: Named functions
        UnaryOperator<String> trim = String::trim;
        UnaryOperator<String> toLower = String::toLowerCase;
        UnaryOperator<String> removeSpecial = s -> s.replaceAll("[^a-z0-9\\s]", "");
        UnaryOperator<String> normalizeSpaces = s -> s.replaceAll("\\s+", "_");
        
        Function<String, String> pipeline = trim
            .andThen(toLower)
            .andThen(removeSpecial)
            .andThen(normalizeSpaces);
        
        List<String> processed = inputs.stream()
            .map(pipeline)
            .toList();
        
        System.out.println("Processed: " + processed);
        
        // GOOD: Debug with peek
        List<String> debugResult = inputs.stream()
            .peek(s -> System.out.println("Input: [" + s + "]"))
            .map(trim)
            .peek(s -> System.out.println("After trim: [" + s + "]"))
            .map(toLower)
            .peek(s -> System.out.println("After lower: [" + s + "]"))
            .toList();
    }
}
```

---

## 13. Hard Example

### Example 1: Functional Design Patterns

```java
package academy.javaengineering.functional.bestpractices;

import java.util.function.*;

public class FunctionalDesignPatterns {
    
    // Strategy Pattern
    @FunctionalInterface
    public interface Strategy<T, R> {
        R execute(T input);
    }
    
    public static class StrategyContext<T, R> {
        private final Strategy<T, R> strategy;
        
        public StrategyContext(Strategy<T, R> strategy) {
            this.strategy = strategy;
        }
        
        public R execute(T input) {
            return strategy.execute(input);
        }
    }
    
    // Template Method Pattern
    public static abstract class Template<T, R> {
        public final R process(T input) {
            T validated = validate(input);
            R result = execute(validated);
            return postProcess(result);
        }
        
        protected abstract T validate(T input);
        protected abstract R execute(T input);
        protected abstract R postProcess(R result);
    }
    
    // Decorator Pattern
    public static <T, R> Function<T, R> decorate(
            Function<T, R> original,
            Function<Function<T, R>, Function<T, R>> decorator) {
        return decorator.apply(original);
    }
    
    public static void main(String[] args) {
        // Strategy Pattern
        Strategy<String, Integer> lengthStrategy = String::length;
        StrategyContext<String, Integer> context = new StrategyContext<>(lengthStrategy);
        System.out.println("Length: " + context.execute("Hello"));
        
        // Decorator Pattern
        Function<String, String> trim = String::trim;
        Function<String, String> decorated = decorate(trim, 
            next -> s -> next.apply(s).toUpperCase());
        
        System.out.println("Decorated: " + decorated.apply("  hello  "));
    }
}
```

---

## 14. Enterprise Example

### Example 1: Functional Error Handling

```java
package academy.javaengineering.functional.bestpractices;

import java.util.Optional;
import java.util.function.Function;

public class FunctionalErrorHandling {
    
    @FunctionalInterface
    public interface SafeFunction<T, R> {
        R apply(T input) throws Exception;
        
        default Function<T, Optional<R>> safe() {
            return input -> {
                try {
                    return Optional.ofNullable(apply(input));
                } catch (Exception e) {
                    return Optional.empty();
                }
            };
        }
    }
    
    public static <T, R> Function<T, R> withFallback(
            SafeFunction<T, R> primary,
            Function<T, R> fallback) {
        return input -> {
            try {
                return primary.apply(input);
            } catch (Exception e) {
                return fallback.apply(input);
            }
        };
    }
    
    public static void main(String[] args) {
        SafeFunction<String, Integer> safeParse = Integer::parseInt;
        
        Function<String, Optional<Integer>> safe = safeParse.safe();
        
        System.out.println("Parse '123': " + safe.apply("123"));
        System.out.println("Parse 'abc': " + safe.apply("abc"));
        
        Function<String, Integer> withDefault = withFallback(
            Integer::parseInt,
            s -> -1
        );
        
        System.out.println("With default '123': " + withDefault.apply("123"));
        System.out.println("With default 'abc': " + withDefault.apply("abc"));
    }
}
```

---

## 15. Performance

### 15.1 Performance Best Practices

| Practice | Impact | Notes |
|----------|--------|-------|
| Filter before map | High | Reduces dataset size early |
| Use primitive streams | Medium | Avoids boxing overhead |
| Reuse lambdas | Medium | Reduces object creation |
| Avoid parallel for small data | High | Overhead exceeds benefit |
| Use toList() | Low | More efficient than collect |

### 15.2 Performance Tips

```java
// GOOD: Filter early
list.stream()
    .filter(expensivePredicate)
    .map(cheapMapper)
    .toList();

// GOOD: Primitive streams
IntStream.range(0, 1000000)
    .filter(n -> n % 2 == 0)
    .sum();

// GOOD: Reuse lambda
private static final Function<String, Integer> TO_LENGTH = String::length;

// BAD: Create new lambda each time
list.stream()
    .map(s -> s.length())  // Creates new lambda
    .toList();
```

---

## 16. Best Practices

1. **Keep lambdas short**: Extract complex logic to named methods
2. **Use method references**: More readable
3. **Filter before map**: Reduce dataset size early
4. **Use toList()**: More efficient than collect(Collectors.toList())
5. **Avoid side effects**: Don't modify external state
6. **Cache composed functions**: Store in static final fields
7. **Use primitive streams**: Avoid boxing overhead
8. **Document functional code**: Explain intent and behavior

---

## 17. Common Mistakes

### Mistake 1: Overly Complex Lambdas

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

// CORRECT: Extract to named predicate
Predicate<Item> isActiveRecentItem = this::isActiveRecent;
list.stream().filter(isActiveRecentItem).toList();
```

### Mistake 2: Side Effects in Streams

```java
// WRONG: Side effects
List<String> result = new ArrayList<>();
list.stream()
    .map(item -> {
        result.add(item.getName());  // Side effect!
        return item;
    })
    .toList();

// CORRECT: Use collect
List<String> result = list.stream()
    .map(Item::getName)
    .toList();
```

---

## 18. Pitfalls

1. **Null in lambdas**: Avoid returning null from lambdas
2. **Mutable captures**: Don't capture mutable variables
3. **Infinite streams**: Always use short-circuit operations
4. **Performance**: Parallel streams have overhead

---

## 19. Debugging Tips

### 1. Use Named Methods

```java
// Instead of complex lambda
list.stream()
    .filter(item -> item.getStatus() == Status.ACTIVE && item.getPriority() > 5)
    .toList();

// Extract to named method
Predicate<Item> isActiveHighPriority = this::isActiveHighPriority;
list.stream().filter(isActiveHighPriority).toList();
```

### 2. Use peek() for Debugging

```java
list.stream()
    .filter(predicate)
    .peek(item -> System.out.println("After filter: " + item))
    .map(transformer)
    .peek(item -> System.out.println("After map: " + item))
    .toList();
```

---

## 20. Comparison Table

| Practice | Good | Bad |
|----------|------|-----|
| Lambda length | Short, focused | Long, complex |
| Method calls | Method references | Lambda wrapping method |
| Stream operations | Filter before map | Map before filter |
| Collection | toList() | collect(Collectors.toList()) |
| State | Stateless | Stateful with side effects |

---

## 21. Decision Tree

```
Should you use this pattern?

┌─ Is the lambda simple (1-3 lines)?
│  ├─ YES → Use inline lambda
│  └─ NO → Extract to named method
│
├─ Does the lambda just call an existing method?
│  ├─ YES → Use method reference
│  └─ NO → Use lambda
│
├─ Are you filtering and mapping?
│  ├─ YES → Filter first
│  └─ NO → Consider order
│
├─ Do you need a List result?
│  ├─ YES → Use toList()
│  └─ NO → Use appropriate collector
│
└─ Is the dataset large and CPU-bound?
   ├─ YES → Consider parallel stream
   └─ NO → Use sequential stream
```

---

## 22. Interview Questions

### Q1: What are the key principles of functional programming in Java?

**Answer**:
1. **Immutability**: Prefer immutable data
2. **Pure Functions**: Avoid side effects
3. **Composition**: Build from small functions
4. **Declarative Style**: Describe what, not how

### Q2: How do you write testable functional code?

**Answer**:
1. Use pure functions without side effects
2. Extract complex lambdas to named methods
3. Use dependency injection for external dependencies
4. Test individual functions independently

### Q3: What are common functional programming pitfalls?

**Answer**:
1. Overly complex lambdas
2. Side effects in streams
3. Mutable variable capture
4. Ignoring performance implications

### Q4: How do you optimize functional code?

**Answer**:
1. Filter before map
2. Use primitive streams
3. Reuse lambda instances
4. Avoid parallel for small datasets

### Q5: When should you use parallel streams?

**Answer**:
1. Large datasets (>10,000 elements)
2. CPU-bound operations
3. Independent element processing
4. Order doesn't matter

---

## 23. Exercises

### Exercise 1: Code Review
Review and improve this code:
```java
list.stream().filter(x -> x != null && x.getName() != null && x.getName().length() > 3).map(x -> x.getName().toUpperCase()).collect(Collectors.toList());
```

### Exercise 2: Lambda Refactoring
Refactor this lambda to be more readable:
```java
list.stream().filter(item -> { if (item == null) return false; if (item.getStatus() == null) return false; return item.getStatus() == Status.ACTIVE; }).toList();
```

### Exercise 3: Performance Optimization
Optimize this stream pipeline:
```java
list.parallelStream().filter(x -> x.getPrice() > 100).map(x -> x.getName().toUpperCase()).collect(Collectors.toList());
```

---

## 24. Assignments

### Assignment 1: Code Standards
Create a functional programming style guide:
1. Lambda formatting rules
2. Stream operation guidelines
3. Naming conventions

### Assignment 2: Refactoring
Refactor imperative code to functional:
1. Convert loops to streams
2. Extract complex lambdas
3. Apply composition

### Assignment 3: Performance Audit
Audit and optimize functional code:
1. Identify bottlenecks
2. Apply best practices
3. Measure improvements

---

## 25. Mini Project

### Project: Functional Programming Toolkit

Build a toolkit of functional programming utilities:

**Requirements:**
1. Utility class for common operations
2. Composition helpers
3. Error handling utilities
4. Performance benchmarks

**Starter Code:**
```java
package academy.javaengineering.functional.bestpractices.project;

import java.util.function.*;

public class FunctionalToolkit {
    
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
    
    public static <T> Function<T, T> peek(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }
    
    // TODO: Implement more utilities
}
```

---

## 26. Summary

Best practices ensure functional code is clean, maintainable, and performant. Key takeaways:

1. **Immutability**: Prefer immutable data
2. **Pure Functions**: Avoid side effects
3. **Composition**: Build from small functions
4. **Declarative Style**: Describe what, not how
5. **Performance**: Filter early, use primitives

### Next Steps
- Topic 11: Mini Project — Apply all concepts
- Continue to advanced functional patterns

---

## 27. References

1. [Effective Java, 3rd Edition - Items 42-44](https://www.oreilly.com/library/view/effective-java/9780134686097/)
2. [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
3. [Baeldung: Java Functional Programming](https://www.baeldung.com/java-functional-programming)
4. [Java Performance, 2nd Edition](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/)
