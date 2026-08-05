# Creational Design Patterns

## Overview

Creational patterns deal with object creation mechanisms, trying to create objects in a manner suitable to the situation. Go simplifies many creational patterns through first-class functions and interfaces.

## Patterns

| Pattern | Go Idiom | Key Mechanism |
|---------|----------|---------------|
| Singleton | `sync.Once` | Thread-safe lazy initialization |
| Factory | Functions | Functions returning interface implementations |
| Abstract Factory | Interfaces | Groups of related factory functions |
| Builder | Fluent API | Method chaining with struct receivers |
| Prototype | Struct cloning | Copying struct values |

## Go Advantages

- **No constructors needed**: Struct literals replace complex constructors
- **Implicit interfaces**: Factories return interfaces without explicit implementation declarations
- **Value semantics**: Prototype pattern works naturally with value types
- **`sync.Once`**: Built-in Singleton without double-checked locking

## When to Use

- **Singleton**: Shared resources (database connections, config)
- **Factory**: Multiple implementations of an interface
- **Abstract Factory**: Families of related objects
- **Builder**: Complex structs with many optional fields
- **Prototype**: Expensive object creation with similar instances

## References

- "Design Patterns" - GoF Chapter 3: Creational Patterns
- Go Blog: "Generating code"
- Go Dev: Effective Go - Composite literals
