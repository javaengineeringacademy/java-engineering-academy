# 06. GC Algorithms Internals Deep Dive

## G1 GC Internals

### Region-Based Architecture

G1 divides the heap into equal-sized regions:

```
Region Types:
├── Eden (E): New object allocation
├── Survivor (S): Objects that survived Young GC
├── Old (O): Long-lived objects
├── Humongous (H): Objects > 50% of region size
└── Free: Unassigned regions

Region Size: 1MB to 32MB (auto-calculated based on heap size)
Default: ~2000 regions for a 4GB heap
```

### G1 Collection Cycle

```
1. Young GC (Evacuation Pause)
   ├── Stop-the-world
   ├── Copy live objects from Eden/Survivor regions
   ├── Promote long-lived objects to Old regions
   ├── Update remembered sets
   └── Resume application threads

2. Concurrent Marking Phase
   ├── Initial Mark (brief STW): Scan GC roots
   ├── Concurrent Mark: Trace object graph
   ├── Remark (brief STW): Process SATB buffers
   └── Cleanup (brief STW): Identify free regions

3. Mixed GC
   ├── Select Young regions + Old regions with most garbage
   ├── Evacuate selected regions
   ├── Copy live objects to new regions
   └── Free evacuated regions

4. Full GC (Fallback)
   ├── Single-threaded (Serial) or multi-threaded
   ├── Compacts entire heap
   ├── Triggered if Mixed GC too slow
   └── Goal: avoid through proper tuning
```

### G1 Key Data Structures

```
Card Table:
├── Divides heap into 512-byte cards
├── Tracks inter-region references
├── Used during remembered set updates
└── Memory cost: ~1.6% of heap

Remembered Sets:
├── Per-region sets of incoming references
├── Used to avoid scanning entire heap
├── Updated during writes (write barrier)
└── Memory cost: ~5-20% of heap (tunable)

Mark Stacks:
├── Per-thread stacks for concurrent marking
├── Hold objects to be scanned
└── Memory cost: proportional to object graph

SATB Buffers:
├── Per-thread buffers for Snapshot-At-The-Beginning
├── Record overwritten references during marking
└── Used to maintain consistent marking view
```

## ZGC Internals

### Colored Pointers

ZGC encodes GC state in the upper bits of object pointers:

```
Pointer Format (64-bit):
┌────────────────────────────────────────────────────────────────┐
│  Unused  │ M0 │ M1 │ Remapped │ Finalizable │    Address     │
│  (16 bits)│(1) │(1) │   (1)    │    (1)      │   (44 bits)   │
└────────────────────────────────────────────────────────────────┘

Colors:
├── Marked0 (M0): Mark bit 0
├── Marked1 (M1): Mark bit 1
├── Remapped: Object has been relocated
├── Finalizable: Object has finalizer
└── Address: Actual memory address (16TB max)
```

### Load Barriers

ZGC uses load barriers for self-healing:

```
Load Barrier Logic:
1. Application loads a reference
2. Barrier checks pointer color
3. If color is wrong (needs healing):
   a. If Marked: heal to current location
   b. If Remapped: do nothing (already correct)
   c. If not yet processed: trigger concurrent processing
4. Return corrected reference
```

### Multi-Phase Collection

ZGC performs collection in phases:

```
Phase 1: Pause Mark Start (STW < 1ms)
├── Scan thread stacks and roots
├── Flip mark bit (M0 <-> M1)
└── Resume application threads

Phase 2: Concurrent Mark
├── Trace object graph from roots
├── Use load barriers for references
└── Application continues running

Phase 3: Pause Mark End (STW < 1ms)
├── Process remaining SATB work
├── Handle reference processing
└── Resume application threads

Phase 4: Concurrent Prepare for Relocate
├── Identify free pages
├── Plan object relocation
└── Compute relocation sets

Phase 5: Pause Relocate Start (STW < 1ms)
├── Initialize forwarding tables
├── Start relocation of roots
└── Resume application threads

Phase 6: Concurrent Relocate
├── Move objects to new pages
├── Update references using load barriers
└── Application continues running
```

## Shenandoah Internals

### Brooks Pointers

Shenandoah uses forwarding pointers for concurrent compaction:

```
Object Header Layout:
┌─────────────────────────────────────────────┐
│  Mark Word (64 bits)                         │
│  - Hash code (31 bits)                       │
│  - GC age (4 bits)                           │
│  - Lock state (2 bits)                       │
├─────────────────────────────────────────────┤
│  Klass Pointer (32/64 bits)                  │
├─────────────────────────────────────────────┤
│  Brooks Pointer (64 bits)                    │
│  - Points to current location of object      │
│  - Initially points to self                  │
│  - Updated during concurrent compaction      │
└─────────────────────────────────────────────┘
```

### Shenandoah Collection Phases

```
Phase 1: Init Mark (STW)
├── Scan roots
├── Set mark bit in Brooks pointer
└── Resume application threads

Phase 2: Concurrent Mark
├── Trace object graph
├── Application continues
└── Mark bits updated via Brooks pointers

Phase 3: Final Mark (STW)
├── Process remaining marking work
├── Complete reference processing
└── Resume application threads

Phase 4: Concurrent Cleanup
├── Reclaim free regions
├── Update region metadata
└── Application continues

Phase 5: Concurrent Evacuation
├── Copy live objects to new regions
├── Update forwarding pointers (Brooks)
├── Application continues
└── Load barriers handle reference updates

Phase 6: Init Update Refs (STW)
├── Prepare for reference update phase
└── Resume application threads

Phase 7: Concurrent Update References
├── Update all references to relocated objects
├── Application continues
└── Load barriers heal references

Phase 8: Final Update Refs (STW)
├── Complete reference updates
├── Clear Brooks pointers
└── Resume application threads
```
