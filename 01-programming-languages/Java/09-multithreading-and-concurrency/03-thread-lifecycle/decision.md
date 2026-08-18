# Thread Lifecycle Decision Guide

## Waiting vs Sleeping vs Blocking

| Method | Releases Lock? | Interruptible? | State |
|--------|---------------|----------------|-------|
| `Thread.sleep(ms)` | No | Yes | TIMED_WAITING |
| `Object.wait(ms)` | Yes | Yes | TIMED_WAITING |
| `Thread.join(ms)` | No | Yes | TIMED_WAITING |
| `LockSupport.park()` | No | Yes | WAITING |
| Contention on synchronized | N/A (waiting for lock) | No | BLOCKED |

## When to Use Each Wait Mechanism

| Situation | Use |
|-----------|-----|
| Pause for fixed duration | `Thread.sleep(ms)` |
| Wait for condition on object | `Object.wait()` with `notifyAll()` |
| Wait for thread to complete | `Thread.join()` |
| Wait for arbitrary condition | `LockSupport.park()` / `Condition.await()` |
