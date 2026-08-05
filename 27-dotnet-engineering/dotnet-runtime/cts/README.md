## Common Type System (CTS)

The CTS defines how types are declared, used, and managed in the .NET runtime, ensuring type safety across languages.

## Overview

The Common Type System establishes the rules that all .NET languages must follow, enabling cross-language inheritance and type safety. It defines how types are classified, stored, and interact.

## Why It Matters

- Enables cross-language inheritance and interop
- Ensures type safety at runtime
- Defines value type vs reference type semantics
- Foundation for boxing and unboxing
- Required for understanding .NET type behavior

## Key Concepts

- **Type Classification**: Value types, reference types, pointer types
- **Type Members**: Fields, methods, properties, events, constructors
- **Type Visibility**: public, internal, assembly-level access
- **Type Inheritance**: Single implementation inheritance, multiple interface inheritance
- **Type Identity**: Fully qualified name, assembly, version
- **Boxing/Unboxing**: Converting between value and reference types

## Core Topics

- CTS type hierarchy and classifications
- Value types vs reference types semantics
- Type visibility and accessibility rules
- Generic type constraints and variance rules
- Boxing and unboxing behavior
- Type identity and equality
- Nullable type handling across languages

## Best Practices

- Understand boxing implications for value types
- Use interfaces for cross-language contracts
- Apply appropriate accessibility modifiers
- Be aware of CTS rules when designing public APIs

## Hands-on Labs

- Explore type metadata with System.Reflection
- Compare type behavior across C# and F#
- Analyze boxing with performance profiler
- Build a type-safe generic container

## Interview Questions

1. What is the Common Type System and why does it exist?
2. How does boxing affect performance?
3. What rules does the CTS enforce for type safety?
4. How do value types and reference types differ in the CTS?

## References

- https://learn.microsoft.com/dotnet/standard/common-type-system/
- https://learn.microsoft.com/dotnet/csharp/language-reference/builtin-types/
- https://learn.microsoft.com/dotnet/standard/base-types/
