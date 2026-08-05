# Observer Pattern (C#)

## Overview

The Observer pattern defines a one-to-many dependency between objects so that when one
object changes state, all its dependents are notified. C# provides built-in event and
delegate mechanisms for observer implementations.

## When to Use

- Changes to one object require changing others
- You don't know how many objects need to be changed
- Objects should notify observers without coupling
- Event-driven systems

## C# Implementation

### Using Events and Delegates

```csharp
public class StockMarket
{
    public event EventHandler<StockChangedEventArgs> StockChanged;

    private decimal _price;

    public decimal Price
    {
        get => _price;
        set
        {
            _price = value;
            StockChanged?.Invoke(this, new StockChangedEventArgs(value));
        }
    }
}

public class StockChangedEventArgs : EventArgs
{
    public decimal NewPrice { get; }

    public StockChangedEventArgs(decimal newPrice) => NewPrice = newPrice;
}

public class Investor
{
    public string Name { get; }

    public Investor(string name) => Name = name;

    public void OnStockChanged(object sender, StockChangedEventArgs e)
    {
        Console.WriteLine($"{Name} notified: New price is ${e.NewPrice}");
    }
}
```

### Generic Observer

```csharp
public interface IObserver<T>
{
    void OnNext(T value);
}

public interface IObservable<T>
{
    IDisposable Subscribe(IObserver<T> observer);
}
```

### Weak Event Pattern

```csharp
public class WeakEventManager<TEventArgs>
{
    private readonly List<WeakReference> _listeners = new();

    public void AddListener(EventHandler<TEventArgs> handler) =>
        _listeners.Add(new WeakReference(handler));

    public void Raise(object sender, TEventArgs e)
    {
        foreach (var weakRef in _listeners.ToArray())
        {
            if (weakRef.Target is EventHandler<TEventArgs> handler)
                handler(sender, e);
            else
                _listeners.Remove(weakRef);
        }
    }
}
```

## Best Practices

- Use weak references to prevent memory leaks
- Consider implementing IDisposable for unsubscribe
- Keep observer interface minimal
- Handle thread safety for concurrent notifications
- Document notification order guarantees

## Interview Questions

1. What is the difference between Observer and Mediator?
2. How do you prevent memory leaks in Observer?
3. What is the Weak Event pattern?
4. Can observers be notified asynchronously?
5. When should you use events vs custom observer?

## References

- Microsoft Docs: Observer Design Pattern
- "Design Patterns" by Gamma et al.
- Reactive Extensions documentation
