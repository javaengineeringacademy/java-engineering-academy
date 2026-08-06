# Java Memory Model — The Complete Guide

## Table of Contents

1. [What Is the JMM](#1-what-is-the-jmm)
2. [Shared Variables vs Local Variables](#2-shared-variables-vs-local-variables)
3. [Actions](#3-actions)
4. [Happens-Before Order](#4-happens-before-order)
5. [Memory Visibility](#5-memory-visibility)
6. [Volatile Semantics](#6-volatile-semantics)
7. [Final Field Semantics](#7-final-field-semantics)
8. [Safe Publication](#8-safe-publication)
9. [Common JMM Mistakes](#9-common-jmm-mistakes)
10. [JMM in Practice](#10-jmm-in-practice)

---

## 1. What Is the JMM

The Java Memory Model is a formal specification (JSR-133) that defines how threads interact through memory. It answers one critical question: **when is a write by one thread guaranteed to be visible to a read by another thread?**

Without the JMM, the JVM is free to reorder operations, cache values in registers, and optimize away memory accesses in ways that break concurrent programs. The JMM establishes the rules that constrain these optimizations and define the legal behaviors of concurrent code.

### Core Abstraction

The JMM models memory as two layers:

```
┌─────────────────────────────────────────┐
│         Thread Local Memory             │
│  (Registers, Caches, Store Buffers)     │
├─────────────────────────────────────────┤
│         Main Memory (Heap)              │
│  (Shared variables live here)           │
└─────────────────────────────────────────┘
```

Each thread has its own working memory (caches, registers) where it keeps copies of shared variables. Threads communicate by writing to main memory and reading from main memory. The JMM defines the rules for when these copies must be refreshed.

### What the JMM Does NOT Define

- **Thread scheduling**: Which thread runs when is OS-dependent
- **Atomicity of primitives**: Only `long`/`double` non-volatile are not guaranteed atomic (practical note: all modern JVMs make them atomic)
- **Memory layout**: How objects are physically arranged in memory

---

## 2. Shared Variables vs Local Variables

Understanding which variables are shared is fundamental to the JMM.

### Local Variables

Local variables are stored on the stack and are **thread-private**. No synchronization is needed.

```java
public void calculate() {
    int x = 10;        // local — thread-private
    int y = x + 5;     // local — thread-private
    // No JMM concerns here
}
```

### Shared Variables

Shared variables are accessible by multiple threads. They live on the heap (instance fields, static fields) or are reachable from shared references.

```java
public class Counter {
    private int count;          // shared — accessible by all threads holding a reference

    public void increment() {
        count++;                // NOT atomic: read, add, write
    }

    public int getCount() {
        return count;           // may see stale value without synchronization
    }
}
```

### Categories of Shared Variables

| Location | Shared? | Example |
|----------|---------|---------|
| Stack (local variable) | No | `int x = 5;` |
| Heap (instance field) | Yes | `this.count` |
| Heap (static field) | Yes | `Config.MAX_SIZE` |
| Heap (object reachable from shared ref) | Yes | `list.get(0)` where `list` is shared |

---

## 3. Actions

The JMM defines six types of actions that threads can perform on shared variables:

### 3.1 Read

A read loads a value from main memory into a thread's working memory.

```java
int localCopy = sharedVariable;  // read action
```

### 3.2 Write

A write stores a value from working memory to main memory.

```java
sharedVariable = 42;  // write action
```

### 3.3 Lock

A lock action associates a monitor with a thread (entering a `synchronized` block).

```java
synchronized (lock) {  // lock action
    // ...
}
```

### 3.4 Unlock

An unlock action disassociates a monitor from a thread (exiting a `synchronized` block).

```java
synchronized (lock) {
    // ...
}  // unlock action
```

### 3.5 Use

A use action reads a value from working memory and executes using that value (method invocation, operand for an instruction).

```java
int result = localCopy + 1;  // use action on localCopy
```

### 3.6 Store

A store action writes a value from working memory to the thread's local copy (not yet main memory).

```java
localCopy = newValue;  // store action
```

### Action Flow

```
Thread A                    Main Memory                 Thread B
────────                    ───────────                 ────────
   │                            │                          │
   │──── write x=1 ────────────>│                          │
   │                            │<──── read x ─────────────│
   │                            │     (may see stale!)     │
   │                            │                          │
```

---

## 4. Happens-Before Order

The happens-before relationship is the core mechanism of the JMM. If action A happens-before action B, then A's effects are guaranteed to be visible to B.

### Complete Rules

The JMM defines six happens-before rules:

#### Rule 1: Program Order Rule
Within a single thread, each action happens-before every subsequent action in program order.

```java
int a = 1;     // A
int b = a + 1; // B
// A happens-before B (same thread, program order)
```

#### Rule 2: Monitor Lock Rule
An unlock on a monitor happens-before every subsequent lock on that same monitor.

```java
// Thread 1
synchronized (lock) {
    sharedData = 42;       // write
}                          // unlock happens-before next lock

// Thread 2
synchronized (lock) {      // lock happens-after unlock
    use(sharedData);       // guaranteed to see 42
}
```

#### Rule 3: Volatile Variable Rule
A write to a volatile field happens-before every subsequent read of that field.

```java
// Thread 1
volatile boolean ready = true;   // write

// Thread 2
if (ready) {                     // read — guaranteed to see true
    start();
}
```

#### Rule 4: Thread Start Rule
A call to `Thread.start()` happens-before any action in the started thread.

```java
sharedData = "ready";            // before start()
Thread t = new Thread(() -> {
    use(sharedData);             // guaranteed to see "ready"
});
t.start();
```

#### Rule 5: Thread Termination Rule
All actions in a thread happen-before any other thread returns from a successful `join()` on that thread.

```java
Thread t = new Thread(() -> {
    sharedResult = compute();    // action in thread
});
t.start();
t.join();                        // join returns
use(sharedResult);               // guaranteed to see computed value
```

#### Rule 6: Transitivity
If A happens-before B, and B happens-before C, then A happens-before C.

```
A hb B  and  B hb C  =>  A hb C
```

### Happens-Before Summary Diagram

```
Program Order ──────┐
                    │
Monitor Lock ───────┤
                    │
Volatile Variable ──┼──> HAPPENS-BEFORE ──> Visibility Guarantee
                    │
Thread Start ───────┤
                    │
Thread Join ────────┤
                    │
Transitivity ───────┘
```

---

## 5. Memory Visibility

Memory visibility refers to whether a write to a shared variable by one thread is seen by a read from another thread.

### Without Synchronization (Broken)

```java
public class VisibilityProblem {
    private boolean running = true;

    public void start() {
        new Thread(() -> {
            while (running) {       // may loop forever!
                // CPU may cache 'running' as true
            }
        }).start();
    }

    public void stop() {
        running = false;            // may never be seen by the other thread
    }
}
```

The JVM, CPU, and compiler may:
1. Cache `running` in a CPU register (never re-read from main memory)
2. Reorder the write to `running = false` after other operations
3. Keep the loop tight in a register without re-fetching

### With Synchronization (Correct)

```java
public class VisibilityFix {
    private volatile boolean running = true;  // volatile guarantees visibility

    public void start() {
        new Thread(() -> {
            while (running) {
                // guaranteed to re-read from main memory
            }
        }).start();
    }

    public void stop() {
        running = false;  // guaranteed visible to other threads
    }
}
```

### When Is Visibility Guaranteed?

| Mechanism | Visibility Guarantee |
|-----------|---------------------|
| `synchronized` (unlock before read) | Yes |
| `volatile` write before volatile read | Yes |
| `Thread.start()` | Yes (for actions before start) |
| `Thread.join()` | Yes (for actions in joined thread) |
| No synchronization | **No guarantee** |

---

## 6. Volatile Semantics

The `volatile` keyword provides two guarantees:

### 6.1 Visibility Guarantee
A write to a volatile variable is immediately flushed to main memory. A read from a volatile variable always goes to main memory.

```java
volatile int counter = 0;

// Thread 1
counter++;   // write — visible to all threads immediately

// Thread 2
int val = counter;  // read — always sees latest write
```

### 6.2 Ordering Guarantee (Memory Barrier)
Volatile operations act as memory barriers:
- **Write barrier**: All prior writes are flushed before the volatile write
- **Read barrier**: All subsequent reads happen after the volatile read

```java
private int a = 0;
private int b = 0;
volatile boolean ready = false;

// Writer thread
a = 1;                    // regular write
b = 2;                    // regular write
ready = true;             // volatile write — orders a and b before ready

// Reader thread
if (ready) {              // volatile read
    // guaranteed to see a=1 and b=2
    use(a);
    use(b);
}
```

### Volatile Does NOT Provide Atomicity

```java
volatile int count = 0;

// NOT thread-safe:
count++;  // This is: read count, add 1, write count
          // Another thread can interleave between read and write
```

For compound operations, use `synchronized` or `AtomicInteger`.

### When to Use volatile

- **State flags**: `volatile boolean shutdownRequested`
- **Double-checked locking**: `volatile` instance reference
- **Immutable objects**: Ensuring safe publication
- **Not for**: counters, accumulators, compound operations

---

## 7. Final Field Semantics

The JMM provides special guarantees for `final` fields of objects. Once a constructor completes, the values assigned to `final` fields are guaranteed to be visible to all threads — without synchronization.

### The Problem Without Final Fields

```java
public class Unsafe {
    int x;
    int y;

    public Unsafe(int x, int y) {
        this.x = x;    // regular field
        this.y = y;    // regular field
    }
}

// Thread 1
Unsafe obj = new Unsafe(1, 2);
sharedRef = obj;    // publish reference

// Thread 2
Unsafe local = sharedRef;
// local.x MIGHT be 0 (default) — reordering can expose partially constructed object!
// local.y MIGHT be 0 (default)
```

### The Solution With Final Fields

```java
public class Safe {
    final int x;
    final int y;

    public Safe(int x, int y) {
        this.x = x;    // final field
        this.y = y;    // final field
    }
}

// Thread 1
Safe obj = new Safe(1, 2);
sharedRef = obj;    // publish reference

// Thread 2
Safe local = sharedRef;
// local.x is GUARANTEED to be 1
// local.y is GUARANTEED to be 2
// (as long as constructor completed before reference was published)
```

### Rules for Final Field Semantics

1. The constructor must not allow `this` to escape during construction
2. The reference must not be published until the constructor completes
3. The guarantee applies to all `final` fields (primitive and reference)

### Practical Use

```java
public class Config {
    private final String host;
    private final int port;
    private final List<String> options;  // reference — contents not immutable!

    public Config(String host, int port, List<String> options) {
        this.host = host;
        this.port = port;
        this.options = List.copyOf(options);  // defensive copy for immutability
    }
}
```

---

## 8. Safe Publication

Safe publication ensures that an object's state is fully visible to other threads when they see the reference.

### Unsafe Publication

```java
// BAD: Reference published before constructor completes
public class Factory {
    private static Factory instance;

    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();  // another thread may see partially constructed object
        }
        return instance;
    }
}
```

### Safe Publication Mechanisms

| Mechanism | How It Works |
|-----------|-------------|
| **Volatile** | Write to volatile reference happens-before read of that reference |
| **Synchronized** | Unlock happens-before next lock on same monitor |
| **Final fields** | Constructor completion + no premature publication |
| **Static initializer** | JVM guarantees safe publication of static fields |
| **Concurrent collection** | `ConcurrentHashMap`, `CopyOnWriteArrayList`, etc. |

### Safe Initialization Patterns

#### Eager Initialization
```java
public class Config {
    private static final Config INSTANCE = new Config();  // safe: static initializer
}
```

#### Lazy Initialization with Double-Checked Locking
```java
public class Config {
    private static volatile Config instance;  // volatile required

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();  // safe: volatile write
                }
            }
        }
        return instance;
    }
}
```

#### Bill Pugh Initialization
```java
public class Config {
    private static class Holder {
        static final Config INSTANCE = new Config();  // safe: class loading
    }

    public static Config getInstance() {
        return Holder.INSTANCE;
    }
}
```

#### Thread-Local Storage
```java
private static final ThreadLocal<Config> CONFIG = ThreadLocal.withInitial(Config::new);
```

---

## 9. Common JMM Mistakes

### Mistake 1: DCL Without Volatile

```java
// BROKEN
public class Singleton {
    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {              // 1st check (no lock)
            synchronized (Singleton.class) {
                if (instance == null) {      // 2nd check (with lock)
                    instance = new Singleton();  // may be seen partially constructed
                }
            }
        }
        return instance;
    }
}

// CORRECT
public class Singleton {
    private static volatile Singleton instance;  // volatile!

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### Mistake 2: Non-Volatile State Flag

```java
// BROKEN
private boolean stopped = false;

public void stop() {
    stopped = true;
}

public void run() {
    while (!stopped) {    // may loop forever
        doWork();
    }
}

// CORRECT
private volatile boolean stopped = false;
```

### Mistake 3: Publishing This in Constructor

```java
// BROKEN
public class EventListener {
    public EventListener(EventBus bus) {
        bus.register(this);  // 'this' escapes before constructor completes
    }
}

// CORRECT
public class EventListener {
    public static EventListener create(EventBus bus) {
        EventListener listener = new EventListener();
        bus.register(listener);  // 'this' published after construction
        return listener;
    }
}
```

### Mistake 4: Relying on Atomicity of long/double

```java
// Technically non-atomic (but all modern JVMs make it atomic)
// The JMM only guarantees atomicity for int, short, char, byte, float, boolean
long counter = 0;
counter++;  // not guaranteed atomic by JMM spec (but practically is on all JVMs)
```

### Mistake 5: Using Final for Immutability of Mutable Objects

```java
// NOT immutable — list contents can be modified
final List<String> items = new ArrayList<>();
items.add("hello");  // legal! only the reference is final

// Actually immutable
final List<String> items = List.of("hello", "world");  // unmodifiable
```

---

## 10. JMM in Practice

### Choosing the Right Tool

```
Need to protect?
├── Simple flag/state ──────────────> volatile
├── Compound operation ─────────────> synchronized or Lock
├── Atomic counter ─────────────────> AtomicInteger / LongAdder
├── Thread-safe map ───────────────> ConcurrentHashMap
├── Immutable object ──────────────> final fields + defensive copy
└── Complex invariants ────────────> synchronized + wait/notify
```

### Performance Implications

| Mechanism | Overhead | Use Case |
|-----------|----------|----------|
| volatile read | ~1 CPU cycle | State flags |
| volatile write | ~10-20 CPU cycles (memory barrier) | State flags |
| uncontended synchronized | ~10-20 CPU cycles | Simple critical sections |
| contended synchronized | ~1000+ CPU cycles | High-contention paths |
| CAS (AtomicInteger) | ~10-50 CPU cycles | Lock-free counters |
| ReentrantLock | ~20-50 CPU cycles | Complex locking patterns |

### Debugging JMM Issues

```java
// Tools:
// 1. jcstress (Java Concurrency Stress Tests)
//    https://github.com/openjdk/jcstress

// 2. Thread Sanitizer (not available in Java, use jcstress instead)

// 3. Verbose GC logging for visibility issues
java -verbose:gc -XX:+PrintGCDetails MyApp

// 4. JMM visualization
// https://www.cl.cam.ac.uk/~pes20/jmm/
```

### Quick Reference

```java
// Visibility: use volatile
private volatile boolean flag;

// Atomicity: use Atomics
private final AtomicInteger counter = new AtomicInteger(0);

// Compound operations: use synchronized
synchronized (lock) {
    sharedState = newState;
}

// Safe publication: use final or volatile
private final Config config;

// Immutable objects: use final + defensive copy
public class Immutable {
    private final List<String> items;
    public Immutable(List<String> items) {
        this.items = List.copyOf(items);
    }
}
```

---

## Summary

| Concept | Description |
|---------|-------------|
| **JMM** | Formal spec defining thread-memory interaction |
| **Shared variable** | Any variable accessible by multiple threads |
| **Local variable** | Thread-private, no synchronization needed |
| **Actions** | Read, write, lock, unlock, use, store |
| **Happens-before** | Ordering guarantee ensuring visibility |
| **volatile** | Visibility + ordering (not atomicity) |
| **final fields** | Safe publication after constructor completion |
| **Safe publication** | Ensuring object state is visible on reference publication |

The JMM is not just theoretical — it directly impacts the correctness of every concurrent Java program. Understanding happens-before relationships is the key to reasoning about thread safety.

## Related Topics
- [Garbage Collection](../garbage-collection/) — How GC manages memory
- [Volatile](../../09-multithreading/04-synchronization/) — Memory visibility
- [JVM Internals](../../10-jvm-internals/) — JVM memory architecture
- [Performance](../../15-senior/performance-engineering/) — Memory performance
