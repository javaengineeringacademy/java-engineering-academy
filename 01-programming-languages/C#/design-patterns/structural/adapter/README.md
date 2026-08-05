# Adapter Pattern (C#)

## Overview

The Adapter pattern converts the interface of a class into another interface clients
expect. C# supports both class adapters (inheritance) and object adapters (composition).

## When to Use

- Integrating third-party libraries with incompatible interfaces
- Reusing existing classes that don't have compatible interfaces
- Creating reusable classes that cooperate with unrelated classes
- Building legacy system integration layers

## C# Implementation

### Object Adapter (Composition)

```csharp
public interface ITarget
{
    string GetRequest();
}

public class Adaptee
{
    public string GetSpecificRequest() => "Specific request";
}

public class Adapter : ITarget
{
    private readonly Adaptee _adaptee;

    public Adapter(Adaptee adaptee)
    {
        _adaptee = adaptee;
    }

    public string GetRequest()
    {
        return _adaptee.GetSpecificRequest();
    }
}
```

### Class Adapter (Inheritance)

```csharp
public class ClassAdapter : Adaptee, ITarget
{
    public string GetRequest()
    {
        return GetSpecificRequest();
    }
}
```

### Interface Adapter

```csharp
public interface ILegacyService
{
    void LegacyMethod();
}

public interface INewService
{
    void NewMethod();
}

public class ServiceAdapter : INewService
{
    private readonly ILegacyService _legacy;

    public ServiceAdapter(ILegacyService legacy) => _legacy = legacy;

    public void NewMethod() => _legacy.LegacyMethod();
}
```

## Best Practices

- Prefer composition over inheritance
- Keep adapters simple and focused
- Document interface differences
- Consider using dependency injection
- Test adapter behavior thoroughly

## Interview Questions

1. What is the difference between class and object adapters?
2. When should you use Adapter vs Facade?
3. How does Adapter relate to Interface Segregation?
4. Can you use adapters for data format conversion?
5. How do you handle multiple interface adaptations?

## References

- Microsoft Docs: Adapter Pattern
- "Design Patterns" by Gamma et al.
- "Head First Design Patterns" by Freeman
