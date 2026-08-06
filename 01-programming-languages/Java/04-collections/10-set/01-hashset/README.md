# HashSet

## 1. Introduction

HashSet is the most commonly used implementation of the `Set` interface. It uses a `HashMap` internally to store elements, providing O(1) average-time performance for add, remove, and contains operations. HashSet does not allow duplicate elements and makes no guarantees about the iteration order.

HashSet is the default choice for most Set use cases because it provides the fastest lookup performance. It allows one null element and is not thread-safe. The iteration order is unpredictable and may change over time as the internal hash table is resized.

Understanding HashSet is essential because it's used everywhere: removing duplicates, membership testing, set operations (union, intersection, difference), and as building blocks for more complex data structures.

## 2. Learning Objectives

- Create and use HashSet with generics
- Understand that HashSet uses HashMap internally
- Learn about hash-based set operations (add, remove, contains)
- Understand that HashSet does not maintain insertion order
- Compare HashSet vs LinkedHashSet vs TreeSet
- Learn about null element handling
- Understand thread-safety considerations
- Master set operations: union, intersection, difference

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Module 15: HashMap (understand hash table internals)
- Understanding of hashCode() and equals() contracts

## 4. Why This Concept Exists

Before HashSet, developers used:
1. **Manual array checking**: O(n) for each `contains()` check
2. **Hashtable**: Synchronized, slow, legacy
3. **TreeSet**: O(log n) for all operations

HashSet provides:
1. **O(1) performance**: For add, remove, and contains
2. **No duplicates**: Automatically prevents duplicate elements
3. **Simplicity**: Easy to use API
4. **Null support**: Allows one null element

HashSet is essential for:
- Removing duplicates from collections
- Fast membership testing
- Set operations (union, intersection, difference)
- Tracking unique elements

## 5. Problem Statement

Consider building a tag system for a blog:
- Each post can have multiple tags
- Tags must be unique per post
- Fast lookup to check if a tag exists
- Quick addition and removal of tags

Without HashSet, you'd need:
- A List with manual duplicate checking: O(n) for each check
- Or a sorted array with binary search: O(log n) but requires sorting

With HashSet, all operations are O(1) average case, and duplicates are automatically prevented.

## 6. Theory

### Internal Structure

HashSet uses a HashMap internally:

```java
private transient HashMap<E,Object> map;

// All values are the same shared object
private static final Object PRESENT = new Object();
```

When you add an element to HashSet:
```java
public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}
```

The element becomes the key in the HashMap, and all values are the shared `PRESENT` object.

### Hash Distribution

HashSet relies on the `hashCode()` method of elements for bucket placement. Good hash codes distribute elements uniformly across the hash table, minimizing collisions.

### Load Factor and Resizing

HashSet uses the same load factor and resizing mechanism as HashMap:
- Default load factor: 0.75
- When `size > capacity * loadFactor`, the table is resized
- New capacity = old capacity * 2

## 7. Internal Working

### The add() Operation

```java
public boolean add(E e) {
    return map.put(e, PRESENT) == null;
}

// HashMap.put() returns null if key is new, old value if key exists
// Since all values are PRESENT, we check if return is null
// null return means key was added (new element)
// PRESENT return means key existed (duplicate)
```

### The contains() Operation

```java
public boolean contains(Object o) {
    return map.containsKey(o);
}

// HashMap.containsKey() is O(1) average
```

### The remove() Operation

```java
public boolean remove(Object o) {
    return map.remove(o) == PRESENT;
}

// HashMap.remove() returns the value if key was present
// We check if it was PRESENT to confirm removal
```

## 8. JVM Perspective

### Memory Allocation

```java
HashSet<String> set = new HashSet<>();
// JVM allocates:
// - HashSet object header: 12 bytes
// - map reference: 8 bytes
// Total HashSet object: ~24 bytes

// When adding elements:
// - HashMap with 16 buckets: ~128 bytes
// - Each Entry: ~40 bytes (hash, key, value, next)
// - Each element: varies (String object)
```

### JIT Optimization

The JIT compiler optimizes HashSet operations by:
- Inlining HashMap methods
- Optimizing hash calculations
- Eliminating redundant null checks

## 9. Memory Representation

```
```
HashSet<String> set = new HashSet<>();
set.add("Apple");
set.add("Banana");
set.add("Cherry");

Memory layout:
┌───────────────────────────────┐
│ HashSet object                │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ map ──────────────────────────┐
└───────────────────────────────┘
                                │
                                ▼
                         HashMap<String, Object>
                         ┌────────────────────────┐
                         │ table → Entry[]         │
                         │ size = 3                │
                         └────────────────────────┘
                                    │
                                    ▼
                         Entry[] bucket array:
                         ┌────────────────────────┐
                         │ [0] → null             │
                         │ [1] → null             │
                         │ [2] → Entry("Apple")   │
                         │ [3] → null             │
                         │ [4] → Entry("Banana")  │
                         │ [5] → null             │
                         │ [6] → Entry("Cherry")  │
                         │ ...                    │
                         └────────────────────────┘

Entry("Apple"):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ key → String "Apple"        │
│ value → Object PRESENT      │
│ next → null                 │
└─────────────────────────────┘
```

## 10. Syntax

```java
import java.util.HashSet;
import java.util.Set;

// ============================================
// CREATION
// ============================================
Set<String> set = new HashSet<>();
Set<String> set = new HashSet<>(16);           // Initial capacity
Set<String> set = new HashSet<>(16, 0.75f);   // Capacity and load factor
Set<String> set = new HashSet<>(otherCollection); // From collection
Set<String> immutable = Set.of("A", "B", "C"); // Java 9+

// ============================================
// ADDING ELEMENTS
// ============================================
set.add("element");              // Returns true if added
set.addAll(collection);          // Add all from collection

// ============================================
// REMOVING ELEMENTS
// ============================================
set.remove("element");           // Returns true if removed
set.removeIf(predicate);         // Conditional removal
set.clear();                     // Remove all

// ============================================
// CHECKING
// ============================================
boolean has = set.contains("element");  // O(1) average
boolean empty = set.isEmpty();
int size = set.size();

// ============================================
// SET OPERATIONS
// ============================================
// Union
Set<String> union = new HashSet<>(set1);
union.addAll(set2);

// Intersection
Set<String> intersection = new HashSet<>(set1);
intersection.retainAll(set2);

// Difference
Set<String> difference = new HashSet<>(set1);
difference.removeAll(set2);

// Symmetric Difference
Set<String> symDiff = new HashSet<>(set1);
symDiff.addAll(set2);
Set<String> common = new HashSet<>(set1);
common.retainAll(set2);
symDiff.removeAll(common);

// ============================================
// CONVERSIONS
// ============================================
String[] array = set.toArray(new String[0]);
List<String> list = new ArrayList<>(set);

// ============================================
// ITERATION
// ============================================
for (String element : set) {
    System.out.println(element);
}

set.forEach(System.out::println);

Iterator<String> it = set.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
```

## 11. Easy Example

```java
import java.util.HashSet;
import java.util.Set;

public class HashSetBasics {
    public static void main(String[] args) {
        // Create and populate
        Set<String> fruits = new HashSet<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate ignored

        System.out.println("Set: " + fruits);
        System.out.println("Size: " + fruits.size());

        // Check if contains
        System.out.println("Contains Apple: " + fruits.contains("Apple"));
        System.out.println("Contains Grape: " + fruits.contains("Grape"));

        // Remove
        fruits.remove("Banana");
        System.out.println("After removing Banana: " + fruits);

        // Iterate
        System.out.print("Iterating: ");
        for (String fruit : fruits) {
            System.out.print(fruit + " ");
        }
        System.out.println();
    }
}
```

## 12. Medium Example

```java
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class HashSetOperations {
    public static void main(String[] args) {
        // Remove duplicates from list
        List<Integer> numbersWithDuplicates = List.of(1, 2, 3, 1, 2, 4, 5, 3);
        Set<Integer> uniqueNumbers = new HashSet<>(numbersWithDuplicates);
        System.out.println("Original: " + numbersWithDuplicates);
        System.out.println("Unique: " + uniqueNumbers);

        // Set operations
        Set<String> set1 = new HashSet<>(Set.of("A", "B", "C", "D"));
        Set<String> set2 = new HashSet<>(Set.of("C", "D", "E", "F"));

        // Union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        System.out.println("Union: " + union);

        // Intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        System.out.println("Intersection: " + intersection);

        // Difference
        Set<String> difference = new HashSet<>(set1);
        difference.removeAll(set2);
        System.out.println("Difference (set1 - set2): " + difference);

        // Find common elements
        List<String> list1 = List.of("A", "B", "C", "D");
        List<String> list2 = List.of("C", "D", "E", "F");
        Set<String> common = new HashSet<>(list1);
        common.retainAll(list2);
        System.out.println("Common elements: " + common);
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedHashSet {
    public static void main(String[] args) {
        // Pattern 1: Custom hashCode/equals
        System.out.println("=== Custom Objects ===");
        Set<Employee> employees = new HashSet<>();
        employees.add(new Employee(1, "Alice", "Engineering"));
        employees.add(new Employee(2, "Bob", "Marketing"));
        employees.add(new Employee(1, "Alice", "Management")); // Replaces
        employees.forEach(e -> System.out.println("  " + e.name()));

        // Pattern 2: Set-based deduplication
        System.out.println("
=== Deduplication ===");
        List<Transaction> transactions = List.of(
            new Transaction("T1", 100),
            new Transaction("T2", 200),
            new Transaction("T1", 100), // Duplicate
            new Transaction("T3", 300)
        );
        Set<Transaction> unique = new HashSet<>(transactions);
        System.out.println("Unique transactions: " + unique.size());

        // Pattern 3: Find missing numbers
        System.out.println("
=== Missing Numbers ===");
        Set<Integer> allNumbers = new HashSet<>(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toSet()));
        Set<Integer> present = Set.of(1, 2, 4, 6, 7, 9);
        allNumbers.removeAll(present);
        System.out.println("Missing: " + allNumbers);

        // Pattern 4: Anagram groups
        System.out.println("
=== Anagram Groups ===");
        String[] words = {"listen", "silent", "enlist", "hello", "olleh"};
        Map<String, Set<String>> anagramGroups = groupAnagrams(words);
        anagramGroups.forEach((key, group) ->
            System.out.println("  " + key + ": " + group)
        );

## 📑 Continue Reading

**Part 1** of 2 | Part 2

