# Abstraction

Abstract base classes, interfaces, and plugin patterns.

## Overview

Abstraction hides implementation details behind a clean interface. Python uses ABC (Abstract Base Classes) to define interfaces.

## When to Use

- Defining interfaces that must be implemented
- Building plugin/extension systems
- Enforcing contracts between components
- Creating framework APIs

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| ABC basics | `abstraction.py:7-24` | @abstractmethod |
| Concrete classes | `abstraction.py:28-44` | GasCar, ElectricCar |
| Abstract property | `abstraction.py:48-67` | Database interface |
| Virtual subclass | `abstraction.py:71-81` | Serializer.register() |
| Plugin system | `abstraction.py:85-110` | Registry pattern |

## Common Mistakes

1. **Forgetting @abstractmethod** — method won't be required
2. **Instantiating abstract class** — raises TypeError
3. **Overusing ABC** — use duck typing when possible
4. **Not calling super().__init__()** — parent won't initialize

## Interview Questions

1. What is an abstract base class?
2. How do you register a virtual subclass?
3. What is the difference between ABC and Protocol?
4. When would you use abstraction vs duck typing?
