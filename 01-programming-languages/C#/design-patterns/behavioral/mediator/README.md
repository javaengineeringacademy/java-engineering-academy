# Mediator Pattern (C#)

## Overview

The Mediator pattern defines an object that encapsulates how a set of objects interact.
C# implementations range from simple mediator classes to libraries like MediatR for
complex applications.

## When to Use

- Set of objects communicate in well-defined but complex ways
- Reuse object is difficult due to dependencies
- Custom behavior distributed across several classes

## C# Implementation

### Basic Mediator

```csharp
public interface IMediator
{
    void Notify(object sender, string eventType);
}

public class ChatMediator : IMediator
{
    private readonly List<ChatUser> _users = new();

    public void Register(ChatUser user) => _users.Add(user);

    public void Notify(object sender, string eventType)
    {
        foreach (var user in _users)
        {
            if (user != sender)
                user.Receive(eventType);
        }
    }
}

public class ChatUser
{
    private readonly IMediator _mediator;
    public string Name { get; }

    public ChatUser(string name, IMediator mediator)
    {
        Name = name;
        _mediator = mediator;
    }

    public void Send(string message) => _mediator.Notify(this, message);

    public void Receive(string message)
    {
        Console.WriteLine($"{Name} received: {message}");
    }
}
```

### MediatR Pattern

```csharp
public record OrderCreated(int OrderId) : IRequest;

public class OrderCreatedHandler : IRequestHandler<OrderCreated>
{
    public async Task Handle(OrderCreated request, CancellationToken ct)
    {
        Console.WriteLine($"Processing order {request.OrderId}");
    }
}
```

## Best Practices

- Keep mediator focused on coordination
- Avoid putting business logic in mediator
- Use interfaces for mediator abstraction
- Consider MediatR for complex applications
- Document component communication patterns

## Interview Questions

1. How does Mediator differ from Observer?
2. What are the benefits of using MediatR?
3. Can mediator handle asynchronous communication?
4. How do you test code with mediator?
5. When should you avoid using Mediator?

## References

- Microsoft Docs: Mediator Pattern
- MediatR library documentation
- "Design Patterns" by Gamma et al.
