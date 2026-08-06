# Comprehensions

List, dict, set comprehensions and generator expressions.

## Overview

Comprehensions provide concise syntax for creating collections from iterables. They're faster and more readable than equivalent loops.

## When to Use

- Transforming collections
- Filtering data
- Creating dicts/sets from other data
- Replacing map/filter with clearer syntax

## Code Reference

| Concept | File | Lines |
|---------|------|-------|
| List comprehension | `comprehensions.py:5-19` | Filter, nested loops |
| Dict comprehension | `comprehensions.py:23-35` | Invert, filter |
| Set comprehension | `comprehensions.py:39-46` | Unique values |
| Generator expression | `comprehensions.py:50-53` | Lazy evaluation |
| Nested | `comprehensions.py:57-65` | Transpose, deep flatten |
| Walrus operator | `comprehensions.py:69-77` | := in comprehensions |
| Advanced patterns | `comprehensions.py:83-95` | chain, partition |

## Common Mistakes

1. **Overly complex comprehensions** — use a loop if > 2 conditions
2. **Using [] for large data** — use generator expression ()
3. **Side effects in comprehensions** — avoid print() or mutations
4. **Readability vs brevity** — prioritize clarity

## Interview Questions

1. What is the difference between a list comprehension and a generator expression?
2. How do you flatten a nested list with a comprehension?
3. When would you use map/filter over a comprehension?
4. What is the walrus operator and how does it help in comprehensions?
