# Kotlin Data Classes

## Overview
Data classes auto-generate common methods.

## Basic Data Class
```kotlin
data class User(val name: String, val age: Int, val email: String)

val user = User("Alice", 30, "alice@example.com")
```

## Auto-generated Methods

### equals()
```kotlin
val user2 = User("Alice", 30, "alice@example.com")
println(user == user2) // true
```

### hashCode()
```kotlin
println(user.hashCode())
```

### toString()
```kotlin
println(user.toString())
// User(name=Alice, age=30, email=alice@example.com)
```

## copy() Function
```kotlin
val copy = user.copy(name = "Bob")
val updated = user.copy(name = "Charlie", age = 35)
```

## Destructuring
```kotlin
val (name, age, email) = user
println("$name is $age years old")

// Partial
val (userName, _, userEmail) = user
```

## Component Functions
```kotlin
user.component1() // name
user.component2() // age
user.component3() // email
```

## Default Values
```kotlin
data class Config(
    val host: String = "localhost",
    val port: Int = 8080
)

val config = Config(port = 9090)
```

## Key Takeaways
1. Use data classes for data carriers
2. Leverage copy() for immutable updates
3. Use destructuring for extraction
4. Combine with defaults for flexibility