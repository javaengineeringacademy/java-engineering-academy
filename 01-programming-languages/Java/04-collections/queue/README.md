# Queue Interface

## 1. What Is It

The `Queue` interface is a collection designed for holding elements prior to processing. It provides FIFO (first-in-first-out) operations, with methods to insert, extract, and inspect elements.

## 2. Characteristics

| Characteristic | Description |
|----------------|-------------|
| FIFO | First element added is first removed |
| Ordering | Insertion order |
| Duplicates | Allowed |
| Null | Depends on implementation |
| Two-ended | Deque extends Queue |

## 3. Queue Contract

### Two Sets of Operations

| Operation | Throws Exception | Returns Special Value |
|-----------|-----------------|----------------------|
| Insert | `add(e)` | `offer(e)` |
| Remove | `remove()` | `poll()` |
| Inspect | `element()` | `peek()` |

**When to use which:**
- Throws exception: When failure is unacceptable
- Returns special value: When you want to handle failure gracefully

### Methods Detail

| Method | Description | Returns |
|--------|-------------|---------|
| `add(E e)` | Inserts element | true / throws IllegalStateException |
| `offer(E e)` | Inserts element | true / false |
| `remove()` | Removes head | head / throws NoSuchElementException |
| `poll()` | Removes head | head / null |
| `element()` | Peeks at head | head / throws NoSuchElementException |
| `peek()` | Peeks at head | head / null |

## 4. Implementations Overview

| Implementation | Structure | Ordering | Bounded | Thread-Safe |
|---------------|-----------|----------|---------|-------------|
| PriorityQueue | Binary heap | Priority order | No | No |
| ArrayDeque | Resizable array | FIFO/LIFO | No | No |
| LinkedList | Doubly-linked list | FIFO | No | No |
| BlockingQueue | Interface | FIFO | Yes | Yes |
| ArrayBlockingQueue | Array | FIFO | Yes | Yes |
| LinkedBlockingQueue | Linked nodes | FIFO | Optional | Yes |
| PriorityBlockingQueue | Binary heap | Priority | No | Yes |

## 5. When to Use Each

| Use Case | Implementation |
|----------|---------------|
| Simple FIFO | ArrayDeque |
| Priority processing | PriorityQueue |
| Bounded queue | ArrayBlockingQueue |
| Producer-consumer | LinkedBlockingQueue |
| LIFO (stack) | ArrayDeque (as Deque) |

## 6. Common Mistakes

1. **Using add()/remove() in concurrent code**: Use offer()/poll() for graceful failure
2. **Not handling null from poll()**: poll() returns null when empty
3. **Using LinkedList as Queue**: ArrayDeque is faster and more memory efficient

## 7. One-Minute Revision

- FIFO collection for processing elements
- Two API styles: exception-throwing and special-value returning
- PriorityQueue: elements processed by priority, not insertion order
- ArrayDeque: fastest FIFO/LIFO implementation
- BlockingQueue: thread-safe, bounded, for producer-consumer patterns

## 8. References

- [Oracle Java Documentation - Queue](https://docs.oracle.com/javase/8/docs/api/java/util/Queue.html)
- [Java Collections Framework Tutorial](https://docs.oracle.com/javase/tutorial/collections/)
