# Garbage Collection in HotSpot

HotSpot provides multiple garbage collector implementations, each optimized for different workload characteristics. The GC manages heap memory by automatically reclaiming objects that are no longer reachable.

## How GC Works

### Object Lifecycle

```
Allocation (TLAB bump-pointer)
  ↓
Survival (Young Gen: Eden → Survivor)
  ↓
Promotion (Old Gen)
  ↓
Reclamation (GC cycle)
```

### Roots and Reachability

GC starts from roots and traces reachable objects:

- **Thread stacks**: Local variables in active frames
- **Static fields**: Class variables
- **JNI references**: Global and local references from native code
- **Synchronized objects**: Monitors held by threads

An object is unreachable if there is no path from any root to it.

## GC Algorithms

### Serial GC

```bash
-XX:+UseSerialGC
```

A stop-the-world, single-threaded collector. Best for small heaps and simple applications.

```
Young Gen: Serial scavenge (copying)
Old Gen:   Serial mark-sweep-compact
```

- **Pause model**: All threads stop during collection
- **Use case**: Single-CPU machines, embedded, small heaps (< 256 MB)
- **Throughput**: Limited by single thread
- **Memory overhead**: Low

### Parallel GC (Throughput Collector)

```bash
-XX:+UseParallelGC
```

Uses multiple threads for young and old generation collection. Default GC before Java 9.

```
Young Gen: Parallel scavenge (multi-threaded copying)
Old Gen:   Parallel mark-sweep-compact (multi-threaded)
```

- **Pause model**: Stop-the-world, multi-threaded
- **Use case**: Throughput-focused workloads (batch, scientific computing)
- **Throughput**: Highest among all collectors
- **Tuning**: `-XX:ParallelGCThreads`, `-XX:GCTimeRatio`

### G1 (Garbage-First) GC

```bash
-XX:+UseG1GC
```

Region-based, concurrent collector. Default since Java 9. Balances throughput and latency.

```
Heap divided into equal-sized regions (1–32 MB):
[E][E][E][S][S][O][O][O][H][H]

E = Eden, S = Survivor, O = Old, H = Humongous, H = Free
```

**Collection phases:**
1. **Young GC**: Evacuate Eden and Survivor regions
2. **Concurrent Marking**: Mark reachable objects (concurrent with app)
3. **Mixed GC**: Evacuate old regions with most garbage
4. **Full GC** (fallback): Stop-the-world compaction

**Key G1 flags:**

```bash
-XX:MaxGCPauseMillis=200        # Target max pause (default 200ms)
-XX:G1HeapRegionSize=16m        # Region size (1–32 MB)
-XX:G1NewSizePercent=5          # Minimum young gen size
-XX:G1MaxNewSizePercent=60      # Maximum young gen size
-XX:InitiatingHeapOccupancyPercent=45  # IHOP threshold
```

### ZGC

```bash
-XX:+UseZGC
```

Ultra-low latency collector with sub-millisecond pauses. Uses colored pointers and load barriers.

**How ZGC works:**
1. Concurrent marking using colored pointers
2. Concurrent compaction using load barriers
3. Relocation via pointer forwarding

```
ZGC Phases:
1. Pause Mark Start (sub-ms) — flip mark bitmap
2. Concurrent Mark — trace reachable objects
3. Pause Mark End (sub-ms) — finalize marking
4. Concurrent Prepare for Relocate — identify regions to compact
5. Pause Relocate Start (sub-ms) — initialize forwarding
6. Concurrent Relocate — move objects, update pointers
```

**Key ZGC flags:**

```bash
-XX:SoftMaxHeapSize=4g          # Soft heap limit
-XX:ConcGCThreads=2             # Concurrent GC threads
-XX:ZCollectionInterval=5       # Proactive GC interval (seconds)
-XX:ZAllocationSpikeTolerance=2 # Allocation spike tolerance
```

### Shenandoah GC

```bash
-XX:+UseShenandoahGC
```

Ultra-low latency collector using Brooks pointers for concurrent compaction.

**How Shenandoah works:**
1. Concurrent marking
2. Concurrent compaction using forwarding pointers
3. Concurrent reference processing

```
Shenandoah Phases:
1. Init Mark (STW) — scan roots
2. Concurrent Mark — trace objects
3. Final Mark (STW) — complete marking
4. Concurrent Cleanup — reclaim unreachable
5. Concurrent Evacuation — move objects
6. Init Update Refs (STW) — prepare for pointer updates
7. Concurrent Update Refs — update all references
8. Final Update Refs (STW) — complete updates
```

**Key Shenandoah flags:**

```bash
-XX:ShenandoahGCHeuristics=adaptive  # Heuristic mode
-XX:ShenandoahMinFreeThreshold=10    # Min free heap before GC
-XX:ShenandoahUncommitDelay=30000    # Memory uncommit delay (ms)
```

## GC Comparison

| GC | Pause Time | Throughput | Heap Size | Best For |
|----|------------|------------|-----------|----------|
| Serial | High (100ms+) | Low | Small | Embedded, simple apps |
| Parallel | Medium (50ms+) | Highest | Medium–Large | Batch, throughput |
| G1 | Low (10–200ms) | High | Medium–Large | General purpose |
| ZGC | Ultra-low (<1ms) | Medium–High | Large | Latency-critical |
| Shenandoah | Ultra-low (<1ms) | Medium–High | Large | Latency-critical |

## GC Logging and Monitoring

### Unified Logging

```bash
# Basic GC logging
-Xlog:gc

# Detailed GC logging
-Xlog:gc*:file=gc.log:time,uptime,level,tags

# GC + heap details
-Xlog:gc+heap=debug:file=gc.log
```

### GC-Specific Logging

```bash
# G1 ergonomics
-Xlog:gc+ergo=trace

# ZGC phases
-Xlog:gc+phases=debug

# Shenandoah heuristics
-Xlog:gc+heuristics=debug
```

### Monitoring Tools

```bash
# JFR (Java Flight Recorder)
jcmd <pid> JFR.start name=gc profile=gc

# jstat
jstat -gcutil <pid> 1000

# VisualVM / JConsole
jvisualvm
```

## GC Tuning Principles

1. **Start with defaults** — HotSpot auto-tunes GC for most workloads
2. **Set latency target** — Use `-XX:MaxGCPauseMillis` or `-XX:MaxGCTimeMillis`
3. **Measure before tuning** — Use JFR and GC logs to understand behavior
4. **Match GC to workload** — Throughput → Parallel; Latency → ZGC/Shenandoah; Balanced → G1
5. **Avoid Full GC** — Tune young gen size and IHOP to prevent Full GC fallback
