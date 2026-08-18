# Thread Pools - Quiz

## Multiple Choice Questions

### 1. Which thread pool reuses threads and has a maximum size?
A) newCachedThreadPool
B) newFixedThreadPool
C) newSingleThreadExecutor
D) ForkJoinPool

**Answer: B** - FixedThreadPool has a fixed core and max size.

### 2. What happens to idle threads in a CachedThreadPool?
A) They remain alive indefinitely
B) They are killed after 60 seconds
C) They are paused and resumed
D) They are garbage collected

**Answer: B** - Default keepAliveTime is 60 seconds for idle threads.

### 3. Which pool type is best for periodic task execution?
A) FixedThreadPool
B) CachedThreadPool
C) ScheduledThreadPool
D) SingleThreadExecutor

**Answer: C** - ScheduledThreadPool supports scheduleAtFixedRate and scheduleWithFixedDelay.

### 4. What does `pool.getQueue().size()` return?
A) Maximum queue capacity
B) Number of completed tasks
C) Number of tasks waiting to execute
D) Number of active threads

**Answer: C** - Returns the number of tasks currently queued.

### 5. Which parameter controls how long idle threads survive?
A) corePoolSize
B) maximumPoolSize
C) keepAliveTime
D) workQueue

**Answer: C** - keepAliveTime determines idle thread lifetime.

## True/False

### 6. A CachedThreadPool can cause OutOfMemoryError under sustained high load.
**Answer: True** - It creates unlimited threads, each consuming stack memory.

### 7. A FixedThreadPool with queue size Integer.MAX_VALUE can cause memory exhaustion before rejection.
**Answer: True** - Unbounded queues grow without limit until memory is exhausted.

### 8. `getLargestPoolSize()` returns the maximum number of threads that have been in the pool simultaneously.
**Answer: True** - It tracks the high-water mark of concurrent threads.

## Code Output

### 9. What does this print?
```java
ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
pool.submit(() -> {});
pool.submit(() -> {});
System.out.println(pool.getPoolSize());
pool.shutdown();
```
**Answer:** `2` - Both core threads are created because tasks were submitted.

### 10. What is the output?
```java
ThreadPoolExecutor pool = new ThreadPoolExecutor(
    2, 4, 0L, TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<>(2)
);
pool.submit(() -> { Thread.sleep(100); });
pool.submit(() -> { Thread.sleep(100); });
pool.submit(() -> { Thread.sleep(100); });
System.out.println(pool.getPoolSize());
pool.shutdown();
```
**Answer:** `3` - Two core threads run, third task spawns a new thread since queue has capacity 2 but is already accepting.
