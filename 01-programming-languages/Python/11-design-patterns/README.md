# Pythonic Design Patterns

## Why Design Patterns Matter

Every growing codebase faces recurring design problems — how to create objects efficiently, how to extend behavior without modifying existing code, how to coordinate complex workflows. Design patterns provide proven solutions to these problems, and Python's dynamic nature makes many patterns simpler or unnecessary compared to statically-typed languages. Without patterns, you'd reinvent solutions to problems that have already been solved.

Without design patterns, you'd write code that's hard to extend, test, and maintain as requirements evolve. That's why patterns exist — they provide a shared vocabulary for engineers, time-tested solutions to common problems, and architectural frameworks that make codebases more predictable and maintainable.

## What You'll Learn

By the end of this module, you'll be able to:

- Recognize when classic GoF patterns apply to Python problems
- Implement patterns using Python's unique features (decorators, metaclasses, duck typing)
- Choose between pattern implementations based on trade-offs
- Avoid over-engineering by knowing when patterns are unnecessary
- Communicate design decisions using a shared vocabulary

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

## Production Checklist

- [ ] Use module-level singletons instead of metaclass-based Singleton
- [ ] Prefer `@dataclass` over Builder for simple data containers
- [ ] Use `functools.singledispatch` instead of Visitor for type-based dispatch
- [ ] Apply Strategy pattern with simple callables (functions, lambdas)
- [ ] Use Context Managers (`with` statement) for resource management patterns
- [ ] Implement Observer with `events` library or custom signals, not manual subscriber lists
- [ ] Use `__init_subclass__` for Factory Method without metaclasses
- [ ] Prefer duck typing over Adapter; only adapt when integrating third-party code
- [ ] Avoid Singleton in tests; use dependency injection instead
- [ ] Document pattern rationale in code comments for team understanding

## Maturity Levels

| Level | Description |
|-------|-------------|
| **Beginner** | Recognizes common patterns (Singleton, Factory, Observer) in existing code |
| **Intermediate** | Implements patterns using Pythonic idioms (decorators, context managers, dataclasses) |
| **Advanced** | Selects appropriate patterns based on trade-offs; avoids over-engineering |
| **Expert** | Adapts patterns to Python's dynamic nature; creates novel pattern combinations |

## Common Myths

1. **"Every problem needs a design pattern"** — Patterns are tools, not mandates; over-engineering is worse
2. **"Singleton is essential for shared state"** — Module-level state is simpler and more Pythonic
3. **"Factory always needs a Factory class"** — Functions and `__init_subclass__` often suffice
4. **"Observer requires a framework"** — Simple callback lists or `events` library work fine
5. **"Strategy needs an interface"** — Python's duck typing makes any callable a strategy
6. **"Design patterns are language-agnostic"** — Python's idioms simplify or eliminate many GoF patterns

## One-Minute Revision

- **Singleton**: Use module-level state or `__new__`; metaclass approach for strict enforcement
- **Factory Method**: Functions, `__init_subclass__`, or classmethods; avoid complex factory hierarchies
- **Builder**: `@dataclass` with `__post_init__`; `NamedTuple` for immutable builders
- **Adapter**: Duck typing usually eliminates need; use when wrapping incompatible interfaces
- **Decorator**: Native `@decorator` syntax; `functools.wraps` for metadata preservation
- **Facade**: Simplify complex APIs with a single class or module-level functions
- **Strategy**: Pass callables as arguments; `functools.singledispatch` for type-based dispatch
- **Observer**: Callback lists, `events` library, or signal/slot patterns
- **Command**: Encapsulate requests as objects; use `@dataclass` for command objects
- **Iterator**: Implement `__iter__` and `__next__`; generators are built-in iterators
- **Context Manager**: `__enter__`/`__exit__`; `contextlib` for simpler patterns
- **Anti-patterns**: Don't use Singleton when module works; don't add Factory when constructor suffices
