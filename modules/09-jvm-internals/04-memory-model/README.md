# 04. Memory Model

## Introduction

The Java Memory Model (JMM) defines how threads interact through memory and what behaviors are allowed in concurrent programs. It specifies the rules for visibility, ordering, and atomicity of memory operations across threads. Understanding the JMM is critical for writing correct concurrent code and avoiding subtle bugs that are difficult to reproduce and diagnose.

The JMM is not just about how memory is organized—it's about how memory operations are ordered and visible across threads. This topic covers the fundamentals of the Java Memory Model, including happens-before relationships, volatile variables, synchronized blocks, and atomic classes.

## Learning Objectives

By the end of this topic, you will be able to:

- [ ] Explain the Java Memory Model and its purpose
- [ ] Understand happens-before relationships
- [ ] Use volatile variables correctly
- [ ] Implement thread-safe code using synchronized blocks
- [ ] Apply atomic classes for lock-free programming
- [ ] Identify and fix memory visibility issues
- [ ] Optimize concurrent code for performance

## Prerequisites

- Completion of Topic 03: Class Loading
- Understanding of basic multithreading concepts
- Familiarity with Java syntax and semantics
- Basic knowledge of CPU architecture

## Why This Concept Exists

### The Concurrency Challenge

Modern applications are highly concurrent:
- Web servers handle thousands of concurrent requests
- Microservices communicate asynchronously
- Background workers process tasks in parallel
- User interfaces respond to multiple events

Without a clear memory model, concurrent code would be:
- **Unpredictable**: Results depend on CPU, compiler, and runtime
- **Non-portable**: Code works on one platform but fails on another
- **Error-prone**: Subtle bugs that are difficult to reproduce

### The Memory Visibility Problem

When multiple threads access shared data:
- Each thread may have its own copy of the data in CPU cache
- Changes made by one thread may not be visible to other threads
- The order of operations may be different from what the programmer intended

### The JMM Solution

The JMM provides a set of rules that guarantee:
- **Visibility**: Changes made by one thread are visible to other threads
- **Ordering**: Operations are executed in a predictable order
- **Atomicity**: Certain operations are executed atomically

## Problem Statement

### The Concurrency Bug

Consider this classic example:

```java
// Shared state
private boolean running = true;
private int counter = 0;

// Thread 1
public void writer() {
    counter = 42;          // Step 1
    running = false;       // Step 2
}

// Thread 2
public void reader() {
    while (running) {      // Step 3
        // Do nothing
    }
    System.out.println(counter);  // Step 4
}
```

Without the JMM guarantees:
- Thread 2 might never see Thread 1's writes
- Thread 2 might see the writes in a different order
- Thread 2 might print something other than 42

### Real-World Example

A financial trading system experienced:
- Orders being processed out of sequence
- Account balances showing incorrect values
- Trades being executed multiple times

The root cause? Memory visibility issues in concurrent code.

## Theory

### The Java Memory Model

The JMM defines:
1. **Memory Operations**: read, write, volatile read, volatile write, lock, unlock
2. **Happens-Before Relationships**: Rules that determine ordering
3. **Synchronization Actions**: Actions that establish happens-before edges

### Happens-Before Relationship

The happens-before relationship is fundamental to the JMM:

```
If action A happens-before action B, then:
- A is visible to B
- A is ordered before B

Program Order Rule:
- Within a single thread, each action happens-before subsequent actions

Monitor Lock Rule:
- An unlock on a monitor happens-before every subsequent lock on that monitor

Volatile Variable Rule:
- A write to a volatile field happens-before every subsequent read of that field

Thread Start Rule:
- A call to Thread.start() happens-before any action in the started thread

Thread Termination Rule:
- Any action in a thread happens-before any other thread detects that the thread has terminated

Transitivity:
- If A happens-before B, and B happens-before C, then A happens-before C
```

### Memory Visibility

Without proper synchronization:
- Threads may see stale values
- Threads may see values from other threads' caches
- Threads may see partially constructed objects

With proper synchronization:
- All threads see a consistent view of memory
- Changes are propagated across threads
- Operations are ordered correctly

## Internal Working

### How the JMM Works

```java
// Example: Memory visibility
public class MemoryVisibility {
    private volatile boolean running = true;
    private int counter = 0;
    
    public void writer() {
        counter = 42;          // Non-volatile write
        running = false;       // Volatile write (establishes happens-before)
    }
    
    public void reader() {
        while (running) {      // Volatile read
            // Do nothing
        }
        // Because of volatile, counter = 42 is guaranteed to be visible
        System.out.println(counter);  // Prints 42
    }
}
```

### CPU Cache and Memory Hierarchy

```
┌─────────────────────────────────────────────────────────────┐
│                        CPU                                  │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐        │
│  │   Core 0    │  │   Core 1    │  │   Core N    │        │
│  │  ┌───────┐  │  │  ┌───────┐  │  │  ┌───────┐  │        │
│  │  │ L1    │  │  │  │ L1    │  │  │  │ L1    │  │        │
│  │  │ Cache │  │  │  │ Cache │  │  │  │ Cache │  │        │
│  │  └───────┘  │  │  └───────┘  │  │  └───────┘  │        │
│  └─────────────┘  └─────────────┘  └─────────────┘        │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                    L2 Cache                          │   │
│  └─────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                    L3 Cache                          │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                    Main Memory                              │
└─────────────────────────────────────────────────────────────┘
```

## JVM Perspective

### What the JVM Sees

The JVM sees:
- **Memory Operations**: reads, writes, fences
- **Synchronization Actions**: locks, unlocks, volatile reads/writes
- **Thread Actions**: starts, joins, interrupts
- **Program Order**: order of operations within a thread

### Memory Barriers

The JVM uses memory barriers to enforce ordering:

```
Store Barrier: Ensures all previous stores are visible before subsequent stores
Load Barrier: Ensures all subsequent loads see the most recent store
Full Barrier: Combination of store and load barriers
```

## Memory Representation

### Memory Layout with JMM

```
┌─────────────────────────────────────────────────────────────┐
│                    Main Memory                              │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Shared Variables                                    │   │
│  │  - counter = 0                                       │   │
│  │  - running = true                                    │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                    CPU Cache (Core 0)                       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Local Copy                                          │   │
│  │  - counter = 0 (stale)                               │   │
│  │  - running = true (stale)                            │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────────┐
│                    CPU Cache (Core 1)                       │
│  ┌─────────────────────────────────────────────────────┐   │
│  │  Local Copy                                          │   │
│  │  - counter = 0 (stale)                               │   │
│  │  - running = true (stale)                            │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

## Syntax

### Volatile Keyword

```java
// Volatile variable
private volatile boolean running = true;

// Volatile read
if (running) {
    // Read is guaranteed to see most recent write
}

// Volatile write
running = false;  // Write is guaranteed to be visible to other threads
```

### Synchronized Block

```java
// Synchronized block
synchronized (lock) {
    // Only one thread can execute this block at a time
    counter++;
}

// Synchronized method
public synchronized void increment() {
    counter++;
}
```

### Atomic Classes

```java
// AtomicInteger
private final AtomicInteger counter = new AtomicInteger(0);

// Atomic increment
counter.incrementAndGet();  // Atomically increment and return

// Atomic compare and set
counter.compareAndSet(10, 20);  // If counter == 10, set to 20
```

## Easy Example

### Basic Memory Visibility

```java
package academy.javaengineering.jvm.memorymodel;

/**
 * Demonstrates basic memory visibility issues and solutions.
 */
public class BasicMemoryVisibility {
    
    // Without volatile, this may not be visible to other threads
    private volatile boolean running = true;
    private int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        BasicMemoryVisibility demo = new BasicMemoryVisibility();
        
        // Start writer thread
        Thread writer = new Thread(() -> {
            demo.writer();
        });
        
        // Start reader thread
        Thread reader = new Thread(() -> {
            demo.reader();
        });
        
        writer.start();
        reader.start();
        
        // Wait for threads to finish
        writer.join();
        reader.join();
    }
    
    private void writer() {
        System.out.println("Writer: Starting...");
        
        // Non-volatile write
        counter = 42;
        
        // Volatile write (establishes happens-before)
        running = false;
        
        System.out.println("Writer: Done");
    }
    
    private void reader() {
        System.out.println("Reader: Starting...");
        
        // Volatile read
        while (running) {
            // Do nothing
        }
        
        // Because of volatile, counter = 42 is guaranteed to be visible
        System.out.println("Reader: counter = " + counter);
        
        System.out.println("Reader: Done");
    }
}
```

## Medium Example

### Thread-Safe Counter

```java
package academy.javaengineering.jvm.memorymodel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates different ways to implement thread-safe counters.
 */
public class ThreadSafeCounter {
    
    // Approach 1: Synchronized method
    private int synchronizedCounter = 0;
    
    // Approach 2: Volatile with synchronized block
    private volatile int volatileCounter = 0;
    
    // Approach 3: AtomicInteger (lock-free)
    private final AtomicInteger atomicCounter = new AtomicInteger(0);
    
    // Approach 4: LongAdder (high contention)
    private final java.util.concurrent.atomic.LongAdder longAdderCounter = 
        new java.util.concurrent.atomic.LongAdder();
    
    public static void main(String[] args) throws InterruptedException {
        ThreadSafeCounter counter = new ThreadSafeCounter();
        
        int threadCount = 10;
        int incrementsPerThread = 100_000;
        
        System.out.println("=== Thread-Safe Counter Demo ===\n");
        
        // Test synchronized counter
        testSynchronizedCounter(counter, threadCount, incrementsPerThread);
        
        // Test volatile counter
        testVolatileCounter(counter, threadCount, incrementsPerThread);
        
        // Test atomic counter
        testAtomicCounter(counter, threadCount, incrementsPerThread);
        
        // Test LongAdder
        testLongAdderCounter(counter, threadCount, incrementsPerThread);
    }
    
    private static void testSynchronizedCounter(ThreadSafeCounter counter, 
            int threadCount, int increments) throws InterruptedException {
        
        long startTime = System.nanoTime();
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    counter.incrementSynchronized();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Synchronized: %d (took %d ms)%n", 
            counter.synchronizedCounter, duration);
    }
    
    private static void testVolatileCounter(ThreadSafeCounter counter, 
            int threadCount, int increments) throws InterruptedException {
        
        long startTime = System.nanoTime();
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    counter.incrementVolatile();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Volatile: %d (took %d ms)%n", 
            counter.volatileCounter, duration);
    }
    
    private static void testAtomicCounter(ThreadSafeCounter counter, 
            int threadCount, int increments) throws InterruptedException {
        
        long startTime = System.nanoTime();
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    counter.incrementAtomic();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("Atomic: %d (took %d ms)%n", 
            counter.atomicCounter.get(), duration);
    }
    
    private static void testLongAdderCounter(ThreadSafeCounter counter, 
            int threadCount, int increments) throws InterruptedException {
        
        long startTime = System.nanoTime();
        
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    counter.incrementLongAdder();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        System.out.printf("LongAdder: %d (took %d ms)%n", 
            counter.longAdderCounter.sum(), duration);
    }
    
    // Synchronized counter (thread-safe but slow)
    public synchronized void incrementSynchronized() {
        synchronizedCounter++;
    }
    
    // Volatile counter (NOT thread-safe for compound operations)
    public void incrementVolatile() {
        volatileCounter++;  // This is NOT atomic!
    }
    
    // Atomic counter (thread-safe and fast)
    public void incrementAtomic() {
        atomicCounter.incrementAndGet();
    }
    
    // LongAdder counter (thread-safe and fastest for high contention)
    public void incrementLongAdder() {
        longAdderCounter.increment();
    }
}
```

## Hard Example

### Producer-Consumer with Memory Barriers

```java
package academy.javaengineering.jvm.memorymodel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstrates producer-consumer pattern with proper memory barriers.
 */
public class ProducerConsumerDemo {
    
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity;
    private final AtomicInteger produced = new AtomicInteger(0);
    private final AtomicInteger consumed = new AtomicInteger(0);
    
    // Lock objects for synchronization
    private final Object producerLock = new Object();
    private final Object consumerLock = new Object();
    
    public ProducerConsumerDemo(int capacity) {
        this.capacity = capacity;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ProducerConsumerDemo demo = new ProducerConsumerDemo(10);
        
        System.out.println("=== Producer-Consumer Demo ===\n");
        
        // Create producer threads
        Thread[] producers = new Thread[3];
        for (int i = 0; i < producers.length; i++) {
            final int producerId = i;
            producers[i] = new Thread(() -> {
                try {
                    demo.produce(producerId, 10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Create consumer threads
        Thread[] consumers = new Thread[3];
        for (int i = 0; i < consumers.length; i++) {
            final int consumerId = i;
            consumers[i] = new Thread(() -> {
                try {
                    demo.consume(consumerId, 10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Start all threads
        for (Thread producer : producers) {
            producer.start();
        }
        for (Thread consumer : consumers) {
            consumer.start();
        }
        
        // Wait for all threads to finish
        for (Thread producer : producers) {
            producer.join();
        }
        for (Thread consumer : consumers) {
            consumer.join();
        }
        
        System.out.println("\nResults:");
        System.out.println("Total produced: " + produced.get());
        System.out.println("Total consumed: " + consumed.get());
    }
    
    public void produce(int producerId, int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            synchronized (producerLock) {
                // Wait while queue is full
                while (queue.size() >= capacity) {
                    producerLock.wait();
                }
                
                // Add item to queue
                int item = produced.incrementAndGet();
                queue.add(item);
                System.out.printf("Producer %d: Produced item %d%n", producerId, item);
                
                // Notify consumers
                synchronized (consumerLock) {
                    consumerLock.notifyAll();
                }
            }
        }
    }
    
    public void consume(int consumerId, int count) throws InterruptedException {
        for (int i = 0; i < count; i++) {
            synchronized (consumerLock) {
                // Wait while queue is empty
                while (queue.isEmpty()) {
                    consumerLock.wait();
                }
                
                // Remove item from queue
                int item = queue.poll();
                consumed.incrementAndGet();
                System.out.printf("Consumer %d: Consumed item %d%n", consumerId, item);
                
                // Notify producers
                synchronized (producerLock) {
                    producerLock.notifyAll();
                }
            }
        }
    }
}
```

## Enterprise Example

### Concurrent Cache with Memory Barriers

```java
package academy.javaengineering.jvm.memorymodel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Enterprise-grade concurrent cache with proper memory management.
 */
public class ConcurrentCacheDemo {
    
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final AtomicLong hitCount = new AtomicLong(0);
    private final AtomicLong missCount = new AtomicLong(0);
    
    public static void main(String[] args) throws InterruptedException {
        ConcurrentCacheDemo demo = new ConcurrentCacheDemo();
        
        System.out.println("=== Concurrent Cache Demo ===\n");
        
        // Simulate concurrent cache operations
        Thread[] threads = new Thread[10];
        
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    String key = "key-" + (j % 20);
                    
                    // Try to get from cache
                    CacheEntry entry = demo.get(key);
                    if (entry != null) {
                        // Cache hit
                        System.out.printf("Thread %d: Cache hit for key %s%n", 
                            threadId, key);
                    } else {
                        // Cache miss
                        demo.put(key, new CacheEntry("value-" + j));
                        System.out.printf("Thread %d: Cache miss for key %s%n", 
                            threadId, key);
                    }
                }
            });
        }
        
        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }
        
        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }
        
        // Print statistics
        System.out.println("\nCache Statistics:");
        System.out.println("Hit count: " + demo.hitCount.get());
        System.out.println("Miss count: " + demo.missCount.get());
        System.out.println("Cache size: " + demo.cache.size());
    }
    
    public CacheEntry get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null) {
            hitCount.incrementAndGet();
            return entry;
        }
        missCount.incrementAndGet();
        return null;
    }
    
    public void put(String key, CacheEntry value) {
        cache.put(key, value);
    }
    
    public void remove(String key) {
        cache.remove(key);
    }
    
    public void clear() {
        cache.clear();
    }
    
    public static class CacheEntry {
        private final String value;
        private final long timestamp;
        
        public CacheEntry(String value) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getValue() {
            return value;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
        
        public boolean isExpired(long ttlMillis) {
            return System.currentTimeMillis() - timestamp > ttlMillis;
        }
    }
}
```

## Performance

### JMM Performance Metrics

| Metric | Description | Target |
|--------|-------------|--------|
| **Throughput** | Operations per second | Maximize |
| **Latency** | Time per operation | Minimize |
| **Contention** | Thread contention level | Minimize |
| **Cache Misses** | CPU cache misses | Minimize |
| **Memory Fences** | Memory barrier operations | Minimize |

### Performance Optimization Strategies

1. **Minimize Synchronization**
   - Use atomic classes instead of synchronized blocks
   - Use read-write locks for read-heavy workloads
   - Use lock-free data structures

2. **Reduce Contention**
   - Use striped locks
   - Use thread-local variables
   - Use work-stealing queues

3. **Optimize Memory Access**
   - Use cache-friendly data structures
   - Minimize false sharing
   - Use appropriate memory layout

## Best Practices

### JMM Best Practices

1. **Use Volatile Appropriately**
   - Use for flags and simple state variables
   - Don't use for compound operations
   - Use with happens-before relationships

2. **Use Synchronized Correctly**
   - Keep synchronized blocks small
   - Don't hold locks for long periods
   - Use try-finally to release locks

3. **Use Atomic Classes**
   - Use for simple atomic operations
   - Use compareAndSet for lock-free algorithms
   - Use LongAdder for high-contention counters

4. **Test Concurrent Code**
   - Use stress testing
   - Use thread sanitizers
   - Use code review

## Common Mistakes

### Mistake 1: Using Volatile Incorrectly

```java
// BAD: Volatile is not sufficient for compound operations
private volatile int counter = 0;

public void increment() {
    counter++;  // This is NOT atomic!
}

// GOOD: Use AtomicInteger for atomic operations
private final AtomicInteger counter = new AtomicInteger(0);

public void increment() {
    counter.incrementAndGet();
}
```

### Mistake 2: Not Establishing Happens-Before

```java
// BAD: No happens-before relationship
private boolean running = true;
private int counter = 0;

public void writer() {
    counter = 42;
    running = false;
}

public void reader() {
    while (running) {}
    System.out.println(counter);  // May not see 42
}

// GOOD: Use volatile to establish happens-before
private volatile boolean running = true;
private int counter = 0;

public void writer() {
    counter = 42;
    running = false;
}

public void reader() {
    while (running) {}
    System.out.println(counter);  // Guaranteed to see 42
}
```

### Mistake 3: Holding Locks Too Long

```java
// BAD: Holding lock for too long
public synchronized void processLargeData(List<Data> data) {
    for (Data item : data) {
        // Long-running operation
        processItem(item);
    }
}

// GOOD: Minimize lock holding time
public void processLargeData(List<Data> data) {
    for (Data item : data) {
        synchronized (this) {
            // Short operation
            processItem(item);
        }
    }
}
```

## Pitfalls

### Pitfall 1: Deadlock

```java
// BAD: May cause deadlock
public class DeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {
            synchronized (lock2) {
                // Do something
            }
        }
    }
    
    public void method2() {
        synchronized (lock2) {
            synchronized (lock1) {
                // Do something
            }
        }
    }
}

// GOOD: Avoid deadlock by consistent lock ordering
public class NoDeadlockExample {
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    
    public void method1() {
        synchronized (lock1) {
            synchronized (lock2) {
                // Do something
            }
        }
    }
    
    public void method2() {
        synchronized (lock1) {  // Same order as method1
            synchronized (lock2) {
                // Do something
            }
        }
    }
}
```

### Pitfall 2: Race Condition

```java
// BAD: Race condition
public class RaceConditionExample {
    private int counter = 0;
    
    public void increment() {
        counter++;  // Non-atomic operation
    }
}

// GOOD: Use atomic operation
public class NoRaceConditionExample {
    private final AtomicInteger counter = new AtomicInteger(0);
    
    public void increment() {
        counter.incrementAndGet();
    }
}
```

## Debugging Tips

### JMM Debug Commands

```bash
# Print thread information
jstack <pid>

# Print lock information
jstack -l <pid>

# Print thread dumps
jcmd <pid> Thread.print

# Print lock contention
jcmd <pid> Thread.print -l

# Print synchronized blocks
jcmd <pid> Compiler.c1
```

### Common JMM Issues

| Issue | Symptom | Solution |
|-------|---------|----------|
| Visibility issue | Stale data | Use volatile or synchronized |
| Atomicity issue | Race condition | Use atomic classes |
| Ordering issue | Unexpected behavior | Use happens-before |
| Deadlock | Application hangs | Use consistent lock ordering |

## Comparison Table

### Synchronization Mechanisms

| Mechanism | Performance | Complexity | Use Case |
|-----------|-------------|------------|----------|
| **synchronized** | Medium | Low | Simple synchronization |
| **volatile** | High | Low | Flags, simple state |
| **Atomic classes** | High | Medium | Lock-free algorithms |
| **ReadWriteLock** | High | Medium | Read-heavy workloads |
| **StampedLock** | Very High | High | Optimistic locking |

### Memory Barriers

| Barrier | Description | Use Case |
|---------|-------------|----------|
| **StoreStore** | Orders stores | Volatile writes |
| **LoadLoad** | Orders loads | Volatile reads |
| **StoreLoad** | Orders store and load | Volatile writes |
| **LoadStore** | Orders load and store | Volatile reads |

## Decision Tree

### Choosing Synchronization

```
What type of operation?
├── Simple flag or state
│   ├── Use: volatile
│   └── Example: running flag
├── Atomic increment/decrement
│   ├── Use: AtomicInteger
│   └── Example: counter
├── Compound operation
│   ├── Use: synchronized or Lock
│   └── Example: check-then-act
├── Read-heavy workload
│   ├── Use: ReadWriteLock
│   └── Example: cache
└── High contention
    ├── Use: LongAdder or StampedLock
    └── Example: statistics counter
```

## Interview Questions

### Basic Questions

1. **What is the Java Memory Model?**
   - A set of rules that define how threads interact through memory

2. **What is the happens-before relationship?**
   - A relationship that determines the ordering of memory operations across threads

3. **What is the difference between volatile and synchronized?**
   - volatile: Ensures visibility, not atomicity
   - synchronized: Ensures both visibility and atomicity

4. **What is a memory barrier?**
   - A CPU instruction that enforces ordering of memory operations

### Intermediate Questions

5. **What are the benefits of volatile?**
   - Ensures visibility across threads
   - Establishes happens-before relationships
   - Lightweight alternative to synchronized

6. **What is the difference between AtomicInteger and synchronized?**
   - AtomicInteger: Lock-free, uses CAS operations
   - synchronized: Uses locks, blocks other threads

7. **What is false sharing?**
   - When different threads access different variables on the same cache line

8. **What is the Memory Visibility Problem?**
   - When one thread's writes are not visible to other threads

### Advanced Questions

9. **How does the JMM handle out-of-order execution?**
   - Through memory barriers and happens-before relationships

10. **What is the difference between acquire and release semantics?**
    - Acquire: Ensures subsequent operations see previous writes
    - Release: Ensures previous operations are visible to subsequent reads

11. **How does the JMM handle relaxed atomics?**
    - Through VarHandle API and memory ordering modes

12. **What is the difference between Java Memory Model and CPU Memory Model?**
    - JMM: Abstract model for Java
    - CPU Model: Hardware-specific memory ordering

## Exercises

### Exercise 1: Visibility Issue
Write a program that demonstrates a memory visibility issue and fix it using volatile.

### Exercise 2: Atomic Counter
Implement a thread-safe counter using AtomicInteger and compare performance with synchronized.

### Exercise 3: Producer-Consumer
Implement a producer-consumer pattern using wait/notify and memory barriers.

### Exercise 4: Deadlock Prevention
Write a program that avoids deadlock by using consistent lock ordering.

## Assignments

### Assignment 1: Thread-Safe Data Structure
Implement a thread-safe queue using memory barriers and atomic operations.

### Assignment 2: Performance Benchmark
Benchmark different synchronization mechanisms and analyze their performance characteristics.

### Assignment 3: Concurrent Algorithm
Implement a lock-free algorithm (e.g., concurrent stack or queue) using atomic operations.

## Mini Project

### Concurrent Cache Library

Create a library that:
1. Implements a thread-safe cache
2. Uses memory barriers for visibility
3. Supports eviction policies
4. Provides monitoring metrics

**Requirements:**
- Use ConcurrentHashMap
- Implement LRU/LFU eviction
- Support TTL (Time-To-Live)
- Provide hit/miss statistics

## Summary

### Key Takeaways

1. **JMM Defines Concurrency Rules**: Ensures correct behavior in concurrent code
2. **Happens-Before is Fundamental**: Determines ordering of operations
3. **Volatile Ensures Visibility**: Changes are visible across threads
4. **Atomic Classes Provide Lock-Free Operations**: Efficient for simple operations
5. **Testing is Critical**: Concurrent code must be thoroughly tested

### Next Steps

- Continue to Topic 05: Garbage Collection
- Study "Java Concurrency in Practice" by Brian Goetz
- Practice with concurrent data structures
- Read JSR 133 (Java Memory Model)

## References

### Official Documentation
- [JSR 133: Java Memory Model](https://www.jcp.org/en/jsr/detail?id=133)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-17.html)
- [Volatile Variable](https://docs.oracle.com/javase/tutorial/essential/concurrency/atomicvars.html)

### Books
- "Java Concurrency in Practice" by Brian Goetz
- "Concurrent Programming in Java" by Doug Lea
- "The Art of Multiprocessor Programming" by Maurice Herlihy

### Online Resources
- [Java Memory Model](https://www.cs.umd.edu/~pugh/java/memoryModel/)
- [Java Concurrency](https://www.baeldung.com/java-concurrency)
- [Volatile vs Synchronized](https://www.baeldung.com/java-volatile)

### Tools
- [jcstress](https://openjdk.java.net/projects/code-tools/jcstress/)
- [FindBugs](https://findbugs.sourceforge.net/)
- [SpotBugs](https://spotbugs.github.io/)

---

**Next Topic**: [05. Garbage Collection](../05-garbage-collection/README.md)
