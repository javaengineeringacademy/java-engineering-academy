# Topic 10: Best Practices (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

│
└─ Is the dataset large and CPU-bound?
   ├─ YES → Consider parallel stream
   └─ NO → Use sequential stream
```

---

## 22. Interview Questions

### Q1: What are the key principles of functional programming in Java?

**Answer**:
1. **Immutability**: Prefer immutable data
2. **Pure Functions**: Avoid side effects
3. **Composition**: Build from small functions
4. **Declarative Style**: Describe what, not how

### Q2: How do you write testable functional code?

**Answer**:
1. Use pure functions without side effects
2. Extract complex lambdas to named methods
3. Use dependency injection for external dependencies
4. Test individual functions independently

### Q3: What are common functional programming pitfalls?

**Answer**:
1. Overly complex lambdas
2. Side effects in streams
3. Mutable variable capture
4. Ignoring performance implications

### Q4: How do you optimize functional code?

**Answer**:
1. Filter before map
2. Use primitive streams
3. Reuse lambda instances
4. Avoid parallel for small datasets

### Q5: When should you use parallel streams?

**Answer**:
1. Large datasets (>10,000 elements)
2. CPU-bound operations
3. Independent element processing
4. Order doesn't matter

---

## 23. Exercises

### Exercise 1: Code Review
Review and improve this code:
```java
list.stream().filter(x -> x != null && x.getName() != null && x.getName().length() > 3).map(x -> x.getName().toUpperCase()).collect(Collectors.toList());
```

### Exercise 2: Lambda Refactoring
Refactor this lambda to be more readable:
```java
list.stream().filter(item -> { if (item == null) return false; if (item.getStatus() == null) return false; return item.getStatus() == Status.ACTIVE; }).toList();
```

### Exercise 3: Performance Optimization
Optimize this stream pipeline:
```java
list.parallelStream().filter(x -> x.getPrice() > 100).map(x -> x.getName().toUpperCase()).collect(Collectors.toList());
```

---

## 24. Assignments

### Assignment 1: Code Standards
Create a functional programming style guide:
1. Lambda formatting rules
2. Stream operation guidelines
3. Naming conventions

### Assignment 2: Refactoring
Refactor imperative code to functional:
1. Convert loops to streams
2. Extract complex lambdas
3. Apply composition

### Assignment 3: Performance Audit
Audit and optimize functional code:
1. Identify bottlenecks
2. Apply best practices
3. Measure improvements

---

## 25. Mini Project

### Project: Functional Programming Toolkit

Build a toolkit of functional programming utilities:

**Requirements:**
1. Utility class for common operations
2. Composition helpers
3. Error handling utilities
4. Performance benchmarks

**Starter Code:**
```java
package academy.javaengineering.functional.bestpractices.project;

import java.util.function.*;

public class FunctionalToolkit {
    
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
    
    public static <T> Function<T, T> peek(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }
    
    // TODO: Implement more utilities
}
```

---

## 26. Summary

Best practices ensure functional code is clean, maintainable, and performant. Key takeaways:

1. **Immutability**: Prefer immutable data
2. **Pure Functions**: Avoid side effects
3. **Composition**: Build from small functions
4. **Declarative Style**: Describe what, not how
5. **Performance**: Filter early, use primitives

### Next Steps
- Topic 11: Mini Project — Apply all concepts
- Continue to advanced functional patterns

---

## 27. References

1. [Effective Java, 3rd Edition - Items 42-44](https://www.oreilly.com/library/view/effective-java/9780134686097/)
2. [Oracle Java Tutorials: Lambda Expressions](https://docs.oracle.com/en/java/javase/21/java/javaOO/lambdaexpressions.html)
3. [Baeldung: Java Functional Programming](https://www.baeldung.com/java-functional-programming)
4. [Java Performance, 2nd Edition](https://www.oreilly.com/library/view/java-performance-2nd/9781492056102/)
```
