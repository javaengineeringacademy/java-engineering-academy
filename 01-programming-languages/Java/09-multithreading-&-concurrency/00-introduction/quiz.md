# Introduction to Multithreading — Quiz

## Question 1

What is the difference between a thread and a process?

- A) Threads share memory; processes do not
- B) Processes share memory; threads do not
- C) They are the same thing
- D) Threads are slower than processes

**Answer: A**
Threads within the same process share heap memory but have separate stacks. Processes have separate memory spaces.

## Question 2

What is the difference between concurrency and parallelism?

- A) They are the same thing
- B) Concurrency is multiple tasks making progress; parallelism is simultaneous execution
- C) Parallelism is multiple tasks making progress; concurrency is simultaneous execution
- D) Neither involves multiple tasks

**Answer: B**
Concurrency means tasks can be interleaved (even on one core). Parallelism requires multiple cores for true simultaneous execution.

## Question 3

What problem does multithreading solve in a GUI application?

- A) It makes the GUI look better
- B) It keeps the UI responsive while performing background work
- C) It reduces memory usage
- D) It prevents crashes

**Answer: B**
A single-threaded GUI freezes when performing long-running operations (network, disk I/O). A background thread handles the work while the UI thread processes events.

## Question 4

Which of the following is NOT a benefit of multithreading?

- A) Better CPU utilization on multi-core systems
- B) Improved application responsiveness
- C) Simplified code structure
- D) Ability to perform multiple tasks simultaneously

**Answer: C**
Multithreading introduces complexity: race conditions, deadlocks, and thread-safety issues. It does not simplify code — it adds synchronization overhead.

## Question 5

What is the main drawback of using too many threads?

- A) Slower compilation
- B) Context-switching overhead and increased memory consumption
- C) Java cannot handle more than 100 threads
- D) Threads automatically merge into one

**Answer: B**
Each thread consumes stack memory (default 1MB on most JVMs) and context switching between many threads adds CPU overhead, degrading performance.

## Question 6

True or False: Java is single-threaded at the language level.

**Answer: False**
Java has built-in multithreading support since JDK 1.0. The `Thread` class and `Runnable` interface are part of the core language.

## Question 7

What is an I/O-bound task?

- A) A task that spends most time performing calculations
- B) A task that spends most time waiting for external resources (disk, network)
- C) A task that uses more than one CPU core
- D) A task that never blocks

**Answer: B**
I/O-bound tasks are limited by the speed of external devices, not CPU. Threads handling I/O benefit from concurrency because they can yield while waiting.

## Question 8

What is a CPU-bound task?

- A) A task limited by disk speed
- B) A task that spends most time performing computations
- C) A task that blocks on network calls
- D) A task that uses ThreadLocal variables

**Answer: B**
CPU-bound tasks are limited by processor speed. Adding more threads beyond the number of cores gives diminishing returns due to context switching.

## Question 9

In Java, which thread has the highest priority by default?

- A) Daemon threads
- B) The main thread
- C) All threads start with the same default priority
- D) Only user threads get priority

**Answer: C**
All threads start with `Thread.NORM_PRIORITY` (5). Priority hints the scheduler but does not guarantee execution order — that depends on the OS.

## Question 10

Which Java feature introduced in Java 21 most significantly simplified concurrent programming?

- A) `CompletableFuture`
- B) Virtual threads
- C) `ConcurrentHashMap`
- D) `ForkJoinPool`

**Answer: B**
Virtual threads (Project Loom, finalized in Java 21) let you write blocking-style code with millions of concurrent threads, dramatically reducing the complexity of concurrent programming.
