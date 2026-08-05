# Bridge Pattern (C#)

## Overview

The Bridge pattern decouples an abstraction from its implementation so that the two
can vary independently. C# uses interfaces and composition to achieve this separation.

## When to Use

- Avoiding permanent binding between abstraction and implementation
- Both abstraction and implementation should be extensible
- Changes in implementation should not affect clients
- Sharing implementation across multiple objects

## C# Implementation

### Basic Bridge

```csharp
public interface IImplementor
{
    string OperationImpl();
}

public class ConcreteImplementorA : IImplementor
{
    public string OperationImpl() => "ConcreteImplementorA";
}

public class ConcreteImplementorB : IImplementor
{
    public string OperationImpl() => "ConcreteImplementorB";
}

public abstract class Abstraction
{
    protected IImplementor _implementor;

    protected Abstraction(IImplementor implementor)
    {
        _implementor = implementor;
    }

    public abstract string Operation();
}

public class RefinedAbstraction : Abstraction
{
    public RefinedAbstraction(IImplementor implementor) : base(implementor) { }

    public override string Operation()
    {
        return $"Refined: {_implementor.OperationImpl()}";
    }
}
```

### With Generics

```csharp
public abstract class Abstraction<T> where T : IImplementor
{
    protected readonly T _implementor;

    protected Abstraction(T implementor)
    {
        _implementor = implementor;
    }

    public abstract string Operation();
}
```

## Best Practices

- Keep abstraction and implementation hierarchies separate
- Use interfaces for implementors
- Consider using dependency injection
- Document extension points clearly
- Use Bridge when inheritance hierarchy grows

## Interview Questions

1. How does Bridge differ from Adapter?
2. What is the relationship between Bridge and Strategy?
3. When should you use Bridge over multiple inheritance?
4. How do you extend implementation without changing abstraction?
5. Can Bridge be combined with Abstract Factory?

## References

- Microsoft Docs: Bridge Pattern
- "Design Patterns" by Gamma et al.
- "Pattern-Oriented Software Architecture" by Buschmann
