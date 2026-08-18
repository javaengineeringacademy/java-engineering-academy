# Volatile Keyword in Java

## What is `volatile`?

The `volatile` keyword in Java is a visibility guarantee, **not** an atomicity guarantee. When a variable is declared `volatile`:

- Every read of the variable goes directly to main memory (not CPU cache)
- Every write to the variable goes directly to main memory
- All threads see the most recent value (visibility guarantee)
- No thread-local caching of the variable

**Key point**: `volatile` ensures **visibility** (all threads see the latest value) but does **NOT** ensure **atomicity** (compound operations like `i++` are still not thread-safe).

## How Volatile Works Under the Hood

### CPU Cache Coherence
Each CPU core has its own cache. Without `volatile`, Thread A's writes may sit in its cache while Thread B reads a stale value from its own cache.

```
Thread A (Core 1)          Main Memory          Thread B (Core 2)
   Cache: value=1      <--- writes value=1       Cache: value=0
                                       Thread B may read value=0 from its cache
```

With `volatile`, writes are flushed to main memory immediately:
```
Thread A (Core 1)          Main Memory          Thread B (Core 2)
   writes value=1  --->   value=1            <--- reads value=1
```

### Memory Barriers (Fences)
`volatile` inserts memory barriers in the instruction stream:
- **Store Barrier (sfence)**: Ensures all previous writes are visible before subsequent writes
- **Load Barrier (lfence)**: Ensures all previous reads are completed before subsequent reads
- **Full Barrier (mfence)**: Combines both store and load barriers

The JVM uses these barriers to enforce ordering:
1. **StoreStore barrier**: Before a volatile write, prevents reordering with previous writes
2. **StoreLoad barrier**: After a volatile write, ensures the write is visible to other threads before any subsequent read
3. **LoadLoad barrier**: After a volatile read, prevents reordering with subsequent reads
4. **LoadStore barrier**: After a volatile read, prevents reordering with subsequent writes

## Volatile vs Synchronized vs Atomic Classes

| Feature | volatile | synchronized | Atomic Classes |
|---------|----------|--------------|----------------|
| Visibility | ✅ Yes | ✅ Yes | ✅ Yes |
| Atomicity | ❌ No | ✅ Yes | ✅ Yes |
| Mutual Exclusion | ❌ No | ✅ Yes | ✅ Yes (CAS) |
| Blocking | ❌ No | ✅ Yes (may block) | ❌ No (lock-free) |
| Performance | Fast | Slow (acquire/release) | Fast |
| Use Case | Simple flags | Complex critical sections | Counters, accumulators |

**When to use what:**
- `volatile`: Simple flag variables (boolean states)
- `synchronized`: Compound operations on shared state
- `Atomic classes`: Counters, sequence generators, statistics

## When to Use Volatile

### 1. Simple Flags / State Indicators
```java
volatile boolean running = true;

// Thread A
while (running) {
    // do work
}

// Thread B
running = false; // Thread A will eventually see this
```

### 2. Status Variables
```java
volatile int status = 0; // 0=IDLE, 1=RUNNING, 2=COMPLETE

// Thread checking status
if (status == 2) {
    // process complete result
}
```

### 3. Double-Checked Locking (DCL)
```java
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
```
Without `volatile` in DCL, the `instance` reference might be published before the object is fully constructed.

## When NOT to Use Volatile

### 1. Compound Operations (i++)
```java
volatile int count = 0;

// NOT thread-safe! Race condition still exists
count++; // This is actually: temp = count; count = temp + 1
// Thread A reads count=5, Thread B reads count=5
// Both compute 6, both write 6. One increment is lost!
```

### 2. Check-Then-Act Patterns
```java
volatile List<String> items;

if (items.isEmpty()) { // Thread A reads empty
    // Thread B adds an item here
    items.add("item"); // Thread A now adds duplicate
}
```

### 3. Non-Atomic Compound States
```java
volatile int x = 0;
volatile int y = 0;

// If Thread A sets x=1 then y=2,
// Thread B might see x=1 but y=0
```

## Common Volatile Patterns

### Pattern 1: Graceful Shutdown
```java
public class Worker implements Runnable {
    private volatile boolean stopped = false;

    public void stop() { stopped = true; }

    @Override
    public void run() {
        while (!stopped) {
            // process work
        }
    }
}
```

### Pattern 2: One-Time Initialization
```java
public class Config {
    private static volatile Config instance;
    private String data;

    public static Config getInstance() {
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }
}
```

### Pattern 3: Flag-Based Communication
```java
volatile boolean dataReady = false;
volatile int[] result;

// Producer
result = computeData();
dataReady = true;

// Consumer
while (!dataReady) { /* wait */ }
process(result);
```

## Volatile in Java Memory Model (Happens-Before)

The `volatile` keyword establishes a **happens-before** relationship:

1. **Write to volatile variable** happens-before every subsequent read of that variable
2. This means:
   - All writes before the volatile write are visible after the volatile read
   - The volatile write and subsequent volatile read are ordered
   - All threads will see the complete sequence of writes

```
Thread A                    Thread B
---------                   ---------
x = 1;                      while (!flag) {}
x = 2;                      print(x); // guaranteed to print 2
flag = true; // volatile
```

Without `volatile`, Thread B might print 0, 1, or 2.

## Volatile with Reference Assignments

When a `volatile` reference is reassigned, the object's fields become visible:

```java
class Holder {
    int x = 10;
}

volatile Holder holder = null;

// Thread A
holder = new Holder(); // Writes new Holder with x=10

// Thread B
while (holder == null) {}
// holder.x is guaranteed to be 10 (not 0 or garbage)
```

**Important**: Volatile reference doesn't make the object's fields thread-safe! It only guarantees the reference itself is visible.

## Performance Implications

- **volatile reads**: Nearly as fast as normal reads (minor overhead from memory barrier)
- **volatile writes**: More expensive than normal writes (must flush to main memory + memory barrier)
- **Overall**: Much faster than `synchronized` but limited to simple use cases
- **False sharing**: Can cause performance issues if volatile variables share a cache line
- **Use `@Contended`**: In Java 8+, annotate volatile fields to prevent false sharing

**Best practice**: Use `volatile` only when you need visibility, not atomicity. For complex thread-safety, use `synchronized` or `Atomic*` classes.
