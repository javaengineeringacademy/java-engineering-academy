# Kotlin Fundamentals

This section covers the core concepts and building blocks of the Kotlin programming language.

## Table of Contents

- [Variables](#variables)
- [Functions](#functions)
- [Null Safety](#null-safety)
- [Classes](#classes)
- [Data Classes](#data-classes)
- [Sealed Classes](#sealed-classes)
- [Interfaces](#interfaces)
- [Collections](#collections)
- [Control Flow](#control-flow)
- [Extension Functions](#extension-functions)
- [Type Checking and Casting](#type-checking-and-casting)
- [Exception Handling](#exception-handling)

## Variables

### Val vs Var

```kotlin
// Immutable variable (read-only)
val name: String = "Kotlin"
val age: Int = 25
val isKotlinAwesome: Boolean = true

// Mutable variable (can be reassigned)
var counter: Int = 0
counter = 1  // This is allowed

// Type inference
val inferredString = "Hello, Kotlin!"  // Type is inferred as String
var inferredInt = 42  // Type is inferred as Int
```

### Variable Initialization

```kotlin
// Late initialization
lateinit var lateString: String
lateinit var lateInt: Int

// Initialize later
fun initializeVariables() {
    lateString = "Initialized later"
    lateInt = 100
}

// Lazy initialization
val lazyValue: String by lazy {
    println("Computing lazy value...")
    "Computed Value"
}

// Usage
fun useLazyValue() {
    println(lazyValue)  // Will compute and print "Computed Value"
    println(lazyValue)  // Will use cached value
}
```

### Constants

```kotlin
// Compile-time constants
const val MAX_CONNECTIONS = 100
const val PI = 3.14159

// Runtime constants (can be initialized with expressions)
val RUNTIME_CONSTANT = System.currentTimeMillis()
```

## Functions

### Basic Functions

```kotlin
// Simple function
fun greet(name: String): String {
    return "Hello, $name!"
}

// Single-expression function
fun add(a: Int, b: Int): Int = a + b

// Function with default parameters
fun greetWithDefault(name: String = "World"): String {
    return "Hello, $name!"
}

// Named arguments
fun createUser(name: String, age: Int, email: String = "N/A") {
    println("Creating user: $name, Age: $age, Email: $email")
}

// Usage
createUser("Alice", 30)
createUser(name = "Bob", age = 25, email = "bob@example.com")
createUser(name = "Charlie", age = 35)
```

### Higher-Order Functions

```kotlin
// Function as parameter
fun performOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Function as return value
fun createMultiplier(factor: Int): (Int) -> Int {
    return { number -> number * factor }
}

// Usage
val sum = performOperation(5, 3) { a, b -> a + b }
val multiplier = createMultiplier(3)
val result = multiplier(5)  // 15

// Lambda functions
val multiply = { a: Int, b: Int -> a * b }
val square = { x: Int -> x * x }

// Function references
fun isEven(number: Int): Boolean = number % 2 == 0
val evenNumbers = listOf(1, 2, 3, 4, 5).filter(::isEven)
```

### Inline Functions

```kotlin
// Inline function
inline fun <T> measureTime(block: () -> T): T {
    val startTime = System.nanoTime()
    val result = block()
    val endTime = System.nanoTime()
    println("Execution time: ${endTime - startTime}ns")
    return result
}

// Usage
val result = measureTime {
    Thread.sleep(100)
    "Operation completed"
}

// Inline with noinline
inline fun inlinedFunction(noinline nonInlined: () -> Unit) {
    // This function is inlined
    nonInlined()  // This lambda is not inlined
}

// Crossinline
inline fun runInThread(crossinline action: () -> Unit) {
    Thread { action() }.start()
}
```

## Null Safety

### Nullable Types

```kotlin
// Non-nullable type
var nonNullable: String = "Hello"
// nonNullable = null  // Compilation error

// Nullable type
var nullable: String? = "Hello"
nullable = null  // This is allowed

// Null safety operators
val length1 = nullable?.length  // Safe call operator
val length2 = nullable?.length ?: 0  // Elvis operator
val length3 = nullable!!.length  // Not-null assertion (throws if null)

// Safe cast
val anyValue: Any = "Hello"
val safeString: String? = anyValue as? String  // Returns null if cast fails

// Let function with null safety
nullable?.let {
    println("String length: ${it.length}")
    println("String value: $it")
}
```

### Null Safety Patterns

```kotlin
// Filter out nulls
val nullableList = listOf(1, null, 3, null, 5)
val nonNullList = nullableList.filterNotNull()

// Default values
val value = nullable ?: "default"

// Check and act
if (nullable != null) {
    println(nullable.length)  // Smart cast to non-nullable
}

// Safe chaining
val result = nullable?.trim()?.uppercase()?.take(5)

// When with null
when (nullable) {
    null -> println("Value is null")
    else -> println("Value: $nullable")
}
```

## Classes

### Basic Classes

```kotlin
// Primary constructor
class Person(val name: String, var age: Int) {
    // Secondary constructor
    constructor(name: String) : this(name, 0)
    
    // Init block
    init {
        require(age >= 0) { "Age must be non-negative" }
    }
    
    // Member functions
    fun greet(): String {
        return "Hi, I'm $name and I'm $age years old."
    }
    
    // Companion object (static members)
    companion object {
        fun create(name: String): Person {
            return Person(name, 0)
        }
    }
}

// Usage
val person = Person("Alice", 30)
val person2 = Person.create("Bob")
println(person.greet())
```

### Inheritance

```kotlin
// Open class (can be inherited)
open class Animal(val name: String) {
    open fun sound(): String {
        return "Some sound"
    }
    
    fun describe(): String {
        return "$name makes ${sound()}"
    }
}

// Derived class
class Dog(name: String) : Animal(name) {
    override fun sound(): String {
        return "Woof!"
    }
}

class Cat(name: String) : Animal(name) {
    override fun sound(): String {
        return "Meow!"
    }
}

// Usage
val dog = Dog("Rex")
val cat = Cat("Whiskers")
println(dog.describe())  // Rex makes Woof!
println(cat.describe())  // Whiskers makes Meow!
```

### Abstract Classes

```kotlin
// Abstract class
abstract class Shape {
    abstract fun area(): Double
    abstract fun perimeter(): Double
    
    fun describe(): String {
        return "Area: ${area()}, Perimeter: ${perimeter()}"
    }
}

// Concrete implementation
class Circle(private val radius: Double) : Shape() {
    override fun area(): Double = Math.PI * radius * radius
    override fun perimeter(): Double = 2 * Math.PI * radius
}

class Rectangle(
    private val width: Double,
    private val height: Double
) : Shape() {
    override fun area(): Double = width * height
    override fun perimeter(): Double = 2 * (width + height)
}

// Usage
val circle = Circle(5.0)
val rectangle = Rectangle(4.0, 6.0)
println(circle.describe())
println(rectangle.describe())
```

## Data Classes

### Basic Data Classes

```kotlin
// Data class
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val isActive: Boolean = true
)

// Usage
val user1 = User(1, "Alice", "alice@example.com")
val user2 = User(1, "Alice", "alice@example.com")
val user3 = user1.copy(id = 2, name = "Bob")

// Automatic implementations
println(user1)  // User(id=1, name=Alice, email=alice@example.com, isActive=true)
println(user1 == user2)  // true (structural equality)
println(user1.hashCode())  // Consistent with equals

// Destructuring
val (id, name, email, isActive) = user1
println("ID: $id, Name: $name, Email: $email, Active: $isActive")

// Component functions
println(user1.component1())  // 1
println(user1.component2())  // Alice
```

### Data Class Best Practices

```kotlin
// Immutable data class (all val properties)
data class ImmutableUser(
    val id: Int,
    val name: String,
    val email: String
)

// Data class with validation
data class ValidatedEmail private constructor(val value: String) {
    companion object {
        fun create(email: String): ValidatedEmail? {
            return if (email.contains("@")) {
                ValidatedEmail(email)
            } else {
                null
            }
        }
    }
}

// Data class with computed properties
data class Rectangle(val width: Double, val height: Double) {
    val area: Double get() = width * height
    val perimeter: Double get() = 2 * (width + height)
    val isSquare: Boolean get() = width == height
}
```

## Sealed Classes

### Basic Sealed Classes

```kotlin
// Sealed class for representing states
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Usage
fun handleResult(result: Result<String>) {
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.exception.message}")
        is Result.Loading -> println("Loading...")
    }
}

// Sealed interface (Kotlin 1.5+)
sealed interface Shape {
    data class Circle(val radius: Double) : Shape
    data class Rectangle(val width: Double, val height: Double) : Shape
    data class Triangle(
        val a: Double,
        val b: Double,
        val c: Double
    ) : Shape
}

// Exhaustive when expression
fun calculateArea(shape: Shape): Double {
    return when (shape) {
        is Shape.Circle -> Math.PI * shape.radius * shape.radius
        is Shape.Rectangle -> shape.width * shape.height
        is Shape.Triangle -> {
            val s = (shape.a + shape.b + shape.c) / 2
            Math.sqrt(s * (s - shape.a) * (s - shape.b) * (s - shape.c))
        }
    }
}
```

### Sealed Classes vs Enums

```kotlin
// Sealed class - can have multiple instances per type
sealed class NetworkResult {
    data class Success(val data: String) : NetworkResult()
    data class Error(val code: Int, val message: String) : NetworkResult()
}

// Enum - single instance per type
enum class NetworkStatus {
    SUCCESS, ERROR, LOADING
}

// Sealed class with type parameters
sealed class Either<out A, out B> {
    data class Left<out A>(val value: A) : Either<A, Nothing>()
    data class Right<out B>(val value: B) : Either<Nothing, B>()
}

// Usage
fun divide(a: Int, b: Int): Either<String, Int> {
    return if (b == 0) {
        Either.Left("Division by zero")
    } else {
        Either.Right(a / b)
    }
}
```

## Interfaces

### Basic Interfaces

```kotlin
// Interface definition
interface Drawable {
    fun draw()
    
    // Default implementation
    fun fill(color: String) {
        println("Filling with $color")
    }
}

// Interface with properties
interface Repository<T> {
    val size: Int
    
    fun get(id: Int): T?
    fun getAll(): List<T>
    fun save(item: T)
    fun delete(id: Int): Boolean
    
    // Default implementation using property
    fun isEmpty(): Boolean = size == 0
}

// Implementation
class UserRepository : Repository<User> {
    private val users = mutableListOf<User>()
    
    override val size: Int get() = users.size
    
    override fun get(id: Int): User? {
        return users.find { it.id == id }
    }
    
    override fun getAll(): List<User> = users.toList()
    
    override fun save(item: User) {
        users.add(item)
    }
    
    override fun delete(id: Int): Boolean {
        return users.removeAll { it.id == id }
    }
}
```

### Multiple Interface Implementation

```kotlin
// Multiple interfaces
interface Readable {
    fun read(): String
}

interface Writable {
    fun write(data: String)
}

// Class implementing multiple interfaces
class FileHandler(private val filename: String) : Readable, Writable {
    override fun read(): String {
        return "Reading from $filename"
    }
    
    override fun write(data: String) {
        println("Writing to $filename: $data")
    }
}

// Interface delegation
class CountingList<T> : MutableList<T> by ArrayList<T>() {
    private var addCount = 0
    
    override fun add(element: T): Boolean {
        addCount++
        return super.add(element)
    }
    
    fun getAddCount(): Int = addCount
}
```

## Collections

### List

```kotlin
// Immutable list
val numbers = listOf(1, 2, 3, 4, 5)
val strings = listOf("a", "b", "c")

// Mutable list
val mutableNumbers = mutableListOf(1, 2, 3)
mutableNumbers.add(4)
mutableNumbers.removeAt(0)

// List operations
val doubled = numbers.map { it * 2 }
val evens = numbers.filter { it % 2 == 0 }
val sum = numbers.reduce { acc, i -> acc + i }
val first = numbers.first()
val last = numbers.last()
val sorted = numbers.sorted()
val reversed = numbers.reversed()
```

### Set

```kotlin
// Immutable set
val uniqueNumbers = setOf(1, 2, 3, 4, 5)

// Mutable set
val mutableSet = mutableSetOf(1, 2, 3)
mutableSet.add(4)
mutableSet.remove(1)

// Set operations
val set1 = setOf(1, 2, 3, 4)
val set2 = setOf(3, 4, 5, 6)

val union = set1.union(set2)  // [1, 2, 3, 4, 5, 6]
val intersection = set1.intersect(set2)  // [3, 4]
val difference = set1.subtract(set2)  // [1, 2]
```

### Map

```kotlin
// Immutable map
val ages = mapOf("Alice" to 30, "Bob" to 25, "Charlie" to 35)

// Mutable map
val mutableAges = mutableMapOf("Alice" to 30)
mutableAges["Bob"] = 25
mutableAges.remove("Alice")

// Map operations
val names = ages.keys
val values = ages.values
val filtered = ages.filter { it.value > 28 }
val mapped = ages.map { "${it.key}: ${it.value}" }

// Safe access
val aliceAge = ages["Alice"]  // 30
val unknownAge = ages["Unknown"]  // null
val defaultAge = ages.getOrDefault("Unknown", 0)  // 0
```

## Control Flow

### If Expression

```kotlin
// If as expression
val max = if (a > b) a else b

// If with blocks
val result = if (score >= 90) {
    "A"
} else if (score >= 80) {
    "B"
} else if (score >= 70) {
    "C"
} else {
    "F"
}

// If with complex conditions
val message = if (age >= 18 && hasID) {
    "Welcome!"
} else if (age < 18) {
    "Sorry, you're too young."
} else {
    "Please show your ID."
}
```

### When Expression

```kotlin
// Basic when
when (day) {
    "Monday" -> println("Start of work week")
    "Friday" -> println("Almost weekend!")
    "Saturday", "Sunday" -> println("Weekend!")
    else -> println("Regular day")
}

// When with conditions
when {
    score >= 90 -> println("A")
    score >= 80 -> println("B")
    score >= 70 -> println("C")
    else -> println("F")
}

// When with type checking
fun describe(x: Any): String = when (x) {
    is Int -> "Integer: $x"
    is String -> "String of length ${x.length}"
    is List<*> -> "List of size ${x.size}"
    else -> "Unknown type"
}

// When with ranges
when (age) {
    in 0..12 -> println("Child")
    in 13..17 -> println("Teenager")
    in 18..64 -> println("Adult")
    else -> println("Senior")
}
```

### For Loops

```kotlin
// Basic for loop
for (i in 1..10) {
    println(i)
}

// DownTo
for (i in 10 downTo 1) {
    println(i)
}

// Step
for (i in 0..20 step 2) {
    println(i)
}

// Iterating over collections
val fruits = listOf("Apple", "Banana", "Cherry")
for (fruit in fruits) {
    println(fruit)
}

// With index
for ((index, fruit) in fruits.withIndex()) {
    println("$index: $fruit")
}

// Iterating over ranges
for (i in 0 until fruits.size) {
    println(fruits[i])
}
```

### While and Do-While Loops

```kotlin
// While loop
var count = 0
while (count < 5) {
    println(count)
    count++
}

// Do-while loop
do {
    println(count)
    count--
} while (count > 0)

// Infinite loop with break
var number = 0
while (true) {
    if (number > 10) break
    println(number)
    number++
}
```

## Extension Functions

### Basic Extension Functions

```kotlin
// Extension function on String
fun String.removeSpaces(): String {
    return this.replace(" ", "")
}

// Extension function with parameters
fun String.truncate(maxLength: Int): String {
    return if (this.length > maxLength) {
        this.take(maxLength) + "..."
    } else {
        this
    }
}

// Usage
val text = "Hello World"
println(text.removeSpaces())  // HelloWorld
println(text.truncate(5))  // Hello...

// Extension property
val String.wordCount: Int
    get() = this.split(" ").size

println("Hello World".wordCount)  // 2
```

### Extension Functions on Collections

```kotlin
// Extension function on List
fun <T> List<T>.secondOrNull(): T? {
    return if (this.size >= 2) this[1] else null
}

// Extension function on Mutable List
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

// Usage
val numbers = listOf(1, 2, 3)
println(numbers.secondOrNull())  // 2

val mutableNumbers = mutableListOf(1, 2, 3)
mutableNumbers.swap(0, 2)
println(mutableNumbers)  // [3, 2, 1]
```

## Type Checking and Casting

### Smart Casting

```kotlin
// Type check with is
fun processValue(value: Any) {
    when (value) {
        is String -> println("String: ${value.uppercase()}")  // Smart cast
        is Int -> println("Integer: ${value * 2}")  // Smart cast
        is List<*> -> println("List size: ${value.size}")  // Smart cast
    }
}

// Unsafe cast
val anyValue: Any = "Hello"
val string: String = anyValue as String  // Throws ClassCastException if wrong

// Safe cast
val maybeString: String? = anyValue as? String  // Returns null if wrong
```

## Exception Handling

### Try-Catch-Finally

```kotlin
// Basic try-catch
try {
    val result = 10 / 0
} catch (e: ArithmeticException) {
    println("Arithmetic error: ${e.message}")
} finally {
    println("This always executes")
}

// Try as expression
val result = try {
    parseInt(input)
} catch (e: NumberFormatException) {
    0
}

// Multiple catch blocks
try {
    // Code that might throw
} catch (e: IOException) {
    println("IO error")
} catch (e: NumberFormatException) {
    println("Number format error")
} catch (e: Exception) {
    println("General error")
}
```

### Custom Exceptions

```kotlin
// Custom exception
class InsufficientFundsException(
    private val amount: Double,
    private val balance: Double
) : Exception() {
    override val message: String
        get() = "Insufficient funds: requested $amount, available $balance"
}

// Using custom exception
fun withdraw(amount: Double, balance: Double): Double {
    if (amount > balance) {
        throw InsufficientFundsException(amount, balance)
    }
    return balance - amount
}

// Usage
try {
    val newBalance = withdraw(100.0, 50.0)
} catch (e: InsufficientFundsException) {
    println(e.message)
}
```

## Summary

Kotlin fundamentals provide a solid foundation for building modern applications:

- **Type Safety**: Strong static typing with type inference
- **Null Safety**: Built-in null safety to prevent NPEs
- **Concise Syntax**: Less boilerplate, more readable code
- **Functional Programming**: First-class functions, lambdas, and higher-order functions
- **OOP Features**: Classes, interfaces, sealed classes, and data classes
- **Extension Functions**: Extend existing classes without inheritance
- **Smart Casting**: Automatic type casting after type checks
- **Expression-Oriented**: Most statements are expressions

Mastering these fundamentals is essential before moving to advanced Kotlin concepts.
