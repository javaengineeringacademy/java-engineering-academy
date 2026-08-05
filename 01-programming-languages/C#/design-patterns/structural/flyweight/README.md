# Flyweight Pattern (C#)

## Overview

The Flyweight pattern minimizes memory usage by sharing as much data as possible with
similar objects. C# provides Dictionary and struct types for efficient flyweight
implementations.

## When to Use

- Application uses large number of objects
- Object state can be made extrinsic
- Memory costs are high
- Many objects can be replaced with fewer shared ones

## C# Implementation

### Basic Flyweight

```csharp
public class Flyweight
{
    private readonly string _sharedState;

    public Flyweight(string sharedState) => _sharedState = sharedState;

    public void Operation(string extrinsicState)
    {
        Console.WriteLine($"Shared: {_sharedState}, Extrinsic: {extrinsicState}");
    }
}

public class FlyweightFactory
{
    private readonly Dictionary<string, Flyweight> _flyweights = new();

    public Flyweight GetFlyweight(string key)
    {
        if (!_flyweights.ContainsKey(key))
        {
            _flyweights[key] = new Flyweight(key);
            Console.WriteLine($"Creating new flyweight for {key}");
        }
        return _flyweights[key];
    }

    public int Count() => _flyweights.Count;
}
```

### Struct-Based Flyweight

```csharp
public readonly struct ColorFlyweight
{
    public byte R { get; }
    public byte G { get; }
    public byte B { get; }

    public ColorFlyweight(byte r, byte g, byte b)
    {
        R = r;
        G = g;
        B = b;
    }

    public static ColorFlyweight Create(byte r, byte g, byte b) =>
        new ColorFlyweight(r, g, b);
}
```

### With Weak References

```csharp
public class WeakFlyweightFactory
{
    private readonly Dictionary<string, WeakReference<Flyweight>> _flyweights = new();

    public Flyweight GetFlyweight(string key)
    {
        if (_flyweights.TryGetValue(key, out var weakRef) &&
            weakRef.TryGetTarget(out var flyweight))
        {
            return flyweight;
        }

        var newFlyweight = new Flyweight(key);
        _flyweights[key] = new WeakReference<Flyweight>(newFlyweight);
        return newFlyweight;
    }
}
```

## Best Practices

- Separate intrinsic (shared) from extrinsic state
- Use struct for small immutable flyweights
- Consider WeakReference for memory-sensitive scenarios
- Document flyweight lifecycle clearly
- Use object pooling as alternative

## Interview Questions

1. What is the difference between Flyweight and Singleton?
2. How do you handle thread safety in Flyweight?
3. What is intrinsic vs extrinsic state?
4. When should you use Flyweight over caching?
5. How do you manage flyweight lifecycle?

## References

- Microsoft Docs: Flyweight Pattern
- "Design Patterns" by Gamma et al.
- "Object-Oriented Software Construction" by Meyer
