# Design Patterns

> A comprehensive collection of the 23 Gang of Four (GoF) design patterns with Java implementations.

## Why Design Patterns?

Design patterns are reusable solutions to common problems in software design. They provide:
- **Proven solutions** — battle-tested approaches to recurring problems
- **Common vocabulary** — shared language for developers
- **Best practices** — established conventions for maintainable code
- **Interview preparation** — frequently asked in technical interviews

## Pattern Categories

### Creational Patterns (5)
| Pattern | Description |
|---------|-------------|
| [Abstract Factory](creational/abstract-factory/) | Create families of related objects |
| [Builder](creational/builder/) | Construct complex objects step by step |
| [Factory Method](creational/factory/) | Create objects without specifying exact class |
| [Prototype](creational/prototype/) | Create objects by cloning existing instances |
| [Singleton](creational/singleton/) | Ensure a class has only one instance |

### Structural Patterns (7)
| Pattern | Description |
|---------|-------------|
| [Adapter](structural/adapter/) | Convert one interface to another |
| [Bridge](structural/bridge/) | Separate abstraction from implementation |
| [Composite](structural/composite/) | Treat individual and composite objects uniformly |
| [Decorator](structural/decorator/) | Add responsibilities dynamically |
| [Facade](structural/facade/) | Provide a simplified interface |
| [Flyweight](structural/flyweight/) | Share common state efficiently |
| [Proxy](structural/proxy/) | Control access to an object |

### Behavioral Patterns (11)
| Pattern | Description |
|---------|-------------|
| [Chain of Responsibility](behavioral/chain/) | Pass requests along a chain of handlers |
| [Command](behavioral/command/) | Encapsulate requests as objects |
| [Interpreter](behavioral/interpreter/) | Define grammar and interprete sentences |
| [Iterator](behavioral/iterator/) | Access elements sequentially |
| [Mediator](behavioral/mediator/) | Reduce coupling between components |
| [Memento](behavioral/memento/) | Capture and restore state |
| [Observer](behavioral/observer/) | Define dependency between objects |
| [State](behavioral/state/) | Change behavior when state changes |
| [Strategy](behavioral/strategy/) | Define interchangeable algorithms |
| [Template Method](behavioral/template-method/) | Define algorithm skeleton |
| [Visitor](behavioral/visitor/) | Define new operations on elements |

## Pattern Selection Guide

```
┌─────────────────────────────────────┐
│        Which Pattern to Use?        │
├─────────────────────────────────────┤
│ Need to create objects?             │
│  → Creational Patterns              │
│                                     │
│ Need to compose classes?            │
│  → Structural Patterns              │
│                                     │
│ Need to manage behavior?            │
│  → Behavioral Patterns              │
└─────────────────────────────────────┘
```

## Resources

- [Refactoring.Guru](https://refactoring.guru/design-patterns)
- [Baeldung Design Patterns](https://www.baeldung.com/java-design-patterns)
- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/9781492077992/)
- [Design Patterns: Elements of Reusable Object-Oriented Software](https://www.oreilly.com/library/view/design-patterns-elements/9780201633610/)
