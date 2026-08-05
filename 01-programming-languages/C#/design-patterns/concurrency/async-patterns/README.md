# Async Patterns (C#)

## Overview

C# provides first-class support for asynchronous programming through async/await,
Task-based patterns, and cancellation tokens. These patterns enable non-blocking
operations while maintaining readable code.

## When to Use

- I/O-bound operations (file, network, database)
- UI responsiveness requirements
- Scalability for high-concurrency applications
- Long-running operations that shouldn't block

## C# Implementation

### Basic Async/Await

```csharp
public async Task<string> FetchDataAsync(string url)
{
    using var client = new HttpClient();
    return await client.GetStringAsync(url);
}
```

### Task Parallel

```csharp
public async Task ProcessMultipleAsync()
{
    var task1 = FetchDataAsync("url1");
    var task2 = FetchDataAsync("url2");

    await Task.WhenAll(task1, task2);
}
```

### Cancellation Pattern

```csharp
public async Task LongOperationAsync(CancellationToken ct)
{
    for (int i = 0; i < 100; i++)
    {
        ct.ThrowIfCancellationRequested();
        await Task.Delay(100, ct);
        Console.WriteLine($"Step {i}");
    }
}
```

### SemaphoreSlim Pattern

```csharp
public class AsyncSemaphore
{
    private readonly SemaphoreSlim _semaphore = new SemaphoreSlim(10);

    public async Task ExecuteAsync(Func<Task> operation)
    {
        await _semaphore.WaitAsync();
        try
        {
            await operation();
        }
        finally
        {
            _semaphore.Release();
        }
    }
}
```

### Async Queue

```csharp
public class AsyncQueue<T>
{
    private readonly Queue<T> _queue = new();
    private readonly SemaphoreSlim _semaphore = new(0);

    public void Enqueue(T item)
    {
        lock (_queue)
        {
            _queue.Enqueue(item);
        }
        _semaphore.Release();
    }

    public async Task<T> DequeueAsync(CancellationToken ct)
    {
        await _semaphore.WaitAsync(ct);
        lock (_queue)
        {
            return _queue.Dequeue();
        }
    }
}
```

## Best Practices

- Always use ConfigureAwait(false) in library code
- Never use async void except for event handlers
- Use cancellation tokens for cancellable operations
- Avoid blocking on async code
- Consider using ValueTask for hot paths

## Interview Questions

1. What is the difference between Task and ValueTask?
2. How does ConfigureAwait work?
3. When should you use async void?
4. How do you handle exceptions in async code?
5. What is the async-over-sync anti-pattern?

## References

- Microsoft Docs: Asynchronous Programming
- "Async and Await" by Stephen Cleary
- "Concurrency in C# Cookbook" by Stephen Cleary
