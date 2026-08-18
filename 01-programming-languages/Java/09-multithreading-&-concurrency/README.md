# Module 09: Multithreading and Concurrency

> **Difficulty:** Advanced
> **Reading:** 60 min | **Practice:** 90 min | **Total:** 150 min

## Overview

Single-threaded applications cannot use multiple CPUs, block on I/O, and offer poor responsiveness. Java's multithreading capabilities let you execute tasks concurrently, keep applications responsive under load, and utilize all available hardware. This module covers thread fundamentals, synchronization, executor services, concurrent collections, atomic variables, the Java Memory Model, and virtual threads.

## Learning Objectives

- [ ] Explain the difference between concurrency and parallelism
- [ ] Create threads using `Thread`, `Runnable`, `Callable`, and virtual threads
- [ ] Describe the six thread states and their transitions
- [ ] Implement `wait()`/`notify()` coordination correctly (with while loop)
- [ ] Prevent race conditions using `synchronized`, `volatile`, and atomic classes
- [ ] Diagnose and prevent deadlocks using lock ordering and tryLock
- [ ] Configure thread pools with appropriate sizing for different workloads
- [ ] Compose asynchronous operations with `CompletableFuture`
- [ ] Choose the right concurrent collection for your use case
- [ ] Explain the happens-before rules and their practical implications
- [ ] Identify and avoid `synchronized` pinning with virtual threads
- [ ] Clean up `ThreadLocal` values to prevent memory leaks in thread pools

## Prerequisites

- **Java Fundamentals**: Classes, interfaces, lambdas, exception handling
- **OOP Concepts**: Inheritance, polymorphism, encapsulation
- **Basic JVM Knowledge**: Heap vs stack, garbage collection, class loading
- **Collections Framework**: List, Map, Queue, Set

## Module Structure

| Topic | Description |
|-------|-------------|
| [00-introduction](00-introduction/) | Why multithreading exists, history, core concepts |
| [01-thread-basics](01-thread-basics/) | Thread class, Runnable, daemon threads, thread groups |
| [02-thread-creation](02-thread-creation/) | Ways to create threads, Runnable vs Thread, lambda |
| [03-thread-lifecycle](03-thread-lifecycle/) | Thread states, state transitions, interrupt handling |
| [04-thread-communication](04-thread-communication/) | wait/notify, join, interrupted, Thread.yield |
| [05-synchronization](05-synchronization/) | synchronized, volatile, race conditions, monitors |
| [06-locks](06-locks/) | ReentrantLock, ReadWriteLock, StampedLock, Conditions |
| [07-java-concurrency-framework](07-java-concurrency-framework/) | ExecutorService, thread pools, CompletableFuture, ForkJoin |
| [08-concurrent-collections](08-concurrent-collections/) | ConcurrentHashMap, BlockingQueue, CopyOnWriteArrayList |
| [09-atomic-classes](09-atomic-classes/) | AtomicInteger, AtomicReference, CAS operations |
| [10-thread-local](10-thread-local/) | ThreadLocal, InheritableThreadLocal, cleanup |
| [11-java-memory-model](11-java-memory-model/) | Happens-before, memory visibility, volatile semantics |
| [12-virtual-threads](12-virtual-threads/) | Project Loom, virtual threads, structured concurrency |

## History / Timeline

| Version | Year | Key Concurrency Additions |
|---------|------|---------------------------|
| Java 1.0 | 1996 | `Thread` class, `Runnable` interface |
| Java 1.2 | 1998 | `ThreadGroup` improvements, `Thread.yield()` |
| Java 1.5 | 2004 | **Major**: `ExecutorService`, `Lock`, `Semaphore`, `CountDownLatch`, `CyclicBarrier`, `ConcurrentHashMap`, `BlockingQueue`, `java.util.concurrent.atomic` package |
| Java 1.6 | 2006 | `Phaser`, `StampedLock` (preview) |
| Java 7 | 2011 | `ForkJoinPool`, `Phaser` (standard), `TransferQueue` |
| Java 8 | 2014 | `CompletableFuture`, `parallelStream()`, `ConcurrentHashMap` redesign (bin-level locking) |
| Java 9 | 2017 | `Flow` API (reactive streams), `CompletionStage` improvements |
| Java 10 | 2018 | Application CDS, local variable type inference |
| Java 11 | 2018 | `String` methods for concurrent string building |
| Java 19 | 2022 | Virtual threads (preview), structured concurrency (preview), scoped values (preview) |
| Java 20 | 2023 | Virtual threads (second preview) |
| Java 21 | 2023 | **Virtual threads (standard)**, structured concurrency (second preview), scoped values (second preview) |
| Java 23 | 2024 | Structured concurrency (third preview), scoped values (third preview) |

## Production Notes

### Thread Pool Sizing

- **CPU-bound tasks**: Pool size = number of CPU cores (`Runtime.getRuntime().availableProcessors()`)
- **I/O-bound tasks**: Pool size = (wait time / service time) × number of cores
- **Mixed workloads**: Use separate pools for CPU-bound and I/O-bound work
- Never use `Executors.newCachedThreadPool()` in production — unbounded thread creation causes OOM

### Monitoring

- `ThreadPoolExecutor.getQueue().size()`: Queue depth indicates if the pool is keeping up
- `ThreadPoolExecutor.getActiveCount()`: Number of threads currently executing tasks
- `ThreadPoolExecutor.getCompletedTaskCount()`: Total completed tasks (for throughput calculation)
- `ForkJoinPool.getStealCount()`: Work-stealing activity indicates load imbalance

### Common Pitfalls in Production

- **Thread pool sizing**: Too few threads → high latency; too many → excessive context switching
- **Task rejection**: Always configure a rejection policy (default `AbortPolicy` throws)
- **Thread leaks**: Always set daemon flag or shut down pools in finally blocks
- **Memory leaks**: Clean up `ThreadLocal` values in thread pools

## Common Mistakes

| Mistake | Why It's Wrong | Fix |
|---------|---------------|-----|
| Using `Thread.stop()` | Deprecated, unsafe — leaves monitors unlocked | Use interrupt + cancellation flag |
| `synchronized` on `this` for volatile fields | Redundant — volatile already provides visibility | Use volatile alone for simple flags |
| Forgetting `unlock()` in finally | Lock is never released → permanent deadlock | Always unlock in finally block |
| Using `wait()` without `while` loop | Spurious wakeup causes incorrect behavior | Always check condition in while loop |
| `Thread.sleep()` while holding a lock | Blocks other threads unnecessarily | Release lock before sleeping |
| Creating new threads per request | Thread creation overhead, OOM under load | Use thread pools or virtual threads |
| `notify()` when multiple threads wait | Wakes wrong thread, missed notifications | Use `notifyAll()` or `Condition` |
| Not cleaning ThreadLocal in pools | Memory leak — old values persist | Always call `remove()` after use |

## Interview Questions

### Fundamentals

1. **What is the difference between a process and a thread?**
   Threads share the same address space within a process. Processes have separate memory spaces. Threads are lighter and faster to create.

2. **What is the difference between `Runnable` and `Callable`?**
   `Runnable.run()` returns void and cannot throw checked exceptions. `Callable.call()` returns a value and can throw checked exceptions.

3. **What are the six thread states in Java?**
   NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED.

### Synchronization

4. **What is the difference between `synchronized` and `volatile`?**
   `synchronized` provides mutual exclusion AND visibility. `volatile` provides visibility only (no mutual exclusion). `volatile` is for flags; `synchronized` for compound operations.

5. **What causes a deadlock and how do you prevent it?**
   Deadlock is a circular lock dependency. Prevent by: lock ordering, tryLock with timeout, avoid holding multiple locks, deadlock detection tools.

6. **What is the difference between `ReentrantLock` and `synchronized`?**
   `ReentrantLock` supports timed wait, interruptible lock, fairness policy, and multiple conditions. `synchronized` is simpler and faster for basic use.

### Memory Model

7. **What does the happens-before relationship guarantee?**
   Writes by one thread are visible to reads by another thread. Without it, threads may see stale values from CPU caches.

8. **Why is `volatile` needed for 64-bit variables?**
   The JVM may split 64-bit reads/writes into two 32-bit operations, causing word tearing. `volatile` ensures atomicity.

### Concurrency Framework

9. **When should you use `CompletableFuture` vs `Future.get()`?**
   Use `CompletableFuture` for composing multiple async operations (chaining, combining). Use `Future.get()` for simple single-task waiting.

10. **What is work-stealing in ForkJoinPool?**
    Idle threads steal tasks from busy threads' deques. This provides load balancing without centralized scheduling.

### Virtual Threads

11. **What is thread pinning and why is it bad for virtual threads?**
    Pinning occurs when a virtual thread blocks in a `synchronized` block or native method. The carrier thread cannot be reused, defeating the purpose of virtual threads. Use `ReentrantLock` instead.

12. **How many virtual threads can you create?**
    Millions, since they use ~1KB heap-allocated stacks vs ~1MB for platform threads.

## Cross-References

- **Previous Module:** [08 - I/O and NIO](../08-io-nio/)
- **Next Module:** [10 - JVM Internals](../10-jvm-internals/)
- **Related:** [04 - Collections](../04-collections/) — thread-safe collections
- **Related:** [07 - Functional Programming](../07-functional-programming/) — CompletableFuture and Stream API
