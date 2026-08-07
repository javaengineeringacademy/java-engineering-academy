# Python Programming

Python is the most versatile language in modern software development — from web applications to data science, from automation to machine learning. This module covers everything from fundamentals to senior-level architecture.

## Modules

| # | Module | Topics |
|---|--------|--------|
| 00 | Knowledge Atoms | Duck typing, GIL, garbage collection, data model |
| 01 | Fundamentals | Variables, operators, control flow, functions, collections, strings, file handling |
| 02 | OOP | Classes, inheritance, polymorphism, encapsulation, abstraction, magic methods |
| 03 | Advanced | Decorators, generators, context managers, comprehensions, lambda |
| 04 | Concurrency | Threading, multiprocessing, asyncio |
| 05 | Testing | unittest, pytest |
| 06 | Type Hints | Type annotations, Protocol, TypeVar, generics |
| 07 | Functional Programming | First-class functions, functools, itertools, immutability |
| 08 | File I/O | pathlib, context managers, CSV, JSON, async I/O |
| 09 | Exception Handling | Custom exceptions, exception chaining, best practices |
| 10 | Internals | CPython internals, bytecode, GIL deep dive |
| 11 | Design Patterns | Creational, structural, behavioral patterns |
| 12 | Collections | list, dict, set, tuple, Counter, defaultdict, performance |
| 13 | Logging | logging module, structured logging, handlers |
| 14 | Memory Management | GC, weakrefs, memory profiling, optimization |
| 15 | Performance | Profiling, optimization, async performance |
| 16 | Best Practices | PEP 8, code review, project structure |
| 17 | Metaclasses | type, ABCMeta, descriptors, class decorators |
| 18 | Senior | Architecture, production, scaling, security |

## Quick Start

```bash
# Run any module directly
python 01-fundamentals/01-variables/variables.py

# Run all fundamentals
for f in 01-fundamentals/*/[!.]*.py; do python "$f"; done
```

## Requirements

- Python 3.10+
- No external dependencies for fundamentals/advanced
- `pytest` for 05-testing/02-pytest
