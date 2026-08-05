# Strategy Pattern (Kotlin)

## Overview

The Strategy pattern defines a family of algorithms, encapsulates each one, and makes
them interchangeable. Kotlin's lambdas and function types make strategies particularly
concise.

## When to Use

- Multiple algorithms for specific task
- Need to switch algorithms at runtime
- Avoiding conditional statements
- Isolating algorithm implementation

## Kotlin Implementation

### Lambda Strategy

```kotlin
typealias Strategy<T, R> = (T) -> R

class Context<T, R>(private var strategy: Strategy<T, R>) {
    fun setStrategy(strategy: Strategy<T, R>) {
        this.strategy = strategy
    }

    fun execute(input: T): R = strategy(input)
}

val doubleStrategy: Strategy<Int, Int> = { it * 2 }
val squareStrategy: Strategy<Int, Int> = { it * it }
```

### Sealed Class Strategy

```kotlin
sealed class SortStrategy {
    abstract fun sort(data: List<Int>): List<Int>

    object BubbleSort : SortStrategy() {
        override fun sort(data: List<Int>): List<Int> {
            println("Bubble sort")
            return data.sorted()
        }
    }

    object QuickSort : SortStrategy() {
        override fun sort(data: List<Int>): List<Int> {
            println("Quick sort")
            return data.sorted()
        }
    }
}
```

### Strategy Registry

```kotlin
class StrategyRegistry<T, R> {
    private val strategies = mutableMapOf<String, (T) -> R>()

    fun register(name: String, strategy: (T) -> R) {
        strategies[name] = strategy
    }

    fun get(name: String): ((T) -> R)? = strategies[name]

    fun execute(name: String, input: T): R? = strategies[name]?.invoke(input)
}
```

### Validation Strategy

```kotlin
typealias ValidationStrategy<T> = (T) -> Boolean

class ValidationContext<T>(private val strategy: ValidationStrategy<T>) {
    fun validate(value: T): Boolean = strategy(value)
}

val emailStrategy: ValidationStrategy<String> = { it.contains("@") }
val phoneStrategy: ValidationStrategy<String> = { it.length == 10 }
```

## Best Practices

- Use function types for simple strategies
- Keep strategy interface small
- Document strategy selection criteria
- Make strategies stateless when possible
- Consider using sealed classes for type safety

## Interview Questions

1. How does Strategy differ from State?
2. When should you use lambdas over classes?
3. Can strategies have state?
4. How do you handle strategy selection?
5. When is Strategy better than inheritance?

## References

- Kotlin documentation: Lambdas
- "Kotlin in Action" by Svetlana Isakova
- "Functional Programming in Kotlin"
