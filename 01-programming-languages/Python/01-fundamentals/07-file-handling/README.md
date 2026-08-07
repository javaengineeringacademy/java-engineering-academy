# File Handling

When your program needs to read from or write to files, handle paths, or serialize data, you need reliable file handling. Python provides built-in file I/O, context managers, path operations, and serialization that make working with files safe and straightforward.

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

## Production Checklist

### ✅ Before using file handling in production:

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

### ❌ Myth 1: `open()` automatically closes files
**Reality:** Files are only closed when using `with` statement or calling `close()` explicitly.

### ❌ Myth 2: `readline()` is always efficient
**Reality:** `readline()` loads entire lines into memory; use `readline()` in a loop for large files.

### ❌ Myth 3: pathlib is just syntactic sugar
**Reality:** pathlib provides OOP interface, better error handling, and platform-independent paths.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Read/write files and paths |
| Complexity | O(n) for file operations |
| Thread Safe | No (file handles are not thread-safe) |
| Best Alternative | Use tempfile for temporary files |
| When to Use | Reading/writing files, processing data |
| When to Avoid | Forgetting to close files, ignoring encoding |
