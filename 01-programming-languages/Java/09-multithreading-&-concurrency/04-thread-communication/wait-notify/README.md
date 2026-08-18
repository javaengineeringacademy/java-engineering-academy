# Wait/Notify in Java

## What Are wait(), notify(), notifyAll()?

These are **Object methods** (not Thread methods) that enable thread communication on a shared monitor.

- **wait()** - Releases the lock and waits until another thread calls notify()
- **notify()** - Wakes ONE waiting thread (JVM chooses which)
- **notifyAll()** - Wakes ALL waiting threads; they must re-compete for the lock

## Critical Rules

1. **Must be called inside a synchronized block** - You must own the monitor
2. **wait() releases the lock** and enters the wait set
3. **Spurious wakeups** - A thread can wake without notify(); always use while loop
4. **Lost wakeup** - Using `if` instead of `while` causes missed notifications

## Why while(not if) with wait()

```java
// WRONG - Lost wakeup
synchronized(lock) {
    if (!condition) {
        lock.wait();
    }
    // process() may run with condition still false
}

// CORRECT
synchronized(lock) {
    while (!condition) {
        lock.wait();
    }
    // condition is guaranteed true here
}
```

## wait() vs sleep() vs join()

| Method     | Releases Lock? | Requires Synchronized? | Purpose                    |
|-----------|----------------|----------------------|----------------------------|
| wait()    | YES            | YES                  | Wait for notification      |
| sleep()   | NO             | NO                   | Pause for time             |
| join()    | NO             | NO                   | Wait for thread to finish  |

## wait(timeout)

```java
lock.wait(5000); // Releases lock, re-acquires after 5s or notify()
```

Useful for timeout scenarios and handling spurious wakeups.

## Producer-Consumer Pattern

- **Producer** waits when buffer is full, produces item, notifies consumer
- **Consumer** waits when buffer is empty, consumes item, notifies producer
- Bounded buffer prevents overflow

## Thread Communication Patterns

1. **Single-waiter** - One thread waits, one notifies
2. **Multiple-waiter** - Multiple threads wait, use notifyAll()
3. **Condition-based** - Multiple conditions (use Condition API instead)
4. **Handoff** - Producer-consumer with bounded buffer
