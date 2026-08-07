# Lambda Functions

When you need short, one-time-use functions for callbacks or functional programming pipelines, lambda functions provide a concise way. Python's anonymous functions, map, filter, and reduce enable functional patterns for data transformation.

## Overview

Lambda functions are anonymous, single-expression functions. Combined with map/filter/reduce, they enable functional programming patterns.

## When to Use

- Short callbacks (sorting keys, event handlers)
- One-time use functions
- Functional programming pipelines
- Data transformation chains

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Lambda basics | `lambda_functions.py:4-9` | Anonymous functions |
| map() | `lambda_functions.py:13-22` | Apply to every item |
| filter() | `lambda_functions.py:26-35` | Conditional filtering |
| reduce() | `lambda_functions.py:39-47` | Accumulate to single value |
| Practical examples | `lambda_functions.py:51-65` | Sorting, filtering dicts |
| operator module | `lambda_functions.py:69-77` | itemgetter, methodcaller |
| sorted() with key | `lambda_functions.py:81-88` | Multi-criteria sort |
| any()/all() | `lambda_functions.py:92-95` | Boolean aggregation |

## Common Mistakes

1. **Using lambda for complex logic** — use def instead
2. **Assigning lambda to variable** — use def for named functions
3. **Ignoring readability** — comprehensions are often clearer
4. **Not using operator module** — faster than lambda for simple ops

## Interview Questions

1. What is a lambda function?
2. What is the difference between map() and a list comprehension?
3. How does reduce() work?
4. When would you use operator.itemgetter over lambda?

## Production Checklist

### ✅ Before using lambda functions in production:

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

### ❌ Myth 1: Lambdas are always faster than def
**Reality:** Lambdas have the same performance; the difference is syntactic, not performance.

### ❌ Myth 2: Lambdas can have multiple statements
**Reality:** Lambdas are limited to a single expression; use def for multiple statements.

### ❌ Myth 3: map/filter are always faster than comprehensions
**Reality:** Comprehensions are often faster and more readable than map/filter with lambdas.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Create anonymous, single-expression functions |
| Complexity | O(1) per call |
| Thread Safe | Yes (lambdas are stateless) |
| Best Alternative | Use def for named functions |
| When to Use | Short callbacks, sorting keys, one-time use |
| When to Avoid | Complex logic, multiple statements |
