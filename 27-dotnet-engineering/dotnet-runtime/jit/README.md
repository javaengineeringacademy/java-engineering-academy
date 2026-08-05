## JIT Compilation in .NET

Just-In-Time compilation converts IL code to native machine code at runtime using RyuJIT.

## Overview

The JIT compiler transforms CIL (Common Intermediate Language) into native machine code at runtime. RyuJIT is the optimizing JIT compiler used in modern .NET, producing highly optimized native code.

## Why It Matters

- Understanding JIT helps write JIT-friendly code
- JIT optimizations directly affect application performance
- Profile-Guided Optimization (PGO) improves real-world performance
- AOT compilation offers alternatives to JIT in some scenarios

## Key Concepts

- **CIL/IL**: Intermediate language output from C# compiler
- **RyuJIT**: The optimizing JIT compiler in .NET
- **Tiered Compilation**: Multiple optimization passes for startup vs throughput
- **PGO**: Profile-Guided Optimization based on runtime data
- **Ready-to-Run (R2R):** Pre-compiled assemblies with JIT fallback
- **Native AOT**: Ahead-of-time compilation to native binary

## Core Topics

- JIT compilation pipeline and phases
- RyuJIT optimization techniques
- Tiered compilation (Quick JIT vs Optimized JIT)
- Profile-Guided Optimization (PGO)
- Ready-to-Run assemblies
- Native AOT compilation
- JIT-related performance counters
- Deoptimization and OSR (On-Stack Replacement)

## Best Practices

- Use tiered compilation for faster startup
- Enable R2R for library pre-compilation
- Consider Native AOT for small, fast-startup apps
- Profile with tiered compilation enabled
- Avoid complex generic instantiations in hot paths

## Hands-on Labs

- Compare JIT vs R2R vs AOT startup times
- Analyze JIT compilation with PerfView
- Enable and test tiered compilation
- Profile PGO improvements

## Interview Questions

1. How does tiered compilation work?
2. What is the difference between R2R and Native AOT?
3. How does RyuJIT optimize code at runtime?
4. What is profile-guided optimization?

## References

- https://learn.microsoft.com/dotnet/core/whats-new/dotnet-7/
- https://learn.microsoft.com/dotnet/core/deploying/ready-to-run
- https://learn.microsoft.com/dotnet/core/deploying/native-aot/
