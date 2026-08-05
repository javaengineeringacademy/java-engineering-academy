# Coroutine Patterns (Kotlin)

## Overview

Kotlin's coroutines provide a powerful way to handle asynchronous programming. These
patterns enable non-blocking operations while maintaining sequential code style.
Coroutines are lightweight threads that can be suspended and resumed.

## When to Use

- Asynchronous I/O operations (network, file, database)
- CPU-intensive tasks that need to be offloaded
- UI responsiveness in Android applications
- High-concurrency applications
- Long-running operations

## Kotlin Implementation

### Basic Coroutine

```kotlin
import kotlinx.coroutines.*

suspend fun fetchData(): String {
    delay(1000) // Non-blocking delay
    return "Data fetched"
}

fun main() = runBlocking {
    val data = fetchData()
    println(data)
}
```

### Structured Concurrency

```kotlin
import kotlinx.coroutines.*

suspend fun processItems() = coroutineScope {
    val job1 = launch {
        delay(1000)
        println("Job 1 completed")
    }

    val job2 = launch {
        delay(500)
        println("Job 2 completed")
    }

    // Wait for all coroutines to complete
}
```

### Channel Pattern

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

suspend fun producer(channel: Channel<Int>) {
    for (i in 1..5) {
        delay(100)
        channel.send(i)
    }
    channel.close()
}

suspend fun consumer(channel: Channel<Int>) {
    for (value in channel) {
        println("Received: $value")
    }
}

fun main() = runBlocking {
    val channel = Channel<Int>()
    launch { producer(channel) }
    launch { consumer(channel) }
}
```

### Flow Pattern

```kotlin
import kotlinx.coroutines.flow.*

fun numbers(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking {
    numbers()
        .map { it * 2 }
        .filter { it > 3 }
        .collect { println(it) }
}
```

### Supervisor Pattern

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {
    val supervisor = SupervisorJob()

    launch(supervisor) {
        delay(200)
        throw RuntimeException("Job 1 failed")
    }

    launch(supervisor) {
        delay(500)
        println("Job 2 completed")
    }

    supervisor.join()
}
```

### Semaphore Pattern

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.*

class RateLimiter(private val permits: Int) {
    private val semaphore = Semaphore(permits)

    suspend fun <T> withRateLimit(block: suspend () -> T): T {
        semaphore.acquire()
        try {
            return block()
        } finally {
            semaphore.release()
        }
    }
}
```

## Best Practices

- Use structured concurrency for lifecycle management
- Prefer Flow over channels for reactive streams
- Use SupervisorJob for independent coroutine failure handling
- Consider using Mutex for thread-safe access
- Avoid globalScope for production code

## Interview Questions

1. What is the difference between launch and async?
2. How does structured concurrency work?
3. When should you use Flow vs channels?
4. How do you handle exceptions in coroutines?
5. What is the difference between delay and Thread.sleep?

## References

- Kotlin Coroutines documentation
- "Kotlin Coroutines" by Marcin Moskala
- "Kotlin in Action" by Svetlana Isakova
