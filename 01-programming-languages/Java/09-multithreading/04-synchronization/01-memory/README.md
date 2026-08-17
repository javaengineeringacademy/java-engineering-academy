# Synchronization - Memory Model

## Java Memory Model (JMM)

### Memory Visibility Problem

```
Thread 1 (Core 1)              Thread 2 (Core 2)
┌─────────────────┐           ┌─────────────────┐
│ L1 Cache        │           │ L1 Cache        │
│ x = 10          │           │ x = 0 (stale!)  │
├─────────────────┤           ├─────────────────┤
│ L2 Cache        │           │ L2 Cache        │
├─────────────────┤           ├─────────────────┤
│ Main Memory     │           │ Main Memory     │
│ x = 10          │           │ x = 10          │
└─────────────────┘           └─────────────────┘

Without volatile/synchronized, Thread 2 may never see Thread 1's write
because the value is cached in Thread 2's L1/L2 cache.
```

### Happens-Before Relationships

The JMM defines happens-before rules:
1. **Program Order**: Within a thread, each action happens-before subsequent actions
2. **Monitor Lock**: Unlock happens-before subsequent lock on same monitor
3. **volatile**: Write happens-before subsequent read of same volatile variable
4. **Thread Start**: `start()` happens-before any action in started thread
5. **Thread Termination**: All actions in a thread happen-before `join()` returns
6. **Transitivity**: If A happens-before B, and B happens-before C, then A happens-before C

### volatile Semantics

```
volatile write:
  ┌──────────────────────────────┐
  │ 1. Write value to main memory │
  │ 2. StoreStore memory fence    │
  │ 3. StoreLoad memory fence     │
  └──────────────────────────────┘

volatile read:
  ┌──────────────────────────────┐
  │ 1. LoadLoad memory fence      │
  │ 2. LoadStore memory fence     │
  │ 3. Read value from main memory │
  └──────────────────────────────┘
```

### synchronized Memory Semantics

When a monitor is released (exit synchronized block):
1. All writes before the synchronized block are flushed to main memory
2. The monitor release creates a happens-before edge

When a monitor is acquired (enter synchronized block):
1. The local CPU cache is invalidated
2. All values are read from main memory

### Atomic Class Memory Layout

```java
AtomicInteger {
    private volatile int value; // volatile for visibility
    // Uses CAS for atomicity (no locks)
    // CAS uses native Unsafe class
}
```

CAS operations are atomic at the hardware level:
- x86: `LOCK CMPXCHG` instruction (atomic read-modify-write)
- ARM: `LDREX`/`STREX` exclusive access instructions
