# Locks — Quiz

## Question 1

What happens if you forget to call `unlock()` in a finally block?

- A) Nothing
- B) The lock is never released, causing deadlock
- C) The lock auto-releases after timeout
- D) An exception is thrown

**Answer: B**
Unlike `synchronized`, explicit locks require manual unlock. Forgetting causes permanent deadlock.

## Question 2

When should you use ReadWriteLock over ReentrantLock?

- A) Always
- B) When reads significantly outnumber writes
- C) When writes outnumber reads
- D) Never

**Answer: B**
ReadWriteLock allows multiple concurrent readers but exclusive writes. It's beneficial when reads >> writes.

## Question 3

What is the difference between `ReentrantLock` and `synchronized`?

- A) They are identical
- B) `ReentrantLock` supports timed wait, interruptible lock, and fairness policies
- C) `synchronized` is more flexible
- D) `ReentrantLock` cannot be reentrant

**Answer: B**
`ReentrantLock` provides `tryLock(timeout)`, `lockInterruptibly()`, and configurable fairness. `synchronized` has none of these features.

## Question 4

What does `tryLock()` return if the lock is unavailable?

- A) Blocks until the lock is acquired
- B) Returns `false` immediately
- C) Throws `LockException`
- D) Waits for a default timeout

**Answer: B**
`tryLock()` is non-blocking — it returns `false` if the lock is not immediately available. This prevents deadlock in some scenarios.

## Question 5

What is lock fairness?

- A) A guarantee that all threads get equal CPU time
- B) A policy that grants the lock to the longest-waiting thread
- C) A setting that prevents starvation
- D) Both B and C

**Answer: D**
A fair lock grants access in FIFO order. This prevents starvation but reduces throughput due to overhead of maintaining the queue.

## Question 6

What is `StampedLock` and when is it useful?

- A) A lock that uses timestamps
- B) A lock with optimistic read,悲观 read, and write modes — best for read-heavy workloads
- C) A replacement for `ReentrantLock`
- D) A lock for database operations

**Answer: B**
`StampedLock` (Java 8+) supports optimistic reads (no locking), read locks, and write locks. Optimistic reads have near-zero overhead for uncontended reads.

## Question 7

What is the output?

```java
ReentrantLock lock = new ReentrantLock();
lock.lock();
lock.lock();
System.out.println(lock.getHoldCount());
lock.unlock();
System.out.println(lock.getHoldCount());
```

- A) 1, 0
- B) 2, 1
- C) 2, 0
- D) Exception

**Answer: B**
`ReentrantLock` is reentrant — the same thread can lock it multiple times. `getHoldCount()` returns 2 (two locks acquired), then 1 after one `unlock()`.

## Question 8

What happens if you call `unlock()` without calling `lock()` first?

- A) Nothing
- B) `IllegalMonitorStateException` is thrown
- C) The lock is released
- D) The thread blocks

**Answer: B**
`unlock()` throws `IllegalMonitorStateException` if the current thread does not hold the lock. Always pair `lock()` and `unlock()` in try-finally blocks.

## Question 9

What is the purpose of `Condition` objects with `ReentrantLock`?

- A) They replace `synchronized`
- B) They provide wait/notify-like functionality with multiple wait sets per lock
- C) They automatically release locks
- D) They prevent deadlocks

**Answer: B**
A `Condition` is tied to a `Lock` and provides `await()`, `signal()`, and `signalAll()`. Multiple Conditions per lock allow threads to wait on different conditions — impossible with a single monitor.

## Question 10

Which is correct for using a `ReentrantLock`?

- A) `lock(); try { ... } finally { unlock(); }`
- B) `try { lock(); ... } finally { unlock(); }`
- C) `lock(); ... unlock();`
- D) `synchronized(lock) { ... }`

**Answer: A**
The correct pattern is `lock()` before `try`, so that `unlock()` is always called in the `finally` block. Placing `lock()` inside `try` means `unlock()` could be called without a corresponding `lock()`.
