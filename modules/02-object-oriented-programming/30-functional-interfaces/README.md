# Functional Interfaces

## Introduction

A functional interface in Java is an interface with exactly one abstract method (SAM — Single Abstract Method). Introduced in Java 8 alongside lambda expressions and the Stream API, functional interfaces are the foundation of Java's functional programming capabilities. The `@FunctionalInterface` annotation (optional but recommended) instructs the compiler to verify that the interface has only one abstract method, preventing accidental addition of methods that would break lambda compatibility. Functional interfaces enable concise lambda expressions and method references, replacing verbose anonymous class implementations. The `java.util.function` package provides a rich set of built-in functional interfaces like `Predicate`, `Function`, `Consumer`, and `Supplier` that cover the most common functional programming patterns. Understanding functional interfaces is essential for writing modern, idiomatic Java code that leverages the Stream API, CompletableFuture, and reactive programming patterns.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Define custom functional interfaces with the `@FunctionalInterface` annotation
- [ ] Use built-in functional interfaces: `Predicate`, `Function`, `Consumer`, `Supplier`
- [ ] Apply lambda expressions and method references with functional interfaces
- [ ] Compose functional interfaces using `and`, `or`, `negate`, `andThen`, and `compose`

## Prerequisites

- [12-interfaces](../12-interfaces/) — Functional interfaces are interfaces
- [29-anonymous-classes](../29-anonymous-classes/) — Functional interfaces replace anonymous classes
- [26-enums](../26-enums/) — Enums can implement functional interfaces

## Why This Concept Exists

### The Problem

Before Java 8, implementing behavioral parameterization required creating anonymous class instances for every callback or strategy:

```java
list.sort(new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});
```

This is verbose, creates separate `.class` files, and obscures the actual logic.

### The Solution

Functional interfaces provide a target type for lambda expressions. A lambda `(a, b) -> a.compareTo(b)` can be assigned to any functional interface with a compatible abstract method. This eliminates boilerplate, enables method references, and makes functional programming patterns natural in Java.

### Real-World Analogy

Think of a functional interface like a power adapter. A USB-C port (functional interface) can connect to many different devices (lambda implementations) — a phone, a laptop, a hard drive. The port defines the contract (one connection type), and each device provides its own behavior. The adapter standardizes the connection point.

## Internal Working

### What Makes an Interface "Functional"

An interface is functional if it has exactly one abstract method. It can have:
- Any number of `default` methods
- Any number of `static` methods
- Any number of `public` methods from `Object` (like `toString`, `equals`)

The compiler counts only abstract methods (excluding `public` methods declared in `Object`).

```java
@FunctionalInterface
interface MyInterface {
    void abstractMethod(); // Counts as the one abstract method

    default void defaultMethod1() {} // Does not count
    default void defaultMethod2() {} // Does not count

    static void staticMethod() {} // Does not count

    // These Object methods don't count:
    @Override
    boolean equals(Object obj);
    @Override
    String toString();
}
```

### Lambda as Functional Interface Implementation

When you write:

```java
Comparator<String> comp = (a, b) -> a.compareTo(b);
```

The compiler:
1. Identifies that `Comparator` is a functional interface with `compare(T, T)`
2. Creates a synthetic class implementing `Comparator` (similar to anonymous class)
3. Generates a `lambda$main$0` method containing the lambda body
4. Uses `invokedynamic` to resolve the lambda at runtime (more efficient than anonymous classes)

### The `java.util.function` Package

Java 8 introduced a standard package of functional interfaces:

| Interface | Method | Description | Example Use |
|-----------|--------|-------------|-------------|
| `Predicate<T>` | `boolean test(T t)` | Tests a condition | Filtering streams |
| `Function<T,R>` | `R apply(T t)` | Transforms T to R | Mapping streams |
| `Consumer<T>` | `void accept(T t)` | Consumes T, returns void | ForEach operations |
| `Supplier<T>` | `T get()` | Provides a T | Lazy evaluation |
| `UnaryOperator<T>` | `T apply(T t)` | Transforms T to T | In-place transformation |
| `BinaryOperator<T>` | `T apply(T a, T b)` | Combines two T | Reduction operations |

## Syntax

```java
// Custom functional interface
@FunctionalInterface
interface Transformer<T, R> {
    R transform(T input);

    // Default method — allowed
    default Transformer<T, R> andThen(Transformer<R, ?> after) {
        return input -> after.transform(transform(input));
    }

    // Static method — allowed
    static <T> Transformer<T, T> identity() {
        return t -> t;
    }
}

// Using with lambda
Transformer<String, Integer> length = s -> s.length();
System.out.println(length.transform("Hello")); // 5

// Using with method reference
Transformer<String, Integer> lengthRef = String::length;

// Built-in functional interfaces
Predicate<String> isEmpty = s -> s.isEmpty();
Function<String, Integer> toLength = String::length;
Consumer<String> printer = System.out::println;
Supplier<String> randomUUID = () -> UUID.randomUUID().toString();

// Composition
Predicate<String> isNotEmpty = isEmpty.negate();
Function<String, String> toUpper = String::toUpperCase;
Function<String, Integer> upperLength = toUpper.andThen(toLength);
```

## Easy Examples

### Example 1: Custom Functional Interface for String Processing

**Problem Statement**: Create a `StringProcessor` functional interface with methods for different string transformations, and use lambda expressions to implement them.

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

@FunctionalInterface
interface StringProcessor {
    String process(String input);

    default StringProcessor andThen(StringProcessor after) {
        return input -> after.process(process(input));
    }
}

public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        StringProcessor toUpper = String::toUpperCase;
        StringProcessor addExclamation = s -> s + "!!!";
        StringProcessor reverse = s -> new StringBuilder(s).reverse().toString();

        String input = "hello world";

        System.out.println("Original: " + input);
        System.out.println("Upper: " + toUpper.process(input));
        System.out.println("Exclaim: " + addExclamation.process(input));
        System.out.println("Reverse: " + reverse.process(input));

        // Composition
        StringProcessor shout = toUpper.andThen(addExclamation);
        System.out.println("Shout: " + shout.process(input));
    }
}
```

**Expected Output**:
```
Original: hello world
Upper: HELLO WORLD
Exclaim: hello world!!!
Reverse: dlrow olleh
Shout: HELLO WORLD!!!
```

**Best Practices**:
- Always annotate with `@FunctionalInterface` to catch errors at compile time
- Keep abstract methods focused on a single responsibility
- Provide default methods for common compositions

### Example 2: Using Built-in Predicate for Filtering

**Problem Statement**: Filter a list of employees using `Predicate` for different criteria (age, department, salary).

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Employee {
    String name;
    int age;
    String department;
    double salary;

    Employee(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return String.format("%s(%d, %s, $%.0f)", name, age, department, salary);
    }
}

public class PredicateDemo {
    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", 30, "Engineering", 95000),
                new Employee("Bob", 45, "Marketing", 75000),
                new Employee("Charlie", 28, "Engineering", 85000),
                new Employee("Diana", 35, "HR", 70000),
                new Employee("Eve", 42, "Engineering", 110000)
        );

        Predicate<Employee> isEngineer = e -> e.department.equals("Engineering");
        Predicate<Employee> isSenior = e -> e.age >= 35;
        Predicate<Employee> highEarner = e -> e.salary > 80000;

        List<Employee> seniorEngineers = employees.stream()
                .filter(isEngineer.and(isSenior))
                .collect(Collectors.toList());
        System.out.println("Senior Engineers: " + seniorEngineers);

        List<Employee> highEarningEngineers = employees.stream()
                .filter(isEngineer.and(highEarner))
                .collect(Collectors.toList());
        System.out.println("High Earning Engineers: " + highEarningEngineers);

        List<Employee> nonEngineers = employees.stream()
                .filter(isEngineer.negate())
                .collect(Collectors.toList());
        System.out.println("Non-Engineers: " + nonEngineers);
    }
}
```

**Expected Output**:
```
Senior Engineers: [Eve(42, Engineering, $110000)]
High Earning Engineers: [Alice(30, Engineering, $95000), Eve(42, Engineering, $110000)]
Non-Engineers: [Bob(45, Marketing, $75000), Diana(35, HR, $70000)]
```

**Best Practices**:
- Use `Predicate.and()`, `or()`, `negate()` to compose complex filters
- Name predicates clearly for readability
- Keep predicates pure — no side effects

### Example 3: Consumer for Batch Processing

**Problem Statement**: Use `Consumer` to process a list of items with multiple operations (log, validate, save).

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerDemo {
    public static void main(String[] args) {
        List<String> orders = Arrays.asList("ORD-001", "ORD-002", "ORD-003", "ORD-004");

        Consumer<String> logOrder = order -> System.out.println("LOG: Processing " + order);
        Consumer<String> validateOrder = order -> {
            if (!order.startsWith("ORD-")) {
                throw new IllegalArgumentException("Invalid order format: " + order);
            }
            System.out.println("VALIDATE: " + order + " is valid");
        };
        Consumer<String> saveOrder = order -> System.out.println("SAVE: " + order + " saved to DB");

        Consumer<String> processOrder = logOrder.andThen(validateOrder).andThen(saveOrder);

        System.out.println("=== Processing Orders ===");
        orders.forEach(processOrder);
    }
}
```

**Expected Output**:
```
=== Processing Orders ===
LOG: Processing ORD-001
VALIDATE: ORD-001 is valid
SAVE: ORD-001 saved to DB
LOG: Processing ORD-002
VALIDATE: ORD-002 is valid
SAVE: ORD-002 saved to DB
LOG: Processing ORD-003
VALIDATE: ORD-003 is valid
SAVE: ORD-003 saved to DB
LOG: Processing ORD-004
VALIDATE: ORD-004 is valid
SAVE: ORD-004 saved to DB
```

**Best Practices**:
- Use `Consumer.andThen()` for sequential processing pipelines
- Consumers should not return values — use `Function` if you need a return value
- Keep consumers focused on a single side effect

## Medium Examples

### Example 1: Function Composition for Data Transformation Pipeline

**Problem Statement**: Build a data transformation pipeline using `Function` composition to process user input through multiple stages.

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.function.Function;

class DataPipeline {
    private final Function<String, String> pipeline;

    private DataPipeline(Function<String, String> pipeline) {
        this.pipeline = pipeline;
    }

    static DataPipeline create() {
        return new DataPipeline(Function.identity());
    }

    DataPipeline addStage(Function<String, String> stage) {
        return new DataPipeline(pipeline.andThen(stage));
    }

    String execute(String input) {
        return pipeline.apply(input);
    }
}

public class FunctionCompositionDemo {
    public static void main(String[] args) {
        Function<String, String> trim = String::trim;
        Function<String, String> toLower = String::toLowerCase;
        Function<String, String> removeSpecial = s -> s.replaceAll("[^a-zA-Z0-9\\s]", "");
        Function<String, String> collapseSpaces = s -> s.replaceAll("\\s+", " ");
        Function<String, String> capitalize = s -> {
            if (s.isEmpty()) return s;
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        };

        // Manual composition
        Function<String, String> normalizer = trim
                .andThen(toLower)
                .andThen(removeSpecial)
                .andThen(collapseSpaces)
                .andThen(capitalize);

        String[] inputs = {
                "  Hello   World!  ",
                "Java~is~Awesome!!",
                "   multiple   spaces   here   "
        };

        for (String input : inputs) {
            System.out.printf("'%s' -> '%s'%n", input, normalizer.apply(input));
        }

        // Pipeline builder pattern
        DataPipeline pipeline = DataPipeline.create()
                .addStage(String::trim)
                .addStage(s -> s.replaceAll("[^a-zA-Z]", ""))
                .addStage(String::toUpperCase);

        System.out.println("\nPipeline: " + pipeline.execute("  Hello World 123!  "));
    }
}
```

**Expected Output**:
```
'  Hello   World!  ' -> 'Hello world'
'Java~is~Awesome!!' -> 'Java is awesome'
'   multiple   spaces   here   ' -> 'Multiple spaces here'

Pipeline: HELLOWORLD
```

**Code Walkthrough**: `Function.andThen()` chains transformations left-to-right. The `DataPipeline` class wraps composition in a builder pattern. Each stage is a pure function with no side effects.

### Example 2: Supplier for Lazy Evaluation and Factory Pattern

**Problem Statement**: Use `Supplier` to implement lazy initialization and a factory pattern for creating expensive objects.

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class ExpensiveResource {
    private final String name;
    private final long createdAt;

    ExpensiveResource(String name) {
        this.name = name;
        System.out.println("  Creating expensive resource: " + name);
        // Simulate expensive initialization
        try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        this.createdAt = System.currentTimeMillis();
    }

    void use() {
        System.out.printf("  Using %s (created at %d)%n", name, createdAt);
    }
}

class ResourceCache {
    private final Map<String, Supplier<ExpensiveResource>> factories = new HashMap<>();
    private final Map<String, ExpensiveResource> cache = new HashMap<>();

    void register(String key, Supplier<ExpensiveResource> factory) {
        factories.put(key, factory);
    }

    ExpensiveResource get(String key) {
        return cache.computeIfAbsent(key, k -> {
            Supplier<ExpensiveResource> factory = factories.get(k);
            if (factory == null) throw new IllegalArgumentException("No factory for: " + k);
            return factory.get();
        });
    }
}

public class SupplierDemo {
    public static void main(String[] args) {
        ResourceCache cache = new ResourceCache();

        // Lazy registration — no resource created yet
        cache.register("db", () -> new ExpensiveResource("DatabaseConnection"));
        cache.register("cache", () -> new ExpensiveResource("RedisConnection"));
        cache.register("mq", () -> new ExpensiveResource("MessageQueue"));

        System.out.println("=== First access (creates resources) ===");
        cache.get("db").use();
        cache.get("cache").use();

        System.out.println("\n=== Second access (uses cached) ===");
        cache.get("db").use();
        cache.get("cache").use();
        cache.get("mq").use();
    }
}
```

**Expected Output**:
```
=== First access (creates resources) ===
  Creating expensive resource: DatabaseConnection
  Using DatabaseConnection (created at ...)
  Creating expensive resource: RedisConnection
  Using RedisConnection (created at ...)

=== Second access (uses cached) ===
  Using DatabaseConnection (created at ...)
  Using RedisConnection (created at ...)
  Creating expensive resource: MessageQueue
  Using MessageQueue (created at ...)
```

**Code Walkthrough**: `Supplier` defers object creation until `get()` is called. `computeIfAbsent` ensures each resource is created only once. The factory lambda captures the resource name but doesn't create the object until needed.

### Example 3: UnaryOperator and BinaryOperator for List Processing

**Problem Statement**: Use `UnaryOperator` and `BinaryOperator` to transform and reduce a collection of numbers.

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class OperatorDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // UnaryOperator: transforms T -> T
        UnaryOperator<Integer> square = n -> n * n;
        UnaryOperator<Integer> doubleValue = n -> n * 2;

        List<Integer> squared = numbers.stream()
                .map(square)
                .collect(Collectors.toList());
        System.out.println("Squared: " + squared);

        List<Integer> doubled = numbers.stream()
                .map(doubleValue)
                .collect(Collectors.toList());
        System.out.println("Doubled: " + doubled);

        // Chaining UnaryOperators
        UnaryOperator<Integer> squareAndDouble = square.andThen(doubleValue);
        List<Integer> result = numbers.stream()
                .map(squareAndDouble)
                .collect(Collectors.toList());
        System.out.println("Square then double: " + result);

        // BinaryOperator: combines (T, T) -> T
        BinaryOperator<Integer> sum = Integer::sum;
        BinaryOperator<Integer> max = BinaryOperator.maxBy(Integer::compareTo);

        int total = numbers.stream().reduce(0, sum);
        System.out.println("Sum: " + total);

        int maxValue = numbers.stream().reduce(max::apply).orElse(0);
        System.out.println("Max: " + maxValue);

        // Reduce with BinaryOperator
        int product = numbers.stream()
                .reduce(1, (a, b) -> a * b);
        System.out.println("Product: " + product);
    }
}
```

**Expected Output**:
```
Squared: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
Doubled: [2, 4, 6, 8, 10, 12, 14, 16, 18, 20]
Square then double: [2, 8, 18, 32, 50, 72, 98, 128, 162, 200]
Sum: 55
Max: 10
Product: 3628800
```

**Code Walkthrough**: `UnaryOperator<T>` is a `Function<T,T>` specialization — input and output types are the same. `BinaryOperator<T>` is a `BiFunction<T,T,T>` specialization — combines two values of the same type. Both are used extensively in Stream operations.

## Hard Examples

### Example 1: Custom Functional Interface with Multiple Composition Strategies

**Problem Statement**: Build a validation framework using custom functional interfaces that support different composition strategies (AND, OR, pipeline).

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Function;

@FunctionalInterface
interface Validator<T> {
    ValidationResult validate(T value);

    default Validator<T> and(Validator<T> other) {
        return value -> {
            ValidationResult r1 = this.validate(value);
            if (!r1.isValid()) return r1;
            return other.validate(value);
        };
    }

    default Validator<T> or(Validator<T> other) {
        return value -> {
            ValidationResult r1 = this.validate(value);
            if (r1.isValid()) return r1;
            return other.validate(value);
        };
    }

    default Validator<T> negate() {
        return value -> {
            ValidationResult r = this.validate(value);
            return r.isValid()
                    ? ValidationResult.invalid("Negated: value should not be valid")
                    : ValidationResult.valid();
        };
    }

    static <T> Validator<T> allOf(List<Validator<T>> validators) {
        return value -> {
            for (Validator<T> v : validators) {
                ValidationResult r = v.validate(value);
                if (!r.isValid()) return r;
            }
            return ValidationResult.valid();
        };
    }
}

class ValidationResult {
    private final boolean valid;
    private final String message;

    private ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    static ValidationResult valid() { return new ValidationResult(true, ""); }
    static ValidationResult invalid(String message) { return new ValidationResult(false, message); }

    boolean isValid() { return valid; }
    String getMessage() { return message; }

    @Override
    public String toString() {
        return valid ? "VALID" : "INVALID: " + message;
    }
}

class ValidationFramework {
    static <T, R> Validator<T> compose(Function<T, R> extractor, Validator<R> validator) {
        return value -> validator.validate(extractor.apply(value));
    }
}

public class ValidatorDemo {
    public static void main(String[] args) {
        Validator<String> nonEmpty = value ->
                value != null && !value.trim().isEmpty()
                        ? ValidationResult.valid()
                        : ValidationResult.invalid("Must not be empty");

        Validator<String> minLength = value ->
                value != null && value.length() >= 3
                        ? ValidationResult.valid()
                        : ValidationResult.invalid("Must be at least 3 characters");

        Validator<String> maxLength = value ->
                value != null && value.length() <= 50
                        ? ValidationResult.valid()
                        : ValidationResult.invalid("Must be at most 50 characters");

        Validator<String> alphanumeric = value ->
                value != null && value.matches("[a-zA-Z0-9]+")
                        ? ValidationResult.valid()
                        : ValidationResult.invalid("Must be alphanumeric");

        // Composition
        Validator<String> usernameValidator = nonEmpty
                .and(minLength)
                .and(maxLength)
                .and(alphanumeric);

        String[] usernames = {"ab", "validUser123", "", "has spaces", "a".repeat(51)};
        for (String username : usernames) {
            System.out.printf("  %-20s -> %s%n", username, usernameValidator.validate(username));
        }

        // Using allOf
        List<Validator<String>> validators = List.of(nonEmpty, minLength, alphanumeric);
        Validator<String> combined = Validator.allOf(validators);
        System.out.println("\nCombined validator on 'test': " + combined.validate("test"));
    }
}
```

**Unit Tests**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ValidatorTest {
    @Test
    void testNonEmpty() {
        Validator<String> validator = value ->
                value != null && !value.trim().isEmpty()
                        ? ValidationResult.valid()
                        : ValidationResult.invalid("Empty");
        assertTrue(validator.validate("hello").isValid());
        assertFalse(validator.validate("").isValid());
        assertFalse(validator.validate(null).isValid());
    }

    @Test
    void testComposition() {
        Validator<String> minLen = v -> v != null && v.length() >= 3
                ? ValidationResult.valid() : ValidationResult.invalid("Too short");
        Validator<String> maxLen = v -> v != null && v.length() <= 10
                ? ValidationResult.valid() : ValidationResult.invalid("Too long");

        Validator<String> combined = minLen.and(maxLen);
        assertTrue(combined.validate("hello").isValid());
        assertFalse(combined.validate("ab").isValid());
        assertFalse(combined.validate("a".repeat(11)).isValid());
    }
}
```

**Execution Flow**: Each validator returns a `Result` object. The `and` composition short-circuits on the first failure. The `or` composition short-circuits on the first success. The `allOf` factory combines multiple validators into a single chain.

**Complexity**: O(n) where n is the number of validators in the chain.

**Best Practices**:
- Use `@FunctionalInterface` to catch accidental method additions
- Return result objects instead of exceptions for validation chains
- Provide composition methods as default methods in the interface

### Example 2: Functional Interface for Strategy Pattern with State

**Problem Statement**: Implement a pricing strategy system using functional interfaces that maintain state across invocations.

**Implementation**:

```java
package academy.javaengineering.oop.functionalinterfaces;

import java.util.function.Function;

interface PricingStrategy {
    double calculate(double basePrice, int quantity);

    static PricingStrategy flatDiscount(double discount) {
        return (price, qty) -> (price - discount) * qty;
    }

    static PricingStrategy percentageDiscount(double percent) {
        return (price, qty) -> price * (1 - percent / 100.0) * qty;
    }

    static PricingStrategy bulkPricing(double bulkPrice, int threshold) {
        return (price, qty) -> qty >= threshold ? bulkPrice * qty : price * qty;
    }

    static PricingStrategy tiered(double[] tierPrices, int[] thresholds) {
        return (price, qty) -> {
            double total = 0;
            int remaining = qty;
            for (int i = 0; i < thresholds.length && remaining > 0; i++) {
                int tierQty = (i == thresholds.length - 1)
                        ? remaining
                        : Math.min(remaining, thresholds[i] - (i == 0 ? 0 : thresholds[i - 1]));
                total += tierPrices[i] * tierQty;
                remaining -= tierQty;
            }
            return total;
        };
    }
}

class OrderCalculator {
    static double calculateTotal(double basePrice, int quantity, PricingStrategy strategy) {
        return strategy.calculate(basePrice, quantity);
    }

    public static void main(String[] args) {
        double basePrice = 100.0;
        int quantity = 5;

        System.out.printf("Base: $%.2f x %d%n", basePrice, quantity);

        PricingStrategy flat = PricingStrategy.flatDiscount(10);
        System.out.printf("  Flat $10 off: $%.2f%n", calculateTotal(basePrice, quantity, flat));

        PricingStrategy pct = PricingStrategy.percentageDiscount(15);
        System.out.printf("  15%% off:      $%.2f%n", calculateTotal(basePrice, quantity, pct));

        PricingStrategy bulk = PricingStrategy.bulkPricing(80, 3);
        System.out.printf("  Bulk (>3):    $%.2f%n", calculateTotal(basePrice, quantity, bulk));

        PricingStrategy tiered = PricingStrategy.tiered(
                new double[]{100, 80, 60},
                new int[]{2, 4, Integer.MAX_VALUE}
        );
        System.out.printf("  Tiered:       $%.2f%n", calculateTotal(basePrice, quantity, tiered));
    }
}
```

**Execution Flow**: Each strategy is a function `(price, qty) -> total`. The static factory methods create different pricing behaviors. The tiered strategy uses thresholds to apply different prices to different quantity ranges.

**Complexity**: O(1) for flat, percentage, and bulk strategies. O(t) for tiered where t is the number of tiers.

**Best Practices**:
- Use static factory methods for readable strategy creation
- Keep strategies stateless when possible
- Document pricing assumptions in each strategy

## Exercises

### Easy

1. Create a `@FunctionalInterface` called `StringTransformer` with a `String transform(String input)` method. Write lambdas for: uppercase, lowercase, reverse, and trim.

2. Use `Predicate<Integer>` to filter a list of integers, keeping only even numbers greater than 10.

3. Use `Consumer<String>` to print each element of a list with a prefix like "[ITEM] ".

### Medium

4. Create a `@FunctionalInterface` called `MatrixTransformer` that takes a `double[][]` and returns a `double[][]`. Implement transformations for: transpose, scalar multiply, and row normalize.

5. Build a `Parser<T>` functional interface that parses a `String` into type `T`. Compose parsers to handle optional prefixes, defaults, and validation.

6. Implement a `@FunctionalInterface` called `RetryPolicy` that takes a `Runnable` and determines whether to retry based on attempt count and exception type.

### Hard

7. Build a reactive `Observable<T>` class that accepts `Consumer<T>` subscribers and notifies them on events. Support `map`, `filter`, and `subscribe` operations with lazy evaluation.

8. Create a type-safe builder using functional interfaces that validates configuration at build time using `Predicate` chains.

9. Implement a memoization decorator using `Function` that caches results of expensive computations. Support cache size limits and eviction policies.

## Interview Questions

### Easy

1. **What is a functional interface?**
   A functional interface is an interface with exactly one abstract method. It can have multiple default and static methods, and any number of `Object` methods. The `@FunctionalInterface` annotation is optional but recommended to enforce the single abstract method constraint.

2. **What is the `@FunctionalInterface` annotation used for?**
   It's a compile-time annotation that instructs the compiler to verify the interface has exactly one abstract method. If the interface has zero or more than one abstract method, compilation fails. It prevents accidental breaking of lambda compatibility.

3. **What is the difference between `Predicate` and `Function`?**
   `Predicate<T>` returns `boolean` — it tests a condition. `Function<T,R>` returns type `R` — it transforms a value. Use `Predicate` for filtering, `Function` for mapping.

### Intermediate

4. **Can a functional interface have default methods?**
   Yes. A functional interface can have any number of default methods. Default methods don't count toward the abstract method limit. They provide utility methods like `and`, `or`, `negate` for composition.

5. **How do method references relate to functional interfaces?**
   Method references (`String::length`, `System.out::println`) are shorthand for lambda expressions. They can be assigned to any compatible functional interface. The four forms are: static method, instance method of a particular object, instance method of an arbitrary object, and constructor reference.

6. **What happens if you add a new abstract method to a functional interface?**
   All lambda implementations break — compilation fails. The `@FunctionalInterface` annotation prevents this at compile time. Without the annotation, the error appears at lambda assignment sites, making debugging harder.

### Hard

7. **How does the JVM implement lambda expressions at the bytecode level?**
   Java 8+ uses `invokedynamic` instructions (the `LambdaMetafactory`) to create lambda implementations at runtime. Unlike anonymous classes, lambdas don't generate separate `.class` files. The JVM creates a synthetic class implementing the functional interface and caches it. This is more efficient than anonymous classes.

8. **Explain the difference between `Function<T,R>` and `BiFunction<T,U,R>`. Why isn't `BiFunction` a functional interface?**
   `BiFunction<T,U,R>` IS a functional interface — it has one abstract method `apply(T, U)`. `Function<T,R>` takes one argument. `BiFunction` takes two. Both are functional interfaces. The question might be testing whether you know that `BiFunction` takes two different input types, while `BinaryOperator<T>` extends `BiFunction<T,T,T>` for same-type inputs.

## Common Pitfalls

### 1. Not Annotating with @FunctionalInterface

**Wrong**:
```java
interface Processor {
    Object process(Object input);
    // Accidentally added later:
    Object process(Object input, String context);
    // Now it's NOT a functional interface — lambdas break!
}
```

**Right**:
```java
@FunctionalInterface
interface Processor {
    Object process(Object input);
    // Adding a second abstract method causes compile error — caught early!
}
```

### 2. Mutating Captured Variables in Lambdas

**Wrong**:
```java
int[] counter = {0};
list.forEach(item -> {
    counter[0]++; // Works but not thread-safe
    System.out.println(item + ": " + counter[0]);
});
```

**Right**:
```java
AtomicInteger counter = new AtomicInteger(0);
list.forEach(item -> {
    int count = counter.incrementAndGet(); // Thread-safe
    System.out.println(item + ": " + count);
});
```

### 3. Using Functional Interfaces Where Named Classes Are Clearer

**Wrong**:
```java
Function<String, ValidationResult> validator = input -> {
    if (input == null) return ValidationResult.invalid("null");
    if (input.isEmpty()) return ValidationResult.invalid("empty");
    if (input.length() > 100) return ValidationResult.invalid("too long");
    if (!input.matches("[a-z]+")) return ValidationResult.invalid("lowercase only");
    return ValidationResult.valid();
};
```

**Right**:
```java
class UsernameValidator implements Validator<String> {
    @Override
    public ValidationResult validate(String input) {
        if (input == null) return ValidationResult.invalid("null");
        if (input.isEmpty()) return ValidationResult.invalid("empty");
        if (input.length() > 100) return ValidationResult.invalid("too long");
        if (!input.matches("[a-z]+")) return ValidationResult.invalid("lowercase only");
        return ValidationResult.valid();
    }
}
```

Named classes are better for complex logic — they're debuggable, testable, and documentable.

## Best Practices

1. **Always use `@FunctionalInterface`** — It catches accidental method additions at compile time and signals intent to other developers.
2. **Prefer built-in functional interfaces** — `Predicate`, `Function`, `Consumer`, `Supplier` cover most use cases. Create custom ones only when none fit.
3. **Keep functional interfaces focused** — Each should represent a single抽象 concept. Avoid combining unrelated operations.
4. **Use method references when possible** — `String::length` is clearer than `s -> s.length()`.
5. **Document non-obvious behavior** — If a functional interface has side effects, state, or threading concerns, document them explicitly.

## Real World Usage

### How Spring Uses This

Spring uses functional interfaces extensively: `Function<T,R>` in `BeanFactoryPostProcessor`, `Predicate<Class<?>>` in component scanning filters, `Consumer<ConfigurableListableBeanFactory>` in bean configuration, `Supplier<T>` in `@Bean` factory methods, and `Supplier<T>` in `@Conditional` evaluation.

### How JDK Uses This

The JDK defines functional interfaces in `java.util.function` (`Predicate`, `Function`, `Consumer`, `Supplier`, `BinaryOperator`, `UnaryOperator`). `java.util.Comparator` is a functional interface. `java.lang.Runnable` and `java.util.concurrent.Callable` are functional interfaces used with threads and concurrency.

### Enterprise Usage

Enterprise applications use functional interfaces for Stream API operations, CompletableFuture chains, custom validation frameworks, event handlers, data transformation pipelines, and strategy patterns. Functional interfaces are the backbone of reactive programming with Project Reactor and RxJava.

## References

- [Oracle — Functional Interface Tutorial](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
- [Javadoc — java.util.function](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/function/package-summary.html)
- [Baeldung — Java Functional Interfaces](https://www.baeldung.com/java-functional-interface)
- [Effective Java, Item 42: Prefer lambdas to anonymous classes](https://books.google.com/books?id=BIoul6j2KcIC)

## Summary

- A functional interface has exactly one abstract method and can be implemented with a lambda expression
- Use `@FunctionalInterface` to enforce the single abstract method constraint at compile time
- Built-in interfaces (`Predicate`, `Function`, `Consumer`, `Supplier`) cover common patterns
- Method references (`String::length`) are concise alternatives to lambdas for existing methods
- Functional interfaces enable composition via `and`, `or`, `negate`, `andThen`, and `compose`
- The JVM implements lambdas using `invokedynamic` — more efficient than anonymous classes

**Next Step**: [31-solid-principles](../31-solid-principles/)
