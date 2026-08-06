# Kotlin Best Practices

## Overview
Guidelines for writing clean, idiomatic Kotlin code.

## 1. Prefer val Over var
```kotlin
// Good
val name = "Alice"
val numbers = listOf(1, 2, 3)

// Bad
var name = "Alice"
var numbers = listOf(1, 2, 3)
```

## 2. Use Type Inference
```kotlin
// Good
val name = "Alice"
val numbers = listOf(1, 2, 3)

// Bad
val name: String = "Alice"
val numbers: List<Int> = listOf(1, 2, 3)
```

## 3. Use Scope Functions
```kotlin
// apply - object configuration
val user = User().apply {
    name = "Alice"
    age = 30
}

// let - null check + transformation
val length = name?.let { it.length } ?: 0

// also - side effects
list.also { println(it) }
```

## 4. Use Extension Functions
```kotlin
fun String.isPalindrome(): Boolean {
    return this == this.reversed()
}

"racecar".isPalindrome()
```

## 5. Use Data Classes
```kotlin
data class User(val name: String, val age: Int)

// Auto-generates: equals, hashCode, toString, copy
```

## 6. Use Sealed Classes
```kotlin
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}
```

## 7. Use when Instead of Switch
```kotlin
when (value) {
    is Int -> "Integer"
    is String -> "String"
    else -> "Unknown"
}
```

## 8. Use Coroutines
```kotlin
// Good
launch {
    val data = withContext(Dispatchers.IO) {
        fetchData()
    }
    updateUI(data)
}
```

## 9. Use Null Safety
```kotlin
// Good
val length = name?.length ?: 0

// Bad
val length = name!!.length
```

## 10. Use Collection Operations
```kotlin
// Good
val result = list
    .filter { it > 0 }
    .map { it * 2 }
    .sorted()

// Bad
val result = mutableListOf<Int>()
for (item in list) {
    if (item > 0) {
        result.add(item * 2)
    }
}
result.sort()
```

## Key Takeaways
1. Prefer immutable types
2. Use idiomatic Kotlin features
3. Leverage null safety
4. Use coroutines for async
5. Keep functions small and focused