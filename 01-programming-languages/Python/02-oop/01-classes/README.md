# Classes

When you need to model real-world entities and bundle data with behavior, classes provide the foundation. Python's class definitions, methods, properties, and dataclasses let you create reusable, organized code that encapsulates state and functionality.

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

## Production Checklist

### ✅ Before using classes in production:

☐ I know the time/space complexity
☐ I know thread safety guarantees
☐ I know memory impact
☐ I know common mistakes
☐ I know alternatives
☐ I know limitations
☐ I know how to debug it

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands thread safety

### Level 3: Deep Knowledge
- Knows internal implementation
- Understands edge cases

### Level 4: Expert
- Can optimize for specific use cases
- Can explain trade-offs

### Level 5: Master
- Can debug in production
- Can design custom implementations

## Common Myths

### ❌ Myth 1: Classes are always better than functions
**Reality:** Functions are simpler for stateless operations; classes add overhead for state management.

### ❌ Myth 2: `@dataclass` is just syntactic sugar
**Reality:** dataclasses provide `__init__`, `__repr__`, `__eq__`, and more with minimal overhead.

### ❌ Myth 3: Instance methods can't access class state
**Reality:** Instance methods can access class attributes via `self.__class__` or `ClassName`.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Create reusable object blueprints |
| Complexity | O(1) instantiation, O(n) for methods |
| Thread Safe | No (instance state is mutable) |
| Best Alternative | Use namedtuples for simple data |
| When to Use | Modeling entities, encapsulating state |
| When to Avoid | Simple stateless operations, overengineering |
