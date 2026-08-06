# File Handling

File I/O, context managers, path operations, and serialization.

## Overview

Python provides built-in file handling through `open()`, context managers (`with`), and the `pathlib` module for modern path operations.

## When to Use

- Reading/writing configuration files
- Processing CSV, JSON, or binary data
- Logging and report generation
- Data import/export pipelines

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Writing files | `file_io.py:4-17` | 'w', 'a' modes, writelines |
| Reading files | `file_io.py:21-33` | read, readline, for loop |
| File modes | `file_io.py:37-43` | r, w, a, x, b, + |
| Binary files | `file_io.py:47-53` | rb, wb modes |
| pathlib | `file_io.py:57-72` | Path, /, properties |
| File operations | `file_io.py:76-87` | exists, listdir, mkdir |
| Context manager | `file_io.py:93-108` | __enter__, __exit__ |
| CSV | `file_io.py:112-127` | csv.reader, csv.writer |
| JSON | `file_io.py:131-140` | json.load, json.dump |

## Common Mistakes

1. **Forgetting to close files** — always use `with` statement
2. **Not specifying encoding** — use `encoding="utf-8"` explicitly
3. **Reading entire large files** — iterate line by line instead
4. **Not handling FileNotFoundError** — use try/except or check exists

## Interview Questions

1. What is the purpose of the `with` statement?
2. What is the difference between `read()` and `readlines()`?
3. How does pathlib improve over os.path?
4. What is a context manager and how do you create one?
