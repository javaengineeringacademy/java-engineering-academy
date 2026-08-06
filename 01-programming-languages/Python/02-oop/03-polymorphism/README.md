# Polymorphism

Duck typing, method overriding, and operator overloading.

## Overview

Polymorphism allows objects of different types to be used through a uniform interface. Python uses duck typing — behavior matters, not type.

## When to Use

- Writing functions that work with multiple types
- Building plugin/extension systems
- Overloading operators for custom types
- Creating polymorphic collections

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Duck typing | `polymorphism.py:4-22` | No shared base required |
| Method overriding | `polymorphism.py:26-48` | Shape.area() |
| Built-in polymorphism | `polymorphism.py:52-56` | len() on any type |
| Operator overloading | `polymorphism.py:60-77` | __add__, __mul__ |
| Polymorphic collections | `polymorphism.py:81-99` | Notification broadcast |

## Common Mistakes

1. **Checking types instead of behavior** — use duck typing, not isinstance
2. **Not implementing __repr__** — makes debugging hard
3. **Overusing isinstance** — breaks polymorphism
4. **Forgetting NotImplemented** — return it from unsupported operations

## Interview Questions

1. What is duck typing?
2. How does operator overloading work in Python?
3. What is the difference between polymorphism and inheritance?
4. How do you make a class iterable?
