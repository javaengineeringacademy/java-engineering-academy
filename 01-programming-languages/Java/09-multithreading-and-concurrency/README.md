# Module 09: Multithreading and Concurrency

> **Difficulty:** Advanced
> **Reading:** 60 min | **Practice:** 90 min | **Total:** 150 min

## Overview

Single-threaded applications cannot use multiple CPUs, block on I/O, and offer poor responsiveness. Java's multithreading capabilities let you execute tasks concurrently, keep applications responsive under load, and utilize all available hardware. This module covers thread fundamentals, synchronization, executor services, concurrent collections, atomic variables, the Java Memory Model, and virtual threads.

## Learning Objectives

- Create and manage threads using Thread, Runnable, and virtual threads
- Prevent race conditions using synchronized blocks, locks, and atomic variables
- Configure thread pools with ExecutorService for scalable task execution
- Use CompletableFuture to compose asynchronous operations
- Identify and diagnose deadlocks, starvation, and other concurrency bugs
- Understand the Java Memory Model and happens-before relationships

## Prerequisites

- OOP concepts (interfaces, inheritance, polymorphism)
- Exception handling
- Basic JVM knowledge

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

## History

| Version | Change |
|---------|--------|
| Java 1.0 | `Thread` class and `Runnable` interface |
| Java 5 | `ExecutorService`, `Lock`, `Semaphore`, `ConcurrentHashMap`, `java.util.concurrent.atomic` |
| Java 7 | `ForkJoinPool`, `Phaser` |
| Java 8 | `CompletableFuture`, `parallelStream()` |
| Java 9 | `Flow` API (reactive streams) |
| Java 19 | Virtual threads (preview) |
| Java 21 | Virtual threads (standard) |

## Cross-References

- **Previous Module:** [08 - I/O and NIO](../08-io-nio/)
- **Next Module:** [10 - JVM Internals](../10-jvm-internals/)
- **Related:** [04 - Collections](../04-collections/) — thread-safe collections
- **Related:** [07 - Functional Programming](../07-functional-programming/) — CompletableFuture and Stream API
