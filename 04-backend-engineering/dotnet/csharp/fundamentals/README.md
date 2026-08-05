## C# Fundamentals

Core building blocks of the C# language including variables, types, control flow, and methods.

## Overview

C# fundamentals form the foundation for all .NET development. Understanding value types, reference types, control flow constructs, and method patterns is essential before moving to advanced topics.

## Why It Matters

- Every .NET application is built on these primitives
- Incorrect type usage leads to bugs and performance issues
- Control flow patterns affect readability and maintainability
- Method design impacts API usability and testability

## Key Concepts

- **Value Types**: int, double, bool, struct, enum, record struct
- **Reference Types**: string, class, interface, delegate, array, record
- **Type Conversion**: Implicit, explicit, boxing/unboxing, pattern-based
- **Control Flow**: if/else, switch, loops (for, while, foreach, do-while)
- **Methods**: Parameters (value, ref, in, out, params), return types, overloading
- **Variables**: Local, field, parameter, const, readonly, static
- **Null Handling**: Nullable reference types, null-conditional, null-coalescing

## Core Topics

- Value types vs reference types
- Nullable reference types and nullable value types
- String interpolation and verbatim strings
- Control flow statements and expressions
- Pattern matching in switch and if statements
- Method parameters and named arguments
- Exception handling and try-catch-finally
- Array and collection initialization

## Best Practices

- Use `var` when the type is obvious from the right side
- Prefer `string interpolation` over `string.Format`
- Use `readonly` for fields that should not change after construction
- Handle nulls with `??` and `?.` operators
- Avoid boxing value types in performance-critical code
- Use expression-bodied members for simple one-liners

## Hands-on Labs

- Build a calculator with switch expressions
- Implement a custom struct with IEquatable
- Create a method using ref return and ref locals
- Use pattern matching to classify shapes
- Handle nullable values in a data processing pipeline

## Interview Questions

1. What is the difference between a value type and a reference type?
2. How do nullable reference types work and how do you enable them?
3. What is boxing and unboxing and why should you avoid it?
4. Explain the difference between `const` and `readonly`.
5. What are expression-bodied members and when should you use them?

## References

- https://learn.microsoft.com/dotnet/csharp/language-reference/builtin-types/
- https://learn.microsoft.com/dotnet/csharp/programming-guide/strings/
- https://learn.microsoft.com/dotnet/csharp/programming-guide/statements-expressions-statements/
