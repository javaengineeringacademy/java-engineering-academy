## Delegates in C#

Delegates are type-safe function pointers that enable callback patterns, event handling, and functional programming in C#.

## Overview

Delegates are the foundation for events, callbacks, LINQ, and lambda expressions in C#. Understanding delegates is essential for working with modern .NET APIs.

## Why It Matters

- Enable callback and strategy patterns
- Foundation for event systems
- Used extensively in LINQ and async APIs
- Enable functional programming patterns
- Essential for understanding lambda expressions

## Key Concepts

- **Delegate**: A type that represents a method reference
- **Func<T, TResult>**: Built-in delegate that returns a value
- **Action<T>**: Built-in delegate with no return value
- **Predicate<T>**: Built-in delegate returning bool
- **Multicast Delegate**: Delegate that invokes multiple methods
- **Anonymous Methods**: Inline delegate implementations
- **Lambda Expressions**: Concise delegate syntax

## Core Topics

- Delegate declaration and invocation
- Built-in delegate types (Func, Action, Predicate)
- Lambda expressions and closures
- Multicast delegates and invocation lists
- Covariance and contravariance in delegates
- Delegate vs interface for strategy patterns
- Expression trees for translatable delegates
- Performance considerations (delegate allocation, caching)

## Best Practices

- Prefer Func/Action over custom delegate types
- Cache delegates to avoid allocations
- Use Expression<Func<T>> when you need to inspect the delegate
- Be aware of closure capture and memory implications
- Prefer interfaces for strategies that are called frequently

## Hands-on Labs

- Implement a plugin system using delegates
- Build a middleware pipeline with Action delegates
- Compare delegate vs interface performance
- Create a fluent API using Func delegates
- Implement a custom Func-like delegate with Expression support

## Interview Questions

1. What is the difference between Func, Action, and Predicate?
2. How do multicast delegates work?
3. What are closures and how do they relate to delegates?
4. When should you use Expression trees instead of delegates?
5. How do covariance and contravariance affect delegates?

## References

- https://learn.microsoft.com/dotnet/csharp/programming-guide/delegates/
- https://learn.microsoft.com/dotnet/api/system.func-2
- https://learn.microsoft.com/dotnet/csharp/language-reference/operators/lambda-expressions
