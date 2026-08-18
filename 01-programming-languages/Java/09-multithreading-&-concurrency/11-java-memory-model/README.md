# 11 - Java Memory Model

## Overview

The Java Memory Model (JMM) is a formal specification that defines how threads interact through shared memory. It defines the **happens-before** partial ordering between actions in different threads, guaranteeing when writes by one thread become visible to reads by another. Without the JMM, concurrent Java programs would be riddled with visibility bugs caused by compiler optimizations, CPU caching, and instruction reordering.

The JMM does **not** describe how a particular JVM or CPU implements memory. It defines a contract: if your code follows the happens-before rules, certain visibility and ordering guarantees hold on *any* platform.

## Core Concepts

| Concept | Description |
|---------|-------------|
| **Happens-Before** | A partial ordering between actions: if action A happens-before action B, then A's effects are visible to B |
| **Memory Visibility** | Whether a thread sees the most recent write to a variable by another thread |
| **Atomicity** | Whether an operation completes indivisibly without interleaving from other threads |
| **Reordering** | Compiler/CPU may execute instructions in a different order than written for performance |
| **Memory Barriers** | Hardware-level instructions that prevent certain reorderings and force cache coherence |
| **Data Race** | Two threads access the same variable, at least one writes, and no happens-before relationship exists |
| **Safe Publication** | Making an object's state visible to other threads without data races |

## Happens-Before Rules — Complete Reference

The JMM defines **9 happens-before rules**. If an action A happens-before action B, then *all memory effects of A* (writes, reads, etc.) are visible to B. The rules are:

### 1. Program Order Rule (JLS §17.4.5)

Within a **single thread**, each action happens-before every subsequent action in program order. This is trivially true — a thread always sees its own writes in order. It only matters when composed with other rules (e.g., transitivity).

```java
int x = 10;          // Action A
int y = x + 5;       // Action B
// A happens-before B within the same thread — guaranteed
```

**Why it matters:** Combined with the Thread Termination Rule, it guarantees that a thread's writes are visible to another thread that joins on it.

### 2. Monitor Lock Rule (unlock → lock)

An **unlock** on a monitor (exiting a `synchronized` block) happens-before every subsequent **lock** on the *same* monitor (entering a `synchronized` block).

```java
// Thread 1                         // Thread 2
synchronized (lock) { // LOCK       synchronized (lock) { // LOCK
    data = 42;  // write               // must see data == 42
} // UNLOCK                         } // UNLOCK
```

**Key detail:** The lock does NOT have to be held at the same time. Thread 1 can release the lock long before Thread 2 acquires it. The happens-before is established at the *point of lock acquisition*, not during the critical section.

**Common mistake:** Developers assume only the *last* write before unlock is visible. In reality, *all* writes that happen-before the unlock are visible to the subsequent lock.

### 3. Volatile Variable Rule (write → read)

A **write** to a `volatile` variable happens-before every subsequent **read** of the same volatile variable. "Subsequent" means in the happens-before order, not necessarily in wall-clock time.

```java
volatile boolean ready = false;
int data = 0;

// Thread 1                     // Thread 2
data = 42;                      while (!ready) { /* spin */ }
ready = true;  // volatile WRITE // volatile READ sees true
                                // Therefore, data == 42 is guaranteed
```

**What volatile does NOT do:** It does NOT make compound operations atomic. `volatile int count; count++` is still a race condition because `count++` is read-modify-write (three operations).

### 4. Thread Start Rule

A call to `Thread.start()` happens-before every action in the started thread. The thread's `run()` method body happens-after `start()` returns.

```java
int sharedData = 0;

Thread t = new Thread(() -> {
    // All actions here happen-after start() returns to caller
    System.out.println(sharedData); // guaranteed to see value set before start()
});
sharedData = 42;
t.start(); // start() happens-before run() begins
```

**Important:** This rule only guarantees visibility of actions *before* `start()`. It does NOT guarantee visibility of actions *after* `start()`.

### 5. Thread Termination Rule (all actions → join() returns)

All actions in a thread happen-before any other thread detects that thread has terminated — either via `Thread.join()` returning or via `Thread.isAlive()` returning false.

```java
int result = 0;

Thread t = new Thread(() -> {
    result = computeExpensiveValue(); // action in thread
});
t.start();
t.join(); // join() returns happens-after all actions in t
// result is guaranteed to have the value computed by t
```

**Common mistake:** Using `Thread.isAlive()` to detect termination does NOT provide the same happens-before guarantee as `join()`. The JMM says `isAlive()` returning false, but doesn't define a happens-before from all actions to the `isAlive()` call in all implementations.

### 6. Transitivity

If A happens-before B, and B happens-before C, then A happens-before C. This is how chains of visibility are established.

```java
// Thread 1                         // Thread 2
data = 42;                          // (no synchronization)
flag = true;  // volatile WRITE     while (!flag) {} // volatile READ
                                      // flag write HB read (Rule 3)
                                      // data write HB flag write (Rule 1)
                                      // Therefore: data write HB flag read
                                      // data == 42 guaranteed!
```

### 7. Interruption Rule

An interrupt on a thread happens-before any other thread detects the interrupt — either by catching `InterruptedException` or by checking `Thread.interrupted()`.

```java
// Thread 1                    // Thread 2
t.interrupt();                 try {
                                   Thread.sleep(Long.MAX_VALUE);
                               } catch (InterruptedException e) {
                                   // interrupt() HB here
                               }
```

### 8. Finalizer Rule

The end of a constructor happens-before the start of the finalizer for the same object. This is more of a practical guarantee — finalizers run after the object is fully constructed.

```java
class Resource {
    int id;
    Resource() { this.id = 42; } // constructor ends
    @Override protected void finalize() { // guaranteed after constructor
        System.out.println("Cleaning up " + id);
    }
}
```

### 9. Object Constructor Rule (this.x = 5 → subthread sees 5)

Writing to a field in a constructor (`this.x = value`) happens-before the first action in a subthread that starts during the constructor (e.g., passing `this` to a thread pool or registering a listener).

```java
class EventSource {
    EventSource() {
        this.data = 100; // field assignment
        // If this.startThread() passes 'this' to a new Thread,
        // the thread is guaranteed to see data == 100
    }
}
```

## Visibility

### What Visibility Means in the JMM

Visibility determines whether a thread can observe a write performed by another thread. Without explicit synchronization, the JMM makes **no guarantees** about visibility. A thread may see stale values indefinitely.

### Why Threads Don't See Each Other's Writes

Modern CPUs have a hierarchy of caches that sit between the processor and main memory:

```
┌─────────┐  ┌─────────┐
│  Core 0  │  │  Core 1  │
│  (L1)    │  │  (L1)    │
│  L2      │  │  L2      │
└────┬─────┘  └────┬─────┘
     │              │
  ┌──┴──────────────┴──┐
  │      L3 Cache       │
  └──────────┬──────────┘
             │
      ┌──────┴──────┐
      │ Main Memory  │
      └──────────────┘
```

Each core has its own L1/L2 cache. When Thread 1 writes to `x`, the write goes to Core 0's cache. Core 1 may still see the old value in its own cache. Without a happens-before relationship, there is no mechanism to force Core 1 to invalidate its cached copy.

Additionally, CPUs use **store buffers** — small FIFO queues between the execution unit and the cache. A write may sit in the store buffer and not yet be visible to other cores, even if it has been "committed" by the executing core.

### How to Guarantee Visibility

| Mechanism | When to Use | Guarantee |
|-----------|-------------|-----------|
| `volatile` | Simple flags, counters, state indicators | Write visible to subsequent read; prevents compiler/CPU reordering |
| `synchronized` | Compound operations, critical sections | All writes before unlock visible to subsequent lock on same monitor |
| `final` fields | Immutable objects | Visible after constructor completes (no sync needed) |
| `Atomic*` classes | Single-variable atomic operations | Volatile semantics + atomicity (CAS) |

## Reordering

### What Is Reordering?

Reordering is the reorganization of instruction execution order by the compiler, JVM, or CPU to improve performance. The JMM allows reordering as long as single-threaded semantics are preserved (as-if-serial).

### Compiler Reordering

The Java compiler can reorder statements if it determines the reordering doesn't affect the program's observable behavior in a single thread:

```java
// Original          // Compiler may reorder to:
int a = 1;           int b = 3;
int b = 3;           int a = 1;
int c = a + b;       int c = a + b;  // same result
```

In concurrent code, this can be devastating:
```java
// Original intent                // Compiler might reorder to:
config = loadConfig();            ready = true;
ready = true;                     config = loadConfig(); // ← config not yet visible when flag set!
```

### CPU Reordering

Modern CPUs execute instructions **out of order** for performance. A write may not be committed to cache even after the instruction "executes." The CPU's memory model (e.g., x86-TSO, ARM-weak) defines which reorderings are allowed at the hardware level.

### Memory Reordering

Memory reordering refers to the order in which writes become visible to other cores. Even if instructions execute in program order, the *visibility* of their effects may be reordered due to store buffers and cache coherence protocols.

### Why Reordering Happens

1. **Instruction-level parallelism (ILP):** CPUs execute multiple instructions simultaneously; reordering exposes more parallelism
2. **Cache efficiency:** Batching writes (store buffering) reduces cache coherence traffic
3. **Branch prediction:** Speculative execution may execute instructions that are later discarded
4. **Compiler optimization:** Eliminating redundant loads, hoisting invariants, etc.

### How to Prevent Reordering

| Mechanism | What It Prevents |
|-----------|------------------|
| `volatile` write | Insert StoreLoad + StoreStore + LoadStore barriers |
| `volatile` read | Insert LoadLoad + LoadStore barriers |
| `synchronized` entry | Insert LoadLoad + LoadStore barriers (acquire semantics) |
| `synchronized` exit | Insert StoreStore + LoadStore + StoreLoad barriers (release semantics) |
| `Unsafe.loadFence()` | Insert a load fence (JDK internal) |
| `Unsafe.storeFence()` | Insert a store fence (JDK internal) |
| `Unsafe.fullFence()` | Insert a full fence (JDK internal) |

## Volatile Semantics

### What volatile Guarantees

1. **Visibility:** A write to a volatile variable is immediately visible to subsequent reads (the write is flushed from the store buffer to the cache, and the reader's cache is invalidated)
2. **Ordering:** A volatile write prevents compiler and CPU from reordering preceding instructions past the write. A volatile read prevents reordering of subsequent instructions before the read.
3. **Happens-before:** A volatile write to variable `v` happens-before every subsequent read of `v` by any thread.

### What volatile Does NOT Guarantee

1. **Atomicity:** `volatile int count; count++` is NOT atomic. It is three operations: read, increment, write.
2. **Mutual exclusion:** Volatile does not prevent concurrent modification — multiple threads can write to a volatile variable simultaneously.
3. **Compound atomicity:** Writing multiple variables atomically (e.g., `this.x = x; this.y = y;` — another thread may see updated `x` but stale `y`).

### Memory Barriers Inserted by volatile

**On a volatile write** (in the JMM's abstract model):
```
StoreStore barrier  ← prevents reordering of prior stores before this store
[volatile write]
StoreLoad barrier   ← prevents reordering of this store before subsequent loads
```

**On a volatile read:**
```
LoadLoad barrier    ← prevents reordering of this load before subsequent loads
[volatile read]
LoadStore barrier   ← prevents reordering of subsequent stores before this load
```

### volatile Reference Semantics

Using volatile on a reference (not the pointed-to object):

```java
volatile SharedObject ref;

// Thread 1                     // Thread 2
ref = new SharedObject();       SharedObject local = ref;
                                if (local != null) {
                                    // local.data is NOT guaranteed visible!
                                    // volatile only guarantees ref is visible
                                }
```

To make the object's fields visible, the reference itself must be volatile **and** the fields must be safely published (volatile, final, or synchronized). Alternatively, all fields of `SharedObject` can be volatile.

## Synchronization Semantics

### Mutual Exclusion

`synchronized` ensures that only one thread can execute a critical section at a time. This prevents data races on shared mutable state.

```java
synchronized (lock) {
    // Only one thread at a time
    sharedState.modify();
}
```

### Memory Visibility (happens-before)

The happens-before guarantee of `synchronized`:

- All writes by Thread 1 **before** releasing a monitor are visible to Thread 2 **after** acquiring the same monitor.
- This means you don't need `synchronized` for *every* read — you need it for the *first* read after acquiring the monitor.

```java
// Thread 1                     // Thread 2
synchronized (lock) {           synchronized (lock) {
    a = 1;                        // guaranteed to see a == 1
    b = 2;                        // guaranteed to see b == 2
} // UNLOCK                    } // (does NOT need to read a and b inside sync)
```

### Reentrancy

`synchronized` is **reentrant**. A thread that already holds a monitor can re-enter it without deadlocking:

```java
synchronized (lock) {
    synchronized (lock) { // same thread re-enters — allowed
        // ...
    }
}
```

### Lock Acquisition and Release Ordering

1. **Acquire:** The thread enters the synchronized block and loads the monitor. All writes by the previous holder (if any) are now visible.
2. **Execute:** The critical section runs.
3. **Release:** The thread exits the synchronized block. All writes in the critical section are flushed and will be visible to the next acquirer.

The ordering is: previous release → current acquire (happens-before relationship).

### synchronized Block vs Method Semantics

A `synchronized` method acquires the monitor **before** executing the method body and releases it **after** the method returns (or throws). For static methods, the monitor is on the `Class` object. For instance methods, it is on `this`.

```java
// Equivalent:
void doWork() {
    synchronized (this) { // monitor on 'this'
        // method body
    }
}

// Equivalent for static:
static void doStaticWork() {
    synchronized (ClassName.class) { // monitor on Class object
        // method body
    }
}
```

**Key difference from block:** The monitor is held for the *entire* method, including any code before/after the critical section. Using a block allows finer-grained control.

## Decision Guide: Which Guarantee Do I Need?

```
Do you need atomicity of a compound operation?
├── Yes → Use synchronized or Atomic* classes
└── No →
    Do you need visibility of a single variable?
    ├── Yes, and it's a simple read/write → Use volatile
    └── Yes, but it's a compound operation → Use synchronized
    
Do you need to publish an immutable object?
├── Yes → Use final fields (no sync needed)
└── No → Do you need ordering between multiple variables?
    ├── Yes → Use synchronized (or volatile for ordered access)
    └── No → No synchronization needed
```

## Common Pitfalls

| Pitfall | Root Cause | Solution |
|---------|-----------|----------|
| Stale reads | No happens-before between writer and reader | Use `volatile` or `synchronized` |
| Word tearing on `long`/`double` | JVM may split 64-bit reads/writes into two 32-bit ops | Declare as `volatile` or use `AtomicLong` |
| Double-checked locking broken | Object construction can be reordered past reference publication | Use `volatile` on the reference field |
| Non-atomic `count++` | `volatile` doesn't make compound ops atomic | Use `AtomicInteger` or `synchronized` |
| Safe publication failure | Publishing `this` reference before constructor finishes | Use `final` fields, `volatile`, or synchronized |
| Over-synchronization | Holding locks for too long reduces concurrency | Use fine-grained locks, minimize critical sections |
| Deadlock | Thread A holds lock X, waits for Y; Thread B holds Y, waits for X | Always acquire locks in a consistent global order |

## Files

| Directory/File | Purpose |
|----------------|---------|
| [00-internals/](00-internals/) | Deep dive into happens-before mechanics |
| [01-memory/](01-memory/) | Memory visibility and cache behavior |
| [examples/](examples/) | Working examples demonstrating each JMM concept |
| [practices/](practices/) | Exercises for hands-on practice |
| [solutions/](solutions/) | Complete solutions to all exercises |
| [decision.md](decision.md) | Quick decision guide |
| [quiz.md](quiz.md) | Self-assessment quiz |
| [references.md](references.md) | JMM specification references |
