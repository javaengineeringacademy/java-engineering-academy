# 10 - ThreadLocal

## Overview

`ThreadLocal` provides per-thread storage. Each thread accessing a ThreadLocal variable has its own independent copy. Commonly used for user sessions, database connections, and SimpleDateFormat.

## Key Concepts

| Concept | Description |
|---------|-------------|
| ThreadLocal | Per-thread storage |
| InheritableThreadLocal | Inherited from parent thread |
| initialValue() | Provide default value |
| withInitial() | Factory method |

## Warning

ThreadLocal values are NOT cleaned up when threads are reused in pools. Always call `remove()` in finally blocks to prevent memory leaks.
