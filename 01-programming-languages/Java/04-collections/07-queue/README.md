# Queue Interface

## 1. Introduction

The `Queue` interface is designed for holding elements prior to processing. It provides operations for inserting, extracting, and inspecting elements. Queue typically orders elements in a FIFO (First-In-First-Out) manner, but priority queues order elements by priority.

Queue extends the `Collection` interface and provides two sets of methods:
1. **Throw exceptions**: `add()`, `remove()`, `element()` - throw exceptions on failure
2. **Return special values**: `offer()`, `poll()`, `peek()` - return null/false on failure

The most common Queue implementations are:
- **ArrayDeque**: Resizable array-based deque (fastest for queue operations)
- **LinkedList**: Doubly-linked list (also implements List)
- **PriorityQueue**: Binary heap (priority-based ordering)

## 2. Learning Objectives

- Understand the Queue interface and its contract
- Learn the difference between throw-exception and return-value methods
- Master Queue operations: offer, poll, peek, add, remove, element
- Understand FIFO vs priority ordering
- Compare ArrayDeque vs LinkedList for queue operations
- Learn about bounded vs unbounded queues
- Understand thread-safe queue options

## 3. Prerequisites

- Module 01: Java Fundamentals
- Module 02: Object-Oriented Programming
- Understanding of Collection interface

## 4. Why This Concept Exists

Before Queue, developers had to:
1. Use arrays with head/tail pointers: Manual management, error-prone
2. Use Vector: Synchronized, slow
3. Implement their own queues: Inconsistent APIs

Queue provides:
1. **Standard API**: Consistent interface across implementations
2. **Two operation styles**: Exception-throwing and special-value returning
3. **Multiple implementations**: Choose based on needs
4. **Integration**: Works with all Collection APIs

Queue is essential for:
- Task scheduling
- Message queues
- BFS (Breadth-First Search)
- Producer-consumer patterns
- Event processing

## 5. Problem Statement

Consider building a print spooler:
- Documents are added to a queue
- Documents are processed in FIFO order
- Need to handle empty queue gracefully
- Need to check if queue is full (bounded)

Without Queue, you'd need to:
- Implement your own queue data structure
- Handle edge cases manually
- Write inconsistent code for different queue types

With Queue, you simply use the standard API.

## 6. Theory

### Queue Operations

| Operation | Throw Exception | Return Special Value |
|-----------|-----------------|---------------------|
| Insert | add(e) | offer(e) |
| Remove | remove() | poll() |
| Examine | element() | peek() |

### FIFO vs Priority

- **FIFO (First-In-First-Out)**: Elements processed in insertion order
  - Implementations: ArrayDeque, LinkedList
- **Priority**: Elements processed by priority
  - Implementation: PriorityQueue

### Bounded vs Unbounded

- **Bounded**: Fixed capacity, throws exception or returns false when full
- **Unbounded**: Grows as needed ( LinkedList, PriorityQueue)

## 7. Internal Working

### ArrayDeque as Queue

ArrayDeque uses a circular array:
```java
transient Object[] elements;
transient int head;
transient int tail;

// offer() adds to tail
// poll() removes from head
// Both are O(1) amortized
```

### LinkedList as Queue

LinkedList uses a doubly-linked list:
```java
transient Node<E> first;
transient Node<E> last;

// offer() adds to end (last)
// poll() removes from head (first)
// Both are O(1)
```

## 8. JVM Perspective

### Memory Allocation

```java
Queue<String> queue = new ArrayDeque<>();
// ArrayDeque: ~48 bytes + initial array (16 × 8 = 128 bytes)

Queue<String> linkedQueue = new LinkedList<>();
// LinkedList: ~32 bytes + ~40 bytes per element
```

### Performance Comparison

ArrayDeque is generally faster than LinkedList for queue operations due to:
- Better cache locality (contiguous array)
- Less memory overhead (no node pointers)
- Fewer object allocations

## 9. Memory Representation

```
ArrayDeque<String> queue:
┌───────────────────────────────┐
│ ArrayDeque object             │
├───────────────────────────────┤
│ Object header (12 bytes)      │
│ elements ─────────────────────┐
│ head = 1 (4 bytes)            │
│ tail = 3 (4 bytes)            │
└───────────────────────────────┘
                                │
                                ▼
                         Object[] elements (capacity 16)
                         ┌────────────────────────┐
                         │ [0] → null             │
                         │ [1] → "Hello"          │ ← head
                         │ [2] → "World"          │
                         │ [3] → "Java"           │ ← tail
                         │ [4] → null             │
                         │ ...                    │
                         └────────────────────────┘
```

## 10. Syntax

```java
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;

// ============================================
// CREATION
// ============================================
Queue<E> queue = new ArrayDeque<>();         // Best for queue operations
Queue<E> queue = new LinkedList<>();         // Also implements List
Queue<E> queue = new PriorityQueue<>();      // Priority ordering
Queue<E> queue = new ArrayDeque<>(100);      // With initial capacity

// ============================================
// INSERTING ELEMENTS
// ============================================
queue.offer(element);        // Returns false if full (bounded)
queue.add(element);          // Throws exception if full
queue.offerLast(element);    // Deque method, same as offer()
queue.addLast(element);      // Deque method, same as add()

// ============================================
// REMOVING ELEMENTS
// ============================================
E element = queue.poll();    // Returns null if empty
E element = queue.remove();  // Throws exception if empty
E element = queue.pollFirst(); // Deque method, same as poll()

// ============================================
// EXAMINING ELEMENTS
// ============================================
E element = queue.peek();    // Returns null if empty
E element = queue.element(); // Throws exception if empty
E element = queue.peekFirst(); // Deque method, same as peek()

// ============================================
// COMMON OPERATIONS
// ============================================
int size = queue.size();     // O(1)
boolean empty = queue.isEmpty(); // O(1)
boolean has = queue.contains(element); // O(n)
queue.clear();               // O(n)

// ============================================
// PROCESSING ALL ELEMENTS
// ============================================
while (!queue.isEmpty()) {
    E element = queue.poll();
    process(element);
}

// ============================================
// ITERATION
// ============================================
// Note: Iteration order may not be FIFO for some implementations
for (E element : queue) {
    System.out.println(element);
}

// ============================================
// PRIORITY QUEUE SPECIFIC
// ============================================
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
```

## 11. Easy Example

```java
import java.util.Queue;
import java.util.ArrayDeque;

public class QueueBasics {
    public static void main(String[] args) {
        // Create and populate
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");

        System.out.println("Queue: " + queue);
        System.out.println("Size: " + queue.size());

        // Peek at head
        System.out.println("Peek: " + queue.peek());

        // Process queue
        System.out.println("
Processing:");
        while (!queue.isEmpty()) {
            System.out.println("  Serving: " + queue.poll());
        }

        System.out.println("Queue empty: " + queue.isEmpty());
    }
}
```

## 12. Medium Example

```java
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueOperations {
    public static void main(String[] args) throws InterruptedException {
        // BFS using Queue
        System.out.println("=== BFS Traversal ===");
        Map<String, List<String>> graph = Map.of(
            "A", List.of("B", "C"),
            "B", List.of("A", "D"),
            "C", List.of("A", "D"),
            "D", List.of("B", "C")
        );
        bfs(graph, "A");

        // Producer-Consumer with bounded queue
        System.out.println("
=== Producer-Consumer ===");
        ArrayBlockingQueue<String> buffer = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    buffer.put("Item" + i);
                    System.out.println("Produced: Item" + i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    String item = buffer.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }

    static void bfs(Map<String, List<String>> graph, String start) {
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String node = queue.poll();
            System.out.print(node + " ");

            for (String neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                }
            }
        }
        System.out.println();
    }
}
```

## 13. Hard Example

```java
import java.util.*;
import java.util.concurrent.*;

public class AdvancedQueuePatterns {
    public static void main(String[] args) throws InterruptedException {
        // Pattern 1: Rate limiter
        System.out.println("=== Rate Limiter ===");
        RateLimiter rateLimiter = new RateLimiter(5, 1000); // 5 requests per second
        for (int i = 0; i < 10; i++) {
            boolean allowed = rateLimiter.tryAcquire();
            System.out.println("Request " + i + ": " + (allowed ? "Allowed" : "Denied"));
            Thread.sleep(200);
        }

        // Pattern 2: Task scheduler
        System.out.println("
=== Task Scheduler ===");
        ScheduledTaskScheduler scheduler = new ScheduledTaskScheduler();
        scheduler.schedule(() -> System.out.println("Task 1 executed"), 1000);
        scheduler.schedule(() -> System.out.println("Task 2 executed"), 2000);
        scheduler.schedule(() -> System.out.println("Task 3 executed"), 1500);
        Thread.sleep(3000);
        scheduler.shutdown();

        // Pattern 3: Windowed average
        System.out.println("
=== Windowed Average ===");
        WindowedAverage window = new WindowedAverage(3);
        window.add(10);
        window.add(20);
        window.add(30);
        System.out.println("Average: " + window.getAverage());
        window.add(40);
        System.out.println("Average after 40: " + window.getAverage());
    }

    static class RateLimiter {
        private final Queue<Long> timestamps;
        private final int maxRequests;
        private final long windowMillis;

        public RateLimiter(int maxRequests, long windowMillis) {
            this.timestamps = new ArrayDeque<>();
            this.maxRequests = maxRequests;
            this.windowMillis = windowMillis;
        }

        public synchronized boolean tryAcquire() {
            long now = System.currentTimeMillis();
            while (!timestamps.isEmpty() && timestamps.peek() <= now - windowMillis) {
                timestamps.poll();
            }
            if (timestamps.size() < maxRequests) {
                timestamps.offer(now);
                return true;
            }
            return false;
        }
    }

    static class ScheduledTaskScheduler {
        private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        private final Queue<ScheduledFuture<?>> tasks = new ArrayDeque<>();

        public void schedule(Runnable task, long delayMillis) {

## 📑 Continue Reading

**Part 1** of 2 | [Part 2](README-part2.md)

```
