# Performance — C++

## Why It Matters

Performance is not about making code fast — it's about making code efficient. When you understand how hardware actually executes your code, you transform from someone who guesses at optimizations into someone who measures, verifies, and delivers real-world speedups. Every nanosecond you save at scale compounds into millions of dollars and happier users.

## What It Is

Performance optimization in C++ involves profiling, cache-friendly data layouts, SIMD vectorization, branchless programming, memory pools, and lock-free data structures to make code work with hardware features instead of against them.

## Engineering Decision Framework

| Decision | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Optimize hot path | Profiling + targeted changes | After measurement shows a bottleneck | Before profiling (premature optimization) |
| Memory layout | Struct-of-Arrays (SoA) vs Array-of-Structs (AoS) | When processing one field across many elements | When objects are accessed as units |
| Allocations | Custom allocator / memory pool | High-frequency small allocations in hot path | Rare allocations or large blocks |
| Parallelism | Thread pool / task system | CPU-bound work that scales with cores | I/O-bound work (use async instead) |
| SIMD | intrinsics or auto-vectorization | Processing arrays of uniform data | Scalar logic with branches |
| Branching | Branchless / predication | Tight loops with unpredictable branches | Simple linear code |
| Caching | Precompute / memoize | Expensive repeated calculations | One-shot computations |

## Expanded Code Examples

### Profiling with perf and Valgrind

```bash
# Compile with debug symbols and frame pointers
g++ -O2 -g -fno-omit-frame-pointer -o program program.cpp

# Linux perf: CPU cycles and cache misses
perf stat ./program
perf record -g ./program
perf report

# Valgrind Callgrind: call-graph profiling
valgrind --tool=callgrind ./program
callgrind_annotate callgrind.out.12345

# Google Benchmark for microbenchmarks
#include <benchmark/benchmark.h>

static void BM_VectorPushBack(benchmark::State& state) {
    for (auto _ : state) {
        std::vector<int> v;
        for (int i = 0; i < state.range(0); ++i) {
            v.push_back(i);
        }
        benchmark::DoNotOptimize(v);
    }
}
BENCHMARK(BM_VectorPushBack)->Range(8, 1 << 20);
BENCHMARK_MAIN();
```

### Cache-Friendly Data Layout

```cpp
#include <vector>
#include <chrono>
#include <iostream>

// Bad: Array of Structures — poor cache locality for hot-field access
struct ParticleAoS {
    float x, y, z;        // position (hot)
    float vx, vy, vz;     // velocity (hot)
    float mass;            // cold
    float color[4];        // cold
    char name[32];         // cold
};

// Good: Structure of Arrays — contiguous hot fields
struct ParticleArrays {
    std::vector<float> x, y, z;
    std::vector<float> vx, vy, vz;
    std::vector<float> mass;
};

void update_positions_aos(std::vector<ParticleAoS>& particles, float dt) {
    for (auto& p : particles) {
        p.x += p.vx * dt;
        p.y += p.vy * dt;
        p.z += p.vz * dt;
    }
}

void update_positions_soa(ParticleArrays& particles, float dt) {
    const size_t n = particles.x.size();
    for (size_t i = 0; i < n; ++i) {
        particles.x[i] += particles.vx[i] * dt;
        particles.y[i] += particles.vy[i] * dt;
        particles.z[i] += particles.vz[i] * dt;
    }
}
```

### SIMD Vectorization

```cpp
#include <immintrin.h>
#include <vector>

// Scalar version
void add_scalar(const float* a, const float* b, float* c, int n) {
    for (int i = 0; i < n; ++i) {
        c[i] = a[i] + b[i];
    }
}

// AVX2 SIMD version — processes 8 floats simultaneously
void add_avx2(const float* a, const float* b, float* c, int n) {
    int i = 0;
    for (; i + 8 <= n; i += 8) {
        __m256 va = _mm256_loadu_ps(&a[i]);
        __m256 vb = _mm256_loadu_ps(&b[i]);
        __m256 vc = _mm256_add_ps(va, vb);
        _mm256_storeu_ps(&c[i], vc);
    }
    // Handle remainder
    for (; i < n; ++i) {
        c[i] = a[i] + b[i];
    }
}

// Compiler auto-vectorization hint
void add_auto(const float* a, const float* b, float* c, int n) {
    #pragma GCC ivdep  // Assume no vector dependencies
    for (int i = 0; i < n; ++i) {
        c[i] = a[i] + b[i];
    }
}
```

### Memory Pool Allocator

```cpp
#include <cstddef>
#include <vector>
#include <cassert>

template <typename T, size_t BlockSize = 4096>
class PoolAllocator {
    struct Block {
        alignas(T) char data[BlockSize];
        size_t offset = 0;
    };
    std::vector<Block> blocks_;

    Block& current_block() {
        if (blocks_.empty() || blocks_.back().offset + sizeof(T) > BlockSize) {
            blocks_.emplace_back();
        }
        return blocks_.back();
    }

public:
    T* allocate() {
        Block& b = current_block();
        if (b.offset + sizeof(T) > BlockSize) {
            blocks_.emplace_back();
            b = blocks_.back();
        }
        T* ptr = reinterpret_cast<T*>(b.data + b.offset);
        b.offset += sizeof(T);
        return ptr;
    }

    void deallocate(T*) {
        // No-op: pool resets all at once
    }

    void reset() {
        blocks_.clear();
    }
};

// Usage: allocate 10K objects without individual new/delete
PoolAllocator<int> pool;
std::vector<int*> ptrs;
for (int i = 0; i < 10000; ++i) {
    ptrs.push_back(pool.allocate());
}
pool.reset();  // Free all at once
```

### Branchless Programming

```cpp
#include <algorithm>

// Branchy version — pipeline stalls on unpredictable branches
int abs_branchy(int x) {
    if (x < 0) return -x;
    return x;
}

// Branchless version — no pipeline stalls
int abs_branchless(int x) {
    int mask = x >> 31;  // All 1s if negative, all 0s if positive
    return (x ^ mask) - mask;
}

// Branchless max — avoids branch misprediction
int max_branchless(int a, int b) {
    int diff = a - b;
    int mask = diff >> 31;
    return b + (diff & ~mask);
}

// Conditional move pattern
int clamp_branchless(int x, int lo, int hi) {
    x = x < lo ? lo : x;
    x = x > hi ? hi : x;
    return x;
}
```

### Lock-Free Atomic Operations

```cpp
#include <atomic>
#include <thread>
#include <vector>
#include <iostream>

// Spinlock using std::atomic_flag
class SpinLock {
    std::atomic_flag flag_ = ATOMIC_FLAG_INIT;
public:
    void lock() {
        while (flag_.test_and_set(std::memory_order_acquire)) {
            // Spin — could add PAUSE instruction here
        }
    }
    void unlock() {
        flag_.clear(std::memory_order_release);
    }
};

// Atomic counter — no locks needed
class Counter {
    std::atomic<long long> count_{0};
public:
    void increment() { count_.fetch_add(1, std::memory_order_relaxed); }
    long long get() const { return count_.load(std::memory_order_relaxed); }
};

// Usage
void parallel_increment() {
    Counter counter;
    std::vector<std::thread> threads;
    for (int i = 0; i < 8; ++i) {
        threads.emplace_back([&counter] {
            for (int j = 0; j < 100000; ++j) {
                counter.increment();
            }
        });
    }
    for (auto& t : threads) t.join();
    std::cout << "Count: " << counter.get() << "\n";  // 800000
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

### Incident 2: False Sharing in Multithreaded Code
**Problem**: A parallel sort implementation showed no speedup beyond 2 threads despite having 8 cores available, with CPU utilization stuck at 25%.

**Cause**: Two threads wrote to adjacent elements of a shared `std::atomic<int>` counter array. The counters occupied the same 64-byte cache line. Each write invalidated the other thread's cached copy, causing the cache line to ping-pong between cores — false sharing.

**Impact**: Parallel sort was 2x slower than single-threaded sort due to cache coherency overhead. The team had budgeted for 8x speedup. The feature shipped with reduced parallelism, limiting throughput.

**Detection**: `perf c2c` (cache-to-cache) analysis identified the false sharing. Intel VTune's "Top Hotspots" showed `memory_order_relaxed` store instructions with high "Cycles Lost" counts on adjacent memory addresses.

**Solution**: Added `alignas(64)` to the counter struct to ensure each thread's counter occupies its own cache line. Used `std::atomic<int>` with `std::memory_order_relaxed` to avoid unnecessary memory barriers. Padding each counter to 64 bytes eliminated the coherency traffic.

**Prevention**: Use `alignas(std::hardware_destructive_interference_size)` (C++17, typically 64 bytes) on shared atomic variables accessed by different threads. Profile with `perf c2c` in CI for multithreaded workloads. Rule — never place two `std::atomic` variables adjacent without padding.

### Incident 3: Memory Fragmentation Causing OOM
**Problem**: A game server crashed with OOM after 6 hours of operation despite having 32GB RAM, even though heap analysis showed only 8GB allocated.

**Cause**: The server allocated and freed variable-sized packets (64B–4KB) millions of times per minute. The system allocator (`malloc`/`free`) fragmented the heap over time, creating thousands of tiny free blocks that couldn't be coalesced. Virtual memory was exhausted even though physical memory was available.

**Impact**: Server crashed every 6 hours. Auto-restart kept the service alive but caused 30-second disconnections for all connected players. ~200K affected users per crash.

**Detection**: `/proc/meminfo` showed high `VmallocUsed` but low `MemUsed`. `jemalloc` heap profiling revealed 90% of virtual address space was fragmented into 4KB–16KB chunks.

**Solution**: Replaced `malloc`/`free` with a slab allocator for packet objects. Packets are now allocated from size-class pools (64B, 128B, 256B, 512B, 1KB, 2KB, 4KB). Each pool uses contiguous mmap'd regions with free-list management. No external fragmentation.

**Prevention**: Profile memory fragmentation in long-running processes. Use specialized allocators (jemalloc, tcmalloc, or custom pools) for high-frequency allocation patterns. Monitor `VmallocUsed` vs `MemUsed` in production metrics.

## Production Checklist

- [ ] Profile before optimizing — never guess
- [ ] Use `-O2` or `-O3` compiler optimizations
- [ ] Enable LTO (Link-Time Optimization) for release builds
- [ ] Prefer stack allocation over heap for small, short-lived objects
- [ ] Use `reserve()` for vectors when size is known
- [ ] Avoid `std::endl` — use `'\n'` to prevent unnecessary flushes
- [ ] Use `constexpr` for compile-time computation
- [ ] Align hot data to cache lines (`alignas(64)`)
- [ ] Pad atomic variables to prevent false sharing
- [ ] Use custom allocators for high-frequency allocation patterns
- [ ] Benchmark before and after every optimization
- [ ] Profile with `perf`, VTune, or Callgrind in CI

## Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Beginner** | Compiler optimizations, basic profiling, `reserve()` for vectors |
| **Intermediate** | Cache-friendly layouts, branchless code, memory pools, false sharing avoidance |
| **Advanced** | SIMD intrinsics, lock-free data structures, custom allocators, profile-guided optimization |

## Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Premature optimization is the root of all evil" | Knuth said "premature" — not "no optimization." Profile first, optimize based on data. |
| "Faster code is always better" | Readability and maintainability matter. Optimize only where measurement shows a bottleneck. |
| "Optimization is only for experts" | Anyone can profile with `perf` and fix the top hotspot. It's a learnable skill. |
| "`-O3` is always the best optimization level" | `-O3` can increase code size and hurt cache performance. Profile both `-O2` and `-O3`. |
| "Inlining everything makes code faster" | Over-inlining increases instruction cache pressure. Let the compiler decide with LTO. |
| "Manual SIMD is always faster than auto-vectorization" | Modern compilers vectorize better than most hand-written SIMD. Profile first. |

## One-Minute Revision Table

| Concept | Description | Tool/Technique |
|---------|-------------|----------------|
| Profiling | Measure where time is spent | `perf`, Valgrind, VTune, Google Benchmark |
| Cache locality | Keep related data contiguous | SoA layout, `alignas(64)`, prefetch |
| SIMD | Process multiple data points per instruction | AVX2 intrinsics, auto-vectorization |
| Branchless | Eliminate branch misprediction | Bit manipulation, conditional moves |
| Memory pool | Reduce allocation overhead | Slab allocator, arena allocator |
| False sharing | Cache line contention between threads | `alignas(64)` padding on atomics |
| Lock-free | Avoid mutex overhead | `std::atomic`, CAS operations |
| LTO | Cross-module optimization | `-flto` compiler flag |
| PGO | Profile-guided optimization | `-fprofile-generate` / `-fprofile-use` |

## Cross-Linked Related Topics

- **Memory Management** → [Module 05: Memory](../05-memory-management/) — Heap vs stack, allocation strategies
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Parallelism, false sharing, lock-free patterns
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — `constexpr`, move semantics, `string_view`
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Compiler flags, LTO, PGO setup
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Performance as a best practice
- **Senior Level** → [Module 15: Senior](../15-senior/) — Performance architecture decisions

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Cache miss causing unexpected slowdown | `perf stat` (L1-dcache-load-misses) | Run `perf stat -e L1-dcache-load-misses ./program`; identify hot loops with high miss rates |
| False sharing between threads | `perf c2c` analysis | Run `perf c2c record ./program`; identify cache lines with high "Cycles Lost" on adjacent atomics |
| Memory fragmentation causing OOM | `jemalloc` heap profiler + `/proc/meminfo` | Profile with jemalloc; compare `VmallocUsed` vs `MemUsed` to detect fragmentation |
| SIMD auto-vectorization not triggering | Compiler report `-fopt-info-vec` | Compile with `-fopt-info-vec`; check if loops are vectorized; add `#pragma GCC ivdep` |
| Branch misprediction in tight loop | `perf stat` (branch-misses) | Profile branch miss rate; convert unpredictable branches to branchless bit manipulation |

## Code Review Checklist

- [ ] Profiling done before any optimization (never guess)
- [ ] `-O2` or `-O3` compiler optimizations enabled for release builds
- [ ] `reserve()` called for vectors when size is known
- [ ] Hot data aligned to cache lines (`alignas(64)`)
- [ ] Atomic variables padded to prevent false sharing
- [ ] Custom allocators used for high-frequency allocation patterns
- [ ] `std::endl` replaced with `'\n'` to prevent unnecessary flushes
- [ ] LTO (Link-Time Optimization) enabled for release builds

## Architecture Considerations

Performance optimization requires understanding the hardware architecture. CPU caches (L1/L2/L3) dominate performance — data layout determines cache hit rates. Memory allocation patterns affect fragmentation and throughput. Branch prediction determines pipeline efficiency. SIMD enables processing multiple data points per instruction. Lock-free data structures avoid mutex overhead. Architecture decisions must balance performance with maintainability.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Structure of Arrays (SoA) | Processing one field across many elements | Cache-friendly for hot fields vs. scattered object access |
| Arena allocator | Frame-based or request-based bulk allocation | O(1) allocation vs. no individual object freeing |
| Branchless programming | Tight loops with unpredictable branches | Eliminates misprediction vs. reduced readability |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Performance optimization bypassing security checks | Vulnerabilities introduced for speed | Never optimize away bounds checks, input validation, or authentication |
| Lock-free data structures introducing ABA problem | Memory corruption, data races | Use hazard pointers or epoch-based reclamation for complex lock-free structures |
| Custom allocator memory leak | Resource exhaustion, DoS | Monitor allocator usage; add leak detection in long-running processes |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| C++11 | `std::atomic` for lock-free operations | Replace manual volatile + memory barriers with `std::atomic` |
| C++17 | `std::hardware_destructive_interference_size` for cache line alignment | Use `alignas(std::hardware_destructive_interference_size)` for shared atomics |
| C++20 | `std::jthread` for automatic thread cleanup | Replace `std::thread` with `std::jthread` for RAII thread management |

## Version Validation

| Feature | C++ Version | Status |
|---------|------------|--------|
| `std::atomic` with memory ordering | C++11 | Widely supported |
| `alignas` for cache line alignment | C++11 | Widely supported |
| `std::hardware_destructive_interference_size` | C++17 | Supported in GCC 7+, Clang 5+, MSVC 19.11+ |
| `std::jthread` / `std::stop_token` | C++20 | Supported in GCC 10+, Clang 14+, MSVC 19.28+ |

## Interview Questions

1. **What is cache-friendly data layout and why does it matter?**: Cache-friendly layout keeps frequently accessed data contiguous in memory (SoA, `alignas(64)`). CPUs fetch data in cache lines (64 bytes); if hot data is scattered, every access causes a cache miss (~100 cycles vs ~1 cycle for L1 hit). This can make 10x difference in throughput.
2. **Explain false sharing and how to fix it**: False sharing occurs when threads write to different variables on the same cache line, causing the line to invalidate and reload on every write. Fix with `alignas(64)` padding to ensure each thread's data occupies its own cache line.
3. **When should you use a memory pool allocator?**: Use pools when you allocate/deallocate many objects of the same size frequently (e.g., packet processing, game entities). Pools eliminate fragmentation, reduce allocator overhead, and enable bulk deallocation.
4. **What is branchless programming and when is it useful?**: Branchless programming replaces conditional branches with arithmetic/bit operations (e.g., `mask = x >> 31`). Useful in tight loops with unpredictable branches where branch misprediction causes pipeline stalls (~15 cycles each).
5. **How do you profile C++ performance effectively?**: Use `perf stat` for hardware counters (cache misses, branch misses), `perf record` + `perf report` for hotspot identification, Google Benchmark for microbenchmarks, and Valgrind Callgrind for call-graph profiling. Always measure before and after optimization.

## References
- [Computer Systems: A Programmer's Perspective — Bryant & O'Hallaron](https://www.amazon.com/Computer-Systems-Programmers-Perspective-2nd/dp=013409266X)
- [CppCon Talk: Optimizing C++ — Benchmarking](https://youtube.com/cppcon)
- [Agner Fog — Optimization Manuals](https://www.agner.org/optimize/)
- [Google Benchmark Library](https://github.com/google/benchmark)
