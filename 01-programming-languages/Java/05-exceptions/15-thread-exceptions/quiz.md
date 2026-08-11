# Thread Exception Handling — Quiz

## Multiple Choice

### Q1. What happens when an exception is thrown in a thread created with `execute()` on an ExecutorService?
A) Exception propagates to the calling thread  
B) Exception is wrapped in a Future  
C) Exception propagates to UncaughtExceptionHandler  
D) Exception is silently lost  

**Answer: C**

### Q2. Which method returns a Future that captures exceptions from a task?
A) `execute()`  
B) `submit()`  
C) `invokeAll()`  
D) Both B and C  

**Answer: D**

### Q3. What is the resolution order for UncaughtExceptionHandler?
A) ThreadGroup → Thread handler → System default  
B) Thread handler → ThreadGroup → System default  
C) System default → Thread handler → ThreadGroup  
D) System default → ThreadGroup → Thread handler  

**Answer: B**

### Q4. Which CompletableFuture method provides a fallback value when an exception occurs?
A) `handle()`  
B) `whenComplete()`  
C) `exceptionally()`  
D) `thenApply()`  

**Answer: C**

### Q5. How do you set a default UncaughtExceptionHandler for all threads?
A) `Thread.setUncaughtExceptionHandler()`  
B) `Thread.setDefaultUncaughtExceptionHandler()`  
C) `ThreadGroup.setUncaughtExceptionHandler()`  
D) `ExecutorService.setUncaughtExceptionHandler()`  

**Answer: B**

## True/False

### Q6. Exceptions in one thread automatically propagate to other threads.
**Answer: False** — Each thread has its own stack; exceptions don't cross boundaries.

### Q7. `Future.get()` without a timeout can block the calling thread forever.
**Answer: True**

### Q8. `exceptionally()` can change the return type of a CompletableFuture.
**Answer: True** — It can return any type, changing the pipeline.

### Q9. Virtual threads require different exception handling than platform threads.
**Answer: False** — The API is the same; differences are in scale and performance.

### Q10. In a thread pool, `submit()` exceptions are always lost.
**Answer: False** — They are captured in the Future and can be retrieved with `get()`.

## Short Answer

### Q11. Why does `execute()` propagate to UncaughtExceptionHandler while `submit()` doesn't?
**Answer:** `execute()` passes the Runnable directly to the thread, so exceptions propagate normally. `submit()` wraps the task in a FutureTask, which captures exceptions in the Future object, preventing them from reaching the handler.

### Q12. What happens if you call `Future.get()` on a cancelled task?
**Answer:** It throws `CancellationException`, which is an unchecked exception (extends IllegalStateException).

### Q13. How should you handle exceptions in a `CompletableFuture` chain with multiple stages?
**Answer:** Place `exceptionally()` or `handle()` at the end of the chain to catch exceptions from any stage. Exceptions propagate through stages until handled.

### Q14. What is the purpose of `setDefaultUncaughtExceptionHandler`?
**Answer:** It sets a global handler for all threads that don't have their own handler, providing a safety net for uncaught exceptions across the application.

### Q15. How do you test exception handling in thread pools?
**Answer:** Submit tasks that throw exceptions, verify Future.get() throws ExecutionException, check UncaughtExceptionHandler is called for execute() tasks, and verify monitoring/alerting systems receive the exceptions.
