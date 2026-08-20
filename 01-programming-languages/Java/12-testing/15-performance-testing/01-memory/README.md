# Performance Testing - Memory

## Memory Model

```
┌─────────────────────────────────────┐
│           Forked JVM                │
│  - Benchmark instances              │
│  - @State objects                   │
│  - JIT compiled code cache          │
│  - JMH infrastructure               │
├─────────────────────────────────────┤
│         JMH Runner                  │
│  - Configuration                    │
│  - Result collector                 │
│  - Report generator                 │
└─────────────────────────────────────┘
```

## Memory Considerations

- Each fork has independent memory
- @State objects persist across iterations
- JIT compilation uses metaspace
- GC may affect results

## Performance Tips

1. Minimize object allocation in benchmarks
2. Use appropriate GC settings
3. Monitor memory usage
4. Consider thread count impact
