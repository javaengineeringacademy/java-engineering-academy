# Topic 01: Introduction to Functional Programming (Part 4)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md) | [📖 Back to Part 3](README-part3.md)

---

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

1. [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
2. [Oracle Java Tutorials: Functional Interfaces](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/function/package-summary.html)
3. [Java 21 Language Specification: Lambda Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html)
4. [Effective Java, 3rd Edition - Item 42: Prefer lambdas to anonymous classes](https://www.oreilly.com/library/view/effective-java/9780134686097/)
5. [Java Performance, 2nd Edition - Chapter on Lambdas](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/)
6. [Baeldung: Functional Programming in Java](https://www.baeldung.com/java-functional-programming)
