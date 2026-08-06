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

---

[📖 Continue to Part 2](README-part2.md)
