# C# Interfaces

## Overview
Interfaces define contracts that classes must implement.

## Basic Interface
```csharp
public interface IMovable
{
    int X { get; set; }
    int Y { get; set; }
    void Move(int x, int y);
}
```

## Implementation
```csharp
public class Player : IMovable
{
    public int X { get; set; }
    public int Y { get; set; }
    
    public void Move(int x, int y)
    {
        X = x;
        Y = y;
    }
}
```

## Multiple Interfaces
```csharp
public class Circle : IDrawable, IResizable
{
    public void Draw() { }
    public void Resize(double factor) { }
}
```

## Interface Inheritance
```csharp
public interface IShape
{
    double Area();
}

public interface IColorShape : IShape
{
    string Color { get; set; }
}
```

## Default Methods (C# 8.0+)
```csharp
public interface ILogger
{
    void Log(string message);
    
    void LogError(string error)
    {
        Log($"ERROR: {error}");
    }
}
```

## Explicit Implementation
```csharp
public class Duck : IFlyable, ISwimmable
{
    void IFlyable.Fly() { }
    void ISwimmable.Swim() { }
}
```

## Key Takeaways
1. Use interfaces for contracts
2. Support multiple interface implementation
3. Use default methods for shared logic
4. Use explicit implementation for conflicts