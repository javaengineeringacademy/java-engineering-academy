# Synchronization

## 1. Introduction

Synchronization is the mechanism that controls access to shared resources in a multithreaded environment. Without synchronization, concurrent access to shared mutable data leads to race conditions, data corruption, and unpredictable behavior. Java provides multiple synchronization mechanisms: the `synchronized` keyword, `volatile` field modifier, and explicit locks in `java.util.concurrent.locks`.

The `synchronized` keyword is Java's built-in monitor-based synchronization mechanism. It ensures that only one thread at a time can execute a block of code or method, providing mutual exclusion and memory visibility guarantees. Understanding synchronization is essential for writing correct concurrent Java programs.

## 2. Learning Objectives

- Understand what race conditions are and why they occur
- Learn how `synchronized` blocks and methods work
- Understand intrinsic locks (monitor locks)
- Learn the difference between synchronized methods and blocks
- Understand the `volatile` keyword and memory visibility
- Know when and how to use synchronization
- Learn about lock reentrancy
- Understand the performance impact of synchronization

## 3. Prerequisites

- Module 08: Introduction to Multithreading
- Module 08: Thread Creation
- Module 08: Thread Lifecycle
- Basic understanding of object monitors

## 4. Why This Concept Exists

Consider a simple counter shared between two threads:

```java
class Counter {
    int count = 0;
    void increment() { count++; }
}
```

The `count++` operation is not atomic. It involves three steps:
1. Read `count` from memory
2. Add 1 to the value
3. Write the new value back to memory

With two threads executing simultaneously:

```
Thread 1: read(0) → add(1) → write(1)
Thread 2:         read(0) → add(1) → write(1)
Result: 1 (should be 2!)
```

This is a **race condition**—the outcome depends on the timing of thread execution. Synchronization prevents this by ensuring only one thread can access the shared data at a time.

## 5. Problem Statement

A banking system needs to transfer money between accounts. Without synchronization:

```java
// Thread 1: Transfer $100 from A to B
balanceA -= 100; // Thread 1 reads A=1000
// Thread 2: Transfer $200 from A to C
balanceA -= 200; // Thread 2 reads A=1000 (stale!)
// Thread 1: writes A=900
// Thread 2: writes A=800 (Thread 1's write lost!)
// Result: A should be 700, but shows 800
```

Synchronization ensures the entire transfer operation is atomic:

```java
synchronized void transfer(Account from, Account to, int amount) {
    from.balance -= amount;
    to.balance += amount;
}
```

## 6. Theory

### Monitor Locks (Intrinsic Locks)

Every Java object has an intrinsic lock (also called a monitor lock). When a thread enters a `synchronized` block, it acquires the lock on the specified object. When it exits the block, it releases the lock.

```
synchronized (object) {
    // Thread acquires lock on 'object'
    // Only ONE thread can be here at a time
    // Critical section
} // Thread releases lock
```

### Synchronized Methods

```java
class SafeCounter {
    private int count = 0;

    // Synchronized on 'this'
    public synchronized void increment() {
        count++;
    }

    // Synchronized on SafeCounter.class
    public static synchronized void staticMethod() {
        // Synchronized on the Class object
    }
}
```

### Synchronized Blocks

```java
class Example {
    private final Object lock = new Object();

    public void doWork() {
        synchronized (lock) {
            // Synchronized on specific lock object
        }
    }
}
```

### Memory Visibility

Synchronization provides two guarantees:
1. **Mutual exclusion**: Only one thread can execute the synchronized block at a time
2. **Memory visibility**: Changes made by one thread in a synchronized block are visible to other threads

Without synchronization, threads may see stale cached values due to CPU caching and compiler optimizations.

### Happens-Before Relationship

The Java Memory Model defines happens-before relationships:
- An unlock on a monitor happens-before every subsequent lock on that monitor
- A write to a volatile field happens-before every subsequent read of that field
- A call to `Thread.start()` happens-before any action in the started thread
- A call to `Thread.join()` happens-before any action in the joined thread

## 7. Internal Working

### How synchronized Works in the JVM

1. **Monitorenter instruction**: When a thread enters a synchronized block, the JVM executes `monitorenter` on the target object.
2. **Lock acquisition**: If the monitor is available, the thread acquires it. If another thread owns it, the current thread blocks (BLOCKED state).
3. **Critical section execution**: The thread executes the code within the synchronized block.
4. **Monitorexit instruction**: When leaving the block (normally or via exception), the JVM executes `monitorexit`, releasing the monitor.

### Lock Reentrancy

Java intrinsic locks are reentrant. A thread that already holds a lock can acquire it again without blocking:

```java
synchronized void outer() {
    synchronized void inner() {
        // Same thread can acquire both - no deadlock
    }
}
```

### Lock Elision and Escaping

The JIT compiler can optimize synchronization:
- **Lock elision**: If an object doesn't escape the current thread, locks may be eliminated
- **Lock coarsening**: Multiple adjacent synchronized blocks on the same lock may be merged
- **Biased locking**: Optimizes single-threaded access patterns (removed in Java 15)

## 8. JVM Perspective

### Monitor Object Structure

Each Java object contains:
```
Object Header (Mark Word):
┌──────────────────────────────────────┐
│ Hashcode (31 bits)                   │
│ GC age (4 bits)                      │
│ Biased locking flag (1 bit)         │
│ Lock state (2 bits):                 │
│   00 = lightweight locked            │
│   01 = unlocked/biased               │
│   10 = heavyweight locked            │
│   11 = GC marked                     │
│ Thread ID (when biased)              │
│ Epoch (when biased)                  │
└──────────────────────────────────────┘
```

### Lock Escalation

When contention occurs, the JVM escalates lock levels:
1. **No lock**: Uncontested
2. **Biased locking**: Single thread, biased (Java <15)
3. **Lightweight lock**: CAS-based, no OS involvement
4. **Heavyweight lock**: OS mutex/futex, involves kernel

### Memory Barriers

Synchronization inserts memory barriers:
- **Acquire barrier**: Prevents reordering of subsequent operations before the barrier
- **Release barrier**: Prevents reordering of preceding operations after the barrier

## 9. Memory Representation

### Synchronized Method Stack Frame

```
Thread stack:
┌─────────────────────────────────────┐
│ synchronized method frame           │
│ ┌─────────────────────────────────┐ │
│ │ Lock acquired on 'this' object │ │
│ │ Local variables                 │ │
│ │ Operand stack                   │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘

Monitor in object header:
┌─────────────────────────────────────┐
│ Mark word → Lock record (lightweight)│
│ or → ObjectMonitor (heavyweight)    │
│   ├─ owner: Thread-X                │
│   ├─ entry set: [Thread-Y, Thread-Z]│
│   └─ wait set: [Thread-W]           │
└─────────────────────────────────────┘
```

## 10. Syntax

```java
// ============================================
// SYNCHRONIZED METHOD
// ============================================
class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++; // Only one thread at a time
    }

    public synchronized int getCount() {
        return count;
    }
}

// ============================================
// SYNCHRONIZED BLOCK
// ============================================
class Example {
    private final Object lock = new Object();

    public void doWork() {
        // Non-critical code (no sync needed)
        prepare();

        synchronized (lock) {
            // Critical section
            updateSharedState();
        }

        // Non-critical code (no sync needed)
        cleanup();
    }
}

// ============================================
// STATIC SYNCHRONIZED
// ============================================
class StaticExample {
    private static int globalCount = 0;

    public static synchronized void increment() {
        globalCount++; // Synchronized on Class object
    }

    // Equivalent to:
    public static void increment2() {
        synchronized (StaticExample.class) {
            globalCount++;
        }
    }
}

// ============================================
// VOLATILE
// ============================================
class VolatileExample {
    private volatile boolean running = true;

    public void stop() {
        running = false; // Immediately visible to other threads
    }

    public void run() {
        while (running) { // Always reads from main memory
            doWork();
        }
    }
}

// ============================================
// synchronized + volatile
// ============================================
class DoubleCheckedLocking {
    private static volatile Instance instance;

    public static Instance getInstance() {
        if (instance == null) {              // First check (no sync)
            synchronized (DoubleCheckedLocking.class) {
                if (instance == null) {      // Second check (with sync)
                    instance = new Instance();
                }
            }
        }
        return instance;
    }
}
```

## 11. Easy Example

```java
public class SynchronizedBasics {
    private int unsafeCount = 0;
    private int safeCount = 0;
    private final Object lock = new Object();

    public synchronized void safeIncrement() {
        safeCount++;
    }

    public void unsafeIncrement() {
        unsafeCount++;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedBasics demo = new SynchronizedBasics();
        int numThreads = 10;
        int incrementsPerThread = 100000;

        Thread[] threads = new Thread[numThreads];

        // Unsafe counter
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    demo.unsafeIncrement();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Unsafe count (expected " +
            (numThreads * incrementsPerThread) + "): " + demo.unsafeCount);

        // Safe counter
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    demo.safeIncrement();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) t.join();
        System.out.println("Safe count: " + demo.safeCount);
    }
}
```

## 12. Medium Example

```java
public class BankAccount {
    private String id;
    private int balance;
    private final Object lock = new Object();

    public BankAccount(String id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public void deposit(int amount) {
        synchronized (lock) {
            int newBalance = balance + amount;
            try { Thread.sleep(10); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            balance = newBalance;
            System.out.println(Thread.currentThread().getName() +
                " deposited " + amount + ", balance: " + balance);
        }
    }

    public synchronized boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println(Thread.currentThread().getName() +
                " withdrew " + amount + ", balance: " + balance);
            return true;
        }
        return false;
    }

    public synchronized int getBalance() {
        return balance;
    }

    public static void main(String[] args) throws InterruptedException {
        BankAccount account = new BankAccount("ACC-001", 1000);

        Thread depositor1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) account.deposit(100);
        }, "Depositor-1");

        Thread depositor2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) account.deposit(150);
        }, "Depositor-2");

        Thread withdrawer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (!account.withdraw(50)) {
                    System.out.println("Insufficient funds");
                }
            }
        }, "Withdrawer");

        depositor1.start();
        depositor2.start();
        withdrawer.start();

        depositor1.join();
        depositor2.join();
        withdrawer.join();

        System.out.println("Final balance: " + account.getBalance());
    }
}
```

## 13. Hard Example

```java
import java.util.concurrent.locks.*;

public class ReadWriteLockExample {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    private final Condition dataReady = writeLock.newCondition();

    private String data = "Initial";
    private boolean ready = false;

    public String read() {
        readLock.lock();
        try {
            while (!ready) {
                try {
                    dataReady.await(1000, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }
            return data;
        } finally {
            readLock.unlock();
        }
    }

    public void write(String newData) {
        writeLock.lock();
        try {
            data = newData;
            ready = true;
            dataReady.signalAll();
        } finally {
            writeLock.unlock();
        }
    }

    public void reset() {
        writeLock.lock();
        try {
            data = "";
            ready = false;
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockExample example = new ReadWriteLockExample();

        // Multiple readers
        Thread[] readers = new Thread[5];
        for (int i = 0; i < 5; i++) {
            final int id = i;
            readers[i] = new Thread(() -> {
                System.out.println("Reader " + id + " waiting...");
                String result = example.read();
                System.out.println("Reader " + id + " got: " + result);
            }, "Reader-" + id);
            readers[i].start();
        }

        Thread.sleep(1000);

        // Writer
        Thread writer = new Thread(() -> {
            example.write("Updated data");
            System.out.println("Writer wrote: Updated data");
        }, "Writer");

        writer.start();

        for (Thread reader : readers) reader.join();
        writer.join();
    }
}
```

## 14. Enterprise Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.*;

public class TransactionProcessor {
    private final ReentrantLock transactionLock = new ReentrantLock(true); // Fair lock
    private final Condition balanceAvailable = transactionLock.newCondition();
    private final ConcurrentHashMap<String, Integer> accounts = new ConcurrentHashMap<>();
    private final AtomicInteger transactionCount = new AtomicInteger(0);
    private final LockStatistics stats = new LockStatistics();

    public void processTransfer(String from, String to, int amount) {
        transactionLock.lock();
        try {
            long startTime = System.nanoTime();

            int fromBalance = accounts.getOrDefault(from, 0);
            int toBalance = accounts.getOrDefault(to, 0);

            if (fromBalance < amount) {
                throw new InsufficientFundsException(
                    "Account " + from + " has " + fromBalance + ", needs " + amount);
            }

            // Simulate processing delay
            Thread.sleep(10);

            accounts.put(from, fromBalance - amount);
            accounts.put(to, toBalance + amount);
            transactionCount.incrementAndGet();

            long elapsed = System.nanoTime() - startTime;
            stats.recordLockHoldTime(elapsed);

            System.out.printf("Transfer: %s → %s: $%d (balance: %d → %d)%n",
                from, to, amount,
                fromBalance, fromBalance - amount);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Transaction interrupted", e);
        } finally {
            transactionLock.unlock();
        }
    }

    public void deposit(String account, int amount) {
        accounts.merge(account, amount, Integer::sum);
    }

    public int getBalance(String account) {
        return accounts.getOrDefault(account, 0);
    }

    public void printStats() {
        System.out.printf("Transactions: %d, Lock hold time avg: %.2f μs%n",
            transactionCount.get(), stats.getAverageHoldTimeNanos() / 1000.0);
    }

    public static void main(String[] args) throws InterruptedException {
        TransactionProcessor processor = new TransactionProcessor();
        processor.deposit("ACC-A", 10000);
        processor.deposit("ACC-B", 10000);
        processor.deposit("ACC-C", 10000);

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int id = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    processor.processTransfer("ACC-A", "ACC-B", 100);
                    processor.processTransfer("ACC-B", "ACC-C", 50);
                }
            }, "Worker-" + id);
            threads[i].start();
        }

        for (Thread t : threads) t.join();
        processor.printStats();
    }

    static class LockStatistics {
        private final java.util.concurrent.atomic.AtomicLong totalHoldTime = new java.util.concurrent.atomic.AtomicLong();
        private final java.util.concurrent.atomic.AtomicInteger count = new java.util.concurrent.atomic.AtomicInteger();

        void recordLockHoldTime(long nanos) {
            totalHoldTime.addAndGet(nanos);
            count.incrementAndGet();
        }

        double getAverageHoldTimeNanos() {
            int c = count.get();
            return c == 0 ? 0 : (double) totalHoldTime.get() / c;
        }
    }

    static class InsufficientFundsException extends RuntimeException {
        InsufficientFundsException(String message) { super(message); }
    }
}
```

## 15. Performance

### Synchronization Overhead

| Operation | Time (approximate) |
|-----------|-------------------|
| Uncontested `synchronized` | ~20-50 ns |
| Contested `synchronized` (lightweight) | ~100-500 ns |
| Contested `synchronized` (heavyweight) | ~1-10 μs |
| Volatile read/write | ~5-10 ns |
| ReentrantLock.lock() (uncontested) | ~20-50 ns |
| CAS operation | ~10-30 ns |

### Reducing Synchronization Overhead

1. **Minimize critical section**: Only synchronize shared mutable data
2. **Use lock-free alternatives**: `AtomicInteger` instead of `synchronized` counter
3. **Use ReadWriteLock**: Multiple readers, single writer
4. **Use striped locks**: Partition data to reduce contention
5. **Avoid nested locks**: Reduces deadlock risk and overhead

### When to Use What

| Scenario | Mechanism |
|----------|-----------|
| Simple atomic update | `AtomicInteger` |
| Compound operations | `synchronized` |
| Read-heavy with occasional writes | `ReadWriteLock` |
| Timeout-based locking | `ReentrantLock.tryLock()` |
| Condition variables | `Condition` |

## 16. Best Practices

1. **Minimize synchronized scope**: Only protect shared mutable data
2. **Prefer private fields**: Only accessible from within the class
3. **Document thread safety**: Make synchronization strategy explicit
4. **Avoid calling external code in synchronized blocks**: May cause deadlocks
5. **Don't synchronize on String literals or boxed primitives**: They're shared
6. **Use final fields**: Immutable objects are inherently thread-safe
7. **Prefer concurrent collections**: Over synchronized wrappers
8. **Don't use `this` as lock for public APIs**: External code may also lock on it
9. **Consider lock granularity**: Fine-grained locking improves concurrency
10. **Test with multiple threads**: Verify correctness under concurrency

## 17. Common Mistakes

```java
// Mistake 1: Synchronizing on wrong object
class BadSync {
    private List<String> list = new ArrayList<>();

    public synchronized void add(String item) { // Locks on 'this'
        list.add(item);
    }

    public synchronized void printAll() { // Locks on 'this'
        list.forEach(System.out::println);
    }

    // Problem: Another thread can modify list between add and printAll
    // because they use the same lock ('this')
}

// Mistake 2: Not synchronizing compound operations
class BadCompound {
    private Map<String, Integer> map = new HashMap<>();

    public void increment(String key) {
        if (!map.containsKey(key)) {
            map.put(key, 0); // Race condition!
        }
        map.put(key, map.get(key) + 1); // Race condition!
    }
}

// Fix:
public void increment(String key) {
    synchronized (map) {
        map.merge(key, 1, Integer::sum);
    }
}

// Mistake 3: Synchronizing on String literals
String lock = "shared";
synchronized (lock) { } // All code using "shared" shares this lock!

// Fix: Use a private final Object
private final Object lock = new Object();

// Mistake 4: Using synchronized on a method that calls another synchronized method
class Recursive {
    public synchronized void a() { b(); }
    public synchronized void b() { /* Same thread reacquires lock */ }
    // This works (reentrant) but indicates poor design
}
```

## 18. Pitfalls

### Deadlock
```java
// Thread 1: synchronized(A) { synchronized(B) { } }
// Thread 2: synchronized(B) { synchronized(A) { } }
// → Deadlock when both threads acquire first lock simultaneously
```

### Starvation
High-priority threads or unfair locks can starve lower-priority threads.

### Lock Convoy
Many threads competing for one lock become serialized, defeating parallelism.

### Invisible Synchronization
Using `synchronized` on a method doesn't prevent unsynchronized access from other code.

## 19. Debugging Tips

1. **Check `Thread.getState()`**: Detect BLOCKED threads
2. **Use `jstack`**: See which locks threads are waiting for
3. **Use `ThreadMXBean.findDeadlockedThreads()`**: Detect deadlocks programmatically
4. **Log lock acquisition**: Add logging inside synchronized blocks
5. **Use `-XX:+PrintBiasedLockingStatistics`**: Monitor biased locking (Java <15)
6. **Profile with async-profiler**: Find contention hotspots
7. **Test with `Thread.sleep()` in critical sections**: Reveal race conditions
8. **Use jcstress**: Framework for testing concurrency correctness

## 20. Comparison Table

| Feature | `synchronized` | `volatile` | `ReentrantLock` |
|---------|---------------|------------|-----------------|
| Mutual exclusion | Yes | No | Yes |
| Memory visibility | Yes | Yes | Yes |
| Reentrant | Yes | N/A | Yes |
| Timed waiting | No | N/A | Yes |
| Interruptible | No | N/A | Yes |
| Fair ordering | No | N/A | Optional |
| Condition support | Only wait/notify | N/A | Multiple Conditions |
| Performance | Good | Best | Good |
| Scope | Block/method | Field | Block |

## 21. Decision Tree

```
Need to protect shared mutable data?
├── Single variable, simple update?
│   └── Yes → Use AtomicInteger/AtomicReference
├── Compound operation (read-modify-write)?
│   ├── Yes → Need timeout/interrupt?
│   │   ├── Yes → Use ReentrantLock
│   │   └── No → Use synchronized
│   └── No → Multiple reads, few writes?
│       └── Yes → Use ReadWriteLock
└── Complex state with conditions?
    └── Use ReentrantLock + Condition
```

## 22. Interview Questions

### Q1: What is the difference between `synchronized` and `volatile`?
**A**: `synchronized` provides mutual exclusion AND memory visibility. `volatile` only provides memory visibility (no mutual exclusion). Use `synchronized` for compound operations, `volatile` for simple flags/variables.

### Q2: Can `synchronized` cause deadlocks?
**A**: Yes, if threads acquire multiple locks in inconsistent order. Prevent by always acquiring locks in the same order, or use `tryLock()` with timeout.

### Q3: What is a reentrant lock?
**A**: A lock that can be acquired multiple times by the same thread without blocking. Java intrinsic locks are reentrant.

### Q4: Why is `count++` not thread-safe?
**A**: It involves three operations: read, increment, write. Another thread can interleave between these operations.

### Q5: When would you use `synchronized` vs `ReentrantLock`?
**A**: Use `synchronized` for simple cases. Use `ReentrantLock` when you need timeout, interruptibility, fair ordering, or multiple condition variables.

### Q6: What happens if a thread throws an exception in a synchronized block?
**A**: The lock is automatically released (via `monitorexit` instruction). This is true even for exceptions.

### Q7: Can two threads access different synchronized methods on the same object simultaneously?
**A**: No. Both methods synchronize on the same object (this). Only one can execute at a time.

## 23. Exercises

### Exercise 1: Thread-Safe Counter
Implement a thread-safe counter using:
1. `synchronized`
2. `AtomicInteger`
3. `ReentrantLock`
Compare the performance of each approach.

### Exercise 2: Producer-Consumer
Implement a producer-consumer pattern using `synchronized` with `wait()`/`notify()`:
- Bounded buffer of size 10
- Multiple producers and consumers
- Graceful shutdown

### Exercise 3: ReadWriteLock
Implement a read-write lock pattern:
- Multiple concurrent readers
- Exclusive writer
- Monitor read/write counts
- Verify no race conditions

## 24. Assignments

### Assignment 1: Thread-Safe Cache
Build a thread-safe cache using synchronization:
- `get(key)` returns cached value
- `put(key, value)` stores value
- `evict(key)` removes entry
- `size()` returns current size
- Support concurrent access

### Assignment 2: Dining Philosophers
Implement the dining philosophers problem:
- 5 philosophers, 5 forks
- Use `synchronized` or `ReentrantLock`
- Prevent deadlock
- Ensure all philosophers eat

## 25. Mini Project

### Concurrent Data Structure

Implement a thread-safe data structure:

```java
// Requirements:
// 1. Thread-safe bounded blocking queue
// 2. Support put() with timeout
// 3. Support take() with timeout
// 4. Support drain() for batch retrieval
// 5. Monitor capacity, size, and waiters
// 6. Implement graceful shutdown
```

## 26. Summary

Key takeaways on synchronization:

- **`synchronized` provides mutual exclusion AND memory visibility**
- **Every object has an intrinsic lock** that `synchronized` uses
- **Locks are reentrant**: Same thread can acquire multiple times
- **`volatile` provides visibility but not mutual exclusion**
- **Minimize synchronized scope** to reduce contention
- **Prefer concurrent utilities** over manual synchronization
- **Always handle `InterruptedException`** in waiting methods
- **Test concurrency** with multiple threads and stress testing

## 27. References

### Official Documentation
- [Synchronized Statement](https://docs.oracle.com/en/java/javase/21/essential/concurrency/syncmeth.html)
- [Volatile Keyword](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html#jls-17.4.1)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)

### Books
- *Java Concurrency in Practice* by Brian Goetz (Chapter 3-4)
- *Effective Java* by Joshua Bloch (Item 78-82)

### Online Resources
- [Baeldung Synchronization](https://www.baeldung.com/java-synchronized)
- [Oracle Concurrency Tutorial](https://docs.oracle.com/en/java/javase/21/essential/concurrency/)

### Related Topics
- [Locks](../05-locks/README.md)
- [Atomic Variables](../06-atomic-variables/README.md)
- [Thread Lifecycle](../03-thread-lifecycle/README.md)
