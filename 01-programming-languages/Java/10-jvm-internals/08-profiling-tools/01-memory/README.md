# 08. Profiling Tools Memory Details

## Profiling Memory Impact

### Sampling Profiler Memory

```
Sampling Data Structures:
├── Per-thread sample buffer: ~1KB per thread
│   └── Stores recent stack traces
├── Global aggregation table: ~100KB-1MB
│   └── Maps (method, caller) to sample count
├── Call tree nodes: ~50-200 bytes per unique call path
│   └── Tree structure of aggregated samples
└── Total: ~1-10MB for typical applications

Memory is reclaimed when profiling stops.
```

### Instrumentation Profiler Memory

```
Instrumentation Overhead:
├── Per-method metadata: ~50-200 bytes
│   ├── Entry/exit code: ~20-50 bytes injected
│   ├── Timing data: ~16 bytes per invocation
│   └── Aggregation: ~100 bytes per method
├── Per-thread timing buffer: ~1KB per thread
│   └── Stores recent timing measurements
└── Total: ~1-50MB depending on instrumented methods

Memory persists during profiling session.
```

### JFR Memory

```
JFR Recording Buffer:
├── Default buffer size: 1MB per thread
├── Number of buffers: 2 per thread (rotating)
├── Total per-thread: ~2MB
├── Global: ~10MB for metadata
└── Recording file: grows with event volume

Memory for 100 threads:
├── 100 threads x 2MB = 200MB
├── Plus global metadata: ~10MB
└── Total: ~210MB during recording
```

### Heap Dump Memory

```
Heap Dump Capture:
├── Full heap dump: ~heap size + 10-20% overhead
│   └── For 4GB heap: ~4.5-5GB dump file
├── Minidump: ~10-50MB (metadata only)
├── Thread dump: ~1-10MB
└── JFR recording: ~100MB-1GB per hour

Heap Dump Analysis (MAT):
├── Dominator Tree: ~50% of heap dump size
├── Leak Suspects: ~20% of heap dump size
├── Histogram: ~10% of heap dump size
└── Total analysis memory: ~80% of dump file size
```
