# 03 - Software Design

A comprehensive guide to software design principles, patterns, and architecture styles.

## Table of Contents

### Object-Oriented Programming (OOP)

| Topic | Description |
|-------|-------------|
| [Classes](oop/classes/README.md) | Classes, objects, constructors, fields, methods, access modifiers, static members |
| [Inheritance](oop/inheritance/README.md) | extends, super, method overriding, covariant returns, abstract classes |
| [Polymorphism](oop/polymorphism/README.md) | Compile-time (overloading), runtime (overriding), dynamic dispatch, pattern matching |
| [Encapsulation](oop/encapsulation/README.md) | Private fields, getters/setters, validation, immutable objects |
| [Abstraction](oop/abstraction/README.md) | Abstract classes, interfaces, default methods, functional interfaces |
| [Composition](oop/composition/README.md) | Composition over inheritance, delegation, has-a vs is-a |

### Functional Programming

| Topic | Description |
|-------|-------------|
| [Lambdas](functional-programming/lambdas/README.md) | Lambda syntax, closures, variable capture, scoping |
| [Higher-Order Functions](functional-programming/higher-order/README.md) | Functions as values, callbacks, combinators |
| [Immutability](functional-programming/immutability/README.md) | Immutable objects, records, unmodifiable collections |
| [Pure Functions](functional-programming/pure-functions/README.md) | No side effects, referential transparency, testing benefits |
| [Monads](functional-programming/monads/README.md) | Optional, Either, Try, composition, chaining |

### Design Patterns - Creational

| Topic | Description |
|-------|-------------|
| [Factory Method](design-patterns/creational/factory/README.md) | Factory method, abstract factory, when to use |
| [Builder](design-patterns/creational/builder/README.md) | Builder pattern, fluent API, telescoping constructor |
| [Singleton](design-patterns/creational/singleton/README.md) | Singleton, thread safety, enum singleton, DI alternative |
| [Prototype](design-patterns/creational/prototype/README.md) | Cloneable, deep vs shallow copy, copy constructor |
| [Abstract Factory](design-patterns/creational/abstract-factory/README.md) | Abstract factory, product families, platform independence |

### Design Patterns - Structural

| Topic | Description |
|-------|-------------|
| [Adapter](design-patterns/structural/adapter/README.md) | Adapter pattern, interface conversion, legacy integration |
| [Bridge](design-patterns/structural/bridge/README.md) | Bridge pattern, abstraction/implementation separation |
| [Composite](design-patterns/structural/composite/README.md) | Composite pattern, tree structures, uniform interface |
| [Decorator](design-patterns/structural/decorator/README.md) | Decorator pattern, dynamic behavior, I/O streams |
| [Facade](design-patterns/structural/facade/README.md) | Facade pattern, simplifying complex subsystems |
| [Flyweight](design-patterns/structural/flyweight/README.md) | Flyweight pattern, shared objects, memory optimization |
| [Proxy](design-patterns/structural/proxy/README.md) | Proxy pattern, lazy loading, access control, caching |

### Design Patterns - Behavioral

| Topic | Description |
|-------|-------------|
| [Chain of Responsibility](design-patterns/behavioral/chain/README.md) | Chain of Responsibility, pipeline, middleware |
| [Command](design-patterns/behavioral/command/README.md) | Command pattern, undo/redo, queuing |
| [Iterator](design-patterns/behavioral/iterator/README.md) | Iterator pattern, traversal, Iterable interface |
| [Mediator](design-patterns/behavioral/mediator/README.md) | Mediator pattern, reducing coupling, chat rooms |
| [Memento](design-patterns/behavioral/memento/README.md) | Memento pattern, state capture, undo |
| [Observer](design-patterns/behavioral/observer/README.md) | Observer pattern, pub/sub, event systems |
| [State](design-patterns/behavioral/state/README.md) | State pattern, finite state machines, TCP connections |
| [Strategy](design-patterns/behavioral/strategy/README.md) | Strategy pattern, algorithm selection, comparators |
| [Template Method](design-patterns/behavioral/template/README.md) | Template method, skeleton algorithms, hooks |
| [Visitor](design-patterns/behavioral/visitor/README.md) | Visitor pattern, double dispatch, AST traversal |

### Architecture Styles

| Topic | Description |
|-------|-------------|
| [Layered Architecture](architecture-styles/layered/README.md) | Presentation/business/data layers |
| [N-Tier Architecture](architecture-styles/n-tier/README.md) | Physical separation of tiers |
| [Client-Server](../README.md) | Client-server model, request-response |
| [Service-Oriented Architecture](../README.md) | SOA, ESB, contracts |
| [Microservices](architecture-styles/microservices/README.md) | Bounded contexts, decomposition, independence |
| [Modular Monolith](architecture-styles/modular-monolith/README.md) | Modules, boundaries within a single deployable |
| [Hexagonal Architecture](architecture-styles/hexagonal/README.md) | Ports and adapters, pluggable infrastructure |
| [Onion Architecture](../README.md) | Concentric layers, dependency inversion |
| [Clean Architecture](architecture-styles/clean/README.md) | Entities, use cases, adapters, frameworks |
| [Event-Driven Architecture](architecture-styles/event-driven/README.md) | Events, event sourcing, CQRS |
| [Serverless Architecture](architecture-styles/serverless/README.md) | FaaS, BaaS, event triggers |

## Design Principles

### SOLID Principles

1. **S**ingle Responsibility - A class should have one reason to change
2. **O**pen/Closed - Open for extension, closed for modification
3. **L**iskov Substitution - Subtypes must be substitutable for base types
4. **I**nterface Segregation - Many specific interfaces over general-purpose ones
5. **D**ependency Inversion - Depend on abstractions, not concretions

### Additional Principles

- **DRY** - Don't Repeat Yourself
- **KISS** - Keep It Simple, Stupid
- **YAGNI** - You Aren't Gonna Need It
- **SoC** - Separation of Concerns
- **LoD** - Law of Demeter (Principle of Least Knowledge)
- **Composition Over Inheritance**

## How to Use This Guide

1. Start with OOP fundamentals for object-oriented design foundations
2. Explore functional programming for alternative approachs
3. Study design patterns for reusable solutions to common problems
4. Review architecture styles for system-level design decisions

Each topic includes:
- **Concept** - What it is and why it matters
- **Implementation** - Code examples with explanations
- **When to Use** - Appropriate use cases
- **Best Practices** - Guidelines and anti-patterns
- **Key Takeaways** - Quick reference summary
