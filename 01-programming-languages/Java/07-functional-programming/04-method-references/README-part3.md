# Topic 04: Method References (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

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
