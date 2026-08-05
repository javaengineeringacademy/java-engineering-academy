## Generics in C#

Generics allow you to write flexible, reusable code that works with any type while maintaining type safety.

## Overview

Generics are a fundamental C# feature enabling type-parameterized classes, methods, interfaces, and delegates. They eliminate the need for boxing and type casting while providing compile-time type safety.

## Why It Matters

- Eliminate boxing/unboxing overhead for value types
- Provide compile-time type safety
- Enable reusable data structures and algorithms
- Foundation for collections, LINQ, and async APIs
- Enable variance for flexible type relationships

## Key Concepts

- **Generic Types**: Classes and structs with type parameters
- **Generic Methods**: Methods with their own type parameters
- **Type Constraints**: where clauses limiting type arguments
- **Variance**: in (contravariance) and out (covariance) keywords
- **Default**: default(T) for type-agnostic default values
- **Generic Math**: Static abstract members in interfaces for math operations

## Core Topics

- Generic class and method declarations
- Type constraints (class, struct, notnull, unmanaged, new)
- Default constraint and default(T)
- Covariance (out) and contravariance (in)
- Generic interfaces and inheritance
- Open vs closed generic types
- Generic collection types (List, Dictionary, etc.)
- Generic math with INumber<T>

## Best Practices

- Use constraints to communicate type requirements
- Prefer generic methods over object parameters
- Use covariance/contravariance for flexible API design
- Consider generic math for numeric code
- Cache generic type instances for performance

## Hands-on Labs

- Build a generic repository pattern
- Create a generic fluent validation library
- Implement a type-safe event aggregator
- Use generic math for numeric algorithms
- Build a generic object pool

## Interview Questions

1. What are generic type constraints and when should you use them?
2. Explain covariance and contravariance in generics.
3. What is the difference between open and closed generic types?
4. How does generic math work in .NET 7+?
5. What performance benefits do generics provide over non-generic alternatives?

## References

- https://learn.microsoft.com/dotnet/csharp/fundamentals/types/generics/
- https://learn.microsoft.com/dotnet/csharp/language-reference/keywords/where-clause/
- https://learn.microsoft.com/dotnet/standard/generics/
