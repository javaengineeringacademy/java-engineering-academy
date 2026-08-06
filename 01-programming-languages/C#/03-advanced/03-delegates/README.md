# C# Delegates

## Overview
Delegates are type-safe function pointers.

## Custom Delegate
```csharp
public delegate int MathOperation(int a, int b);

MathOperation add = (a, b) => a + b;
```

## Built-in Delegates

### Func (returns value)
```csharp
Func<int, int, int> sum = (a, b) => a + b;
Func<string> getName = () => "Alice";
```

### Action (no return)
```csharp
Action<string> print = message => Console.WriteLine(message);
Action<int, int> log = (a, b) => Console.WriteLine($"{a}, {b}");
```

### Predicate (returns bool)
```csharp
Predicate<int> isEven = n => n % 2 == 0;
```

## Events
```csharp
public delegate void EventHandler(string message);

public class OrderService
{
    public event EventHandler OnOrderCreated;
    
    public void CreateOrder(string id)
    {
        OnOrderCreated?.Invoke($"Order {id}");
    }
}

// Subscribe
orderService.OnOrderCreated += logger.Log;
```

## Multicast Delegates
```csharp
Action<string> combined = PrintToConsole;
combined += PrintToFile;
combined("Hello"); // Calls both methods
```

## Key Takeaways
1. Use Func for functions with return values
2. Use Action for void methods
3. Use Predicate for boolean checks
4. Use events for publish-subscribe patterns