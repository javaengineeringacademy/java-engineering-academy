fun main() {
    // Basic function
    println(add(5, 3))

    // Named arguments
    println(greet(name = "Kotlin", greeting = "Hello"))

    // Default parameters
    println(greet(name = "World"))

    // Single-expression function
    println(multiply(4, 5))

    // Function with return
    val result = calculate(10, 5)
    println("Result: $result")

    // Higher-order functions
    val numbers = listOf(1, 2, 3, 4, 5)
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")

    // Function types
    val operation: (Int, Int) -> Int = { a, b -> a + b }
    println("Operation: ${operation(10, 20)}")

    // Variadic functions
    println("Sum: ${sum(1, 2, 3, 4, 5)}")

    // Recursive functions
    println("Factorial of 5: ${factorial(5)}")

    // Infix functions
    val pair = "Hello" to "World"
    println("Pair: $pair")

    // Extension functions
    println("Kotlin".addExclamation())

    // Tail recursive functions
    println("Fibonacci(10): ${fibonacci(10)}")
}

// Basic function
fun add(a: Int, b: Int): Int {
    return a + b
}

// Function with named and default parameters
fun greet(name: String, greeting: String = "Hi"): String {
    return "$greeting, $name!"
}

// Single-expression function
fun multiply(a: Int, b: Int) = a * b

// Function with complex return
fun calculate(a: Int, b: Int): Map<String, Int> {
    return mapOf(
        "sum" to a + b,
        "diff" to a - b,
        "product" to a * b,
        "quotient" to a / b
    )
}

// Variadic function
fun sum(vararg numbers: Int): Int {
    return numbers.sum()
}

// Recursive function
fun factorial(n: Int): Long {
    return if (n <= 1) 1 else n * factorial(n - 1)
}

// Infix function
infix fun String.and(other: String): String {
    return "$this and $other"
}

// Extension function
fun String.addExclamation(): String {
    return "$this!"
}

// Tail recursive function
tailrec fun fibonacci(n: Int, a: Long = 0, b: Long = 1): Long {
    return if (n == 0) a else fibonacci(n - 1, b, a + b)
}
