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
ExecutorService pool = Executors.newFixedThreadPool(n);
try {
    // submit tasks
} finally {
    pool.shutdown();
    if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
        pool.shutdownNow();
    }
}
```

### Bounded Execution with Timeout
```java
Future<Result> future = pool.submit(task);
try {
    Result result = future.get(5, TimeUnit.SECONDS);
} catch (TimeoutException e) {
    future.cancel(true);
}
```

### Thread Pool Monitoring
```java
ThreadPoolExecutor executor = (ThreadPoolExecutor) pool;
int active = executor.getActiveCount();
long completed = executor.getCompletedTaskCount();
int queued = executor.getQueue().size();
```

## Common Mistakes

1. **Unbounded queues**: Can cause OOM with fixed pools
2. **Ignoring shutdown**: Leaks threads
3. **Using `Thread.sleep()` in pools**: Wastes threads
4. **Not handling exceptions**: Swallowed in execute()
5. **Static pool creation**: Creates too many pools

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
