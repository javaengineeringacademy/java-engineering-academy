# Thread Basics — Quiz

## Question 1

What happens when you call `t.run()` instead of `t.start()`?

- A) Creates a new thread
- B) Runs the code in the current thread (no new thread)
- C) Throws an exception
- D) Does nothing

**Answer: B**
`run()` is just a method call. `start()` creates a new thread and calls `run()` in that thread.

## Question 2

What is a daemon thread?

- A) A thread that runs forever
- B) A thread that doesn't prevent JVM shutdown
- C) A thread with highest priority
- D) A thread that cannot be interrupted

**Answer: B**
Daemon threads are background threads (like garbage collection) that the JVM can terminate when all user threads finish.

## Question 3

What is the default name of a thread created with `new Thread()`?

- A) `daemon-0`
- B) `Thread-0`
- C) `main-thread`
- D) `UnnamedThread`

**Answer: B**
The default naming convention is `Thread-N` where N is a monotonically increasing counter starting at 0.

## Question 4

What does `Thread.currentThread()` return?

- A) The main thread
- B) A reference to the thread currently executing the method
- C) The first thread created
- D) The daemon thread

**Answer: B**
`Thread.currentThread()` is a static method that returns a reference to the currently executing thread object.

## Question 5

What is the output?

```java
Thread t = new Thread(() -> {
    System.out.println(Thread.currentThread().getName());
});
t.start();
```

- A) `main`
- B) `Thread-0`
- C) `null`
- D) Throws an exception

**Answer: B**
The lambda runs inside the new thread named `Thread-0`, not the main thread.

## Question 6

True or False: A thread can call `start()` more than once.

**Answer: False**
Calling `start()` on a thread that has already been started throws `IllegalThreadStateException`. You must create a new Thread object.

## Question 7

What does `setDaemon(true)` do when called on a thread?

- A) Makes the thread run faster
- B) Marks the thread as a daemon, so it won't prevent JVM exit
- C) Stops the thread immediately
- D) Gives the thread highest priority

**Answer: B**
`setDaemon(true)` must be called before `start()`. The JVM exits when only daemon threads remain.

## Question 8

What happens when you call `Thread.yield()`?

- A) The current thread stops permanently
- B) The current thread hints to the scheduler that it is willing to give up its time slice
- C) The thread is removed from the ready queue
- D) The thread acquires a new lock

**Answer: B**
`yield()` is a hint (not a command) that the scheduler may use to give other threads a chance to run. The thread remains in the RUNNABLE state.

## Question 9

What is a `ThreadGroup`?

- A) A mechanism to execute threads in parallel
- B) A container that manages a group of threads for batch operations like interrupt
- C) A collection of all daemon threads
- D) A priority queue for threads

**Answer: B**
`ThreadGroup` provides methods to manage multiple threads at once (e.g., `interrupt()`, `enumerate()`). It is largely deprecated since Java 5 in favor of `ExecutorService`.

## Question 10

What is the `Runnable` interface's single method?

- A) `call()`
- B) `start()`
- C) `run()`
- D) `execute()`

**Answer: C**
`Runnable` defines a single `run()` method that returns `void`. It does not throw checked exceptions and cannot return a result.
