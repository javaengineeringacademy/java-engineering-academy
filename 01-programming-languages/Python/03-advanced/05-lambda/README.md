# Lambda Functions

Anonymous functions, map, filter, and reduce.

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
