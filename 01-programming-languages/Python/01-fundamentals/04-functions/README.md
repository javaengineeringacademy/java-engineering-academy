# Functions

Function definitions, arguments, closures, and decorators.

## Overview

Functions are first-class objects in Python — they can be assigned to variables, passed as arguments, and returned from other functions.

## When to Use

- Code reuse and organization
- Encapsulating logic
- Creating decorators and higher-order functions
- Building APIs and libraries

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic function | `functions.py:5-9` | def, return |
| Default args | `functions.py:13-16` | title="Mr." |
| *args/**kwargs | `functions.py:20-25` | Flexible arguments |
| Keyword-only | `functions.py:29-32` | After * |
| Multiple return | `functions.py:37-40` | Tuple unpacking |
| First-class | `functions.py:45-49` | Passing functions |
| Lambda | `functions.py:53-57` | Anonymous functions |
| Closures | `functions.py:61-71` | nonlocal, captured state |
| Decorator | `functions.py:75-87` | @timer |
| Type hints | `functions.py:91-101` | typing module |

## Common Mistakes

1. **Mutable default arguments** — `def f(x=[])` shares the list across calls
2. **Using `return` in a generator** — use `yield` instead
3. **Forgetting `self`** in class methods
4. **Overusing lambdas** — use `def` for complex logic

## Interview Questions

1. What are *args and **kwargs?
2. Explain closures and the `nonlocal` keyword.
3. Why are mutable default arguments dangerous?
4. What is the difference between a generator and a regular function?
