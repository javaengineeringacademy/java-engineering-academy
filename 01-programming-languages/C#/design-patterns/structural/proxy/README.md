# Proxy Pattern (C#)

## Overview

The Proxy pattern provides a surrogate or placeholder for another object to control
access to it. C# supports various proxy types including virtual, protection, and
remote proxies.

## When to Use

- Lazy initialization of expensive objects
- Access control to original object
- Logging and monitoring
- Caching remote requests
- Creating reference counting proxies

## C# Implementation

### Virtual Proxy (Lazy Loading)

```csharp
public interface IImage
{
    void Display();
}

public class RealImage : IImage
{
    private readonly string _filename;

    public RealImage(string filename)
    {
        _filename = filename;
        LoadFromDisk();
    }

    private void LoadFromDisk() => Console.WriteLine($"Loading {_filename}");

    public void Display() => Console.WriteLine($"Displaying {_filename}");
}

public class ImageProxy : IImage
{
    private RealImage _image;
    private readonly string _filename;

    public ImageProxy(string filename) => _filename = filename;

    public void Display()
    {
        _image ??= new RealImage(_filename);
        _image.Display();
    }
}
```

### Protection Proxy

```csharp
public interface IDocument
{
    void Read();
    void Write(string content);
}

public class Document : IDocument
{
    public void Read() => Console.WriteLine("Reading document");
    public void Write(string content) => Console.WriteLine($"Writing: {content}");
}

public class DocumentProtectionProxy : IDocument
{
    private readonly Document _document;
    private readonly string _userRole;

    public DocumentProtectionProxy(string userRole)
    {
        _userRole = userRole;
        _document = new Document();
    }

    public void Read() => _document.Read();

    public void Write(string content)
    {
        if (_userRole == "Admin")
            _document.Write(content);
        else
            Console.WriteLine("Access denied");
    }
}
```

## Best Practices

- Keep proxy interface identical to real subject
- Consider using Castle DynamicProxy for AOP
- Use lazy proxy for expensive resource initialization
- Document proxy behavior differences
- Consider virtual proxy vs lazy initialization trade-offs

## Interview Questions

1. What are the different types of proxies?
2. How does virtual proxy differ from lazy initialization?
3. Can proxies add behavior transparently?
4. How do you handle proxy for async operations?
5. When should you use Proxy vs Decorator?

## References

- Microsoft Docs: Proxy Pattern
- "Design Patterns" by Gamma et al.
- Castle DynamicProxy documentation
