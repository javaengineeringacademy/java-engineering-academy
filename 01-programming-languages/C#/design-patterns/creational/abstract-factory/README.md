# Abstract Factory Pattern (C#)

## Overview

The Abstract Factory pattern provides an interface for creating families of related
or dependent objects without specifying their concrete classes. C# leverages interfaces
and dependency injection for clean implementations.

## When to Use

- System needs to be independent of object creation
- You need to enforce constraints about which objects are used together
- Working with multiple families of products
- Creating UI themes or platform-specific components

## C# Implementation

### Classic Abstract Factory

```csharp
public interface IButton { void Render(); }
public interface ITextBox { void Display(); }

public class WindowsButton : IButton
{
    public void Render() => Console.WriteLine("Windows Button");
}

public class WindowsTextBox : ITextBox
{
    public void Display() => Console.WriteLine("Windows TextBox");
}

public class MacOSButton : IButton
{
    public void Render() => Console.WriteLine("MacOS Button");
}

public interface IGUIFactory
{
    IButton CreateButton();
    ITextBox CreateTextBox();
}

public class WindowsFactory : IGUIFactory
{
    public IButton CreateButton() => new WindowsButton();
    public ITextBox CreateTextBox() => new WindowsTextBox();
}
```

### With Dependency Injection

```csharp
services.AddSingleton<IGUIFactory, WindowsFactory>();
```

## Best Practices

- Define factory interfaces clearly
- Use dependency injection for factory resolution
- Keep product families consistent
- Document relationships between products
- Consider using abstract base classes for related products

## Interview Questions

1. How does Abstract Factory differ from Factory Method?
2. What are product families?
3. How do you extend an Abstract Factory with new products?
4. Can Abstract Factory work with dependency injection?
5. When would you choose Abstract Factory over Factory Method?

## References

- Microsoft Docs: Abstract Factory Pattern
- "Design Patterns" by Gamma et al.
- "Dependency Injection in .NET" by Mark Seemann
