## Async/Await in C#

Asynchronous programming in C# using async/await, Task, ValueTask, and CancellationToken for non-blocking I/O operations.

## Overview

Async/await is C#'s primary mechanism for writing non-blocking asynchronous code. It enables applications to remain responsive during I/O operations like network calls, file access, and database queries.

## Why It Matters

- Prevents thread blocking during I/O operations
- Improves application scalability and throughput
- Simplifies complex asynchronous workflows
- Essential for web server and high-performance applications
- Foundation for modern .NET APIs

## Key Concepts

- **Task<T>**: Represents an asynchronous operation that returns a value
- **Task**: Represents an asynchronous operation with no return value
- **async/await**: Keywords that enable writing asynchronous code synchronously
- **CancellationToken**: Cooperative cancellation for long-running operations
- **ValueTask<T>:** Allocation-free alternative to Task for synchronous completions
- **IAsyncEnumerable<T>:** Asynchronous streaming of sequences
- **ConfigureAwait**: Controls synchronization context capture

## Core Topics

- async/await fundamentals and state machines
- Task vs ValueTask usage patterns
- CancellationToken propagation and registration
- Parallel operations with Task.WhenAll and Task.WhenAny
- Async disposal with IAsyncDisposable
- Async streams with yield return and await foreach
- ConfigureAwait(false) and synchronization context
- Common pitfalls (deadlocks, fire-and-forget, exception handling)

## Best Practices

- Always pass CancellationToken through the call chain
- Use ValueTask for hot paths that often complete synchronously
- Apply ConfigureAwait(false) in library code
- Never use async void except for event handlers
- Use WhenAll for parallel, WhenAny for racing
- Handle AggregateException properly
- Use IAsyncDisposable for async resource cleanup

## Hands-on Labs

- Build an async HTTP client wrapper with retry logic
- Implement async disposal for database connections
- Create a pipeline using IAsyncEnumerable
- Demonstrate cancellation token patterns
- Compare Task vs ValueTask performance with BenchmarkDotNet

## Interview Questions

1. What is the difference between Task and ValueTask?
2. Explain the state machine transformation behind async/await.
3. When should you use ConfigureAwait(false)?
4. How does CancellationToken work and how do you propagate it?
5. What are common async/await pitfalls and how do you avoid them?
6. What is async void and why is it dangerous?

## References

- https://learn.microsoft.com/dotnet/csharp/async/
- https://learn.microsoft.com/dotnet/standard/parallel-processing-and-concurrency/
- https://learn.microsoft.com/dotnet/api/system.threading.tasks.valuetask
