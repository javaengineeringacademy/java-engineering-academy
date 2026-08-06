# Kotlin Lambdas

## Overview
Lambdas are anonymous functions for concise code.

## Basic Lambda
```kotlin
val add = { a: Int, b: Int -> a + b }
println(add(5, 3)) // 8
```

## Lambda Types
```kotlin
val square: (Int) -> Int = { it * it }
val greet: () -> Unit = { println("Hello") }
val multiply: (Int, Int) -> Int = { a, b -> a * b }
```

## Single Parameter (it)
```kotlin
val square: (Int) -> Int = { it * it }
val doubled = numbers.map { it * 2 }
```

## Higher-Order Functions
```kotlin
fun perform(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

perform(5, 3) { a, b -> a + b }
```

## Trailing Lambdas
```kotlin
val doubled = numbers.map { it * 2 }
val evens = numbers.filter { it % 2 == 0 }
```

## Lambdas with Receiver
```kotlin
fun buildString(action: StringBuilder.() -> Unit): String {
    return StringBuilder().apply(action).toString()
}

val str = buildString {
    append("Hello")
    append(" World")
}
```

## Collection Operations
```kotlin
numbers
    .filter { it > 0 }
    .map { it * 2 }
    .sorted()
    .forEach { println(it) }
```

## Key Takeaways
1. Use `it` for single parameters
2. Use trailing lambdas for readability
3. Leverage collection operations
4. Use lambdas with receiver for DSLs