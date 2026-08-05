## Common Language Specification (CLS)

The CLS is a set of rules that language compilers must follow to ensure interoperability across .NET languages.

## Overview

The CLS defines a subset of the CTS that all .NET languages should support, enabling code written in one language to be consumed by another. It ensures cross-language compatibility.

## Why It Matters

- Enables code reuse across .NET languages
- Ensures library compatibility between C#, F#, VB.NET
- Defines public API surface rules
- Required for building language-agnostic libraries

## Key Concepts

- **CLS Compliance**: Meeting the minimum requirements for interoperability
- **CLS Tragets**: Using CLSCompliant attribute to verify compliance
- **Naming Conventions**: Rules for member naming across languages
- **Type Rules**: Restrictions on types used in public APIs
- **Access Modifiers**: Visibility rules for CLS-compliant assemblies

## Core Topics

- CLS rules and restrictions
- CLSCompliant attribute usage
- Naming conventions for cross-language interop
- Type restrictions for public APIs
- Testing CLS compliance with tools
- Common CLS violations and fixes

## Best Practices

- Mark public libraries with [assembly: CLSCompliant(true)]
- Use CLS-compliant types in public APIs
- Avoid unsigned types in public interfaces
- Follow naming conventions for cross-language consumers

## Hands-on Labs

- Audit a library for CLS compliance
- Fix common CLS violations
- Build a CLS-compliant NuGet package
- Test interop between C# and F#

## Interview Questions

1. What is the difference between CTS and CLS?
2. Why should libraries be CLS-compliant?
3. What are common CLS compliance issues?

## References

- https://learn.microsoft.com/dotnet/standard/language-independent-and-language-independent-components/
- https://learn.microsoft.com/dotnet/api/system.clscompliantattribute
