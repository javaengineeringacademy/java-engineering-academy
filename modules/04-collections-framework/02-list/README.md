# List Interface

## Introduction

List is an ordered collection (sequence) that allows duplicate elements. It provides positional access to elements by index.

## Learning Objectives

- Understand the List interface and its methods
- Learn about index-based operations
- Understand element ordering and duplicates
- Know when to use List vs other collections

## Prerequisites

- Introduction to Collections Framework
- Basic array concepts

## Why This Matters

Lists are the most commonly used collection type, providing ordered storage with indexed access similar to arrays but with dynamic sizing.

## Syntax

```java
// List interface methods
List<E> list = new ArrayList<>();
list.add(element);           // Add to end
list.add(index, element);    // Add at index
list.get(index);             // Get element
list.set(index, element);    // Replace element
list.remove(index);          // Remove by index
list.remove(object);         // Remove by object
list.size();                 // Get size
list.contains(element);      // Check existence
list.indexOf(element);       // Find index
list.subList(from, to);      // Get sublist
```

## Examples

```java
// Example 1: Basic List operations
List<String> fruits = new ArrayList<>();
fruits.add("Apple");
fruits.add("Banana");
fruits.add("Cherry");
fruits.add(1, "Blueberry");  // Insert at index 1

System.out.println(fruits);       // [Apple, Blueberry, Banana, Cherry]
System.out.println(fruits.get(1)); // Blueberry

// Example 2: List iteration
for (String fruit : fruits) {
    System.out.println(fruit);
}

// With index
for (int i = 0; i < fruits.size(); i++) {
    System.out.println(i + ": " + fruits.get(i));
}

// Example 3: List with objects
List<Person> people = new ArrayList<>();
people.add(new Person("Alice", 30));
people.add(new Person("Bob", 25));

Person found = people.stream()
    .filter(p -> p.getName().equals("Bob"))
    .findFirst()
    .orElse(null);
```

## Exercises

1. Create a List of integers, add 5 numbers, and print them in reverse order.
2. Write a method that removes all duplicate elements from a List.
3. Create a List of strings and sort them alphabetically.

## Interview Questions

- What is the difference between ArrayList and LinkedList?
- How do you remove elements from a List while iterating?
- What is the time complexity of get() and add() operations?

## Common Pitfalls

- Modifying a list while iterating (use Iterator or removeIf)
- IndexOutOfBoundsException when accessing invalid indices
- Confusing remove(index) with remove(object)

## Best Practices

- Use the interface type (List) not the implementation
- Prefer enhanced for loop or forEach for iteration
- Use removeIf for conditional removal

## Real World Applications

- Storing ordered data (user lists, product catalogs)
- Implementing undo/redo functionality
- Managing playlists or queues
- Caching with positional access

## References

- [List Interface](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/interfaces/list.html)

## Summary

In this topic, you learned about the List interface and its capabilities for ordered, indexed collections. Practice with the exercises before exploring specific List implementations.
