# PriorityQueue

## 1. Introduction

PriorityQueue is an unbounded priority queue based on a binary heap data structure. Elements are processed in priority order rather than insertion order. The head of the queue is the least element according to the specified ordering. PriorityQueue provides O(log n) time for offer (add) and poll (remove) operations, and O(1) for peek (examine) operations.

PriorityQueue is essential for:
- Task scheduling by priority
- Finding k-th largest/smallest elements
- Merging k sorted lists
- Dijkstra's algorithm
- Event-driven simulation

Unlike Queue implementations like LinkedList or ArrayDeque, PriorityQueue does not allow null elements and does not guarantee FIFO ordering. The ordering is determined by the natural ordering of elements or a custom Comparator.

## 2. Learning Objectives

- Create and use PriorityQueue with natural ordering and custom comparators
- Understand the binary heap data structure
- Learn min-heap vs max-heap configurations
- Master priority queue operations: offer, poll, peek, remove
- Compare PriorityQueue vs ArrayDeque vs LinkedList
- Understand when PriorityQueue is the right choice
- Learn about heap-based algorithms

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Understanding of Queue interface
- Basic knowledge of binary trees

## 4. Why This Concept Exists

Before PriorityQueue, developers had to:
1. Sort the entire collection: O(n log n)
2. Use a List and sort before each poll: O(n log n) per poll
3. Implement their own heap: Error-prone, inefficient

PriorityQueue provides:
1. **O(log n) offer/poll**: Efficient priority-based processing
2. **O(1) peek**: Constant-time access to the highest priority element
3. **Dynamic sizing**: Grows as needed
4. **Flexible ordering**: Natural order or custom Comparator

PriorityQueue is essential for:
- Operating system process scheduling
- Network packet prioritization
- Event-driven simulation
- Graph algorithms (Dijkstra, Prim)
- Finding k-th largest/smallest elements

## 5. Problem Statement

Consider building a hospital emergency room:
- Patients arrive with different priorities (critical, urgent, normal)
- Need to serve highest priority patient first
- New patients arrive constantly
- Can't sort all patients before each service

Without PriorityQueue, you'd need to:
- Sort all patients before each service: O(n log n)
- Or scan all patients to find highest priority: O(n)

With PriorityQueue, serving the highest priority patient is O(log n).

## 6. Theory

### Binary Heap Structure

PriorityQueue uses a binary heap:
- **Min-heap**: Parent <= children (default)
- **Max-heap**: Parent >= children (use reverse comparator)

The heap is stored in an array:
- For element at index i:
  - Left child: 2i + 1
  - Right child: 2i + 2
  - Parent: (i - 1) / 2

### Heap Operations

**Offer (add)**:
1. Add element at end of array
2. Bubble up (swap with parent if out of order)
3. O(log n)

**Poll (remove)**:
1. Save root element (minimum)
2. Move last element to root
3. Bubble down (swap with smaller child if out of order)
4. O(log n)

**Peek**:
1. Return root element
2. O(1)

### Resizing

PriorityQueue uses a dynamic array that doubles when full:
- Initial capacity: 11
- Growth: capacity + (capacity >> 1) (1.5x)

## 7. Internal Working

### The offer() Operation

```java
public boolean offer(E e) {
    if (e == null) throw new NullPointerException();
    modCount++;
    int i = size;
    if (i >= queue.length)
        grow(i + 1);
    size = i + 1;
    if (i == 0)
        queue[0] = e;
    else
        siftUp(i, e);
    return true;
}

private void siftUp(int k, E x) {
    if (comparator != null)
        siftUpUsingComparator(k, x);
    else
        siftUpComparable(k, x);
}

private void siftUpComparable(int k, E x) {
    Comparable<? super E> key = (Comparable<? super E>) x;
    while (k > 0) {
        int parent = (k - 1) >>> 1;
        Object e = queue[parent];
        if (key.compareTo((E) e) >= 0)
            break;
        queue[k] = e;
        k = parent;
    }
    queue[k] = key;
}
```

### The poll() Operation

```java
public E poll() {
    if (size == 0)
        return null;
    int s = --size;
    modCount++;
    E result = (E) queue[0];
    E x = (E) queue[s];
    queue[s] = null;
    if (s != 0)
        siftDown(0, x);
    return result;
}

private void siftDown(int k, E x) {
    if (comparator != null)
        siftDownUsingComparator(k, x);
    else
        siftDownComparable(k, x);
}

private void siftDownComparable(int k, E x) {
    Comparable<? super E> key = (Comparable<? super E>) x;
    int half = size >>> 1;
    while (k < half) {
        int child = (k << 1) + 1;
        Object c = queue[child];
        int right = child + 1;
        if (right < size &&
            ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
            c = queue[child = right];
        if (key.compareTo((E) c) <= 0)
            break;
        queue[k] = c;
        k = child;
    }
    queue[k] = key;
}
```

## 8. JVM Perspective

### Memory Allocation

```java
PriorityQueue<Integer> queue = new PriorityQueue<>();
// JVM allocates:
// - PriorityQueue object header: 12 bytes
// - queue reference: 8 bytes
// - size field: 4 bytes
// - modCount: 4 bytes
// Total PriorityQueue object: ~32 bytes

// Initial backing array:
// Object[] queue = new Object[11];
// Array: 11 × 8 = 88 bytes
```

### Array Representation of Heap

```
PriorityQueue<Integer> (min-heap):
Array: [1, 3, 5, 7, 9]

Binary tree:
        1
       / \
      3   5
     / \
    7   9

Index mapping:
- Element 1: index 0
- Element 3: index 1 (left child of 1)
- Element 5: index 2 (right child of 1)
- Element 7: index 3 (left child of 3)
- Element 9: index 4 (right child of 3)
```

## 9. Memory Representation

```
PriorityQueue<Integer> queue = new PriorityQueue<>();
queue.offer(5);
queue.offer(2);
queue.offer(8);
queue.offer(1);

Memory layout:
┌───────────────────────────────┐
│ PriorityQueue object          │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ queue ──────────────────────────┐
│ size = 4 (4 bytes)            │     │
│ modCount = 4 (4 bytes)        │     │
└───────────────────────────────┘     │
                                      ▼
                               Object[] queue (capacity 11)
                               ┌────────────────────────┐
                               │ [0] → Integer 1        │
                               │ [1] → Integer 2        │
                               │ [2] → Integer 8        │
                               │ [3] → Integer 5        │
                               │ [4] → null             │
                               │ ...                    │
                               └────────────────────────┘

Heap structure (min-heap):
        1
       / \
      2   8
     /
    5
```

## 10. Syntax

```java
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Comparator;

// ============================================
// CREATION
// ============================================
PriorityQueue<Integer> minHeap = new PriorityQueue<>();              // Natural ordering
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
PriorityQueue<String> byLength = new PriorityQueue<>(Comparator.comparingInt(String::length));
PriorityQueue<Integer> withCapacity = new PriorityQueue<>(100);
PriorityQueue<Integer> fromCollection = new PriorityQueue<>(List.of(5, 2, 8, 1));

// ============================================
// QUEUE OPERATIONS
// ============================================
queue.offer(element);        // Add to queue (returns boolean)
queue.add(element);          // Add to queue (throws exception if full)
queue.poll();                // Remove head (returns null if empty)
queue.remove();              // Remove head (throws exception if empty)
queue.peek();                // View head (returns null if empty)
queue.element();             // View head (throws exception if empty)

// ============================================
// ADDITIONAL OPERATIONS
// ============================================
queue.size();                // O(1)
queue.isEmpty();             // O(1)
queue.contains(element);     // O(n)
queue.remove(object);        // O(n)
queue.clear();               // O(n)
queue.toArray();             // Convert to array

// ============================================
// ITERATION
// ============================================
// Note: Iteration does NOT guarantee priority order
for (Integer element : queue) {
    System.out.println(element);
}

// To process in priority order:
while (!queue.isEmpty()) {
    System.out.println(queue.poll());
}

// ============================================
// HEAP OPERATIONS
// ============================================
// Find k-th largest using min-heap
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    minHeap.offer(num);
    if (minHeap.size() > k) {
        minHeap.poll();
    }
}
int kthLargest = minHeap.peek();

// Find k-th smallest using max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
for (int num : nums) {
    maxHeap.offer(num);
    if (maxHeap.size() > k) {
        maxHeap.poll();
    }
}
int kthSmallest = maxHeap.peek();
```

## 11. Easy Example

```java
import java.util.PriorityQueue;
import java.util.Comparator;

public class PriorityQueueBasics {
    public static void main(String[] args) {
        // Min-heap
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(2);
        minHeap.offer(8);
        minHeap.offer(1);

        System.out.println("Min-heap poll order:");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println();

        // Max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(2);
        maxHeap.offer(8);
        maxHeap.offer(1);

        System.out.println("Max-heap poll order:");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println();
    }
}
```

## 12. Medium Example

```java
import java.util.*;
import java.util.stream.Collectors;

public class PriorityQueueOperations {
    public static void main(String[] args) {
        // Task scheduling
        System.out.println("=== Task Scheduling ===");
        PriorityQueue<Task> taskQueue = new PriorityQueue<>(
            Comparator.comparingInt(Task::priority).thenComparing(Task::createdAt)
        );

        taskQueue.offer(new Task("Low priority", 1, new Date()));
        taskQueue.offer(new Task("High priority", 3, new Date()));
        taskQueue.offer(new Task("Medium priority", 2, new Date()));

        while (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            System.out.println("Processing: " + task.description());
        }

        // Find k-th largest
        System.out.println("
=== K-th Largest ===");
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println(k + "-th largest: " + findKthLargest(nums, k));


## 📑 Continue Reading

**Part 1** of 3 | [Part 2](README-part2.md) | [Part 3](README-part3.md)

```
