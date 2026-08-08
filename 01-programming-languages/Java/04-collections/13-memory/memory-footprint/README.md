# Memory Footprint

## Overview

Different collections have different memory overhead per element. Understanding memory footprint helps you choose the right collection for memory-sensitive applications.

## Memory Overhead Per Element

| Collection | Overhead per Element | Total per Element |
|------------|---------------------|-------------------|
| `ArrayList` | 4-8 bytes (reference) + array slots | ~8 bytes |
| `LinkedList` | 24-32 bytes (node + two pointers) | ~40 bytes |
| `HashSet` | 32-48 bytes (HashMap entry) | ~48 bytes |
| `HashMap` | 32-48 bytes (Entry + hash + next) | ~48 bytes |
| `TreeSet` | 40-56 bytes (TreeNode) | ~56 bytes |
| `TreeMap` | 40-56 bytes (TreeNode) | ~56 bytes |
| `ArrayDeque` | 4-8 bytes (reference) + array slots | ~8 bytes |
| `PriorityQueue` | 4-8 bytes (reference) + array slots | ~8 bytes |

## Object Header

Every Java object has a 12-byte header (mark word + klass pointer on 64-bit JVMs). This is per-object, not per-element.

## Array vs Linked Structure

```
ArrayList (3 elements):
- ArrayList object: 32 bytes
- Object[] array: 24 + (capacity × 8) bytes
- 3 String objects: ~150 bytes (varies)
- Total: ~206 bytes (68 bytes per element)

LinkedList (3 elements):
- LinkedList object: 32 bytes
- 3 Node objects: 3 × 40 = 120 bytes
- 3 String objects: ~150 bytes (varies)
- Total: ~302 bytes (100 bytes per element)
```

## Key Takeaways

1. ArrayList uses ~8 bytes per element reference vs LinkedList's ~40 bytes per node
2. HashMap/HashSet entries are expensive (~48 bytes each)
3. Object headers add 12 bytes per object (including nodes, entries)
4. Pre-sizing collections reduces resizing overhead and memory fragmentation
5. For memory-sensitive applications, consider arrays or primitive-specialized libraries (Eclipse Collections, HPPC)
