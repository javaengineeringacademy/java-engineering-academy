# Topic 09: Function Composition (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


### Assignment 2: Data Pipeline
Build a data pipeline using composition:
1. Read data from source
2. Transform through multiple steps
3. Filter invalid records
4. Output to destination

### Assignment 3: Composition Framework
Design a composition framework:
1. Support lazy composition
2. Enable parallel composition
3. Provide debugging support

---

## 25. Mini Project

### Project: Function Composition Engine

Build a composition engine for data processing:

**Requirements:**
1. Support function composition with andThen/compose
2. Enable predicate composition
3. Provide pipeline builder
4. Support lazy evaluation

**Starter Code:**
```java
package academy.javaengineering.functional.composition.project;

import java.util.function.*;

public class CompositionEngine {
    
    public static <T> Function<T, T> compose(Function<T, T>... functions) {
        Function<T, T> result = Function.identity();
        for (Function<T, T> f : functions) {
            result = result.andThen(f);
        }
        return result;
    }
    
    // TODO: Implement more composition utilities
}
```

---

## 26. Summary

Function composition enables building complex transformations from simple functions. Key takeaways:

1. **andThen**: Apply this first, then argument
2. **compose**: Apply argument first, then this
3. **Predicate composition**: and, or, negate
4. **Cache composed functions**: Store in static final fields
5. **Keep chains short**: 3-5 functions maximum

### Next Steps
- Topic 10: Best Practices — Functional programming best practices
- Topic 11: Mini Project — Apply all concepts

---

## 27. References

1. [Oracle Java Tutorials: Function Composition](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/function/package-summary.html)
2. [Java Language Specification: Function Interface](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 42](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Function Composition](https://www.baeldung.com/java-function-composition)
