## Performance in C#

Techniques and tools for optimizing .NET application performance including benchmarking, pooling, and hot path optimization.

## Overview

.NET provides excellent performance out of the box, but understanding allocation patterns, JIT optimization, and profiling tools enables you to write high-performance code.

## Why It Matters

- Reduces infrastructure costs in cloud environments
- Improves user experience with lower latency
- Enables handling more requests per server
- Critical for real-time and high-throughput systems
- Differentiates senior engineers from juniors

## Key Concepts

- **BenchmarkDotNet**: Industry-standard benchmarking framework
- **ArrayPool<T>:** Reuse arrays to reduce GC pressure
- **StringPool**: Deduplicate strings to reduce allocations
- **Span<T>:** Zero-copy memory operations
- **ValueTask**: Allocation-free async for hot paths
- **Object Pooling**: Reuse expensive objects
- **JIT Optimization**: Understanding compiler optimizations
- **Allocation Tracking**: Identifying where memory is allocated

## Core Topics

- BenchmarkDotNet setup and usage
- Allocation analysis with dotMemory and PerfView
- String optimization (StringBuilder, string.Create, span-based)
- Object pooling patterns
- Cache-friendly data structures
- SIMD and hardware intrinsics
- Link-time and AOT compilation
- Performance counters and EventCounters

## Best Practices

- Benchmark before optimizing
- Profile allocations in high-throughput code
- Use ArrayPool for temporary buffers
- Prefer Span<T> over string.Substring
- Pool database connections and HTTP clients
- Use StringBuilder for many concatenations
- Consider value types for small, short-lived data

## Hands-on Labs

- Write benchmarks comparing approaches
- Reduce allocations in a hot path
- Implement a custom object pool
- Optimize string processing with Span<T>
- Use BenchmarkDotNet to compare Task vs ValueTask

## Interview Questions

1. How do you identify performance bottlenecks in a .NET application?
2. Explain the tradeoffs between allocation and computation.
3. What is the difference between ValueTask and Task from a performance perspective?
4. How does JIT optimization affect your code?
5. What tools do you use for .NET performance profiling?

## References

- https://learn.microsoft.com/dotnet/core/performance/
- https://learn.microsoft.com/dotnet/core/diagnostics/
- https://benchmarkdotnet.org/
- https://learn.microsoft.com/dotnet/api/system.buffers.arraypool-1
