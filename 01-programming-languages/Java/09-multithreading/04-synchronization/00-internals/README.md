# Synchronization - Internals

## How Synchronization Works Internally

### Monitor Lock (Intrinsic Lock)

Every Java object has an associated monitor lock:
- The lock is created with the object
- It's stored in the object header (mark word)
- Only one thread can hold the monitor at a time

When a thread enters `synchronized(obj)`:
1. The JVM checks if the monitor is free
2. If free, the thread acquires the lock (sets mark word to point to stack)
3. If held by the same thread, increment reentrant count
4. If held by another thread, the current thread enters the entry set (BLOCKED)

### CAS (Compare-And-Swap) Operations

Atomic classes use hardware-level CAS:
1. Read current value
2. Compute new value
3. Attempt atomic swap: `CAS(current, newValue)`
4. If current value changed since read, retry (spin)

On x86: `LOCK CMPXCHG` instruction
On ARM: `LDREX`/`STREX` instructions

### volatile Internals

When a thread writes to a `volatile` variable:
1. The JVM emits a memory fence (StoreStore + StoreLoad)
2. The value is written to main memory (not just cache)
3. Other threads' caches are invalidated

When a thread reads a `volatile` variable:
1. The JVM emits a memory fence (LoadLoad + LoadStore)
2. The value is read from main memory (not cache)
3. Subsequent reads see the latest value

### Synchronized Method Internals

A `synchronized` method:
1. The compiler adds `ACC_SYNCHRONIZED` flag to the method
2. Before entering the method, the thread acquires the monitor
3. After the method returns (or throws), the monitor is released
4. For static methods, the monitor is on the Class object
