# References

## Official Documentation
- [JDK 21 — Thread.UncaughtExceptionHandler](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.UncaughtExceptionHandler.html) — Handler for uncaught exceptions
- [JDK 21 — CompletableFuture](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/CompletableFuture.html) — Async exception handling
- [JDK 21 — ExecutorService](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ExecutorService.html) — Thread pool exception handling
- [JEP 444 — Virtual Threads](https://openjdk.org/jeps/444) — Virtual threads exception handling in JDK 21

## Official Source Code
- [OpenJDK 21 — Thread.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/lang/Thread.java) — Thread and UncaughtExceptionHandler source
- [OpenJDK 21 — CompletableFuture.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/util/concurrent/CompletableFuture.java) — CompletableFuture source
- [OpenJDK 21 — ExecutionException.java](https://github.com/openjdk/jdk/blob/master/src/java.base/java/util/concurrent/ExecutionException.java) — ExecutionException source

## Key Classes
- `java.lang.Thread.UncaughtExceptionHandler`
- `java.lang.ThreadGroup`
- `java.util.concurrent.ExecutionException`
- `java.util.concurrent.Future`
- `java.util.concurrent.CompletableFuture`
- `java.util.concurrent.TimeoutException`

## Version History

| Version | Change |
|---------|--------|
| JDK 5 | `ExecutorService` and `Future` introduced |
| JDK 8 | `CompletableFuture` introduced |
| JDK 19 | Virtual Threads preview (JEP 425) |
| JDK 21 | Virtual Threads finalized (JEP 444) |

## Recommended Reading
- **Java Concurrency in Practice** — Brian Goetz, Chapter 5: Building Custom Thread Primitives
- **Effective Java (3rd Ed)** — Item 84: Don't depend on the thread scheduler
- **Core Java, Vol. I** — Cay S. Horstmann, Chapter on Concurrency
