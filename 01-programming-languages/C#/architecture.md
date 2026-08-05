# C# Architecture

## Common Language Runtime (CLR)

The CLR is the virtual machine that manages the execution of .NET programs. It handles memory management, type safety, exception handling, garbage collection, security, and thread management.

### Key Responsibilities

- **Code Management**: Compiles IL (Intermediate Language) to native code
- **Memory Management**: Allocates and deallocates memory automatically
- **Type Safety**: Verifies type compatibility at runtime
- **Exception Handling**: Provides structured exception handling across language boundaries
- **Thread Management**: Manages thread pool and async scheduling

## Just-In-Time (JIT) Compilation

The JIT compiler converts IL code into native machine code at runtime. This enables platform-specific optimizations but adds startup overhead.

### JIT Modes

- **Normal JIT**: Compiles methods on first invocation
- **Ahead-of-Time (AOT)**: Compiles ahead via ReadyToRun or NativeAOT
- **Tiered Compilation**: Starts with quickly-compiled code, recompiles hot paths

```csharp
// The CLR compiles this IL to native code at runtime
public int Add(int a, int b) => a + b;
```

## Managed vs Unmanaged Code

| Feature | Managed Code | Unmanaged Code |
|---------|-------------|----------------|
| Memory | GC handles | Manual management |
| Type Safety | Verified | Not verified |
| Portability | Cross-platform | Platform-specific |
| Safety | Buffer overflows prevented | Vulnerable |

## Garbage Collection

The generational GC optimizes for typical allocation patterns:

- **Gen 0**: Short-lived objects, collected frequently
- **Gen 1**: Buffer between short and long-lived
- **Gen 2**: Long-lived objects, collected less often
- **Large Object Heap**: Objects >= 85,000 bytes

```csharp
// GC automatically manages this memory
var list = new List<int>();
list.Add(42);
// When list goes out of scope, GC reclaims memory
```

## AppDomains

AppDomains provide isolation boundaries within a process. They have been largely replaced by AssemblyLoadContext in .NET Core and later versions.

### Historical Usage

- Plugin isolation
- Security boundaries
- Unloading assemblies

## Application Architecture Layers

```
Presentation (UI/API)
    |
Business Logic (Services)
    |
Data Access (Repositories)
    |
Infrastructure (Database, Cache, Messaging)
```

## Assemblies and Metadata

Assemblies contain compiled code plus metadata describing types, members, and references. The runtime uses metadata for reflection, serialization, and dependency resolution.
