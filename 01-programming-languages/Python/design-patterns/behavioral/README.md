# Behavioral Design Patterns in Python

Behavioral patterns are concerned with algorithms and the assignment of responsibilities between objects. They describe patterns of communication between objects and how the flow of control is managed.

## Patterns Overview

| Pattern | Purpose | Pythonic Implementation |
|---------|---------|------------------------|
| Observer | Event notification | Callbacks, signals |
| Strategy | Algorithm selection | Functions, callables |
| Command | Encapsulated requests | Callable objects |
| Iterator | Sequential access | `__iter__`, `__next__` |
| State | State-driven behavior | State classes, dicts |
| Template Method | Algorithm skeleton | Abstract methods |
| Chain of Responsibility | Request handling | Linked handlers |
| Mediator | Object communication | Event buses |
| Memento | State capture/restoration | Pickle, deepcopy |
| Visitor | Operation dispatch | `singledispatch` |
| Interpreter | Grammar representation | AST, parsing |

## Why Behavioral Patterns Matter

1. **Communication** - Define clear object interaction protocols
2. **Responsibility** - Distribute work appropriately
3. **Flexibility** - Change behavior without modifying structure
4. **Encapsulation** - Hide implementation details

## Python-Specific Advantages

### First-Class Functions
```python
# Strategy as function
def sort_by_name(items):
    return sorted(items, key=lambda x: x.name)

def sort_by_date(items):
    return sorted(items, key=lambda x: x.date)

class Processor:
    def __init__(self, strategy=None):
        self.strategy = strategy or sort_by_name
    
    def process(self, items):
        return self.strategy(items)
```

### Generators for Iterator
```python
def fibonacci():
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

# Iterator pattern built into language
fib = fibonacci()
first_10 = [next(fib) for _ in range(10)]
```

### Context Managers for State
```python
from contextlib import contextmanager

@contextmanager
def transaction_state():
    state = {"committed": False}
    try:
        yield state
        state["committed"] = True
    except Exception:
        state["rolled_back"] = True
```

## Common Behavioral Patterns in Python

1. **Observer** - Event systems, signals
2. **Strategy** - Algorithm selection via callables
3. **Command** - Undo/redo systems
4. **Iterator** - Generator protocols
5. **State** - Finite state machines

## When to Use Patterns

- Defining communication between objects
- Managing complex control flow
- Implementing algorithms that vary at runtime
- Capturing and restoring object state
- Parsing and interpreting languages

## References

- *Design Patterns* - GoF, Chapter 5
- *Fluent Python* - Luciano Ramalho
- Python documentation - Generators
- PEP 289 - Generator Expressions
