# Thread Creation

## 1. Introduction

Thread creation is the foundation of multithreading in Java. Java provides multiple ways to create threads, each with distinct advantages and trade-offs. Understanding these approaches is essential for writing concurrent code that is correct, efficient, and maintainable.

The four primary ways to create threads in Java are:
1. **Extending the `Thread` class** – The original approach since Java 1.0
2. **Implementing the `Runnable` interface** – The preferred approach for most use cases
3. **Implementing the `Callable` interface** – When tasks need to return values
4. **Using `CompletableFuture`** – Modern async composition (Java 8+)

Additionally, Java 21 introduced **virtual threads**, which fundamentally change how threads are created by making them extremely lightweight.

Each method has implications for code design, testability, reusability, and performance. This topic explores all approaches in depth.

## 2. Learning Objectives

- Understand all four ways to create threads in Java
- Learn when to use each thread creation approach
- Understand the difference between `Runnable`, `Callable`, and `Thread`
- Learn how to use `ThreadFactory` for custom thread creation
- Understand virtual thread creation (Java 21+)
- Know the performance implications of each approach
- Learn common patterns for thread creation in enterprise applications

## 3. Prerequisites

- Module 08: Introduction to Multithreading
- Understanding of interfaces and lambda expressions
- Familiarity with anonymous inner classes
- Basic knowledge of exceptions and generics

## 4. Why This Concept Exists

Different scenarios require different threading approaches:

- **Simple background tasks**: Just need to run code asynchronously → `Runnable`
- **Tasks with results**: Need to compute and return a value → `Callable`
- **Custom thread behavior**: Need to customize thread name, priority, exception handler → `ThreadFactory`
- **High-concurrency I/O**: Need millions of concurrent operations → Virtual Threads
- **Legacy compatibility**: Existing code that extends Thread → Extend Thread

The evolution of thread creation in Java reflects the language's maturation:
- Java 1.0: `Thread` class and `Runnable` interface
- Java 5: `Callable`, `Future`, `ExecutorService`
- Java 8: `CompletableFuture`, lambda expressions
- Java 21: Virtual threads

## 5. Problem Statement

Consider building a file download manager. You need to:
- Download multiple files concurrently
- Track progress of each download
- Handle errors for individual downloads
- Report download results (success/failure, bytes downloaded)
- Scale to handle hundreds of concurrent downloads

Different thread creation approaches offer different solutions:

```java
// Approach 1: Runnable (no return value, fire-and-forget)
executor.submit(() -> downloadFile(url));

// Approach 2: Callable (returns download result)
Future<DownloadResult> future = executor.submit(() -> downloadFile(url));

// Approach 3: CompletableFuture (composable async pipeline)
CompletableFuture<DownloadResult> future = CompletableFuture
    .supplyAsync(() -> downloadFile(url))
    .thenApply(result -> processResult(result));

// Approach 4: Virtual Threads (massive concurrency)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> downloadFile(url));
}
```

## 6. Theory

### The Runnable Interface

`Runnable` is a functional interface with a single method `run()` that returns void:

```java
@FunctionalInterface
public interface Runnable {
    void run();
}
```

**Characteristics:**
- No return value
- Cannot throw checked exceptions
- Can be used as a lambda expression
- Stateless lambdas can be shared across threads

### The Callable Interface

`Callable<V>` is a functional interface with a single method `call()` that returns a value:

```java
@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

**Characteristics:**
- Returns a value of type V
- Can throw checked exceptions
- Results are accessed via `Future<V>`
- Used with `ExecutorService.submit()`

### Extending Thread

The `Thread` class implements `Runnable`:

```java
public class Thread implements Runnable {
    public void run() {
        // Default implementation does nothing
    }
}
```

**Characteristics:**
- Can override `start()`, `run()`, and other Thread methods
- Cannot extend other classes (single inheritance)
- Tightly couples task logic with threading mechanism
- Generally discouraged in modern Java

### ThreadFactory Pattern

`ThreadFactory` is an interface for creating threads on-demand:

```java
@FunctionalInterface
public interface ThreadFactory {
    Thread newThread(Runnable r);
}
```

Used by `ExecutorService` to create threads. Allows customization of thread names, daemon status, priority, and exception handlers.

## 7. Internal Working

### Thread Creation Process (Platform Threads)

When `Thread.start()` is called:

1. **JVM allocates thread object** on heap
2. **Native method `start0()`** is invoked
3. **OS creates native thread** (via `pthread_create` on Linux)
4. **Native thread stack is allocated** (default 1MB)
5. **Thread registers with JVM's thread list**
6. **OS schedules the thread** for execution
7. **`run()` method begins execution** in the new thread
8. **When `run()` completes**, thread transitions to TERMINATED state

### Thread Creation Process (Virtual Threads)

When `Thread.startVirtualThread(runnable)` is called:

1. **JVM creates virtual thread object** on heap (lightweight)
2. **Virtual thread is mounted on a carrier platform thread**
3. **No native OS thread is created**
4. **Execution begins immediately** on the carrier thread
5. **When blocked (I/O)**, virtual thread is unmounted and carrier thread is freed
6. **When ready**, virtual thread is remounted on any available carrier thread

### Runnable vs Callable Execution

```
Runnable execution:
Thread.start() → new OS thread → run() → void return

Callable execution:
ExecutorService.submit() → wraps in FutureTask → executes call()
                         → result stored in FutureTask
                         → Future.get() retrieves result
```

## 8. JVM Perspective

### Object Allocation

Each thread creation involves heap allocation:

```java
// Platform thread creation
Thread t = new Thread(runnable);  // ~100 bytes on heap
t.start();                        // ~1MB native stack allocation

// Virtual thread creation
Thread vt = Thread.ofVirtual().start(runnable);  // ~few hundred bytes on heap
                                                  // No native stack until mounted
```

### Thread-Local Storage (TLS)

Each thread maintains its own:
- **Stack**: Local variables, method frames
- **PC Register**: Current instruction pointer
- **Thread-local variables**: Accessed via `ThreadLocal<T>`
- **Interrupt status**: Boolean flag

### JIT Compilation

The JIT compiler optimizes thread creation:
- **Inlining**: Small `run()` methods may be inlined
- **Escape analysis**: Thread-local objects may be scalar-replaced
- **Lock elision**: If a thread doesn't escape, synchronization may be eliminated

### GC Impact

- Each thread's stack is scanned during GC
- More threads = more GC roots = longer GC pauses
- Virtual threads reduce this pressure by sharing carrier thread stacks

## 9. Memory Representation

### Platform Thread Object

```
Thread object (heap):
┌─────────────────────────────┐
│ Object header (12 bytes)    │
│ target (Runnable ref)       │
│ name (String ref)           │
│ priority (int)              │
│ daemon (boolean)            │
│ group (ThreadGroup ref)     │
│ contextClassLoader (ref)    │
│ inheritedAccessControlCtx   │
│ target (Runnable ref)       │
│ threadLocals (ref)          │
│ inheritedThreadLocals (ref) │
│ stackSize (long)            │
│ nativeParkBlocker (ref)     │
│ tid (long)                  │
│ stillborn (boolean)         │
└─────────────────────────────┘
         │
         ▼
Native thread control block (OS memory):
┌─────────────────────────────┐
│ Thread ID                   │
│ Stack base/limit            │
│ Register state              │
│ Signal mask                 │
│ Scheduling priority         │
└─────────────────────────────┘
         │
         ▼
Thread stack (native memory, ~1MB):
┌─────────────────────────────┐
│ Stack Frame N (current)     │
│ ...                         │
│ Stack Frame 2               │
│ Stack Frame 1 (main entry)  │
└─────────────────────────────┘
```

### Virtual Thread Object

```
Virtual thread object (heap):
┌─────────────────────────────┐
│ Object header               │
│ carrier thread (ref)        │──────┐ (null when unmounted)
│ name (String ref)           │      │
│ interrupt status (boolean)  │      ▼
│ continuation (ref)          │ Carrier platform thread:
└─────────────────────────────┘ ┌────────────────────┐
                                │ Platform thread    │
                                │ (shared among many │
                                │  virtual threads)  │
                                └────────────────────┘
```

## 10. Syntax

```java
// ============================================
// METHOD 1: EXTEND THREAD CLASS
// ============================================
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread: " + getName());
    }
}

MyThread t = new MyThread();
t.setName("my-thread");
t.setPriority(Thread.MAX_PRIORITY);
t.start();

// ============================================
// METHOD 2: IMPLEMENT RUNNABLE
// ============================================
Runnable task = () -> {
    System.out.println("Runnable: " + Thread.currentThread().getName());
};

Thread t = new Thread(task, "my-runnable-thread");
t.start();

// Or with ExecutorService
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(task);
executor.shutdown();

// ============================================
// METHOD 3: IMPLEMENT CALLABLE
// ============================================
Callable<Integer> callable = () -> {
    Thread.sleep(1000);
    return 42;
};

ExecutorService executor = Executors.newFixedThreadPool(4);
Future<Integer> future = executor.submit(callable);
Integer result = future.get(); // Blocks until result is available
executor.shutdown();

// ============================================
// METHOD 4: COMPLETABLE FUTURE
// ============================================
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    Thread.sleep(1000);
    return 42;
});

Integer result = future.join(); // Blocks until complete
Integer result = future.getNow(0); // Non-blocking with default

// ============================================
// METHOD 5: VIRTUAL THREADS (Java 21+)
// ============================================
// Method A: Direct creation
Thread vt = Thread.ofVirtual().name("vt-1").start(() -> {
    System.out.println("Virtual thread: " + Thread.currentThread().getName());
});

// Method B: Via executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> System.out.println("Virtual thread task"));
}

// Method C: Factory
ThreadFactory factory = Thread.ofVirtual().name("vt-", 0).factory();
Thread vt = factory.newThread(() -> System.out.println("From factory"));

// ============================================
// THREAD FACTORY CUSTOMIZATION
// ============================================
ThreadFactory factory = new ThreadFactory() {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "custom-thread-" + counter.incrementAndGet());
        t.setDaemon(true);
        t.setPriority(Thread.NORM_PRIORITY);
        t.setUncaughtExceptionHandler((thread, ex) -> {
            System.err.println("Thread " + thread.getName() + " threw: " + ex);
        });
        return t;
    }
};

ExecutorService executor = Executors.newFixedThreadPool(4, factory);
```

## 11. Easy Example

```java
public class ThreadCreationBasics {
    public static void main(String[] args) {
        System.out.println("Main thread: " + Thread.currentThread().getName());

        // Method 1: Extend Thread
        Thread extendedThread = new Thread() {
            @Override
            public void run() {
                System.out.println("Extended Thread: " + getName());
            }
        };
        extendedThread.setName("extended-thread-1");

        // Method 2: Implement Runnable (lambda)
        Runnable runnableTask = () -> {
            System.out.println("Runnable Thread: " + Thread.currentThread().getName());
        };
        Thread runnableThread = new Thread(runnableThread, "runnable-thread-1");

        // Method 3: Anonymous Runnable
        Thread anonymousThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous Thread: " + Thread.currentThread().getName());
            }
        }, "anonymous-thread-1");

        // Start all threads
        extendedThread.start();
        runnableThread.start();
        anonymousThread.start();

        // Wait for all to complete
        try {
            extendedThread.join();
            runnableThread.join();
            anonymousThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All threads completed");
    }
}
```

## 12. Medium Example

```java
import java.util.concurrent.*;

public class FileDownloader {
    private final ExecutorService executor;

    public FileDownloader(int threadCount) {
        this.executor = Executors.newFixedThreadPool(threadCount, r -> {
            Thread t = new Thread(r);
            t.setName("downloader-" + t.threadId());
            t.setDaemon(true);
            return t;
        });
    }

    public Future<DownloadResult> download(String url) {
        return executor.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("[" + threadName + "] Starting download: " + url);

            // Simulate download
            Thread.sleep(1000 + (int)(Math.random() * 2000));

            // Simulate random failure
            if (Math.random() < 0.1) {
                throw new RuntimeException("Download failed for: " + url);
            }

            long bytesDownloaded = (long)(Math.random() * 10_000_000);
            System.out.println("[" + threadName + "] Completed: " + url +
                " (" + bytesDownloaded + " bytes)");

            return new DownloadResult(url, bytesDownloaded, true);
        });
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        FileDownloader downloader = new FileDownloader(4);

        String[] urls = {
            "https://example.com/file1.zip",
            "https://example.com/file2.zip",
            "https://example.com/file3.zip",
            "https://example.com/file4.zip",
            "https://example.com/file5.zip"
        };

        for (String url : urls) {
            downloader.download(url)
                .thenAccept(result -> {
                    System.out.println("Result: " + result);
                })
                .exceptionally(ex -> {
                    System.err.println("Error: " + ex.getMessage());
                    return null;
                });
        }

        downloader.shutdown();
    }

    record DownloadResult(String url, long bytes, boolean success) {}
}
```

## 13. Hard Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvancedThreadCreation {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Custom Thread Factory ===");

        // Custom thread factory with monitoring
        MonitoredThreadFactory factory = new MonitoredThreadFactory("web-worker");
        ExecutorService executor = Executors.newFixedThreadPool(4, factory);

        // Submit tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " on " +
                    Thread.currentThread().getName());
                Thread.sleep(500);
            });
        }

        Thread.sleep(2000);
        factory.printStats();
        executor.shutdown();

        System.out.println("\n=== Virtual Thread Creation ===");

        // Virtual thread with custom configuration
        Thread virtualThread = Thread.ofVirtual()
            .name("vt-custom-", 0)
            .daemon(true)
            .uncaughtExceptionHandler((t, e) ->
                System.err.println("VT " + t.getName() + " failed: " + e))
            .start(() -> {
                System.out.println("Virtual: " + Thread.currentThread().getName());
                System.out.println("Is virtual: " + Thread.currentThread().isVirtual());
            });

        virtualThread.join();

        System.out.println("\n=== Scoped Values (Virtual Threads) ===");

        // ScopedValue (Java 21) - thread-local without ThreadLocal overhead
        var USER_ID = ScopedValue.newInstance(String.class);
        ScopedValue.runWhere(USER_ID, "user-123", () -> {
            System.out.println("User in scope: " + USER_ID.get());
            Thread.startVirtualThread(() -> {
                System.out.println("User in VT: " + USER_ID.get());
            });
        });
    }

    static class MonitoredThreadFactory implements ThreadFactory {
        private final String prefix;
        private final AtomicInteger count = new AtomicInteger(0);
        private final AtomicInteger active = new AtomicInteger(0);
        private final AtomicInteger totalCreated = new AtomicInteger(0);

        MonitoredThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            totalCreated.incrementAndGet();
            active.incrementAndGet();

            Thread t = new Thread(() -> {
                try {
                    r.run();
                } finally {
                    active.decrementAndGet();
                }
            }, prefix + "-" + count.incrementAndGet());

            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            t.setUncaughtExceptionHandler((thread, ex) -> {
                System.err.println("Uncaught in " + thread.getName() + ": " + ex);
            });

            return t;
        }

        void printStats() {
            System.out.printf("Factory '%s' - Created: %d, Active: %d%n",
                prefix, totalCreated.get(), active.get());
        }
    }
}
```

## 14. Enterprise Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class EnterpriseThreadManagement {

    // Thread pool registry for monitoring
    private static final ConcurrentMap<String, ThreadPoolExecutor> POOLS =
        new ConcurrentHashMap<>();

    public static ThreadPoolConfig createPool(String name, int core, int max) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            core, max, 60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            createThreadFactory(name),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );

        executor.allowCoreThreadTimeOut(true);
        POOLS.put(name, executor);

        return new ThreadPoolConfig(executor, name);
    }

    private static ThreadFactory createThreadFactory(String poolName) {
        return r -> {
            Thread t = new Thread(r);
            t.setName(poolName + "-worker-" + t.threadId());
            t.setDaemon(false);
            t.setPriority(Thread.NORM_PRIORITY);
            return t;
        };
    }

    public static void printAllPoolStats() {
        POOLS.forEach((name, pool) -> {
            System.out.printf("Pool '%s': active=%d, completed=%d, queue=%d%n",
                name, pool.getActiveCount(), pool.getCompletedTaskCount(),
                pool.getQueue().size());
        });
    }

    public static void shutdownAll() {
        POOLS.values().forEach(pool -> {
            pool.shutdown();
            try {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                pool.shutdownNow();
            }
        });
    }

    record ThreadPoolConfig(ThreadPoolExecutor executor, String name) {
        public <T> Future<T> submit(Callable<T> task) {
            return executor.submit(task);
        }
    }

    public static void main(String[] args) throws Exception {
        // Create specialized pools
        ThreadPoolConfig ioPool = createPool("io", 8, 16);
        ThreadPoolConfig cpuPool = createPool("cpu", 4, 4);
        ThreadPoolConfig analyticsPool = createPool("analytics", 2, 4);

        // Submit tasks to appropriate pools
        ioPool.submit(() -> {
            System.out.println("IO task on " + Thread.currentThread().getName());
            Thread.sleep(1000);
            return "IO done";
        });

        cpuPool.submit(() -> {
            System.out.println("CPU task on " + Thread.currentThread().getName());
            return 42;
        });

        Thread.sleep(3000);
        printAllPoolStats();
        shutdownAll();
    }
}
```

## 15. Performance

### Thread Creation Benchmarks

| Method | Creation Time | Memory | Scalability |
|--------|--------------|--------|-------------|
| `new Thread().start()` | 1-10 ms | ~1 MB | ~1,000 threads |
| `Executors.newFixedThreadPool()` | Reuses threads | Shared | ~10,000 tasks |
| `Thread.ofVirtual().start()` | 1-10 μs | ~few KB | ~1,000,000 threads |
| `CompletableFuture.supplyAsync()` | Variable | Depends on pool | Depends on pool |

### Throughput vs Thread Count

```
Platform threads:
Throughput
    │
    │      ┌──── optimal zone
    │      │
    │   ───┼─────────────────────
    │      │         ↘
    │      │           ↘ (overhead dominates)
    └──────┴────────────────────── Threads
           100   1000   10000

Virtual threads:
Throughput
    │
    │              ┌──── optimal zone
    │             ╱
    │           ╱
    │         ╱
    │       ╱
    │     ╱
    └────┴─────────────────────── Threads
        100  1000  10000  100000
```

### Best Practices Summary

| Scenario | Recommended Approach |
|----------|---------------------|
| Simple background task | `Runnable` + `ExecutorService` |
| Task returning result | `Callable` + `Future` |
| Async composition | `CompletableFuture` |
| High-concurrency I/O | Virtual Threads |
| Custom thread config | `ThreadFactory` |
| Legacy compatibility | Extend `Thread` (avoid if possible) |

## 16. Best Practices

1. **Prefer Runnable over extending Thread**: Enables thread pool reuse and better testability.
2. **Use Callable when you need results**: Avoid shared mutable state for returning values.
3. **Always use meaningful thread names**: Critical for debugging and monitoring.
4. **Set uncaught exception handlers**: Prevent silent thread deaths.
5. **Reuse threads via pools**: Thread creation is expensive—pool and reuse.
6. **Prefer virtual threads for I/O**: Java 21 virtual threads are ideal for blocking I/O.
7. **Don't create threads in constructors**: The `this` reference may escape before construction completes.
8. **Set daemon flag before start()**: `setDaemon(true)` after `start()` throws exception.
9. **Use ThreadLocal sparingly**: Memory leaks if not cleaned up properly.
10. **Prefer CompletableFuture for composition**: Easier to chain async operations.

## 17. Common Mistakes

```java
// Mistake 1: Creating threads in tight loops
for (int i = 0; i < 10000; i++) {
    new Thread(() -> process(i)).start(); // Don't!
}
// Fix: Use ExecutorService

// Mistake 2: Starting thread before construction is complete
class BadThread extends Thread {
    private final int value;
    BadThread(int value) {
        this.value = value;
        this.start(); // 'this' escapes!
    }
}

// Mistake 3: Forgetting to set daemon for background threads
Thread bg = new Thread(() -> cleanup());
bg.start(); // JVM won't exit until this completes
// Fix: bg.setDaemon(true); before start()

// Mistake 4: Using Thread.stop() (deprecated, unsafe)
thread.stop(); // Never do this!

// Mistake 5: Not handling uncaught exceptions
Thread t = new Thread(() -> {
    throw new RuntimeException("oops"); // Thread dies silently
});
// Fix: Set UncaughtExceptionHandler
```

## 18. Pitfalls

### The `this` Escape Problem
If you start a thread in a constructor and pass `this`, the object may be seen by the new thread before construction completes.

```java
class EscapingThis {
    final int value;
    EscapingThis(int value) {
        this.value = value;
        new Thread(() -> System.out.println(this.value)).start(); // 'this' escapes
    }
}
```

### Thread Naming Conflicts
Using generic names like "Thread-0" makes debugging difficult. Always use descriptive, unique names.

### Forgetting to Join
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
