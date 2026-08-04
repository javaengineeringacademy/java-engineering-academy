# Kotlin Interview Questions

This section provides common Kotlin interview questions with detailed answers.

## Table of Contents

- [Basic Questions](#basic-questions)
- [Intermediate Questions](#intermediate-questions)
- [Advanced Questions](#advanced-questions)
- [Coroutines Questions](#coroutines-questions)
- [Collections Questions](#collections-questions)
- [Design Patterns](#design-patterns)
- [Best Practices](#best-practices)

## Basic Questions

### 1. What is Kotlin?

**Answer:**
Kotlin is a modern, statically-typed programming language that runs on the JVM, Android, iOS, macOS, Linux, Windows, and JavaScript. It is fully interoperable with Java and provides features like null safety, coroutines, extension functions, and more.

**Key Features:**
- Concise syntax
- Null safety
- Coroutines for concurrency
- Extension functions
- Data classes
- Smart casting
- Interoperable with Java

### 2. What are the differences between `val` and `var`?

**Answer:**
- `val` creates an immutable variable (read-only)
- `var` creates a mutable variable (can be reassigned)

```kotlin
val immutable = "cannot change"  // Read-only
var mutable = "can change"  // Can be reassigned
mutable = "new value"  // Allowed
// immutable = "new value"  // Compilation error
```

### 3. What is null safety in Kotlin?

**Answer:**
Kotlin provides null safety features to prevent NullPointerExceptions (NPEs):

```kotlin
// Nullable type
var nullable: String? = "Hello"
nullable = null  // Allowed

// Non-nullable type
var nonNullable: String = "Hello"
// nonNullable = null  // Compilation error

// Safe call operator
val length = nullable?.length

// Elvis operator
val length = nullable?.length ?: 0

// Not-null assertion
val length = nullable!!.length  // Throws NPE if null

// Let function
nullable?.let {
    println(it.length)
}
```

### 4. What are data classes?

**Answer:**
Data classes are classes that are primarily used to hold data. They automatically generate `equals()`, `hashCode()`, `toString()`, `copy()`, and component functions.

```kotlin
data class User(
    val id: Int,
    val name: String,
    val email: String
)

// Usage
val user = User(1, "Alice", "alice@example.com")
val copy = user.copy(name = "Bob")
val (id, name, email) = user
println(user)  // User(id=1, name=Alice, email=alice@example.com)
```

### 5. What are extension functions?

**Answer:**
Extension functions allow you to add new functions to existing classes without modifying their source code.

```kotlin
// Extension function on String
fun String.removeSpaces(): String {
    return this.replace(" ", "")
}

// Extension function with parameters
fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }
}

// Usage
val text = "Hello World"
println(text.removeSpaces())  // HelloWorld
println(text.truncate(5))  // Hello...
```

## Intermediate Questions

### 6. What are sealed classes?

**Answer:**
Sealed classes are used to represent restricted class hierarchies. All subclasses must be defined within the same file.

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Usage
fun handleResult(result: Result<String>) {
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.exception.message}")
        is Result.Loading -> println("Loading...")
    }
}
```

### 7. What are higher-order functions?

**Answer:**
Higher-order functions are functions that take functions as parameters or return functions.

```kotlin
// Function as parameter
fun performOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Function as return value
fun createMultiplier(factor: Int): (Int) -> Int {
    return { number -> number * factor }
}

// Usage
val sum = performOperation(5, 3) { a, b -> a + b }
val multiplier = createMultiplier(3)
val result = multiplier(5)  // 15
```

### 8. What are inline functions?

**Answer:**
Inline functions are functions where the function body is copied to the call site at compile time, eliminating function call overhead.

```kotlin
// Inline function
inline fun <T> measureTime(block: () -> T): T {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
    return result
}

// Crossinline - prevents non-local returns
inline fun runInThread(crossinline action: () -> Unit) {
    Thread {
        action()  // Can't use return here
    }.start()
}

// Noinline - prevents inlining
inline fun inlinedFunction(noinline lambda: () -> Unit) {
    val storedLambda = lambda  // Can store because it's noinline
    storedLambda()
}
```

### 9. What are delegation patterns in Kotlin?

**Answer:**
Kotlin supports delegation through the `by` keyword:

```kotlin
// Property delegation
class Delegate {
    private var value: String = ""
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return value
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        this.value = value
    }
}

class MyClass {
    var delegatedProperty: String by Delegate()
}

// Class delegation
interface Base {
    fun print()
}

class BaseImpl(private val x: Int) : Base {
    override fun print() { print(x) }
}

class Derived(b: Base) : Base by b

// Usage
fun delegationExample() {
    val b = BaseImpl(10)
    Derived(b).print()  // prints 10
}
```

## Advanced Questions

### 10. What are coroutines?

**Answer:**
Coroutines are lightweight threads that enable asynchronous programming without blocking the main thread.

```kotlin
import kotlinx.coroutines.*

// Launching a coroutine
fun main() = runBlocking {
    launch {
        delay(1000)
        println("World!")
    }
    println("Hello,")
}

// Structured concurrency
fun structuredConcurrency() = runBlocking {
    launch {
        delay(1000)
        println("Child 1")
    }
    
    launch {
        delay(2000)
        println("Child 2")
    }
    
    println("Parent completed")
}
```

### 11. What is the difference between `launch` and `async`?

**Answer:**
- `launch` is a coroutine builder that doesn't return a result
- `async` is a coroutine builder that returns a `Deferred` result

```kotlin
fun launchVsAsync() = runBlocking {
    // Launch - fire and forget
    val job = launch {
        delay(1000)
        println("Completed")
    }
    
    // Async - returns result
    val deferred = async {
        delay(1000)
        "Result"
    }
    
    // Wait for async result
    val result = deferred.await()
    println(result)
}
```

### 12. What are flows?

**Answer:**
Flows are cold asynchronous streams that emit values sequentially.

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Simple flow
fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
}

// Flow operators
fun flowOperators() = runBlocking {
    (1..10).asFlow()
        .filter { it % 2 == 0 }
        .map { it * it }
        .collect { println(it) }
}

// StateFlow
class CounterViewModel {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()
    
    fun increment() {
        _count.value++
    }
}
```

## Coroutines Questions

### 13. What are dispatchers in coroutines?

**Answer:**
Dispatchers determine which thread or thread pool a coroutine runs on:

```kotlin
import kotlinx.coroutines.*

fun dispatchersExample() = runBlocking {
    // Default - CPU-intensive work
    launch(Dispatchers.Default) {
        println("Default: ${Thread.currentThread().name}")
    }
    
    // IO - I/O operations
    launch(Dispatchers.IO) {
        println("IO: ${Thread.currentThread().name}")
    }
    
    // Main - UI operations
    // launch(Dispatchers.Main) { ... }
    
    // Unconfined - runs in current thread
    launch(Dispatchers.Unconfined) {
        println("Unconfined: ${Thread.currentThread().name}")
    }
}
```

### 14. What is structured concurrency?

**Answer:**
Structured concurrency ensures that child coroutines are properly managed and cancelled when the parent coroutine is cancelled.

```kotlin
import kotlinx.coroutines.*

fun structuredConcurrency() = runBlocking {
    // Parent waits for all children
    launch {
        delay(1000)
        println("Child 1 completed")
    }
    
    launch {
        delay(2000)
        println("Child 2 completed")
    }
    
    println("Parent completed")
}

// Coroutine scope
class MyViewModel : CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    fun loadData() {
        launch {
            // Perform work
            delay(1000)
            println("Data loaded")
        }
    }
    
    fun onDestroy() {
        cancel()
    }
}
```

### 15. How do you handle exceptions in coroutines?

**Answer:**
```kotlin
import kotlinx.coroutines.*

// Try-catch
fun tryCatchExample() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("Job: $i")
                delay(500)
            }
        } catch (e: CancellationException) {
            println("Job cancelled")
        } finally {
            println("Cleanup")
        }
    }
    
    delay(1300)
    job.cancelAndJoin()
}

// CoroutineExceptionHandler
fun exceptionHandlerExample() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception: ${exception.message}")
    }
    
    val job = launch(handler) {
        throw RuntimeException("Test exception")
    }
    
    job.join()
}
```

## Collections Questions

### 16. What is the difference between List and MutableList?

**Answer:**
- `List` is immutable (read-only)
- `MutableList` is mutable (can be modified)

```kotlin
// Immutable list
val list = listOf(1, 2, 3)
// list.add(4)  // Compilation error

// Mutable list
val mutableList = mutableListOf(1, 2, 3)
mutableList.add(4)  // Allowed

// Converting between them
val immutable = mutableList.toList()
val mutable = list.toMutableList()
```

### 17. What are sequences in Kotlin?

**Answer:**
Sequences provide lazy evaluation for large datasets:

```kotlin
// Eager evaluation (List)
val list = (1..1000000).toList()
val result = list
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(5)

// Lazy evaluation (Sequence)
val result = (1..1000000).asSequence()
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(5)
    .toList()
```

### 18. What are collection operations?

**Answer:**
```kotlin
val list = listOf(1, 2, 3, 4, 5)

// Transformation
val doubled = list.map { it * 2 }
val evens = list.filter { it % 2 == 0 }

// Aggregation
val sum = list.sum()
val average = list.average()

// Search
val first = list.first { it > 3 }
val contains = list.contains(3)

// Sorting
val sorted = list.sorted()
val reversed = list.reversed()
```

## Design Patterns

### 19. How do you implement the Builder pattern in Kotlin?

**Answer:**
```kotlin
// Builder pattern
class DatabaseConfig private constructor(
    val host: String,
    val port: Int,
    val username: String,
    val password: String
) {
    class Builder {
        private var host: String = ""
        private var port: Int = 5432
        private var username: String = ""
        private var password: String = ""
        
        fun host(host: String) = apply { this.host = host }
        fun port(port: Int) = apply { this.port = port }
        fun username(username: String) = apply { this.username = username }
        fun password(password: String) = apply { this.password = password }
        
        fun build(): DatabaseConfig {
            require(host.isNotBlank()) { "Host must not be blank" }
            return DatabaseConfig(host, port, username, password)
        }
    }
}

// Usage
val config = DatabaseConfig.Builder()
    .host("localhost")
    .port(5432)
    .username("admin")
    .password("secret")
    .build()
```

### 20. How do you implement the Factory pattern in Kotlin?

**Answer:**
```kotlin
// Factory pattern
interface Shape {
    fun draw()
}

class Circle : Shape {
    override fun draw() = println("Drawing circle")
}

class Rectangle : Shape {
    override fun draw() = println("Drawing rectangle")
}

object ShapeFactory {
    fun create(type: String): Shape {
        return when (type) {
            "circle" -> Circle()
            "rectangle" -> Rectangle()
            else -> throw IllegalArgumentException("Unknown shape: $type")
        }
    }
}

// Usage
val shape = ShapeFactory.create("circle")
shape.draw()
```

## Best Practices

### 21. What are Kotlin best practices?

**Answer:**
1. Use immutable variables by default (`val` over `var`)
2. Use data classes for data holders
3. Use scope functions (`let`, `apply`, `run`, `with`, `also`)
4. Use extension functions to add functionality
5. Use coroutines for concurrency
6. Use sequences for large datasets
7. Use sealed classes for restricted hierarchies
8. Use null safety features
9. Follow naming conventions
10. Write tests

### 22. How do you handle errors in Kotlin?

**Answer:**
```kotlin
// Try-catch
fun divide(a: Int, b: Int): Int {
    return try {
        a / b
    } catch (e: ArithmeticException) {
        0
    }
}

// Result type
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

fun safeDivide(a: Int, b: Int): Result<Int> {
    return try {
        Result.Success(a / b)
    } catch (e: ArithmeticException) {
        Result.Error(e)
    }
}

// Usage
when (val result = safeDivide(10, 0)) {
    is Result.Success -> println("Result: ${result.data}")
    is Result.Error -> println("Error: ${result.exception.message}")
}
```

## Summary

Key Kotlin interview topics:

- **Basics**: Variables, null safety, data classes, extension functions
- **Intermediate**: Sealed classes, higher-order functions, inline functions, delegation
- **Advanced**: Coroutines, flows, dispatchers, structured concurrency
- **Collections**: Lists, sets, maps, sequences, operations
- **Design Patterns**: Builder, Factory, Singleton
- **Best Practices**: Immutability, scope functions, testing

Practice these questions and understand the underlying concepts for successful Kotlin interviews.
