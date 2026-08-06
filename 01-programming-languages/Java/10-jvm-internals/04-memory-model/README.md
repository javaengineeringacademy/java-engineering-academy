# 04. Memory Model - Deep Dive

## Introduction

The JVM Memory Model defines how memory is organized and managed. Understanding memory areas, object lifecycle, memory alignment, and TLAB is critical for writing high-performance Java applications.

## Memory Areas with Sizes

```
┌─────────────────────────────────────────────────────────────┐
│                        Heap                                 │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Young Generation                   │   │
│  │  ┌─────────────┐  ┌─────────────┐  ┌────────────┐  │   │
│  │  │    Eden     │  │  Survivor 0 │  │ Survivor 1 │  │   │
│  │  │   (80%)     │  │   (10%)     │  │   (10%)    │  │   │
│  │  └─────────────┘  └─────────────┘  └────────────┘  │   │
│  └─────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                  Old Generation                     │   │
│  │  (Long-lived objects that survived multiple GC)     │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Code Cache │  │  Metaspace   │  │  Native      │     │
│  │   (JIT code) │  │  (class meta)│  │  Memory      │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
```

## Object Lifecycle in Memory

### 1. Object Allocation
- **Eden Space**: New objects allocated here (via TLAB)
- **TLAB**: Thread-Local Allocation Buffer for lock-free allocation
- **Humongous Objects**: Large objects allocated directly in Old Gen

### 2. Object Promotion
- After surviving N Minor GC cycles → promoted to Old Generation
- Default: `MaxTenuringThreshold = 15`

### 3. Object Collection
- Unreachable objects collected by GC
- Mark-Sweep-Compact or Copy algorithms

## Memory Alignment and Padding

### Object Layout (64-bit JVM)

```
┌─────────────────────────────────────┐
│ Object Header (16 bytes)             │
│  - Mark Word (8 bytes):              │
│    - Hash code (31 bits)             │
│    - GC age (4 bits)                 │
│    - Lock state (2 bits)             │
│  - Klass Pointer (4 or 8 bytes):     │
│    - Points to class metadata        │
├─────────────────────────────────────┤
│ Instance Fields (variable size)      │
│  - Ordered by size (largest first)   │
│  - References: 4 or 8 bytes each     │
├─────────────────────────────────────┤
│ Padding (0-7 bytes)                  │
│  - Align to 8 bytes                  │
└─────────────────────────────────────┘
```

### Alignment Rules

- Objects aligned to 8 bytes on 64-bit JVM
- Padding added to reach alignment boundary
- With compressed oops: references are 4 bytes instead of 8

### Object Size Examples

| Object Type | Header | Fields | Padding | Total |
|-------------|--------|--------|---------|-------|
| Object | 16 | 0 | 8 | 24 |
| Integer | 16 | 4 | 4 | 24 |
| Long | 16 | 8 | 0 | 24 |
| byte[0] | 16 | 4 | 4 | 24 |
| byte[1] | 16 | 5 | 3 | 24 |
| byte[8] | 16 | 12 | 0 | 28→32 |

## TLAB (Thread-Local Allocation Buffer)

### How TLAB Works

```
┌─────────────────────────────────────────┐
│ Eden Space                               │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐ │
│  │ TLAB 1  │  │ TLAB 2  │  │ TLAB 3  │ │
│  │ Thread 1│  │ Thread 2│  │ Thread 3│ │
│  └─────────┘  └─────────┘  └─────────┘ │
└─────────────────────────────────────────┘
```

### TLAB Allocation Process

1. Thread checks if object fits in current TLAB
2. If yes: bump pointer, return object (fast path)
3. If no: allocate new TLAB or use slow path
4. Slow path: Eden allocation with CAS

### TLAB Configuration

```bash
-XX:+UseTLAB              # Enable TLAB (default: true)
-XX:TLABSize=512k         # Initial TLAB size
-XX:MinTLABSize=2k        # Minimum TLAB size
-XX:TLABRefillWasteFraction=64  # Refill waste threshold
```

## Eden Space

- Default: ~80% of Young Generation
- Thread-Local Allocation Buffers (TLABs) for fast allocation
- When Eden fills up → Minor GC

```bash
-XX:NewSize=256m          # Initial young gen size
-XX:MaxNewSize=1g         # Max young gen size
-XX:SurvivorRatio=8       # Eden:Survivor = 8:1:1
```

## Survivor Spaces (S0, S1)

- Two equal-sized spaces (S0 and S1)
- One is always empty (to-space)
- Objects survive N GC cycles → promoted to Old Generation

```bash
-XX:SurvivorRatio=8       # Eden:S0:S1 = 8:1:1
-XX:MaxTenuringThreshold=15  # Max cycles before promotion
-XX:TargetSurvivorRatio=50   # Target survivor occupancy
```

## Old Generation

- Stores long-lived objects
- Objects promoted from Young Generation
- Major GC / Full GC collects this area

```bash
-Xms2g                   # Initial heap size
-Xmx4g                   # Max heap size
-XX:NewRatio=2            # Old:Young = 2:1
```

## Metaspace

- Replaces PermGen (Java 8+)
- Stores class metadata, method metadata, constant pool
- Uses native memory (off-heap)

```bash
-XX:MetaspaceSize=256m     # Initial size
-XX:MaxMetaspaceSize=512m  # Max size
-XX:CompressedClassSpaceSize=1g  # Compressed class pointers
```

## Code Cache

- Stores JIT-compiled native code
- Divided into: Non-method, Profiled, Non-profiled

```bash
-XX:InitialCodeCacheSize=256k   # Initial size
-XX:ReservedCodeCacheSize=256m  # Max size
-XX:CodeCacheExpansionSize=64   # Expansion size
```

## Best Practices

1. **Right-size the heap**: Match heap to application needs
2. **Use TLAB**: Enable for multi-threaded allocation
3. **Monitor Metaspace**: Watch for classloader leaks
4. **Profile object creation**: Minimize short-lived objects
5. **Use appropriate data structures**: Consider memory overhead

## Interview Questions

1. **What is the difference between Eden and Survivor spaces?** - Eden is for new objects, Survivor holds objects that survived GC
2. **What is TLAB?** - Thread-Local Allocation Buffer for lock-free allocation
3. **How are objects laid out in memory?** - Header (16 bytes) + fields + padding
4. **What is Metaspace?** - Class metadata storage (replaces PermGen)

## References

- [Java Memory Model](https://docs.oracle.com/javase/specs/)
- "Java Performance" by Scott Oaks
- "Optimizing Java" by Benjamin J. Evans
