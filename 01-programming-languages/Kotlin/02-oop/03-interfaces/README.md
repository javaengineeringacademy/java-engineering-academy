# Kotlin Interfaces

## Overview
Interfaces define contracts for classes.

## Basic Interface
```kotlin
interface Drivable {
    fun start()
    fun stop()
}
```

## Default Methods
```kotlin
interface Drivable {
    fun start()
    fun stop()
    
    fun isRunning(): Boolean {
        return false
    }
}
```

## Interface Properties
```kotlin
interface Identifiable {
    val id: Int
    
    val displayName: String
        get() = "Item #$id"
}
```

## Multiple Interfaces
```kotlin
class Document : Readable, Writable, Cacheable {
    override fun read(): String = content
    override fun write(data: String) { content = data }
    override fun cache() { }
}
```

## Interface Inheritance
```kotlin
interface Cacheable : Identifiable {
    fun cache()
    fun evict()
}
```

## Interface as Parameter
```kotlin
fun processData(data: Readable) {
    println(data.read())
}
```

## Companion Object
```kotlin
interface Logger {
    fun log(message: String)
    
    companion object {
        fun create(): Logger = ConsoleLogger()
    }
}
```

## Key Takeaways
1. Use interfaces for contracts
2. Provide default implementations
3. Support multiple interfaces
4. Use interfaces for polymorphism