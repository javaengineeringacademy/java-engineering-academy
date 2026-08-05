# Kotlin Design Patterns

## Overview

Design patterns provide reusable solutions to common software design problems. Kotlin's
features like data classes, sealed classes, coroutines, and extension functions enable
concise and elegant implementations of classic GoF patterns. This module covers all 23
GoF patterns plus Kotlin-specific patterns.

## Pattern Categories

### Creational Patterns
- **Singleton** - Object keyword implementation
- **Factory** - Companion object factories
- **Builder** - DSL builders
- **Prototype** - copy() function

### Structural Patterns
- **Adapter** - Interface adaptation
- **Decorator** - Delegation pattern
- **Facade** - Simplified interface
- **Proxy** - by lazy delegation
- **Composite** - Recursive structures
- **Bridge** - Abstraction decoupling
- **Flyweight** - Memory optimization

### Behavioral Patterns
- **Observer** - Flow, Observable
- **Strategy** - Lambda strategies
- **Command** - Command encapsulation
- **Iterator** - Sequences
- **State** - Sealed class states
- **Template Method** - Open functions
- **Chain of Responsibility** - Middleware pattern
- **Mediator** - Communication hub
- **Memento** - State capture
- **Visitor** - Sealed class visitor
- **Interpreter** - DSLs

### Kotlin-Specific Patterns
- **Coroutines** - Coroutine patterns
- **Sealed Classes** - Algebraic data types
- **Extension Functions** - Function patterns

## When to Use

- Building Android applications
- Creating multiplatform projects
- Implementing reactive systems
- Improving code readability
- Learning functional programming

## References

- "Kotlin in Action" by Svetlana Isakova
- Gang of Four, "Design Patterns"
- Kotlin documentation
- "Atomic Kotlin" by Bruce Eckel
