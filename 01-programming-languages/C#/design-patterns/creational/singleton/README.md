# Singleton Pattern (C#)

## Overview

The Singleton pattern ensures a class has only one instance and provides a global
point of access to it. C# offers multiple implementation approaches including static
initialization, lazy loading, and thread-safe double-checked locking.

## When to Use

- Managing shared resources like database connections
- Logging services
- Configuration management
- Thread pool management
- Caching mechanisms

## C# Implementation

### Static Initialization

```csharp
public sealed class Singleton
{
    private static readonly Singleton _instance = new Singleton();

    private Singleton() { }

    public static Singleton Instance => _instance;

    public void DoWork()
    {
        Console.WriteLine("Working...");
    }
}
```

### Lazy Initialization

```csharp
public sealed class Singleton
{
    private static readonly Lazy<Singleton> _lazy =
        new Lazy<Singleton>(() => new Singleton());

    private Singleton() { }

    public static Singleton Instance => _lazy.Value;
}
```

### Thread-Safe Double-Checked Locking

```csharp
public sealed class Singleton
{
    private static Singleton _instance;
    private static readonly object _lock = new object();

    private Singleton() { }

    public static Singleton Instance
    {
        get
        {
            if (_instance == null)
            {
                lock (_lock)
                {
                    if (_instance == null)
                    {
                        _instance = new Singleton();
                    }
                }
            }
            return _instance;
        }
    }
}
```

## Best Practices

- Use sealed class to prevent inheritance
- Prefer Lazy<T> for lazy initialization
- Consider thread safety requirements
- Avoid storing state in singletons when possible
- Use dependency injection as alternative

## Interview Questions

1. What problems does the Singleton pattern solve?
2. How to implement thread-safe Singleton in C#?
3. What is the difference between Lazy<T> and double-checked locking?
4. How does Singleton affect unit testing?
5. When should you avoid using Singleton?

## References

- Microsoft Docs: Singleton Pattern
- "Design Patterns" by Gamma et al.
- Jon Skeet, "C# in Depth"
