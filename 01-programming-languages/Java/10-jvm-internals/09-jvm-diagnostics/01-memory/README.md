# 09. JVM Diagnostics Memory Details

## Diagnostic Data Memory

### Thread Dump Memory

```
Thread Dump Size:
├── Per thread: ~1-10KB
│   ├── Thread name, state, priority: ~100 bytes
│   ├── Stack frames: ~500-5000 bytes (depends on stack depth)
│   ├── Lock information: ~200-500 bytes
│   └── Local variables: ~100-1000 bytes
├── Total for 100 threads: ~100KB-1MB
└── Deadlock information: ~1-10KB

Thread dump is captured in memory and written to output.
Memory is reclaimed after dump is written.
```

### Heap Dump Memory

```
Heap Dump File Size:
├── Full heap dump: ~heap size + 10-20% overhead
│   ├── 4GB heap: ~4.5-5GB dump file
│   ├── 8GB heap: ~9-10GB dump file
│   └── 16GB heap: ~18-20GB dump file
├── Minidump: ~10-50MB (metadata only)
├── Thread dump: ~1-10MB
└── JFR recording: ~100MB-1GB per hour

Heap Dump Capture:
├── Pause all threads (STW)
├── Traverse entire heap
├── Write to disk
└── Resume threads
```

### JFR Recording Memory

```
JFR Buffer Sizing:
├── Default: 1MB per thread (2 rotating buffers)
├── Per-thread: ~2MB total
├── For 100 threads: ~200MB
├── Global metadata: ~10MB
└── Recording file: grows with event volume

JFR Event Overhead:
├── Instant events: ~100-500 bytes per event
├── Duration events: ~200-1000 bytes per event
├── Sampled events: ~50-200 bytes per sample
└── Total: depends on event configuration
```

### jstat Memory

```
jstat Data Structures:
├── Per-thread statistics: ~1KB per thread
├── GC statistics: ~100 bytes per GC event
├── Class loading statistics: ~50 bytes per class
├── JIT compilation statistics: ~50 bytes per compilation
└── Total: ~100KB-1MB for typical applications
```
