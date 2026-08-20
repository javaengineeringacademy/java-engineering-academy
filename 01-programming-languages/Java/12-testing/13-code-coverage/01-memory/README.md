# Code Coverage - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           JVM Heap                  │
│  - Instrumented class bytecode      │
│  - Probe arrays (boolean[])         │
│  - Execution data records           │
├─────────────────────────────────────┤
│         JaCoCo Agent                │
│  - Runtime data collector           │
│  - Class loader hook                │
│  - Data merger                      │
└─────────────────────────────────────┘
```

## Memory Considerations

- Each class has a probe array
- Probes consume minimal memory
- Execution data stored temporarily
- Reports generated offline (not in test JVM)

## Performance Impact

- Instrumentation: ~5-10% overhead
- Probe execution: Negligible
- Data collection: Minimal
- Report generation: Done post-test
