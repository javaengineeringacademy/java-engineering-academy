# Kotlin Pitfalls

## Force Unwrap (!!)

Using `!!` operator defeats null safety and causes NPEs.

```kotlin
// Bad - can throw NPE
val user = getUser()
val name = user!!.name!!

// Good - use safe calls
val name = getUser()?.name ?: "Unknown"
```

## Blocking Main Thread

Running blocking operations on Dispatchers.Main causes ANR.

```kotlin
// Bad - blocks main thread
GlobalScope.launch(Dispatchers.Main) {
    val data = blockingNetworkCall()
    updateUI(data)
}

// Good - use IO dispatcher
lifecycleScope.launch {
    val data = withContext(Dispatchers.IO) {
        blockingNetworkCall()
    }
    updateUI(data)
}
```

## Coroutine Scope Leaks

Not managing coroutine scope leads to leaked coroutines.

```kotlin
// Bad - runs forever
GlobalScope.launch {
    while (isActive) {
        // Never stops
    }
}

// Good - lifecycle-aware
lifecycleScope.launch {
    while (isActive) {
        // Stops when scope is cancelled
    }
}
```

## Ignoring Coroutine Cancellation

Not checking for cancellation wastes resources.

```kotlin
// Bad - does not check cancellation
suspend fun longTask() {
    while (true) {
        process()
    }
}

// Good - checks isActive
suspend fun longTask() = coroutineScope {
    while (isActive) {
        process()
    }
}
```

## Using var Unnecessarily

Using `var` when `val` would work reduces predictability.

```kotlin
// Bad - mutable when unnecessary
var counter = 0
var name = "Alice"

// Good - immutable
val counter = 0
val name = "Alice"
```

## Scope Function Abuse

Overusing let, run, with, apply, also reduces readability.

```kotlin
// Bad - nested scope functions
person.apply {
    name = "John".also {
        println(it)
    }.run {
        uppercase()
    }
}

// Good - simple and readable
val processedName = person.name?.uppercase() ?: "UNKNOWN"
```

## Not Using Data Classes

Creating regular classes for data holders misses auto-generated methods.

```kotlin
// Bad - manual implementation
class User(val name: String, val age: Int) {
    override fun equals(other: Any?): Boolean { /* ... */ }
    override fun hashCode(): Int { /* ... */ }
}

// Good - data class
data class User(val name: String, val age: Int)
```

## String Templates with Complex Expressions

Complex expressions in string templates reduce readability.

```kotlin
// Bad - hard to read
println("User: ${user.name.uppercase().take(10)}")

// Good - assign to variable first
val displayName = user.name.uppercase().take(10)
println("User: $displayName")
```
