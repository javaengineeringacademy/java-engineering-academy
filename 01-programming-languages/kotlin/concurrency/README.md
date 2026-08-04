# Kotlin Concurrency

This section covers concurrent programming in Kotlin using coroutines, dispatchers, channels, and flows.

## Table of Contents

- [Coroutines](#coroutines)
- [Dispatchers](#dispatchers)
- [Structured Concurrency](#structured-concurrency)
- [Actors](#actors)
- [Shared State](#shared-state)
- [Channels](#channels)
- [Flows](#flows)
- [Synchronization](#synchronization)
- [Error Handling](#error-handling)
- [Performance](#performance)

## Coroutines

### Creating Coroutines

```kotlin
import kotlinx.coroutines.*

// launch - fire and forget
fun launchExample() = runBlocking {
    val job: Job = launch {
        delay(1000)
        println("Coroutine completed")
    }
    println("Main continues")
    job.join()  // Wait for coroutine to finish
}

// async - returns a result
fun asyncExample() = runBlocking {
    val deferred: Deferred<String> = async {
        delay(1000)
        "Result from coroutine"
    }
    
    val result = deferred.await()
    println(result)
}

// runBlocking - blocks current thread
fun runBlockingExample() {
    runBlocking {
        delay(1000)
        println("Coroutine completed")
    }
    println("Main thread continues after blocking")
}

// coroutineScope - creates a new scope
fun coroutineScopeExample() = runBlocking {
    coroutineScope {
        launch {
            delay(1000)
            println("Child 1")
        }
        launch {
            delay(2000)
            println("Child 2")
        }
    }
    println("All children completed")
}
```

### Coroutine Builders

```kotlin
import kotlinx.coroutines.*

// launch - fire and forget
fun launchBuilder() = runBlocking {
    val job = launch {
        repeat(5) { i ->
            println("Job: $i")
            delay(500)
        }
    }
    
    delay(1300)
    job.cancel()
    println("Job cancelled")
}

// async - returns Deferred
fun asyncBuilder() = runBlocking {
    val deferred1 = async {
        delay(1000)
        10
    }
    
    val deferred2 = async {
        delay(2000)
        20
    }
    
    // Both run concurrently
    val sum = deferred1.await() + deferred2.await()
    println("Sum: $sum")
}

// runBlocking - bridges blocking and non-blocking
fun runBlockingBuilder() = runBlocking {
    launch {
        delay(1000)
        println("World!")
    }
    println("Hello,")
    delay(2000)
}

// coroutineScope - creates a new scope
fun coroutineScopeBuilder() = runBlocking {
    coroutineScope {
        launch {
            delay(1000)
            println("Child 1")
        }
        launch {
            delay(2000)
            println("Child 2")
        }
    }
    println("Scope completed")
}
```

## Dispatchers

### Available Dispatchers

```kotlin
import kotlinx.coroutines.*

// Default - CPU-intensive work
fun defaultDispatcher() = runBlocking {
    launch(Dispatchers.Default) {
        println("Default: ${Thread.currentThread().name}")
        // CPU-bound work
        val result = (1..1000000).toList().sum()
        println("Sum: $result")
    }
}

// IO - I/O operations
fun ioDispatcher() = runBlocking {
    launch(Dispatchers.IO) {
        println("IO: ${Thread.currentThread().name}")
        // I/O-bound work
        delay(1000)
        println("IO operation completed")
    }
}

// Main - UI operations (Android)
// launch(Dispatchers.Main) {
//     // Update UI
// }

// Unconfined - runs in current thread
fun unconfinedDispatcher() = runBlocking {
    launch(Dispatchers.Unconfined) {
        println("Unconfined: ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined after delay: ${Thread.currentThread().name}")
    }
    
    delay(200)
}

// Single thread
fun singleDispatcher() = runBlocking {
    val singleThread = newSingleThreadContext("MyThread")
    launch(singleThread) {
        println("Single: ${Thread.currentThread().name}")
    }
    
    delay(1000)
    singleThread.close()
}
```

### Custom Dispatchers

```kotlin
import kotlinx.coroutines.*
import java.util.concurrent.Executors

// Fixed thread pool
fun fixedThreadPoolDispatcher() = runBlocking {
    val dispatcher = Executors.newFixedThreadPool(4).asCoroutineDispatcher()
    
    val jobs = List(10) { i ->
        launch(dispatcher) {
            println("Job $i: ${Thread.currentThread().name}")
            delay(1000)
        }
    }
    
    jobs.forEach { it.join() }
    dispatcher.close()
}

// Single thread context
fun singleThreadContext() = runBlocking {
    val singleThread = newSingleThreadContext("MyThread")
    
    val jobs = List(5) { i ->
        launch(singleThread) {
            println("Job $i: ${Thread.currentThread().name}")
        }
    }
    
    jobs.forEach { it.join() }
    singleThread.close()
}

// Custom dispatcher with limited parallelism
fun limitedParallelism() = runBlocking {
    val limitedDispatcher = Dispatchers.Default.limitedParallelism(2)
    
    val jobs = List(10) { i ->
        launch(limitedDispatcher) {
            println("Job $i: ${Thread.currentThread().name}")
            delay(1000)
        }
    }
    
    jobs.forEach { it.join() }
}
```

## Structured Concurrency

### Parent-Child Relationships

```kotlin
import kotlinx.coroutines.*

// Parent waits for all children
fun parentChildExample() = runBlocking {
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

// Child failure affects parent
fun childFailureExample() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught: ${exception.message}")
    }
    
    launch(handler) {
        launch {
            throw RuntimeException("Child 1 failed")
        }
        
        launch {
            delay(1000)
            println("Child 2 completed")
        }
    }
    
    delay(2000)
}

// Coroutine scope
class MyScope : CoroutineScope by CoroutineScope(Dispatchers.Default + Job()) {
    fun startWork() {
        launch {
            delay(1000)
            println("Work completed")
        }
    }
    
    fun cancelWork() {
        cancel()
    }
}
```

### Exception Handling

```kotlin
import kotlinx.coroutines.*

// CoroutineExceptionHandler
fun exceptionHandlerExample() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception caught: ${exception.message}")
    }
    
    val job = launch(handler) {
        throw RuntimeException("Test exception")
    }
    
    job.join()
}

// Supervisor job
fun supervisorJobExample() = runBlocking {
    val supervisor = SupervisorJob()
    
    val scope = CoroutineScope(supervisor)
    
    scope.launch {
        throw RuntimeException("Child 1 failed")
    }
    
    scope.launch {
        delay(1000)
        println("Child 2 completed")
    }
    
    delay(2000)
    supervisor.cancel()
}

// Try-catch in coroutines
fun tryCatchExample() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("Job: $i")
                delay(500)
            }
        } catch (e: CancellationException) {
            println("Job cancelled: ${e.message}")
        } finally {
            println("Cleanup")
            withContext(NonCancellable) {
                delay(1000)
                println("Cleanup completed")
            }
        }
    }
    
    delay(1300)
    job.cancelAndJoin()
}
```

## Actors

### Basic Actor Pattern

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Actor definition
class CounterActor {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val channel = Channel<CounterCommand>(Channel.BUFFERED)
    
    init {
        scope.launch {
            var count = 0
            for (command in channel) {
                when (command) {
                    is CounterCommand.Increment -> count++
                    is CounterCommand.Decrement -> count--
                    is CounterCommand.Get -> command.response.complete(count)
                }
            }
        }
    }
    
    fun increment() {
        channel.trySend(CounterCommand.Increment)
    }
    
    fun decrement() {
        channel.trySend(CounterCommand.Decrement)
    }
    
    suspend fun getCount(): Int {
        val response = CompletableDeferred<Int>()
        channel.send(CounterCommand.Get(response))
        return response.await()
    }
    
    fun close() {
        channel.close()
        scope.cancel()
    }
}

sealed class CounterCommand {
    object Increment : CounterCommand()
    object Decrement : CounterCommand()
    data class Get(val response: CompletableDeferred<Int>) : CounterCommand()
}

// Usage
fun actorExample() = runBlocking {
    val counter = CounterActor()
    
    counter.increment()
    counter.increment()
    counter.increment()
    counter.decrement()
    
    val count = counter.getCount()
    println("Count: $count")
    
    counter.close()
}
```

### Actor for State Management

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Actor for managing state
class StateActor<T>(initialState: T) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val channel = Channel<StateCommand<T>>(Channel.BUFFERED)
    
    init {
        scope.launch {
            var state = initialState
            for (command in channel) {
                when (command) {
                    is StateCommand.Update -> {
                        state = command.transformer(state)
                    }
                    is StateCommand.Get -> {
                        command.response.complete(state)
                    }
                }
            }
        }
    }
    
    suspend fun update(transformer: (T) -> T) {
        channel.send(StateCommand.Update(transformer))
    }
    
    suspend fun getState(): T {
        val response = CompletableDeferred<T>()
        channel.send(StateCommand.Get(response))
        return response.await()
    }
    
    fun close() {
        channel.close()
        scope.cancel()
    }
}

sealed class StateCommand<T> {
    data class Update<T>(val transformer: (T) -> T) : StateCommand<T>()
    data class Get<T>(val response: CompletableDeferred<T>) : StateCommand<T>()
}

// Usage
fun stateActorExample() = runBlocking {
    val counter = StateActor(0)
    
    counter.update { it + 1 }
    counter.update { it + 1 }
    counter.update { it + 1 }
    
    val state = counter.getState()
    println("State: $state")
    
    counter.close()
}
```

## Shared State

### Thread-Safe Data Structures

```kotlin
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ConcurrentHashMap

// AtomicInteger
fun atomicExample() = runBlocking {
    val counter = AtomicInteger(0)
    
    val jobs = List(1000) {
        launch {
            repeat(1000) {
                counter.incrementAndGet()
            }
        }
    }
    
    jobs.forEach { it.join() }
    println("Counter: ${counter.get()}")
}

// ConcurrentHashMap
fun concurrentMapExample() = runBlocking {
    val map = ConcurrentHashMap<String, Int>()
    
    val jobs = List(100) { i ->
        launch {
            repeat(100) { j ->
                map["key-${i * 100 + j}"] = j
            }
        }
    }
    
    jobs.forEach { it.join() }
    println("Map size: ${map.size}")
}
```

### Mutex

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Basic mutex usage
fun mutexExample() = runBlocking {
    val mutex = Mutex()
    var counter = 0
    
    val jobs = List(1000) {
        launch {
            mutex.withLock {
                counter++
            }
        }
    }
    
    jobs.forEach { it.join() }
    println("Counter: $counter")
}

// Mutex with timeout
fun mutexWithTimeout() = runBlocking {
    val mutex = Mutex()
    
    val job1 = launch {
        mutex.withLock {
            println("Job 1 acquired lock")
            delay(2000)
            println("Job 1 releasing lock")
        }
    }
    
    val job2 = launch {
        delay(500)
        if (mutex.tryLock()) {
            try {
                println("Job 2 acquired lock")
            } finally {
                mutex.unlock()
            }
        } else {
            println("Job 2 couldn't acquire lock")
        }
    }
    
    joinAll(job1, job2)
}
```

### ThreadLocal

```kotlin
import kotlinx.coroutines.*

// ThreadLocal usage
fun threadLocalExample() = runBlocking {
    val threadLocal = ThreadLocal<String>()
    
    val jobs = List(5) { i ->
        launch(Dispatchers.Default) {
            threadLocal.set("Value $i")
            delay(100)
            println("Thread ${Thread.currentThread().name}: ${threadLocal.get()}")
        }
    }
    
    jobs.forEach { it.join() }
}

// CoroutineContext element
val ContextKey = CoroutineContext.Key<ContextElement>

class ContextElement(val value: String) : CoroutineContext.Element {
    override val key: CoroutineContext.Key<*> = ContextKey
}

fun coroutineContextExample() = runBlocking {
    val job = launch(ContextElement("Hello")) {
        val value = coroutineContext[ContextKey]?.value
        println("Value: $value")
    }
    
    job.join()
}
```

## Channels

### Channel Types

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Rendezvous channel (no buffer)
fun rendezvousChannel() = runBlocking {
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
        delay(100)
    }
}

// Buffered channel
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

### Channel Operations

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Send and receive
fun sendReceiveExample() = runBlocking {
    val channel = Channel<Int>(Channel.BUFFERED)
    
    launch {
        for (i in 1..5) {
            channel.send(i)
        }
        channel.close()
    }
    
    for (value in channel) {
        println("Received: $value")
    }
}

// Try send and receive
fun trySendReceiveExample() = runBlocking {
    val channel = Channel<Int>(Channel.BUFFERED)
    
    launch {
        for (i in 1..5) {
            val result = channel.trySend(i)
            if (result.isSuccess) {
                println("Sent: $i")
            }
        }
        channel.close()
    }
    
    for (value in channel) {
        println("Received: $value")
    }
}

// Channel iteration
fun channelIterationExample() = runBlocking {
    val channel = Channel<String>(Channel.BUFFERED)
    
    launch {
        val messages = listOf("Hello", "World", "Foo", "Bar")
        for (message in messages) {
            channel.send(message)
        }
        channel.close()
    }
    
    for (message in channel) {
        println("Message: $message")
    }
}
```

## Flows

### Basic Flows

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

// Flow collection
fun flowCollectionExample() = runBlocking {
    simpleFlow().collect { value ->
        println("Value: $value")
    }
}

// Flow builder
fun flowBuilderExample() = runBlocking {
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

// Transformation operators
fun transformationOperators() = runBlocking {
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

## Synchronization

### Synchronized Blocks

```kotlin
import kotlinx.coroutines.*

// Synchronized example
fun synchronizedExample() = runBlocking {
    val lock = Any()
    var counter = 0
    
    val jobs = List(1000) {
        launch {
            synchronized(lock) {
                counter++
            }
        }
    }
    
    jobs.forEach { it.join() }
    println("Counter: $counter")
}

// ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

fun readWriteLockExample() = runBlocking {
    val lock = ReentrantReadWriteLock()
    var data = mutableListOf<Int>()
    
    val readers = List(5) { i ->
        launch {
            lock.readLock().lock()
            try {
                println("Reader $i: ${data.size}")
                delay(100)
            } finally {
                lock.readLock().unlock()
            }
        }
    }
    
    val writers = List(2) { i ->
        launch {
            delay(50)
            lock.writeLock().lock()
            try {
                data.add(i)
                println("Writer $i: added $i")
            } finally {
                lock.writeLock().unlock()
            }
        }
    }
    
    joinAll(readers + writers)
}
```

### Concurrent Data Structures

```kotlin
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicReference
import java.util.concurrent.ConcurrentLinkedQueue

// AtomicReference
fun atomicReferenceExample() = runBlocking {
    val atomicRef = AtomicReference(listOf<Int>())
    
    val jobs = List(100) { i ->
        launch {
            while (true) {
                val current = atomicRef.get()
                val updated = current + i
                if (atomicRef.compareAndSet(current, updated)) {
                    break
                }
            }
        }
    }
    
    jobs.forEach { it.join() }
    println("List size: ${atomicRef.get().size}")
}

// ConcurrentLinkedQueue
fun concurrentQueueExample() = runBlocking {
    val queue = ConcurrentLinkedQueue<Int>()
    
    val producers = List(5) { i ->
        launch {
            for (j in 1..100) {
                queue.add(i * 100 + j)
            }
        }
    }
    
    val consumers = List(5) { i ->
        launch {
            repeat(100) {
                while (true) {
                    val item = queue.poll()
                    if (item != null) {
                        println("Consumer $i: $item")
                        break
                    }
                    delay(1)
                }
            }
        }
    }
    
    joinAll(producers + consumers)
}
```

## Error Handling

### Coroutine Exception Handling

```kotlin
import kotlinx.coroutines.*

// Try-catch in coroutines
fun tryCatchInCoroutine() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("Job: $i")
                delay(500)
            }
        } catch (e: CancellationException) {
            println("Job cancelled: ${e.message}")
        } finally {
            println("Cleanup")
            withContext(NonCancellable) {
                delay(1000)
                println("Cleanup completed")
            }
        }
    }
    
    delay(1300)
    job.cancelAndJoin()
}

// CoroutineExceptionHandler
fun coroutineExceptionHandler() = runBlocking {
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception caught: ${exception.message}")
    }
    
    val job = launch(handler) {
        throw RuntimeException("Test exception")
    }
    
    job.join()
}

// Supervisor job
fun supervisorJob() = runBlocking {
    val supervisor = SupervisorJob()
    val scope = CoroutineScope(supervisor)
    
    scope.launch {
        throw RuntimeException("Child 1 failed")
    }
    
    scope.launch {
        delay(1000)
        println("Child 2 completed")
    }
    
    delay(2000)
    supervisor.cancel()
}
```

### Flow Error Handling

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Flow catch operator
fun flowCatchExample() = runBlocking {
    flow {
        for (i in 1..5) {
            if (i == 3) throw RuntimeException("Error at $i")
            emit(i)
        }
    }
    .catch { e -> println("Caught: ${e.message}") }
    .collect { println(it) }
}

// Flow onCompletion
fun flowOnCompletionExample() = runBlocking {
    (1..5).asFlow()
        .onCompletion { cause ->
            if (cause != null) {
                println("Flow completed with error: ${cause.message}")
            } else {
                println("Flow completed successfully")
            }
        }
        .collect { println(it) }
}

// Flow retry
fun flowRetryExample() = runBlocking {
    var attempt = 0
    
    flow {
        attempt++
        if (attempt < 3) {
            throw RuntimeException("Attempt $attempt failed")
        }
        emit(attempt)
    }
    .retry(3) { cause ->
        cause is RuntimeException
    }
    .collect { println("Success: $it") }
}
```

## Performance

### Coroutine Performance Tips

```kotlin
import kotlinx.coroutines.*

// Use appropriate dispatcher
fun appropriateDispatcher() = runBlocking {
    // CPU-bound work
    launch(Dispatchers.Default) {
        val result = (1..1000000).toList().sum()
        println("CPU result: $result")
    }
    
    // I/O-bound work
    launch(Dispatchers.IO) {
        delay(1000)
        println("I/O completed")
    }
}

// Avoid blocking operations
fun avoidBlocking() = runBlocking {
    // Bad: Blocking call
    // Thread.sleep(1000)
    
    // Good: Non-blocking delay
    delay(1000)
}

// Use coroutine scope for lifecycle
class MyViewModel : CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    private var job: Job? = null
    
    fun loadData() {
        job = launch {
            val data = withContext(Dispatchers.IO) {
                // Perform network call
                delay(1000)
                "Data"
            }
            // Update UI with data
            println("Loaded: $data")
        }
    }
    
    fun cancelLoad() {
        job?.cancel()
    }
    
    fun onDestroy() {
        cancel()
    }
}
```

## Summary

Kotlin concurrency provides powerful tools for parallel and asynchronous programming:

- **Coroutines**: Lightweight threads with structured concurrency
- **Dispatchers**: Control which thread coroutines run on
- **Structured Concurrency**: Automatic lifecycle management
- **Actors**: Message-passing concurrency pattern
- **Synchronization**: Mutex, locks, and atomic operations
- **Channels**: Communication between coroutines
- **Flows**: Cold asynchronous streams
- **Error Handling**: Structured exception handling

Mastering these concepts is essential for building responsive and scalable applications.
