## Common Language Runtime (CLR)

The CLR is the execution engine of .NET, managing code execution, memory, type safety, and security.

## Overview

The CLR provides the managed execution environment for .NET code. It handles JIT compilation, garbage collection, exception handling, thread management, and security enforcement.

## Why It Matters

- Understanding the CLR helps diagnose runtime issues
- Knowledge of CLR internals enables performance optimization
- CLR security features protect applications
- Thread pool management affects application scalability

## Key Concepts

- **Code Access Security**: Restricting what code can do
- **Type Safety**: Ensuring code only accesses authorized memory
- **Exception Handling**: Structured exception handling across languages
- **Thread Pool**: Managed pool of worker threads
- **AppDomains**: Isolation boundaries within a process (legacy)
- **Assemblies**: Units of deployment and versioning

## Core Topics

- CLR startup and code loading
- JIT compilation process
- Type loading and verification
- Exception handling internals
- Thread pool management and queuing
- AppDomain and AssemblyLoadContext
- Security policies and CAS (legacy)

## Best Practices

- Use AssemblyLoadContext for plugin isolation
- Monitor thread pool starvation with EventCounters
- Handle exceptions at appropriate boundaries
- Use async to avoid thread pool blocking

## Hands-on Labs

- Analyze CLR startup with PerfView
- Monitor thread pool queues in production
- Implement plugin isolation with AssemblyLoadContext
- Profile exception handling overhead

## Interview Questions

1. What is the role of the CLR in .NET application execution?
2. How does the CLR manage thread pools?
3. Explain AppDomain vs AssemblyLoadContext.
4. What happens during CLR startup?

## References

- https://learn.microsoft.com/dotnet/standard/clr/
- https://learn.microsoft.com/dotnet/framework/app-domains/
- https://learn.microsoft.com/dotnet/api/system.runtime.loader.assemblyloadcontext
