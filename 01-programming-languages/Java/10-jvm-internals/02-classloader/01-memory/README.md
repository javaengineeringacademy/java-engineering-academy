# 02. ClassLoader Memory Details

## Memory Areas Affected by Class Loading

### Metaspace (Class Metadata Storage)

Metaspace stores all class-related metadata, replacing PermGen from Java 8+:

```
Metaspace Layout
├── Class Metadata
│   ├── Constant Pool
│   ├── Field Descriptors
│   ├── Method Descriptors
│   ├── Bytecode Arrays
│   └── Exception Table
├── Method Metadata
│   ├── Method Bytecode
│   ├── StackMapTable Attributes
│   └── Code Attributes
├── Constant Pool Cache
│   ├── Resolved References
│   └── Resolved Strings
└── Compressed Class Space
    ├── Klass Pointers
    └── Class Hierarchy Data
```

### Memory Footprint Per Class

Each loaded class consumes memory in multiple areas:

```
Per-Class Memory Cost:
├── Metaspace: ~1-3 KB per class (varies with complexity)
│   ├── Class descriptor: ~200 bytes
│   ├── Method metadata: ~100 bytes per method
│   ├── Field metadata: ~50 bytes per field
│   ├── Constant pool: variable (depends on pool size)
│   └── Bytecode: ~100 bytes per method (average)
├── Heap: Instance data (when objects are created)
│   ├── Object header: 16 bytes
│   ├── Instance fields: variable
│   └── Padding: 0-7 bytes
└── Code Cache: JIT-compiled native code
    ├── Non-method: ~5 MB default
    ├── Profiled: ~122 MB default
    └── Non-profiled: ~122 MB default
```

### TLAB and Class Loading Interaction

Thread-Local Allocation Buffers (TLABs) interact with class loading:

```
New Object Allocation:
1. Class loaded → Class object created in Metaspace
2. new ClassName() → Instance allocated in Eden (via TLAB)
3. TLAB bump pointer allocates without synchronization
4. If TLAB full → new TLAB from Eden
5. If Eden full → Minor GC

Class Unloading Impact:
1. Custom ClassLoader becomes unreachable
2. GC collects the ClassLoader
3. All Class objects defined by it become unreachable
4. Metaspace memory reclaimed
5. Code Cache entries for JIT-compiled methods freed
```

### Metaspace Sizing

```bash
# Metaspace configuration
-XX:MetaspaceSize=256m         # Initial size (high default to avoid early GC)
-XX:MaxMetaspaceSize=256m      # Max size (unlimited by default)
-XX:CompressedClassSpaceSize=1g  # Compressed class pointer space

# Monitoring
jcmd <pid> VM.metaspace
jcmd <pid> GC.heap_info
```

### Code Cache and Class Loading

JIT-compiled code is stored in the Code Cache:

```
Code Cache Layout:
├── Non-method Code Cache (~5 MB)
│   ├── Adapter handlers
│   ├── Buffer blobs
│   └── Stub routines
├── Profiled Code Cache (~122 MB)
│   ├── C1-compiled methods
│   └── Optimized for fast compilation
└── Non-profiled Code Cache (~122 MB)
    ├── C2-compiled methods
    └── Optimized for peak performance

When a class is unloaded:
├── Its JIT-compiled code is reclaimed
├── Code Cache entries marked as free
└── Memory returned to the Code Cache pool
```

### ClassLoader Leak Memory Growth

Without proper cleanup, classloader leaks cause continuous memory growth:

```
Without leak fix:
├── Redeploy #1: Load 500 classes → Metaspace: 50 MB
├── Redeploy #2: Load 500 classes → Metaspace: 100 MB (old classes not freed)
├── Redeploy #3: Load 500 classes → Metaspace: 150 MB
└── Eventually: OutOfMemoryError: Metaspace

With leak fix (proper cleanup):
├── Redeploy #1: Load 500 classes → Metaspace: 50 MB
├── Redeploy #2: Old classes GC'd → Metaspace: 50 MB (stable)
└── Metaspace remains stable
```
