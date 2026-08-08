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

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Recurring design problems, improving maintainability, team communication | Simple code for simple problems |
| When NOT to use | Don't over-engineer; prefer simple solutions | Use functions for simple cases |
| Alternatives | Pythonic idioms (decorators, context managers, dataclasses) | Module-level state over Singleton |
| Production Examples | Web frameworks, ORMs, plugin systems | Simple scripts, prototypes |
| Common Mistakes | Over-using Singleton, complex Factory hierarchies | Prefer composition; use `__init_subclass__` |

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

## Production Incidents

### Incident 1: Singleton Causing Test Contamination

**Problem:** Tests passed individually but failed in suite
**Cause:** Singleton retained state between tests
**Impact:** 40% of test suite was flaky; CI/CD unreliable
**Detection:** Random test failures; state leaking between tests
**Solution:**
```python
# BAD: Singleton with mutable state
class Database(metaclass=SingletonMeta):
    def __init__(self):
        self.connection = None

# GOOD: Module-level state or dependency injection
_db_connection = None

def get_db():
    global _db_connection
    if _db_connection is None:
        _db_connection = create_connection()
    return _db_connection

# In tests: mock the module-level variable
```
**Prevention:** Prefer module-level state over Singleton; use dependency injection; reset state in test fixtures

### Incident 2: Observer Pattern Memory Leak

**Problem:** Event system memory grew unbounded over time
**Cause:** Observers not removed after objects were deleted
**Impact:** Service OOM after 48 hours; required daily restarts
**Detection:** Memory monitoring showed linear growth
**Solution:**
```python
# BAD: Strong references to observers
class EventBus:
    def __init__(self):
        self._handlers = []  # Never cleaned up!

# GOOD: Weak references
import weakref

class EventBus:
    def __init__(self):
        self._handlers = weakref.WeakSet()
```
**Prevention:** Use `weakref.WeakSet` for observer lists; implement unsubscribe; test memory behavior

### Incident 3: Factory Pattern Breaking Open/Closed Principle

**Problem:** Adding new product type required modifying factory class
**Cause:** Factory used if/elif chain; not open for extension
**Impact:** Each new type required touching 3+ files; merge conflicts
**Detection:** Code review flagged violation of OCP
**Solution:**
```python
# BAD: if/elif factory
def create_product(type):
    if type == "a":
        return ProductA()
    elif type == "b":
        return ProductB()  # Must modify!

# GOOD: Registry pattern
_registry = {}

def register_product(type):
    def decorator(cls):
        _registry[type] = cls
        return cls
    return decorator

@register_product("a")
class ProductA: pass
```
**Prevention:** Use registry pattern; apply `__init_subclass__` for auto-registration; test extensibility

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

## Related Topics

- [02-oop](../02-oop/) - OOP fundamentals
- [17-metaclasses](../17-metaclasses/) - Advanced class creation
- [18-senior](../18-senior/) - Production architecture patterns

## Interview Questions

### Q1: What is the Singleton pattern and when to avoid it?
**Answer:** Singleton ensures one instance. Avoid: makes testing hard, hides dependencies, creates global state. Use module-level instance instead.

### Q2: What is the difference between Strategy and Observer?
**Answer:** Strategy: object behavior changes at runtime. Observer: multiple objects react to state changes. Strategy is one-to-one, Observer is one-to-many.

### Q3: What is the Factory pattern and when to use it?
**Answer:** Factory creates objects without specifying exact class. Use when: object creation is complex, you need to create different types based on input.

### Q4: What is the Decorator pattern vs Python decorators?
**Answer:** Decorator pattern adds behavior to objects (OOP). Python decorators modify functions (functional). Different concepts, similar name.

### Q5: What is dependency injection?
**Answer:** Passing dependencies as parameters instead of creating them internally. Makes code testable, loosely coupled. Use constructor injection.

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Singleton retaining state between tests | Module-level state or dependency injection | Replace Singleton with module-level instance; reset in test fixtures |
| Observer pattern memory leak | `weakref.WeakSet` for observer lists | Use weak references; implement unsubscribe; test memory behavior |
| Factory pattern violating open/closed | Registry pattern with `__init_subclass__` | Use decorator-based registration; avoid if/elif chains |
| Metaclass conflict in inheritance | Check `__mro__` for metaclass compatibility | Use `__init_subclass__` instead of metaclasses |
| Strategy pattern creating too many classes | Use simple callables (functions, lambdas) | Strategies don't need classes; functions are first-class citizens |

## Code Review Checklist

- [ ] Module-level state preferred over Singleton pattern
- [ ] `@dataclass` used for simple data containers instead of Builder
- [ ] `functools.singledispatch` used instead of Visitor for type-based dispatch
- [ ] Strategy pattern implemented with simple callables, not complex class hierarchies
- [ ] Context managers used for resource management patterns
- [ ] Observer lists use `weakref.WeakSet` to prevent memory leaks
- [ ] `__init_subclass__` used for Factory Method without metaclasses

## Architecture Considerations

Design patterns provide proven solutions to recurring problems, but Python's dynamic nature simplifies many GoF patterns. Decorators, context managers, and first-class functions eliminate the need for complex class hierarchies. The key is choosing Pythonic idioms over rigid pattern implementations.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Module-level Singleton | Shared state without class overhead | Simple but hard to test |
| `functools.singledispatch` | Type-based dispatch | Pythonic but limited to single argument |
| Context Manager | Resource lifecycle | Guarantees cleanup but requires `with` blocks |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Singleton holding sensitive data | Credential exposure across requests | Use module-level state with explicit reset; never cache secrets |
| Observer pattern leaking event data | Information disclosure | Use weak references; validate event data before publishing |
| Factory creating objects from untrusted input | Object injection attacks | Validate input types; restrict factory to known types |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.7+ | `@dataclass` for data containers | Replace manual Builder pattern with `@dataclass` |
| Python 3.8+ | `functools.cached_property` | Replace manual caching in Strategy pattern |
| Python 3.12+ | `@override` decorator | Use in Template Method pattern to enforce method overrides |

## Version Validation

| Feature | Python Version | Status |
|---------|---------------|--------|
| `functools.singledispatch` | 3.4+ | Stable, type-based dispatch |
| `__init_subclass__` | 3.6+ | Stable, alternative to metaclasses |
| `@dataclass` | 3.7+ | Stable, replaces Builder for simple containers |
| `contextlib.contextmanager` | 3.2+ | Stable, resource management patterns |
