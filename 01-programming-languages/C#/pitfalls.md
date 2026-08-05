# C# Common Pitfalls

## Async Void

```csharp
// Dangerous - cannot be awaited, exceptions are lost
public async void FireAndForget()
{
    throw new Exception("This will crash the process");
}

// Correct
public async Task DoWorkAsync()
{
    throw new Exception("Can be caught by caller");
}
```

## String Comparison

```csharp
// Wrong - culture-dependent
if (name == "admin") { }

// Correct - ordinal comparison
if (string.Equals(name, "admin", StringComparison.OrdinalIgnoreCase)) { }
```

## Null Reference Exceptions

```csharp
// Unsafe
var length = user?.Address?.Street?.Length;

// Safe with null-conditional
var length = user?.Address?.Street?.Length ?? 0;
```

## Disposing Objects

```csharp
// Leaks connection
public void GetData()
{
    var connection = new SqlConnection(connectionString);
    connection.Open();
    // connection never disposed
}

// Correct
public async Task GetDataAsync()
{
    await using var connection = new SqlConnection(connectionString);
    await connection.OpenAsync();
}
```

## Collection Modification

```csharp
// Throws InvalidOperationException
foreach (var item in list)
{
    if (item.ShouldRemove) list.Remove(item);
}

// Safe approach
var toRemove = list.Where(i => i.ShouldRemove).ToList();
foreach (var item in toRemove) list.Remove(item);
```

## Thread Safety

```csharp
// Not thread-safe
public class Counter
{
    private int _count;
    public void Increment() => _count++;
}

// Thread-safe
public class Counter
{
    private int _count;
    public void Increment() => Interlocked.Increment(ref _count);
}
```

## Boxing Value Types

```csharp
// Boxing allocation
object boxed = 42;

// Use generics to avoid
T GetValue<T>() where T : struct => default;
```

## String Concatenation in Loops

```csharp
// O(n^2) allocations
string result = "";
foreach (var item in items)
    result += item;

// O(n)
var sb = new StringBuilder();
foreach (var item in items)
    sb.Append(item);
```

## Closing Over Loop Variable

```csharp
// All actions reference final value of i
for (int i = 0; i < 5; i++)
{
    actions.Add(() => Console.WriteLine(i));
}

// Fix - capture local
for (int i = 0; i < 5; i++)
{
    int captured = i;
    actions.Add(() => Console.WriteLine(captured));
}
```

## Blocking Async Code

```csharp
// Deadlock risk in UI/server contexts
var result = GetDataAsync().Result;

// Safe alternatives
var result = await GetDataAsync();
var result = GetDataAsync().GetAwaiter().GetResult();
```

## Race Conditions in Initialization

```csharp
// Lazy<T> for thread-safe lazy initialization
private static readonly Lazy<MyService> _instance =
    new Lazy<MyService>(() => new MyService());

public static MyService Instance => _instance.Value;
```
