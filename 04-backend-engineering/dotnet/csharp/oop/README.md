## Object-Oriented Programming in C#

C# provides full support for OOP with classes, interfaces, inheritance, polymorphism, and encapsulation.

## Overview

OOP is the approach underlying most .NET applications. C# supports single inheritance for classes, multiple interface implementation, abstract classes, virtual methods, and sealed types.

## Why It Matters

- Enables code reuse through inheritance
- Promotes loose coupling through interfaces
- Supports testability through abstraction
- Foundation for design patterns used throughout .NET

## Key Concepts

- **Classes**: Blueprints for objects with fields, properties, methods, constructors
- **Inheritance**: Base and derived classes, virtual/override/abstract/sealed
- **Polymorphism**: Method overriding, interface implementation, dynamic dispatch
- **Encapsulation**: Access modifiers (public, private, protected, internal, protected internal)
- **Interfaces**: Contract definitions, default interface methods, static abstract members
- **Abstract Classes**: Partial implementations, template method pattern
- **Record Types**: Immutable reference types with value-based equality

## Core Topics

- Class design and constructors (static, private, primary)
- Property patterns (init-only, required, computed)
- Inheritance hierarchies and virtual dispatch
- Interface default methods and static abstract members
- Abstract classes vs interfaces
- Sealed classes and finalizers
- Object initializers and collection expressions
- Deconstruction and pattern matching with types

## Best Practices

- Prefer composition over inheritance
- Use interfaces for abstraction and testability
- Make types immutable where possible
- Use `sealed` when inheritance is not needed
- Follow SOLID principles
- Use primary constructors for parameter-to-field mapping
- Implement `IDisposable` for resource management

## Hands-on Labs

- Build a class hierarchy for a shape drawing system
- Implement the Strategy pattern using interfaces
- Create an abstract base class with template method
- Use records for immutable value objects
- Implement default interface methods for backward compatibility

## Interview Questions

1. What is the difference between abstract classes and interfaces?
2. Explain the SOLID principles with C# examples.
3. When should you use virtual methods vs sealed classes?
4. What are default interface methods and when are they useful?
5. How do records differ from classes in C#?

## References

- https://learn.microsoft.com/dotnet/csharp/fundamentals/object-oriented/
- https://learn.microsoft.com/dotnet/csharp/language-reference/keywords/class
- https://learn.microsoft.com/dotnet/csharp/language-reference/keywords/interface
