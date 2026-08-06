# Introduction to Collections Framework

## 1. Introduction

The Java Collections Framework (JCF) is a unified architecture for representing and manipulating groups of objects. Introduced in Java 1.2, it provides a comprehensive set of interfaces, implementations, and algorithms that simplify the most common tasks in Java programming. The framework includes roughly 30 classes and 15 interfaces, forming a coherent system for managing collections of data.

At its core, the Collections Framework replaces the legacy `Vector`, `Hashtable`, and `Arrays`-based approaches with a modern, high-performance, and well-designed set of data structures. Understanding the JCF is essential for every Java developer because virtually every Java application—whether a simple script or a large enterprise system—relies on collections to store, retrieve, and process data.

This module covers every major component of the Collections Framework: List, Set, Queue, Deque, Map, iterators, comparators, sorting algorithms, and thread-safe collections. By the end, you will understand when and why to use each collection, how they work internally, and how to write efficient, maintainable code.

## 2. Learning Objectives

- Understand the Collection interface hierarchy and the Map hierarchy
- Learn the difference between Collection and Map
- Identify the major interfaces: List, Set, Queue, Deque, Map
- Understand the benefits of the Collections Framework over legacy classes
- Recognize the role of generics in type-safe collections
- Learn the difference between Collection and Collections utility class
- Understand the concept of fail-fast and fail-safe iterators
- Know the performance characteristics of major implementations

## 3. Prerequisites

- Module 01: Java Fundamentals (variables, control flow, methods)
- Module 02: Object-Oriented Programming (interfaces, inheritance, polymorphism)
- Module 03: Generics basics
- Basic understanding of arrays and their limitations
- Familiarity with the concept of time complexity (Big O notation)

## 4. Why This Concept Exists

Before the Collections Framework, Java developers had to rely on legacy classes like `Vector`, `Hashtable`, and `Stack`, along with raw arrays. These had several problems:

1. **No unified architecture**: Each class had its own API, making them hard to learn and use interchangeably.
2. **Poor performance**: Legacy classes were synchronized by default, adding overhead even in single-threaded applications.
3. **No algorithm support**: There were no standard algorithms for sorting, searching, or manipulating collections.
4. **No interoperability**: It was difficult to convert between different collection types.
5. **Type safety issues**: Legacy classes stored `Object` types, requiring manual casting.

The Collections Framework solves all of these problems by providing:
- A consistent API across all collection types
- High-performance implementations with optional synchronization
- Standard algorithms (sorting, searching, shuffling)
- Easy interoperability between implementations
- Generics for compile-time type safety

## 5. Problem Statement

Consider a real-world scenario: building a contact management application. You need to:
- Store contacts in a list (ordered, allows duplicates)
- Quickly look up a contact by phone number (key-value mapping)
- Maintain a set of unique email addresses (no duplicates)
- Process contacts in order of last called (priority queue)

Without the Collections Framework, you would need to implement each data structure from scratch, handle resizing, manage memory, and write sorting algorithms. With the JCF, you simply choose the right implementation:

```java
List<Contact> contacts = new ArrayList<>();
Map<String, Contact> phoneIndex = new HashMap<>();
Set<String> emails = new HashSet<>();
Queue<Contact> recentCalls = new PriorityQueue<>(Comparator.comparing(Contact::getLastCalled));
```

## 6. Theory

### The Collection Hierarchy

```
```
Iterable<E>                    (root interface - provides iterator())
├── Collection<E>              (main interface - defines bulk operations)
    ├── List<E>                (ordered, allows duplicates)
    │   ├── ArrayList<E>       (dynamic array)
    │   ├── LinkedList<E>      (doubly-linked list)
    │   └── Vector<E>          (synchronized array, legacy)
    │       └── Stack<E>       (LIFO stack, legacy)
    ├── Set<E>                 (no duplicates)
    │   ├── HashSet<E>         (hash table)
    │   │   └── LinkedHashSet<E> (hash table + linked list)
    │   └── TreeSet<E>         (red-black tree)
    ├── Queue<E>               (FIFO operations)
    │   ├── PriorityQueue<E>   (heap-based priority queue)
    │   └── ArrayDeque<E>      (resizable array deque)
    └── Deque<E>               (double-ended queue)
        ├── ArrayDeque<E>      (resizable array)
        └── LinkedList<E>      (doubly-linked list)
```

### The Map Hierarchy (Separate from Collection)

```
Map<K,V>                       (key-value pairs)
├── HashMap<K,V>              (hash table)
│   └── LinkedHashMap<K,V>    (hash table + linked list)
├── TreeMap<K,V>              (red-black tree)
├── Hashtable<K,V>            (synchronized hash table, legacy)
└── ConcurrentHashMap<K,V>   (thread-safe hash table)
```

### Key Interfaces

| Interface | Purpose | Order | Duplicates | Null Elements |
|-----------|---------|-------|------------|---------------|
| List | Ordered sequence | Index-based | Yes | Yes |
| Set | Unique elements | Varies | No | At most one null |
| Queue | FIFO processing | Insertion order | Yes | Yes |
| Deque | Double-ended queue | Insertion order | Yes | Yes |
| Map | Key-value pairs | Key-based | Keys: No | Keys: At most one null |

## 7. Internal Working

### How Collections Work Under the Hood

Each collection implementation uses a different internal data structure:

**ArrayList**: Uses a `transient Object[] elementData` array. When elements are added and the array is full, a new array is created with 1.5x the capacity (via `Arrays.copyOf()`), and elements are copied over.

**LinkedList**: Uses a doubly-linked list where each element is a `Node` containing `item`, `next`, and `prev` references. No array resizing, but each node requires extra memory for two pointers.

**HashSet**: Internally uses a `HashMap<E, Object>` where all values are a shared `PRESENT` object. The hash code of elements determines bucket placement.

**HashMap**: Uses an array of `Node` buckets. Each bucket is a linked list (or tree for 8+ entries). The key's `hashCode()` determines the bucket index via `hash(key) & (n-1)`.

**TreeMap**: Uses a red-black tree (self-balancing binary search tree). All operations are O(log n). Maintains keys in sorted order.

### The Role of hashCode() and equals()

Every object used as a key in a Map or element in a Set must properly implement `hashCode()` and `equals()`:

```java
// Contract:
// 1. If equals() returns true, hashCode() MUST return the same value
// 2. If hashCode() returns the same value, equals() MAY return false
// 3. equals() must be reflexive, symmetric, transitive, and consistent
// 4. hashCode() must return the same value for the same object across invocations
```

## 8. JVM Perspective

### Memory Allocation

When you create a collection, the JVM allocates memory on the heap:

```java
List<String> list = new ArrayList<>();
// JVM allocates:
// - Object header (12 bytes on 64-bit JVM)
// - Reference to backing array (8 bytes)
// - Initial backing array Object[] (empty or default capacity)
// - Size counter (4 bytes)
```

### Garbage Collection Considerations

- Collections hold references to their elements, preventing garbage collection
- Large collections can cause GC pressure during resizing
- The `WeakReference` and `SoftReference` classes can be used with collections for memory-sensitive caching

### JIT Optimization

The JIT compiler optimizes collection operations:
- Inlines small methods (e.g., `ArrayList.get()`)
- Eliminates bounds checks for indexed access
- Devirtualizes calls when the concrete type is known
- Scalar-replaces small objects

### Unsafe Operations

The JDK uses `sun.misc.Unsafe` internally for some collection operations (e.g., array bounds checks in `ArrayList`). In Java 9+, these are replaced with `VarHandle` for better encapsulation.

## 9. Memory Representation

### ArrayList Memory Layout

```
ArrayList object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ size (int, 4 bytes)         │
│ elementData reference (8B)  │──────┐
└─────────────────────────────┘      │
                                     ▼
                              Object[] array (on heap):
                              ┌──────────────────────┐
                              │ [0] → String "Hello" │
                              │ [1] → String "World" │
                              │ [2] → null           │
                              │ [3] → null           │
                              └──────────────────────┘
```

### LinkedList Memory Layout

```
LinkedList object (on heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ size (int, 4 bytes)         │
│ first → Node (8 bytes)      │──────┐
│ last → Node (8 bytes)       │──┐   │
└─────────────────────────────┘  │   │
                                 │   ▼
                          Node object:    Node object:
                          ┌────────────┐  ┌────────────┐
                          │ item → "A" │  │ item → "B" │
                          │ next ──────────→ null      │
                          │ prev = null│  │ prev ──────────→ Node A
                          └────────────┘  └────────────┘
```

### HashMap Memory Layout

```
HashMap object:
┌──────────────────────────────┐
│ Object header                │
│ table → Node[] (reference)   │──────┐
│ size (int)                   │      │
│ threshold (int)              │      ▼
│ loadFactor (float)           │      Node[] bucket array:
└──────────────────────────────┘      ┌────────────────────────┐
                                      │ [0] → null             │
                                      │ [1] → Node → Node → .. │
                                      │ [2] → null             │
                                      │ [3] → Node             │
                                      │ ...                    │
                                      └────────────────────────┘
```

## 10. Syntax

```java
// ============================================
// LIST
// ============================================
List<E> list = new ArrayList<>();
List<E> list = new ArrayList<>(initialCapacity);
List<E> list = new LinkedList<>();
List<E> list = List.of("a", "b", "c");          // Immutable (Java 9+)
List<E> list = new ArrayList<>(List.of("a", "b")); // Mutable copy

list.add(element);              // append
list.add(index, element);       // insert
list.get(index);                // random access
list.set(index, element);       // replace
list.remove(index);             // remove by index
list.remove(object);            // remove by value
list.contains(object);          // O(n)
list.indexOf(object);           // O(n)
list.subList(from, to);         // view
list.size();                    // count
list.isEmpty();                 // boolean
list.clear();                   // remove all
list.addAll(collection);        // bulk add
list.sort(comparator);          // sort in place

// ============================================
// SET
// ============================================
Set<E> set = new HashSet<>();
Set<E> set = new LinkedHashSet<>();
Set<E> set = new TreeSet<>();
Set<E> set = Set.of("a", "b", "c");              // Immutable

set.add(element);               // add
set.remove(element);            // remove
set.contains(element);          // O(1) for HashSet
set.size();                     // count
set.addAll(collection);         // union
set.retainAll(collection);      // intersection
set.removeAll(collection);      // difference

// ============================================
// MAP
// ============================================
Map<K, V> map = new HashMap<>();
Map<K, V> map = new LinkedHashMap<>();
Map<K, V> map = new TreeMap<>();
Map<K, V> map = Map.of("k1", "v1", "k2", "v2");  // Immutable

map.put(key, value);            // add/replace
map.putIfAbsent(key, value);    // add only if absent
map.get(key);                   // retrieve
map.getOrDefault(key, default); // retrieve with default
map.remove(key);                // remove
map.containsKey(key);           // O(1) for HashMap
map.containsValue(value);       // O(n)
map.keySet();                   // Set of keys
map.values();                   // Collection of values
map.entrySet();                 // Set<Map.Entry<K,V>>
map.size();                     // count
map.forEach((k, v) -> ...);    // iterate
map.merge(key, value, remappingFn); // merge
map.compute(key, mappingFn);    // compute
map.computeIfAbsent(key, mappingFn); // compute if absent
map.computeIfPresent(key, remappingFn); // compute if present

// ============================================
// QUEUE / DEQUE
// ============================================
Queue<E> queue = new PriorityQueue<>();
Deque<E> deque = new ArrayDeque<>();
Deque<E> deque = new LinkedList<>();

queue.offer(element);           // add to tail
queue.poll();                   // remove from head
queue.peek();                   // view head
deque.offerFirst(element);      // add to head
deque.offerLast(element);       // add to tail
deque.pollFirst();              // remove from head
deque.pollLast();               // remove from tail
deque.peekFirst();              // view head
deque.peekLast();               // view tail

// ============================================
// UTILITY OPERATIONS
// ============================================
Collections.sort(list);
Collections.reverse(list);
Collections.shuffle(list);
Collections.unmodifiableList(list);   // immutable view
Collections.synchronizedList(list);   // thread-safe wrapper
Collections.synchronizedMap(map);
Collections.checkedList(list, type);  // type-safe wrapper
```

## 11. Easy Example

```java
import java.util.*;

public class CollectionsBasics {
    public static void main(String[] args) {
        // List: Ordered, allows duplicates
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate allowed

        System.out.println("List: " + fruits);
        System.out.println("Size: " + fruits.size());
        System.out.println("Contains Apple: " + fruits.contains("Apple"));

        // Set: No duplicates
        Set<String> uniqueFruits = new HashSet<>(fruits);
        System.out.println("
Set: " + uniqueFruits);
        System.out.println("Size (no duplicates): " + uniqueFruits.size());

        // Map: Key-value pairs
        Map<String, Integer> fruitPrices = new HashMap<>();
        fruitPrices.put("Apple", 1.50);
        fruitPrices.put("Banana", 0.75);
        fruitPrices.put("Cherry", 3.00);

        System.out.println("
Map:");
        for (Map.Entry<String, Integer> entry : fruitPrices.entrySet()) {
            System.out.println("  " + entry.getKey() + " = $" + entry.getValue());
        }

        // Queue: FIFO processing
        Queue<String> orderQueue = new LinkedList<>();
        orderQueue.offer("Order 1");
        orderQueue.offer("Order 2");
        orderQueue.offer("Order 3");

        System.out.println("
Processing orders:");
        while (!orderQueue.isEmpty()) {
            System.out.println("  Processing: " + orderQueue.poll());
        }

        // Iterating collections
        System.out.println("
Iterating with for-each:");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }

        // Converting between collections
        List<String> sortedFruits = new ArrayList<>(uniqueFruits);
        Collections.sort(sortedFruits);
        System.out.println("
Sorted: " + sortedFruits);
    }
}
```

## 12. Medium Example

```java
import java.util.*;
import java.util.stream.*;

public class StudentManagement {
    public static void main(String[] args) {
        // Create students
        List<Student> students = List.of(
            new Student("Alice", "CS", 3.8),
            new Student("Bob", "Math", 3.5),
            new Student("Charlie", "CS", 3.9),
            new Student("Diana", "Physics", 3.7),
            new Student("Eve", "Math", 3.6)
        );

## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

