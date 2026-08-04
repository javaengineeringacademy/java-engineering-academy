# Kotlin Advanced Concepts

This section covers advanced Kotlin features including coroutines, flows, channels, delegates, and more.

## Table of Contents

- [Coroutines](#coroutines)
- [Flows](#flows)
- [Channels](#channels)
- [Delegates](#delegates)
- [Inline Functions](#inline-functions)
- [Reified Generics](#reified-generics)
- [DSL Builders](#dsl-builders)
- [Multiplatform](#multiplatform)
- [Advanced Type System](#advanced-type-system)
- [Meta-programming](#meta-programming)

## Coroutines

### Basic Coroutines

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

// Coroutine builder - launch
fun exampleLaunch() = runBlocking {
    val job: Job = launch {
        repeat(1000) { i ->
            println("Job is running: $i")
            delay(500)
        }
    }
    
    delay(1300)
    println("Main: I'm tired of waiting!")
    job.cancel()
    println("Main: Now I can quit.")
}

// Coroutine builder - async
fun exampleAsync() = runBlocking {
    val deferred1 = async {
        delay(1000)
        println("Loading 1...")
        "Result 1"
    }
    
    val deferred2 = async {
        delay(2000)
        println("Loading 2...")
        "Result 2"
    }
    
    // Both run concurrently
    println("Results: ${deferred1.await()} and ${deferred2.await()}")
}
```

### Coroutine Context and Dispatchers

```kotlin
import kotlinx.coroutines.*

// Dispatchers
fun exampleDispatchers() = runBlocking {
    // Default dispatcher - for CPU-intensive work
    launch(Dispatchers.Default) {
        println("Default: ${Thread.currentThread().name}")
    }
    
    // IO dispatcher - for I/O operations
    launch(Dispatchers.IO) {
        println("IO: ${Thread.currentThread().name}")
    }
    
    // Main dispatcher - for Android UI operations
    // launch(Dispatchers.Main) { ... }
    
    // Unconfined - runs in the current thread until first suspension
    launch(Dispatchers.Unconfined) {
        println("Unconfined: ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined after delay: ${Thread.currentThread().name}")
    }
}

// Custom dispatcher
fun customDispatcher() = runBlocking {
    val customDispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
    
    launch(customDispatcher) {
        println("Custom: ${Thread.currentThread().name}")
    }
    
    // Don't forget to close the dispatcher
    (customDispatcher as ExecutorCoroutineDispatcher).close()
}
```

### Structured Concurrency

```kotlin
import kotlinx.coroutines.*

// Structured concurrency
fun structuredConcurrency() = runBlocking {
    // Parent coroutine waits for all children
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

// CoroutineScope
class MyViewModel : CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    private var job: Job? = null
    
    fun startLoading() {
        job = launch {
            // Load data
            val data = withContext(Dispatchers.IO) {
                // Perform network call
                delay(1000)
                "Data"
            }
            // Update UI with data
            println("Loaded: $data")
        }
    }
    
    fun stopLoading() {
        job?.cancel()
    }
    
    fun onDestroy() {
        cancel()  // Cancel all coroutines in this scope
    }
}

// Exception handling
fun exceptionHandling() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught: ${exception.message}")
    }
    
    launch(handler) {
        throw RuntimeException("Test exception")
    }
    
    delay(1000)
}
```

### Coroutine Control

```kotlin
import kotlinx.coroutines.*

// Cancellation
fun cancellation() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("Job: $i")
                delay(500)
            }
        } catch (e: CancellationException) {
            println("Job cancelled: ${e.message}")
            // Cleanup resources
        } finally {
            println("Cleaning up...")
            // Non-cancellable context for cleanup
            withContext(NonCancellable) {
                delay(1000)
                println("Cleanup completed")
            }
        }
    }
    
    delay(1300)
    job.cancelAndJoin()
    println("Main: Job is cancelled")
}

// Timeout
fun timeout() = runBlocking {
    try {
        withTimeout(3000) {
            repeat(100) { i ->
                println("Job: $i")
                delay(500)
            }
        }
    } catch (e: TimeoutCancellationException) {
        println("Timed out: ${e.message}")
    }
}

// Dispatchers with timeout
fun timeoutWithDispatcher() = runBlocking {
    val result = withTimeoutOrNull(3000) {
        withContext(Dispatchers.IO) {
            delay(2000)
            "Result"
        }
    }
    println("Result: $result")
}
```

## Flows

### Basic Flows

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Simple flow
fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

// Collecting flows
fun flowExample() = runBlocking {
    simpleFlow().collect { value ->
        println(value)
    }
}

// Flow builder
fun flowBuilder() = runBlocking {
    val flow = flow {
        for (i in 1..5) {
            emit(i)
            delay(100)
        }
    }
    
    flow.collect { println(it) }
}
```

### Flow Operators

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Transform operators
fun transformOperators() = runBlocking {
    // Map
    (1..5).asFlow()
        .map { it * it }
        .collect { println(it) }
    
    // Filter
    (1..10).asFlow()
        .filter { it % 2 == 0 }
        .collect { println(it) }
    
    // Transform
    (1..5).asFlow()
        .transform { value ->
            emit("Processing $value")
            emit("Done $value")
        }
        .collect { println(it) }
    
    // Take
    (1..100).asFlow()
        .take(3)
        .collect { println(it) }
}

// Terminal operators
fun terminalOperators() = runBlocking {
    // Collect
    (1..5).asFlow()
        .collect { println(it) }
    
    // Reduce
    val sum = (1..5).asFlow()
        .reduce { a, b -> a + b }
    println("Sum: $sum")
    
    // Fold
    val product = (1..5).asFlow()
        .fold(1) { acc, value -> acc * value }
    println("Product: $product")
    
    // ToList
    val list = (1..5).asFlow()
        .toList()
    println("List: $list")
    
    // First
    val first = (1..5).asFlow()
        .first()
    println("First: $first")
}

// Flow context
fun flowContext() = runBlocking {
    (1..5).asFlow()
        .flowOn(Dispatchers.IO)
        .collect { println("${Thread.currentThread().name}: $it") }
}
```

### StateFlow and SharedFlow

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// StateFlow
class CounterViewModel {
    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count.asStateFlow()
    
    fun increment() {
        _count.value++
    }
    
    fun decrement() {
        _count.value--
    }
}

// SharedFlow
class EventBus {
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()
    
    suspend fun emit(event: Event) {
        _events.emit(event)
    }
}

sealed class Event {
    data class Message(val text: String) : Event()
    data class Error(val exception: Exception) : Event()
}

// Usage
fun stateFlowExample() = runBlocking {
    val viewModel = CounterViewModel()
    
    // Collect state
    val job = launch {
        viewModel.count.collect { count ->
            println("Count: $count")
        }
    }
    
    viewModel.increment()
    delay(100)
    viewModel.increment()
    delay(100)
    viewModel.decrement()
    
    job.cancel()
}
```

## Channels

### Basic Channels

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Basic channel
fun channelExample() = runBlocking {
    val channel = Channel<Int>()
    
    launch {
        for (i in 1..5) {
            channel.send(i)
            println("Sent: $i")
        }
        channel.close()
    }
    
    for (value in channel) {
        println("Received: $value")
    }
}

// Channel with buffer
fun bufferedChannel() = runBlocking {
    val channel = Channel<Int>(Channel.BUFFERED)
    
    launch {
        for (i in 1..5) {
            channel.send(i)
            println("Sent: $i")
        }
        channel.close()
    }
    
    for (value in channel) {
        println("Received: $value")
    }
}
```

### Channel Types

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Rendezvous channel (no buffer)
fun rendezvousChannel() = runBlocking {
    val channel = Channel<Int>()  // rendezvous
    
    launch {
        for (i in 1..3) {
            channel.send(i)
            println("Sent: $i")
        }
        channel.close()
    }
    
    for (value in channel) {
        println("Received: $value")
        delay(100)
    }
}

// Buffered channel
fun bufferedChannelExample() = runBlocking {
    val channel = Channel<Int>(5)  // buffer size 5
    
    launch {
        for (i in 1..5) {
            channel.send(i)
            println("Sent: $i")
        }
        channel.close()
    }
    
    for (value in channel) {
        println("Received: $value")
    }
}

// Conflated channel
fun conflatedChannel() = runBlocking {
    val channel = Channel<Int>(Channel.CONFLATED)
    
    launch {
        for (i in 1..5) {
            channel.send(i)
            println("Sent: $i")
            delay(10)
        }
        channel.close()
    }
    
    delay(100)
    for (value in channel) {
        println("Received: $value")
    }
}
```

### Channel Communication Patterns

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Producer-consumer pattern
fun producerConsumer() = runBlocking {
    val channel = Channel<Int>()
    
    // Producer
    val producer = launch {
        for (i in 1..10) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
    
    // Consumer
    val consumer = launch {
        for (value in channel) {
            println("Consumed: $value")
            delay(200)
        }
    }
    
    producer.join()
    consumer.join()
}

// Pipeline pattern
fun pipeline() = runBlocking {
    val channel = Channel<Int>()
    val resultChannel = Channel<String>()
    
    // Stage 1: Generate numbers
    launch {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
    
    // Stage 2: Square numbers
    launch {
        for (value in channel) {
            resultChannel.send("Squared: ${value * value}")
        }
        resultChannel.close()
    }
    
    // Stage 3: Print results
    for (result in resultChannel) {
        println(result)
    }
}
```

## Delegates

### Property Delegates

```kotlin
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

// Lazy delegate
class LazyExample {
    val expensiveValue: String by lazy {
        println("Computing expensive value...")
        "Expensive Value"
    }
}

// Observable delegate
class ObservableExample {
    var name: String by Delegates.observable("Initial") { prop, old, new ->
        println("$old -> $new")
    }
}

// Vetoable delegate
class VetoableExample {
    var age: Int by Delegates.vetoable(0) { prop, old, new ->
        new >= 0  // Only allow non-negative values
    }
}

// Usage
fun delegateExample() {
    val lazy = LazyExample()
    println(lazy.expensiveValue)  // Computes and caches
    println(lazy.expensiveValue)  // Uses cached value
    
    val observable = ObservableExample()
    observable.name = "Changed"  // Prints: Initial -> Changed
    
    val vetoable = VetoableExample()
    vetoable.age = 25  // Allowed
    println(vetoable.age)  // 25
    vetoable.age = -5  // Rejected
    println(vetoable.age)  // Still 25
}
```

### Custom Delegates

```kotlin
import kotlin.reflect.KProperty

// Custom delegate
class PreferenceDelegate<T>(
    private val defaultValue: T
) {
    private var value: T = defaultValue
    
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }
    
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        println("${property.name} changed to $value")
    }
}

// Usage
class Settings {
    var theme: String by PreferenceDelegate("light")
    var fontSize: Int by PreferenceDelegate(14)
}

// Map delegate
class MapDelegate(private val map: Map<String, Any>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Any? {
        return map[property.name]
    }
}

// Usage
fun mapDelegateExample() {
    val config = mapOf(
        "host" to "localhost",
        "port" to 8080,
        "debug" to true
    )
    
    class Server {
        val host: String by MapDelegate(config)
        val port: Int by MapDelegate(config)
        val debug: Boolean by MapDelegate(config)
    }
    
    val server = Server()
    println("${server.host}:${server.port} (debug: ${server.debug})")
}
```

## Inline Functions

### Advanced Inline Functions

```kotlin
// Inline function with reified type
inline fun <reified T> isType(value: Any): Boolean {
    return value is T
}

// Inline function with multiple lambdas
inline fun <T> measureTime(
    block: () -> T,
    noinline onFinally: () -> Unit = {}
): T {
    val startTime = System.nanoTime()
    try {
        return block()
    } finally {
        val endTime = System.nanoTime()
        println("Execution time: ${endTime - startTime}ns")
        onFinally()
    }
}

// Crossinline - prevents non-local returns
inline fun runInThread(crossinline action: () -> Unit) {
    Thread {
        action()  // Can't use return here
    }.start()
}

// Noinline - prevents inlining
inline fun inlinedFunction(
    inlined: () -> Unit,
    noinline notInlined: () -> Unit
) {
    inlined()  // This is inlined
    notInlined()  // This is not inlined
}

// Usage
fun inlineExample() {
    val result = measureTime({
        Thread.sleep(100)
        "Result"
    }, {
        println("Cleanup")
    })
    println(result)
}
```

## Reified Generics

### Reified Type Parameters

```kotlin
// Reified type parameters
inline fun <reified T> filterByType(list: List<Any>): List<T> {
    return list.filterIsInstance<T>()
}

// Reified with where clause
inline fun <reified T> deserialize(json: String): T? {
    // Implementation would use reflection
    return null
}

// Reified in extension functions
inline fun <reified T> Any.isInstanceOf(): Boolean {
    return this is T
}

// Usage
fun reifiedExample() {
    val mixedList = listOf(1, "hello", 2.0, "world", 3)
    val strings = filterByType<String>(mixedList)
    println(strings)  // [hello, world]
    
    val value: Any = "Hello"
    println(value.isInstanceOf<String>())  // true
    println(value.isInstanceOf<Int>())  // false
}
```

## DSL Builders

### Type-Safe Builders

```kotlin
// HTML DSL
@DslMarker
annotation class HtmlDsl

@HtmlDsl
class HTML {
    private val children = mutableListOf<HtmlElement>()
    
    fun head(init: Head.() -> Unit) {
        children.add(Head().apply(init))
    }
    
    fun body(init: Body.() -> Unit) {
        children.add(Body().apply(init))
    }
    
    override fun toString(): String {
        return "<html>\n${children.joinToString("\n")}\n</html>"
    }
}

@HtmlDsl
class Head : HtmlElement() {
    fun title(text: String) {
        children.add(TextElement("<title>$text</title>"))
    }
}

@HtmlDsl
class Body : HtmlElement() {
    fun h1(text: String, init: H1.() -> Unit = {}) {
        children.add(H1().apply {
            children.add(TextElement(text))
            init()
        })
    }
    
    fun p(text: String) {
        children.add(TextElement("<p>$text</p>"))
    }
}

@HtmlDsl
open class HtmlElement {
    val children = mutableListOf<HtmlElement>()
}

class TextElement(private val text: String) : HtmlElement() {
    override fun toString(): String = text
}

class H1 : HtmlElement()

// Builder function
fun html(init: HTML.() -> Unit): HTML {
    return HTML().apply(init)
}

// Usage
fun dslExample() {
    val page = html {
        head {
            title("My Page")
        }
        body {
            h1("Welcome")
            p("This is a paragraph")
        }
    }
    println(page)
}
```

### Builder Pattern

```kotlin
// Builder pattern with DSL
class DatabaseConfig private constructor(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String,
    val maxConnections: Int,
    val timeout: Long
) {
    class Builder {
        private var host: String = "localhost"
        private var port: Int = 5432
        private var username: String = ""
        private var password: String = ""
        private var database: String = ""
        private var maxConnections: Int = 10
        private var timeout: Long = 30000
        
        fun host(host: String) = apply { this.host = host }
        fun port(port: Int) = apply { this.port = port }
        fun username(username: String) = apply { this.username = username }
        fun password(password: String) = apply { this.password = password }
        fun database(database: String) = apply { this.database = database }
        fun maxConnections(maxConnections: Int) = apply { this.maxConnections = maxConnections }
        fun timeout(timeout: Long) = apply { this.timeout = timeout }
        
        fun build(): DatabaseConfig {
            require(host.isNotBlank()) { "Host must not be blank" }
            require(username.isNotBlank()) { "Username must not be blank" }
            require(database.isNotBlank()) { "Database must not be blank" }
            
            return DatabaseConfig(
                host, port, username, password,
                database, maxConnections, timeout
            )
        }
    }
}

// DSL-style builder
class DatabaseConfigDsl {
    var host: String = "localhost"
    var port: Int = 5432
    var username: String = ""
    var password: String = ""
    var database: String = ""
    var maxConnections: Int = 10
    var timeout: Long = 30000
}

fun databaseConfig(init: DatabaseConfigDsl.() -> Unit): DatabaseConfig {
    val config = DatabaseConfigDsl().apply(init)
    return DatabaseConfig.Builder()
        .host(config.host)
        .port(config.port)
        .username(config.username)
        .password(config.password)
        .database(config.database)
        .maxConnections(config.maxConnections)
        .timeout(config.timeout)
        .build()
}

// Usage
fun builderExample() {
    val config = databaseConfig {
        host = "db.example.com"
        port = 5432
        username = "admin"
        password = "secret"
        database = "myapp"
        maxConnections = 20
        timeout = 60000
    }
}
```

## Multiplatform

### Kotlin Multiplatform

```kotlin
// Common code
expect fun platformName(): String

class Platform {
    val name: String = platformName()
}

// JVM implementation
actual fun platformName(): String = "JVM"

// JavaScript implementation
actual fun platformName(): String = "JavaScript"

// Native implementation
actual fun platformName(): String = "Native"
```

### Multiplatform Project Structure

```kotlin
// commonMain/src/Main.kt
expect class Platform() {
    val name: String
}

// jvmMain/src/Platform.kt
actual class Platform actual constructor() {
    actual val name: String = "JVM"
}

// jsMain/src/Platform.kt
actual class Platform actual constructor() {
    actual val name: String = "JS"
}

// nativeMain/src/Platform.kt
actual class Platform actual constructor() {
    actual val name: String = "Native"
}
```

## Advanced Type System

### Variance

```kotlin
// Covariance (out)
class Producer<out T>(private val value: T) {
    fun get(): T = value
}

// Contravariance (in)
class Consumer<in T> {
    fun consume(item: T) {
        println(item)
    }
}

// Invariance (no modifier)
class MutableBox<T>(var item: T) {
    fun get(): T = item
    fun set(item: T) {
        this.item = item
    }
}

// Usage
fun varianceExample() {
    val stringProducer: Producer<String> = Producer("Hello")
    val anyProducer: Producer<Any> = stringProducer  // Allowed due to covariance
    
    val anyConsumer: Consumer<Any> = Consumer()
    val stringConsumer: Consumer<String> = anyConsumer  // Allowed due to contravariance
    
    val stringBox = MutableBox("Hello")
    // val anyBox: MutableBox<Any> = stringBox  // Not allowed - invariance
}
```

### Type Aliases

```kotlin
// Type aliases
typealias StringMap = MutableMap<String, String>
typealias Predicate<T> = (T) -> Boolean
typealias ResultHandler<T> = (Result<T>) -> Unit

// Usage
fun typeAliasExample() {
    val map: StringMap = mutableMapOf("key" to "value")
    val isEven: Predicate<Int> = { it % 2 == 0 }
    
    val evens = listOf(1, 2, 3, 4, 5).filter(isEven)
    println(evens)  // [2, 4]
}
```

## Meta-programming

### Annotations

```kotlin
// Custom annotations
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonName(val value: String)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class JsonSerializable

// Using annotations
@JsonSerializable
class User(
    @JsonName("user_name")
    val name: String,
    
    @JsonName("user_age")
    val age: Int
)

// Reflection with annotations
fun serialize(obj: Any): String {
    val klass = obj::class
    val jsonName = klass.findAnnotation<JsonSerializable>()
    
    if (jsonName == null) {
        return obj.toString()
    }
    
    val properties = klass.memberProperties
    val json = properties.map { property ->
        val name = property.findAnnotation<JsonName>()?.value ?: property.name
        val value = property.call(obj)
        "\"$name\": \"$value\""
    }
    
    return "{${json.joinToString(", ")}}"
}
```

## Summary

Advanced Kotlin concepts enable powerful and expressive code:

- **Coroutines**: Lightweight concurrency with structured concurrency
- **Flows**: Cold asynchronous streams with rich operators
- **Channels**: Hot communication channels between coroutines
- **Delegates**: Powerful property delegation with custom implementations
- **Inline Functions**: Zero-cost abstractions with reified generics
- **DSL Builders**: Type-safe builders for creating domain-specific languages
- **Multiplatform**: Write once, run anywhere with expect/actual
- **Advanced Type System**: Variance, type aliases, and higher-kinded types
- **Meta-programming**: Annotations and reflection for code generation

These advanced features make Kotlin suitable for complex, modern applications.
