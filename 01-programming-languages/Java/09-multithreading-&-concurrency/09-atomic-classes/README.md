# 09 - Atomic Classes

## Overview

Atomic classes in `java.util.concurrent.atomic` provide lock-free, thread-safe operations on single variables using CAS (Compare-And-Swap) hardware instructions.

## Key Classes

| Class | Description |
|-------|-------------|
| AtomicInteger | Thread-safe int |
| AtomicLong | Thread-safe long |
| AtomicBoolean | Thread-safe boolean |
| AtomicReference<V> | Thread-safe reference |
| AtomicIntegerArray | Thread-safe int array |
| LongAdder | High-throughput counter |

## Key Methods

| Method | Description |
|--------|-------------|
| get() / set() | Read / write |
| incrementAndGet() | ++i atomically |
| getAndIncrement() | i++ atomically |
| compareAndSet(expected, update) | CAS operation |
| updateAndGet(UnaryOperator) | Atomic update |
