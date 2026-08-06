# Multithreading Exercises

Practice Java multithreading through hands-on exercises.

## Exercise 1: Thread Creation

**Problem Statement:**
Create a multi-threaded program using three different approaches: extending `Thread`, implementing `Runnable`, and implementing `Callable` with `FutureTask`. Each thread should compute a partial sum of a large array and return the result.

**Expected Behavior:**
- Three threads run concurrently, each computing a sum of array segments.
- `Thread` subclass prints its result directly.
- `Runnable` stores its result in a shared `AtomicInteger`.
- `Callable` returns its result via `Future.get()`.
- All three approaches produce the correct total sum.
- The main thread waits for all to complete before printing the total.

**Hints:**
- Use `Thread.start()` to begin execution and `Thread.join()` to wait for completion.
- For `Callable`, wrap in `FutureTask` and pass to `Thread` constructor.
- Use `ExecutorService.submit()` for the `Callable` approach.
- Divide the array evenly among threads (e.g., array.length / 3 per thread).

---

## Exercise 2: Synchronization

**Problem Statement:**
Implement a thread-safe counter class using three synchronization strategies: `synchronized` methods, `synchronized` blocks, and `ReentrantLock`. Run 100 threads each incrementing the counter 10,000 times and verify the final count is correct for each approach.

**Expected Behavior:**
- An unsynchronized counter produces an incorrect final count (less than expected).
- The `synchronized` method counter produces exactly 1,000,000.
- The `synchronized` block counter produces exactly 1,000,000.
- The `ReentrantLock` counter produces exactly 1,000,000.
- Performance timing is printed for each approach.

**Hints:**
- Use `Thread.join()` to wait for all threads to finish before checking the count.
- Use `System.nanoTime()` to measure the time taken by each approach.
- The unsynchronized counter demonstrates the race condition clearly.
- Use `lock.lock()` and `lock.unlock()` in a try-finally block for ReentrantLock.

---

## Exercise 3: Producer-Consumer

**Problem Statement:**
Implement a producer-consumer system using a shared `BlockingQueue`. Producers generate random integers and put them in the queue. Consumers take integers and compute their square. Use multiple producers and multiple consumers.

**Expected Behavior:**
- 3 producers each produce 20 random integers.
- 2 consumers each consume and process integers until the queue is empty.
- A poison pill (e.g., `-1`) signals producers to stop.
- No items are lost or processed twice.
- The program terminates gracefully after all items are consumed.

**Hints:**
- Use `LinkedBlockingQueue` or `ArrayBlockingQueue` as the shared buffer.
- Use `queue.put()` in producers and `queue.take()` in consumers.
- Use poison pill values or `Thread.interrupted()` to signal termination.
- Count total items produced and consumed to verify correctness.

---

## Exercise 4: Thread Pool

**Problem Statement:**
Create a web scraper simulator that downloads multiple URLs concurrently using `ExecutorService`. Implement fixed thread pool, cached thread pool, and scheduled thread pool approaches. Handle task failures gracefully.

**Expected Behavior:**
- Fixed pool with 5 threads processes 20 URLs concurrently.
- Cached pool creates threads as needed and reuses idle threads.
- Scheduled pool executes tasks after a specified delay.
- Failed downloads are logged without stopping other tasks.
- All pools are shut down gracefully using `shutdown()` and `awaitTermination()`.

**Hints:**
- Use `Executors.newFixedThreadPool(5)`, `newCachedThreadPool()`, and `newScheduledThreadPool(2)`.
- Wrap task execution in try-catch within the Runnable to isolate failures.
- Use `Future.get(timeout, TimeUnit.SECONDS)` to handle slow tasks.
- Call `executor.shutdown()` followed by `executor.awaitTermination()` for graceful shutdown.

---

## Exercise 5: CompletableFuture Pipeline

**Problem Statement:**
Build an asynchronous data processing pipeline using `CompletableFuture`. Chain operations: fetch user data, then fetch their orders, then calculate order total, then send a notification. Each step simulates network latency.

**Expected Behavior:**
- The pipeline executes asynchronously without blocking the main thread.
- Each step completes before the next one starts (sequential chain).
- `thenApply` transforms data at each stage.
- `thenAccept` consumes the final result.
- Errors at any stage are caught by `exceptionally` handlers.
- The total pipeline time is less than the sum of all delays.

**Hints:**
- Use `CompletableFuture.supplyAsync()` to start the chain.
- Use `thenApplyAsync()` for CPU-bound transformations.
- Use `thenAcceptAsync()` for final consumption.
- Use `exceptionally()` to handle failures at each stage.
- Measure wall-clock time to verify async benefits.

---

## Exercise 6: Deadlock Detection

**Problem Statement:**
Write a program that intentionally creates a deadlock between two threads. Then implement a deadlock detection mechanism using `ThreadMXBean` to identify and report the deadlocked threads.

**Expected Behavior:**
- Two threads each acquire two locks in opposite order, causing deadlock.
- `ThreadMXBean.findDeadlockedThreads()` detects the deadlocked threads.
- The detected thread IDs and their lock information are printed.
- The program can optionally resolve the deadlock by interrupting one thread.
- A log of lock acquisition is maintained for debugging.

**Hints:**
- Create two static lock objects and have each thread lock them in different orders.
- Use `ManagementFactory.getThreadMXBean()` to get the MXBean.
- Call `findDeadlockedThreads()` periodically or after a timeout.
- Use `thread.getThreadInfo()` to get detailed lock and block information.

---

## Exercise 7: Concurrent Collections

**Problem Statement:**
Compare the performance and behavior of `HashMap`, `ConcurrentHashMap`, and `Collections.synchronizedMap()` under concurrent access. Run multiple threads performing simultaneous reads and writes on each map type.

**Expected Behavior:**
- `HashMap` may throw `ConcurrentModificationException` or produce corrupt data.
- `synchronizedMap` works correctly but blocks all concurrent access.
- `ConcurrentHashMap` allows concurrent reads and striped writes.
- `ConcurrentHashMap` has the best throughput under high contention.
- Each map is tested with 10 threads performing 10,000 operations each.

**Hints:**
- Use `ConcurrentModificationException` detection with iterator-based reads.
- Measure throughput using `CountDownLatch` to synchronize thread start times.
- Use `ConcurrentHashMap.putIfAbsent()` and `compute()` for atomic operations.
- Print the final map size and execution time for each map type.

---

## Exercise 8: Atomic Variables

**Problem Statement:**
Implement a lock-free counter using `AtomicLong`, a concurrent accumulator using `LongAdder`, and a compare-and-swap based stack. Compare their performance and correctness under high contention.

**Expected Behavior:**
- `AtomicLong` provides thread-safe increment/decrement operations.
- `LongAdder` outperforms `AtomicLong` under high contention.
- The CAS-based stack supports `push`, `pop`, and `isEmpty` without locks.
- All implementations produce correct results with concurrent access.
- Performance metrics show the throughput difference between approaches.

**Hints:**
- Use `atomicLong.incrementAndGet()` and `compareAndSet()` directly.
- Use `LongAdder.increment()` and `LongAdder.sum()` for the adder.
- For the CAS stack, use `AtomicReference<Node>` with a loop around `compareAndSet()`.
- Use multiple threads incrementing/operating simultaneously to create contention.
