# Bounded Type Parameters

## Introduction

Bounded type parameters restrict the types that can be used as type arguments, ensuring they have certain capabilities.

## Learning Objectives

- Create upper bounded type parameters
- Understand the extends keyword in generics
- Apply multiple bounds
- Know when to use bounded types

## Prerequisites

- Generic Classes
- Generic Methods
- Interface concepts

## Why This Matters

Bounded types ensure type arguments have specific capabilities, enabling operations like comparison while maintaining type safety.

## Syntax

```java
// Upper bound (extends)
public <T extends Number> void process(T number) {
    // Can use Number methods
}

// Multiple bounds
public <T extends Comparable<T> & Serializable> void process(T item) {
    // Can use Comparable and Serializable methods
}

// Wildcard with bound
public void process(List<? extends Number> list) {
    // Can read Numbers, but can't add
}
```

## Examples

```java
// Example 1: Upper bounded type parameter
public class MathUtils {
    public static <T extends Number> double sum(List<T> list) {
        double total = 0;
        for (T number : list) {
            total += number.doubleValue();
        }
        return total;
    }
}

List<Integer> ints = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);

System.out.println(MathUtils.sum(ints));    // 6.0
System.out.println(MathUtils.sum(doubles)); // 7.5

// Example 2: Multiple bounds
public class Serializer {
    public static <T extends Comparable<T> & Serializable> T findMax(List<T> list) {
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
}

// Example 3: Bounded type in class
public class NumberBox<T extends Number> {
    private T number;

    public NumberBox(T number) {
        this.number = number;
    }

    public double doubleValue() {
        return number.doubleValue();
    }

    public int intValue() {
        return number.intValue();
    }
}

NumberBox<Integer> intBox = new NumberBox<>(42);
NumberBox<Double> doubleBox = new NumberBox<>(3.14);

// NumberBox<String> strBox = new NumberBox<>("Hello");  // Compile error

// Example 4: Recursive bound
public class NaturalNumber<T extends NaturalNumber<T>> {
    private int n;

    public boolean isNaturalNumber() {
        return n > 0;
    }
}
```

## Exercises

1. Create a generic method that finds the maximum in a list of Comparable objects.
2. Write a generic class that only accepts Number subclasses.
3. Create a method that processes only Serializable objects.

## Interview Questions

- What is the difference between extends and super in generics?
- Can you have multiple bounds on a type parameter?
- Why can't you use primitives as type arguments?

## Common Pitfalls

- Using extends when you need super (PECS principle)
- Overcomplicating bounds
- Not understanding type erasure implications

## Best Practices

- Use bounded types to enforce type constraints
- Keep bounds simple and focused
- Document type parameter requirements clearly

## Real World Applications

- Sorting algorithms (requires Comparable)
- Mathematical operations (requires Number)
- Serialization (requires Serializable)
- Collection operations

## References

- [Bounded Type Parameters](https://docs.oracle.com/javase/tutorial/java/generics/bounded.html)
- [Multiple Bounds](https://docs.oracle.com/javase/tutorial/java/generics/bounded.html)

## Summary

In this topic, you learned about bounded type parameters and how to restrict type arguments. Bounded types ensure type safety while enabling specific operations. Practice with the exercises before learning about wildcards.
