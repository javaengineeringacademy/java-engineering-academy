# Atomic Classes Quiz

## Question 1
What does CAS stand for and why is it used?

- A) Concurrent Application Scheduling
- B) Compare-And-Swap — hardware instruction for atomic updates
- C) Concurrent Access Synchronization
- D) Cache Allocation System

**Answer: B**
CAS atomically compares the current value with an expected value and, if they match, updates to a new value. It's the foundation of lock-free algorithms.

## Question 2
When should you use LongAdder instead of AtomicLong?

- A) Always
- B) When many threads update the same counter (better throughput)
- C) When you need CAS
- D) When you need AtomicReference semantics

**Answer: B**
LongAdder uses stripe-based aggregation to reduce contention. It's faster for high-contention counters but uses more memory.
