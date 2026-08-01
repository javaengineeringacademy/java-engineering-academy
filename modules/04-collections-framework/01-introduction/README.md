# Introduction to Collections Framework

## Introduction

Java Collections Framework provides a unified architecture for representing and manipulating collections of objects. It includes interfaces, implementations, and algorithms.

## Learning Objectives

- Understand the Collection interface hierarchy
- Learn the difference between Collection and Map
- Identify when to use different collection types
- Understand the benefits of the Collections Framework

## Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Basic understanding of arrays

## Why This Matters

Collections are fundamental to Java programming. They provide efficient ways to store, retrieve, and manipulate data, replacing manual array management.

## Syntax

```java
// Collection hierarchy
Collection<E>  (interface)
├── List<E>    (interface)
├── Set<E>     (interface)
└── Queue<E>   (interface)

Map<K,V>      (interface) - separate hierarchy
```

## Examples

```java
// Example 1: Using List
import java.util.List;
import java.util.ArrayList;

List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
System.out.println(names.size());  // 2

// Example 2: Using Set
import java.util.Set;
import java.util.HashSet;

Set<Integer> numbers = new HashSet<>();
numbers.add(1);
numbers.add(1);  // Duplicate ignored
System.out.println(numbers.size());  // 1

// Example 3: Using Map
import java.util.Map;
import java.util.HashMap;

Map<String, Integer> ages = new HashMap<>();
ages.put("Alice", 30);
ages.put("Bob", 25);
System.out.println(ages.get("Alice"));  // 30
```

## Exercises

1. Create a List of your favorite movies and print them.
2. Create a Set of unique numbers and verify duplicates are ignored.
3. Create a Map of country names to capitals and look up a capital.

## Interview Questions

- What is the difference between List, Set, and Map?
- Why do we use interfaces instead of concrete classes?
- What is the difference between Collection and Collections?

## Common Pitfalls

- Confusing Collection and Map hierarchies
- Using raw types without generics
- Choosing the wrong collection for the use case

## Best Practices

- Program to interfaces (List, Set, Map) not implementations
- Use generics for type safety
- Choose the right collection based on your needs

## Real World Applications

- Storing user data in lists
- Tracking unique items with sets
- Creating key-value lookups with maps
- Implementing caches and queues

## References

- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Collections API](https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html)

## Summary

In this topic, you learned the basics of the Java Collections Framework and its main interfaces. Practice with the exercises before diving into specific collection types.
