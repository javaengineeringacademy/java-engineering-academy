# Kotlin Anti-Patterns

## Overview
Common mistakes and anti-patterns to avoid in Kotlin.

## 1. Overusing !! Operator
```kotlin
// Bad
val length = name!!.length

// Good
val length = name?.length ?: 0
```

## 2. Not Using apply
```kotlin
// Bad
val sb = StringBuilder()
sb.append("Hello")
sb.append(" ")
sb.append("World")

// Good
val sb = StringBuilder().apply {
    append("Hello")
    append(" ")
    append("World")
}
```

## 3. Using var When val Would Work
```kotlin
// Bad
var name = "Alice"
name = "Alice" // Never changes

// Good
val name = "Alice"
```

## 4. Not Using Scope Functions
```kotlin
// Bad
val user = User()
user.name = "Alice"
user.age = 30
user.email = "alice@example.com"

// Good
val user = User().apply {
    name = "Alice"
    age = 30
    email = "alice@example.com"
}
```

## 5. Using forEach Instead of for Loop
```kotlin
// Bad
list.forEach { println(it) }

// Good
for (item in list) {
    println(item)
}
```

## 6. Not Using Data Classes
```kotlin
// Bad
class User {
    var name = ""
    var age = 0
    
    override fun equals(other: Any?) = ...
    override fun hashCode() = ...
    override fun toString() = ...
}

// Good
data class User(val name: String, val age: Int)
```

## 7. Using runBlocking Incorrectly
```kotlin
// Bad
fun main() = runBlocking {
    // All code runs in main thread
}

// Good
fun main() = runBlocking {
    launch {
        // Background work
    }
}
```

## 8. Not Using Sealed Classes
```kotlin
// Bad
class Result {
    class Success(val data: String) : Result()
    class Error(val message: String) : Result()
}

// Good
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}
```

## Best Practices
1. Prefer val over var
2. Use scope functions appropriately
3. Use data classes for data
4. Use sealed classes for hierarchies
5. Avoid !! operator