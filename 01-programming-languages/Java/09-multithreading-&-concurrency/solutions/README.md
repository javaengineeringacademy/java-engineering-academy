# Java Concurrency Solutions

This directory contains complete solutions for all exercises in the practices folder.

## Structure

| # | File | Topic | Exercises |
|---|------|-------|-----------|
| 1 | ThreadBasicsSolutions | Thread Creation & Runnable | 5 |
| 2 | ThreadMethodsSolutions | Thread Lifecycle Methods | 5 |
| 3 | SynchronizationSolutions | synchronized keyword | 5 |
| 4 | WaitNotifySolutions | wait/notify/producer-consumer | 5 |
| 5 | LockSolutions | ReentrantLock, ReadWriteLock | 5 |
| 6 | ExecutorServiceSolutions | Thread Pools & Executors | 5 |
| 7 | CompletableFutureSolutions | Async Composition | 5 |
| 8 | ConcurrentCollectionsSolutions | Thread-safe Collections | 5 |
| 9 | AtomicSolutions | Atomic Variables | 5 |
| 10 | MemoryModelSolutions | JMM & happens-before | 5 |
| 11 | VirtualThreadSolutions | Virtual Threads & Structured Concurrency | 5 |

## How to Use

1. Try to solve exercises in the practices folder first
2. Use solutions as reference when stuck
3. Run each solution to see expected output
4. Study the patterns and techniques used

## Running

```bash
# From the project root
javac -d out solutions/ThreadBasicsSolutions.java
java -cp out academy.javaengineering.concurrency.solutions.ThreadBasicsSolutions
```
