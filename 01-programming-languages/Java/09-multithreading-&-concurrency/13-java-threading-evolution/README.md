# Java Threading Evolution & Scheduler Deep Dive

## Overview

Java's threading model has undergone fundamental transformations since 1.96, evolving from green threads to native threads to virtual threads. This evolution reflects the industry's changing understanding of concurrency, parallelism, and resource efficiency.

---

## 1. Green Threads (Java 1.0 – 1.3)

### What Were Green Threads?

Green threads were Java's original threading implementation where the JVM managed thread scheduling entirely in **user space**, without relying on OS-level thread support. The JVM acted as its own scheduler, multiplexing multiple Java threads onto a single OS thread.

### Threading Model: N:1 (Many-to-One)

```
┌─────────────────────────────────────────────┐
│              Java Application                │
│  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐       │
│  │ T1  │  │ T2  │  │ T3  │  │ T4  │       │
│  └──┬──┘  └──┬──┘  └──┬──┘  └──┬──┘       │
│     │        │        │        │            │
│  ┌──▼────────▼────────▼────────▼──┐         │
│  │     Green Thread Scheduler     │         │
│  │     (User-Space in JVM)        │         │
│  └───────────────┬────────────────┘         │
│                  │                          │
│  ┌───────────────▼────────────────┐         │
│  │      Single OS Thread          │         │
│  │      (Kernel Space)            │         │
│  └────────────────────────────────┘         │
│                                             │
│  ┌────────────────────────────────┐         │
│  │      Single CPU Core           │         │
│  └────────────────────────────────┘         │
└─────────────────────────────────────────────┘
```

### How Green Threads Worked

1. **Cooperative Scheduling**: Threads voluntarily yielded control at predetermined points (method calls, loop iterations)
2. **No Preemption**: A thread could not be forcibly interrupted; it had to explicitly yield
3. **Single Execution Context**: Only one Java thread executed at a time on the OS thread
4. **Context Switching**: Fast (user-space only, no kernel transitions)

### Key Characteristics

| Property | Green Threads |
|----------|---------------|
| Thread Model | N:1 (Many Java threads → 1 OS thread) |
| Scheduling | Cooperative (yield-based) |
| Parallelism | None (single-core only) |
| I/O Handling | Blocking (blocks entire JVM) |
| Context Switch Cost | Very Low (~microseconds) |
| Thread Creation Cost | Very Low (~kilobytes) |
| Portability | Platform-independent |

### Limitations

**1. Blocking I/O Blocks All Threads**
```java
// With green threads, this I/O operation blocks ALL Java threads
// Not just the one performing the read
InputStream is = new FileInputStream("large_file.txt");
byte[] buffer = new byte[1024];
is.read(buffer); // JVM is frozen during this operation
```

**2. No Multi-Core Utilization**
```java
// Even with 8 CPU cores, only one thread runs at a time
// No true parallelism possible
for (int i = 0; i < 8; i++) {
    new Thread(() -> {
        // These run sequentially, not in parallel
        heavyComputation();
    }).start();
}
```

**3. Cooperative Scheduling Fragility**
```java
// A thread that never yields starves all other threads
while (true) {
    // No yield() call → other threads never get CPU time
    doWork();
}
```

**4. Platform Inconsistency**
- Worked on Solaris, Linux (with native threads disabled)
- Failed on Windows (which had native thread support)
- Created "write once, debug everywhere" problems

### Why Green Threads Were Removed

- **JDK 1.3 (2000)**: Native threads became the default on most platforms
- **JDK 1.4 (2002)**: Green threads completely removed
- **Reason**: Multi-core processors becoming standard; blocking I/O behavior unacceptable for server applications; cooperative scheduling unreliable

---

## 2. Native Threads (Java 1.3+)

### What Are Native Threads?

Native threads map Java threads directly to OS-level threads. Each Java thread corresponds to exactly one kernel thread, managed by the operating system's thread scheduler.

### Threading Model: 1:1 (One-to-One)

```
┌─────────────────────────────────────────────┐
│              Java Application                │
│  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐       │
│  │ T1  │  │ T2  │  │ T3  │  │ T4  │       │
│  └──┬──┘  └──┬──┘  └──┬──┘  └──┬──┘       │
│     │        │        │        │            │
├─────┼────────┼────────┼────────┼────────────┤
│     │   JVM (minimal mapping)  │            │
├─────┼────────┼────────┼────────┼────────────┤
│     ▼        ▼        ▼        ▼            │
│  ┌─────┐  ┌─────┐  ┌─────┐  ┌─────┐       │
│  │OS T1│  │OS T2│  │OS T3│  │OS T4│       │
│  └──┬──┘  └──┬──┘  └──┬──┘  └──┬──┘       │
│     │        │        │        │            │
│  ┌──▼────────▼────────▼────────▼──┐         │
│  │    OS Thread Scheduler          │         │
│  │    (Kernel Space)               │         │
│  └───────────────┬────────────────┘         │
│                  │                          │
│  ┌──────┬────────┼────────┬──────┐          │
│  │Core 1│ Core 2 │ Core 3 │Core 4│          │
│  └──────┴────────┴────────┴──────┘          │
└─────────────────────────────────────────────┘
```

### Key Characteristics

| Property | Native Threads |
|----------|---------------|
| Thread Model | 1:1 (1 Java thread → 1 OS thread) |
| Scheduling | Preemptive (OS-managed) |
| Parallelism | True multi-core parallelism |
| I/O Handling | Non-blocking per thread |
| Context Switch Cost | Moderate (~10-100 microseconds) |
| Thread Creation Cost | Moderate (~1MB stack) |
| Portability | Platform-dependent behavior |

### Advantages

**1. True Parallelism**
```java
// Now threads actually run on different cores simultaneously
IntStream.range(0, 8).parallel().forEach(i -> {
    // These run on different CPU cores
    System.out.println("Core: " + Thread.currentThread().getName());
});
```

**2. Non-Blocking I/O**
```java
// Thread 1 can block on I/O while Thread 2 continues
Thread t1 = new Thread(() -> {
    readFromNetwork(); // Blocks only this thread
});
Thread t2 = new Thread(() -> {
    processInMemory(); // Runs concurrently
});
```

**3. Preemptive Scheduling**
```java
// OS can forcibly switch between threads
// No thread can starve others indefinitely
// Time quantum typically 1-10ms
```

### Disadvantages

**1. High Thread Creation Cost**
```java
// Each thread requires ~1MB stack space
// Creating 10,000 threads → ~10GB memory
for (int i = 0; i < 10_000; i++) {
    new Thread(() -> {
        // Memory-intensive
        doWork();
    }).start();
}
```

**2. Expensive Context Switching**
```java
// Context switch involves:
// 1. Save thread state to kernel memory
// 2. Switch to kernel mode
// 3. Load new thread state
// 4. Switch back to user mode
// Cost: ~1-10 microseconds per switch
```

**3. Resource Contention**
```java
// Many threads competing for:
// - CPU time slices
// - Memory bandwidth
// - Cache lines
// Can cause "thread thrashing"
```

---

## 3. Daemon Threads

### What Are Daemon Threads?

Daemon threads are background service threads that the JVM does not keep alive after all non-daemon (user) threads have terminated. They are "utility" threads that serve other threads.

### Key Rules

1. **`setDaemon(true)` must be called BEFORE `start()`**
2. **Daemon status cannot be changed after thread starts**
3. **New daemon threads inherit daemon status from parent**
4. **JVM exits when only daemon threads remain running**

### Thread Lifecycle and Daemon Status

```
┌─────────────────────────────────────────────────┐
│                 Thread Creation                  │
│                                                  │
│  Thread t = new Thread(() -> { ... });           │
│  t.setDaemon(true);  // MUST be before start()  │
│  t.start();                                     │
│                                                  │
├─────────────────────────────────────────────────┤
│                 Thread States                    │
│                                                  │
│  NEW ──start()──► RUNNABLE ──► BLOCKED          │
│                    │             │               │
│                    │             ▼               │
│                    │         WAITING             │
│                    │             │               │
│                    ▼             ▼               │
│              TIMED_WAITING  TERMINATED           │
│                    │             │               │
│                    └──────┬──────┘               │
│                           │                      │
│                           ▼                      │
│                    JVM Exit Check                │
│                    (Any daemon threads alive?)   │
│                    No → JVM exits                │
│                    Yes → Wait/Force              │
└─────────────────────────────────────────────────┘
```

### Daemon Thread Use Cases

| Use Case | Example | Why Daemon |
|----------|---------|------------|
| Garbage Collection | `FinalizerDaemon` | Background cleanup |
| Finalization | `FinalizerThread` | Object finalization |
| Signal Dispatch | `Signal Dispatcher` | OS signal handling |
| Timer Tasks | `Timer` background | Periodic scheduling |
| I/O Monitoring | File watchers | Background monitoring |
| Cache Cleanup | Expired entries | Background maintenance |

### Example Pattern

```java
Thread daemonThread = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        performBackgroundTask();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
        }
    }
});

daemonThread.setDaemon(true);  // Before start()
daemonThread.start();

// Main thread does work
doMainWork();

// When main thread finishes, daemon thread is automatically killed
// JVM exits
```

---

## 4. Java 21 Virtual Threads (Project Loom)

### What Are Virtual Threads?

Virtual threads are lightweight threads managed by the JVM, not the OS. They implement the **M:N threading model** where many virtual threads are multiplexed onto a small number of carrier (platform) threads.

### Threading Model: M:N (Many-to-Many)

```
┌─────────────────────────────────────────────┐
│              Java Application                │
│  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐  │
│  │VT 1 │ │VT 2 │ │VT 3 │ │VT 4 │ │VT N │  │
│  └──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘ └──┬──┘  │
│     │       │       │       │       │       │
│  ┌──▼───────▼───────▼───────▼───────▼──┐   │
│  │     Virtual Thread Scheduler         │   │
│  │     (ForkJoinPool - JVM managed)     │   │
│  └──────────────┬───────────────────────┘   │
│                 │                            │
│  ┌──────┬───────┼───────┬──────┐            │
│  │      │       │       │      │            │
│  ▼      ▼       ▼       ▼      ▼            │
│ ┌───┐ ┌───┐ ┌───┐                    │     │
│ │CT1│ │CT2│ │CT3│  Carrier Threads   │     │
│ └─┬─┘ └─┬─┘ └─┬─┘  (Platform)       │     │
│   │     │     │                       │     │
│  ┌▼─────▼─────▼┐                     │     │
│  │ OS Scheduler │                     │     │
│  └──────┬──────┘                     │     │
│         │                             │     │
│  ┌──────▼──────┐                     │     │
│  │ CPU Cores   │                     │     │
│  └─────────────┘                     │     │
└─────────────────────────────────────────────┘
```

### Key Characteristics

| Property | Virtual Threads |
|----------|----------------|
| Thread Model | M:N (Many virtual → few carrier) |
| Scheduling | JVM-managed (work-stealing) |
| Memory Cost | ~1KB per thread (vs ~1MB for platform) |
| Creation Cost | Very Low |
| Blocking Behavior | Carrier thread released during blocking |
| Maximum Threads | Millions possible |
| API Compatibility | Same `Thread` API |

### How Virtual Threads Work

**1. Carrier Threads (ForkJoinPool)**
```java
// Default carrier threads = available processors
// Can customize via system property:
// -Djdk.virtualThreadScheduler.parallelism=4
// -Djdk.virtualThreadScheduler.maxPoolSize=8
```

**2. Blocking is Cheap**
```java
// When a virtual thread blocks (I/O, sleep, lock):
// 1. Virtual thread state is saved
// 2. Carrier thread is released back to pool
// 3. Carrier thread runs other virtual threads
// 4. When unblocked, virtual thread resumes on available carrier

// Traditional platform thread: OS thread blocked, wasted
// Virtual thread: Carrier thread productive, virtual thread waiting
```

**3. Pinning Problem**
```java
// Virtual threads get "pinned" to carrier when:
// - Holding a native (JNI) method
// - Inside synchronized block during blocking I/O

// Solution: Use ReentrantLock instead of synchronized
// Bad:
synchronized (lock) {
    blockingOperation(); // Pins virtual thread
}

// Good:
ReentrantLock lock = new ReentrantLock();
lock.lock();
try {
    blockingOperation(); // Virtual thread can unmount
} finally {
    lock.unlock();
}
```

### Creating Virtual Threads

```java
// Method 1: Thread.ofVirtual()
Thread vt = Thread.ofVirtual().name("my-vt").start(() -> {
    System.out.println("Running in virtual thread");
});

// Method 2: Thread.Builder
Thread vt = Thread.ofVirtual()
    .name("worker-", 0)
    .start(() -> doWork());

// Method 3: ExecutorService (recommended for bulk)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i -> {
        executor.submit(() -> {
            // 100,000 virtual threads!
            Thread.sleep(Duration.ofSeconds(1));
            return i;
        });
    });
}

// Method 4: try-with-resources
try (var vt = Thread.ofVirtual().start(() -> {
    doWork();
})) {
    vt.join();
}
```

### Structured Concurrency (Preview in 21, Stable in 23+)

```java
// Structured concurrency ensures child tasks complete
// before parent scope exits

try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Subtask<String> user = scope.fork(() -> fetchUser());
    Subtask<Order>  order = scope.fork(() -> fetchOrder());
    Subtask<Receipt> receipt = scope.fork(() -> fetchReceipt());

    scope.join();            // Wait for all
    scope.throwIfFailed();   // Propagate errors

    // All results available here
    return new Response(user.get(), order.get(), receipt.get());
}
// Scope exit guarantees cleanup
```

### Scoped Values (Replacement for ThreadLocal)

```java
// ThreadLocal - problematic with virtual threads
// Creates thread-local copies, memory leak risk

// ScopedValue - structured, auto-cleaned
private static final ScopedValue<User> CURRENT_USER = ScopedValue.newInstance();

ScopedValue.where(CURRENT_USER, user).run(() -> {
    // CURRENT_USER is accessible in this scope
    // Automatically cleaned up when scope exits
    // Works perfectly with virtual threads
    handleRequest();
});
```

### When to Use Virtual Threads vs Platform Threads

| Scenario | Use Virtual | Use Platform |
|----------|-------------|--------------|
| I/O-bound (HTTP, DB, file) | ✅ | ❌ |
| High thread count (>10K) | ✅ | ❌ |
| CPU-bound computation | ❌ | ✅ |
| Low-latency (<1ms) | ❌ | ✅ |
| Thread-per-request servers | ✅ | ❌ |
| Legacy thread pool code | ✅ (drop-in) | ✅ |
| Need thread affinity | ❌ | ✅ |

---

## 5. Java 26+ Virtual Threads (Future/Current)

### Recent Enhancements

Java 26 continues to refine virtual threads with:

**1. Improved Pinning Detection**
```java
// Better diagnostics for pinned virtual threads
// JDK 24+: -Djdk.tracePinnedThreads=full
// Shows stack traces of pinning incidents
```

**2. Structured Concurrency Enhancements**
```java
// New APIs for structured task management
// Better error propagation
// Cancellation semantics
```

**3. Scoped Values Maturity**
```java
// Finalized API (no longer preview)
// Better integration with virtual threads
// Read-only scoped values for safety
```

**4. Performance Optimizations**
```
- Reduced memory overhead per virtual thread
- Faster mount/unmount operations
- Better carrier thread utilization
- Improved work-stealing algorithm
```

**5. New APIs**
```java
// Thread.interruptOnClose() - structured interruption
// Thread.ofVirtual().scheduler() - custom schedulers
// StructuredTaskScope improvements
```

---

## 6. Thread Scheduler Deep Dive

### How OS Schedulers Work

Operating systems use scheduling algorithms to determine which thread runs on which CPU core and for how long.

### Scheduling Models

**1. Preemptive Scheduling**
```
┌─────────────────────────────────────────┐
│         Preemptive Scheduling            │
│                                          │
│  Thread A runs ──► Timer Interrupt        │
│       │              │                   │
│       │              ▼                   │
│       │         Scheduler decides        │
│       │              │                   │
│       │         ┌────┴────┐             │
│       │         │         │             │
│       ▼         ▼         ▼             │
│     Resume   Switch to   Switch to      │
│     Thread A Thread B   Thread C        │
│                                          │
│  Key: OS can forcibly preempt any thread │
│       at any timer interrupt             │
└─────────────────────────────────────────┘
```

**2. Cooperative Scheduling**
```
┌─────────────────────────────────────────┐
│         Cooperative Scheduling           │
│                                          │
│  Thread A runs ──► yield() or block      │
│       │              │                   │
│       │              ▼                   │
│       │         Scheduler runs           │
│       │              │                   │
│       ▼              ▼                   │
│  Thread A      Thread B runs            │
│  (resumed)     (gets CPU)               │
│                                          │
│  Key: Thread must voluntarily give up    │
│       CPU for others to run              │
└─────────────────────────────────────────┘
```

### Thread Priority Scheduling

```java
// Java thread priorities (1-10)
// Map to OS priority levels (varies by OS)

Thread t1 = new Thread(() -> doWork());
Thread t2 = new Thread(() -> doWork());

t1.setPriority(Thread.MAX_PRIORITY);  // 10
t2.setPriority(Thread.MIN_PRIORITY);  // 1

// Priority is a HINT, not a guarantee
// OS may ignore or reinterpret priorities
```

**Priority Mapping by OS:**
| Java Priority | Linux (nice) | Windows | macOS |
|---------------|--------------|---------|-------|
| 1 (MIN) | 19 | 1 | 0 |
| 5 (NORM) | 10 | 8 | 31 |
| 10 (MAX) | 0 | 31 | 63 |

### Time Slicing

```
┌─────────────────────────────────────────────────┐
│              Time Slicing                        │
│                                                  │
│  Time Slice = ~1-10ms (OS dependent)             │
│                                                  │
│  Core 1:  [A][A][A][B][B][B][C][C][C][A][A]     │
│  Core 2:  [D][D][E][E][E][F][F][F][D][D][D]     │
│                                                  │
│  Each block = 1 time slice (~1ms)                │
│                                                  │
│  Context Switch occurs at slice boundaries       │
│  Total time to switch: ~1-10 microseconds        │
└─────────────────────────────────────────────────┘
```

### Linux CFS (Completely Fair Scheduler)

```
┌─────────────────────────────────────────────────┐
│         Linux CFS (Linux 2.6.23+)                │
│                                                  │
│  Goal: Fair CPU time distribution                │
│                                                  │
│  Red-Black Tree Structure:                       │
│                                                  │
│              [D:10ms]                            │
│             /        \                           │
│         [B:5ms]    [E:15ms]                     │
│         /    \         /    \                    │
│     [A:2ms] [C:8ms] [F:20ms] [G:12ms]          │
│                                                  │
│  Node key = virtual runtime (vruntime)           │
│  Leftmost node = next to execute                 │
│                                                  │
│  Nice values affect time slices:                 │
│  nice -20 = more CPU time                        │
│  nice +19 = less CPU time                        │
│                                                  │
│  Time slice = base + (base × nice_factor)        │
└─────────────────────────────────────────────────┘
```

**CFS Behavior:**
- Tracks "virtual runtime" per thread
- Thread with lowest vruntime runs next
- Nice values scale vruntime accumulation rate
- No fixed time slices (completely fair)
- O(log n) for scheduling decisions

### Windows Thread Scheduler

```
┌─────────────────────────────────────────────────┐
│         Windows Thread Scheduler                 │
│                                                  │
│  32 Priority Levels (0-31):                      │
│  ┌────────────────────────────────────┐         │
│  │ 31: Real-time                     │         │
│  │ 16-30: Real-time class            │         │
│  │ 1-15: Variable class              │         │
│  │ 0: Zero page thread               │         │
│  └────────────────────────────────────┘         │
│                                                  │
│  Preemptive with priority boosting:             │
│  - Foreground app gets priority boost           │
│  - I/O completion boosts priority               │
│  - Time quantum: ~20ms (short) or ~60ms (long)  │
│                                                  │
│  Multi-level Feedback Queue:                    │
│  - Threads move between queues                  │
│  - CPU-bound threads → lower priority           │
│  - I/O-bound threads → higher priority          │
└─────────────────────────────────────────────────┘
```

### macOS Scheduler (XNU Kernel)

```
┌─────────────────────────────────────────────────┐
│         macOS/XNU Scheduler                      │
│                                                  │
│  Based on Mach scheduler + BSD scheduler         │
│                                                  │
│  Thread QoS Classes:                             │
│  ┌────────────────────────────────────┐         │
│  │ User Interactive (highest)         │         │
│  │ User Initiated                     │         │
│  │ Default                            │         │
│  │ Utility                            │         │
│  │ Background (lowest)                │         │
│  └────────────────────────────────────┘         │
│                                                  │
│  Features:                                       │
│  - Work stealing across cores                    │
│  - QoS-aware scheduling                         │
│  - Thermal-aware (reduces under heat)           │
│  - Fair share scheduling per process            │
└─────────────────────────────────────────────────┘
```

---

## 7. JVM Scheduler and OS Interaction

### JVM-OS Thread Mapping

```
┌─────────────────────────────────────────────────┐
│              JVM Thread Architecture              │
│                                                  │
│  ┌─────────────────────────────────────────┐    │
│  │           Java Application               │    │
│  │  Thread objects, Runnable, etc.          │    │
│  └──────────────────┬──────────────────────┘    │
│                     │                            │
│  ┌──────────────────▼──────────────────────┐    │
│  │           JVM Thread Manager             │    │
│  │  - Thread state tracking                 │    │
│  │  - JIT compilation decisions             │    │
│  │  - GC safepoint coordination             │    │
│  └──────────────────┬──────────────────────┘    │
│                     │                            │
│  ┌──────────────────▼──────────────────────┐    │
│  │           OS Thread Abstraction          │    │
│  │  pthread (Linux), CreateThread (Win)     │    │
│  └──────────────────┬──────────────────────┘    │
│                     │                            │
│  ┌──────────────────▼──────────────────────┐    │
│  │           OS Kernel Scheduler            │    │
│  │  - Preemptive scheduling                 │    │
│  │  - Priority mapping                      │    │
│  │  - Time slicing                          │    │
│  └─────────────────────────────────────────┘    │
└─────────────────────────────────────────────────┘
```

### Thread States and Transitions

```
┌───────────────────────────────────────────────────────────┐
│                   Thread State Diagram                      │
│                                                            │
│                    ┌──────────┐                            │
│         new() ──►  │   NEW    │                            │
│                    └────┬─────┘                            │
│                         │ start()                          │
│                         ▼                                  │
│                    ┌──────────┐                            │
│        ┌─────────│ RUNNABLE │◄────────┐                  │
│        │          └────┬─────┘         │                  │
│        │               │               │                  │
│  wait()/│         ┌────▼────┐    notify()/│           │
│  join() │         │         │    notifyAll()│          │
│  ┌──────▼───┐     │ RUNNING │    └──────┬─────┐       │
│  │ WAITING  │     │ (on CPU)│           │     │       │
│  └──────┬───┘     │         │           │     │       │
│         │         └────┬────┘           │     │       │
│    signal()            │                │     │       │
│         │          ┌───▼──────┐         │     │       │
│         └─────────│BLOCKED   │─────────┘     │       │
│                   │(lock)    │               │       │
│                   └──────────┘               │       │
│                                              │       │
│                   ┌──────────────┐           │       │
│        ┌─────────│TIMED_WAITING │───────────┘       │
│        │         │(sleep, wait(t))│                 │
│        │         └──────────────┘                   │
│        │              │                             │
│        │          timeout                           │
│        │              │                             │
│        └──────────────┘                             │
│                   │                                  │
│              run() completes                         │
│                   │                                  │
│                   ▼                                  │
│            ┌───────────┐                            │
│            │ TERMINATED │                            │
│            └───────────┘                            │
└───────────────────────────────────────────────────────────┘
```

### Parking and Unparking

```java
// JVM internal mechanism for thread synchronization
// Used by LockSupport, Condition, etc.

// park() - blocks virtual thread, releases carrier
// unpark() - wakes up parked virtual thread

LockSupport.park();        // Block until unparked
LockSupport.unpark(t);     // Unblock thread t

// Virtual threads: park releases carrier thread
// Platform threads: park blocks OS thread
```

### JIT Compiler Impact on Scheduling

```
┌─────────────────────────────────────────────────┐
│         JIT Compiler Effects                     │
│                                                  │
│  1. Method Inlining                              │
│     - Reduces call overhead                      │
│     - Fewer safepoint checks                     │
│     - Smoother scheduling                        │
│                                                  │
│  2. Lock Elision                                 │
│     - Eliminates unnecessary synchronization     │
│     - Reduces blocking                           │
│     - Better scheduling decisions                │
│                                                  │
│  3. Loop Optimization                            │
│     - Can insert safepoint polls                 │
│     - May affect scheduling responsiveness       │
│     - Balanced between throughput and fairness   │
│                                                  │
│  4. Escape Analysis                              │
│     - Stack allocation reduces heap pressure     │
│     - Less GC = fewer safepoint pauses           │
│     - More predictable scheduling                │
└─────────────────────────────────────────────────┘
```

---

## 8. Summary: Evolution Timeline

```
1996 ─── Java 1.0: Green Threads (N:1)
         │  - User-space scheduling
         │  - No parallelism
         │  - Cooperative scheduling
         │
2000 ─── Java 1.3: Native Threads (1:1) [Default]
         │  - OS-level threading
         │  - True parallelism
         │  - Preemptive scheduling
         │
2002 ─── Java 1.4: Green Threads Removed
         │  - Native threads only
         │  - Multi-core awareness growing
         │
2004 ─── Java 5: Executor Framework
         │  - Thread pool abstraction
         │  - Better task scheduling
         │  - ExecutorService, ThreadPoolExecutor
         │
2006 ─── Java 6: Improved Executors
         │  - ForkJoinPool introduced
         │  - Work-stealing scheduler
         │
2011 ─── Java 7: ForkJoinPool
         │  - Recursive task parallelism
         │  - Work-stealing queues
         │
2014 ─── Java 8: Parallel Streams
         │  - ForkJoinPool.commonPool()
         │  - CompletableFuture
         │
2017 ─── Java 9-10: Flow API, Reactive Streams
         │
2021 ─── Java 17: Scoped Values Preview
         │
2023 ─── Java 21: Virtual Threads (GA)
         │  - M:N threading model
         │  - Structured concurrency preview
         │  - Scoped values preview
         │  - Lightweight: millions of threads
         │
2024 ─── Java 22-23: Virtual Threads improvements
         │  - Better pinning diagnostics
         │  - Structured concurrency enhancements
         │
2025 ─── Java 24-25: Scoped Values stable
         │  - Better carrier thread management
         │  - Performance optimizations
         │
2026 ─── Java 26+: Continued refinement
         │  - Enhanced structured concurrency
         │  - Improved virtual thread scheduling
         │  - Better debugging tools
```

---

## Key Takeaways

1. **Green threads were a stepping stone** - taught us about threading limitations on shared OS threads
2. **Native threads enabled true parallelism** - but at high memory and context-switch cost
3. **Daemon threads are background helpers** - JVM exits when only daemons remain
4. **Virtual threads solve the cost problem** - millions of threads with cheap blocking
5. **Scheduler is the brain** - OS and JVM schedulers determine thread execution order
6. **No one-size-fits-all** - Choose virtual threads for I/O-bound, platform threads for CPU-bound

---

## Related Topics

- [Thread Fundamentals](../08-thread-fundamentals/)
- [Synchronization & Locks](../09-synchronization/)
- [Executor Framework](../10-executor-framework/)
- [Concurrent Collections](../11-concurrent-collections/)
- [CompletableFuture](../12-completable-future/)
