# 05. Garbage Collection Memory Details

## GC Memory Layout

### Heap Structure

```
┌─────────────────────────────────────────────────────────────────┐
│                          Heap                                   │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              Young Generation (1/3)                     │   │
│  │  ┌───────────────┐  ┌──────────┐  ┌──────────┐        │   │
│  │  │    Eden       │  │   S0     │  │   S1     │        │   │
│  │  │   (80%)       │  │  (10%)   │  │  (10%)   │        │   │
│  │  └───────────────┘  └──────────┘  └──────────┘        │   │
│  └─────────────────────────────────────────────────────────┘   │
│                                                                 │
│  ┌─────────────────────────────────────────────────────────┐   │
│  │              Old Generation (2/3)                       │   │
│  │                                                         │   │
│  │  Long-lived objects promoted from Young Generation      │   │
│  └─────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────┘

Off-Heap:
├── Metaspace: Class metadata (unlimited by default)
├── Code Cache: JIT-compiled code (240MB default)
└── Native Memory: Direct buffers, JNI allocations
```

### Object Promotion

```
Object Lifecycle in Memory:
1. Allocation in Eden
2. Survive Minor GC → copied to Survivor
3. Survive N Minor GCs → promoted to Old Gen
4. Old Gen full → Major/Full GC

Promotion Threshold:
-XX:MaxTenuringThreshold=15 (default)
-XX:TargetSurvivorRatio=50 (50% of Survivor target)
```

### GC Roots in Memory

```
GC Root Types and Their Memory Locations:
├── Stack: Local variables, method parameters
│   └── Per-thread, LIFO order
├── Static: Class static fields
│   └── In Metaspace, shared across threads
├── JNI: Native method references
│   └── Native memory, tracked by JVM
├── Monitor: Objects locked by synchronized
│   └── In heap, tracked by object header
└── JVM Internal: Class loaders, etc.
    └── Various locations
```

### Memory Overhead of GC

Each GC algorithm has different memory overhead:

```
Serial GC: Minimal overhead
├── No concurrent threads
├── No additional data structures
└── Memory cost: ~5-10% of heap

Parallel GC: Moderate overhead
├── GC threads stack space
├── Work queues for parallel processing
└── Memory cost: ~10-15% of heap

G1 GC: Significant overhead
├── Remembered Sets (per region)
├── Card tables
├── Mark stacks
├── SATB (Snapshot-At-The-Beginning) buffers
└── Memory cost: ~10-20% of heap

ZGC: Higher overhead
├── Colored pointer metadata
├── Load barrier stubs
├── Relocation forwarding pointers
└── Memory cost: ~15-25% of heap

Shenandoah: Higher overhead
├── Brooks pointers (extra word per object)
├── Concurrent marking bitmaps
├── Forwarding information
└── Memory cost: ~15-25% of heap
```
