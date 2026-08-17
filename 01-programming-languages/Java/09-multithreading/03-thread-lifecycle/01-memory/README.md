# Thread Lifecycle - Memory Model

## Thread State Memory Layout

### When a Thread Waits

When a thread calls `Object.wait()`:

```
Before wait():
┌─────────────────────────────────┐
│ Thread Stack                    │
│ ┌─────────────────────────────┐ │
│ │ Method: synchronized block │ │
│ │ Local variables             │ │
│ │ Operand stack               │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
         │ holds monitor
         ▼
┌─────────────────────────────────┐
│ Object Monitor (on heap)        │
│ ┌─────────────────────────────┐ │
│ │ Owner: Thread-1             │ │
│ │ Entry Set: [Thread-2]       │ │
│ │ Wait Set: []                │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘

After wait():
┌─────────────────────────────────┐
│ Thread Stack (saved state)      │
│ ┌─────────────────────────────┐ │
│ │ State saved for resume      │ │
│ │ PC, locals, operand stack  │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
         │ released monitor
         ▼
┌─────────────────────────────────┐
│ Object Monitor (on heap)        │
│ ┌─────────────────────────────┐ │
│ │ Owner: none                 │ │
│ │ Entry Set: [Thread-2]       │ │
│ │ Wait Set: [Thread-1]        │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
```

### Thread Stack During State Changes

| State | Stack Content | Monitor |
|-------|--------------|---------|
| NEW | Empty stack | None |
| RUNNABLE | Active frames | May hold |
| BLOCKED | Saved, waiting for entry | Waiting to acquire |
| WAITING | Saved, in wait set | Released |
| TIMED_WAITING | Saved, in wait set | Released |
| TERMINATED | Stack deallocated | Released |

### Why sleep() Doesn't Release Locks

`sleep()` is a static method that operates on the current thread:
- The thread's stack state is saved
- The OS timer is set
- The thread remains in the "owner" field of any held monitors
- No notification is sent to wait/entry sets
- Only the scheduler decides when the thread runs again
