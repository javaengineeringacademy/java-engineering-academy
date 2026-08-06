# Thread Lifecycle (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---


### Q4: What causes a thread to enter TIMED_WAITING?
**A**: `Thread.sleep(ms)`, `Object.wait(ms)`, `Thread.join(ms)`, `LockSupport.parkNanos()`, `LockSupport.parkUntil()`.

### Q5: How do you detect a deadlock in Java?
**A**: Use `ThreadMXBean.findDeadlockedThreads()`, use `jstack`, or use VisualVM.

### Q6: What is a spurious wakeup?
**A**: When `Object.wait()` returns without `notify()` being called. Always use `while (!condition)` loops to handle this.

### Q7: Can a daemon thread prevent JVM shutdown?
**A**: No. When all non-daemon threads finish, the JVM exits and daemon threads are killed. However, daemon threads can delay shutdown if they hold resources.

## 23. Exercises

### Exercise 1: State Monitoring
Write a program that creates 5 threads and monitors their states every 100ms. Print a state summary showing how many threads are in each state.

### Exercise 2: Deadlock Prevention
Implement a deadlock prevention strategy using `tryLock()` with timeout. Two threads try to acquire two locks in different orders.

### Exercise 3: Interruptible Waiting
Create a thread that waits for a condition. Implement a mechanism to interrupt the waiting thread gracefully, with proper cleanup.

### Exercise 4: Thread Lifecycle Visualization
Create a console-based visualization that shows thread state transitions in real-time as threads execute.

## 24. Assignments

### Assignment 1: Thread Monitor
Build a thread monitoring system that:
- Periodically dumps all thread states
- Detects deadlock conditions
- Logs threads stuck in WAITING/BLOCKED states for too long
- Generates alerts for abnormal thread counts

### Assignment 2: Graceful Shutdown Framework
Implement a framework for graceful service shutdown:
- Track all active threads
- Support ordered shutdown (dependencies)
- Implement timeout-based forced shutdown
- Clean up resources properly

## 25. Mini Project

### Thread Lifecycle Visualizer

Build a real-time thread lifecycle visualization:

```java
// Requirements:
// 1. Create multiple threads with different behaviors
// 2. Visualize state transitions in real-time
// 3. Show lock contention and wait/notify
// 4. Detect and highlight deadlocks
// 5. Generate lifecycle reports
// 6. Support interactive thread control (pause, resume, interrupt)
```

## 26. Summary

Key takeaways on thread lifecycle:

- **Six states**: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
- **BLOCKED vs WAITING**: Involuntary (lock) vs voluntary (signal) waiting
- **Spurious wakeups**: Always use `while` loops with `wait()`
- **Use `interrupt()` for cancellation**: Never use deprecated methods
- **Monitor thread states**: Use `ThreadMXBean` and thread dumps
- **Implement timeouts**: Prevent indefinite blocking
- **Handle `InterruptedException`**: Restore interrupt flag or propagate

## 27. References

### Official Documentation
- [Thread.State Enum](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.State.html)
- [Thread Class API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)

### Books
- *Java Concurrency in Practice* by Brian Goetz (Chapter 5)
- *Java Threads* by Scott Oaks and Henry Wong

### Online Resources
- [Baeldung Thread States](https://www.baeldung.com/java-thread-state)
- [Jenkov Thread Lifecycle](https://jenkov.com/tutorials/java-concurrency/thread-signaling.html)

### Related Topics
- [Synchronization](../04-synchronization/README.md)
- [Locks](../05-locks/README.md)
- [Best Practices](../12-best-practices/README.md)
