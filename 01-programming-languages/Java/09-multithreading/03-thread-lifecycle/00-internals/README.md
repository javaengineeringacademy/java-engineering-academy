# Thread Lifecycle - Internals

## Thread State Machine Internals

### How the JVM Manages Thread States

The JVM maintains thread state as an integer field in the Thread object:

```java
// Internal representation (simplified)
private volatile int threadStatus = 0; // NEW
```

State transitions are managed by the JVM:
- `start()` changes state from 0 (NEW) to RUNNABLE
- Blocking operations change state to BLOCKED/WAITING/TIMED_WAITING
- When the `run()` method returns, state changes to TERMINATED

### Monitor Lock Internals

When a thread enters a `synchronized` block:
1. The JVM attempts to acquire the object's monitor
2. If the monitor is free, the thread acquires it and proceeds
3. If the monitor is held by another thread, the current thread enters BLOCKED
4. The thread is added to the monitor's "entry set"
5. When the monitor is released, one blocked thread is selected to acquire it

### wait() Internals

When `Object.wait()` is called:
1. The thread must hold the monitor (or `IllegalMonitorStateException`)
2. The thread is added to the object's "wait set"
3. The monitor is released
4. The thread enters WAITING state
5. When `notify()` is called, one thread is moved from wait set to entry set

### sleep() Internals

When `Thread.sleep()` is called:
1. The JVM requests the OS to put the thread to sleep
2. The thread does NOT release any monitor locks
3. After the specified time, the thread is moved back to the runnable queue
4. The OS may wake the thread earlier (spurious wakeup)

### Thread Termination Internals

When `run()` completes:
1. The thread's stack is unwound
2. Any synchronized locks are released
3. The thread state is set to TERMINATED
4. The thread object becomes eligible for garbage collection
5. Any threads in `join()` on this thread are notified
