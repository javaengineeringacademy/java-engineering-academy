# Thread Implementation in HotSpot

HotSpot implements Java threads as native OS threads. Each `java.lang.Thread` instance maps 1:1 to a platform thread (pthread on Unix, Thread on Windows).

## Thread Mapping

```
java.lang.Thread (Java object)
    ↓
JavaThread (C++ object in HotSpot)
    ↓
OSThread (platform-specific)
    ↓
pthread_t / Thread (OS thread)
```

### JavaThread Class

The `JavaThread` class holds per-thread state:

```cpp
class JavaThread : public Thread {
    // Execution state
    JavaFrameAnchor _anchor;    // Last known frame
    oop           _threadObj;   // java.lang.Thread mirror
    JNIEnv*       _jni_attach_counter;

    // Safepoint state
    SafepointState  _safepoint_state;

    // Thread-local allocation
    TLAB            _tlab;

    // Deoptimization
   rowableFreeChunk* _deopt_mirror_begin;

    // ...
};
```

## Thread States

### Java-Level States

```
NEW → RUNNABLE → BLOCKED → WAITING → TIMED_WAITING → TERMINATED
```

### VM-Level States

| State | Meaning |
|-------|---------|
| `_thread_new` | Thread object created, not yet started |
| `_thread_in_Java` | Executing Java bytecode |
| `_thread_in_vm` | Executing VM internal code |
| `_thread_in_native` | Executing native (JNI) code |
| `_thread_blocked` | Blocked on a monitor (synchronized) |
| `_thread_trans` | Transitioning between states |

## Thread Creation

### Java Side

```java
Thread t = new Thread(() -> { /* work */ });
t.start();  // Calls native start0()
```

### Native Side

```
Thread.start()
  → Thread.start0() [JNI]
    → JavaThread::start()
      → os::start_thread()
        → pthread_create()
          → JavaThread::thread_entry()
            → Thread.run()
```

The new thread runs `JavaThread::thread_entry()` which calls `Thread.run()` in Java.

## Thread Scheduling

HotSpot does not implement its own thread scheduler — it relies on the OS:

- **Preemptive scheduling**: OS decides which thread runs
- **Priority mapping**: Java priorities 1–10 map to OS priorities (not 1:1)
- **Time slicing**: OS provides time slicing for threads of equal priority

```bash
# Thread priority mapping
Java 1 (MIN_PRIORITY) → OS lowest
Java 5 (NORM_PRIORITY) → OS normal
Java 10 (MAX_PRIORITY) → OS highest
```

### yield() and sleep()

```java
Thread.yield();        // Hint to scheduler (no guarantee)
Thread.sleep(1000);    // Timed wait, releases CPU
```

Neither method guarantees specific scheduling behavior.

## Synchronization

### Monitors (synchronized)

Every Java object has an associated monitor:

```
synchronized(obj) {
    // Enter monitor (lock)
    // ...
} // Exit monitor (unlock)
```

HotSpot uses a **biased locking → lightweight locking → heavyweight locking** progression:

1. **Biased locking**: No atomic operations, single-threaded access
2. **Lightweight locking**: CAS-based, no OS involvement
3. **Heavyweight locking**: OS mutex/futex, parking threads

### Lock Inflation

```
Uncontended: Biased locking (fastest)
    ↓ (contention)
Lightweight: CAS lock record (fast)
    ↓ (contention / wait)
Heavyweight: OS mutex + parking (slowest)
```

### park() and unpark()

The `java.util.concurrent.locks.LockSupport` class provides low-level thread parking:

```java
LockSupport.park();     // Block until unparked
LockSupport.unpark(t);  // Unblock thread t
```

Under the hood, this uses OS primitives (futex on Linux, WaitOnAddress on Windows).

## Thread-Local Storage (TLS)

Each thread has private data:

| TLS Element | Purpose |
|-------------|---------|
| `_anchor` | Last Java frame pointer |
| `_tlab` | Thread-local allocation buffer |
| `_deopt_mirror` | Deoptimization data |
| `_stack_base` / `_stack_size` | Stack bounds |
| `pending_exception` | Pending exception to be thrown |

## Thread Dumping

### jstack

```bash
jstack <pid>
# Shows all thread states, stack traces, lock info
```

### Thread.dump()

```java
Thread.getAllStackTraces();  // Programmatic thread dump
```

### HotSpot Internal

```bash
# Verbose thread dump
-XX:+UnlockDiagnosticVMOptions -XX:+PrintThreads

# Print concurrent locks
-XX:+UnlockDiagnosticVMOptions -XX:+PrintConcurrentLocks
```

## Virtual Threads (Project Loom)

Virtual threads are lightweight threads managed by the JVM, not the OS:

```
Virtual Thread (Java)
    ↓ (many:1)
Platform Thread (OS)
    ↓ (1:1)
OS Thread (pthread)
```

### How Virtual Threads Work

- Virtual threads are scheduled onto carrier (platform) threads
- When a virtual thread blocks (I/O, sleep), it unmounts from its carrier
- The carrier thread can run another virtual thread
- When I/O completes, the virtual thread is rescheduled

```java
// Virtual thread creation
Thread.startVirtualThread(() -> {
    // Runs on a carrier thread
    // Blocks without occupying an OS thread
});
```

### Benefits

- Millions of concurrent threads (vs. thousands with platform threads)
- No thread-per-request model needed
- Existing blocking code works without modification

### Key Flags

```bash
# Enable virtual threads
-XX:+EnableVirtualThreads

# Carrier thread pool size
-XX:ActiveProcessorCount=4
```

## Key Source Files

| File | Purpose |
|------|---------|
| `src/hotspot/share/runtime/thread.hpp` | JavaThread class definition |
| `src/hotspot/share/runtime/thread.cpp` | Thread implementation |
| `src/hotspot/share/runtime/mutex.cpp` | Locking implementation |
| `src/hotspot/share/runtime/objectMonitor.cpp` | Monitor (inflated lock) |
| `src/hotspot/os/*/osThread.cpp` | Platform-specific thread |
| `src/hotspot/share/classfile/javaClasses.cpp` | Thread Java class binding |
