# Generators

When you need to process large datasets or infinite sequences without loading everything into memory, generators enable efficient iteration. Python's yield, generator expressions, and lazy evaluation produce values on-demand, saving memory and improving performance.

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

## Production Checklist

### ✅ Before using generators in production:

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

### ❌ Myth 1: Generators are always slower than lists
**Reality:** Generators use less memory; speed depends on use case and iteration pattern.

### ❌ Myth 2: Generators can be restarted
**Reality:** Generators are exhausted after one iteration; create a new one to restart.

### ❌ Myth 3: `yield` always pauses execution
**Reality:** `yield` only pauses when the generator is being iterated; not during function call.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Produce values lazily for memory efficiency |
| Complexity | O(1) per yield, O(n) total |
| Thread Safe | Yes (generators are stateless) |
| Best Alternative | Use itertools for complex iteration |
| When to Use | Large datasets, pipelines, infinite sequences |
| When to Avoid | Needing random access, restarting iteration |
