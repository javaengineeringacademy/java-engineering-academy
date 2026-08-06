# Topic 02: Lambda Expressions (Part 4)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md) | [📖 Back to Part 3](README-part3.md)

---

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

1. [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
2. [Java Language Specification: Lambda Expressions](https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html#jls-15.27)
3. [Effective Java, 3rd Edition - Item 42](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Lambda Expressions](https://www.baeldung.com/java-lambda-expressions)
5. [Java Performance, 2nd Edition](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/)
