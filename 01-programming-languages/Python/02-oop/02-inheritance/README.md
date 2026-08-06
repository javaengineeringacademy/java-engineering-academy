# Inheritance

Single, multiple inheritance, MRO, and composition.

## Overview

Inheritance enables code reuse by creating new classes from existing ones. Python supports single, multiple, and multilevel inheritance.

## When to Use

- Sharing behavior between related classes
- Implementing polymorphic interfaces
- Building class hierarchies (is-a relationship)
- Use composition (has-a) when inheritance doesn't fit

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Single inheritance | `inheritance.py:4-26` | super().__init__ |
| Method override | `inheritance.py:30-32` | Redefining parent method |
| Multiple inheritance | `inheritance.py:36-48` | Diamond pattern |
| MRO | `inheritance.py:52-65` | __mro__, C3 linearization |
| isinstance/issubclass | `inheritance.py:69-74` | Type checking |
| Composition | `inheritance.py:78-88` | has-a vs is-a |
| ABC interface | `inheritance.py:92-112` | Abstract base classes |

## Common Mistakes

1. **Deep inheritance chains** — prefer composition over inheritance
2. **Not calling super().__init__()** — parent won't be initialized
3. **Diamond problem** — Python resolves via MRO, but be explicit
4. **Using inheritance for code reuse** — use mixins or composition

## Interview Questions

1. What is MRO and how does Python resolve it?
2. What is the difference between `isinstance` and `type`?
3. When would you use composition over inheritance?
4. Explain the diamond problem and how Python handles it.
