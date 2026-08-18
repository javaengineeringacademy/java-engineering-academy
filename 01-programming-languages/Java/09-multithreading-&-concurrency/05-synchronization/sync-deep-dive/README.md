# Synchronization Deep Dive

## 1. synchronized Keyword - What It Does

The `synchronized` keyword provides **mutual exclusion** and **memory visibility** guarantees. When a thread enters a synchronized block/method:
1. It acquires a lock (monitor)
2. All changes to shared memory made by this thread become visible to other threads
3. Other threads attempting to acquire the same lock block until it is released

```java
synchronized (lockObject) {
    // critical section - only one thread at a time
}
```

---

## 2. Three Levels of Synchronization

### a) Synchronized Instance Method — Locks `this`

```java
public synchronized void increment() {
    count++;
}
```

- **Lock object**: `this` (the instance itself)
- **Scope**: All synchronized instance methods on the same object share the same lock
- **Effect**: One thread per object instance; different instances have independent locks

### b) Static Synchronized Method — Locks `Class` Object

```java
public static synchronized void increment() {
    count++;
}
```

- **Lock object**: `ClassName.class` (the Class object)
- **Scope**: All static synchronized methods across all instances share one lock
- **Effect**: One thread across the entire class; regardless of which instance called it

### c) Synchronized Block — Locks Any Object

```java
synchronized (someObject) {
    // critical section
}
```

- **Lock object**: Explicitly specified
- **Scope**: Only code inside the block; other code using different objects is unaffected
- **Effect**: Fine-grained control; can lock on private objects to avoid external interference

---

## 3. Lock Scope Comparison

| Aspect | Instance Method | Static Method | Block |
|---|---|---|---|
| Lock object | `this` | `ClassName.class` | Any object |
| Scope | Per-instance | Per-class | Per-lock-object |
| External access | Cannot lock on different object from outside | Cannot change | Any object can be used |
| Granularity | Coarse | Coarse | Fine |
| Risk of contention | Medium | High (global lock) | Low (if designed well) |

---

## 4. Reentrancy

A thread that already holds a lock can re-acquire the same lock without deadlocking itself. This is called **reentrancy**.

```java
public synchronized void methodA() {
    methodB(); // same thread calling another synchronized method
}

public synchronized void methodB() {
    // works fine - same thread re-enters the same lock
}
```

Java intrinsic locks (monitors) are reentrant. Each lock has an acquisition count:
- First acquisition: count = 1
- Re-entry: count = 2
- Each exit: count = count - 1
- When count = 0, lock is released

---

## 5. Memory Visibility (Happens-Before)

`synchronized` establishes **happens-before** relationships:
- **Unlock of a monitor happens-before every subsequent lock of that same monitor**
- All writes to variables inside a synchronized block are visible to the next thread that enters the same synchronized block

```java
// Without synchronization - data race possible
private int value;
public void write() { value = 42; }
public int read() { return value; }

// With synchronization - guaranteed visibility
public synchronized void write() { value = 42; }
public synchronized int read() { return value; }
```

---

## 6. synchronized vs volatile

| Feature | synchronized | volatile |
|---|---|---|
| Mutual exclusion | Yes | No |
| Memory visibility | Yes | Yes |
| Atomicity of compound ops | Yes (whole block) | No (only single reads/writes) |
| Can block threads | Yes | No |
| Use case | Complex critical sections | Simple flags, state indicators |
| Performance | Heavier (lock/unlock) | Lighter (memory fence only) |

**Rule of thumb**: Use `volatile` when one thread writes and others only read. Use `synchronized` when multiple threads read and write.

---

## 7. synchronized vs ReentrantLock

| Feature | synchronized | ReentrantLock |
|---|---|---|
| API | Implicit (keyword) | Explicit (lock/unlock) |
| Reentrant | Yes | Yes |
| Try lock | No | `tryLock()` with timeout |
| Interruptible | No | `lockInterruptibly()` |
| Fairness | No | Optional (`new ReentrantLock(true)`) |
| Condition variables | Single (wait/notify) | Multiple (`newCondition()`) |
| Release | Automatic (exit block) | Manual (must call unlock in finally) |

---

## 8. ReadWriteLock

Allows multiple concurrent readers but exclusive writers:

```java
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();    // multiple threads can hold this
rwLock.writeLock().lock();   // exclusive - blocks all readers and writers
```

**Best for**: Read-heavy workloads where reads far outnumber writes.

---

## 9. StampedLock (Java 8+)

Provides three modes:
1. **Writing**: Exclusive, like WriteLock
2. **Reading**: Shared, like ReadLock
3. **Optimistic Reading**: Non-blocking read with validation

```java
StampedLock sl = new StampedLock();
long stamp = sl.tryOptimisticRead(); // non-blocking
if (!sl.validate(stamp)) {
    stamp = sl.readLock(); // fall back to blocking read
}
// ... read shared data ...
sl.unlockRead(stamp);
```

**Best for**: Read-dominant workloads where optimistic reads can avoid locking overhead entirely.

---

## 10. Lock Ordering to Prevent Deadlock

Deadlock occurs when two or more threads hold locks the other needs.

**Solution**: Always acquire locks in the same global order.

```java
// Thread 1
synchronized (lockA) {
    synchronized (lockB) { ... }
}

// Thread 2
synchronized (lockA) {    // SAME ORDER as Thread 1
    synchronized (lockB) { ... }
}
```

**Technique**: Assign a numeric order to every lock and always acquire in ascending order.

---

## 11. Lock Timeout Patterns

Use `tryLock()` with a timeout to avoid indefinite blocking:

```java
ReentrantLock lock = new ReentrantLock();
if (lock.tryLock(5, TimeUnit.SECONDS)) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
} else {
    // handle timeout - do something else
}
```

Benefits:
- Prevents deadlock (threads don't wait forever)
- Allows graceful degradation
- Enables deadlock detection via timeout patterns

---

## 12. Thread Starvation

Starvation occurs when a thread is perpetually denied access to resources because other threads are always prioritized.

Causes:
- **Unfair locks**: High-throughput but can starve low-priority threads
- **Greedy synchronization**: Long-held locks block others indefinitely
- **Priority inversion**: Lower-priority thread holds lock needed by higher-priority thread

Solutions:
- Use fair locks: `new ReentrantLock(true)`
- Minimize lock hold time
- Use lock timeouts

---

## 13. Lock Convoy Problem

A **lock convoy** occurs when many threads contend for the same lock, causing them to form a "convoy" — one thread holds the lock while all others wait, then another acquires it while all wait again. This creates poor CPU utilization because:
- Only one thread runs at a time
- Context switching overhead dominates
- Cache thrashing across CPUs

**Solutions**:
- Reduce lock scope (use finer-grained locks)
- Use `ReadWriteLock` for read-heavy workloads
- Consider lock-free algorithms
- Use thread-local variables where possible

---

## Summary Table

| Concept | Key Point |
|---|---|
| synchronized | Mutual exclusion + memory visibility |
| Instance sync | Locks `this` — per-object |
| Static sync | Locks `Class` — per-class |
| Block sync | Locks any object — fine-grained |
| Reentrancy | Same thread can re-acquire same lock |
| volatile vs synchronized | Visibility-only vs exclusion+visibility |
| ReentrantLock | Explicit lock with tryLock, timeouts, fairness |
| ReadWriteLock | Multiple readers, one writer |
| StampedLock | Optimistic reads + read/write locks |
| Deadlock prevention | Lock ordering + timeouts |
| Starvation | Fair locks + minimize hold time |
| Lock convoy | Reduce contention + finer granularity |

---

## Exercises

See `practices/Practices.java` for 5 exercises and `solutions/Solutions.java` for complete solutions.
