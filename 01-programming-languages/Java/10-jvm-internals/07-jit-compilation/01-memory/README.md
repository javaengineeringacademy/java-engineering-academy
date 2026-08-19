# 07. JIT Compilation Memory Details

## Code Cache Memory

### Code Cache Layout

```
Code Cache (default 240MB):
├── Non-method (~5MB)
│   ├── Adapter handlers: Convert calling conventions
│   ├── Buffer blobs: Temporary code buffers
│   └── Stub routines: GC barriers, safepoint stubs
├── Profiled (~122MB)
│   ├── C1-compiled methods with profiling
│   └── Methods still collecting profiling data
└── Non-profiled (~122MB)
    ├── C2-compiled methods (fully optimized)
    └── High-performance native code
```

### Compilation Memory Overhead

```
Per Compilation Cost:
├── C1 compilation: ~10-50KB native code
│   └── Compilation time: 1-10ms
├── C2 compilation: ~50-500KB native code
│   └── Compilation time: 10-100ms
├── Per-method overhead: ~100-500 bytes metadata
└── Profiling data: ~50-200 bytes per call site

Total code cache usage depends on:
├── Number of hot methods
├── Method complexity
├── Inlining decisions
└── Optimization level
```

### JIT Memory Interaction

```
JIT and Heap Interaction:
├── Compilation threads allocate from native memory
├── Compiled code stored in Code Cache (off-heap)
├── GC does not collect Code Cache entries
├── Code Cache entries freed when methods are unloaded
└── Deoptimized code marked as invalid (space reused)

JIT and Metaspace Interaction:
├── Method metadata stored in Metaspace
├── Compiled code references method metadata
├── Class unloading frees both Metaspace and Code Cache
└── Profiling data stored in MethodDataOop (heap)
```

### Inline Cache Memory

```
Inline Caches (per call site):
├── Monomorphic: One type expected (fast path)
│   └── Memory: ~16 bytes per call site
├── Bimorphic: Two types expected
│   └── Memory: ~32 bytes per call site
├── Polymorphic: Many types (slow path)
│   └── Memory: ~64+ bytes per call site
└── Megamorphic: Too many types (no caching)
    └── Memory: minimal (no inline cache)
```
