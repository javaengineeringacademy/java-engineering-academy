# TreeSet

## 1. Introduction

TreeSet is a `SortedSet` implementation based on a `TreeMap` (red-black tree). It stores unique elements in sorted order according to the natural ordering of elements or a custom `Comparator`. TreeSet provides O(log n) time for basic operations (add, remove, contains) and guarantees sorted iteration order.

TreeSet is the go-to choice when you need:
- Elements in sorted order
- Range operations (subSet, headSet, tailSet)
- Navigation methods (floor, ceiling, lower, higher)
- Guaranteed O(log n) performance

Unlike HashSet, TreeSet does not allow null elements (throws NullPointerException) and is not thread-safe.

## 2. Learning Objectives

- Create and use TreeSet with natural ordering and custom comparators
- Understand that TreeSet uses TreeMap internally
- Learn about sorted set operations: first, last, subSet, headSet, tailSet
- Master navigation methods: floor, ceiling, lower, higher
- Compare TreeSet vs HashSet vs LinkedHashSet
- Understand null element handling
- Learn about NavigableSet interface

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 11: HashSet (understand set basics)
- Understanding of Comparable and Comparator interfaces

## 4. Why This Concept Exists

HashSet provides O(1) performance but:
1. **No ordering**: Elements are in unpredictable order
2. **No range operations**: Can't efficiently find elements in a range
3. **No navigation methods**: Can't find closest elements

TreeSet provides:
1. **Sorted order**: Elements always in sorted order
2. **Range operations**: subSet, headSet, tailSet
3. **Navigation methods**: floor, ceiling, lower, higher
4. **Guaranteed O(log n)**: Red-black tree ensures balanced tree

## 5. Problem Statement

Consider building a priority task system:
- Tasks have priorities
- Need to display tasks in priority order
- Need to find tasks in a priority range
- Need to find the closest priority to a given value

HashSet can't maintain order. TreeSet provides all these operations efficiently.

## 6. Theory

### Internal Structure

TreeSet uses TreeMap internally:

```java
private transient NavigableMap<E,Object> m;
private static final Object PRESENT = new Object();

// When element is added:
public boolean add(E e) {
    return m.put(e, PRESENT) == null;
}
```

### Red-Black Tree

TreeSet uses a red-black tree (via TreeMap) which is a self-balancing binary search tree ensuring O(log n) operations.

## 7. Internal Working

### The add() Operation

```java
public boolean add(E e) {
    return m.put(e, PRESENT) == null;
}

// TreeMap.put() returns null if key is new, old value if key exists
// Since all values are PRESENT, we check if return is null
```

### Navigation Methods

```java
// floor: greatest element <= given element
public E floor(E e) {
    return m.floorKey(e);
}

// ceiling: smallest element >= given element
public E ceiling(E e) {
    return m.ceilingKey(e);
}

// lower: greatest element < given element
public E lower(E e) {
    return m.lowerKey(e);
}

// higher: smallest element > given element
public E higher(E e) {
    return m.higherKey(e);
}
```

## 8. JVM Perspective

### Memory Allocation

```java
TreeSet<String> set = new TreeSet<>();
// JVM allocates:
// - TreeSet object header: 12 bytes
// - map reference: 8 bytes
// Total TreeSet object: ~24 bytes

// Each element:
// - TreeMap Entry: ~56 bytes
// - Element object: varies
```

## 9. Memory Representation

```
TreeSet<Integer> set = new TreeSet<>();
set.add(5);
set.add(3);
set.add(7);
set.add(1);

Memory layout:
┌───────────────────────────────┐
│ TreeSet object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ map ──────────────────────────┐
└───────────────────────────────┘
                                │
                                ▼
                         TreeMap<Integer, Object>
                         ┌────────────────────────┐
                         │ root → Entry(5)        │
                         └────────────────────────┘
                                    │
                                    ▼
                         Entry(5) (root, BLACK)
                         ┌────────────────────┐
                         │ key = 5            │
                         │ value = PRESENT    │
                         │ left → Entry(3)    │
                         │ right → Entry(7)   │
                         └────────────────────┘

Tree structure (sorted):
        5 (BLACK)
       /    \
      3      7
     /
    1
```

## 10. Syntax

```java
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.NavigableSet;
import java.util.Comparator;

// ============================================
// CREATION
// ============================================
TreeSet<Integer> set = new TreeSet<>();                    // Natural ordering
TreeSet<Integer> set = new TreeSet<>(Comparator.reverseOrder()); // Reverse
TreeSet<Integer> set = new TreeSet<>(comparator);          // Custom comparator
TreeSet<Integer> set = new TreeSet<>(collection);          // From collection

// ============================================
// BASIC SET OPERATIONS
// ============================================
set.add(element);              // O(log n)
set.remove(element);           // O(log n)
set.contains(element);         // O(log n)
set.size();                    // O(1)
set.isEmpty();                 // O(1)
set.clear();                   // O(n)

// ============================================
// SORTED SET OPERATIONS
// ============================================
E first = set.first();         // O(log n)
E last = set.last();           // O(log n)

SortedSet<E> head = set.headSet(element);       // Elements < element
SortedSet<E> tail = set.tailSet(element);       // Elements >= element
SortedSet<E> sub = set.subSet(from, to);        // Elements in [from, to)

// ============================================
// NAVIGABLE SET OPERATIONS
// ============================================
E floor = set.floor(element);           // Greatest element <= element
E ceiling = set.ceiling(element);       // Smallest element >= element
E lower = set.lower(element);           // Greatest element < element
E higher = set.higher(element);         // Smallest element > element

NavigableSet<E> descending = set.descendingSet();
E firstDescending = descending.first();

// ============================================
// SET OPERATIONS
// ============================================
// Union
SortedSet<Integer> union = new TreeSet<>(set1);
union.addAll(set2);

// Intersection
SortedSet<Integer> intersection = new TreeSet<>(set1);
intersection.retainAll(set2);

// Difference
SortedSet<Integer> difference = new TreeSet<>(set1);
difference.removeAll(set2);

// ============================================
// ITERATION
// ============================================
for (Integer element : set) {
    System.out.println(element);
}

// Reverse iteration
for (Integer element : set.descendingSet()) {
    System.out.println(element);
}
```

## 11. Easy Example

```java
import java.util.TreeSet;
import java.util.Set;

public class TreeSetBasics {
    public static void main(String[] args) {
        // Create and populate
        TreeSet<Integer> numbers = new TreeSet<>();
        numbers.add(5);
        numbers.add(2);
        numbers.add(8);
        numbers.add(1);
        numbers.add(5); // Duplicate ignored

        System.out.println("Set (sorted): " + numbers);
        System.out.println("First: " + numbers.first());
        System.out.println("Last: " + numbers.last());

        // Navigation
        System.out.println("Floor of 3: " + numbers.floor(3));
        System.out.println("Ceiling of 3: " + numbers.ceiling(3));
        System.out.println("Lower of 5: " + numbers.lower(5));
        System.out.println("Higher of 5: " + numbers.higher(5));

        // Range operations
        System.out.println("Head (<5): " + numbers.headSet(5));
        System.out.println("Tail (>=3): " + numbers.tailSet(3));
        System.out.println("Sub (2,7): " + numbers.subSet(2, 7));

        // Iterate in order
        System.out.print("Sorted: ");
        for (Integer num : numbers) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
```

## 12. Medium Example

```java
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Set;

public class TreeSetOperations {
    public static void main(String[] args) {
        // Custom objects with comparator
        TreeSet<String> words = new TreeSet<>(Comparator.comparingInt(String::length)
                .thenComparing(Comparator.naturalOrder()));
        words.add("Banana");
        words.add("Apple");
        words.add("Cherry");
        words.add("Date");
        words.add("Elderberry");

        System.out.println("Sorted by length then alphabetically:");
        words.forEach(w -> System.out.println("  " + w));

        // Find k closest elements
        System.out.println("
K closest to 5:");
        TreeSet<Integer> numbers = new TreeSet<>(Set.of(1, 3, 5, 7, 9, 11, 13));
        int target = 6;
        int k = 3;
        System.out.println("  " + findKClosest(numbers, target, k));

        // Range query
        System.out.println("
Numbers between 3 and 10:");
        numbers.subSet(3, true, 10, true).forEach(n ->
            System.out.println("  " + n)
        );
    }

    static <T extends Comparable<T>> java.util.List<T> findKClosest(TreeSet<T> set, T target, int k) {
        java.util.List<T> result = new java.util.ArrayList<>();
        T floor = set.floor(target);
        T ceiling = set.ceiling(target);

        while (result.size() < k) {
            T lowerCandidate = floor != null ? floor : null;
            T higherCandidate = ceiling != null ? ceiling : null;

            if (lowerCandidate == null && higherCandidate == null) break;

            if (lowerCandidate != null && higherCandidate != null) {
                if (target.compareTo(lowerCandidate) - lowerCandidate.compareTo(target) <=
                    higherCandidate.compareTo(target) - target.compareTo(higherCandidate)) {
                    result.add(lowerCandidate);
                    floor = set.lower(lowerCandidate);
                } else {
                    result.add(higherCandidate);
                    ceiling = set.higher(higherCandidate);
                }
            } else if (lowerCandidate != null) {
                result.add(lowerCandidate);
                floor = set.lower(lowerCandidate);
            } else {
                result.add(higherCandidate);
                ceiling = set.higher(higherCandidate);
            }
        }
        return result;
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedTreeSet {
    public static void main(String[] args) {
        // Pattern 1: Interval merging
        System.out.println("=== Interval Merging ===");
        TreeSet<int[]> intervals = new TreeSet<>(Comparator.comparingInt(a -> a[0]));
        intervals.add(new int[]{1, 3});
        intervals.add(new int[]{2, 5});
        intervals.add(new int[]{4, 7});
        intervals.add(new int[]{6, 8});

        List<int[]> merged = mergeIntervals(new ArrayList<>(intervals));
        merged.forEach(i -> System.out.println("  [" + i[0] + ", " + i[1] + "]"));

        // Pattern 2: Sliding window median
        System.out.println("
=== Sliding Window Median ===");
        int[] data = {1, 3, -1, -3, 5, 3, 6, 7};
        int windowSize = 3;
        List<Double> medians = slidingWindowMedian(data, windowSize);
        System.out.println("  Medians: " + medians);

        // Pattern 3: Find k-th smallest
        System.out.println("
=== K-th Smallest ===");
        TreeSet<Integer> numbers = new TreeSet<>(Set.of(5, 3, 8, 1, 9, 2, 7, 4, 6));
        int k = 3;
        System.out.println("  " + k + "-th smallest: " + findKthSmallest(numbers, k));
    }

    static List<int[]> mergeIntervals(List<int[]> intervals) {
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(
                    merged.get(merged.size() - 1)[1], interval[1]
                );
            }
        }
        return merged;
    }

    static List<Double> slidingWindowMedian(int[] data, int k) {
        List<Double> medians = new ArrayList<>();

## 📑 Continue Reading

**Part 1** of 2 | [Part 2](README-part2.md)

```
