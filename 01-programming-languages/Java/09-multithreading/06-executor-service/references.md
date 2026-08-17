# ExecutorService References

## Official Documentation
- [Oracle Java Documentation - ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)
- [Oracle Java Documentation - ThreadPoolExecutor](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html)
- [Oracle Java Documentation - Executors Factory](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/Executors.html)

## Books
- *Effective Java* - Item 84: Don't depend on the thread scheduler
- *Java Concurrency in Practice* - Chapter 8: Thread Pools
- *Java Concurrency in Practice* - Chapter 5: Building Blocks

## Articles
- [Oracle Blog: Executors are dangerous](https://www.oracle.com/technical-resources/articles/java/best-practices.html)
- [Baeldung: ThreadPoolExecutor in Java](https://www.baeldung.com/java-thread-pool-executor-tutorial)
- [HowToGrade: ExecutorService vs ForkJoinPool](https://www.holi.fi/en/blog/2019/06/java-executor-service-vs-fork-join-pool)

## Source Code
- `java.util.concurrent.ThreadPoolExecutor` — Core implementation
- `java.util.concurrent.Executors` — Factory methods
- `java.util.concurrent.AbstractExecutorService` — Base class

## Related Topics
- [Thread Pools](../07-thread-pools/) — Pool configuration and tuning
- [Virtual Threads](../08-virtual-threads/) — Modern alternative to thread pools
- [ForkJoinPool](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ForkJoinPool.html) — Recursive task parallelism
