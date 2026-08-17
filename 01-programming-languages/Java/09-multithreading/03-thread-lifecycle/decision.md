# Thread Lifecycle - Decision Guide

## Thread State Transitions

```
                    start()
    NEW ──────────────────────────► RUNNABLE
                                     │   ▲
                   ┌─────────────────┤   │
                   │                 │   │
                   ▼                 │   │
               BLOCKED ◄────────────┤   │
               (waiting for lock)   │   │
                   │                 │   │
                   ▼                 │   │
               WAITING ◄────────────┤   │
               (indefinite wait)    │   │
                   │                 │   │
                   ▼                 │   │
           TIMED_WAITING ───────────┘   │
           (sleep, timed wait)          │
                   │                    │
                   ▼                    │
               TERMINATED ◄─────────────┘
                          (run() completes)
```

## When to Use Each State Transition

| Transition | Method | Use Case |
|-----------|--------|----------|
| NEW → RUNNABLE | `start()` | Begin thread execution |
| RUNNABLE → BLOCKED | `synchronized` | Waiting for monitor lock |
| BLOCKED → RUNNABLE | Lock acquired | Resume after lock |
| RUNNABLE → WAITING | `Object.wait()` | Wait for condition |
| RUNNABLE → WAITING | `Thread.join()` | Wait for thread completion |
| RUNNABLE → TIMED_WAITING | `Thread.sleep(ms)` | Pause execution |
| RUNNABLE → TIMED_WAITING | `wait(ms)` | Timed wait for condition |
| RUNNABLE → TERMINATED | `run()` completes | Task finished |

## Choosing Wait/Notify vs Lock/Condition

| Scenario | Approach | Why |
|----------|----------|-----|
| Simple condition wait | `wait()`/`notify()` | Built-in, no extra objects |
| Multiple conditions | `Condition` objects | Multiple wait sets per lock |
| Timed waiting | `wait(ms)` or `sleep(ms)` | Automatic timeout |
| Interruptible wait | `Condition.await()` | Better interrupt handling |
| Complex coordination | `CountDownLatch`/`CyclicBarrier` | Higher-level API |

## Common Lifecycle Anti-Patterns

1. **Catching `InterruptedException` and continuing** → Restore interrupt flag
2. **Using `Thread.stop()`** → Use interruption instead
3. **Polling `Thread.isAlive()` in loops** → Use `join()`
4. **Calling `wait()` without holding the monitor** → Always synchronize first
5. **Using `sleep()` for synchronization** → Use proper synchronization primitives
