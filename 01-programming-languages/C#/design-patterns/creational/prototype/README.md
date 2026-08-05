# Prototype Pattern (C#)

## Overview

The Prototype pattern creates new objects by cloning existing instances. C# provides
ICloneable interface and memberwise cloning, though careful implementation is needed
for deep copies.

## When to Use

- Creating objects expensive to construct
- When object creation is complex
- When you need many similar objects
- Avoiding subclassing for object creation

## C# Implementation

### ICloneable Implementation

```csharp
public class Employee : ICloneable
{
    public string Name { get; set; }
    public Address Address { get; set; }

    public object Clone()
    {
        return new Employee
        {
            Name = this.Name,
            Address = (Address)this.Address.Clone()
        };
    }
}

public class Address : ICloneable
{
    public string City { get; set; }

    public object Clone()
    {
        return new Address { City = this.City };
    }
}
```

### Generic Prototype Manager

```csharp
public class PrototypeManager<T> where T : ICloneable
{
    private readonly Dictionary<string, T> _prototypes = new();

    public void Add(string key, T prototype) => _prototypes[key] = prototype;

    public T Create(string key) => (T)_prototypes[key].Clone();
}
```

### Record Types (C# 9+)

```csharp
public record Person(string Name, int Age);
```

## Best Practices

- Prefer deep copy over shallow copy
- Consider using records for immutable prototypes
- Validate cloned objects
- Use serialization for complex deep copies
- Document clone semantics (shallow vs deep)

## Interview Questions

1. What is the difference between shallow and deep copy?
2. Why is ICloneable not type-safe?
3. How do C# records help with prototyping?
4. When should you avoid using Clone?
5. How do you handle circular references in cloning?

## References

- Microsoft Docs: ICloneable Interface
- "Design Patterns" by Gamma et al.
- "C# in Depth" by Jon Skeet
