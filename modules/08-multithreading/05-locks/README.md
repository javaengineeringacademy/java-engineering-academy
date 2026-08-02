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
    private int count = 0;

    public void increment() {
        lock.lock();
        try {
            count++;
            System.out.println(Thread.currentThread().getName() +
                " incremented to " + count);
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockBasics demo = new LockBasics();

        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    demo.increment();
                }
            }, "Thread-" + i);
            threads[i].start();
        }

        for (Thread t : threads) t.join();
        System.out.println("Final count: " + demo.getCount());
    }
}
```

## 12. Medium Example

```java
import java.util.concurrent.locks.*;

public class BoundedBuffer<T> {
    private final Object[] buffer;
    private int head = 0, tail = 0, count = 0;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedBuffer(int capacity) {
        buffer = new Object[capacity];
    }

    public void put(T item, long timeoutMs) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            long deadline = System.currentTimeMillis() + timeoutMs;
            while (count == buffer.length) {
                long remaining = deadline - System.currentTimeMillis();
                if (remaining <= 0) {
                    throw new InterruptedException("Timeout waiting for space");
                }
                notFull.await(remaining, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
            buffer[tail] = item;
            tail = (tail + 1) % buffer.length;
            count++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public T take(long timeoutMs) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            long deadline = System.currentTimeMillis() + timeoutMs;
            while (count == 0) {
                long remaining = deadline - System.currentTimeMillis();
                if (remaining <= 0) {
                    throw new InterruptedException("Timeout waiting for item");
                }
                notEmpty.await(remaining, java.util.concurrent.TimeUnit.MILLISECONDS);
            }
            T item = (T) buffer[head];
            buffer[head] = null;
            head = (head + 1) % buffer.length;
            count--;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);

        Thread producer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    buffer.put(i, 1000);
                    System.out.println("Produced: " + i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Producer");

        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    Integer item = buffer.take(1000);
                    System.out.println("Consumed: " + item);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
```

## 13. Hard Example

```java
import java.util.concurrent.locks.*;

public class ReadWriteLockCache<K, V> {
    private final java.util.Map<K, V> cache = new java.util.HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    private final int maxSize;
    private int hits = 0, misses = 0;

    public ReadWriteLockCache(int maxSize) {
        this.maxSize = maxSize;
    }

    public V get(K key) {
        readLock.lock();
        try {
            V value = cache.get(key);
            if (value != null) {
                hits++;
                return value;
            }
            misses++;
            return null;
        } finally {
            readLock.unlock();
        }
    }

    public void put(K key, V value) {
        writeLock.lock();
        try {
            if (cache.size() >= maxSize && !cache.containsKey(key)) {
                // Evict oldest entry (simplified)
                K firstKey = cache.keySet().iterator().next();
                cache.remove(firstKey);
            }
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public V computeIfAbsent(K key, java.util.function.Function<K, V> mappingFunction) {
        // First try read lock (optimistic)
        readLock.lock();
        try {
            V value = cache.get(key);
            if (value != null) {
                hits++;
                return value;
            }
        } finally {
            readLock.unlock();
        }

        // Upgrade to write lock
        writeLock.lock();
        try {
            // Double-check after acquiring write lock
            V value = cache.get(key);
            if (value != null) {
                hits++;
                return value;
            }

            // Compute and store
            value = mappingFunction.apply(key);
            cache.put(key, value);
            misses++;
            return value;
        } finally {
            writeLock.unlock();
        }
    }

    public void printStats() {
        readLock.lock();
        try {
            System.out.printf("Cache: size=%d, hits=%d, misses=%d, hitRate=%.2f%%%n",
                cache.size(), hits, misses,
                hits + misses > 0 ? (100.0 * hits / (hits + misses)) : 0);
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockCache<String, String> cache = new ReadWriteLockCache<>(100);

        // Pre-populate
        for (int i = 0; i < 50; i++) {
            cache.put("key-" + i, "value-" + i);
        }

        // Concurrent reads
        Thread[] readers = new Thread[10];
        for (int i = 0; i < 10; i++) {
            readers[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    cache.get("key-" + (j % 50));
                }
            });
            readers[i].start();
        }

        // Concurrent writes
        Thread[] writers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            writers[i] = new Thread(() -> {
                for (int j = 50; j < 100; j++) {
                    cache.put("key-" + j, "value-" + j);
                }
            });
            writers[i].start();
        }

        for (Thread t : readers) t.join();
        for (Thread t : writers) t.join();

        cache.printStats();
    }
}
```

## 14. Enterprise Example

```java
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class DistributedLockSimulator {
    private final ReentrantLock globalLock = new ReentrantLock(true);
    private final ConcurrentHashMap<String, ReentrantLock> resourceLocks = new ConcurrentHashMap<>();
    private final Condition lockAvailable = globalLock.newCondition();
    private final ConcurrentHashMap<String, Long> lockTimestamps = new ConcurrentHashMap<>();
    private static final long LOCK_TIMEOUT_MS = 30_000;

    public boolean acquireLock(String resourceId, long timeoutMs) {
        ReentrantLock resourceLock = resourceLocks.computeIfAbsent(
            resourceId, k -> new ReentrantLock());

        long deadline = System.currentTimeMillis() + timeoutMs;
        long remaining = timeoutMs;

        while (remaining > 0) {
            if (resourceLock.tryLock(remaining, TimeUnit.MILLISECONDS)) {
                lockTimestamps.put(resourceId, System.currentTimeMillis());
                System.out.println("[" + Thread.currentThread().getName() +
                    "] Acquired lock on " + resourceId);
                return true;
            }
            remaining = deadline - System.currentTimeMillis();
        }

        System.out.println("[" + Thread.currentThread().getName() +
            "] Failed to acquire lock on " + resourceId + " (timeout)");
        return false;
    }

    public void releaseLock(String resourceId) {
        ReentrantLock resourceLock = resourceLocks.get(resourceId);
        if (resourceLock != null && resourceLock.isHeldByCurrentThread()) {
            resourceLock.unlock();
            lockTimestamps.remove(resourceId);
            System.out.println("[" + Thread.currentThread().getName() +
                "] Released lock on " + resourceId);
        }
    }

    public void detectStaleLocks() {
        long now = System.currentTimeMillis();
        lockTimestamps.forEach((resourceId, timestamp) -> {
            if (now - timestamp > LOCK_TIMEOUT_MS) {
                System.out.println("WARNING: Lock on " + resourceId +
                    " held for " + (now - timestamp) + "ms (possible stale lock)");
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        DistributedLockSimulator simulator = new DistributedLockSimulator();

        // Simulate concurrent access to shared resources
        String[] resources = {"database", "file-system", "cache"};

        Thread[] threads = new Thread[6];
        for (int i = 0; i < 6; i++) {
            final int id = i;
            final String resource = resources[i % resources.length];
            threads[i] = new Thread(() -> {
                if (simulator.acquireLock(resource, 5000)) {
                    try {
                        System.out.println("[" + Thread.currentThread().getName() +
                            "] Processing " + resource);
                        Thread.sleep(1000 + (int)(Math.random() * 2000));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        simulator.releaseLock(resource);
                    }
                }
            }, "Worker-" + id);
            threads[i].start();
        }

        for (Thread t : threads) t.join();
        simulator.detectStaleLocks();
    }
}
```

## 15. Performance

### Lock Performance Comparison

| Lock Type | Uncontested | Contended | Fair |
|-----------|------------|-----------|------|
| `synchronized` | ~20 ns | ~1-10 μs | No |
| `ReentrantLock` | ~20 ns | ~1-10 μs | Optional |
| `ReentrantReadWriteLock` (read) | ~10 ns | ~100 ns | Optional |
| `ReentrantReadWriteLock` (write) | ~20 ns | ~1-10 μs | Optional |
| `StampedLock` (optimistic) | ~5 ns | N/A | No |

### When to Use Each Lock

| Scenario | Recommended Lock |
|----------|-----------------|
| Simple mutual exclusion | `synchronized` |
| Timeout required | `ReentrantLock` |
| Interruptible locking | `ReentrantLock` |
| Read-heavy workload | `ReadWriteLock` |
| Read-write with validation | `StampedLock` |
| Multiple conditions | `ReentrantLock` |
| Fair ordering required | `ReentrantLock(true)` |

### Performance Tips

1. **Minimize lock hold time**: Only hold lock during critical section
2. **Use read locks for reads**: Multiple readers can proceed concurrently
3. **Prefer optimistic reads**: When reads are more common than writes
4. **Avoid lock escalation**: Don't acquire write lock when read lock suffices
5. **Use tryLock with timeout**: Prevent indefinite blocking
6. **Profile contention**: Use async-profiler to find hot spots

## 16. Best Practices

1. **Always unlock in finally**: Ensure lock release even on exceptions
2. **Don't transfer lock ownership**: Only the acquiring thread should release
3. **Use tryLock for deadlock prevention**: Set timeouts to prevent deadlocks
4. **Minimize lock scope**: Only protect shared mutable data
5. **Prefer private lock objects**: Don't expose lock instances
6. **Document lock ordering**: Prevent deadlocks in complex code
7. **Consider lock alternatives**: Atomics, concurrent collections, actors
8. **Test under contention**: Verify correctness with multiple threads
9. **Monitor lock usage**: Track contention and hold times
10. **Choose appropriate lock type**: Match lock to workload pattern

## 17. Common Mistakes

```java
// Mistake 1: Forgetting to unlock
lock.lock();
// Critical section
// BUG: Lock never released if exception occurs!
lock.unlock();

// Fix:
lock.lock();
try {
    // Critical section
} finally {
    lock.unlock();
}

// Mistake 2: Unlocking in wrong place
try {
    lock.lock();
    if (condition) {
        return; // BUG: Lock not released on return!
    }
} finally {
    lock.unlock();
}

// Mistake 3: Using same lock for different purposes
private final ReentrantLock lock = new ReentrantLock();

public void methodA() { lock.lock(); /* ... */ lock.unlock(); }
public void methodB() { lock.lock(); /* ... */ lock.unlock(); }
// Both methods contend for same lock, even if independent

// Fix: Use separate locks for separate resources

// Mistake 4: Not handling InterruptedException
try {
    lock.lockInterruptibly();
    // work
} catch (InterruptedException e) {
    // Lock not acquired - don't try to unlock!
}

// Mistake 5: Using StampedLock incorrectly
long stamp = sl.writeLock();
try {
    // work
} finally {
    sl.unlockWrite(stamp); // Must use same stamp!
}
```

## 18. Pitfalls

### Lock Starvation
Unfair locks may starve threads. Use fair locks when starvation is unacceptable (at performance cost).

### Reentrancy Confusion
`tryLock()` doesn't support reentrancy by default. Use `getHoldCount()` to check.

### ReadWriteLock Deadlock
Converting read lock to write lock must be done carefully to avoid deadlock.

### StampedLock Complexity
Optimistic reads require careful validation and fallback logic.

## 19. Debugging Tips

1. **Check `lock.getQueueLength()`**: See how many threads are waiting
2. **Check `lock.isLocked()`**: Verify lock state
3. **Check `lock.getHoldCount()`**: See reentrancy depth
4. **Use `lock.isHeldByCurrentThread()`**: Verify lock ownership
5. **Monitor contention**: Use JMH or async-profiler
6. **Check `lock.getWaitQueueLength()`**: See condition waiters
7. **Use thread dumps**: See lock ownership in stack traces
8. **Enable lock diagnostics**: `-XX:+UseLockedRegistering` (if available)

## 20. Comparison Table

| Feature | `synchronized` | `ReentrantLock` | `ReadWriteLock` | `StampedLock` |
|---------|---------------|-----------------|-----------------|---------------|
| Reentrant | Yes | Yes | Yes | No |
| Try lock | No | Yes | Yes | Yes |
| Timed lock | No | Yes | Yes | Yes |
| Interruptible | No | Yes | Yes | Yes |
| Fair option | No | Yes | Yes | No |
| Multiple conditions | No | Yes | Yes | No |
| Optimistic read | No | No | No | Yes |
| Performance | Good | Good | Better (reads) | Best (reads) |
| Complexity | Low | Medium | Medium | High |

## 21. Decision Tree

```
Need explicit lock?
├── Need timeout/interruptibility?
│   ├── Yes → ReentrantLock
│   └── No → Need multiple conditions?
│       ├── Yes → ReentrantLock
│       └── No → synchronized is sufficient
├── Read-heavy workload?
│   ├── Yes → Need optimistic reads?
│   │   ├── Yes → StampedLock
│   │   └── No → ReadWriteLock
│   └── No → ReentrantLock
└── Fair ordering required?
    └── Yes → ReentrantLock(true)
```

## 22. Interview Questions

### Q1: What is the difference between `synchronized` and `ReentrantLock`?
**A**: `ReentrantLock` provides tryLock, timed lock, interruptible lock, fair ordering, and multiple conditions. `synchronized` is simpler but lacks these features.

### Q2: When would you use ReadWriteLock?
**A**: When you have many reads and few writes. Read locks allow concurrent readers, improving throughput for read-heavy workloads.

### Q3: What is optimistic locking in StampedLock?
**A**: A non-blocking read that doesn't acquire a lock. After reading, you validate with `validate(stamp)`. If validation fails, fallback to a pessimistic read lock.

### Q4: How does tryLock prevent deadlocks?
**A**: By setting a timeout, `tryLock` prevents indefinite blocking. If a lock can't be acquired within the timeout, the thread can back off and retry, breaking the circular wait condition.

### Q5: Can you use Condition with synchronized?
**A**: No, `synchronized` only supports `wait()`/`notify()`/`notifyAll()`, which act on a single implicit condition. `Condition` provides multiple wait sets.

### Q6: What happens if you forget to unlock?
**A**: The lock is never released, causing all other threads waiting for the lock to block indefinitely. Always unlock in a finally block.

### Q7: Is ReadWriteLock always faster than ReentrantLock?
**A**: No. For write-heavy workloads, ReadWriteLock is slower due to the overhead of managing read/write lock states. Use it only for read-heavy scenarios.

## 23. Exercises

### Exercise 1: Fair Lock Demo
Compare fair vs unfair ReentrantLock:
- Create 10 threads trying to acquire the lock 100 times each
- Measure total acquisition time
- Verify fair lock provides FIFO ordering

### Exercise 2: ReadWriteLock Cache
Implement a thread-safe cache with:
- ReadLock for get operations
- WriteLock for put/remove operations
- Statistics for hits, misses, and contention

### Exercise 3: Condition Variables
Implement a bounded blocking queue using:
- ReentrantLock
- Two Condition variables (notFull, notEmpty)
- Support timeout on put/take operations

## 24. Assignments

### Assignment 1: Lock Monitor
Build a lock monitoring system:
- Track lock acquisition and release times
- Detect lock contention hotspots
- Generate contention reports
- Support multiple lock types

### Assignment 2: Transaction Manager
Implement a simple transaction manager:
- Support begin, commit, rollback
- Use locks for isolation
- Handle deadlocks with timeout
- Support nested transactions

## 25. Mini Project

### Concurrent Cache Framework

Build a high-performance concurrent cache:

```java
// Requirements:
// 1. Support multiple eviction policies (LRU, LFU, FIFO)
// 2. Use ReadWriteLock for concurrency
// 3. Support TTL (time-to-live) for entries
// 4. Monitor hit rate, eviction rate, contention
// 5. Support bulk operations
// 6. Implement cache statistics
```

## 26. Summary

Key takeaways on locks:

- **`synchronized` is sufficient for most cases**: Use explicit locks when you need additional features
- **ReentrantLock**: Adds tryLock, timed lock, interruptible lock, fairness, and conditions
- **ReadWriteLock**: Optimizes read-heavy workloads with shared read locks
- **StampedLock**: Provides optimistic reads for maximum read performance
- **Always unlock in finally**: Prevent lock leaks
- **Prefer tryLock with timeout**: Prevent deadlocks
- **Minimize lock scope**: Reduce contention
- **Choose the right lock**: Match lock type to workload

## 27. References

### Official Documentation
- [Lock Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/Lock.html)
- [ReentrantLock](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html)
- [ReentrantReadWriteLock](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/ReentrantReadWriteLock.html)
- [StampedLock](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/StampedLock.html)

### Books
- *Java Concurrency in Practice* by Brian Goetz (Chapter 13)
- *Effective Java* by Joshua Bloch (Item 81)

### Online Resources
- [Baeldung Lock](https://www.baeldung.com/java-concurrent-locks)
- [Oracle Concurrency Locks](https://docs.oracle.com/javase/tutorial/essential/concurrency/locksync.html)

### Related Topics
- [Synchronization](../04-synchronization/README.md)
- [Atomic Variables](../06-atomic-variables/README.md)
- [Concurrent Collections](../07-concurrent-collections/README.md)
