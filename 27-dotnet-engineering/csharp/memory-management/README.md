## Memory Management in C#

Understanding garbage collection, spans, memory, and disposal patterns for efficient .NET memory usage.

## Overview

.NET uses automatic garbage collection to manage heap memory, but understanding memory allocation patterns, spans, and disposal is critical for writing performant applications.

## Why It Matters

- Improves application performance and reduces GC pressure
- Prevents memory leaks from undisposed resources
- Essential for high-performance server applications
- Reduces latency in real-time systems
- Understanding allocation patterns enables optimization

## Key Concepts

- **Garbage Collector**: Automatic memory management with generational collection
- **Gen 0, 1, 2**: Generational collections based on object lifetime
- **Server GC**: High-throughput GC for server applications
- **Span<T>:** Stack-allocated, zero-copy view of memory
- **Memory<T>:** Heap-friendly wrapper around Span
- **IDisposable**: Synchronous resource cleanup pattern
- **IAsyncDisposable**: Asynchronous resource cleanup pattern
- **using Statement**: Automatic disposal of IDisposable objects

## Core Topics

- Garbage collector generations and collection triggers
- Server vs workstation garbage collection
- IDisposable and IAsyncDisposable patterns
- using and await using statements
- Span<T> and Memory<T> usage
- ArrayPool and MemoryPool for reuse
- Weak references and conditional weak tables
- Memory profiling and diagnostics

## Best Practices

- Implement IDisposable for unmanaged resources
- Use `using` statements for all IDisposable objects
- Use Span<T> for temporary buffer operations
- Pool frequently allocated large objects
- Avoid finalizers unless managing unmanaged resources
- Use GC.GetAllocatedBytesForCurrentThread to track allocations

## Hands-on Labs

- Implement IDisposable and IAsyncDisposable patterns
- Build a buffer pool using ArrayPool<T>
- Use Span<T> for string parsing without allocations
- Profile memory allocations with dotMemory
- Build a finalizer-safe disposable type

## Interview Questions

1. Explain the three generations of the garbage collector.
2. What is the difference between IDisposable and IAsyncDisposable?
3. How does Span<T> avoid heap allocations?
4. When should you use Server GC vs Workstation GC?
5. What is the Dispose pattern and why is it important?

## References

- https://learn.microsoft.com/dotnet/standard/garbage-collection/
- https://learn.microsoft.com/dotnet/api/system.idisposable
- https://learn.microsoft.com/dotnet/api/system.span-1
- https://learn.microsoft.com/dotnet/api/system.buffers.arraypool-1
