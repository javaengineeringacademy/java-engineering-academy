# Generics Best Practices

## Introduction

Following best practices ensures your generic code is type-safe, readable, and maintainable.

## Learning Objectives

- Apply generics best practices
- Avoid common generic anti-patterns
- Design effective generic APIs
- Create reusable generic utilities

## Prerequisites

- All previous generics topics
- Understanding of clean code principles

## Why This Matters

Good generics usage leads to safer, more reusable code. Poor usage can cause confusion and runtime errors.

## Syntax/Principles

```java
// Principle 1: Use meaningful type parameter names
public class Repository<T> { }     // Good
public class Repository<X> { }     // Bad (meaningless)

// Principle 2: Use bounded types when needed
public <T extends Comparable<T>> void sort(List<T> list) { }  // Good
public <T> void sort(List<T> list) { }  // Bad (no constraint)

// Principle 3: Prefer wildcards over type parameters for methods
public void copy(List<? super T> dest, List<? extends T> src) { }  // Good
public <U> void copy(List<T> dest, List<U> src) { }  // Less flexible
```

## Examples

```java
// Example 1: Proper type parameter naming
public class Cache<K, V> {
    private Map<K, V> store = new HashMap<>();

    public void put(K key, V value) {
        store.put(key, value);
    }

    public V get(K key) {
        return store.get(key);
    }
}

// Example 2: Bounded types for flexibility
public class Statistics {
    public static <T extends Number> double average(List<T> numbers) {
        return numbers.stream()
            .mapToDouble(Number::doubleValue)
            .average()
            .orElse(0);
    }
}

// Example 3: Wildcards for API flexibility
public class CollectionsUtil {
    public static <T> List<T> filter(List<? extends T> list,
                                     Predicate<? super T> predicate) {
        return list.stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
}

// Example 4: Avoiding raw types
List<String> good = new ArrayList<>();     // Good
List<String> bad = new ArrayList();        // Bad (raw type)
List raw = new ArrayList<String>();        // Bad (raw type)
```

## Exercises

1. Review the following code and identify issues:
   ```java
   public class Box<T> {
       private Object content;
       public void set(T item) { content = item; }
       public T get() { return (T) content; }
   }
   ```
2. Refactor a class with poor generic usage to follow best practices.
3. Create a generic utility class following best practices.

## Interview Questions

- What are the common generic anti-patterns?
- When should you use bounded types vs unbounded types?
- How do you design a type-safe generic API?

## Common Pitfalls

- Using raw types
- Overusing wildcards
- Not documenting type parameters
- Creating overly complex generic hierarchies

## Best Practices

1. Use meaningful type parameter names (T, E, K, V)
2. Use bounded types when type operations are needed
3. Prefer wildcards for method parameters
4. Document type parameter constraints
5. Avoid raw types
6. Keep generic hierarchies simple
7. Test with multiple types

## Real World Applications

- Framework design
- Library development
- API design
- Utility class creation

## References

- [Effective Java - Generics](https://www.oreilly.com/library/view/effective-java/9780134686097/)
- [Java Generics Best Practices](https://www.baeldung.com/java-generics-best-practices)

## Summary

In this topic, you learned the best practices for using generics in Java. Following these practices leads to safer, more maintainable code. Apply these principles in your projects.
