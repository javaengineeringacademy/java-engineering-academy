# Classes

Class definitions, methods, properties, and dataclasses.

## Overview

Classes are the foundation of OOP in Python. They bundle data (attributes) and behavior (methods) into reusable blueprints.

## When to Use

- Modeling real-world entities
- Encapsulating related data and behavior
- Creating reusable components
- Building APIs and libraries

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic class | `classes.py:4-24` | __init__, instance methods |
| Class vs instance | `classes.py:28-39` | Class attributes, shared state |
| Methods | `classes.py:43-60` | @classmethod, @staticmethod |
| Property | `classes.py:64-85` | @property, getter/setter |
| __str__/__repr__ | `classes.py:89-103` | String representations |
| Dataclass | `classes.py:107-120` | @dataclass, field |

## Common Mistakes

1. **Forgetting `self`** — first parameter of methods is always `self`
2. **Using mutable defaults** — use `None` and set in `__init__`
3. **Confusing class and instance attributes** — class attrs are shared
4. **Not implementing `__repr__`** — makes debugging harder

## Interview Questions

1. What is the difference between `__str__` and `__repr__`?
2. What is a class method vs a static method?
3. When would you use `@property`?
4. What are dataclasses and how do they differ from regular classes?
