# Python Fundamentals

Core building blocks of Python programming — variables, operators, control flow, functions, collections, strings, and file handling.

## Topics

| # | Topic | Description |
|---|-------|-------------|
| 01 | Variables & Types | Dynamic typing, assignment, type conversion |
| 02 | Operators | Arithmetic, comparison, logical, and bitwise operators |
| 03 | Control Flow | if/elif/else, for/while loops, break/continue |
| 04 | Functions | Defining functions, arguments, return values, scope |
| 05 | Collections | Lists, tuples, dictionaries, sets |
| 06 | Strings | String methods, formatting, slicing, encoding |
| 07 | File Handling | Reading/writing files, context managers, paths |

## Prerequisites

- Python installed (3.10+)
- A text editor or IDE

## Learning Objectives

By the end of this module you will be able to:

- Declare variables and work with Python's built-in types
- Use operators and control flow to write logic
- Define and call functions with different argument patterns
- Manipulate collections and strings effectively
- Read from and write to files

## Quick Start

```bash
# Run any topic directly
python 01-variables/variables.py
python 02-operators/operators.py
python 03-control-flow/control_flow.py
python 04-functions/functions.py
python 05-collections/collections.py
python 06-strings/strings.py
python 07-file-handling/file_handling.py
```

## Production Checklist

### ✅ Before using Python fundamentals in production:

☐ I know the time/space complexity of built-in collections
☐ I know common mistakes (mutable default args, shallow vs deep copy)
☐ I know alternatives (numpy arrays vs lists, named tuples vs dicts)
☐ I know limitations (GIL, dynamic typing overhead)
☐ I know how to debug it (pdb, logging, traceback module)
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

### ❌ Myth 1: Python is too slow for real applications
**Reality:** Python's speed is adequate for I/O-bound tasks; profile before optimizing and use C extensions or async for bottlenecks.

### ❌ Myth 2: Dynamic typing means no type safety
**Reality:** Type hints (PEP 484) plus tools like mypy catch type errors at development time without runtime cost.

### ❌ Myth 3: Lists are always the best collection choice
**Reality:** Tuples are faster and immutable; sets excel at membership testing; dicts are ideal for key-value lookups. Choose based on use case.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Core building blocks of Python programming |
| Complexity | O(1) access for dicts/sets; O(n) for list search |
| Thread Safe | No (use threading.Lock for shared state) |
| Best Alternative | NumPy for numeric, TypedDict for structured dicts |
| When to Use | Rapid prototyping, scripting, data pipelines |
| When to Avoid | Real-time systems, heavy numeric computation without NumPy |
