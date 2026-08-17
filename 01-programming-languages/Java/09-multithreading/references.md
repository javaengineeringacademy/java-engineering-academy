# Multithreading References

## Official Documentation

### Oracle Java Concurrency
- [Trail: Concurrency (Oracle Docs)](https://docs.oracle.com/javase/tutorial/essential/concurrency/) - Official Java tutorial covering threads, synchronization, executors, and concurrent collections.
- [Concurrency API (Java SE 21)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html) - API documentation for `java.util.concurrent` package.
- [java.lang.Thread](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html) - Thread class documentation.
- [Java Memory Model (JLS §17)](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html) - Formal specification of happens-before, synchronization, and memory visibility.

### OpenJDK Virtual Threads
- [JEP 444: Virtual Threads (Final)](https://openjdk.org/jeps/444) - Official proposal for virtual threads in Java 21.
- [JEP 425: Virtual Threads (Preview)](https://openjdk.org/jeps/425) - Earlier preview of virtual threads.
- [Project Loom](https://openjdk.org/projects/loom/) - Overview of Project Loom and virtual threads.
- [Virtual Threads Best Practices (Oracle)](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html) - Official guidance on when and how to use virtual threads.

---

## Books

### Java Concurrency in Practice
- **Authors**: Brian Goetz, Tim Peierls, Joshua Bloch, Joseph Bowbeer, David Holmes, Doug Lea
- **ISBN**: 0321349601
- **Why read**: The definitive guide to Java concurrency. Covers the `java.util.concurrent` package, thread safety, building concurrent applications, and advanced patterns. Essential for understanding why concurrent code breaks and how to fix it.

### Effective Java (3rd Edition)
- **Authors**: Joshua Bloch
- **ISBN**: 013468599X
- **Concurrency chapters**: Chapter 11 (Concurrency) covers synchronized, `volatile`, concurrent collections, executors, parallelism, and avoiding common concurrency pitfalls. Concise and practical.

### Java: The Complete Reference (Herbert Schildt)
- **Chapters on concurrency**: Covers threading basics through advanced executor frameworks. Good for structured learning from fundamentals.

---

## Tutorials and Guides

### Baeldung Concurrency
- [Java Concurrency](https://www.baeldung.com/java-concurrency) - Comprehensive index of concurrency tutorials.
- [Virtual Threads in Java](https://www.baeldung.com/java-virtual-threads) - Virtual threads walkthrough with examples.
- [ReentrantLock in Java](https://www.baeldung.com/java-reentrant-lock) - Lock API deep dive.
- [ExecutorService Guide](https://www.baeldung.com/java-executor-service-tutorial) - Thread pool management.
- [CompletableFuture Guide](https://www.baeldung.com/java-completablefuture) - Async programming with CompletableFuture.
- [ConcurrentHashMap Guide](https://www.baeldung.com/java-concurrent-hashmap) - Concurrent map operations.
- [ThreadLocal Guide](https://www.baeldung.com/java-threadlocal) - Thread-local storage patterns.
- [CountDownLatch vs CyclicBarrier](https://www.baeldung.com/java-countdown-latch-cyclic-barrier) - Synchronization utilities compared.

### Java Official Tutorials
- [Basic Concurrency](https://docs.oracle.com/javase/tutorial/essential/concurrency/runsync.html) - Synchronized and volatile.
- [High-Level Concurrency Objects](https://docs.oracle.com/javase/tutorial/essential/concurrency/highlevel.html) - Executors, locks, concurrent collections.

### Other Tutorials
- [Jenkov - Java Concurrency](http://tutorials.jenkov.com/java-concurrency/index.html) - In-depth tutorial series on every concurrency topic.
- [HowToXML - Java Concurrency](https://howtodoinjava.com/java-concurrency-tutorial/) - Practical examples and patterns.

---

## Practice Platforms

### LeetCode
- [Concurrency Problems](https://leetcode.com/problemset/concurrency/) - Dedicated concurrency section with problems like Dining Philosophers, Fizz Buzz Multithreaded, and Print in Order.

### HackerRank
- [Java Concurrency](https://www.hackerrank.com/domains/java?badge_type=java) - Java-specific challenges including threading and synchronization.

### Codewars
- [Concurrency Kata](https://www.codewars.com/) - Search for concurrency katas. Good for practicing thread-safe data structures and patterns.

### Exercism
- [Java Track](https://exercism.org/tracks/java) - Mentor-guided exercises, some covering concurrency.

---

## Quick Reference Links

| Topic | Resource |
|---|---|
| Thread basics | [Oracle Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/) |
| Virtual Threads | [JEP 444](https://openjdk.org/jeps/444) |
| Lock API | [ReentrantLock Docs](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/locks/ReentrantLock.html) |
| Executors | [ExecutorService Docs](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ExecutorService.html) |
| CompletableFuture | [CompletableFuture Docs](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html) |
| ConcurrentHashMap | [ConcurrentHashMap Docs](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html) |
| Memory Model | [JLS §17](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html) |
