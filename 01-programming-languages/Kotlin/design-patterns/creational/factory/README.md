# Factory Pattern (Kotlin)

## Overview

The Factory pattern provides an interface for creating objects without specifying their
concrete classes. Kotlin's companion objects and sealed classes enable concise factory
implementations.

## When to Use

- Object creation logic is complex
- Need to create different types based on input
- Avoiding code duplication in object creation
- Creating objects from configuration

## Kotlin Implementation

### Companion Object Factory

```kotlin
class User private constructor(val name: String, val role: String) {
    companion object {
        fun admin(name: String) = User(name, "Admin")
        fun user(name: String) = User(name, "User")
        fun guest() = User("Guest", "Guest")
    }
}

val admin = User.admin("John")
```

### Sealed Class Factory

```kotlin
sealed class Shape {
    data class Circle(val radius: Double) : Shape()
    data class Rectangle(val width: Double, val height: Double) : Shape()
    data class Triangle(val base: Double, val height: Double) : Shape()

    companion object {
        fun create(type: String, vararg params: Double): Shape {
            return when (type) {
                "circle" -> Circle(params[0])
                "rectangle" -> Rectangle(params[0], params[1])
                "triangle" -> Triangle(params[0], params[1])
                else -> throw IllegalArgumentException("Unknown shape: $type")
            }
        }
    }
}
```

### Generic Factory

```kotlin
interface Factory<T> {
    fun create(): T
}

class UserFactory : Factory<User> {
    override fun create(): User = User("Default", "User")
}

class ConfigFactory(private val name: String) : Factory<Config> {
    override fun create(): Config = Config(name)
}
```

### Factory with Builder

```kotlin
class Vehicle private constructor(
    val type: String,
    val wheels: Int,
    val engine: String
) {
    companion object {
        fun builder() = VehicleBuilder()
    }

    class VehicleBuilder {
        private var type: String = "Car"
        private var wheels: Int = 4
        private var engine: String = "Gasoline"

        fun type(type: String) = apply { this.type = type }
        fun wheels(wheels: Int) = apply { this.wheels = wheels }
        fun engine(engine: String) = apply { this.engine = engine }

        fun build() = Vehicle(type, wheels, engine)
    }
}
```

## Best Practices

- Use companion objects for factory methods
- Consider sealed classes for type safety
- Use generics for reusable factories
- Document return type expectations
- Consider using dependency injection

## Interview Questions

1. What is the difference between Factory and Abstract Factory?
2. When should you use companion object factory?
3. Can factory pattern work with sealed classes?
4. How do you handle factory errors?
5. When is factory better than direct object creation?

## References

- Kotlin documentation: Companion objects
- "Kotlin in Action" by Svetlana Isakova
- "Atomic Kotlin" by Bruce Eckel
