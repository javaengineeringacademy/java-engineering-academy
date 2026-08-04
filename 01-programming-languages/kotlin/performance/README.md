# Kotlin Performance

This section covers performance optimization techniques, benchmarking, and best practices for Kotlin applications.

## Table of Contents

- [Benchmarking](#benchmarking)
- [Inline Functions Performance](#inline-functions-performance)
- [Coroutines vs Threads](#coroutines-vs-threads)
- [Collection Performance](#collection-performance)
- [Memory Optimization](#memory-optimization)
- [JVM Optimization](#jvm-optimization)
- [Performance Tools](#performance-tools)
- [Best Practices](#best-practices)

## Benchmarking

### Kotlin Microbenchmark

```kotlin
// Using kotlinx-benchmark
import kotlinx.benchmark.*

@State(Scope.Benchmark)
class MyBenchmark {
    private var list: List<Int> = emptyList()
    
    @Setup
    fun setup() {
        list = (1..1000000).toList()
    }
    
    @Benchmark
    fun listSum(): Int {
        return list.sum()
    }
    
    @Benchmark
    fun sequenceSum(): Int {
        return list.asSequence().sum()
    }
}

// Run benchmarks
fun main() {
    Runner(listOf(MyBenchmark::class)).run()
}
```

### JMH (Java Microbenchmark Harness)

```kotlin
// JMH benchmark
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
class JmhBenchmark {
    private var list: List<Int> = emptyList()
    
    @Setup
    fun setup() {
        list = (1..1000000).toList()
    }
    
    @Benchmark
    fun listSum(): Int {
        return list.sum()
    }
    
    @Benchmark
    fun sequenceSum(): Int {
        return list.asSequence().sum()
    }
}

// Run with: java -jar benchmarks.jar
```

### Performance Measurement

```kotlin
// Measure execution time
fun <T> measureTime(block: () -> T): Pair<T, Long> {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    return Pair(result, endTime - startTime)
}

// Usage
val (result, time) = measureTime {
    (1..1000000).toList().sum()
}
println("Result: $result, Time: $time ns")

// Measure multiple iterations
fun benchmark(iterations: Int = 1000, block: () -> Unit): Double {
    val times = (1..iterations).map {
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        end - start
    }
    return times.average()
}

// Usage
val averageTime = benchmark(1000) {
    // Code to benchmark
    (1..1000).toList().map { it * it }
}
println("Average time: $averageTime ns")
```

## Inline Functions Performance

### Zero-Cost Abstractions

```kotlin
// Inline function - no overhead
inline fun <T> measureTime(block: () -> T): T {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
    return result
}

// Usage
val result = measureTime {
    Thread.sleep(100)
    "Result"
}

// No function call overhead - code is inlined
```

### Inline vs Non-Inline

```kotlin
// Non-inline function
fun nonInlineMeasureTime(block: () -> Unit) {
    val startTime = System.nanoTime()
    block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
}

// Inline function
inline fun inlineMeasureTime(block: () -> Unit) {
    val startTime = System.nanoTime()
    block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
}

// Performance comparison
fun comparePerformance() {
    val iterations = 1000000
    
    val nonInlineTime = measureTimeMillis {
        repeat(iterations) {
            nonInlineMeasureTime { /* empty */ }
        }
    }
    
    val inlineTime = measureTimeMillis {
        repeat(iterations) {
            inlineMeasureTime { /* empty */ }
        }
    }
    
    println("Non-inline: $nonInlineTime ms")
    println("Inline: $inlineTime ms")
    // Inline should be significantly faster
}
```

### Crossinline and Noinline

```kotlin
// Crossinline - prevents non-local returns
inline fun runInThread(crossinline action: () -> Unit) {
    Thread {
        action()
    }.start()
}

// Noinline - prevents inlining
inline fun inlinedFunction(noinline lambda: () -> Unit) {
    // This function is inlined
    lambda()  // This lambda is not inlined
}

// Performance implications
fun performanceImplications() {
    // Crossinline has no performance impact
    // Noinline has slight overhead (function call)
}
```

## Coroutines vs Threads

### Thread Performance

```kotlin
// Traditional threads
fun threadPerformance() {
    val threads = (1..1000).map { i ->
        Thread {
            Thread.sleep(1000)
            println("Thread $i completed")
        }
    }
    
    threads.forEach { it.start() }
    threads.forEach { it.join() }
}

// Thread overhead:
// - Each thread uses ~1MB stack space
// - Thread creation is expensive
// - Context switching is expensive
```

### Coroutine Performance

```kotlin
import kotlinx.coroutines.*

// Coroutines are lightweight
fun coroutinePerformance() = runBlocking {
    val coroutines = (1..1000).map { i ->
        launch {
            delay(1000)
            println("Coroutine $i completed")
        }
    }
    
    coroutines.forEach { it.join() }
}

// Coroutine overhead:
// - Each coroutine uses ~few hundred bytes
// - Coroutine creation is cheap
// - No context switching overhead
```

### Performance Comparison

```kotlin
import kotlinx.coroutines.*

// Thread vs Coroutine performance
fun performanceComparison() {
    val iterations = 1000
    
    // Thread performance
    val threadTime = measureTimeMillis {
        val threads = (1..iterations).map { i ->
            Thread {
                Thread.sleep(100)
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
    }
    
    // Coroutine performance
    val coroutineTime = measureTimeMillis {
        runBlocking {
            val coroutines = (1..iterations).map { i ->
                launch {
                    delay(100)
                }
            }
            coroutines.forEach { it.join() }
        }
    }
    
    println("Thread time: $threadTime ms")
    println("Coroutine time: $coroutineTime ms")
    // Coroutines should be much faster
}
```

## Collection Performance

### List Performance

```kotlin
// List operations performance
fun listPerformance() {
    val list = (1..1000000).toList()
    
    // Sum - O(n)
    val sumTime = measureTimeMillis { list.sum() }
    
    // Contains - O(n)
    val containsTime = measureTimeMillis { list.contains(999999) }
    
    // Get by index - O(1)
    val getTime = measureTimeMillis { list[999999] }
    
    println("Sum: $sumTime ms")
    println("Contains: $containsTime ms")
    println("Get: $getTime ms")
}
```

### Map Performance

```kotlin
// Map operations performance
fun mapPerformance() {
    val map = (1..1000000).associate { it to it.toString() }
    
    // Get by key - O(1) average
    val getTime = measureTimeMillis { map[999999] }
    
    // Contains key - O(1) average
    val containsTime = measureTimeMillis { map.containsKey(999999) }
    
    // Iteration - O(n)
    val iterationTime = measureTimeMillis { map.forEach { (k, v) -> } }
    
    println("Get: $getTime ms")
    println("Contains: $containsTime ms")
    println("Iteration: $iterationTime ms")
}
```

### Set Performance

```kotlin
// Set operations performance
fun setPerformance() {
    val set = (1..1000000).toSet()
    
    // Contains - O(1) average
    val containsTime = measureTimeMillis { set.contains(999999) }
    
    // Union - O(n + m)
    val set2 = (500000..1500000).toSet()
    val unionTime = measureTimeMillis { set.union(set2) }
    
    // Intersection - O(min(n, m))
    val intersectionTime = measureTimeMillis { set.intersect(set2) }
    
    println("Contains: $containsTime ms")
    println("Union: $unionTime ms")
    println("Intersection: $intersectionTime ms")
}
```

## Memory Optimization

### Object Allocation

```kotlin
// Minimize object allocation
fun minimizeAllocation() {
    // Bad: Creates many objects
    val bad = (1..1000000).map { "Item $it" }
    
    // Better: Use sequence for lazy evaluation
    val better = (1..1000000).asSequence().map { "Item $it" }.toList()
    
    // Best: Use StringBuilder for string concatenation
    val best = StringBuilder()
    for (i in 1..1000000) {
        best.append("Item $i")
    }
}
```

### String Optimization

```kotlin
// String optimization
fun stringOptimization() {
    // Bad: String concatenation creates many objects
    var bad = ""
    for (i in 1..10000) {
        bad += "Item $i"  // Creates new string each time
    }
    
    // Good: StringBuilder
    val good = StringBuilder()
    for (i in 1..10000) {
        good.append("Item $i")  // Modifies same buffer
    }
    
    // Good: String template (compiler optimizes)
    val items = (1..10000).toList()
    val result = items.joinToString("") { "Item $it" }
}
```

### Collection Optimization

```kotlin
// Collection optimization
fun collectionOptimization() {
    // Bad: Mutable list with frequent adds
    val bad = mutableListOf<Int>()
    for (i in 1..100000) {
        bad.add(i)  // May cause array resizing
    }
    
    // Good: Pre-allocate capacity
    val good = ArrayList<Int>(100000)
    for (i in 1..100000) {
        good.add(i)  // No resizing needed
    }
    
    // Good: Use appropriate collection type
    val set = mutableSetOf<Int>()  // For unique elements
    val map = mutableMapOf<Int, String>()  // For key-value pairs
}
```

## JVM Optimization

### JVM Flags

```kotlin
// JVM optimization flags
// java -Xms512m -Xmx2g -XX:+UseG1GC -XX:+UseStringDeduplication

// Memory settings
// -Xms: Initial heap size
// -Xmx: Maximum heap size
// -Xss: Thread stack size

// Garbage collection
// -XX:+UseG1GC: Use G1 garbage collector
// -XX:+UseConcMarkSweepGC: Use CMS garbage collector
// -XX:+UseParallelGC: Use parallel garbage collector
```

### Bytecode Optimization

```kotlin
// Kotlin compiler optimizations
// 1. Inline functions
// 2. Constant folding
// 3. Dead code elimination
// 4. Null check elimination
// 5. Type check elimination

// Example: constant folding
val constant = 5 * 10  // Compiled to: val constant = 50

// Example: null check elimination
val nonNull: String = "Hello"
// if (nonNull != null) println(nonNull.length)
// Compiled to: println(nonNull.length)
```

### ProGuard/R8

```kotlin
// ProGuard configuration
// -keep class com.example.** { *; }
// -keepclassmembers class * {
//     @com.example.annotation *;
// }

// R8 optimization
// - Optimization
// - Shrinking
// - Obfuscation

// Usage in build.gradle
// android {
//     buildTypes {
//         release {
//             minifyEnabled true
//             proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
//         }
//     }
// }
```

## Performance Tools

### Profiling Tools

```kotlin
// VisualVM
// - Monitor CPU and memory usage
// - Take heap dumps
// - Analyze thread states

// Android Studio Profiler
// - CPU profiling
// - Memory profiling
// - Network profiling
// - Energy profiling

// JProfiler
// - CPU profiling
// - Memory profiling
// - Thread profiling
// - GC profiling
```

### Memory Analysis

```kotlin
// Memory leak detection
class MemoryLeakExample {
    companion object {
        val cache = mutableMapOf<String, Any>()
    }
    
    fun addToCache(key: String, value: Any) {
        cache[key] = value  // Memory leak if cache grows indefinitely
    }
}

// Solution: Use WeakReference
import java.lang.ref.WeakReference

class NoMemoryLeakExample {
    companion object {
        val cache = mutableMapOf<String, WeakReference<Any>>()
    }
    
    fun addToCache(key: String, value: Any) {
        cache[key] = WeakReference(value)
    }
}
```

### Benchmarking Tools

```kotlin
// Kotlin Microbenchmark
import kotlinx.benchmark.*

@State(Scope.Benchmark)
class MyBenchmark {
    @Benchmark
    fun myBenchmark() {
        // Benchmark code
    }
}

// JMH
import org.openjdk.jmh.annotations.*

@State(Scope.Benchmark)
class JmhBenchmark {
    @Benchmark
    fun myBenchmark() {
        // Benchmark code
    }
}
```

## Best Practices

### Performance Best Practices

```kotlin
// 1. Use inline functions for small, frequently called functions
inline fun <T> measureTime(block: () -> T): T {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
    return result
}

// 2. Use sequences for large datasets
val largeList = (1..1000000).toList()
val result = largeList.asSequence()
    .filter { it % 2 == 0 }
    .take(10)
    .toList()

// 3. Use appropriate collection types
val list = listOf(1, 2, 3)  // For indexed access
val set = setOf(1, 2, 3)  // For unique elements
val map = mapOf(1 to "one", 2 to "two")  // For key-value pairs

// 4. Minimize object allocation
val sb = StringBuilder()
for (i in 1..10000) {
    sb.append("Item $i")
}

// 5. Use coroutines for concurrency
fun processData() = runBlocking {
    val deferred1 = async { loadData1() }
    val deferred2 = async { loadData2() }
    val data1 = deferred1.await()
    val data2 = deferred2.await()
    processData(data1, data2)
}
```

## Summary

Kotlin performance optimization involves:

- **Benchmarking**: Use JMH or kotlin-benchmark for accurate measurements
- **Inline Functions**: Zero-cost abstractions for small functions
- **Coroutines vs Threads**: Coroutines are more efficient for I/O-bound work
- **Collection Performance**: Choose appropriate collection types
- **Memory Optimization**: Minimize object allocation, use WeakReference
- **JVM Optimization**: Use appropriate JVM flags and ProGuard/R8
- **Performance Tools**: VisualVM, Android Profiler, JProfiler

Following these practices ensures optimal Kotlin application performance.
