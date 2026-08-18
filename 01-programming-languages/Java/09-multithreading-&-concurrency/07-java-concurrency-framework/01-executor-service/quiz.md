# ExecutorService - Quiz

## Multiple Choice Questions

### 1. Which ExecutorService creation method creates an unbounded thread pool?
A) newFixedThreadPool(4)
B) newCachedThreadPool()
C) newSingleThreadExecutor()
D) newScheduledThreadPool(2)

**Answer: B** - newCachedThreadPool creates threads on demand with no upper bound.

### 2. What happens when you call `submit()` on a shutdown ExecutorService?
A) Returns null
B) Throws IllegalStateException
C) Throws RejectedExecutionException
D) Task still executes

**Answer: C** - RejectedExecutionException is thrown after shutdown.

### 3. Which rejection policy runs the task in the submitting thread?
A) AbortPolicy
B) DiscardPolicy
C) DiscardOldestPolicy
D) CallerRunsPolicy

**Answer: D** - CallerRunsPolicy executes the task in the caller's thread as backpressure.

### 4. What is the default rejection policy of ThreadPoolExecutor?
A) CallerRunsPolicy
B) DiscardPolicy
C) AbortPolicy
D) DiscardOldestPolicy

**Answer: C** - AbortPolicy throws RejectedExecutionException.

### 5. `awaitTermination(timeout, unit)` returns what?
A) void
B) List of remaining tasks
C) boolean indicating if all tasks finished
D) Count of completed tasks

**Answer: C** - Returns true if all tasks completed before timeout.

## True/False

### 6. You can call `execute()` and `submit()` after calling `shutdown()`.
**Answer: False** - Both throw RejectedExecutionException after shutdown.

### 7. `shutdownNow()` attempts to interrupt currently running tasks via Thread.interrupt().
**Answer: True** - It calls Thread.interrupt() on running worker threads.

### 8. A CachedThreadPool will create unlimited threads if tasks are submitted faster than they complete.
**Answer: False** - It creates threads on demand but idle threads are reclaimed after 60 seconds.

## Code Output

### 9. What is the output?
```java
ExecutorService ex = Executors.newFixedThreadPool(2);
Future<String> f = ex.submit(() -> "Hello");
System.out.println(f.get());
ex.shutdown();
```
**Answer:** `Hello` - submit() returns a Future; get() blocks and returns the result.

### 10. What happens at runtime?
```java
ExecutorService ex = Executors.newFixedThreadPool(1);
ex.shutdown();
ex.submit(() -> System.out.print("X"));
```
**Answer:** `RejectedExecutionException` - submit() after shutdown() throws.
