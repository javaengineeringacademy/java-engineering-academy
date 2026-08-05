# Pythonic Design Patterns

Python's dynamic nature and first-class functions enable unique implementations of classic GoF patterns. Many traditional patterns become simpler or unnecessary in Python.

## Pattern Categories

### Creational Patterns
- **Singleton** - Single instance enforcement (metaclass, decorator, or module)
- **Factory Method** - Object creation via functions or `__init_subclass__`
- **Abstract Factory** - Families of related objects
- **Builder** - Step-by-step complex object construction
- **Prototype** - Copying existing objects via `copy` module

### Structural Patterns
- **Adapter** - Interface compatibility using duck typing
- **Decorator** - Behavior extension (function decorators and `@decorator` syntax)
- **Facade** - Simplified complex subsystem interfaces
- **Proxy** - Placeholder and access control
- **Composite** - Tree structures via duck typing
- **Bridge** - Abstraction-implementation separation
- **Flyweight** - Memory-efficient shared objects

### Behavioral Patterns
- **Observer** - Event notification system
- **Strategy** - Algorithm selection at runtime
- **Command** - Encapsulated requests as objects
- **Iterator** - Python protocols (`__iter__`, `__next__`)
- **State** - State-driven behavior changes
- **Template Method** - Algorithm skeletons with hook methods
- **Chain of Responsibility** - Sequential request handling
- **Mediator** - Centralized object communication
- **Memento** - State capture and restoration
- **Visitor** - Operation execution without modifying classes
- **Interpreter** - Language grammar representation

## Python Advantages

- **Duck Typing** - Many structural patterns become implicit
- **First-Class Functions** - Strategies, Commands often are just callables
- **Decorators** - Native syntax for Decorator pattern
- **Generators** - Built-in Iterator pattern
- **Context Managers** - Resource management patterns
- **`__new__` and Metaclasses** - Singleton and creation control

## When to Use Patterns

- Solving recurring design problems
- Improving code maintainability and testability
- Communicating design decisions to team members
- Avoiding anti-patterns and code smells

## Anti-Patterns to Avoid

- Over-engineering with unnecessary patterns
- Using Singleton when module-level state suffices
- Implementing Visitor when `functools.singledispatch` works
- Adding Factory when constructor parameters suffice

## References

- Gamma, Helm, Johnson, Vlissides - *Design Patterns: Elements of Reusable Object-Oriented Software*
- Alex Martelli - *Python Cookbook* (O'Reilly)
- Brandon Rhodes - *Python Design Patterns* (PyCon talks)
- Head First Design Patterns (2nd Edition)
