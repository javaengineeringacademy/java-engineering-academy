# Factory Method Pattern (C#)

## Overview

The Factory Method pattern defines an interface for creating an object, but lets
subclasses decide which class to instantiate. C# uses virtual methods or delegates
to implement factory behavior.

## When to Use

- When class doesn't know what objects it must create
- When subclasses specify objects to create
- When you want to localize creation logic
- Working with frameworks that need to instantiate objects

## C# Implementation

### Basic Factory Method

```csharp
public abstract class Product { }

public class ConcreteProductA : Product { }
public class ConcreteProductB : Product { }

public abstract class Creator
{
    public abstract Product FactoryMethod();
}

public class ConcreteCreatorA : Creator
{
    public override Product FactoryMethod() => new ConcreteProductA();
}

public class ConcreteCreatorB : Creator
{
    public override Product FactoryMethod() => new ConcreteProductB();
}
```

### Generic Factory

```csharp
public interface IFactory<T>
{
    T Create();
}

public class StringFactory : IFactory<string>
{
    public string Create() => string.Empty;
}
```

## Best Practices

- Use interfaces for product types
- Consider dependency injection containers
- Use generic factories for type safety
- Keep factory methods simple
- Document which products each factory creates

## Interview Questions

1. How does Factory Method differ from Abstract Factory?
2. What role does the Creator class play?
3. Can you implement Factory Method using delegates?
4. How does dependency injection relate to Factory Method?
5. When should you use a static factory method?

## References

- Microsoft Docs: Factory Pattern
- "Design Patterns" by Gamma et al.
- "C# Design Patterns" by Vaskaran Sarcar
