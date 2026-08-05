# Topic 04: Method References

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

Method references are shorthand notations for lambda expressions that simply call an existing method. They provide a more readable and concise way to express simple lambdas, making code easier to understand and maintain.

There are four types of method references in Java:

1. **Reference to a static method**: `ClassName::staticMethod`
2. **Reference to an instance method of a particular object**: `object::instanceMethod`
3. **Reference to an instance method of an arbitrary object of a particular type**: `ClassName::instanceMethod`
4. **Reference to a constructor**: `ClassName::new`

### When to Use Method References

Method references are appropriate when:
- A lambda simply calls an existing method
- The lambda has no additional logic beyond the method call
- The method reference improves readability

---

## 2. Learning Objectives

After completing this topic, you will be able to:

1. Identify the four types of method references
2. Convert lambda expressions to method references
3. Understand when to use method references vs lambdas
4. Apply method references in stream operations
5. Use constructor references for object creation
6. Recognize the performance benefits of method references

---

## 3. Prerequisites

Before starting this topic, you should be comfortable with:

- **Lambda Expressions**: Basic syntax and usage (Topic 02)
- **Functional Interfaces**: Built-in interfaces (Topic 03)
- **Stream API**: Basic stream operations (Topic 05)

---

## 4. Why This Concept Exists

### The Problem with Simple Lambdas

Many lambda expressions simply delegate to an existing method:

```java
// Lambda that just calls an existing method
Function<String, Integer> toLength = s -> s.length();
Predicate<String> isEmpty = s -> s.isEmpty();
Consumer<String> printer = s -> System.out.println(s);
```

These lambdas add unnecessary verbosity when the method being called is obvious from context.

### The Method Reference Solution

Method references provide a more concise syntax:

```java
// Method references
Function<String, Integer> toLength = String::length;
Predicate<String> isEmpty = String::isEmpty;
Consumer<String> printer = System.out::println;
```

---

## 5. Problem Statement

### Real-World Scenario: Code Readability

A codebase contains many simple lambda expressions that just delegate to existing methods. This creates:

- **Noise**: The lambda syntax obscures the method being called
- **Verbosity**: More characters than necessary
- **Reduced readability**: Harder to scan and understand

### Requirements

1. Provide a concise syntax for method delegation
2. Maintain type safety
3. Support all method invocation patterns
4. Enable better code readability

---

## 6. Theory

### 6.1 Four Types of Method References

#### 1. Static Method Reference

```java
// Lambda
Function<String, Integer> parseInt = s -> Integer.parseInt(s);

// Method reference
Function<String, Integer> parseInt = Integer::parseInt;
```

#### 2. Instance Method of Particular Object

```java
// Lambda
String prefix = "Mr. ";
Function<String, String> addPrefix = s -> prefix.concat(s);

// Method reference
Function<String, String> addPrefix = prefix::concat;
```

#### 3. Instance Method of Arbitrary Object

```java
// Lambda
Function<String, Integer> toLength = s -> s.length();

// Method reference
Function<String, Integer> toLength = String::length;
```

#### 4. Constructor Reference

```java
// Lambda
Supplier<List<String>> listFactory = () -> new ArrayList<>();

// Method reference
Supplier<List<String>> listFactory = ArrayList::new;
```

### 6.2 Type Inference with Method References

Method references use the same type inference as lambdas:

```java
// Types inferred from context
Function<String, Integer> toLength = String::length;
// String::length → Function<String, Integer>

// Explicit types when needed
UnaryOperator<String> toUpper = String::toUpperCase;
// String::toUpperCase → UnaryOperator<String>
```

### 6.3 Method Reference vs Lambda

```java
// Equivalent forms
Function<String, Integer> f1 = s -> s.length();
Function<String, Integer> f2 = String::length;

Consumer<String> c1 = s -> System.out.println(s);
Consumer<String> c2 = System.out::println;

Supplier<List<String>> s1 = () -> new ArrayList<>();
Supplier<List<String>> s2 = ArrayList::new;
```

---

## 7. Internal Working

### 7.1 Compilation Process

Method references are compiled similarly to lambdas:

1. The compiler identifies the target functional interface
2. Resolves the method reference to a specific method
3. Generates `invokedynamic` bytecode
4. Uses `LambdaMetafactory` at runtime

### 7.2 Method Resolution

The compiler resolves method references by:

1. Matching the functional interface's SAM signature
2. Finding the method with matching name and parameters
3. Validating compatibility

```
Method Reference → Compiler → Method Resolution → InvokeDynamic → LambdaMetafactory → Implementation
```

### 7.3 Overloaded Method Resolution

When a method name is overloaded, the compiler uses the functional interface's SAM to disambiguate:

```java
// Integer.parseInt(String) vs Integer.parseInt(String, int)
Function<String, Integer> parseInt = Integer::parseInt;  // Resolves to parseInt(String)
IntFunction<Integer> parseRadix = s -> Integer.parseInt(s, 10);  // Different signature
```

---

## 8. JVM Perspective

### 8.1 Bytecode Generation

Method references generate similar bytecode to lambdas:

```
Method Reference: String::length
↓
Synthetic method: static int lambda$main$0(String s) { return s.length(); }
↓
InvokeDynamic call site
```

### 8.2 Performance

Method references are often slightly faster than equivalent lambdas because:

1. The compiler can optimize the direct method call
2. No lambda body compilation needed
3. Better JIT inlining opportunities

---

## 9. Memory Representation

### 9.1 Memory Footprint

Method references have minimal memory overhead:

```
Method Reference Object:
┌─────────────────────────────────────┐
│  Header (mark word + klass pointer) │
├─────────────────────────────────────┤
│  Method handle to target method     │
└─────────────────────────────────────┘
```

### 9.2 Comparison

| Implementation | Memory | Separate Class | Performance |
|----------------|--------|----------------|-------------|
| Method Reference | ~12 bytes | No | Excellent |
| Lambda | ~16 bytes | No | Excellent |
| Anonymous Class | ~40 bytes | Yes | Good |

---

## 10. Syntax

### 10.1 Static Method Reference

```java
// Syntax: ClassName::staticMethod
Function<String, Integer> parseInt = Integer::parseInt;
UnaryOperator<String> toLower = String::toLowerCase;
BinaryOperator<Integer> add = Integer::sum;
```

### 10.2 Instance Method of Particular Object

```java
// Syntax: object::instanceMethod
String prefix = "Mr. ";
Function<String, String> addPrefix = prefix::concat;

StringBuilder builder = new StringBuilder();
Supplier<String> buildString = builder::toString;
```

### 10.3 Instance Method of Arbitrary Object

```java
// Syntax: ClassName::instanceMethod
Function<String, Integer> toLength = String::length;
Predicate<String> isEmpty = String::isEmpty;
UnaryOperator<String> toUpper = String::toUpperCase;
```

### 10.4 Constructor Reference

```java
// Syntax: ClassName::new
Supplier<List<String>> listFactory = ArrayList::new;
Function<String, StringBuilder> toBuilder = StringBuilder::new;
Function<Integer, int[]> arrayFactory = int[]::new;
```

---

## 11. Easy Example

### Example 1: Basic Method References

```java
package academy.javaengineering.functional.references;

import java.util.Arrays;
import java.util.List;
import java.util.function.*;

public class BasicMethodReferences {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("alice", "bob", "charlie", "diana");
        
        // Lambda
        List<String> upperLambda = names.stream()
            .map(name -> name.toUpperCase())
            .toList();
        
        // Method reference
        List<String> upperRef = names.stream()
            .map(String::toUpperCase)
            .toList();
        
        System.out.println("Lambda: " + upperLambda);
        System.out.println("Reference: " + upperRef);
        
        // Print with lambda
        names.forEach(name -> System.out.println(name));
        
        // Print with method reference
        names.forEach(System.out::println);
    }
}
```

### Example 2: Constructor References

```java
package academy.javaengineering.functional.references;

import java.util.function.*;

public class ConstructorReferences {
    public static void main(String args[]) {
        // Lambda
        Supplier<StringBuilder> lambdaBuilder = () -> new StringBuilder();
        
        // Method reference
        Supplier<StringBuilder> refBuilder = StringBuilder::new;
        
        StringBuilder sb1 = lambdaBuilder.get();
        StringBuilder sb2 = refBuilder.get();
        
        sb1.append("Lambda");
        sb2.append("Reference");
        
        System.out.println("Lambda: " + sb1);
        System.out.println("Reference: " + sb2);
        
        // Array constructor reference
        Function<Integer, int[]> arrayFactory = int[]::new;
        int[] array = arrayFactory.apply(5);
        System.out.println("Array length: " + array.length);
    }
}
```

---

## 12. Medium Example

### Example 1: Method References in Stream Pipelines

```java
package academy.javaengineering.functional.references;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamMethodReferences {
    
    record Person(String name, int age, String city) {}
    
    public static void main(String[] args) {
        List<Person> people = Arrays.asList(
            new Person("Alice", 25, "New York"),
            new Person("Bob", 30, "London"),
            new Person("Charlie", 35, "New York"),
            new Person("Diana", 28, "Paris")
        );
        
        // Extract names using method reference
        List<String> names = people.stream()
            .map(Person::name)
            .toList();
        System.out.println("Names: " + names);
        
        // Filter by age using method reference
        List<Person> over30 = people.stream()
            .filter(p -> p.age() > 30)
            .toList();
        System.out.println("Over 30: " + over30);
        
        // Sort by name using method reference
        List<String> sortedNames = names.stream()
            .sorted(String::compareToIgnoreCase)
            .toList();
        System.out.println("Sorted: " + sortedNames);
        
        // Group by city using method reference
        var grouped = people.stream()
            .collect(Collectors.groupingBy(Person::city));
        System.out.println("Grouped by city: " + grouped);
    }
}
```

### Example 2: Method Reference Composition

```java
package academy.javaengineering.functional.references;

import java.util.function.*;

public class MethodReferenceComposition {
    
    public static <T, R, V> Function<T, V> compose(
            Function<T, R> first, 
            Function<R, V> second) {
        return first.andThen(second);
    }
    
    public static void main(String[] args) {
        // Compose method references
        Function<String, Integer> toLength = String::length;
        Function<Integer, String> toString = Object::toString;
        
        Function<String, String> pipeline = compose(toLength, toString);
        System.out.println("Pipeline 'hello': " + pipeline.apply("hello"));
        
        // More complex composition
        Function<String, String> trim = String::trim;
        Function<String, String> toLower = String::toLowerCase;
        Function<String, Integer> length = String::length;
        
        Function<String, Integer> processAndMeasure = trim
            .andThen(toLower)
            .andThen(length);
        
        System.out.println("Process and measure '  Hello  ': " + 
            processAndMeasure.apply("  Hello  "));
    }
}
```

---

## 13. Hard Example

### Example 1: Generic Method Reference Framework

```java
package academy.javaengineering.functional.references;

import java.util.function.*;

public class MethodReferenceFramework {
    
    @FunctionalInterface
    public interface ReferenceFunction<T, R> {
        R apply(T t);
        
        default <V> ReferenceFunction<T, V> andThen(ReferenceFunction<R, V> after) {
            return t -> after.apply(this.apply(t));
        }
        
        static <T> ReferenceFunction<T, T> identity() {
            return t -> t;
        }
    }
    
    @FunctionalInterface
    public interface ReferencePredicate<T> {
        boolean test(T t);
        
        default ReferencePredicate<T> and(ReferencePredicate<? super T> other) {
            return t -> this.test(t) && other.test(t);
        }
        
        default ReferencePredicate<T> or(ReferencePredicate<? super T> other) {
            return t -> this.test(t) || other.test(t);
        }
        
        default ReferencePredicate<T> negate() {
            return t -> !this.test(t);
        }
    }
    
    public static class MethodReferenceBuilder<I, O> {
        private final ReferenceFunction<I, O> function;
        
        private MethodReferenceBuilder(ReferenceFunction<I, O> function) {
            this.function = function;
        }
        
        public static <T> MethodReferenceBuilder<T, T> of(ReferenceFunction<T, T> function) {
            return new MethodReferenceBuilder<>(function);
        }
        
        public <R> MethodReferenceBuilder<I, R> andThen(ReferenceFunction<O, R> after) {
            return new MethodReferenceBuilder<>(function.andThen(after));
        }
        
        public O apply(I input) {
            return function.apply(input);
        }
    }
    
    public static void main(String[] args) {
        // Build a string processing pipeline using method references
        MethodReferenceBuilder<String, String> pipeline = 
            MethodReferenceBuilder.of(s -> s)
                .andThen(String::trim)
                .andThen(String::toLowerCase)
                .andThen(s -> s.replaceAll("[^a-z0-9\\s]", ""))
                .andThen(s -> s.replaceAll("\\s+", "_"));
        
        System.out.println("Pipeline: " + pipeline.apply("  Hello, World!  "));
        
        // Build a validation pipeline using method references
        ReferencePredicate<String> isNotEmpty = s -> !s.isEmpty();
        ReferencePredicate<String> hasMinLength = s -> s.length() >= 3;
        ReferencePredicate<String> isAlphanumeric = s -> s.matches("[a-zA-Z0-9]+");
        
        ReferencePredicate<String> isValid = isNotEmpty
            .and(hasMinLength)
            .and(isAlphanumeric);
        
        System.out.println("Is 'abc' valid? " + isValid.test("abc"));
        System.out.println("Is 'ab' valid? " + isValid.test("ab"));
        System.out.println("Is 'abc!' valid? " + isValid.test("abc!"));
    }
}
```

---

## 14. Enterprise Example

### Example 1: Data Access Layer with Method References

```java
package academy.javaengineering.functional.references;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class DataAccessLayer {
    
    @FunctionalInterface
    public interface EntityMapper<T, R> {
        R map(T entity);
        
        default <V> EntityMapper<T, V> andThen(EntityMapper<R, V> after) {
            return entity -> after.map(this.map(entity));
        }
    }
    
    @FunctionalInterface
    public interface EntityPredicate<T> {
        boolean test(T entity);
        
        default EntityPredicate<T> and(EntityPredicate<? super T> other) {
            return entity -> this.test(entity) && other.test(entity);
        }
    }
    
    public record User(String id, String name, String email, boolean active) {}
    
    public record UserDTO(String displayName, String contactInfo) {}
    
    public static class UserRepository {
        private final List<User> users;
        
        public UserRepository(List<User> users) {
            this.users = List.copyOf(users);
        }
        
        public <R> List<R> findAllMapped(EntityMapper<User, R> mapper) {
            return users.stream()
                .map(mapper::map)
                .toList();
        }
        
        public <R> List<R> findFilteredMapped(
                EntityPredicate<User> predicate, 
                EntityMapper<User, R> mapper) {
            return users.stream()
                .filter(predicate::test)
                .map(mapper::map)
                .toList();
        }
    }
    
    public static void main(String[] args) {
        // Create test data
        List<User> users = List.of(
            new User("U001", "Alice", "alice@example.com", true),
            new User("U002", "Bob", "bob@example.com", true),
            new User("U003", "Charlie", "charlie@example.com", false),
            new User("U004", "Diana", "diana@example.com", true)
        );
        
        UserRepository repo = new UserRepository(users);
        
        // Define mappers using method references
        EntityMapper<User, String> toName = User::name;
        EntityMapper<User, String> toEmail = User::email;
        EntityMapper<User, UserDTO> toDTO = user -> 
            new UserDTO(user.name(), user.email());
        
        // Define predicates using method references
        EntityPredicate<User> isActive = User::active;
        EntityPredicate<User> isNotActive = isActive.negate();
        
        // Use method references in queries
        List<String> activeNames = repo.findFilteredMapped(isActive, toName);
        System.out.println("Active users: " + activeNames);
        
        List<String> allEmails = repo.findAllMapped(toEmail);
        System.out.println("All emails: " + allEmails);
        
        List<UserDTO> activeDTOs = repo.findFilteredMapped(isActive, toDTO);
        System.out.println("Active DTOs: " + activeDTOs);
    }
}
```

---

## 15. Performance

### 15.1 Method Reference vs Lambda Performance

| Aspect | Method Reference | Lambda | Difference |
|--------|------------------|--------|------------|
| **Compilation** | Direct resolution | Body compilation | Faster |
| **Memory** | ~12 bytes | ~16 bytes | 25% less |
| **JIT Optimization** | Excellent | Excellent | Slightly better |
| **Readability** | Excellent | Good | Better |

### 15.2 When Method References Are Faster

Method references are faster when:
1. The method call is the only operation
2. The method is well-known and can be inlined
3. The JIT compiler can optimize the direct call

---

## 16. Best Practices

1. **Use method references when lambdas just call existing methods**
2. **Prefer instance method references over lambdas for readability**
3. **Use constructor references for object creation**
4. **Avoid method references when additional logic is needed**
5. **Document complex method references with comments**

---

## 17. Common Mistakes

### Mistake 1: Using Method References for Complex Logic

```java
// WRONG: Method reference can't express additional logic
list.stream()
    .map(s -> s.toUpperCase().trim())  // Lambda needed
    .toList();

// CORRECT: Use lambda for complex logic
list.stream()
    .map(s -> s.toUpperCase().trim())
    .toList();
```

### Mistake 2: Confusing Instance Method References

```java
// WRONG: Incorrect syntax
Function<String, Integer> toLength = String::length;  // OK
// Function<String, Integer> toLength = "hello"::length;  // Different object

// CORRECT: Understand the difference
Function<String, Integer> toLength1 = String::length;  // Arbitrary object
Supplier<Integer> toLength2 = "hello"::length;  // Particular object
```

---

## 18. Pitfalls

1. **Overloaded methods**: May require explicit lambda when methods are overloaded
2. **Complex logic**: Method references can't express additional logic
3. **Readability**: Sometimes lambdas are clearer for simple operations

---

## 19. Debugging Tips

### 1. Use Named Methods for Complex Logic

```java
// Instead of complex method reference chain
list.stream()
    .map(s -> s.toUpperCase().trim().replaceAll("\\s+", "_"))
    .toList();

// Extract to named method
Function<String, String> processString = this::processString;
list.stream().map(processString).toList();
```

### 2. Add Debug Logging

```java
list.stream()
    .map(s -> {
        System.out.println("Processing: " + s);
        return s.toUpperCase();
    })
    .toList();
```

---

## 20. Comparison Table

| Feature | Lambda | Method Reference | Anonymous Class |
|---------|--------|------------------|-----------------|
| **Syntax** | `s -> s.length()` | `String::length` | `new Func() { ... }` |
| **Readability** | Good | Excellent | Verbose |
| **Flexibility** | High | Limited | High |
| **Performance** | Excellent | Excellent | Good |
| **Use Case** | Complex logic | Simple delegation | Multiple methods |

---

## 21. Decision Tree

```
Should you use a method reference?

┌─ Does the lambda just call an existing method?
│  ├─ YES → Use method reference
│  │        ├─ Is it a static method?
│  │        │  └─ ClassName::staticMethod
│  │        ├─ Is it an instance method of a particular object?
│  │        │  └─ object::instanceMethod
│  │        ├─ Is it an instance method of an arbitrary object?
│  │        │  └─ ClassName::instanceMethod
│  │        └─ Is it a constructor?
│  │           └─ ClassName::new
│  └─ NO → Use lambda
│
└─ Is the method overloaded?
   ├─ YES → May need explicit lambda
   └─ NO → Use method reference
```

---

## 22. Interview Questions

### Q1: What are the four types of method references?

**Answer**:
1. **Static method**: `ClassName::staticMethod`
2. **Instance method of particular object**: `object::instanceMethod`
3. **Instance method of arbitrary object**: `ClassName::instanceMethod`
4. **Constructor**: `ClassName::new`

### Q2: When should you use method references over lambdas?

**Answer**: Use method references when a lambda simply delegates to an existing method. They improve readability and can be slightly faster due to direct method resolution.

### Q3: Can method references be used with overloaded methods?

**Answer**: Yes, the compiler uses the functional interface's SAM signature to disambiguate overloaded methods. If ambiguity remains, use an explicit lambda.

### Q4: What is the difference between `String::length` and `"hello"::length`?

**Answer**: `String::length` is a reference to an instance method of an arbitrary String, while `"hello"::length` is a reference to an instance method of a particular String object.

### Q5: Can method references throw exceptions?

**Answer**: Method references can throw exceptions if the underlying method throws exceptions. The functional interface's SAM must declare the exception or it must be unchecked.

---

## 23. Exercises

### Exercise 1: Convert Lambdas to Method References
Convert these lambdas to method references:

```java
Function<String, Integer> f1 = s -> s.length();
Predicate<String> p1 = s -> s.isEmpty();
Consumer<String> c1 = s -> System.out.println(s);
Supplier<List<String>> s1 = () -> new ArrayList<>();
UnaryOperator<String> u1 = s -> s.toUpperCase();
```

### Exercise 2: Method Reference Composition
Build a pipeline using method references:
1. Trim a string
2. Convert to lowercase
3. Replace spaces with underscores
4. Get the length

### Exercise 3: Constructor References
Use constructor references to create:
1. A factory for StringBuilder
2. A factory for LinkedList<String>
3. A factory for int[] arrays

---

## 24. Assignments

### Assignment 1: Method Reference Library
Create a utility class with method references for:
1. String operations (trim, toUpper, toLower, length)
2. Number operations (parseInt, parseDouble, valueOf)
3. Collection operations (of, copyOf, unmodifiable)

### Assignment 2: Data Processing Pipeline
Build a data processing pipeline using method references:
1. Read data from a source
2. Transform using method references
3. Filter using method references
4. Output results

### Assignment 3: Factory Pattern with Constructor References
Implement a factory pattern using constructor references:
1. Create a generic factory interface
2. Implement factories for different types
3. Use method references for factory methods

---

## 25. Mini Project

### Project: Method Reference Utility Library

Build a comprehensive utility library using method references:

**Requirements:**
1. Create utility classes for common operations
2. Use method references wherever possible
3. Provide composition methods
4. Include performance benchmarks

**Starter Code:**
```java
package academy.javaengineering.functional.references.project;

import java.util.function.*;

public class MethodReferenceUtils {
    
    // String utilities
    public static final Function<String, String> TRIM = String::trim;
    public static final Function<String, String> TO_LOWER = String::toLowerCase;
    public static final Function<String, String> TO_UPPER = String::toUpperCase;
    public static final Function<String, Integer> LENGTH = String::length;
    
    // Number utilities
    public static final Function<String, Integer> PARSE_INT = Integer::parseInt;
    public static final Function<String, Double> PARSE_DOUBLE = Double::parseDouble;
    
    // TODO: Add more utilities
}
```

---

## 26. Summary

Method references provide a concise syntax for lambda expressions that simply call existing methods. Key takeaways:

1. **Four types**: Static, instance of particular object, instance of arbitrary object, constructor
2. **Readability**: More readable than equivalent lambdas
3. **Performance**: Slightly faster due to direct method resolution
4. **Use cases**: Simple method delegation
5. **Limitations**: Can't express additional logic

### Next Steps
- Topic 05: Stream API — Declarative data processing
- Topic 06: Stream Operations — Advanced stream operations

---

## 27. References

1. [Oracle Java Tutorials: Method References](https://docs.oracle.com/en/java/javase/21/java/javaOO/methodreferences.html)
2. [Java Language Specification: Method Reference Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.13)
3. [Effective Java, 3rd Edition - Item 42](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Method References](https://www.baeldung.com/java-method-references)
