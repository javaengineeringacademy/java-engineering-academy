# Internals

## Overview

This section dives deep into the internal implementation details of Java's most commonly used collections. Understanding internals helps you make informed decisions about performance, memory usage, and debugging.

## Topics

### ArrayList Internals
- Backed by `Object[]` array
- Default initial capacity: 10
- Growth factor: 1.5x (`newCapacity = oldCapacity + (oldCapacity >> 1)`)
- `add()` at end: amortized O(1), worst O(n) on resize
- `add(index)` / `remove(index)`: O(n) due to element shifting

### HashMap Internals
- Array of `Node<K,V>` buckets (default capacity 16, load factor 0.75)
- Key's `hashCode()` determines bucket: `hash(key) & (n-1)`
- Collisions handled by linked list (Java 8+: treeifies at 8 entries)
- Resize when `size > capacity * loadFactor`

### Cache Locality
- ArrayList stores elements contiguously (cache-friendly)
- LinkedList nodes scattered in heap (cache-unfriendly)
- ArrayDeque uses circular array (cache-friendly)
- Cache line size: typically 64 bytes
- Sequential access patterns benefit from prefetching

## Why This Matters

1. **Performance**: Understanding internals explains why ArrayList beats LinkedList for most operations
2. **Debugging**: Knowing modCount mechanism helps debug ConcurrentModificationException
3. **Memory**: Understanding array sizing helps predict memory usage
4. **Scalability**: Knowing resize triggers helps optimize for large datasets

## Subtopics

- [ArrayList Internals](arraylist-internals/)
- [HashMap Internals](hashmap-internals/)
- [Cache Locality](cache-locality/)
