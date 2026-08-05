## C# Language

C# is a modern, type-safe, object-oriented language designed for building .NET applications. It combines powerful language features with extensive framework support.

## Overview

C# evolved from a simple Java-like language to a rich, expressive language with pattern matching, records, pattern-based destructuring, and advanced generics. The latest versions focus on performance, minimal allocations, and developer productivity.

## Why It Matters

- Primary language for .NET development
- Strongly-typed with excellent IDE support
- Continuous evolution with annual releases
- First-class async/await support
- Rich ecosystem of libraries and tools

## Key Concepts

- **Type System**: Value types, reference types, generics, nullable references
- **Memory Management**: Stack vs heap, spans, memory, disposal patterns
- **Asynchronous Programming**: async/await, Task, ValueTask, CancellationToken
- **Functional Features**: Records, pattern matching, local functions, lambdas
- **OOP Pillars**: Encapsulation, inheritance, polymorphism, abstraction
- **LINQ**: Query syntax and method syntax, deferred execution
- **Metaprogramming**: Attributes, reflection, source generators

## Core Topics

- Fundamentals (Variables, Types, Control Flow, Methods)
- OOP (Classes, Inheritance, Polymorphism, Encapsulation)
- Advanced (Pattern Matching, Spans, Records, Top-Level Statements)
- LINQ (Operators, Deferred Execution, IQueryable)
- Async/Await (Task, ValueTask, CancellationToken)
- Reflection (Type Inspection, Attributes, Dynamic Loading)
- Delegates (Func, Action, Predicate, Patterns)
- Events (Event Patterns, Handlers, Pub/Sub)
- Generics (Types, Constraints, Variance)
- Memory Management (GC, Spans, Memory, Disposal)
- Performance (Benchmarks, Pooling, Hot Paths)

## Best Practices

- Enable nullable reference types in all projects
- Use `record` types for immutable data models
- Prefer `ValueTask` over `Task` for high-throughput hot paths
- Use `Span<T>` and `Memory<T>` for zero-copy operations
- Apply `IAsyncDisposable` for async resource cleanup
- Use pattern matching instead of complex if/else chains
- Write unit tests for all business logic
- Use analyzers and code style enforcement

## Hands-on Labs

- Build a console app with top-level statements
- Implement a generic repository pattern
- Create a custom attribute with reflection
- Benchmark code using BenchmarkDotNet
- Build an async pipeline with channels
- Use pattern matching for complex data transformation

## Interview Questions

1. What is the difference between `ref`, `in`, and `out` parameters?
2. Explain nullable reference types and how they differ from nullable value types.
3. What are records and when should you use them?
4. How does pattern matching work in C#?
5. What is the difference between `Span<T>` and `Array<T>`?
6. Explain the difference between `IAsyncEnumerable<T>` and `IEnumerable<T>`.

## References

- https://learn.microsoft.com/dotnet/csharp/
- https://learn.microsoft.com/dotnet/csharp/whats-new/
- https://docs.microsoft.com/dotnet/csharp/language-reference/
- https://github.com/dotnet/roslyn
