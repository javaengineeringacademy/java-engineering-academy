# Introduction to Multithreading (Part 3)

[📖 Back to Part 1](README.md) | [📖 Back to Part 2](README-part2.md)

---

- Aggregate results safely
- Handle file not found exceptions

## 25. Mini Project

### Thread Monitor Dashboard

Create a thread monitoring system:

```java
// Requirements:
// 1. Create and manage multiple thread pools
// 2. Monitor thread states (active, idle, waiting, terminated)
// 3. Track task submission and completion rates
// 4. Detect deadlocks in real-time
// 5. Generate thread dump reports
// 6. Implement thread pool auto-scaling
```

**Features to implement:**
- Thread pool creation with configurable parameters
- Real-time monitoring dashboard (console-based)
- Deadlock detection using `ThreadMXBean`
- Task queue monitoring
- Thread state change notifications
- Graceful shutdown with timeout
- Performance metrics collection

## 26. Summary

Key takeaways from this introduction to multithreading:

- **Threads are lightweight units of execution** that share process memory
- **Concurrency** means overlapping execution; **parallelism** means simultaneous execution
- **Java threads map to OS threads** (1:1 mapping in modern JVM)
- **Thread creation has overhead** (~1MB memory per thread)
- **Always handle `InterruptedException`** properly
- **Use `start()` to create threads**, never call `run()` directly
- **Prefer `Runnable`/`Callable` over extending `Thread`**
- **Use executor services** instead of creating threads manually
- **Virtual threads** (Java 21+) offer lightweight concurrency for I/O-bound tasks

## 27. References

### Official Documentation
- [Oracle Threads Tutorial](https://docs.oracle.com/en/java/javase/21/essential/concurrency/)
- [Thread Class API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)

### Books
- *Java Concurrency in Practice* by Brian Goetz
- *Effective Java* by Joshua Bloch (Item 78-82)
- *Java: The Complete Reference* by Herbert Schildt

### Online Resources
- [Baeldung Threading Guide](https://www.baeldung.com/java-concurrency)
- [Jenkov Java Concurrency](https://jenkov.com/tutorials/java-concurrency/)
- [OpenJDK Virtual Threads Documentation](https://openjdk.org/jeps/444)

### Related Topics
- [Thread Creation](../02-thread-creation/README.md)
- [Thread Lifecycle](../03-thread-lifecycle/README.md)
- [Synchronization](../04-synchronization/README.md)
