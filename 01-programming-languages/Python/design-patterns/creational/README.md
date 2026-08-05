# Creational Design Patterns in Python

Creational patterns handle object creation mechanisms, aiming to create objects in a manner suitable to the situation. They help make a system independent of how its objects are created, composed, and represented.

## Patterns Overview

| Pattern | Purpose | Pythonic Implementation |
|---------|---------|------------------------|
| Singleton | Single instance | Metaclass, decorator, or module |
| Factory Method | Object creation delegation | Functions, `__init_subclass__` |
| Abstract Factory | Families of related objects | Abstract base classes, protocols |
| Builder | Complex object construction | Fluent interfaces, dataclasses |
| Prototype | Object cloning | `copy.deepcopy()` |

## Why Creational Patterns Matter

1. **Encapsulation** - Hide creation logic from clients
2. **Flexibility** - Change created types without modifying client code
3. **Reusability** - Share creation logic across application
4. **Control** - Manage object lifecycle and dependencies

## Python-Specific Approaches

### Module as Factory
```python
# config.py
def create_storage(backend: str):
    if backend == "redis":
        return RedisStorage()
    elif backend == "sql":
        return SQLStorage()
```

### Dataclass Builders
```python
from dataclasses import dataclass, field

@dataclass
class ServerConfig:
    host: str = "localhost"
    port: int = 8080
    debug: bool = False
    middleware: list = field(default_factory=list)
```

### Using `__init_subclass__`
```python
class Plugin:
    _registry = {}
    
    def __init_subclass__(cls, name=None, **kwargs):
        super().__init_subclass__(**kwargs)
        Plugin._registry[name or cls.__name__] = cls
```

## Common Pitfalls

- Using Singleton when module-level state is sufficient
- Over-engineering Factory for simple object creation
- Not considering dependency injection as alternative
- Creating complex hierarchies when composition works better

## Decision Guide

- Need exactly one instance? Consider module-level state first
- Creating related objects? Factory or Abstract Factory
- Complex initialization? Builder pattern
- Need object copies? Prototype with `copy` module

## References

- *Design Patterns: Elements of Reusable Object-Oriented Software* (GoF)
- *Python Cookbook* - Alex Martelli
- *Fluent Python* - Luciano Ramalho
