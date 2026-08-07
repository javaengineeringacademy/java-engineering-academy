# Decorators

When you need to modify or extend function or class behavior without changing the original code, decorators provide a clean way to add cross-cutting concerns. Python supports function decorators, class decorators, and patterns for logging, caching, and validation.

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

## Production Checklist

### ✅ Before using decorators in production:

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

### ❌ Myth 1: Decorators always improve performance
**Reality:** Decorators add overhead; use them for cross-cutting concerns, not performance.

### ❌ Myth 2: `@functools.wraps` is optional
**Reality:** Without `@wraps`, decorated functions lose metadata (`__name__`, `__doc__`).

### ❌ Myth 3: Class decorators are always better than function decorators
**Reality:** Function decorators are simpler for most cases; class decorators are for complex state.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Modify or extend function/class behavior |
| Complexity | O(1) per call (plus decorator overhead) |
| Thread Safe | Yes (decorator logic is usually stateless) |
| Best Alternative | Use context managers for resource management |
| When to Use | Logging, caching, validation, timing |
| When to Avoid | Overusing, making code harder to debug |
