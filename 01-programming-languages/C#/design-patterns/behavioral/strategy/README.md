# Strategy Pattern (C#)

## Overview

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes
them interchangeable. C# uses interfaces, delegates, and lambda expressions for
flexible strategy implementations.

## When to Use

- Multiple algorithms for specific task
- Need to switch algorithms at runtime
- Avoiding conditional statements for algorithm selection
- Isolating algorithm implementation from clients

## C# Implementation

### Classic Strategy

```csharp
public interface ISortStrategy
{
    void Sort(int[] array);
}

public class BubbleSort : ISortStrategy
{
    public void Sort(int[] array)
    {
        Console.WriteLine("Sorting with Bubble Sort");
    }
}

public class QuickSort : ISortStrategy
{
    public void Sort(int[] array)
    {
        Console.WriteLine("Sorting with Quick Sort");
    }
}

public class Sorter
{
    private ISortStrategy _strategy;

    public Sorter(ISortStrategy strategy) => _strategy = strategy;

    public void SetStrategy(ISortStrategy strategy) => _strategy = strategy;

    public void Sort(int[] array) => _strategy.Sort(array);
}
```

### Lambda Strategy

```csharp
public class Processor
{
    private Func<int, int> _process;

    public Processor(Func<int, int> process) => _process = process;

    public void SetStrategy(Func<int, int> process) => _process = process;

    public int Execute(int value) => _process(value);
}

var processor = new Processor(x => x * 2);
```

### With Records

```csharp
public record DiscountStrategy(Func<decimal, decimal> Calculate);
```

## Best Practices

- Keep strategy interface small
- Use delegates for simple strategies
- Consider dependency injection for strategy resolution
- Make strategies stateless when possible
- Document strategy selection criteria

## Interview Questions

1. How does Strategy differ from State?
2. When should you use delegates over interfaces?
3. Can strategies have state?
4. How do you handle strategy selection?
5. When is Strategy better than inheritance?

## References

- Microsoft Docs: Strategy Pattern
- "Design Patterns" by Gamma et al.
- Functional Programming in C# by Enrico Buonanno
