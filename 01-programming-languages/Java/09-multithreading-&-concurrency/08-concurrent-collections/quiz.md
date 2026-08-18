# Concurrent Collections — Quiz

## Question 1

Why is ConcurrentHashMap faster than Collections.synchronizedMap()?

- A) It uses no locks
- B) It uses lock striping (multiple locks for different segments)
- C) It uses volatile
- D) It uses atomic operations

**Answer: B**
ConcurrentHashMap divides the map into segments, each with its own lock. This allows concurrent reads and writes to different segments.

## Question 2

What happens when you iterate over CopyOnWriteArrayList while another thread modifies it?

- A) ConcurrentModificationException
- B) The iterator sees a snapshot of the list at creation time
- C) The iterator blocks
- D) Undefined behavior

**Answer: B**
CopyOnWriteArrayList creates a new copy of the array on each write. Iterators operate on the snapshot, so they never see concurrent modifications.

## Question 3

Which concurrent collection should you use for a high-throughput queue with blocking behavior?

- A) `Collections.synchronizedList()`
- B) `ArrayBlockingQueue` or `LinkedBlockingQueue`
- C) `ConcurrentHashMap`
- D) `CopyOnWriteArrayList`

**Answer: B**
`BlockingQueue` implementations provide `put()` (blocks when full) and `take()` (blocks when empty), making them ideal for producer-consumer patterns.

## Question 4

What is the difference between `ArrayBlockingQueue` and `LinkedBlockingQueue`?

- A) They are identical
- B) `ArrayBlockingQueue` is bounded and array-based; `LinkedBlockingQueue` can be bounded or unbounded
- C) `LinkedBlockingQueue` is faster for all workloads
- D) `ArrayBlockingQueue` supports null elements

**Answer: B**
`ArrayBlockingQueue` requires a fixed capacity at construction. `LinkedBlockingQueue` defaults to `Integer.MAX_VALUE` capacity (effectively unbounded) and uses linked nodes.

## Question 5

What happens if you put a null key in ConcurrentHashMap?

- A) It's stored as null
- B) NullPointerException is thrown
- C) The entry is silently ignored
- D) The map creates a sentinel value

**Answer: B**
ConcurrentHashMap (like HashMap since JDK 8) does not allow null keys or values. `put(null, ...)` throws `NullPointerException`.

## Question 6

What is the `ConcurrentLinkedQueue` suitable for?

- A) Blocking producer-consumer
- B) Non-blocking, lock-free FIFO queue for single-producer/single-consumer
- C) Thread-safe sorted operations
- D) Bounded buffers

**Answer: B**
`ConcurrentLinkedQueue` is a lock-free, unbounded queue. It's ideal for scenarios where contention is low and you don't need blocking semantics.

## Question 7

True or False: `CopyOnWriteArrayList` is efficient for lists that are modified frequently.

**Answer: False**
Every write creates a new array copy, making it expensive for write-heavy workloads. It is designed for read-heavy, write-rare scenarios like listener lists.

## Question 8

What does `ConcurrentSkipListMap` provide that `ConcurrentHashMap` does not?

- A) Better concurrency
- B) Sorted key ordering with O(log n) operations
- C) Null key support
- D) Blocking operations

**Answer: B**
`ConcurrentSkipListMap` is a concurrent sorted map based on skip lists. It maintains keys in sorted order, unlike `ConcurrentHashMap` which has no ordering guarantees.

## Question 9

What is the difference between `size()` on `ConcurrentHashMap` and `HashMap`?

- A) They are identical
- B) `ConcurrentHashMap.size()` is approximate under concurrent modification
- C) `HashMap.size()` is always exact
- D) `ConcurrentHashMap` does not support `size()`

**Answer: B**
`ConcurrentHashMap.size()` may not be perfectly accurate under concurrent modification because counting across segments is not atomic. `HashMap.size()` is exact in a single-threaded context.

## Question 10

Which collection does NOT throw `ConcurrentModificationException` when modified during iteration?

- A) `ArrayList`
- B) `HashMap`
- C) `CopyOnWriteArrayList`
- D) Both A and B

**Answer: C**
`CopyOnWriteArrayList` iterators work on a snapshot. `ArrayList` and `HashMap` iterators throw `ConcurrentModificationException` on concurrent structural modification.
