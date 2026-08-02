# List Interface

## 1. Introduction

The `List` interface is an ordered collection (also known as a sequence) that allows duplicate elements. It extends the `Collection` interface and provides additional operations for positional access, search, iteration, and range-view. List is one of the most fundamental interfaces in the Java Collections Framework.

List implementations store elements in a sequence, where each element has an integer index (starting from 0). This allows random access to any element by its index, as well as efficient iteration from beginning to end.

The most common List implementations are:
- **ArrayList**: Dynamic array (fast random access, slow insertions)
- **LinkedList**: Doubly-linked list (fast insertions, slow random access)
- **Vector**: Legacy synchronized array (avoid in modern code)
- **CopyOnWriteArrayList**: Thread-safe for read-heavy scenarios

## 2. Learning Objectives

- Understand the List interface and its contract
- Learn the difference between List implementations
- Master List operations: add, get, set, remove, subList
- Understand index-based access and its performance implications
- Learn about List iterators (ListIterator)
- Compare ArrayList vs LinkedList for different scenarios
- Understand thread-safety options for List

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming (interfaces, inheritance)
- Module 03: Generics basics
- Basic understanding of arrays

## 4. Why This Concept Exists

Before List, developers had to:
1. Use arrays: Fixed size, manual management
2. Use Vector: Synchronized, slow
3. Implement their own data structures: Error-prone, inefficient

List provides:
1. **Dynamic sizing**: Grows and shrinks as needed
2. **Indexed access**: O(1) for ArrayList
3. **Rich API**: add, remove, search, sort, subList
4. **Multiple implementations**: Choose the right one for your needs
5. **Integration**: Works with all Collection APIs

## 5. Problem Statement

Consider building a to-do list application:
- Add tasks at any position
- Remove tasks by position
- Get task by position
- Reorder tasks
- Search for tasks

Without List, you'd need to:
- Use arrays and manage resizing manually
- Or implement your own linked list

With List, you simply choose ArrayList or LinkedList based on your needs.

## 6. Theory

### List Interface Contract

1. **Ordered**: Elements have a defined order (insertion order or index-based)
2. **Indexed**: Each element has an integer index (0-based)
3. **Duplicates**: Allows duplicate elements
4. **Null elements**: Allows multiple null elements

### List Operations

| Operation | ArrayList | LinkedList | Notes |
|-----------|-----------|------------|-------|
| add(E) | O(1)* | O(1) | Append to end |
| add(int, E) | O(n) | O(n)** | Insert at index |
| get(int) | O(1) | O(n) | Random access |
| set(int, E) | O(1) | O(n) | Replace at index |
| remove(int) | O(n) | O(n)** | Remove by index |
| remove(Object) | O(n) | O(n) | Remove by value |
| contains(Object) | O(n) | O(n) | Linear search |
| indexOf(Object) | O(n) | O(n) | Linear search |
| size() | O(1) | O(1) | Field access |
| subList(int, int) | O(1) | O(n) | View (ArrayList), traversal (LinkedList) |

*Amortized O(1) for ArrayList (occasional resize is O(n))
**O(n) to find the node, then O(1) to insert/remove

## 7. Internal Working

### ArrayList Internal Structure

```java
// ArrayList uses a dynamic array
private transient Object[] elementData;
private int size;

// When adding and array is full:
// 1. Create new array with 1.5x capacity
// 2. Copy all elements using Arrays.copyOf()
// 3. Replace old array reference
```

### LinkedList Internal Structure

```java
// LinkedList uses a doubly-linked list
transient int size = 0;
transient Node<E> first;
transient Node<E> last;

private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;
    
    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

### ListIterator

ListIterator extends Iterator for bidirectional traversal:
- `hasPrevious()`: Check if previous element exists
- `previous()`: Get previous element
- `nextIndex()`: Get index of next element
- `previousIndex()`: Get index of previous element
- `set(E)`: Replace current element
- `add(E)`: Insert element at current position

## 8. JVM Perspective

### Memory Allocation

```java
List<String> list = new ArrayList<>();
// ArrayList object: ~32 bytes
// Backing array: 10 references × 8 bytes = 80 bytes (default capacity)

List<String> linkedList = new LinkedList<>();
// LinkedList object: ~32 bytes
// Each Node: ~40 bytes (item + next + prev + object header)
```

### JIT Optimization

The JIT compiler optimizes List operations:
- Inline `get()` and `set()` for ArrayList
- Optimize bounds checking
- Devirtualize calls when concrete type is known

## 9. Memory Representation

```
ArrayList<String> list:
┌───────────────────────────────┐
│ ArrayList object              │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ elementData ──────────────────┐
│ size = 3 (4 bytes)            │
└───────────────────────────────┘
                                │
                                ▼
                         Object[] elementData
                         ┌────────────────────┐
                         │ [0] → "Hello"      │
                         │ [1] → "World"      │
                         │ [2] → "Java"       │
                         │ [3] → null         │
                         └────────────────────┘

LinkedList<String> list:
┌───────────────────────────────┐
│ LinkedList object             │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ size = 3 (4 bytes)            │
│ first ──────────────────────────┐
│ last ───────────────────────────┼──┐
└───────────────────────────────┘  │  │
                                   ▼  │
                          Node "Hello" │
                          ┌────────────┐
                          │ prev=null  │
                          │ item="Hello"│
                          │ next ──────────→ Node "World"
                          └────────────┘
```

## 10. Syntax

```java
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;

// ============================================
// CREATION
// ============================================
List<E> list = new ArrayList<>();           // Empty ArrayList
List<E> list = new ArrayList<>(100);       // With initial capacity
List<E> list = new ArrayList<>(collection); // From collection
List<E> list = new LinkedList<>();          // Empty LinkedList
List<E> list = List.of("a", "b", "c");     // Immutable (Java 9+)
List<E> list = new ArrayList<>(List.of("a", "b")); // Mutable copy

// ============================================
// ADDING ELEMENTS
// ============================================
list.add(element);              // Append to end
list.add(index, element);       // Insert at index
list.addAll(collection);        // Add all from collection
list.addAll(index, collection); // Add all at index

// ============================================
// ACCESSING ELEMENTS
// ============================================
E element = list.get(index);           // O(1) for ArrayList
list.set(index, element);              // Replace at index
int index = list.indexOf(element);     // First occurrence
int lastIndex = list.lastIndexOf(element); // Last occurrence

// ============================================
// REMOVING ELEMENTS
// ============================================
E removed = list.remove(index);        // Remove by index
boolean success = list.remove(object); // Remove by value
list.removeIf(predicate);              // Conditional removal
list.clear();                          // Remove all

// ============================================
// SEARCHING
// ============================================
boolean has = list.contains(element);  // O(n)
int index = list.indexOf(element);     // O(n)
boolean empty = list.isEmpty();        // O(1)
int size = list.size();                // O(1)

// ============================================
// SUBLIST (view, not copy)
// ============================================
List<E> sub = list.subList(fromIndex, toIndex); // [from, to)
sub.set(0, newValue); // Modifies original list!
list.subList(0, 3).clear(); // Removes from original

// ============================================
// SORTING
// ============================================
Collections.sort(list);                    // Natural order
list.sort(Comparator.naturalOrder());     // Natural order
list.sort(Comparator.reverseOrder());     // Reverse order
list.sort(Comparator.comparing(String::length)); // Custom

// ============================================
// CONVERSIONS
// ============================================
Object[] array = list.toArray();
String[] array = list.toArray(new String[0]);
List<String> copy = new ArrayList<>(list);

// ============================================
// ITERATION
// ============================================
// Enhanced for loop
for (E element : list) {
    System.out.println(element);
}

// Iterator
Iterator<E> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ListIterator (bidirectional)
ListIterator<E> lit = list.listIterator();
while (lit.hasNext()) {
    System.out.println(lit.nextIndex() + ": " + lit.next());
}

// forEach with lambda
list.forEach(System.out::println);

// Stream
list.stream().filter(e -> ...).forEach(System.out::println);

// ============================================
// THREAD SAFETY
// ============================================
List<E> syncList = Collections.synchronizedList(new ArrayList<>());
List<E> copyOnWrite = new java.util.concurrent.CopyOnWriteArrayList<>();

// ============================================
// IMMUTABLE VIEWS
// ============================================
List<E> unmodifiable = Collections.unmodifiableList(list);
List<E> immutable = List.copyOf(list); // Truly immutable
```

## 11. Easy Example

```java
import java.util.*;

public class ListBasics {
    public static void main(String[] args) {
        // Create and populate
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Apple"); // Duplicate allowed

        System.out.println("List: " + fruits);
        System.out.println("Size: " + fruits.size());

        // Access by index
        System.out.println("First: " + fruits.get(0));
        System.out.println("Last: " + fruits.get(fruits.size() - 1));

        // Search
        System.out.println("Contains Apple: " + fruits.contains("Apple"));
        System.out.println("Index of Banana: " + fruits.indexOf("Banana"));

        // Remove
        fruits.remove("Banana");
        fruits.remove(0);
        System.out.println("After removal: " + fruits);

        // Add at index
        fruits.add(0, "Mango");
        System.out.println("After add: " + fruits);

        // Sort
        fruits.sort(String::compareToIgnoreCase);
        System.out.println("Sorted: " + fruits);

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
import java.util.*;
import java.util.stream.Collectors;

public class ListOperations {
    public static void main(String[] args) {
        // Remove duplicates while preserving order
        List<Integer> withDuplicates = List.of(1, 2, 3, 1, 2, 4, 5, 3);
        List<Integer> withoutDuplicates = new ArrayList<>();
        for (Integer num : withDuplicates) {
            if (!withoutDuplicates.contains(num)) {
                withoutDuplicates.add(num);
            }
        }
        System.out.println("Without duplicates: " + withoutDuplicates);

        // Rotate list
        List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        Collections.rotate(numbers, 2);
        System.out.println("Rotated: " + numbers);

        // Chunk list
        List<List<Integer>> chunks = chunk(List.of(1, 2, 3, 4, 5, 6, 7), 3);
        System.out.println("Chunks: " + chunks);

        // Interleave lists
        List<String> list1 = List.of("A", "B", "C");
        List<String> list2 = List.of("1", "2", "3");
        List<String> interleaved = interleave(list1, list2);
        System.out.println("Interleaved: " + interleaved);

        // Find common elements
        List<String> l1 = List.of("A", "B", "C", "D");
        List<String> l2 = List.of("C", "D", "E", "F");
        Set<String> common = new HashSet<>(l1);
        common.retainAll(l2);
        System.out.println("Common: " + common);
    }

    static <T> List<List<T>> chunk(List<T> list, int size) {
        List<List<T>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            chunks.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
        }
        return chunks;
    }

    static <T> List<T> interleave(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>(a.size() + b.size());
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            result.add(a.get(i++));
            result.add(b.get(j++));
        }
        while (i < a.size()) result.add(a.get(i++));
        while (j < b.size()) result.add(b.get(j++));
        return result;
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AdvancedListPatterns {
    public static void main(String[] args) {
        // Pattern 1: Custom ArrayList with capacity tracking
        System.out.println("=== Capacity Tracking ===");
        TrackedArrayList<String> tracked = new TrackedArrayList<>(4);
        for (int i = 0; i < 10; i++) {
            tracked.add("Item" + i);
            System.out.printf("Added Item%d: size=%d, capacity=%d%n",
                i, tracked.size(), tracked.getCapacity());
        }

        // Pattern 2: Thread-safe list operations
        System.out.println("\n=== Thread-Safe Lists ===");
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        List<String> cowList = new CopyOnWriteArrayList<>();

        // Pattern 3: List as a stack
        System.out.println("\n=== List as Stack ===");
        ArrayList<String> stack = new ArrayList<>();
        stack.add("First");
        stack.add("Second");
        stack.add("Third");
        System.out.println("Pop: " + stack.remove(stack.size() - 1));

        // Pattern 4: List as a queue (bad practice)
        System.out.println("\n=== List as Queue (bad) ===");
        ArrayList<String> queue = new ArrayList<>();
        queue.add("A");
        queue.add("B");
        queue.add("C");
        String dequeued = queue.remove(0); // O(n) - bad!
        System.out.println("Dequeued: " + dequeued);
    }

    static class TrackedArrayList<E> extends ArrayList<E> {
        private int capacity;

        public TrackedArrayList(int initialCapacity) {
            super(initialCapacity);
            this.capacity = initialCapacity;
        }

        @Override
        public boolean add(E e) {
            boolean result = super.add(e);
            if (size() > capacity) {
                capacity = size();
            }
            return result;
        }

        public int getCapacity() {
            return capacity;
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TaskManagementSystem {
    private final List<Task> tasks;
    private final List<TaskHistory> history;

    public TaskManagementSystem() {
        this.tasks = new CopyOnWriteArrayList<>();
        this.history = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
        history.add(new TaskHistory(task.id(), "CREATED", new Date()));
    }

    public Optional<Task> findTask(String taskId) {
        return tasks.stream()
            .filter(t -> t.id().equals(taskId))
            .findFirst();
    }

    public List<Task> getTasksByStatus(Task.Status status) {
        return tasks.stream()
            .filter(t -> t.status() == status)
            .collect(Collectors.toList());
    }

    public List<Task> getHighPriorityTasks() {
        return tasks.stream()
            .filter(t -> t.priority() >= 3)
            .sorted(Comparator.comparingInt(Task::priority).reversed())
            .collect(Collectors.toList());
    }

    public Map<Task.Status, Long> getTaskStatusSummary() {
        return tasks.stream()
            .collect(Collectors.groupingBy(
                Task::status,
                Collectors.counting()
            ));
    }

    public void updateTaskStatus(String taskId, Task.Status newStatus) {
        tasks.replaceAll(task -> {
            if (task.id().equals(taskId)) {
                history.add(new TaskHistory(taskId, "STATUS_CHANGED", new Date()));
                return new Task(taskId, task.title(), task.description(),
                    newStatus, task.priority(), task.createdAt());
            }
            return task;
        });
    }

    public static void main(String[] args) {
        TaskManagementSystem system = new TaskManagementSystem();

        system.addTask(new Task("T1", "Task 1", "Description 1",
            Task.Status.PENDING, 3, new Date()));
        system.addTask(new Task("T2", "Task 2", "Description 2",
            Task.Status.IN_PROGRESS, 2, new Date()));
        system.addTask(new Task("T3", "Task 3", "Description 3",
            Task.Status.COMPLETED, 1, new Date()));

        System.out.println("=== Task Status Summary ===");
        system.getTaskStatusSummary().forEach((status, count) ->
            System.out.println("  " + status + ": " + count)
        );

        System.out.println("\n=== High Priority Tasks ===");
        system.getHighPriorityTasks().forEach(task ->
            System.out.println("  " + task.title() + " (priority: " + task.priority() + ")")
        );
    }

    record Task(String id, String title, String description,
                Status status, int priority, Date createdAt) {
        enum Status { PENDING, IN_PROGRESS, COMPLETED, CANCELLED }
    }

    record TaskHistory(String taskId, String action, Date timestamp) {}
}
```

## 15. Performance

### Time Complexity

| Operation | ArrayList | LinkedList | Notes |
|-----------|-----------|------------|-------|
| add(E) | O(1)* | O(1) | Append to end |
| add(0, E) | O(n) | O(1) | Insert at beginning |
| add(index, E) | O(n) | O(n) | Insert at index |
| get(index) | O(1) | O(n) | Random access |
| set(index, E) | O(1) | O(n) | Replace at index |
| remove(index) | O(n) | O(n) | Remove by index |
| remove(Object) | O(n) | O(n) | Remove by value |
| contains(Object) | O(n) | O(n) | Linear search |
| indexOf(Object) | O(n) | O(n) | Linear search |
| size() | O(1) | O(1) | Field access |
| subList() | O(1) | O(n) | View vs traversal |

*Amortized O(1) for ArrayList

### ArrayList vs LinkedList

| Operation | ArrayList | LinkedList | Winner |
|-----------|-----------|------------|--------|
| get(index) | O(1) | O(n) | ArrayList |
| add(end) | O(1)* | O(1) | Tie |
| add(beginning) | O(n) | O(1) | LinkedList |
| remove(beginning) | O(n) | O(1) | LinkedList |
| iteration | O(n) | O(n) | ArrayList (cache) |
| memory | Less | More | ArrayList |

## 16. Best Practices

1. **Program to interfaces**: Use `List<E>` instead of `ArrayList<E>` for declarations
2. **Set initial capacity**: For ArrayList with known sizes
3. **Use enhanced for loop**: Cleaner than index-based loops
4. **Prefer ArrayList**: For most use cases (better cache locality)
5. **Use LinkedList**: For frequent insertions at beginning (but consider ArrayDeque)
6. **Use subList for views**: Don't create copies for range operations
7. **Thread safety**: Use `Collections.synchronizedList()` or `CopyOnWriteArrayList`

## 17. Common Mistakes

```java
// Mistake 1: Using LinkedList for random access
// Bad - O(n) for each get
for (int i = 0; i < linkedList.size(); i++) {
    process(linkedList.get(i));
}

// Good - O(1) for each get
for (int i = 0; i < arrayList.size(); i++) {
    process(arrayList.get(i));
}

// Mistake 2: Modifying list during enhanced for loop
for (String s : list) {
    if (s.isEmpty()) {
        list.remove(s); // ConcurrentModificationException!
    }
}

// Mistake 3: Not using subList correctly
List<String> original = new ArrayList<>(List.of("A", "B", "C"));
List<String> sub = original.subList(0, 2);
sub.clear(); // Also clears original!

// Mistake 4: Using ArrayList as queue
// Bad - O(n) for remove(0)
ArrayList<String> queue = new ArrayList<>();
queue.remove(0); // O(n)

// Good - O(1) for poll()
ArrayDeque<String> queue = new ArrayDeque<>();
queue.poll(); // O(1)
```

## 18. Pitfalls

### SubList is a View
Changes to subList affect the original list and vice versa. Create a copy if you need independence.

### ConcurrentModificationException
Modifying list during enhanced for loop or Iterator causes this exception. Use Iterator.remove() or removeIf().

### Thread Safety
List is NOT thread-safe. Use Collections.synchronizedList() or CopyOnWriteArrayList for concurrent access.

### Null Elements
List allows multiple null elements. This can cause issues with some algorithms.

### Memory Overhead
LinkedList uses more memory per element (24 bytes) vs ArrayList (4 bytes).

## 19. Debugging Tips

1. **Print list contents**: Use System.out.println() for debugging
2. **Use IDE debugger**: Inspect internal state
3. **Check for nulls**: Use list.contains(null) to detect null elements
4. **Monitor size**: Verify expected element count
5. **Use assertions**: Check list invariants
6. **Profile memory**: Use JProfiler to check memory usage

## 20. Comparison Table

| Feature | List | Set | Queue | Deque |
|---------|------|-----|-------|-------|
| Ordered | Yes | Depends | Yes | Yes |
| Duplicates | Yes | No | Yes | Yes |
| Indexed | Yes | No | No | No |
| Null elements | Multiple | One | Multiple | Multiple |
| Interface | Collection | Collection | Collection | Collection |

## 21. Decision Tree

```
Need a List?
├── Yes → Need random access by index?
│   ├── Yes → ArrayList (default)
│   └── No → Need frequent insertions at beginning?
│       ├── Yes → LinkedList (but consider ArrayDeque)
│       └── No → ArrayList (usually still better)
├── Need thread safety?
│   └── Yes → CopyOnWriteArrayList or synchronizedList
└── Need immutable list?
    └── Use List.of() or List.copyOf()
```

## 22. Interview Questions

### Q1: What is the difference between ArrayList and LinkedList?
**A**: ArrayList uses a dynamic array (O(1) random access, O(n) insertions). LinkedList uses a doubly-linked list (O(1) insertions at ends, O(n) random access). ArrayList is preferred for most use cases due to cache locality.

### Q2: How does ArrayList resize itself?
**A**: When the backing array is full, a new array with 1.5x capacity is created. All elements are copied using Arrays.copyOf(). The old array becomes garbage.

### Q3: What is the time complexity of List operations?
**A**: ArrayList: get/set O(1), add(end) O(1)*, add(index) O(n), remove O(n). LinkedList: add/remove at ends O(1), get/set O(n).

### Q4: What is ConcurrentModificationException?
**A**: Thrown when a collection is modified structurally while being iterated. Use Iterator.remove() or removeIf() for safe removal during iteration.

### Q5: When would you use LinkedList over ArrayList?
**A**: Rarely. LinkedList is better for frequent insertions at the beginning or when using as a Deque. For most use cases, ArrayList is superior.

### Q6: How do you make a List thread-safe?
**A**: Use Collections.synchronizedList() for simple synchronization, or CopyOnWriteArrayList for read-heavy scenarios.

### Q7: What is the difference between List.of() and new ArrayList<>()?
**A**: List.of() returns an immutable list (can't add/remove elements). new ArrayList<>() returns a mutable list.

## 23. Exercises

### Exercise 1: Find Second Largest
Given a List of integers, find the second largest element.

### Exercise 2: Rotate List
Implement a method to rotate a list by k positions.

### Exercise 3: Remove Duplicates
Remove duplicates from a List while preserving order.

### Exercise 4: Chunk List
Split a List into chunks of specified size.

## 24. Assignments

### Assignment 1: Task Manager
Build a task management system using List:
- Add/remove/edit tasks
- Search by name or priority
- Sort by different criteria
- Export to file

### Assignment 2: Contact Book
Create a contact management system:
- Store contacts in List
- Search by name, phone, or email
- Sort by name or creation date
- Handle duplicates

## 25. Mini Project

### Shopping Cart System

Build a shopping cart using List:

```java
// Features:
// 1. Add/remove items
// 2. Update quantities
// 3. Calculate total
// 4. Apply discounts
// 5. Export to file
// 6. Handle concurrent access
```

**Requirements:**
- Use ArrayList for main storage
- Implement Comparable for sorting
- Handle thread safety
- Support undo/redo

## 26. Summary

List is an ordered collection that allows duplicates:

- **Implementations**: ArrayList (array), LinkedList (linked list), Vector (legacy)
- **Performance**: ArrayList for random access, LinkedList for insertions at ends
- **Operations**: add, get, set, remove, contains, indexOf, subList
- **Iteration**: Enhanced for, Iterator, ListIterator, forEach, Stream
- **Thread safety**: Collections.synchronizedList(), CopyOnWriteArrayList
- **Best for**: Sequential data with index-based access

## 27. References

### Official Documentation
- [List Interface](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)
- [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)
- [LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)

### Books
- *Effective Java* by Joshua Bloch

### Online Resources
- [Baeldung List Guide](https://www.baeldung.com/java-list)
- [GeeksforGeeks List](https://www.geeksforgeeks.org/list-interface-java-examples/)

### Related Topics
- [ArrayList](../03-arraylist/README.md)
- [LinkedList](../04-linkedlist/README.md)
- [Iterator](../24-iterator/README.md)
