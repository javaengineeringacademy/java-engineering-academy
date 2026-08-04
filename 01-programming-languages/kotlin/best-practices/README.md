# Kotlin Best Practices

This section covers idiomatic Kotlin, naming conventions, null safety patterns, coroutine patterns, and testing best practices.

## Table of Contents

- [Idiomatic Kotlin](#idiomatic-kotlin)
- [Naming Conventions](#naming-conventions)
- [Null Safety Patterns](#null-safety-patterns)
- [Coroutine Patterns](#coroutine-patterns)
- [Testing](#testing)
- [Code Organization](#code-organization)
- [Performance Tips](#performance-tips)
- [Common Pitfalls](#common-pitfalls)

## Idiomatic Kotlin

### Use Expression Body Functions

```kotlin
// Bad: Block body function
fun add(a: Int, b: Int): Int {
    return a + b
}

// Good: Expression body function
fun add(a: Int, b: Int): Int = a + b

// Even better: Type inference
fun add(a: Int, b: Int) = a + b
```

### Use Scope Functions

```kotlin
// Bad: Imperative style
class Person {
    var name: String = ""
    var age: Int = 0
}

fun createPerson(): Person {
    val person = Person()
    person.name = "Alice"
    person.age = 30
    return person
}

// Good: Using apply
fun createPerson() = Person().apply {
    name = "Alice"
    age = 30
}

// Using let for transformations
fun processString(input: String?): Int {
    return input?.let {
        it.length
    } ?: 0
}

// Using run for configuration
fun configureServer() = Server().run {
    host = "localhost"
    port = 8080
    start()
}
```

### Use Data Classes

```kotlin
// Bad: Regular class
class User(
    val id: Int,
    val name: String,
    val email: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id && name == other.name && email == other.email
    }
    
    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + email.hashCode()
        return result
    }
    
    override fun toString(): String {
        return "User(id=$id, name=$name, email=$email)"
    }
}

// Good: Data class
data class User(
    val id: Int,
    val name: String,
    val email: String
)
```

### Use Sealed Classes

```kotlin
// Bad: Using constants
sealed class NetworkStatus {
    companion object {
        const val LOADING = 0
        const val SUCCESS = 1
        const val ERROR = 2
    }
}

// Good: Sealed class with states
sealed class NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Exception) : NetworkResult<Nothing>()
}

// Usage
fun handleResult(result: NetworkResult<String>) {
    when (result) {
        is NetworkResult.Loading -> showLoading()
        is NetworkResult.Success -> showData(result.data)
        is NetworkResult.Error -> showError(result.exception)
    }
}
```

## Naming Conventions

### General Rules

```kotlin
// Classes: PascalCase
class UserManager
data class UserProfile
sealed class NetworkResult

// Functions: camelCase
fun getUser()
fun processData()
fun calculateTotal()

// Variables: camelCase
val userName = "Alice"
var counter = 0

// Constants: SCREAMING_SNAKE_CASE
const val MAX_CONNECTIONS = 100
const val API_BASE_URL = "https://api.example.com"

// Enums: PascalCase, members: SCREAMING_SNAKE_CASE
enum class Color {
    RED, GREEN, BLUE
}
```

### Specific Naming

```kotlin
// Boolean variables/functions: is, has, can
val isActive = true
fun hasPermission(): Boolean
canExecute(): Boolean

// Extensions: describe what they extend
fun String.isPalindrome(): Boolean
fun List<Int>.sumOfSquares(): Int

// Collectors: toType
fun <T> List<T>.toSet(): Set<T>
fun Map<String, Any>.toQueryString(): String

// Filters: filterBy, filterWhere
fun <T> List<T>.filterBy(predicate: (T) -> Boolean): List<T>

// Transformers: asType, toType
fun String.asInt(): Int?
fun Int.toHex(): String
```

## Null Safety Patterns

### Safe Calls and Elvis

```kotlin
// Safe call operator
val length = nullableString?.length

// Elvis operator with default
val length = nullableString?.length ?: 0

// Safe call chain
val result = user?.address?.city?.uppercase()

// Let function with null check
nullableString?.let { string ->
    println("String length: ${string.length}")
}
```

### Early Returns

```kotlin
// Bad: Nested if statements
fun processUser(user: User?) {
    if (user != null) {
        if (user.isActive) {
            if (user.hasPermission()) {
                // Process user
            } else {
                println("No permission")
            }
        } else {
            println("User not active")
        }
    } else {
        println("User is null")
    }
}

// Good: Early returns
fun processUser(user: User?) {
    val currentUser = user ?: return
    if (!currentUser.isActive) return
    if (!currentUser.hasPermission()) return
    
    // Process user
}
```

### Type Checks and Smart Casts

```kotlin
// Type check with when
fun processValue(value: Any) {
    when (value) {
        is String -> println(value.length)
        is Int -> println(value * 2)
        is List<*> -> println(value.size)
    }
}

// Safe cast
val maybeString: String? = anyValue as? String

// Non-null assertion (use sparingly)
val length = nullableString!!.length
```

## Coroutine Patterns

### Structured Concurrency

```kotlin
import kotlinx.coroutines.*

// Pattern: ViewModel with structured concurrency
class MyViewModel : CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    private var loadDataJob: Job? = null
    
    fun loadData() {
        loadDataJob?.cancel()
        loadDataJob = launch {
            try {
                val data = withContext(Dispatchers.IO) {
                    // Perform network call
                    delay(1000)
                    "Data"
                }
                // Update UI
                println("Loaded: $data")
            } catch (e: CancellationException) {
                println("Load cancelled")
            }
        }
    }
    
    fun onDestroy() {
        cancel()
    }
}
```

### Flow Patterns

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Pattern: Repository with Flow
class UserRepository {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    suspend fun refreshUsers() {
        val newUsers = withContext(Dispatchers.IO) {
            // Fetch from network
            delay(1000)
            listOf(User(1, "Alice"), User(2, "Bob"))
        }
        _users.value = newUsers
    }
}

// Pattern: Event bus
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
```

### Channel Patterns

```kotlin
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

// Pattern: Producer-consumer
fun producerConsumer() = runBlocking {
    val channel = Channel<Int>()
    
    val producer = launch {
        for (i in 1..10) {
            channel.send(i)
            delay(100)
        }
        channel.close()
    }
    
    val consumer = launch {
        for (value in channel) {
            println("Consumed: $value")
            delay(200)
        }
    }
    
    producer.join()
    consumer.join()
}

// Pattern: Pipeline
fun pipeline() = runBlocking {
    val numbers = Channel<Int>()
    val squares = Channel<Int>()
    val results = Channel<String>()
    
    launch {
        for (i in 1..10) {
            numbers.send(i)
        }
        numbers.close()
    }
    
    launch {
        for (value in numbers) {
            squares.send(value * value)
        }
        squares.close()
    }
    
    launch {
        for (value in squares) {
            results.send("Square: $value")
        }
        results.close()
    }
    
    for (result in results) {
        println(result)
    }
}
```

## Testing

### Unit Testing

```kotlin
import org.junit.Test
import org.junit.Assert.*

class CalculatorTest {
    private val calculator = Calculator()
    
    @Test
    fun `add two numbers`() {
        val result = calculator.add(2, 3)
        assertEquals(5, result)
    }
    
    @Test
    fun `subtract two numbers`() {
        val result = calculator.subtract(5, 3)
        assertEquals(2, result)
    }
    
    @Test
    fun `divide by zero throws exception`() {
        assertThrows(ArithmeticException::class.java) {
            calculator.divide(10, 0)
        }
    }
}
```

### Integration Testing

```kotlin
import org.junit.Test
import org.junit.Assert.*

class UserRepositoryTest {
    private val repository = UserRepository()
    
    @Test
    fun `get user by id`() = runBlocking {
        val user = repository.getUserById(1)
        assertNotNull(user)
        assertEquals("Alice", user?.name)
    }
    
    @Test
    fun `get all users`() = runBlocking {
        val users = repository.getAllUsers()
        assertFalse(users.isEmpty())
    }
    
    @Test
    fun `create user`() = runBlocking {
        val user = repository.createUser(User(0, "Bob", "bob@example.com"))
        assertNotNull(user)
        assertEquals("Bob", user.name)
    }
}
```

### Mocking

```kotlin
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

class UserServiceTest {
    private val repository = mock<UserRepository>()
    private val service = UserService(repository)
    
    @Test
    fun `get user calls repository`() = runBlocking {
        whenever(repository.getUserById(1)).thenReturn(User(1, "Alice", "alice@example.com"))
        
        val user = service.getUser(1)
        
        verify(repository).getUserById(1)
        assertEquals("Alice", user?.name)
    }
}
```

## Code Organization

### Package Structure

```kotlin
// Feature-based package structure
com.example.app/
├── features/
│   ├── auth/
│   │   ├── data/
│   │   ├── domain/
│   │   └── presentation/
│   ├── profile/
│   │   ├── data/
│   │   ├── domain/
│   │   └── presentation/
│   └── settings/
│       ├── data/
│       ├── domain/
│       └── presentation/
├── core/
│   ├── network/
│   ├── database/
│   └── utils/
└── App.kt
```

### Layer Separation

```kotlin
// Data layer
class UserRepository(private val api: UserApi, private val db: UserDatabase) {
    suspend fun getUsers(): List<User> {
        return try {
            api.getUsers()
        } catch (e: Exception) {
            db.getUsers()
        }
    }
}

// Domain layer
class GetUsersUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): List<User> {
        return repository.getUsers()
    }
}

// Presentation layer
class UserViewModel(private val getUsersUseCase: GetUsersUseCase) : 
    CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    
    fun loadUsers() {
        launch {
            _users.value = getUsersUseCase()
        }
    }
}
```

## Performance Tips

### Use Inline Functions

```kotlin
// Inline for small, frequently called functions
inline fun <T> measureTime(block: () -> T): T {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
    return result
}

// Crossinline for lambdas that can't use non-local returns
inline fun runInThread(crossinline action: () -> Unit) {
    Thread {
        action()
    }.start()
}

// Noinline for lambdas that need to be stored
inline fun inlinedFunction(noinline lambda: () -> Unit) {
    val storedLambda = lambda  // Can store because it's noinline
    storedLambda()
}
```

### Use Sequences for Large Data

```kotlin
// Bad: Eager evaluation
val list = (1..1000000).toList()
val result = list
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(5)

// Good: Lazy evaluation with sequences
val result = (1..1000000).asSequence()
    .map { it * it }
    .filter { it % 2 == 0 }
    .take(5)
    .toList()
```

### Use Appropriate Collection Types

```kotlin
// List: ordered, indexed access
val list = listOf(1, 2, 3)

// Set: unique elements
val set = setOf(1, 2, 3)

// Map: key-value pairs
val map = mapOf(1 to "one", 2 to "two")

// Use mutable versions when needed
val mutableList = mutableListOf(1, 2, 3)
mutableList.add(4)

// Use immutable versions by default
val immutableList = listOf(1, 2, 3)
```

## Common Pitfalls

### Avoid Blocking Operations

```kotlin
import kotlinx.coroutines.*

// Bad: Blocking call in coroutine
fun badExample() = runBlocking {
    val result = withContext(Dispatchers.IO) {
        Thread.sleep(1000)  // Blocking call
        "Result"
    }
}

// Good: Non-blocking delay
fun goodExample() = runBlocking {
    val result = withContext(Dispatchers.IO) {
        delay(1000)  // Non-blocking delay
        "Result"
    }
}
```

### Avoid Memory Leaks

```kotlin
import kotlinx.coroutines.*

// Bad: Memory leak
class BadViewModel {
    private val scope = CoroutineScope(Dispatchers.Main)
    
    fun loadData() {
        scope.launch {
            delay(1000)
            println("Data loaded")
        }
    }
    // Scope is never cancelled
}

// Good: Structured concurrency
class GoodViewModel : CoroutineScope by CoroutineScope(Dispatchers.Main + Job()) {
    fun loadData() {
        launch {
            delay(1000)
            println("Data loaded")
        }
    }
    
    fun onDestroy() {
        cancel()
    }
}
```

### Avoid Premature Optimization

```kotlin
// Bad: Premature optimization
fun processList(list: List<Int>): List<Int> {
    return list.asSequence()
        .filter { it % 2 == 0 }
        .map { it * it }
        .toList()
}

// Good: Simple first, optimize later
fun processList(list: List<Int>): List<Int> {
    return list
        .filter { it % 2 == 0 }
        .map { it * it }
}
// Profile first, then optimize if needed
```

## Summary

Kotlin best practices include:

- **Idiomatic Kotlin**: Use expression bodies, scope functions, data classes
- **Naming Conventions**: Follow consistent naming patterns
- **Null Safety**: Use safe calls, elvis operator, early returns
- **Coroutine Patterns**: Structured concurrency, flows, channels
- **Testing**: Unit tests, integration tests, mocking
- **Code Organization**: Feature-based packages, layer separation
- **Performance**: Inline functions, sequences, appropriate collections
- **Avoid Pitfalls**: No blocking operations, no memory leaks

Following these practices leads to clean, maintainable, and efficient Kotlin code.
