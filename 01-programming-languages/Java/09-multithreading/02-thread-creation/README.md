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

---

[📖 Continue to Part 2](README-part2.md)
