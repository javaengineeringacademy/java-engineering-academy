# Functional Interfaces

## Introduction

A functional interface is an interface with exactly one abstract method. They are the foundation for lambda expressions and method references in Java 8+, enabling functional programming paradigms.

## Learning Objectives

By the end of this topic, you will be able to:

- Understand what makes an interface functional
- Use built-in functional interfaces from `java.util.function`
- Create custom functional interfaces
- Apply lambda expressions and method references
- Understand the @FunctionalInterface annotation

## Prerequisites

- Interfaces
- Anonymous Classes
- Lambda Expressions (basic understanding)

## Why This Concept Exists

### The Problem

Before Java 8:

- Iterating required explicit loops
- Callbacks needed anonymous classes (verbose)
- No first-class functions
- No easy way to pass behavior as data

### The Solution

Functional interfaces enable:

- **Lambda expressions**: Concise behavior implementation
- **Method references**: Even more concise syntax
- **Higher-order functions**: Functions that accept/return functions
- **Streams API**: Functional data processing
- **Composition**: Combine behaviors

### Real-World Analogy

Think of functional interfaces as **job descriptions**:

- A job description specifies what needs to be done (abstract method)
- Different people can fill the role (implementations)
- The interface defines the contract, not the implementation
- You can swap people in/out without changing the system

## Internal Working

### How Functional Interfaces Work

1. **Compilation**: Compiler verifies exactly one abstract method
2. **Lambda**: Lambda expressions create instances of functional interfaces
3. **Method References**: Shorthand for simple lambda expressions
4. **Type Inference**: Compiler infers types from context

### @FunctionalInterface Annotation

```java
@FunctionalInterface
public interface MyFunction<T, R> {
    R apply(T t);
    
    // OK: default methods don't count
    default void doSomething() {}
    
    // OK: static methods don't count
    static void staticMethod() {}
    
    // ERROR: Would make it non-functional
    // void anotherMethod();
}
```

## Built-in Functional Interfaces

| Interface | Method | Description | Example |
|-----------|--------|-------------|---------|
| `Predicate<T>` | `boolean test(T t)` | Tests a condition | `s -> s.isEmpty()` |
| `Function<T, R>` | `R apply(T t)` | Transforms T to R | `s -> s.length()` |
| `Consumer<T>` | `void accept(T t)` | Consumes T | `s -> System.out.println(s)` |
| `Supplier<T>` | `T get()` | Supplies T | `() -> new ArrayList<>()` |
| `UnaryOperator<T>` | `T apply(T t)` | Unary operation on T | `s -> s.toUpperCase()` |
| `BinaryOperator<T>` | `T apply(T t1, T t2)` | Binary operation on T | `(a, b) -> a + b` |

## Syntax

### Basic Functional Interface

```java
@FunctionalInterface
public interface Transformer<T, R> {
    R transform(T input);
}

// Using with lambda
Transformer<String, Integer> lengthTransformer = s -> s.length();
```

### With Multiple Parameters

```java
@FunctionalInterface
public interface BiFunction<T, U, R> {
    R apply(T t, U u);
}

// Using with lambda
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
```

## Easy Examples

### Example 1: Predicate - Filtering

**Problem Statement**: Use Predicate to filter a list of strings.

**Implementation**:

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateExample {
    
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // Filter names starting with 'A'
        List<String> aNames = filter(names, s -> s.startsWith("A"));
        System.out.println("Names starting with A: " + aNames);
        
        // Filter names with length > 3
        List<String> longNames = filter(names, s -> s.length() > 3);
        System.out.println("Names with length > 3: " + longNames);
        
        // Combine predicates
        Predicate<String> startsWithA = s -> s.startsWith("A");
        Predicate<String> lengthGreaterThan3 = s -> s.length() > 3;
        
        List<String> combined = filter(names, startsWithA.and(lengthGreaterThan3));
        System.out.println("Starts with A AND length > 3: " + combined);
    }
}
```

**Output**:
```
Names starting with A: [Alice]
Names with length > 3: [Alice, Charlie, Diana]
Starts with A AND length > 3: [Alice]
```

**Complexity**: O(n) for filtering

**Best Practices**:
- Use Predicate for boolean conditions
- Combine predicates using `and()`, `or()`, `negate()`
- Keep predicates simple and focused

### Example 2: Function - Transformation

**Problem Statement**: Use Function to transform a list of strings to their lengths.

**Implementation**:

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FunctionExample {
    
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        return list.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana");
        
        // Map to lengths
        List<Integer> lengths = map(names, String::length);
        System.out.println("Lengths: " + lengths);
        
        // Map to uppercase
        List<String> uppercased = map(names, String::toUpperCase);
        System.out.println("Uppercased: " + uppercased);
        
        // Compose functions
        Function<String, Integer> lengthFunc = String::length;
        Function<Integer, String> toStringFunc = i -> "Length: " + i;
        
        Function<String, String> composed = lengthFunc.andThen(toStringFunc);
        List<String> descriptions = map(names, composed);
        System.out.println("Descriptions: " + descriptions);
    }
}
```

**Output**:
```
Lengths: [5, 3, 7, 5]
Uppercased: [ALICE, BOB, CHARLIE, DIANA]
Descriptions: [Length: 5, Length: 3, Length: 7, Length: 5]
```

**Complexity**: O(n) for mapping

### Example 3: Consumer - Side Effects

**Problem Statement**: Use Consumer to perform actions on a list.

**Implementation**:

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerExample {
    
    public static <T> void forEach(List<T> list, Consumer<T> action) {
        for (T item : list) {
            action.accept(item);
        }
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        
        // Print each name
        forEach(names, name -> System.out.println("Name: " + name));
        
        // Convert to uppercase and print
        forEach(names, name -> System.out.println("Uppercase: " + name.toUpperCase()));
        
        // Chain consumers
        Consumer<String> print = name -> System.out.print(name);
        Consumer<String> newLine = name -> System.out.println();
        
        forEach(names, print.andThen(newLine));
    }
}
```

**Output**:
```
Name: Alice
Name: Bob
Name: Charlie
Uppercase: ALICE
Uppercase: BOB
Uppercase: CHARLIE
Alice
Bob
Charlie
```

## Medium Examples

### Example 4: Supplier - Factory Pattern

**Problem Statement**: Use Supplier to create objects lazily.

**Implementation**:

```java
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.List;

public class SupplierExample {
    
    // Supplier as factory
    public static <T> List<T> createList(Supplier<List<T>> supplier, int count, java.util.function.Function<Integer, T> factory) {
        List<T> list = supplier.get();
        for (int i = 0; i < count; i++) {
            list.add(factory.apply(i));
        }
        return list;
    }
    
    public static void main(String[] args) {
        // Create list of integers
        List<Integer> numbers = createList(ArrayList::new, 5, i -> i * 2);
        System.out.println("Numbers: " + numbers);
        
        // Create list of strings
        List<String> strings = createList(ArrayList::new, 3, i -> "Item " + i);
        System.out.println("Strings: " + strings);
        
        // Lazy initialization
        Supplier<List<String>> lazyList = () -> {
            System.out.println("Creating list...");
            return new ArrayList<>();
        };
        
        System.out.println("Before get:");
        List<String> list = lazyList.get(); // Creates list now
        System.out.println("After get");
    }
}
```

**Output**：
```
Numbers: [0, 2, 4, 6, 8]
Strings: [Item 0, Item 1, Item 2]
Before get:
Creating list...
After get
```

**Best Practices**:
- Use Supplier for lazy initialization
- Use for factory patterns
- Consider caching for expensive objects

### Example 5: Method References

**Problem Statement**: Simplify lambda expressions using method references.

**Implementation**:

```java
import java.util.Arrays;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;

public class MethodReferenceExample {
    
    // Class::staticMethod
    static boolean isStatic(String s) {
        return s.startsWith("S");
    }
    
    // Instance::method
    String instanceMethod(String s) {
        return s.toLowerCase();
    }
    
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "Diana", "Eve");
        
        // Lambda: s -> s.length()
        // Method reference: String::length
        List<Integer> lengths = names.stream()
            .map(String::length) // Method reference
            .collect(Collectors.toList());
        System.out.println("Lengths: " + lengths);
        
        // Lambda: s -> System.out.println(s)
        // Method reference: System.out::println
        names.forEach(System.out::println);
        
        // Lambda: s -> Integer.parseInt(s)
        // Method reference: Integer::parseInt
        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5");
        List<Integer> parsed = numbers.stream()
            .map(Integer::parseInt)
            .collect(Collectors.toList());
        System.out.println("Parsed: " + parsed);
        
        // Constructor reference
        List<StringBuilder> builders = names.stream()
            .map(StringBuilder::new) // Constructor reference
            .collect(Collectors.toList());
        System.out.println("Builders: " + builders.size());
    }
}
```

**Output**:
```
Lengths: [5, 3, 7, 5, 3]
Alice
Bob
Charlie
Diana
Eve
Parsed: [1, 2, 3, 4, 5]
Builders: 5
```

**Complexity**: O(n) for stream operations

## Hard Examples

### Example 6: Custom Functional Interface with Composition

**Problem Statement**: Create a custom functional interface with composition methods.

**Implementation**:

```java
@FunctionalInterface
public interface FunctionChain<T, R> {
    R apply(T input);
    
    // Composition: this after other
    default <V> FunctionChain<T, R> compose(FunctionChain<V, T> before) {
        return input -> apply(before.apply(input));
    }
    
    // Composition: other after this
    default <V> FunctionChain<R, V> andThen(FunctionChain<R, V> after) {
        return input -> after.apply(apply(input));
    }
    
    // Identity function
    static <T> FunctionChain<T, T> identity() {
        return input -> input;
    }
    
    // Compose multiple functions
    @SafeVarargs
    static <T> FunctionChain<T, T> composeAll(FunctionChain<T, T>... functions) {
        FunctionChain<T, T> result = identity();
        for (FunctionChain<T, T> function : functions) {
            result = result.andThen(function);
        }
        return result;
    }
}

// Usage
public class FunctionChainExample {
    
    public static void main(String[] args) {
        // Chain transformations
        FunctionChain<String, String> trim = String::trim;
        FunctionChain<String, String> lower = String::toLowerCase;
        FunctionChain<String, String> removeSpaces = s -> s.replace(" ", "");
        
        FunctionChain<String, String> pipeline = trim
            .andThen(lower)
            .andThen(removeSpaces);
        
        System.out.println(pipeline.apply("  Hello World  ")); // helloworld
        
        // Compose functions
        FunctionChain<Integer, Integer> doubleIt = x -> x * 2;
        FunctionChain<Integer, Integer> addTen = x -> x + 10;
        
        FunctionChain<Integer, Integer> composed = doubleIt.compose(addTen);
        System.out.println(composed.apply(5)); // 20 (5+10=15, 15*2=30)
        
        // Compose all
        FunctionChain<Integer, Integer> pipeline2 = FunctionChain.composeAll(
            x -> x + 1,
            x -> x * 2,
            x -> x - 3
        );
        
        System.out.println(pipeline2.apply(5)); // 9 ((5+1)=6, 6*2=12, 12-3=9)
    }
}
```

**Output**:
```
helloworld
30
9
```

**Complexity**: O(1) for each function application

**Best Practices**:
- Use composition to build complex transformations
- Keep individual functions simple and focused
- Consider performance implications of chaining

### Example 7: Functional Interface for Stateful Operations

**Problem Statement**: Create a stateful accumulator using functional interfaces.

**Implementation**：

```java
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;

public class StatefulAccumulator<T> {
    
    private final Supplier<T> initializer;
    private final BinaryOperator<T> accumulator;
    private final Function<T, T> transformer;
    
    private T state;
    
    public StatefulAccumulator(Supplier<T> initializer, 
                               BinaryOperator<T> accumulator,
                               Function<T, T> transformer) {
        this.initializer = initializer;
        this.accumulator = accumulator;
        this.transformer = transformer;
        this.state = initializer.get();
    }
    
    public void accumulate(T value) {
        state = accumulator.apply(state, value);
    }
    
    public void transform() {
        state = transformer.apply(state);
    }
    
    public T getState() {
        return state;
    }
    
    public void reset() {
        state = initializer.get();
    }
    
    // Factory methods
    public static StatefulAccumulator<Integer> createSumAccumulator() {
        return new StatefulAccumulator<>(
            () -> 0,
            Integer::sum,
            x -> x * 2 // Double the sum
        );
    }
    
    public static StatefulAccumulator<String> createStringConcatAccumulator() {
        return new StatefulAccumulator<>(
            () -> "",
            String::concat,
            String::toUpperCase
        );
    }
    
    public static void main(String[] args) {
        // Integer accumulator
        StatefulAccumulator<Integer> intAcc = StatefulAccumulator.createSumAccumulator();
        intAcc.accumulate(5);
        intAcc.accumulate(10);
        intAcc.accumulate(15);
        System.out.println("Sum: " + intAcc.getState()); // 30
        
        intAcc.transform(); // Double
        System.out.println("Doubled: " + intAcc.getState()); // 60
        
        // String accumulator
        StatefulAccumulator<String> strAcc = StatefulAccumulator.createStringConcatAccumulator();
        strAcc.accumulate("Hello");
        strAcc.accumulate(" ");
        strAcc.accumulate("World");
        System.out.println("Concatenated: " + strAcc.getState()); // HelloWorld
        
        strAcc.transform(); // Uppercase
        System.out.println("Uppercased: " + strAcc.getState()); // HELLOWORLD
    }
}
```

**Output**:
```
Sum: 30
Doubled: 60
Concatenated: HelloWorld
Uppercased: HELLOWORLD
```

## Exercises

### Easy

1. **Filter Numbers**: Create a list of integers and filter them using Predicate to get only even numbers.

2. **Transform Strings**: Use Function to convert a list of strings to uppercase.

### Medium

3. **Custom Functional Interface**: Create a functional interface `Validator<T>` with a `validate(T t)` method that returns a boolean.

4. **Function Composition**: Implement a pipeline that takes a string, trims it, converts to lowercase, and removes special characters.

### Hard

5. **Stateful Stream Processing**: Create a stateful accumulator that tracks running average using functional interfaces.

6. **Custom Stream Operations**: Implement `reduce`, `filter`, and `map` operations using functional interfaces.

## Interview Questions

### Beginner

1. **What is a functional interface?**
   A functional interface is an interface with exactly one abstract method. It can have default and static methods, but only one abstract method.

2. **What is the @FunctionalInterface annotation?**
   It's an optional annotation that tells the compiler to verify the interface has exactly one abstract method. It's not required but recommended.

3. **What's the difference between Predicate and Function?**
   Predicate returns boolean (tests a condition), Function returns a value (transforms input).

### Intermediate

4. **Can a functional interface have default methods?**
   Yes, functional interfaces can have any number of default and static methods, as long as there's only one abstract method.

5. **How do method references work with functional interfaces?**
   Method references are shorthand for lambda expressions that simply call an existing method. They make code more readable.

6. **What is function composition?**
   Combining multiple functions into one. `andThen` applies this function first, then the other. `compose` applies the other function first, then this one.

### Senior

7. **How do functional interfaces relate to the Command pattern?**
   Functional interfaces implement the Command pattern. Each lambda or method reference is a command object.

8. **What are the performance implications of using functional interfaces?**
   Functional interfaces can be inlined by the JVM, avoiding object creation. However, complex lambdas may not be optimized.

9. **How do functional interfaces work with type inference?**
   The compiler infers types from context. This allows concise syntax without explicit type declarations.

### Architecture

10. **When would you create a custom functional interface vs using built-in ones?**
    Create custom when you need specific documentation, multiple abstract methods (not functional), or domain-specific semantics.

11. **How do functional interfaces support the Strategy pattern?**
    Each strategy can be implemented as a functional interface, allowing strategies to be passed as lambdas.

12. **Can functional interfaces be used for dependency injection?**
    Yes, you can inject functional interfaces as dependencies, allowing flexible behavior configuration.

### Scenario

13. **You need to implement a filter for a collection. How would you use functional interfaces?**

14. **You're building a data pipeline with multiple transformations. How would you design it?**

15. **You have a legacy codebase and want to modernize it. How would you introduce functional interfaces?**

### Coding

16. **Implement a `map` function that transforms a list using a Function.**

17. **Create a `reduce` function that accumulates a list using a BinaryOperator.**

18. **Design a pipeline builder that chains multiple transformations.

### Production

19. **How would you handle errors in functional interfaces?**

20. **How would you test code that uses functional interfaces?**

### Debugging

21. **Why am I getting "Variable used in lambda expression must be final or effectively final"?**

22. **How do I debug a complex function composition pipeline?

## Common Pitfalls

### 1. Mutable State in Lambdas

**Wrong**:
```java
int[] counter = {0};
list.forEach(x -> counter[0]++); // Avoid mutable state
```

**Right**:
```java
long count = list.stream().count(); // Use stream operations
```

### 2. Overusing Functional Interfaces

**Wrong**:
```java
// Simple logic doesn't need functional interfaces
Function<String, Boolean> isEmpty = s -> s.isEmpty();
list.stream().filter(isEmpty::apply);
```

**Right**:
```java
// Direct usage is clearer
list.stream().filter(String::isEmpty);
```

### 3. Forgetting That Lambdas Create Objects

**Wrong**:
```java
// Creates new object on each call
list.stream().filter(s -> s.length() > 3);
```

**Right**:
```java
// Reuse if possible
Predicate<String> longString = s -> s.length() > 3;
list.stream().filter(longString);
```

## Best Practices

### 1. Use @FunctionalInterface Annotation

Always annotate functional interfaces to get compile-time verification.

### 2. Keep Functional Interfaces Focused

Each functional interface should have a single, clear purpose.

### 3. Use Method References When Possible

Method references are more readable than lambdas for simple cases.

### 4. Consider Performance

Functional interfaces can create objects. Reuse them when possible.

### 5. Document Parameters

Add Javadoc to clarify what each parameter represents.

## Real World Usage

### JDK Usage

The JDK uses functional interfaces extensively:

```java
// java.util.function package
public interface Predicate<T> {
    boolean test(T t);
}

public interface Function<T, R> {
    R apply(T t);
}

// java.util.stream
Stream<T> filter(Predicate<? super T> predicate);
<R> Stream<R> map(Function<? super T, ? extends R> mapper);
```

### Spring Framework

```java
// Spring uses functional interfaces for configuration
@Bean
public Function<DatabaseConnection, DataSource> dataSourceFactory() {
    return connection -> new DataSource(connection);
}
```

### Reactor (Reactive Streams)

```java
// Reactor uses functional interfaces heavily
Mono.fromCallable(() -> fetchData())
    .map(data -> transform(data))
    .filter(data -> data.isValid())
    .subscribe(result -> process(result));
```

## Summary

Functional interfaces are the foundation of functional programming in Java. Key takeaways:

- **Definition**: Interface with exactly one abstract method
- **Annotation**: Use `@FunctionalInterface` for verification
- **Built-in**: Predicate, Function, Consumer, Supplier, etc.
- **Lambda expressions**: Create implementations concisely
- **Method references**: Even more concise syntax
- **Composition**: Combine functions using `andThen` and `compose`
- **Use cases**: Callbacks, strategies, pipelines, streams

**Next Steps**: Learn about sealed classes for restricted hierarchies, or design patterns that use functional interfaces.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    JAVA 21 FUNCTIONAL INTERFACES            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              java.util.function Package              │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │                                                     │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐         │   │
│  │  │Predicate │  │ Function │  │ Consumer │         │   │
│  │  │  <T>     │  │  <T,R>   │  │  <T>     │         │   │
│  │  └────┬─────┘  └────┬─────┘  └────┬─────┘         │   │
│  │       │              │              │               │   │
│  │       ▼              ▼              ▼               │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐         │   │
│  │  │ Supplier │  │UnaryOp   │  │BinaryOp  │         │   │
│  │  │  <T>     │  │  <T>     │  │  <T>     │         │   │
│  │  └──────────┘  └──────────┘  └──────────┘         │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Implementation Mechanisms              │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │                                                     │   │
│  │  Lambda Expressions ──► Anonymous Classes           │   │
│  │         │                      │                    │   │
│  │         ▼                      ▼                    │   │
│  │  Method References ──► Bytecode Generation          │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │              Composition Pipeline                   │   │
│  ├─────────────────────────────────────────────────────┤   │
│  │                                                     │   │
│  │  Input ──► andThen() ──► andThen() ──► Output       │   │
│  │    ▲                                       │        │   │
│  │    └───────────── compose() ◄─────────────┘        │   │
│  │                                                     │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

## Flow Diagram

```
┌──────────────┐
│   Start      │
└──────┬───────┘
       │
       ▼
┌──────────────┐     No
│ Need to      ├──────────┐
│ pass behavior│          │
│ as parameter?│          │
└──────┬───────┘          │
       │ Yes              │
       ▼                  ▼
┌──────────────┐  ┌──────────────┐
│ Define or    │  │ Use regular  │
│ use existing │  │ method/class │
│ functional   │  └──────────────┘
│ interface?   │
└──────┬───────┘
       │
       ▼
┌──────────────┐     Yes
│ Has exactly  ├──────────┐
│ one abstract │          │
│ method?      │          │
└──────┬───────┘          │
       │ No               │
       ▼                  ▼
┌──────────────┐  ┌──────────────┐
│ Use @        │  │ Apply        │
│ Functional   │  │ @Functional  │
│ Interface    │  │ Interface    │
│ anyway       │  │ annotation   │
└──────┬───────┘  └──────┬───────┘
       │                 │
       │                 ▼
       │          ┌──────────────┐
       │          │ Choose       │
       │          │ syntax:      │
       │          │ Lambda or    │
       │          │ Method Ref?  │
       │          └──────┬───────┘
       │                 │
       │      ┌──────────┴──────────┐
       │      ▼                     ▼
       │ ┌──────────────┐  ┌──────────────┐
       │ │ Write lambda │  │ Use ::       │
       │ │ expression   │  │ method ref   │
       │ └──────┬───────┘  └──────┬───────┘
       │        │                 │
       │        └────────┬────────┘
       │                 │
       │                 ▼
       │          ┌──────────────┐
       │          │ Apply to     │
       │          │ streams or   │
       │          │ higher-order │
       │          │ functions    │
       │          └──────┬───────┘
       │                 │
       └────────┬────────┘
                │
                ▼
         ┌──────────────┐
         │     End      │
         └──────────────┘
```

## Time Complexity

| Operation | Complexity | Description |
|-----------|------------|-------------|
| Lambda creation | O(1) | Compiling a lambda is constant time |
| Method reference | O(1) | Same as lambda, just syntactic sugar |
| Single `apply()` call | O(1) | Direct method invocation |
| `Predicate.test()` | O(1) typically | Depends on implementation |
| `Function.apply()` | O(1) typically | Depends on implementation |
| `andThen()` composition | O(1) | Creates composed wrapper, no execution |
| `compose()` composition | O(1) | Creates composed wrapper, no execution |
| Stream `filter(Predicate)` | O(n) | Evaluates predicate per element |
| Stream `map(Function)` | O(n) | Applies function per element |
| Stream `reduce(BinaryOperator)` | O(n) | Combines n elements |

### Benchmarking Example

```java
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class ComplexityBenchmark {
    
    static long benchmark(Runnable operation, int iterations) {
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            operation.run();
        }
        return System.nanoTime() - start;
    }
    
    public static void main(String[] args) {
        int iterations = 10_000_000;
        
        // O(1) lambda creation vs execution
        IntUnaryOperator square = x -> x * x;
        long lambdaTime = benchmark(() -> square.applyAsInt(42), iterations);
        System.out.println("Lambda execution: " + lambdaTime / 1_000_000 + "ms");
        
        // Composition overhead
        IntUnaryOperator doubleIt = x -> x * 2;
        IntUnaryOperator addTen = x -> x + 10;
        IntUnaryOperator composed = doubleIt.andThen(addTen);
        long composedTime = benchmark(() -> composed.applyAsInt(42), iterations);
        System.out.println("Composed execution: " + composedTime / 1_000_000 + "ms");
        
        // Direct method call for comparison
        long directTime = benchmark(() -> {
            int result = 42;
            result = result * 2;
            result = result + 10;
        }, iterations);
        System.out.println("Direct execution: " + directTime / 1_000_000 + "ms");
    }
}
```

## Space Complexity

| Object Created | Space | Notes |
|----------------|-------|-------|
| Lambda instance | O(1) | JVM may cache and reuse |
| Method reference | O(1) | Same as lambda |
| `andThen()` chain | O(k) | k = chain depth |
| `compose()` chain | O(k) | k = chain depth |
| Closure capturing | O(c) | c = captured variables |

### Memory Analysis Example

```java
import java.util.function.*;
import java.util.List;
import java.util.ArrayList;

public class SpaceComplexityDemo {
    
    // Captures no variables - can be cached
    static final Function<Integer, Integer> CACHED_LAMBDA = x -> x * 2;
    
    // Captures variable - new instance each time (if effectively non-final)
    static Function<Integer, Integer> createMultiplier(int factor) {
        return x -> x * factor; // Captures 'factor'
    }
    
    public static void main(String[] args) {
        // These may reuse the same object (JVM optimization)
        Function<Integer, Integer> f1 = x -> x + 1;
        Function<Integer, Integer> f2 = x -> x + 1;
        System.out.println("Same instance? " + (f1 == f2)); // Often true
        
        // Composition creates new objects
        Function<Integer, Integer> addOne = x -> x + 1;
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<Integer, Integer> tripleIt = x -> x * 3;
        
        // Chain depth 2: addOne -> doubleIt
        Function<Integer, Integer> chain2 = addOne.andThen(doubleIt);
        
        // Chain depth 3: addOne -> doubleIt -> tripleIt
        Function<Integer, Integer> chain3 = addOne.andThen(doubleIt).andThen(tripleIt);
        
        System.out.println("Chain2 result: " + chain2.apply(5)); // (5+1)*2 = 12
        System.out.println("Chain3 result: " + chain3.apply(5)); // ((5+1)*2)*3 = 36
        
        // Each composition adds one wrapper object to memory
        // Prefer flatMap over nested andThen when possible
    }
}
```

## Thread Safety

### Immutable Functional Interfaces (Thread-Safe)

```java
import java.util.function.*;

public class ThreadSafeFunctional {
    
    // Predicate: stateless, thread-safe
    static final Predicate<String> IS_EMPTY = String::isEmpty;
    
    // Function: stateless, thread-safe
    static final Function<String, Integer> TO_LENGTH = String::length;
    
    // Consumer: stateless, thread-safe
    static final Consumer<String> PRINTER = System.out::println;
    
    public static void main(String[] args) throws InterruptedException {
        // Safe to share across threads
        List<Thread> threads = new ArrayList<>();
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");
        
        for (int i = 0; i < 4; i++) {
            final int index = i;
            Thread t = new Thread(() -> {
                String name = names.get(index);
                if (IS_EMPTY.test(name)) {
                    PRINTER.accept("Empty: " + name);
                } else {
                    PRINTER.accept("Length " + TO_LENGTH.apply(name) + ": " + name);
                }
            });
            threads.add(t);
            t.start();
        }
        
        for (Thread t : threads) {
            t.join();
        }
    }
}
```

### Mutable State Pitfall

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class MutableStatePitfall {
    
    public static void main(String[] args) {
        // WRONG: Race condition
        // int[] counter = {0};
        // IntConsumer increment = i -> counter[0]++; // Not thread-safe
        
        // RIGHT: Use AtomicReference
        AtomicInteger safeCounter = new AtomicInteger(0);
        IntConsumer safeIncrement = safeCounter::incrementAndGet;
        
        // Simulate concurrent access
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safeIncrement.accept(j);
                }
            });
        }
        
        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        
        System.out.println("Safe counter: " + safeCounter.get()); // 10000
    }
}
```

## Comparison Table

| Feature | Functional Interface | Abstract Class | Regular Interface |
|---------|---------------------|----------------|-------------------|
| Abstract methods | Exactly 1 | 0 or more | 0 or more |
| Default methods | Yes | Yes | Yes |
| Static methods | Yes | Yes | Yes |
| Instance fields | No | Yes | No (only static final) |
| Constructor | No | Yes | No |
| Instantiation via lambda | Yes | No | No |
| Multiple inheritance | No (single method) | Yes | Yes |
| @FunctionalInterface | Recommended | N/A | N/A |

### Java Functional Interfaces vs C++ Function Objects

| Aspect | Java | C++ |
|--------|------|-----|
| Mechanism | Interface + lambda | `std::function` + functor |
| Type safety | Full | Full |
| Overhead | Indirect (invokedynamic) | Direct (template) |
| Multiple methods | No (1 abstract only) | Yes (operator()) |
| State capture | Via closure | Via lambda capture |

## Decision Tree

```
Should you use a functional interface?

Do you need to pass behavior as a parameter?
├── Yes
│   ├── Is the behavior a single method call?
│   │   ├── Yes → Use method reference (ClassName::methodName)
│   │   └── No
│   │       ├── Is it a simple expression?
│   │       │   ├── Yes → Use lambda expression
│   │       │   └── No → Consider named class + @FunctionalInterface
│   │       │
│   │       └── Do you need to compose multiple behaviors?
│   │           ├── Yes → Use andThen()/compose() pipeline
│   │           └── No → Single functional interface
│   │
│   └── Does the behavior need state?
│       ├── Yes → Use class with mutable fields (not functional interface)
│       └── No → Use functional interface
│
└── No
    └── Use a regular interface/abstract class
```

### Code Decision Helper

```java
import java.util.function.*;

public class DecisionHelper {
    
    // DECISION: Simple callback → use Consumer
    public static <T> void executeWithCallback(T value, Consumer<T> callback) {
        callback.accept(value);
    }
    
    // DECISION: Validation → use Predicate
    public static <T> boolean validate(T value, Predicate<T> validator) {
        return validator.test(value);
    }
    
    // DECISION: Transformation → use Function
    public static <T, R> R transform(T input, Function<T, R> transformer) {
        return transformer.apply(input);
    }
    
    // DECISION: Lazy initialization → use Supplier
    public static <T> T lazilyInitialize(Supplier<T> factory) {
        return factory.get();
    }
    
    public static void main(String[] args) {
        // Using each decision path
        executeWithCallback("Hello", s -> System.out.println(s.toUpperCase()));
        
        boolean valid = validate(42, n -> n > 0 && n < 100);
        System.out.println("Valid: " + valid);
        
        String result = transform(42, n -> "Number: " + n);
        System.out.println(result);
        
        String lazy = lazilyInitialize(() -> "Expensive initialization");
        System.out.println(lazy);
    }
}
```

## Assignments

### Assignment 1: Event Handler System (Easy)

Create a generic event handler system using functional interfaces.

```java
@FunctionalInterface
public interface EventHandler<T> {
    void handle(T event);
    
    default EventHandler<T> andThen(EventHandler<T> after) {
        return event -> {
            handle(event);
            after.handle(event);
        };
    }
}

public class EventEmitter<T> {
    private final List<EventHandler<T>> handlers = new ArrayList<>();
    
    public void on(EventHandler<T> handler) {
        handlers.add(handler);
    }
    
    public void emit(T event) {
        handlers.forEach(h -> h.handle(event));
    }
    
    public static void main(String[] args) {
        EventEmitter<String> emitter = new EventEmitter<>();
        
        emitter.on(event -> System.out.println("Logger: " + event));
        emitter.on(event -> System.out.println("Metrics: " + event.length()));
        
        emitter.emit("User logged in");
    }
}
```

### Assignment 2: Data Pipeline Builder (Medium)

Build a reusable data pipeline using function composition.

```java
import java.util.function.*;
import java.util.List;
import java.util.stream.Collectors;

public class PipelineBuilder<T> {
    private Function<T, T> pipeline = Function.identity();
    
    public PipelineBuilder<T> addStep(Function<T, T> step) {
        pipeline = pipeline.andThen(step);
        return this;
    }
    
    public PipelineBuilder<T> addFilter(Predicate<T> filter) {
        // This is simplified; in practice you'd work with Stream<T>
        return this;
    }
    
    public Function<T, T> build() {
        return pipeline;
    }
    
    public T execute(T input) {
        return pipeline.apply(input);
    }
    
    public static void main(String[] args) {
        PipelineBuilder<String> pipeline = new PipelineBuilder<>();
        
        Function<String, String> processor = pipeline
            .addStep(String::trim)
            .addStep(String::toLowerCase)
            .addStep(s -> s.replaceAll("\\s+", "_"))
            .build();
        
        System.out.println(processor.apply("  Hello World  ")); // hello_world
        System.out.println(processor.apply("  Java 21  is  GREAT  ")); // java_21_is_great
    }
}
```

### Assignment 3: Functional Cache (Hard)

Implement a memoization utility using functional interfaces.

```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Memoizer<T, R> {
    private final Map<T, R> cache = new ConcurrentHashMap<>();
    private final Function<T, R> function;
    
    private Memoizer(Function<T, R> function) {
        this.function = function;
    }
    
    public static <T, R> Memoizer<T, R> memoize(Function<T, R> function) {
        return new Memoizer<>(function);
    }
    
    public R apply(T input) {
        return cache.computeIfAbsent(input, function);
    }
    
    public int cacheSize() {
        return cache.size();
    }
    
    public static void main(String[] args) {
        // Expensive computation
        Memoizer<Integer, Long> fibonacci = Memoizer.memoize(n -> {
            if (n <= 1) return (long) n;
            long a = 0, b = 1;
            for (int i = 2; i <= n; i++) {
                long temp = a + b;
                a = b;
                b = temp;
            }
            return b;
        });
        
        System.out.println(fibonacci.apply(50)); // 12586269025
        System.out.println("Cache size: " + fibonacci.cacheSize()); // 1
        
        System.out.println(fibonacci.apply(50)); // 12586269025 (from cache)
        System.out.println("Cache size: " + fibonacci.cacheSize()); // 1
    }
}
```

## Mini Project: Functional Configuration System

Build a configuration system that uses functional interfaces for transformation, validation, and defaults.

```java
import java.util.*;
import java.util.function.*;

public class ConfigEntry<T> {
    private final String key;
    private final T defaultValue;
    private final Function<String, Optional<T>> parser;
    private final Predicate<T> validator;
    private final Function<T, T> transformer;
    
    private ConfigEntry(Builder<T> builder) {
        this.key = builder.key;
        this.defaultValue = builder.defaultValue;
        this.parser = builder.parser;
        this.validator = builder.validator;
        this.transformer = builder.transformer;
    }
    
    public Optional<T> parse(String rawValue) {
        return parser.apply(rawValue)
            .map(transformer)
            .filter(validator);
    }
    
    public T getValue(Map<String, String> config) {
        return config.containsKey(key)
            ? parse(config.get(key)).orElse(defaultValue)
            : defaultValue;
    }
    
    public static <T> Builder<T> builder(String key) {
        return new Builder<>(key);
    }
    
    public static class Builder<T> {
        private final String key;
        private T defaultValue;
        private Function<String, Optional<T>> parser;
        private Predicate<T> validator = v -> true;
        private Function<T, T> transformer = Function.identity();
        
        Builder(String key) { this.key = key; }
        
        public Builder<T> defaultValue(T val) { this.defaultValue = val; return this; }
        public Builder<T> parser(Function<String, Optional<T>> p) { this.parser = p; return this; }
        public Builder<T> validator(Predicate<T> v) { this.validator = v; return this; }
        public Builder<T> transformer(Function<T, T> t) { this.transformer = t; return this; }
        
        public ConfigEntry<T> build() { return new ConfigEntry<>(this); }
    }
}

class FunctionalConfig {
    private final Map<String, String> rawConfig;
    private final List<String> errors = new ArrayList<>();
    
    public FunctionalConfig(Map<String, String> rawConfig) {
        this.rawConfig = rawConfig;
    }
    
    public <T> T get(ConfigEntry<T> entry) {
        T value = entry.getValue(rawConfig);
        if (value == null) {
            errors.add("Invalid value for key: " + entry.key);
        }
        return value;
    }
    
    public List<String> getErrors() { return errors; }
    
    public static void main(String[] args) {
        Map<String, String> props = Map.of(
            "server.port", "8080",
            "server.host", "localhost",
            "app.debug", "true",
            "app.max.connections", "200"
        );
        
        // Define configuration entries with functional interfaces
        ConfigEntry<Integer> port = ConfigEntry.<Integer>builder("server.port")
            .defaultValue(80)
            .parser(s -> { try { return Optional.of(Integer.parseInt(s)); } catch (Exception e) { return Optional.empty(); } })
            .validator(p -> p > 0 && p < 65535)
            .transformer(p -> p)
            .build();
        
        ConfigEntry<String> host = ConfigEntry.<String>builder("server.host")
            .defaultValue("0.0.0.0")
            .parser(Optional::of)
            .validator(h -> !h.isEmpty())
            .transformer(String::toLowerCase)
            .build();
        
        ConfigEntry<Boolean> debug = ConfigEntry.<Boolean>builder("app.debug")
            .defaultValue(false)
            .parser(s -> Optional.of(Boolean.parseBoolean(s)))
            .validator(Objects::nonNull)
            .build();
        
        ConfigEntry<Integer> maxConn = ConfigEntry.<Integer>builder("app.max.connections")
            .defaultValue(10)
            .parser(s -> { try { return Optional.of(Integer.parseInt(s)); } catch (Exception e) { return Optional.empty(); } })
            .validator(n -> n > 0)
            .transformer(n -> Math.min(n, 1000)) // Cap at 1000
            .build();
        
        FunctionalConfig config = new FunctionalConfig(props);
        
        System.out.println("Port: " + config.get(port));       // 8080
        System.out.println("Host: " + config.get(host));       // localhost
        System.out.println("Debug: " + config.get(debug));     // true
        System.out.println("Max Conn: " + config.get(maxConn)); // 200
        
        if (!config.getErrors().isEmpty()) {
            System.out.println("Errors: " + config.getErrors());
        }
    }
}
```

## Use Cases

| Use Case | Functional Interface | Example |
|----------|---------------------|---------|
| Event handling | `Consumer<T>` | Button click listener |
| Data validation | `Predicate<T>` | Form field validation |
| Data transformation | `Function<T,R>` | Map a stream element |
| Lazy initialization | `Supplier<T>` | Database connection factory |
| Combining values | `BinaryOperator<T>` | Reduce operation |
| Logging | `Consumer<T>` | Debug output |
| Caching | `Function<T,R>` | Memoization wrapper |
| Strategy pattern | Any single-method | Sorting algorithm selection |

### Use Case: Retry Mechanism

```java
import java.util.function.Supplier;
import java.util.function.Predicate;

public class RetryUtil {
    
    public static <T> T retryWithPredicate(
            Supplier<T> action,
            Predicate<T> isSuccess,
            int maxAttempts) {
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            T result = action.get();
            if (isSuccess.test(result)) {
                return result;
            }
            System.out.println("Attempt " + attempt + " failed, retrying...");
        }
        throw new RuntimeException("All " + maxAttempts + " attempts failed");
    }
    
    public static void main(String[] args) {
        String result = retryWithPredicate(
            () -> {
                // Simulate flaky operation
                double random = Math.random();
                return random > 0.7 ? "SUCCESS" : "FAIL";
            },
            "SUCCESS"::equals,
            10
        );
        System.out.println("Got: " + result);
    }
}
```

## Design Patterns

### Strategy Pattern with Functional Interfaces

```java
import java.util.Map;
import java.util.HashMap;
import java.util.function.BinaryOperator;

public class DiscountStrategy {
    
    private final Map<String, BinaryOperator<Double>> strategies = new HashMap<>();
    
    public DiscountStrategy() {
        strategies.put("SUMMER", (price, discount) -> price * (1 - discount / 100));
        strategies.put("BLACK_FRIDAY", (price, discount) -> price - discount);
        strategies.put("STUDENT", (price, discount) -> price * 0.9);
    }
    
    public double applyDiscount(String strategy, double price, double discount) {
        BinaryOperator<Double> calculator = strategies.get(strategy);
        if (calculator == null) {
            throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
        return calculator.apply(price, discount);
    }
    
    public static void main(String[] args) {
        DiscountStrategy ds = new DiscountStrategy();
        System.out.println(ds.applyDiscount("SUMMER", 100.0, 20.0)); // 80.0
        System.out.println(ds.applyDiscount("BLACK_FRIDAY", 100.0, 25.0)); // 75.0
        System.out.println(ds.applyDiscount("STUDENT", 100.0, 0.0)); // 90.0
    }
}
```

### Observer Pattern with Functional Interfaces

```java
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

public class EventBus {
    private final Map<String, List<Consumer<Object>>> subscribers = new HashMap<>();
    
    public <T> void subscribe(String topic, Consumer<T> handler) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>())
            .add(event -> handler.accept((T) event));
    }
    
    public void publish(String topic, Object event) {
        List<Consumer<Object>> handlers = subscribers.getOrDefault(topic, List.of());
        handlers.forEach(h -> h.accept(event));
    }
    
    public static void main(String[] args) {
        EventBus bus = new EventBus();
        
        bus.subscribe("user.created", (String name) -> 
            System.out.println("Welcome email sent to: " + name));
        
        bus.subscribe("user.created", (String name) -> 
            System.out.println("Audit log: user " + name + " created"));
        
        bus.publish("user.created", "Alice");
    }
}
```

## Testing

### Testing Functional Interfaces

```java
import java.util.function.*;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionalInterfaceTest {
    
    static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
    }
    
    static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        return list.stream().map(mapper).collect(Collectors.toList());
    }
    
    static <T> void forEach(List<T> list, Consumer<T> action) {
        list.forEach(action);
    }
    
    // Test helper: create a predicate that tracks invocations
    static <T> Predicate<T> countingPredicate(Predicate<T> delegate, int[] counter) {
        return item -> {
            counter[0]++;
            return delegate.test(item);
        };
    }
    
    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Test predicate
        int[] counter = {0};
        Predicate<Integer> isEven = countingPredicate(n -> n % 2 == 0, counter);
        List<Integer> evens = filter(numbers, isEven);
        assert evens.equals(List.of(2, 4, 6, 8, 10)) : "Even filter failed";
        assert counter[0] == 10 : "Predicate called wrong number of times: " + counter[0];
        
        // Test function
        List<String> strings = map(numbers, n -> "n=" + n);
        assert strings.size() == 10 : "Map size wrong";
        assert strings.get(0).equals("n=1") : "Map transformation wrong";
        
        // Test consumer
        StringBuilder sb = new StringBuilder();
        forEach(numbers.subList(0, 3), n -> sb.append(n).append(","));
        assert sb.toString().equals("1,2,3,") : "Consumer chain failed";
        
        System.out.println("All tests passed!");
    }
}
```

## Performance Optimization

### Object Reuse

```java
import java.util.function.*;

public class PerformanceTips {
    
    // BAD: Creates new lambda on each invocation
    static void badExample() {
        List<String> list = List.of("a", "b", "c");
        for (int i = 0; i < 1000; i++) {
            list.stream().filter(s -> s.length() > 0).count(); // New lambda each time
        }
    }
    
    // GOOD: Reuse lambda instance
    static void goodExample() {
        List<String> list = List.of("a", "b", "c");
        Predicate<String> nonEmpty = s -> !s.isEmpty();
        for (int i = 0; i < 1000; i++) {
            list.stream().filter(nonEmpty).count(); // Reused lambda
        }
    }
    
    // GOOD: Use static final for stateless lambdas
    static final Predicate<String> NON_EMPTY = s -> !s.isEmpty();
    static final Function<String, Integer> TO_UPPER_LEN = s -> s.toUpperCase().length();
    
    public static void main(String[] args) {
        goodExample();
        
        List<String> names = List.of("Alice", "Bob", "Charlie");
        long count = names.stream().filter(NON_EMPTY).count();
        System.out.println("Non-empty count: " + count);
    }
}
```

## Java Version Evolution

| Version | Feature | Functional Interface Impact |
|---------|---------|---------------------------|
| Java 8 | Lambda expressions | Introduction of functional interfaces |
| Java 8 | `java.util.function` | 43 built-in functional interfaces |
| Java 8 | Method references | Shorthand for simple lambdas |
| Java 9 | Private methods in interfaces | Can share code in default methods |
| Java 10 | `var` in lambda params | `@FunctionalInterface` with type inference |
| Java 11 | `String.isBlank()`, `indent()` | Cleaner method references |
| Java 12 | `String.transform()` | Direct Function application |
| Java 14 | Switch expressions | Expression lambdas with switch |
| Java 16 | Records | Concise DTOs for functional pipelines |
| Java 17 | Sealed interfaces | Restricted functional hierarchies |
| Java 21 | Virtual threads | Functional interfaces in concurrency |
| Java 21 | Pattern matching | `switch` with `Function` and `Predicate` |

### Java 21 Example: Pattern Matching with Functional Interfaces

```java
import java.util.function.Function;

public class Java21Features {
    
    static Function<Object, String> describe = obj -> switch (obj) {
        case Integer i -> "Integer: " + i;
        case String s -> "String: \"" + s + "\"";
        case null -> "Null value";
        default -> "Unknown: " + obj.getClass().getSimpleName();
    };
    
    public static void main(String[] args) {
        System.out.println(describe.apply(42));      // Integer: 42
        System.out.println(describe.apply("hello")); // String: "hello"
        System.out.println(describe.apply(null));    // Null value
        System.out.println(describe.apply(3.14));    // Unknown: Double
    }
}
```

## Resources

### Official Documentation
- [Java SE 21 - java.util.function](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/function/package-summary.html)
- [Java Language Specification - Lambda Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.27)

### Books
- *Effective Java* by Joshua Bloch - Chapter on Lambdas and Streams
- *Java Concurrency in Practice* - Thread safety with functional interfaces
- *Modern Java in Action* - Comprehensive lambda/stream coverage

### Online Resources
- [Baeldung - Java 8 Functional Interfaces](https://www.baeldung.com/java-8-functional-interface)
- [Oracle Java Tutorials - Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)

## Glossary

| Term | Definition |
|------|-----------|
| **Functional Interface** | An interface with exactly one abstract method |
| **SAM (Single Abstract Method)** | Synonym for functional interface |
| **Lambda Expression** | Anonymous function that implements a functional interface |
| **Method Reference** | Shorthand syntax (`ClassName::methodName`) for simple lambdas |
| **Composition** | Combining multiple functions into a pipeline |
| **andThen** | Applies this function first, then the next |
| **compose** | Applies the other function first, then this one |
| **Predicate** | Function returning boolean |
| **Consumer** | Function returning void |
| **Supplier** | Function taking no arguments |
| **UnaryOperator** | Function from T to T |
| **BinaryOperator** | Function from (T, T) to T |
| **@FunctionalInterface** | Annotation to verify single abstract method |
| **Effectively Final** | Variable that can be safely captured in a lambda |
| **Type Inference** | Compiler determining types from context |
| **Invokedynamic** | JVM instruction used to implement lambdas |
| **Closure** | Lambda capturing variables from its enclosing scope |
