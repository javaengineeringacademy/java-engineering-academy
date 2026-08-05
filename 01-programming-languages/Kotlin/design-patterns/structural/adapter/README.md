# Adapter Pattern (Kotlin)

## Overview

The Adapter pattern converts the interface of a class into another interface clients
expect. Kotlin's interfaces and extension functions enable concise adapter implementations.

## When to Use

- Integrating third-party libraries
- Reusing existing classes with incompatible interfaces
- Building legacy system integration
- Converting data formats

## Kotlin Implementation

### Interface Adapter

```kotlin
interface Target {
    fun request(): String
}

class Adaptee {
    fun specificRequest(): String = "Adaptee request"
}

class Adapter(private val adaptee: Adaptee) : Target {
    override fun request(): String = adaptee.specificRequest()
}
```

### Extension Function Adapter

```kotlin
class LegacyAPI {
    fun getData(): Pair<String, Long> = Pair("data", System.currentTimeMillis())
}

class NewAPI {
    suspend fun fetchData(): Map<String, Any> = mapOf("data" to "new", "time" to System.currentTimeMillis())
}

fun LegacyAPI.toNewAPI(): NewAPI {
    val legacy = this
    return object : NewAPI() {
        override suspend fun fetchData(): Map<String, Any> {
            val (data, timestamp) = legacy.getData()
            return mapOf("data" to data, "time" to timestamp)
        }
    }
}
```

### Generic Adapter

```kotlin
interface Adapter<T, R> {
    fun adapt(input: T): R
}

class StringToIntAdapter : Adapter<String, Int> {
    override fun adapt(input: String): Int {
        return input.toIntOrNull() ?: 0
    }
}
```

### Data Format Adapter

```kotlin
data class XMLData(val elements: Map<String, String>)
data class JSONData(val elements: Map<String, String>)

class XMLToJSONAdapter {
    fun adapt(xml: XMLData): JSONData {
        return JSONData(xml.elements)
    }
}
```

## Best Practices

- Keep adapter interface consistent
- Use extension functions for seamless adaptation
- Document interface differences
- Keep adapters simple and focused
- Consider testing adapter behavior thoroughly

## Interview Questions

1. How does Adapter differ from Facade?
2. Can you use adapters for data format conversion?
3. When should you use Adapter vs Wrapper?
4. How do you handle multiple interface adaptations?
5. Can adapters add new functionality?

## References

- Kotlin documentation: Interfaces
- "Kotlin in Action" by Svetlana Isakova
- "Head First Design Patterns" by Freeman
