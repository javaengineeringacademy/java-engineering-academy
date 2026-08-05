# Composite Pattern (C#)

## Overview

The Composite pattern lets you compose objects into tree structures to represent
part-whole hierarchies. C# interfaces enable uniform treatment of individual and
composite objects.

## When to Use

- Representing part-whole hierarchies
- Treating individual and composite objects uniformly
- Building tree structures like menus or file systems
- Creating UI component hierarchies

## C# Implementation

### Basic Composite

```csharp
public interface IComponent
{
    string Name { get; }
    decimal Price { get; }
    void Display(int depth = 0);
}

public class Product : IComponent
{
    public string Name { get; }
    public decimal Price { get; }

    public Product(string name, decimal price)
    {
        Name = name;
        Price = price;
    }

    public void Display(int depth = 0)
    {
        Console.WriteLine(new string('-', depth) + $" {Name}: ${Price}");
    }
}

public class Box : IComponent
{
    private readonly List<IComponent> _children = new();

    public string Name { get; }
    public decimal Price => _children.Sum(c => c.Price);

    public Box(string name) => Name = name;

    public void Add(IComponent component) => _children.Add(component);
    public void Remove(IComponent component) => _children.Remove(component);

    public void Display(int depth = 0)
    {
        Console.WriteLine(new string('-', depth) + $" +{Name} (${Price})");
        foreach (var child in _children)
            child.Display(depth + 2);
    }
}
```

### With Generic Support

```csharp
public interface IComponent<T>
{
    T Value { get; }
    IEnumerable<IComponent<T>> Children { get; }
}
```

## Best Practices

- Define uniform interface for all components
- Consider making leaf operations no-ops
- Use strongly typed collections
- Implement composite traversal patterns
- Consider visitor for complex operations

## Interview Questions

1. How does Composite differ fromDecorator?
2. What is the transparency principle in Composite?
3. Can composite operations fail on leaves?
4. How do you traverse a composite tree?
5. When should you avoid using Composite?

## References

- Microsoft Docs: Composite Pattern
- "Design Patterns" by Gamma et al.
- "Head First Design Patterns" by Freeman
