# Kotlin Classes

## Overview
Kotlin classes are blueprints for creating objects.

## Basic Class
```kotlin
class Person(val name: String, val age: Int) {
    fun greet(): String = "Hello, I'm $name"
}
```

## Primary Constructor
```kotlin
class User(val name: String, val email: String) {
    var age: Int = 0
        private set
}
```

## Secondary Constructor
```kotlin
class User(val name: String, val email: String) {
    constructor(name: String, email: String, age: Int) : this(name, email) {
        this.age = age
    }
}
```

## Data Class
```kotlin
data class Point(val x: Int, val y: Int)

val p1 = Point(1, 2)
val p2 = p1.copy(x = 10)
val (x, y) = p1  // Destructuring
```

## Sealed Class
```kotlin
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}
```

## Abstract Class
```kotlin
abstract class Shape {
    abstract fun area(): Double
    fun describe() = "Area: ${area()}"
}
```

## Companion Object
```kotlin
class MathUtils {
    companion object {
        const val PI = 3.14159
        fun add(a: Int, b: Int) = a + b
    }
}

MathUtils.add(2, 3)
```

## Singleton
```kotlin
object Database {
    fun connect(url: String) { }
}

Database.connect("url")
```

## Key Takeaways
1. Use primary constructors for simple classes
2. Use data classes for data carriers
3. Use sealed classes for restricted hierarchies
4. Use companion objects for static members