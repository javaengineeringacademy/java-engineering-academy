# Advanced Concurrency

## Overview

Advanced concurrency in Java goes beyond basic `synchronized` and `volatile` to include sophisticated constructs for complex parallel and asynchronous programming. This guide covers modern concurrency tools available in Java 11+.

---

## CompletableFuture

`CompletableFuture` is Java's implementation of the Future pattern with functional programming support, enabling non-blocking asynchronous operations.

### Basic Usage

```java
// Simple async operation
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return fetchDataFromService();
});

// Get result (blocking)
String result = future.get();

// Get result with timeout
String result = future.get(5, TimeUnit.SECONDS);
```

### Chaining Operations

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> fetchUserId())           // Async: get user ID
    .thenApply(id -> fetchUserName(id))         // Sync: get name
    .thenApply(name -> formatName(name))        // Sync: format name
    .exceptionally(ex -> "Error: " + ex.getMessage());  // Handle errors
```

### Combining Futures

```java
// Combine two independent futures
CompletableFuture<String> userFuture = CompletableFuture.supplyAsync(() -> fetchUser());
CompletableFuture<String> orderFuture = CompletableFuture.supplyAsync(() -> fetchOrder());

// Both must complete
CompletableFuture<String> combined = userFuture.thenCombine(orderFuture,
    (user, order) -> user + " - " + order);

// Wait for all
CompletableFuture<Void> all = CompletableFuture.allOf(future1, future2, future3);
all.join();  // Blocks until all complete

// Wait for any
CompletableFuture<Object> any = CompletableFuture.anyOf(future1, future2, future3);
```

### Async Variants

```java
// SupplyAsync with custom executor
ExecutorService executor = Executors.newFixedThreadPool(10);
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return fetchData();
}, executor);

// thenApplyAsync for CPU-intensive work
CompletableFuture<Result> future = CompletableFuture
    .supplyAsync(() -> fetchData())
    .thenApplyAsync(data -> processExpensive(data), executor);

// thenAcceptAsync for side effects
CompletableFuture<Void> future = CompletableFuture
    .supplyAsync(() -> fetchData())
    .thenAcceptAsync(data -> saveToDatabase(data), executor);
```

### Error Handling

```java
CompletableFuture<String> future = CompletableFuture
    .supplyAsync(() -> riskyOperation())
    .exceptionally(ex -> {
        log.error("Operation failed", ex);
        return "fallback";
    })
    .thenApply(result -> process(result));
```

---

## Fork/Join Framework

The Fork/Join framework enables parallel divide-and-conquer algorithms using work-stealing.

### RecursiveTask Example

```java
public class ParallelSum extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10_000;
    private final long[] array;
    private final int start;
    private final int end;

    public ParallelSum(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // Base case: compute directly
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }

        // Recursive case: fork and join
        int mid = (start + end) / 2;
        ParallelSum left = new ParallelSum(array, start, mid);
        ParallelSum right = new ParallelSum(array, mid, end);

        left.fork();  // Async execution
        long rightResult = right.compute();  // Sync execution
        long leftResult = left.join();  // Wait for forked task

        return leftResult + rightResult;
    }

    public static long parallelSum(long[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        try {
            return pool.invoke(new ParallelSum(array, 0, array.length));
        } finally {
            pool.shutdown();
        }
    }
}
```

### Work Stealing

```java
// ForkJoinPool uses work-stealing algorithm:
// - Each thread has its own deque
// - When a thread's deque is empty, it steals from others
// - Reduces contention and improves load balancing

ForkJoinPool pool = new ForkJoinPool(
    Runtime.getRuntime().availableProcessors(),  // parallelism
    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
    null,  // exception handler
    true   // asyncMode
);
```

---

## StampedLock

`StampedLock` is a capability-based lock with three modes: writing, reading, and optimistic reading.

### Basic Usage

```java
public class Point {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    // Exclusive write lock
    public void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // Shared read lock
    public double distanceFromOrigin() {
        long stamp = sl.readLock();
        try {
            return Math.sqrt(x * x + y * y);
        } finally {
            sl.unlockRead(stamp);
        }
    }

    // Optimistic read (no locking overhead)
    public double distanceFromOriginOptimistic() {
        long stamp = sl.tryOptimisticRead();
        double currentX = x, currentY = y;
        
        if (!sl.validate(stamp)) {
            // Fallback to read lock
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                sl.unlockRead(stamp);
            }
        }
        
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }
}
```

### Read-Write Lock Comparison

```java
// StampedLock vs ReReadWriteLock:
// - StampedLock supports optimistic reads (no lock)
// - StampedLock doesn't support conditions
// - StampedLock is not reentrant
// - StampedLock may be faster for read-heavy workloads
```

---

## Atomic Variables

Atomic variables provide lock-free, thread-safe operations using CAS (Compare-And-Swap).

### AtomicInteger Example

```java
public class Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int get() {
        return count.get();
    }

    // CAS operation
    public boolean incrementIfLessThan(int threshold) {
        int current;
        do {
            current = count.get();
            if (current >= threshold) return false;
        } while (!count.compareAndSet(current, current + 1));
        return true;
    }
}
```

### AtomicReference Example

```java
public class Stack<T> {
    private final AtomicReference<Node<T>> top = new AtomicReference<>();

    public void push(T value) {
        Node<T> oldTop = top.get();
        Node<T> newTop = new Node<>(value, oldTop);
        while (!top.compareAndSet(oldTop, newTop)) {
            oldTop = top.get();
            newTop = new Node<>(value, oldTop);
        }
    }

    public T pop() {
        Node<T> oldTop = top.get();
        if (oldTop == null) return null;
        
        Node<T> newTop = oldTop.next;
        while (!top.compareAndSet(oldTop, newTop)) {
            oldTop = top.get();
            if (oldTop == null) return null;
            newTop = oldTop.next;
        }
        return oldTop.value;
    }

    private static class Node<T> {
        final T value;
        final Node<T> next;
        
        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }
}
```

### LongAdder (Java 8+)

```java
// LongAdder is more efficient than AtomicLong for high contention
LongAdder adder = new LongAdder();
adder.increment();
adder.add(10);
long total = adder.sum();
```

---

## ConcurrentHashMap Deep Dive

### Thread-Safe Operations

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// Atomic operations
map.putIfAbsent("key", 1);
map.computeIfAbsent("key", k -> expensiveComputation(k));
map.merge("key", 1, Integer::sum);

// Bulk operations (parallel)
map.forEach(4, (key, value) -> process(key, value));
long count = map.reduceValues(4, v -> v > 10 ? 1L : 0L, Long::sum);
```

### Segment Locking (Legacy)

```java
// ConcurrentHashMap uses segment locking (pre-Java 8)
// Each segment is a separate hash table with its own lock
// Allows concurrent access to different segments

ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>(16, 0.75f, 16);
// initialCapacity, loadFactor, concurrencyLevel
```

### CAS-Based Operations (Java 8+)

```java
// Java 8+ uses CAS for fine-grained locking
// Node-level locking instead of segment-level
// Better performance for high-contention scenarios
```

---

## Virtual Threads (Project Loom)

Virtual threads are lightweight threads managed by the JVM, not the OS.

### Basic Usage

```java
// Create virtual thread
Thread vThread = Thread.ofVirtual().start(() -> {
    System.out.println("Running in virtual thread");
});

// Virtual thread executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i -> {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}
```

### Structured Concurrency (Preview)

```java
// Structured concurrency ensures child tasks complete before parent
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser());
    Subtask<Order> order = scope.fork(() -> fetchOrder());
    
    scope.join();
    
    return new Response(user.get(), order.get());
}
```

### Pinned Threads

```java
// Virtual threads can be pinned by:
// 1. synchronized blocks
// 2. Native methods

// Solution: Use ReentrantLock instead of synchronized
public class SafeClass {
    private final ReentrantLock lock = new ReentrantLock();
    
    public void safeMethod() {
        lock.lock();
        try {
            // Not pinned
        } finally {
            lock.unlock();
        }
    }
    
    public synchronized void pinnedMethod() {
        // Pinned! Blocks carrier thread
    }
}
```

### Best Practices

```java
// 1. Use virtual threads for I/O-bound tasks
// 2. Avoid pinning (no synchronized, no native calls)
// 3. Don't pool virtual threads (one per task)
// 4. Use structured concurrency for complex flows
// 5. Monitor with JFR (jdk.VirtualThreadStart, jdk.VirtualThreadEnd)
```

---

## Summary

| Tool | Use Case | Key Feature |
|------|----------|-------------|
| **CompletableFuture** | Async composition | Functional chaining |
| **Fork/Join** | Divide-and-conquer | Work stealing |
| **StampedLock** | Read-heavy workloads | Optimistic reads |
| **Atomic variables** | Lock-free algorithms | CAS operations |
| **ConcurrentHashMap** | Concurrent maps | Segment/CAS locking |
| **Virtual threads** | High concurrency | Lightweight threads |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
