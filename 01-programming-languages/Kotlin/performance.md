# Kotlin Performance

## Compilation Optimization

Kotlin compiler optimizes code through several techniques.

- **Inline Functions**: Eliminate function call overhead for small functions
- **Smart Casts**: Avoid redundant type checks
- **Constant Folding**: Evaluate constant expressions at compile time
- **Dead Code Elimination**: Remove unreachable code paths

```kotlin
inline fun <T> measureTime(block: () -> T): T {
    val start = System.nanoTime()
    val result = block()
    val duration = (System.nanoTime() - start) / 1_000_000
    println("Execution: ${duration}ms")
    return result
}
```

## Inline Functions

Use `inline` to avoid lambda allocation overhead.

```kotlin
// Without inline: creates AnonymousInnerClass for lambda
fun <T> List<T>.filterAndPrint(predicate: (T) -> Boolean): List<T> {
    return filter(predicate).also { println(it) }
}

// With inline: no lambda allocation
inline fun <T> List<T>.filterAndPrintInline(crossinline predicate: (T) -> Boolean): List<T> {
    return filter { predicate(it) }.also { println(it) }
}
```

## Coroutine Performance

Coroutines are lightweight compared to threads.

```kotlin
// Launching 100,000 coroutines
suspend fun processItems(items: List<Int>) = coroutineScope {
    items.map { item ->
        async {
            processItem(item) // Runs concurrently
        }
    }.awaitAll()
}

// Use Dispatchers for thread management
suspend fun ioOperation() = withContext(Dispatchers.IO) {
    // Blocking I/O without blocking threads
}
```

## Collections

```kotlin
// Use sequences for lazy evaluation on large collections
val result = largeList.asSequence()
    .filter { it.isValid }
    .map { it.transform() }
    .take(10)
    .toList()

// Avoid intermediate collections
val sum = (1..1000000).sum() // Better than creating list first
```

## Memory Management

```kotlin
// Use data classes for value types (better memory layout)
data class Point(val x: Int, val y: Int)

// Use object declarations for singletons
object DatabaseConfig {
    val host = "localhost"
    val port = 5432
}

// Avoid creating unnecessary objects in loops
repeat(1000) {
    // Each iteration creates new scope
}
```

## Benchmarking

```kotlin
@Benchmark
fun benchmarkFunction(): Int {
    return (1..1000).sum()
}

// Use JMH for accurate benchmarks
// Add kotlinx-benchmark dependency
// @State(Scope.Benchmark)
```

## Profiling Tips

- Use `time` command to measure execution time
- Profile memory with `jmap -histo <pid>`
- Use VisualVM or IntelliJ Profiler
- Check GC logs for allocation patterns
- Use `@JvmStatic` for Java interop performance
