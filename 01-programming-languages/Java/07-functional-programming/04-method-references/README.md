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


---

[📖 Continue to Part 2](README-part2.md)
