# Kotlin Memory Management

This section covers memory management in Kotlin, including JVM memory model, inline classes, and memory optimization techniques.

## Table of Contents

- [JVM Memory Model](#jvm-memory-model)
- [Kotlin Memory Management](#kotlin-memory-management)
- [Inline Classes](#inline-classes)
- [Value Classes](#value-classes)
- [Noinline and Crossinline](#noinline-and-crossinline)
- [Coroutines Memory](#coroutines-memory)
- [Memory Leaks](#memory-leaks)
- [Optimization Techniques](#optimization-techniques)
- [Profiling Tools](#profiling-tools)

## JVM Memory Model

### Memory Areas

```kotlin
// Stack memory - local variables, method parameters
fun stackMemory() {
    val localVariable = 10
    val anotherVariable = "Hello"
    // These are stored on the stack
}

// Heap memory - objects, collections
fun heapMemory() {
    val object1 = Object()
    val list = listOf(1, 2, 3)
    // These are stored on the heap
}

// Method area - class metadata, static variables
class MethodAreaExample {
    companion object {
        val CONSTANT = "constant"
    }
}

// Program counter - current instruction address
// Native method stack - native method calls
```

### Object Lifecycle

```kotlin
// Object creation
class Person(val name: String, val age: Int)

fun createObjects() {
    val person = Person("Alice", 30)  // Created on heap
    val anotherPerson = Person("Bob", 25)  // Another object on heap
}

// Object reachability
class Node(val value: Int, var next: Node? = null)

fun reachabilityExample() {
    var node1 = Node(1)
    var node2 = Node(2)
    var node3 = Node(3)
    
    node1.next = node2
    node2.next = node3
    
    node2 = null  // Node 2 is still reachable via node1
    
    node1 = null  // Now nodes 1, 2, and 3 are unreachable
    // Garbage collector can reclaim them
}
```

## Kotlin Memory Management

### Implicit Memory Management

```kotlin
// Kotlin automatically manages memory
class AutomaticMemoryManagement {
    val list = mutableListOf<Int>()
    
    fun addItem(item: Int) {
        list.add(item)
    }
    
    fun clearItems() {
        list.clear()
        // Memory will be reclaimed by garbage collector
    }
}

// No manual memory management needed
fun noManualManagement() {
    val array = IntArray(1000000)
    // No need to free array manually
}
```

### Null Safety and Memory

```kotlin
// Nullable types prevent memory leaks
class NullableMemory {
    var reference: String? = null
    
    fun setReference(value: String?) {
        reference = value
    }
    
    fun clearReference() {
        reference = null
    }
}

// Weak references
import java.lang.ref.WeakReference

class WeakReferenceExample {
    private var weakRef: WeakReference<String>? = null
    
    fun setReference(value: String) {
        weakRef = WeakReference(value)
    }
    
    fun getReference(): String? {
        return weakRef?.get()
    }
}
```

## Inline Classes

### Basic Inline Classes

```kotlin
// Inline class - wraps a single value
inline class Email(val value: String) {
    init {
        require(value.contains("@")) { "Invalid email" }
    }
}

// Usage
fun sendEmail(email: Email) {
    println("Sending to ${email.value}")
}

// Usage
fun inlineClassExample() {
    val email = Email("user@example.com")
    sendEmail(email)
    
    // No runtime overhead - Email is just a String at runtime
}

// Inline class with method
inline class Password(val value: String) {
    fun isValid(): Boolean {
        return value.length >= 8
    }
    
    fun masked(): String {
        return "*".repeat(value.length)
    }
}

// Usage
fun passwordExample() {
    val password = Password("secret123")
    println("Valid: ${password.isValid()}")
    println("Masked: ${password.masked()}")
}
```

### Inline Classes vs Regular Classes

```kotlin
// Regular class - has runtime overhead
class RegularEmail(val value: String) {
    init {
        require(value.contains("@")) { "Invalid email" }
    }
}

// Inline class - no runtime overhead
inline class InlineEmail(val value: String) {
    init {
        require(value.contains("@")) { "Invalid email" }
    }
}

// Performance comparison
fun performanceComparison() {
    val emails = (1..1000000).map { RegularEmail("user$it@example.com") }
    val inlineEmails = (1..1000000).map { InlineEmail("user$it@example.com") }
    
    // Regular class: Each element is a separate object
    // Inline class: Elements are just strings at runtime
}
```

## Value Classes

### Basic Value Classes

```kotlin
// Value class - similar to inline class
@JvmInline
value class Dollars(val cents: Int) {
    init {
        require(cents >= 0) { "Dollars cannot be negative" }
    }
    
    fun toDollars(): Double = cents / 100.0
}

// Usage
fun valueClassExample() {
    val price = Dollars(1999)
    println("Price: ${price.toDollars()}")
    
    // No runtime overhead
}

// Value class with multiple properties (Kotlin 1.7.20+)
@JvmInline
value class EmailAddress(val localPart: String, val domain: String) {
    val fullEmail: String
        get() = "$localPart@$domain"
}

// Usage
fun multiplePropertiesExample() {
    val email = EmailAddress("user", "example.com")
    println(email.fullEmail)
}
```

### Value Class Restrictions

```kotlin
// Value class restrictions:
// 1. Only one property in primary constructor
// 2. Property must be of a type that can be represented in JVM
// 3. Cannot have init blocks
// 4. Cannot have backing fields

// Invalid value class (multiple properties)
// @JvmInline
// value class Invalid(val a: Int, val b: String)  // Compilation error

// Valid value class
@JvmInline
value class Valid(val value: Int)  // OK

// Value class with companion object
@JvmInline
value class PositiveInt(val value: Int) {
    companion object {
        fun create(value: Int): PositiveInt? {
            return if (value > 0) PositiveInt(value) else null
        }
    }
}

// Usage
fun companionObjectExample() {
    val positive = PositiveInt.create(5)
    val invalid = PositiveInt.create(-1)
    
    println("Positive: ${positive?.value}")
    println("Invalid: ${invalid?.value}")
}
```

## Noinline and Crossinline

### Noinline

```kotlin
// Noinline - prevents inlining of function
inline fun inlinedFunction(noinline lambda: () -> Unit) {
    // This function is inlined
    lambda()  // This lambda is not inlined
}

// When to use noinline
fun noinlineExample() {
    // Store lambda in variable
    val storedLambda: () -> Unit
    inlinedFunction {
        storedLambda = { println("Stored") }
    }
    
    // Pass lambda to non-inline function
    inlinedFunction {
        someOtherFunction { println("Not inlined") }
    }
}

fun someOtherFunction(block: () -> Unit) {
    block()
}
```

### Crossinline

```kotlin
// Crossinline - prevents non-local returns
inline fun runInThread(crossinline action: () -> Unit) {
    Thread {
        action()  // Can't use return here
    }.start()
}

// Usage
fun crossinlineExample() {
    runInThread {
        println("Running in thread")
        // return  // Compilation error - can't use non-local return
    }
}

// When to use crossinline
inline fun filterAndProcess(
    list: List<Int>,
    crossinline predicate: (Int) -> Boolean,
    crossinline processor: (Int) -> Unit
) {
    list.filter(predicate).forEach { processor(it) }
}

// Usage
fun crossinlineFilterExample() {
    filterAndProcess(
        list = listOf(1, 2, 3, 4, 5),
        predicate = { it % 2 == 0 },
        processor = { println("Processing: $it") }
    )
}
```

## Coroutines Memory

### Coroutine Memory Management

```kotlin
import kotlinx.coroutines.*

// Coroutine scope memory
fun coroutineScopeMemory() = runBlocking {
    val scope = CoroutineScope(Dispatchers.Default)
    
    val job = scope.launch {
        repeat(1000) { i ->
            println("Job: $i")
            delay(500)
        }
    }
    
    // Cancel when done to free memory
    delay(1300)
    job.cancel()
    scope.cancel()
}

// Structured concurrency memory
fun structuredConcurrencyMemory() = runBlocking {
    // Parent scope manages child coroutines
    launch {
        launch {
            delay(1000)
            println("Child 1")
        }
        launch {
            delay(2000)
            println("Child 2")
        }
    }
    
    // All children are cancelled when parent is cancelled
}
```

### Flow Memory Management

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Flow memory management
fun flowMemoryManagement() = runBlocking {
    val flow = flow {
        for (i in 1..1000) {
            emit(i)
            delay(10)
        }
    }
    
    // Collect only what you need
    flow.take(10).collect { println(it) }
    
    // Or cancel collection
    val job = launch {
        flow.collect { println(it) }
    }
    
    delay(100)
    job.cancel()
}

// StateFlow memory management
class StateFlowMemory {
    private val _state = MutableStateFlow(0)
    val state: StateFlow<Int> = _state.asStateFlow()
    
    fun updateState(value: Int) {
        _state.value = value
    }
}
```

## Memory Leaks

### Common Memory Leaks

```kotlin
// Memory leak: Holding reference to activity
class MemoryLeakExample {
    private var activity: Activity? = null
    
    fun setActivity(activity: Activity) {
        this.activity = activity
    }
    
    // Activity is never released
}

// Solution: Use weak reference
import java.lang.ref.WeakReference

class NoMemoryLeakExample {
    private var activity: WeakReference<Activity>? = null
    
    fun setActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }
    
    fun getActivity(): Activity? {
        return activity?.get()
    }
}
```

### Preventing Memory Leaks

```kotlin
import kotlinx.coroutines.*

// Prevent memory leak with structured concurrency
class ViewModel : CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    private var job: Job? = null
    
    fun loadData() {
        job = launch {
            // Perform work
            val data = withContext(Dispatchers.IO) {
                delay(1000)
                "Data"
            }
            // Update UI
            println("Loaded: $data")
        }
    }
    
    fun onDestroy() {
        // Cancel all coroutines to prevent memory leaks
        cancel()
    }
}

// Prevent memory leak with lifecycle-aware components
class LifecycleAwareComponent {
    fun start() {
        // Start work
    }
    
    fun stop() {
        // Stop work and release resources
    }
}
```

## Optimization Techniques

### Object Pooling

```kotlin
// Object pooling to reduce garbage collection
class ObjectPool<T>(
    private val factory: () -> T,
    private val maxSize: Int = 10
) {
    private val pool = mutableListOf<T>()
    
    fun acquire(): T {
        return if (pool.isNotEmpty()) {
            pool.removeAt(pool.lastIndex)
        } else {
            factory()
        }
    }
    
    fun release(obj: T) {
        if (pool.size < maxSize) {
            pool.add(obj)
        }
    }
}

// Usage
fun objectPoolingExample() {
    val pool = ObjectPool({ StringBuilder() }, 10)
    
    val sb1 = pool.acquire()
    sb1.append("Hello")
    pool.release(sb1)
    
    val sb2 = pool.acquire()
    println(sb2.toString())  // "Hello" - reused from pool
}
```

### Efficient Collections

```kotlin
// Use appropriate collection types
fun efficientCollections() {
    // Use Array for primitive types
    val intArray = IntArray(1000)
    
    // Use List for indexed access
    val list = listOf(1, 2, 3)
    
    // Use Set for unique elements
    val set = setOf(1, 2, 3)
    
    // Use Map for key-value pairs
    val map = mapOf(1 to "one", 2 to "two")
    
    // Use Sequence for lazy evaluation
    val sequence = (1..1000000).asSequence()
        .filter { it % 2 == 0 }
        .take(10)
        .toList()
}

// Use built-in functions
fun builtInFunctions() {
    val list = listOf(1, 2, 3, 4, 5)
    
    // Use built-in sum
    val sum = list.sum()
    
    // Use built-in average
    val average = list.average()
    
    // Use built-in min/max
    val min = list.min()
    val max = list.max()
}
```

## Profiling Tools

### Memory Profiling

```kotlin
// Using VisualVM
// 1. Run application with VisualVM attached
// 2. Take heap dump
// 3. Analyze objects and memory usage

// Using Android Studio Profiler
// 1. Open profiler
// 2. Select memory tab
// 3. Take heap dump
// 4. Analyze memory usage

// Using jmap and jhat
// jmap -dump:live,format=b,file=heap.bin <pid>
// jhat heap.bin
```

### Performance Monitoring

```kotlin
// Memory usage monitoring
fun memoryMonitoring() {
    val runtime = Runtime.getRuntime()
    val totalMemory = runtime.totalMemory()
    val freeMemory = runtime.freeMemory()
    val usedMemory = totalMemory - freeMemory
    
    println("Total: ${totalMemory / 1024 / 1024} MB")
    println("Free: ${freeMemory / 1024 / 1024} MB")
    println("Used: ${usedMemory / 1024 / 1024} MB")
}

// Garbage collection monitoring
fun gcMonitoring() {
    val bean = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()
    
    for (gc in bean) {
        println("GC: ${gc.name}")
        println("Collections: ${gc.collectionCount}")
        println("Time: ${gc.collectionTime}")
    }
}
```

## Summary

Kotlin memory management builds on JVM memory management:

- **JVM Memory Model**: Stack, heap, method area, program counter
- **Automatic Management**: Garbage collection handles memory deallocation
- **Inline Classes**: Zero-cost abstractions for type safety
- **Value Classes**: Similar to inline classes with additional features
- **Coroutines Memory**: Structured concurrency prevents memory leaks
- **Memory Leaks**: Use weak references and lifecycle-aware components
- **Optimization**: Object pooling, efficient collections, lazy evaluation
- **Profiling Tools**: VisualVM, Android Profiler, jmap/jhat

Understanding memory management is crucial for building efficient Kotlin applications.
