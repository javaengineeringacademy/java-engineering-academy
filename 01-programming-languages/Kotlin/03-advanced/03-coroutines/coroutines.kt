// Kotlin Coroutines - launch, async, withContext

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    // Launch coroutine
    println("=== Launch ===")
    val job = launch {
        delay(1000)
        println("Coroutine completed")
    }
    println("Main continues...")
    job.join() // Wait for completion
    
    // Async coroutine
    println("\n=== Async ===")
    val deferred1 = async {
        delay(1000)
        println("Task 1 completed")
        42
    }
    
    val deferred2 = async {
        delay(1500)
        println("Task 2 completed")
        "Result"
    }
    
    val result1 = deferred1.await()
    val result2 = deferred2.await()
    println("Results: $result1, $result2")
    
    // withContext - switch dispatcher
    println("\n=== withContext ===")
    val result = withContext(Dispatchers.IO) {
        // Perform I/O operation
        println("Running on: ${Thread.currentThread().name}")
        "Data from IO"
    }
    println("Result: $result")
    
    // Structured concurrency
    println("\n=== Structured Concurrency ===")
    val time = measureTimeMillis {
        coroutineScope {
            launch { delay(1000); println("Task 1") }
            launch { delay(1000); println("Task 2") }
            launch { delay(1000); println("Task 3") }
        }
    }
    println("Completed in $time ms")
    
    // Exception handling
    println("\n=== Exception Handling ===")
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught: ${exception.message}")
    }
    
    launch(handler) {
        throw RuntimeException("Something went wrong")
    }
    delay(100) // Wait for handler
    
    // Cancellation
    println("\n=== Cancellation ===")
    val cancellableJob = launch {
        repeat(5) { i ->
            println("Working on $i...")
            delay(500)
        }
    }
    
    delay(1200)
    println("Cancelling job...")
    cancellableJob.cancelAndJoin()
    println("Job cancelled")
    
    // Timeout
    println("\n=== Timeout ===")
    try {
        withTimeout(1000) {
            delay(2000)
            println("This won't print")
        }
    } catch (e: TimeoutCancellationException) {
        println("Timeout: ${e.message}")
    }
    
    // Channels (basic)
    println("\n=== Channels ===")
    val channel = Channel<String>()
    
    launch {
        channel.send("Hello")
        channel.send("World")
        channel.close()
    }
    
    for (message in channel) {
        println("Received: $message")
    }
    
    println("Coroutines example running")
}