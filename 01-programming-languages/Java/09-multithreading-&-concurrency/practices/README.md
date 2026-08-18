# Java Concurrency Practices

This directory contains hands-on exercises to master Java multithreading and concurrency concepts.

## Structure

| # | File | Topic | Exercises |
|---|------|-------|-----------|
| 1 | ThreadBasicsExercises | Thread Creation & Runnable | 5 |
| 2 | ThreadMethodsExercises | Thread Lifecycle Methods | 5 |
| 3 | SynchronizationExercises | synchronized keyword | 5 |
| 4 | WaitNotifyExercises | wait/notify/producer-consumer | 5 |
| 5 | LockExercises | ReentrantLock, ReadWriteLock | 5 |
| 6 | ExecutorServiceExercises | Thread Pools & Executors | 5 |
| 7 | CompletableFutureExercises | Async Composition | 5 |
| 8 | ConcurrentCollectionsExercises | Thread-safe Collections | 5 |
| 9 | AtomicExercises | Atomic Variables | 5 |
| 10 | MemoryModelExercises | JMM & happens-before | 5 |
| 11 | VirtualThreadExercises | Virtual Threads & Structured Concurrency | 5 |

## How to Use

1. Each file has TODO comments marking what you need to implement
2. Attempt each exercise before looking at the solutions folder
3. Run the `main()` method to verify your implementation
4. Compare with solutions when stuck or to check your approach

## Running

```bash
# From the project root
javac -d out practices/ThreadBasicsExercises.java
java -cp out academy.javaengineering.concurrency.practices.ThreadBasicsExercises
```
