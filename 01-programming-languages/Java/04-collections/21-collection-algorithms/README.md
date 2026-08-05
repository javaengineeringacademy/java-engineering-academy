# Collection Algorithms

## Introduction

The Collections class provides utility methods for working with collections, including searching, shuffling, and creating unmodifiable collections.

## Learning Objectives

- Use Collections utility methods
- Create unmodifiable collections
- Perform searching and algorithms
- Understand Collections vs Collection

## Prerequisites

- All previous collection topics
- Java 8 streams (for modern alternatives)

## Why This Matters

Collections utilities simplify common operations and provide convenient methods that would otherwise require writing boilerplate code.

## Syntax

```java
// Searching
Collections.binarySearch(sortedList, key);
Collections.max(collection);
Collections.min(collection);
Collections.frequency(collection, object);

// Modifying
Collections.reverse(list);
Collections.shuffle(list);
Collections.sort(list);
Collections.swap(list, i, j);
Collections.rotate(list, distance);

// Creating views
Collections.unmodifiableList(list);
Collections.unmodifiableMap(map);
Collections.unmodifiableSet(set);
Collections.synchronizedList(list);
Collections.synchronizedMap(map);

// Creating empty collections
Collections.emptyList();
Collections.emptyMap();
Collections.emptySet();

// Singleton collections
Collections.singletonList(element);
Collections.singletonMap(key, value);
Collections.singleton(element);

// Filling
Collections.fill(list, object);
Collections.nCopies(n, object);
```

## Examples

```java
// Example 1: Searching
List<Integer> sorted = Arrays.asList(1, 3, 5, 7, 9);
int index = Collections.binarySearch(sorted, 5);
System.out.println(index);  // 2

int max = Collections.max(sorted);
int min = Collections.min(sorted);
int freq = Collections.frequency(sorted, 3);

// Example 2: Modifying
List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
Collections.reverse(names);
System.out.println(names);  // [Charlie, Bob, Alice]

Collections.shuffle(names);
System.out.println(names);  // Random order

// Example 3: Unmodifiable collections
List<String> immutable = Collections.unmodifiableList(Arrays.asList("A", "B", "C"));
// immutable.add("D");  // Throws UnsupportedOperationException

// Example 4: Synchronized collections
List<String> syncList = Collections.synchronizedList(new ArrayList<>());
// Thread-safe operations

// Example 5: Creating empty/singleton collections
List<String> empty = Collections.emptyList();
Map<String, Integer> singleton = Collections.singletonMap("key", 1);
```

## Exercises

1. Use Collections.binarySearch to find an element in a sorted list.
2. Create an unmodifiable map and try to modify it.
3. Use Collections.rotate to rotate a list by n positions.

## Interview Questions

- What is the difference between Collection and Collections?
- How do you create an unmodifiable collection?
- What is the difference between unmodifiable and immutable?

## Common Pitfalls

- Modifying unmodifiable collections (throws exception)
- Using binarySearch on unsorted list
- Not handling null values in utility methods

## Best Practices

- Use Collections.unmodifiable* for defensive copying
- Use Collections.synchronized* for thread safety
- Use Java 8+ methods when available
- Consider Guava or Apache Commons for additional utilities

## Real World Applications

- Defensive copying
- Thread-safe collections
- Quick sorting and searching
- Creating empty/singleton collections for defaults

## References

- [Collections Class](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Collections.html)
- [Java Collections Tutorial](https://docs.oracle.com/en/java/javase/21/collections/algorithms/)

## Summary

In this topic, you learned about the Collections utility class and its convenient methods for working with collections. Practice with the exercises before starting the mini-project.
