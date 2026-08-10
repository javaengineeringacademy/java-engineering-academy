# LinkedList Decision Guide

## Decision Tree

```
Need a linked structure?
├── Need queue/deque operations? → ArrayDeque (not LinkedList)
├── Need frequent insert/remove at known position? → LinkedList
├── Need random access by index? → ArrayList (not LinkedList)
├── Need thread safety? → Collections.synchronizedLinkedList() (but ArrayDeque is faster)
└── Need memory efficiency? → ArrayList (not LinkedList)
```

## Comparison Matrix

| Feature | LinkedList | ArrayList | ArrayDeque |
|---------|------------|-----------|------------|
| get(i) | O(n) | O(1) | O(n) |
| addFirst/addLast | O(1) | O(n)/O(1) | O(1) |
| add(mid) | O(1) | O(n) | O(n) |
| removeFirst/removeLast | O(1) | O(n)/O(1) | O(1) |
| Memory | High (16-24 bytes/element) | Low | Low |
| Cache locality | Poor | Excellent | Excellent |

## Selection Criteria

| Requirement | Recommended Choice | Why |
|-------------|-------------------|-----|
| Queue/deque operations | ArrayDeque | Faster than LinkedList |
| Insert/remove at known position | LinkedList | O(1) at cursor |
| Random access | ArrayList | O(1) get() |
| Memory efficiency | ArrayList | Lower memory overhead |
| Iteration speed | ArrayList | Better cache locality |

## Production Recommendations

> **Avoid LinkedList** — ArrayDeque is faster for queue/deque operations, ArrayList is faster for list operations.

> **Only use LinkedList if** you've measured and confirmed it's faster for your specific use case.

> **Use ArrayDeque for stack/queue** — it's faster than LinkedList and has better cache locality.

> **LinkedList memory overhead is significant** — each node has prev+next pointers (16-24 bytes per element).

## Engineering Trade-offs

| Trade-off | LinkedList | Alternative |
|-----------|------------|-------------|
| Insert/remove vs Random access | Fast insert, slow get | ArrayList: fast get, slow insert |
| Memory vs Cache locality | High memory, poor cache | ArrayList: low memory, excellent cache |
| Simplicity vs Performance | Simple but slow | ArrayDeque: simple and fast |
| Queue vs List | LinkedList implements both | ArrayDeque: queue-only, faster |

## Common Code Review Comments

- "Why are you using LinkedList? ArrayDeque is faster for queue operations."
- "This LinkedList has high memory overhead — consider ArrayList."
- "ArrayDeque is faster for stack/queue operations."
- "Have you measured that LinkedList is actually faster?"

## Common Production Mistakes

> Notice: LinkedList memory overhead is significant — each node has prev+next pointers (16-24 bytes per element).

> Notice: LinkedList.get(i) is O(n) — don't use it for random access.

> Notice: LinkedList is not thread-safe — even for reads, concurrent modification can cause data corruption.

> Notice: LinkedList implements both List and Deque — but ArrayDeque is faster for Deque operations.
