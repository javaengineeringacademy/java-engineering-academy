# Bridge Pattern (Kotlin)

## Overview

The Bridge pattern decouples an abstraction from its implementation so that the two
can vary independently. Kotlin's interfaces and delegation enable clean bridge
implementations.

## When to Use

- Avoiding permanent binding between abstraction and implementation
- Both abstraction and implementation should be extensible
- Changes in implementation should not affect clients
- Sharing implementation across multiple objects

## Kotlin Implementation

### Interface Bridge

```kotlin
interface Implementation {
    fun operationImpl(): String
}

class ConcreteImplementationA : Implementation {
    override fun operationImpl(): String = "ImplementationA"
}

class ConcreteImplementationB : Implementation {
    override fun operationImpl(): String = "ImplementationB"
}

abstract class Abstraction(protected val implementation: Implementation) {
    abstract fun operation(): String
}

class RefinedAbstraction(implementation: Implementation) : Abstraction(implementation) {
    override fun operation(): String {
        return "Refined: ${implementation.operationImpl()}"
    }
}
```

### Generic Bridge

```kotlin
interface Bridge<T> {
    fun operation(): T
}

class BridgeImpl<T>(private val impl: () -> T) : Bridge<T> {
    override fun operation(): T = impl()
}
```

### Event Bridge

```kotlin
interface Emitter {
    fun emit(event: String, data: Any?)
    fun on(event: String, callback: (Any?) -> Unit)
}

class BridgeAbstraction(private val emitter: Emitter) {
    fun send(event: String, data: Any?) {
        emitter.emit(event, data)
    }
}
```

### Functional Bridge

```kotlin
fun <T> createBridge(implementation: () -> T): () -> T = implementation
```

## Best Practices

- Keep abstraction and implementation hierarchies separate
- Use interfaces for implementation contracts
- Document extension points clearly
- Use Bridge when inheritance hierarchy grows
- Consider using delegation

## Interview Questions

1. How does Bridge differ from Adapter?
2. What is the relationship between Bridge and Strategy?
3. When should you use Bridge over multiple inheritance?
4. How do you extend implementation without changing abstraction?
5. Can Bridge be combined with Abstract Factory?

## References

- Kotlin documentation: Interfaces
- "Kotlin in Action" by Svetlana Isakova
- "Pattern-Oriented Software Architecture" by Buschmann
