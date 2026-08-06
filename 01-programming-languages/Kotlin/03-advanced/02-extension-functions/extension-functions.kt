// Kotlin Extension Functions

// Basic extension function
fun String.isPalindrome(): Boolean {
    return this == this.reversed()
}

// Extension property
val String.wordCount: Int
    get() = this.split(" ").size

// Extension function with parameter
fun String.repeat(times: Int): String {
    return this.repeat(times)
}

// Nullable extension function
fun String?.orDefault(default: String): String {
    return this ?: default
}

// Extension function on collection
fun <T> List<T>.secondOrNull(): T? {
    return if (this.size >= 2) this[1] else null
}

// Generic extension function
fun <T> T?.println() {
    println(this)
}

// Extension function with receiver
fun StringBuilder.appendLine(text: String): StringBuilder {
    appendLine(text)
    return this
}

// Infix extension function
infix fun Int.pow(exponent: Int): Int {
    var result = 1
    for (i in 1..exponent) {
        result *= this
    }
    return result
}

// Operator extension function
operator fun Int.times(text: String): String {
    return text.repeat(this)
}

// Scope functions as extensions
fun <T> T.alsoPrint(): T {
    println(this)
    return this
}

// Extension in different scopes
class Person(val name: String, val age: Int)

fun Person.isAdult(): Boolean = age >= 18

// Companion object extension
class MathUtils {
    companion object {
        // Empty companion
    }
}

fun MathUtils.Companion.add(a: Int, b: Int): Int = a + b

fun main() {
    // String extensions
    val palindrome = "racecar"
    println("'$palindrome' is palindrome: ${palindrome.isPalindrome()}")
    
    // Extension property
    val sentence = "Hello World Kotlin"
    println("Word count: ${sentence.wordCount}")
    
    // Nullable extension
    val nullStr: String? = null
    println("Default: ${nullStr.orDefault("N/A")}")
    
    // Collection extensions
    val numbers = listOf(1, 2, 3, 4, 5)
    println("Second: ${numbers.secondOrNull()}")
    
    // Generic extension
    "Hello".println()
    42.println()
    
    // StringBuilder extension
    val sb = StringBuilder()
    sb.appendLine("Hello")
    sb.appendLine("World")
    println("Built: ${sb.toString()}")
    
    // Infix extension
    println("2^10 = ${2 pow 10}")
    
    // Operator extension
    println(3 * "Ha")
    
    // Also extension
    "Test".alsoPrint()
    
    // Person extensions
    val person = Person("Alice", 30)
    println("${person.name} is adult: ${person.isAdult()}")
    
    // Companion object extension
    println("Add: ${MathUtils.add(2, 3)}")
    
    // Extension functions in chains
    val result = "Hello World"
        .uppercase()
        .reversed()
        .alsoPrint()
        .length
    
    println("Length: $result")
    
    println("Extension functions example running")
}