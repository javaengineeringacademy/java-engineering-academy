# 04. Memory Model Internals Deep Dive

## The Java Memory Model (JMM)

The JMM defines how threads interact through memory. It specifies which writes by one thread are guaranteed to be visible to reads by another thread.

### Happens-Before Rules

The JMM defines these happens-before rules:

```
Program Order Rule:
  Each action in a thread happens-before every subsequent action in the same thread.

Monitor Lock Rule:
  An unlock on a monitor happens-before every subsequent lock on the same monitor.

Volatile Variable Rule:
  A write to a volatile field happens-before every subsequent read of that field.

Thread Start Rule:
  A call to Thread.start() happens-before any action in the started thread.

Thread Termination Rule:
  Any action in a thread happens-before any other thread detects that thread has terminated
  (via Thread.join() or Thread.isAlive()).

Transitivity:
  If A happens-before B, and B happens-before C, then A happens-before C.

Final Field Rule:
  An object is fully constructed when its constructor completes and the reference
  is safely published. All threads see correct final field values.
```

### Memory Visibility Problem

Without proper synchronization, threads may see stale values:

```
Thread 1                    Thread 2
--------                    --------
x = 42;                     while (!ready) {}
ready = true;               System.out.println(x);
// May print 0 (stale)!
```

The JMM allows this because:
1. The compiler may reorder the writes
2. The CPU may reorder the writes
3. Caches may not be flushed

### Memory Barriers

Memory barriers enforce ordering and visibility:

```
Load Barrier (acquire semantics):
├── Prevents loads after the barrier from being reordered before it
├── Used after volatile read
└── Used before synchronized block entry

Store Barrier (release semantics):
├── Prevents stores before the barrier from being reordered after it
├── Used before volatile write
└── Used after synchronized block exit

Full Barrier (sequential consistency):
├── Both load and store semantics
├── Prevents all reordering across the barrier
└── Used for volatile read + write
```

### Synchronized Internals

The synchronized keyword uses monitors (locks):

```
monitorenter bytecode:
├── Acquire the monitor
├── If already held by another thread: block
├── Establish acquire barrier
└── Enter the critical section

monitorexit bytecode:
├── Release the monitor
├── Establish release barrier
├── Wake up waiting threads
└── Exit the critical section

Monitor object:
├── Entry queue (threads waiting to acquire)
├── Wait set (threads in Object.wait())
├── Owner thread (current holder)
└── Recursion count (reentrant lock depth)
```

### Volatile Internals

Volatile variables use memory barriers:

```
volatile read:
├── Load the value from memory
├── Acquire barrier: no subsequent reads/writes move before this
└── Return the value

volatile write:
├── Store the value to memory
├── Release barrier: no preceding reads/writes move after this
└── Return
```

### Unsafe Operations and Happens-Before

The `sun.misc.Unsafe` class provides low-level memory operations:

```
Unsafe.putOrderedInt(): StoreStore barrier (lazySet)
Unsafe.putObjectVolatile(): Full barrier (volatile write)
Unsafe.getObjectVolatile(): Full barrier (volatile read)
Unsafe.compareAndSwapInt(): Atomic CAS with full barrier
Unsafe.loadFence(): Load barrier
Unsafe.storeFence(): Store barrier
Unsafe.fullFence(): Full barrier
```
