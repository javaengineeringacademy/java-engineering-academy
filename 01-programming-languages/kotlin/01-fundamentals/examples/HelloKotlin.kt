fun main() {
    // Variables
    val name = "Kotlin"   // immutable
    var version = 1.9     // mutable
    println("Language: $name, Version: $version")

    // Null safety
    var nullable: String? = "Hello"
    println(nullable?.length)
    nullable = null
    println(nullable?.length ?: 0)  // Elvis operator

    // Data classes
    data class Person(val name: String, val age: Int)
    val p = Person("Alice", 30)
    println("Person: $p")

    // Extension functions
    fun String.addExclamation(): String = "$this!"
    println("Kotlin".addExclamation())

    // Coroutines
    import kotlinx.coroutines.*
    runBlocking {
        launch {
            delay(1000L)
            println("Coroutine!")
        }
    }

    // Lambdas
    val numbers = listOf(1, 2, 3, 4, 5)
    val doubled = numbers.map { it * 2 }
    val sum = numbers.reduce { acc, i -> acc + i }
    println("Doubled: $doubled, Sum: $sum")

    // When expression
    val x = 5
    when (x) {
        1 -> println("One")
        2 -> println("Two")
        in 3..10 -> println("3-10")
        else -> println("Other")
    }
}
