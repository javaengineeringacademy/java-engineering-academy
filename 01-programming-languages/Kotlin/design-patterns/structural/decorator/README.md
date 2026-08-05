# Decorator Pattern (Kotlin)

## Overview

The Decorator pattern attaches additional responsibilities to objects dynamically.
Kotlin's delegation pattern and extension functions enable elegant decorator
implementations.

## When to Use

- Adding responsibilities dynamically
- Extending functionality without subclassing
- Creating layered behaviors
- Implementing cross-cutting concerns

## Kotlin Implementation

### Delegation Pattern

```kotlin
interface Car {
    fun drive(): String
}

class SportsCar : Car {
    override fun drive(): String = "Driving sports car"
}

class DecoratedCar(private val car: Car) : Car by car {
    override fun drive(): String {
        return car.drive() + " with turbo"
    }
}
```

### Extension Function Decorator

```kotlin
fun Car.withGPS(): Car {
    val original = this
    return object : Car by original {
        override fun drive(): String {
            return original.drive() + " with GPS"
        }
    }
}
```

### Generic Decorator

```kotlin
interface Wrapper<T> {
    fun wrap(value: T): T
}

class LoggingDecorator<T>(private val logger: (String) -> Unit) : Wrapper<T> {
    override fun wrap(value: T): T {
        logger("Wrapping: $value")
        return value
    }
}
```

### Middleware Pattern

```kotlin
typealias Middleware<T> = suspend (T, suspend () -> Unit) -> Unit

class Pipeline<T> {
    private val middlewares = mutableListOf<Middleware<T>>()

    fun use(middleware: Middleware<T>) {
        middlewares.add(middleware)
    }

    suspend fun execute(context: T) {
        var index = 0
        val next: suspend () -> Unit = {
            if (index < middlewares.size) {
                val middleware = middlewares[index++]
                middleware(context, next)
            }
        }
        next()
    }
}
```

## Best Practices

- Use delegation for transparent decoration
- Keep decorators focused and small
- Document decorator behavior clearly
- Consider using middleware for complex scenarios
- Make decorators composable

## Interview Questions

1. How does Decorator differ from inheritance?
2. What is the delegation pattern in Kotlin?
3. Can you stack multiple decorators?
4. When should you use Decorator vs Proxy?
5. How do you handle decorator ordering?

## References

- Kotlin documentation: Delegation
- "Kotlin in Action" by Svetlana Isakova
- "Atomic Kotlin" by Bruce Eckel
