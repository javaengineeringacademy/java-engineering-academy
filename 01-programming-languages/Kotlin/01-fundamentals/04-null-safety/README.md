# Kotlin Null Safety

## Overview
Kotlin provides built-in null safety to prevent NullPointerException.

## Nullable Types
```kotlin
var name: String? = "Alice"
name = null  // Allowed
```

## Safe Call Operator (?.)
```kotlin
val length: Int? = name?.length
val city: String? = user?.address?.city
```

## Elvis Operator (?:)
```kotlin
val safeLength: Int = name?.length ?: 0
val displayName: String = name ?: "Unknown"
```

## Not-Null Assertion (!!)
```kotlin
// Throws NPE if null - use sparingly
val length = name!!.length
```

## let Function
```kotlin
name?.let { nonNullName ->
    println("Name is $nonNullName")
}
```

## Smart Casts
```kotlin
if (name != null) {
    println(name.length) // Smart cast to String
}
```

## Safe Casts
```kotlin
val safeString: String? = obj as? String
```

## Nullable Collections
```kotlin
val list: List<Int>? = listOf(1, 2, 3)
val first: Int? = list?.firstOrNull()

// Filter nulls
val mixed = listOf(1, null, 3)
val nonNull = mixed.filterNotNull()
```

## Scope Functions
```kotlin
// let - null check + transformation
name?.let { "Name is $it" }

// also - side effects
name?.also { println(it) }

// run - null check + transformation
name?.run { length }

// apply - object configuration
StringBuilder().apply { append("Hello") }
```

## Key Takeaways
1. Use ? for nullable types
2. Use ?. for safe access
3. Use ?: for defaults
4. Use let for null checks