# Memory Management in HotSpot

HotSpot manages memory across several areas: the heap, metaspace, native memory, and thread stacks. Understanding memory layout is critical for tuning and diagnosing issues.

## Memory Areas

### Heap

The heap is where Java objects live. It is divided into generations and regions depending on the GC.

```
Heap (controlled by -Xms / -Xmx)
├── Young Generation (survivor space)
│   ├── Eden — New objects allocated here
│   ├── Survivor 0 (From)
│   └── Survivor 1 (To)
└── Old Generation (tenured)
    └── Long-lived objects
```

**Heap sizing flags:**

```bash
-Xms256m           # Initial heap size
-Xmx4g             # Maximum heap size
-Xmn1g             # Young generation size
-XX:NewRatio=2     # Old:Young ratio (2:1 default)
-XX:NewSize=256m   # Initial young gen size
-XX:MaxNewSize=1g  # Maximum young gen size
```

### Metaspace (Native Memory)

Metaspace stores class metadata off-heap. It replaced PermGen in Java 8.

```
Metaspace
├── Class metadata (Klass structures)
├── Constant pool
├── Method bytecode
├── Method handles
├── Annotation data
└── Compressed class pointers (CompressedClassSpace)
```

**Metaspace flags:**

```bash
-XX:MetaspaceSize=256m        # Initial size (soft limit)
-XX:MaxMetaspaceSize=1g       # Maximum size
-XX:CompressedClassSpaceSize=1g  # Compressed class space
```

Metaspace grows automatically up to `MaxMetaspaceSize`. When it hits the limit, a Full GC reclaims unused class data.

### Native Memory

Beyond the heap and metaspace, HotSpot uses native memory for:

- **Thread stacks**: `-Xss` per thread (default 512 KB – 1 MB)
- **Code cache**: JIT-compiled code (`-XX:ReservedCodeCacheSize=240m`)
- **Direct byte buffers**: `ByteBuffer.allocateDirect()` off-heap
- **GC data structures**: Card tables, remembered sets, bitmaps
- **Internal buffers**: String dedup tables, symbol tables

### Thread Stacks

Each thread gets its own stack in native memory:

```
Thread Stack (-Xss1m)
├── Frame: main()
│   ├── Locals: args, x, y
│   ├── Operand stack
│   └── Return address
├── Frame: compute()
│   └── ...
└── Frame: helper()
    └── ...
```

Stack overflow occurs when a thread exceeds its stack size (usually from deep recursion).

## Object Memory Layout

### Object Header

Every Java object has a header in HotSpot:

```
64-bit object layout (with compressed class pointers):
┌──────────────────────────────────────────┐
│ Mark Word (64 bits)                       │
│  - Hash code (31 bits)                    │
│  - GC age (4 bits)                        │
│  - Lock status (2 bits)                   │
│  - Biased locking info                    │
├──────────────────────────────────────────┤
│ Klass Pointer (32 bits, compressed)       │
├──────────────────────────────────────────┤
│ Instance fields (padded to 8-byte align)  │
└──────────────────────────────────────────┘
```

**Mark Word states:**
- **Unlocked**: Hash code + age + 01 tag
- **Lightweight locked**:指向 lock record + 00 tag
- **Heavyweight locked**: 指向 monitor + 10 tag
- **GC marked**: Forwarding pointer + 11 tag

### Array Layout

Arrays have an additional length field:

```
┌──────────────────────────┐
│ Mark Word                 │
│ Klass Pointer             │
│ Array length (32 bits)    │
│ Elements [0..n]           │
└──────────────────────────┘
```

## Memory Allocation

### TLAB (Thread Local Allocation Buffer)

Each thread gets a private allocation buffer for fast, lock-free allocation:

```
TLAB: bump-pointer allocation
  ptr → [  object  |  object  |  free  ]
         ↑ allocated   ↑ next free

When TLAB fills: get a new TLAB from Eden
When Eden fills: trigger Young GC
```

TLAB size: automatically determined by the JVM based on allocation rates.

### Allocation Paths

1. **TLAB fast path**: Bump pointer in TLAB (no lock, ~10ns)
2. **TLAB slow path**: TLAB exhausted, allocate new TLAB
3. **Eden slow path**: Eden full, trigger GC
4. **Old gen slow path**: Promotion or direct allocation for large objects

### Humongous Objects

Objects larger than half a region size in G1 are allocated as "humongous" objects directly in old generation regions. ZGC and Shenandoah handle large objects differently.

## Memory Barriers and Barriers

### Write Barriers

Used by the GC to track object references:

```cpp
void post_barrier(oop* field, oop new_value) {
    // Card table: mark card as dirty
    dirty_card(field);
    // Remembered set: track cross-region references
    remember_ref(field, new_value);
}
```

### Load Barriers (ZGC / Shenandoah)

Used to maintain colored pointer invariants:

```cpp
oop load_barrier(oop* field) {
    oop ref = *field;
    if (needs_relocation(ref)) {
        ref = relocate(ref);  // Move object, update pointer
    }
    return ref;
}
```

## Diagnostic Commands

```bash
# Heap dump
jmap -dump:format=b,file=heap.hprof <pid>

# Heap summary
jmap -heap <pid>

# Finalizer info
jcmd <pid> GC.finalizer_info

# Native memory tracking
-XX:NativeMemoryTracking=summary
jcmd <pid> VM.native_memory summary

# Object histogram
jcmd <pid> GC.class_histogram

# String deduplication
-XX:+UseStringDeduplication  # G1 only
```

## Key Source Files

| File | Purpose |
|------|---------|
| `src/hotspot/share/gc/shared/collectedHeap.hpp` | Heap abstraction |
| `src/hotspot/share/memory/metaspace/` | Metaspace implementation |
| `src/hotspot/share/oops/oop.hpp` | Object representation |
| `src/hotspot/share/runtime/objectMonitor.hpp` | Locking / monitors |
| `src/hotspot/share/gc/shared/threadLocalAllocBuffer.cpp` | TLAB implementation |

## Interview Questions

[5-10 interview questions with answers]

1. **What is this concept?**
   [Answer]

2. **When would you use it?**
   [Answer]

3. **What are the alternatives?**
   [Answer]

4. **What are common mistakes?**
   [Answer]

5. **How does it perform compared to alternatives?**
   [Answer]

## Pitfalls

[Common mistakes and anti-patterns]

## Performance

[Performance considerations and benchmarks]

## Examples

[Code examples demonstrating the concept]

## Internal Working

[How this works under the hood]

## Why This Concept Exists

[Problem this concept solves and motivation behind it]

## Overview

[Brief description of the topic]

## References

[Links to official docs, tutorials, and related topics]

- [Official Documentation](#)
- [Related: topic1](#)
- [Related: topic2](#)
