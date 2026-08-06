# Locks

## 1. Introduction

While `synchronized` provides basic mutual exclusion, Java's explicit lock framework in `java.util.concurrent.locks` offers more flexible and powerful synchronization mechanisms. The `Lock` interface and its implementations—`ReentrantLock`, `ReentrantReadWriteLock`, and `StampedLock`—provide features like timed locking, interruptible locking, fair ordering, and multiple condition variables.

Explicit locks give developers finer control over synchronization behavior, enabling patterns that are difficult or impossible with `synchronized` alone. Understanding when and how to use these locks is essential for building high-performance concurrent applications.

## 2. Learning Objectives

- Understand the Lock interface and its methods
- Learn ReentrantLock and its features (timed lock, interruptible lock, fairness)
- Understand ReentrantReadWriteLock for read-heavy workloads
- Learn about StampedLock for optimistic reading
- Know when to use each lock type
- Understand lock ordering to prevent deadlocks
- Learn about Condition variables
- Understand lock performance characteristics

## 3. Prerequisites

- Module 08: Synchronization
- Module 08: Thread Lifecycle
- Understanding of intrinsic locks (synchronized)
- Basic knowledge of the java.util.concurrent package

## 4. Why This Concept Exists

`synchronized` has limitations:
- Cannot attempt to acquire a lock with a timeout
- Cannot interrupt a thread waiting for a lock
- Cannot ensure fair ordering (threads may starve)
- Only one condition variable (wait/notify)
- Cannot query lock state (is it held? by whom?)

Explicit locks address these limitations:

```java
// Try to acquire lock with timeout
if (lock.tryLock(1, TimeUnit.SECONDS)) {
    try { /* work */ }
    finally { lock.unlock(); }
} else {
    // Handle timeout
}

// Interruptible lock acquisition
try {
    lock.lockInterruptibly();
    try { /* work */ }
    finally { lock.unlock(); }
} catch (InterruptedException e) {
    // Handle interruption
}
```

## 5. Problem Statement

Consider a web application where:
1. Most operations read data (95% reads, 5% writes)
2. Some operations may take a long time
3. Users expect responsive behavior
4. Deadlock prevention is critical

With `synchronized`:
- All operations are exclusive, even reads
- Long operations block all other threads
- No timeout mechanism for lock acquisition
- Risk of deadlock

With explicit locks:
- ReadWriteLock allows concurrent reads
- tryLock() prevents indefinite blocking
- lockInterruptibly() enables cancellation
- Fair locks prevent starvation

## 6. Theory

### The Lock Interface

```java
public interface Lock {
    void lock();                    // Acquire lock (blocking)
    void lockInterruptibly();       // Acquire lock (interruptible)
    boolean tryLock();              // Try to acquire (non-blocking)
    boolean tryLock(long time, TimeUnit unit); // Try with timeout
    void unlock();                  // Release lock
    Condition newCondition();       // Create condition variable
}
```

### ReentrantLock

A reentrant mutual exclusion lock with the same basic behavior as `synchronized`, but with additional features:

```java
ReentrantLock lock = new ReentrantLock();
// or
ReentrantLock fairLock = new ReentrantLock(true); // Fair ordering
```

**Features:**
- Reentrant: Same thread can acquire multiple times
- Timed: `tryLock(timeout, unit)`
- Interruptible: `lockInterruptibly()`
- Fair: Optional FIFO ordering
- Multiple conditions: `newCondition()`

### ReentrantReadWriteLock

Separates locks into read and write locks:
- **Read lock**: Shared (multiple readers allowed)
- **Write lock**: Exclusive (only one writer, no readers)

```java
ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
Lock readLock = rwLock.readLock();
Lock writeLock = rwLock.writeLock();

readLock.lock();    // Multiple threads can hold this
readLock.unlock();

writeLock.lock();   // Only one thread can hold this
writeLock.unlock();
```

### StampedLock (Java 8+)

A capability-based lock with three modes:
- **Writing**: Exclusive
- **Reading**: Shared
- **Optimistic reading**: Non-blocking, validate after reading

```java
StampedLock sl = new StampedLock();

// Optimistic read
long stamp = sl.tryOptimisticRead();
// Read shared data
if (!sl.validate(stamp)) {
    // Fallback to read lock
    stamp = sl.readLock();
    try { /* read data */ }
    finally { sl.unlockRead(stamp); }
}
```

### Condition Variables

Explicit locks support multiple condition variables (unlike synchronized's single wait/notify):

```java
ReentrantLock lock = new ReentrantLock();
Condition notFull = lock.newCondition();
Condition notEmpty = lock.newCondition();

// Producer
lock.lock();
try {
    while (count == items.length) notFull.await();
    items[tail++] = item;
    notEmpty.signal();
} finally {
    lock.unlock();
}

// Consumer
lock.lock();
try {
    while (count == 0) notEmpty.await();
    Object item = items[head++];
    notFull.signal();
} finally {
    lock.unlock();
}
```

## 7. Internal Working

### How ReentrantLock Works

ReentrantLock is implemented using the `AbstractQueuedSynchronizer` (AQS):

```
```
AQS State:
┌─────────────────────────────────────┐
│ state: 0 (unlocked)                │
│ state: 1 (locked, no reentry)      │
│ state: N (locked, N-1 reentries)   │
│ owner: null or Thread reference     │
└─────────────────────────────────────┘

Thread Queue (FIFO, for fairness):
┌─────────────────────────────────────┐
│ HEAD → Thread-2 → Thread-3 → TAIL  │
│ (waiting)  (waiting)   (waiting)   │
└─────────────────────────────────────┘
```

### Lock Acquisition Process

1. **CAS on state**: Attempt to change state from 0 to 1
2. **If succeeds**: Thread acquires lock
3. **If fails**: Check if owner is current thread (reentry)
4. **If not owner**: Thread is enqueued in AQS queue
5. **Park thread**: Thread is suspended until lock is available
6. **Unpark thread**: When lock is released, next thread is woken

### ReadWriteLock Internals

```
State (int, 32 bits):
┌──────────────────────────────────────┐
│ High 16 bits: Hold count (write)     │
│ Low 16 bits: Hold count (read)       │
│ Bit 0: Write lock held               │
└──────────────────────────────────────┘

Read locks: Increment low 16 bits (shared)
Write lock: Set bit 0 (exclusive)
```

## 8. JVM Perspective

### Lock Objects in Memory

```
ReentrantLock object:
┌─────────────────────────────────────┐
│ Object header                       │
│ sync (AQS reference) ──────────────┐│
└────────────────────────────────────┘│
                                      ▼
                              AQS object:
                              ┌─────────────────────┐
                              │ state (int)         │
                              │ exclusiveOwnerThread│
                              │ CLH queue:          │
                              │   HEAD → T → T → TAIL│
                              └─────────────────────┘
```

### CLH Queue

The AQS uses a CLH (Craig, Landin, and Hagersten) lock queue:
- **FIFO ordering**: Fair lock behavior
- **Lock-free enqueue**: Uses CAS
- **Parked threads**: Suspended via `LockSupport.park()`

### Lock Elision by JIT

The JIT compiler may eliminate locks entirely:
- **Escaped lock**: Lock cannot be eliminated
- **Non-escaped lock**: Lock may be eliminated
- **Biased locking**: Single-thread optimization (removed in Java 15)

## 9. Memory Representation

### ReentrantLock Memory Layout

```
ReentrantLock instance (heap):
┌─────────────────────────────────────┐
│ Object header (12 bytes)            │
│ sync ref → AQS object (8 bytes) ───┐│
└────────────────────────────────────┘│
                                      ▼
                              AQS (heap):
                              ┌─────────────────────┐
                              │ state: int (4 bytes) │
                              │ owner: Thread ref    │
                              │ tail → Node          │
                              └──────────┬──────────┘
                                         │
                                         ▼
                              CLH Queue (linked list):
                              ┌─────────┐
                              │ Node    │
                              │ thread: │→ Thread-2
                              │ status: │  PARKED
                              │ next ────────→ Node
                              └─────────┘         │
                                                  ▼
                                           ┌─────────┐
                                           │ Node    │
                                           │ thread: │→ Thread-3
                                           │ status: │  WAITING
                                           │ next:   │→ null
                                           └─────────┘
```

## 10. Syntax

```java
// ============================================
// REENTRANT LOCK
// ============================================
ReentrantLock lock = new ReentrantLock();

// Basic locking
lock.lock();
try {
    // Critical section
} finally {
    lock.unlock(); // Always unlock in finally
}

// Timed locking
boolean acquired = lock.tryLock(1, TimeUnit.SECONDS);
if (acquired) {
    try { /* work */ }
    finally { lock.unlock(); }
} else {
    // Handle timeout
}

// Interruptible locking
try {
    lock.lockInterruptibly();
    try { /* work */ }
    finally { lock.unlock(); }
} catch (InterruptedException e) {
    // Handle interruption
}

// ============================================
// READWRITE LOCK
// ============================================
ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
Lock readLock = rwLock.readLock();
Lock writeLock = rwLock.writeLock();

// Reading
readLock.lock();
try {
    // Multiple threads can read simultaneously
    return data;
} finally {
    readLock.unlock();
}

// Writing
writeLock.lock();
try {
    // Only one thread can write
    data = newData;
} finally {
    writeLock.unlock();
}

// ============================================
// STAMPED LOCK
// ============================================
StampedLock sl = new StampedLock();

// Optimistic read
long stamp = sl.tryOptimisticRead();
String localData = data; // Read without lock
if (!sl.validate(stamp)) {
    // Fallback to pessimistic read
    stamp = sl.readLock();
    try {
        localData = data;
    } finally {
        sl.unlockRead(stamp);
    }
}

// ============================================
// CONDITION VARIABLES
// ============================================
ReentrantLock lock = new ReentrantLock();
Condition dataAvailable = lock.newCondition();
Condition spaceAvailable = lock.newCondition();

lock.lock();
try {
    while (!hasData) {
        dataAvailable.await(5, TimeUnit.SECONDS); // Timed wait
    }
    // Process data
    spaceAvailable.signalAll(); // Notify waiting threads
} finally {
    lock.unlock();
}

// ============================================
// FAIR LOCK
// ============================================
ReentrantLock fairLock = new ReentrantLock(true); // Fair ordering
// Threads acquire in FIFO order (slower but no starvation)
```

## 11. Easy Example

```java
import java.util.concurrent.locks.ReentrantLock;

public class LockBasics {
    private final ReentrantLock lock = new ReentrantLock();

---

[📖 Continue to Part 2](README-part2.md)
