# Observer Pattern (Kotlin)

## Overview

The Observer pattern defines a one-to-many dependency between objects so that when one
object changes state, all its dependents are notified. Kotlin's Flow and sealed classes
enable reactive observer implementations.

## When to Use

- Changes to one object require changing others
- Don't know how many objects need to be changed
- Objects should notify observers without coupling
- Event-driven systems

## Kotlin Implementation

### Flow-Based Observer

```kotlin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Observable<T>(initialValue: T) {
    private val _state = MutableStateFlow(initialValue)
    val state: StateFlow<T> = _state.asStateFlow()

    fun update(value: T) {
        _state.value = value
    }
}
```

### Custom Observer

```kotlin
interface Observer<T> {
    fun update(value: T)
}

class Subject<T> {
    private val observers = mutableListOf<Observer<T>>()

    fun attach(observer: Observer<T>) {
        observers.add(observer)
    }

    fun detach(observer: Observer<T>) {
        observers.remove(observer)
    }

    fun notify(value: T) {
        observers.forEach { it.update(value) }
    }
}
```

### LiveData Style

```kotlin
class MutableLiveData<T>(private var value: T) {
    private val observers = mutableListOf<(T) -> Unit>()

    fun observe(observer: (T) -> Unit) {
        observers.add(observer)
        observer(value)
    }

    fun setValue(newValue: T) {
        value = newValue
        observers.forEach { it(newValue) }
    }
}
```

### Event Bus

```kotlin
object EventBus {
    private val channels = mutableMapOf<String, MutableList<(Any) -> Unit>>()

    fun subscribe(event: String, callback: (Any) -> Unit) {
        channels.getOrPut(event) { mutableListOf() }.add(callback)
    }

    fun publish(event: String, data: Any) {
        channels[event]?.forEach { it(data) }
    }
}
```

## Best Practices

- Use Flow for reactive programming
- Implement unsubscribe functionality
- Keep observer interface minimal
- Handle errors in observers
- Document notification order

## Interview Questions

1. What is the difference between Flow and LiveData?
2. How do you prevent memory leaks in Observer?
3. Can observers be notified asynchronously?
4. When should you use events vs custom observer?
5. How do you handle observer errors?

## References

- Kotlin documentation: Coroutines Flow
- "Kotlin in Action" by Svetlana Isakova
- Android LiveData documentation
