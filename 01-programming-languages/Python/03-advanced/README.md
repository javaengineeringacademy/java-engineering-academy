# Advanced Python

## Why Advanced Features Matter

Every Python application eventually needs to handle cross-cutting concerns, process large datasets efficiently, manage resources safely, and write concise, expressive code. Advanced Python features — decorators, generators, context managers, comprehensions, and lambda functions — provide elegant solutions to these challenges. Without them, you'd write verbose, repetitive code that's hard to maintain.

Without these advanced features, you'd have to implement resource management manually, write loops for every data transformation, and duplicate boilerplate code across functions. That's why these features exist — they let you write Pythonic code that's concise, efficient, and leverages the language's full expressive power.

## What You'll Learn

By the end of this module, you'll be able to:

- Write and compose decorators for cross-cutting concerns
- Use generators for memory-efficient iteration
- Create custom context managers for resource management
- Write concise comprehensions for data transformation
- Apply lambda functions with map, filter, and reduce

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

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Elegant, Pythonic patterns for iteration, resource management, and composition |
| Complexity | Generators: O(1) memory; comprehensions: O(n) like loops |
| Thread Safe | Yes (generators and lambdas are stateless) |
| Best Alternative | itertools for iteration, functools for function transformation |
| When to Use | Memory-efficient pipelines, cross-cutting concerns, concise transforms |
| When to Avoid | When readability suffers, when debugging stack traces matter |
