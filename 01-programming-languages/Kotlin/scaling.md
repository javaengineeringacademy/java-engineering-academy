# Kotlin Scaling

## Coroutines for Concurrency

Coroutines enable lightweight concurrency without thread overhead.

```kotlin
// Process many requests concurrently
suspend fun handleRequests(requests: List<Request>) = coroutineScope {
    requests.map { request ->
        async(Dispatchers.IO) {
            processRequest(request)
        }
    }.awaitAll()
}

// Limit concurrency with semaphore
val semaphore = Semaphore(10)
suspend fun limitedProcessing(item: Item) = semaphore.withPermit {
    process(item)
}
```

## Async Operations

Use structured concurrency for parallel operations.

```kotlin
// Parallel API calls
suspend fun fetchUserData(userId: Long): UserData = coroutineScope {
    val userDeferred = async { userApi.getUser(userId) }
    val postsDeferred = async { postApi.getPosts(userId) }
    val friendsDeferred = async { friendApi.getFriends(userId) }

    UserData(
        user = userDeferred.await(),
        posts = postsDeferred.await(),
        friends = friendsDeferred.await()
    )
}
```

## Reactive Streams

Use Flow for reactive data processing.

```kotlin
fun events(): Flow<Event> = flow {
    while (true) {
        val event = receiveEvent()
        emit(event)
        delay(100)
    }
}

// Process events reactively
events()
    .filter { it.type == "important" }
    .map { transform(it) }
    .catch { e -> logError(e) }
    .collect { process(it) }
```

## Connection Pooling

```kotlin
class ConnectionPool<T>(
    private val maxSize: Int = 10,
    private val factory: suspend () -> T
) {
    private val pool = Channel<T>(maxSize)

    init {
        repeat(maxSize) {
            runBlocking { pool.send(factory()) }
        }
    }

    suspend fun <R> withConnection(block: suspend (T) -> R): R {
        val conn = pool.receive()
        return try {
            block(conn)
        } finally {
            pool.send(conn)
        }
    }
}
```

## Caching

```kotlin
class Cache<K, V>(
    private val maxSize: Int = 1000,
    private val ttl: Duration = Duration.ofMinutes(5)
) {
    private val entries = ConcurrentHashMap<K, CacheEntry<V>>()

    suspend fun getOrPut(key: K, loader: suspend () -> V): V {
        entries[key]?.let { entry ->
            if (entry.isValid()) return entry.value
        }
        val value = loader()
        entries[key] = CacheEntry(value, Instant.now())
        return value
    }
}
```

## Load Balancing

```kotlin
class LoadBalancer<T>(private val backends: List<T>) {
    private val counter = AtomicInteger(0)

    fun next(): T {
        val index = counter.getAndIncrement() % backends.size
        return backends[index]
    }
}
```

## Rate Limiting

```kotlin
class RateLimiter(
    private val maxRequests: Int,
    private val window: Duration
) {
    private val requests = mutableListOf<Instant>()

    suspend fun acquire() {
        val now = Instant.now()
        requests.removeAll { it.isBefore(now.minus(window)) }
        if (requests.size >= maxRequests) {
            val waitTime = requests.first().plus(window).minus(now)
            delay(waitTime.toMillis())
        }
        requests.add(now)
    }
}
```
