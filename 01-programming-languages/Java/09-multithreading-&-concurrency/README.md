# Module 09: Multithreading and Concurrency

> **Difficulty:** ⭐⭐⭐⭐ Advanced  
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

## History

- **1996** — Java 1.0 introduced `Thread` class and `Runnable` interface
- **1998** — Java 1.2 added `ThreadGroup` improvements and `Thread.yield()`
- **2004** — Java 1.5 added `ExecutorService`, `Lock`, `Semaphore`, `CountDownLatch`, `CyclicBarrier`, `ConcurrentHashMap`, `BlockingQueue`, `java.util.concurrent.atomic` package
- **2006** — Java 1.6 added `Phaser` and `StampedLock` (preview)
- **2011** — Java 7 added `ForkJoinPool`, `Phaser` (standard), `TransferQueue`
- **2014** — Java 8 added `CompletableFuture`, `parallelStream()`, `ConcurrentHashMap` redesign (bin-level locking)
- **2017** — Java 9 added `Flow` API (reactive streams), `CompletionStage` improvements
- **2022** — Java 19 added virtual threads (preview), structured concurrency (preview)
- **2023** — Java 21 added virtual threads (standard), structured concurrency (second preview)

## Production Notes

### Thread Pool Sizing

- **CPU-bound tasks**: Pool size = number of CPU cores
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

## Why This Concept Exists

- **Responsiveness**: Keep UI/server responsive while processing in background
- **Throughput**: Utilize multiple CPUs for parallel processing
- **Resource sharing**: Threads share memory and file handles
- **Composition**: Combine I/O-bound and CPU-bound tasks efficiently

## Core Concepts

### Thread States

```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
```

| State | Description |
|-------|-------------|
| NEW | Created but not started |
| RUNNABLE | Ready to run, waiting for CPU time |
| BLOCKED | Waiting to acquire a monitor lock |
| WAITING | Waiting indefinitely for another thread |
| TIMED_WAITING | Waiting for a specified time |
| TERMINATED | Completed or threw exception |

### Synchronization

```java
// synchronized method
public synchronized void increment() {
    count++;
}

// synchronized block
public void increment() {
    synchronized (this) {
        count++;
    }
}

// volatile field
private volatile boolean running = true;

// Atomic classes
private final AtomicInteger count = new AtomicInteger(0);
```

### Wait/Notify

```java
// Producer-Consumer pattern
public synchronized void produce() throws InterruptedException {
    while (queue.isFull()) {
        wait(); // Release lock and wait
    }
    queue.add(item);
    notifyAll(); // Wake all waiting threads
}

public synchronized void consume() throws InterruptedException {
    while (queue.isEmpty()) {
        wait();
    }
    Item item = queue.remove();
    notifyAll();
}
```

### ExecutorService

```java
// Fixed thread pool
ExecutorService executor = Executors.newFixedThreadPool(4);

// Submit tasks
Future<String> future = executor.submit(() -> {
    return "Result from thread";
});

// Get result
String result = future.get();

// Shutdown
executor.shutdown();
executor.awaitTermination(5, TimeUnit.SECONDS);
```

### CompletableFuture

```java
CompletableFuture.supplyAsync(() -> fetchUser(id))
    .thenApply(user -> enrichUser(user))
    .thenAccept(user -> saveUser(user))
    .exceptionally(ex -> {
        log.error("Failed", ex);
        return null;
    });
```

## Internal Working

### Thread Creation Internals

1. **`new Thread()`** — Allocates Thread object on heap
2. **`start()`** — JNI call to OS to create native thread
3. **OS schedules thread** — Thread added to OS scheduler queue
4. **`run()` called** — JVM invokes `run()` method on new thread
5. **`terminate()`** — Thread exits `run()`, resources released

### Monitor Lock Internals

```
Object Header (Mark Word) → Monitor Object
├── Owner thread
├── Entry List (BLOCKED threads)
├── Wait Set (WAITING threads)
└── Recursive count (reentrant)
```

### Happens-Before Rules

| Rule | Guarantee |
|------|-----------|
| Program order | Each action happens-before next action in same thread |
| Monitor unlock | Unlock happens-before subsequent lock |
| Volatile write | Write happens-before subsequent read |
| Thread start | `start()` happens-before any action in started thread |
| Thread join | Any action in joined thread happens-before `join()` returns |
| Transitivity | If A happens-before B and B happens-before C, then A happens-before C |

## Syntax

```java
// Creating threads
Thread t1 = new Thread(() -> System.out.println("Hello"));
t1.start();

// Callable with Future
ExecutorService executor = Executors.newFixedThreadPool(4);
Future<Integer> future = executor.submit(() -> computeValue());

// ReentrantLock
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    // critical section
} finally {
    lock.unlock();
}

// ReadWriteLock
ReadWriteLock rwLock = new ReentrantReadWriteLock();
rwLock.readLock().lock();
try {
    // read-only operations
} finally {
    rwLock.readLock().unlock();
}

// Virtual threads (Java 21)
Thread.startVirtualThread(() -> {
    System.out.println("Running on virtual thread");
});

// CompletableFuture
CompletableFuture<Void> cf = CompletableFuture.runAsync(() -> {
    System.out.println("Async task");
});
```

## Examples

### Easy: Basic Thread Creation
```java
public class BasicThread {
    public static void main(String[] args) {
        // Using Runnable
        Runnable task = () -> {
            System.out.println("Running on: " + Thread.currentThread().getName());
        };
        
        Thread thread = new Thread(task, "MyThread");
        thread.start();
        
        // Using lambda
        new Thread(() -> {
            System.out.println("Lambda thread: " + Thread.currentThread().getName());
        }).start();
        
        System.out.println("Main thread: " + Thread.currentThread().getName());
    }
}
```

### Medium: Producer-Consumer
```java
import java.util.concurrent.*;

public class ProducerConsumer {
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
    
    public static void main(String[] args) {
        // Producer
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        // Consumer
        new Thread(() -> {
            try {
                while (true) {
                    int item = queue.take();
                    System.out.println("Consumed: " + item);
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
```

### Hard: Deadlock Prevention
```java
import java.util.concurrent.locks.*;

public class DeadlockPrevention {
    private final ReentrantLock lock1 = new ReentrantLock();
    private final ReentrantLock lock2 = new ReentrantLock();
    
    // Try lock with timeout to prevent deadlock
    public void transfer(int amount) {
        boolean acquired1 = false;
        boolean acquired2 = false;
        try {
            acquired1 = lock1.tryLock(100, TimeUnit.MILLISECONDS);
            acquired2 = lock2.tryLock(100, TimeUnit.MILLISECONDS);
            
            if (acquired1 && acquired2) {
                System.out.println("Transferring: " + amount);
            } else {
                System.out.println("Could not acquire locks, retrying...");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquired1) lock1.unlock();
            if (acquired2) lock2.unlock();
        }
    }
}
```

### Enterprise: CompletableFuture Pipeline
```java
import java.util.concurrent.*;
import java.util.stream.*;

public class AsyncPipeline {
    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> fetchOrderIds(), executor)
            .thenApplyAsync(ids -> ids.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> fetchOrder(id), executor))
                .collect(Collectors.toList()), executor)
            .thenApply(futures -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()))
            .thenAccept(orders -> {
                System.out.println("Fetched " + orders.size() + " orders");
                orders.forEach(System.out::println);
            })
            .exceptionally(ex -> {
                System.err.println("Pipeline failed: " + ex.getMessage());
                return null;
            });
    }
    
    static java.util.List<String> fetchOrderIds() {
        return java.util.List.of("ORD-001", "ORD-002", "ORD-003");
    }
    
    static String fetchOrder(String id) {
        return "Order{id=" + id + ", status=SHIPPED}";
    }
}
```

## Performance Considerations

| Operation | Cost | Notes |
|-----------|------|-------|
| Thread creation | ~1ms | OS thread allocation |
| Context switch | ~10μs | CPU save/restore state |
| Monitor acquisition | ~100ns | uncontended |
| Volatile read | ~10ns | Memory barrier |
| Atomic operation | ~20ns | CAS operation |
| Virtual thread | ~1μs | JVM-managed, heap-allocated |

- **Thread pools** reduce creation overhead
- **Virtual threads** eliminate most threading concerns
- **Atomic classes** outperform synchronized for simple operations
- **ConcurrentHashMap** scales better than `Collections.synchronizedMap`

## Best Practices

**Do's:**
- Use thread pools instead of creating threads per task
- Use `volatile` for flags, atomic classes for counters
- Use `CompletableFuture` for async composition
- Use `ReentrantLock` over `synchronized` when you need fairness or timed wait
- Clean up `ThreadLocal` values in thread pools
- Use virtual threads (Java 21+) for I/O-bound work

**Don'ts:**
- Don't use `Thread.stop()` (deprecated, unsafe)
- Don't use `Thread.sleep()` while holding a lock
- Don't use `wait()` without a `while` loop
- Don't use `Executors.newCachedThreadPool()` in production
- Don't create new threads per request
- Don't use `synchronized` on `this` for volatile fields

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

### Q1: What is the difference between a process and a thread?
**Answer:** Threads share the same address space within a process. Processes have separate memory spaces. Threads are lighter and faster to create, but share memory, which requires synchronization.

### Q2: What is the difference between `Runnable` and `Callable`?
**Answer:** `Runnable.run()` returns void and cannot throw checked exceptions. `Callable.call()` returns a value and can throw checked exceptions. Use `Callable` when you need a return value.

### Q3: What are the six thread states in Java?
**Answer:** NEW (created), RUNNABLE (ready/running), BLOCKED (waiting for lock), WAITING (waiting indefinitely), TIMED_WAITING (waiting with timeout), TERMINATED (completed).

### Q4: What is the difference between `synchronized` and `volatile`?
**Answer:** `synchronized` provides mutual exclusion AND visibility. `volatile` provides visibility only (no mutual exclusion). Use `volatile` for flags; use `synchronized` for compound operations.

### Q5: What causes a deadlock and how do you prevent it?
**Answer:** Deadlock is a circular lock dependency. Prevent by: lock ordering, tryLock with timeout, avoid holding multiple locks, deadlock detection tools.

### Q6: What is the difference between `ReentrantLock` and `synchronized`?
**Answer:** `ReentrantLock` supports timed wait, interruptible lock, fairness policy, and multiple conditions. `synchronized` is simpler and faster for basic use.

### Q7: What does the happens-before relationship guarantee?
**Answer:** Writes by one thread are visible to reads by another thread. Without it, threads may see stale values from CPU caches. The JMM defines specific happens-before rules.

### Q8: Why is `volatile` needed for 64-bit variables?
**Answer:** The JVM may split 64-bit reads/writes into two 32-bit operations, causing word tearing. `volatile` ensures atomicity for 64-bit variables.

### Q9: When should you use `CompletableFuture` vs `Future.get()`?
**Answer:** Use `CompletableFuture` for composing multiple async operations (chaining, combining). Use `Future.get()` for simple single-task waiting.

### Q10: What is work-stealing in ForkJoinPool?
**Answer:** Idle threads steal tasks from busy threads' deques. This provides load balancing without centralized scheduling.

### Q11: What is thread pinning and why is it bad for virtual threads?
**Answer:** Pinning occurs when a virtual thread blocks in a `synchronized` block or native method. The carrier thread cannot be reused, defeating the purpose of virtual threads. Use `ReentrantLock` instead.

### Q12: How many virtual threads can you create?
**Answer:** Millions, since they use ~1KB heap-allocated stacks vs ~1MB for platform threads.

## Cross-References

- **Previous Module:** [08 - I/O and NIO](../08-io-nio/)
- **Next Module:** [10 - JVM Internals](../10-jvm-internals/)
- **Related:** [04 - Collections](../04-collections/) — thread-safe collections
- **Related:** [07 - Functional Programming](../07-functional-programming/) — CompletableFuture and Stream API
- **Related:** [00 - Knowledge Atoms](../00-knowledge-atoms/) — volatile, synchronized

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Deadlock | Thread dump (`jstack`) | Look for BLOCKED threads and lock chains |
| Race condition | Thread sanitizer + stress testing | Run with multiple threads, check output |
| Memory leak (ThreadLocal) | Heap dump analysis | Find ThreadLocal values not cleaned up |
| High CPU usage | Thread dump + profiling | Identify busy loops or spinning threads |
| Virtual thread pinning | JFR + JDK 21 tools | Monitor `jdk.VirtualThreadPinned` events |

## Code Review Checklist

- [ ] Thread pools used instead of manual thread creation
- [ ] `volatile` used for simple flags, atomic classes for counters
- [ ] Locks released in finally blocks
- [ ] `wait()` called in while loops
- [ ] `ThreadLocal` values cleaned up in thread pools
- [ ] No `Thread.stop()` usage
- [ ] Appropriate thread pool sizing
- [ ] Deadlock prevention strategy in place

## Architecture Considerations

Concurrency is a cross-cutting concern that affects every layer of an application. At scale, thread pool sizing, lock granularity, and async patterns determine system throughput and latency. For microservices, thread pool isolation prevents cascading failures. For event-driven systems, async patterns enable high throughput with fewer resources.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Thread pool | Background tasks | Pros: Reuses threads, controlled concurrency; Cons: Pool sizing complexity |
| CompletableFuture | Async composition | Pros: Composable, non-blocking; Cons: Debugging complexity |
| Virtual threads | I/O-bound work | Pros: Simple code, high concurrency; Cons: Pinned threads, learning curve |
| ForkJoinPool | Divide-and-conquer | Pros: Work-stealing, parallelism; Cons: Complexity, task splitting |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Race condition in authentication | Bypass security checks | Use synchronized for check-then-act |
| Thread pool exhaustion (DoS) | Denial of service | Configure bounded pools with rejection policies |
| ThreadLocal leaking sensitive data | Information exposure | Clean up ThreadLocal after use |
| Uncontrolled parallelism | Resource exhaustion | Limit concurrent threads |
| Deadlock in security-critical code | System hang | Use tryLock with timeout |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Java 1.0 | `Thread`, `Runnable` | N/A — foundational |
| Java 5 | `ExecutorService`, `Lock`, atomics | Replace manual thread management |
| Java 7 | `ForkJoinPool` | Use for divide-and-conquer |
| Java 8 | `CompletableFuture` | Replace `Future.get()` with async |
| Java 19 | Virtual threads (preview) | Test with preview features |
| Java 21 | Virtual threads (standard) | Use for I/O-bound work |

## Version Validation

| Feature | Java Version | Status |
|---------|-------------|--------|
| `Thread`, `Runnable` | Java 1.0 | Stable |
| `ExecutorService`, `Lock` | Java 5 | Stable |
| `CompletableFuture` | Java 8 | Stable |
| Virtual threads | Java 21 | Stable |
| Structured concurrency | Java 21 | Preview |

## Production Incidents

### Incident 1: Thread Pool Exhaustion Causing Cascade Failure

**Problem:** A microservice became unresponsive under load; all requests timed out.
**Cause:** Fixed thread pool of 10 threads was exhausted by slow database queries; new requests queued indefinitely.
**Impact:** Complete service outage for 15 minutes; affected 3 downstream services.
**Detection:** Monitoring showed queue depth growing; thread dump showed all threads BLOCKED on DB.
**Solution:** Increased pool size to 50; added timeout to DB queries; implemented circuit breaker.
**Prevention:** Monitor queue depth; size pools based on workload; implement circuit breakers.

### Incident 2: Race Condition in Shared Counter

**Problem:** A billing system counted transactions incorrectly; totals were 10-15% lower than expected.
**Cause:** Non-atomic `count++` on shared variable; concurrent updates lost.
**Impact:** Incorrect billing totals; financial discrepancy discovered during audit.
**Detection:** Audit revealed count mismatch; code review showed non-atomic increment.
**Solution:** Changed to `AtomicInteger.incrementAndGet()`.
**Prevention:** Use atomic classes for counters; use `synchronized` for compound operations.

### Incident 3: Virtual Thread Pinning in Legacy Code

**Problem:** A Java 21 application using virtual threads showed no performance improvement over platform threads.
**Cause:** Legacy code used `synchronized` blocks extensively; virtual threads were pinned to carrier threads.
**Impact:** 10x more virtual threads but same throughput as platform threads.
**Detection:** JFR showed `jdk.VirtualThreadPinned` events; profiling revealed synchronized usage.
**Solution:** Replaced `synchronized` with `ReentrantLock` in hot paths.
**Prevention:** Audit code for `synchronized` before migrating to virtual threads; use JFR monitoring.

## Production Checklist

- [ ] Thread pools configured with appropriate sizing
- [ ] Rejection policies configured for thread pools
- [ ] `volatile` used for simple flags, atomic classes for counters
- [ ] Locks released in finally blocks
- [ ] `wait()` called in while loops
- [ ] `ThreadLocal` values cleaned up in thread pools
- [ ] No `Thread.stop()` usage
- [ ] Deadlock prevention strategy in place
- [ ] Circuit breakers for external calls
- [ ] Monitoring for thread pool metrics

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Creates threads manually; doesn't understand states; uses `Thread.sleep()` everywhere |
| Intermediate | Uses thread pools; understands synchronization; avoids deadlocks |
| Advanced | Uses CompletableFuture; understands JMM; configures thread pools |
| Expert | Designs concurrent systems; uses virtual threads; mentors on concurrency |

## Common Myths

1. **Myth**: `synchronized` is always slower than `ReentrantLock`
   **Truth**: `synchronized` is faster for uncontended locks. `ReentrantLock` is better when you need fairness, timed wait, or multiple conditions.

2. **Myth**: Virtual threads eliminate all concurrency concerns
   **Truth**: Virtual threads still require synchronization. Pinned threads (in `synchronized` blocks) defeat the purpose. Use `ReentrantLock` for virtual thread code.

3. **Myth**: More threads always mean better performance
   **Truth**: Too many threads cause context switching overhead. Optimal thread count depends on workload (CPU cores, I/O wait time).

4. **Myth**: `volatile` makes operations atomic
   **Truth**: `volatile` ensures visibility, not atomicity. `count++` is not atomic even with `volatile`. Use `AtomicInteger` for atomic operations.

5. **Myth**: Thread pools are always better than manual thread creation
   **Truth**: For long-running, single tasks, a dedicated thread may be simpler. Thread pools are for many short-lived tasks.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Concurrent execution, parallel processing |
| Thread states | NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED |
| Synchronized | Mutual exclusion + visibility |
| Volatile | Visibility only (no mutual exclusion) |
| Atomic classes | Lock-free thread-safe operations |
| Thread pool | Reuses threads, controlled concurrency |
| CompletableFuture | Async composition, non-blocking |
| Virtual threads | JVM-managed, millions possible |
| Best practice | Use thread pools, clean ThreadLocal |
| Common mistake | Creating threads per request |
| When to use | All concurrent applications |
| When to avoid | Never — concurrency is fundamental |
