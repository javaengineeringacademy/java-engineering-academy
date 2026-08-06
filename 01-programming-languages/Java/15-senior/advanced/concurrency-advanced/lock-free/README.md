# Lock-Free Programming

## Overview

Lock-free programming uses atomic operations (like CAS) instead of traditional locks to achieve thread-safe data structures. This can provide better performance under high contention.

## Lock-Free vs Lock-Based

| Aspect | Lock-Based | Lock-Free |
|--------|-----------|-----------|
| Blocking | Yes | No |
| Deadlocks | Possible | Impossible |
| Priority inversion | Possible | Impossible |
| Throughput under contention | Lower | Higher |
| Implementation complexity | Lower | Higher |
| Progress guarantee | Blocking | Non-blocking |

## CAS Algorithm

Compare-And-Swap (CAS) is the fundamental building block:

```
CAS(memory, expected, new_value):
    if memory == expected:
        memory = new_value
        return true  // Success
    else:
        return false // Failure (retry)
```

### Java Atomic Classes
- `AtomicInteger`, `AtomicLong` - Numeric atomics
- `AtomicReference<T>` - Reference atomics
- `AtomicStampedReference<T>` - With version stamp (ABA prevention)
- `AtomicMarkableReference<T>` - With boolean mark

### CAS Retry Pattern
```java
int attempts = 0;
while (true) {
    int current = counter.get();
    int next = calculateNewValue(current);
    attempts++;
    if (counter.compareAndSet(current, next)) {
        break; // Success
    }
    // CAS failed, retry
}
```

## ABA Problem

### What is ABA?
1. Thread 1 reads value A
2. Thread 2 changes A -> B
3. Thread 2 changes B -> A
4. Thread 1's CAS succeeds (sees A), but state may be inconsistent

### Solutions

1. **AtomicStampedReference** - Adds version number (stamp)
2. **AtomicMarkableReference** - Adds boolean mark
3. **Hazard pointers** - Advanced memory reclamation
4. **Epoch-based reclamation** - Similar to hazard pointers

## When to Use Lock-Free

### Good Candidates
- Counters and accumulators
- Simple linked structures
- Single-producer/single-consumer queues
- When contention is high

### Poor Candidates
- Complex multi-step operations
- When operations need to be atomic across multiple variables
- When implementation complexity outweighs benefits

## Performance Considerations

- Under low contention: Lock-free may be slower (CAS overhead)
- Under high contention: Lock-free usually wins
- Always benchmark with realistic workloads

## Interview Questions

1. **What is the ABA problem and how do you solve it?** — ABA occurs when a value changes from A to B and back to A between a read and CAS. Use `AtomicStampedReference` with version stamps to detect this.

2. **When would you use lock-free over synchronized?** — Under high contention where threads frequently block. Lock-free avoids deadlocks and priority inversion but has higher implementation complexity.

3. **What is CAS and how does it work?** — Compare-And-Swap atomically compares a memory location with an expected value and swaps if equal. It is a hardware-level instruction (CMPXCHG on x86).

4. **What are the downsides of lock-free programming?** — ABA problem, livelock under extreme contention, harder to debug, memory reclamation complexity (hazard pointers, epoch-based).

5. **How does `AtomicInteger` differ from `synchronized int`?** — AtomicInteger uses CAS (non-blocking); synchronized uses monitor locks (blocking). Under low contention, synchronized may be faster; under high contention, AtomicInteger wins.

6. **What is a spin lock and when is it useful?** — A lock that busy-waits instead of blocking. Useful for very short critical sections where context switch overhead exceeds spin time.

## Pitfalls

1. **ABA problem**: CAS can succeed even when value changed A→B→A — use `AtomicStampedReference`
2. **Livelock**: Thread repeatedly retries CAS without progress — add backoff or randomization
3. **Memory reclamation**: Lock-free data structures need careful memory management (hazard pointers)
4. **False sharing**: CAS on adjacent fields causes cache line bouncing — use padding
5. **Over-engineering**: Simple operations don't need lock-free — `synchronized` is often faster under low contention

## Examples

```java
// Lock-free counter
class LockFreeCounter {
    private final AtomicInteger count = new AtomicInteger(0);
    
    void increment() {
        count.incrementAndGet();
    }
    
    int get() {
        return count.get();
    }
}

// CAS retry pattern
class CASRetryExample {
    private final AtomicInteger value = new AtomicInteger(0);
    
    int computeNewValue(int current) {
        return current * 2 + 1;
    }
    
    void update() {
        int attempts = 0;
        while (true) {
            int current = value.get();
            int next = computeNewValue(current);
            if (value.compareAndSet(current, next)) {
                System.out.println("Updated after " + attempts + " attempts");
                break;
            }
            attempts++;
        }
    }
}
```

## Internal Working

Lock-free algorithms use hardware atomic instructions (CAS) to ensure thread safety without locks. When a CAS fails (another thread modified the value), the operation retries with the new value. The JVM provides `sun.misc.Unsafe` for CAS operations, wrapped by `java.util.concurrent.atomic` classes. The hardware guarantees atomicity of the CAS instruction on a single memory word.

## Why This Concept Exists

Traditional locks have issues: deadlocks, priority inversion, and thread blocking under contention. Lock-free algorithms avoid these by using non-blocking atomic operations. At least one thread is always guaranteed to make progress, preventing system-wide stalls. This is critical for high-throughput systems: message queues, counters, and concurrent data structures.

## See Also

- `LockFreeProgramming.java` - Practical examples
- `FalseSharingDemo.java` - Performance impact of cache effects

## Performance

Lock-free operations are O(1) but may retry multiple times under contention. Under low contention, CAS is faster than locks (~20ns vs ~100ns). Under high contention, lock-free provides better throughput because threads don't block. The JVM's biased locking and lock coarsening can make `synchronized` competitive for uncontended cases. Always benchmark with realistic workloads.

## References

- [Java Concurrent Atomic Package](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/atomic/package-summary.html)
- [The Art of Multiprocessor Programming](https://www.amazon.com/Art-Multiprocessor-Programming-Revised-Reprint/dp/0123705916)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)
