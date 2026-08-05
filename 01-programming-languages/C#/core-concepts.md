# C# Core Concepts

## Object-Oriented Programming

### Four Pillars

```csharp
// Encapsulation - hiding internal state
public class BankAccount
{
    private decimal _balance;
    public decimal Balance => _balance;
    public void Deposit(decimal amount) => _balance += amount;
}

// Inheritance - deriving from base classes
public class SavingsAccount : BankAccount
{
    public decimal InterestRate { get; set; }
}

// Polymorphism - same interface, different behavior
public abstract class Shape
{
    public abstract double Area();
}

public class Circle : Shape
{
    public double Radius { get; set; }
    public override double Area() => Math.PI * Radius * Radius;
}

// Abstraction - hiding complexity
public interface IRepository<T>
{
    Task<T?> GetByIdAsync(int id);
    Task<IEnumerable<T>> GetAllAsync();
}
```

## Generics

```csharp
// Type-safe, reusable components
public class Stack<T>
{
    private readonly List<T> _items = new();
    public void Push(T item) => _items.Add(item);
    public T Pop() => _items[^1];
}

// Constraints
public T Max<T>(T a, T b) where T : IComparable<T>
{
    return a.CompareTo(b) > 0 ? a : b;
}
```

## LINQ (Language Integrated Query)

```csharp
var results = products
    .Where(p => p.Price > 10)
    .OrderBy(p => p.Name)
    .Select(p => new { p.Name, p.Price });

// Method syntax vs query syntax
var query = from p in products
            where p.Price > 10
            orderby p.Name
            select p;
```

## Async/Await

```csharp
public async Task<User?> GetUserAsync(int id)
{
    using var client = new HttpClient();
    var response = await client.GetAsync($"/api/users/{id}");
    response.EnsureSuccessStatusCode();
    return await response.Content.ReadFromJsonAsync<User>();
}

// ValueTask for hot paths
public ValueTask<int> GetCachedValueAsync(int key)
{
    if (_cache.TryGetValue(key, out int value))
        return new ValueTask<int>(value);
    return new ValueTask<int>(ComputeValueAsync(key));
}
```

## Delegates and Events

```csharp
// Delegates - function pointers
public delegate void NotifyHandler(string message);

// Events
public class OrderProcessor
{
    public event EventHandler<OrderEventArgs>? OrderProcessed;
    protected virtual void OnOrderProcessed(OrderEventArgs e)
    {
        OrderProcessed?.Invoke(this, e);
    }
}

// Func and Action
Func<int, int, int> add = (a, b) => a + b;
Action<string> log = message => Console.WriteLine(message);
```

## Value Types vs Reference Types

| Value Types | Reference Types |
|-------------|-----------------|
| struct, enum | class, interface, delegate |
| Stored on stack | Stored on heap |
| Copy by value | Copy by reference |
| No null (unless nullable) | Nullable by default |

## Pattern Matching

```csharp
public string Classify(object obj) => obj switch
{
    int i when i > 0 => "positive integer",
    int => "non-positive integer",
    string s when s.Length > 0 => "non-empty string",
    null => "null",
    _ => "other"
};
```
