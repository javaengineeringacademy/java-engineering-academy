# Kotlin Functions

## Overview
Kotlin functions are first-class citizens with powerful features.

## Basic Function
```kotlin
fun greet(name: String): String {
    return "Hello, $name!"
}
```

## Single Expression
```kotlin
fun add(a: Int, b: Int): Int = a + b
```

## Default Parameters
```kotlin
fun greet(name: String, greeting: String = "Hello"): String {
    return "$greeting, $name!"
}
```

## Named Arguments
```kotlin
createUser(name = "Alice", age = 30, email = "alice@example.com")
```

## Vararg Parameters
```kotlin
fun sum(vararg numbers: Int): Int = numbers.sum()

sum(1, 2, 3, 4, 5)
```

## Higher-Order Functions
```kotlin
fun perform(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

perform(5, 3) { a, b -> a + b }
```

## Lambda Functions
```kotlin
val multiply = { a: Int, b: Int -> a * b }
val square: (Int) -> Int = { it * it }
```

## Extension Functions
```kotlin
fun String.isPalindrome(): Boolean {
    return this == this.reversed()
}
```

## Tail Recursion
```kotlin
tailrec fun factorial(n: Int, acc: Int = 1): Int {
    return if (n <= 1) acc else factorial(n - 1, n * acc)
}
```

## Infix Functions
```kotlin
infix fun Int.power(exponent: Int): Int = (1..exponent).fold(1) { acc, _ -> acc * this }

2 power 10  // 1024
```

## Key Takeaways
1. Use default parameters for optional values
2. Use named arguments for clarity
3. Leverage higher-order functions
4. Use extension functions for utility