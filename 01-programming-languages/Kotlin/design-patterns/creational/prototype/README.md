# Prototype Pattern (Kotlin)

## Overview

The Prototype pattern creates new objects by cloning existing instances. Kotlin's
data classes with copy() function provide built-in prototype support.

## When to Use

- Creating objects expensive to construct
- When object creation is complex
- Need many similar objects
- Avoiding subclassing for object creation

## Kotlin Implementation

### Data Class Copy

```kotlin
data class User(
    val name: String,
    val email: String,
    val age: Int,
    val settings: Map<String, String> = emptyMap()
)

val original = User("John", "john@example.com", 30, mapOf("theme" to "dark"))
val copy = original.copy(name = "Jane", age = 25)
```

### Deep Copy

```kotlin
data class Address(val city: String, val country: String)

data class Employee(
    val name: String,
    val address: Address
) {
    fun deepCopy(): Employee {
        return Employee(name, address.copy())
    }
}
```

### Cloneable Interface

```kotlin
interface Cloneable<T> {
    fun clone(): T
}

class Prototype(private var value: Int) : Cloneable<Prototype> {
    override fun clone(): Prototype {
        return Prototype(value)
    }

    fun setValue(value: Int) {
        this.value = value
    }

    fun getValue(): Int = value
}
```

### Prototype Registry

```kotlin
class PrototypeRegistry<T : Cloneable<T>> {
    private val prototypes = mutableMapOf<String, T>()

    fun register(key: String, prototype: T) {
        prototypes[key] = prototype
    }

    fun create(key: String): T? {
        return prototypes[key]?.clone()
    }
}
```

### Builder Pattern with Copy

```kotlin
data class Configuration(
    val host: String = "localhost",
    val port: Int = 8080,
    val debug: Boolean = false
) {
    fun withHost(host: String) = copy(host = host)
    fun withPort(port: Int) = copy(port = port)
    fun withDebug(debug: Boolean) = copy(debug = debug)
}

val config = Configuration().withHost("example.com").withPort(443)
```

## Best Practices

- Use data classes for automatic copy support
- Implement deep copy for nested objects
- Consider using clone() for mutable objects
- Document clone semantics (shallow vs deep)
- Use copy() for immutable updates

## Interview Questions

1. What is the difference between shallow and deep copy?
2. How does Kotlin's copy function work?
3. When should you use copy vs clone?
4. Can you implement prototype without data classes?
5. How do you handle circular references in copying?

## References

- Kotlin documentation: Data classes
- "Kotlin in Action" by Svetlana Isakova
- "Atomic Kotlin" by Bruce Eckel
