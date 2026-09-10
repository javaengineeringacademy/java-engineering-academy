# Performance — C++

## 1. Overview

C++ performance optimization is the disciplined practice of making code execute faster and use fewer resources by working with — not against — the underlying hardware. Unlike higher-level languages where the VM or interpreter abstracts hardware details, C++ gives you direct access to cache hierarchies, SIMD units, branch predictors, and memory alignment. This module covers the techniques that separate fast code from truly optimized code: profiling-driven development, cache-friendly data layouts, SIMD vectorization, branchless programming, memory pool allocation, and lock-free concurrency. The goal is not micro-optimization — it's understanding where your bottleneck actually is and applying the right technique to eliminate it.

## Why It Matters

Performance is not about making code fast — it's about making code efficient. When you understand how hardware actually executes your code, you transform from someone who guesses at optimizations into someone who measures, verifies, and delivers real-world speedups. Every nanosecond you save at scale compounds into millions of dollars and happier users.

## 2. Learning Objectives

- Profile C++ code using `perf`, Valgrind Callgrind, Intel VTune, and Google Benchmark to identify real bottlenecks
- Design cache-friendly data structures using SoA (Structure of Arrays) layouts and cache-line alignment
- Write SIMD-vectorized code using AVX2 intrinsics and compiler auto-vectorization hints
- Eliminate branch misprediction with branchless programming techniques
- Reduce allocation overhead with memory pool allocators and arena allocation
- Avoid false sharing in multithreaded code using cache-line-padded atomics
- Apply profile-guided optimization (PGO) and link-time optimization (LTO) in release builds
- Recognize when optimization is necessary and when it's premature

## 3. Prerequisites

Before tackling this module, you should be comfortable with:
- **Module 05: Memory Management** (`../05-memory-management/`) — heap vs stack allocation, `new`/`delete`, smart pointers, RAII
- **Module 07: Concurrency** (`../07-concurrency/`) — `std::thread`, `std::mutex`, `std::atomic`, memory ordering
- Basic familiarity with compiler flags (`-O2`, `-O3`, `-g`) and how to compile C++ from the command line
- Understanding of CPU architecture basics: registers, cache, pipeline

## 4. History

Donald Knuth wrote in 1974: *"We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil. Yet we should not pass up our opportunities in that critical 3%."* This quote is often misused to argue against optimization — Knuth's actual point was to optimize *after* measuring, not to skip optimization entirely.

Performance optimization in C++ has evolved alongside hardware:
- **1970s–1980s**: Manual register allocation, hand-tuned assembly, loop unrolling by hand. Programmers thought in terms of instruction counts.
- **1990s**: Compiler optimizations matured (`-O2`, `-O3`). Profiling tools like `gprof` emerged. The focus shifted from instruction counting to understanding memory access patterns.
- **2000s**: The "memory wall" became apparent — CPU speeds outpaced memory speeds by 10x. Cache-conscious programming became essential. Tools like Valgrind Massif and Intel VTune appeared.
- **2010s**: Multi-core dominance. False sharing, lock-free programming, and SIMD (SSE/AVX) became mainstream. Google Benchmark popularized microbenchmarking. `perf` (Linux) became the standard profiling tool.
- **2020s**: Profile-guided optimization (PGO) and link-time optimization (LTO) adopted widely. Hardware prefetchers improved but cache-friendly design still matters. Auto-vectorization by modern compilers (GCC 12+, Clang 15+) rivals hand-written SIMD in many cases.

## 5. Production Notes

- **Always profile before optimizing.** Use `perf stat` for a quick overview, then `perf record` + `perf report` to identify hotspots. Never optimize code you haven't measured.
- **Optimization flags matter.** `-O2` is safe for most code; `-O3` enables aggressive inlining and vectorization but can increase code size. `-Os` optimizes for size (smaller instruction cache footprint).
- **Profile-guided optimization (PGO)** uses runtime data to guide compiler decisions. Compile with `-fprofile-generate`, run representative workloads, then recompile with `-fprofile-use`. Typical improvement: 10–20%.
- **Link-Time Optimization (LTO)** enables cross-module inlining and dead code elimination. Enable with `-flto` for release builds.
- **Benchmark on production-like hardware.** Desktop performance characteristics differ from servers. Cache sizes, memory bandwidth, and NUMA topology all affect results.
- **One optimization at a time.** Change one variable, measure, then decide. Chaining optimizations without measurement makes it impossible to know which change helped.

## 6. Core Concepts

| Concept | Description | Why It Matters |
|---------|-------------|----------------|
| **Cache hierarchy** | L1 (32KB, ~1 cycle), L2 (256KB, ~4 cycles), L3 (8–32MB, ~10 cycles), Main memory (~100 cycles) | Every cache miss costs 100x more than a cache hit. Data layout determines miss rate. |
| **Branch prediction** | CPU speculatively executes the most likely branch path; misprediction costs ~15 cycles | Unpredictable branches stall the pipeline. Branchless code avoids this. |
| **SIMD** | Single Instruction, Multiple Data — process 4/8/16 values per instruction | AVX2 processes 8 floats simultaneously. 8x throughput for data-parallel loops. |
| **False sharing** | Different threads writing to variables on the same cache line causes coherency traffic | Can reduce parallel performance below single-threaded levels. |
| **Memory alignment** | Data aligned to cache line boundaries (64 bytes) enables efficient prefetching | Misaligned data may span cache lines, doubling the cost of each access. |
| **Data layout** | SoA (Structure of Arrays) vs AoS (Array of Structures) | SoA keeps hot fields contiguous for vectorization and cache efficiency. |

## 7. Internal Working

### CPU Cache

Modern CPUs have a multi-level cache hierarchy. L1 is split into L1i (instruction) and L1d (data), typically 32KB each with ~1 cycle latency. L2 is unified, ~256KB–1MB, ~4 cycles. L3 is shared across cores, 8–32MB, ~10 cycles. Main memory (DRAM) is ~100 cycles away.

The CPU fetches data in **cache lines** — typically 64 bytes. When you access a single `float` (4 bytes), the entire 64-byte line containing it is loaded. This means sequential array access is ~16x more efficient than random access (you get 16 useful floats per cache line instead of 1).

**Prefetching**: Hardware prefetchers detect sequential and strided access patterns and load data before the CPU requests it. Accessing data randomly defeats prefetchers, causing cache misses. `__builtin_prefetch(&data[i + 16])` gives a software hint.

### Branch Prediction

The CPU pipeline is typically 15–20 stages deep. When a branch (`if`/`else`) is encountered, the CPU must predict which path to take to keep the pipeline full. Modern predictors are ~95–97% accurate, but unpredictable branches (e.g., random data-dependent conditions) cause pipeline flushes costing ~15 cycles each.

Branchless programming replaces conditional branches with arithmetic: `result = (condition * value_a) + (!condition * value_b)`. On modern CPUs, the compiler often converts simple ternary operators to `cmov` instructions (conditional moves) which don't stall the pipeline.

### Memory Alignment

`alignas(64)` ensures a struct starts at a 64-byte boundary (the cache line size). This is critical for:
1. **Atomic variables**: If two atomics share a cache line, writes from different cores cause false sharing.
2. **SIMD loads**: `_mm256_load_ps` requires 32-byte alignment. `_mm256_loadu_ps` works on unaligned data but may be slower.
3. **DMA and I/O**: Hardware devices often require page-aligned (4KB) buffers.

## 8. Syntax

### Compiler Flags
```bash
# Standard optimization
g++ -O2 -o program program.cpp

# Aggressive optimization with PGO
g++ -O3 -fprofile-generate -o program program.cpp
./program  # Run representative workload
g++ -O3 -fprofile-use -o program program.cpp

# Link-Time Optimization
g++ -O3 -flto -o program program.cpp

# Auto-vectorization report
g++ -O3 -fopt-info-vec -o program program.cpp
```

### Alignment
```cpp
struct alignas(64) CacheAligned {
    std::atomic<int> counter;
    char padding[60];  // Fill rest of cache line
};

// C++17 portable cache line size
struct alignas(std::hardware_destructive_interference_size) PaddedAtomic {
    std::atomic<int> value;
};
```

### SIMD Intrinsics
```cpp
#include <immintrin.h>

__m256 va = _mm256_loadu_ps(&a[i]);   // Load 8 floats
__m256 vb = _mm256_loadu_ps(&b[i]);
__m256 vc = _mm256_add_ps(va, vb);    // Add 8 pairs
_mm256_storeu_ps(&c[i], vc);          // Store 8 results
```

### Google Benchmark
```cpp
#include <benchmark/benchmark.h>

static void BM_Sort(benchmark::State& state) {
    for (auto _ : state) {
        std::vector<int> v(state.range(0));
        std::iota(v.begin(), v.end(), 0);
        std::sort(v.begin(), v.end());
        benchmark::DoNotOptimize(v);
    }
}
BENCHMARK(BM_Sort)->Range(8, 1 << 20);
```

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
# include <benchmark/benchmark.h>

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

## 9. Performance Considerations

### Cache Hierarchy

| Level | Size | Latency | Implication |
|-------|------|---------|-------------|
| L1d | 32KB | ~1 cycle | Hot data must fit here for peak performance |
| L2 | 256KB–1MB | ~4 cycles | Working set that doesn't fit in L1 |
| L3 | 8–32MB | ~10 cycles | Shared across cores; important for multithreaded |
| DRAM | GBs | ~100 cycles | Cache miss = 100x slowdown |

**Rule of thumb**: If your hot loop's working set exceeds L1 size (32KB), you're memory-bound. Restructure data to keep hot fields contiguous. A `std::vector<ParticleAoS>` with 10K particles has a working set of ~1.2MB (120 bytes × 10K), far exceeding L1. Converting to SoA for hot fields (`pos` + `vel` = 24 bytes × 10K = 240KB) brings it closer to L2.

### False Sharing

False sharing occurs when independent variables share a cache line (64 bytes). Core A writes to variable X, Core B writes to variable Y, but X and Y are on the same line. Each write invalidates the other core's copy, causing cache line bouncing.

```cpp
// BAD: adjacent atomics cause false sharing
struct Counters {
    std::atomic<int> counter_a;  // 4 bytes
    std::atomic<int> counter_b;  // 4 bytes — same cache line!
};

// GOOD: pad to separate cache lines
struct Counters {
    alignas(64) std::atomic<int> counter_a;
    alignas(64) std::atomic<int> counter_b;
};
```

**Detection**: Run `perf c2c record ./program` then `perf c2c report`. Look for cache lines with high "SMT" or "HITM" (Hit Modified Memory) counts on adjacent addresses.

### SIMD

| Instruction Set | Width | Floats per Instruction | Availability |
|-----------------|-------|----------------------|--------------|
| SSE | 128-bit | 4 | All x86-64 |
| AVX2 | 256-bit | 8 | Most modern CPUs (2013+) |
| AVX-512 | 512-bit | 16 | Server CPUs (Ice Lake+) |
| NEON | 128-bit | 4 | ARM (Apple M1+, Raspberry Pi) |

**When SIMD helps**: Tight loops processing arrays of uniform data (add, multiply, dot product, sort). **When it doesn't**: Scalar logic with branches, irregular data access patterns, small arrays (<32 elements).

## 10. Best Practices

1. **Measure first, optimize second.** Profile with `perf stat` before writing a single optimization. The bottleneck is rarely where you think.
2. **Use `-O2` for debug, `-O3` + LTO + PGO for release.** The compiler is better at most optimizations than you are.
3. **Design for cache from the start.** Use SoA layouts for hot fields. Keep hot data contiguous. Use `reserve()` for vectors.
4. **Pad shared atomics.** `alignas(64)` on every atomic variable accessed by multiple threads.
5. **Prefer stack over heap.** Stack allocation is ~100x faster than `malloc`. Use `std::array` for fixed-size data.
6. **Use `constexpr` for compile-time computation.** Moves work from runtime to compile time.
7. **Avoid `std::endl`.** Use `'\n'` — `endl` forces a flush, which is a syscall.
8. **Profile memory fragmentation** in long-running processes. Use jemalloc or tcmalloc for high-frequency allocation patterns.
9. **Benchmark on production hardware.** Desktop performance doesn't predict server performance.
10. **One change at a time.** Measure before and after each optimization to isolate the effect.

## 11. Common Mistakes

| Mistake | Why It's Wrong | What to Do Instead |
|---------|---------------|-------------------|
| Optimizing before profiling | You're optimizing the wrong thing | Profile with `perf` first, identify the actual hotspot |
| Using `-O3` for everything | Can increase code size, hurting i-cache | Profile both `-O2` and `-O3`, choose based on results |
| Assuming `std::vector` is always fast | Unreserved `push_back` causes reallocations | Call `reserve()` when size is known |
| Using `std::endl` in loops | Forces a syscall flush on every iteration | Use `'\n'` |
| Ignoring false sharing | Parallel code can be slower than serial | Pad shared atomics with `alignas(64)` |
| Hand-writing SIMD without checking auto-vectorization | Modern compilers often match hand-written SIMD | Compile with `-fopt-info-vec` first |
| Not benchmarking on target hardware | Desktop ≠ server performance | Profile on production-like machines |
| Using `malloc` in hot paths | System allocator has overhead and fragmentation | Use a memory pool or arena allocator |

## 12. Interview Questions

1. **What is cache-friendly data layout and why does it matter?** Cache-friendly layout keeps frequently accessed data contiguous in memory (SoA, `alignas(64)`). CPUs fetch data in cache lines (64 bytes); if hot data is scattered, every access causes a cache miss (~100 cycles vs ~1 cycle for L1 hit). This can make 10x difference in throughput.
2. **Explain false sharing and how to fix it**: False sharing occurs when threads write to different variables on the same cache line, causing the line to invalidate and reload on every write. Fix with `alignas(64)` padding to ensure each thread's data occupies its own cache line.
3. **When should you use a memory pool allocator?**: Use pools when you allocate/deallocate many objects of the same size frequently (e.g., packet processing, game entities). Pools eliminate fragmentation, reduce allocator overhead, and enable bulk deallocation.
4. **What is branchless programming and when is it useful?**: Branchless programming replaces conditional branches with arithmetic/bit operations (e.g., `mask = x >> 31`). Useful in tight loops with unpredictable branches where branch misprediction causes pipeline stalls (~15 cycles each).
5. **How do you profile C++ performance effectively?**: Use `perf stat` for hardware counters (cache misses, branch misses), `perf record` + `perf report` for hotspot identification, Google Benchmark for microbenchmarks, and Valgrind Callgrind for call-graph profiling. Always measure before and after optimization.
6. **What is the difference between AoS and SoA, and when do you use each?**: AoS (Array of Structures) stores all fields of each object together — good when you access entire objects. SoA (Structure of Arrays) stores each field in a separate array — good when a hot loop accesses one field across many objects. SoA enables SIMD vectorization and better cache utilization for hot-field loops.
7. **How does profile-guided optimization (PGO) work?**: PGO uses runtime profiling data to guide compiler decisions. Step 1: compile with `-fprofile-generate`. Step 2: run representative workloads. Step 3: recompile with `-fprofile-use`. The compiler uses branch frequency data to inline hot functions, lay out hot paths, and optimize branch prediction. Typical improvement: 10–20%.
8. **Explain the CPU cache hierarchy and its impact on performance**: L1d (~32KB, 1 cycle) → L2 (~256KB, 4 cycles) → L3 (~8MB, 10 cycles) → DRAM (~100 cycles). Data is fetched in 64-byte cache lines. If your working set fits in L1, you get ~1 cycle per access. If it spills to DRAM, you pay 100x more. Cache-friendly design means keeping hot data small and contiguous.
9. **What is SIMD and how do you use it in C++?**: SIMD (Single Instruction, Multiple Data) processes multiple values per instruction. AVX2 processes 8 floats per instruction using `__m256` types and intrinsics like `_mm256_add_ps`. You can write intrinsics directly or rely on compiler auto-vectorization with `#pragma GCC ivdep`. Profile to determine which approach is faster for your use case.
10. **How do you avoid memory fragmentation in long-running C++ processes?**: Use specialized allocators (jemalloc, tcmalloc, or custom pool/arena allocators) instead of `malloc`/`free`. Size-class pools (64B, 128B, 256B, etc.) eliminate external fragmentation. Arena allocators provide bulk deallocation. Monitor with `/proc/meminfo` (`VmallocUsed` vs `MemUsed`).
11. **When is lock-free programming appropriate and what are the risks?**: Lock-free programming (using `std::atomic`, CAS operations) is appropriate when lock contention is a measured bottleneck. Risks include: ABA problem, memory ordering bugs, livelock under high contention, and difficulty of correctness verification. Use established patterns (spinlock, atomic counter, Michael-Scott queue) and validate with stress testing.
12. **What is the impact of `-O3` vs `-O2` and when would you choose each?**: `-O3` enables aggressive inlining, loop unrolling, and auto-vectorization. It can increase code size (hurting instruction cache) and sometimes slow down code. Profile both. Choose `-O2` when instruction cache pressure is high (large codebases) or `-O3` when the hot path benefits from vectorization (array processing).
13. **How does branch prediction work and why do unpredictable branches hurt performance?**: The CPU predicts the most likely branch direction to keep the pipeline full. Modern predictors are ~95–97% accurate. Mispredictions cause a pipeline flush (~15 cycle penalty). In a tight loop with 10M iterations, even 3% misprediction costs 4.5M cycles. Branchless code eliminates this entirely.
14. **Explain cache-line alignment and when you need `alignas(64)`**: Cache lines are 64 bytes on x86-64. `alignas(64)` ensures data starts at a cache line boundary. Essential for: (1) shared atomics to prevent false sharing, (2) SIMD loads with `_mm256_load_ps` (requires 32-byte alignment), (3) DMA buffers. Not needed for sequential array access — the first element's alignment propagates.
15. **How do you benchmark C++ code correctly?**: Use Google Benchmark or a similar framework. Warm up the CPU (run the benchmark once before measuring). Use `benchmark::DoNotOptimize()` to prevent dead code elimination. Use `benchmark::ClobberMemory()` to prevent reordering. Run enough iterations for statistical significance. Benchmark on production hardware. Compare before/after each optimization.

## 13. Cross-References

- **Memory Management** → [Module 05: Memory](../05-memory-management/) — Heap vs stack, allocation strategies, smart pointers
- **Concurrency** → [Module 07: Concurrency](../07-concurrency/) — Parallelism, false sharing, lock-free patterns
- **Modern C++** → [Module 08: Modern C++](../08-modern-cpp/) — `constexpr`, move semantics, `string_view`
- **Build Systems** → [Module 13: Build Systems](../13-build-systems/) — Compiler flags, LTO, PGO setup
- **Best Practices** → [Module 14: Best Practices](../14-best-practices/) — Performance as a best practice
- **Senior Level** → [Module 15: Senior](../15-senior/) — Performance architecture decisions

## 14. Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Cache miss causing unexpected slowdown | `perf stat` (L1-dcache-load-misses) | Run `perf stat -e L1-dcache-load-misses ./program`; identify hot loops with high miss rates |
| False sharing between threads | `perf c2c` analysis | Run `perf c2c record ./program`; identify cache lines with high "Cycles Lost" on adjacent atomics |
| Memory fragmentation causing OOM | `jemalloc` heap profiler + `/proc/meminfo` | Profile with jemalloc; compare `VmallocUsed` vs `MemUsed` to detect fragmentation |
| SIMD auto-vectorization not triggering | Compiler report `-fopt-info-vec` | Compile with `-fopt-info-vec`; check if loops are vectorized; add `#pragma GCC ivdep` |
| Branch misprediction in tight loop | `perf stat` (branch-misses) | Profile branch miss rate; convert unpredictable branches to branchless bit manipulation |

## 15. Code Review Checklist

- [ ] Profiling done before any optimization (never guess)
- [ ] `-O2` or `-O3` compiler optimizations enabled for release builds
- [ ] `reserve()` called for vectors when size is known
- [ ] Hot data aligned to cache lines (`alignas(64)`)
- [ ] Atomic variables padded to prevent false sharing
- [ ] Custom allocators used for high-frequency allocation patterns
- [ ] `std::endl` replaced with `'\n'` to prevent unnecessary flushes
- [ ] LTO (Link-Time Optimization) enabled for release builds

## 16. Architecture

Performance optimization requires understanding the hardware architecture. CPU caches (L1/L2/L3) dominate performance — data layout determines cache hit rates. Memory allocation patterns affect fragmentation and throughput. Branch prediction determines pipeline efficiency. SIMD enables processing multiple data points per instruction. Lock-free data structures avoid mutex overhead. Architecture decisions must balance performance with maintainability.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Structure of Arrays (SoA) | Processing one field across many elements | Cache-friendly for hot fields vs. scattered object access |
| Arena allocator | Frame-based or request-based bulk allocation | O(1) allocation vs. no individual object freeing |
| Branchless programming | Tight loops with unpredictable branches | Eliminates misprediction vs. reduced readability |

## 17. Security

| Risk | Impact | Mitigation |
|------|--------|------------|
| Performance optimization bypassing security checks | Vulnerabilities introduced for speed | Never optimize away bounds checks, input validation, or authentication |
| Lock-free data structures introducing ABA problem | Memory corruption, data races | Use hazard pointers or epoch-based reclamation for complex lock-free structures |
| Custom allocator memory leak | Resource exhaustion, DoS | Monitor allocator usage; add leak detection in long-running processes |

## 18. Production Incidents

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

### Incident 4: Auto-Vectorization Regression After Refactor
**Problem**: A real-time audio processing pipeline that ran in 2.1ms spiked to 8.7ms after a code refactor, missing the 3ms real-time deadline and causing audible glitches.

**Cause**: The refactor changed a `float*` raw pointer loop to use `std::vector::iterator` with a range-based for loop. The iterator abstraction introduced an extra indirection that prevented GCC from auto-vectorizing the inner loop. The SIMD width dropped from 8-wide AVX2 to scalar execution.

**Impact**: Audio buffer underruns caused 200ms pops every 40ms. The release was blocked for 3 days. The audio team spent 16 hours bisecting the regression.

**Detection**: Compile with `-fopt-info-vec` revealed the inner loop was no longer vectorized after the refactor. `perf stat` showed IPC (instructions per cycle) dropped from 3.2 to 0.8, confirming the loop went from SIMD to scalar.

**Solution**: Reverted to raw pointer access in the hot loop (`float* data = vec.data()`) and added `#pragma GCC ivdep` to assert no vector dependencies. The loop was re-vectorized at 8-wide AVX2. Also added a static assertion that `sizeof(float) * 8 == 32` for the SIMD width.

**Prevention**: Add `-fopt-info-vec` to CI builds and fail if the hot loop loses vectorization. Write microbenchmarks for performance-critical loops and run them in CI. Document which loops require SIMD and why.

### Incident 5: Lock-Free Queue Starvation Under Contention
**Problem**: A high-throughth message queue using a lock-free MPMC (multi-producer, multi-consumer) ring buffer worked perfectly at 10K msgs/sec but caused 40% of consumer threads to stall at 1M msgs/sec, with some threads starved for 30+ seconds.

**Cause**: The CAS (compare-and-swap) loop for the tail pointer had a livelock under high contention. When 8 producer threads all CAS'd the tail simultaneously, only one succeeded and the rest retried immediately, creating a thundering herd. The retry loop had no backoff, so threads burned CPU cycles in a tight spin without making progress.

**Impact**: Consumer threads starved, messages buffered in the ring buffer grew to capacity, and producers began blocking. P99 latency spiked from 2μs to 30 seconds. The system lost 50K messages during the 10-minute incident window.

**Detection**: `perf top` showed the CAS retry loop consuming 95% of CPU on the affected cores. `strace` revealed no syscalls — the threads were purely in user-space spin loops. Thread sanitizer confirmed no data races, ruling out a correctness bug.

**Solution**: Added exponential backoff to the CAS retry loop (10ns → 20ns → 40ns → ... up to 1ms). Replaced the single tail pointer with a per-thread counter that batches updates, reducing CAS contention by 8x. Used `std::atomic_thread_fence(std::memory_order_seq_cst)` only on the fast path, relaxing to `relaxed` on the retry path.

**Prevention**: Stress-test lock-free structures at 10x expected throughput. Add contention metrics (CAS retry count, backoff time) as observability signals. Use `std::hardware_destructive_interference_size` alignment on the ring buffer slots to prevent false sharing on the head/tail pointers.

## 19. Production Checklist

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

## 20. Maturity Levels

| Level | Capabilities |
|-------|-------------|
| **Beginner** | Compiler optimizations, basic profiling, `reserve()` for vectors |
| **Intermediate** | Cache-friendly layouts, branchless code, memory pools, false sharing avoidance |
| **Advanced** | SIMD intrinsics, lock-free data structures, custom allocators, profile-guided optimization |

## 21. Common Myths — Debunked

| Myth | Reality |
|------|---------|
| "Premature optimization is the root of all evil" | Knuth said "premature" — not "no optimization." Profile first, optimize based on data. |
| "Faster code is always better" | Readability and maintainability matter. Optimize only where measurement shows a bottleneck. |
| "Optimization is only for experts" | Anyone can profile with `perf` and fix the top hotspot. It's a learnable skill. |
| "`-O3` is always the best optimization level" | `-O3` can increase code size and hurt cache performance. Profile both `-O2` and `-O3`. |
| "Inlining everything makes code faster" | Over-inlining increases instruction cache pressure. Let the compiler decide with LTO. |
| "Manual SIMD is always faster than auto-vectorization" | Modern compilers vectorize better than most hand-written SIMD. Profile first. |

## 22. One-Minute Revision Table

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

## 23. Engineering Decision Framework

| Decision | Approach | When to Use | When NOT to Use |
|----------|----------|-------------|-----------------|
| Optimize hot path | Profiling + targeted changes | After measurement shows a bottleneck | Before profiling (premature optimization) |
| Memory layout | Struct-of-Arrays (SoA) vs Array-of-Structs (AoS) | When processing one field across many elements | When objects are accessed as units |
| Allocations | Custom allocator / memory pool | High-frequency small allocations in hot path | Rare allocations or large blocks |
| Parallelism | Thread pool / task system | CPU-bound work that scales with cores | I/O-bound work (use async instead) |
| SIMD | intrinsics or auto-vectorization | Processing arrays of uniform data | Scalar logic with branches |
| Branching | Branchless / predication | Tight loops with unpredictable branches | Simple linear code |
| Caching | Precompute / memoize | Expensive repeated calculations | One-shot computations |

## References
- [Computer Systems: A Programmer's Perspective — Bryant & O'Hallaron](https://www.amazon.com/Computer-Systems-Programmers-Perspective-2nd/dp/013409266X)
- [CppCon Talk: Optimizing C++ — Benchmarking](https://youtube.com/cppcon)
- [Agner Fog — Optimization Manuals](https://www.agner.org/optimize/)
- [Google Benchmark Library](https://github.com/google/benchmark)
