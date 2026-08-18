# Thread Lifecycle Quiz

## Question 1
A thread is in BLOCKED state. What does this mean?

- A) The thread is sleeping
- B) The thread is waiting for a synchronized lock
- C) The thread is waiting for a notification
- D) The thread has finished

**Answer: B**
BLOCKED means the thread is waiting to acquire a monitor lock held by another thread.

## Question 2
What causes a thread to move from WAITING to RUNNABLE?

- A) Timeout
- B) `notify()` or `unpark()` is called
- C) The lock is released
- D) `Thread.sleep()` expires

**Answer: B**
WAITING threads are released by `notify()`, `notifyAll()`, or `unpark()`.
