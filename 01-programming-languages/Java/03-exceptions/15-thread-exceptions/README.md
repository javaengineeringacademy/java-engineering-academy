# Thread Exception Handling

## Scope

This topic covers exception handling in multi-threaded Java applications, including uncaught exception handlers, ExecutorService exception patterns, CompletableFuture exception handling, and virtual thread exception propagation.

## Why It Exists

In single-threaded applications, exception handling is straightforward: exceptions propagate up the call stack until caught. In multi-threaded applications, each thread has its own stack, and exceptions in one thread don't automatically propagate to other threads. This creates challenges:

- Exceptions can be silently lost if not properly handled
- Thread pools may silently swallow exceptions
- Debugging becomes harder when exceptions occur in background threads
- Production systems need robust exception monitoring across all threads

## Design Rationale

Java provides multiple mechanisms for thread exception handling:

1. **UncaughtExceptionHandler** — Last-resort handler for any uncaught exception in a thread
2. **Future.get()** — Propagates exceptions from executor tasks to the calling thread
3. **CompletableFuture** — Functional-style exception handling for asynchronous pipelines
4. **try-catch in Runnable/Callable** — Explicit exception handling within thread execution

The design ensures that exceptions are never truly "lost" — they always have a destination if properly configured.

---

## What Are Thread Exceptions

When an exception occurs in a thread, it propagates up that thread's stack. If uncaught, it terminates the thread. Unlike single-threaded code, the exception doesn't automatically affect other threads.

```java
// Exception in this thread kills only this thread
new Thread(() -> {
    throw new RuntimeException("Thread error");
}).start();
// Main thread continues unaffected
```

Key characteristics:
- Each thread has an independent exception handling context
- Uncaught exceptions kill only the thread where they occur
- Thread pools manage thread lifecycle and may recreate threads after exceptions
- Exceptions don't cross thread boundaries without explicit mechanisms

---

## UncaughtExceptionHandler

Every thread has an associated uncaught exception handler called when an exception goes uncaught.

### Thread.UncaughtExceptionHandler Interface

```java
@FunctionalInterface
public interface UncaughtExceptionHandler {
    void uncaughtException(Thread t, Throwable e);
}
```

### Setting Handler Per Thread

```java
Thread thread = new Thread(() -> {
    throw new RuntimeException("Boom");
});

thread.setUncaughtExceptionHandler((t, e) -> {
    System.err.println("Exception in " + t.getName() + ": " + e.getMessage());
});

thread.start();
```

### ThreadGroup Handler

ThreadGroup implements UncaughtExceptionHandler. By default, it delegates to the parent group's handler, ultimately reaching the system default handler.

```java
ThreadGroup group = new ThreadGroup("MyGroup") {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println("Group handler: " + e.getMessage());
    }
};

Thread thread = new Thread(group, () -> {
    throw new RuntimeException("Group error");
});
thread.start();
```

### Handler Resolution Order

1. Thread's own UncaughtExceptionHandler (set via setUncaughtExceptionHandler)
2. ThreadGroup's uncaughtException method
3. System default UncaughtExceptionHandler

If no handler is set at any level, the exception prints to System.err and the thread terminates.

---

## setDefaultUncaughtExceptionHandler

Sets the default handler for all threads that don't have their own handler set.

```java
Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
    Logger.error("Uncaught exception in thread {}: {}", t.getName(), e.getMessage());
    // Report to monitoring system
    Metrics.counter("thread.exception").increment();
});
```

Best practice: Set this early in application startup, before creating any threads.

```java
public class Application {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error("Fatal thread exception", e);
            // Alert operations team
        });
        
        // Start application threads
    }
}
```

---

## Exceptions in ExecutorService

### execute() vs submit()

The behavior differs significantly:

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

// execute() — exception propagates to UncaughtExceptionHandler
executor.execute(() -> {
    throw new RuntimeException("execute exception");
});

// submit() — exception captured in Future, NOT thrown to handler
Future<?> future = executor.submit(() -> {
    throw new RuntimeException("submit exception");
});

// Must call get() to retrieve exception
try {
    future.get();
} catch (ExecutionException e) {
    System.out.println("Captured: " + e.getCause());
}
```

### Why the Difference?

- `execute()` passes the Runnable directly to the thread — no wrapper
- `submit()` wraps the task in a FutureTask — exception is captured, not thrown
- This is by design: submit() returns a Future, so exceptions are part of the Future contract

### Handling Multiple Tasks

```java
List<Future<String>> futures = executor.invokeAll(tasks);

for (Future<String> future : futures) {
    try {
        String result = future.get(5, TimeUnit.SECONDS);
    } catch (ExecutionException e) {
        log.error("Task failed", e.getCause());
    } catch (TimeoutException e) {
        future.cancel(true);
        log.warn("Task timed out");
    }
}
```

### invokeAll() and invokeAny()

```java
// invokeAll() — waits for all, exceptions wrapped in ExecutionException
List<Future<String>> results = executor.invokeAll(taskList);

// invokeAny() — returns first successful, others cancelled
String result = executor.invokeAny(taskList);
```

---

## CompletableFuture Exception Handling

CompletableFuture provides functional-style exception handling for asynchronous pipelines.

### exceptionally()

Handles exceptions and provides a fallback value:

```java
CompletableFuture.supplyAsync(() -> {
    if (random.nextBoolean()) throw new RuntimeException("Random failure");
    return "Success";
})
.exceptionally(ex -> {
    log.error("Failed, providing fallback", ex);
    return "Fallback value";
});
```

### handle()

Handles both success and failure:

```java
CompletableFuture.supplyAsync(() -> riskyOperation())
.handle((result, ex) -> {
    if (ex != null) {
        return "Error: " + ex.getMessage();
    }
    return "Result: " + result;
});
```

### whenComplete()

Performs action on completion without changing the result:

```java
CompletableFuture.supplyAsync(() -> riskyOperation())
.whenComplete((result, ex) -> {
    if (ex != null) {
        log.error("Operation failed", ex);
    } else {
        log.info("Operation succeeded: {}", result);
    }
});
```

### Exception Propagation in Chains

Exceptions propagate through the chain until handled:

```java
CompletableFuture.supplyAsync(() -> {
    throw new RuntimeException("Step 1 failed");
})
.thenApply(result -> result + " Step 2")  // Skipped
.thenApply(result -> result + " Step 3")  // Skipped
.exceptionally(ex -> {
    System.out.println("Caught: " + ex.getMessage()); // Caught here
    return "Recovered";
});
```

### CompletableFuture.allOf() Exceptions

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "A");
CompletableFuture<String> future2 = CompletableFuture.failedFuture(new RuntimeException("B"));
CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "C");

CompletableFuture.allOf(future1, future2, future3)
    .exceptionally(ex -> {
        log.error("One of the futures failed", ex);
        return null;
    });
```

---
**Continue:** [Part 2](README-Part2.md)
