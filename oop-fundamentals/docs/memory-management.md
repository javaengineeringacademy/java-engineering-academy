# Memory Management in Java

## 1. Introduction

Memory management is the process of allocating, using, and reclaiming memory during a Java program's execution. Java uses automatic memory management through the JVM's Garbage Collector, relieving developers from manual memory allocation and deallocation. Understanding how the JVM manages memory is essential for writing high-performance, leak-free applications.

Java 21 introduced generational ZGC as the default low-latency garbage collector, fundamentally changing the default memory management behavior. This document covers the heap, stack, GC roots, generational memory layout, GC algorithms, memory leaks, and profiling tools.

## 2. Learning Objectives

- Understand the JVM memory model and its different memory areas
- Differentiate between heap and stack memory
- Identify GC roots and how reachability analysis works
- Understand Young Generation, Old Generation, and their roles
- Compare GC algorithms: Serial, Parallel, G1, ZGC, Shenandoah
- Detect and prevent memory leaks
- Use profiling tools like JFR, VisualVM, and Eclipse MAT
- Tune JVM memory flags for production workloads
- Apply best practices for efficient memory usage

## 3. Prerequisites

- Basic Java syntax and object-oriented concepts
- Familiarity with JVM compilation and execution model
- Understanding of classes, objects, and references
- Command-line basics for running JVM diagnostic tools
- Familiarity with at least one IDE (IntelliJ IDEA, Eclipse)

## 4. Why This Concept Exists

Manual memory management (as in C/C++) is error-prone and leads to bugs like dangling pointers, double frees, and memory leaks. Java's automatic memory management eliminates these classes of bugs entirely. The garbage collector automatically reclaims memory occupied by unreachable objects, allowing developers to focus on business logic rather than memory bookkeeping.

However, automatic memory management does not eliminate all memory-related issues. Poorly designed applications can still suffer from memory leaks, excessive garbage collection pauses, and out-of-memory errors. Understanding how memory works under the hood is critical for building reliable, high-performance Java applications.

## 5. Problem Statement

Consider an e-commerce application processing thousands of concurrent orders:

```java
public class OrderProcessor {
    private static final List<Order> orders = new ArrayList<>();

    public void processOrder(Order order) {
        // Processing logic
        orders.add(order); // Memory leak: orders never removed
    }
}
```

Each order is added to a static list but never removed. Over time, this causes the heap to fill up, triggering frequent GC cycles and eventually an `OutOfMemoryError`. Understanding memory management helps identify and fix such issues before they reach production.

## 6. Theory

### JVM Memory Areas

The JVM divides memory into several distinct areas, each with a specific purpose:

```
┌─────────────────────────────────────────────────────────────────┐
│                        JVM Memory                               │
├──────────────────┬──────────────────┬───────────────────────────┤
│      Heap        │    Metaspace     │      Non-Heap             │
│   (Objects)      │  (Class Data)    │   (Stack, Code Cache)     │
├──────────────────┼──────────────────┼───────────────────────────┤
│  Young Gen       │  Klass objects   │   Thread Stacks           │
│    Eden          │  Method data     │   Native Memory           │
│    Survivor 0/1  │  Constant pool   │   Code Cache              │
│  Old Gen         │  Interned str    │   Direct Buffers          │
└──────────────────┴──────────────────┴───────────────────────────┘
```

### Heap Structure and Generations

The heap is divided into generations based on object lifetime:

| Generation | Purpose | GC Algorithm | Typical Size |
|------------|---------|--------------|--------------|
| **Young** | Short-lived objects | Minor GC (Scavenge) | 25-40% of heap |
| **Old** | Long-lived objects | Major GC (Mark-Sweep-Compact) | 60-75% of heap |
| **Metaspace** | Class metadata | Full GC | Unbounded (OS-managed) |

### Young Generation Internals

```
Young Generation
┌──────────────────────┬──────────────┬──────────────┐
│       Eden           │  Survivor S0 │  Survivor S1 │
│       (80%)          │    (10%)     │    (10%)     │
└──────────────────────┴──────────────┴──────────────┘
```

- **Eden**: Where all new objects are allocated
- **Survivor spaces**: Hold objects that survived at least one Minor GC
- After each Minor GC, surviving objects are copied between S0 and S1 (from-space/to-space)

### Object Promotion Flow

```
New Object → Eden → Survives Minor GC → S0/S1 → Survives N GCs → Old Gen
```

The tenuring threshold (default 15, configurable via `-XX:MaxTenuringThreshold`) determines when objects are promoted from Young to Old Generation.

## 7. Internal Working

### Object Allocation Process

When a new object is created, the JVM follows this allocation sequence:

1. **TLAB Check**: Each thread has a Thread-Local Allocation Buffer (TLAB) in Eden. If the object fits, it is allocated by bumping a pointer (fast path, no synchronization).
2. **Shared Eden**: If the TLAB is full, the JVM attempts to allocate in the shared Eden space using CAS (compare-and-swap) for thread safety.
3. **Minor GC**: If Eden is full, a Minor GC is triggered. Surviving objects are copied to a Survivor space.
4. **Old Gen**: If an object is large enough or has survived enough GC cycles, it is allocated directly in Old Generation.

### Garbage Collection Process

The GC identifies unreachable objects through reachability analysis starting from GC roots:

1. **Mark Phase**: Traverse from all GC roots, marking every reachable object.
2. **Sweep Phase**: Reclaim memory occupied by unmarked (unreachable) objects.
3. **Compact Phase** (optional): Defragment the heap to eliminate fragmentation and improve allocation speed.

### TLAB (Thread-Local Allocation Buffer)

```
┌─────────────────────────────────┐
│           Eden Space            │
│  ┌────────┐ ┌────────┐ ┌─────┐ │
│  │ TLAB 1 │ │ TLAB 2 │ │ ... │ │
│  │Thread-1│ │Thread-2│ │     │ │
│  └────────┘ └────────┘ └─────┘ │
└─────────────────────────────────┘
```

TLABs eliminate contention for object allocation by giving each thread its own private allocation buffer. Allocation within a TLAB is a simple pointer bump — extremely fast and lock-free.

## 8. JVM Perspective

### Memory Allocation at the JVM Level

The JVM uses different allocation strategies depending on the memory area:

| Strategy | Area | Mechanism | Thread Safety |
|----------|------|-----------|---------------|
| **Bump Pointer** | Eden (within TLAB) | Pointer increment | Thread-local |
| **CAS** | Eden (shared) | Compare-and-swap | Lock-free |
| **Free List** | Old Gen | Free block list | Synchronized |
| **Humongous** | Old Gen (G1) | Direct allocation | Region-based |

### Metaspace (Java 8+)

Metaspace replaced PermGen in Java 8. It stores class metadata, method bytecode, constant pool, and annotations. Unlike PermGen, Metaspace is not part of the heap and is managed natively by the OS, growing automatically unless bounded by `-XX:MaxMetaspaceSize`.

### Code Cache

The JVM stores compiled native code (from JIT compilation) in the Code Cache. If the Code Cache fills up, the JVM stops JIT compilation, causing significant performance degradation. Monitor with `-XX:ReservedCodeCacheSize`.

## 9. Memory Representation

### Object Header Layout

Every Java object on the heap has a header:

```
Object Header (12-16 bytes on 64-bit JVM with compressed oops)
├── Mark Word (8 bytes)
│   ├── Hashcode (31 bits)
│   ├── GC Age (4 bits)
│   ├── Lock status (2 bits)
│   └── Identity hash flag (1 bit)
├── Class Pointer (4 bytes, compressed)
└── Array Length (4 bytes, only for arrays)
```

### Field Alignment

Instance fields are laid out in memory with padding to 8-byte boundaries:

```java
class Example {
    byte a;     // 1 byte + 7 padding = 8 bytes
    int b;      // 4 bytes
    long c;     // 8 bytes
    double d;   // 8 bytes
}
// Total: 8 + 4 (aligned to 8) + 8 + 8 = 32 bytes
```

### Reference Representation

With compressed oops (default for heaps < 32 GB), object references are 4 bytes. Without compression, they are 8 bytes. Use `-XX:+UseCompressedOops` (default on) for heaps up to 32 GB.

## 10. Architecture Diagram

```
┌──────────────────────────────────────────────────────────────────────┐
│                            JVM Architecture                          │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────────┐ │
│  │                          Heap Memory                            │ │
│  │  ┌──────────────────────────┐  ┌────────────────────────────┐  │ │
│  │  │    Young Generation      │  │     Old Generation         │  │ │
│  │  │  ┌──────┬───────┬──────┐ │  │  ┌──────────────────────┐  │  │ │
│  │  │  │ Eden │  S0   │  S1  │ │  │  │   Long-lived objects │  │  │ │
│  │  │  │ 80%  │  10%  │ 10%  │ │  │  │                      │  │  │ │
│  │  │  └──────┴───────┴──────┘ │  │  └──────────────────────┘  │  │ │
│  │  └──────────────────────────┘  └────────────────────────────┘  │ │
│  └─────────────────────────────────────────────────────────────────┘ │
│                                                                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────────┐  │
│  │  Metaspace   │  │  Code Cache  │  │   Thread Stacks          │  │
│  │ Class Data   │  │  JIT Code    │  │  ┌──────┐ ┌──────┐      │  │
│  │ Method Info  │  │              │  │  │T1    │ │T2    │ ...  │  │
│  │ Const Pool   │  │              │  │  │frame │ │frame │      │  │
│  └──────────────┘  └──────────────┘  │  └──────┘ └──────┘      │  │
│                                       └──────────────────────────┘  │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────────┐ │
│  │                    Garbage Collector                             │ │
│  │   G1 (default)  |  ZGC  |  Shenandoah  |  Serial  |  Parallel │ │
│  └─────────────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────────────┘
```

## 11. Flow Diagram

### Object Allocation Flow

```
new Object()
    │
    ▼
┌──────────────┐    Yes    ┌──────────────────┐
│ Fits in TLAB? ├─────────►│ Bump pointer, done│
└──────┬───────┘          └──────────────────┘
       │ No
       ▼
┌──────────────┐    Yes    ┌──────────────────┐
│ Eden has     ├─────────►│ CAS allocate in   │
│ space?       │          │ shared Eden       │
└──────┬───────┘          └──────────────────┘
       │ No
       ▼
┌──────────────┐
│ Trigger      │
│ Minor GC     │
└──────┬───────┘
       │
       ▼
┌──────────────┐    Yes    ┌──────────────────┐
│ Survived     ├─────────►│ Copy to Survivor  │
│ < threshold? │          │ space             │
└──────┬───────┘          └──────────────────┘
       │ No
       ▼
┌──────────────┐
│ Promote to   │
│ Old Gen      │
└──────────────┘
```

### GC Root Traversal

```
GC Roots
├── Thread stack variables
├── Static fields
├── JNI references
├── Monitors (synchronized blocks)
└── System class loader
    │
    ▼
┌──────────────┐
│ Mark Phase   │ ──► Traverse from each root
│              │     Mark all reachable objects
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ Sweep Phase  │ ──► Free unmarked objects
│              │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ Compact      │ ──► Defragment (optional)
│ (if needed)  │
└──────────────┘
```

## 12. Syntax

### JVM Memory Flags

```bash
# Heap sizing
-Xms<size>              # Initial heap size (e.g., -Xms4g)
-Xmx<size>              # Maximum heap size (e.g., -Xmx4g)
-XX:NewRatio=<n>        # Old:Young ratio (e.g., -XX:NewRatio=2)
-XX:SurvivorRatio=<n>   # Eden:Survivor ratio (e.g., -XX:SurvivorRatio=8)
-XX:MaxTenuringThreshold=<n>  # Max GC cycles before promotion (default: 15)

# GC selection
-XX:+UseG1GC            # Use G1 garbage collector
-XX:+UseZGC             # Use Z garbage collector
-XX:+UseShenandoahGC    # Use Shenandoah garbage collector
-XX:+UseSerialGC        # Use Serial garbage collector
-XX:+UseParallelGC      # Use Parallel garbage collector

# G1 tuning
-XX:MaxGCPauseMillis=<ms>   # Target max pause time (default: 200ms)
-XX:G1HeapRegionSize=<size> # Region size (1-32MB, default auto)

# ZGC tuning
-XX:+UseZGC -Xmx<size>  # ZGC with max heap

# GC logging (Java 9+)
-Xlog:gc*:file=gc.log:time,uptime,level,tags

# Metaspace
-XX:MaxMetaspaceSize=<size>

# Diagnostics
-XX:+PrintGCDetails      # Detailed GC logs (Java 8)
-XX:+PrintHeapAtGC       # Heap state at each GC
```

### Java Code for Memory Monitoring

```java
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryMonitor {
    public static void main(String[] args) {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        System.out.println("Heap Used: " + heapUsage.getUsed() / 1024 / 1024 + " MB");
        System.out.println("Heap Max: " + heapUsage.getMax() / 1024 / 1024 + " MB");

        MemoryUsage nonHeapUsage = memoryBean.getNonHeapMemoryUsage();
        System.out.println("Non-Heap Used: " + nonHeapUsage.getUsed() / 1024 / 1024 + " MB");
    }
}
```

## 13. Easy Example

### Basic Object Allocation

```java
public class BasicMemoryExample {
    public static void main(String[] args) {
        // Primitive: stored on stack (4 bytes)
        int count = 42;

        // Object: reference on stack, object on heap
        String name = new String("Alice");

        // Array: reference on stack, array on heap
        int[] numbers = new int[100];

        System.out.println("count (stack): " + count);
        System.out.println("name ref (stack) -> String (heap): " + name);
        System.out.println("numbers ref (stack) -> array (heap): " + numbers.length);
    }
}
```

**Output:**
```
count (stack): 42
name ref (stack) -> String (heap): Alice
numbers ref (stack) -> array (heap): 100
```

**What happens in memory:**
- `count` (primitive `int`) lives on the stack — 4 bytes
- `name` is a reference on the stack pointing to a `String` object on the heap
- `numbers` is a reference on the stack pointing to an `int[100]` array on the heap

## 14. Medium Example

### Generational Object Lifecycle

```java
import java.util.ArrayList;
import java.util.List;

public class GenerationalExample {
    public static void main(String[] args) {
        List<byte[]> shortLived = new ArrayList<>();
        List<byte[]> longLived = new ArrayList<>();

        // Short-lived objects: created and quickly discarded
        for (int i = 0; i < 1000; i++) {
            shortLived.add(new byte[1024]); // 1KB each in Eden
        }
        shortLived.clear(); // Objects become eligible for GC

        // Long-lived objects: promoted to Old Gen
        for (int i = 0; i < 100; i++) {
            longLived.add(new byte[10240]); // 10KB each
        }

        System.out.println("Short-lived list size: " + shortLived.size());
        System.out.println("Long-lived list size: " + longLived.size());
    }
}
```

**Memory Behavior:**
- Short-lived byte arrays are allocated in Eden, survive Minor GC, and are quickly collected
- Long-lived byte arrays survive multiple GC cycles and are promoted to Old Generation
- Run with `-Xlog:gc*` to observe the GC activity

## 15. Hard Example

### Custom Object Pool with Weak References

```java
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedMemoryExample {

    /**
     * A cache that uses SoftReferences to allow GC to reclaim entries
     * under memory pressure.
     */
    static class SoftCache<K, V> {
        private final Map<K, SoftReference<V>> cache = new ConcurrentHashMap<>();
        private final ReferenceQueue<V> refQueue = new ReferenceQueue<>();

        public void put(K key, Value<V> value) {
            cache.put(key, new SoftReference<>(value, refQueue));
        }

        public V get(K key) {
            SoftReference<V> ref = cache.get(key);
            return ref != null ? ref.get() : null;
        }

        public void cleanup() {
            // Remove entries whose SoftReferences have been collected
            SoftReference<V> ref;
            while ((ref = (SoftReference<V>) refQueue.poll()) != null) {
                cache.values().remove(ref);
            }
        }
    }

    /**
     * A WeakHashMap-like structure for tracking objects without
     * preventing garbage collection.
     */
    static class WeakTracker<T> {
        private final Map<T, WeakReference<T>> tracked = new ConcurrentHashMap<>();

        public void track(T object) {
            tracked.put(object, new WeakReference<>(object));
        }

        public boolean isAlive(T object) {
            WeakReference<T> ref = tracked.get(object);
            return ref != null && ref.get() != null;
        }

        public int aliveCount() {
            int count = 0;
            for (WeakReference<T> ref : tracked.values()) {
                if (ref.get() != null) {
                    count++;
                }
            }
            return count;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SoftCache<String, byte[]> cache = new SoftCache<>();
        WeakTracker<String> tracker = new WeakTracker<>();

        // Populate cache
        for (int i = 0; i < 1000; i++) {
            String key = "entry-" + i;
            byte[] data = new byte[1024 * 100]; // 100KB each
            cache.put(key, data);
            tracker.track(key);
        }

        System.out.println("Before GC - alive: " + tracker.aliveCount());

        // Force GC
        System.gc();
        Thread.sleep(1000);

        System.out.println("After GC - alive: " + tracker.aliveCount());
    }
}
```

**Key Concepts:**
- `SoftReference`: Cleared by GC only when memory is low — ideal for caches
- `WeakReference`: Cleared by GC at the next cycle — ideal for canonicalization
- `ReferenceQueue`: Notification mechanism when references are enqueued after collection

## 16. Enterprise Example

### Connection Pool with Memory Leak Prevention

```java
import java.lang.ref.Cleaner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EnterpriseConnectionPool implements AutoCloseable {

    private static final Cleaner CLEANER = Cleaner.create();
    private final BlockingQueue<Connection> pool;
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final String url;
    private final String username;
    private final String password;
    private volatile boolean closed = false;
    private final Cleaner.Cleanable cleanable;

    // Cleanup action for preventing native resource leaks
    private static class PoolCleanup implements Runnable {
        private final BlockingQueue<Connection> connections;
        private final AtomicInteger activeCount;

        PoolCleanup(BlockingQueue<Connection> connections, AtomicInteger activeCount) {
            this.connections = connections;
            this.activeCount = activeCount;
        }

        @Override
        public void run() {
            System.err.println("CRITICAL: Pool not closed properly! "
                + "Active connections: " + activeCount.get());
            connections.forEach(conn -> {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Failed to close leaked connection: " + e.getMessage());
                }
            });
        }
    }

    public EnterpriseConnectionPool(String url, String username, String password,
                                     int poolSize) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.pool = new ArrayBlockingQueue<>(poolSize);
        this.cleanable = CLEANER.register(this, new PoolCleanup(pool, activeConnections));

        // Pre-populate pool
        for (int i = 0; i < poolSize; i++) {
            try {
                pool.offer(DriverManager.getConnection(url, username, password));
            } catch (SQLException e) {
                throw new RuntimeException("Failed to initialize connection pool", e);
            }
        }
    }

    public Connection getConnection() throws InterruptedException, SQLException {
        if (closed) {
            throw new SQLException("Pool is closed");
        }
        Connection conn = pool.poll(5, TimeUnit.SECONDS);
        if (conn == null) {
            throw new SQLException("Timeout waiting for available connection");
        }
        activeConnections.incrementAndGet();
        return conn;
    }

    public void returnConnection(Connection conn) {
        if (conn == null) return;
        activeConnections.decrementAndGet();
        try {
            if (!conn.isClosed() && !closed) {
                pool.offer(conn);
            }
        } catch (SQLException e) {
            // Connection is broken, discard it
        }
    }

    @Override
    public void close() {
        closed = true;
        cleanable.clean();
        pool.forEach(conn -> {
            try {
                conn.close();
            } catch (SQLException e) {
                // Log and continue
            }
        });
    }

    public static void main(String[] args) throws Exception {
        try (EnterpriseConnectionPool pool = new EnterpriseConnectionPool(
                "jdbc:h2:mem:test", "sa", "", 10)) {

            Connection conn = pool.getConnection();
            try {
                conn.createStatement().executeQuery("SELECT 1");
                System.out.println("Query executed successfully");
            } finally {
                pool.returnConnection(conn);
            }
        }
    }
}
```

**Enterprise Patterns:**
- `AutoCloseable` with `try-with-resources` for deterministic cleanup
- `Cleaner` for emergency cleanup if the pool is not properly closed
- Thread-safe design with `AtomicInteger` and `BlockingQueue`
- Connection leak detection through the `PoolCleanup` action

## 17. Performance

### GC Pause Time Comparison

| Collector | Typical Pause | Throughput | Heap Size | Best For |
|-----------|---------------|------------|-----------|----------|
| **Serial** | 100-500ms | 95-99% | < 256MB | Single-threaded apps |
| **Parallel** | 50-200ms | 95-99% | 256MB-4GB | Batch processing |
| **G1** | 50-200ms | 90-95% | 4GB-32GB | General purpose |
| **ZGC** | < 1ms | 85-95% | 8MB-16TB | Latency-sensitive apps |
| **Shenandoah** | < 10ms | 85-95% | 4GB-4TB | Latency-sensitive apps |

### Tuning Guidelines

```bash
# Fixed heap to avoid resize overhead
-Xms4g -Xmx4g

# G1 with 200ms target pause
-XX:+UseG1GC -XX:MaxGCPauseMillis=200

# ZGC for ultra-low latency
-XX:+UseZGC -XX:+ZGenerational -Xmx16g

# Large region size for large heaps
-XX:G1HeapRegionSize=32m
```

### Allocation Rate

High allocation rates increase GC frequency. Reduce allocation pressure by:
- Reusing objects (object pooling)
- Using primitives instead of wrappers
- Pre-sizing collections (`new ArrayList<>(expectedSize)`)
- Using `StringBuilder` instead of string concatenation in loops

## 18. Time Complexity

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| Object allocation (TLAB) | O(1) | Pointer bump |
| Object allocation (Eden) | O(1) amortized | CAS when needed |
| Minor GC (G1) | O(Live objects in Young) | Proportional to live data |
| Major GC (G1) | O(All live objects) | Mark + sweep + compact |
| Full GC (Serial) | O(Entire heap) | Stop-the-world |
| ZGC concurrent mark | O(Live objects) | Mostly concurrent |
| Escape analysis | O(Allocation sites) | JIT compilation time |

## 19. Space Complexity

| Component | Typical Space | Notes |
|-----------|---------------|-------|
| Java object overhead | 12-16 bytes | Object header + alignment |
| Reference | 4 bytes | With compressed oops (< 32GB heap) |
| Reference | 8 bytes | Without compressed oops |
| Thread stack | ~1MB | Configurable with `-Xss` |
| TLAB | ~64KB-256KB | Per thread, in Eden |
| Metaspace | 50-200MB | Depends on class count |
| Code Cache | 48-240MB | JIT compiled code |
| GC overhead | 5-15% of heap | Algorithm dependent |

## 20. Thread Safety

### Concurrent GC Algorithms

All modern GC algorithms are designed for concurrent operation:

- **G1 GC**: Concurrent marking, parallel evacuation
- **ZGC**: Concurrent marking, concurrent relocation
- **Shenandoah**: Concurrent everything (mark, compact, cleanup)

### Thread Safety of Memory Operations

```java
// NOT thread-safe: shared mutable state
public class UnsafeCounter {
    private static int count = 0; // Race condition!
    public static void increment() { count++; }
}

// Thread-safe: using atomic operations
public class SafeCounter {
    private static final AtomicInteger count = new AtomicInteger(0);
    public static void increment() { count.incrementAndGet(); }
}
```

### Thread-Local Allocation Benefits

TLABs eliminate contention for object allocation. Each thread allocates within its own buffer, requiring no synchronization. When the TLAB is full, a new one is allocated or the thread falls back to Eden with CAS-based allocation.

### Safe Point Mechanism

The JVM uses safe points to coordinate GC pauses. All threads must reach a safe point before GC can proceed. Typical safe points include method returns, loop back-edges, and poll points. Use `-XX:+PrintGCApplicationStoppedTime` to monitor pause durations.

## 21. Best Practices

### Heap Sizing

```bash
# Set equal initial and maximum heap to avoid resize pauses
-Xms4g -Xmx4g

# Set NewRatio based on workload
# Batch processing (many short-lived objects): -XX:NewRatio=2
# Long-lived objects: -XX:NewRatio=3
```

### Object Creation

```java
// Use primitives when possible
int count = 0;        // 4 bytes on stack
Integer count = 0;    // 16+ bytes on heap

// Pre-size collections
List<String> items = new ArrayList<>(expectedSize);

// Use StringBuilder in loops
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();
```

### Resource Management

```java
// Use try-with-resources for deterministic cleanup
try (var conn = dataSource.getConnection();
     var stmt = conn.prepareStatement(sql)) {
    // Resources auto-closed even on exception
}
```

### Reference Types for Caches

```java
// WeakHashMap: entries collected when key has no strong references
Map<Key, Value> cache = new WeakHashMap<>();

// SoftReference: memory-sensitive cache
SoftReference<byte[]> cache = new SoftReference<>(largeData);
```

### ThreadLocal Cleanup

```java
private static final ThreadLocal<Buffer> BUFFER =
    ThreadLocal.withInitial(Buffer::new);

public void process() {
    try {
        Buffer buf = BUFFER.get();
        // Use buffer
    } finally {
        BUFFER.remove(); // Prevent memory leak in thread pools
    }
}
```

## 22. Common Mistakes

### Mistake 1: Static Collections as Caches

```java
// BAD: Memory leak
public class Cache {
    private static final Map<String, Object> cache = new HashMap<>();
    public static void put(String key, Object value) {
        cache.put(key, value); // Never released!
    }
}

// GOOD: Use WeakHashMap or bounded cache
public class Cache {
    private static final Map<String, Object> cache =
        new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_SIZE;
            }
        };
}
```

### Mistake 2: Unclosed Resources

```java
// BAD: Resource leak
public void readFile(String path) throws IOException {
    InputStream in = new FileInputStream(path);
    // If exception here, stream is never closed
    int data = in.read();
    in.close();
}

// GOOD: try-with-resources
public void readFile(String path) throws IOException {
    try (var in = new FileInputStream(path)) {
        int data = in.read();
    }
}
```

### Mistake 3: String Concatenation in Loops

```java
// BAD: Creates N intermediate String objects
String result = "";
for (String s : list) {
    result += s; // Each += creates a new String
}

// GOOD: Single allocation
StringBuilder sb = new StringBuilder();
for (String s : list) {
    sb.append(s);
}
String result = sb.toString();
```

### Mistake 4: Unbounded Recursion

```java
// BAD: StackOverflowError
public int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1); // Deep recursion fills stack
}

// GOOD: Iterative approach
public long factorial(int n) {
    long result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}
```

## 23. Pitfalls

| Pitfall | Description | Impact | Solution |
|---------|-------------|--------|----------|
| **Memory leak in static fields** | Objects referenced by static fields never GC'd | OOM | Use WeakHashMap, clear entries |
| **ThreadLocal leaks** | ThreadLocal values not cleaned in thread pools | Memory growth | Always call `remove()` |
| **String.intern() abuse** | Interning too many strings fills String Pool | OOM | Use explicit caches instead |
| **Finalizers** | `finalize()` delays GC, unpredictable timing | Performance | Use `Cleaner` instead |
| **Classloader leaks** | Dynamic class loading without unloading | Metaspace growth | Use isolated classloaders |
| **NIO direct buffers** | Off-heap memory not tracked by GC | OOM | Monitor with `-XX:MaxDirectMemorySize` |
| **Excessive boxing** | Auto-boxing creates wrapper objects | GC pressure | Use primitives, `Stream.mapToInt` |

## 24. Debugging Tips

### Enabling GC Logging

```bash
# Java 9+ unified logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=10,filesize=100M

# Java 8
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.log
```

### Taking Heap Dumps

```bash
# On OutOfMemoryError
-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof

# Manual heap dump
jcmd <pid> GC.heap_dump /tmp/heapdump.hprof

# Using jmap
jmap -dump:live,format=b,file=/tmp/heapdump.hprof <pid>
```

### Diagnosing with JFR

```bash
# Start JFR recording
jcmd <pid> JFR.start name=myrecording duration=60s filename=recording.jfr

# View JFR data
jfr print --events GCHeapStatistics recording.jfr
jfr print --events GCReferenceStatistics recording.jfr
```

### VisualVM Profiling

1. Connect to running JVM process
2. Monitor heap usage in the "Monitor" tab
3. Take heap dump via "Sampler" tab
4. Analyze dominator tree for largest objects

### Eclipse MAT Analysis

1. Open heap dump in Eclipse MAT
2. Run "Leak Suspects" report
3. Examine "Dominator Tree" for memory hogs
4. Use OQL to query specific object patterns

## 25. Comparison Table

| Feature | Stack | Heap |
|---------|-------|------|
| **Purpose** | Method execution & local variables | Object storage & dynamic allocation |
| **Allocation** | LIFO (automatic) | Dynamic (GC-managed) |
| **Size** | ~1MB/thread (configurable) | Multi-GB |
| **Speed** | Very fast (pointer arithmetic) | Slower (allocation + GC overhead) |
| **Lifetime** | Method scope | Until GC collects unreachable objects |
| **Thread Safety** | Thread-local (no sync needed) | Shared (requires synchronization) |
| **Fragmentation** | None | Possible (compacted by GC) |
| **Tuning Flag** | `-Xss` | `-Xms`, `-Xmx` |
| **Error** | `StackOverflowError` | `OutOfMemoryError` |

| GC Algorithm | Pause Time | Throughput | Heap Size | Default Since |
|--------------|------------|------------|-----------|---------------|
| **Serial** | High (100-500ms) | Highest | < 256MB | Always available |
| **Parallel** | Medium (50-200ms) | High | 256MB-4GB | Java 8 default |
| **G1** | Low (50-200ms) | Good | 4GB-32GB | Java 9 default |
| **ZGC** | Ultra-low (< 1ms) | Good | 8MB-16TB | Java 21 default |
| **Shenandoah** | Ultra-low (< 10ms) | Good | 4GB-4TB | Available via flag |

## 26. Decision Tree

### Which GC Algorithm to Choose?

```
Application Type?
├── Batch/Throughput-focused
│   └── Use Parallel GC (-XX:+UseParallelGC)
│
├── Latency-sensitive (< 10ms pauses)
│   ├── Heap > 4TB?
│   │   └── Use ZGC (-XX:+UseZGC)
│   └── Heap < 4TB?
│       ├── Use ZGC (Java 21+ default)
│       └── Or Shenandoah (-XX:+UseShenandoahGC)
│
├── General purpose
│   ├── Java 21+?
│   │   └── ZGC Generational (default)
│   └── Java 9-20?
│       └── G1 (default)
│
├── Single-threaded / embedded
│   └── Use Serial GC (-XX:+UseSerialGC)
│
└── Unknown / mixed workload
    └── Use G1 or ZGC (safe defaults)
```

### When to Tune Memory?

```
Application behavior?
├── Frequent Full GCs
│   ├── Increase heap (-Xmx)
│   └── Check for memory leaks
│
├── Long GC pauses
│   ├── Switch to ZGC or Shenandoah
│   └── Reduce allocation rate
│
├── OutOfMemoryError
│   ├── Java heap space → Increase -Xmx
│   ├── Metaspace → Increase -XX:MaxMetaspaceSize
│   ├── Direct buffer → Increase -XX:MaxDirectMemorySize
│   └── Check for memory leaks
│
└── High CPU from GC
    ├── Reduce allocation rate
    ├── Profile allocation hotspots
    └── Increase heap if under-provisioned
```

## 27. Interview Questions

### Basic

1. **What are the main memory areas in the JVM?**
   Heap (objects), Metaspace (class metadata), Thread Stacks (method execution), Code Cache (JIT compiled code), and Native Memory (JNI, direct buffers).

2. **What is the difference between heap and stack?**
   Stack stores primitives and local variables, is thread-local, and uses LIFO allocation. Heap stores objects, is shared across threads, and uses garbage collection for deallocation.

3. **What is a GC root?**
   GC roots are starting points for reachability analysis. They include thread stack variables, static fields, JNI references, monitors, and system class loaders.

### Intermediate

4. **Explain the generational hypothesis.**
   Most objects die young. The JVM separates memory into Young and Old generations to optimize GC. Young Gen is collected frequently (Minor GC), while Old Gen is collected less often.

5. **What is TLAB and why does it exist?**
   Thread-Local Allocation Buffer is a private allocation buffer in Eden for each thread. It eliminates contention for object allocation by allowing lock-free pointer bump allocation.

6. **What is the difference between G1 and ZGC?**
   G1 uses regions and achieves low pause times through incremental collection. ZGC uses concurrent collection with sub-millisecond pauses, supports larger heaps, and is the default in Java 21.

### Advanced

7. **How does escape analysis work?**
   Escape analysis determines if an object's reference escapes the current method. If it doesn't, the JVM can allocate it on the stack (scalar replacement) or eliminate the allocation entirely.

8. **What is the Metaspace and how does it differ from PermGen?**
   Metaspace (Java 8+) replaced PermGen. It stores class metadata in native memory (not heap), grows automatically, and is not subject to fixed-size limits. It can be bounded with `-XX:MaxMetaspaceSize`.

9. **How do you detect a memory leak in production?**
   Enable GC logging, monitor heap usage trends, take heap dumps with JFR or jmap, analyze with Eclipse MAT, look for dominator trees with large retained sizes, and check for growing collections.

10. **What is the impact of compressed oops?**
    Compressed oops reduce pointer size from 8 to 4 bytes, saving memory and improving cache performance. They work for heaps up to ~32 GB. Larger heaps require uncompressed 8-byte pointers.

## 28. Exercises

### Exercise 1: Memory Layout Analysis (Beginner)

Write a program that allocates various object types and prints their estimated memory usage using `java.lang.instrument.Instrumentation`.

```java
// Hint: Implement a premain agent or use jol (Java Object Layout)
```

### Exercise 2: GC Observation (Intermediate)

```java
public class GCObserver {
    public static void main(String[] args) throws InterruptedException {
        // Allocate objects in a loop
        // Trigger GC with System.gc()
        // Print memory usage before and after
        // Observe the effect of different allocation sizes
    }
}
```

### Exercise 3: Reference Types (Intermediate)

Implement a bounded cache using `WeakHashMap` and `LinkedHashMap` with access ordering. Compare memory behavior under load.

### Exercise 4: Memory Leak Detective (Advanced)

Given the following code, identify and fix the memory leak:

```java
public class LeakyService {
    private static final Map<String, List<byte[]>> data = new HashMap<>();

    public void store(String key, byte[] value) {
        data.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }
}
```

### Exercise 5: Custom Object Pool (Advanced)

Implement a thread-safe object pool that uses `SoftReference` for cached objects and a `ReferenceQueue` to clean up collected entries.

## 29. Assignments

### Assignment 1: JVM Memory Report

Write a program that generates a comprehensive memory report including:
- Current heap usage (used, committed, max)
- Non-heap usage (Metaspace, Code Cache)
- Thread count and stack size
- GC statistics (collection count, time spent)

### Assignment 2: Memory Leak Simulator

Create three different memory leak scenarios:
1. Static collection growing indefinitely
2. ThreadLocal not cleaned in a thread pool
3. Unclosed resources (streams, connections)

For each, write the leaky code, demonstrate the leak, then provide the fix.

### Assignment 3: GC Benchmark

Write a benchmark comparing GC algorithms:
- Allocate and discard objects at different rates
- Measure pause times, throughput, and memory usage
- Compare results across Serial, Parallel, G1, and ZGC
- Document findings with graphs

### Assignment 4: Production Memory Troubleshooting

Given a production scenario:
- Application slows down after 2 hours
- GC logs show increasing Full GC frequency
- Heap dump shows 80% Old Gen usage

Diagnose the issue and propose a solution.

## 30. Mini Project: Memory Monitor Dashboard

### Project Description

Build a Java application that monitors JVM memory in real-time and provides a web-based dashboard:

### Requirements

1. **JMX Integration**: Connect to JMX beans for memory metrics
2. **Real-time Metrics**: Heap usage, GC count, GC time, thread count
3. **Web Dashboard**: Simple HTML/JS interface using embedded Jetty
4. **Alerts**: Alert when heap usage exceeds threshold
5. **Historical Data**: Store metrics in-memory for trend analysis

### Implementation Structure

```
memory-monitor/
├── src/main/java/
│   ├── com/example/monitor/
│   │   ├── MemoryMonitor.java
│   │   ├── MetricsCollector.java
│   │   ├── AlertService.java
│   │   └── WebServer.java
│   └── com/example/monitor/model/
│       ├── MemorySnapshot.java
│       └── Alert.java
├── src/main/resources/
│   └── dashboard.html
└── pom.xml
```

### Key Classes

```java
// MetricsCollector.java
public class MetricsCollector {
    private final MemoryMXBean memoryBean;
    private final List<MemorySnapshot> history;

    public MetricsCollector() {
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.history = new ArrayList<>();
    }

    public MemorySnapshot collect() {
        MemoryUsage heap = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();
        MemorySnapshot snapshot = new MemorySnapshot(
            System.currentTimeMillis(),
            heap.getUsed(),
            heap.getMax(),
            nonHeap.getUsed(),
            ManagementFactory.getGarbageCollectorMXBeans().stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionCount)
                .sum(),
            ManagementFactory.getGarbageCollectorMXBeans().stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionTime)
                .sum()
        );
        history.add(snapshot);
        return snapshot;
    }
}
```

### Evaluation Criteria

- Correct JMX metric collection
- Real-time dashboard updates
- Alert accuracy and responsiveness
- Code quality and documentation
- Thread safety and resource management

## 31. Summary

| Concept | Key Takeaway |
|---------|--------------|
| **Heap** | Stores all objects; divided into Young and Old generations |
| **Stack** | Stores primitives and local variables; thread-local |
| **GC Roots** | Starting points for reachability analysis |
| **Young Generation** | Eden + 2 Survivor spaces; collected frequently |
| **Old Generation** | Long-lived objects; collected less frequently |
| **G1 GC** | Region-based; default in Java 9-20 |
| **ZGC** | Concurrent; ultra-low pauses; default in Java 21+ |
| **Memory Leaks** | Caused by unreachable but uncollectable objects |
| **Profiling** | JFR, VisualVM, Eclipse MAT for diagnosis |
| **Best Practices** | Fix heap size, use primitives, clean ThreadLocal, try-with-resources |

### Key JVM Flags to Remember

```bash
-Xms4g -Xmx4g                    # Fixed heap
-XX:+UseZGC                      # Java 21 default
-XX:MaxGCPauseMillis=200         # G1 target pause
-XX:+HeapDumpOnOutOfMemoryError  # Auto heap dump on OOM
-Xlog:gc*:file=gc.log            # GC logging
```

## 32. References

- [JVM Specification - Memory Model](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html)
- [Java Language Specification - Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)
- [G1 GC Tuning Guide](https://docs.oracle.com/en/java/javase/21/docs/technotes/guides/vm/gctuning/g1_gc.html)
- [ZGC Documentation](https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html)
- [Java Object Layout (JOL)](https://openjdk.org/projects/code-tools/jol/)
- [Eclipse Memory Analyzer](https://eclipse.dev/mat/)
- [Java Flight Recorder](https://docs.oracle.com/en/java/javase/21/jfapi/)
- [Effective Java, 3rd Edition](https://www.oreilly.com/library/view/effective-java/9780134686097/) — Joshua Bloch
- [Java Performance: The Definitive Guide](https://www.oreilly.com/library/view/java-performance-the/9781492056027/) — Scott Oaks
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
