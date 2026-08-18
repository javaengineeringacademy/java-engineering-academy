# Thread Communication — Quiz

## Question 1

Why must `wait()` be called inside a `synchronized` block?

- A) It's a syntax requirement
- B) To prevent missed notifications (race condition)
- C) For performance
- D) It doesn't need to be

**Answer: B**
Without synchronization, a notification could be sent before the thread starts waiting, causing it to be missed.

## Question 2

What happens if you call `notify()` when no threads are waiting?

- A) Exception thrown
- B) Notification is lost (no effect)
- C) The notification is stored
- D) All threads are notified

**Answer: B**
The notification is simply lost. This is why `notifyAll()` is safer — it doesn't have this problem as often.

## Question 3

What is the difference between `notify()` and `notifyAll()`?

- A) `notify()` wakes all waiting threads; `notifyAll()` wakes one
- B) `notify()` wakes one waiting thread; `notifyAll()` wakes all
- C) They are identical
- D) `notify()` is faster than `notifyAll()`

**Answer: B**
`notify()` picks one arbitrary waiting thread to wake. `notifyAll()` wakes all threads waiting on the object's monitor. `notifyAll()` is generally safer to avoid missed wakeups.

## Question 4

What happens when a thread calls `wait()`?

- A) The thread continues running
- B) The thread releases the monitor lock and enters WAITING state
- C) The thread acquires a new lock
- D) The thread is terminated

**Answer: B**
`wait()` atomically releases the object's monitor and suspends the thread. The thread will remain in WAITING state until another thread calls `notify()` or `notifyAll()` on the same object.

## Question 5

What is the output?

```java
Object lock = new Object();
synchronized(lock) {
    lock.wait();
    System.out.println("Woken up");
}
```

- A) Prints `Woken up` immediately
- B) The thread blocks forever waiting for a notification
- C) Throws `IllegalMonitorStateException`
- D) Prints nothing

**Answer: B**
The thread enters WAITING state and no other thread calls `notify()` or `notifyAll()`, so it waits indefinitely.

## Question 6

True or False: `Thread.join()` causes the calling thread to release all locks it holds.

**Answer: False**
`join()` does not release any locks. If the calling thread holds locks while waiting in `join()`, those locks remain held, potentially causing deadlocks.

## Question 7

What is the purpose of `Thread.yield()` in thread communication?

- A) It sends a signal to another thread
- B) It hints to the scheduler that the current thread is willing to give up its time slice
- C) It forces a context switch
- D) It unblocks a waiting thread

**Answer: B**
`yield()` is a hint (not a guarantee) that the scheduler should give other threads of equal or higher priority a chance to run. The thread remains RUNNABLE.

## Question 8

What does `Thread.interrupt()` do to a thread that is in WAITING state?

- A) Nothing
- B) Throws `InterruptedException` and clears the interrupt status
- C) The thread moves to BLOCKED
- D) The thread is terminated

**Answer: B**
When a thread in `wait()`, `sleep()`, or `join()` is interrupted, it receives an `InterruptedException` and the interrupt flag is cleared.

## Question 9

What is a common pitfall of using `wait()`/`notify()` in a while loop instead of an if statement?

```java
synchronized(lock) {
    while (!condition) { wait(); }  // correct
    // vs
    // if (!condition) { wait(); } // incorrect
}
```

- A) No difference
- B) The `if` version may proceed when the condition is still false (spurious wakeup)
- C) The `while` version causes a deadlock
- D) The `if` version is more efficient

**Answer: B**
Spurious wakeups can cause a waiting thread to wake even without `notify()`. Checking the condition in a `while` loop ensures the thread rechecks before proceeding.

## Question 10

What is `CountDownLatch` used for?

- A) Locking a resource
- B) Allowing one or more threads to wait until a set of operations completes
- C) Limiting concurrent access
- D) Scheduling periodic tasks

**Answer: B**
`CountDownLatch` is initialized with a count. Threads call `await()` to wait. Each completion calls `countDown()`. When the count reaches zero, all waiting threads are released.
