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

---

[📖 Continue to Part 2](README-part2.md)

## Related Topics
- [Java Memory Model](../../00-knowledge-atoms/java-memory-model/) — Happens-before relationships
- [Volatile](../05-locks/) — Volatile vs synchronized
- [Virtual Threads](../../15-senior/concurrency-advanced/virtual-threads/) — Modern alternative
- [Lock-free](../../15-senior/concurrency-advanced/lock-free/) — Lock-free alternatives
- [False Sharing](../../15-senior/concurrency-advanced/false-sharing/) — Performance impact
