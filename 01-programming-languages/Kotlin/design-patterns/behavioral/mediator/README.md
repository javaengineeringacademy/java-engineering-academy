# Mediator Pattern (Kotlin)

## Overview

The Mediator pattern defines an object that encapsulates how a set of objects interact.
Kotlin's sealed classes and coroutine flows enable type-safe mediator implementations.

## When to Use

- Set of objects communicate in complex ways
- Reuse object is difficult due to dependencies
- Custom behavior distributed across several classes
- Event-driven communication systems

## Kotlin Implementation

### Channel-Based Mediator

```kotlin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class Mediator {
    private val channel = Channel<Pair<String, Any>>(Channel.BUFFERED)

    suspend fun send(sender: String, message: Any) {
        channel.send(Pair(sender, message))
    }

    suspend fun receive(): Pair<String, Any> {
        return channel.receive()
    }
}
```

### Flow-Based Mediator

```kotlin
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventMediator {
    private val _events = MutableSharedFlow<Pair<String, Any>>()
    val events: SharedFlow<Pair<String, Any>> = _events.asSharedFlow()

    suspend fun publish(sender: String, event: Any) {
        _events.emit(Pair(sender, event))
    }
}
```

### Class-Based Mediator

```kotlin
interface Mediator {
    fun notify(sender: Colleague, event: String)
}

abstract class Colleague(protected val mediator: Mediator) {
    fun send(event: String) {
        mediator.notify(this, event)
    }

    abstract fun receive(message: String)
}
```

### Event Bus

```kotlin
object EventBus {
    private val channels = mutableMapOf<String, MutableList<suspend (Any) -> Unit>>()

    suspend fun subscribe(event: String, callback: suspend (Any) -> Unit) {
        channels.getOrPut(event) { mutableListOf() }.add(callback)
    }

    suspend fun publish(event: String, data: Any) {
        channels[event]?.forEach { it(data) }
    }
}
```

## Best Practices

- Use channels for coroutine-based communication
- Keep mediator focused on coordination
- Avoid putting business logic in mediator
- Document component communication patterns
- Consider using sealed classes for type safety

## Interview Questions

1. How does Mediator differ from Observer?
2. Can mediator handle asynchronous communication?
3. How do you test code with mediator?
4. When should you avoid using Mediator?
5. How do you handle mediator in microservices?

## References

- Kotlin documentation: Coroutines Channels
- "Kotlin in Action" by Svetlana Isakova
- "Head First Design Patterns" by Freeman
