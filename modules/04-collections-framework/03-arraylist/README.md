# ArrayList

## Introduction

ArrayList is the most commonly used List implementation. It uses a dynamic array internally, providing fast random access and efficient iteration.

## Learning Objectives

- Create and use ArrayList
- Understand ArrayList's performance characteristics
- Learn common ArrayList operations
- Know when to use ArrayList vs alternatives

## Prerequisites

- List Interface
- Basic array concepts

## Why This Matters

ArrayList is the default choice for most List use cases due to its simplicity and performance for random access operations.

## Syntax

```java
// Creating ArrayList
List<E> list = new ArrayList<>();           // Empty
List<E> list = new ArrayList<>(capacity);   // With initial capacity
List<E> list = new ArrayList<>(collection); // From another collection

// Common operations
list.add(element);        // O(1) amortized
list.get(index);          // O(1)
list.set(index, element); // O(1)
list.remove(index);       // O(n)
list.size();              // O(1)
```

## Examples

```java
// Example 1: Basic ArrayList
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
names.add("Charlie");

System.out.println(names);  // [Alice, Bob, Charlie]
System.out.println(names.get(0));  // Alice

// Example 2: ArrayList with initial capacity
List<Integer> numbers = new ArrayList<>(100);
for (int i = 0; i < 100; i++) {
    numbers.add(i);
}

// Example 3: Converting array to ArrayList
String[] array = {"A", "B", "C"};
List<String> list = new ArrayList<>(Arrays.asList(array));

// Example 4: ArrayList of custom objects
List<Product> products = new ArrayList<>();
products.add(new Product("Laptop", 999.99));
products.add(new Product("Phone", 699.99));

// Find expensive products
List<Product> expensive = products.stream()
    .filter(p -> p.getPrice() > 800)
    .collect(Collectors.toList());
```

## Exercises

1. Create an ArrayList of integers and implement a method to find the second largest element.
2. Write a program to remove all strings shorter than 5 characters from a list.
3. Create a ArrayList that stores and retrieves student grades.

## Interview Questions

- What is the time complexity of add(), get(), and remove() operations?
- When would you use ArrayList over LinkedList?
- How does ArrayList handle resizing internally?

## Common Pitfalls

- Not considering initial capacity (causes repeated resizing)
- Using remove() in a loop without proper index handling
- Thread-safety issues (ArrayList is not synchronized)

## Best Practices

- Set initial capacity if you know the approximate size
- Use removeIf() for conditional removal
- Consider LinkedList for frequent insertions/deletions
- Use Collections.unmodifiableList() for immutable lists

## Real World Applications

- Storing dynamic lists of data (users, products, records)
- Implementing stacks and queues (with add/remove at end)
- Caching frequently accessed data
- Building dynamic arrays for algorithms

## References

- [ArrayList Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/implementations/arraylist.html)

## Summary

In this topic, you learned about ArrayList, the most commonly used List implementation. It provides fast random access and is ideal for most list use cases. Practice with the exercises before learning about LinkedList.
