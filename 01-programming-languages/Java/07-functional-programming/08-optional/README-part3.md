# Topic 08: Optional (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

optional.ifPresent(value -> System.out.println("Found: " + value));
optional.ifPresentOrElse(
    value -> System.out.println("Found: " + value),
    () -> System.out.println("Empty")
);
```

---

## 20. Comparison Table

| Feature | Optional | Null |
|---------|----------|------|
| **Explicit** | Yes | No |
| **Null-safe** | Yes | No |
| **Composable** | Yes | No |
| **Performance** | Overhead | None |
| **Readability** | Good | Verbose |

---

## 21. Decision Tree

```
Should you use Optional?

┌─ Is this a method return type?
│  ├─ YES → Use Optional
│  └─ NO → Continue
│
├─ Is the value potentially absent?
│  ├─ YES → Use Optional
│  └─ NO → Use regular type
│
├─ Is this a class field?
│  ├─ YES → Use null with @Nullable
│  └─ NO → Continue
│
├─ Is this a method parameter?
│  ├─ YES → Use overloading
│  └─ NO → Continue
│
└─ Do you need composable operations?
   ├─ YES → Use Optional
   └─ NO → Use null checks
```

---

## 22. Interview Questions

### Q1: What is Optional and when should you use it?

**Answer**: Optional is a container that may or may not contain a non-null value. Use it for method return types when the value may be absent. Avoid using it for class fields or method parameters.

### Q2: What is the difference between orElse() and orElseGet()?

**Answer**: `orElse()` always evaluates the default value. `orElseGet()` only evaluates the supplier if Optional is empty. Use `orElseGet()` when computing the default is expensive.

### Q3: What is the difference between map() and flatMap()?

**Answer**: `map()` transforms the value and wraps in Optional. `flatMap()` transforms the value to another Optional and unwraps it. Use `flatMap` when the mapping function returns Optional.

### Q4: Can Optional be serialized?

**Answer**: No. Optional is not serializable. This is a deliberate design decision to discourage using Optional for fields or in serialization contexts.

### Q5: How do you use Optional with streams?

**Answer**: Use `Optional::stream` to convert Optional to Stream:
```java
list.stream()
    .map(Item::optionalField)
    .flatMap(Optional::stream)
    .toList();
```

---

## 23. Exercises

### Exercise 1: Basic Optional
Create methods that return Optional:
1. `findUser(int id)` - returns Optional<User>
2. `parseInteger(String s)` - returns Optional<Integer>
3. `divide(int a, int b)` - returns Optional<Integer>

### Exercise 2: Optional Chaining
Chain Optional operations to:
1. Find a user by ID
2. Get their address
3. Get their city
4. Return "Unknown" if any step fails

### Exercise 3: Optional with Streams
Use Optional with streams to:
1. Filter out null values
2. Extract present values
3. Transform present values

---

## 24. Assignments

### Assignment 1: Optional API
Design an API using Optional:
1. Create a service that returns Optional
2. Document when values are absent
3. Handle null inputs gracefully

### Assignment 2: Optional Composition
Implement Optional composition:
1. Combine two Optionals
2. Handle multiple optional values
3. Create a builder for Optional chains

### Assignment 3: Optional Utilities
Create utility methods for Optional:
1. `firstPresent(Optional<T>... optionals)`
2. `allPresent(List<Optional<T>> optionals)`
3. `toStream(Optional<T> optional)`

---

## 25. Mini Project

### Project: Optional-Based Configuration System

Build a configuration system using Optional:

**Requirements:**
1. Type-safe configuration access
2. Default value support
3. Configuration composition
4. Null-safe access

**Starter Code:**
```java
package academy.javaengineering.functional.optional.project;

import java.util.Optional;

public class ConfigSystem {
    
    public static class Config {
        public Optional<String> getString(String key) {
            return Optional.empty();
        }
        
        public Optional<Integer> getInteger(String key) {
            return Optional.empty();
        }
        
        // TODO: Implement configuration access
    }
}
```

---

## 26. Summary

Optional provides explicit, null-safe handling of potentially absent values. Key takeaways:

1. **Use for return types**: When value may be absent
2. **Avoid for fields/parameters**: Use null with documentation
3. **Compose operations**: map, flatMap, filter
4. **Access safely**: orElse, ifPresent, not get()
5. **Not serializable**: Don't use in serialization contexts

### Next Steps
- Topic 09: Composition — Function composition patterns
- Topic 10: Best Practices — Functional programming best practices

---

## 27. References

1. [Oracle Java Tutorials: Optional](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Optional.html)
2. [Java Language Specification: Optional](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html)
3. [Effective Java, 3rd Edition - Item 54](https://www.oreilly.com/library/view/effective-java/9780134686097/)
4. [Baeldung: Java Optional](https://www.baeldung.com/java-optional)
