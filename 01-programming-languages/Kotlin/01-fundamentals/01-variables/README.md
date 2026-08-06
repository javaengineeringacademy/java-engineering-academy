# Kotlin Variables

## Overview
Kotlin has two types of variables: val (immutable) and var (mutable).

## Immutable Variables (val)
```kotlin
val name = "Alice"  // Type inferred
val age: Int = 30   // Explicit type
```

## Mutable Variables (var)
```kotlin
var count = 0
count = 10  // Allowed
```

## Basic Types
```kotlin
val integer: Int = 42
val long: Long = 123456789L
val float: Float = 3.14f
val double: Double = 3.14159
val boolean: Boolean = true
val char: Char = 'A'
val string: String = "Hello"
```

## Type Inference
```kotlin
val inferredInt = 42        // Int
val inferredDouble = 3.14   // Double
val inferredString = "Hello" // String
```

## String Templates
```kotlin
val name = "Alice"
val message = "Hello, $name!"
val expression = "Sum: ${1 + 2}"
```

## Multi-line Strings
```kotlin
val multiLine = """
    This is a
    multi-line string
""".trimIndent()
```

## Nullable Types
```kotlin
var nullable: String? = "Hello"
nullable = null  // Allowed
```

## Type Conversion
```kotlin
val int = 42
val long = int.toLong()
val double = int.toDouble()
val string = int.toString()
```

## Key Takeaways
1. Use val for immutable variables
2. Use var for mutable variables
3. Leverage type inference
4. Use string templates for formatting