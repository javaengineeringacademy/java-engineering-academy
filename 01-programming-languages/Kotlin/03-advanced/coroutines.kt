import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    // Basic coroutine
    launch {
        delay(1000)
        println("Basic coroutine completed")
    }
    println("Main continues")
    delay(2000)

    // Coroutine with result
    val deferred = async {
        delay(500)
        "Hello from coroutine"
    }
    val result = deferred.await()
    println("Result: $result")

    // Multiple coroutines
    val time = measureTimeMillis {
        val d1 = async { fetchData(1) }
        val d2 = async { fetchData(2) }
        val d3 = async { fetchData(3) }

        val results = listOf(d1, d2, d3).awaitAll()
        println("All results: $results")
    }
    println("Time: ${time}ms")

    // CoroutineScope
    val scope = CoroutineScope(Dispatchers.Default)
    val job = scope.launch {
        repeat(5) { i ->
            println("Iteration $i")
            delay(300)
        }
    }

    delay(1000)
    job.cancel()
    println("Coroutine cancelled")

    // Exception handling
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Handled: ${exception.message}")
    }

    launch(handler) {
        throw RuntimeException("Test exception")
    }
    delay(500)

    // Structured concurrency
    val parentJob = launch {
        val child1 = launch {
            delay(500)
            println("Child 1 done")
        }
        val child2 = launch {
            delay(1000)
            println("Child 2 done")
        }
    }

    delay(2000)

    // Channel basics
    val channel = Channel<Int>()

    launch {
        for (i in 1..5) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }

    for (value in channel) {
        println("Received: $value")
    }

    // Mutex for thread safety
    val mutex = Mutex()
    var counter = 0

    val jobs = List(10) {
        launch {
            mutex.withLock {
                counter++
                delay(10)
            }
        }
    }

    jobs.joinAll()
    println("Counter: $counter")

    // Flow basics
    val flow = flow {
        for (i in 1..5) {
            delay(200)
            emit(i)
        }
    }

    flow.collect { value ->
        println("Flow value: $value")
    }

    // StateFlow
    val stateFlow = MutableStateFlow(0)

    launch {
        stateFlow.collect { value ->
            println("State: $value")
        }
    }

    repeat(3) {
        delay(300)
        stateFlow.value++
    }

    delay(1500)
}

suspend fun fetchData(id: Int): String {
    delay(500)
    return "Data $id"
}
