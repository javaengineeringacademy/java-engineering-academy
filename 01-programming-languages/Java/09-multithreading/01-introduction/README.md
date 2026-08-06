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

---

[📖 Continue to Part 2](README-part2.md)
