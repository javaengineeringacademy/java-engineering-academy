# Strings

When working with text data, you need efficient ways to manipulate, format, and encode strings. Python's immutable Unicode strings come with powerful built-in methods for processing, formatting, encoding, and manipulating text data.

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

## Production Checklist

### ✅ Before using strings in production:

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

### ❌ Myth 1: Strings are mutable in Python
**Reality:** Strings are immutable; operations create new string objects.

### ❌ Myth 2: f-strings are always faster than .format()
**Reality:** f-strings are faster due to compile-time evaluation, but the difference is minimal for simple cases.

### ❌ Myth 3: `+` is efficient for string concatenation
**Reality:** `+` creates new strings each time; use `join()` for multiple concatenations.

## One-Minute Revision

| Aspect | Value |
|--------|-------|
| Purpose | Process and manipulate text |
| Complexity | O(n) for most operations |
| Thread Safe | Yes (immutable objects) |
| Best Alternative | Use re module for complex patterns |
| When to Use | Text processing, formatting, parsing |
| When to Avoid | Concatenation in loops, ignoring encoding |
