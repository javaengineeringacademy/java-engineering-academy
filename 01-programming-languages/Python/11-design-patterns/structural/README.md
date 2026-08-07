# Structural Design Patterns in Python

Structural patterns deal with object composition, describing how classes and objects are composed to form larger structures. Python's duck typing and dynamic nature often simplify these patterns.

## Patterns Overview

| Pattern | Purpose | Pythonic Implementation |
|---------|---------|------------------------|
| Adapter | Interface compatibility | Duck typing, wrappers |
| Decorator | Behavior extension | `@decorator` syntax |
| Facade | Simplified interface | Module functions, classes |
| Proxy | Access control | `__getattr__`, properties |
| Composite | Tree structures | Duck typing, recursion |
| Bridge | Implementation separation | Abstract base classes |
| Flyweight | Memory efficiency | `__slots__`, caching |

## Why Structural Patterns Matter

1. **Composition over Inheritance** - Build complex objects from simpler ones
2. **Flexibility** - Change structure without modifying components
3. **Reusability** - Combine patterns to create new functionality
4. **Maintainability** - Clear separation of concerns

## Python-Specific Advantages

### Duck Typing Simplifies Patterns
```python
# No formal interface needed for Adapter
class OldSystem:
    def old_method(self):
        return "old result"

class NewSystemAdapter:
    def __init__(self, old_system):
        self._old = old_system
    
    def new_method(self):
        return self._old.old_method()
```

### Decorators as First-Class Citizens
```python
def log_calls(func):
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__}")
        return func(*args, **kwargs)
    return wrapper

@log_calls
def process_data(data):
    return data * 2
```

### Context Managers for Facade
```python
from contextlib import contextmanager

@contextmanager
def database_connection():
    conn = create_connection()
    try:
        yield conn
    finally:
        conn.close()
```

## Common Structural Patterns in Python

1. **Decorator** - `@property`, `@staticmethod`, `@classmethod`
2. **Adapter** - Wrapping third-party libraries
3. **Facade** - Simplifying complex libraries
4. **Proxy** - Lazy loading, caching, access control
5. **Composite** - Tree structures in GUIs, file systems

## When to Use Patterns

- Adapting incompatible interfaces without modifying source code
- Adding responsibilities to objects dynamically
- Providing simplified interfaces to complex subsystems
- Controlling access to objects
- Representing part-whole hierarchies

## References

- *Design Patterns* - GoF, Chapter 4
- *Fluent Python* - Luciano Ramalho
- Python documentation - Context Managers
- PEP 343 - The `with` Statement
