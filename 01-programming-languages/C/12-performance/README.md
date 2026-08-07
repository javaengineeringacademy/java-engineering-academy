# Performance — C Language

## What it is
Performance optimization involves making programs faster and more efficient.

## Why it exists
To meet performance requirements and resource constraints.

## When to use it
When profiling shows bottlenecks or requirements demand optimization.

## How it works

### Profiling

```bash
gcc -pg -o program program.c
./program
gprof program gmon.out > analysis.txt
```

### Cache Optimization

```c
// Bad: column-major traversal
for (int j = 0; j < N; j++)
    for (int i = 0; i < N; i++)
        sum += matrix[i][j];

// Good: row-major traversal
for (int i = 0; i < N; i++)
    for (int j = 0; j < N; j++)
        sum += matrix[i][j];
```

### Loop Optimization

```c
// Loop unrolling
for (int i = 0; i < N; i += 4) {
    sum += arr[i];
    sum += arr[i+1];
    sum += arr[i+2];
    sum += arr[i+3];
}
```

### Compiler Optimization

```bash
gcc -O2 -march=native -o program program.c
```

### Memory Pool

```c
typedef struct {
    char *pool;
    size_t offset;
    size_t size;
} MemPool;

void *pool_alloc(MemPool *mp, size_t size) {
    if (mp->offset + size > mp->size) return NULL;
    void *ptr = mp->pool + mp->offset;
    mp->offset += size;
    return ptr;
}
```

## Production Incidents

### Incident 1: Cache Miss Causing Slowdown

**Problem:** A data processing pipeline that should handle 10K records/sec drops to 500 records/sec in production.

**Cause:** Column-major traversal of a large matrix causes constant cache misses:

```c
typedef struct {
    int id;
    char name[64];
    float scores[100];
} Record;

Record *records;  // Array of 1M records

// Bad: column-major access
for (int i = 0; i < 1000000; i++) {
    for (int j = 0; j < 100; j++) {
        sum += records[i].scores[j];  // Stride = sizeof(Record) per i
    }
}
```

**Impact:** 20x performance degradation, SLA violations, customer complaints, revenue loss.

**Detection:** `perf stat` shows 90% L3 cache miss rate. `cachegrind` confirms poor locality.

**Solution:** Restructure data or access pattern for row-major locality:

```c
// Option 1: Transpose data layout
typedef struct {
    float scores[1000000][100];
} ScoreMatrix;

// Option 2: Block processing
for (int i = 0; i < 1000000; i += 64) {
    for (int b = 0; b < 64; b++) {
        for (int j = 0; j < 100; j++) {
            sum += records[i + b].scores[j];
        }
    }
}
```

**Prevention:** Profile with `perf` and `valgrind --tool=cachegrind`, design data structures for sequential access, use `__builtin_prefetch` for predictable patterns.

---

### Incident 2: Branch Prediction Failure

**Problem:** A packet classifier processes only 200K packets/sec instead of the required 1M packets/sec.

**Cause:** Sorting condition on unpredictable data causes constant branch mispredictions:

```c
int classify_packet(Packet *p) {
    if (p->src_port > 1024) {      // Unpredictable branch
        if (p->dst_port < 80) {
            return TYPE_A;
        } else {
            return TYPE_B;
        }
    } else {
        return TYPE_C;
    }
}
```

**Impact:** Pipeline stalls on every misprediction (~15 cycles each), 5x throughput reduction, packet drops, network degradation.

**Detection:** `perf stat` shows 30% branch miss rate. `perf record -e branches` confirms mispredictions in `classify_packet`.

**Solution:** Use branchless techniques or lookup tables:

```c
// Branchless classification
int classify_packet(Packet *p) {
    int type_a = (p->src_port > 1024) & (p->dst_port < 80);
    int type_b = (p->src_port > 1024) & (p->dst_port >= 80);
    return type_a * TYPE_A + type_b * TYPE_B + (!type_a && !type_b) * TYPE_C;
}

// Or: lookup table
int classify_packet(Packet *p) {
    return lookup_table[p->src_port >> 6][p->dst_port >> 6];
}
```

**Prevention:** Profile branch prediction with `perf`, use branchless code for hot paths, sort data before processing to improve predictability.

## Production Checklist

- [ ] Profile before optimizing
- [ ] Focus on hot paths
- [ ] Consider cache locality
- [ ] Use appropriate data structures
- [ ] Enable compiler optimizations

## Maturity Levels

| Level | Description |
|-------|-------------|
| Beginner | Uses compiler flags |
| Intermediate | Optimizes loops and data access |
| Advanced | Implements custom allocators |

## Common Myths

1. **Myth**: Optimization is always needed
   **Truth**: Profile first; don't optimize prematurely

2. **Myth**: Inline functions are always faster
   **Truth**: Inlining can increase code size, hurting cache

## One-Minute Revision

| Technique | Benefit |
|-----------|---------|
| Profiling | Find bottlenecks |
| Cache optimization | Reduce cache misses |
| Loop unrolling | Reduce loop overhead |
| Compiler flags | Enable optimizations |
| Memory pools | Reduce allocation overhead |
| SIMD | Parallel data processing |

## Related Topics

- [Algorithms](../07-algorithms/README.md)
- [Best Practices](../15-best-practices/README.md)
