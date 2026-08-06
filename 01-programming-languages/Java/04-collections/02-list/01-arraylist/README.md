# ArrayList

## 1. Introduction

ArrayList is the most widely used implementation of the `List` interface in Java. It uses a dynamic array internally, providing O(1) random access to elements and efficient iteration. Think of ArrayList as a resizable array that automatically grows and shrinks as you add or remove elements.

ArrayList is the default choice for most List use cases because arrays are the most efficient data structure for indexed access, and ArrayList adds the convenience of dynamic resizing. It provides the best balance of performance, memory efficiency, and ease of use for the majority of real-world scenarios.

The internal backing array (`elementData`) is allocated with some extra capacity beyond the current size. When the array fills up, a new array is created with 1.5x the previous capacity (default 10 → 15 → 22 → 33 → ...), and all elements are copied over. This amortized approach ensures that most `add()` operations are O(1) even though occasional resizing is O(n).

## 2. Learning Objectives

- Create and use ArrayList with generics
- Understand ArrayList's internal dynamic array mechanism
- Learn ArrayList performance characteristics (O(1) access, O(n) insertion/removal)
- Master common operations: add, get, set, remove, subList, sort
- Compare ArrayList vs LinkedList with concrete performance benchmarks
- Understand capacity vs size and initial capacity optimization
- Learn thread-safety considerations for ArrayList
- Recognize when ArrayList is NOT the right choice

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming (interfaces, inheritance)
- Module 03: Generics basics
- Basic understanding of arrays and their limitations
- Familiarity with the List interface

## 4. Why This Concept Exists

Arrays in Java are fixed-size: once created, you cannot add or remove elements. This is a significant limitation for real-world applications where data sizes are dynamic. Before ArrayList, developers had to manually:
1. Create arrays of estimated size
2. Track the current size separately
3. Create new arrays and copy elements when full
4. Handle null values for unused slots

ArrayList solves all these problems by:
- **Automatic resizing**: Grows dynamically as elements are added
- **Simplified API**: `add()`, `get()`, `remove()` without manual array management
- **Type safety**: Generic type parameter prevents ClassCastException
- **Integration**: Works with all Collection APIs (streams, iterators, algorithms)

## 4b. Why ArrayList Is Backed by Object[]

ArrayList uses an `Object[]` array as its internal storage. This design choice is deliberate and optimal for several reasons.

**Array is the fastest data structure for indexed access.** An array provides O(1) random access because elements are stored contiguously in memory. The JVM calculates the address of any element with a single arithmetic operation: `baseAddress + (index * elementSize)`. No pointer chasing, no tree traversal — just a direct memory offset. This is as fast as memory access gets.

**ArrayList adds dynamic resizing on top of raw speed.** A plain Java array has a fixed size. ArrayList wraps the array and automatically grows it (by 1.5x) when capacity is exceeded. The amortized cost of `add()` remains O(1) because resizing happens infrequently — the geometric growth ensures that the total copies across n insertions sum to O(n).

**Why not LinkedList?** LinkedList stores elements as doubly-linked nodes scattered across the heap. Each node is a separate object with two pointers (prev, next) plus the element reference — that's ~48 bytes of overhead per element versus ~8 bytes for an array reference. More critically, LinkedList has poor **cache locality**: traversing a linked list causes cache misses because nodes are non-contiguous in memory. Modern CPUs prefetch sequential memory; linked lists defeat this optimization. Benchmarks consistently show ArrayList outperforms LinkedList for iteration, random access, and even many insertion patterns.

**Why not Vector?** Vector is synchronized — every method acquires a monitor lock. In single-threaded code (the vast majority of use cases), this synchronization is pure overhead. Even in multi-threaded code, Vector's coarse-grained locking (locking the entire collection for each operation) provides no real benefit over explicit synchronization when needed. ArrayList lets you choose your synchronization strategy: use `Collections.synchronizedList()` or `CopyOnWriteArrayList` only when you actually need thread safety.

**Summary:**

| Factor | Object[] (ArrayList) | Linked Nodes (LinkedList) | Synchronized (Vector) |
|--------|---------------------|--------------------------|----------------------|
| Access time | O(1) | O(n) | O(1) + lock overhead |
| Memory per element | ~8 bytes ref | ~48 bytes node | ~8 bytes + lock |
| Cache performance | Excellent (contiguous) | Poor (scattered) | Good + lock overhead |
| Thread-safe | No (by design) | No | Yes (always) |

## 5. Problem Statement

Consider building a shopping cart for an e-commerce application:
- Items are added as users browse
- Items can be removed at any time
- The cart must display items in order
- Users may have 1 item or 1000 items
- The cart must support quick access to calculate totals

A fixed-size array would fail because we don't know the cart size upfront. A LinkedList would work but provide slower random access for calculating totals. ArrayList provides the optimal solution: dynamic sizing with O(1) indexed access.

## 6. Theory

### Internal Structure

ArrayList maintains:
- `transient Object[] elementData`: The backing array
- `private int size`: Number of elements (not the array length)

### Resizing Mechanism

When `add()` is called and the array is full:
1. New capacity = old capacity + (old capacity >> 1) (1.5x)
2. A new array of the new capacity is created
3. `Arrays.copyOf()` copies all elements to the new array
4. The old array becomes eligible for garbage collection

### Growth Factor Analysis

| Initial | After 10 adds | After 100 adds | After 1000 adds |
|---------|---------------|----------------|-----------------|
| 10 | 15 | 169 | 1706 |

The 1.5x growth factor is a balance between:
- **Too small (1.1x)**: Frequent resizing, O(n) copies
- **Too large (2x)**: Wasted memory (up to 50% unused)
- **1.5x**: At most 33% wasted space, O(n/3) total copies for n elements

### Amortized Analysis

Adding n elements to an initially empty ArrayList:
- Total cost = n + n/2 + n/4 + ... + 1 ≈ 2n
- Amortized cost per add = O(1)

### modCount for Fail-Fast Iterators

ArrayList maintains a `modCount` field that increments on structural modifications. Iterators check this value to detect concurrent modification:
```java
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
```

## 7. Internal Working

### The add() Operation

```java
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount
    elementData[size++] = e;
    return true;
}

private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;  // For fail-fast iterators
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5x
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

### The get() Operation

```java
public E get(int index) {
    rangeCheck(index);  // O(1) bounds check
    return elementData(index);  // O(1) array access
}

private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

E elementData(int index) {
    return (E) elementData[index];  // Direct array access
}
```

### The remove() Operation

```java
public E remove(int index) {
    rangeCheck(index);
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null; // Help GC
    return oldValue;
}
```

## 8. JVM Perspective

### Memory Allocation

```java
List<String> list = new ArrayList<>();
// JVM allocates:
// - ArrayList object header: 12 bytes (mark word + klass pointer)
// - elementData reference: 8 bytes (pointer to backing array)
// - size field: 4 bytes
// - Padding to 8-byte boundary: 4 bytes
// Total ArrayList object: ~32 bytes

// When adding elements:
// - Backing array: 10 references × 8 bytes = 80 bytes (default capacity)
// - Each String reference in array: 8 bytes
```

### JIT Optimization

The JIT compiler optimizes ArrayList operations:
- **Inlining**: `get()` and `set()` methods are inlined for direct array access
- **Bounds check elimination**: JIT can eliminate redundant range checks
- **Escape analysis**: Small ArrayLists may be scalar-replaced
- **Loop unrolling**: Enhanced for loops over ArrayList are optimized

### Garbage Collection Impact

- Removed elements set to `null` to help GC
- Resizing creates garbage (old array)
- Large ArrayLists may be stored in Old Gen
- Weak references can be used for caching

## 9. Memory Representation

```
```
ArrayList<String> list = new ArrayList<>(4);
list.add("Hello");
list.add("World");
list.add("Java");

Memory layout:
┌───────────────────────────────┐
│ ArrayList object              │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ elementData ──────────────────────┐
│ size = 3 (4 bytes)            │      │
│ (padding 4 bytes)             │      │
└───────────────────────────────┘      │
                                       ▼
                               Object[] elementData
                               ┌──────────────────┐
                               │ [0] → "Hello"    │ (8 bytes ref)
                               │ [1] → "World"    │ (8 bytes ref)
                               │ [2] → "Java"     │ (8 bytes ref)
                               │ [3] → null       │ (8 bytes, unused)
                               └──────────────────┘
                               Capacity: 4, Size: 3

After adding 4th element (resize):
New capacity = 4 + (4 >> 1) = 6
Arrays.copyOf() creates new array of size 6
```

### String Objects in Memory

```
"Hello" String object (in String Pool or heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ hash (int, 4 bytes)         │
│ value reference (8 bytes) ──────→ char[] or byte[] (Java 9+)
└─────────────────────────────┘
```

## 10. Syntax

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

// ============================================
// CREATION
// ============================================
List<String> empty = new ArrayList<>();
List<String> withCapacity = new ArrayList<>(100);
List<String> fromCollection = new ArrayList<>(List.of("A", "B", "C"));
List<String> fromArray = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
List<String> immutable = List.of("A", "B", "C"); // Java 9+
List<String> mutable = new ArrayList<>(List.of("A", "B", "C"));

// ============================================
// ADDING ELEMENTS
// ============================================
list.add("element");              // Append to end, returns true
list.add(0, "element");          // Insert at index
list.addAll(List.of("a", "b"));  // Add all from collection
list.addAll(0, List.of("a"));   // Add all at index

// ============================================
// ACCESSING ELEMENTS
// ============================================
String element = list.get(0);           // O(1) random access
int index = list.indexOf("element");    // O(n) search
int lastIndex = list.lastIndexOf("element"); // O(n) search from end
boolean has = list.contains("element"); // O(n) search

// ============================================
// REMOVING ELEMENTS
// ============================================
String removed = list.remove(0);        // Remove by index, O(n)
boolean success = list.remove("element"); // Remove by value, O(n)
list.removeIf(s -> s.startsWith("A")); // Conditional removal
list.clear();                           // Remove all

// ============================================
// REPLACING ELEMENTS
// ============================================
list.set(0, "new value");              // Replace at index

// ============================================
// SEARCHING
// ============================================
int idx = list.indexOf(obj);
int lastIdx = list.lastIndexOf(obj);
boolean contains = list.contains(obj);

// ============================================
// SORTING
// ============================================
Collections.sort(list);                    // Natural order
list.sort(Comparator.naturalOrder());     // Natural order
list.sort(Comparator.reverseOrder());     // Reverse order
list.sort(Comparator.comparing(String::length)); // Custom comparator

// ============================================
// SUBLIST (view, not copy)
// ============================================
List<String> sub = list.subList(0, 3);    // [0, 3)
sub.set(0, "modified"); // Modifies original!

// ============================================
// CONVERSIONS
// ============================================
String[] array = list.toArray(new String[0]);
Object[] objArray = list.toArray();

// ============================================
// IMMUTABLE VIEWS
// ============================================
List<String> unmodifiable = Collections.unmodifiableList(list);
List<String> copied = List.copyOf(list); // Truly immutable copy

// ============================================
// ITERATION
// ============================================
// Enhanced for loop
for (String s : list) {
    System.out.println(s);
}

// Iterator
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}

// ListIterator (bidirectional)
ListIterator<String> lit = list.listIterator();
while (lit.hasNext()) {
    System.out.println(lit.nextIndex() + ": " + lit.next());
}

// forEach with lambda
list.forEach(System.out::println);

// Stream
list.stream().filter(s -> s.length() > 3).forEach(System.out::println);
```

## 11. Easy Example

```java
import java.util.ArrayList;
import java.util.List;

public class ArrayListBasics {
    public static void main(String[] args) {
        // Create and populate
        List<String> colors = new ArrayList<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Yellow");

        System.out.println("Colors: " + colors);
        System.out.println("Size: " + colors.size());

        // Access by index
        System.out.println("First: " + colors.get(0));
        System.out.println("Last: " + colors.get(colors.size() - 1));

        // Check if contains
        System.out.println("Contains Red: " + colors.contains("Red"));
        System.out.println("Index of Blue: " + colors.indexOf("Blue"));

        // Remove
        colors.remove("Yellow");
        colors.remove(0);
        System.out.println("After removal: " + colors);

        // Add at specific position
        colors.add(0, "Purple");
        System.out.println("After insert: " + colors);

        // Sort
        colors.sort(String::compareToIgnoreCase);
        System.out.println("Sorted: " + colors);

        // Iterate
        System.out.println("Iterating:");
        for (String color : colors) {
            System.out.println("  - " + color);
        }
    }
}
```

## 12. Medium Example

```java
import java.util.ArrayList;

## 📑 Continue Reading

**Part 1** of 3 | Part 2 | Part 3

## Engineering Decision Framework

### ✅ Use ArrayList when:
- You need frequent random access by index (O(1))
- Append operations dominate (add at end)
- Cache-friendly iteration is important
- You need a simple, general-purpose list
- You know approximate size upfront (pre-allocate capacity)

### ❌ Avoid ArrayList when:
- Frequent insertions/removals in the middle (use LinkedList or ArrayDeque)
- Queue/deque operations are primary (use ArrayDeque)
- Thread-safe access is needed without external sync (use CopyOnWriteArrayList)
- You need consistent O(1) add/remove at both ends

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| LinkedList | Frequent insertions/removals at known positions |
| ArrayDeque | Queue/deque operations (add/remove from both ends) |
| CopyOnWriteArrayList | Read-heavy concurrent access with rare writes |
| Vector | Legacy code requiring synchronized list |
| Stack | LIFO operations (prefer ArrayDeque instead) |

### Production Examples
- Data grid row storage in UI frameworks
- Event listener registration lists
- Undo/redo history buffers
- CSV/JSON record collections
- Caching recently accessed items

### Common Production Mistakes
- Using indexOf() in hot loops (O(n) per call)
- Not pre-allocating capacity when size is known
- Modifying list during enhanced for-loop (ConcurrentModificationException)
- Using subList() as a persistent view (it's a live view of original)
- Removing elements by index in a loop without accounting for shifted indices

## Related Topics
- Cache Locality — Why ArrayList beats LinkedList
- Memory Footprint — ArrayList memory layout
- Iterator Internals — How ArrayList iteration works
- Generics — Type-safe list operations
- Streams — Stream processing on lists

