# Java Memory Model (JMM)

The Java Memory Model defines how threads interact through memory and what behaviors are allowed in concurrent Java programs. It's crucial for writing correct concurrent code.

## Formal Definition

JMM is defined in JLS Chapter 17. It provides a mathematical framework for reasoning about memory operations and their ordering.

### Key Concepts

1. **Shared Variables**: Memory locations that can be accessed by multiple threads
2. **Actions**: Memory operations (read, write, lock, unlock)
3. **Happens-Before Order**: Partial ordering of actions
4. **Memory Consistency**: Guarantees about when writes become visible to reads

## Shared Variables

### What Are Shared Variables?

- Instance fields of objects
- Static fields of classes
- Array elements
- Fields referenced by object references

### Non-Shared Variables

- Local variables (method parameters, local objects)
- Method parameters
- Variables on the stack

```java
public void example() {
    int local = 10;  // Thread-local
    
    // Shared:
    this.instanceField = 20;
    staticField = 30;
    array[0] = 40;
}
```

## Actions

### Memory Actions

1. **Read**: Loading a value from shared memory
   - Volatile read
   - Non-volatile read

2. **Write**: Storing a value to shared memory
   - Volatile write
   - Non-volatile write

3. **Lock**: Acquiring a monitor
   - `synchronized` block entry
   - `ReentrantLock.lock()`

4. **Unlock**: Releasing a monitor
   - `synchronized` block exit
   - `ReentrantLock.unlock()`

5. **Volatile Variable Access**
   - Volatile read
   - Volatile write

6. **Thread Start/Join**
   - `Thread.start()`
   - `Thread.join()`

### Synchronization Actions

- Locking/unlocking monitors
- Reading/writing volatile variables
- Starting/joining threads

## Happens-Before Order

The happens-before order is the heart of JMM. It's a partial ordering that determines when one action is guaranteed to be visible to another.

### Complete Happens-Before Rules

#### Rule 1: Program Order

If action A comes before B in program order within a single thread, then A happens-before B.

```java
int x = 1;          // Action A
int y = x + 1;      // Action B
// A happens-before B
```

#### Rule 2: Monitor Lock

An unlock on a monitor happens-before every subsequent lock on that same monitor.

```java
// Thread 1
synchronized (lock) {      // Lock action
    sharedVariable = 10;    // Write
}                           // Unlock action

// Thread 2
synchronized (lock) {      // Lock happens-after Thread 1's unlock
    // Can see Thread 1's write
    System.out.println(sharedVariable); // 10
}
```

#### Rule 3: Volatile Variable

A write to a volatile field happens-before every subsequent read of that same field.

```java
volatile boolean flag = false;

// Thread 1
data = 10;             // Write to non-volatile
flag = true;           // Volatile write (happens-before all future reads of flag)

// Thread 2
if (flag) {            // Volatile read
    System.out.println(data); // 10 (guaranteed to see Thread 1's write)
}
```

#### Rule 4: Thread Start

A call to `Thread.start()` happens-before any action in the started thread.

```java
Thread t = new Thread(() -> {
    // Everything here happens-after start() call
    System.out.println(data); // Sees all writes before start()
});
data = 10;            // Before start
t.start();            // Happens-before everything in thread t
```

#### Rule 5: Thread Join

All actions in a thread happen-before any other thread successfully returns from a `join()` on that thread.

```java
Thread t = new Thread(() -> {
    data = 10;        // Write in thread
});
t.start();
t.join();             // Returns after thread completes
System.out.println(data); // 10 guaranteed
```

#### Rule 6: Transitivity

If A happens-before B, and B happens-before C, then A happens-before C.

```java
// Thread 1
synchronized (lock1) {   // Unlock lock1
    shared = 10;
}
synchronized (lock2) {   // Uses lock1 -> lock2 ordering
    // ...
}

// Thread 2
synchronized (lock2) {
    synchronized (lock1) { // Same ordering
        // Sees Thread 1's write
        System.out.println(shared); // 10
    }
}
```

#### Rule 7: Final Field Semantics

An object is safely published when its final fields are initialized and the reference is shared.

```java
class SafePublication {
    final int x;
    
    SafePublication(int x) {
        this.x = x;  // Initialize final field
    }
}

// Thread 1
SafePublication safe = new SafePublication(10);

// Thread 2
System.out.println(safe.x); // 10 guaranteed (if safe is published safely)
```

## Memory Visibility

### Without Volatile/Synchronization

Writes may not be visible to other threads:

```java
boolean running = true;

// Thread 1
while (running) {
    // May loop forever if Thread 2 writes running = false
}

// Thread 2
running = false; // May never be seen by Thread 1
```

### With Volatile

Writes are immediately visible:

```java
volatile boolean running = true;

// Thread 1
while (running) {
    // Will see Thread 2's write
}

// Thread 2
running = false; // Guaranteed to be seen
```

### With Synchronization

Critical sections establish happens-before:

```java
synchronized (this) {
    // All writes here are visible to next synchronized block
}
```

## Volatile Semantics

### What Volatile Guarantees

1. **Visibility**: Writes are visible to all threads
2. **Ordering**: Volatile operations cannot be reordered with each other
3. **Atomicity for 64-bit**: Long/double reads/writes are atomic

### What Volatile Does NOT Guarantee

- **Compound atomicity**: `i++` is not atomic
- **Mutual exclusion**: Multiple threads can access volatile variable simultaneously
- **Non-volatile operations**: Operations around volatile may be reordered

```java
volatile int counter = 0;

// NOT atomic:
counter++;  // Read, increment, write (3 steps)

// Atomic operations:
int temp = counter; // Read
counter = temp;     // Write
```

### Volatile vs Synchronized

| Feature | Volatile | Synchronized |
|---------|----------|--------------|
| Atomicity | No (compound ops) | Yes |
| Visibility | Yes | Yes |
| Mutual Exclusion | No | Yes |
| Performance | Faster | Slower |

## Final Field Semantics

### The Problem

Without special rules, final fields could be seen as uninitialized:

```java
class SafeObject {
    final int x;
    
    SafeObject(int x) {
        this.x = x;
    }
}

// Thread 1
SafeObject obj = new SafeObject(10);
// Reference escapes before constructor completes

// Thread 2
System.out.println(obj.x); // Might see 0 without final field semantics
```

### The Solution

JMM provides special semantics for final fields:

1. Final field values must be set in constructor
2. Constructor must complete before object reference is published
3. All threads see final field values as set in constructor

```java
class SafeObject {
    final int x;
    final int y;
    
    SafeObject(int x, int y) {
        this.x = x;  // Must be set
        this.y = y;  // Before constructor completes
    }
}

// Safe publication
SafeObject obj = new SafeObject(10, 20);
// All threads see x=10, y=20
```

### Unsafe Publication

```java
class UnsafePublication {
    int x;  // Not final!
    
    UnsafePublication(int x) {
        this.x = x;
    }
}

// Without synchronization, x might be 0 to other threads
UnsafePublication obj = new UnsafePublication(10);
// Another thread might see obj.x == 0
```

## Safe Publication

### How to Safely Publish Objects

1. **Final fields**: Object is safely constructed
2. **Volatile reference**: Reference is safely published
3. **Synchronized access**: Both construction and access synchronized
4. **Thread-safe container**: `ConcurrentHashMap`, `CopyOnWriteArrayList`

```java
// Unsafe publication
class Holder {
    int x;
}

Holder holder = null; // Not safe

// Thread 1
holder = new Holder(); // Might publish before constructor completes

// Thread 2
if (holder != null) {
    System.out.println(holder.x); // Might see 0
}

// Safe publication
volatile Holder safeHolder = null;

// Thread 1
safeHolder = new Holder(); // Safe due to volatile

// Thread 2
if (safeHolder != null) {
    System.out.println(safeHolder.x); // Sees initialized value
}
```

### Safe Initialization Patterns

```java
// Double-checked locking (safe with volatile)
class Singleton {
    private static volatile Singleton instance;
    
    static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// Bill Pugh Singleton (safe)
class Singleton {
    private static class Holder {
        static final Singleton INSTANCE = new Singleton();
    }
    
    static Singleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

## Common JMM Pitfalls

### 1. Reordering

```java
// Without volatile, compiler/JVM can reorder:
int a = 1;        // Action 1
int b = 2;        // Action 2
int c = a + b;    // Action 3

// Could become:
int b = 2;        // Reordered
int a = 1;        // Reordered
int c = a + b;
```

### 2. Out-of-Thin-Air Values

```java
// This can see values that were never written
int x = 0;

// Thread 1
while (x == 0) {}
System.out.println(y);

// Thread 2
while (y == 0) {}
x = 1;

// JMM allows both threads to continue (out-of-thin-air)
```

### 3. Non-Atomic 64-bit Operations

```java
long counter = 0;

// Without volatile, read/write might not be atomic
counter = 1L; // Might see partial write (32 bits)
```

### 4. Instruction Reordering

```java
int a = 1;
int b = 2;
int c = a + b;

// JVM might reorder for performance
int b = 2;
int c = a + b;
int a = 1;
```

## Memory Model Properties

### Causality Requirement

JMM prohibits causality loops:

```java
// This is not allowed
int x = 0;
// Thread 1
if (x == 1) x = 2;
// Thread 2
if (x == 2) x = 1;

// JMM prevents both threads from making progress
```

### Out-of-Thin-Air Safety

JMM prevents seeing values that were never written (except for 32-bit values).

## Testing JMM Concurrency

```java
public class JMMTest {
    private int x = 0;
    private int y = 0;
    private volatile boolean ready = false;
    
    public void writer() {
        x = 1;
        y = 2;
        ready = true; // Volatile write
    }
    
    public void reader() {
        if (ready) {  // Volatile read
            // Guaranteed to see x=1, y=2
            assert x == 1;
            assert y == 2;
        }
    }
}
```

## Resources

- **JLS Chapter 17**: https://docs.oracle.com/javase/specs/jls/se21/html/index.html
- **JSR 133**: Java Memory Model and Thread Specification
- **"Java Concurrency in Practice"**: Practical JMM guide
- **OpenJDK JMM Tests**: Source code examples
- ** jcstress**: Java Concurrency Stress testing framework