# Magic Methods

Dunder methods for customizing object behavior.

## Overview

Magic methods (double underscore methods) let you define how objects behave with built-in operations like printing, comparison, arithmetic, and iteration.

## When to Use

- Making custom objects work with built-in functions
- Overloading operators for custom types
- Implementing container protocols
- Creating context managers

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| __str__/__repr__ | `magic_methods.py:5-18` | String representations |
| Comparison | `magic_methods.py:22-42` | __eq__, __lt__, __hash__ |
| total_ordering | `magic_methods.py:46-57` | Auto-generate comparisons |
| Arithmetic | `magic_methods.py:61-88` | __add__, __sub__, __mul__ |
| Container | `magic_methods.py:92-113` | __len__, __getitem__, __iter__ |
| Context manager | `magic_methods.py:117-130` | __enter__, __exit__ |
| Callable | `magic_methods.py:134-142` | __call__ |

## Common Mistakes

1. **Defining __eq__ without __hash__** — breaks set/dict usage
2. **Forgetting __repr__** — makes debugging painful
3. **Not returning NotImplemented** — for unsupported operations
4. **Overusing magic methods** — keep it simple

## Interview Questions

1. What is the difference between __str__ and __repr__?
2. How do you make a class iterable?
3. What does __slots__ do?
4. How do context managers work with __enter__ and __exit__?
