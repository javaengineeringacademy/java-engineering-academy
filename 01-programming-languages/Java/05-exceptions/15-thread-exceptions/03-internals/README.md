# Thread Exception Handling — Internals

## How JVM Handles Exceptions Across Threads

### Thread Isolation

Each thread in the JVM has its own execution stack. When an exception is thrown, the JVM unwinds that thread's stack only. Other threads are completely unaffected.

```
Thread 1 Stack:          Thread 2 Stack:
┌─────────────────┐     ┌─────────────────┐
│ methodC()       │     │ methodX()       │
│ methodB()       │     │ methodY()       │
│ methodA()       │     │ main()          │
│ run()           │     │                 │
│ Thread.start()  │     │                 │
└─────────────────┘     └─────────────────┘
Exception here ──────►  Unaffected
```

### Exception Propagation in Thread Lifecycle

1. Exception thrown in thread's execution
2. JVM searches for catch block in current method
3. If not found, unwinds to caller (next frame on stack)
4. Continues until catch block found or stack empty
5. If stack empty:
   - Thread terminates
   - UncaughtExceptionHandler invoked (if set)
   - Otherwise, exception printed to System.err

### Stack Walking with Exceptions

The JVM uses stack walking during exception handling to find matching catch blocks:

```java
public void methodA() {
    try {
        methodB();
    } catch (Exception e) {
        // JVM walked back to find this
    }
}

public void methodB() {
    throw new RuntimeException(); // Stack walk begins here
}
```

StackWalking API (Java 9+) allows efficient stack inspection:

```java
StackWalker walker = StackWalker.getInstance();
walker.walk(stackFrame -> {
    stackFrame.forEach(frame -> {
        System.out.println(frame.getMethodName());
    });
    return null;
});
```

### Thread Death and Exception Handling

When a thread dies due to an uncaught exception:

1. Thread state transitions to TERMINATED
2. Thread object still exists (can query isAlive(), getState())
3. Thread cannot be restarted
4. ThreadLocal variables become eligible for GC
5. Any monitors held by the thread are released (in finally blocks or implicitly)

### FutureTask Exception Capture

When using `submit()`, the task is wrapped in FutureTask:

```
submit(Callable) → FutureTask → Worker thread executes FutureTask.run()
                                      │
                                      ├─ If exception: FutureTask stores it
                                      │  future.get() throws ExecutionException
                                      │
                                      └─ If success: FutureTask stores result
                                         future.get() returns result
```

FutureTask uses `java.util.concurrent.atomic.AtomicReference` to store outcome (result or exception).

### CompletableFuture Internals

CompletableFuture uses a `Stack<Node>` for dependent stages. When an exception occurs:

1. Exception stored in outcome field
2. Dependent nodes notified
3. Exceptional completion triggers dependent stages to handle or propagate
4. Stack-based notification ensures proper ordering

### Virtual Thread Internals (Java 21)

Virtual threads are scheduled by the JVM onto platform threads. Exception handling:

- Same API as platform threads
- UncaughtExceptionHandler works identically
- Continuation-based scheduling means stack frames are heap-allocated
- Mounting/unmounting virtual threads during I/O doesn't affect exception handling
- StructuredTaskScope provides structured concurrency with exception propagation

### Thread Death Hook

JVM provides no built-in thread death hook. To detect thread death:

1. Use UncaughtExceptionHandler (most common)
2. Monitor thread state periodically
3. Use `Thread.isAlive()` checks
4. Framework-specific mechanisms (e.g., ExecutorService tracking)

### Native Methods and Exceptions

Exceptions in native methods propagate through JNI boundary:

1. Native method throws exception
2. JVM unwinds through native code
3. Exception crosses JNI boundary
4. Continues unwinding in Java code
5. May require special handling in native code (JNI exception handling)
