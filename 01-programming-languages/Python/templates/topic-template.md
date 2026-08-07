# Topic Template

Use this template for individual topic READMEs within a module (e.g., `01-variables/README.md`, `02-oop/README.md`).

---

## Template

```markdown
# [Topic Name]

[One sentence explaining the real-world problem this topic solves.]

## What

[2-3 paragraphs defining the concept. What is it? How does it work internally?]

## Why

- **[Reason 1]:** [Explanation]
- **[Reason 2]:** [Explanation]
- **[Reason 3]:** [Explanation]

## When

Use [topic] when:
- [Use case 1]
- [Use case 2]
- [Use case 3]

Avoid [topic] when:
- [Anti-pattern 1]
- [Anti-pattern 2]

## How

### [Subtopic 1]

```python
# Working code example
# Should be runnable and produce output
```

### [Subtopic 2]

```python
# Working code example
```

## Production Checklist

### Before using in production:

- [ ] I know the time/space complexity
- [ ] I know thread safety guarantees
- [ ] I know memory impact
- [ ] I know common mistakes
- [ ] I know alternatives
- [ ] I know limitations
- [ ] I know how to debug it

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

### Myth 1: [Common misconception]
**Reality:** [Correct understanding]

### Myth 2: [Common misconception]
**Reality:** [Correct understanding]

### Myth 3: [Common misconception]
**Reality:** [Correct understanding]

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | [What it does] |
| Complexity | [Time/space] |
| Thread Safe | [Yes/No/Depends] |
| Best Alternative | [When to use something else] |
| When to Use | [Primary use cases] |
| When to Avoid | [Known limitations] |
```

---

## Formatting Guidelines

- First line must state the problem, not the solution: "When building APIs that validate input" not "Pydantic is a library"
- "What" section defines the concept for someone who's never heard of it
- "Why" must have 3+ reasons — students need motivation
- "When" must include both positive and negative guidance
- "How" code examples must be runnable standalone
- "Production Checklist" is the same across all topics — copy as-is
- "Maturity Levels" is the same across all topics — copy as-is
- "Common Myths" must address real misconceptions students have
- "One-Minute Revision" must be scannable in 30 seconds

---

## Example: Context Managers

```markdown
# Context Managers

When building applications that manage external resources like files, database connections, or network sockets, you need a reliable way to ensure cleanup happens even if errors occur. Python's context managers guarantee resource acquisition and release follow a predictable pattern.

## What

Context managers are objects that implement the `__enter__` and `__exit__` methods, providing a `with` statement protocol for resource management. When you write `with resource as r:`, Python calls `resource.__enter__()` at the start and `resource.__exit__()` at the end — even if an exception occurs.

The `__exit__` method receives exception information (type, value, traceback) and can suppress exceptions by returning `True`. This makes context managers ideal for cleanup logic that must run regardless of success or failure.

## Why

- **Guaranteed cleanup:** Resources are released even if exceptions occur, preventing leaks
- **Readable code:** The `with` block clearly shows the scope of resource usage
- **Composability:** Context managers can be nested and combined with `contextlib`

## When

Use context managers when:
- Opening files that must be closed
- Managing database connections or transactions
- Acquiring and releasing locks
- Starting and stopping timers or profilers
- Temporary state changes that must be reverted

Avoid context managers when:
- Resources have long lifetimes managed by a connection pool
- You need the resource to outlive the `with` block
- Setup/teardown logic is trivial and overhead isn't justified

## How

### File Handling

```python
# Bad — file may not close on error
f = open("data.txt", "r")
content = f.read()
f.close()

# Good — guaranteed close
with open("data.txt", "r") as f:
    content = f.read()
```

### Custom Context Manager

```python
from contextlib import contextmanager

@contextmanager
def timer(label):
    import time
    start = time.time()
    try:
        yield
    finally:
        elapsed = time.time() - start
        print(f"{label}: {elapsed:.4f}s")

with timer("Sort"):
    sorted(range(1000000))
```

### Database Transaction

```python
from contextlib import contextmanager

@contextmanager
def transaction(conn):
    cursor = conn.cursor()
    try:
        yield cursor
        conn.commit()
    except Exception:
        conn.rollback()
        raise
    finally:
        cursor.close()
```

## Production Checklist

### Before using in production:

- [ ] I know the time/space complexity
- [ ] I know thread safety guarantees
- [ ] I know memory impact
- [ ] I know common mistakes
- [ ] I know alternatives
- [ ] I know limitations
- [ ] I know how to debug it

## Engineering Maturity Levels

### Level 1: Can Use
- Knows basic `with` syntax
- Can use built-in context managers

### Level 2: Understands
- Knows `__enter__`/`__exit__` protocol
- Understands exception suppression

### Level 3: Deep Knowledge
- Can write custom context managers
- Knows `contextlib` utilities

### Level 4: Expert
- Can design complex resource management patterns
- Understands context manager stacking

### Level 5: Master
- Can debug resource leaks in production
- Can design async context managers

## Common Myths

### Myth 1: Context managers are only for files
**Reality:** Context managers work for any resource that needs acquire/release: locks, database connections, network sockets, timers, and temporary state changes.

### Myth 2: `finally` blocks make context managers unnecessary
**Reality:** `finally` works, but context managers are reusable, composable, and separate cleanup logic from the code block. A context manager can be imported and used anywhere; a `finally` block cannot.

### Myth 3: Context managers have significant performance overhead
**Reality:** The overhead is negligible — one method call at entry and one at exit. Profile before optimizing; context managers rarely appear in hot paths.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Guarantee resource cleanup |
| Complexity | O(1) overhead |
| Thread Safe | Yes (per-instance) |
| Best Alternative | `try/finally` for simple cases |
| When to Use | Any resource needing acquire/release |
| When to Avoid | Resources managed externally (connection pools) |
```

---

## Checklist

Before publishing a topic README:

- [ ] Title is specific (not "Advanced Python")
- [ ] "What" defines the concept in plain language
- [ ] "Why" has 3+ reasons
- [ ] "When" includes both positive and negative guidance
- [ ] "How" code examples are runnable
- [ ] "Production Checklist" is complete
- [ ] "Maturity Levels" are filled in
- [ ] "Common Myths" address real misconceptions
- [ ] "One-Minute Revision" table is scannable
