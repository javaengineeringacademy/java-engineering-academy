# Performance

## What it is
Optimizing code for speed, memory usage, and efficiency.

## Why it exists
To make applications faster, use fewer resources, and scale better.

## When to use it
When performance is critical or when profiling reveals bottlenecks.

## How it works

### Profiling
```bash
# Using gprof
g++ -pg -o program program.cpp
./program
gprof program gmon.out > analysis.txt

# Using Valgrind
valgrind --tool=callgrind ./program
```

### Cache Optimization
```cpp
// Bad: Cache-unfriendly
for (int j = 0; j < N; j++) {
    for (int i = 0; i < N; i++) {
        matrix[i][j] = 0;
    }
}

// Good: Cache-friendly
for (int i = 0; i < N; i++) {
    for (int j = 0; j < N; j++) {
        matrix[i][j] = 0;
    }
}
```

### SIMD Optimization
```cpp
#include <immintrin.h>

void add_arrays(float* a, float* b, float* c, int n) {
    for (int i = 0; i < n; i += 8) {
        __m256 va = _mm256_loadu_ps(&a[i]);
        __m256 vb = _mm256_loadu_ps(&b[i]);
        __m256 vc = _mm256_add_ps(va, vb);
        _mm256_storeu_ps(&c[i], vc);
    }
}
```

## Production Incidents

### Incident 1: Cache Miss Causing Slowdown
**Problem**: A physics simulation engine that processed 100K particles dropped from 60 FPS to 8 FPS after a seemingly innocuous refactor that changed a data layout.

**Cause**: Particle data was reorganized from an array-of-structures (`struct Particle { vec3 pos; vec3 vel; float mass; ... }`) to a structure-of-arrays layout. However, the hot loop only accessed `pos` and `vel`, and the new layout scattered these across separate memory regions. The CPU's L1 cache couldn't hold the working set, causing constant cache misses.

**Impact**: Frame rate dropped 87%. Physics simulation became visibly stuttery. The release candidate was delayed by 1 week while the team investigated. Profiling consumed 40 engineering hours.

**Detection**: `perf stat` showed L1-dcache-load-misses jumping from 2% to 34%. Intel VTune confirmed the hot loop spent 60% of cycles waiting for memory. Hardware performance counters revealed the cache miss pattern.

**Solution**: Restructured the hot-path data to keep `pos` and `vel` contiguous (`struct ParticleCore { vec3 pos; vec3 vel; }`). Separated rarely-accessed fields (mass, color, collision radius) into a secondary array indexed by particle ID. Used `__builtin_prefetch` for the next particle in the loop.

**Prevention**: Profile with `perf` after every data structure layout change. Use `alignas(64)` and structure padding to keep hot fields in the same cache line. Adopt an ECS (Entity Component System) pattern where components accessed together are stored together.

---

### Incident 2: False Sharing in Multithreaded Code
**Problem**: A parallel sort implementation showed no speedup beyond 2 threads despite having 8 cores available, with CPU utilization stuck at 25%.

**Cause**: Two threads wrote to adjacent elements of a shared `std::atomic<int>` counter array. The counters occupied the same 64-byte cache line. Each write invalidated the other thread's cached copy, causing the cache line to ping-pong between cores — false sharing.

**Impact**: Parallel sort was 2x slower than single-threaded sort due to cache coherency overhead. The team had budgeted for 8x speedup. The feature shipped with reduced parallelism, limiting throughput.

**Detection**: `perf c2c` (cache-to-cache) analysis identified the false sharing. Intel VTune's "Top Hotspots" showed `memory_order_relaxed` store instructions with high "Cycles Lost" counts on adjacent memory addresses.

**Solution**: Added `alignas(64)` to the counter struct to ensure each thread's counter occupies its own cache line. Used `std::atomic<int>` with `std::memory_order_relaxed` to avoid unnecessary memory barriers. Padding each counter to 64 bytes eliminated the coherency traffic.

**Prevention**: Use `alignas(std::hardware_destructive_interference_size)` (C++17, typically 64 bytes) on shared atomic variables accessed by different threads. Profile with `perf c2c` in CI for multithreaded workloads. Rule — never place two `std::atomic` variables adjacent without padding.

---

## Production Checklist
- [ ] Profile before optimizing
- [ ] Use efficient algorithms and data structures
- [ ] Minimize memory allocations
- [ ] Optimize hot paths
- [ ] Use compiler optimizations
- [ ] Benchmark changes

## Maturity Levels
- **Beginner**: Basic profiling, algorithm optimization
- **Intermediate**: Cache optimization, memory pooling
- **Advanced**: SIMD, lock-free programming, custom allocators

## Common Myths
- ❌ "Premature optimization is the root of all evil"
- ❌ "Faster code is always better"
- ❌ "Optimization is only for experts"

## One-Minute Revision
| Concept | Description |
|---------|-------------|
| Profiling | Measuring performance |
| Cache | Fast memory access |
| SIMD | Vectorized operations |
| Memory Pool | Reduced allocations |
| Branch Prediction | CPU optimization |

## Related Topics
- [Memory Management](../05-memory-management/)
- [Concurrency](../07-concurrency/)
- [Best Practices](../14-best-practices/)