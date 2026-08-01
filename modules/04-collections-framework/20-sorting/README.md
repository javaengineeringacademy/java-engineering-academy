# Sorting

## Introduction

Sorting is a fundamental operation for organizing data. Java provides multiple ways to sort collections and arrays.

## Learning Objectives

- Sort collections using Collections.sort()
- Sort arrays using Arrays.sort()
- Understand sorting algorithms
- Apply custom sorting with Comparators

## Prerequisites

- Comparators
- Comparable interface
- List and array concepts

## Why This Matters

Sorting is essential for data presentation, searching, and many algorithms. Understanding sorting helps optimize application performance.

## Syntax

```java
// Sort collections
Collections.sort(list);                    // Natural ordering
Collections.sort(list, comparator);        // Custom ordering
list.sort(comparator);                     // List.sort()

// Sort arrays
Arrays.sort(array);                        // Natural ordering
Arrays.sort(array, comparator);            // Custom ordering
Arrays.parallelSort(array);                // Parallel sorting
```

## Examples

```java
// Example 1: Basic sorting
List<Integer> numbers = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9));
Collections.sort(numbers);
System.out.println(numbers);  // [1, 2, 5, 8, 9]

// Example 2: Sorting with Comparator
List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
names.sort(Comparator.naturalOrder());
System.out.println(names);  // [Alice, Bob, Charlie]

names.sort(Comparator.reverseOrder());
System.out.println(names);  // [Charlie, Bob, Alice]

// Example 3: Sorting custom objects
class Employee {
    String name;
    double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
}

List<Employee> employees = new ArrayList<>();
employees.add(new Employee("Alice", 75000));
employees.add(new Employee("Bob", 85000));
employees.add(new Employee("Charlie", 65000));

// Sort by salary
employees.sort(Comparator.comparingDouble(e -> e.salary));

// Sort by name
employees.sort(Comparator.comparing(e -> e.name));

// Example 4: Sorting with streams
List<Employee> sorted = employees.stream()
    .sorted(Comparator.comparing(e -> e.name))
    .collect(Collectors.toList());

// Example 5: Parallel sorting
int[] largeArray = new int[1000000];
// Fill array
Arrays.parallelSort(largeArray);  // Uses multiple threads
```

## Exercises

1. Sort a list of strings by their length.
2. Sort a map by values instead of keys.
3. Implement a stable sort for objects with multiple fields.

## Interview Questions

- What sorting algorithm does Arrays.sort() use?
- What is the difference between stable and unstable sorting?
- When would you use parallelSort()?

## Common Pitfalls

- Not handling null values in comparators
- Modifying the collection during sorting
- Not considering time complexity

## Best Practices

- Use built-in sort methods when possible
- Use Comparator composition for complex sorting
- Consider parallelSort for large datasets
- Document sorting criteria

## Real World Applications

- Displaying data in sorted order
- Search optimization
- Data processing pipelines
- Report generation

## References

- [Collections.sort()](https://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#sort-java.util.List-)
- [Arrays.sort()](https://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#sort-int:A-)

## Summary

In this topic, you learned about sorting in Java, including collection and array sorting with natural and custom ordering. Practice with the exercises before learning about Collection Utilities.
