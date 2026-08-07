# Python Programming

Python is the most versatile language in modern software development, powering everything from web applications to data science, automation, and machine learning. This module provides a structured learning path from fundamentals to advanced topics.

## Modules

| # | Module | Topics |
|---|--------|--------|
| 01 | Fundamentals | Variables, operators, control flow, functions, collections, strings, file handling |
| 02 | OOP | Classes, inheritance, polymorphism, encapsulation, abstraction, magic methods |
| 03 | Advanced | Decorators, generators, context managers, comprehensions, lambda |
| 04 | Concurrency | Threading, multiprocessing, asyncio |
| 05 | Testing | unittest, pytest |
| ref | Reference | Best practices, anti-patterns, patterns, performance, comparisons |

## Quick Start

```python
# Run any module directly
python 01-fundamentals/01-variables/variables.py

# Run all fundamentals
for f in 01-fundamentals/*/[!.]*.py; do python "$f"; done
```

## Requirements

- Python 3.10+
- No external dependencies for fundamentals/advanced
- `pytest` for 05-testing/02-pytest
