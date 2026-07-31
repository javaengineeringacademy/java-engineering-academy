# Garbage Collection

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