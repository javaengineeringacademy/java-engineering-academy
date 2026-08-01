# TreeSet

## Introduction

TreeSet is a NavigableSet implementation based on a TreeMap (Red-Black tree). It maintains elements in sorted order.

## Learning Objectives

- Create and use TreeSet
- Understand natural ordering and custom comparators
- Learn NavigableSet methods
- Know when to use TreeSet vs HashSet

## Prerequisites

- Set Interface
- Comparable and Comparator interfaces

## Why This Matters

TreeSet provides sorted set operations with O(log n) performance, essential for range queries and sorted data requirements.

## Syntax

```java
// Creating TreeSet
TreeSet<E> set = new TreeSet<>();              // Natural ordering
TreeSet<E> set = new TreeSet<>(comparator);    // Custom ordering
TreeSet<E> set = new TreeSet<>(collection);    // From collection

// Standard Set operations
set.add(element);        // O(log n)
set.remove(element);     // O(log n)
set.contains(element);   // O(log n)

// NavigableSet methods
set.first();             // Smallest element
set.last();              // Largest element
set.lower(element);      // Greatest element less than
set.higher(element);     // Smallest element greater than
set.floor(element);      // Greatest element less than or equal
set.ceiling(element);    // Smallest element greater than or equal
set.headSet(toElement);  // Elements less than
set.tailSet(fromElement); // Elements greater than or equal
set.subSet(from, to);    // Range of elements
```

## Examples

```java
// Example 1: Natural ordering
TreeSet<Integer> numbers = new TreeSet<>();
numbers.add(5);
numbers.add(1);
numbers.add(3);

System.out.println(numbers);  // [1, 3, 5] - sorted
System.out.println(numbers.first());  // 1
System.out.println(numbers.last());   // 5

// Example 2: Custom comparator
TreeSet<String> names = new TreeSet<>(Comparator.reverseOrder());
names.add("Alice");
names.add("Bob");
names.add("Charlie");

System.out.println(names);  // [Charlie, Bob, Alice] - reverse sorted

// Example 3: NavigableSet operations
TreeSet<Integer> set = new TreeSet<>(Arrays.asList(1, 3, 5, 7, 9));

System.out.println(set.lower(5));      // 3
System.out.println(set.higher(5));     // 7
System.out.println(set.floor(4));      // 3
System.out.println(set.ceiling(4));    // 5

System.out.println(set.headSet(5));    // [1, 3]
System.out.println(set.tailSet(5));    // [5, 7, 9]
System.out.println(set.subSet(3, 8));  // [3, 5, 7]

// Example 4: Custom objects
class Student implements Comparable<Student> {
    private String name;
    private double gpa;

    @Override
    public int compareTo(Student other) {
        return Double.compare(other.gpa, this.gpa);  // Descending by GPA
    }
}

TreeSet<Student> students = new TreeSet<>();
students.add(new Student("Alice", 3.8));
students.add(new Student("Bob", 3.5));
students.add(new Student("Charlie", 3.9));
// Sorted by GPA descending
```

## Exercises

1. Create a TreeSet of strings sorted by length, then alphabetically.
2. Implement a method that finds the two closest numbers in a TreeSet.
3. Create a TreeSet of custom objects with multiple sorting criteria.

## Interview Questions

- What is the time complexity of add(), remove(), and contains()?
- What is the difference between floor() and lower()?
- How does TreeSet handle duplicate elements?

## Common Pitfalls

- Assuming TreeSet allows null elements (it doesn't with natural ordering)
- Not implementing Comparable for custom objects
- Using TreeSet when HashSet would suffice

## Best Practices

- Use TreeSet when you need sorted elements
- Use HashSet when order doesn't matter (better performance)
- Implement Comparable for custom objects
- Consider the performance overhead for large datasets

## Real World Applications

- Leaderboards and rankings
- Sorted data display
- Range queries
- Time-based data ordering
- Priority-based processing

## References

- [TreeSet Documentation](https://docs.oracle.com/javase/8/docs/api/java/util/TreeSet.html)
- [NavigableSet Interface](https://docs.oracle.com/javase/8/docs/api/java/util/NavigableSet.html)

## Summary

In this topic, you learned about TreeSet and its sorted set capabilities with O(log n) performance. It's essential for range queries and sorted data. Practice with the exercises before learning about Map.
