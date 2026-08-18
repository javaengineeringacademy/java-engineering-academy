# Multithreading Quiz

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
