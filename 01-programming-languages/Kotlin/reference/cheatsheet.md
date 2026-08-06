# Kotlin Reference

## Type System
| Type | Description | Example |
|------|-------------|---------|
| `Int` | 32-bit integer | `val x: Int = 42` |
| `Long` | 64-bit integer | `val x: Long = 42L` |
| `Double` | 64-bit float | `val x: Double = 3.14` |
| `String` | Text | `val s: String = "Hello"` |
| `Boolean` | True/false | `val b: Boolean = true` |
| `Char` | Single character | `val c: Char = 'A'` |
| `Any` | Supertype of all | `val x: Any = "Hello"` |
| `Nothing` | Bottom type | `throw Exception()` |

## Null Safety
```kotlin
var nullable: String? = null
val length: Int? = nullable?.length
val safe: String = nullable ?: "default"
val forced: String = nullable!!  // Throws NPE
```

## Scope Functions
| Function | Object | Return |
|----------|--------|--------|
| `let` | `it` | Lambda result |
| `run` | `this` | Lambda result |
| `with` | `this` | Lambda result |
| `apply` | `this` | Object itself |
| `also` | `it` | Object itself |

## Coroutine Builders
| Builder | Returns | Use Case |
|---------|---------|----------|
| `launch` | `Job` | Fire and forget |
| `async` | `Deferred` | Parallel computation |
| `runBlocking` | `T` | Blocking context |

## Collection Operations
```kotlin
list.map { it * 2 }
list.filter { it > 0 }
list.reduce { acc, i -> acc + i }
list.groupBy { it.length }
list.partition { it > 0 }
```

## Common Patterns
```kotlin
// Safe call chain
user?.address?.city

// Elvis with exception
val value = nullable ?: throw IllegalArgumentException()

// Extension function
fun String.isEmail() = contains("@")

// Inline function
inline fun <T> measure(block: () -> T): T {
    val start = System.nanoTime()
    val result = block()
    println("Time: ${System.nanoTime() - start}ns")
    return result
}
```
