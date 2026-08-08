# Sorting

## Overview

Sorting is a fundamental operation for organizing data. Java provides multiple ways to sort collections and arrays, using TimSort (a hybrid merge sort / insertion sort) for objects and dual-pivot quicksort for primitives.

## Learning Objectives

- Sort collections using `Collections.sort()` and `List.sort()`
- Sort arrays using `Arrays.sort()` and `Arrays.parallelSort()`
- Understand sorting algorithms (TimSort, dual-pivot quicksort)
- Apply custom sorting with Comparators
- Know when to use parallel sorting

## Sorting Collections

```java
// Natural ordering
Collections.sort(list);
list.sort(Comparator.naturalOrder());

// Custom ordering
list.sort(Comparator.comparingInt(String::length));
list.sort(Comparator.reverseOrder());

// Sort with Comparator
Collections.sort(list, comparator);
```

## Sorting Arrays

```java
// Natural ordering
Arrays.sort(array);

// Custom ordering
Arrays.sort(array, comparator);

// Parallel sorting for large datasets
Arrays.parallelSort(array);
```

## Sorting Algorithms

| Algorithm | Used For | Time Complexity | Stable |
|-----------|----------|----------------|--------|
| TimSort | Objects | O(n log n) | Yes |
| Dual-pivot quicksort | Primitives | O(n log n) | No |
| Parallel sort | Large arrays | O(n/p log n) | Depends |

## Stability

A stable sort preserves the relative order of equal elements. This matters when sorting by multiple fields:

```java
// Stable sort: Alice (30) comes before Bob (30) if both have same age
employees.sort(Comparator.comparingInt(e -> e.age));
// Previous order preserved for equal ages
```

## Best Practices

- Use built-in sort methods (optimized, tested)
- Use `Comparator.comparing()` and composition for complex sorting
- Consider `parallelSort()` for large datasets (> 8192 elements)
- Document sorting criteria
- Keep sorting consistent with `equals()` when stability matters
