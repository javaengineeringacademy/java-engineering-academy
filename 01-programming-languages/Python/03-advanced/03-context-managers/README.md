# Context Managers

with statement, contextlib, and resource management.

## Overview

Context managers ensure proper resource acquisition and release using the `with` statement. They handle setup and teardown automatically.

## When to Use

- File handling (auto-close)
- Database connections (auto-commit/rollback)
- Locks and synchronization
- Temporary state changes
- Resource cleanup

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| Class-based | `context_managers.py:4-21` | __enter__, __exit__ |
| contextmanager | `context_managers.py:25-34` | @contextmanager decorator |
| Timer | `context_managers.py:38-46` | Performance measurement |
| suppress | `context_managers.py:50-52` | Exception suppression |
| redirect_stdout | `context_managers.py:56-61` | Output capture |
| ExitStack | `context_managers.py:65-71` | Dynamic context managers |
| Async | `context_managers.py:75-83` | asynccontextmanager |

## Common Mistakes

1. **Forgetting finally** — resources won't be cleaned up
2. **Suppressing all exceptions** — return False from __exit__
3. **Not using context managers** — manual try/finally is error-prone
4. **Forgetting to yield** — context manager won't work

## Interview Questions

1. How does the with statement work internally?
2. What is the difference between __exit__ return values?
3. When would you use ExitStack?
4. How do you create an async context manager?
