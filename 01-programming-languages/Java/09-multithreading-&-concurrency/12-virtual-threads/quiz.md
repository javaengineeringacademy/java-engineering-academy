# Virtual Threads — Quiz

## Question 1

How many virtual threads can you create compared to platform threads?

- A) Same number
- B) Virtual threads are limited to 1000
- C) Millions of virtual threads vs thousands of platform threads
- D) Virtual threads are slower

**Answer: C**
Virtual threads use ~1KB stack vs 1MB for platform threads, enabling millions of concurrent threads.

## Question 2

What happens when a virtual thread blocks on I/O?

- A) The carrier thread is blocked
- B) The virtual thread is unmounted from the carrier, freeing it
- C) The JVM throws an exception
- D) The thread pool is exhausted

**Answer: B**
When a virtual thread blocks, the JVM unmounts it from the carrier thread and mounts another, so the carrier is never wasted on blocking.

## Question 3

What is a "carrier thread" in the context of virtual threads?

- A) A daemon thread that transports data
- B) A platform thread that executes virtual threads when they are runnable
- C) The main thread
- D) A thread that creates virtual threads

**Answer: B**
Carrier threads are platform threads (by default from a ForkJoinPool) that virtual threads are mounted on for execution. When a virtual thread blocks, it unmounts and the carrier picks up another.

## Question 4

How do you create a virtual thread (Java 21)?

- A) `new Thread(() -> task())`
- B) `Thread.ofVirtual().start(() -> task())`
- C) `Executors.newVirtualThreadPerTaskExecutor().submit(() -> task())`
- D) Both B and C

**Answer: D**
Option B creates a single virtual thread directly. Option C creates an ExecutorService that creates a new virtual thread per submitted task. Option A creates a platform thread.

## Question 5

What is structured concurrency (Preview in Java 21)?

- A) Using synchronized blocks in a structured way
- B) A pattern where a task's lifetime is tied to its parent scope — child tasks must complete before the parent
- C) Using try-with-resources for threads
- D) A thread pool configuration

**Answer: B**
Structured concurrency ensures that if a parent task fails or is cancelled, all child tasks are cancelled. It provides clear task hierarchies and eliminates orphaned threads.

## Question 6

True or False: Virtual threads can use `synchronized` blocks.

**Answer: True**
Virtual threads work with synchronized blocks. However, when a virtual thread blocks in synchronized, it pins its carrier thread (cannot unmount). Use `ReentrantLock` instead for optimal performance.

## Question 7

What is "thread pinning" in virtual threads?

- A) When a virtual thread is permanently assigned to a carrier
- B) When a virtual thread blocks in a native method or synchronized block, preventing it from unmounting
- C) When a virtual thread is garbage collected
- D) When a carrier thread runs out of virtual threads

**Answer: B**
Pinning occurs during native method calls or synchronized block contention. The virtual thread cannot unmount, so the carrier is blocked. This defeats the purpose of virtual threads.

## Question 8

What is the `ThreadScope` class used for in structured concurrency?

- A) To define thread-local variables
- B) To manage a hierarchical scope of concurrent tasks
- C) To limit CPU usage
- D) To configure carrier thread count

**Answer: B**
`ThreadScope` (preview API) manages a hierarchy of tasks. Child tasks forked within a scope must complete before the scope closes, enabling structured cleanup and error propagation.

## Question 9

How does `Thread.ofVirtual().name("prefix-", 0).start(runnable)` name threads?

- A) All threads are named "prefix-"
- B) Threads are named "prefix-0", "prefix-1", "prefix-2", etc.
- C) The name is ignored
- D) Threads are named "virtual-0", "virtual-1"

**Answer: B**
The `name(prefix, start)` method generates sequential names: the first virtual thread gets "prefix-0", the next "prefix-1", and so on.

## Question 10

What is the recommended replacement for `synchronized` in virtual thread code?

- A) `volatile`
- B) `ReentrantLock`
- C) `Thread.sleep()`
- D) `AtomicInteger`

**Answer: B**
`ReentrantLock` is not a native method and does not cause pinning. Replace `synchronized` with `tryLock()`/`unlock()` in `finally` when using virtual threads for I/O-heavy code.
