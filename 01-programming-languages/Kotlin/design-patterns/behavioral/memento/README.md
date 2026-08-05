# Memento Pattern (Kotlin)

## Overview

The Memento pattern provides the ability to restore an object to its previous state.
Kotlin's data classes and copy functions enable clean memento implementations.

## When to Use

- Need to save and restore object state
- Implementing undo/redo functionality
- Capturing snapshots without exposing internals
- Transaction rollback mechanisms

## Kotlin Implementation

### Data Class Memento

```kotlin
data class Memento<T>(
    val state: T,
    val timestamp: Long = System.currentTimeMillis()
)

class Originator<T>(private var state: T) {
    fun setState(state: T) {
        this.state = state
    }

    fun getState(): T = state

    fun save(): Memento<T> = Memento(state)

    fun restore(memento: Memento<T>) {
        state = memento.state
    }
}
```

### Caretaker

```kotlin
class Caretaker<T>(private val originator: Originator<T>) {
    private val history = mutableListOf<Memento<T>>()
    private var currentIndex = -1

    fun save() {
        history.add(originator.save())
        currentIndex++
    }

    fun undo() {
        if (currentIndex > 0) {
            currentIndex--
            originator.restore(history[currentIndex])
        }
    }

    fun redo() {
        if (currentIndex < history.size - 1) {
            currentIndex++
            originator.restore(history[currentIndex])
        }
    }
}
```

### Snapshot Pattern

```kotlin
class Snapshot<T>(initialState: T) {
    private var state: T = initialState
    private val history = mutableListOf<T>()

    fun getState(): T = state

    fun setState(newState: T) {
        history.add(state)
        state = newState
    }

    fun undo() {
        if (history.isNotEmpty()) {
            state = history.removeLast()
        }
    }
}
```

### Immutable Memento

```kotlin
data class ImmutableState(
    val value: Int,
    val name: String
)

fun ImmutableState.update(block: ImmutableState.() -> ImmutableState): ImmutableState {
    return this.block()
}
```

## Best Practices

- Use data classes for automatic copy support
- Consider using copy() for immutable updates
- Limit history size to prevent memory issues
- Document state capture semantics
- Use memento for transactional operations

## Interview Questions

1. What is the difference between Memento and Command?
2. How do you handle large object states?
3. Can memento be used across sessions?
4. When should you use Memento vs Command for undo?
5. How do you implement memento with data classes?

## References

- Kotlin documentation: Data classes
- "Kotlin in Action" by Svetlana Isakova
- "Head First Design Patterns" by Freeman
