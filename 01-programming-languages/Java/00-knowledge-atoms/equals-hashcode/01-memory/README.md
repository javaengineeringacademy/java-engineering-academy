# Memory: Equals & HashCode

## Overview
This folder contains memory-related demonstrations for equals() and hashCode().

## Files
- `EqualsHashCodeMemory.java` — Demonstrates memory implications of equals/hashCode in collections

## What You'll Learn
- Memory overhead of HashMap entries (buckets, linked lists, tree nodes)
- How hashCode() quality affects memory usage in hash tables
- Memory retention of cached objects in hash-based collections
- Impact of equals/hashCode contract violations on memory leaks

## Key Concepts
- HashMap entry overhead: key reference + value reference + hash + next pointer
- Linked list vs red-black tree bucket storage (treeify threshold = 8)
- Memory cost of poor hashCode() distribution
