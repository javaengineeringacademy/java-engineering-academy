# C# Anti-Patterns

## Overview
Common mistakes and anti-patterns to avoid in C#.

## 1. Boxing/Unboxing
```csharp
// Bad
object boxed = 42;
int unboxed = (int)boxed; // Performance hit

// Good
int value = 42;
```

## 2. String Concatenation in Loop
```csharp
// Bad
string result = "";
for (int i = 0; i < 1000; i++)
{
    result += i.ToString(); // Creates many strings
}

// Good
var sb = new StringBuilder();
for (int i = 0; i < 1000; i++)
{
    sb.Append(i);
}
```

## 3. Not Disposing Resources
```csharp
// Bad
var stream = new FileStream("file.txt", FileMode.Open);
// File not closed

// Good
using (var stream = new FileStream("file.txt", FileMode.Open))
{
    // Use stream
}
```

## 4. Catching All Exceptions
```csharp
// Bad
try
{
    DoSomething();
}
catch (Exception) { } // Swallows all errors

// Good
try
{
    DoSomething();
}
catch (SpecificException ex)
{
    Logger.Error(ex.Message);
}
```

## 5. Blocking Async Code
```csharp
// Bad
var result = FetchDataAsync().Result; // Deadlock risk

// Good
var result = await FetchDataAsync();
```

## 6. Empty Finalizers
```csharp
// Bad
~MyClass() { } // Unnecessary

// Good - only if you have unmanaged resources
~MyClass()
{
    Dispose(false);
}
```

## 7. Not Using Using
```csharp
// Bad
SqlConnection conn = new SqlConnection(connString);
conn.Open();
// Connection not closed

// Good
using var conn = new SqlConnection(connString);
conn.Open();
```

## 8. Public Fields
```csharp
// Bad
public class User
{
    public string Name; // No encapsulation
}

// Good
public class User
{
    public string Name { get; set; }
}
```

## Best Practices
1. Use using for IDisposable
2. Use async/await properly
3. Use StringBuilder for string operations
4. Handle specific exceptions
5. Avoid boxing/unboxing