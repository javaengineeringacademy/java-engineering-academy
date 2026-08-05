# Topic 09: Function Composition

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

Function composition is the process of combining two or more functions to produce a new function. In Java, function composition is achieved through default methods on functional interfaces like `Function`, `Predicate`, and `UnaryOperator`.

Composition enables building complex transformations from simple, reusable functions. This promotes:
- **Modularity**: Small, focused functions
- **Reusability**: Compose functions in different ways
- **Readability**: Declarative function pipelines
- **Testability**: Test individual functions independently

### Composition Methods

| Method | Description | Order |
|--------|-------------|-------|
| `andThen(after)` | Apply this function first, then `after` | this → after |
| `compose(before)` | Apply `before` first, then this function | before → this |

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Understand function composition principles
2. Apply `andThen()` and `compose()` methods
3. Build complex transformations from simple functions
4. Use predicate composition (and, or, negate)
5. Create function pipelines
6. Apply composition in enterprise patterns

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Lambda Expressions**: Basic syntax (Topic 02)
- **Functional Interfaces**: Function, Predicate, Consumer (Topic 03)
- **Method References**: Shorthand for lambdas (Topic 04)

---

## 4. Why This Concept Exists

### The Problem Without Composition

Without function composition, developers must:
1. Write monolithic functions that do everything
2. Duplicate transformation logic
3. Create complex, hard-to-test methods

```java
// Monolithic function
Function<Order, String> processOrder = order -> {
    String status = order.status();
    String customer = order.customer();
    double amount = order.amount();
    // ... complex logic
    return result;
};
```

### The Composition Solution

```java
// Composable functions
Function<Order, String> getStatus = Order::status;
Function<Order, String> getCustomer = Order::customer;
Function<Order, Double> getAmount = Order::amount;

Function<Order, String> processOrder = order ->
    getStatus.apply(order) + " - " + getCustomer.apply(order);
```

---

## 5. Problem Statement

### Real-World Scenario: Data Transformation Pipeline

A data processing system needs to:
- **Transform** data through multiple steps
- **Reuse** transformation logic
- **Build** complex pipelines from simple functions
- **Test** each transformation independently

### Requirements

1. Compose functions declaratively
2. Support both andThen and compose
3. Enable predicate composition
4. Provide type safety
5. Support parallel composition

---

## 6. Theory

### 6.1 Function Composition with andThen

`andThen` applies the current function first, then the argument:

```java
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
// doubleThenAdd.apply(5) → 5 * 2 + 10 = 20
```

### 6.2 Function Composition with compose

`compose` applies the argument first, then the current function:

```java
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
// addThenDouble.apply(5) → (5 + 10) * 2 = 30
```

### 6.3 Predicate Composition

Predicates support `and`, `or`, and `negate`:

```java
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEven = n -> n % 2 == 0;

Predicate<Integer> isPositiveEven = isPositive.and(isEven);
Predicate<Integer> isPositiveOrEven = isPositive.or(isEven);
Predicate<Integer> isNotPositive = isPositive.negate();
```

### 6.4 Consumer Composition

Consumers support `andThen`:

```java
Consumer<String> print = System.out::println;
Consumer<String> log = s -> System.out.println("LOG: " + s);

Consumer<String> printAndLog = print.andThen(log);
```

### 6.5 UnaryOperator Composition

UnaryOperators support `andThen` and `compose`:

```java
UnaryOperator<String> trim = String::trim;
UnaryOperator<String> toLower = String::toLowerCase;

UnaryOperator<String> process = trim.andThen(toLower);
```

---

## 7. Internal Working

### 7.1 Composition Implementation

`andThen` creates a new function that chains operations:

```java
default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return t -> after.apply(this.apply(t));
}
```

### 7.2 Composition Chain

Composition creates a linked list of functions:

```
Function1.andThen(Function2).andThen(Function3)
↓
Function1 → Function2 → Function3
```

### 7.3 Type Safety

Composition maintains type safety through generics:

```java
Function<String, Integer> toLength = String::length;
Function<Integer, String> toString = Object::toString;

Function<String, String> pipeline = toLength.andThen(toString);
// Type: String → Integer → String
```

---

## 8. JVM Perspective

### 8.1 Composition Object Layout

Each composition creates a new function object:

```
Function.andThen(other):
┌─────────────────────────────────────┐
│  Header                             │
├─────────────────────────────────────┤
│  this function reference            │
│  after function reference           │
└─────────────────────────────────────┘
```

### 8.2 Method Dispatch

Composition uses virtual method dispatch:

```
pipeline.apply(input)
→ this.apply(input)  // First function
→ after.apply(result)  // Second function
```

### 8.3 JIT Optimization

The JIT compiler can optimize composition:

1. **Inlining**: Small functions are inlined
2. **Loop fusion**: Multiple compositions are combined
3. **Escape analysis**: Composition objects may be stack-allocated

---

## 9. Memory Representation

### 9.1 Composition Chain Memory

```
Function1.andThen(Function2).andThen(Function3):

Memory:
┌─────────────┐
│  Composition│
├─────────────┤
│  Function1  │
│  Function2  │
└─────────────┘
      ↓
┌─────────────┐
│  Composition│
├─────────────┤
│  Previous   │
│  Function3  │
└─────────────┘
```

### 9.2 Memory Usage

- **Single function**: ~16 bytes
- **Composition**: ~16 bytes per composition
- **Chain**: Proportional to chain length

---

## 10. Syntax

### 10.1 Function Composition

```java
// andThen: this → after
Function<T, R> composed = function1.andThen(function2);

// compose: before → this
Function<T, R> composed = function1.compose(function2);

// Chain
Function<T, V> pipeline = function1
    .andThen(function2)
    .andThen(function3);
```

### 10.2 Predicate Composition

```java
// and
Predicate<T> combined = predicate1.and(predicate2);

// or
Predicate<T> combined = predicate1.or(predicate2);

// negate
Predicate<T> negated = predicate.negate();
```

### 10.3 Consumer Composition

```java
// andThen
Consumer<T> combined = consumer1.andThen(consumer2);
```

---

## 11. Easy Example

### Example 1: Basic Function Composition

```java
package academy.javaengineering.functional.composition;

import java.util.function.Function;

public class BasicComposition {
    public static void main(String[] args) {
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> addTen = x -> x + 10;
        
        // andThen: double first, then add 10
        Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);
        System.out.println("5 double then add 10: " + doubleThenAdd.apply(5));
        
        // compose: add 10 first, then double
        Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);
        System.out.println("5 add 10 then double: " + addThenDouble.apply(5));
        
        // Chain
        Function<Integer, Integer> pipeline = doubleIt
            .andThen(addTen)
            .andThen(x -> x * x);
        System.out.println("5 pipeline: " + pipeline.apply(5));
    }
}
```

### Example 2: Predicate Composition

```java
package academy.javaengineering.functional.composition;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateComposition {
    public static void main(String[] args) {
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isLessThan100 = n -> n < 100;
        
        // and
        Predicate<Integer> isPositiveEven = isPositive.and(isEven);
        System.out.println("5 is positive even: " + isPositiveEven.test(5));
        System.out.println("6 is positive even: " + isPositiveEven.test(6));
        
        // or
        Predicate<Integer> isSmallOrEven = isLessThan100.or(isEven);
        System.out.println("-5 is small or even: " + isSmallOrEven.test(-5));
        
        // negate
        Predicate<Integer> isNotPositive = isPositive.negate();
        System.out.println("-1 is not positive: " + isNotPositive.test(-1));
        
        List<Integer> numbers = Arrays.asList(-5, 0, 3, 6, 100);
        System.out.println("Positive and even: " + numbers.stream()
            .filter(isPositiveEven)
            .toList());
    }
}
```

---

## 12. Medium Example

### Example 1: Text Processing Pipeline

```java
package academy.javaengineering.functional.composition;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TextPipeline {
    
    public static Function<String, String> buildTextPipeline() {
        UnaryOperator<String> trim = String::trim;
        UnaryOperator<String> toLower = String::toLowerCase;
        UnaryOperator<String> removeSpecial = s -> s.replaceAll("[^a-z0-9\\s]", "");
        UnaryOperator<String> collapseSpaces = s -> s.replaceAll("\\s+", " ");
        UnaryOperator<String> addPrefix = s -> "processed: " + s;
        
        return Function.<String>identity()
            .andThen(trim)
            .andThen(toLower)
            .andThen(removeSpecial)
            .andThen(collapseSpaces)
            .andThen(addPrefix);
    }
    
    public static void main(String[] args) {
        Function<String, String> pipeline = buildTextPipeline();
        
        System.out.println(pipeline.apply("  Hello, World!  "));
        System.out.println(pipeline.apply("  Java Programming 101  "));
        System.out.println(pipeline.apply("  Lambda Expressions!!!  "));
    }
}
```

### Example 2: Function Composition with Types

```java
package academy.javaengineering.functional.composition;

import java.util.function.Function;

public class TypedComposition {
    
    record Person(String name, int age) {}
    record PersonSummary(String displayName, String ageGroup) {}
    
    public static void main(String[] args) {
        Function<Person, String> getDisplayName = p -> 
            p.name().substring(0, 1).toUpperCase() + p.name().substring(1);
        
        Function<Integer, String> getAgeGroup = age -> {
            if (age < 18) return "Minor";
            if (age < 65) return "Adult";
            return "Senior";
        };
        
        Function<Person, PersonSummary> toSummary = person ->
            new PersonSummary(
                getDisplayName.apply(person),
                getAgeGroup.apply(person.age())
            );
        
        Person alice = new Person("alice", 25);
        PersonSummary summary = toSummary.apply(alice);
        System.out.println("Summary: " + summary);
    }
}
```

---

## 13. Hard Example

### Example 1: Generic Composition Framework

```java
package academy.javaengineering.functional.composition;

import java.util.function.*;

public class CompositionFramework {
    
    @FunctionalInterface
    public interface Composable<T, R> {
        R apply(T t);
        
        default <V> Composable<T, V> andThen(Composable<R, V> after) {
            return t -> after.apply(this.apply(t));
        }
        
        default <V> Composable<V, R> compose(Composable<V, T> before) {
            return v -> this.apply(before.apply(v));
        }
        
        static <T> Composable<T, T> identity() {
            return t -> t;
        }
    }
    
    public static class PipelineBuilder<I, O> {
        private final Composable<I, O> composable;
        
        private PipelineBuilder(Composable<I, O> composable) {
            this.composable = composable;
        }
        
        public static <T> PipelineBuilder<T, T> create() {
            return new PipelineBuilder<>(Composable.identity());
        }
        
        public <R> PipelineBuilder<I, R> addStep(Composable<O, R> step) {
            return new PipelineBuilder<>(composable.andThen(step));
        }
        
        public O apply(I input) {
            return composable.apply(input);
        }
    }
    
    public static void main(String[] args) {
        // Build a pipeline
        PipelineBuilder<String, String> pipeline = PipelineBuilder.<String>create()
            .addStep(String::trim)
            .addStep(String::toLowerCase)
            .addStep(s -> s.replaceAll("[^a-z0-9\\s]", ""))
            .addStep(s -> s.replaceAll("\\s+", "_"));
        
        System.out.println(pipeline.apply("  Hello, World!  "));
        
        // Build a transformation pipeline
        PipelineBuilder<Integer, String> numberPipeline = PipelineBuilder.<Integer>create()
            .addStep(n -> n * 2)
            .addStep(n -> n + 10)
            .addStep(n -> "Result: " + n);
        
        System.out.println(numberPipeline.apply(5));
    }
}
```

---

## 14. Enterprise Example

### Example 1: Data Transformation Pipeline

```java
package academy.javaengineering.functional.composition;

import java.util.function.Function;

public class DataTransformationPipeline {
    
    record RawData(String input, String format, boolean valid) {}
    record CleanedData(String data, String format) {}
    record ProcessedData(String data, String format, int length) {}
    
    public static void main(String[] args) {
        // Define transformation functions
        Function<RawData, CleanedData> clean = raw ->
            new CleanedData(
                raw.input().trim().toLowerCase(),
                raw.format()
            );
        
        Function<CleanedData, ProcessedData> process = cleaned ->
            new ProcessedData(
                cleaned.data(),
                cleaned.format(),
                cleaned.data().length()
            );
        
        // Compose pipeline
        Function<RawData, ProcessedData> pipeline = clean.andThen(process);
        
        // Process data
        RawData raw = new RawData("  Hello World  ", "text", true);
        ProcessedData result = pipeline.apply(raw);
        
        System.out.println("Result: " + result);
    }
}
```

---

## 15. Performance

### 15.1 Composition Performance

| Composition | Overhead | Notes |
|-------------|----------|-------|
| Single function | ~5ns | Baseline |
| andThen | ~10ns | One extra dispatch |
| compose | ~10ns | One extra dispatch |
| Chain of N | ~5n ns | Linear |

### 15.2 Performance Tips

1. **Cache composed functions**: Store in static final fields
2. **Avoid deep chains**: Prefer 3-5 functions
3. **Use method references**: Better JIT optimization
4. **Consider parallel composition**: For independent transformations

---

## 16. Best Practices

1. **Cache composed functions**: Store in static final fields
2. **Keep chains short**: 3-5 functions maximum
3. **Use method references**: More readable and optimizable
4. **Document composition**: Explain the pipeline
5. **Test composed functions**: Verify correctness

---

## 17. Common Mistakes

### Mistake 1: Confusing andThen with compose

```java
// WRONG: Confusing order
Function<Integer, Integer> doubleIt = x -> x * 2;
Function<Integer, Integer> addTen = x -> x + 10;

// This is: add 10, then double
Function<Integer, Integer> result = doubleIt.compose(addTen);

// CORRECT: Be explicit about order
Function<Integer, Integer> addThenDouble = doubleIt.compose(addTen);  // add first
Function<Integer, Integer> doubleThenAdd = doubleIt.andThen(addTen);  // double first
```

### Mistake 2: Deep Composition Chains

```java
// WRONG: Hard to read
Function<String, String> pipeline = Function.<String>identity()
    .andThen(s -> s.trim())
    .andThen(s -> s.toLowerCase())
    .andThen(s -> s.replaceAll("[^a-z0-9\\s]", ""))
    .andThen(s -> s.replaceAll("\\s+", "_"))
    .andThen(s -> s.substring(0, Math.min(s.length(), 50)))
    .andThen(s -> "prefix_" + s);

// CORRECT: Extract to named functions
UnaryOperator<String> trim = String::trim;
UnaryOperator<String> toLower = String::toLowerCase;
// ... build pipeline with named functions
```

---

## 18. Pitfalls

1. **Type mismatches**: Composition must match types
2. **Null handling**: Functions may return null
3. **Side effects**: Avoid in composed functions
4. **Performance**: Each composition adds overhead

---

## 19. Debugging Tips

### 1. Extract Complex Functions

```java
// Instead of complex composition
Function<String, String> pipeline = s -> s.trim().toLowerCase().replaceAll("\\s+", "_");

// Extract to named functions
UnaryOperator<String> trim = String::trim;
UnaryOperator<String> toLower = String::toLowerCase;
UnaryOperator<String> normalizeSpaces = s -> s.replaceAll("\\s+", "_");
Function<String, String> pipeline = trim.andThen(toLower).andThen(normalizeSpaces);
```

### 2. Use peek() for Debugging

```java
Function<String, String> debug = s -> {
    System.out.println("Input: " + s);
    return s;
};

Function<String, String> pipeline = debug
    .andThen(String::trim)
    .andThen(debug)
    .andThen(String::toLowerCase)
    .andThen(debug);
```

---

## 20. Comparison Table

| Composition | Order | Use Case |
|-------------|-------|----------|
| `andThen` | this → after | Sequential processing |
| `compose` | before → this | Pre-processing |
| `Predicate.and` | Both must be true | Combined conditions |
| `Predicate.or` | Either must be true | Alternative conditions |
| `Consumer.andThen` | First, then second | Side effects |

---

## 21. Decision Tree

```
Which composition method should you use?

┌─ Do you need to apply this function first?
│  ├─ YES → andThen
│  └─ NO → Continue
│
├─ Do you need to apply the other function first?
│  ├─ YES → compose
│  └─ NO → Continue
│
├─ Are you combining predicates?
│  ├─ YES → and/or/negate
│  └─ NO → Continue
│
├─ Are you chaining consumers?
│  ├─ YES → andThen
│  └─ NO → Continue
│
└─ Are you building a pipeline?
   └─ YES → andThen (most common)
```

---

## 22. Interview Questions

### Q1: What is the difference between andThen and compose?

**Answer**: `andThen` applies the current function first, then the argument. `compose` applies the argument first, then the current function. `andThen` is more commonly used for building pipelines.

### Q2: How do you compose predicates?

**Answer**: Use `and()`, `or()`, and `negate()` methods:
```java
Predicate<Integer> isPositiveEven = isPositive.and(isEven);
Predicate<Integer> isSmallOrNegative = isSmall.or(isNegative);
Predicate<Integer> isNotPositive = isPositive.negate();
```

### Q3: Can you compose functions with different types?

**Answer**: Yes, as long as the output type of one function matches the input type of the next. Use generics for type safety.

### Q4: What are the performance implications of composition?

**Answer**: Each composition adds a method dispatch (~5ns). For deep chains, consider caching the composed function in a static final field.

### Q5: How do you debug composed functions?

**Answer**: Extract complex functions to named methods. Use `peek()` or debug functions to inspect intermediate values.

---

## 23. Exercises

### Exercise 1: Basic Composition
Compose functions to:
1. Double a number, then add 10
2. Trim a string, then convert to uppercase
3. Filter positive numbers, then square them

### Exercise 2: Predicate Composition
Create predicates to:
1. Check if a number is positive AND even
2. Check if a string is long OR contains a specific character
3. Check if a person is NOT a minor

### Exercise 3: Function Pipeline
Build a pipeline that:
1. Trims whitespace
2. Converts to lowercase
3. Removes special characters
4. Adds a prefix

---

## 24. Assignments

### Assignment 1: Composition Library
Create a composition utility class:
1. `compose(Function<T,R>... functions)` - compose multiple functions
2. `andThen(Predicate<T>... predicates)` - combine predicates with AND
3. `or(Predicate<T>... predicates)` - combine predicates with OR

### Assignment 2: Data Pipeline
Build a data pipeline using composition:
1. Read data from source
2. Transform through multiple steps
3. Filter invalid records
4. Output to destination

### Assignment 3: Composition Framework
Design a composition framework:
1. Support lazy composition
2. Enable parallel composition
3. Provide debugging support

---

## 25. Mini Project

### Project: Function Composition Engine

Build a composition engine for data processing:

**Requirements:**
1. Support function composition with andThen/compose
2. Enable predicate composition
3. Provide pipeline builder
4. Support lazy evaluation

**Starter Code:**
```java
package academy.javaengineering.functional.composition.project;

import java.util.function.*;

public class CompositionEngine {
    
    public static <T> Function<T, T> compose(Function<T, T>... functions) {
        Function<T, T> result = Function.identity();
        for (Function<T, T> f : functions) {
            result = result.andThen(f);
        }
        return result;
    }
    
    // TODO: Implement more composition utilities
}
```

---

## 26. Summary

Function composition enables building complex transformations from simple functions. Key takeaways:

1. **andThen**: Apply this first, then argument
2. **compose**: Apply argument first, then this
3. **Predicate composition**: and, or, negate
4. **Cache composed functions**: Store in static final fields
5. **Keep chains short**: 3-5 functions maximum

### Next Steps
- Topic 10: Best Practices — Functional programming best practices
- Topic 11: Mini Project — Apply all concepts

---

## 27. References

1. [Oracle Java Tutorials: Function Composition](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/function/package-summary.html)
2. [Java Language Specification: Function Interface](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 42](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Function Composition](https://www.baeldung.com/java-function-composition)
