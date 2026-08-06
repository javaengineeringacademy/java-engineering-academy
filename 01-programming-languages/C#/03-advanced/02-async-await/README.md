# C# Async/Await

## Overview
Async/await provides asynchronous programming patterns.

## Basic Async Method
```csharp
static async Task<string> FetchDataAsync()
{
    await Task.Delay(2000); // Simulate work
    return "data";
}

// Call
string result = await FetchDataAsync();
```

## Async with Return Value
```csharp
static async Task<int> CalculateAsync(int a, int b)
{
    await Task.Delay(1000);
    return a + b;
}

int sum = await CalculateAsync(5, 3);
```

## Cancellation
```csharp
var cts = new CancellationTokenSource();

async Task ProcessAsync(CancellationToken token)
{
    for (int i = 0; i < 10; i++)
    {
        token.ThrowIfCancellationRequested();
        await Task.Delay(500, token);
    }
}

// Cancel after 2 seconds
await Task.Delay(2000);
cts.Cancel();
```

## Concurrent Tasks
```csharp
var tasks = new List<Task<string>>
{
    FetchDataAsync(),
    FetchDataAsync(),
    FetchDataAsync()
};

string[] results = await Task.WhenAll(tasks);
```

## Error Handling
```csharp
try
{
    string data = await FetchDataAsync();
}
catch (Exception ex)
{
    Console.WriteLine($"Error: {ex.Message}");
}
```

## Async Iterator (C# 8.0+)
```csharp
static async IAsyncEnumerable<int> GenerateAsync()
{
    for (int i = 0; i < 5; i++)
    {
        await Task.Delay(500);
        yield return i;
    }
}

await foreach (var num in GenerateAsync())
{
    Console.WriteLine(num);
}
```

## Key Takeaways
1. Always await async calls
2. Use CancellationToken for cancellation
3. Use Task.WhenAll for concurrency
4. Avoid async void except for events