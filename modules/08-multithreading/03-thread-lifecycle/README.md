# Thread Lifecycle

## 1. Introduction

Every thread in Java goes through a well-defined lifecycle from creation to termination. Understanding thread states and transitions is critical for writing correct concurrent code. A thread can be in one of six states defined by the `Thread.State` enum, and understanding when and why threads transition between these states helps diagnose issues like deadlocks, starvation, and contention.

The thread lifecycle is managed by the Java Virtual Machine (JVM) in cooperation with the operating system's thread scheduler. Each state transition is triggered by specific operations—some initiated by the programmer, others by the JVM or OS.

This topic covers every thread state, the operations that cause transitions, and how to monitor and debug thread lifecycle issues.

## 2. Learning Objectives

- Understand all six thread states in Java
- Learn what operations cause transitions between states
- Understand the difference between BLOCKED and WAITING states
- Learn how to monitor thread states programmatically
- Understand thread interruption and its effect on lifecycle
- Know how to detect and diagnose thread lifecycle issues
- Understand daemon vs non-daemon thread lifecycle differences

## 3. Prerequisites

- Module 08: Introduction to Multithreading
- Module 08: Thread Creation
- Understanding of Java enums
- Basic knowledge of synchronization (helpful but not required)

## 4. Why This Concept Exists

Without understanding thread lifecycle, developers frequently encounter:
- **Deadlocks**: Threads permanently stuck in BLOCKED state
- **Starvation**: Threads never reaching RUNNABLE state
- **Resource leaks**: Threads stuck in WAITING state, holding resources
- **Race conditions**: Threads interleaving unexpectedly

By understanding the lifecycle, you can:
- Predict thread behavior under different conditions
- Diagnose production issues using thread dumps
- Write code that handles thread state transitions gracefully
- Implement proper shutdown and cleanup mechanisms

## 5. Problem Statement

Consider a web application with a thread pool handling requests. If the database becomes slow:
1. Worker threads execute SQL queries (RUNNABLE → TIMED_WAITING on I/O)
2. Thread pool queue fills up with waiting requests
3. New requests cannot be processed (rejected or queued)
4. Application becomes unresponsive

Without lifecycle awareness, you might:
- Not detect that threads are stuck in WAITING state
- Fail to implement timeouts on database queries
- Not have a mechanism to interrupt stuck threads
- Have no monitoring to detect the problem

Understanding thread lifecycle enables you to:
- Set appropriate timeouts
- Implement thread interruption for cancellation
- Monitor thread states to detect problems early
- Design graceful degradation strategies

## 6. Theory

### The Six Thread States

```
                    ┌──────────────┐
                    │    NEW       │
                    └──────┬───────┘
                           │ start()
                           ▼
                    ┌──────────────┐
           ┌───────│  RUNNABLE    │───────┐
           │       └──────┬───────┘       │
           │              │               │
     lock acquired  │   Thread.yield()  │ scheduler
           │              │               │ preempts
           ▼              ▼               │
    ┌──────────────┐           │          │
    │   BLOCKED    │           │          │
    └──────────────┘           │          │
                               │          │
                    ┌──────────┴──────────┴──┐
                    │     RUNNABLE           │
                    └──────────┬──────────────┘
                               │
              ┌────────────────┼────────────────┐
              │                │                │
              ▼                ▼                ▼
     ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
     │   WAITING    │ │TIMED_WAITING │ │  TERMINATED  │
     └──────────────┘ └──────────────┘ └──────────────┘
              │                │
              │ notify()/      │ timeout/
              │ notifyAll()    │ interrupt
              │                │
              ▼                ▼
     ┌──────────────┐
     │  RUNNABLE    │
     └──────────────┘
```

### State Descriptions

| State | Description | Thread is executing? |
|-------|-------------|---------------------|
| NEW | Created but not started | No |
| RUNNABLE | Executing or ready to execute | Yes (or ready) |
| BLOCKED | Waiting to acquire a monitor lock | No |
| WAITING | Waiting indefinitely for another thread | No |
| TIMED_WAITING | Waiting for a specified time | No |
| TERMINATED | Completed execution | No |

### Operations That Cause Transitions

| From | To | Trigger |
|------|----|---------|
| NEW | RUNNABLE | `thread.start()` |
| RUNNABLE | BLOCKED | Attempting to enter synchronized block held by another thread |
| BLOCKED | RUNNABLE | Lock becomes available and thread acquires it |
| RUNNABLE | WAITING | `Object.wait()`, `Thread.join()`, `LockSupport.park()` |
| RUNNABLE | TIMED_WAITING | `Thread.sleep(ms)`, `Object.wait(ms)`, `Thread.join(ms)` |
| WAITING | RUNNABLE | `Object.notify()`, `Object.notifyAll()`, thread being joined completes |
| TIMED_WAITING | RUNNABLE | Timeout expires, `notify()`/`notifyAll()`, `interrupt()` |
| RUNNABLE | TERMINATED | `run()` method completes (normally or via exception) |
| Any | RUNNABLE | `thread.interrupt()` (for WAITING/TIMED_WAITING states) |

### Daemon vs Non-Daemon Threads

- **Non-daemon threads**: The JVM waits for all non-daemon threads to terminate before shutting down.
- **Daemon threads**: The JVM does not wait for daemon threads. When all non-daemon threads finish, daemon threads are killed.

```java
Thread daemon = new Thread(() -> {
    while (true) {
        try {
            Thread.sleep(1000);
            System.out.println("Daemon alive");
        } catch (InterruptedException e) {
            return;
        }
    }
});
daemon.setDaemon(true);
daemon.start();
// When main thread ends, daemon is killed automatically
```

## 7. Internal Working

### Thread State Machine in the JVM

The JVM maintains thread state using native OS thread states and its own bookkeeping:

```
Java Thread.State    ←→    OS Thread State
─────────────────────────────────────────
NEW                   →    (not created yet)
RUNNABLE              →    RUNNING / READY
BLOCKED              →    WAITING (on lock)
WAITING              →    WAITING (parked)
TIMED_WAITING        →    TIMED_WAITING
TERMINATED           →    TERMINATED
```

### Monitor Lock Mechanism

When a thread enters a `synchronized` block:
1. JVM checks if the monitor is available
2. If available: thread owns the monitor, continues execution
3. If not available: thread is placed in the BLOCKED state's entry set
4. When the monitor is released, one blocked thread is selected (JVM implementation-dependent)

### Wait/Notify Mechanism

```
Thread A (producer):          Thread B (consumer):
synchronized(lock) {          synchronized(lock) {
  // produce                    while (!ready)
  ready = true;                  lock.wait(); // TIMED_WAITING
  lock.notify();               // consume
}                               }
                                }
```

1. Thread B calls `lock.wait()` → releases monitor, enters WAITING state
2. Thread A calls `lock.notify()` → selects one waiting thread to wake up
3. Woken thread re-acquires monitor and continues from `wait()` call

## 8. JVM Perspective

### Thread State Storage

The JVM stores thread state in the `java.lang.Thread` object:

```java
// Thread.java (simplified)
private volatile ThreadState threadState;
private volatile boolean interrupted;

public enum State {
    NEW,
    RUNNABLE,
    BLOCKED,
    WAITING,
    TIMED_WAITING,
    TERMINATED
}
```

### Context Switching

When the OS scheduler switches threads:
1. Current thread's CPU registers are saved to its stack
2. New thread's registers are restored from its stack
3. Program counter is updated to new thread's position
4. TLB (Translation Lookaside Buffer) may be flushed
5. Cache may be partially invalidated

Cost: ~1-10 microseconds per context switch.

### Thread Termination Internals

When a thread's `run()` method completes:
1. The thread's stack is unwound
2. Local variables become eligible for GC
3. The thread is removed from the JVM's thread list
4. The OS thread is destroyed
5. The Thread object transitions to TERMINATED state
6. The Thread object becomes eligible for GC (unless referenced)

## 9. Memory Representation

### Thread State Transitions in Memory

```
Thread Object (heap):
┌─────────────────────────────┐
│ threadState: RUNNABLE       │ ← volatile field
│ interrupted: false          │ ← volatile field
│ target: Runnable ref        │
│ name: "worker-1"            │
│ ...                         │
└─────────────────────────────┘

Monitor (object header):
┌─────────────────────────────┐
│ Mark word: lock record ptr  │ ← Points to lock record when locked
│ owning thread: Thread-2     │ ← Which thread owns the monitor
│ entry set: [Thread-3,       │ ← Threads BLOCKED on this monitor
│             Thread-4]       │
│ wait set: [Thread-5,        │ ← Threads WAITING on this monitor
│            Thread-6]        │
└─────────────────────────────┘
```

### Stack Frame Changes During State Transitions

```
RUNNABLE → WAITING (Object.wait()):
┌───────────────────────┐
│ Wait for notify        │ ← Special stack frame
│ (releases monitor)     │
├───────────────────────┤
│ Previous frame         │
└───────────────────────┘

WAITING → RUNNABLE (notify received):
┌───────────────────────┐
│ (monitor re-acquired)  │ ← Stack frame removed
├───────────────────────┤
│ Previous frame         │ ← Execution continues here
└───────────────────────┘
```

## 10. Syntax

```java
// ============================================
// CHECKING THREAD STATE
// ============================================
Thread t = new Thread(() -> { /* work */ });
Thread.State state = t.getState(); // State.NEW

t.start();
state = t.getState(); // State.RUNNABLE

// ============================================
// THREAD LIFECYCLE OPERATIONS
// ============================================

// Starting a thread
t.start(); // NEW → RUNNABLE

// Sleeping (TIMED_WAITING)
Thread.sleep(1000); // RUNNABLE → TIMED_WAITING → RUNNABLE

// Waiting (WAITING)
synchronized (lock) {
    lock.wait(); // RUNNABLE → WAITING
}
// Woken by: lock.notify() or lock.notifyAll()

// Timed waiting
synchronized (lock) {
    lock.wait(5000); // RUNNABLE → TIMED_WAITING → RUNNABLE (after 5s or notify)
}

// Joining (WAITING or TIMED_WAITING)
t.join();     // RUNNABLE → WAITING (until t completes)
t.join(5000); // RUNNABLE → TIMED_WAITING (until t completes or 5s)

// Blocking (BLOCKED)
synchronized (sharedObject) {
    // If another thread holds lock on sharedObject:
    // RUNNABLE → BLOCKED → RUNNABLE (when lock acquired)
}

// Interrupting
t.interrupt(); // Interrupts t if in WAITING/TIMED_WAITING/BLOCKED

// Yielding (hint to scheduler)
Thread.yield(); // RUNNABLE → RUNNABLE (hint only)

// ============================================
// MONITORING THREAD STATES
// ============================================
Thread.getAllStackTraces().forEach((thread, stackTrace) -> {
    System.out.println(thread.getName() + ": " + thread.getState());
});

// ============================================
// THREAD TERMINATION
// ============================================
// Normal: run() method completes
// Abnormal: Uncaught exception in run()
// Interrupted: InterruptedException in wait/sleep/join
// Note: stop(), suspend(), resume() are deprecated and unsafe
```

## 11. Easy Example

```java
public class ThreadLifecycleDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try {
                System.out.println("Worker: RUNNING, sleeping...");
                Thread.sleep(1000);
                System.out.println("Worker: Woke up, doing work...");
                for (int i = 0; i < 5; i++) {
                    System.out.println("Worker: step " + i);
                    Thread.sleep(200);
                }
                System.out.println("Worker: DONE");
            } catch (InterruptedException e) {
                System.out.println("Worker: INTERRUPTED");
            }
        });

        // State: NEW
        System.out.println("1. State: " + worker.getState()); // NEW

        worker.start();
        System.out.println("2. State: " + worker.getState()); // RUNNABLE

        Thread.sleep(500);
        System.out.println("3. State: " + worker.getState()); // TIMED_WAITING (sleeping)

        worker.join();
        System.out.println("4. State: " + worker.getState()); // TERMINATED
    }
}
```

## 12. Medium Example

```java
import java.util.concurrent.locks.ReentrantLock;

public class ThreadStateTransitions {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Object monitor = new Object();

    public static void main(String[] args) throws InterruptedException {
        // Demonstrate BLOCKED state
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("T1: Holding lock, sleeping...");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }, "Thread-1");

        Thread t2 = new Thread(() -> {
            System.out.println("T2: Waiting for lock (BLOCKED)...");
            lock.lock(); // Will block until T1 releases
            try {
                System.out.println("T2: Acquired lock!");
            } finally {
                lock.unlock();
            }
        }, "Thread-2");

        t1.start();
        Thread.sleep(100); // Let t1 acquire lock first
        t2.start();

        Thread.sleep(500);
        System.out.println("T1 state: " + t1.getState()); // TIMED_WAITING
        System.out.println("T2 state: " + t2.getState()); // BLOCKED

        t1.join();
        t2.join();
        System.out.println("Both threads completed");

        // Demonstrate WAITING state
        System.out.println("\n--- WAITING Demo ---");
        Thread waiter = new Thread(() -> {
            synchronized (monitor) {
                try {
                    System.out.println("Waiter: Waiting...");
                    monitor.wait(); // WAITING
                    System.out.println("Waiter: Notified!");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Waiter");

        Thread notifier = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (monitor) {
                System.out.println("Notifier: Notifying...");
                monitor.notify();
            }
        }, "Notifier");

        waiter.start();
        notifier.start();

        Thread.sleep(200);
        System.out.println("Waiter state: " + waiter.getState()); // WAITING

        waiter.join();
        notifier.join();
    }
}
```

## 13. Hard Example

```java
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class AdvancedLifecycleManagement {
    private static final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static final Condition dataAvailable = rwLock.writeLock().newCondition();
    private static volatile boolean dataReady = false;
    private static String sharedData = "";

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== ReadWriteLock Lifecycle ===");

        // Reader threads
        Thread[] readers = new Thread[3];
        for (int i = 0; i < 3; i++) {
            final int id = i;
            readers[i] = new Thread(() -> {
                rwLock.readLock().lock();
                try {
                    while (!dataReady) {
                        System.out.println("Reader " + id + " waiting for data...");
                        dataAvailable.await(500, TimeUnit.MILLISECONDS); // TIMED_WAITING
                    }
                    System.out.println("Reader " + id + " read: " + sharedData);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    rwLock.readLock().unlock();
                }
            }, "Reader-" + id);
            readers[i].start();
        }

        Thread.sleep(1000);

        // Writer thread
        Thread writer = new Thread(() -> {
            rwLock.writeLock().lock();
            try {
                sharedData = "Updated data";
                dataReady = true;
                System.out.println("Writer: Data ready, notifying readers...");
                dataAvailable.signalAll();
            } finally {
                rwLock.writeLock().unlock();
            }
        }, "Writer");

        writer.start();

        for (Thread reader : readers) {
            reader.join();
        }
        writer.join();

        System.out.println("\n=== Deadlock Detection ===");
        demoDeadlockDetection();
    }

    private static void demoDeadlockDetection() throws InterruptedException {
        Object lockA = new Object();
        Object lockB = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (lockB) {
                    System.out.println("T1 acquired both locks");
                }
            }
        }, "Deadlock-T1");

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                try { Thread.sleep(100); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                synchronized (lockA) {
                    System.out.println("T2 acquired both locks");
                }
            }
        }, "Deadlock-T2");

        t1.start();
        t2.start();
        Thread.sleep(2000);

        // Detect deadlock
        var mxBean = java.lang.management.ManagementFactory.getThreadMXBean();
        long[] deadlockedThreads = mxBean.findDeadlockedThreads();
        if (deadlockedThreads != null) {
            System.out.println("DEADLOCK DETECTED! Threads: " + deadlockedThreads.length);
            var threadInfos = mxBean.getThreadInfo(deadlockedThreads);
            for (var info : threadInfos) {
                System.out.println("  Blocked thread: " + info.getThreadName() +
                    " waiting for: " + info.getLockName());
            }
        } else {
            System.out.println("No deadlock detected");
        }

        t1.interrupt();
        t2.interrupt();
    }
}
```

## 14. Enterprise Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class GracefulShutdownService {
    private final ExecutorService executor;
    private final AtomicBoolean shutdownRequested = new AtomicBoolean(false);
    private final ReentrantLock shutdownLock = new ReentrantLock();
    private final Condition shutdownComplete = shutdownLock.newCondition();

    public GracefulShutdownService(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize, r -> {
            Thread t = new Thread(r);
            t.setDaemon(false);
            t.setName("service-worker-" + t.threadId());
            return t;
        });
    }

    public void submitTask(Runnable task) {
        if (shutdownRequested.get()) {
            throw new IllegalStateException("Service is shutting down");
        }
        executor.submit(task);
    }

    public void shutdownGracefully(long timeoutMs) {
        shutdownRequested.set(true);
        System.out.println("Graceful shutdown initiated...");

        executor.shutdown();
        shutdownLock.lock();
        try {
            // Wait for all tasks to complete
            if (!executor.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
                System.out.println("Timeout reached, forcing shutdown...");
                executor.shutdownNow();

                // Wait for tasks to respond to interruption
                if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                    System.err.println("Some tasks did not terminate");
                }
            }
            shutdownComplete.signalAll();
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        } finally {
            shutdownLock.unlock();
        }

        System.out.println("Shutdown complete");
    }

    public void monitorThreads() {
        System.out.println("\n=== Thread Status ===");
        Thread.getAllStackTraces().forEach((thread, stackTrace) -> {
            if (thread.getName().startsWith("service-worker")) {
                System.out.printf("  %s: %s%n", thread.getName(), thread.getState());
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        GracefulShutdownService service = new GracefulShutdownService(4);

        // Submit some tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            service.submitTask(() -> {
                try {
                    System.out.println("Task " + taskId + " started on " +
                        Thread.currentThread().getName());
                    Thread.sleep(1000 + (int)(Math.random() * 2000));
                    System.out.println("Task " + taskId + " completed");
                } catch (InterruptedException e) {
                    System.out.println("Task " + taskId + " interrupted, cleaning up...");
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Monitor for a bit
        Thread.sleep(1000);
        service.monitorThreads();

        // Graceful shutdown
        service.shutdownGracefully(5000);
    }
}
```

## 15. Performance

### Thread State Impact on Performance

| State | CPU Usage | Memory | Wake-up Cost |
|-------|-----------|--------|--------------|
| RUNNABLE | 100% of share | Normal | N/A |
| BLOCKED | 0% | Monitor overhead | Lock release (~1μs) |
| WAITING | 0% | Minimal | notify/notifyAll (~1μs) |
| TIMED_WAITING | 0% | Minimal | Timer expiry + context switch (~5μs) |
| TERMINATED | 0% | Eligible for GC | N/A |

### Context Switch Cost

Each thread state transition may involve a context switch:
- **RUNNABLE → BLOCKED**: ~1-5μs
- **BLOCKED → RUNNABLE**: ~1-5μs
- **RUNNABLE → WAITING**: ~1-5μs
- **WAITING → RUNNABLE**: ~5-10μs (includes monitor re-acquisition)

### Thread Dump Overhead

Using `Thread.getState()` or thread dumps:
- `getState()`: ~100ns per call
- `jstack`: ~10-100ms for full dump
- `Thread.getAllStackTraces()`: ~1-10ms depending on thread count

## 16. Best Practices

1. **Always check thread state before operations**: Prevent illegal state transitions.
2. **Use `interrupt()` for cancellation**: Don't use `stop()`, `suspend()`, or `resume()`.
3. **Handle `InterruptedException` properly**: Restore interrupt flag or propagate.
4. **Implement timeouts**: Use timed waits to prevent indefinite blocking.
5. **Monitor thread counts**: Alert on excessive thread creation or stuck threads.
6. **Use `ThreadMXBean` for deadlock detection**: Periodic checks in production.
7. **Set daemon flag appropriately**: Background threads should be daemon.
8. **Clean up in `finally` blocks**: Ensure locks are released regardless of exceptions.
9. **Avoid holding locks during I/O**: Use `synchronized` only for short operations.
10. **Document thread safety guarantees**: Make state transition behavior explicit.

## 17. Common Mistakes

```java
// Mistake 1: Not handling interrupted state
synchronized (lock) {
    while (!condition) {
        lock.wait(); // If interrupted, loop continues without checking
    }
}
// Fix: Check Thread.interrupted() or handle InterruptedException

// Mistake 2: Holding locks during long operations
synchronized (sharedResource) {
    expensiveOperation(); // Other threads BLOCKED for entire duration
}
// Fix: Minimize critical sections

// Mistake 3: Using deprecated Thread methods
thread.stop();  // Deprecated, unsafe
thread.suspend(); // Deprecated, can cause deadlock
thread.resume();  // Deprecated
// Fix: Use interruption and flags

// Mistake 4: Not joining started threads
Thread t = new Thread(() -> doWork());
t.start();
// main exits before t completes
// Fix: t.join()

// Mistake 5: Confusing BLOCKED and WAITING
// BLOCKED: Waiting for monitor lock (synchronized)
// WAITING: Waiting for signal (wait/notify/join/park)
```

## 18. Pitfalls

### Invisible Deadlock
A deadlock may not be obvious from code inspection. Use `ThreadMXBean.findDeadlockedThreads()` to detect.

### Spurious Wakeups
`Object.wait()` can return without being notified (spurious wakeup). Always use `while (!condition)` loops.

### Thread Starvation
High-priority threads can starve low-priority threads. The OS scheduler may not honor Java priorities.

### Lock Convoy
Many threads competing for the same lock create a "convoy" where they serialize, defeating the purpose of multithreading.

## 19. Debugging Tips

1. **Use `jstack <pid>`**: Get thread dump with states and stack traces.
2. **Use VisualVM**: Visual thread monitoring and deadlock detection.
3. **Log state transitions**: Add logging at state change points.
4. **Use `Thread.getState()`**: Check state before operations.
5. **Check `ThreadMXBean`**: Programmatic deadlock detection.
6. **Use `Thread.holdsLock()`**: Verify lock ownership.
7. **Enable `-XX:+UseBiasedLocking`**: Optimize lock contention (pre-Java 15).
8. **Use async-profiler**: Low-overhead profiling of thread contention.

## 20. Comparison Table

| Operation | Thread State | Releases Lock? | Interruptible? |
|-----------|-------------|----------------|----------------|
| `Thread.sleep(ms)` | TIMED_WAITING | No | Yes |
| `Object.wait()` | WAITING | Yes | Yes |
| `Object.wait(ms)` | TIMED_WAITING | Yes | Yes |
| `Thread.join()` | WAITING | No | Yes |
| `Thread.join(ms)` | TIMED_WAITING | No | Yes |
| `LockSupport.park()` | WAITING | No | Yes |
| `synchronized` block | BLOCKED | N/A | No |

## 21. Decision Tree

```
Thread not making progress?
├── Is it BLOCKED?
│   ├── Yes → Check for deadlock with ThreadMXBean
│   │         Check lock contention
│   └── No → Is it WAITING/TIMED_WAITING?
│       ├── Yes → Was it interrupted?
│       │   ├── Yes → InterruptedException should be handled
│       │   └── No → Is the signal being sent?
│       │       ├── Yes → Check notify()/notifyAll() calls
│       │       └── No → Identify what it's waiting for
│       └── No → Is it TERMINATED?
│           └── Yes → Thread completed, check return value/exception
```

## 22. Interview Questions

### Q1: What are the six thread states in Java?
**A**: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED.

### Q2: What is the difference between BLOCKED and WAITING?
**A**: BLOCKED means waiting to acquire a monitor lock (synchronized). WAITING means waiting for a signal (via `wait()`, `join()`, `park()`). BLOCKED is involuntary; WAITING is voluntary.

### Q3: Can a thread go from NEW directly to TERMINATED?
**A**: No. A thread must pass through RUNNABLE. If `start()` throws (e.g., already started), it remains in NEW.

### Q4: What causes a thread to enter TIMED_WAITING?
**A**: `Thread.sleep(ms)`, `Object.wait(ms)`, `Thread.join(ms)`, `LockSupport.parkNanos()`, `LockSupport.parkUntil()`.

### Q5: How do you detect a deadlock in Java?
**A**: Use `ThreadMXBean.findDeadlockedThreads()`, use `jstack`, or use VisualVM.

### Q6: What is a spurious wakeup?
**A**: When `Object.wait()` returns without `notify()` being called. Always use `while (!condition)` loops to handle this.

### Q7: Can a daemon thread prevent JVM shutdown?
**A**: No. When all non-daemon threads finish, the JVM exits and daemon threads are killed. However, daemon threads can delay shutdown if they hold resources.

## 23. Exercises

### Exercise 1: State Monitoring
Write a program that creates 5 threads and monitors their states every 100ms. Print a state summary showing how many threads are in each state.

### Exercise 2: Deadlock Prevention
Implement a deadlock prevention strategy using `tryLock()` with timeout. Two threads try to acquire two locks in different orders.

### Exercise 3: Interruptible Waiting
Create a thread that waits for a condition. Implement a mechanism to interrupt the waiting thread gracefully, with proper cleanup.

### Exercise 4: Thread Lifecycle Visualization
Create a console-based visualization that shows thread state transitions in real-time as threads execute.

## 24. Assignments

### Assignment 1: Thread Monitor
Build a thread monitoring system that:
- Periodically dumps all thread states
- Detects deadlock conditions
- Logs threads stuck in WAITING/BLOCKED states for too long
- Generates alerts for abnormal thread counts

### Assignment 2: Graceful Shutdown Framework
Implement a framework for graceful service shutdown:
- Track all active threads
- Support ordered shutdown (dependencies)
- Implement timeout-based forced shutdown
- Clean up resources properly

## 25. Mini Project

### Thread Lifecycle Visualizer

Build a real-time thread lifecycle visualization:

```java
// Requirements:
// 1. Create multiple threads with different behaviors
// 2. Visualize state transitions in real-time
// 3. Show lock contention and wait/notify
// 4. Detect and highlight deadlocks
// 5. Generate lifecycle reports
// 6. Support interactive thread control (pause, resume, interrupt)
```

## 26. Summary

Key takeaways on thread lifecycle:

- **Six states**: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
- **BLOCKED vs WAITING**: Involuntary (lock) vs voluntary (signal) waiting
- **Spurious wakeups**: Always use `while` loops with `wait()`
- **Use `interrupt()` for cancellation**: Never use deprecated methods
- **Monitor thread states**: Use `ThreadMXBean` and thread dumps
- **Implement timeouts**: Prevent indefinite blocking
- **Handle `InterruptedException`**: Restore interrupt flag or propagate

## 27. References

### Official Documentation
- [Thread.State Enum](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.State.html)
- [Thread Class API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Thread.html)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)

### Books
- *Java Concurrency in Practice* by Brian Goetz (Chapter 5)
- *Java Threads* by Scott Oaks and Henry Wong

### Online Resources
- [Baeldung Thread States](https://www.baeldung.com/java-thread-state)
- [Jenkov Thread Lifecycle](https://jenkov.com/tutorials/java-concurrency/thread-signaling.html)

### Related Topics
- [Synchronization](../04-synchronization/README.md)
- [Locks](../05-locks/README.md)
- [Best Practices](../12-best-practices/README.md)
