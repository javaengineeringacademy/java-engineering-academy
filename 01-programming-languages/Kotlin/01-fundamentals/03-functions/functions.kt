// Kotlin Functions - Default args, named args, single expression

// Basic function
fun greet(name: String): String {
    return "Hello, $name!"
}

// Single expression function
fun add(a: Int, b: Int): Int = a + b

// Default parameters
fun greetWithDefault(name: String, greeting: String = "Hello"): String {
    return "$greeting, $name!"
}

// Named arguments
fun createUser(name: String, age: Int, email: String) {
    println("Creating user: $name, $age, $email")
}

// Variable number of arguments (vararg)
fun sum(vararg numbers: Int): Int {
    return numbers.sum()
}

// Function with Unit return type (void)
fun printMessage(message: String): Unit {
    println(message)
}

// Short form of Unit
fun printShort(message: String) {
    println(message)
}

// Higher-order function
fun performOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
    return operation(a, b)
}

// Lambda functions
val multiply = { a: Int, b: Int -> a * b }
val square: (Int) -> Int = { it * it }

// Extension function
fun String.isPalindrome(): Boolean {
    return this == this.reversed()
}

// Tail recursive function
tailrec fun factorial(n: Int, accumulator: Int = 1): Int {
    return if (n <= 1) accumulator else factorial(n - 1, n * accumulator)
}

// Generic function
fun <T> firstOrDefault(list: List<T>, default: T): T {
    return list.firstOrNull() ?: default
}

// Infix function
infix fun Int.power(exponent: Int): Int {
    var result = 1
    for (i in 1..exponent) {
        result *= this
    }
    return result
}

fun main() {
    // Basic function
    println(greet("Alice"))
    
    // Single expression
    println("5 + 3 = ${add(5, 3)}")
    
    // Default parameters
    println(greetWithDefault("Bob"))
    println(greetWithDefault("Charlie", "Hi"))
    
    // Named arguments
    createUser(name = "Alice", age = 30, email = "alice@example.com")
    
    // Vararg
    println("Sum: ${sum(1, 2, 3, 4, 5)}")
    val numbers = intArrayOf(1, 2, 3)
    println("Sum with spread: ${sum(*numbers)}")
    
    // Higher-order function
    val result = performOperation(5, 3) { a, b -> a + b }
    println("Operation result: $result")
    
    // Lambda
    println("Multiply: ${multiply(5, 3)}")
    println("Square: ${square(4)}")
    
    // Extension function
    val palindrome = "racecar"
    println("'$palindrome' is palindrome: ${palindrome.isPalindrome()}")
    
    // Tail recursive
    println("Factorial of 10: ${factorial(10)}")
    
    // Generic function
    val strings = listOf("apple", "banana")
    val default = firstOrDefault(strings, "cherry")
    println("First or default: $default")
    
    // Infix function
    println("2 power 10 = ${2 power 10}")
    
    // Lambda with receiver
    val sb = StringBuilder().apply {
        append("Hello")
        append(" ")
        append("World")
    }
    println("Builder: ${sb.toString()}")
    
    println("Functions example running")
}