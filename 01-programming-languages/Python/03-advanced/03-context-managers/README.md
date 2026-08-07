# Context Managers

When you need to ensure resources are properly acquired and released, context managers provide a safe way to handle setup and teardown. Python's with statement, contextlib, and resource management guarantee cleanup even if exceptions occur.

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

## Production Checklist

### ✅ Before using context managers in production:

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

### ❌ Myth 1: Context managers are only for file handling
**Reality:** Context managers are used for any resource that needs setup/teardown (locks, connections, etc.).

### ❌ Myth 2: `@contextmanager` is always better than class-based
**Reality:** Class-based context managers are more explicit and easier to debug.

### ❌ Myth 3: Context managers always clean up resources
**Reality:** If `__exit__` doesn't handle exceptions properly, resources may leak.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Ensure proper resource acquisition and release |
| Complexity | O(1) for setup/teardown |
| Thread Safe | Yes (context managers are stateless) |
| Best Alternative | Use try/finally for simple cleanup |
| When to Use | File handling, database connections, locks |
| When to Avoid | Simple operations without resource management |
