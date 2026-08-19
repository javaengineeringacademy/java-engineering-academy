# 04. Memory Model Memory Details

## Memory Visibility and Hardware

### CPU Cache Coherency

Modern CPUs have cache hierarchies that create visibility issues:

```
CPU Core 0          CPU Core 1          Main Memory
┌──────────┐        ┌──────────┐        ┌──────────┐
│ L1 Cache │        │ L1 Cache │        │          │
│ x = 42   │        │ x = 0    │        │ x = 42   │
└──────────┘        └──────────┘        └──────────┘

Problem: Core 1 may never see Core 0's write
Solution: Memory barriers force cache invalidation/update
```

### Memory Barrier Instructions

On x86/x64:
```
MFENCE (Memory Fence): Full barrier
├── Prevents load-load reordering
├── Prevents store-store reordering
├── Prevents load-store reordering
└── Prevents store-load reordering

LFENCE (Load Fence): Load barrier
├── Prevents load-load reordering
└── Used for acquire semantics

SFENCE (Store Fence): Store barrier
├── Prevents store-store reordering
└── Used for release semantics
```

### Compressed Oops and Memory Model

Compressed oops affect how references are stored and read:

```
Without compressed oops:
├── Reference size: 8 bytes
├── Direct memory access
└── No special ordering needed

With compressed oops:
├── Reference size: 4 bytes
├── Shifted addressing (always aligned to 8 bytes)
└── Special ordering for CAS operations
```

### Volatile in Memory

Volatile variables have specific memory layout:

```
volatile int x;
├── Stored in normal heap memory
├── Mark word has volatile flag
├── Access uses memory barriers
├── Read: load + acquire barrier
└── Write: store + release barrier

volatile Object ref;
├── Reference itself is volatile
├── Referenced object's fields are NOT automatically volatile
└── Need synchronization for field access
```

### Thread-Local Memory

Thread-local variables have special memory handling:

```
ThreadLocal:
├── Each thread has its own copy
├── Stored in Thread's ThreadLocalMap
├── No synchronization needed
├── Memory cost: per-thread, per-ThreadLocal
└── Cleanup: remove() must be called to prevent leaks

InheritableThreadLocal:
├── Child thread inherits parent's value
├── Copied on thread creation
├── Does not track parent changes after creation
└── Memory cost: per-thread copy
```
