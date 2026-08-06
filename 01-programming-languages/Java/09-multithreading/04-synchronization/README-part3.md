# Synchronization (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


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
