fun main() {
    // map transformation
    val numbers = listOf(1, 2, 3, 4, 5)
    val squared = numbers.map { it * it }
    println("Squared: $squared")

    // filter
    val evens = numbers.filter { it % 2 == 0 }
    println("Evens: $evens")

    // reduce
    val sum = numbers.reduce { acc, i -> acc + i }
    println("Sum: $sum")

    // fold
    val product = numbers.fold(1) { acc, i -> acc * i }
    println("Product: $product")

    // chain operations
    val result = numbers
        .filter { it > 2 }
        .map { it * 3 }
        .sorted()
    println("Chained: $result")

    // Function as parameter
    fun applyOperation(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        return operation(a, b)
    }
    println("Add: ${applyOperation(5, 3, { a, b -> a + b })}")
    println("Multiply: ${applyOperation(5, 3) { a, b -> a * b }}")

    // Returning functions
    fun <T, R> List<T>.transform(transformer: (T) -> R): List<R> {
        val result = mutableListOf<R>()
        for (item in this) {
            result.add(transformer(item))
        }
        return result
    }

    val words = listOf("hello", "world", "kotlin")
    val uppercased = words.transform { it.uppercase() }
    println("Uppercased: $uppercased")

    // Currying
    fun add(a: Int): (Int) -> Int = { b -> a + b }
    val addFive = add(5)
    println("Add 5 to 3: ${addFive(3)}")

    // Composition
    fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C = { f(g(it)) }

    val doubleThenSquare = compose<Int, Int, Int>({ it * it }, { it * 2 })
    println("Double then square: ${doubleThenSquare(3)}")

    // Partial application
    fun log(level: String, tag: String, message: String) = println("[$level][$tag] $message")
    val debugLog = { msg: String -> log("DEBUG", "APP", msg) }
    debugLog("Application started")

    // let with transformations
    val length = "Hello, World!".let {
        println("Processing: $it")
        it.length
    }
    println("Length: $length")

    // run with context
    val result2 = StringBuilder().run {
        append("Hello")
        append(", ")
        append("World!")
        toString()
    }
    println("Built: $result2")

    // apply for configuration
    val config = Config().apply {
        host = "localhost"
        port = 8080
        debug = true
    }
    println("Config: $config")

    // also for side effects
    val list = mutableListOf(1, 2, 3).also {
        println("Original: $it")
        it.add(4)
    }.also {
        println("Modified: $it")
    }
    println("Final: $list")

    // Sequence (lazy evaluation)
    val lazyResult = numbers.asSequence()
        .filter { it > 2 }
        .map { it * 2 }
        .toList()
    println("Lazy: $lazyResult")
}

data class Config(
    var host: String = "",
    var port: Int = 0,
    var debug: Boolean = false
)
