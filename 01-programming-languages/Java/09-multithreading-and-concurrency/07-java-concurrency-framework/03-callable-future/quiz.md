# Callable and Future - Quiz

## Multiple Choice Questions

### 1. What does `Callable.call()` return?
A) void
B) Object
C)泛型 V
D) Optional

**Answer: C** - Callable<V> returns a value of type V.

### 2. Which exception does `Future.get()` throw if the task failed?
A) ExecutionException
B) InterruptedException
C) CancellationException
D) TimeoutException

**Answer: A** - ExecutionException wraps the exception thrown by the task.

### 3. What does `Future.cancel(true)` do to a sleeping thread?
A) Nothing
B) Sets interrupt flag, waking the thread
C) Forcefully kills the thread
D) Pauses the thread

**Answer: B** - cancel(true) calls Thread.interrupt() which breaks out of sleep/wait.

### 4. Can you submit a Runnable to an ExecutorService?
A) No, only Callable
B) Yes, it is wrapped as a Callable<Void>
C) Yes, but it returns null
D) Yes, but it throws an exception

**Answer: B** - ExecutorService wraps Runnable as a Callable returning null.

### 5. What does `isDone()` return before the task starts?
A) true
B) false
C) Throws IllegalStateException
D) Depends on the executor

**Answer: B** - isDone() returns false until the task completes, fails, or is cancelled.

## True/False

### 6. A Future can only be obtained by submitting a Callable, not a Runnable.
**Answer: False** - submit(Runnable) also returns a Future<Void>.

### 7. Future.get() without timeout can block forever if the task never completes.
**Answer: True** - The calling thread blocks indefinitely without a timeout.

### 8. Calling cancel(false) on an already-running non-interruptible task has no effect.
**Answer: True** - cancel(false) does not interrupt; the task runs to completion.

## Code Output

### 9. What does this print?
```java
ExecutorService ex = Executors.newFixedThreadPool(1);
Future<Integer> f = ex.submit(() -> 42);
System.out.println(f.get());
ex.shutdown();
```
**Answer:** `42` - Callable returns 42; Future.get() retrieves it.

### 10. What is the output?
```java
ExecutorService ex = Executors.newFixedThreadPool(1);
Future<String> f = ex.submit(() -> { throw new RuntimeException("fail"); });
try {
    f.get();
} catch (Exception e) {
    System.out.println(e.getClass().getSimpleName());
}
ex.shutdown();
```
**Answer:** `ExecutionException` - The task exception is wrapped in ExecutionException.
