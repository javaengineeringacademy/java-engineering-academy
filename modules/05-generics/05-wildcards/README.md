# Wildcards

## Introduction

Wildcards (?) represent unknown types in generic code, enabling flexible method signatures and covariance/contravariance.

## Learning Objectives

- Use unbounded wildcards
- Apply upper bounded wildcards (extends)
- Apply lower bounded wildcards (super)
- Understand the PECS principle

## Prerequisites

- Bounded Type Parameters
- Generic Classes
- Generic Methods

## Why This Matters

Wildcards enable flexible API design by accepting wider ranges of types while maintaining type safety.

## Syntax

```java
// Unbounded wildcard
public void process(List<?> list) {
    // Can read Objects, can't add (except null)
}

// Upper bounded wildcard (covariance)
public double sum(List<? extends Number> list) {
    // Can read Numbers, can't add
}

// Lower bounded wildcard (contravariance)
public void addNumbers(List<? super Integer> list) {
    // Can add Integers, can read Objects
}

// PECS: Producer Extends, Consumer Super
public void copy(List<? super T> dest, List<? extends T> src) {
    // src produces T, dest consumes T
}
```

## Examples

```java
// Example 1: Unbounded wildcard
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

List<String> strings = Arrays.asList("A", "B", "C");
List<Integer> numbers = Arrays.asList(1, 2, 3);

printList(strings);  // Works
printList(numbers);  // Works

// Example 2: Upper bounded wildcard (extends)
public static double sum(List<? extends Number> list) {
    double total = 0;
    for (Number num : list) {
        total += num.doubleValue();
    }
    return total;
}

List<Integer> ints = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);

System.out.println(sum(ints));    // 6.0
System.out.println(sum(doubles)); // 7.5

// Example 3: Lower bounded wildcard (super)
public static void addIntegers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

List<Number> numbers = new ArrayList<>();
List<Object> objects = new ArrayList<>();

addIntegers(numbers);  // Works
addIntegers(objects);  // Works

// Example 4: PECS principle
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {
        dest.add(item);
    }
}

List<Number> dest = new ArrayList<>();
List<Integer> src = Arrays.asList(1, 2, 3);

copy(dest, src);  // dest consumes, src produces
```

## Exercises

1. Write a method that finds the maximum in a list using wildcards.
2. Create a method that copies elements between lists with different type parameters.
3. Implement a method that prints all elements in a collection of any type.

## Interview Questions

- What is the difference between List<?> and List<Object>?
- When should you use extends vs super?
- What is the PECS principle?

## Common Pitfalls

- Using List<?> when you need to modify the list
- Confusing extends and super bounds
- Not understanding type inference with wildcards

## Best Practices

- Use extends for producers (reading data)
- Use super for consumers (writing data)
- Keep wildcard usage simple and clear

## Real World Applications

- Flexible API design
- Collection copying methods
- Utility methods that work with multiple types
- Covariant return types

## References

- [Wildcards Tutorial](https://docs.oracle.com/javase/tutorial/java/generics/wildcards.html)
- [PECS Principle](https://www.oracle.com/technetwork/articles/java/peec-140465.html)

## Summary

In this topic, you learned about wildcards and the PECS principle. Wildcards enable flexible, type-safe API design. Practice with the exercises before learning about type erasure.
