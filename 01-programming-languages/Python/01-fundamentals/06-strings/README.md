# Strings

String methods, formatting, encoding, and manipulation.

## Overview

Strings in Python are immutable sequences of Unicode characters with powerful built-in methods.

## When to Use

- Text processing and manipulation
- Data formatting and display
- File parsing (CSV, JSON, XML)
- Building user-facing output

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| String basics | `strings.py:4-9` | Quotes, triple quotes |
| Operations | `strings.py:13-16` | +, *, len, in |
| Common methods | `strings.py:20-31` | strip, lower, split, find |
| Splitting/Joining | `strings.py:35-43` | split, join, rsplit |
| f-strings | `strings.py:47-56` | f"{var:.2f}", alignment |
| .format() | `strings.py:58-60` | Named/positional args |
| Type checks | `strings.py:68-75` | isalnum, isdigit, etc. |
| Encoding | `strings.py:79-82` | encode/decode UTF-8 |

## Common Mistakes

1. **Forgetting strings are immutable** — `s[0] = 'x'` raises TypeError
2. **Using `+` in loops** — use `join()` instead
3. **Not handling encoding** — always specify encoding in `open()`
4. **Comparing case-sensitive** — use `.lower()` or `.casefold()`

## Interview Questions

1. Why are strings immutable in Python?
2. What is the difference between `split()` and `rsplit()`?
3. How do f-strings work internally?
4. What is the difference between `encode()` and `decode()`?
