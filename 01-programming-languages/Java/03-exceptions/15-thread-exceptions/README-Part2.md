# 15 - Thread Exceptions (Part 2)
**Previous:** [Part 1](README.md)

## Virtual Threads and Exception Propagation (Java 21)

Virtual threads handle exceptions similarly to platform threads but with some differences:

```java
// Virtual thread with try-catch
Thread.startVirtualThread(() -> {
    try {
        throw new RuntimeException("Virtual thread error");
    } catch (Exception e) {
        System.err.println("Caught in virtual thread: " + e.getMessage());
    }
});

// Virtual thread with UncaughtExceptionHandler
Thread vt = Thread.ofVirtual()
    .name("my-virtual-thread")
    .uncaughtExceptionHandler((t, e) -> {
        System.err.println("Uncaught in virtual thread: " + e.getMessage());
    })
    .start(() -> {
        throw new RuntimeException("Unhandled");
    });
```

Key considerations:
- Virtual threads are lightweight; thousands may exist simultaneously
- Setting UncaughtExceptionHandler on each virtual thread may be impractical
- Consider using StructuredTaskScope for structured concurrency (preview in Java 21)
- Pinning (holding monitors during blocking) can affect exception handling behavior

---

## Common Pitfalls

### 1. Exceptions Lost in Thread Pools

```java
// BAD: Exception silently swallowed
executor.submit(() -> {
    throw new RuntimeException("Lost forever"); // Never reported!
});

// GOOD: Check Future or use execute()
Future<?> f = executor.submit(() -> {
    throw new RuntimeException("Captured");
});
f.get(); // Throws ExecutionException
```

### 2. Uncaught Handler Not Set

```java
// BAD: No handler, exception goes to System.err only
new Thread(() -> riskyOperation()).start();

// GOOD: Set default handler early
Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
    monitoringService.report(t, e);
});
```

### 3. Exception in finally Block

```java
// Exception in finally overrides exception in try
try {
    throw new RuntimeException("Original");
} finally {
    throw new RuntimeException("In finally"); // Original is lost!
}
```

### 4. Catching Too Broadly

```java
// BAD: Catches everything, hides real issues
try {
    threadPool.submit(task).get();
} catch (Exception e) {
    // What actually failed?
}

// GOOD: Specific exception handling
try {
    threadPool.submit(task).get();
} catch (ExecutionException e) {
    Throwable cause = e.getCause();
    if (cause instanceof CustomException) {
        handleCustomException((CustomException) cause);
    }
}
```

### 5. Forgetting to Handle Future.get() Timeout

```java
// BAD: May block forever
future.get();

// GOOD: Always use timeout
future.get(30, TimeUnit.SECONDS);
```

---

## Production Patterns

### Thread Pool Exception Handling

```java
public class MonitoredThreadPool {
    private final ThreadPoolExecutor executor;
    
    public MonitoredThreadPool(int coreSize, int maxSize) {
        this.executor = new ThreadPoolExecutor(
            coreSize, maxSize, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            threadFactory("worker"),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        // Set rejected execution handler
        executor.setRejectedExecutionHandler((r, e) -> {
            Metrics.counter("thread.pool.rejected").increment();
            log.warn("Task rejected, CallerRunsPolicy will execute in caller");
            e.execute(r); // CallerRunsPolicy
        });
    }
    
    public <T> Future<T> submit(Callable<T> task) {
        Callable<T> monitored = () -> {
            long start = System.nanoTime();
            try {
                return task.call();
            } catch (Exception e) {
                Metrics.counter("thread.task.exception").increment();
                throw e;
            } finally {
                Metrics.timer("thread.task.duration")
                    .record(System.nanoTime() - start, TimeUnit.NANOSECONDS);
            }
        };
        return executor.submit(monitored);
    }
}
```

### Structured Exception Logging

```java
public class ExceptionLogger implements UncaughtExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionLogger.class);
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        MDC.put("thread.name", t.getName());
        MDC.put("thread.id", String.valueOf(t.getId()));
        
        if (e instanceof OutOfMemoryError) {
            log.error("Fatal: OutOfMemoryError in thread {}", t.getName(), e);
            Runtime.getRuntime().halt(1); // Force shutdown
        } else if (e instanceof StackOverflowError) {
            log.error("Fatal: StackOverflowError in thread {}", t.getName(), e);
        } else {
            log.error("Uncaught exception in thread {}", t.getName(), e);
        }
        
        MDC.clear();
    }
}
```

### Graceful Shutdown with Exception Handling

```java
public class GracefulShutdown {
    private final ExecutorService executor;
    private final Thread shutdownHook;
    
    public GracefulShutdown() {
        this.executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors()
        );
        
        this.shutdownHook = new Thread(() -> {
            log.info("Shutdown initiated");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        });
        
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        
        // Set global uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("Uncaught exception in {}", t.getName(), e);
        });
    }
}
```

---

## Summary

| Mechanism | Use Case | Scope |
|-----------|----------|-------|
| UncaughtExceptionHandler | Last-resort handler | Per-thread or global |
| setDefaultUncaughtExceptionHandler | Application-wide default | All threads |
| Future.get() | Retrieve executor task exceptions | Calling thread |
| CompletableFuture.exceptionally() | Functional exception recovery | Async pipeline |
| CompletableFuture.handle() | Handle both success and failure | Async pipeline |
| try-catch in Runnable | Explicit handling | Current thread |

---

## Related Topics

- [Exception Basics](../01-exception-basics/)
- [Custom Exceptions](../02-custom-exceptions/)
- [try-with-resources](../05-try-with-resources/)
- [Exception Performance](../10-exception-performance/)
