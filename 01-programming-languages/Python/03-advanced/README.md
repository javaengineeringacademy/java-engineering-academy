# Advanced Python

## Why Advanced Features Matter

Every Python application eventually needs to handle cross-cutting concerns, process large datasets efficiently, manage resources safely, and write concise, expressive code. Advanced Python features — decorators, generators, context managers, comprehensions, and lambda functions — provide elegant solutions to these challenges. Without them, you'd write verbose, repetitive code that's hard to maintain.

Without these advanced features, you'd have to implement resource management manually, write loops for every data transformation, and duplicate boilerplate code across functions. That's why these features exist — they let you write Pythonic code that's concise, efficient, and uses the language's full expressive power.

## What You'll Learn

By the end of this module, you'll be able to:

- Write and compose decorators for cross-cutting concerns
- Use generators for memory-efficient iteration
- Create custom context managers for resource management
- Write concise comprehensions for data transformation
- Apply lambda functions with map, filter, and reduce

## Engineering Decision Framework

| Factor | Use This | Consider Alternatives |
|--------|----------|----------------------|
| When to use | Cross-cutting concerns, memory-efficient iteration, resource management | Simple loops for small datasets |
| When NOT to use | Don't over-decorate; don't use generators when you need random access | Use simple functions for straightforward logic |
| Alternatives | itertools for iteration, functools for function transforms | Basic loops and context managers |
| Production Examples | Web middleware, data pipelines, file processing | Simple scripts, prototypes |
| Common Mistakes | Generator exhaustion, decorator order confusion, context manager leaks | Test decorator stacking; use `@wraps` |

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Decorators | Function/class decorators, stacking, argument decorators |
| 02 | Generators | yield, generator expressions, lazy evaluation |
| 03 | Context Managers | with statement, __enter__/__exit__, contextlib |
| 04 | Comprehensions | List, dict, set, and generator comprehensions |
| 05 | Lambda | Anonymous functions, map, filter, reduce |

## Prerequisites

- Python Fundamentals (01-fundamentals)
- Object-Oriented Programming (02-oop)

## Interview Questions

### Q1: What is a closure and how is it different from a nested function?
**Answer:** A closure is a nested function that captures variables from enclosing scope. The closure keeps references to free variables even after the outer function returns.

### Q2: Explain decorator execution order.
**Answer:** Decorators are applied bottom-up. @a @b def f is equivalent to f = a(b(f)). The innermost decorator runs first.

### Q3: What is the difference between `yield` and `return`?
**Answer:** `return` exits the function and returns a value. `yield` suspends execution and returns a value, maintaining state for next call.

### Q4: What is the iterator protocol?
**Answer:** Objects implement __iter__() and __next__() methods. __iter__() returns self, __next__() returns next value or raises StopIteration.

### Q5: What is a generator expression vs list comprehension?
**Answer:** Generator expression uses () and yields one item at a time (lazy). List comprehension uses [] and creates entire list in memory (eager).

## Learning Objectives

By the end of this module you will be able to:

- Write and compose decorators for cross-cutting concerns
- Use generators for memory-efficient iteration
- Create custom context managers for resource management
- Write concise comprehensions for data transformation
- Apply lambda functions with map, filter, and reduce

## Quick Start

```bash
# Run any topic directly
python 01-decorators/decorators.py
python 02-generators/generators.py
python 03-context-managers/context_managers.py
python 04-comprehensions/comprehensions.py
python 05-lambda/lambda_functions.py
```

## Production Incidents

### Incident 1: Generator Exhaustion in Pipeline

**Problem:** Data pipeline processed only first batch, then returned empty results
**Cause:** Generator expression was consumed once; second iteration yielded nothing
**Impact:** 80% of data records were silently dropped
**Detection:** Data quality dashboard showed missing records
**Solution:**
```python
# BAD: generator = (process(x) for x in data)
# Generator consumed once

# GOOD: Convert to list if needed multiple times
processed = [process(x) for x in data]
# OR: Create generator function that regenerates
def data_generator():
    for x in get_data():
        yield process(x)
```
**Prevention:** Document generator behavior; use `itertools.tee()` for multiple consumers; test iteration

### Incident 2: Decorator Order Causing Authentication Bypass

**Problem:** Authentication check was skipped on protected endpoints
**Cause:** `@cache` decorator was applied before `@require_auth`, bypassing auth
**Impact:** Unauthenticated users could access protected data
**Detection:** Security audit found unprotected endpoints
**Solution:**
```python
# BAD:
@cache
@require_auth
def get_sensitive_data(): ...

# GOOD: Auth before cache
@require_auth
@cache
def get_sensitive_data(): ...
```
**Prevention:** Establish decorator ordering policy; test auth decorators run first; use `@functools.wraps`

### Incident 3: Context Manager Not Releasing Database Connection

**Problem:** Database connection pool exhausted after 100 requests
**Cause:** Custom context manager didn't call `close()` in `finally` block on exception
**Impact:** Service degradation; 50% of requests failed with connection timeout
**Detection:** Connection pool monitoring alerted on exhaustion
**Solution:**
```python
from contextlib import contextmanager

@contextmanager
def get_db_connection():
    conn = pool.acquire()
    try:
        yield conn
    finally:
        conn.close()  # Always release
```
**Prevention:** Always use `try/finally` in context managers; test exception paths; use `@contextmanager` decorator

## Production Checklist

### ✅ Before using advanced Python features in production:

☐ I know the time/space complexity of generators vs lists, decorator stacking
☐ I know common mistakes (generator exhaustion, decorator order confusion, context manager resource leaks)
☐ I know alternatives (itertools, functools.partial, contextlib.suppress)
☐ I know limitations (generators can't be rewound, decorators add call overhead)
☐ I know how to debug it (inspect, dis module, logging decorators)
☐ I've tested with realistic data volume
☐ I've profiled for performance

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic syntax
- Can write working code

### Level 2: Understands
- Knows time/space complexity
- Understands edge cases

### Level 3: Deep Knowledge
- Knows internal implementation
- Can explain trade-offs

### Level 4: Expert
- Can optimize for specific use cases
- Can debug in production

### Level 5: Master
- Can design custom implementations
- Can teach others

## Common Myths

### ❌ Myth 1: Decorators are just for logging or auth
**Reality:** Decorators are general-purpose wrappers for caching, retry logic, rate limiting, validation, and any cross-cutting concern.

### ❌ Myth 2: Generators are always more memory-efficient
**Reality:** Generators add per-yield overhead. For small, fully-materialized datasets, lists can be faster and simpler.

### ❌ Myth 3: Comprehensions are always faster than loops
**Reality:** Comprehensions are optimized for simple cases, but complex logic with conditionals and side effects is often clearer and equivalent in speed with explicit loops.

## Related Topics

- [07-functional-programming](../07-functional-programming/) - Lambda, map, filter, reduce
- [10-internals](../10-internals/) - Generator and context manager internals
- [15-performance](../15-performance/) - Performance optimization techniques

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Elegant, Pythonic patterns for iteration, resource management, and composition |
| Complexity | Generators: O(1) memory; comprehensions: O(n) like loops |
| Thread Safe | Yes (generators and lambdas are stateless) |
| Best Alternative | itertools for iteration, functools for function transformation |
| When to Use | Memory-efficient pipelines, cross-cutting concerns, concise transforms |
| When to Avoid | When readability suffers, when debugging stack traces matter |

---

## Debugging Tips

| Problem | Tool/Technique | How |
|---------|---------------|-----|
| Generator exhaustion silently returning empty | Add `len()` check or use `itertools.tee()` | Document generator behavior; convert to list if multiple passes needed |
| Decorator order causing logic bypass | `inspect.getsource()` to trace applied decorators | Establish ordering policy: auth before cache; use `@functools.wraps` |
| Context manager not releasing resource on exception | Test exception paths explicitly | Always use `try/finally` in context managers; test with `pytest.raises` |
| Decorator adding call overhead | `cProfile` on decorated vs undecorated function | Use `@functools.wraps` to minimize overhead; avoid stacking when performance matters |
| Lambda capturing loop variable by value | Print lambda defaults at creation time | Use default argument `i=i` or `functools.partial` to bind current value |

## Code Review Checklist

- [ ] `@functools.wraps` used in all custom decorators to preserve metadata
- [ ] Generator exhaustion documented; consumers aware of single-pass nature
- [ ] Context managers use `try/finally` for guaranteed resource cleanup
- [ ] Decorator ordering verified (security decorators outermost)
- [ ] Lambda limited to simple expressions; complex logic uses `def`
- [ ] Generator expressions used for large datasets instead of list comprehensions
- [ ] `map`/`filter` only when function is already defined; comprehensions preferred otherwise

## Architecture Considerations

Advanced Python features enable elegant cross-cutting concerns. Decorators provide AOP-like behavior for logging, auth, and caching. Generators enable memory-efficient data pipelines that process infinite streams. Context managers enforce resource lifecycle guarantees. Together, these patterns compose into declarative, maintainable architectures that separate concerns cleanly.

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| Decorator stacking | Logging, auth, caching, retry | Readable but order-dependent; adds call overhead |
| Generator pipelines | Data processing, streaming | Memory efficient but single-pass; harder to debug |
| Context managers | Resource lifecycle (DB, files, connections) | Guaranteed cleanup but requires explicit `with` blocks |

## Security Considerations

| Risk | Impact | Mitigation |
|------|--------|------------|
| Decorator order bypassing auth | Unauthenticated access to protected endpoints | Place security decorators outermost; test decorator stacking order |
| Generator yielding sensitive data to unauthorized consumers | Data leakage | Ensure generators are consumed by authorized callers only |
| Context manager exception swallowing | Resource leak or security bypass | Use `try/finally` explicitly; test exception paths |

## Evolution & Modernization

| Version | Change | Migration Path |
|---------|--------|----------------|
| Python 3.7+ | `contextlib.AsyncContextManager` | Use for async resource management in `async with` |
| Python 3.9+ | `functools.cache` (unbounded) | Replace `lru_cache(maxsize=None)` with `@cache` |
| Python 3.12+ | Generator expression improvements | Use for cleaner pipeline expressions |

## Version Validation

| Feature | Python Version | Status |
|---------|---------------|--------|
| `@contextmanager` | 3.2+ | Stable, preferred for simple context managers |
| Generator expressions `()` | 3.0+ | Stable, memory-efficient iteration |
| `functools.lru_cache` | 3.2+ | Stable, memoization for pure functions |
| `contextlib.asynccontextmanager` | 3.7+ | Stable, async resource management |
