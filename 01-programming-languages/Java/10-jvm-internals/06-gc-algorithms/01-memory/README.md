# 06. GC Algorithms Memory Details

## Memory Overhead by Algorithm

### G1 GC Memory

```
G1 Memory Overhead:
├── Card Table: ~1.6% of heap
│   └── 512 bytes per card, tracks inter-region refs
├── Remembered Sets: 5-20% of heap
│   └── Per-region sets of incoming references
├── Mark Stacks: ~1% of heap
│   └── Per-thread marking work queues
├── SATB Buffers: ~0.5% of heap
│   └── Per-thread Snapshot-At-The-Beginning buffers
└── Total: ~10-25% of heap

Example: 4GB heap = 400MB-1GB overhead
```

### ZGC Memory

```
ZGC Memory Overhead:
├── Colored Pointers: 4 bits per pointer
│   └── Stored in upper bits of 64-bit references
├── Forwarding Tables: ~0.5% of heap
│   └── Maps old location to new location
├── Page Tables: ~0.2% of heap
│   └── Multi-level page mapping
├── Load Barrier Stubs: compiled code overhead
│   └── ~50 bytes per barrier site
└── Total: ~15-25% of heap

Example: 32GB heap = 5-8GB overhead
```

### Shenandoah Memory

```
Shenandoah Memory Overhead:
├── Brooks Pointers: 8 bytes per object
│   └── Extra word in every object header
├── Mark Bitmaps: ~1.6% of heap
│   └── Tracks marked objects
├── Collection Set: ~1% of heap
│   └── Regions selected for collection
└── Total: ~15-25% of heap

Example: 16GB heap = 2.5-4GB overhead
```

### Humongous Object Memory (G1)

```
Humongous Objects:
├── Size: > 50% of region size (e.g., > 16MB for 32MB regions)
├── Allocation: Consecutive humongous regions
├── Collection: Only during Full GC or Mixed GC
├── Fragmentation: Can cause region fragmentation
└── Mitigation: Use -XX:G1HeapRegionSize to tune region size

Tuning:
├── Increase region size: Fewer humongous objects
├── Decrease region size: Less waste for smaller objects
└── G1HeapRegionSize: 1MB to 32MB (power of 2)
```
