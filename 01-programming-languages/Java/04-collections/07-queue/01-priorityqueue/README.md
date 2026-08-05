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
        System.out.println("\n=== K-th Largest ===");
        int[] nums = {3, 2, 1, 5, 6, 4};
        int k = 2;
        System.out.println(k + "-th largest: " + findKthLargest(nums, k));

        // Merge k sorted lists
        System.out.println("\n=== Merge K Sorted Lists ===");
        List<List<Integer>> lists = List.of(
            List.of(1, 4, 7),
            List.of(2, 5, 8),
            List.of(3, 6, 9)
        );
        List<Integer> merged = mergeKSortedLists(lists);
        System.out.println("Merged: " + merged);
    }

    static int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    static List<Integer> mergeKSortedLists(List<List<Integer>> lists) {
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) {
                minHeap.offer(new int[]{lists.get(i).get(0), i, 0});
            }
        }

        while (!minHeap.isEmpty()) {
            int[] current = minHeap.poll();
            result.add(current[0]);

            if (current[2] + 1 < lists.get(current[1]).size()) {
                minHeap.offer(new int[]{
                    lists.get(current[1]).get(current[2] + 1),
                    current[1],
                    current[2] + 1
                });
            }
        }

        return result;
    }

    record Task(String description, int priority, Date createdAt) {}
}
```

## 13. Hard Example

```java
import java.util.*;

public class AdvancedPriorityQueue {
    public static void main(String[] args) {
        // Pattern 1: Sliding window maximum
        System.out.println("=== Sliding Window Maximum ===");
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        int windowSize = 3;
        List<Integer> maxes = slidingWindowMax(nums, windowSize);
        System.out.println("Max values: " + maxes);

        // Pattern 2: Task scheduler with cooldown
        System.out.println("\n=== Task Scheduler ===");
        char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
        int cooldown = 2;
        int intervals = leastInterval(tasks, cooldown);
        System.out.println("Minimum intervals: " + intervals);

        // Pattern 3: Find median from data stream
        System.out.println("\n=== Median Finder ===");
        MedianFinder medianFinder = new MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        System.out.println("Median after 1,2: " + medianFinder.findMedian());
        medianFinder.addNum(3);
        System.out.println("Median after 1,2,3: " + medianFinder.findMedian());
    }

    static List<Integer> slidingWindowMax(int[] nums, int k) {
        List<Integer> result = new ArrayList<>();
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);

        for (int i = 0; i < nums.length; i++) {
            maxHeap.offer(new int[]{nums[i], i});
            while (maxHeap.peek()[1] <= i - k) {
                maxHeap.poll();
            }
            if (i >= k - 1) {
                result.add(maxHeap.peek()[0]);
            }
        }
        return result;
    }

    static int leastInterval(char[] tasks, int n) {
        int[] counts = new int[26];
        for (char task : tasks) {
            counts[task - 'A']++;
        }

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        for (int count : counts) {
            if (count > 0) {
                maxHeap.offer(count);
            }
        }

        int intervals = 0;
        while (!maxHeap.isEmpty()) {
            List<Integer> temp = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                if (!maxHeap.isEmpty()) {
                    temp.add(maxHeap.poll() - 1);
                }
            }

            for (int count : temp) {
                if (count > 0) {
                    maxHeap.offer(count);
                }
            }

            intervals += maxHeap.isEmpty() ? temp.size() : n + 1;
        }
        return intervals;
    }

    static class MedianFinder {
        private final PriorityQueue<Integer> maxHeap; // Lower half
        private final PriorityQueue<Integer> minHeap; // Upper half

        public MedianFinder() {
            maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
            minHeap = new PriorityQueue<>();
        }

        public void addNum(int num) {
            maxHeap.offer(num);
            minHeap.offer(maxHeap.poll());

            if (minHeap.size() > maxHeap.size()) {
                maxHeap.offer(minHeap.poll());
            }
        }

        public double findMedian() {
            if (maxHeap.size() > minHeap.size()) {
                return maxHeap.peek();
            }
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class EventProcessingSystem {
    private final PriorityBlockingQueue<Event> eventQueue;
    private final List<Event> processedEvents;

    public EventProcessingSystem() {
        this.eventQueue = new PriorityBlockingQueue<>(
            100,
            Comparator.comparingInt(Event::priority)
                     .thenComparing(Event::timestamp)
        );
        this.processedEvents = new ArrayList<>();
    }

    public void submitEvent(Event event) {
        eventQueue.offer(event);
    }

    public Optional<Event> processNextEvent() {
        Event event = eventQueue.poll();
        if (event != null) {
            processedEvents.add(event);
        }
        return Optional.ofNullable(event);
    }

    public int getQueueSize() {
        return eventQueue.size();
    }

    public Map<Event.Type, Long> getEventStatistics() {
        return processedEvents.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Event::type,
                java.util.stream.Collectors.counting()
            ));
    }

    public static void main(String[] args) {
        EventProcessingSystem system = new EventProcessingSystem();

        system.submitEvent(new Event("Low priority", Event.Type.INFO, 1, new Date()));
        system.submitEvent(new Event("High priority", Event.Type.ERROR, 3, new Date()));
        system.submitEvent(new Event("Medium priority", Event.Type.WARNING, 2, new Date()));

        System.out.println("=== Processing Events ===");
        while (system.getQueueSize() > 0) {
            system.processNextEvent().ifPresent(event ->
                System.out.println("Processing: " + event.description() +
                    " (" + event.type() + ")")
            );
        }

        System.out.println("\n=== Event Statistics ===");
        system.getEventStatistics().forEach((type, count) ->
            System.out.println("  " + type + ": " + count)
        );
    }

    record Event(String description, Type type, int priority, Date timestamp) {
        enum Type { INFO, WARNING, ERROR }
    }
}
```

## 15. Performance

### Time Complexity

| Operation | Average | Worst Case | Notes |
|-----------|---------|------------|-------|
| offer() | O(log n) | O(log n) | Bubble up |
| poll() | O(log n) | O(log n) | Bubble down |
| peek() | O(1) | O(1) | Root access |
| remove(object) | O(n) | O(n) | Search + remove |
| contains(object) | O(n) | O(n) | Linear search |
| size() | O(1) | O(1) | Field access |
| iteration | O(n) | O(n) | No order guarantee |

### PriorityQueue vs ArrayDeque vs LinkedList

| Feature | PriorityQueue | ArrayDeque | LinkedList |
|---------|---------------|------------|------------|
| Ordering | Priority | FIFO/LIFO | FIFO/LIFO |
| offer() | O(log n) | O(1) | O(1) |
| poll() | O(log n) | O(1) | O(1) |
| peek() | O(1) | O(1) | O(1) |
| remove(object) | O(n) | O(n) | O(n) |
| null elements | No | No | Yes |
| Thread-safe | No | No | No |

### When to Use PriorityQueue

1. **Priority-based processing**: When order depends on priority, not insertion
2. **Finding k-th elements**: Min-heap for k-th largest, max-heap for k-th smallest
3. **Merging sorted data**: Merge k sorted lists/arrays
4. **Graph algorithms**: Dijkstra's, Prim's, A*
5. **Event simulation**: Process events by time/priority

## 16. Best Practices

1. **Choose min-heap or max-heap**: Use Comparator.reverseOrder() for max-heap
2. **Set initial capacity**: For known sizes to avoid resizing
3. **Don't iterate for priority order**: Use poll() instead
4. **Use appropriate comparator**: For custom priority logic
5. **Thread safety**: Use PriorityBlockingQueue for concurrent access
6. **Avoid null elements**: PriorityQueue doesn't allow them
7. **Consider ArrayDeque**: For FIFO/LIFO without priority

## 17. Common Mistakes

```java
// Mistake 1: Assuming iteration order
PriorityQueue<Integer> queue = new PriorityQueue<>();
queue.offer(5);
queue.offer(2);
queue.offer(8);

// Bad - iteration does NOT guarantee priority order
for (Integer num : queue) {
    System.out.println(num); // Not necessarily 2, 5, 8
}

// Good - use poll() for priority order
while (!queue.isEmpty()) {
    System.out.println(queue.poll()); // 2, 5, 8
}

// Mistake 2: Using remove() for priority processing
// Bad - O(n) remove
queue.remove(5);

// Good - O(log n) poll
queue.poll();

// Mistake 3: Not using appropriate comparator
// Bad - min-heap when you want max-heap
PriorityQueue<Integer> wrong = new PriorityQueue<>();

// Good - max-heap
PriorityQueue<Integer> correct = new PriorityQueue<>(Comparator.reverseOrder());

// Mistake 4: Adding null elements
queue.offer(null); // NullPointerException!
```

## 18. Pitfalls

### No Iteration Order
PriorityQueue iteration does NOT guarantee priority order. Use poll() to process in priority order.

### Not Thread-Safe
PriorityQueue is NOT thread-safe. Use PriorityBlockingQueue for concurrent access.

### No Random Access
PriorityQueue doesn't support get(index) operation. It's a queue, not a list.

### O(n) for contains/remove(object)
While offer/poll/peek are O(log n)/O(1), contains and remove(object) are O(n).

### Null Elements
PriorityQueue does NOT allow null elements (throws NullPointerException).

## 19. Debugging Tips

1. **Override toString()**: For custom element classes
2. **Check comparator**: Verify priority ordering
3. **Use debugger**: Inspect heap structure
4. **Monitor size**: Verify expected number of elements
5. **Profile memory**: Use JProfiler to check memory usage
6. **Test with multiple threads**: Use PriorityBlockingQueue for concurrent testing

## 20. Comparison Table

| Feature | PriorityQueue | ArrayDeque | LinkedList | PriorityBlockingQueue |
|---------|---------------|------------|------------|----------------------|
| Structure | Binary heap | Circular array | Doubly-linked list | Binary heap + lock |
| Ordering | Priority | FIFO/LIFO | FIFO/LIFO | Priority |
| Thread-safe | No | No | No | Yes |
| Null elements | No | No | Yes | No |
| Performance | O(log n) | O(1) | O(1) | O(log n) |

## 21. Decision Tree

```
Need a Queue?
├── Yes → Need priority ordering?
│   ├── Yes → PriorityQueue
│   └── No → Need FIFO?
│       ├── Yes → ArrayDeque (preferred)
│       └── No → Need LIFO (stack)?
│           └── Use ArrayDeque (not Stack class)
├── Need thread safety?
│   └── Yes → PriorityBlockingQueue
└── Need null elements?
    └── Yes → LinkedList (not PriorityQueue)
```

## 22. Interview Questions

### Q1: What is the time complexity of PriorityQueue operations?
**A**: offer/poll: O(log n), peek: O(1), contains/remove(object): O(n), size: O(1).

### Q2: What data structure does PriorityQueue use internally?
**A**: A binary heap stored in an array. For element at index i, left child is 2i+1, right child is 2i+2, parent is (i-1)/2.

### Q3: How do you create a max-heap in Java?
**A**: Pass Comparator.reverseOrder() to the constructor: `new PriorityQueue<>(Comparator.reverseOrder())`.

### Q4: Does PriorityQueue maintain insertion order?
**A**: No. PriorityQueue maintains priority order. Iteration does NOT guarantee any specific order. Use poll() to process in priority order.

### Q5: Can PriorityQueue contain null elements?
**A**: No. PriorityQueue throws NullPointerException if you try to add null elements.

### Q6: What is the difference between offer() and add()?
**A**: Both add elements. offer() returns false if queue is full (for bounded queues). add() throws exception. PriorityQueue is unbounded, so both behave the same.

### Q7: When would you use PriorityQueue over ArrayDeque?
**A**: When you need priority-based processing (not FIFO). PriorityQueue is O(log n) for offer/poll, while ArrayDeque is O(1) but doesn't support priority ordering.

## 23. Exercises

### Exercise 1: Find K-th Largest
Given an array and integer k, find the k-th largest element using PriorityQueue.

### Exercise 2: Merge K Sorted Lists
Merge k sorted lists into one sorted list using PriorityQueue.

### Exercise 3: Task Scheduler
Implement a task scheduler that processes tasks by priority with cooldown periods.

## 24. Assignments

### Assignment 1: Hospital ER System
Build an emergency room system using PriorityQueue:
- Add patients with priorities
- Serve highest priority patient first
- Track wait times
- Generate statistics

### Assignment 2: Network Packet Scheduler
Create a network packet scheduler:
- Packets have priorities
- Process by priority
- Handle bandwidth limits
- Track statistics

## 25. Mini Project

### Real-Time Event Processing System

Build an event processing system using PriorityQueue:

```java
// Features:
// 1. Submit events with priorities
// 2. Process by priority
// 3. Handle event dependencies
// 4. Track processing times
// 5. Generate reports
// 6. Thread-safe for concurrent access
```

**Requirements:**
- Use PriorityBlockingQueue for thread safety
- Implement event dependencies
- Handle event timeouts
- Generate processing statistics

## 26. Summary

PriorityQueue is a priority-based queue implementation:

- **Internal structure**: Binary heap (array-based)
- **Performance**: O(log n) offer/poll, O(1) peek
- **Ordering**: Min-heap (default) or max-heap (reverse comparator)
- **Null elements**: Not allowed
- **Iteration**: Does NOT guarantee priority order
- **Best for**: Priority-based processing, k-th element finding, merging sorted data

## 27. References

### Official Documentation
- [PriorityQueue JavaDoc](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/PriorityQueue.html)
- [Queue Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java/util/Queue.html)

### Books
- *Effective Java* by Joshua Bloch
- *Introduction to Algorithms* (CLRS) - Heaps

### Online Resources
- [Baeldung PriorityQueue Guide](https://www.baeldung.com/java-priority-queue)
- [GeeksforGeeks PriorityQueue](https://www.geeksforgeeks.org/priority-queue-class-in-java/)

### Related Topics
- [Queue](../07-queue/README.md)
- [Deque](../09-deque/README.md)
- [ArrayDeque](../09-deque/README.md)
