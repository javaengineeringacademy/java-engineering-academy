# Java Multithreading & Concurrency - Examples Index

## Thread Basics
| File | Topics |
|------|--------|
| `ThreadBasicsExamples.java` | Thread creation, Runnable interface, Thread class, naming threads, daemon threads |
| `ThreadMethodsExamples.java` | start(), run(), join(), sleep(), yield(), interrupt(), isAlive(), getState() |

## Synchronization
| File | Topics |
|------|--------|
| `SynchronizationExamples.java` | synchronized methods, synchronized blocks, static synchronization, volatile keyword |
| `WaitNotifyExamples.java` | wait(), notify(), notifyAll(), producer-consumer with wait/notify |
| `LockExamples.java` | ReentrantLock, ReadWriteLock, StampedLock, tryLock, lockInterruptibly |

## Executor Framework
| File | Topics |
|------|--------|
| `ExecutorServiceExamples.java` | ExecutorService, thread pools (Fixed, Cached, Single, Scheduled), Callable, Future |
| `CompletableFutureExamples.java` | thenApply, thenCompose, thenCombine, allOf, anyOf, exceptionally, handle |

## Concurrent Collections
| File | Topics |
|------|--------|
| `ConcurrentCollectionsExamples.java` | ConcurrentHashMap, ConcurrentLinkedQueue, BlockingQueue, CopyOnWriteArrayList |
| `AtomicExamples.java` | AtomicInteger, AtomicLong, AtomicReference, AtomicBoolean, CAS operations |

## ThreadLocal & Memory Model
| File | Topics |
|------|--------|
| `ThreadLocalExamples.java` | ThreadLocal, InheritableThreadLocal, with InitialValue, remove() |
| `MemoryModelExamples.java` | happens-before, visibility problems, reordering, memory barriers |

## Advanced Topics
| File | Topics |
|------|--------|
| `VirtualThreadExamples.java` | Java 21 virtual threads, Thread.ofVirtual(), structured concurrency |
| `ForkJoinExamples.java` | ForkJoinPool, RecursiveTask, RecursiveAction, work-stealing |
| `DeadlockExamples.java` | Classic deadlock, detection, prevention, lock ordering |
| `ProducerConsumerPattern.java` | Multiple producer-consumer patterns with BlockingQueue, wait/notify, Lock |
