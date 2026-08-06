fun main() {
    // Basic extension function
    println("Hello".addExclamation())
    println(42.isEven())

    // Extension property
    println("Hello World".wordCount)
    println(123.digitSum)

    // Extension function with receiver
    fun StringBuilder.appendLine(text: String): StringBuilder {
        appendLine(text)
        return this
    }

    val sb = StringBuilder().apply {
        appendLine("First")
        appendLine("Second")
        appendLine("Third")
    }
    println(sb.toString())

    // Extension on nullable types
    fun String?.isNullOrEmptyOrBlank(): Boolean {
        return this == null || this.isEmpty() || this.isBlank()
    }

    println(null.isNullOrEmptyOrBlank())
    println("".isNullOrEmptyOrBlank())
    println("  ".isNullOrEmptyOrBlank())
    println("Hello".isNullOrEmptyOrBlank())

    // Generic extension function
    fun <T> List<T>.secondOrNull(): T? {
        return if (this.size >= 2) this[1] else null
    }

    val list = listOf(1, 2, 3)
    println("Second: ${list.secondOrNull()}")

    // Extension function in scope
    fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
        val result = mutableListOf<T>()
        for (item in this) {
            if (predicate(item)) result.add(item)
        }
        return result
    }

    val numbers = listOf(1, 2, 3, 4, 5)
    val filtered = numbers.customFilter { it > 2 }
    println("Filtered: $filtered}")

    // Extension on companion object
    class User(val name: String, val email: String) {
        companion object {
            fun create(name: String, email: String): User {
                return User(name, email)
            }
        }
    }

    fun User.Companion.fromEmail(email: String): User {
        val name = email.substringBefore("@")
        return User(name, email)
    }

    val user = User.fromEmail("alice@example.com")
    println("User: ${user.name}")

    // Extension with inline
    inline fun <T> T.applyIf(condition: Boolean, block: T.() -> T): T {
        return if (condition) this.block() else this
    }

    val result = "Hello".applyIf(true) { uppercase() }
    println("ApplyIf: $result")

    // Extension function overriding
    open class Animal(val name: String) {
        open fun speak() = println("$name makes a sound")
    }

    class Dog(name: String) : Animal(name) {
        override fun speak() = println("$name barks")
    }

    fun Animal.describe() = "${this::class.simpleName}: $name"
    fun Dog.describe() = "Dog: $name (loyal)"

    val animal: Animal = Dog("Rex")
    animal.describe()  // Calls Animal version
    (animal as Dog).describe()  // Calls Dog version

    // Scope functions with extensions
    data class Person(var name: String, var age: Int)

    val person = Person("Alice", 30).apply {
        name = "Bob"
        age = 25
    }
    println("Person: $person")

    val description = person.run {
        "$name is $age years old"
    }
    println("Description: $description")

    // Extension on function types
    fun ((Int) -> Int).compose(other: (Int) -> Int): (Int) -> Int {
        return { other(this(it)) }
    }

    val double = { x: Int -> x * 2 }
    val square = { x: Int -> x * x }
    val doubleThenSquare = double.compose(square)
    println("Double then square: ${doubleThenSquare(3)}")
}

fun String.addExclamation(): String = "$this!"
fun Int.isEven(): Boolean = this % 2 == 0
val String.wordCount: Int get() = split(" ").size
val Int.digitSum: Int get() = toString().sumOf { it.digitToInt() }
