# Atomic Classes — Quiz

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

## Question 3

What happens when a CAS operation fails?

- A) The thread is blocked
- B) The thread retries the operation in a loop (spin-wait)
- C) An exception is thrown
- D) The value is rolled back

**Answer: B**
CAS is part of a compare-and-swap loop. When another thread modifies the value between read and CAS, the operation fails and retries with the updated value.

## Question 4

What is the difference between `AtomicInteger` and `AtomicStampedReference`?

- A) They are identical
- B) `AtomicStampedReference` can detect ABA problems using a version stamp
- C) `AtomicInteger` is faster
- D) `AtomicStampedReference` supports null

**Answer: B**
`AtomicStampedReference` pairs the value with a stamp (version number). This detects ABA problems where a value changes from A to B and back to A.

## Question 5

What is the ABA problem?

- A) A thread acquires the same lock twice
- B) A value changes from A to B and back to A between a read and a CAS, causing the CAS to succeed incorrectly
- C) Two threads deadlock
- D) A thread is interrupted twice

**Answer: B**
CAS sees value A, but it may have been changed to B and back to A by another thread. The CAS succeeds because it sees A, but the intermediate state matters.

## Question 6

What does `AtomicReference.compareAndSet()` do?

- A) Compares two references for equality
- B) Atomically updates the reference only if the current value matches the expected value
- C) Creates a new AtomicReference
- D) Returns the current reference value

**Answer: B**
It performs CAS on the reference itself. If the current reference is `==` to the expected value, it atomically sets it to the new value and returns `true`.

## Question 7

True or False: `AtomicBoolean` can be used to implement a simple non-reentrant lock.

**Answer: True**
`AtomicBoolean.compareAndSet(false, true)` acts as a lock acquisition, and `set(false)` acts as release. It is non-reentrant because the same thread cannot acquire it twice.

## Question 8

What is the advantage of `LongAccumulator` over `LongAdder`?

- A) It's faster
- B) It supports custom accumulation functions (not just addition)
- C) It uses less memory
- D) It's thread-safe for read operations

**Answer: B**
`LongAccumulator` accepts a `LongBinaryOperator` allowing custom aggregation (e.g., max, min, XOR). `LongAdder` only supports addition.

## Question 9

What does `AtomicMarkableReference` provide?

- A) Thread-safe reference updates only
- B) Reference updates with a boolean mark (true/false)
- C) Reference updates with a version stamp
- D) Lazy initialization of references

**Answer: B**
`AtomicMarkableReference` pairs a reference with a boolean. This is useful for marking items (e.g., "deleted" flag) without external synchronization.

## Question 10

Why are atomic classes preferred over `synchronized` for simple operations?

- A) They use less memory
- B) They are lock-free and avoid thread blocking for simple read-modify-write operations
- C) They support more operations
- D) They work with virtual threads

**Answer: B**
Atomic classes use CAS instead of locks, avoiding thread suspension/resumption overhead. For simple operations (increment, compare-and-swap), they outperform synchronized blocks.
