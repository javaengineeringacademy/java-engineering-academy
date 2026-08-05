# Decorator Pattern (C#)

## Overview

The Decorator pattern attaches additional responsibilities to an object dynamically.
C# supports this through interfaces and composition, with extension methods providing
syntactic sugar.

## When to Use

- Adding responsibilities to objects dynamically
- Extending functionality without subclassing
- Supporting open/closed principle
- Creating layered or stacked behaviors

## C# Implementation

### Classic Decorator

```csharp
public interface IDataSource
{
    void WriteData(string data);
    string ReadData();
}

public class FileDataSource : IDataSource
{
    private readonly string _filename;

    public FileDataSource(string filename) => _filename = filename;

    public void WriteData(string data) => Console.WriteLine($"Writing to {_filename}");
    public string ReadData() => $"Data from {_filename}";
}

public class EncryptionDecorator : IDataSource
{
    private readonly IDataSource _dataSource;

    public EncryptionDecorator(IDataSource source) => _dataSource = source;

    public void WriteData(string data)
    {
        var encrypted = Encrypt(data);
        _dataSource.WriteData(encrypted);
    }

    public string ReadData()
    {
        var data = _dataSource.ReadData();
        return Decrypt(data);
    }

    private string Encrypt(string data) => $"encrypted({data})";
    private string Decrypt(string data) => data;
}
```

### With Extension Methods

```csharp
public static class DataSourceExtensions
{
    public static IDataSource WithEncryption(this IDataSource source) =>
        new EncryptionDecorator(source);

    public static IDataSource WithCompression(this IDataSource source) =>
        new CompressionDecorator(source);
}
```

## Best Practices

- Keep decorator interface consistent with base class
- Decorators should be transparent to clients
- Use dependency injection for decorator registration
- Consider using proxy pattern as alternative
- Avoid too many decorator layers

## Interview Questions

1. How does Decorator differ from inheritance?
2. What is the transparency principle for decorators?
3. Can you remove a decorator at runtime?
4. How do decorators compose compared to strategy?
5. When should you use Decorator vs Proxy?

## References

- Microsoft Docs: Decorator Pattern
- "Design Patterns" by Gamma et al.
- "Head First Design Patterns" by Freeman
