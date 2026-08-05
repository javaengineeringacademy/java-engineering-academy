# Kotlin Core Concepts

## Null Safety

Kotlin eliminates null pointer exceptions at compile time.

```kotlin
var name: String = "Alice"    // Non-nullable
var nullable: String? = null  // Nullable

// Safe calls
println(nullable?.length)     // null if nullable is null

// Elvis operator
val length = nullable?.length ?: 0

// Not-null assertion (use sparingly)
val notNull: String = nullable!!

// Safe casting
val number: Int? = value as? Int
```

The compiler enforces null checks, preventing most NPEs at runtime.

## Coroutines

Coroutines enable asynchronous programming with sequential code.

```kotlin
suspend fun fetchData(): String {
    delay(1000) // Non-blocking delay
    return "data"
}

// Launch coroutine
lifecycleScope.launch {
    val data = fetchData()
    updateUI(data)
}

// Structured concurrency
suspend fun processData() = coroutineScope {
    val deferred1 = async { fetchUser() }
    val deferred2 = async { fetchPosts() }
    UserWithPosts(deferred1.await(), deferred2.await())
}
```

## Extension Functions

Add functions to existing classes without inheritance.

```kotlin
fun String.isEmail(): Boolean {
    return matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
}

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

"test@email.com".isEmail()  // true
```

## Data Classes

Data classes automatically generate useful methods.

```kotlin
data class User(val name: String, val age: Int)

// Auto-generated: equals, hashCode, toString, copy
val user1 = User("Alice", 30)
val user2 = user1.copy(name = "Bob")

// Destructuring
val (name, age) = user1
```

## Sealed Classes

Sealed classes restrict class hierarchies for exhaustive when expressions.

```kotlin
sealed class Result {
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
    object Loading : Result()
}

fun handle(result: Result) = when (result) {
    is Result.Success -> println(result.data)
    is Result.Error -> println(result.message)
    Result.Loading -> println("Loading")
    // No else needed - compiler knows all cases
}
```

## Scope Functions

Kotlin provides five scope functions for concise code.

```kotlin
// let - transform or apply null checks
val result = nullable?.let { it.uppercase() }

// run - execute block on object with context
val length = "hello".run { length }

// with - group operations on object
with(user) {
    println(name)
    println(age)
}

// apply - configure object
val person = Person().apply {
    name = "Alice"
    age = 30
}

// also - side effects
val list = mutableListOf(1, 2, 3).also { println(it) }
```

## Collections

Kotlin collections support both mutable and immutable variants.

```kotlin
val immutable = listOf(1, 2, 3)
val mutable = mutableListOf(1, 2, 3)
val immutableMap = mapOf("a" to 1, "b" to 2)
val mutableMap = mutableMapOf("a" to 1)

// Operations
val filtered = immutable.filter { it > 1 }
val mapped = immutable.map { it * 2 }
val sum = immutable.sum()
```
