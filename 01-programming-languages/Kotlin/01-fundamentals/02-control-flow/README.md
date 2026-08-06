# Kotlin Control Flow

## Overview
Kotlin has modern control flow expressions.

## if Expression
```kotlin
val max = if (x > y) x else y

// With blocks
val result = if (x > y) {
    println("x is greater")
    x
} else {
    println("y is greater")
    y
}
```

## when Expression
```kotlin
// Basic when
val dayType = when (day) {
    "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "Weekday"
    "Saturday", "Sunday" -> "Weekend"
    else -> "Unknown"
}

// When with conditions
val weather = when {
    temperature > 30 -> "Hot"
    temperature > 20 -> "Warm"
    else -> "Cold"
}

// When with type checking
val description = when (obj) {
    is Int -> "Integer: $obj"
    is String -> "String: $obj"
    else -> "Unknown"
}
```

## for Loop
```kotlin
for (i in 1..5) print("$i ")        // 1 2 3 4 5
for (i in 0..10 step 2) print("$i ") // 0 2 4 6 8 10
for (i in 10 downTo 1) print("$i ")  // 10 9 8 7 6 5 4 3 2 1
for (i in 0 until 5) print("$i ")    // 0 1 2 3 4
```

## while Loop
```kotlin
while (count < 5) {
    println(count)
    count++
}

do {
    println(count)
    count++
} while (count < 5)
```

## break and continue
```kotlin
for (i in 1..5) {
    if (i == 3) break    // Exit loop
    if (i % 2 == 0) continue // Skip iteration
    println(i)
}
```

## Key Takeaways
1. Use when instead of switch
2. Use ranges for iteration
3. Control flow are expressions
4. Use labels for nested loops