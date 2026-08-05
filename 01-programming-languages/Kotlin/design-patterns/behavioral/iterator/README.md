# Iterator Pattern (Kotlin)

## Overview

The Iterator pattern provides a way to access elements of a collection sequentially
without exposing its underlying representation. Kotlin's sequences and iterator
functions provide built-in iterator support.

## When to Use

- Accessing collection elements without exposing representation
- Supporting multiple traversal strategies
- Creating custom iteration patterns
- Implementing lazy evaluation

## Kotlin Implementation

### Sequence

```kotlin
fun fibonacci(): Sequence<Long> = sequence {
    var a = 0L
    var b = 1L
    while (true) {
        yield(a)
        val temp = a + b
        a = b
        b = temp
    }
}

fibonacci().take(10).forEach { println(it) }
```

### Custom Iterator

```kotlin
class RangeIterator(private val start: Int, private val end: Int) : Iterator<Int> {
    private var current = start

    override fun hasNext(): Boolean = current <= end

    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        return current++
    }
}

class Range(private val start: Int, private val end: Int) : Iterable<Int> {
    override fun iterator(): Iterator<Int> = RangeIterator(start, end)
}
```

### Iterator Extension

```kotlin
fun <T> Iterator<T>.toList(): List<T> {
    val list = mutableListOf<T>()
    while (hasNext()) {
        list.add(next())
    }
    return list
}

fun <T> Iterator<T>.filter(predicate: (T) -> Boolean): Iterator<T> {
    return object : Iterator<T> {
        private var nextElement: T? = null
        private var hasNext = false

        override fun hasNext(): Boolean {
            if (hasNext) return true
            while (this@filter.hasNext()) {
                val element = this@filter.next()
                if (predicate(element)) {
                    nextElement = element
                    hasNext = true
                    return true
                }
            }
            return false
        }

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            hasNext = false
            return nextElement!!
        }
    }
}
```

### Sequence Builder

```kotlin
fun <T> buildSequence(block: suspend SequenceScope<T>.() -> Unit): Sequence<T> {
    return sequence(block)
}
```

## Best Practices

- Use sequences for lazy evaluation
- Implement Iterable for custom collections
- Use extension functions for iterator operations
- Consider using coroutines for async iteration
- Handle infinite sequences carefully

## Interview Questions

1. What is the difference between Iterator and Sequence?
2. How do you implement Iterable in Kotlin?
3. Can iterators be infinite sequences?
4. How do you handle iterator disposal?
5. When should you use custom iterator vs collection methods?

## References

- Kotlin documentation: Sequences
- "Kotlin in Action" by Svetlana Isakova
- "Atomic Kotlin" by Bruce Eckel
