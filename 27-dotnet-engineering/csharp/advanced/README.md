## Advanced C# Features

Modern C# features including pattern matching, spans, records, top-level statements, and other recent additions.

## Overview

Recent C# versions (9-12) introduced features that simplify code, improve performance, and enable more expressive programming patterns. These features are essential for modern .NET development.

## Why It Matters

- Reduce boilerplate code significantly
- Improve performance with zero-allocation patterns
- Enable more expressive and readable code
- Align with modern language design principles
- Required for working with latest .NET APIs

## Key Concepts

- **Pattern Matching**: Type, property, relational, logical patterns
- **Records**: Immutable reference types with value equality
- **Record Structs**: Immutable value types
- **Top-Level Statements**: Simplified program entry points
- **Primary Constructors**: Constructor parameters on class/struct declarations
- **Collection Expressions**: `[1, 2, 3]` syntax for collections
- **Required Members**: Compile-time enforcement of initialization
- **Raw String Literals**: Multi-line strings without escaping
- **Span and Memory**: Zero-copy slicing and array segments

## Core Topics

- Pattern matching with `is`, `switch`, and `when` clauses
- Record types and positional syntax
- Top-level statements and minimal programs
- Primary constructors for classes and structs
- Collection expressions and spread operators
- Required properties and members
- Global using directives
- File-scoped namespaces
- Raw string literals and string interpolation

## Best Practices

- Use records for DTOs and immutable data
- Apply pattern matching instead of complex conditional logic
- Use top-level statements for simple tools and scripts
- Leverage required members for valid object construction
- Use collection expressions for cleaner initialization
- Apply primary constructors to reduce ceremony

## Hands-on Labs

- Refactor a class hierarchy using pattern matching
- Convert DTOs to record types
- Build a CLI tool with top-level statements
- Use primary constructors in a service class
- Demonstrate collection expressions vs traditional initialization
- Build a performance-critical path using Span<T>

## Interview Questions

1. What are the advantages of record types over classes?
2. Explain pattern matching types in C#.
3. What are primary constructors and how do they differ from traditional constructors?
4. How do collection expressions work and when should you use them?
5. What is the purpose of required members?

## References

- https://learn.microsoft.com/dotnet/csharp/whats-new/csharp-12/
- https://learn.microsoft.com/dotnet/csharp/whats-new/csharp-11/
- https://learn.microsoft.com/dotnet/csharp/language-reference/statements/pattern-matching
