# Multithreading & Concurrency — Comprehensive Quiz

## Question 1

A web server handles 10,000 concurrent connections. The current implementation creates a new thread per connection and crashes under load. How should you redesign it?

- A) Create a new thread for each connection
- B) Use a fixed thread pool with size equal to CPU cores
- C) Use a cached thread pool or virtual threads (Java 21+)
- D) Use a single thread for all connections

**Answer: C**
I/O-bound operations benefit from many threads. Virtual threads handle millions of concurrent connections with minimal overhead.

## Question 2

A financial system processes transactions with shared counters. `synchronized` causes contention. Which alternative reduces contention?

- A) `volatile` variables
- B) `AtomicLong` with `compareAndSet()`
- C) `Thread.sleep()` between updates
- D) `wait()` and `notify()`

**Answer: B**
AtomicLong with CAS provides lock-free thread safety without blocking threads.

## Question 3

What is the output?

```java
Thread t = new Thread(() -> System.out.print("Worker "));
t.start();
t.join();
System.out.print("Main ");
```

**Answer:** `Worker Main ` (with possible whitespace variation)
`join()` waits for thread `t` to complete before main thread continues.

## Question 4

Find the bug:

```java
class Counter {
    int count = 0;
    void increment() { count++; }
}
// Used by 100 threads each incrementing 1000 times
```

**Bug:** Race condition — `count++` is not atomic.
**Fix:** Use `AtomicInteger` or `synchronized`.

## Question 5

Which collection is best for producer-consumer with bounded buffer?

- A) `ArrayList` with synchronized methods
- B) `BlockingQueue` (e.g., `ArrayBlockingQueue`)
- C) `HashMap` with locks
- D) `LinkedList` with manual synchronization

**Answer: B**
BlockingQueue provides built-in `put()` (blocks when full) and `take()` (blocks when empty).

## Question 6

What is the difference between a process and a thread?

- A) A thread is a lightweight process with its own memory
- B) A process is a lightweight thread with shared memory
- C) Threads share the same address space within a process
- D) Processes are faster than threads

**Answer: C**
Threads within the same process share heap memory, code, and data segments but have their own stack and program counter. Processes have entirely separate address spaces.

## Question 7

What is the output?

```java
ExecutorService exec = Executors.newFixedThreadPool(1);
exec.submit(() -> { throw new RuntimeException("boom"); });
Future<?> f = exec.submit(() -> "done");
System.out.println(f.get());
```

- A) Prints `done`
- B) Throws `ExecutionException`
- C) Prints `boom`
- D) `RejectedExecutionException`

**Answer: A**
The exception from the first task is captured in the Future returned by that submit. The second task submits successfully and its Future returns `done`. The pool remains alive.

## Question 8

True or False: `Thread.interrupted()` clears the interrupted status.

**Answer: True**
`Thread.interrupted()` is a static method that checks and then clears the interrupted status of the current thread. `isInterrupted()` does not clear it.

## Question 9

Which statement about `synchronized` is FALSE?

- A) It re-entrant — a thread can re-acquire its own monitor
- B) It guarantees memory visibility for all variables
- C) It prevents thread interleaving within the synchronized block
- D) The lock is released automatically when the block exits

**Answer: B**
`synchronized` guarantees visibility only for variables accessed within the synchronized block. Variables read before entering the block may be stale.

## Question 10

What happens when you call `Thread.sleep(1000)`?

- A) The thread releases its monitor lock
- B) The thread yields but does not release any lock
- C) The JVM pauses the entire process
- D) The thread stops permanently

**Answer: B**
`Thread.sleep()` does NOT release any locks. It merely causes the thread to enter TIMED_WAITING state. Other threads needing the same lock will block.

## Question 11

In a `ThreadPoolExecutor`, what happens when the queue is full and the maximum pool size is reached?

- A) New threads are created indefinitely
- B) The caller thread runs the task (default rejection policy)
- C) The task is silently discarded
- D) The JVM throws an OutOfMemoryError

**Answer: B**
With the default `AbortPolicy`, a `RejectedExecutionException` is thrown. However, the default `CallerRunsPolicy` causes the submitting thread to execute the task directly if the pool and queue are both full.

## Question 12

Which is the correct way to start a virtual thread (Java 21+)?

- A) `Thread.startVirtualThread(() -> task())`
- B) `Executors.newVirtualThreadPerTaskExecutor()`
- C) `Thread.ofVirtual().name("vt").start(() -> task())`
- D) All of the above

**Answer: D**
All three are valid. `Thread.startVirtualThread()` is the simplest one-shot API. `Thread.ofVirtual()` provides a builder. `Executors.newVirtualThreadPerTaskExecutor()` returns an ExecutorService backed by virtual threads.
