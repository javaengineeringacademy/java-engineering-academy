# Chain of Responsibility Pattern (C#)

## Overview

The Chain of Responsibility pattern avoids coupling the sender of a request to its
receiver by giving more than one object a chance to handle the request. C# middleware
pipeline is a prime example of this pattern.

## When to Use

- Multiple objects may handle a request
- Handler should be determined at runtime
- Request should be handled by one of multiple handlers
- Set of handlers should be specified dynamically

## C# Implementation

### Basic Chain

```csharp
public abstract class Handler
{
    protected Handler _next;

    public Handler SetNext(Handler next)
    {
        _next = next;
        return next;
    }

    public virtual void Handle(Request request)
    {
        if (_next != null)
            _next.Handle(request);
    }
}

public class ConcreteHandlerA : Handler
{
    public override void Handle(Request request)
    {
        if (CanHandle(request))
        {
            Console.WriteLine("Handler A processing");
        }
        else
        {
            base.Handle(request);
        }
    }

    private bool CanHandle(Request request) => request.Type == "A";
}
```

### Middleware Pipeline

```csharp
public class Middleware
{
    private readonly RequestDelegate _next;

    public Middleware(RequestDelegate next) => _next = next;

    public async Task InvokeAsync(HttpContext context)
    {
        Console.WriteLine("Before");
        await _next(context);
        Console.WriteLine("After");
    }
}
```

### Functional Chain

```csharp
public class RequestPipeline
{
    private readonly List<Func<Request, Task<Response>>> _handlers = new();

    public void Add(Func<Request, Task<Response>> handler) => _handlers.Add(handler);

    public async Task<Response> Process(Request request)
    {
        foreach (var handler in _handlers)
        {
            var response = await handler(request);
            if (response.Handled) return response;
        }
        return new Response { Handled = false };
    }
}
```

## Best Practices

- Keep handlers focused and small
- Define default behavior when no handler processes request
- Consider using dependency injection for handler registration
- Handle circular chains
- Document handler ordering

## Interview Questions

1. What is the difference between Chain of Responsibility and Middleware?
2. Can multiple handlers process same request?
3. How do you handle unhandled requests?
4. When should you use Chain vs Decorator?
5. How do you implement async chain of responsibility?

## References

- Microsoft Docs: Chain of Responsibility
- "Design Patterns" by Gamma et al.
- ASP.NET Core Middleware documentation
