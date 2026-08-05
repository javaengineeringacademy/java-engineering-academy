# C# Design Patterns

## Overview

Design patterns are reusable solutions to common software design problems. C# provides
unique features like LINQ, async/await, generics, and properties that enable elegant
implementations of classic GoF patterns. This module covers all 23 GoF patterns plus
C#-specific concurrency patterns.

## Pattern Categories

### Creational Patterns
- **Singleton** - Ensure a class has only one instance
- **Factory Method** - Create objects without specifying exact class
- **Abstract Factory** - Create families of related objects
- **Builder** - Construct complex objects step by step
- **Prototype** - Create objects by cloning existing instances

### Structural Patterns
- **Adapter** - Convert interface of existing class into another interface
- **Decorator** - Add responsibilities to objects dynamically
- **Facade** - Provide unified interface to subsystem
- **Proxy** - Provide surrogate or placeholder for another object
- **Composite** - Compose objects into tree structures
- **Bridge** - Decouple abstraction from implementation
- **Flyweight** - Support large numbers of fine-grained objects

### Behavioral Patterns
- **Observer** - Define one-to-many dependency between objects
- **Strategy** - Define family of algorithms, make them interchangeable
- **Command** - Encapsulate requests as objects
- **Iterator** - Provide way to access elements sequentially
- **State** - Allow object to alter behavior when state changes
- **Template Method** - Define algorithm skeleton, defer steps to subclasses
- **Chain of Responsibility** - Pass request along chain of handlers
- **Mediator** - Define simplified communication between classes
- **Memento** - Capture and externalize object state
- **Visitor** - Define new operation without changing classes
- **Interpreter** - Define grammar and interpreter for language

### C#-Specific Patterns
- **Async Patterns** - async/await, Task, CancellationToken patterns
- **LINQ Patterns** - Query-based data operations
- **Extension Methods** - Add methods to existing types

## When to Use

- Building enterprise applications
- Creating reusable libraries
- Implementing SOLID principles
- Improving code maintainability
- Team communication about design

## References

- Gang of Four, "Design Patterns: Elements of Reusable Object-Oriented Software"
- Microsoft .NET Design Patterns documentation
- "Head First Design Patterns" by Freeman and Robson
- "C# in Depth" by Jon Skeet
