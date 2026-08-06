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

## See Also

- `LockFreeProgramming.java` - Practical examples
- `FalseSharingDemo.java` - Performance impact of cache effects
