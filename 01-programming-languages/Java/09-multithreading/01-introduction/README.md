# Introduction to Multithreading

## 1. Introduction

Multithreading is one of the most powerful and complex features in Java. It allows a program to perform multiple tasks concurrently, improving responsiveness, throughput, and resource utilization. At its core, multithreading is about executing multiple threads of control within a single process, sharing the same memory space but maintaining their own execution stacks.

Java was designed from the ground up with multithreading support. The `java.lang.Thread` class and `java.lang.Runnable` interface have been part of Java since version 1.0. Over the years, Java has evolved its concurrency utilities through the `java.util.concurrent` package (Java 5+), virtual threads (Java 21+), and various optimizations in the JVM.

Understanding multithreading is essential for modern Java development. Whether you're building web applications, microservices, data processing pipelines, or desktop applications, concurrency plays a critical role. However, multithreading introduces complexity—race conditions, deadlocks, visibility issues, and thread-safety problems—that can lead to subtle, hard-to-reproduce bugs.

This module provides a comprehensive understanding of Java multithreading, from fundamental concepts to advanced patterns used in enterprise applications.

## 2. Learning Objectives

- Understand what threads are and how they differ from processes
- Learn why multithreading is necessary in modern applications
- Understand the relationship between threads and the JVM
- Learn the different states a thread can be in
- Understand the overhead and trade-offs of multithreading
- Know when to use multithreading and when to avoid it
- Understand the difference between concurrency and parallelism
- Learn the historical evolution of Java's threading model

## 3. Prerequisites

- Module 01: Java Fundamentals (variables, control flow, methods)
- Module 02: Object-Oriented Programming (interfaces, inheritance, polymorphism)
- Basic understanding of how programs execute on a CPU
- Familiarity with the concept of program memory (stack vs heap)

## 4. Why This Concept Exists

Before multithreading, programs executed instructions sequentially—one instruction at a time. This was simple but inefficient:

1. **Idle CPU**: When a program waits for I/O (disk, network, user input), the CPU sits idle.
2. **Poor responsiveness**: GUI applications freeze during long operations.
3. **Underutilization**: Modern CPUs have multiple cores, but sequential programs use only one.
4. **Complex asynchronous code**: Without threads, handling concurrent operations requires complex callback structures.

Multithreading solves these problems by allowing a program to:

- **Overlap I/O with computation**: While one thread waits for data, another can process results.
- **Improve responsiveness**: Background threads handle long operations while the UI thread stays responsive.
- **Utilize multiple cores**: Threads can run in parallel on different CPU cores.
- **Simplify program structure**: Each thread handles one logical task, making code easier to reason about.

However, multithreading is not free. It introduces:
- **Context switching overhead**: The OS must save and restore thread state.
- **Synchronization complexity**: Shared data requires protection from concurrent access.
- **Memory overhead**: Each thread needs its own stack (typically 512KB-1MB).
- **Debugging difficulty**: Race conditions and deadlocks are hard to reproduce.

## 5. Problem Statement

Consider a web server handling HTTP requests. A single-threaded server must process each request completely before handling the next:

```
Request 1: [===processing===] [===response===] → Request 2: [===processing===] [===response===] → ...
```

If Request 1 takes 100ms, Request 2 must wait at least 100ms before it starts. With 100 concurrent users, the last request waits nearly 10 seconds.

A multithreaded server handles each request in a separate thread:

```
Thread 1: [===Request 1===]
Thread 2: [===Request 2===]     (running simultaneously)
Thread 3: [===Request 3===]
...
```

Now all requests are processed concurrently, and the total time is determined by the slowest single request, not the sum of all requests.

## 6. Theory

### Concurrency vs Parallelism

These terms are often used interchangeably but have distinct meanings:

- **Concurrency**: Multiple tasks making progress during overlapping time periods. They may or may not run simultaneously. On a single-core CPU, threads are time-sliced—they take turns executing.
- **Parallelism**: Multiple tasks literally executing at the same instant. Requires multiple CPU cores.

```
Single-core (Concurrency only):
Thread A: [==A==]    [==A==]    [==A==]
Thread B:     [==B==]    [==B==]    [==B==]
                      ↑ time-slicing

Multi-core (Concurrency + Parallelism):
Core 1, Thread A: [==A==][==A==][==A==]
Core 2, Thread B: [==B==][==B==][==B==]
                      ↑ true simultaneous execution
```

### Thread vs Process

| Aspect | Thread | Process |
|--------|--------|---------|
| Memory | Shares heap with other threads in same process | Isolated memory space |
| Creation cost | Lightweight (~1MB stack) | Heavy (~10MB+ memory) |
| Communication | Direct (shared memory) | IPC required (pipes, sockets) |
| Isolation | No isolation (can corrupt shared data) | Full isolation |
| Context switch | Fast (~1-10 microseconds) | Slow (~10-100 microseconds) |

### The Java Threading Model

Java threads are mapped to native OS threads (called "green threads" in early Java, but modern Java uses native threads). Each Java thread corresponds to an OS thread, which is scheduled by the OS kernel.

```
Java Application
├── Main Thread (Thread.currentThread())
├── Thread-1 (user-created)
├── Thread-2 (user-created)
├── Finalizer Thread (JVM internal)
├── Reference Handler Thread (JVM internal)
├── Signal Dispatcher Thread (JVM internal)
└── [GC threads] (JVM internal)
```

### Thread Types in Java

1. **User Threads**: Created by the application. The JVM waits for all user threads to complete before terminating.
2. **Daemon Threads**: Background threads (e.g., GC, finalizer). The JVM does not wait for daemon threads to complete before terminating.

## 7. Internal Working

### How a Thread Executes

When a thread executes, the JVM:

1. **Allocates a stack** for the thread (default size varies by OS: 512KB-1MB).
2. **Creates a Program Counter (PC) register** pointing to the current bytecode instruction.
3. **Executes bytecode** from the PC register, manipulating the stack and local variables.
4. **Context switches** when the OS scheduler decides to run another thread.

### The JVM's Role

The JVM manages threads through:

- **Thread Scheduler**: Decides which thread to run (OS-level).
- **Memory Model**: Defines how threads see each other's writes (Java Memory Model).
- **Garbage Collector**: Runs concurrently with application threads.

### Thread Creation Internals

When you call `new Thread(runnable)`:

1. A `Thread` object is allocated on the heap.
2. The object stores a reference to the `Runnable`.
3. When `start()` is called, the JVM calls the native `start0()` method.
4. The native method creates an OS-level thread (via `pthread_create` on Linux, `CreateThread` on Windows).
5. The new OS thread begins executing the `run()` method.

## 8. JVM Perspective

### Thread-Local Storage

Each thread has its own:
- **Stack**: Local variables, method call frames.
- **Program Counter**: Current bytecode position.
- **Thread-Local Variables**: Accessed via `ThreadLocal<T>`.

### Memory Visibility

Without proper synchronization, threads may see stale data:

```
Thread 1:              Thread 2:
x = 10;                while (running) {
// May not see       print(x);
// Thread 1's write    }
// immediately!
```

The Java Memory Model (JMM) guarantees visibility only when happens-before relationships are established (via `synchronized`, `volatile`, etc.).

### Thread State in the JVM

The JVM tracks thread states internally:

```
NEW        → Thread object created, start() not called
RUNNABLE   → Thread is executing or ready to execute
BLOCKED    → Waiting to acquire a monitor lock
WAITING    → Waiting indefinitely for another thread
TIMED_WAITING → Waiting for a specified time
TERMINATED → Thread has completed execution
```

### Stack Frame Structure

Each thread's stack contains stack frames:

```
Stack Frame (for each method call):
┌─────────────────────────┐
│ Local variables         │
│ Operand stack           │
│ Frame data (constants,  │
│   return address, etc.) │
└─────────────────────────┘
```

## 9. Memory Representation

### Thread Memory Layout

```
JVM Process Memory:
┌────────────────────────────────────────────────────┐
│ Heap (shared among all threads)                    │
│ ┌──────────────────────────────────────────────┐   │
│ │ Objects, Arrays, Class data                  │   │
│ └──────────────────────────────────────────────┘   │
│                                                    │
│ Thread 1 Stack (1MB)     Thread 2 Stack (1MB)     │
│ ┌──────────────────┐     ┌──────────────────┐     │
│ │ Stack Frame 3    │     │ Stack Frame 2    │     │
│ │ Stack Frame 2    │     │ Stack Frame 1    │     │
│ │ Stack Frame 1    │     │ main()           │     │
│ └──────────────────┘     └──────────────────┘     │
│                                                    │
│ Method Area (shared)    Native Memory             │
│ ┌──────────────────┐     ┌──────────────────┐     │
│ │ Class metadata   │     │ Thread control   │     │
│ │ Constant pool    │     │ blocks (TCBs)    │     │
│ └──────────────────┘     └──────────────────┘     │
└────────────────────────────────────────────────────┘
```

### Object Header and Thread Safety

Every Java object has an object header containing:
- **Mark word** (64 bits): Contains hashCode, GC age, lock state.
- **Class pointer** (64 bits): Points to class metadata.

When a thread acquires a lock (via `synchronized`), the mark word is modified to point to a lock record or monitor object.

## 10. Syntax

```java
// ============================================
// THREAD CREATION
// ============================================

// Method 1: Extend Thread class
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running: " + getName());
    }
}
Thread t1 = new MyThread();
t1.start();

// Method 2: Implement Runnable (preferred)
Thread t2 = new Thread(() -> {
    System.out.println("Runnable thread running");
});
t2.start();

// Method 3: Implement Callable (returns result)
Callable<Integer> callable = () -> {
    return 42;
};

// ============================================
// THREAD LIFECYCLE
// ============================================
Thread t = new Thread(() -> { /* work */ });
// State: NEW

t.start();
// State: RUNNABLE

t.join();       // Wait for completion
t.sleep(1000);  // Sleep for 1 second
t.interrupt();  // Interrupt the thread

// ============================================
// THREAD PROPERTIES
// ============================================
t.getName();         // Thread name
t.getPriority();     // Thread priority (1-10)
t.isAlive();         // Is thread started and not terminated
t.isDaemon();        // Is this a daemon thread
t.getState();        // Thread state enum
t.getThreadGroup();  // Thread group

// ============================================
// COMMON OPERATIONS
// ============================================
Thread.sleep(long millis);           // Current thread sleeps
Thread.yield();                      // Hint to scheduler
Thread.currentThread();              // Get current thread
Thread.interrupted();                // Check and clear interrupt
t.setPriority(Thread.MAX_PRIORITY);  // Set priority
t.setDaemon(true);                   // Set as daemon (before start)
t.join(5000);                        // Wait up to 5 seconds
```

## 11. Easy Example

```java
public class ThreadBasics {
    public static void main(String[] args) {
        // Basic thread creation and execution
        System.out.println("Main thread started: " + Thread.currentThread().getName());

        // Create a thread using lambda (Runnable)
        Thread workerThread = new Thread(() -> {
            System.out.println("Worker thread started: " + Thread.currentThread().getName());
            for (int i = 1; i <= 5; i++) {
                System.out.println("Worker step " + i);
                try {
                    Thread.sleep(500); // Simulate work
                } catch (InterruptedException e) {
                    System.out.println("Worker interrupted!");
                    return;
                }
            }
            System.out.println("Worker thread finished");
        });

        // Create a daemon thread (background cleanup)
        Thread daemonThread = new Thread(() -> {
            while (true) {
                System.out.println("Daemon: doing cleanup...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
        daemonThread.setDaemon(true); // Must be set before start()

        // Start threads
        workerThread.start();
        daemonThread.start();

        // Wait for worker to complete
        try {
            workerThread.join();
            System.out.println("Worker thread joined. Main thread continuing.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main thread finished");
    }
}
```

## 12. Medium Example

```java
import java.util.ArrayList;
import java.util.List;

public class ThreadCommunication {
    private static final List<Integer> sharedList = new ArrayList<>();
    private static final Object lock = new Object();
    private static boolean dataReady = false;

    public static void main(String[] args) {
        // Producer-Consumer pattern
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                synchronized (lock) {
                    sharedList.add(i);
                    System.out.println("Produced: " + i);
                    dataReady = true;
                    lock.notify();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        Thread consumer = new Thread(() -> {
            int count = 0;
            while (count < 10) {
                synchronized (lock) {
                    while (!dataReady) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    if (!sharedList.isEmpty()) {
                        int value = sharedList.remove(0);
                        System.out.println("Consumed: " + value);
                        count++;
                    }
                    dataReady = false;
                }
            }
        });

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Production-Consumption complete");
    }
}
```

## 13. Hard Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class AdvancedThreading {
    private static final int NUM_THREADS = 10;
    private static final int INCREMENTS_PER_THREAD = 100000;

    // Race condition demo
    private static int unsafeCounter = 0;
    private static final AtomicInteger safeCounter = new AtomicInteger(0);
    private static final ReentrantLock lock = new ReentrantLock();
    private static int lockedCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Race Condition Demo ===");

        // Unsafe (race condition)
        unsafeCounter = 0;
        Thread[] unsafeThreads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter++; // Not atomic!
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) t.join();
        System.out.println("Unsafe counter (expected " +
            (NUM_THREADS * INCREMENTS_PER_THREAD) + "): " + safeCounter);

        // Safe with AtomicInteger
        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    safeCounter.incrementAndGet();
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) t.join();
        System.out.println("Atomic counter: " + safeCounter.get());

        // Safe with Lock
        for (int i = 0; i < NUM_THREADS; i++) {
            unsafeThreads[i] = new Thread(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    lock.lock();
                    try {
                        lockedCounter++;
                    } finally {
                        lock.unlock();
                    }
                }
            });
            unsafeThreads[i].start();
        }
        for (Thread t : unsafeThreads) t.join();
        System.out.println("Locked counter: " + lockedCounter);

        System.out.println("\n=== Thread Interruption Demo ===");
        Thread interruptible = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Working...");
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted, cleaning up...");
            }
            System.out.println("Thread finished gracefully");
        });

        interruptible.start();
        Thread.sleep(2000);
        interruptible.interrupt();
        interruptible.join();
    }
}
```

## 14. Enterprise Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class EnterpriseThreadPool {
    private final ThreadPoolExecutor executor;
    private final AtomicLong tasksSubmitted = new AtomicLong(0);
    private final AtomicLong tasksCompleted = new AtomicLong(0);

    public EnterpriseThreadPool() {
        this.executor = new ThreadPoolExecutor(
            4,                          // core pool size
            8,                          // max pool size
            60L, TimeUnit.SECONDS,      // keep alive time
            new LinkedBlockingQueue<>(1000), // work queue
            new ThreadFactory() {
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r, "enterprise-worker-" + count++);
                    t.setDaemon(false);
                    t.setPriority(Thread.NORM_PRIORITY);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // rejection policy
        );
    }

    public <T> CompletableFuture<T> submitTask(Callable<T> task) {
        tasksSubmitted.incrementAndGet();
        return CompletableFuture.supplyAsync(() -> {
            try {
                T result = task.call();
                tasksCompleted.incrementAndGet();
                return result;
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void printStats() {
        System.out.printf("Stats - Active: %d, Completed: %d, Queue: %d, Submitted: %d, Completed: %d%n",
            executor.getActiveCount(),
            executor.getCompletedTaskCount(),
            executor.getQueue().size(),
            tasksSubmitted.get(),
            tasksCompleted.get());
    }

    public static void main(String[] args) {
        EnterpriseThreadPool pool = new EnterpriseThreadPool();

        for (int i = 0; i < 20; i++) {
            final int taskId = i;
            pool.submitTask(() -> {
                Thread.sleep(1000);
                return "Task " + taskId + " completed on " + Thread.currentThread().getName();
            }).thenAccept(result -> System.out.println(result));
        }

        pool.printStats();
        pool.shutdown();
    }
}
```

## 15. Performance

### Thread Creation Overhead

| Operation | Time (approximate) |
|-----------|-------------------|
| Thread creation (Java) | 1-10 ms |
| Thread creation (virtual) | 1-10 μs |
| Thread context switch | 1-10 μs |
| Process creation | 10-100 ms |

### Memory Cost Per Thread

| Component | Size |
|-----------|------|
| Thread object | ~100 bytes |
| Stack (default) | 512KB-1MB |
| Native thread control block | ~1KB |
| Total per thread | ~1MB |

### Scalability Limits

- **Platform threads**: Limited by OS (typically 1,000-10,000 threads)
- **Virtual threads**: Limited by memory (millions possible)

### When Multithreading Helps

- CPU-bound tasks on multi-core systems
- I/O-bound tasks (network, disk, database)
- Long-running background tasks
- Responsiveness-critical applications

### When Multithreading Hurts

- Simple sequential tasks (overhead exceeds benefit)
- Shared-state-heavy applications (synchronization overhead)
- Very short tasks (thread creation cost dominates)
- Single-core systems for CPU-bound work

## 16. Best Practices

1. **Prefer Runnable/Callable over extending Thread**: Allows reuse of thread pools and separation of task from execution.
2. **Always handle InterruptedException**: Don't swallow interrupts—restore the interrupt flag or propagate the exception.
3. **Use meaningful thread names**: Helps debugging and profiling.
4. **Avoid creating threads manually**: Use ExecutorService instead of `new Thread()`.
5. **Set daemon flag before start()**: Calling `setDaemon(true)` after `start()` throws `IllegalThreadStateException`.
6. **Don't rely on thread priority**: Priorities are hints, not guarantees. They vary across OS platforms.
7. **Use Thread.interrupted() carefully**: It clears the interrupt status.
8. **Minimize shared state**: Use thread-local storage or immutable objects when possible.
9. **Always join threads you start**: Ensure threads complete before program exits.
10. **Use try-finally for thread cleanup**: Ensure resources are released even if exceptions occur.

## 17. Common Mistakes

```java
// Mistake 1: Starting a thread twice
Thread t = new Thread(() -> System.out.println("hello"));
t.start();
t.start(); // IllegalThreadStateException!

// Mistake 2: Calling run() instead of start()
t.run(); // Executes in the calling thread, NOT a new thread!

// Mistake 3: Swallowing InterruptedException
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    // BAD: Interrupt status is cleared
}

// Correct:
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // Restore interrupt status
}

// Mistake 4: Not joining started threads
Thread worker = new Thread(() -> doWork());
worker.start();
// worker not joined - main may exit before worker completes

// Mistake 5: Creating threads in tight loops
for (int i = 0; i < 10000; i++) {
    new Thread(() -> process(i)).start(); // Creates 10,000 threads!
}
// Use ExecutorService instead
```

## 18. Pitfalls

### Race Conditions
When two threads access shared mutable data without synchronization, the result depends on the timing of execution.

### Deadlocks
Two threads waiting for each other's locks create a permanent block:
```
Thread 1: holds Lock A, waiting for Lock B
Thread 2: holds Lock B, waiting for Lock A
→ Both blocked forever
```

### Starvation
A thread never gets CPU time because higher-priority threads keep running.

### Livelock
Threads keep responding to each other without making progress (like two people stepping aside for each other).

### Heisenbugs
Bugs that disappear when you try to debug them (because debugging changes timing).

## 19. Debugging Tips

1. **Use `Thread.currentThread().getName()`**: Identify which thread is executing.
2. **Use `jstack`**: Dump thread state for analysis.
3. **Use VisualVM or JConsole**: Monitor thread count, state, and contention.
4. **Add logging with thread names**: `log.info("[{}] Doing something", Thread.currentThread().getName())`
5. **Use `Thread.getState()`**: Check if threads are in expected states.
6. **Enable thread safety checks**: Use tools like FindBugs/SpotBugs, Error Prone.
7. **Reproduce with deterministic timing**: Use `CountDownLatch` or `CyclicBarrier` to force ordering.
8. **Check for deadlock**: Use `ThreadMXBean.findDeadlockedThreads()`.
9. **Profile thread contention**: Use JMH or async-profiler.
10. **Use thread dumps**: `jstack <pid>` or `kill -3 <pid>` on Unix.

## 20. Comparison Table

| Feature | Thread | Runnable | Callable | Virtual Thread |
|---------|--------|----------|----------|----------------|
| Returns value | No | No | Yes (via Future) | Yes (via Future) |
| Exception handling | In run() | In run() | Checked exceptions | Checked exceptions |
| Extensibility | Extends Thread | No | No | No |
| Reusability | No | Yes (with pool) | Yes (with pool) | Yes |
| Memory cost | High (~1MB) | High (~1MB) | High (~1MB) | Low (~few KB) |
| Creation speed | Slow | Slow | Slow | Fast |
| Use case | Legacy code | General tasks | Result-bearing tasks | High-concurrency I/O |

## 21. Decision Tree

```
Need concurrent execution?
├── Yes → Need return value from task?
│   ├── Yes → Use Callable + Future/CompletableFuture
│   └── No → Use Runnable
│       ├── Short-lived task? → CompletableFuture.runAsync()
│       ├── Long-running task? → ExecutorService.submit()
│       └── High concurrency (I/O)? → Virtual Threads
└── No → Sequential execution is fine
```

## 22. Interview Questions

### Q1: What is the difference between a process and a thread?
**A**: A process is an independent program with its own memory space. A thread is a lightweight unit of execution within a process, sharing the heap memory with other threads in the same process. Threads have lower creation overhead and faster context switching than processes.

### Q2: Why is `Thread.start()` needed instead of calling `run()` directly?
**A**: `start()` creates a new OS-level thread and begins execution in that thread. Calling `run()` directly executes the code in the current thread, not in a new thread.

### Q3: What happens when you call `Thread.sleep()`?
**A**: The current thread moves to the TIMED_WAITING state for the specified duration. It does not release any monitors (locks). Other threads can execute while it sleeps.

### Q4: Can you start a thread twice?
**A**: No. After `start()` is called, the thread's run method executes and the thread terminates. Calling `start()` again throws `IllegalThreadStateException`.

### Q5: What is a daemon thread?
**A**: A daemon thread is a background thread that the JVM does not wait for before exiting. When all non-daemon threads complete, the JVM shuts down, killing any daemon threads. Common examples: GC, finalizer.

### Q6: Explain the Java Memory Model in simple terms.
**A**: The JMM defines how threads interact through memory. Without synchronization, threads may see stale values because of CPU caching and compiler optimizations. The JMM defines happens-before relationships (via volatile, synchronized, etc.) that guarantee visibility.

### Q7: What is thread starvation?
**A**: When a thread is perpetually denied access to resources (CPU time, locks) because other threads are always prioritized or holding the resource.

## 23. Exercises

### Exercise 1: Thread Basics
Create a program with 3 threads that each print numbers 1-10 with different delays. Use `join()` to wait for all to complete.

### Exercise 2: Thread Communication
Implement a producer-consumer system where:
- Producer generates numbers 1-20
- Consumer processes them
- Use `wait()`/`notify()` for synchronization
- Handle thread interruption gracefully

### Exercise 3: Thread Safety
Create a shared counter incremented by 10 threads, each incrementing 100,000 times. Verify the final count matches expected (1,000,000). Fix race conditions.

### Exercise 4: Thread Pool
Implement a simple task processor using `ExecutorService`:
- Submit 50 tasks
- Each task takes 1-3 seconds
- Monitor active threads, completed tasks, queue size
- Implement graceful shutdown

## 24. Assignments

### Assignment 1: Web Request Simulator
Build a multithreaded web request simulator:
- Simulate 100 HTTP requests with random response times (100ms-2000ms)
- Use a fixed thread pool of 10 threads
- Track response times, failures, and throughput
- Generate a summary report

### Assignment 2: Thread-Safe Data Structure
Implement a thread-safe bounded queue:
- Support `put()` and `take()` operations
- Handle full queue (block producer) and empty queue (block consumer)
- Use `wait()`/`notify()` or explicit locks
- Support timeout on operations

### Assignment 3: Parallel File Processor
Build a multithreaded file processor:
- Read multiple files in parallel
- Process each file (count words, lines, characters)
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
