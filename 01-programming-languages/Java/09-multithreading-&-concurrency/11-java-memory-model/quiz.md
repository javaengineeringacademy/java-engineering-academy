# Java Memory Model — Quiz

## Question 1

What does the happens-before relationship guarantee?

- A) Order of execution
- B) Memory visibility of writes
- C) Atomicity of operations
- D) Thread scheduling order

**Answer: B**
Hensens-before guarantees that writes by one thread are visible to reads by another thread when the happens-before relationship exists.

## Question 2

Why is volatile needed for a 64-bit variable?

- A) It's not needed
- B) JVM may split 64-bit reads/writes into two 32-bit operations
- C) For performance
- D) For atomicity

**Answer: B**
Without volatile, the JVM may perform two separate 32-bit reads/writes for long/double, causing word tearing.

## Question 3

What is the difference between the Java Memory Model and the hardware memory model?

- A) They are identical
- B) JMM defines rules for Java programs; hardware model is CPU-specific
- C) JMM is less abstract than hardware models
- D) JMM only applies to virtual threads

**Answer: B**
The JMM is an abstract model defining visibility and ordering rules for Java code. Hardware models are CPU-specific (x86 vs ARM have different memory ordering guarantees).

## Question 4

Which of the following establishes a happens-before relationship?

- A) Thread.start() happens-before any action in the started thread
- B) A volatile write happens-before any subsequent volatile read of the same variable
- C) Thread.join() happens-before any action in the joining thread after join returns
- D) All of the above

**Answer: D**
All three are happens-before rules defined by the JMM. Thread.start(), volatile read/write pairs, and Thread.join() all guarantee memory visibility.

## Question 5

What is the `final` field guarantee in the JMM?

- A) Final fields cannot be modified after construction
- B) When an object is fully constructed, all threads see the correct values of final fields
- C) Final fields are automatically synchronized
- D) Final fields prevent deadlocks

**Answer: B**
The JMM guarantees that once an object's constructor completes (the `this` reference escapes), all threads see the correct values of its final fields without synchronization.

## Question 6

What is instruction reordering and why does it matter?

- A) The CPU executes instructions in a different order than written
- B) It's a compiler optimization that can cause unexpected behavior in concurrent code
- C) It only affects single-threaded programs
- D) It's always harmful

**Answer: B**
Both compilers and CPUs can reorder instructions for optimization. In concurrent programs, this can break assumptions about visibility and ordering, which is why volatile and synchronized exist.

## Question 7

True or False: The `synchronized` block guarantees that all writes inside it are immediately visible to all threads.

**Answer: False**
Writes inside a synchronized block are visible to threads that subsequently enter a synchronized block on the SAME monitor. Without a corresponding acquire, writes are not guaranteed visible.

## Question 8

What is a "publication" in the context of the JMM?

- A) Making an object reference available to other threads
- B) Compiling Java bytecode
- C) Deploying to production
- D) Writing to a volatile variable

**Answer: A**
Safe publication means making an object's state visible to other threads without data races. volatile, final fields, or synchronized blocks ensure safe publication.

## Question 9

What happens if you read a non-volatile variable without a happens-before guarantee?

- A) You always get the latest value
- B) You may see stale or partially written values
- C) You get an exception
- D) The JVM forces a memory flush

**Answer: B**
Without a happens-before edge, the JMM allows the reader to see cached or stale values from the CPU cache or store buffer. This is the fundamental visibility problem.

## Question 10

What is the JMM's "out-of-thin-air" guarantee?

- A) Threads can create objects from nothing
- B) No thread can observe a value that was never written by any thread
- C) volatile writes are faster than synchronized writes
- D) Threads always see the most recent write

**Answer: B**
The JMM prohibits reordering or optimization that would allow a thread to observe a value that no thread ever wrote. This prevents nonsensical behavior from aggressive optimization.
