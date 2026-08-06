# Topic 03: Functional Interfaces (Part 4)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md) | [📖 Back to Part 3](README-part3.md)

---


```
Which functional interface should you use?

┌─ Does the operation return a value?
│  ├─ NO → Consumer<T>
│  └─ YES → Continue
│
├─ Does the operation take no input?
│  ├─ YES → Supplier<T>
│  └─ NO → Continue
│
├─ Does the operation return a boolean?
│  ├─ YES → Predicate<T>
│  └─ NO → Continue
│
├─ Does the operation transform T to T?
│  ├─ YES → UnaryOperator<T>
│  └─ NO → Function<T, R>
│
└─ Does the operation combine two T values?
   ├─ YES → BinaryOperator<T>
   └─ NO → BiFunction<T, U, R>
```

---

## 22. Interview Questions

### Q1: What is a functional interface?

**Answer**: A functional interface is an interface with exactly one abstract method (SAM - Single Abstract Method). It can have any number of default and static methods. The `@FunctionalInterface` annotation provides compile-time validation.

### Q2: What is the difference between Predicate, Function, Consumer, and Supplier?

**Answer**:
- **Predicate<T>**: Takes T, returns boolean (tests a condition)
- **Function<T,R>**: Takes T, returns R (transforms a value)
- **Consumer<T>**: Takes T, returns void (performs an action)
- **Supplier<T>**: Takes nothing, returns T (provides a value)

### Q3: When should you use primitive specialized interfaces?

**Answer**: Use primitive specialized interfaces (IntPredicate, LongConsumer, etc.) when:
1. Working with primitive types in performance-critical code
2. Avoiding boxing overhead in tight loops
3. Processing large datasets with streams

### Q4: Can a functional interface have default methods?

**Answer**: Yes. Functional interfaces can have any number of default and static methods. Only the abstract method count matters for the functional interface definition.

### Q5: How do you compose functional interfaces?

**Answer**: Use the default methods provided:
- **Predicate**: `and()`, `or()`, `negate()`
- **Function**: `andThen()`, `compose()`
- **Consumer**: `andThen()`
- **BiFunction**: `andThen()`

---

## 23. Exercises

### Exercise 1: Functional Interface Creation
Create a `@FunctionalInterface` called `Validator<T>` that:
1. Has a method `boolean validate(T value)`
2. Has a default method `and(Validator<T> other)`
3. Has a default method `or(Validator<T> other)`
4. Has a static method `not(Validator<T> validator)`

### Exercise 2: Predicate Composition
Using `Predicate<String>`, create predicates to:
1. Check if a string is longer than 5 characters
2. Check if a string contains the letter 'a'
3. Combine them to find strings that are long AND contain 'a'
4. Negate to find strings that are NOT long AND contain 'a'

### Exercise 3: Function Pipeline
Build a `Function<String, String>` pipeline that:
1. Trims whitespace
2. Converts to lowercase
3. Replaces spaces with underscores
4. Removes special characters

---

## 24. Assignments

### Assignment 1: Custom Functional Interface Library
Create a library of custom functional interfaces:
1. `TryCatch<T, R>` - Function that handles exceptions
2. `Retry<T, R>` - Function that retries on failure
3. `Cache<T, R>` - Function with memoization
4. Each should have appropriate default methods

### Assignment 2: Type-Safe Configuration
Build a type-safe configuration system:
1. Use `Supplier<T>` for lazy configuration loading
2. Use `Function<String, T>` for type conversion
3. Use `Predicate<T>` for validation
4. Support configuration composition

### Assignment 3: Event System
Design an event system using functional interfaces:
1. `EventListener<T>` for handling events
2. `EventFilter<T>` for filtering events
3. `EventTransformer<T, R>` for changing events
4. Support event chaining and composition

---

## 25. Mini Project

### Project: Functional Interface Toolkit

Build a detailed toolkit of functional interfaces:

**Requirements:**
1. Create custom interfaces: `TryCatch`, `Retry`, `Cache`, `Validator`
2. Implement composition methods for each
3. Build a pipeline builder using these interfaces
4. Include performance benchmarks
5. Provide documentation for when to use each interface

**Starter Code:**
```java
package academy.javaengineering.functional.interfaces.project;

import java.util.function.*;

public class FunctionalToolkit {
    
    @FunctionalInterface
    public interface TryCatch<T, R> {
        R apply(T input) throws Exception;
        
        default Function<T, R> orElse(R defaultValue) { ... }
        default Function<T, R> orElseGet(Supplier<R> defaultSupplier) { ... }
    }
    
    @FunctionalInterface
    public interface Retry<T, R> {
        R apply(T input);
        
        static <T, R> Retry<T, R> of(TryCatch<T, R> tryCatch, int maxRetries) { ... }
    }
    
    // TODO: Implement Cache, Validator, and Pipeline
}
```

---

## 26. Summary

Functional interfaces are the foundation of Java's functional programming support. Key takeaways:

1. **SAM Rule**: Exactly one abstract method
2. **@FunctionalInterface**: Optional annotation for validation
3. **Built-in Catalog**: Predicate, Function, Consumer, Supplier, UnaryOperator, BinaryOperator
4. **Primitive Specialization**: IntPredicate, LongConsumer, etc. for performance
5. **Composition**: Default methods enable function composition

### Next Steps
- Topic 04: Method References — Simplifying lambda syntax
- Topic 05: Stream API — Declarative data processing

---

## 27. References

1. [Oracle Java Tutorials: Functional Interfaces](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/function/package-summary.html)
2. [Java Language Specification: Functional Interfaces](https://docs.oracle.com/javase/specs/jls/se21/html/jls-9.html#jls-9.8)
3. [Effective Java, 3rd Edition - Item 42](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Functional Interfaces](https://www.baeldung.com/java-functional-interface)
5. [Java 21 API Documentation](https://docs.oracle.com/en/java/javase/21/docs/api/)
