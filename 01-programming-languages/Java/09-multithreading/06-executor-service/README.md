# ExecutorService

## Overview

Every time you call `new Thread(runnable).start()` in production code, you're creating a thread that takes ~1MB of stack space and costs milliseconds to spawn. If your application handles hundreds of concurrent requests, you're burning through memory and CPU just on thread lifecycle management. ExecutorService fixes this by reusing a pool of threads — submit your task, get a Future back, and let the pool handle the rest.

But here's what most engineers get wrong: they grab `Executors.newFixedThreadPool(10)` from a tutorial and ship it to production. That pool uses an unbounded queue, which means under load, tasks pile up until you hit OutOfMemoryError. This topic covers not just how ExecutorService works, but how to size pools correctly, shut down gracefully, and avoid the thread-starvation bugs that cause cascading failures.

## Why It Matters

ExecutorService solves the problems of manual thread management: thread creation overhead, lack of thread reuse, no task queuing, and no graceful shutdown. It provides thread pools that reuse threads across tasks, built-in task queuing, and lifecycle management.

## Prerequisites

- Module 08: Introduction to Multithreading
- Module 08: Thread Creation
- Basic understanding of Runnable and Callable interfaces
- Understanding of Future and concurrency basics

## History

| Version | Change |
|---------|--------|
| JDK 5 | ExecutorService, ThreadPoolExecutor, Executors factory |
| JDK 7 | ForkJoinPool introduced |
| JDK 8 | CompletableFuture added |
| JDK 19 | Virtual Threads preview |
| JDK 21 | Virtual Threads GA |

## Learning Objectives

By the end of this topic you will be able to:

- Choose the right thread pool type (fixed, cached, scheduled) based on task characteristics.
- Calculate optimal pool size using the CPU-bound and IO-bound formulas.
- Implement graceful shutdown with timeout and fallback to `shutdownNow()`.
- Diagnose thread starvation from thread dumps and queue depth metrics.
- Avoid the unbounded-queue OOM trap that `Executors.newFixedThreadPool()` creates.
- Use `Future.get(timeout)` to prevent one slow task from blocking the entire pool.

## Thread Pool Types and When to Use

### Fixed Thread Pool
- **Use case**: CPU-bound tasks, predictable workload
- **Queue**: LinkedBlockingQueue (unbounded)
- **Risk**: OutOfMemoryError if tasks accumulate faster than processed

### Cached Thread Pool
- **Use case**: Short-lived IO-bound tasks
- **Queue**: SynchronousQueue (no buffering)
- **Risk**: Unbounded thread creation under load

### Scheduled Thread Pool
- **Use case**: Periodic tasks, delayed execution
- **Queue**: DelayedWorkQueue
- **Risk**: Task starvation if pool is too small

## Pool Sizing Formulas

### CPU-Bound Work
```
poolSize = numCPUcores + 1
```
Extra thread compensates for page faults or other occasional stalls.

### IO-Bound Work
```
poolSize = numCPUcores * (1 + waitTime / computeTime)
```
Example: If tasks wait 80% of the time on 4 cores:
```
poolSize = 4 * (1 + 0.8/0.2) = 4 * 5 = 20
```

### Mixed Workload
```
poolSize = numCPUcores * targetUtilization * (1 + waitTime / computeTime)
```
Where targetUtilization is between 0 and 1.

## Shutdown Strategies

| Method | Behavior |
|--------|----------|
| `shutdown()` | No new tasks, completes existing |
| `shutdownNow()` | Interrupts running, returns pending |
| `isShutdown()` | True after shutdown() called |
| `isTerminated()` | True when all tasks complete |
| `awaitTermination(timeout)` | Blocks until terminated or timeout |

### Best Practices
1. Always call `shutdown()` after use
2. Use `awaitTermination()` to wait for completion
3. Handle `InterruptedException` properly
4. Use `shutdownNow()` only when cancellation is needed

## RejectedExecutionHandler

| Policy | Behavior |
|--------|----------|
| `AbortPolicy` | Throws RejectedExecutionException (default) |
| `CallerRunsPolicy` | Runs task in calling thread |
| `DiscardPolicy` | Silently discards task |
| `DiscardOldestPolicy` | Discards oldest in queue, retries |

## Common Patterns

### Graceful Shutdown

This example demonstrates the recommended shutdown pattern: call shutdown() then awaitTermination(), with a fallback to shutdownNow() if tasks don't complete in time.

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

ExecutorService pool = Executors.newFixedThreadPool(4);
try {
    // submit tasks
    pool.submit(() -> System.out.println("Task 1"));
    pool.submit(() -> System.out.println("Task 2"));
} finally {
    pool.shutdown();
    if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
        pool.shutdownNow();
    }
}
```

> **Production Note:** Always call shutdown() after use to prevent thread leaks. Use awaitTermination() with a reasonable timeout to allow tasks to complete gracefully.

### Bounded Execution with Timeout

This example shows how to submit a task with a timeout and cancel it if it takes too long.

```java
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

Future<Result> future = pool.submit(task);
try {
    Result result = future.get(5, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    future.cancel(true);
}
```

> **Production Note:** Always set timeouts on Future.get() to prevent indefinite blocking. Use cancel(true) to interrupt the task if it exceeds the timeout.

### Thread Pool Monitoring

This example demonstrates how to monitor thread pool metrics including active threads, completed tasks, and queue depth.

```java
import java.util.concurrent.ThreadPoolExecutor;

ThreadPoolExecutor executor = (ThreadPoolExecutor) pool;
int active = executor.getActiveCount();
long completed = executor.getCompletedTaskCount();
int queued = executor.getQueue().size();
System.out.println("Active: " + active + ", Completed: " + completed + ", Queued: " + queued);
```

> **Production Note:** Monitor these metrics in production to detect thread starvation, queue buildup, and pool exhaustion. Set alerts on queue depth thresholds.

## Common Mistakes

1. **Unbounded queues**: Can cause OOM with fixed pools
2. **Ignoring shutdown**: Leaks threads
3. **Using `Thread.sleep()` in pools**: Wastes threads
4. **Not handling exceptions**: Swallowed in execute()
5. **Static pool creation**: Creates too many pools

## When NOT to Use ExecutorService

- **Single long-running task**: A raw `Thread` is simpler and doesn't need pooling.
- **I/O-bound with massive concurrency**: Virtual threads (Java 21+) handle thousands of connections without pool sizing headaches.
- **Simple async composition**: `CompletableFuture` chains are cleaner for simple request-response flows.
- **One-off tasks with no reuse**: Creating a pool for a single task wastes resources.

## Trade-offs

- **Fixed vs. cached pools**: Fixed pools give predictable resource usage but can queue tasks indefinitely. Cached pools handle bursts but can create unbounded threads under load.
- **Unbounded vs. bounded queues**: Unbounded queues prevent task rejection but risk OOM. Bounded queues force you to handle backpressure but require a rejection policy.
- **execute() vs. submit()**: `execute()` is simpler but swallows exceptions silently. `submit()` returns a Future and preserves exception details — always prefer it unless you don't care about errors.

## Why ExecutorService Over Raw Threads?

| Criteria | ExecutorService | Raw Threads |
|----------|----------------|-------------|
| Thread reuse | Yes | No |
| Task queue | Built-in | Manual |
| Shutdown | Graceful | Manual |
| Scaling | Pool sizing | Manual |
| Use when | Multiple tasks | Single long-running task |

### Decision Flowchart
Multiple tasks? → Yes → Use ExecutorService
Single long-running task? → Yes → Use raw Thread

## Production Notes

**Where is it used?**
- Web request handling thread pools
- Background job processing queues
- Async email/notification sending
- Database connection pool management
- Scheduled health checks and monitoring

**Why is it useful?**
- Thread reuse reduces creation overhead
- Built-in task queuing and lifecycle management
- Graceful shutdown prevents thread leaks
- Configurable pool sizing for different workloads

**When should it be avoided?**
- A single long-running task is sufficient (use raw Thread)
- Virtual threads better fit I/O-bound workloads
- Simple CompletableFuture composition is enough
- One-off tasks with no need for pooling

**Alternative?**
- Virtual Threads (Java 21+) — massive I/O-bound concurrency
- CompletableFuture — async composition and chaining
- ForkJoinPool — recursive divide-and-conquer tasks
- Raw Thread — single long-running daemon task

## Interview Questions

1. **What is the difference between execute() and submit()?** — execute() returns void and swallows exceptions; submit() returns Future and captures exceptions.
2. **Why not use Executors.newFixedThreadPool() in production?** — It uses unbounded LinkedBlockingQueue which can cause OOM under load.
3. **What is the recommended pool size for CPU-bound work?** — numCPUcores + 1
4. **What is the recommended pool size for I/O-bound work?** — numCPUcores * (1 + waitTime / computeTime)
5. **How do you handle RejectedExecutionException?** — Use a rejection policy: CallerRunsPolicy, AbortPolicy, DiscardPolicy, or DiscardOldestPolicy.

## One-Minute Revision

- ExecutorService manages thread pools for task execution
- Use ThreadPoolExecutor directly with bounded queues in production
- Always call shutdown() and awaitTermination() for graceful shutdown
- Size pools based on workload: CPU-bound (cores+1), I/O-bound (cores * wait/compute ratio)
- Monitor active count, completed tasks, and queue depth

## Quiz

**Q1:** What happens when a fixed thread pool's queue is full?
<details><summary>Answer</summary>The RejectedExecutionHandler is invoked. Default is AbortPolicy which throws RejectedExecutionException.</details>

**Q2:** What is the difference between shutdown() and shutdownNow()?
<details><summary>Answer</summary>shutdown() stops accepting new tasks and waits for existing tasks. shutdownNow() interrupts running tasks and returns a list of pending tasks.</details>

## References

- [Oracle Java Documentation - ExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ExecutorService.html)
- [Effective Java - Item 84: Don't depend on the thread scheduler](https://learning.oreilly.com/library/view/effective-java/9780134686097/)

## Production Incidents

### Incident 1: Thread Pool Exhaustion Causing Cascading Failure

**Problem:** An order processing service stopped processing orders during peak traffic. Downstream services also failed due to timeout cascades.
**Cause:** A fixed thread pool of 50 threads was used for external API calls. One external API started responding slowly (5-second timeouts instead of 50ms). All 50 threads were blocked waiting for responses, and new orders piled up in the queue indefinitely.
**Impact:** Order processing halted for 3 hours. Revenue loss estimated at $200K. SLA breach triggered penalties.
**Detection:** Monitoring showed thread pool active count at 50 with queue depth growing. Alerts fired on queue depth threshold.
**Solution:** Implement bounded queues with `CallerRunsPolicy` to apply backpressure. Add per-task timeouts using `Future.get(timeout, unit)`. Separate slow and fast operations into different pools.
**Prevention:** Always set task timeouts. Use bounded queues with rejection policies. Add circuit breakers for external calls. Monitor thread pool utilization metrics.

### Incident 2: Unbounded Queue Causing OOM

**Problem:** A background job processing system crashed with OutOfMemoryError after running for several days.
**Cause:** A fixed thread pool with an unbounded `LinkedBlockingQueue` was used. Tasks were submitted faster than processed. The queue grew unbounded, accumulating millions of task objects in memory.
**Impact:** Production OOM crash. 4-hour recovery time. Data loss for unprocessed tasks.
**Detection:** Heap dumps showed a massive LinkedBlockingQueue consuming 90% of heap space.
**Solution:** Replace unbounded queue with `ArrayBlockingQueue` of fixed capacity. Implement `RejectedExecutionHandler` that logs and alerts on rejection. Add queue depth monitoring.
**Prevention:** Never use `Executors.newFixedThreadPool()` in production (it uses unbounded queues). Always use `ThreadPoolExecutor` directly with bounded queues. Set up queue depth alerts.

## Production Checklist

### ✅ Before using ExecutorService in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it
☐ I've tested with realistic data volume

## Alternatives

| Approach | Thread Reuse | Task Queue | Scaling | Use When |
|----------|-------------|------------|---------|----------|
| ExecutorService | Yes | Built-in | Pool sizing | Multiple concurrent tasks |
| Virtual Threads (21+) | JVM-managed | None needed | Auto | Massive I/O-bound concurrency |
| CompletableFuture | No | No | N/A | Async composition and chaining |
| ForkJoinPool | Yes | Work-stealing | Recursive | Divide-and-conquer tasks |
| Raw Thread | No | Manual | Manual | Single long-running task |

## Trade-offs

ExecutorService provides thread management because it:
- Requires explicit pool sizing (use virtual threads to avoid sizing dilemma)
- Unbounded queues can cause OOM (use bounded queues with rejection policies)
- Fixed pools can starve under slow tasks (separate fast/slow into different pools)
- Requires explicit shutdown (forgetting leaks threads)
- Common ForkJoinPool is shared (use dedicated pools for production workloads)

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Knows resize/rehash algorithms
- Can optimize for specific use cases

### Level 5: Master
- Can debug in production
- Can explain trade-offs to team
- Can design custom implementations
