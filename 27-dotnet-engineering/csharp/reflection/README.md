## Reflection in C#

Reflection enables runtime type inspection, dynamic member access, and attribute-based metadata in .NET applications.

## Overview

Reflection is the ability to inspect and interact with types, methods, properties, and other metadata at runtime. It powers frameworks like ASP.NET Core, EF Core, and dependency injection containers.

## Why It Matters

- Enables framework features like dependency injection and model binding
- Powers serialization and deserialization libraries
- Used for plugin architectures and dynamic loading
- Supports attribute-based configuration patterns
- Required for understanding how most .NET frameworks work

## Key Concepts

- **Type**: The metadata representation of a class, interface, or value type
- **MethodInfo, PropertyInfo, FieldInfo**: Metadata about members
- **Assembly**: Represents a compiled .NET assembly
- **Attribute**: Declarative metadata attached to types and members
- **Dynamic**: Late-bound member access without compile-time knowledge
- **Emit**: IL code generation at runtime

## Core Topics

- Type inspection (GetType, typeof, typeof operator)
- Member discovery and invocation
- Attribute creation and reading
- Assembly loading and inspection
- Dynamic invocation with DynamicObject
- Performance considerations and alternatives
- Source generators as reflection alternatives
- Custom attribute development

## Best Practices

- Cache reflected metadata to avoid repeated lookups
- Consider source generators over reflection for performance
- Use typeof() over GetType() when the type is known at compile time
- Avoid reflection in hot paths
- Use Attributes for declarative configuration
- Prefer compile-time code generation where possible

## Hands-on Labs

- Build a simple IoC container using reflection
- Create custom attributes for validation
- Implement a plugin loader with Assembly.Load
- Compare reflection vs source generator performance
- Build a simple ORM mapping layer

## Interview Questions

1. What is the difference between `typeof()` and `GetType()`?
2. How does reflection affect performance and how can you mitigate it?
3. What are source generators and how do they replace reflection?
4. Explain how dependency injection uses reflection.
5. What are the security implications of reflection?

## References

- https://learn.microsoft.com/dotnet/framework/reflection-and-codedom/
- https://learn.microsoft.com/dotnet/api/system.reflection
- https://learn.microsoft.com/dotnet/csharp/programming-guide/concepts/reflection/
