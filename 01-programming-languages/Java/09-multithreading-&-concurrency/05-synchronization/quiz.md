# Synchronization — Quiz

## Question 1

What does `volatile` guarantee?

- A) Atomicity of compound operations
- B) Mutual exclusion
- C) Visibility of writes across threads
- D) Thread scheduling

**Answer: C**
`volatile` ensures that reads and writes go directly to main memory, making changes visible to all threads. It does NOT provide atomicity for compound operations like `count++`.

## Question 2

What causes a deadlock?

- A) Too many threads
- B) Circular lock dependency
- C) Using synchronized
- D) Using volatile

**Answer: B**
Deadlock occurs when two or more threads each hold a lock the other needs, creating a circular wait.

## Question 3

What is the difference between `synchronized` and `volatile`?

- A) They are identical
- B) `synchronized` provides mutual exclusion AND visibility; `volatile` provides visibility only
- C) `volatile` is faster and always preferred
- D) `synchronized` works on variables; `volatile` works on methods

**Answer: B**
`synchronized` ensures only one thread enters a block (mutual exclusion) and that memory changes are visible. `volatile` ensures visibility but NOT mutual exclusion.

## Question 4

What is a race condition?

- A) When two threads race to finish first
- B) When the program's behavior depends on the relative timing of threads
- C) When a thread exceeds its time slice
- D) When a thread pool runs out of threads

**Answer: B**
A race condition occurs when correctness depends on non-deterministic thread interleaving. It leads to unpredictable results, not just performance issues.

## Question 5

What is the output?

```java
class Shared {
    volatile int x = 0;
    void increment() { x++; }
}
// Thread A: s.increment(); s.increment();
// Thread B: s.increment();
// What values can x have?
```

- A) Exactly 3
- B) 2 or 3
- C) 1, 2, or 3
- D) 3 only

**Answer: B**
`volatile` guarantees visibility but `x++` is read-modify-write (not atomic). Two threads reading the same value before either writes causes a lost update. The minimum is 2 (two reads of 0, two writes of 1, one write of 2).

## Question 6

What is a monitor in Java?

- A) A CPU scheduling algorithm
- B) A synchronization mechanism associated with each object
- C) A type of daemon thread
- D) A debugging tool

**Answer: B**
Every Java object has a monitor — a lock that enables mutual exclusion via `synchronized`. Only one thread can own an object's monitor at a time.

## Question 7

What is the difference between synchronized method and synchronized block?

- A) They are identical
- B) A synchronized block locks a specific object; a synchronized method locks `this`
- C) A synchronized block is always faster
- D) A synchronized method can only be static

**Answer: B**
`synchronized(this) { ... }` is equivalent to a synchronized method. A synchronized block allows locking on any object and provides finer-grained control over scope.

## Question 8

What is the lost update problem?

- A) When a thread deletes data
- B) When two threads read the same value and both write updates, one write is lost
- C) When a thread updates a value to null
- D) When an update is not visible to the main thread

**Answer: B**
If Thread A reads `x=5` and Thread B reads `x=5` simultaneously, both compute `x=6`, and both write `6`. One increment is lost. `synchronized` or atomic classes prevent this.

## Question 9

True or False: A synchronized method can call a non-synchronized method on the same object without releasing the lock.

**Answer: True**
The lock is held for the entire duration of the synchronized method. Calling other methods on the same object does not release it.

## Question 10

Which of the following operations on an `AtomicInteger` is lock-free?

- A) `get()`
- B) `set()`
- C) `compareAndSet()`
- D) All of the above

**Answer: D**
All `AtomicInteger` operations use CAS (Compare-And-Swap) or volatile reads/writes, making them lock-free. No explicit synchronization is needed.
