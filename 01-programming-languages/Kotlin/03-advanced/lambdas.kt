fun main() {
    // Lambda expression
    val sum = { a: Int, b: Int -> a + b }
    println("Sum: ${sum(5, 3)}")

    // Lambda with receiver
    val greet: String.() -> String = { "Hello, $this!" }
    println("Kotlin".greet())

    // Lambda assigned to variable
    val numbers = listOf(1, 2, 3, 4, 5)
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")

    // Lambda with multiple statements
    val process = { x: Int ->
        val result = x * x
        println("Processed $x to $result")
        result  // Last expression is return value
    }
    process(5)

    // It parameter
    val evens = numbers.filter { it % 2 == 0 }
    println("Evens: $evens")

    // Lambda with explicit parameters
    val multiply = { a: Int, b: Int -> a * b }
    println("Multiply: ${multiply(4, 5)}")

    // Function reference
    val isEven = ::isEvenNumber
    val evenNumbers = numbers.filter(isEven)
    println("Even numbers: $evenNumbers")

    // Lambda as return value
    val multiplier = createMultiplier(3)
    println("Multiplier(5): ${multiplier(5)}")

    // Lambda in collections
    val words = listOf("apple", "banana", "cherry", "date")
    val result = words
        .filter { it.length > 4 }
        .map { it.uppercase() }
        .sorted()
    println("Result: $result")

    // Closure
    var counter = 0
    val increment = { counter++ }
    increment()
    increment()
    println("Counter: $counter")

    // Non-local return (inline)
    val list = listOf(1, 2, 3, 4, 5)
    printWithReturn(list)

    // Destructuring in lambdas
    data class Point(val x: Int, val y: Int)
    val points = listOf(Point(1, 2), Point(3, 4), Point(5, 6))
    points.forEach { (x, y) -> println("Point: ($x, $y)") }

    // Lambda with type parameters
    fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
        val result = mutableListOf<T>()
        for (item in this) {
            if (predicate(item)) result.add(item)
        }
        return result
    }

    val filtered = numbers.customFilter { it > 3 }
    println("Custom filter: $filtered")

    // Inline function with lambda
    val time = measureTime {
        Thread.sleep(100)
    }
    println("Time: ${time}ms")
}

fun isEvenNumber(n: Int): Boolean = n % 2 == 0

fun createMultiplier(factor: Int): (Int) -> Int = { it * factor }

inline fun printWithReturn(list: List<Int>) {
    list.forEach {
        if (it == 3) return  // Non-local return
        print("$it ")
    }
    println()
}

fun measureTime(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}
