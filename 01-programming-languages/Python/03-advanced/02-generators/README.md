# Generators

yield, generator expressions, and lazy evaluation.

## Overview

Generators produce values on-demand using `yield` instead of returning a list. They're memory-efficient for large datasets and infinite sequences.

## When to Use

- Processing large files line by line
- Infinite sequences (Fibonacci, counters)
- Pipeline processing (filter → transform → consume)
- Memory-constrained environments

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Generator function | `generators.py:4-17` | yield, next() |
| Generator expression | `generators.py:21-28` | () vs [] |
| Infinite generators | `generators.py:32-39` | Fibonacci, islice |
| Pipeline | `generators.py:43-56` | read → filter → parse |
| yield from | `generators.py:60-69` | Recursive flattening |
| send() | `generators.py:73-83` | Send values to generator |
| Running average | `generators.py:87-99` | Coroutine pattern |
| itertools | `generators.py:103-113` | chain, islice, groupby |

## Common Mistakes

1. **Forgetting to prime generators** — call next() before send()
2. **Consuming a generator twice** — generators are exhausted
3. **Using yield in non-generator** — only one yield per function
4. **Not using islice for infinite generators** — will loop forever

## Interview Questions

1. What is the difference between a generator and an iterator?
2. How does yield work internally?
3. When would you use yield from?
4. What are the memory benefits of generators?
