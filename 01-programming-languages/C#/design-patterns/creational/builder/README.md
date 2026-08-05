# Builder Pattern (C#)

## Overview

The Builder pattern separates construction of a complex object from its representation.
C# supports fluent interfaces with method chaining and immutable builders.

## When to Use

- Creating complex objects with many optional parameters
- Avoiding telescoping constructor anti-pattern
- Constructing different representations of same object
- Building composite objects step by step

## C# Implementation

### Fluent Builder

```csharp
public class HttpRequest
{
    public string Url { get; set; }
    public string Method { get; set; }
    public Dictionary<string, string> Headers { get; set; }
    public string Body { get; set; }
}

public class HttpRequestBuilder
{
    private readonly HttpRequest _request = new HttpRequest();

    public HttpRequestBuilder SetUrl(string url)
    {
        _request.Url = url;
        return this;
    }

    public HttpRequestBuilder SetMethod(string method)
    {
        _request.Method = method;
        return this;
    }

    public HttpRequestBuilder AddHeader(string key, string value)
    {
        _request.Headers ??= new Dictionary<string, string>();
        _request.Headers[key] = value;
        return this;
    }

    public HttpRequest Build() => _request;
}

var request = new HttpRequestBuilder()
    .SetUrl("https://api.example.com")
    .SetMethod("GET")
    .AddHeader("Authorization", "Bearer token")
    .Build();
```

### Immutable Builder

```csharp
public class ImmutableHttpRequest
{
    public string Url { get; }
    public string Method { get; }

    public ImmutableHttpRequest(string url, string method)
    {
        Url = url;
        Method = method;
    }
}
```

## Best Practices

- Return builder instance from setter methods
- Validate in Build method
- Consider thread safety for shared builders
- Use interfaces for product abstractions
- Separate director class for complex construction sequences

## Interview Questions

1. What problem does Builder pattern solve?
2. How does fluent interface differ from method chaining?
3. What is the Director class role?
4. Can Builder pattern be thread-safe?
5. When should you use Builder over Factory?

## References

- Microsoft Docs: Builder Pattern
- "Design Patterns" by Gamma et al.
- "Effective C#" by Bill Wagner
