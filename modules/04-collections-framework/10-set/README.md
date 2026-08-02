# Set Interface

## Introduction

Set is a collection that contains no duplicate elements. It models the mathematical set abstraction.

## Learning Objectives

- Understand the Set interface and its properties
- Learn about element uniqueness
- Understand Set implementations
- Know when to use Set vs List

## Prerequisites

- Introduction to Collections Framework
- List Interface
- equals() and hashCode() methods

## Why This Matters

Sets are essential for operations requiring unique elements, such as membership testing, removing duplicates, and set operations (union, intersection).

## Syntax

```java
// Set interface methods
Set<E> set = new HashSet<>();
set.add(element);        // Add element (returns false if duplicate)
set.remove(element);     // Remove element
set.contains(element);   // Check existence
set.size();              // Get size
set.isEmpty();           // Check if empty

// Set operations
set.addAll(collection);      // Union
set.retainAll(collection);   // Intersection
set.removeAll(collection);   // Difference
```

## Examples

```java
// Example 1: Basic Set operations
Set<String> names = new HashSet<>();
names.add("Alice");
names.add("Bob");
names.add("Alice");  // Duplicate ignored

System.out.println(names.size());      // 2
System.out.println(names.contains("Alice"));  // true

// Example 2: Removing duplicates from List
List<Integer> numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3);
Set<Integer> uniqueNumbers = new HashSet<>(numbersWithDuplicates);
List<Integer> numbersWithoutDuplicates = new ArrayList<>(uniqueNumbers);

// Example 3: Set operations
Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));

// Union
Set<Integer> union = new HashSet<>(set1);
union.addAll(set2);
System.out.println(union);  // [1, 2, 3, 4, 5, 6]

// Intersection
Set<Integer> intersection = new HashSet<>(set1);
intersection.retainAll(set2);
System.out.println(intersection);  // [3, 4]

// Difference
Set<Integer> difference = new HashSet<>(set1);
difference.removeAll(set2);
System.out.println(difference);  // [1, 2]
```

## Exercises

1. Create a Set of strings and verify it prevents duplicates.
2. Write a method that finds common elements between two Sets.
3. Implement a method that checks if one Set is a subset of another.

## Interview Questions

- What is the difference between Set and List?
- Why must elements in a Set implement equals() and hashCode()?
- What are the different Set implementations and their characteristics?

## Common Pitfalls

- Not implementing equals() and hashCode() for custom objects
- Assuming Set maintains insertion order (HashSet doesn't)
- Using Set when you need duplicates

## Best Practices

- Use HashSet for best performance when order doesn't matter
- Use LinkedHashSet when you need insertion order
- Use TreeSet when you need sorted order
- Always implement equals() and hashCode() for custom objects

## Real World Applications

- Tracking unique users or sessions
- Removing duplicate records
- Implementing membership tests
- Set operations in data analysis

## References

- [Set Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Set.html)
- [Java Collections Tutorial](https://docs.oracle.com/en/java/javase/21/collections/interfaces/set.html)

## Summary

In this topic, you learned about the Set interface and its guarantee of unique elements. Sets are essential for many operations requiring uniqueness. Practice with the exercises before learning about HashSet.
