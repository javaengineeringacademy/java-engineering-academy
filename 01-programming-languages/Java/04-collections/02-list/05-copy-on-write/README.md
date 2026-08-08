# CopyOnWriteArrayList

## Overview

`CopyOnWriteArrayList` is a thread-safe variant of `ArrayList` where all mutative operations (`add`, `set`, `remove`) are implemented by creating a fresh copy of the underlying array. This makes it ideal for read-heavy, write-rarely scenarios like listener lists.

## Learning Objectives

- Understand Copy-On-Write mechanism
- Learn when to use CopyOnWriteArrayList vs synchronized alternatives
- Understand thread-safe iteration without `ConcurrentModificationException`
- Recognize performance trade-offs

## How It Works

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();

// Reads are lock-free (direct array access)
String element = list.get(0);

// Writes create a new copy of the array
list.add("new element");  // Creates new array, copies all elements

// Iteration works on snapshot
Iterator<String> it = list.iterator();
list.add("another");  // No ConcurrentModificationException
it.next();  // Still sees original snapshot
```

## When to Use

- **Read-heavy, write-rarely**: Listener lists, observer patterns
- **Iteration during modification**: When you can't avoid modifying during iteration
- **Small lists**: Copy cost is proportional to list size

## When NOT to Use

- **Write-heavy**: Each write copies the entire array
- **Large lists**: Copying becomes expensive
- **Real-time consistency needed**: Iterators see stale snapshots

## Comparison

| Feature | CopyOnWriteArrayList | Collections.synchronizedList | ArrayList |
|---------|---------------------|------------------------------|-----------|
| Thread-safe | Yes | Yes | No |
| Iterator | Snapshot (fail-safe) | Fail-fast (must sync externally) | Fail-fast |
| Read performance | Excellent (lock-free) | Good (lock per op) | Best |
| Write performance | Poor (copies array) | Good (lock per op) | Best |
| Memory overhead | High (array copy) | Low | Lowest |

## Example: Listener List

```java
public class EventManager {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    public void fireEvent(Event event) {
        // Safe to iterate while other threads add/remove listeners
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
```
