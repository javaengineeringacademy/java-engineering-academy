# Proxy Pattern (Kotlin)

## Overview

The Proxy pattern provides a surrogate or placeholder for another object to control
access. Kotlin's by lazy delegation provides built-in proxy support for lazy
initialization.

## When to Use

- Lazy initialization
- Access control
- Logging and monitoring
- Caching
- Validation

## Kotlin Implementation

### Lazy Proxy

```kotlin
class ExpensiveObject {
    init {
        println("Creating expensive object")
    }

    fun doWork(): String = "Working"
}

val proxy: ExpensiveObject by lazy {
    ExpensiveObject()
}
```

### Virtual Proxy

```kotlin
interface Image {
    fun display(): String
}

class RealImage(private val filename: String) : Image {
    init {
        println("Loading $filename")
    }

    override fun display(): String = "Displaying $filename"
}

class ImageProxy(private val filename: String) : Image {
    private val realImage by lazy { RealImage(filename) }

    override fun display(): String = realImage.display()
}
```

### Protection Proxy

```kotlin
interface Document {
    fun read(): String
    fun write(content: String)
}

class RealDocument : Document {
    override fun read(): String = "Document content"
    override fun write(content: String) = println("Writing: $content")
}

class DocumentProxy(private val userRole: String) : Document {
    private val document = RealDocument()

    override fun read(): String = document.read()

    override fun write(content: String) {
        if (userRole == "Admin") {
            document.write(content)
        } else {
            println("Access denied")
        }
    }
}
```

### Caching Proxy

```kotlin
class CachingProxy<T>(private val factory: () -> T) {
    private val cache by lazy { factory() }

    fun get(): T = cache
}
```

## Best Practices

- Use by lazy for simple lazy initialization
- Consider using delegation for complex proxies
- Document proxy behavior clearly
- Use generics for type safety
- Consider performance implications

## Interview Questions

1. What is the difference between Proxy and Decorator?
2. How does by lazy work in Kotlin?
3. Can you proxy functions in Kotlin?
4. When should you use Proxy vs lazy initialization?
5. How do you handle proxy for async operations?

## References

- Kotlin documentation: Delegation
- "Kotlin in Action" by Svetlana Isakova
- "Atomic Kotlin" by Bruce Eckel
