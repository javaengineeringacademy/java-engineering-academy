# Thread Creation (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

If the main method returns before spawned threads complete, the JVM may terminate them.

### Overusing `Thread.sleep()`
Sleep-based synchronization is unreliable and wastes CPU. Use proper synchronization primitives.

## 19. Debugging Tips

1. **Name all threads**: `thread.setName("order-processor-" + orderId)`
2. **Use jstack**: `jstack <pid>` to see thread states
3. **Use VisualVM**: Monitor thread count and states visually
4. **Check thread state**: `System.out.println(thread.getState())`
5. **Log thread names**: Include thread name in log patterns
6. **Use `Thread.holdsLock()`**: Check if current thread holds a lock
7. **Enable assertions**: `-ea` for runtime checks
8. **Use structured concurrency**: Java 21 preview for better thread management

## 20. Comparison Table

| Approach | Return Value | Exception Handling | Reusability | Complexity |
|----------|-------------|-------------------|-------------|------------|
| Extend Thread | No | In run() | No | Low |
| Runnable | No | In run() | Yes | Low |
| Callable | Yes (Future) | Checked | Yes | Medium |
| CompletableFuture | Yes (async) | CompletionException | Yes | High |
| Virtual Thread | Yes (Future) | Same as above | Yes | Low |

## 21. Decision Tree

```
Need to create a thread?
├── Need to return a value?
│   ├── Yes → Need async composition?
│   │   ├── Yes → CompletableFuture
│   │   └── No → Callable + ExecutorService
│   └── No → Need custom thread config?
│       ├── Yes → ThreadFactory
│       └── No → Need high concurrency (I/O)?
│           ├── Yes → Virtual Threads
│           └── No → Runnable + ExecutorService
└── Extending Thread class?
    └── Only for legacy compatibility
```

## 22. Interview Questions

### Q1: What is the difference between Runnable and Callable?
**A**: `Runnable.run()` returns void and cannot throw checked exceptions. `Callable.call()` returns a value and can throw exceptions. `Callable` results are accessed via `Future`.

### Q2: Why is extending Thread discouraged?
**A**: It couples task logic with threading mechanism, prevents extending other classes (single inheritance), and makes code less reusable with thread pools.

### Q3: What is a ThreadFactory?
**A**: An interface with a single method `newThread(Runnable)` that creates threads on demand. Used by ExecutorService to customize thread names, daemon status, and exception handlers.

### Q4: How do virtual threads differ from platform threads?
**A**: Virtual threads are scheduled by the JVM, not the OS. They're lightweight (few KB vs ~1MB), created in microseconds, and can scale to millions. Platform threads map 1:1 to OS threads.

### Q5: Can you start a Thread object more than once?
**A**: No. Calling `start()` a second time throws `IllegalThreadStateException`. Once a thread completes, it cannot be restarted.

### Q6: What is the advantage of using ExecutorService over manual thread creation?
**A**: Thread pooling (reuse), task queuing, lifecycle management, monitoring, and easier shutdown.

### Q7: When would you use Thread.currentThread().interrupt()?
**A**: When catching `InterruptedException` to restore the interrupt status, so calling code can respond to the interruption.

## 23. Exercises

### Exercise 1: Thread Creation Methods
Create threads using all 4 methods (extend Thread, Runnable, Callable, virtual thread). Compare their behavior and output.

### Exercise 2: Custom Thread Factory
Implement a ThreadFactory that:
- Names threads with a prefix and counter
- Sets threads as daemon
- Sets uncaught exception handlers
- Logs when threads are created

### Exercise 3: Callable with Future
Submit 10 Callable tasks that compute factorials. Collect all results using `Future.get()`.

### Exercise 4: Virtual Thread Comparison
Compare platform thread vs virtual thread performance:
- Create 10,000 threads
- Each thread performs blocking I/O (simulated with sleep)
- Measure total execution time for both approaches

## 24. Assignments

### Assignment 1: Task Processor
Build a task processor that:
- Accepts tasks via `submit(Runnable)` and `submit(Callable)`
- Uses a custom ThreadFactory for thread naming
- Implements graceful shutdown with timeout
- Reports task completion statistics

### Assignment 2: Async Download Manager
Create a file download manager:
- Use CompletableFuture for async downloads
- Chain processing steps (download → validate → store)
- Handle failures with fallback values
- Support cancellation

## 25. Mini Project

### Thread Pool Monitor

Build a monitoring system for thread pools:

```java
// Requirements:
// 1. Create multiple named thread pools
// 2. Monitor active threads, queue size, completed tasks
// 3. Auto-scale pools based on load
// 4. Alert when queues are nearly full
// 5. Generate periodic reports
// 6. Support graceful shutdown with drain
```

## 26. Summary

Key takeaways on thread creation:

- **Runnable**: Simple, no return value, lambda-friendly
- **Callable**: Returns value via Future, throws checked exceptions
- **Extending Thread**: Avoid in modern code, couples task with threading
- **ThreadFactory**: Customizes thread creation for pools
- **Virtual Threads**: Java 21, lightweight, millions possible
- **Always use pools**: Never create threads in tight loops
- **Name your threads**: Critical for debugging
- **Handle exceptions**: Set UncaughtExceptionHandler

## 27. References

### Official Documentation
- [Thread Class](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html)
- [Runnable Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Runnable.html)
- [Callable Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/Callable.html)
- [ThreadFactory Interface](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ThreadFactory.html)

### Books
- *Java Concurrency in Practice* by Brian Goetz (Chapter 5)
- *Effective Java* by Joshua Bloch (Item 78-82)

### Online Resources
- [Baeldung Thread Creation](https://www.baeldung.com/java-thread)
- [Oracle Virtual Threads Tutorial](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)

### Related Topics
- [Thread Lifecycle](../03-thread-lifecycle/README.md)
- [Executor Framework](../08-executor-framework/README.md)
- [Virtual Threads](../11-virtual-threads/README.md)
