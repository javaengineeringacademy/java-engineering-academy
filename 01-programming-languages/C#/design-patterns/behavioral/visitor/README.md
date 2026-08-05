# Visitor Pattern (C#)

## Overview

The Visitor pattern lets you separate algorithms from the objects on which they operate.
C# uses interfaces and double dispatch for visitor implementations.

## When to Use

- Object structure contains classes with many interfaces
- Need to perform operations on objects without changing classes
- Operations vary across object types
- Related operations should be grouped together

## C# Implementation

### Basic Visitor

```csharp
public interface IVisitor
{
    void Visit(Book book);
    void Visit(ElectronicProduct product);
}

public interface IVisitable
{
    void Accept(IVisitor visitor);
}

public class Book : IVisitable
{
    public string Title { get; set; }
    public decimal Price { get; set; }

    public void Accept(IVisitor visitor) => visitor.Visit(this);
}

public class PriceVisitor : IVisitor
{
    public decimal Total { get; private set; }

    public void Visit(Book book) => Total += book.Price;

    public void Visit(ElectronicProduct product) => Total += product.Price * 0.9m;
}
```

### Expression Visitor

```csharp
public abstract class Expression
{
    public abstract void Accept(ExpressionVisitor visitor);
}

public class Addition : Expression
{
    public Expression Left { get; set; }
    public Expression Right { get; set; }

    public override void Accept(ExpressionVisitor visitor) =>
        visitor.VisitAddition(this);
}

public abstract class ExpressionVisitor
{
    public abstract void VisitAddition(Addition addition);
    public abstract void VisitNumber(Number number);
}
```

## Best Practices

- Keep visitor interface focused
- Consider using pattern matching in modern C#
- Document visitor responsibilities clearly
- Use visitor when operations change frequently
- Consider using interfaces for element types

## Interview Questions

1. What is double dispatch in Visitor?
2. How does Visitor violate encapsulation?
3. Can you add new elements without changing visitor?
4. When should you use Visitor vs Strategy?
5. How do you handle null elements in Visitor?

## References

- Microsoft Docs: Visitor Pattern
- "Design Patterns" by Gamma et al.
- "Refactoring to Patterns" by Kerievsky
