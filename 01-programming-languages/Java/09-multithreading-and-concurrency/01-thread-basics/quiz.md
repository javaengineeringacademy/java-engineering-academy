# Thread Basics Quiz

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
