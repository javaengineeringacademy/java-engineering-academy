# ExecutorService API Reference

## Overview

`ExecutorService` is the primary interface for managing thread pools and asynchronous task execution in Java.

## Factory Methods

| Method | Description |
|--------|-------------|
| `newFixedThreadPool(n)` | Fixed number of threads, unbounded queue |
| `newCachedThreadPool()` | Creates threads as needed, reuses idle |
| `newSingleThreadExecutor()` | Single worker thread, ordered execution |
| `newScheduledThreadPool(n)` | Fixed pool for delayed/periodic tasks |

## Task Submission

| Method | Return | Blocking | Cancellation |
|--------|--------|----------|--------------|
| `execute(Runnable)` | void | No | No |
| `submit(Callable<T>)` | Future<T> | No (on get) | Yes |
| `invokeAll(Collection)` | List<Future<T>> | Yes | Yes |
| `invokeAny(Collection)` | T | Yes | Others cancelled |

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

### Bounded Execution with Timeout
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

### Thread Pool Monitoring
```java
import java.util.concurrent.ThreadPoolExecutor;

ThreadPoolExecutor executor = (ThreadPoolExecutor) pool;
int active = executor.getActiveCount();
long completed = executor.getCompletedTaskCount();
int queued = executor.getQueue().size();
System.out.println("Active: " + active + ", Completed: " + completed + ", Queued: " + queued);
```

## Common Mistakes

1. **Unbounded queues**: Can cause OOM with fixed pools
2. **Ignoring shutdown**: Leaks threads
3. **Using `Thread.sleep()` in pools**: Wastes threads
4. **Not handling exceptions**: Swallowed in execute()
5. **Static pool creation**: Creates too many pools

## Engineering Decision Framework

### ✅ Use ExecutorService when:
- You have multiple tasks to execute concurrently
- Thread reuse across tasks improves performance
- Graceful shutdown and lifecycle management is needed
- Task queuing and scheduling is required
- You need Future-based result handling

### ❌ Avoid ExecutorService when:
- A single long-running task is sufficient (use raw Thread)
- Virtual threads better fit I/O-bound workloads
- Simple CompletableFuture composition is enough
- One-off tasks with no need for pooling

### Better Alternatives

| Alternative | When to use |
|-------------|-------------|
| Virtual Threads (Java 21+) | Massive I/O-bound concurrency |
| CompletableFuture | Async composition and chaining |
| ForkJoinPool | Recursive divide-and-conquer tasks |
| ScheduledExecutorService | Delayed or periodic task execution |
| Raw Thread | Single long-running daemon task |

### Production Examples
- Web request handling thread pools
- Background job processing queues
- Async email/notification sending
- Database connection pool management
- Scheduled health checks and monitoring

### Common Production Mistakes
- Using unbounded queues with fixed pools (risk of OOM)
- Not calling shutdown() (thread leaks)
- Ignoring RejectedExecutionException (task drops silently)
- Creating new ExecutorService per request (wastes resources)
- Using Executors.newCachedThreadPool() in production (unbounded threads)

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

## Common Myths

### ❌ Myth 1: More threads = better performance
**Reality:** Context switching cost degrades performance beyond optimal thread count. Use pool sizing formulas.

### ❌ Myth 2: Thread pools auto-size
**Reality:** Must be configured. Default pools may not match your workload characteristics.

### ❌ Myth 3: execute() and submit() are the same
**Reality:** Different error handling. execute() swallows exceptions; submit() returns Future with exception details.

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
