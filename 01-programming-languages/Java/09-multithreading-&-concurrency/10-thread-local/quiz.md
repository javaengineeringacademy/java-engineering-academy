# ThreadLocal — Quiz

## Question 1

What happens if you don't call remove() on ThreadLocal in a thread pool?

- A) Nothing
- B) Memory leak — value stays in thread's storage
- C) Exception
- D) Value is garbage collected

**Answer: B**
In thread pools, threads are reused. Previous ThreadLocal values persist until removed or the thread dies, causing memory leaks.

## Question 2

What is InheritableThreadLocal?

- A) ThreadLocal that is inherited from parent thread
- B) ThreadLocal that is synchronized
- C) ThreadLocal that is volatile
- D) ThreadLocal with default value

**Answer: A**
InheritableThreadLocal copies the parent thread's value to the child thread when the child is created.

## Question 3

How does ThreadLocal store data?

- A) In a global HashMap
- B) In each thread's own ThreadLocalMap
- C) On the heap with reference counting
- D) In CPU registers

**Answer: B**
Each Thread object contains a `ThreadLocalMap`. When you call `threadLocal.get()`, it accesses the current thread's map using the ThreadLocal as the key.

## Question 4

What is the default value returned by `ThreadLocal.get()` if no value has been set?

- A) `null`
- B) `0`
- C) An empty string
- D) Throws `IllegalStateException`

**Answer: A**
`ThreadLocal.get()` returns `null` if no value has been set. Override `initialValue()` to provide a default.

## Question 5

When is `ThreadLocal` most useful?

- A) When sharing data between threads
- B) When each thread needs its own isolated copy of a variable
- C) When implementing producer-consumer
- D) When using virtual threads

**Answer: B**
ThreadLocal provides thread-confined storage. Each thread gets its own independent copy, eliminating synchronization overhead.

## Question 6

True or False: ThreadLocal values are garbage collected when the thread terminates.

**Answer: True**
When a thread dies, its `ThreadLocalMap` is garbage collected along with all ThreadLocal values stored in it. However, in thread pools, threads don't die — that's why `remove()` is critical.

## Question 7

What problem can ThreadLocal cause in application servers?

- A) Class loader leak — ThreadLocal holds references to the class loader, preventing garbage collection during hot reload
- B) Deadlock
- C) Performance degradation only
- D) No problems

**Answer: A**
Application servers use class loaders for hot deployment. If a ThreadLocal in a pooled thread holds a reference to a class loaded by an old class loader, it prevents that class loader from being garbage collected.

## Question 8

What is the difference between `ThreadLocal.withInitial()` and overriding `initialValue()`?

- A) They are identical
- B) `withInitial()` uses a lambda; overriding is for subclasses
- C) `withInitial()` is thread-safe; overriding is not
- D) Overriding is preferred for performance

**Answer: B**
`ThreadLocal.withInitial(Supplier)` is a factory method using a lambda (Java 8+). Overriding `initialValue()` is the older approach. They are functionally equivalent.

## Question 9

What is the `ThreadLocalRandom` class used for?

- A) Generating random numbers shared between threads
- B) Generating random numbers without contention between threads
- C) Seeding random number generators
- D) Thread-safe UUID generation

**Answer: B**
`ThreadLocalRandom` maintains per-thread random state, avoiding contention on a shared `java.util.Random` instance. It's significantly faster in multi-threaded scenarios.

## Question 10

How many `ThreadLocal` variables can you create per thread?

- A) Only one
- B) Limited to 10
- C) Unlimited (limited only by memory)
- D) Limited by CPU cores

**Answer: C**
Each thread can have many `ThreadLocal` variables. They are stored in the thread's `ThreadLocalMap`, which grows dynamically.
