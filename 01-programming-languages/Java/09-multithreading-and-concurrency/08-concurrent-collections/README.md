# 08 - Concurrent Collections

## Overview

Java provides thread-safe collections that handle synchronization internally, offering better performance than wrapping collections with `Collections.synchronized*`.

## Collections Comparison

| Collection | Thread-Safe Alternative | Best For |
|------------|----------------------|----------|
| HashMap | ConcurrentHashMap | Concurrent reads/writes |
| ArrayList | CopyOnWriteArrayList | Read-heavy, rare writes |
| LinkedList | ConcurrentLinkedQueue | Producer-consumer |
| TreeMap | ConcurrentSkipListMap | Sorted concurrent map |

## Key Classes

| Class | Description |
|-------|-------------|
| ConcurrentHashMap | Lock-striped hash map |
| CopyOnWriteArrayList | Copy-on-write list |
| ConcurrentLinkedQueue | Non-blocking queue |
| ArrayBlockingQueue | Bounded blocking queue |
| LinkedBlockingQueue | Optionally bounded blocking queue |
| ConcurrentSkipListMap | Sorted concurrent map |
