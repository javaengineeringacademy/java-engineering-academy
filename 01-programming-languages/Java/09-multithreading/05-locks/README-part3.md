# Locks (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

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
- [Oracle Concurrency Locks](https://docs.oracle.com/en/java/javase/21/essential/concurrency/locksync.html)

### Related Topics
- [Synchronization](../04-synchronization/README.md)
- [Atomic Variables](../06-atomic-variables/README.md)
- [Concurrent Collections](../07-concurrent-collections/README.md)
