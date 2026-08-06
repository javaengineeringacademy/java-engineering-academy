# HotSpot Runtime — Data Areas and Thread System

The HotSpot runtime is the backbone of the JVM. It manages memory layout, thread execution, and the core data structures that make Java programs run.

## Runtime Data Areas

The JVM specification defines several memory areas that HotSpot implements:

### Heap

The heap is where all object instances and arrays are allocated. It is shared across all threads and managed by the garbage collector.

```
Heap (managed by GC)
├── Young Generation
│   ├── Eden (new objects)
│   ├── Survivor Space 0
│   └── Survivor Space 1
└── Old Generation (long-lived objects)
```

HotSpot implements the heap as a set of regions. The exact layout depends on the selected GC:

- **G1**: Fixed-size regions (1–32 MB), heap divided into Eden, Survivor, and Old regions
- **ZGC**: Multi-phase coloring with load barriers
- **Shenandoah**: Brooks pointers for concurrent compaction
- **Serial/Parallel**: Traditional generational layout

### Method Area (Metaspace)

Metaspace stores class metadata, method bytecode, constant pool, and field/method descriptors. Since Java 8, it is allocated off-heap in native memory.

```
Metaspace (native memory)
├── Klass structures (class metadata)
├── Constant pool
├── Method bytecode
├── Annotation data
└── Vtable / itable
```

The `CompressedClassSpace` is a sub-region of Metaspace used to compress class pointers, reducing memory overhead.

### Stack

Each thread has its own stack. The stack stores:

- **Stack frames**: One per method invocation
- **Local variables**: Method parameters and local variables
- **Operand stack**: Intermediate computation results
- **Return address**: Where to resume after method return

```
Thread Stack
├── Frame N (current method)
│   ├── Local variables [0..n]
│   ├── Operand stack
│   └── Return address
├── Frame N-1 (caller)
└── Frame 0 (oldest)
```

Stack size is controlled by `-Xss` (default 512 KB to 1 MB depending on platform).

### Program Counter (PC) Register

Each thread has a private PC register holding the address of the currently executing JVM instruction. For native methods, the PC is undefined.

## Thread Implementation

### Java Thread Mapping

HotSpot maps each `java.lang.Thread` to a native OS thread:

```
java.lang.Thread
    ↓ (1:1 mapping)
Native OS Thread (pthread on Linux/macOS, Thread on Windows)
```

This is the standard "green threads are dead" model — Java uses native threads, not user-mode threads.

### Thread States

A Java thread progresses through these states:

```
NEW → RUNNABLE → (BLOCKED | WAITING | TIMED_WAITING) → TERMINATED
```

In HotSpot, the internal representation tracks:

- `_thread_new` — Just created
- `_thread_in_Java` — Executing Java code
- `_thread_in_vm` — Executing VM code
- `_thread_in_native` — Executing native code
- `_thread_blocked` — Blocked on monitor
- `_thread_trans` — Transitioning between states

### Thread Local Storage (TLS)

Each thread has private storage for:

- Current Java frame pointer
- Last Java PC / bcp (bytecode pointer)
- Pending exception
- Thread-local allocation buffer (TLAB) for fast object allocation
- Card table reference for GC write barriers

### Safepoints

A safepoint is a point where all threads are guaranteed to be in a known state. The GC needs safepoints to:

- Walk the stack roots
- Relocate objects (for compacting collectors)
- Process object graphs

HotSpot uses polling-based safepoints: the JIT compiler inserts checks at method back-edges and loop entries. When a safepoint is requested, threads poll the safepoint page and block until the operation completes.

### Thread Scheduling

HotSpot relies on the OS thread scheduler. Java threads are scheduled by the operating system, not by the JVM. The JVM provides:

- `Thread.yield()` — Hint to the scheduler (no guarantee)
- `Thread.sleep()` — Timed wait
- `Thread.join()` — Wait for another thread
- `synchronized` — Monitor-based blocking
- `java.util.concurrent.locks` — Flexible locking

### TLAB (Thread Local Allocation Buffer)

To avoid contention on the shared heap, each thread gets a private TLAB:

```
Thread 1: [TLAB 1 - 64KB] → Eden
Thread 2: [TLAB 2 - 64KB] → Eden
Thread 3: [TLAB 3 - 64KB] → Eden
```

Object allocation in a TLAB is bump-pointer allocation — extremely fast with no locking. When a TLAB fills up, the thread requests a new one from the Eden space.

## Key Source Files

| File | Purpose |
|------|---------|
| `src/hotspot/share/runtime/thread.hpp` | Thread class definition |
| `src/hotspot/share/runtime/thread.cpp` | Thread implementation |
| `src/hotspot/share/runtime/safepoint.cpp` | Safepoint management |
| `src/hotspot/share/runtime/vframe.hpp` | Virtual frame (stack walking) |
| `src/hotspot/share/gc/shared/collectedHeap.hpp` | Heap abstraction |
| `src/hotspot/share/memory/metaspace/` | Metaspace implementation |
