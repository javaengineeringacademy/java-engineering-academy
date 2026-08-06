# Decorators

Function decorators, class decorators, and patterns.

## Overview

Decorators modify or extend function/class behavior without changing the original code. They're a powerful metaprogramming tool.

## When to Use

- Cross-cutting concerns (logging, timing, auth)
- Memoization and caching
- Input validation
- API rate limiting
- Singleton pattern

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Basic decorator | `decorators.py:5-18` | @timer with @wraps |
| Decorator with args | `decorators.py:22-38` | @retry(max_attempts=3) |
| Stacking | `decorators.py:42-53` | @bold @italic |
| Class decorator | `decorators.py:57-72` | @add_repr |
| lru_cache | `decorators.py:76-81` | Memoization |
| property | `decorators.py:85-100` | Getter/setter |
| dataclass | `decorators.py:110-118` | frozen=True |
| Singleton | `decorators.py:122-132` | Class-level caching |

## Common Mistakes

1. **Forgetting @functools.wraps** — loses function metadata
2. **Not returning the wrapper** — breaks the decorated function
3. **Overusing decorators** — makes code harder to debug
4. **Mutable default in closure** — shared state between calls

## Interview Questions

1. How does @functools.wraps work?
2. Write a decorator that logs function arguments.
3. What is the difference between a decorator and a context manager?
4. How would you create a caching decorator?
