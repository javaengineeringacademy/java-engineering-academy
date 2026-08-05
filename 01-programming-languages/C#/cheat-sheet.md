# C# Cheat Sheet

## String Operations

```csharp
string.IsNullOrEmpty(s)           // Check null or empty
string.IsNullOrWhiteSpace(s)     // Check null, empty, or whitespace
s.Contains("text", StringComparison.OrdinalIgnoreCase)
s.Split(',', StringSplitOptions.RemoveEmptyEntries)
string.Join(", ", array)
$"Hello {name}"                  // Interpolation
$"""Raw string literal"""        // Raw string
```

## Collection Operations

```csharp
list.Where(x => x > 5).ToList()  // Filter
list.Select(x => x.Name).ToList() // Transform
list.OrderBy(x => x.Name)        // Sort
list.GroupBy(x => x.Category)    // Group
list.Any(x => x.Id == 1)         // Check existence
list.First(x => x.Id == 1)       // Find first
list.Count(x => x.IsActive)      // Count with predicate
```

## Null Handling

```csharp
name ?? "default"                 // Null coalescing
name ??= "default"                // Null coalescing assignment
obj?.Property                     // Null conditional
obj?.Method()                     // Null conditional method
obj!.Property                     // Null-forgiving
```

## Type Operations

```csharp
obj is string                     // Type check
obj as string                     // Safe cast (returns null on failure)
(string)obj                       // Direct cast (throws on failure)
obj.GetType()                     // Get runtime type
typeof(string)                    // Get compile-time type
```

## Async Patterns

```csharp
await Task.WhenAll(task1, task2)  // Parallel execution
await Task.WhenAny(task1, task2)  // First to complete
await Task.Delay(1000)            // Async delay
await using var x = ...           // Async dispose
```

## File Operations

```csharp
await File.ReadAllTextAsync(path)
await File.WriteAllTextAsync(path, content)
await File.ReadAllLinesAsync(path)
File.Exists(path)
Directory.GetFiles(path, "*.cs")
```

## HTTP Client

```csharp
using var client = new HttpClient();
var response = await client.GetAsync(url);
response.EnsureSuccessStatusCode();
var data = await response.Content.ReadFromJsonAsync<T>();
await client.PostAsJsonAsync(url, payload);
```

## Common Attributes

```csharp
[Serializable]
[Obsolete("Use NewMethod instead")]
[JsonPropertyName("camelCase")]
[ApiController]
[HttpGet("{id}")]
[HttpPost]
[ValidateAntiForgeryToken]
```

## LINQ Quick Reference

```csharp
Enumerable.Range(1, 10)          // Generate sequence
Enumerable.Repeat(value, count)  // Repeat value
list.Distinct()                  // Remove duplicates
list.Zip(list2, (a, b) => ...)  // Combine sequences
list.Aggregate((a, b) => a + b) // Fold
```

## Pattern Matching

```csharp
x is > 10                        // Relational
x is string { Length: > 5 }      // Property
point is ( > 0, > 0)             // Tuple
obj switch { Type t => ..., _ => default }  // Type
```
