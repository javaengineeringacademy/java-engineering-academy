# 05. Garbage Collection Internals Deep Dive

## GC Algorithm Fundamentals

### Reachability Analysis

The JVM determines which objects are alive by tracing from GC roots:

```
GC Roots:
├── Thread stack frames (local variables)
├── Static fields in loaded classes
├── JNI (Native) references
├── Monitors (locked objects)
├── JVM internal references (class loaders, etc.)
├── Objects used for synchronization

Reachability:
├── Strongly reachable: Will NOT be collected
├── Softly reachable: May be collected (after GC)
├── Weakly reachable: Will be collected
├── Phantom reachable: Finalized, awaiting cleanup
```

### Object Lifecycle

```
Allocation:
├── New objects allocated in Eden (via TLAB)
├── TLAB provides lock-free allocation
├── Humongous objects go directly to Old Gen (G1)
└── Some objects栈上分配 (escape analysis)

Minor GC:
├── Collects Young Generation
├── Copying algorithm (survivors copied to other Survivor)
├── Fast: only scans live objects in Young Gen
├── Stop-the-world pause (typically < 10ms)
└── Occurs when Eden is full

Major GC / Full GC:
├── Collects entire heap (or Old Gen only)
├── Mark-Compact or Copying algorithm
├── Longer pause (can be 100ms+)
├── Occurs when Old Gen is full or promotion fails
└── Goal: avoid Full GC through proper tuning
```

### GC Algorithms

```
Serial GC:
├── Single-threaded
├── Stop-the-world for entire collection
├── Simple, low overhead
├── Good for small heaps (< 256MB)
└── Flag: -XX:+UseSerialGC

Parallel GC (Throughput):
├── Multi-threaded
├── Stop-the-world for entire collection
├── High throughput (maximize work per time)
├── Good for batch processing
└── Flag: -XX:+UseParallelGC

CMS (Concurrent Mark Sweep):
├── Concurrent marking and sweeping
├── Brief STW for initial mark and remark
├── Lower latency than Parallel
├── Deprecated in Java 9, removed in Java 14
└── Flag: -XX:+UseConcMarkSweepGC

G1 GC:
├── Region-based
├── Concurrent + STW phases
├── Predictable pause times (MaxGCPauseMillis)
├── Default in Java 9+
└── Flag: -XX:+UseG1GC

ZGC:
├── Colored pointers + load barriers
├── Concurrent almost entire time
├── Sub-millisecond pauses
├── Supports heaps up to 16TB
└── Flag: -XX:+UseZGC

Shenandoah:
├── Brooks pointers
├── Concurrent compaction
├── Low, predictable pauses
├── OpenJDK only
└── Flag: -XX:+UseShenandoahGC
```

### G1 GC Internals

G1 divides the heap into equal-sized regions:

```
Heap Region Layout:
┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐
│  E  │  E  │  S  │  O  │  O  │  O  │  H  │ Free│
└─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘

E = Eden (Young)
S = Survivor (Young)
O = Old
H = Humongous (> 50% of region size)
Free = Unassigned

G1 Collection Cycle:
├── Young GC: Collect all Eden + Survivor regions
├── Concurrent Marking: Mark reachable objects
├── Mixed GC: Collect Young + selected Old regions
└── Full GC (rare): Fallback if Mixed GC too slow
```

### ZGC Internals

ZGC uses colored pointers for concurrent collection:

```
Pointer Colors:
├── Marked0 (M0): Concurrent mark in progress
├── Marked1 (M1): Concurrent mark completed
├── Remapped: Object has been relocated
└── Finalizable: Object has finalizer

ZGC Phases:
├── Pause Mark Start (STW < 1ms): Scan roots
├── Concurrent Mark: Trace reachable objects
├── Pause Mark End (STW < 1ms): Process remaining work
├── Concurrent Prepare for Relocate: Plan relocation
├── Pause Relocate Start (STW < 1ms): Initialize relocation
└── Concurrent Relocate: Move objects, update references
```
