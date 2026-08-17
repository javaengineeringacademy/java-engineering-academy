# Locks - Internals

## How Locks Work Internally

### AQS (AbstractQueuedSynchronizer)

Most Java locks are built on AQS:
- Maintains a `volatile int state` (0 = unlocked, 1+ = locked/reentrant)
- Uses a FIFO queue of waiting threads (CLH queue variant)
- Thread contends by CAS on the state variable

```
AQS State Machine:
┌────────────────────────────────────────┐
│ state = 0: Unlocked                   │
│ state = 1: Locked (exclusive)         │
│ state = N: Reentrant lock held N times│
└────────────────────────────────────────┘

CLH Queue:
┌──────┐   ┌──────┐   ┌──────┐
│ T1   │──▶│ T2   │──▶│ T3   │
│ WAIT │   │ WAIT │   │ WAIT │
└──────┘   └──────┘   └──────┘
```

### ReentrantLock Internals

When `lock()` is called:
1. Try CAS on AQS state (0 → 1)
2. If successful: thread acquires lock, sets exclusive owner
3. If failed: check if current thread is owner (reentrant)
4. If reentrant: increment state count
5. If not: create node, add to CLH queue, park (block)

When `unlock()` is called:
1. Decrement state count
2. If count reaches 0: clear owner, CAS state to 0
3. Unpark the next thread in the CLH queue

### ReadWriteLock Internals

`ReentrantReadWriteLock` uses two AQS states:
- **Read lock**: Shared mode (multiple readers)
- **Write lock**: Exclusive mode (single writer)

```
State layout (combined):
┌──────────────────────────────────┐
│ High bits: write lock count      │
│ Low bits: read lock count        │
└──────────────────────────────────┘
```

Read lock succeeds if:
- No write lock is held
- OR current thread holds the read lock (reentrant)

Write lock succeeds if:
- No locks are held by any thread

### StampedLock Internals

`StampedLock` uses a stamp (long) as lock state:
- Optimistic read: get stamp without locking
- Read lock: returns new stamp
- Write lock: returns new stamp
- Validate: check if stamp is still valid (no write occurred)

### Condition Internals

Each `Condition` has its own wait set:
- `await()` adds thread to condition's wait set, releases lock
- `signal()` moves one thread from condition's wait set to lock's entry set
- `signalAll()` moves all threads from condition's wait set

This allows multiple independent wait sets per lock, unlike `synchronized` which has only one wait set per monitor.
