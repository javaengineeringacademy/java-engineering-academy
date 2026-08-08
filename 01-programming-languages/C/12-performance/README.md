# Performance — C Language

## Why It Matters

When you're building a database processing 10K queries/sec that needs to handle 100K, or a packet classifier handling 200K packets/sec that needs to handle 1M, performance optimization is the difference between a system that works and one that works at scale. C is already fast, but fast is not fast enough — the key is to profile first, identify bottlenecks, and optimize the critical 1% of code that accounts for 99% of execution time, avoiding premature optimization that wastes time and creates complexity.

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Hot paths identified by profiling, latency/throughput-critical code | Don't optimize cold paths |
| When NOT to use | Before profiling — premature optimization wastes time | Correctness first, optimize after |
| Alternatives | Rust (LLVM backend), C++ (same performance, more abstractions) | Same perf potential, different ergonomics |
| Production Examples | Redis (event loop), Linux kernel (BPF), SQLite (query optimizer) | Profile-driven optimization |
| Common Mistakes | Optimizing before profiling, ignoring cache behavior, branch-heavy code | Profile first, use cache-friendly access, branchless |

## What It Is

Performance optimization in C involves:

| Technique | Purpose | Impact |
|-----------|---------|--------|
| Profiling | Find bottlenecks | Know where to optimize |
| Cache optimization | Reduce cache misses | 2-10x speedup |
| Branch optimization | Reduce mispredictions | 2-5x speedup |
| SIMD | Parallel data processing | 4-16x speedup |
| Memory pools | Reduce allocation overhead | 10-100x for allocation-heavy code |
| Compiler flags | Enable optimizations | 1.5-3x speedup |
| Lock-free programming | Reduce synchronization overhead | 2-10x for concurrent code |

## Why It Exists

Performance optimization exists because:
- Hardware is finite; demand grows
- Latency-sensitive applications (trading, gaming, real-time) need every cycle
- Throughput-critical systems (databases, web servers) need maximum efficiency
- Resource-constrained systems (embedded, IoT) need minimal footprint

### Architecture: Performance Hierarchy

```
┌─────────────────────────────────────┐
│         Algorithm Choice            │ ← Biggest impact (O(n²) → O(n log n))
├─────────────────────────────────────┤
│        Data Structure Choice        │ ← Cache behavior, access patterns
├─────────────────────────────────────┤
│     Memory Access Patterns          │ ← Sequential vs random, prefetching
├─────────────────────────────────────┤
│       Branch Prediction             │ ← Branchless code, sorted data
├─────────────────────────────────────┤
│       SIMD / Vectorization          │ ← Parallel data processing
├─────────────────────────────────────┤
│       Compiler Optimizations        │ ← -O2, -march=native, LTO
└─────────────────────────────────────┘
```

## Expanded Code Examples

### Profiling with gprof

```bash
# Step 1: Compile with profiling
gcc -pg -O2 -o program program.c

# Step 2: Run the program
./program

# Step 3: Analyze
gprof program gmon.out > analysis.txt

# Step 4: Read the output
# Look for functions with high "self time" — these are your bottlenecks
```

### Profiling with perf (Linux)

```bash
# Record performance data
perf record -g ./program

# View report
perf report

# Count specific events
perf stat -e cache-misses,branch-misses,instructions,cycles ./program
```

### Cache-Friendly Code

```c
#include <stdio.h>
#include <time.h>

#define N 4096

// BAD: Column-major access (stride = N * sizeof(int))
void column_major(int matrix[N][N], long *sum) {
    *sum = 0;
    for (int j = 0; j < N; j++) {
        for (int i = 0; i < N; i++) {
            *sum += matrix[i][j];  // Jump N*4 bytes each iteration
        }
    }
}

// GOOD: Row-major access (stride = sizeof(int))
void row_major(int matrix[N][N], long *sum) {
    *sum = 0;
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            *sum += matrix[i][j];  // Sequential access
        }
    }
}

// BETTER: Block processing (cache-friendly + parallelizable)
void blocked(int matrix[N][N], long *sum) {
    *sum = 0;
    #define BLOCK 64
    for (int ii = 0; ii < N; ii += BLOCK) {
        for (int jj = 0; jj < N; jj += BLOCK) {
            for (int i = ii; i < ii + BLOCK && i < N; i++) {
                for (int j = jj; j < jj + BLOCK && j < N; j++) {
                    *sum += matrix[i][j];
                }
            }
        }
    }
}
```

### Loop Optimizations

```c
#include <stdio.h>

// Loop unrolling
int sum_unrolled(int *arr, int n) {
    int sum = 0;
    int i = 0;

    // Process 4 elements at a time
    for (; i + 3 < n; i += 4) {
        sum += arr[i];
        sum += arr[i + 1];
        sum += arr[i + 2];
        sum += arr[i + 3];
    }

    // Handle remaining elements
    for (; i < n; i++) {
        sum += arr[i];
    }

    return sum;
}

// Strength reduction: replace multiply with add
void strength_reduction(int *arr, int n) {
    // Bad: multiply in inner loop
    for (int i = 0; i < n; i++) {
        arr[i] = i * 7;
    }

    // Good: add in inner loop
    int val = 0;
    for (int i = 0; i < n; i++) {
        arr[i] = val;
        val += 7;
    }
}

// Eliminate redundant computation
void optimize_computation(int *arr, int n) {
    // Bad: recomputes strlen in each iteration
    for (int i = 0; i < n; i++) {
        arr[i] = i * i;
    }

    // Good: precompute if pattern is complex
    int square = 0;
    int delta = 1;
    for (int i = 0; i < n; i++) {
        arr[i] = square;
        square += delta;
        delta += 2;
    }
}
```

### Memory Pool Allocator

```c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BLOCK_SIZE 4096

typedef struct Block {
    struct Block *next;
    size_t used;
    char data[BLOCK_SIZE];
} Block;

typedef struct {
    Block *blocks;
    size_t total_allocated;
    size_t total_used;
} PoolAllocator;

void pool_init(PoolAllocator *pool) {
    pool->blocks = NULL;
    pool->total_allocated = 0;
    pool->total_used = 0;
}

void *pool_alloc(PoolAllocator *pool, size_t size) {
    // Align to 8 bytes
    size = (size + 7) & ~(size_t)7;

    // Find a block with enough space
    for (Block *b = pool->blocks; b; b = b->next) {
        if (BLOCK_SIZE - b->used >= size) {
            void *ptr = b->data + b->used;
            b->used += size;
            pool->total_used += size;
            return ptr;
        }
    }

    // Allocate new block
    Block *b = malloc(sizeof(Block));
    if (!b) return NULL;
    b->used = size;
    b->next = pool->blocks;
    pool->blocks = b;
    pool->total_allocated += BLOCK_SIZE;
    pool->total_used += size;

    return b->data;
}

void pool_free_all(PoolAllocator *pool) {
    Block *b = pool->blocks;
    while (b) {
        Block *next = b->next;
        free(b);
        b = next;
    }
    pool->blocks = NULL;
    pool->total_allocated = 0;
    pool->total_used = 0;
}
```

### Branchless Programming

```c
#include <stdio.h>

// BAD: Branch (unpredictable)
int abs_branch(int x) {
    return (x < 0) ? -x : x;
}

// GOOD: Branchless
int abs_branchless(int x) {
    int mask = x >> 31;  // All 1s if negative, all 0s if positive
    return (x ^ mask) - mask;
}

// BAD: Conditional branch
int max_branch(int a, int b) {
    return (a > b) ? a : b;
}

// GOOD: Branchless max
int max_branchless(int a, int b) {
    return a ^ ((a ^ b) & -(a < b));
}

// Branchless min/max without comparison
int min(int a, int b) {
    int diff = a - b;
    int mask = diff >> 31;
    return b + (diff & mask);
}
```

## Production Incidents

### Incident 1: Cache Miss Causing 20x Slowdown

**Problem**: Data processing pipeline drops from 10K to 500 records/sec.

**Cause**: Column-major access of large struct array:

```c
for (int i = 0; i < 1000000; i++) {
    for (int j = 0; j < 100; j++) {
        sum += records[i].scores[j];  // Stride = sizeof(Record)
    }
}
```

**Solution**: Restructure for sequential access:

```c
// Transpose data layout
float scores[1000000][100];  // Sequential access
for (int i = 0; i < 1000000; i++) {
    for (int j = 0; j < 100; j++) {
        sum += scores[i][j];
    }
}
```

### Incident 2: Branch Misprediction

**Problem**: Packet classifier processes 200K instead of 1M packets/sec.

**Cause**: Sorting on unpredictable data:

```c
if (p->src_port > 1024) {  // Unpredictable
    if (p->dst_port < 80) { return A; }
    else { return B; }
} else { return C; }
```

**Solution**: Branchless or lookup table:

```c
int classify(Packet *p) {
    return lookup[p->src_port >> 6][p->dst_port >> 6];
}
```

## Production Checklist

- [ ] Profile before optimizing — find the real bottleneck
- [ ] Focus on hot paths (top 1% of code)
- [ ] Design data structures for sequential access
- [ ] Use block processing for large arrays
- [ ] Enable compiler optimizations (`-O2 -march=native`)
- [ ] Use memory pools for high-frequency allocations
- [ ] Use branchless techniques for hot paths
- [ ] Measure before and after each optimization
- [ ] Avoid premature optimization
- [ ] Document performance-critical decisions

## Maturity Levels

| Level | Description | Indicators |
|-------|-------------|------------|
| **Beginner** | Uses compiler flags | `-O2`, `-march=native` |
| **Intermediate** | Optimizes loops and data access | Cache-friendly traversal, loop unrolling |
| **Advanced** | Implements custom allocators | Memory pools, arena allocators |
| **Expert** | Uses SIMD, lock-free programming | Vectorization, atomic operations |

## Common Myths Debunked

1. **Myth**: Optimization is always needed
   **Truth**: Profile first. Most code is not performance-critical. Optimize only what matters.

2. **Myth**: Inline functions are always faster
   **Truth**: Inlining can increase code size, hurting instruction cache. Let the compiler decide with `-O2`.

3. **Myth**: Low-level optimization is always better
   **Truth**: Algorithmic improvements (O(n²) → O(n log n)) dwarf micro-optimizations. Choose the right algorithm first.

## One-Minute Revision

| Technique | Benefit | When to Use |
|-----------|---------|-------------|
| Profiling | Find bottlenecks | Always, before any optimization |
| Cache optimization | Reduce cache misses | Large data processing |
| Loop unrolling | Reduce loop overhead | Tight inner loops |
| Branchless code | Eliminate mispredictions | Unpredictable branches |
| Memory pools | Reduce allocation overhead | Many small allocations |
| SIMD | Parallel data processing | Data-parallel workloads |
| Compiler flags | Enable optimizations | Always (`-O2` minimum) |

## Related Topics

- [Algorithms](../07-algorithms/README.md) — Algorithmic complexity
- [Memory Management](../08-memory-management/README.md) — Custom allocators
- [Concurrency](../09-concurrency/README.md) — Parallelism
- [Best Practices](../15-best-practices/README.md) — Balancing performance with readability

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Cache misses causing slowdown | `perf stat -e cache-misses,cache-references` | Profile cache miss rate; restructure data for sequential access (row-major instead of column-major) |
| Branch misprediction reducing throughput | `perf stat -e branch-misses` | Identify hot branches; replace with branchless code or sort data to make branches predictable |
| Excessive memory allocation overhead | Memory pool + profiling | Replace `malloc`/`free` with pool allocator for hot paths; measure allocation count before/after |
| False sharing in multi-threaded code | `perf c2c` + padding | Detect cache line sharing between threads; pad data structures to separate thread-local data onto different cache lines |
| Compiler not optimizing hot loop | `gcc -S -O2` assembly inspection | Inspect generated assembly; add `__builtin_expect` for branch hints; use `restrict` pointers |

## Code Review Checklist

- [ ] Profile before optimizing — identify real bottleneck with `perf` or `gprof`
- [ ] Hot paths (top 1% of code) optimized first
- [ ] Data structures designed for sequential access (row-major layout)
- [ ] Block processing used for large array operations (cache-friendly)
- [ ] Branchless techniques applied to unpredictable hot-path branches
- [ ] Memory pools used for high-frequency small allocations
- [ ] Compiler optimizations enabled (`-O2 -march=native`)
- [ ] Performance measured before and after each optimization

## Architecture Considerations

Performance optimization follows a hierarchy: algorithm choice (biggest impact), data structure choice (cache behavior), memory access patterns (sequential vs random), branch prediction (branchless code), SIMD (parallel data processing), and compiler optimizations (last resort). Profile first, identify the bottleneck, and optimize the critical path. Premature optimization wastes time and creates complexity.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Cache-oblivious algorithms | Unknown cache sizes, portable performance | Optimal for all cache levels but complex to implement |
| SIMD vectorization | Data-parallel workloads (image processing, crypto) | 4-16x speedup but platform-specific and harder to debug |
| Memory pool | High-frequency small allocations | Reduces `malloc` overhead; wastes memory if pool size is misjudged |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Constant-time operations for cryptographic code | Timing side-channel attacks | Use constant-time comparisons and operations; avoid data-dependent branches |
| Optimized-out security checks | Compiler removes "unnecessary" validation | Use `volatile` for security-critical checks; verify with `-O2` assembly inspection |
| Performance optimizations bypassing safety | Bounds checks removed for speed | Keep safety checks in debug builds; use `-D_FORTIFY_SOURCE=2` in release |

## Evolution & Modernization

| Era | Change | Migration Path |
|-----|--------|----------------|
| C89 → C99 | Added `restrict` qualifier (pointer aliasing), `inline` | Add `restrict` to hot-path pointer parameters; use `inline` for small functions |
| C99 → C11 | Added `_Alignas`, `_Alignof` for cache-line alignment | Use `_Alignas(64)` for cache-line aligned structures in hot paths |
| C11 → C23 | Added `typeof`, improved `constexpr`, `#embed` | Use `typeof` for type-generic performance macros; use `constexpr` for compile-time constants |

## Version Validation

| Feature | C Standard | Status |
|---------|-----------|--------|
| `restrict` qualifier (aliasing optimization) | C99 | Standard — add to hot-path pointer parameters |
| `_Alignas` / `_Alignof` (alignment control) | C11 | Standard — use for cache-line alignment |
| `_Noreturn` (noreturn functions) | C11 | Standard — use for `abort()`, `exit()` to enable optimizer |
| `typeof` for type-generic operations | C23 (standardized) | Use for type-safe performance macros |

## Interview Questions

1. **What is the performance hierarchy and why does order matter?**: The hierarchy is: algorithm choice → data structure → memory access patterns → branch prediction → SIMD → compiler optimizations. Order matters because algorithmic improvements (O(n²) → O(n log n)) dwarf micro-optimizations. Always choose the right algorithm first.
2. **How does cache behavior affect performance?**: CPU cache hits (~1ns) are 100x faster than main memory (~100ns). Sequential access (arrays) exploits spatial locality; random access (linked lists) causes cache misses. Block processing and row-major layout improve cache utilization.
3. **When should you use branchless programming?**: Use branchless code for unpredictable branches in hot paths (e.g., `abs()`, `max()`, conditional assignments). Branch misprediction costs ~15-20 CPU cycles. Branchless code eliminates this penalty at the cost of reduced readability.
4. **What is false sharing and how do you fix it?**: False sharing occurs when two threads modify variables on the same cache line, causing the cache line to bounce between CPU cores. Fix by padding data structures to separate thread-local data onto different cache lines (e.g., `_Alignas(64)`).
5. **How do you measure performance accurately?**: Use `perf stat` for hardware counters (cache misses, branch misses, cycles), `perf record` + `perf report` for function-level profiling, and `gprof` for call-graph profiling. Always run multiple iterations and warm up the CPU cache before measuring.

## References

- [C Standard (N3220)](https://www.open-std.org/jtc1/sc22/wg14/www/docs/n3220.pdf)
- [Computer Architecture: A Quantitative Approach (Hennessy & Patterson)](https://www.elsevier.com/books/computer-architecture/hennessy/978-0-128-20109-1)
- [Linux perf性能分析工具](https://perf.wiki.kernel.org/index.php/Main_Page)
