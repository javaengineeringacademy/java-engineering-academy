# C# Generics

## Overview
Generics allow writing type-safe, reusable code.

## Generic Class
```csharp
public class Repository<T>
{
    private readonly List<T> items = new List<T>();
    
    public void Add(T item) => items.Add(item);
    public T GetById(int index) => items[index];
}
```

## Generic Interface
```csharp
public interface IRepository<T>
{
    void Add(T item);
    T GetById(int id);
}
```

## Generic Methods
```csharp
public static void Swap<T>(ref T a, ref T b)
{
    T temp = a;
    a = b;
    b = temp;
}
```

## Constraints
```csharp
// where T : constraint
public class Cache<TKey, TValue> 
    where TKey : notnull
    where TValue : class
{
    // Implementation
}
```

## Common Constraints
```csharp
where T : class        // Reference type
where T : struct       // Value type
where T : new()        // Has parameterless constructor
where T : IComparable  // Implements interface
where T : BaseClass    // Inherits from class
```

## Generic Collections
```csharp
List<T>           // Dynamic array
Dictionary<K,V>   // Key-value pairs
Queue<T>          // FIFO collection
Stack<T>          // LIFO collection
```

## Key Takeaways
1. Use generics for type safety
2. Apply constraints for requirements
3. Leverage built-in generic collections
4. Avoid boxing with generic types