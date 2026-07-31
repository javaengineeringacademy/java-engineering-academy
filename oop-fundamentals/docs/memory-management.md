# Memory Management

## Objective
Understand JVM memory model, allocation, and garbage collection fundamentals.

## JVM Memory Model

### Memory Areas

```
┌─────────────────────────────────────────────────────────────┐
│                      JVM Memory                             │
├─────────────────┬─────────────────┬─────────────────────────┤
│    Heap         │   Metaspace     │     Non-Heap            │
│ (Objects)       │  (Class Data)   │  (Stack, Code Cache)    │
├─────────────────┼─────────────────┼─────────────────────────┤
│ Young Gen       │  Klass objects  │  Thread Stacks          │
│  Eden           │  Method data    │  Native Memory          │
│  Survivor 0/1   │  Constant pool  │  Code Cache             │
│ Old Gen         │  Interned str   │  Direct Buffers         │
└─────────────────┴─────────────────┴─────────────────────────┘
```

## Heap Structure

### Generations

| Generation | Purpose | GC Algorithm |
|------------|---------|--------------|
| **Young** | Short-lived objects | Minor GC (Scavenge) |
| **Old** | Long-lived objects | Major GC (Mark-Sweep-Compact) |

### Young Generation
```
Young Generation
┌─────────────┬───────────┬───────────┐
│    Eden     │  Survivor │  Survivor │
│   (80%)     │   S0      │   S1      │
│             │  (10%)    │  (10%)    │
└─────────────┴───────────┴───────────┘
```

### Object Promotion
```
New Object → Eden → S0 → S1 → Old Gen
                ↳ Minor GC survives
```

### Tenuring Threshold
```java
// Object age increments each Minor GC survived
// Default threshold: 15 (configurable via -XX:MaxTenuringThreshold)
-XX:MaxTenuringThreshold=15
```

## Object Allocation

### Allocation Strategies

| Strategy | Use Case |
|----------|----------|
| **TLAB** (Thread-Local Allocation Buffer) | Fast per-thread allocation |
| **Bump Pointer** | Eden (contiguous) |
| **Free List** | Old Gen (fragmented) |

### TLAB (Thread-Local Allocation Buffer)
```java
// Each thread gets private buffer in Eden
// Allocation = pointer bump (fast, no sync)
// When full → request new TLAB or allocate in shared Eden
```

## Garbage Collection

### GC Algorithms

| Collector | Type | Heap Size | Latency | Use Case |
|-----------|------|-----------|---------|----------|
| **Serial** | Single-threaded | Small | High | Single-threaded apps |
| **Parallel** | Multi-threaded | Medium | Medium | Batch processing |
| **G1** | Regional, incremental | Large | Low | General purpose (default Java 9+) |
| **ZGC** | Concurrent | Huge | Ultra-low | Low-latency apps |
| **Shenandoah** | Concurrent | Large | Ultra-low | Low-latency apps |

### G1 GC (Default Java 9+)
```
Heap → Regions (1-32MB each)
Eden, Survivor, Old, Humongous regions

Phases:
1. Young GC (Eden full) → Eden + Survivors
2. Mixed GC (Old Gen pressure) → Old + some Young
3. Full GC (fallback) → Full heap
```

### ZGC (Ultra-Low Latency)
```
Concurrent marking + relocation
Pause times < 1ms
Supports terabytes of heap
```

## Object Lifecycle

```
NEW → [REACHABLE] → [UNREACHABLE] → FINALIZATION → COLLECTED
                      ↑
              GC Roots: stack vars, static fields, JNI refs
```

### Reachability
```
Strong → Soft → Weak → Phantom → Unreachable
   ↓         ↓         ↓          ↓
 Normal   Memory   Cache      Finalization   GC
 sensitive  sensitive          cleanup
```

### Reference Types

| Type | Strength | Use Case |
|------|----------|----------|
| **Strong** | Normal | Default |
| **Soft** | Memory-sensitive | Caches |
| **Weak** | Non-essential | Canonical maps |
| **Phantom** | Finalization | Cleanup |

```java
// Soft Reference - cache
SoftReference<Bitmap> ref = new SoftReference<>(bitmap);

// Weak Reference - canonical map
WeakReference<Class> weakRef = new WeakReference<>(clazz);

// Phantom Reference - cleanup
PhantomReference<Resource> ref = new PhantomReference<>(res, queue);
```

## Memory Leaks

### Common Causes

| Leak Type | Cause | Detection |
|-----------|-------|-----------|
| Static collections | Growing cache | Heap dump analysis |
| Listeners not removed | Observer pattern | Leak detection tools |
| ThreadLocal not cleaned | Thread pools | Memory profilers |
| Unclosed resources | Streams, connections | Leak detectors |

### Prevention

```java
// WeakHashMap for caches
Map<Key, Value> cache = new WeakHashMap<>();

// Clean up listeners
button.removeActionListener(listener);

// Close resources
try (Resource r = new Resource()) { ... }

// Clean ThreadLocal
threadLocal.remove();
```

## Tools

| Tool | Purpose |
|--------|---------|
| **jcmd** | JVM diagnostics |
| **jmap** | Heap dump |
| **jhat** | Heap analysis |
| **VisualVM** | Profiling |
| **JConsole** | Monitoring |
| **JFR** (Flight Recorder) | Deep profiling |
| **Eclipse MAT** | Heap analysis |

## Monitoring

```bash
# Heap usage
jcmd <pid> GC.heap_info

# GC logs
-XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xlog:gc*:file=gc.log

# JFR recording
jcmd <pid> JFR.start duration=60s filename=record.jfr
```

## JVM Tuning Flags

```bash
# Heap sizing
-Xms4g -Xmx4g              # Fixed heap
-XX:NewRatio=2             # Old:Young = 2:1
-XX:SurvivorRatio=8        # Eden:Survivor = 8:1

# G1 tuning
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m

# ZGC
-XX:+UseZGC -Xmx16g

# GC logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags
```

## Best Practices

| Practice | Reason |
|----------|--------|
| Size heap appropriately | Avoid frequent GC / OOM |
| Use primitives | Avoid object overhead |
| Object pooling | Reduce allocation pressure |
| Weak/Soft references | Memory-sensitive caches |
| Profile before tuning | Measure first |

## Related Topics
← [Stack vs Heap](stack-vs-heap.md) | → [Garbage Collection](garbage-collection.md)

## References
- [JVM Memory Model](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html)
- [G1 GC Tuning](https://docs.oracle.com/javase/8/docs/technotes/guides/vm/gctuning/g1_gc.html)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se21/html/jls-17.html)