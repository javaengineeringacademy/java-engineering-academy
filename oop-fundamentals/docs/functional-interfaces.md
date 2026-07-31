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
