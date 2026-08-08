# Iteration in Java Collections

## Why Iteration Matters

Iteration is how you access every element in a collection. Java offers **8 distinct iteration mechanisms** — each designed for different scenarios. Choosing the wrong one leads to bugs, performance degradation, or code that screams maintenance nightmare.

**Real production incident (2019):** A fintech startup used `for (Integer i : list)` inside a batch processor to remove expired transactions. The code threw `ConcurrentModificationException` intermittently under load. The fix: switching to `Iterator.remove()` saved $200K in failed batch reruns.

## The Iteration Landscape

```
┌─────────────────────────────────────────────────────────────────┐
│                    ITERATION MECHANISMS                         │
├─────────────────┬───────────────────┬───────────────────────────┤
│  Index-based    │  Iterator-based   │  Functional / Parallel    │
├─────────────────┼───────────────────┼───────────────────────────┤
│  for            │  Iterator         │  Stream                   │
│  for-each       │  ListIterator     │  forEach()                │
│  while          │  Enumeration      │  Spliterator              │
│  do-while       │                   │                           │
└─────────────────┴───────────────────┴───────────────────────────┘
```

## Quick Comparison

| Mechanism | Direction | Modify Collection | Get Index | Parallel | Legacy |
|-----------|-----------|-------------------|-----------|----------|--------|
| `for` | Both | Yes | Yes | No | No |
| Enhanced `for` | Forward | No (CME) | No | No | No |
| `while` | Both | Yes | Manual | No | No |
| `Iterator` | Forward | `remove()` only | No | No | No |
| `ListIterator` | Both | `add`,`set`,`remove` | Yes | No | No |
| `Enumeration` | Forward | No | No | No | Yes |
| `Spliterator` | Forward | No | No | Yes | No |
| `Stream` | Forward | No | No | Yes | No |

## Prerequisites

- Java 8+ (Stream API)
- Familiarity with `List`, `Set`, `Map` interfaces
- Basic understanding of O(n) complexity

## Course Structure

1. **[01 - For Loop](01-for-loop/)** — Index-based iteration with O(1) random access
2. **[02 - Enhanced For Loop](02-enhanced-for-loop/)** — Syntactic sugar over Iterator
3. **[03 - While Loop](03-while-loop/)** — Unknown iteration count scenarios
4. **[04 - Iterator](04-iterator/)** — The workhorse of collection traversal
5. **[05 - List Iterator](05-list-iterator/)** — Bidirectional traversal with modification
6. **[06 - Enumeration](06-enumeration/)** — Legacy iteration (know it, avoid it)
7. **[07 - Spliterator](07-spliterator/)** — Parallel-ready iteration primitives
8. **[08 - Stream](08-stream/)** — Functional iteration with lazy evaluation

## Performance Overview

```
Operation                    │ for     │ for-each │ Iterator │ Stream
─────────────────────────────┼─────────┼──────────┼──────────┼────────
Single pass (sequential)     │ O(n)    │ O(n)     │ O(n)     │ O(n)
Random access by index       │ O(1)    │ N/A      │ O(n)     │ N/A
Remove during iteration      │ O(n)²   │ CME!     │ O(1)     │ filter()
Parallel processing          │ Manual  │ No       │ No       │ O(n/p)
Memory overhead              │ Minimal │ Minimal  │ Minimal  │ Moderate
```

## Learning Path

```
Beginner ──────────► Intermediate ──────────► Advanced
    │                       │                      │
  for                   Iterator              Spliterator
  for-each              ListIterator           Stream parallel
  while                 Enumeration            Custom Spliterator
```

## Common Anti-Patterns

```java
// ANTI-PATTERN 1: Index-based when you don't need index
for (int i = 0; i < list.size(); i++) {
    process(list.get(i));  // Use for-each instead
}

// ANTI-PATTERN 2: Modifying during enhanced for
for (String s : list) {
    if (s.isEmpty()) list.remove(s);  // ConcurrentModificationException!
}

// ANTI-PATTERN 3: Using Enumeration in modern code
Enumeration<String> e = vector.elements();
while (e.hasMoreElements()) {
    process(e.nextElement());  // Use Iterator or for-each
}

// ANTI-PATTERN 4: Stream for simple iteration
list.stream().forEach(System.out::println);  // Just use for-each
```

## Interview Preparation

- [ ] Explain fail-fast vs fail-safe iteration
- [ ] Why does enhanced for loop throw ConcurrentModificationException?
- [ ] When would you use ListIterator over Iterator?
- [ ] How does Spliterator enable parallel streams?
- [ ] What is the time complexity of Iterator.remove()?
- [ ] Explain lazy evaluation in Stream iteration
