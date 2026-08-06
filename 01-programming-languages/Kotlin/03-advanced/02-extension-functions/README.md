# Kotlin Extension Functions

## Overview
Extension functions add functionality to existing classes.

## Basic Extension
```kotlin
fun String.isPalindrome(): Boolean {
    return this == this.reversed()
}

"racecar".isPalindrome() // true
```

## Extension Properties
```kotlin
val String.wordCount: Int
    get() = this.split(" ").size

"Hello World".wordCount // 2
```

## Nullable Extensions
```kotlin
fun String?.orDefault(default: String): String {
    return this ?: default
}

val nullStr: String? = null
nullStr.orDefault("N/A") // "N/A"
```

## Generic Extensions
```kotlin
fun <T> T?.println() {
    println(this)
}

"Hello".println()
```

## Infix Extensions
```kotlin
infix fun Int.pow(exponent: Int): Int {
    return (1..exponent).fold(1) { acc, _ -> acc * this }
}

2 pow 10 // 1024
```

## Operator Extensions
```kotlin
operator fun Int.times(text: String): String {
    return text.repeat(this)
}

3 * "Ha" // "HaHaHa"
```

## Companion Object Extensions
```kotlin
fun MathUtils.Companion.add(a: Int, b: Int) = a + b

MathUtils.add(2, 3)
```

## Key Takeaways
1. Use extensions for utility functions
2. Keep extensions focused
3. Use infix for readable operators
4. Avoid extension functions on Any