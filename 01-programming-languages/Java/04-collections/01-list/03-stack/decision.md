# Stack Decision Guide

## Decision Tree

```
Need a LIFO stack?
├── Is this new code? → Use ArrayDeque (not Stack)
├── Is this legacy code? → Stack (but plan migration)
├── Need thread safety? → ArrayDeque + synchronized block
└── Need bounded stack? → Use ArrayDeque with capacity check
```

## Comparison Matrix

| Feature | Stack | ArrayDeque | LinkedList |
|---------|-------|------------|------------|
| push/pop | O(1) | O(1) | O(1) |
| Thread-safe | Yes (all methods) | No | No |
| Memory | Medium | Low | High |
| Legacy | Yes | No | No |
| Implements | Vector (List) | Deque | List + Deque |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| New code | ArrayDeque | Faster, no legacy overhead |
| Legacy code | Stack | Don't break existing code |
| Thread-safe stack | ArrayDeque + synchronized | Better performance |
| Bounded stack | ArrayDeque + capacity | Custom implementation |

## Production Recommendations

> **Never use Stack in new code** — it's legacy and extends Vector with synchronized overhead.

> **Use ArrayDeque for stack operations** — it's faster and has better cache locality.

> **Migrate from Stack to ArrayDeque** — it's a simple replacement with better performance.

> **Stack extends Vector** — it inherits all Vector's synchronized overhead.

## Engineering Trade-offs

| Trade-off | Stack | Alternative |
|-----------|-------|-------------|
| Legacy vs Modern | Legacy | ArrayDeque: modern, faster |
| Thread-safety vs Performance | Synchronized (slow) | ArrayDeque: fast, no safety |
| Memory vs Performance | Medium memory | ArrayDeque: low memory, fast |
| Simplicity vs Performance | Simple | ArrayDeque: simple and fast |

## Common Code Review Comments

- "Why are you using Stack? Use ArrayDeque instead."
- "Stack is legacy — plan migration to ArrayDeque."
- "This Stack should be an ArrayDeque for better performance."
- "Stack extends Vector — it has synchronized overhead."

## Common Production Mistakes

> Notice: Stack is deprecated for removal in Java 9+ — plan migration to ArrayDeque.

> Notice: Stack extends Vector — it inherits all Vector's synchronized overhead.

> Notice: Stack.toString() is synchronized — it can cause contention in concurrent code.

> Notice: Stack is legacy — it was part of Java 1.0, before the Collections Framework.
