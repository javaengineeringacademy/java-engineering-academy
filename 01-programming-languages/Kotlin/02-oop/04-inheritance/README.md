# Kotlin Inheritance

## Overview
Kotlin classes are final by default. Use `open` for inheritance.

## Open Class
```kotlin
open class Animal(val name: String) {
    open fun speak(): String = "Sound"
}

class Dog(name: String) : Animal(name) {
    override fun speak(): String = "Bark"
}
```

## Override Keyword
```kotlin
open class Animal {
    open fun speak() = "Sound"
}

class Dog : Animal() {
    override fun speak() = "Bark"
}
```

## Abstract Class
```kotlin
abstract class Shape {
    abstract fun area(): Double
    fun describe() = "Area: ${area()}"
}

class Circle(val radius: Double) : Shape() {
    override fun area() = Math.PI * radius * radius
}
```

## Sealed Class
```kotlin
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}

// When exhaustive
when (result) {
    is Result.Success -> println(result.data)
    is Result.Error -> println(result.message)
}
```

## Multi-level Inheritance
```kotlin
open class Animal
class Dog : Animal()
class Puppy : Dog()
```

## Type Checking and Smart Casts
```kotlin
if (dog is Dog) {
    dog.fetch("ball") // Smart cast
}
```

## Key Takeaways
1. Use open for inheritable classes
2. Use override for overridden members
3. Use abstract for base contracts
4. Use sealed for restricted hierarchies