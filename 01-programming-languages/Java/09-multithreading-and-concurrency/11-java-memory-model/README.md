# 11 - Java Memory Model

## Overview

The Java Memory Model (JMM) defines how threads interact through memory. It specifies happens-before relationships that guarantee visibility of writes across threads.

## Core Concepts

| Concept | Description |
|---------|-------------|
| Happens-Before | Order guarantee for memory operations |
| Memory Visibility | Whether a thread sees writes from other threads |
| Atomicity | Whether an operation completes indivisibly |
| Reordering | Compiler/CPU may reorder instructions for performance |

## Happens-Before Rules

1. **Program order**: Each action happens-before next in same thread
2. **Monitor lock**: Unlock happens-before subsequent lock
3. **Volatile**: Write happens-before subsequent read
4. **Thread.start()**: All actions in started thread happen-before start() returns
5. **Thread.join()**: All actions in thread happen-before join() returns
6. **Transitivity**: If A hb B and B hb C, then A hb C
