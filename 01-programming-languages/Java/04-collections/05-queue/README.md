# Queue Interface

## Overview

The `Queue` interface is designed for holding elements prior to processing. It provides operations for inserting, extracting, and inspecting elements. Queue typically orders elements in a FIFO (First-In-First-Out) manner, but priority queues order elements by priority.

## Learning Objectives

- Understand the Queue interface and its contract
- Learn the difference between throw-exception and return-value methods
- Master Queue operations: offer, poll, peek, add, remove, element
- Understand FIFO vs priority ordering
- Compare ArrayDeque vs LinkedList for queue operations
- Learn about bounded vs unbounded queues

## Queue Operations

| Operation | Throw Exception | Return Special Value |
|-----------|-----------------|---------------------|
| Insert | `add(e)` | `offer(e)` |
| Remove | `remove()` | `poll()` |
| Examine | `element()` | `peek()` |

## Implementations

- **ArrayDeque**: Resizable array-based deque (fastest for queue operations)
- **LinkedList**: Doubly-linked list (also implements List)
- **PriorityQueue**: Binary heap (priority-based ordering)

## When to Use Each

- **ArrayDeque**: Best for FIFO queue and deque operations (faster than LinkedList)
- **PriorityQueue**: Need priority-based processing (min-heap by default)
- **LinkedList**: Legacy choice; prefer ArrayDeque for queue/deque use cases

## Subtopics

- [PriorityQueue](01-priorityqueue/)
- [Deque](02-deque/)
