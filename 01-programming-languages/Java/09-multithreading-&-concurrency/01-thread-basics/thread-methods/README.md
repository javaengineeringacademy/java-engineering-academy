# Thread Methods - Complete Guide

## Overview

The `Thread` class in Java provides methods to control thread behavior, manage lifecycle, and handle concurrency. This guide covers ALL Thread methods.

## Thread Lifecycle & Methods

```
NEW → RUNNABLE → BLOCKED/WAITING/TIMED_WAITING → TERMINATED
 ↑ start()    ↑ wait/join/sleep        ↑ run() completes
```

## Core Thread Control Methods

### 1. `start()` - Launch Thread

```java
Thread t = new Thread(() -> System.out.println("Running"));
t.start();  // Creates new thread, calls run() in new thread
// t.start();  // ERROR: Thread already started
```

**Key Points:**
- Creates a new OS thread
- Calls `run()` in the NEW thread
- Cannot call `start()` twice on same thread
- Throws `IllegalThreadStateException` if called again

### 2. `run()` - Thread Task

```java
Thread t = new Thread(() -> {
    System.out.println("Task executed");
});
t.start();  // Calls run() in NEW thread
// t.run();  // WRONG: Executes in CURRENT thread, no new thread created
```

**Key Points:**
- Contains the code to be executed
- Calling `run()` directly does NOT create a new thread
- Should not be called directly (use `start()` instead)

### 3. `yield()` - Thread Scheduler Hint

```java
Thread.yield();  // Hint to scheduler: give other threads a chance
```

**Key Points:**
- **NOT a guarantee** - scheduler may ignore
- Suggests current thread pauses to let others run
- No guarantee of which thread executes next
- Useful for fairness in CPU-bound tasks

### 4. `join()` - Wait for Thread Completion

```java
Thread t = new Thread(() -> { /* work */ });
t.start();
t.join();  // Current thread waits until t completes
System.out.println("t finished");
```

### 5. `join(timeout)` - Wait with Timeout

```java
t.join(5000);  // Wait up to 5 seconds
if (t.isAlive()) {
    System.out.println("Thread still running");
}
```

**Key Points:**
- Throws `InterruptedException` if interrupted while waiting
- Returns immediately if thread already terminated
- Use `isAlive()` to check if completed or timed out

### 6. `sleep(ms)` - Pause Thread

```java
Thread.sleep(1000);  // Pause for 1 second
Thread.sleep(1000, 500000);  // 1 second + 500 microseconds
```

**Key Points:**
- Throws `InterruptedException`
- Does NOT release monitors/locks
- Yields CPU to other threads
- Precision varies by OS

## Interrupt Methods

### 7. `interrupt()` - Request Thread Stop

```java
t.interrupt();  // Sets interrupt flag (or wakes if sleeping/waiting)
```

### 8. `interrupted()` - Check & CLEAR Interrupt Status

```java
if (Thread.interrupted()) {
    System.out.println("Interrupted! Status cleared");
}
System.out.println(Thread.interrupted());  // false (already cleared)
```

**Key Points:**
- Returns interrupt status
- **CLEARS the flag** after reading
- Static method

### 9. `isInterrupted()` - Check WITHOUT Clearing

```java
if (t.isInterrupted()) {
    System.out.println("Interrupted! Flag still set");
}
if (t.isInterrupted()) {
    System.out.println("Still interrupted");  // true again
}
```

**Key Points:**
- Returns interrupt status
- Does NOT clear the flag
- Instance method

## Thread Information Methods

### 10. `isAlive()` - Check Thread Status

```java
t.start();
if (t.isAlive()) {
    System.out.println("Thread is running");
}
```

### 11. `setDaemon(boolean)` - Daemon Thread

```java
Thread daemon = new Thread(() -> {
    while (true) { /* background work */ }
});
daemon.setDaemon(true);  // Must be called BEFORE start()
daemon.start();
// JVM exits when only daemon threads remain
```

**Key Points:**
- Must call before `start()`
- Daemon threads don't prevent JVM shutdown
- Common for background tasks (GC, cleanup)

### 12. `isDaemon()` - Check Daemon Status

```java
if (t.isDaemon()) {
    System.out.println("Daemon thread");
}
```

### 13. `setPriority(int)` / `getPriority()`

```java
t.setPriority(Thread.MAX_PRIORITY);  // 10
t.setPriority(Thread.MIN_PRIORITY);  // 1
t.setPriority(Thread.NORM_PRIORITY); // 5 (default)

int p = t.getPriority();
```

**Key Points:**
- Range: 1 (MIN) to 10 (MAX), default 5
- Actual scheduling depends on OS
- Don't rely on priorities for correctness

### 14. `currentThread()` - Get Current Thread

```java
Thread main = Thread.currentThread();
System.out.println(main.getName());  // "main"
```

### 15. `getId()` - Get Thread ID

```java
long id = Thread.currentThread().getId();
System.out.println("Thread ID: " + id);
```

### 16. `getName()` / `setName()` - Thread Name

```java
t.setName("Worker-1");
System.out.println(t.getName());  // "Worker-1"

// Constructor
Thread t2 = new Thread(() -> {}, "MyThread");
```

### 17. `getState()` - Thread State

```java
Thread.State state = t.getState();
switch (state) {
    case NEW:            // Created, not started
    case RUNNABLE:       // Running or ready to run
    case BLOCKED:        // Waiting for monitor lock
    case WAITING:        // Waiting indefinitely (wait(), join())
    case TIMED_WAITING:  // Waiting with timeout (sleep(), wait(timeout))
    case TERMINATED:     // Completed execution
}
```

### 18. `holdsLock(object)` - Check Lock Ownership

```java
Object lock = new Object();
synchronized (lock) {
    if (Thread.holdsLock(lock)) {
        System.out.println("Current thread holds lock");
    }
}
```

### 19. `dumpStack()` - Print Stack Trace

```java
Thread.dumpStack();  // Prints current thread's stack to stderr
```

### 20. `enumerate(Thread[])` - List Active Threads

```java
Thread[] threads = new Thread[Thread.activeCount()];
int count = Thread.enumerate(threads);
for (int i = 0; i < count; i++) {
    System.out.println(threads[i].getName());
}
```

## Quick Reference Table

| Method | Description | Throws |
|--------|-------------|--------|
| `start()` | Create thread, call run() | `IllegalThreadStateException` |
| `run()` | Task code (don't call directly) | - |
| `yield()` | Hint to scheduler | - |
| `join()` | Wait for thread to finish | `InterruptedException` |
| `sleep(ms)` | Pause thread | `InterruptedException` |
| `interrupt()` | Request stop | - |
| `interrupted()` | Check & clear flag | - |
| `isInterrupted()` | Check flag only | - |
| `isAlive()` | Is thread running? | - |
| `setDaemon(bool)` | Set daemon status | `IllegalThreadStateException` |
| `setPriority(int)` | Set priority (1-10) | `IllegalArgumentException` |
| `getState()` | Get thread state | - |
| `holdsLock(obj)` | Check lock ownership | - |
| `dumpStack()` | Print stack trace | - |
| `enumerate(Thread[])` | List active threads | - |

## Best Practices

1. **Always use `start()` not `run()`** to create real threads
2. **Handle `InterruptedException`** properly - don't swallow it
3. **Don't rely on `yield()`** - it's just a hint
4. **Use daemon threads** only for background tasks
5. **Don't use `stop()`** (deprecated) - use interrupts instead
6. **Check `Thread.interrupted()`** in loops to handle interrupts
7. **Use `join(timeout)`** to avoid infinite waits
8. **Don't depend on priorities** for correctness

## Files in This Module

- `StartVsRun.java` - Critical difference between start() and run()
- `YieldExample.java` - yield() behavior
- `JoinExample.java` - join() usage patterns
- `SleepExample.java` - sleep() and lock behavior
- `InterruptExample.java` - interrupt handling
- `ThreadStateExample.java` - Thread state transitions
- `practices/Practices.java` - 5 exercises
- `solutions/Solutions.java` - Complete solutions
