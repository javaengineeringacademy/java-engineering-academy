# Thread Lifecycle — Quiz

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

## Question 3

What are the six thread states in Java?

- A) NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED
- B) CREATED, READY, RUNNING, SLEEPING, BLOCKED, DONE
- C) INIT, START, WAIT, BLOCK, STOP, END
- D) NEW, ACTIVE, PAUSED, SLEEPING, TERMINATED

**Answer: A**
The six states are defined in `Thread.State`: NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED.

## Question 4

Which method moves a thread from NEW to RUNNABLE?

- A) `run()`
- B) `start()`
- C) `resume()`
- D) `notify()`

**Answer: B**
`start()` transitions a thread from NEW to RUNNABLE. The OS scheduler then decides when it actually runs.

## Question 5

What causes a thread to enter TIMED_WAITING?

- A) `Object.wait()` without timeout
- B) `Thread.sleep(duration)` or `Object.wait(duration)`
- C) `Thread.interrupt()`
- D) `Thread.start()`

**Answer: B**
TIMED_WAITING is entered when a thread calls a time-bounded waiting method: `sleep(ms)`, `wait(ms)`, `join(ms)`, `LockSupport.parkNanos(ms)`, etc.

## Question 6

True or False: A thread in TERMINATED state can be restarted by calling `start()`.

**Answer: False**
Once a thread terminates, it cannot be restarted. Calling `start()` throws `IllegalThreadStateException`. You must create a new Thread instance.

## Question 7

What is the difference between WAITING and TIMED_WAITING?

- A) WAITING uses a timeout; TIMED_WAITING does not
- B) WAITING has no timeout; TIMED_WAITING has a maximum wait duration
- C) WAITING is for daemon threads only
- D) There is no difference

**Answer: B**
WAITING (from `wait()`, `join()`, `LockSupport.park()`) has no timeout — the thread waits indefinitely. TIMED_WAITING (from `wait(ms)`, `sleep(ms)`, etc.) returns after the specified duration or when signaled.

## Question 8

A thread is in BLOCKED state. Another thread calls `Thread.interrupt()` on it. What happens?

- A) The thread remains BLOCKED
- B) The thread receives the interrupted flag and moves to RUNNABLE
- C) An `InterruptedException` is thrown
- D) The thread is terminated

**Answer: A**
`interrupt()` does not wake a BLOCKED thread. It sets the interrupt flag, which will be recognized if the thread later enters a method that responds to interruption (like `wait()`, `sleep()`, `join()`).

## Question 9

What does `Thread.getState()` return for a thread that has never been started?

- A) `RUNNABLE`
- B) `NEW`
- C) `TERMINATED`
- D) `null`

**Answer: B**
A thread that has been constructed but not yet started has state `NEW`.

## Question 10

What is the difference between `isAlive()` returning true and `Thread.State.RUNNABLE`?

- A) They are identical
- B) `isAlive()` is true for any started thread that hasn't terminated; `RUNNABLE` is one specific state
- C) `isAlive()` is only true for daemon threads
- D) `RUNNABLE` is broader than `isAlive()`

**Answer: B**
A thread is alive from `start()` until it terminates, covering all intermediate states (RUNNABLE, BLOCKED, WAITING, TIMED_WAITING). `RUNNABLE` specifically means the thread is eligible to run.
