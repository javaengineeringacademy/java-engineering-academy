# Kotlin Coroutines

## Overview
Coroutines provide lightweight concurrency.

## Launch
```kotlin
val job = launch {
    delay(1000)
    println("Completed")
}
job.join()
```

## Async
```kotlin
val deferred = async {
    delay(1000)
    42
}
val result = deferred.await()
```

## withContext
```kotlin
val result = withContext(Dispatchers.IO) {
    // I/O operation
    "Data"
}
```

## Dispatchers
```kotlin
Dispatchers.Default   // CPU-intensive
Dispatchers.IO        // I/O operations
Dispatchers.Main      // UI thread
```

## Exception Handling
```kotlin
val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught: ${exception.message}")
}

launch(handler) {
    throw RuntimeException("Error")
}
```

## Cancellation
```kotlin
val job = launch {
    repeat(100) { i ->
        delay(500)
        println(i)
    }
}

delay(1000)
job.cancelAndJoin()
```

## Timeout
```kotlin
try {
    withTimeout(1000) {
        delay(2000)
    }
} catch (e: TimeoutCancellationException) {
    println("Timeout")
}
```

## Structured Concurrency
```kotlin
coroutineScope {
    launch { /* task 1 */ }
    launch { /* task 2 */ }
}
```

## Key Takeaways
1. Use launch for fire-and-forget
2. Use async for return values
3. Use withContext for dispatcher changes
4. Always handle exceptions