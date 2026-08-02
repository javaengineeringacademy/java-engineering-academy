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
        System.out.println("\nSet: " + uniqueFruits);
        System.out.println("Size (no duplicates): " + uniqueFruits.size());

        // Map: Key-value pairs
        Map<String, Integer> fruitPrices = new HashMap<>();
        fruitPrices.put("Apple", 1.50);
        fruitPrices.put("Banana", 0.75);
        fruitPrices.put("Cherry", 3.00);

        System.out.println("\nMap:");
        for (Map.Entry<String, Integer> entry : fruitPrices.entrySet()) {
            System.out.println("  " + entry.getKey() + " = $" + entry.getValue());
        }

        // Queue: FIFO processing
        Queue<String> orderQueue = new LinkedList<>();
        orderQueue.offer("Order 1");
        orderQueue.offer("Order 2");
        orderQueue.offer("Order 3");

        System.out.println("\nProcessing orders:");
        while (!orderQueue.isEmpty()) {
            System.out.println("  Processing: " + orderQueue.poll());
        }

        // Iterating collections
        System.out.println("\nIterating with for-each:");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }

        // Converting between collections
        List<String> sortedFruits = new ArrayList<>(uniqueFruits);
        Collections.sort(sortedFruits);
        System.out.println("\nSorted: " + sortedFruits);
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

        // Group by major
        Map<String, List<Student>> byMajor = students.stream()
            .collect(Collectors.groupingBy(Student::getMajor));

        System.out.println("Students by Major:");
        byMajor.forEach((major, list) -> {
            System.out.println("  " + major + ":");
            list.forEach(s -> System.out.println("    " + s.getName()));
        });

        // Calculate average GPA per major
        Map<String, Double> avgGpaByMajor = students.stream()
            .collect(Collectors.groupingBy(
                Student::getMajor,
                Collectors.averagingDouble(Student::getGpa)
            ));

        System.out.println("\nAverage GPA by Major:");
        avgGpaByMajor.forEach((major, avg) ->
            System.out.printf("  %s: %.2f%n", major, avg)
        );

        // Find top student per major
        Map<String, Optional<Student>> topByMajor = students.stream()
            .collect(Collectors.groupingBy(
                Student::getMajor,
                Collectors.maxBy(Comparator.comparing(Student::getGpa))
            ));

        System.out.println("\nTop Student by Major:");
        topByMajor.forEach((major, opt) ->
            opt.ifPresent(s ->
                System.out.printf("  %s: %s (%.2f)%n", major, s.getName(), s.getGpa())
            )
        );

        // Sort by GPA descending
        List<Student> sorted = students.stream()
            .sorted(Comparator.comparing(Student::getGpa).reversed())
            .toList();

        System.out.println("\nStudents sorted by GPA (desc):");
        sorted.forEach(s ->
            System.out.printf("  %s: %.2f%n", s.getName(), s.getGpa())
        );

        // Set operations
        Set<String> csMajors = students.stream()
            .filter(s -> s.getMajor().equals("CS"))
            .map(Student::getName)
            .collect(Collectors.toSet());

        Set<String> highGpaStudents = students.stream()
            .filter(s -> s.getGpa() >= 3.8)
            .map(Student::getName)
            .collect(Collectors.toSet());

        System.out.println("\nCS Majors: " + csMajors);
        System.out.println("High GPA (>=3.8): " + highGpaStudents);
        System.out.println("CS with High GPA: " +
            csMajors.stream().filter(highGpaStudents::contains).collect(Collectors.toSet()));
    }

    record Student(String name, String major, double gpa) {}
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.concurrent.*;

public class AdvancedCollectionPatterns {
    public static void main(String[] args) throws Exception {
        // Pattern 1: LRU Cache using LinkedHashMap
        System.out.println("=== LRU Cache ===");
        Map<String, String> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > 5;
            }
        };
        lruCache.put("1", "A");
        lruCache.put("2", "B");
        lruCache.put("3", "C");
        lruCache.get("1"); // Access "1" - moves to end
        lruCache.put("4", "D");
        lruCache.put("5", "E");
        lruCache.put("6", "F"); // Evicts "2" (least recently used)
        System.out.println("LRU Cache: " + lruCache);

        // Pattern 2: Multimap using Map<K, List<V>>
        System.out.println("\n=== Multimap Pattern ===");
        Map<String, List<String>> multimap = new HashMap<>();
        addToList(multimap, "fruits", "apple");
        addToList(multimap, "fruits", "banana");
        addToList(multimap, "colors", "red");
        addToList(multimap, "colors", "blue");
        multimap.forEach((key, values) ->
            System.out.println(key + ": " + values)
        );

        // Pattern 3: Immutable collections
        System.out.println("\n=== Immutable Collections ===");
        List<String> immutableList = List.of("A", "B", "C");
        Map<String, Integer> immutableMap = Map.of("x", 1, "y", 2);
        Set<Integer> immutableSet = Set.of(1, 2, 3);
        System.out.println("Immutable List: " + immutableList);
        System.out.println("Immutable Map: " + immutableMap);
        System.out.println("Immutable Set: " + immutableSet);

        // Pattern 4: Bounded priority queue
        System.out.println("\n=== Top-K Pattern ===");
        int[] nums = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5};
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > 3) {
                minHeap.poll();
            }
        }
        System.out.println("Top 3 largest: " + minHeap);

        // Pattern 5: Concurrent collection operations
        System.out.println("\n=== Concurrent Collections ===");
        ConcurrentHashMap<String, AtomicInteger> wordCount = new ConcurrentHashMap<>();
        String text = "the quick brown fox jumps over the lazy dog the fox";
        Arrays.stream(text.split(" "))
            .parallel()
            .forEach(word -> wordCount
                .computeIfAbsent(word, k -> new AtomicInteger(0))
                .incrementAndGet()
            );
        wordCount.forEach((word, count) ->
            System.out.println(word + ": " + count.get())
        );

        // Pattern 6: EnumMap for enum keys
        System.out.println("\n=== EnumMap Pattern ===");
        Map<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
        schedule.put(DayOfWeek.MONDAY, "Work");
        schedule.put(DayOfWeek.TUESDAY, "Gym");
        schedule.put(DayOfWeek.WEDNESDAY, "Work");
        schedule.put(DayOfWeek.THURSDAY, "Gym");
        schedule.put(DayOfWeek.FRIDAY, "Work");
        schedule.put(DayOfWeek.SATURDAY, "Rest");
        schedule.put(DayOfWeek.SUNDAY, "Rest");
        schedule.forEach((day, activity) ->
            System.out.println(day + ": " + activity)
        );
    }

    static <K, V> void addToList(Map<K, List<V>> map, K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class OrderProcessingSystem {
    private final Map<String, Order> ordersById;
    private final Map<String, List<Order>> ordersByCustomer;
    private final Map<Order.Status, List<Order>> ordersByStatus;
    private final Queue<Order> processingQueue;
    private final Deque<Order> recentOrders;

    public OrderProcessingSystem() {
        this.ordersById = new ConcurrentHashMap<>();
        this.ordersByCustomer = new ConcurrentHashMap<>();
        this.ordersByStatus = new EnumMap<>(Order.Status.class);
        this.processingQueue = new LinkedBlockingQueue<>();
        this.recentOrders = new ArrayDeque<>(100);
    }

    public void processOrder(Order order) {
        ordersById.put(order.getId(), order);
        ordersByCustomer
            .computeIfAbsent(order.getCustomerId(), k -> new CopyOnWriteArrayList<>())
            .add(order);
        ordersByStatus
            .computeIfAbsent(order.getStatus(), k -> new CopyOnWriteArrayList<>())
            .add(order);
        processingQueue.offer(order);
    }

    public Optional<Order> findOrder(String orderId) {
        return Optional.ofNullable(ordersById.get(orderId));
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return ordersByCustomer.getOrDefault(customerId, List.of());
    }

    public Map<Order.Status, Long> getOrderStatusSummary() {
        return ordersById.values().stream()
            .collect(Collectors.groupingBy(
                Order::getStatus,
                Collectors.counting()
            ));
    }

    public List<Order> getRecentOrders(int count) {
        return recentOrders.stream()
            .limit(count)
            .toList();
    }

    public double calculateTotalRevenue() {
        return ordersById.values().stream()
            .filter(o -> o.getStatus() == Order.Status.COMPLETED)
            .mapToDouble(Order::getAmount)
            .sum();
    }

    public Map<String, Double> getRevenueByCustomer() {
        return ordersById.values().stream()
            .filter(o -> o.getStatus() == Order.Status.COMPLETED)
            .collect(Collectors.groupingBy(
                Order::getCustomerId,
                Collectors.summingDouble(Order::getAmount)
            ));
    }

    public static void main(String[] args) {
        OrderProcessingSystem system = new OrderProcessingSystem();

        // Process sample orders
        system.processOrder(new Order("ORD001", "CUST1", 150.00, Order.Status.COMPLETED));
        system.processOrder(new Order("ORD002", "CUST1", 200.00, Order.Status.PROCESSING));
        system.processOrder(new Order("ORD003", "CUST2", 75.00, Order.Status.COMPLETED));
        system.processOrder(new Order("ORD004", "CUST3", 300.00, Order.Status.PENDING));
        system.processOrder(new Order("ORD005", "CUST2", 125.00, Order.Status.COMPLETED));

        // Generate reports
        System.out.println("=== Order Status Summary ===");
        system.getOrderStatusSummary().forEach((status, count) ->
            System.out.printf("  %s: %d orders%n", status, count)
        );

        System.out.println("\n=== Total Revenue ===");
        System.out.printf("  $%.2f%n", system.calculateTotalRevenue());

        System.out.println("\n=== Revenue by Customer ===");
        system.getRevenueByCustomer().forEach((customer, revenue) ->
            System.out.printf("  %s: $%.2f%n", customer, revenue)
        );

        System.out.println("\n=== Orders for CUST1 ===");
        system.getOrdersByCustomer("CUST1").forEach(order ->
            System.out.printf("  %s - $%.2f (%s)%n",
                order.getId(), order.getAmount(), order.getStatus())
        );
    }

    record Order(String id, String customerId, double amount, Status status) {
        enum Status { PENDING, PROCESSING, COMPLETED, CANCELLED }
    }
}
```

## 15. Performance

### Time Complexity Comparison

| Operation | ArrayList | LinkedList | HashSet | TreeSet | HashMap | TreeMap |
|-----------|-----------|------------|---------|---------|---------|---------|
| add | O(1)* | O(1) | O(1) | O(log n) | O(1) | O(log n) |
| get | O(1) | O(n) | N/A | N/A | O(1) | O(log n) |
| contains | O(n) | O(n) | O(1) | O(log n) | O(1) | O(log n) |
| remove | O(n) | O(1) | O(1) | O(log n) | O(1) | O(log n) |
| iteration | O(n) | O(n) | O(n) | O(n) | O(n) | O(n) |

*amortized

### Space Complexity

| Collection | Per Element Overhead |
|------------|---------------------|
| ArrayList | 4 bytes (reference) |
| LinkedList | 24 bytes (node + 2 pointers) |
| HashSet | 32 bytes (Entry object) |
| HashMap | 32 bytes (Node object) |
| TreeMap | 40 bytes (Entry + color bit) |

### When to Use What

| Use Case | Recommended Collection |
|----------|----------------------|
| Random access by index | ArrayList |
| Frequent insert/remove at beginning | LinkedList |
| Unique elements, fast lookup | HashSet |
| Unique elements, sorted | TreeSet |
| Key-value pairs, fast lookup | HashMap |
| Key-value pairs, sorted keys | TreeMap |
| Thread-safe key-value | ConcurrentHashMap |
| FIFO processing | ArrayDeque |
| Priority-based processing | PriorityQueue |

## 16. Best Practices

1. **Program to interfaces**: Use `List<E>` instead of `ArrayList<E>` for declarations
2. **Set initial capacity**: Avoid rehashing/resizing by specifying capacity
3. **Use generics**: Always specify type parameters for type safety
4. **Choose the right implementation**: Match the collection to your access patterns
5. **Use immutable collections**: Prefer `List.of()`, `Map.of()` for fixed data
6. **Avoid raw types**: Never use `List` without a type parameter
7. **Override equals/hashCode**: When using custom objects in Sets or as Map keys
8. **Use enhanced for loop**: Cleaner than Iterator when you don't need modification
9. **Prefer ArrayList**: Default choice for most List use cases
10. **Use ConcurrentHashMap**: For concurrent access instead of `Collections.synchronizedMap()`

## 17. Common Mistakes

```java
// Mistake 1: Modifying collection during enhanced for loop
for (String s : list) {
    if (s.equals("remove")) {
        list.remove(s); // ConcurrentModificationException!
    }
}

// Mistake 2: Not overriding equals/hashCode for Map keys
class BadKey {
    String value;
    // Missing equals() and hashCode()!
}
Map<BadKey, String> map = new HashMap<>();
BadKey key1 = new BadKey("test");
BadKey key2 = new BadKey("test");
map.put(key1, "value");
map.get(key2); // Returns null! key1 and key2 are different objects

// Mistake 3: Using Vector/Hashtable in modern code
Vector<String> legacy = new Vector<>(); // Don't do this!
Hashtable<String, String> legacyMap = new Hashtable<>(); // Use HashMap/ConcurrentHashMap

// Mistake 4: Confusing Collection and Collections
// Collection is the interface, Collections is the utility class
Collection<String> c = new ArrayList<>(); // Interface
Collections.sort(list); // Utility class

// Mistake 5: Not using generics
List list = new ArrayList(); // Raw type - unsafe!
list.add("hello");
list.add(123); // No compile error, but ClassCastException at runtime
```

## 18. Pitfalls

### ConcurrentModificationException
Occurs when a collection is modified structurally while being iterated. Solution: Use Iterator.remove() or concurrent collections.

### NullPointerException
Using null keys in HashMap is allowed but dangerous. Using null in TreeSet/TreeMap with natural ordering will throw NullPointerException.

### Unmodifiable vs Immutable
`Collections.unmodifiableList()` returns a view that prevents modification but can still be changed through the original reference. `List.of()` returns a truly immutable collection.

### Performance Surprises
- `LinkedList.get(i)` is O(n), not O(1)
- `HashMap.containsKey()` is O(1) but `containsValue()` is O(n)
- `TreeSet.contains()` is O(log n), not O(1)

### Generics Type Erasure
At runtime, `List<String>` and `List<Integer>` are the same type (`List`). This affects instance checks, array creation, and static members.

## 19. Debugging Tips

1. **Print collections properly**: Use `System.out.println(collection)` for debugging
2. **Use IDE debugger**: Inspect internal state of collections
3. **Check equals/hashCode**: If Map lookups fail, verify your key's contract
4. **Watch for side effects**: Lambda expressions in stream operations should be side-effect free
5. **Monitor memory**: Use VisualVM or JConsole to watch collection sizes
6. **Enable assertions**: Use `-ea` flag to catch invariant violations
7. **Use `toString()`**: Override in custom objects for meaningful debug output
8. **Profile hot paths**: Use JMH or YourKit to identify collection bottlenecks

## 20. Comparison Table

| Feature | List | Set | Queue | Map |
|---------|------|-----|-------|-----|
| Ordered | Yes | Depends | Yes | Depends |
| Duplicates | Yes | No | Yes | Keys: No |
| Null elements | Multiple | One | Multiple | One key |
| Index access | Yes | No | No | Key access |
| Interface | Collection | Collection | Collection | Separate |
| Primary use | Sequence | Unique | Processing | Lookup |

## 21. Decision Tree

```
Need to store elements?
├── Yes → Need key-value pairs?
│   ├── Yes → Need sorted keys?
│   │   ├── Yes → TreeMap
│   │   └── No → Need thread safety?
│   │       ├── Yes → ConcurrentHashMap
│   │       └── No → Need insertion order?
│   │           ├── Yes → LinkedHashMap
│   │           └── No → HashMap
│   └── No → Need unique elements?
│       ├── Yes → Need sorted elements?
│       │   ├── Yes → TreeSet
│       │   └── No → Need insertion order?
│       │       ├── Yes → LinkedHashSet
│       │       └── No → HashSet
│       └── No → Need FIFO?
│           ├── Yes → ArrayDeque (preferred) or LinkedList
│           └── No → Need priority ordering?
│               ├── Yes → PriorityQueue
│               └── No → Need index access?
│                   ├── Yes → ArrayList (default) or LinkedList
│                   └── No → LinkedList
└── No → Error
```

## 22. Interview Questions

### Q1: What is the difference between Collection and Collections?
**A**: `Collection` is the root interface in the Collections Framework hierarchy. `Collections` is a utility class with static methods for operating on collections (sorting, searching, synchronization).

### Q2: Why is ArrayList preferred over LinkedList?
**A**: ArrayList provides O(1) random access, better cache locality due to contiguous memory, and less memory overhead. LinkedList only wins for frequent insertions/deletions at the beginning, which is rare in practice.

### Q3: What happens when HashMap gets too many collisions?
**A**: When a bucket has 8 or more entries, the linked list is converted to a red-black tree (Java 8+), changing lookup from O(n) to O(log n). When the tree shrinks to 6 entries, it converts back to a linked list.

### Q4: How does HashMap handle null keys?
**A**: HashMap allows one null key (hash code = 0). The null key is always placed in bucket 0. This is different from TreeMap, which throws NullPointerException.

### Q5: What is the difference between fail-fast and fail-safe iterators?
**A**: Fail-fast iterators throw ConcurrentModificationException if the collection is modified during iteration. Fail-safe iterators (e.g., CopyOnWriteArrayList, ConcurrentHashMap) work on a snapshot and don't throw the exception.

### Q6: When would you use a TreeMap over a HashMap?
**A**: When you need keys in sorted order, need range queries (subMap, headMap, tailMap), or need guaranteed O(log n) performance. TreeMap uses a red-black tree internally.

### Q7: What is the time complexity of HashMap operations?
**A**: Average case: O(1) for get, put, remove. Worst case: O(n) with many collisions, or O(log n) with treeification (Java 8+). The hash function quality and load factor affect actual performance.

## 23. Exercises

### Exercise 1: Basic Operations
Create a program that:
1. Creates an ArrayList of 10 integers
2. Adds, removes, and retrieves elements
3. Converts the list to a HashSet and prints unique values
4. Creates a HashMap mapping each integer to its square

### Exercise 2: Collection Transformation
Given a `List<String>` of words:
1. Find all words longer than 5 characters
2. Group words by their first letter
3. Sort words by length, then alphabetically
4. Create a frequency map of all characters

### Exercise 3: Custom Object Collections
Create a `Person` class with name, age, and email fields:
1. Store persons in a List, Set, and Map (keyed by email)
2. Filter persons by age range
3. Sort persons by name, then by age
4. Find the oldest person in each collection

### Exercise 4: Performance Comparison
Write a benchmark comparing:
1. ArrayList vs LinkedList for random access (10,000 elements)
2. HashSet vs TreeSet for contains() operations
3. HashMap vs TreeMap for get() operations

## 24. Assignments

### Assignment 1: Contact Book
Build a contact management system using collections:
- Store contacts in a Map<String, Contact> (phone → Contact)
- Implement search by name (partial match)
- Group contacts by first letter of name
- Export contacts sorted by name

### Assignment 2: Inventory System
Create an inventory management system:
- Use Map<String, Product> for product lookup by SKU
- Use List<Product> for ordered display
- Use Set<String> for categories
- Implement low-stock alerts and revenue calculations

### Assignment 3: Task Scheduler
Build a task scheduling system:
- Use PriorityQueue for task priority
- Use Map<String, Task> for task lookup
- Use Queue<Task> for FIFO processing
- Implement task dependencies using graph relationships

## 25. Mini Project

### Smart Library Management System

Build a library system that demonstrates all collection types:

```java
// Requirements:
// 1. Store books in ArrayList (for ordered display)
// 2. Index books by ISBN using HashMap
// 3. Track unique genres using TreeSet
// 4. Maintain borrowing queue using LinkedList
// 5. Keep history using ArrayDeque
// 6. Generate reports using Stream API
```

**Features to implement:**
- Add/remove/search books
- Borrow/return books with queue management
- Genre-based categorization
- Search by author, title, or ISBN
- Borrowing history with recent activity
- Most borrowed books report
- Overdue books tracking

## 26. Summary

The Java Collections Framework is the backbone of Java programming. Key takeaways:

- **Interfaces define contracts**: List, Set, Queue, Map
- **Implementations provide performance characteristics**: ArrayList vs LinkedList, HashMap vs TreeMap
- **Generics ensure type safety**: Always use parameterized types
- **Choose the right collection**: Based on access patterns, ordering, uniqueness, and thread safety
- **Understand internals**: Know how your collections work to avoid pitfalls
- **Use modern APIs**: Java 9+ factory methods (`List.of()`, `Map.of()`) for immutable collections
- **Performance matters**: Know the Big O complexity of operations
- **Thread safety**: Use ConcurrentHashMap or synchronized wrappers when needed

## 27. References

### Official Documentation
- [Java Collections Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
- [Collection Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)
- [Map Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Map.html)

### Books
- *Effective Java* by Joshua Bloch (Item 15-19, 54-56)
- *Java Generics and Collections* by Maurice Naftalin and Philip Wadler
- *Java Concurrency in Practice* by Brian Goetz

### Online Resources
- [Baeldung Collections Guide](https://www.baeldung.com/java-collections)
- [GeeksforGeeks Collections Framework](https://www.geeksforgeeks.org/collections-framework-in-java/)
- [Java Collections Memory Model](https://shipilev.net/)

### Related Topics
- [ArrayList](../03-arraylist/README.md)
- [HashMap](../15-hashmap/README.md)
- [ConcurrentHashMap](../18-concurrenthashmap/README.md)
