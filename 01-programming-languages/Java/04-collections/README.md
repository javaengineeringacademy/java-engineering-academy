# Module 04: Collections Framework

> **Difficulty:** ⭐⭐⭐ Intermediate  
> **Reading:** 45 min | **Practice:** 60 min | **Total:** 105 min

## Overview

The Java Collections Framework (JCF) provides a unified architecture for representing and manipulating groups of objects. Introduced in Java 1.2, it replaced legacy classes like `Vector`, `Hashtable`, and `Stack` with a coherent set of interfaces, implementations, and algorithms. This module covers List, Set, Map, Queue, Deque, sorting, searching, and thread-safe collections.

## Learning Objectives

- [ ] Choose the right collection type for different use cases (List, Set, Map, Queue)
- [ ] Explain the difference between ArrayList and LinkedList performance characteristics
- [ ] Implement custom sorting with Comparable and Comparator
- [ ] Use Map operations efficiently (get, put, compute, merge)
- [ ] Identify thread-safe collection alternatives for concurrent access
- [ ] Apply the Collections utility methods for sorting, shuffling, and searching
- [ ] Avoid common collection pitfalls (ConcurrentModificationException, null keys)

## Prerequisites

- Java fundamentals (variables, methods, classes)
- Basic OOP (inheritance, interfaces)
- Familiarity with generics (Module 06)

## History

- **1998** — Java 1.2 introduced the Collections Framework with List, Set, Map, Queue interfaces and ArrayList, HashSet, HashMap implementations
- **2004** — Java 5 added generics for type safety, eliminating raw types and manual casting
- **2006** — Java 6 added NavigableMap and NavigableSet for range-based queries
- **2011** — Java 7 completed Deque interface with ArrayDeque
- **2014** — Java 8 added Stream API, lambda support, and default methods on Collection interface
- **2017** — Java 9 added `List.of()`, `Map.of()`, `Set.of()` factory methods for immutable collections
- **2021** — Java 16 added Record types as Map keys
- **2023** — Java 21 added SequencedCollection interface for uniform ordering

## Production Notes

- **Where is it used?** In every Java application that stores, retrieves, or processes groups of objects
- **Why is it useful?** Provides type-safe, high-performance data structures with standard algorithms
- **When should it be avoided?** Not applicable; collections are fundamental to all Java development
- **Alternative?** Arrays (fixed-size), third-party libraries (Eclipse Collections, Koloboke)

## Why This Concept Exists

Before JCF, Java developers faced:
- No unified API — each class had its own methods
- Poor performance — legacy classes synchronized everything
- No algorithms — no standard sort, search, or shuffle
- No interoperability — difficult to convert between types
- No type safety — raw Object storage required manual casting

JCF provides a consistent API, high-performance implementations, standard algorithms, and generics for type safety.

## Core Concepts

### Collection Hierarchy

```
Iterable<E>
├── Collection<E>
│   ├── List<E>       → ArrayList, LinkedList, Vector
│   ├── Set<E>        → HashSet, LinkedHashSet, TreeSet
│   ├── Queue<E>      → PriorityQueue, ArrayDeque
│   └── Deque<E>      → ArrayDeque, LinkedList

Map<K,V>
├── HashMap<K,V>      → LinkedHashMap
├── TreeMap<K,V>
├── Hashtable<K,V>
└── ConcurrentHashMap<K,V>
```

### Collection vs Map

| Aspect | Collection | Map |
|--------|-----------|-----|
| Stores | Individual elements | Key-value pairs |
| Duplicates | Depends on subinterface | No duplicate keys |
| Interface | Collection<E> | Map<K,V> |
| Hierarchy | Part of Collection | Separate hierarchy |

### Interface vs Implementation

| Interface | Purpose | Common Implementation |
|-----------|---------|----------------------|
| List | Ordered, duplicates | ArrayList |
| Set | Unique elements | HashSet |
| Queue | FIFO processing | ArrayDeque |
| Deque | Double-ended queue | ArrayDeque |
| Map | Key-value pairs | HashMap |

## Internal Working

### ArrayList Internals

```
ArrayList uses Object[] array internally:
- Default capacity: 10
- Growth: 1.5x (newCapacity = oldCapacity + (oldCapacity >> 1))
- Add: O(1) amortized (resizing is O(n))
- Get: O(1) by index
- Remove: O(n) — shifts elements
```

### HashMap Internals

```
HashMap uses Node[] table with linked list/red-black tree:
- Default capacity: 16, load factor: 0.75
- Hash: (h = key.hashCode()) ^ (h >>> 16)
- Bucket index: (n - 1) & hash
- Collision: Linked list → Red-black tree (treeify at 8 nodes)
- Resize: When size > capacity * load factor
```

### LinkedList Internals

```
LinkedList uses doubly-linked Node objects:
- Each node has: prev, item, next
- Add: O(1) at ends, O(n) by index
- Get: O(n) — must traverse
- Remove: O(1) if node reference known
```

## Syntax

```java
// Creating collections
List<String> list = new ArrayList<>();
Set<Integer> set = new HashSet<>();
Map<String, Integer> map = new HashMap<>();
Queue<String> queue = new ArrayDeque<>();
Deque<String> deque = new ArrayDeque<>();

// Factory methods (Java 9+)
List<String> immutable = List.of("a", "b", "c");
Set<Integer> immutableSet = Set.of(1, 2, 3);
Map<String, Integer> immutableMap = Map.of("a", 1, "b", 2);

// Adding elements
list.add("element");
list.add(0, "first");
set.add("unique");
map.put("key", 1);

// Retrieving
String s = list.get(0);
int v = map.get("key");
boolean has = set.contains("element");

// Removing
list.remove("element");
list.remove(0);
set.remove("element");
map.remove("key");

// Iterating
for (String s : list) { }
for (Map.Entry<String, Integer> e : map.entrySet()) { }
list.forEach(System.out::println);
map.forEach((k, v) -> System.out.println(k + "=" + v));
```

## Examples

### Easy: Basic List Operations
```java
import java.util.*;

public class ListExample {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        
        names.add("Diana");
        names.remove("Bob");
        names.set(0, "Alicia");
        
        System.out.println("Names: " + names);
        System.out.println("Size: " + names.size());
        System.out.println("Contains Charlie: " + names.contains("Charlie"));
        System.out.println("Index of Diana: " + names.indexOf("Diana"));
    }
}
```

### Medium: Map Operations
```java
import java.util.*;
import java.util.stream.*;

public class MapExample {
    public static void main(String[] args) {
        Map<String, Integer> wordCount = new HashMap<>();
        
        String[] words = "the cat sat on the mat the cat".split(" ");
        for (String word : words) {
            wordCount.merge(word, 1, Integer::sum);
        }
        
        System.out.println("Word counts: " + wordCount);
        
        // Sort by value
        Map<String, Integer> sorted = wordCount.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> a,
                LinkedHashMap::new
            ));
        
        System.out.println("Sorted: " + sorted);
    }
}
```

### Hard: Custom Sorting
```java
import java.util.*;

public class CustomSorting {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>(List.of(
            new Employee("Alice", 75000),
            new Employee("Bob", 90000),
            new Employee("Charlie", 60000)
        ));
        
        // Sort by salary using Comparator
        employees.sort(Comparator.comparingDouble(Employee::getSalary));
        System.out.println("By salary: " + employees);
        
        // Sort by name length, then alphabetically
        employees.sort(Comparator
            .comparingInt(e -> e.getName().length())
            .thenComparing(Employee::getName));
        System.out.println("By name: " + employees);
    }
}

class Employee {
    private final String name;
    private final double salary;
    
    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public double getSalary() { return salary; }
    
    @Override
    public String toString() {
        return name + "($" + salary + ")";
    }
}
```

### Enterprise: Thread-Safe Collections
```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ConcurrentCollectionExample {
    public static void main(String[] args) throws InterruptedException {
        // ConcurrentHashMap for concurrent access
        ConcurrentHashMap<String, AtomicInteger> counters = new ConcurrentHashMap<>();
        
        String[] items = {"apple", "banana", "apple", "cherry", "banana", "apple"};
        
        for (String item : items) {
            counters.computeIfAbsent(item, k -> new AtomicInteger(0))
                     .incrementAndGet();
        }
        
        System.out.println("Counts: " + counters);
        
        // CopyOnWriteArrayList for read-heavy scenarios
        CopyOnWriteArrayList<String> threadSafeList = new CopyOnWriteArrayList<>();
        threadSafeList.add("a");
        threadSafeList.add("b");
        
        // BlockingQueue for producer-consumer
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        queue.put("item"); // blocks if full
        String taken = queue.take(); // blocks if empty
    }
}
```

## Performance Considerations

| Operation | ArrayList | LinkedList | HashSet | HashMap | TreeMap |
|-----------|-----------|------------|---------|---------|---------|
| Add | O(1) amortized | O(1) | O(1) | O(1) | O(log n) |
| Remove | O(n) | O(n) | O(1) | O(1) | O(log n) |
| Contains | O(n) | O(n) | O(1) | O(1) | O(log n) |
| Get by index | O(1) | O(n) | N/A | N/A | N/A |
| Get by key | N/A | N/A | N/A | O(1) | O(log n) |

- **ArrayList** is fastest for random access; LinkedList for frequent insert/delete at ends
- **HashMap** is fastest for key-value lookup; TreeMap for sorted keys
- **HashSet** is fastest for membership testing
- **Always prefer ArrayList** unless you have a specific reason to use LinkedList

## Best Practices

**Do's:**
- Program to interfaces (`List<String>` not `ArrayList<String>`)
- Use diamond operator (`new ArrayList<>()`)
- Use `List.of()` for immutable collections (Java 9+)
- Use `computeIfAbsent`/`merge` for atomic map operations
- Choose the right collection for your use case

**Don'ts:**
- Don't modify collection during iteration (use `Iterator.remove()`)
- Don't use `Vector` or `Hashtable` (use `CopyOnWriteArrayList`/`ConcurrentHashMap`)
- Don't use `null` keys in `HashMap` (use `Optional` or sentinel values)
- Don't create unnecessary copies of collections
- Don't use `size() == 0` when `isEmpty()` is clearer

## Common Mistakes

| Mistake | Problem | Fix |
|---------|---------|-----|
| `ConcurrentModificationException` | Modifying during iteration | Use `Iterator.remove()` or `removeIf()` |
| Using `Vector` | Synchronized overhead, legacy API | Use `ArrayList` or `CopyOnWriteArrayList` |
| `HashMap` with null key | Unexpected behavior | Use `Optional` or sentinel values |
| `==` on wrapper classes | Reference comparison, not value | Use `.equals()` or auto-unboxing |
| Modifying unmodifiable list | `UnsupportedOperationException` | Use mutable list if modification needed |

## Interview Questions

### Q1: What is the difference between ArrayList and LinkedList?
**Answer:** ArrayList uses a resizable array — O(1) random access, O(n) insert/delete. LinkedList uses doubly-linked nodes — O(1) insert/delete at ends, O(n) random access. ArrayList is faster for most use cases due to cache locality.

### Q2: How does HashMap handle collisions?
**Answer:** HashMap uses a linked list for collisions. When a bucket has 8+ entries, it converts to a red-black tree for O(log n) lookup. When capacity exceeds threshold (capacity × load factor), it resizes to double capacity and rehashes.

### Q3: What is the difference between HashMap and ConcurrentHashMap?
**Answer:** HashMap is not thread-safe — concurrent modification causes `ConcurrentModificationException`. ConcurrentHashMap uses fine-grained locking (bin-level locking in Java 8+) for concurrent access without locking the entire map.

### Q4: What is `ConcurrentModificationException` and how do you avoid it?
**Answer:** Thrown when a collection is modified during iteration (except via the iterator's own methods). Avoid by using `Iterator.remove()`, `removeIf()`, or concurrent collections.

### Q5: When should you use LinkedList over ArrayList?
**Answer:** Rarely. LinkedList is better only when you need O(1) insert/delete at both ends AND don't need random access. ArrayList's cache locality makes it faster for most operations.

### Q6: What is the difference between HashSet and TreeSet?
**Answer:** HashSet uses hashing for O(1) add/contains/remove but is unordered. TreeSet uses a red-black tree for O(log n) operations but maintains sorted order. Use HashSet unless you need sorted iteration.

### Q7: What is `computeIfAbsent` and why is it useful?
**Answer:** Atomic operation that computes a value only if the key is absent: `map.computeIfAbsent(key, k -> new ArrayList<>())`. Prevents race conditions and redundant computations in concurrent code.

### Q8: How do you sort a List?
**Answer:** Use `list.sort(Comparator)` or `Collections.sort(list)`. For custom objects, implement `Comparable` or provide a `Comparator`. Java 8+ added default methods: `Comparator.comparing()`, `thenComparing()`, `reversed()`.

### Q9: What is the difference between `size() == 0` and `isEmpty()`?
**Answer:** Functionally identical. `isEmpty()` is more readable and is the preferred idiom. Some implementations may have different performance characteristics.

### Q10: What is `SequencedCollection` in Java 21?
**Answer:** New interface that provides uniform access to the first/last elements and reverse iteration for all ordered collections (List, Deque, SortedSet, etc.). Adds `getFirst()`, `getLast()`, `addFirst()`, `addLast()`, `reversed()`.

## Cross-References

- **Previous Module:** [03 - Exceptions](../03-exceptions/)
- **Next Module:** [05 - Text Processing](../05-text-processing/)
- **Related:** [06 - Generics](../06-generics/) — parameterized collection types
- **Related:** [07 - Functional Programming](../07-functional-programming/) — Stream API with collections
- **Related:** [09 - Multithreading](../09-multithreading-&-concurrency/) — thread-safe collections

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| `ConcurrentModificationException` | Stack trace analysis | Identify line where collection is modified during iteration |
| HashMap not finding key | Debug key's `hashCode()` | Verify key's `hashCode()` and `equals()` are consistent |
| Null pointer in collection | Check collection contents | Verify collection doesn't contain null; check return values |
| Slow collection performance | JMH microbenchmarks | Benchmark different collection types for your use case |
| Thread-safety issue | Thread dump + profiling | Use `jstack` to identify thread contention |

## Code Review Checklist

- [ ] Program to interfaces (`List` not `ArrayList`)
- [ ] No `Vector` or `Hashtable` used
- [ ] No collection modification during iteration
- [ ] `equals()` and `hashCode()` consistent for Map keys
- [ ] Appropriate collection type chosen for use case
- [ ] Thread-safe collections used for concurrent access
- [ ] Immutable collections used where appropriate
- [ ] No null keys in HashMap (use sentinel or Optional)

## Architecture Considerations

Collections are the foundation of data processing in Java applications. At scale, collection choice directly impacts memory usage, throughput, and GC pressure. For high-throughput systems, understanding collection internals (ArrayList's growth factor, HashMap's load factor, ConcurrentHashMap's bin locking) enables optimal configuration.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| ArrayList | Random access, iteration | Pros: Fast access, cache-friendly; Cons: Slow insert/delete |
| LinkedList | Frequent insert/delete at ends | Pros: O(1) insert/delete; Cons: Slow random access, memory overhead |
| HashMap | Key-value lookup | Pros: O(1) get/put; Cons: No ordering, null keys |
| TreeMap | Sorted key-value lookup | Pros: Sorted iteration, range queries; Cons: O(log n) operations |
| ConcurrentHashMap | Concurrent key-value access | Pros: Thread-safe, high throughput; Cons: More complex API |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Null key in HashMap causing DoS | Hash collision attack | Validate keys, use `Objects.requireNonNull()` |
| HashMap key with crafted hashCode() | Hash collision attack (HashDoS) | Use `ConcurrentHashMap` with balanced trees |
| Untrusted deserialization of collections | Remote code execution | Use `ObjectInputStream` with whitelisting |
| Thread-unsafe collection in concurrent code | Data corruption | Use concurrent collections or synchronization |
| Collection containing sensitive data | Information exposure | Clear collections after use; use immutable collections |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.2 | Collections Framework introduced | Replace Vector, Hashtable with ArrayList, HashMap |
| Java 5 | Generics added | Replace raw types with parameterized types |
| Java 7 | Diamond operator | Use `new ArrayList<>()` instead of `new ArrayList<String>()` |
| Java 8 | Stream API | Use streams for complex collection processing |
| Java 9 | `List.of()`, `Map.of()`, `Set.of()` | Use factory methods for immutable collections |
| Java 16 | Record types as Map keys | Records auto-implement equals/hashCode |
| Java 21 | SequencedCollection | Use `getFirst()`, `getLast()`, `reversed()` |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| Collections Framework | Java 1.2 | Stable |
| Generics | Java 5 | Stable |
| Diamond operator | Java 7 | Stable |
| `List.of()`, `Map.of()`, `Set.of()` | Java 9 | Stable |
| `Stream.toList()` | Java 16 | Stable |
| SequencedCollection | Java 21 | Stable |

## Production Incidents

### Incident 1: ConcurrentModificationException in Production

**Problem:** A web application crashed during peak traffic with `ConcurrentModificationException` in the shopping cart service.
**Cause:** Multiple threads iterated over and modified the same `ArrayList` without synchronization.
**Impact:** Shopping cart failures for 40% of users; $50K lost revenue during 2-hour outage.
**Detection:** Exception logs showed the error in cart iteration code.
**Solution:** Replaced `ArrayList` with `CopyOnWriteArrayList` for read-heavy cart; used `synchronized` for write-heavy operations.
**Prevention:** Use concurrent collections for multi-threaded access; document thread-safety requirements.

### Incident 2: HashMap Null Key Causing NullPointerException

**Problem:** A caching service threw `NullPointerException` intermittently when retrieving cached values.
**Cause:** `HashMap` stored a null key (user ID was sometimes null), and `get()` returned null which was unboxed to int.
**Impact:** Cache misses caused database overload; 3x normal DB load for 4 hours.
**Detection:** Monitoring showed spike in DB queries; investigation revealed null key in cache.
**Solution:** Added null check before caching; used `Optional` for nullable keys.
**Prevention:** Never use null keys in HashMap; validate keys before insertion.

### Incident 3: ArrayList O(n) Performance in Hot Path

**Problem:** A high-throughput API showed degraded response times under load; latency increased from 5ms to 200ms.
**Cause:** Code used `ArrayList.contains()` in a hot loop — O(n) lookup on growing list.
**Impact:** API response times degraded 40x; SLA violations; customer complaints.
**Detection:** Profiling showed 80% of time spent in `ArrayList.contains()`.
**Solution:** Changed to `HashSet` for O(1) membership testing; reduced latency to 3ms.
**Prevention:** Choose the right collection for the access pattern; profile before optimizing.

## Production Checklist

- [ ] Program to interfaces (`List` not `ArrayList`)
- [ ] No `Vector` or `Hashtable` used
- [ ] No collection modification during iteration
- [ ] `equals()` and `hashCode()` consistent for Map keys
- [ ] Appropriate collection type chosen for use case
- [ ] Thread-safe collections used for concurrent access
- [ ] Immutable collections used where appropriate
- [ ] No null keys in HashMap
- [ ] Collections cleared when no longer needed
- [ ] Large collections use initial capacity hint

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses ArrayList for everything; doesn't understand generics; uses raw types |
| Intermediate | Chooses appropriate collection; understands generics; uses iterator safely |
| Advanced | Uses ConcurrentHashMap; understands internals; optimizes collection usage |
| Expert | Designs collection-based APIs; mentors on collection patterns; contributes to JCF |

## Common Myths

1. **Myth**: LinkedList is always better than ArrayList for insertions
   **Truth**: ArrayList is faster for most operations due to cache locality. LinkedList is only better for frequent insertions at both ends without random access.

2. **Myth**: HashMap is always O(1)
   **Truth**: HashMap is O(1) average case but O(n) worst case (all keys hash to same bucket). Java 8+ converts to red-black tree at 8 entries, making worst case O(log n).

3. **Myth**: Collections.synchronizedList is thread-safe
   **Truth**: It's thread-safe for individual operations but not compound operations (check-then-act). Use `ConcurrentHashMap` or explicit synchronization for compound operations.

4. **Myth**: `size() == 0` is faster than `isEmpty()`
   **Truth**: They're identical in performance. `isEmpty()` is more readable and preferred.

5. **Myth**: You must use `ArrayList` for random access
   **Truth**: Any `List` implementation supports random access. `ArrayList` is faster for it, but `LinkedList` also has `get(int index)` — it's just O(n).

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Store and manipulate groups of objects |
| Hierarchy | Collection (List, Set, Queue) + Map |
| ArrayList | Resizable array, O(1) random access |
| LinkedList | Doubly-linked nodes, O(1) insert/delete at ends |
| HashSet | Hash-based, O(1) add/contains/remove |
| HashMap | Hash-based, O(1) get/put |
| TreeMap | Tree-based, O(log n), sorted keys |
| Thread-safe | ConcurrentHashMap, CopyOnWriteArrayList |
| Best practice | Program to interfaces, choose right collection |
| Common mistake | ConcurrentModificationException |
| When to use | All Java applications |
| When to avoid | Never — collections are fundamental |
