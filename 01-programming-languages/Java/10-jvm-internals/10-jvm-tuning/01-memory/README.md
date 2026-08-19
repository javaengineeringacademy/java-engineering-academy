# 10. JVM Tuning Memory Details

## Heap Sizing Strategies

### Container Memory Allocation

```
Container Memory Layout:
┌─────────────────────────────────────────┐
│           Container (4GB)                │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  JVM Heap (-Xmx)               │   │
│  │  Young Gen (1/3) + Old Gen     │   │
│  │  ~3GB (75%)                    │   │
│  └─────────────────────────────────┘   │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │  Non-Heap (~1GB, 25%)          │   │
│  │  - Metaspace: ~256MB           │   │
│  │  - Code Cache: ~240MB          │   │
│  │  - Thread Stacks: ~100MB       │   │
│  │  - Direct Buffers: ~100MB      │   │
│  │  - Native Memory: ~200MB       │   │
│  └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
```

### Heap Size Impact

```
Heap Size vs GC Behavior:
├── Too small (-Xmx256m):
│   ├── Frequent GC cycles
│   ├── High allocation failure rate
│   ├── Promotion failures
│   └── Full GC causing long pauses
├── Too large (-Xmx16g):
│   ├── Long GC pauses (more objects to scan)
│   ├── Higher memory cost
│   ├── Longer pause times
│   └── May cause container OOM kills
└── Just right:
    ├── GC pauses meet SLA targets
    ├── Memory utilization balanced
    ├── Promotion rate reasonable
    └── No Full GC or minimal Full GC
```

### Young Generation Sizing

```
Young Gen Impact:
├── Larger Young Gen:
│   ├── More objects allocated before GC
│   ├── Fewer Minor GC cycles
│   ├── Higher memory usage
│   └── Longer Minor GC pauses (more objects to copy)
├── Smaller Young Gen:
│   ├── More frequent Minor GC
│   ├── Lower memory usage
│   ├── Shorter Minor GC pauses
│   └── Higher promotion rate
└── Tuning:
    ├── -XX:NewRatio=2 (Old:Young = 2:1)
    ├── -XX:NewSize=256m (initial Young size)
    └── -XX:MaxNewSize=1g (max Young size)
```
