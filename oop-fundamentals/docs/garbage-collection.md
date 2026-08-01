# Garbage Collection

## 1. Introduction

Garbage Collection (GC) is the automatic memory management mechanism in Java that identifies and reclaims memory occupied by objects no longer in use. Unlike languages such as C/C++ where developers manually allocate and free memory, Java's garbage collector runs in the background, freeing developers from error-prone manual memory management while preventing memory leaks and dangling pointer bugs.

In Java 21, the garbage collector has evolved significantly with generational garbage collection, concurrent collectors like ZGC and Shenandoah, and region-based memory management through G1 GC. Understanding garbage collection is essential for building high-performance, scalable Java applications.

---

## 2. Learning Objectives

After completing this section, you will be able to:

- Explain the purpose and benefits of automatic garbage collection
- Identify the different memory areas in the JVM and their roles
- Understand generational memory management and object lifecycle
- Compare GC algorithms (Serial, Parallel, G1, ZGC, Shenandoah) and choose the right one
- Tune GC parameters for specific workloads
- Diagnose and resolve memory-related performance issues using monitoring tools
- Recognize common memory leak patterns and apply prevention strategies

---

## 3. Prerequisites

Before studying garbage collection, you should be familiar with:

- **Java fundamentals**: Object creation, references, and variable scope
- **OOP concepts**: Classes, inheritance, polymorphism
- **Basic JVM architecture**: Stack vs. heap distinction
- **Java memory model**: Thread safety, happens-before relationships
- **Performance concepts**: Latency, throughput, response time

Recommended reading: [Memory Management](memory-management.md), [Instance Members](instance-members.md)

---

## 4. Why This Concept Exists

Manual memory management in languages like C/C++ leads to two critical problems:

1. **Memory leaks**: Failing to free allocated memory causes applications to consume increasing amounts of memory until they crash.
2. **Dangling pointers**: Accessing memory after it has been freed leads to undefined behavior and crashes.

Java eliminates these problems by providing automatic garbage collection. The JVM tracks object references and automatically reclaims memory when objects become unreachable. This design trade-off sacrifices some control and predictability in exchange for dramatically improved developer productivity and application stability.

---

## 5. Problem Statement

Consider a web server handling thousands of requests per second. Each request creates numerous objects: request parameters, response bodies, database connections, temporary buffers. Without garbage collection:

```java
// C-style manual memory management (NOT Java)
Request* req = malloc(sizeof(Request));
Response* res = malloc(sizeof(Response));
// ... process request ...
free(req);
free(res);  // What if we forget this? MEMORY LEAK!
```

Java's garbage collector solves this by automatically tracking which objects are still reachable and reclaiming unreachable ones, ensuring memory is efficiently managed without developer intervention.

---

## 6. Theory

### What is Garbage Collection?

Garbage collection is a form of automatic memory management where the JVM periodically identifies objects that are no longer reachable by the application and reclaims their memory. The process involves:

1. **Reachability analysis**: Traversing from GC roots (stack variables, static fields, JNI references) to determine which objects are still accessible.
2. **Marking**: Tagging all reachable objects.
3. **Sweeping**: Reclaiming memory occupied by unmarked objects.
4. **Compaction** (optional): Moving reachable objects together to eliminate fragmentation.

### Generational Hypothesis

Most objects in Java are short-lived. The generational hypothesis states that objects created recently are more likely to become unreachable quickly. This insight drives the design of generational garbage collectors, which divide the heap into generations to optimize collection efficiency.

### GC Roots

GC roots are the starting points for reachability analysis. They include:

| GC Root Type | Description |
|--------------|-------------|
| **Stack local variables** | Variables in currently executing methods |
| **Static fields** | Class-level variables holding object references |
| **JNI references** | Native method references to Java objects |
| **Thread references** | Active threads and their stacks |
| **Synchronized monitors** | Objects currently locked |

---

## 7. Internal Working

### Mark-Sweep-Compact Algorithm

```
Phase 1: MARK
┌─────────────────────────────────────────┐
│  Start from GC Roots                     │
│  Traverse object graph                   │
│  Mark reachable objects                  │
│                                          │
│  [Root] → [A] → [B] → [C] (marked)     │
│       ↘ [D] (marked)                    │
│            [E] (unmarked - garbage)      │
└─────────────────────────────────────────┘

Phase 2: SWEEP
┌─────────────────────────────────────────┐
│  Scan entire heap                         │
│  Free memory of unmarked objects         │
│                                          │
│  [A] [B] [C] [D]    [E] → FREE          │
└─────────────────────────────────────────┘

Phase 3: COMPACT (optional)
┌─────────────────────────────────────────┐
│  Move live objects together              │
│  Eliminate memory fragmentation          │
│                                          │
│  [A][B][C][D]  [free space contiguous]   │
└─────────────────────────────────────────┘
```

### Object Promotion Flow

```
New Object → Eden Space
                ↓
         Minor GC Triggered
                ↓
    ┌───────────┴───────────┐
    │  Reachable?           │
    │  YES → Move to Survivor│
    │  NO  → Collect        │
    └───────────┬───────────┘
                ↓
         After N Minor GCs
         (age >= threshold)
                ↓
         Promote to Old Gen
                ↓
         Major GC / Full GC
```

---

## 8. JVM Perspective

### How the JVM Manages GC

The JVM implements garbage collection through several subsystems:

```
┌─────────────────────────────────────────────────────┐
│                    JVM Runtime                       │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │  GC Manager  │  │ Heap Manager│  │  Memory     │ │
│  │  - Triggers  │  │ - Allocation│  │  Allocator  │ │
│  │  - Scheduling│  │ - Regions   │  │  - TLAB     │ │
│  │  - Roots     │  │ - Segments  │  │  - Bump Ptr │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
│         │                │                │         │
│         ▼                ▼                ▼         │
│  ┌─────────────────────────────────────────────┐   │
│  │           Garbage Collector                  │   │
│  │  - Mark phase (concurrent/STW)               │   │
│  │  - Sweep phase (concurrent/STW)              │   │
│  │  - Compact phase (concurrent/STW)            │   │
│  └─────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

### GC Threads

- **GC Worker Threads**: Perform the actual marking and sweeping
- **Concurrent Threads**: Run alongside application threads (G1, ZGC)
- **Stop-The-World (STW) Threads**: Pause application threads for root scanning

---

## 9. Memory Representation

### Object Layout in Memory

```
┌──────────────────────────────────────────────────────────┐
│                   JVM Heap (Young Generation)            │
├──────────────────────────────────────────────────────────┤
│                                                          │
│  Eden Space (80%)              Survivor Spaces (10% each)│
│  ┌──────────────────┐          ┌──────┐  ┌──────┐       │
│  │ Object A (16B)   │          │  S0  │  │  S1  │       │
│  │ Object B (32B)   │          │      │  │      │       │
│  │ Object C (24B)   │          │      │  │      │       │
│  │                  │          │      │  │      │       │
│  └──────────────────┘          └──────┘  └──────┘       │
│                                                          │
├──────────────────────────────────────────────────────────┤
│                   JVM Heap (Old Generation)              │
│  ┌─────────────────────────────────────────────────────┐ │
│  │ Long-lived Object D (128B)                          │ │
│  │ Long-lived Object E (64B)                           │ │
│  └─────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────┘
```

### Object Header Structure

```
┌─────────────────────────────────────────────┐
│              Object Header                   │
├─────────────────┬───────────────────────────┤
│  Mark Word (64B)│  Klass Pointer (32/64B)   │
│  - GC age       │  - Class metadata ref     │
│  - Lock state   │                           │
│  - Hash code    │                           │
├─────────────────┴───────────────────────────┤
│              Instance Data                   │
│  - field1: type                             │
│  - field2: type                             │
├─────────────────────────────────────────────┤
│              Padding (to 8-byte boundary)   │
└─────────────────────────────────────────────┘
```

---

## 10. Syntax

### Enabling GC Algorithms

```bash
# G1 GC (default in Java 9+)
java -XX:+UseG1GC -jar Application.jar

# ZGC (ultra-low latency)
java -XX:+UseZGC -Xmx16g -jar Application.jar

# Shenandoah GC
java -XX:+UseShenandoahGC -jar Application.jar

# Parallel GC (throughput-focused)
java -XX:+UseParallelGC -jar Application.jar
```

### Heap Sizing Flags

```bash
# Set minimum and maximum heap size
java -Xms2g -Xmx4g -jar Application.jar

# Young generation ratio
java -XX:NewRatio=2 -jar Application.jar  # Old:Young = 2:1

# Survivor space ratio
java -XX:SurvivorRatio=8 -jar Application.jar  # Eden:Survivor = 8:1
```

### GC Logging

```bash
# Java 21 unified logging
java -Xlog:gc*:file=gc.log:time,uptime,level,tags -jar Application.jar

# GC logging with rotation
java -Xlog:gc*:file=gc.log:time,uptime,level,tags:filecount=5,filesize=10m \
     -jar Application.jar
```

---

## 11. Easy Example

```java
public class GcBasicExample {

    public static void main(String[] args) {
        // Object becomes eligible for GC when no references remain
        createAndForget();

        // Force GC (for demonstration only - never do this in production)
        System.gc();

        System.out.println("GC triggered. Check logs for collection details.");
    }

    private static void createAndForget() {
        // Local reference - lives on stack
        String message = "Hello";

        // After this block, 'temp' is unreachable
        {
            String temp = "Temporary object";
            System.out.println(temp);
        }
        // 'temp' is now eligible for garbage collection
    }
}
```

---

## 12. Medium Example

```java
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;

public class GcReferenceExample {

    // WeakHashMap entries are collected when keys are unreachable
    private static final Map<Object, String> cache = new WeakHashMap<>();

    public static void main(String[] args) {
        // Strong reference - won't be collected while reachable
        String strong = new String("Strong Reference");

        // Soft reference - collected only under memory pressure
        SoftReference<String> soft = new SoftReference<>(new String("Soft Reference"));

        // Weak reference - collected at next GC cycle
        WeakReference<String> weak = new WeakReference<>(new String("Weak Reference"));

        // Demonstrate weak reference collection
        System.out.println("Weak before GC: " + weak.get());
        System.gc();
        Thread.onSpinWait();
        System.out.println("Weak after GC:  " + weak.get());  // Likely null

        // Demonstrate WeakHashMap behavior
        addCacheEntries();

        System.gc();
        Thread.onSpinWait();

        System.out.println("Cache after GC: " + cache.size());
    }

    private static void addCacheEntries() {
        for (int i = 0; i < 1000; i++) {
            // Key objects are created locally and become unreachable
            Object key = new Object();
            cache.put(key, "Value " + i);
        }
        System.out.println("Cache before GC: " + cache.size());
    }
}
```

---

## 13. Hard Example

```java
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

public class GcAdvancedExample {

    // Phantom reference queue for cleanup notifications
    private static final ReferenceQueue<Object> phantomQueue = new ReferenceQueue<>();
    private static final List<PhantomReference<Object>> phantomRefs = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        // Demonstrate object lifecycle and GC interaction
        demonstrateObjectPromotion();

        // Demonstrate phantom reference cleanup
        demonstratePhantomReferences();

        // Demonstrate memory pressure and GC behavior
        demonstrateMemoryPressure();
    }

    private static void demonstrateObjectPromotion() throws InterruptedException {
        System.out.println("=== Object Promotion Demo ===");

        // Create objects that survive multiple GC cycles
        List<byte[]> survivors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            survivors.add(new byte[1024 * 1024]);  // 1MB each
            System.out.printf("Created object %d, triggering GC%n", i);

            if (i % 3 == 0) {
                System.gc();
                Thread.onSpinWait();
            }
        }

        // Keep references alive
        survivors.clear();
    }

    private static void demonstratePhantomReferences() throws InterruptedException {
        System.out.println("\n=== Phantom Reference Demo ===");

        // Create object with phantom reference
        Object obj = new Object();
        PhantomReference<Object> phantomRef = new PhantomReference<>(obj, phantomQueue);
        phantomRefs.add(phantomRef);

        System.out.println("Phantom get before GC: " + phantomRef.get());  // null

        // Remove strong reference
        obj = null;
        System.gc();
        Thread.onSpinWait();

        // Check if object was enqueued
        var ref = phantomQueue.poll();
        System.out.println("Phantom ref enqueued: " + (ref != null));

        // Perform cleanup in a separate thread
        startCleanupThread();
    }

    private static void startCleanupThread() {
        Thread cleanupThread = new Thread(() -> {
            try {
                while (true) {
                    var ref = phantomQueue.remove();
                    System.out.println("Cleaning up phantom-referenced object");
                    ref.clear();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Phantom-Cleanup");
        cleanupThread.setDaemon(true);
        cleanupThread.start();
    }

    private static void demonstrateMemoryPressure() throws InterruptedException {
        System.out.println("\n=== Memory Pressure Demo ===");

        // Allocate memory to trigger GC
        List<byte[]> pressure = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            pressure.add(new byte[1024 * 1024 * 2]);  // 2MB each
            Thread.sleep(10);
        }

        pressure.clear();
        System.out.println("Memory pressure released");
    }
}
```

---

## 14. Enterprise Example

```java
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EnterpriseCacheService {

    private static final ConcurrentHashMap<String, SoftReference<Object>>
        cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService cleaner =
        Executors.newSingleThreadScheduledExecutor();

    // Initialize cache cleaner on startup
    static {
        cleaner.scheduleAtFixedRate(
            EnterpriseCacheService::cleanStaleEntries,
            5, 5, TimeUnit.MINUTES
        );
    }

    public static void main(String[] args) {
        // Simulate enterprise cache usage
        populateCache();
        demonstrateCacheBehavior();
        simulateMemoryPressure();
    }

    public static <T> void put(String key, T value) {
        cache.put(key, new SoftReference<>(value));
    }

    public static <T> T get(String key) {
        SoftReference<Object> ref = cache.get(key);
        if (ref != null) {
            @SuppressWarnings("unchecked")
            T value = (T) ref.get();
            if (value != null) {
                return value;
            }
            // Value was collected, remove stale entry
            cache.remove(key);
        }
        return null;
    }

    private static void cleanStaleEntries() {
        int cleaned = 0;
        for (var entry : cache.entrySet()) {
            if (entry.getValue().get() == null) {
                cache.remove(entry.getKey());
                cleaned++;
            }
        }
        if (cleaned > 0) {
            System.out.println("Cleaned " + cleaned + " stale cache entries");
        }
    }

    private static void populateCache() {
        for (int i = 0; i < 1000; i++) {
            put("key-" + i, new byte[1024 * 100]);  // 100KB each
        }
        System.out.println("Cache populated: " + cache.size() + " entries");
    }

    private static void demonstrateCacheBehavior() {
        // Access some entries to keep them alive
        for (int i = 0; i < 100; i++) {
            get("key-" + i);
        }

        System.gc();
        Thread.onSpinWait();
        System.out.println("Cache after GC: " + cache.size() + " entries");
    }

    private static void simulateMemoryPressure() {
        // Allocate memory to trigger SoftReference collection
        List<byte[]> pressure = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            pressure.add(new byte[1024 * 1024 * 5]);  // 5MB each
        }

        System.out.println("Cache under pressure: " + cache.size() + " entries");
        pressure.clear();
    }
}
```

---

## 15. Performance

### GC Impact on Application Performance

| Metric | Description | Target |
|--------|-------------|--------|
| **Throughput** | Time spent in application vs. GC | > 95% |
| **Latency** | Pause time per GC event | < 200ms (G1), < 1ms (ZGC) |
| **Footprint** | Memory overhead of GC | Minimal |
| **Frequency** | How often GC runs | Depends on allocation rate |

### GC Pause Time Comparison

| Collector | Typical Pause | Max Pause | When to Use |
|-----------|--------------|-----------|-------------|
| **Serial** | 100-500ms | Seconds | Single-core, small heap |
| **Parallel** | 50-200ms | 500ms+ | Batch processing |
| **G1** | 50-200ms | Configurable | General purpose |
| **ZGC** | < 1ms | < 1ms | Latency-sensitive |
| **Shenandoah** | < 1ms | < 1ms | Latency-sensitive |

### Performance Tuning Checklist

```
□ Set -Xms = -Xmx (avoid heap resizing)
□ Choose appropriate GC algorithm
□ Tune young generation size (-XX:NewRatio)
□ Monitor GC frequency and pause times
□ Set -XX:MaxGCPauseMillis for G1
□ Enable GC logging for analysis
□ Profile before and after tuning
```

---

## 16. Best Practices

### Do's

```java
// Use try-with-resources to ensure deterministic cleanup
try (var connection = dataSource.getConnection();
     var statement = connection.prepareStatement(sql)) {
    // Resources auto-closed, memory eligible for GC
}

// Use WeakHashMap for caches with automatic eviction
Map<Key, Value> cache = new WeakHashMap<>();

// Use SoftReference for memory-sensitive caches
SoftReference<byte[]> cache = new SoftReference<>(largeData);

// Nullify references when done
largeObject = null;
```

### Don'ts

```java
// DON'T: Call System.gc() in production
System.gc();  // Never do this!

// DON'T: Hold references unnecessarily
public class BadPractice {
    private final byte[] hugeArray = new byte[1024 * 1024];  // 1MB
    private final String rarelyUsed = "some value";  // Keep only if needed
}

// DON'T: Use finalizers (deprecated in Java 9+)
protected void finalize() {  // DON'T DO THIS
    // Cleanup code
}
```

### Configuration Best Practices

```bash
# Fixed heap size to avoid resizing overhead
-Xms4g -Xmx4g

# G1 with target pause time
-XX:+UseG1GC -XX:MaxGCPauseMillis=200

# Enable GC logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags
```

---

## 17. Common Mistakes

| Mistake | Symptom | Fix |
|---------|---------|-----|
| Heap too small | Frequent Full GC, OOM | Increase `-Xmx` |
| Heap too large | Long GC pauses | Reduce heap or use ZGC |
| Object retention | Memory leak, growing heap | Remove unnecessary references |
| Finalizer usage | Slow GC, resurrection | Use `Cleaner` or `try-with-resources` |
| String concatenation in loops | Excessive allocation | Use `StringBuilder` |
| Not closing resources | Memory/resource leak | Use `try-with-resources` |
| Calling `System.gc()` | Unpredictable pauses | Remove call |

---

## 18. Pitfalls

### 1. Memory Leaks via Static Collections

```java
public class LeakyCache {
    // BAD: Unbounded cache grows forever
    private static final Map<String, Object> cache = new HashMap<>();

    public void cache(String key, Object value) {
        cache.put(key, value);  // Never released!
    }
}

// GOOD: Use WeakHashMap or bounded cache
public class SafeCache {
    private static final Map<String, Object> cache =
        new WeakHashMap<>();  // Auto-cleanup
}
```

### 2. ThreadLocal Memory Leaks

```java
public class ThreadLocalLeak {
    // BAD: ThreadLocal in thread pool
    private static final ThreadLocal<byte[]> BUFFER =
        ThreadLocal.withInitial(() -> new byte[1024 * 1024]);

    // GOOD: Clean up after use
    public void process() {
        try {
            byte[] buffer = BUFFER.get();
            // Use buffer
        } finally {
            BUFFER.remove();  // Prevent memory leak
        }
    }
}
```

### 3. Invisible Object Retention

```java
public class InvisibleRetention {
    private List<byte[]> data = new ArrayList<>();

    public void add(byte[] bytes) {
        data.add(bytes);
        // data keeps references even after method returns
    }

    public void clear() {
        data.clear();  // Explicit cleanup
    }
}
```

---

## 19. Debugging Tips

### 1. Enable GC Logging

```bash
# Java 21 unified logging
java -Xlog:gc*:file=gc.log:time,uptime,level,tags -jar App.jar

# Analyze GC log output
grep "pause" gc.log | head -20
```

### 2. Use jcmd for Diagnostics

```bash
# Check heap info
jcmd <pid> GC.heap_info

# Run GC manually (diagnostic only)
jcmd <pid> GC.run

# Dump heap for analysis
jcmd <pid> GC.heap_dump /tmp/heap.hprof
```

### 3. Monitor GC with jstat

```bash
# GC statistics every 1 second
jstat -gc <pid> 1000

# Output columns:
# S0C/S1C: Survivor space capacity
# S0U/S1U: Survivor space used
# EC/Eden: Eden space capacity/used
# OC/OU: Old gen capacity/used
# YGC/YGCT: Young GC count/time
# FGC/FGC: Full GC count/time
```

### 4. Analyze Heap Dumps

```bash
# Generate heap dump
jmap -dump:live,format=b,file=/tmp/heap.hprof <pid>

# Analyze with Eclipse MAT or VisualVM
# Look for:
# - Dominator tree (largest objects)
# - Leak suspects (automatic detection)
# - GC roots (why objects are retained)
```

---

## 20. Comparison Table

| Feature | Serial | Parallel | G1 | ZGC | Shenandoah |
|---------|--------|----------|----|----|------------|
| **Algorithm** | Mark-Sweep | Mark-Sweep | Regional | Concurrent | Concurrent |
| **Threads** | Single | Multi | Multi | Multi | Multi |
| **Pause** | High | Medium | Low | Ultra-low | Ultra-low |
| **Throughput** | Low | High | High | High | High |
| **Heap Size** | Small | Medium | Large | Huge | Large |
| **Use Case** | Embedded | Batch | General | Real-time | Real-time |
| **Default** | No | Java 8 | Java 9+ | No | No |
| **Maturity** | Legacy | Stable | Stable | Mature | Mature |

---

## 21. Decision Tree

```
Start: What is your primary concern?
│
├── Throughput (batch processing)
│   └── Use Parallel GC (-XX:+UseParallelGC)
│
├── Latency (real-time systems)
│   ├── Heap < 8GB
│   │   └── Use G1 (-XX:+UseG1GC -XX:MaxGCPauseMillis=50)
│   └── Heap >= 8GB
│       ├── Need < 1ms pauses
│       │   ├── Use ZGC (-XX:+UseZGC)
│       │   └── Use Shenandoah (-XX:+UseShenandoahGC)
│       └── Need < 10ms pauses
│           └── Use G1 (-XX:+UseG1GC -XX:MaxGCPauseMillis=10)
│
├── Memory (heap > 16GB)
│   ├── ZGC (best for very large heaps)
│   └── Shenandoah (alternative)
│
└── Embedded/Small
    └── Use Serial GC (-XX:+UseSerialGC)
```

---

## 22. Interview Questions

1. **How does G1 GC work?**
   - Regionalized heap divides heap into 1-32MB regions
   - Concurrent marking identifies live objects
   - Incremental compaction reduces pause times
   - Mixed GC collects both young and old regions

2. **What is the time complexity of Mark-Sweep-Compact?**
   - O(n) where n = number of objects in heap
   - Mark phase: O(reachable objects)
   - Sweep phase: O(all objects)
   - Compact phase: O(live objects)

3. **What is the "GC pause"?**
   - Time when application threads are stopped (Stop-The-World)
   - Required for safe object graph traversal
   - Duration varies by algorithm and heap state
   - G1: configurable via -XX:MaxGCPauseMillis

4. **How would you tune GC for low latency?**
   - Choose ZGC or Shenandoah for ultra-low latency
   - Keep young generation small to reduce minor GC pauses
   - Set -XX:MaxGCPauseMillis for G1
   - Avoid Full GC by tuning heap size and promotion

5. **What are GC roots?**
   - Starting points for reachability analysis
   - Include stack variables, static fields, JNI references
   - Used to determine which objects are still in use

6. **What is the difference between Minor GC and Major GC?**
   - Minor GC: Collects only young generation (fast)
   - Major GC: Collects entire heap or old generation (slower)
   - Full GC: Collects entire heap including metaspace (slowest)

7. **How do SoftReferences differ from WeakReferences?**
   - SoftReference: Cleared only under memory pressure
   - WeakReference: Cleared at next GC cycle regardless
   - Use SoftReference for caches, WeakReference for canonicalization

---

## 23. Exercises

1. **GC Logging Analysis**: Enable GC logging on a Java application and analyze the output. Identify GC frequency, pause times, and heap usage patterns.

2. **Reference Types**: Write a program that demonstrates Strong, Soft, Weak, and Phantom references. Observe their behavior under memory pressure.

3. **Memory Leak Detection**: Create a program with an intentional memory leak. Use jstat and jcmd to detect and diagnose the leak.

4. **GC Algorithm Comparison**: Run the same workload with Serial, Parallel, and G1 GC. Compare throughput and latency metrics.

5. **Heap Dump Analysis**: Generate a heap dump and analyze it using Eclipse MAT. Identify the largest objects and any potential leaks.

---

## 24. Assignments

1. **Performance Tuning**: Profile a Java application and tune GC parameters to meet specific latency requirements (e.g., 99th percentile < 100ms).

2. **Cache Implementation**: Implement a bounded cache using SoftReferences and WeakHashMap. Measure hit rates under memory pressure.

3. **Monitoring Dashboard**: Create a simple monitoring solution that tracks GC activity and heap usage in real-time.

4. **Leak Prevention**: Review a codebase for common memory leak patterns and implement fixes.

---

## 25. Mini Project

### Project: GC Monitor and Analyzer

Build a GC monitoring tool that:

1. Parses GC logs to extract pause times, frequency, and heap usage
2. Generates statistics (average, max, 99th percentile pause times)
3. Provides recommendations for GC tuning
4. Supports multiple GC algorithms (G1, ZGC, Shenandoah)

**Technologies**: Java 21, file I/O, regex parsing, statistical analysis

**Deliverables**:
- Source code with unit tests
- Documentation with usage examples
- Sample GC log analysis output

---

## 26. Summary

Garbage collection is Java's automatic memory management system that:

- **Eliminates manual memory management** and associated bugs
- **Uses generational collection** based on the observation that most objects are short-lived
- **Provides multiple algorithms** (G1, ZGC, Shenandoah) optimized for different use cases
- **Requires tuning** for optimal performance in production
- **Can be monitored** using built-in tools and logs

Key takeaways:
- G1 is the default and suitable for most applications
- ZGC and Shenandoah provide ultra-low latency for real-time systems
- GC tuning is workload-dependent and requires measurement
- Memory leaks can still occur through reference retention patterns

---

## 27. References

- [JVM Specification - Memory Management](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html)
- [G1 GC Tuning Guide](https://docs.oracle.com/javase/10/gctuning/)
- [ZGC Documentation](https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html)
- [Shenandoah GC](https://wiki.openjdk.org/display/shenandoah/)
- [Java Performance: The Definitive Guide](https://www.oreilly.com/library/view/java-performance-the/9781492056034/)
- [JEP 439: Generational ZGC](https://openjdk.org/jeps/439)
- [JEP 457: Shenandoah](https://openjdk.org/jeps/457)

---

## JVM Memory Areas

```
┌─────────────────────────────────────────────────────────────┐
│                        JVM MEMORY                            │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────┐  │
│  │    Heap      │  │   Metaspace  │  │    Stack           │  │
│  │  (Objects)   │  │  (Classes)   │  │  (Frames)          │  │
│  ├──────────────┤  ├──────────────┤  ├────────────────────┤  │
│  │ Young Gen    │  │ Class metadata│ │ Method frames      │  │
│  │ ├ Eden       │  │ Method data  │ │ Local variables    │  │
│  │ ├ Survivor 1 │  │ Annotations  │ │ Operand stack      │  │
│  │ └ Survivor 2 │  │ Constant pool│ │ Return address     │  │
│  │ Old Gen      │  │              │ │                    │  │
│  └──────────────┘  └──────────────┘  └────────────────────┘  │
│         │                │                    │               │
│         ▼                ▼                    ▼               │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────────────┐  │
│  │  GC Roots    │  │  Native Mem  │  │  Code Cache        │  │
│  │ Stack vars   │  │ Direct Byte  │  │ JIT compiled       │  │
│  │ Static fields│  │ Buffers      │  │ Code               │  │
│  │ JNI refs     │  │ Mapped files │  │                    │  │
│  │ Thread refs  │  │              │  │                    │  │
│  └──────────────┘  └──────────────┘  └────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Heap Generations

| Generation | Purpose | GC Algorithm |
|------------|---------|--------------|
| **Eden** | New objects | Minor GC |
| **Survivor 1/2** | Survived minor GC | Copying |
| **Old Gen** | Long-lived objects | Major GC (Mark-Sweep-Compact) |

### Object Promotion
```
Eden → Survivor 1 → Survivor 2 → Old Gen
  │         │            │          │
  ▼         ▼            ▼          ▼
Minor GC  Minor GC    Minor GC   Major GC
```

## GC Algorithms

| Algorithm | Latency | Throughput | Heap Size | Default |
|-----------|---------|------------|-----------|---------|
| **Serial** | High | Low | Small | No |
| **Parallel** | High | High | Medium | Java 8 |
| **CMS** | Low | Medium | Large | Java 9-13 |
| **G1** | Low | High | Large | **Java 9+** |
| **ZGC** | Ultra-low | High | Huge | Java 11+ |
| **Shenandoah** | Ultra-low | High | Huge | Java 12+ |

## GC Tuning

```bash
# Heap size
-Xms4g -Xmx4g

# Generation ratios
-XX:NewRatio=2          # Old:New = 2:1
-XX:SurvivorRatio=8     # Eden:Survivor = 8:1

# GC Algorithm
-XX:+UseG1GC            # G1 (default Java 9+)
-XX:+UseZGC             # Low latency
-XX:+UseShenandoahGC    # Ultra low latency
```

## GC Monitoring

```bash
# Enable GC logs
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xlog:gc*:file=gc.log

# Tools
jstat -gc <pid> 1000
VisualVM / JConsole / Mission Control
```

## GC Process (Mark-Sweep-Compact)

```
1. MARK:   Traverse from GC roots → mark reachable
2. SWEEP:  Scan heap → free unmarked
3. COMPACT: Move objects → eliminate fragmentation
```

## GC Roots
Objects always reachable:
- Local variables in stack frames
- Static fields
- JNI references
- Thread references
- Synchronized locks

## GC Monitoring

```bash
# Enable GC logs
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xlog:gc*:file=gc.log

# Tools
jstat -gc <pid> 1000
VisualVM / JConsole / Mission Control
```

## GC Tuning Checklist

- [ ] Appropriate heap size (`-Xms` = `-Xmx`)
- [ ] Appropriate GC algorithm for workload
- [ ] No memory leaks (monitor heap trend)
- [ ] GC pause times within SLA
- [ ] Appropriate young/old generation sizing
- [ ] Metaspace sized appropriately
- [ ] Direct memory limits set

## Interview Questions

1. **How does G1 GC work?**
   - Regionalized heap, concurrent marking, incremental compaction

2. **What is the time complexity of Mark-Sweep-Compact?**
   - O(n) where n = number of objects

3. **What is the "GC pause"?**
   - Time application threads stopped for GC

4. **How would you tune GC for low latency?**
   - ZGC/Shenandoah, smaller heap, tune young gen

---

## Further Reading
- [JVM Specification](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html)
- [G1 GC Tuning](https://docs.oracle.com/javase/10/gctuning/)
- [ZGC Documentation](https://docs.oracle.com/en/java/javase/21/gctuning/z-garbage-collector.html)