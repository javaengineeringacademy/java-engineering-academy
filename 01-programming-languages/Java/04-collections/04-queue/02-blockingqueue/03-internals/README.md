# BlockingQueue Internals

## Interface Definition

```
BlockingQueue extends Queue with blocking operations:

- put(element):  blocks until space is available
- take():        blocks until element is available
- offer(timeout): blocks for specified time
- poll(timeout):  blocks for specified time
```

## Producer-Consumer Pattern

```
┌─────────────┐     ┌─────────────────┐     ┌─────────────┐
│   Producer  │────►│  BlockingQueue  │────►│   Consumer  │
│   Thread    │     │                 │     │   Thread    │
└─────────────┘     │  ┌───┬───┬───┐  │     └─────────────┘
                    │  │ A │ B │ C │  │
                    │  └───┴───┴───┘  │
                    │    (capacity=3)  │
                    └─────────────────┘

Producer:  put("D") → blocks if full
Consumer:  take()   → blocks if empty
```

## ArrayBlockingQueue Implementation

```java
public class ArrayBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {

    private final Object[] items;
    private int takeIndex;
    private int putIndex;
    private int count;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
}
```

### Circular Array

```
Same as ArrayDeque but with count tracking:

items:    [A] [B] [ ] [ ] [D] [E] [ ] [ ]
index:     0   1   2   3   4   5   6   7
           ↑               ↑
        takeIndex       putIndex

count = 4 (number of elements)
```

### put(element) — Blocking Insert

```
1. lock.lock()
2. while count == items.length:
     notFull.await()      // Wait for space
3. items[putIndex] = element
4. putIndex = (putIndex + 1) % items.length
5. count++
6. notEmpty.signal()      // Wake waiting consumers
7. lock.unlock()

Time: O(1) + potential wait
```

### take() — Blocking Remove

```
1. lock.lock()
2. while count == 0:
     notEmpty.await()     // Wait for element
3. E result = items[takeIndex]
4. items[takeIndex] = null
5. takeIndex = (takeIndex + 1) % items.length
6. count--
7. notFull.signal()       // Wake waiting producers
8. lock.unlock()

Time: O(1) + potential wait
```

## LinkedBlockingQueue Implementation

```java
public class LinkedBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {

    private final int capacity;
    private final AtomicInteger count = new AtomicInteger(0);
    private transient Node<E> head;
    private transient Node<E> last;
    private final ReentrantLock putLock = new ReentrantLock();
    private final ReentrantLock takeLock = new ReentrantLock();
    private final Condition notEmpty;
    private final Condition notFull;
}
```

### Dual Lock Design

```
LinkedBlockingQueue uses TWO locks:

putLock:  controls put/offer operations
takeLock: controls take/poll operations

This allows concurrent put and take:
  Producer:  putLock.lock()   ─── put() ─── putLock.unlock()
  Consumer:                    takeLock.lock() ─── take() ─── takeLock.unlock()
                     ↑ Can overlap! ↑
```

## Memory Layout Diagram

```
ArrayBlockingQueue instance:
┌──────────────────────────────────────┐
│  Object header           (12 bytes)  │
│  Object[] items ref       (8 bytes)  │
│  int takeIndex            (4 bytes)  │
│  int putIndex             (4 bytes)  │
│  int count                (4 bytes)  │
│  ReentrantLock ref        (8 bytes)  │
│  Condition notEmpty ref   (8 bytes)  │
│  Condition notFull ref    (8 bytes)  │
│  Padding                  (4 bytes)  │
└──────────────┬───────────────────────┘
               │
               ▼
     ┌──────────────────────────────────────┐
     │  Circular array                      │
     │  [A] [B] [ ] [ ] [D] [E] [ ] [ ]   │
     │   ↑               ↑                  │
     │ takeIndex       putIndex             │
     └──────────────────────────────────────┘
```

## Blocking Behavior Timeline

```
Full queue (capacity=3):
  [A] [B] [C]
  count=3

Producer calls put("D"):
  1. Acquires lock
  2. count == 3 → full → await()
  3. Thread BLOCKED (parked)
  4. Consumer calls take() → removes "A"
  5. count drops to 2 → signal notFull
  6. Producer wakes up
  7. Puts "D" at putIndex
  8. Releases lock
```

## Thread Safety Model

```
ArrayBlockingQueue:
  - Single ReentrantLock for all operations
  - Full mutual exclusion
  - Simpler but more contention

LinkedBlockingQueue:
  - Two separate locks (putLock, takeLock)
  - Allows concurrent put and take
  - Less contention for mixed workloads
```

## Key Implementation Details

1. **Capacity bounded** — ArrayBlockingQueue has fixed capacity.
   LinkedBlockingQueue can be Integer.MAX_VALUE (unbounded).

2. **Fair ordering** — ArrayBlockingQueue supports fair lock ordering
   (FIFO for waiting threads). Slower but predictable.

3. **Interruptible** — put() and take() respond to interrupts.
   InterruptedException thrown if thread interrupted while waiting.

4. **Time-based operations** — offer(timeout) and poll(timeout)
   allow timed waits instead of indefinite blocking.

5. **No null elements** — Both implementations reject null elements.
   Null signals "queue empty" in poll().

6. **Memory consistency** — Actions in one thread are visible to
   another thread via the lock guarantees.
