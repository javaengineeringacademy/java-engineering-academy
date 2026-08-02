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
        System.out.println("\nK closest to 5:");
        TreeSet<Integer> numbers = new TreeSet<>(Set.of(1, 3, 5, 7, 9, 11, 13));
        int target = 6;
        int k = 3;
        System.out.println("  " + findKClosest(numbers, target, k));

        // Range query
        System.out.println("\nNumbers between 3 and 10:");
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
        System.out.println("\n=== Sliding Window Median ===");
        int[] data = {1, 3, -1, -3, 5, 3, 6, 7};
        int windowSize = 3;
        List<Double> medians = slidingWindowMedian(data, windowSize);
        System.out.println("  Medians: " + medians);

        // Pattern 3: Find k-th smallest
        System.out.println("\n=== K-th Smallest ===");
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
        TreeSet<Integer> window = new TreeSet<>();

        for (int i = 0; i < data.length; i++) {
            window.add(data[i]);
            if (i >= k) {
                window.remove(data[i - k]);
            }
            if (i >= k - 1) {
                Iterator<Integer> it = window.iterator();
                for (int j = 0; j < k / 2; j++) it.next();
                if (k % 2 == 0) {
                    medians.add((double) (it.next() + window.first()) / 2);
                } else {
                    medians.add((double) it.next());
                }
            }
        }
        return medians;
    }

    static int findKthSmallest(TreeSet<Integer> set, int k) {
        Iterator<Integer> it = set.iterator();
        for (int i = 0; i < k - 1; i++) {
            it.next();
        }
        return it.next();
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class PriorityQueueSystem {
    private final TreeSet<Task> taskQueue;
    private final Map<String, Task> taskIndex;

    public PriorityQueueSystem() {
        this.taskQueue = new TreeSet<>(
            Comparator.comparingInt(Task::priority)
                     .thenComparing(Task::createdAt)
        );
        this.taskIndex = new HashMap<>();
    }

    public void addTask(Task task) {
        taskQueue.add(task);
        taskIndex.put(task.id(), task);
    }

    public Optional<Task> pollTask() {
        Task task = taskQueue.pollFirst();
        if (task != null) {
            taskIndex.remove(task.id());
        }
        return Optional.ofNullable(task);
    }

    public List<Task> getTasksByPriority(int minPriority, int maxPriority) {
        Task minTask = new Task("", minPriority, "", new Date(0));
        Task maxTask = new Task("", maxPriority, "", new Date(Long.MAX_VALUE));
        return taskQueue.subSet(minTask, true, maxTask, true).stream()
            .collect(Collectors.toList());
    }

    public Optional<Task> peekHighestPriority() {
        return Optional.ofNullable(taskQueue.first());
    }

    public int getQueueSize() {
        return taskQueue.size();
    }

    public static void main(String[] args) {
        PriorityQueueSystem system = new PriorityQueueSystem();

        system.addTask(new Task("T1", 1, "Low priority", new Date()));
        system.addTask(new Task("T2", 3, "High priority", new Date()));
        system.addTask(new Task("T3", 2, "Medium priority", new Date()));
        system.addTask(new Task("T4", 3, "Another high", new Date()));

        System.out.println("=== Task Queue ===");
        System.out.println("Queue size: " + system.getQueueSize());

        System.out.println("\nHighest priority task:");
        system.peekHighestPriority().ifPresent(task ->
            System.out.println("  " + task.description() + " (priority: " + task.priority() + ")")
        );

        System.out.println("\nProcessing tasks:");
        while (system.getQueueSize() > 0) {
            system.pollTask().ifPresent(task ->
                System.out.println("  Processing: " + task.description())
            );
        }
    }

    record Task(String id, int priority, String description, Date createdAt) {}
}
```

## 15. Performance

### Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| add() | O(log n) | O(log n) | Red-black tree |
| remove() | O(log n) | O(log n) | Red-black tree |
| contains() | O(log n) | O(log n) | Tree traversal |
| first() | O(log n) | O(log n) | Leftmost node |
| last() | O(log n) | O(log n) | Rightmost node |
| subSet() | O(log n + k) | O(log n + k) | k = range size |
| floor() | O(log n) | O(log n) | Tree traversal |
| iteration | O(n) | O(n) | In-order traversal |

### TreeSet vs HashSet vs LinkedHashSet

| Feature | TreeSet | HashSet | LinkedHashSet |
|---------|---------|---------|---------------|
| Structure | Red-black tree | Hash table | Hash table + linked list |
| Ordering | Sorted | None | Insertion/access |
| add() | O(log n) | O(1) | O(1) |
| remove() | O(log n) | O(1) | O(1) |
| contains() | O(log n) | O(1) | O(1) |
| Memory | More | Less | More |
| Null elements | None | One | One |

## 16. Best Practices

1. **Override compareTo() consistently**: For custom element classes
2. **Use natural ordering when possible**: Simpler code
3. **Prefer TreeSet for sorted data**: When order matters
4. **Use descendingSet()**: For reverse iteration
5. **Thread safety**: Use ConcurrentSkipListSet for concurrent access
6. **Set initial capacity**: Not applicable (tree-based, no resize)
7. **Use NavigableSet methods**: For efficient element lookups

## 17. Common Mistakes

```java
// Mistake 1: Adding null elements
TreeSet<String> set = new TreeSet<>();
set.add(null); // NullPointerException!

// Mistake 2: Inconsistent compareTo()
class BadElement implements Comparable<BadElement> {
    int value;
    public int compareTo(BadElement other) {
        return value - other.value; // Overflow possible!
    }
}

// Good - use Integer.compare()
class GoodElement implements Comparable<GoodElement> {
    int value;
    public int compareTo(GoodElement other) {
        return Integer.compare(value, other.value);
    }
}

// Mistake 3: Assuming equals() consistency
// compareTo() == 0 does NOT imply equals() == true

// Mistake 4: Using TreeSet when order doesn't matter
// TreeSet is slower than HashSet for basic operations
```

## 18. Pitfalls

### No Null Elements
TreeSet does NOT allow null elements (throws NullPointerException). This is different from HashSet which allows one null.

### compareTo() Contract
compareTo() must be consistent with equals(). Violating this causes unexpected behavior.

### Performance Overhead
TreeSet has O(log n) for all operations, while HashSet has O(1) average.

### Thread Safety
TreeSet is NOT thread-safe. Use ConcurrentSkipListSet for concurrent access.

## 19. Debugging Tips

1. **Override toString()**: For custom element classes
2. **Check compareTo()**: Verify consistency with equals()
3. **Use debugger**: Inspect tree structure
4. **Monitor tree height**: Verify balance
5. **Profile memory**: Use JProfiler to check memory usage

## 20. Comparison Table

| Feature | TreeSet | HashSet | LinkedHashSet | EnumSet |
|---------|---------|---------|---------------|---------|
| Structure | Red-black tree | Hash table | Hash table + linked list | Bit vector |
| Ordering | Sorted | None | Insertion/access | Enum natural |
| Performance | O(log n) | O(1) | O(1) | O(1) |
| Null elements | None | One | One | None |
| Thread-safe | No | No | No | No |

## 21. Decision Tree

```
Need a Set?
├── Yes → Need sorted elements?
│   ├── Yes → TreeSet
│   └── No → Need insertion order?
│       ├── Yes → LinkedHashSet
│       └── No → HashSet
├── Need range operations?
│   └── Yes → TreeSet
└── Need navigation methods?
    └── Yes → TreeSet
```

## 22. Interview Questions

### Q1: How does TreeSet use TreeMap internally?
**A**: TreeSet maintains a TreeMap where elements are keys and all values are a shared PRESENT object. `add()` calls `m.put(e, PRESENT)` and checks if the return is null.

### Q2: Can TreeSet contain null elements?
**A**: No. TreeSet does not allow null elements because it uses compareTo() or comparator, which would throw NullPointerException.

### Q3: What is the difference between TreeSet and HashSet?
**A**: TreeSet uses a red-black tree (O(log n) operations, sorted order). HashSet uses a hash table (O(1) operations, no order). TreeSet is better for sorted data.

### Q4: What is the time complexity of TreeSet operations?
**A**: O(log n) for add, remove, contains. O(1) for size. O(log n + k) for subSet where k is the range size.

### Q5: When would you use TreeSet over HashSet?
**A**: When you need elements in sorted order, need range operations (subSet, headSet, tailSet), or need navigation methods (floor, ceiling, lower, higher).

### Q6: How do you sort a TreeSet in reverse order?
**A**: Pass a reverse comparator: `new TreeSet<>(Comparator.reverseOrder())`.

### Q7: Is TreeSet thread-safe?
**A**: No. Use `Collections.synchronizedSortedSet()` or `ConcurrentSkipListSet` for concurrent access.

## 23. Exercises

### Exercise 1: Find K Closest Elements
Given a TreeSet and a target value, find the k closest elements.

### Exercise 2: Merge Intervals
Given a set of intervals, merge overlapping intervals using TreeSet.

### Exercise 3: Sliding Window Median
Find the median of each sliding window in an array using TreeSet.

## 24. Assignments

### Assignment 1: Priority Task Queue
Build a priority task queue using TreeSet that supports:
- Adding tasks with priorities
- Polling highest priority task
- Finding tasks by priority range
- Removing tasks

### Assignment 2: Online Exam System
Create an exam system that:
- Stores questions in sorted order
- Allows range queries by difficulty
- Tracks student scores
- Generates ranked reports

## 25. Mini Project

### Conference Room Booking System

Build a booking system using TreeSet:

```java
// Features:
// 1. Book rooms for time slots
// 2. Find available rooms
// 3. Detect conflicts
// 4. Priority booking
// 5. Calendar view
```

**Requirements:**
- Use TreeSet for time slot storage
- Implement conflict detection
- Handle concurrent bookings
- Export booking history

## 26. Summary

TreeSet is the sorted Set implementation based on red-black trees:

- **Internal structure**: Red-black tree (via TreeMap)
- **Performance**: O(log n) for all operations
- **Ordering**: Sorted by natural order or custom Comparator
- **Null elements**: Not allowed
- **Range operations**: subSet, headSet, tailSet
- **Navigation**: floor, ceiling, lower, higher
- **Best for**: Sorted iteration, range operations, navigation

## 27. References

### Official Documentation
- [TreeSet JavaDoc](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/TreeSet.html)
- [SortedSet Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/SortedSet.html)
- [NavigableSet Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/NavigableSet.html)

### Books
- *Effective Java* by Joshua Bloch

### Online Resources
- [Baeldung TreeSet Guide](https://www.baeldung.com/java-treeset)
- [GeeksforGeeks TreeSet](https://www.geeksforgeeks.org/treeset-in-java/)

### Related Topics
- [HashSet](../11-hashset/README.md)
- [LinkedHashSet](../12-linkedhashset/README.md)
- [TreeMap](../17-treemap/README.md)
