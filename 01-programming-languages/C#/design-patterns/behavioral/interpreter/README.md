# Interpreter Pattern (C#)

## Overview

The Interpreter pattern defines a grammatical representation for a language and provides
an interpreter to work with this grammar. C# expression trees and LINQ provide
built-in interpreter capabilities.

## When to Use

- Simple grammar representation needed
- Efficiency is not critical concern
- Grammar is simple and simple interpreter suffices
- Similar patterns of expressions occur frequently

## C# Implementation

### Basic Interpreter

```csharp
public interface IExpression
{
    int Interpret();
}

public class NumberExpression : IExpression
{
    private readonly int _number;

    public NumberExpression(int number) => _number = number;

    public int Interpret() => _number;
}

public class AddExpression : IExpression
{
    private readonly IExpression _left;
    private readonly IExpression _right;

    public AddExpression(IExpression left, IExpression right)
    {
        _left = left;
        _right = right;
    }

    public int Interpret() => _left.Interpret() + _right.Interpret();
}
```

### Expression Tree Interpreter

```csharp
public abstract class Expression
{
    public abstract double Evaluate(Dictionary<string, double> variables);
}

public class VariableExpression : Expression
{
    private readonly string _name;

    public VariableExpression(string name) => _name = name;

    public override double Evaluate(Dictionary<string, double> variables) =>
        variables[_name];
}

public class MultiplyExpression : Expression
{
    private readonly Expression _left;
    private readonly Expression _right;

    public MultiplyExpression(Expression left, Expression right)
    {
        _left = left;
        _right = right;
    }

    public override double Evaluate(Dictionary<string, double> variables) =>
        _left.Evaluate(variables) * _right.Evaluate(variables);
}
```

## Best Practices

- Keep grammar simple
- Consider using parser generators for complex grammars
- Use expression trees for performance
- Document grammar rules clearly
- Consider caching for repeated interpretations

## Interview Questions

1. When should you use Interpreter pattern?
2. What are expression trees in C#?
3. How does Interpreter relate to Compiler design?
4. Can Interpreter be used for SQL parsing?
5. What are alternatives to Interpreter for complex grammars?

## References

- Microsoft Docs: Expression Trees
- "Design Patterns" by Gamma et al.
- "Compilers: Principles, Techniques, and Tools"
