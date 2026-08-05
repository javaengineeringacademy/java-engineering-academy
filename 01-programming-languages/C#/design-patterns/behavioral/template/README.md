# Template Method Pattern (C#)

## Overview

The Template Method pattern defines the skeleton of an algorithm in a base class,
letting subclasses override specific steps. C# uses virtual methods and abstract
classes for template implementations.

## When to Use

- Common algorithm structure with varying implementations
- Eliminating code duplication
- Enforcing algorithm structure
- Subclass customization points

## C# Implementation

### Basic Template Method

```csharp
public abstract class DataMiner
{
    public void Mine()
    {
        OpenFile();
        ExtractData();
        ParseData();
        AnalyzeData();
        SendReport();
        CloseFile();
    }

    protected abstract void OpenFile();
    protected abstract void ExtractData();
    protected virtual void ParseData() => Console.WriteLine("Parsing data...");
    protected virtual void AnalyzeData() => Console.WriteLine("Analyzing...");
    protected abstract void SendReport();
    protected abstract void CloseFile();
}

public class CSVDataMiner : DataMiner
{
    protected override void OpenFile() => Console.WriteLine("Opening CSV");
    protected override void ExtractData() => Console.WriteLine("Extracting CSV");
    protected override void SendReport() => Console.WriteLine("Sending CSV report");
    protected override void CloseFile() => Console.WriteLine("Closing CSV");
}
```

### With Hook Methods

```csharp
public abstract class WebCrawler
{
    public void Crawl()
    {
        if (BeforeCrawl())
        {
            Connect();
            Download();
            Process();
            AfterCrawl();
        }
    }

    protected virtual bool BeforeCrawl() => true;
    protected virtual void AfterCrawl() { }

    protected abstract void Connect();
    protected abstract void Download();
    protected abstract void Process();
}
```

### Generic Template

```csharp
public abstract class Pipeline<T>
{
    public void Execute(T input)
    {
        var transformed = Transform(input);
        Process(transformed);
        Output(transformed);
    }

    protected abstract T Transform(T input);
    protected abstract void Process(T input);
    protected virtual void Output(T input) => Console.WriteLine(input);
}
```

## Best Practices

- Keep template method small
- Use hook methods for optional steps
- Document customization points
- Consider using delegates for simple templates
- Avoid calling virtual methods from constructor

## Interview Questions

1. How does Template Method differ from Strategy?
2. What are hook methods?
3. Can template methods be sealed?
4. How do you handle template method with parameters?
5. When should you use Template Method vs composition?

## References

- Microsoft Docs: Template Method Pattern
- "Design Patterns" by Gamma et al.
- "Clean Code" by Robert C. Martin
