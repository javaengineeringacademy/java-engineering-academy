// Kotlin Lambdas - Lambda syntax, it keyword

fun main() {
    // Basic lambda
    val add = { a: Int, b: Int -> a + b }
    println("Add: ${add(5, 3)}")
    
    // Lambda with explicit type
    val multiply: (Int, Int) -> Int = { a, b -> a * b }
    println("Multiply: ${multiply(5, 3)}")
    
    // Lambda with single parameter (it)
    val square: (Int) -> Int = { it * it }
    println("Square: ${square(4)}")
    
    // Lambda with no parameters
    val greet = { println("Hello!") }
    greet()
    
    // Higher-order function
    fun performOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        return operation(a, b)
    }
    
    println("Add: ${performOperation(5, 3) { a, b -> a + b }}")
    println("Multiply: ${performOperation(5, 3) { a, b -> a * b }}")
    
    // Lambda as last parameter (trailing lambda)
    val numbers = listOf(1, 2, 3, 4, 5)
    
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")
    
    val evens = numbers.filter { it % 2 == 0 }
    println("Evens: $evens}")
    
    val sum = numbers.reduce { acc, it -> acc + it }
    println("Sum: $sum}")
    
    // forEach with lambda
    numbers.forEach { print("$it ") }
    println()
    
    // Multiple lambdas
    fun processNumbers(
        numbers: List<Int>,
        onEach: (Int) -> Unit,
        onDone: () -> Unit
    ) {
        numbers.forEach(onEach)
        onDone()
    }
    
    processNumbers(
        listOf(1, 2, 3),
        onEach = { println("Processing: $it") },
        onDone = { println("Done!") }
    )
    
    // Lambda with receiver
    fun buildString(action: StringBuilder.() -> Unit): String {
        return StringBuilder().apply(action).toString()
    }
    
    val str = buildString {
        append("Hello")
        append(" ")
        append("World")
    }
    println("Built: $str")
    
    // Closure
    var counter = 0
    val increment = { counter++ }
    increment()
    increment()
    println("Counter: $counter")
    
    // Non-local return
    fun findFirstPositive(numbers: List<Int>): Int? {
        return numbers.firstOrNull {
            if (it > 0) return it // Non-local return
            false
        }
    }
    
    val result = findFirstPositive(listOf(-1, -2, 3, 4))
    println("First positive: $result")
    
    // Inline functions with lambdas
    inline fun <T> measureTime(block: () -> T): T {
        val start = System.currentTimeMillis()
        val result = block()
        val end = System.currentTimeMillis()
        println("Time: ${end - start}ms")
        return result
    }
    
    val measured = measureTime {
        Thread.sleep(100)
        "Result"
    }
    println("Measured: $measured")
    
    // Lambda in collection operations
    val people = listOf("Alice", "Bob", "Charlie", "David")
    
    val result2 = people
        .filter { it.length > 3 }
        .map { it.uppercase() }
        .sorted()
    
    println("Processed: $result2")
    
    println("Lambdas example running")
}