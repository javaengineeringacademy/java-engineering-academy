# Executor Framework Introduction - Quiz

## Multiple Choice Questions

### 1. Which interface does NOT extend Executor?
A) ExecutorService
B) ScheduledExecutorService
C) ThreadPoolExecutor
D) ScheduledThreadPoolExecutor

**Answer: C** - ThreadPoolExecutor is a class, not an interface.

### 2. What does `Executor.execute(Runnable)` return?
A) Future<Void>
B) void
C) Thread
D) CompletableFuture

**Answer: B** - execute() returns void with no result reference.

### 3. Which method stops accepting new tasks but lets running tasks finish?
A) shutdownNow()
B) close()
C) shutdown()
D) terminate()

**Answer: C** - shutdown() allows previously submitted tasks to complete.

### 4. The Executor framework was introduced in which Java version?
A) Java 1.4
B) Java 5
C) Java 7
D) Java 8

**Answer: B** - java.util.concurrent was added in Java 5 (Tiger).

### 5. What is the root interface of the Executor hierarchy?
A) Runnable
B) Thread
C) Executor
D) ExecutorService

**Answer: C** - Executor is the root; ExecutorService extends it.

## True/False

### 6. `execute(Runnable)` throws a RejectedExecutionException if the task cannot be accepted.
**Answer: True** - When the pool or queue is full and no rejection policy accepts it.

### 7. An ExecutorService can be reused after calling shutdownNow().
**Answer: False** - ExecutorService cannot be restarted once shut down.

### 8. The Executor framework manages thread creation, scheduling, and lifecycle automatically.
**Answer: True** - That is its primary purpose over manual Thread management.

## Code Output

### 9. What does this print?
```java
ExecutorService ex = Executors.newFixedThreadPool(1);
ex.execute(() -> System.out.print("A"));
ex.execute(() -> System.out.print("B"));
ex.shutdown();
```
**Answer:** `AB` - Single-thread pool executes tasks sequentially.

### 10. What is the output?
```java
ExecutorService ex = Executors.newFixedThreadPool(2);
ex.shutdownNow();
System.out.println(ex.isShutdown());
```
**Answer:** `true` - isShutdown() returns true after shutdownNow() is called.
