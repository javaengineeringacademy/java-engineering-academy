# 04. Memory Model - Quiz

## Questions

### Q1: Happens-Before
What is the happens-before relationship?
- A) A total ordering of all memory operations
- B) A guarantee that memory writes by one action are visible to subsequent actions by another thread
- C) A guarantee that operations execute in program order
- D) A mechanism for garbage collecting unused objects

**Answer: B**
Explanation: Happens-before ensures that memory writes by one action are visible to subsequent reads by another thread. It is not a total ordering; it defines visibility guarantees, not execution order.

### Q2: Volatile
What does the `volatile` keyword guarantee?
- A) Atomicity of compound operations
- B) Mutual exclusion
- C) Visibility of writes and happens-before ordering
- D) Thread confinement

**Answer: C**
Explanation: Volatile guarantees that writes to a volatile variable are visible to subsequent reads by any thread. It establishes a happens-before relationship. It does NOT provide atomicity for compound operations (use AtomicLong for that).

### Q3: Synchronized
What happens-before relationship does synchronized establish?
- A) An unlock on a monitor happens-before every subsequent lock on the same monitor
- B) A write to a volatile variable happens-before every subsequent read
- C) A thread's start() happens-before any action in the started thread
- D) All actions in a thread happen-before any action in another thread

**Answer: A**
Explanation: When a thread exits a synchronized block (unlock), all memory writes are visible to the next thread that enters a synchronized block on the same monitor (lock).

### Q4: Memory Barriers
What is a memory barrier (fence)?
- A) A synchronization primitive like a lock
- B) A CPU instruction that restricts reordering of memory operations
- C) A garbage collection phase
- D) A JVM flag for memory configuration

**Answer: B**
Explanation: Memory barriers are CPU instructions that prevent the compiler and CPU from reordering memory operations across the barrier. They enforce the happens-before guarantees specified by the JMM.

### Q5: Thread.start()
What happens-before relationship is established by Thread.start()?
- A) Actions before start() are visible to the started thread
- B) Actions in the started thread are visible to the calling thread
- C) There is no happens-before relationship
- D) Only the most recent write is visible

**Answer: A**
Explanation: Every action in the thread that calls start() happens-before any action in the started thread. This ensures the started thread sees all initialization done before starting.

### Q6: Final Fields
Why are final fields special in the JMM?
- A) They are always thread-safe without synchronization
- B) The JMM guarantees that final field values are visible to all threads after the constructor completes
- C) They cannot be modified after construction
- D) They are stored in a special memory area

**Answer: B**
Explanation: The JMM provides a special guarantee for final fields: once a constructor completes and the object reference is published, all threads see the correct values of final fields without synchronization.

### Q7: Data Race
What defines a data race?
- A) Two threads accessing the same variable
- B) Two threads accessing the same variable, at least one is a write, and no synchronization coordinates the access
- C) Two threads accessing different variables
- D) A thread accessing a volatile variable

**Answer: B**
Explanation: A data race occurs when two threads access the same variable concurrently, at least one access is a write, and there is no happens-before relationship between the accesses.

### Q8: Atomic Variables
When should you use AtomicInteger instead of volatile int?
- A) When you need only visibility guarantees
- B) When you need atomicity for compound operations like increment
- C) When you want to use less memory
- D) When you only read the variable

**Answer: B**
Explanation: Volatile provides visibility but not atomicity for compound operations. AtomicInteger uses CAS (compare-and-swap) to ensure atomicity for operations like incrementAndGet().

### Q9: Thread.join()
What happens-before relationship does Thread.join() establish?
- A) Actions before start() are visible to the joined thread
- B) All actions in the joined thread happen-before the thread that calls join() returns
- C) There is no happens-before relationship
- D) Only the last action in the joined thread is visible

**Answer: B**
Explanation: When a thread terminates and join() returns, all actions in the terminated thread happen-before any action in the thread that called join().

### Q10: Safe Publication
What is safe publication?
- A) Publishing an object reference using a volatile variable or synchronized block
- B) Making an object reference visible to other threads before the object is fully constructed
- C) Storing an object in a static field
- D) Returning an object from a method

**Answer: A**
Explanation: Safe publication ensures that all threads see a fully constructed object. It requires a happens-before relationship between the construction and the publication (volatile, synchronized, or final fields).

## Score Guide
- **9-10 correct**: JMM expert
- **7-8 correct**: Solid understanding, review edge cases
- **5-6 correct**: Good start, study happens-before rules
- **Below 5**: Review basics before proceeding
