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
        System.out.println("\nProcessing:");
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
        System.out.println("\n=== Producer-Consumer ===");
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
        System.out.println("\n=== Task Scheduler ===");
        ScheduledTaskScheduler scheduler = new ScheduledTaskScheduler();
        scheduler.schedule(() -> System.out.println("Task 1 executed"), 1000);
        scheduler.schedule(() -> System.out.println("Task 2 executed"), 2000);
        scheduler.schedule(() -> System.out.println("Task 3 executed"), 1500);
        Thread.sleep(3000);
        scheduler.shutdown();

        // Pattern 3: Windowed average
        System.out.println("\n=== Windowed Average ===");
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
            ScheduledFuture<?> future = executor.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
            tasks.offer(future);
        }

        public void shutdown() {
            executor.shutdown();
        }
    }

    static class WindowedAverage {
        private final Queue<Double> window;
        private final int maxSize;
        private double sum = 0;

        public WindowedAverage(int maxSize) {
            this.window = new ArrayDeque<>();
            this.maxSize = maxSize;
        }

        public void add(double value) {
            if (window.size() >= maxSize) {
                sum -= window.poll();
            }
            window.offer(value);
            sum += value;
        }

        public double getAverage() {
            return window.isEmpty() ? 0 : sum / window.size();
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.*;
import java.util.concurrent.*;

public class MessageQueueSystem {
    private final BlockingQueue<Message> messageQueue;
    private final List<Message> processedMessages;
    private final int maxRetries;

    public MessageQueueSystem(int capacity, int maxRetries) {
        this.messageQueue = new ArrayBlockingQueue<>(capacity);
        this.processedMessages = new CopyOnWriteArrayList<>();
        this.maxRetries = maxRetries;
    }

    public boolean publish(Message message) {
        return messageQueue.offer(message);
    }

    public Optional<Message> consume() {
        try {
            return Optional.ofNullable(messageQueue.poll(1, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    public void processMessages(int count) {
        for (int i = 0; i < count; i++) {
            consume().ifPresent(message -> {
                if (processMessage(message)) {
                    processedMessages.add(message);
                }
            });
        }
    }

    private boolean processMessage(Message message) {
        // Simulate processing
        try {
            Thread.sleep(100);
            return Math.random() > 0.1; // 90% success rate
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public Map<String, Long> getMessageStatistics() {
        return processedMessages.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Message::type,
                java.util.stream.Collectors.counting()
            ));
    }

    public static void main(String[] args) {
        MessageQueueSystem system = new MessageQueueSystem(100, 3);

        // Publish messages
        for (int i = 0; i < 20; i++) {
            system.publish(new Message("MSG" + i, "ORDER", new Date()));
        }

        System.out.println("=== Processing Messages ===");
        system.processMessages(20);

        System.out.println("\n=== Message Statistics ===");
        system.getMessageStatistics().forEach((type, count) ->
            System.out.println("  " + type + ": " + count + " processed")
        );
    }

    record Message(String id, String type, Date timestamp) {}
}
```

## 15. Performance

### Time Complexity

| Operation | ArrayDeque | LinkedList | PriorityQueue |
|-----------|------------|------------|---------------|
| offer() | O(1)* | O(1) | O(log n) |
| poll() | O(1) | O(1) | O(log n) |
| peek() | O(1) | O(1) | O(1) |
| size() | O(1) | O(1) | O(1) |
| contains() | O(n) | O(n) | O(n) |

*Amortized O(1) for ArrayDeque

### ArrayDeque vs LinkedList

| Feature | ArrayDeque | LinkedList | Winner |
|---------|------------|------------|--------|
| Memory | Less | More | ArrayDeque |
| Cache locality | Good | Poor | ArrayDeque |
| Performance | Faster | Slower | ArrayDeque |
| Implements | Deque | List, Deque | ArrayDeque |
| Thread-safe | No | No | Tie |

**Always prefer ArrayDeque over LinkedList for queue/deque operations.**

## 16. Best Practices

1. **Use ArrayDeque**: Default choice for queue operations (faster than LinkedList)
2. **Use offer/poll/peek**: For null-returning behavior (safer than add/remove/element)
3. **Set initial capacity**: For bounded queues or known sizes
4. **Use BlockingQueue**: For producer-consumer patterns
5. **Check isEmpty()**: Before poll/peek to avoid null
6. **Thread safety**: Use BlockingQueue implementations for concurrent access

## 17. Common Mistakes

```java
// Mistake 1: Using LinkedList for queue operations
// Bad - slower than ArrayDeque
Queue<String> queue = new LinkedList<>();

// Good - faster
Queue<String> queue = new ArrayDeque<>();

// Mistake 2: Using add/remove/element for null safety
// Bad - throws exceptions
try {
    String element = queue.remove();
} catch (NoSuchElementException e) {
    // Handle exception
}

// Good - returns null
String element = queue.poll();
if (element != null) {
    // Process element
}

// Mistake 3: Not checking size for bounded queues
ArrayBlockingQueue<String> boundedQueue = new ArrayBlockingQueue<>(10);
boolean added = boundedQueue.offer("item"); // May return false!

// Mistake 4: Iterating queue for processing
// Bad - doesn't remove elements
for (String s : queue) {
    process(s);
}

// Good - processes and removes
while (!queue.isEmpty()) {
    process(queue.poll());
}
```

## 18. Pitfalls

### Exceptions vs Special Values
- `add()/remove()/element()` throw exceptions on failure
- `offer()/poll()/peek()` return false/null on failure
- Choose based on your error handling strategy

### Bounded Queue Capacity
Bounded queues (ArrayBlockingQueue) have fixed capacity. `offer()` returns false when full, `put()` blocks.

### Thread Safety
Queue implementations are NOT thread-safe (except BlockingQueue). Use BlockingQueue for concurrent access.

### Null Elements
Most Queue implementations don't allow null elements (throws NullPointerException).

### Iteration Order
PriorityQueue iteration does NOT guarantee priority order. Use poll() for priority processing.

## 19. Debugging Tips

1. **Print queue contents**: Use System.out.println() for debugging
2. **Check size**: Verify expected element count
3. **Use offer/poll**: For safer operations
4. **Monitor capacity**: For bounded queues
5. **Profile memory**: Use JProfiler to check memory usage
6. **Test with multiple threads**: For BlockingQueue implementations

## 20. Comparison Table

| Feature | Queue | Deque | PriorityQueue | BlockingQueue |
|---------|-------|-------|----------------|---------------|
| Ordering | FIFO | FIFO/LIFO | Priority | FIFO |
| Capacity | Unbounded | Unbounded | Unbounded | Bounded |
| Thread-safe | No | No | No | Yes |
| Null elements | No | No | No | No |
| Performance | O(1) | O(1) | O(log n) | O(1)* |

*With waiting

## 21. Decision Tree

```
Need a Queue?
├── Yes → Need priority ordering?
│   ├── Yes → PriorityQueue
│   └── No → Need thread safety?
│       ├── Yes → ArrayBlockingQueue or LinkedBlockingQueue
│       └── No → ArrayDeque (default choice)
├── Need FIFO/LIFO?
│   └── Use ArrayDeque
└── Need producer-consumer?
    └── Use BlockingQueue
```

## 22. Interview Questions

### Q1: What is the difference between Queue and Deque?
**A**: Queue is FIFO only. Deque (Double-Ended Queue) supports FIFO and LIFO operations. Deque extends Queue interface.

### Q2: What is the difference between offer() and add()?
**A**: Both add elements. offer() returns false if queue is full (for bounded queues). add() throws exception. Use offer() for bounded queues.

### Q3: What is the difference between poll() and remove()?
**A**: Both remove and return head element. poll() returns null if empty. remove() throws exception. Use poll() for safer code.

### Q4: Which Queue implementation is fastest?
**A**: ArrayDeque is fastest for most queue operations due to better cache locality and less memory overhead.

### Q5: When would you use PriorityQueue over ArrayDeque?
**A**: When you need priority-based processing instead of FIFO. PriorityQueue orders by priority, ArrayDeque orders by insertion.

### Q6: What is BlockingQueue?
**A**: A Queue that supports blocking operations. put() blocks if queue is full, take() blocks if queue is empty. Used for producer-consumer patterns.

### Q7: Can Queue contain null elements?
**A**: Most Queue implementations don't allow null elements (throws NullPointerException). LinkedList is an exception.

## 23. Exercises

### Exercise 1: BFS Implementation
Implement BFS traversal using Queue.

### Exercise 2: Producer-Consumer
Implement a producer-consumer pattern using BlockingQueue.

### Exercise 3: Sliding Window Maximum
Find the maximum element in each sliding window using Queue.

## 24. Assignments

### Assignment 1: Print Spooler
Build a print spooler system using Queue:
- Add print jobs
- Process in FIFO order
- Handle priority jobs
- Track job status

### Assignment 2: Task Scheduler
Create a task scheduling system:
- Schedule tasks with delays
- Execute in order
- Handle cancellations
- Track execution history

## 25. Mini Project

### Chat Message System

Build a chat system using Queue:

```java
// Features:
// 1. Send/receive messages
// 2. Message queue for offline users
// 3. Priority messages
// 4. Message history
// 5. Thread-safe for multiple users
```

**Requirements:**
- Use BlockingQueue for thread safety
- Implement message priorities
- Handle reconnection
- Track delivery status

## 26. Summary

Queue is designed for FIFO processing:

- **Operations**: offer/poll/peek (null-returning), add/remove/element (exception-throwing)
- **Implementations**: ArrayDeque (fastest), LinkedList, PriorityQueue
- **Use cases**: BFS, producer-consumer, task scheduling
- **Thread safety**: Use BlockingQueue for concurrent access
- **Best practice**: Prefer ArrayDeque over LinkedList for queue operations

## 27. References

### Official Documentation
- [Queue Interface](https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html)
- [ArrayDeque](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayDeque.html)
- [BlockingQueue](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/BlockingQueue.html)

### Books
- *Effective Java* by Joshua Bloch
- *Java Concurrency in Practice* by Brian Goetz

### Online Resources
- [Baeldung Queue Guide](https://www.baeldung.com/java-queue)
- [GeeksforGeeks Queue](https://www.geeksforgeeks.org/queue-interface-java/)

### Related Topics
- [Deque](../09-deque/README.md)
- [PriorityQueue](../08-priorityqueue/README.md)
- [ArrayDeque](../09-deque/README.md)
