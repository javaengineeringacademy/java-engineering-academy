# C# Performance

## BenchmarkDotNet

Measure performance accurately with statistical rigor.

```csharp
using BenchmarkDotNet.Running;
using BenchmarkDotNet.Attributes;

[MemoryDiagnoser]
public class StringBenchmarks
{
    private readonly string _input = new string('x', 1000);

    [Benchmark(Baseline = true)]
    public string StringConcat() => "Hello" + " " + "World";

    [Benchmark]
    public string StringInterpolation() => $"Hello {"World"}";

    [Benchmark]
    public string StringConcatenateLarge() =>
        string.Concat(_input, _input, _input);
}

BenchmarkRunner.Run<StringBenchmarks>();
```

## Span and Memory

Work with slices of arrays without allocations.

```csharp
// Span<T> - stack-only, ref struct
public static int SumSpan(ReadOnlySpan<int> numbers)
{
    int sum = 0;
    foreach (var n in numbers)
        sum += n;
    return sum;
}

// Memory<T> - can be stored on heap
public async Task ProcessLargeFile(string path)
{
    using var stream = File.OpenRead(path);
    Memory<byte> buffer = new byte[4096];
    while (await stream.ReadAsync(buffer) > 0)
    {
        ProcessBuffer(buffer.Span);
    }
}
```

## Object Pooling

Reuse expensive objects to reduce allocations.

```csharp
// Using ArrayPool<T>
public class PooledBuffer : IDisposable
{
    private readonly byte[] _buffer;
    public ArrayPool<byte> Pool { get; }

    public PooledBuffer(int minimumLength)
    {
        Pool = ArrayPool<byte>.Shared;
        _buffer = Pool.Rent(minimumLength);
    }

    public void Dispose() => Pool.Return(_buffer);
}

// Using ObjectPool<T>
var pool = new DefaultObjectPoolProvider().CreateStringBuilderPool();
var sb = pool.Get();
try
{
    sb.Clear();
    // use StringBuilder
}
finally { pool.Return(sb); }
```

## Value Types

```csharp
// Use struct for small, immutable data
public readonly record struct Point(double X, double Y);

// readonly ref struct for stack-only
public readonly ref struct ParsedNumber
{
    public readonly double Value;
    public ParsedNumber(ReadOnlySpan<char> input)
    {
        Value = double.Parse(input);
    }
}
```

## Async Performance

```csharp
// Use ValueTask for hot synchronous paths
public ValueTask<int> GetOrComputeAsync(int key)
{
    if (_cache.TryGetValue(key, out var cached))
        return new ValueTask<int>(cached);
    return new ValueTask<int>(ComputeSlowAsync(key));
}

// Configure await to avoid context capture
await HttpGetAsync(url).ConfigureAwait(false);
```

## High-Performance Tips

- Avoid string allocations in loops
- Use `StringBuilder` for many concatenations
- Prefer `Span<T>` over `byte[]` for parsing
- Use `stackalloc` for small temporary buffers
- Avoid LINQ in performance-critical paths
- Cache compiled delegates and regex patterns
- Use `FrozenSet<T>` and `FrozenDictionary<T>` for read-heavy lookups

## Profile with dotnet-trace

```bash
dotnet-trace collect -p <pid> --duration 00:00:30
dotnet-trace convert trace.nettrace --format SpeedScope
```
