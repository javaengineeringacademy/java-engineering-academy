# Singleton Pattern (Kotlin)

## Overview

The Singleton pattern ensures a class has only one instance. Kotlin's object keyword
provides built-in singleton support with thread-safe lazy initialization.

## When to Use

- Managing global state
- Database connections
- Configuration objects
- Logging services
- Cache implementations

## Kotlin Implementation

### Object Keyword

```kotlin
object Database {
    private var connection: Connection? = null

    fun connect(): Connection {
        if (connection == null) {
            connection = createConnection()
        }
        return connection!!
    }

    private fun createConnection(): Connection {
        println("Creating database connection")
        return Connection()
    }
}

class Connection
```

### Companion Object Factory

```kotlin
class Singleton private constructor() {
    companion object {
        private var instance: Singleton? = null

        fun getInstance(): Singleton {
            if (instance == null) {
                instance = Singleton()
            }
            return instance!!
        }
    }

    fun doWork() {
        println("Working...")
    }
}
```

### Lazy Initialization

```kotlin
val instance: Database by lazy {
    Database().also {
        println("Database initialized")
    }
}
```

### Thread-Safe Singleton

```kotlin
class ThreadSafeSingleton private constructor() {
    companion object {
        @Volatile
        private var instance: ThreadSafeSingleton? = null

        fun getInstance(): ThreadSafeSingleton {
            return instance ?: synchronized(this) {
                instance ?: ThreadSafeSingleton().also { instance = it }
            }
        }
    }
}
```

### Enum Singleton

```kotlin
enum class Configuration {
    INSTANCE;

    val settings: MutableMap<String, Any> = mutableMapOf()

    fun get(key: String): Any? = settings[key]
    fun set(key: String, value: Any) {
        settings[key] = value
    }
}
```

## Best Practices

- Use object keyword for simple singletons
- Prefer lazy initialization for expensive objects
- Consider using dependency injection
- Document singleton usage clearly
- Use @Volatile for thread safety

## Interview Questions

1. How does Kotlin's object keyword implement Singleton?
2. What is the difference between object and companion object?
3. How do you make singleton thread-safe in Kotlin?
4. When should you use singleton vs dependency injection?
5. How do you test code using singletons?

## References

- Kotlin documentation: Object expressions
- "Kotlin in Action" by Svetlana Isakova
- "Atomic Kotlin" by Bruce Eckel
