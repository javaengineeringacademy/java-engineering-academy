## Garbage Collection in .NET

The .NET garbage collector automatically manages heap memory using generational collection strategies.

## Overview

The GC is a tracing garbage collector that identifies and reclaims memory occupied by objects no longer referenced by the application. It uses generational and server-based strategies for optimization.

## Why It Matters

- Understanding GC helps reduce pause times
- Proper allocation patterns reduce GC pressure
- Server GC vs Workstation GC affects throughput
- GC settings can be tuned for specific workloads

## Key Concepts

- **Generational GC**: Objects grouped by lifetime (Gen 0, 1, 2)
- **Gen 0**: Short-lived objects, collected frequently
- **Gen 1**: Buffer between short and long-lived objects
- **Gen 2**: Long-lived objects, collected less frequently
- **LOH**: Large Object Heap for objects >= 85,000 bytes
- **Server GC**: Multi-processor GC for high throughput
- **Workstation GC**: Single-processor GC for low latency

## Core Topics

- Generational collection mechanics
- Server vs Workstation GC configuration
- Large Object Heap management
- GC collection modes (Background, Concurrent, Foreground)
- GC latency modes (LowLatency, SustainedLowLatency)
- GC settings and configuration
- GC performance monitoring and diagnostics
- Pinned objects and their impact

## Best Practices

- Allocate short-lived objects on the stack or pooled
- Avoid finalizers unless managing unmanaged resources
- Use ArrayPool for temporary large buffers
- Set GC mode based on workload characteristics
- Monitor GC pauses with EventCounters
- Avoid pinned objects in hot paths

## Hands-on Labs

- Compare Server vs Workstation GC performance
- Monitor GC collections with dotMemory
- Tune GC settings for a web application
- Analyze LOH fragmentation and compact

## Interview Questions

1. What are the three generations of the GC and how do they differ?
2. When should you use Server GC vs Workstation GC?
3. How does background GC work?
4. What is the Large Object Heap and why does it matter?

## References

- https://learn.microsoft.com/dotnet/standard/garbage-collection/
- https://learn.microsoft.com/dotnet/core/runtime-config/garbage-collection
- https://learn.microsoft.com/dotnet/api/system.gc
