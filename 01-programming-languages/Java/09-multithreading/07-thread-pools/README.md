# Thread Pool Configuration Guide

## Pool Types Overview

| Pool Type | Best For | Queue | Risk |
|-----------|----------|-------|------|
| Fixed | CPU-bound | Unbounded | OOM |
| Cached | Short IO-bound | None | Thread explosion |
| Single | Sequential tasks | Unbounded | Bottleneck |
| Scheduled | Periodic/delayed | DelayedWorkQueue | Starvation |
| ForkJoin | Recursive tasks | WorkStealingQueue | Stack overflow |

## Sizing Guidelines

### CPU-Bound Tasks
```
poolSize = numCPUcores + 1
```
- One extra thread compensates for OS scheduling overhead
- More threads cause context switching overhead

### IO-Bound Tasks
```
poolSize = numCPUcores * (1 + waitTime / computeTime)
```
Example calculations:
- 4 cores, 80% wait time: `4 * (1 + 4) = 20 threads`
- 8 cores, 50% wait time: `8 * (1 + 1) = 16 threads`

### Web Application (Mixed)
```
poolSize = numCPUcores * targetUtilization * (1 + waitTime / computeTime)
```
- targetUtilization: 0.7-0.9 (leave headroom for spikes)

## Monitoring Key Metrics

### ThreadPoolExecutor Metrics
```java
ThreadPoolExecutor pool = ...;
pool.getActiveCount();      // Currently executing tasks
pool.getPoolSize();         // Current thread count
pool.getQueue().size();     // Pending tasks in queue
pool.getCompletedTaskCount(); // Completed tasks
pool.getLargestPoolSize();  // Peak thread count
pool.getTaskCount();        // Total submitted tasks
```

### Health Indicators
- **Queue size growing**: Pool is saturated, increase threads or optimize tasks
- **Active count at max**: Pool cannot keep up
- **Completed count low**: Tasks are slow or blocked

## Queue Selection

| Queue Type | Behavior | Use Case |
|------------|----------|----------|
| LinkedBlockingQueue | Unbounded, FIFO | General purpose |
| ArrayBlockingQueue | Bounded, FIFO | Prevent OOM |
| SynchronousQueue | No capacity, direct handoff | Cached pool |
| PriorityBlockingQueue | Priority ordering | Priority tasks |
| DelayedWorkQueue | Delayed execution | Scheduled pool |

## Common Mistakes

### 1. Unbounded Queue with Fixed Pool
```java
// BAD: Can accumulate millions of tasks
ExecutorService pool = Executors.newFixedThreadPool(10);

// GOOD: Bounded queue with rejection handler
new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<>(1000),
    new ThreadPoolExecutor.CallerRunsPolicy());
```

### 2. Creating Too Many Pools
```java
// BAD: Each request creates a new pool
ExecutorService pool = Executors.newFixedThreadPool(10);

// GOOD: Use shared singleton pool
private static final ExecutorService SHARED_POOL =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
```

### 3. Not Handling Rejections
```java
// Tasks silently dropped
new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
    new SynchronousQueue<>(), new ThreadPoolExecutor.DiscardPolicy());

// GOOD: Log and handle rejections
new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
    new SynchronousQueue<>(), (r, executor) -> {
        log.warn("Task rejected: " + r.toString());
        // Optionally retry or notify
    });
```

### 4. Blocking in Pool Tasks
```java
// BAD: Wastes pool thread on blocking I/O
pool.submit(() -> {
    socket.getInputStream().read(); // Blocks thread
});

// GOOD: Use async I/O or dedicated blocking pool
pool.submit(() -> {
    CompletableFuture.supplyAsync(() -> readFromSocket(), blockingPool);
});
```

## Production Recommendations

1. **Always use bounded queues** to prevent OOM
2. **Set meaningful thread names** for debugging
3. **Implement rejection handlers** with logging
4. **Monitor queue growth** as early warning
5. **Use separate pools** for different workload types
6. **Test under load** to find optimal sizing
7. **Set timeouts** on Future.get() calls
